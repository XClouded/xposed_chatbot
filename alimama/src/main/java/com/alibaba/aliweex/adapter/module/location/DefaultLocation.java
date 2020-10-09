package com.alibaba.aliweex.adapter.module.location;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.taobao.windvane.thread.WVThreadPool;
import android.text.TextUtils;
import androidx.core.app.ActivityCompat;
import com.alibaba.aliweex.adapter.module.location.ILocatable;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.bridge.SimpleJSCallback;
import com.taobao.weex.utils.WXLogUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;

public class DefaultLocation implements ILocatable {
    private static final int GPS_TIMEOUT = 15000;
    private static final String TAG = "DefaultLocation";
    private static final int TIME_OUT_WHAT = 17;
    private int MIN_DISTANCE = 5;
    private int MIN_TIME = 20000;
    private LocationManager mLocationManager;
    private Map<String, WXLocationListener> mRegisterSucCallbacks = new HashMap();
    private List<WXLocationListener> mWXLocationListeners = new ArrayList();
    private WXSDKInstance mWXSDKInstance;

    public DefaultLocation(WXSDKInstance wXSDKInstance) {
        this.mWXSDKInstance = wXSDKInstance;
    }

    public void setWXSDKInstance(WXSDKInstance wXSDKInstance) {
        this.mWXSDKInstance = wXSDKInstance;
    }

    public void getCurrentPosition(String str, String str2, String str3) {
        WXLogUtils.d(TAG, "into--[getCurrentPosition] successCallback:" + str + " \nerrorCallback:" + str2 + " \nparams:" + str3);
        if (!TextUtils.isEmpty(str3)) {
            try {
                JSONObject jSONObject = new JSONObject(str3);
                WXLocationListener findLocation = findLocation((String) null, str, str2, jSONObject.optBoolean("enableHighAccuracy"), jSONObject.optBoolean(ILocatable.ADDRESS));
                if (findLocation != null) {
                    this.mWXLocationListeners.add(findLocation);
                    return;
                }
                return;
            } catch (JSONException e) {
                WXLogUtils.e(TAG, (Throwable) e);
            }
        }
        HashMap hashMap = new HashMap();
        hashMap.put("errorCode", 90002);
        hashMap.put(ILocatable.ERROR_MSG, ILocatable.ErrorMsg.PARAMS_ERROR);
        if (this.mWXSDKInstance != null) {
            new SimpleJSCallback(this.mWXSDKInstance.getInstanceId(), str2).invoke(hashMap);
        }
    }

    private WXLocationListener findLocation(String str, String str2, String str3, boolean z, boolean z2) {
        WXLogUtils.d(TAG, "into--[findLocation] mWatchId:" + str + "\nsuccessCallback:" + str2 + "\nerrorCallback:" + str3 + "\nenableHighAccuracy:" + z + "\nmEnableAddress:" + z2);
        if (this.mLocationManager == null) {
            if (this.mWXSDKInstance == null || this.mWXSDKInstance.getContext() == null) {
                return null;
            }
            this.mLocationManager = (LocationManager) this.mWXSDKInstance.getContext().getSystemService("location");
        }
        Criteria criteria = new Criteria();
        if (z) {
            criteria.setAccuracy(2);
        }
        if (this.mWXSDKInstance == null || !checkSelfPermission(this.mWXSDKInstance.getContext())) {
            HashMap hashMap = new HashMap();
            hashMap.put("errorCode", 90001);
            hashMap.put(ILocatable.ERROR_MSG, ILocatable.ErrorMsg.NO_PERMISSION_ERROR);
            if (this.mWXSDKInstance != null) {
                new SimpleJSCallback(this.mWXSDKInstance.getInstanceId(), str3).invoke(hashMap);
            }
            return null;
        }
        WXLocationListener wXLocationListener = new WXLocationListener(this.mLocationManager, this.mWXSDKInstance, str, str2, str3, z2);
        try {
            if (this.mLocationManager.getAllProviders() != null && this.mLocationManager.getAllProviders().contains("gps")) {
                this.mLocationManager.requestLocationUpdates("gps", (long) this.MIN_TIME, (float) this.MIN_DISTANCE, wXLocationListener);
            }
            if (this.mLocationManager.getAllProviders() == null || !this.mLocationManager.getAllProviders().contains("network")) {
                return wXLocationListener;
            }
            this.mLocationManager.requestLocationUpdates("network", (long) this.MIN_TIME, (float) this.MIN_DISTANCE, wXLocationListener);
            return wXLocationListener;
        } catch (Exception e) {
            HashMap hashMap2 = new HashMap();
            hashMap2.put("errorCode", Integer.valueOf(ILocatable.ErrorCode.LOCATION_ERROR));
            hashMap2.put(ILocatable.ERROR_MSG, ILocatable.ErrorMsg.LOCATION_ERROR);
            if (this.mWXSDKInstance != null) {
                new SimpleJSCallback(this.mWXSDKInstance.getInstanceId(), str3).invoke(hashMap2);
            }
            WXLogUtils.e(TAG, WXLogUtils.getStackTrace(e));
            return null;
        }
    }

