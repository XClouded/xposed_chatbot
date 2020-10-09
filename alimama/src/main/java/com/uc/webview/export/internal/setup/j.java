package com.uc.webview.export.internal.setup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Looper;
import android.webkit.ValueCallback;
import com.alibaba.wireless.security.SecExceptionCode;
import com.uc.webview.export.extension.ILocationManager;
import com.uc.webview.export.extension.SettingKeys;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.SDKFactory;
import com.uc.webview.export.internal.d;
import com.uc.webview.export.internal.interfaces.UCMobileWebKit;
import com.uc.webview.export.internal.setup.ae;
import com.uc.webview.export.internal.uc.CoreFactory;
import com.uc.webview.export.internal.uc.startup.b;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.utility.Utils;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/* compiled from: U4Source */
public final class j {
    private static AtomicBoolean a = new AtomicBoolean(false);

    public static void a() {
        SDKFactory.c = af.e();
        long d = b.d();
        com.uc.webview.export.internal.uc.startup.a.a();
        b.a((int) SecExceptionCode.SEC_ERROR_STA_DECRYPT_MISMATCH_KEY_DATA, b.d() - d);
        ae a2 = ae.a();
        int i = ae.d.a;
        ae.b bVar = ae.b.SETUP_CORE_FACTORY;
        ae a3 = ae.a();
        a3.getClass();
        a2.a(i, bVar, new ae.a(new k(), (ValueCallback) null), (ValueCallback<Object>) null);
    }

    public static void b() {
        long d = b.d();
        SDKFactory.f(CoreFactory.getCoreType().intValue());
        b.a(308, b.d() - d);
    }

    public static boolean a(Boolean bool) {
        if (bool != null) {
            return bool.booleanValue();
        }
        return (Build.VERSION.SDK_INT >= 14) && Utils.checkSupportSamplerExternalOES();
    }

    public static UCMobileWebKit a(Context context) {
        synchronized (a) {
            if (a.get()) {
                UCMobileWebKit uCMobileWebKit = SDKFactory.d;
                return uCMobileWebKit;
            }
            b.a(231);
            UCMobileWebKit initUCMobileWebKit = CoreFactory.initUCMobileWebKit(context, af.e, false);
            SDKFactory.d = initUCMobileWebKit;
            d.a(initUCMobileWebKit);
            b.a(232);
            a.set(true);
            return SDKFactory.d;
        }
    }

    public static void c() {
        if (af.b && SDKFactory.d != null) {
            SDKFactory.d.setLocationManagerUC(new a(af.a));
        }
    }

    public static void d() {
        Log.i("InitUtil", "initVideoSetting begin");
        Context context = af.a;
        if (af.b) {
            b.a(235);
            boolean z = af.e;
            boolean z2 = true;
            if (e() == -1 || e() != 1) {
                z2 = false;
            }
            if (!z && z2) {
                Log.i("InitUtil", "UC Core not support Hardware accelerated.");
                z2 = false;
            }
            if (Build.VERSION.SDK_INT < 14) {
                if (z2) {
                    Log.i("InitUtil", "Video Hardware accelerated is supported start at api level 14 and now is " + Build.VERSION.SDK_INT);
                }
                CoreFactory.b().setBoolValue(SettingKeys.VideoUseStandardMode, false);
            } else {
                CoreFactory.b().setBoolValue(SettingKeys.VideoUseStandardMode, z2);
                if (z2) {
                    SDKFactory.a((Long) 1048576L);
                }
            }
            SDKFactory.i();
            SDKFactory.a((Map<String, Object>) af.d);
            SDKFactory.d(context);
            SDKFactory.m();
            b.a(236);
        }
        Log.i("InitUtil", "initVideoSetting end");
    }

    private static int e() {
        Integer num = (Integer) com.uc.webview.export.internal.utility.d.a().a(UCCore.OPTION_VIDEO_HARDWARE_ACCELERATED);
        if (num != null) {
            return num.intValue();
        }
        return -1;
    }

    /* compiled from: U4Source */
    static class a implements ILocationManager {
        private LocationManager a;

        public a(Context context) {
            this.a = (LocationManager) context.getSystemService("location");
        }

        @SuppressLint({"MissingPermission"})
        public final void requestLocationUpdates(String str, long j, float f, LocationListener locationListener) {
            if (this.a != null) {
                try {
                    this.a.requestLocationUpdates(str, j, f, locationListener);
                } catch (Throwable unused) {
                    Criteria criteria = new Criteria();
                    if ("gps".equals(str)) {
                        criteria.setAccuracy(1);
                    }
                    this.a.requestLocationUpdates(0, 0.0f, criteria, locationListener, Looper.getMainLooper());
                }
            }
        }

        public final void requestLocationUpdatesWithUrl(String str, long j, float f, LocationListener locationListener, String str2) {
            requestLocationUpdates(str, j, f, locationListener);
        }

        public final void removeUpdates(LocationListener locationListener) {
            if (this.a != null) {
                this.a.removeUpdates(locationListener);
            }
        }
    }
}
