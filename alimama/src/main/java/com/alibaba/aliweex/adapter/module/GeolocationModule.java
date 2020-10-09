package com.alibaba.aliweex.adapter.module;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.alibaba.aliweex.adapter.module.location.DefaultLocation;
import com.alibaba.aliweex.adapter.module.location.ILocatable;
import com.alibaba.aliweex.adapter.module.location.LocationFactory;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.Destroyable;
import com.taobao.weex.common.WXModule;
import java.util.HashMap;

public class GeolocationModule extends WXModule implements Destroyable {
    private HandlerThread mGeolocationThread = new HandlerThread("Geolocation");
    private Handler mHandler;
    /* access modifiers changed from: private */
    public ILocatable mILocatable = LocationFactory.getLocationProvider(this.mWXSDKInstance);

    public GeolocationModule() {
        this.mGeolocationThread.start();
        this.mHandler = new Handler(this.mGeolocationThread.getLooper());
    }

    @JSMethod(uiThread = false)
    public void getCurrentPosition(final String str, final String str2, final String str3) {
        if (this.mWXSDKInstance != null && this.mWXSDKInstance.getContext() != null) {
            this.mILocatable.setWXSDKInstance(this.mWXSDKInstance);
            this.mHandler.post(new Runnable() {
                public void run() {
                    if (GeolocationModule.this.checkPermission()) {
                        GeolocationModule.this.mILocatable.getCurrentPosition(str, str2, str3);
                    } else {
                        GeolocationModule.this.requestPermission(str, str2, str3, 18);
                    }
                }
            });
        }
    }

    @JSMethod(uiThread = false)
    public void watchPosition(final String str, final String str2, final String str3) {
        if (this.mWXSDKInstance != null && this.mWXSDKInstance.getContext() != null) {
            this.mILocatable.setWXSDKInstance(this.mWXSDKInstance);
            this.mHandler.post(new Runnable() {
                public void run() {
                    if (GeolocationModule.this.checkPermission()) {
                        GeolocationModule.this.mILocatable.watchPosition(str, str2, str3);
                    } else {
                        GeolocationModule.this.requestPermission(str, str2, str3, 19);
                    }
                }
            });
        }
    }

    @JSMethod(uiThread = false)
    public void clearWatch(final String str) {
        this.mILocatable.setWXSDKInstance(this.mWXSDKInstance);
        this.mHandler.post(new Runnable() {
            public void run() {
                GeolocationModule.this.mILocatable.clearWatch(str);
            }
        });
    }

    public void destroy() {
        this.mILocatable.destroy();
        if (this.mGeolocationThread != null) {
            this.mGeolocationThread.quit();
            this.mGeolocationThread = null;
        }
    }

    /* access modifiers changed from: private */
    public void requestPermission(String str, String str2, String str3, int i) {
        try {
            ActivityCompat.requestPermissions((Activity) this.mWXSDKInstance.getContext(), new String[]{"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"}, i);
            LocalBroadcastManager.getInstance(this.mWXSDKInstance.getContext()).registerReceiver(new PerReceiver(this.mWXSDKInstance.getInstanceId(), this.mILocatable, str, str2, str3), new IntentFilter(WXModule.ACTION_REQUEST_PERMISSIONS_RESULT));
        } catch (Throwable unused) {
        }
    }

    /* access modifiers changed from: private */
    public boolean checkPermission() {
        if (this.mWXSDKInstance != null) {
            return DefaultLocation.checkSelfPermission(this.mWXSDKInstance.getContext());
        }
        return false;
    }

    static class PerReceiver extends BroadcastReceiver {
        String mErrorCallback;
        String mInstanceId;
        ILocatable mLocatable;
        String mParams;
        String mSuccessCallback;

        PerReceiver(String str, ILocatable iLocatable, String str2, String str3, String str4) {
            this.mLocatable = iLocatable;
            this.mSuccessCallback = str2;
            this.mErrorCallback = str3;
            this.mParams = str4;
            this.mInstanceId = str;
        }

        public void onReceive(Context context, Intent intent) {
            int intExtra = intent.getIntExtra("requestCode", 0);
            int[] intArrayExtra = intent.getIntArrayExtra(WXModule.GRANT_RESULTS);
            if (intExtra == 18) {
                if (intArrayExtra.length <= 0 || intArrayExtra[0] != 0) {
                    noPermission();
                } else {
                    this.mLocatable.getCurrentPosition(this.mSuccessCallback, this.mErrorCallback, this.mParams);
                }
            } else if (intExtra == 19) {
                if (intArrayExtra.length <= 0 || intArrayExtra[0] != 0) {
                    noPermission();
                } else {
                    this.mLocatable.watchPosition(this.mSuccessCallback, this.mErrorCallback, this.mParams);
                }
            }
            LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
        }

        private void noPermission() {
            HashMap hashMap = new HashMap();
            hashMap.put("errorCode", 90001);
            hashMap.put(ILocatable.ERROR_MSG, ILocatable.ErrorMsg.NO_PERMISSION_ERROR);
            WXSDKManager.getInstance().callback(this.mInstanceId, this.mErrorCallback, hashMap);
        }
    }
}