    public void watchPosition(String str, String str2, String str3) {
        WXLogUtils.d("into--[watchPosition] successCallback:" + str + " errorCallback:" + str2 + "\nparams:" + str3);
        if (!TextUtils.isEmpty(str3)) {
            try {
                JSONObject jSONObject = new JSONObject(str3);
                boolean optBoolean = jSONObject.optBoolean("enableHighAccuracy");
                boolean optBoolean2 = jSONObject.optBoolean(ILocatable.ADDRESS);
                String uuid = UUID.randomUUID().toString();
                WXLocationListener findLocation = findLocation(uuid, str, str2, optBoolean, optBoolean2);
                if (findLocation != null) {
                    this.mRegisterSucCallbacks.put(uuid, findLocation);
                    return;
                }
                return;
            } catch (JSONException e) {
                WXLogUtils.e(TAG, (Throwable) e);
            }
        }
        HashMap hashMap = new HashMap();
        hashMap.put("errorCode", 90002);
        hashMap.put(ILocatable.ERROR_MSG, ILocatable.ErrorMsg.PARAMS_ERROR);
        if (this.mWXSDKInstance != null) {
            new SimpleJSCallback(this.mWXSDKInstance.getInstanceId(), str2).invoke(hashMap);
        }
    }

    public void clearWatch(String str) {
        WXLogUtils.d("into--[clearWatch] mWatchId:" + str);
        if (this.mWXSDKInstance != null && !this.mWXSDKInstance.isDestroy() && this.mLocationManager != null && checkSelfPermission(this.mWXSDKInstance.getContext())) {
            WXLocationListener wXLocationListener = this.mRegisterSucCallbacks.get(str);
            if (wXLocationListener != null) {
                wXLocationListener.destroy();
                this.mLocationManager.removeUpdates(wXLocationListener);
            }
            this.mRegisterSucCallbacks.remove(str);
        }
    }

    public void destroy() {
        WXLogUtils.d("into--[destroy]");
        if (this.mWXSDKInstance != null && !this.mWXSDKInstance.isDestroy() && this.mLocationManager != null) {
            if (this.mWXLocationListeners != null && this.mWXLocationListeners.size() > 0 && checkSelfPermission(this.mWXSDKInstance.getContext())) {
                for (WXLocationListener next : this.mWXLocationListeners) {
                    if (next != null) {
                        next.destroy();
                        this.mLocationManager.removeUpdates(next);
                    }
                }
                this.mWXLocationListeners.clear();
            }
            if (this.mRegisterSucCallbacks != null && this.mRegisterSucCallbacks.size() > 0) {
                Collection<WXLocationListener> values = this.mRegisterSucCallbacks.values();
                if (checkSelfPermission(this.mWXSDKInstance.getContext())) {
                    for (WXLocationListener next2 : values) {
                        next2.destroy();
                        this.mLocationManager.removeUpdates(next2);
                    }
                    this.mRegisterSucCallbacks.clear();
                }
            }
        }
    }

