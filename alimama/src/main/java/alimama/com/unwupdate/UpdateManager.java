package alimama.com.unwupdate;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.IAppEnvironment;
import alimama.com.unwbase.interfaces.IRxRequestManager;
import alimama.com.unwbase.interfaces.ISecurity;
import alimama.com.unwbase.interfaces.IUpdateManager;
import alimama.com.unwnetwork.ApiInfo;
import alimama.com.unwnetwork.RxMtopRequest;
import alimama.com.unwnetwork.RxMtopResponse;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.TextUtils;
import com.alimama.union.app.pagerouter.AppPageInfo;
import com.alimamaunion.base.safejson.SafeJSONObject;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

public class UpdateManager extends RxMtopRequest<UpdateResult> implements IUpdateManager<UpdateParams>, RxMtopRequest.RxMtopResult<UpdateResult> {
    private static final ApiInfo API_CHECK_UPDATE = new ApiInfo("mtop.client.mudp.update", "1.0");
    private static final String KEY_IDENTIFIER = "identifier";
    private static final String KEY_NET_STATUS = "netStatus";
    private static final String KEY_UPDATE_TYPES = "updateTypes";
    private static final String KEY_VERSION = "appVersion";
    private static final String kEY_API_LEVEL = "apiLevel";
    private static UpdateManager sInstance;
    private String appVersion;
    private String appkey;
    private Application application;
    private boolean hasChecked;
    private IDialogShow iDialogShow;
    private int iconResId;
    private String ttid;
    private UpdateParams updateParams;

    public void init() {
    }

    public UpdateManager(int i) {
        this(i, (IDialogShow) null);
    }

    public UpdateManager(int i, IDialogShow iDialogShow2) {
        this.hasChecked = false;
        checkIsNull((IRxRequestManager) UNWManager.getInstance().getService(IRxRequestManager.class), true);
        IAppEnvironment iAppEnvironment = (IAppEnvironment) UNWManager.getInstance().getService(IAppEnvironment.class);
        checkIsNull(iAppEnvironment, true);
        ISecurity iSecurity = (ISecurity) UNWManager.getInstance().getService(ISecurity.class);
        checkIsNull(iSecurity, true);
        this.application = UNWManager.getInstance().application;
        this.ttid = iAppEnvironment.getTTid();
        this.iconResId = i;
        this.appVersion = UNWManager.getInstance().getAppVersion();
        this.appkey = iSecurity.getAppkey();
        setApiInfo(API_CHECK_UPDATE);
        this.iDialogShow = iDialogShow2;
        setRxMtopResult(this);
    }

    public String getAppVersion() {
        return this.appVersion;
    }

    public int getIconResId() {
        return this.iconResId;
    }

    public Application getApplication() {
        return this.application;
    }

    public String getAppKey() {
        return this.appkey;
    }

    public String getTtid() {
        return this.ttid;
    }

    public UpdateParams getUpdateParams() {
        if (this.updateParams == null) {
            return new UpdateParams();
        }
        return this.updateParams;
    }

    public void setHasChecked(boolean z) {
        this.hasChecked = z;
    }

    public void checkUpdate(boolean z, UpdateParams updateParams2) {
        if (!this.hasChecked || z) {
            this.updateParams = updateParams2;
            this.hasChecked = true;
            IUpdateManager iUpdateManager = (IUpdateManager) UNWManager.getInstance().getService(IUpdateManager.class);
            if (iUpdateManager != null) {
                UpdateController.getInstance().init(UNWManager.getInstance().application, iUpdateManager.getIconResId());
                buildParams();
                sendRequest();
            }
        }
    }

