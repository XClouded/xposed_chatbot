package com.taobao.agoo.control;

import android.content.Context;
import android.text.TextUtils;
import com.taobao.accs.base.AccsDataListener;
import com.taobao.accs.base.TaoBaseService;
import com.taobao.accs.client.GlobalClientInfo;
import com.taobao.accs.common.Constants;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.ForeBackManager;
import com.taobao.accs.utl.JsonUtility;
import com.taobao.accs.utl.UtilityImpl;
import com.taobao.agoo.ICallback;
import com.taobao.agoo.IRegister;
import com.taobao.agoo.TaobaoConstants;
import com.taobao.agoo.TaobaoRegister;
import com.taobao.agoo.control.data.AliasDO;
import com.taobao.agoo.control.data.BaseDO;
import com.taobao.agoo.control.data.SwitchDO;
import java.util.HashMap;
import java.util.Map;
import org.android.agoo.common.Config;
import org.json.JSONException;
import org.json.JSONObject;

public class RequestListener implements AccsDataListener {
    private static final String TAG = "RequestListener";
    public static AgooBindCache mAgooBindCache;
    public Map<String, ICallback> mListeners = new HashMap();

    public void onAntiBrush(boolean z, TaoBaseService.ExtraInfo extraInfo) {
    }

    public void onBind(String str, int i, TaoBaseService.ExtraInfo extraInfo) {
    }

    public void onConnected(TaoBaseService.ConnectInfo connectInfo) {
    }

    public void onData(String str, String str2, String str3, byte[] bArr, TaoBaseService.ExtraInfo extraInfo) {
    }

    public void onDisconnected(TaoBaseService.ConnectInfo connectInfo) {
    }

    public void onSendData(String str, String str2, int i, TaoBaseService.ExtraInfo extraInfo) {
    }

    public void onUnbind(String str, int i, TaoBaseService.ExtraInfo extraInfo) {
    }

    public RequestListener(Context context) {
        if (mAgooBindCache == null) {
            mAgooBindCache = new AgooBindCache(context.getApplicationContext());
        }
    }

    public void onResponse(String str, String str2, int i, byte[] bArr, TaoBaseService.ExtraInfo extraInfo) {
        try {
            if (TaobaoConstants.SERVICE_ID_DEVICECMD.equals(str)) {
                ICallback iCallback = this.mListeners.get(str2);
                if (i == 200) {
                    String str3 = new String(bArr, "utf-8");
                    ALog.i(TAG, "RequestListener onResponse", Constants.KEY_DATA_ID, str2, "listener", iCallback, "json", str3);
                    JSONObject jSONObject = new JSONObject(str3);
                    String string = JsonUtility.getString(jSONObject, "resultCode", (String) null);
                    String string2 = JsonUtility.getString(jSONObject, BaseDO.JSON_CMD, (String) null);
                    if (!"success".equals(string)) {
                        if (iCallback != null) {
                            iCallback.onFailure(String.valueOf(string), "agoo server error");
                        }
                        if (TaobaoConstants.SERVICE_ID_DEVICECMD.equals(str)) {
                            this.mListeners.remove(str2);
                            return;
                        }
                        return;
                    } else if ("register".equals(string2)) {
                        String string3 = JsonUtility.getString(jSONObject, "deviceId", (String) null);
                        if (!TextUtils.isEmpty(string3)) {
                            TaobaoRegister.setIsRegisterSuccess(true);
                            ForeBackManager.getManager().reportSaveClickMessage();
                            Config.setDeviceToken(GlobalClientInfo.getContext(), string3);
                            mAgooBindCache.onAgooRegister(GlobalClientInfo.getContext().getPackageName());
                            if (iCallback instanceof IRegister) {
                                UtilityImpl.saveUtdid(Config.PREFERENCES, GlobalClientInfo.getContext());
                                ((IRegister) iCallback).onSuccess(string3);
                            }
                        } else if (iCallback != null) {
                            iCallback.onFailure("", "agoo server error deviceid null");
                        }
                        if (TaobaoConstants.SERVICE_ID_DEVICECMD.equals(str)) {
                            this.mListeners.remove(str2);
                            return;
                        }
                        return;
                    } else if (AliasDO.JSON_CMD_SETALIAS.equals(string2)) {
                        handleSetAlias(jSONObject, iCallback);
                        if (TaobaoConstants.SERVICE_ID_DEVICECMD.equals(str)) {
                            this.mListeners.remove(str2);
                            return;
                        }
                        return;
                    } else if (AliasDO.JSON_CMD_REMOVEALIAS.equals(string2)) {
                        Config.setPushAliasToken(GlobalClientInfo.getContext(), (String) null);
                        if (iCallback != null) {
                            iCallback.onSuccess();
                        }
                        mAgooBindCache.onAgooAliasUnBind();
                        if (TaobaoConstants.SERVICE_ID_DEVICECMD.equals(str)) {
                            this.mListeners.remove(str2);
                            return;
                        }
                        return;
                    } else if ((SwitchDO.JSON_CMD_ENABLEPUSH.equals(string2) || SwitchDO.JSON_CMD_DISABLEPUSH.equals(string2)) && iCallback != null) {
                        iCallback.onSuccess();
                    }
                } else if (iCallback != null) {
                    iCallback.onFailure(String.valueOf(i), "accs channel error");
                }
            }
            if (!TaobaoConstants.SERVICE_ID_DEVICECMD.equals(str)) {
                return;
            }
        } catch (Throwable th) {
            if (TaobaoConstants.SERVICE_ID_DEVICECMD.equals(str)) {
                this.mListeners.remove(str2);
            }
            throw th;
        }
        this.mListeners.remove(str2);
    }

    private void handleSetAlias(JSONObject jSONObject, ICallback iCallback) throws JSONException {
        String string = JsonUtility.getString(jSONObject, AliasDO.JSON_PUSH_USER_TOKEN, (String) null);
        if (!TextUtils.isEmpty(string)) {
            Config.setPushAliasToken(GlobalClientInfo.getContext(), string);
            if (iCallback != null) {
                iCallback.onSuccess();
                mAgooBindCache.onAgooAliasBind(iCallback.extra);
            }
        } else if (iCallback != null) {
            iCallback.onFailure("", "agoo server error-pushtoken null");
        }
    }
}