    static class WXLocationListener implements LocationListener, Handler.Callback {
        private Context mContext;
        private boolean mEnableAddress;
        private SimpleJSCallback mErrorCallback;
        /* access modifiers changed from: private */
        public Handler mHandler;
        private LocationManager mLocationManager;
        private SimpleJSCallback mSucCallback;
        private String mWatchId;

        private WXLocationListener(LocationManager locationManager, WXSDKInstance wXSDKInstance, String str, String str2, String str3, boolean z) {
            this.mContext = null;
            this.mSucCallback = null;
            this.mErrorCallback = null;
            this.mWatchId = str;
            if (wXSDKInstance != null) {
                this.mSucCallback = new SimpleJSCallback(wXSDKInstance.getInstanceId(), str2);
                this.mErrorCallback = new SimpleJSCallback(wXSDKInstance.getInstanceId(), str3);
                this.mContext = wXSDKInstance.getContext();
            }
            this.mEnableAddress = z;
            this.mHandler = new Handler(this);
            this.mLocationManager = locationManager;
            WVThreadPool.getInstance().execute(new Runnable() {
                public void run() {
                    WXLocationListener.this.mHandler.sendEmptyMessageDelayed(17, 15000);
                }
            });
        }

        public void onLocationChanged(Location location) {
            this.mHandler.removeMessages(17);
            StringBuilder sb = new StringBuilder();
            sb.append("into--[onLocationChanged] location:");
            sb.append(location);
            WXLogUtils.d(DefaultLocation.TAG, sb.toString() == null ? "Location is NULL!" : location.toString());
            if (location != null) {
                HashMap hashMap = new HashMap();
                HashMap hashMap2 = new HashMap();
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                hashMap2.put("longitude", Double.valueOf(longitude));
                hashMap2.put("latitude", Double.valueOf(latitude));
                hashMap2.put("altitude", Double.valueOf(location.getAltitude()));
                hashMap2.put("accuracy", Float.valueOf(location.getAccuracy()));
                hashMap2.put("heading", Float.valueOf(location.getBearing()));
                hashMap2.put("speed", Float.valueOf(location.getSpeed()));
                hashMap.put(ILocatable.COORDS, hashMap2);
                if (this.mEnableAddress) {
                    Address address = getAddress(latitude, longitude);
                    HashMap hashMap3 = new HashMap();
                    if (address != null) {
                        hashMap3.put("country", address.getCountryName());
                        hashMap3.put("province", address.getAdminArea());
                        hashMap3.put("city", address.getLocality());
                        hashMap3.put("cityCode", address.getPostalCode());
                        hashMap3.put("area", address.getSubLocality());
                        hashMap3.put("road", address.getThoroughfare());
                        StringBuilder sb2 = new StringBuilder();
                        for (int i = 1; i <= 2; i++) {
                            if (!TextUtils.isEmpty(address.getAddressLine(i))) {
                                sb2.append(address.getAddressLine(i));
                            }
                        }
                        hashMap3.put("addressLine", sb2.toString());
                    }
                    hashMap.put(ILocatable.ADDRESS, hashMap3);
                }
                hashMap.put("errorCode", Integer.valueOf(ILocatable.ErrorCode.SUCCESS));
                hashMap.put(ILocatable.ERROR_MSG, "SUCCESS");
                if (!TextUtils.isEmpty(this.mWatchId)) {
                    hashMap.put(ILocatable.WATCH_ID, this.mWatchId);
                }
                if (this.mSucCallback != null) {
                    if (!TextUtils.isEmpty(this.mWatchId)) {
                        this.mSucCallback.invokeAndKeepAlive(hashMap);
                    } else {
                        this.mSucCallback.invoke(hashMap);
                    }
                }
            } else {
                HashMap hashMap4 = new HashMap();
                hashMap4.put("errorCode", Integer.valueOf(ILocatable.ErrorCode.LOCATION_ERROR));
                hashMap4.put(ILocatable.ERROR_MSG, ILocatable.ErrorMsg.LOCATION_ERROR);
                if (!TextUtils.isEmpty(this.mWatchId)) {
                    hashMap4.put(ILocatable.WATCH_ID, this.mWatchId);
                }
                if (this.mErrorCallback != null) {
                    if (true ^ TextUtils.isEmpty(this.mWatchId)) {
                        this.mErrorCallback.invokeAndKeepAlive(hashMap4);
                    } else {
                        this.mErrorCallback.invoke(hashMap4);
                    }
                }
            }
            if (TextUtils.isEmpty(this.mWatchId) && this.mContext != null && DefaultLocation.checkSelfPermission(this.mContext)) {
                this.mLocationManager.removeUpdates(this);
            }
        }

