package com.ali.user.mobile.app.init;

import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.info.AlipayInfo;
import com.ali.user.mobile.info.AppInfo;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.service.FaceService;
import com.ali.user.mobile.service.FingerprintService;
import com.ali.user.mobile.service.NavigatorService;
import com.ali.user.mobile.service.NumberAuthService;
import com.ali.user.mobile.service.RpcService;
import com.ali.user.mobile.service.SNSBindService;
import com.ali.user.mobile.service.SNSService;
import com.ali.user.mobile.service.ServiceFactory;
import com.ali.user.mobile.service.StorageService;
import com.ali.user.mobile.service.UIService;
import com.ali.user.mobile.service.UserTrackService;

public class AliUserInit {
    private static final String TAG = "login.AliUserSdkInit";

    public static void init() {
        TLogAdapter.e(TAG, "AliUserInit 初始化开始");
        AppInfo.getInstance().init();
        try {
            Class.forName("com.alipay.apmobilesecuritysdk.face.APSecuritySdk");
            AlipayInfo.getInstance().init();
        } catch (Throwable unused) {
        }
        TLogAdapter.d(TAG, "AliUserInit 初始化结束");
        LocalBroadcastManager.getInstance(DataProviderFactory.getApplicationContext()).sendBroadcast(new Intent(AppInfo.INITED_ACTION));
        try {
            if (DataProviderFactory.getDataProvider().getEnvType() == 1) {
                ((RpcService) ServiceFactory.getService(RpcService.class)).sslDegrade();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public static void initSystemService() {
        ServiceFactory.registerService(RpcService.class);
        ServiceFactory.registerService(StorageService.class);
        ServiceFactory.registerService(NavigatorService.class);
        ServiceFactory.registerService(UIService.class);
        ServiceFactory.registerService(FaceService.class);
        ServiceFactory.registerService(SNSService.class);
        ServiceFactory.registerService(FingerprintService.class);
        ServiceFactory.registerService(SNSBindService.class);
        ServiceFactory.registerService(UserTrackService.class);
        ServiceFactory.registerService(NumberAuthService.class);
    }
}
