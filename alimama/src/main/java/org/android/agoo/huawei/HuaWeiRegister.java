package org.android.agoo.huawei;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.common.handler.ConnectHandler;
import com.huawei.android.hms.agent.push.handler.GetTokenHandler;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.UtilityImpl;
import com.taobao.agoo.BaseNotifyClickActivity;
import org.android.agoo.common.AgooConstants;

public class HuaWeiRegister {
    private static final String TAG = "HuaWeiRegister";
    public static boolean isChannelRegister = false;

    public static void register(Application application) {
        registerBundle(application, false);
    }

    public static void registerBundle(Application application, boolean z) {
        try {
            isChannelRegister = z;
            if (!isChannelRegister && !UtilityImpl.isMainProcess(application)) {
                ALog.e(TAG, "register not in main process, return", new Object[0]);
            } else if (checkDevice()) {
                HMSAgent.init(application);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    public void run() {
                        ALog.i(HuaWeiRegister.TAG, "register begin", "isChannel", Boolean.valueOf(HuaWeiRegister.isChannelRegister));
                        BaseNotifyClickActivity.addNotifyListener(new HuaweiMsgParseImpl());
                        HMSAgent.connect((Activity) null, new ConnectHandler() {
                            public void onConnect(int i) {
                                ALog.i(HuaWeiRegister.TAG, "connect", "result", Integer.valueOf(i));
                                if (i == 0) {
                                    HuaWeiRegister.getToken();
                                }
                            }
                        });
                    }
                }, 5000);
            } else {
                ALog.e(TAG, "register checkDevice false", new Object[0]);
            }
        } catch (Throwable th) {
            ALog.e(TAG, "register", th, new Object[0]);
        }
    }

    /* access modifiers changed from: private */
    public static void getToken() {
        HMSAgent.Push.getToken(new GetTokenHandler() {
            public void onResult(int i) {
                ALog.i(HuaWeiRegister.TAG, "getToken", "result", Integer.valueOf(i));
            }
        });
    }

    private static boolean checkDevice() {
        return Build.BRAND.equalsIgnoreCase(AgooConstants.MESSAGE_SYSTEM_SOURCE_HUAWEI) || Build.BRAND.equalsIgnoreCase("honor");
    }
}
