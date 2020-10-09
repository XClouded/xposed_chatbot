package alimama.com.unweventparse.popup;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.callback.UNWEventTaskCompletionBlock;
import alimama.com.unwbase.interfaces.IEtaoLogger;
import alimama.com.unwbase.interfaces.IOrange;
import alimama.com.unwbase.interfaces.IRouter;
import alimama.com.unwbase.tools.ConvertUtils;
import alimama.com.unwbase.tools.UNWLog;
import alimama.com.unweventparse.ResourceFactory;
import alimama.com.unweventparse.interfaces.BaseExecr;
import alimama.com.unwlottiedialog.LottieData;
import alimama.com.unwlottiedialog.LottieDialogCallback;
import alimama.com.unwlottiedialog.UNWLottieDialog;
import alimama.com.unwviewbase.marketController.UNWDialogController;
import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;

public class PopUpExecer extends BaseExecr {
    public static final String H5TYPE = "h5Weex";
    public static final String IMGTYPE = "img";
    public static final String LOTTIETYPE = "lottie";
    private static final String TAG = "_PopUpExecer";
    private UNWEventTaskCompletionBlock callBack;
    private JSONObject object;

    public void exec(Uri uri) {
        exec(uri, (UNWEventTaskCompletionBlock) null);
    }

    public void exec(JSONObject jSONObject, UNWEventTaskCompletionBlock uNWEventTaskCompletionBlock) {
        super.exec(jSONObject, uNWEventTaskCompletionBlock);
        realExec((DialogData) ResourceFactory.getInstance().create(ResourceFactory.TYPE_POPUP, jSONObject));
    }

    public void exec(Uri uri, UNWEventTaskCompletionBlock uNWEventTaskCompletionBlock) {
        realExec((DialogData) ResourceFactory.getInstance().create(ResourceFactory.TYPE_POPUP, uri));
    }

    public void exec(JSONObject jSONObject) {
        exec(jSONObject, (UNWEventTaskCompletionBlock) null);
    }

    private void log(String str) {
        UNWLog.error(TAG, str);
        IEtaoLogger logger = UNWManager.getInstance().getLogger();
        if (logger != null) {
            logger.info(TAG, TAG, str);
        }
    }

    /* JADX WARNING: type inference failed for: r2v1, types: [alimama.com.unwbase.interfaces.IResourceManager, java.lang.Object, alimama.com.unwlottiedialog.UNWLottieDialog] */
    private void realExec(DialogData dialogData) {
        IRouter iRouter;
        if (dialogData != null && (iRouter = (IRouter) UNWManager.getInstance().getService(IRouter.class)) != null) {
            Activity currentActivity = iRouter.getCurrentActivity();
            IOrange iOrange = (IOrange) UNWManager.getInstance().getService(IOrange.class);
            if (currentActivity != null) {
                ? uNWLottieDialog = new UNWLottieDialog(currentActivity, convertLottieDate(dialogData), getCallBacK(dialogData));
                uNWLottieDialog.fatigueTime = 0;
                int safeIntValue = ConvertUtils.getSafeIntValue(dialogData.priority);
                if (safeIntValue <= 0) {
                    uNWLottieDialog.priority = 50;
                } else {
                    uNWLottieDialog.priority = safeIntValue;
                }
                if (!TextUtils.isEmpty(dialogData.resKey) && iOrange != null) {
                    String config = iOrange.getConfig("DialogResource", dialogData.resKey, "");
                    if (!TextUtils.isEmpty(config)) {
                        try {
                            JSONArray parseArray = JSONArray.parseArray(config);
                            if (parseArray != null && parseArray.size() > 0) {
                                ArrayList arrayList = new ArrayList();
                                for (int i = 0; i < parseArray.size(); i++) {
                                    arrayList.add(parseArray.getString(i));
                                }
                                uNWLottieDialog.whitePageName = arrayList;
                            }
                        } catch (Throwable th) {
                            UNWManager.getInstance().getLogger().error(TAG, TAG, th.getLocalizedMessage());
                        }
                    }
                }
                uNWLottieDialog.type = ResourceFactory.TYPE_POPUP;
                handleCallBack(uNWLottieDialog, "");
                UNWDialogController.getInstance().commit(uNWLottieDialog);
            }
        }
    }

    private LottieDialogCallback getCallBacK(final DialogData dialogData) {
        return new LottieDialogCallback() {
            public boolean clickClose() {
                dialogData.click("close");
                return true;
            }

            public boolean clickContent() {
                dialogData.click();
                IRouter iRouter = (IRouter) UNWManager.getInstance().getService(IRouter.class);
                if (iRouter == null) {
                    return true;
                }
                iRouter.gotoPage(dialogData.url);
                return true;
            }

            public boolean clickBg() {
                dialogData.click("bg");
                IRouter iRouter = (IRouter) UNWManager.getInstance().getService(IRouter.class);
                if (iRouter == null) {
                    return true;
                }
                iRouter.gotoPage(dialogData.bgUrl);
                return true;
            }

            public boolean startShow() {
                dialogData.exposeUt();
                return true;
            }
        };
    }

    private LottieData convertLottieDate(DialogData dialogData) {
        LottieData lottieData = new LottieData();
        lottieData.width = ConvertUtils.getSafeIntValue(dialogData.width);
        lottieData.height = ConvertUtils.getSafeIntValue(dialogData.height);
        lottieData.url = dialogData.url;
        lottieData.bg_url = dialogData.bgUrl;
        if (TextUtils.equals(dialogData.assetType, "img")) {
            lottieData.img = dialogData.img;
            UNWLog.error(TAG, "data.img=" + dialogData.img);
        } else if (TextUtils.equals(dialogData.assetType, LOTTIETYPE)) {
            lottieData.lottie_url = dialogData.lottie;
            UNWLog.error(TAG, " data.lottie=" + dialogData.lottie);
        } else if (TextUtils.equals(dialogData.assetType, H5TYPE)) {
            lottieData.other_url = dialogData.h5WeexUrl;
            UNWLog.error(TAG, " data.h5WeexUrl=" + dialogData.h5WeexUrl);
        }
        lottieData.isShowCloseBtn = true;
        return lottieData;
    }
}