    private void buildParams() {
        String str = "";
        if (NetworkStatusManager.getInstance() != null) {
            int networkType = NetworkStatusManager.getInstance().getNetworkType();
            if (networkType != 999) {
                switch (networkType) {
                    case 0:
                        str = "1";
                        break;
                    case 1:
                        str = "1";
                        break;
                    case 2:
                        str = "2";
                        break;
                    case 3:
                        str = "4";
                        break;
                }
            } else {
                str = "10";
            }
        } else {
            str = "1";
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add("main");
        appendParam(KEY_UPDATE_TYPES, arrayList.toString());
        IUpdateManager iUpdateManager = (IUpdateManager) UNWManager.getInstance().getService(IUpdateManager.class);
        if (iUpdateManager != null) {
            String str2 = ((UpdateParams) iUpdateManager.getUpdateParams()).groupValue;
            if (TextUtils.isEmpty(str2)) {
                str2 = "";
            }
            appendParam("identifier", str2);
            appendParam(kEY_API_LEVEL, String.valueOf(Build.VERSION.SDK_INT));
            appendParam(KEY_NET_STATUS, str);
            appendParam("appVersion", this.appVersion);
        }
    }

    public UpdateResult decodeResult(SafeJSONObject safeJSONObject) {
        return UpdateResult.parse(safeJSONObject);
    }

    public void result(RxMtopResponse<UpdateResult> rxMtopResponse) {
        if (!rxMtopResponse.isReqSuccess) {
            processResult((UpdateResult) null);
        } else {
            processResult((UpdateResult) rxMtopResponse.result);
        }
    }

    private boolean checkIsNull(Object obj, boolean z) {
        boolean z2 = obj == null;
        if (!z2 || !z) {
            return z2;
        }
        throw new IllegalArgumentException(obj.getClass().getSimpleName() + "should not be null");
    }

    public void processResult(final UpdateResult updateResult) {
        final IUpdateManager iUpdateManager = (IUpdateManager) UNWManager.getInstance().getService(IUpdateManager.class);
        if (iUpdateManager != null) {
            if (updateResult == null || !updateResult.hasUpdate) {
                UpdateIntercept updateIntercept = ((UpdateParams) iUpdateManager.getUpdateParams()).updateIntercept;
                if (updateIntercept != null) {
                    updateIntercept.noUpdateIntercept();
                    return;
                }
                return;
            }
            String str = "";
            if (!TextUtils.isEmpty(((UpdateParams) iUpdateManager.getUpdateParams()).appName)) {
                str = ((UpdateParams) iUpdateManager.getUpdateParams()).appName;
            }
            String str2 = ("发现" + str + "新版本V" + updateResult.pkgVersion + "，立即体验\n\n") + "升级提示：" + updateResult.info;
            WeakReference<Activity> weakReference = ((UpdateParams) iUpdateManager.getUpdateParams()).activityRef;
            if (weakReference != null && !((Activity) weakReference.get()).isFinishing()) {
                AlertDialog.Builder positiveButton = new AlertDialog.Builder((Context) weakReference.get()).setMessage(str2).setPositiveButton("好,升级", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        HashMap hashMap = new HashMap();
                        hashMap.put("isForceUpdate", String.valueOf(((UpdateParams) iUpdateManager.getUpdateParams()).isForceUpdate));
                        UNWManager.getInstance().getLogger().info("UnionUpdate", AppPageInfo.UPDATE, "点击升级按钮", hashMap);
                        if (((UpdateParams) iUpdateManager.getUpdateParams()).updateIntercept != null) {
                            ((UpdateParams) iUpdateManager.getUpdateParams()).updateIntercept.updateIntercept();
                        }
                        UpdateController.getInstance().beginDownLoad(updateResult.pkgUrl, updateResult);
                    }
                });
                positiveButton.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        HashMap hashMap = new HashMap();
                        hashMap.put("isForceUpdate", String.valueOf(((UpdateParams) iUpdateManager.getUpdateParams()).isForceUpdate));
                        UNWManager.getInstance().getLogger().info("UnionUpdate", "cancel", "点击取消按钮", hashMap);
                        if (((UpdateParams) iUpdateManager.getUpdateParams()).updateIntercept != null) {
                            ((UpdateParams) iUpdateManager.getUpdateParams()).updateIntercept.cancelUpdateIntercept();
                        }
                        if (updateResult.forceUpdate || ((UpdateParams) iUpdateManager.getUpdateParams()).isForceUpdate) {
                            iUpdateManager.setHasChecked(false);
                            if (((UpdateParams) iUpdateManager.getUpdateParams()).cancelUpdateListener != null) {
                                ((UpdateParams) iUpdateManager.getUpdateParams()).cancelUpdateListener.cancelUpdate();
                            }
                        }
                    }
                });
                positiveButton.setTitle("更新提示");
                AlertDialog create = positiveButton.create();
                boolean z = false;
                create.setCanceledOnTouchOutside(false);
                create.setCancelable(false);
                create.setOnShowListener(new DialogInterface.OnShowListener() {
                    public void onShow(DialogInterface dialogInterface) {
                        HashMap hashMap = new HashMap();
                        hashMap.put("isForceUpdate", String.valueOf(((UpdateParams) iUpdateManager.getUpdateParams()).isForceUpdate));
                        UNWManager.getInstance().getLogger().info("UnionUpdate", "show", "升级弹窗开始展示", hashMap);
                    }
                });
                if (this.iDialogShow != null) {
                    IDialogShow iDialogShow2 = this.iDialogShow;
                    if (updateResult.forceUpdate || ((UpdateParams) iUpdateManager.getUpdateParams()).isForceUpdate) {
                        z = true;
                    }
                    iDialogShow2.show(z, create);
                    return;
                }
                create.show();
            }
        }
    }
}