        public void onStatusChanged(String str, int i, Bundle bundle) {
            WXLogUtils.i(DefaultLocation.TAG, "into--[onStatusChanged] provider:" + str + " status:" + i);
        }

        public void onProviderEnabled(String str) {
            WXLogUtils.i(DefaultLocation.TAG, "into--[onProviderEnabled] provider:" + str);
        }

        public void onProviderDisabled(String str) {
            WXLogUtils.i(DefaultLocation.TAG, "into--[onProviderDisabled] provider:" + str);
        }

        public boolean handleMessage(Message message) {
            try {
                if (message.what == 17) {
                    WXLogUtils.d(DefaultLocation.TAG, "into--[handleMessage] Location Time Out!");
                    if (this.mContext != null) {
                        if (this.mLocationManager != null) {
                            if (DefaultLocation.checkSelfPermission(this.mContext)) {
                                this.mLocationManager.removeUpdates(this);
                            }
                            HashMap hashMap = new HashMap();
                            hashMap.put("errorCode", Integer.valueOf(ILocatable.ErrorCode.LOCATION_TIME_OUT));
                            hashMap.put(ILocatable.ERROR_MSG, ILocatable.ErrorMsg.LOCATION_TIME_OUT);
                            if (!TextUtils.isEmpty(this.mWatchId)) {
                                hashMap.put(ILocatable.WATCH_ID, this.mWatchId);
                            }
                            if (this.mErrorCallback == null) {
                                return true;
                            }
                            this.mErrorCallback.invoke(hashMap);
                            return true;
                        }
                    }
                    return false;
                }
            } catch (Throwable unused) {
            }
            return false;
        }

        private Address getAddress(double d, double d2) {
            List<Address> fromLocation;
            WXLogUtils.d(DefaultLocation.TAG, "into--[getAddress] latitude:" + d + " longitude:" + d2);
            try {
                if (!(this.mContext == null || (fromLocation = new Geocoder(this.mContext).getFromLocation(d, d2, 1)) == null || fromLocation.size() <= 0)) {
                    return fromLocation.get(0);
                }
            } catch (Exception e) {
                WXLogUtils.e(DefaultLocation.TAG, (Throwable) e);
            }
            return null;
        }

        public void destroy() {
            if (this.mHandler != null) {
                this.mHandler.removeMessages(17);
                this.mContext = null;
            }
        }
    }

    public static boolean checkSelfPermission(Context context) {
        if (context != null) {
            try {
                return ActivityCompat.checkSelfPermission(context, "android.permission.ACCESS_FINE_LOCATION") == 0 && ActivityCompat.checkSelfPermission(context, "android.permission.ACCESS_COARSE_LOCATION") == 0;
            } catch (Throwable unused) {
            }
        }
        return false;
    }
}
