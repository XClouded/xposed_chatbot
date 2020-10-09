package android.taobao.windvane.jsbridge.api;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.thread.WVThreadPool;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;

import com.alibaba.aliweex.adapter.module.location.ILocatable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WVLocation extends WVApiPlugin implements LocationListener, Handler.Callback {
    private static final int GPS_TIMEOUT = 15000;
    private static final String TAG = "WVLocation";
    private int MIN_DISTANCE;
    private int MIN_TIME;
    /* access modifiers changed from: private */
    public boolean enableAddress;
    private boolean getLocationSuccess;
    private LocationManager locationManager;
    /* access modifiers changed from: private */
    public ArrayList<WVCallBackContext> mCallbacks;
    /* access modifiers changed from: private */
    public Handler mHandler;

    public WVLocation() {
        this.MIN_TIME = 20000;
        this.MIN_DISTANCE = 30;
        this.mHandler = null;
        this.mCallbacks = new ArrayList<>();
        this.getLocationSuccess = false;
        this.enableAddress = false;
        this.locationManager = null;
        this.mHandler = new Handler(Looper.getMainLooper(), this);
    }

    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        if (!"getLocation".equals(str)) {
            return false;
        }
        getLocation(wVCallBackContext, str2);
        return true;
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void getLocation(final android.taobao.windvane.jsbridge.WVCallBackContext r5, final java.lang.String r6) {
        /*
            r4 = this;
            monitor-enter(r4)
            android.content.Context r0 = r4.mContext     // Catch:{ Exception -> 0x002d, all -> 0x002a }
            r1 = 2
            java.lang.String[] r1 = new java.lang.String[r1]     // Catch:{ Exception -> 0x002d, all -> 0x002a }
            r2 = 0
            java.lang.String r3 = "android.permission.ACCESS_FINE_LOCATION"
            r1[r2] = r3     // Catch:{ Exception -> 0x002d, all -> 0x002a }
            r2 = 1
            java.lang.String r3 = "android.permission.ACCESS_COARSE_LOCATION"
            r1[r2] = r3     // Catch:{ Exception -> 0x002d, all -> 0x002a }
            android.taobao.windvane.runtimepermission.PermissionProposer$PermissionRequestTask r0 = android.taobao.windvane.runtimepermission.PermissionProposer.buildPermissionTask(r0, r1)     // Catch:{ Exception -> 0x002d, all -> 0x002a }
            android.taobao.windvane.jsbridge.api.WVLocation$2 r1 = new android.taobao.windvane.jsbridge.api.WVLocation$2     // Catch:{ Exception -> 0x002d, all -> 0x002a }
            r1.<init>(r5, r6)     // Catch:{ Exception -> 0x002d, all -> 0x002a }
            android.taobao.windvane.runtimepermission.PermissionProposer$PermissionRequestTask r6 = r0.setTaskOnPermissionGranted(r1)     // Catch:{ Exception -> 0x002d, all -> 0x002a }
            android.taobao.windvane.jsbridge.api.WVLocation$1 r0 = new android.taobao.windvane.jsbridge.api.WVLocation$1     // Catch:{ Exception -> 0x002d, all -> 0x002a }
            r0.<init>(r5)     // Catch:{ Exception -> 0x002d, all -> 0x002a }
            android.taobao.windvane.runtimepermission.PermissionProposer$PermissionRequestTask r5 = r6.setTaskOnPermissionDenied(r0)     // Catch:{ Exception -> 0x002d, all -> 0x002a }
            r5.execute()     // Catch:{ Exception -> 0x002d, all -> 0x002a }
            goto L_0x002d
        L_0x002a:
            r5 = move-exception
            monitor-exit(r4)
            throw r5
        L_0x002d:
            monitor-exit(r4)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.jsbridge.api.WVLocation.getLocation(android.taobao.windvane.jsbridge.WVCallBackContext, java.lang.String):void");
    }

    public void requestLocation(WVCallBackContext wVCallBackContext, String str) {
        boolean z;
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                z = jSONObject.optBoolean("enableHighAcuracy");
                this.enableAddress = jSONObject.optBoolean(ILocatable.ADDRESS);
            } catch (JSONException unused) {
                TaoLog.e("WVLocation", "getLocation: param parse to JSON error, param=" + str);
                WVResult wVResult = new WVResult();
                wVResult.setResult("HY_PARAM_ERR");
                wVCallBackContext.error(wVResult);
                return;
            }
        } else {
            z = false;
        }
        if (this.mCallbacks == null) {
            this.mCallbacks = new ArrayList<>();
        }
        this.mCallbacks.add(wVCallBackContext);
        registerLocation(z);
        WVThreadPool.getInstance().execute(new Runnable() {
            public void run() {
                WVLocation.this.mHandler.sendEmptyMessageDelayed(1, 15000);
            }
        });
    }

    private void registerLocation(boolean z) {
        if (this.locationManager == null) {
            this.locationManager = (LocationManager) this.mContext.getSystemService("location");
        }
        try {
            this.getLocationSuccess = false;
            this.locationManager.requestLocationUpdates("network", (long) this.MIN_TIME, (float) this.MIN_DISTANCE, this);
            this.locationManager.requestLocationUpdates("gps", (long) this.MIN_TIME, (float) this.MIN_DISTANCE, this);
            if (TaoLog.getLogStatus()) {
                TaoLog.d("WVLocation", " registerLocation start provider GPS and NETWORK");
            }
        } catch (Exception e) {
            TaoLog.e("WVLocation", "registerLocation error: " + e.getMessage());
        }
    }

    /* access modifiers changed from: private */
    public Address getAddress(double d, double d2) {
        try {
            List<Address> fromLocation = new Geocoder(this.mContext).getFromLocation(d, d2, 1);
            if (fromLocation == null || fromLocation.size() <= 0) {
                return null;
            }
            return fromLocation.get(0);
        } catch (Exception e) {
            TaoLog.e("WVLocation", "getAddress: getFromLocation error. " + e.getMessage());
            return null;
        }
    }

    public void onLocationChanged(Location location) {
        if (TaoLog.getLogStatus()) {
            TaoLog.d("WVLocation", " onLocationChanged. ");
        }
        if (this.locationManager != null) {
            wrapResult(location);
            this.locationManager.removeUpdates(this);
            this.getLocationSuccess = true;
        }
    }

    public void onStatusChanged(String str, int i, Bundle bundle) {
        if (TaoLog.getLogStatus()) {
            TaoLog.d("WVLocation", " onStatusChanged. provider: " + str + ";status: " + i);
        }
    }

    public void onProviderEnabled(String str) {
        if (TaoLog.getLogStatus()) {
            TaoLog.d("WVLocation", " onProviderEnabled. provider: " + str);
        }
    }

    public void onProviderDisabled(String str) {
        if (TaoLog.getLogStatus()) {
            TaoLog.d("WVLocation", " onProviderDisabled. provider: " + str);
        }
    }

    private void wrapResult(final Location location) {
        if (this.mCallbacks == null || this.mCallbacks.isEmpty()) {
            TaoLog.d("WVLocation", "GetLocation wrapResult callbackContext is null");
        } else if (location == null) {
            TaoLog.w("WVLocation", "getLocation: location is null");
            Iterator<WVCallBackContext> it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                it.next().error(new WVResult());
            }
            this.mCallbacks.clear();
        } else {
            AsyncTask.execute(new Runnable() {
                public void run() {
                    WVResult wVResult = new WVResult();
                    JSONObject jSONObject = new JSONObject();
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();
                    try {
                        jSONObject.put("longitude", longitude);
                        jSONObject.put("latitude", latitude);
                        jSONObject.put("altitude", location.getAltitude());
                        jSONObject.put("accuracy", (double) location.getAccuracy());
                        jSONObject.put("heading", (double) location.getBearing());
                        jSONObject.put("speed", (double) location.getSpeed());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    wVResult.addData(ILocatable.COORDS, jSONObject);
                    if (TaoLog.getLogStatus()) {
                        TaoLog.d("WVLocation", " getLocation success. latitude: " + latitude + "; longitude: " + longitude);
                    }
                    if (WVLocation.this.enableAddress) {
                        Address access$200 = WVLocation.this.getAddress(latitude, longitude);
                        JSONObject jSONObject2 = new JSONObject();
                        if (access$200 != null) {
                            try {
                                jSONObject2.put("country", access$200.getCountryName());
                                jSONObject2.put("province", access$200.getAdminArea());
                                jSONObject2.put("city", access$200.getLocality());
                                jSONObject2.put("cityCode", access$200.getPostalCode());
                                jSONObject2.put("area", access$200.getSubLocality());
                                jSONObject2.put("road", access$200.getThoroughfare());
                                StringBuilder sb = new StringBuilder();
                                for (int i = 1; i <= 2; i++) {
                                    if (!TextUtils.isEmpty(access$200.getAddressLine(i))) {
                                        sb.append(access$200.getAddressLine(i));
                                    }
                                }
                                jSONObject2.put("addressLine", sb.toString());
                                if (TaoLog.getLogStatus()) {
                                    TaoLog.d("WVLocation", " getAddress success. " + access$200.getAddressLine(0));
                                }
                            } catch (JSONException e2) {
                                e2.printStackTrace();
                            }
                        } else if (TaoLog.getLogStatus()) {
                            TaoLog.w("WVLocation", " getAddress fail. ");
                        }
                        wVResult.addData(ILocatable.ADDRESS, jSONObject2);
                    }
                    try {
                        Iterator it = WVLocation.this.mCallbacks.iterator();
                        while (it.hasNext()) {
                            ((WVCallBackContext) it.next()).success(wVResult);
                        }
                        WVLocation.this.mCallbacks.clear();
                        if (TaoLog.getLogStatus()) {
                            TaoLog.d("WVLocation", "callback success. retString: " + wVResult.toJsonString());
                        }
                    } catch (Throwable unused) {
                    }
                }
            });
        }
    }

    public void onDestroy() {
        if (this.locationManager != null) {
            if (!this.getLocationSuccess) {
                try {
                    this.locationManager.removeUpdates(this);
                } catch (Exception unused) {
                }
            }
            this.locationManager = null;
        }
        if (this.mCallbacks != null) {
            this.mCallbacks.clear();
        }
    }

    public boolean handleMessage(Message message) {
        if (message.what != 1) {
            return false;
        }
        if (this.locationManager != null) {
            try {
                this.locationManager.removeUpdates(this);
                if (this.mCallbacks != null) {
                    if (!this.mCallbacks.isEmpty()) {
                        if (!this.getLocationSuccess) {
                            Iterator<WVCallBackContext> it = this.mCallbacks.iterator();
                            while (it.hasNext()) {
                                it.next().error(new WVResult());
                            }
                            this.mCallbacks.clear();
                        }
                    }
                }
                TaoLog.d("WVLocation", "GetLocation wrapResult callbackContext is null");
                return false;
            } catch (Exception e) {
                TaoLog.e("WVLocation", "GetLocation timeout" + e.getMessage());
                Iterator<WVCallBackContext> it2 = this.mCallbacks.iterator();
                while (it2.hasNext()) {
                    it2.next().error(new WVResult());
                }
                this.mCallbacks.clear();
            }
        }
        return true;
    }
}
