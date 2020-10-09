package com.alibaba.aliweex.adapter.module;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.alibaba.aliweex.adapter.module.calendar.CalendarManager;
import com.alibaba.aliweex.adapter.module.calendar.DateUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.coloros.mcssdk.mode.Message;
import com.taobao.alivfsadapter.MonitorCacheEvent;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.utils.WXLogUtils;
import java.util.ArrayList;
import java.util.Calendar;

public class WXCalendarModule extends WXModule {
    static final int REQUEST_CALENDAR_PERMISSION_CODE = 25;
    public static final String TAG = "WXCalendarModule";

    public interface PermissionCallback {
        void onPermissionsDenied(String str);

        void onPermissionsGranted();
    }

    @JSMethod
    public void addEvent(final JSONObject jSONObject, final JSCallback jSCallback, final JSCallback jSCallback2) {
        requestPermission(new PermissionCallback() {
            public void onPermissionsGranted() {
                if (jSONObject.containsKey("batch")) {
                    JSONArray jSONArray = jSONObject.getJSONArray("batch");
                    for (int i = 0; i < jSONArray.size(); i++) {
                        boolean unused = WXCalendarModule.this.addSingleEvent(jSONArray.getJSONObject(i));
                    }
                    jSCallback.invoke((Object) null);
                    return;
                }
                boolean unused2 = WXCalendarModule.this.addSingleEvent(jSONObject);
                jSCallback.invoke((Object) null);
            }

            public void onPermissionsDenied(String str) {
                JSCallback jSCallback = jSCallback2;
                WXCalendarModule wXCalendarModule = WXCalendarModule.this;
                jSCallback.invoke(wXCalendarModule.buildError("no permission:" + str));
            }
        }, "android.permission.READ_CALENDAR", "android.permission.WRITE_CALENDAR");
    }

    /* access modifiers changed from: private */
    public boolean addSingleEvent(JSONObject jSONObject) {
        try {
            String string = jSONObject.getString("title");
            String string2 = jSONObject.getString("note");
            String string3 = jSONObject.getString(Message.START_DATE);
            String string4 = jSONObject.getString(Message.END_DATE);
            int intValue = jSONObject.getIntValue("timeOffset");
            Calendar instance = Calendar.getInstance();
            instance.setTime(DateUtils.parseDate(string3));
            Calendar instance2 = Calendar.getInstance();
            instance2.setTime(DateUtils.parseDate(string4));
            if (CalendarManager.addEvent(this.mWXSDKInstance.getContext(), string, string2, instance, instance2, intValue / 60)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            WXLogUtils.e(TAG, (Throwable) e);
            return false;
        }
    }

    @JSMethod
    public void checkEvent(final JSONObject jSONObject, final JSCallback jSCallback, final JSCallback jSCallback2) {
        requestPermission(new PermissionCallback() {
            public void onPermissionsGranted() {
                if (jSONObject.containsKey("batch")) {
                    JSONArray jSONArray = new JSONArray();
                    JSONArray jSONArray2 = jSONObject.getJSONArray("batch");
                    for (int i = 0; i < jSONArray2.size(); i++) {
                        jSONArray.add(Boolean.valueOf(WXCalendarModule.this.checkSingleEvent(jSONArray2.getJSONObject(i))));
                    }
                    jSCallback.invoke(jSONArray);
                } else if (WXCalendarModule.this.checkSingleEvent(jSONObject)) {
                    jSCallback.invoke(Boolean.TRUE);
                } else {
                    jSCallback.invoke(Boolean.FALSE);
                }
            }

            public void onPermissionsDenied(String str) {
                JSCallback jSCallback = jSCallback2;
                WXCalendarModule wXCalendarModule = WXCalendarModule.this;
                jSCallback.invoke(wXCalendarModule.buildError("no permission:" + str));
            }
        }, "android.permission.READ_CALENDAR");
    }

    /* access modifiers changed from: private */
    public boolean checkSingleEvent(JSONObject jSONObject) {
        try {
            String string = jSONObject.getString("title");
            String string2 = jSONObject.getString(Message.START_DATE);
            String string3 = jSONObject.getString(Message.END_DATE);
            Calendar instance = Calendar.getInstance();
            instance.setTime(DateUtils.parseDate(string2));
            Calendar instance2 = Calendar.getInstance();
            instance2.setTime(DateUtils.parseDate(string3));
            if (CalendarManager.checkEvent(this.mWXSDKInstance.getContext(), string, "", instance, instance2)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            WXLogUtils.e(TAG, (Throwable) e);
            return false;
        }
    }

    @JSMethod
    public void removeEvent(final JSONObject jSONObject, final JSCallback jSCallback, final JSCallback jSCallback2) {
        requestPermission(new PermissionCallback() {
            public void onPermissionsGranted() {
                if (jSONObject.containsKey("batch")) {
                    JSONArray jSONArray = jSONObject.getJSONArray("batch");
                    for (int i = 0; i < jSONArray.size(); i++) {
                        boolean unused = WXCalendarModule.this.removeSingleEvent(jSONArray.getJSONObject(i));
                    }
                    jSCallback.invoke((Object) null);
                    return;
                }
                boolean unused2 = WXCalendarModule.this.removeSingleEvent(jSONObject);
                jSCallback.invoke((Object) null);
            }

            public void onPermissionsDenied(String str) {
                JSCallback jSCallback = jSCallback2;
                WXCalendarModule wXCalendarModule = WXCalendarModule.this;
                jSCallback.invoke(wXCalendarModule.buildError("no permission: " + str));
            }
        }, "android.permission.READ_CALENDAR", "android.permission.WRITE_CALENDAR");
    }

    /* access modifiers changed from: private */
    public boolean removeSingleEvent(JSONObject jSONObject) {
        try {
            String string = jSONObject.getString("title");
            String string2 = jSONObject.getString(Message.START_DATE);
            String string3 = jSONObject.getString(Message.END_DATE);
            Calendar instance = Calendar.getInstance();
            instance.setTime(DateUtils.parseDate(string2));
            Calendar instance2 = Calendar.getInstance();
            instance2.setTime(DateUtils.parseDate(string3));
            return CalendarManager.delEvent(this.mWXSDKInstance.getContext(), string, instance, instance2);
        } catch (Exception e) {
            WXLogUtils.e(TAG, (Throwable) e);
            return false;
        }
    }

    /* access modifiers changed from: private */
    public JSONObject buildError(String str) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("result", (Object) "WX_FAILED");
        jSONObject.put("message", (Object) str);
        return jSONObject;
    }

    private void requestPermission(PermissionCallback permissionCallback, String... strArr) {
        try {
            ActivityCompat.requestPermissions((Activity) this.mWXSDKInstance.getContext(), strArr, 25);
            LocalBroadcastManager.getInstance(this.mWXSDKInstance.getContext()).registerReceiver(new PerReceiver(permissionCallback), new IntentFilter(WXModule.ACTION_REQUEST_PERMISSIONS_RESULT));
        } catch (Throwable th) {
            WXLogUtils.e(TAG, th);
        }
    }

    @JSMethod
    public void checkPermission(JSONObject jSONObject, JSCallback jSCallback, JSCallback jSCallback2) {
        try {
            Context context = this.mWXSDKInstance.getContext();
            ArrayList<String> arrayList = new ArrayList<>();
            if (jSONObject == null || jSONObject.getJSONArray(WXModule.PERMISSIONS) == null) {
                arrayList.add("android.permission.READ_CALENDAR");
                arrayList.add("android.permission.WRITE_CALENDAR");
            } else {
                JSONArray jSONArray = jSONObject.getJSONArray(WXModule.PERMISSIONS);
                if (jSONArray.size() == 0) {
                    jSCallback2.invoke(buildError("permissions.size() == 0"));
                    return;
                }
                for (int i = 0; i < jSONArray.size(); i++) {
                    String string = jSONArray.getString(i);
                    char c = 65535;
                    int hashCode = string.hashCode();
                    if (hashCode != 3496342) {
                        if (hashCode == 113399775) {
                            if (string.equals(MonitorCacheEvent.OPERATION_WRITE)) {
                                c = 1;
                            }
                        }
                    } else if (string.equals(MonitorCacheEvent.OPERATION_READ)) {
                        c = 0;
                    }
                    switch (c) {
                        case 0:
                            arrayList.add("android.permission.READ_CALENDAR");
                            break;
                        case 1:
                            arrayList.add("android.permission.WRITE_CALENDAR");
                            break;
                        default:
                            jSCallback2.invoke(buildError("undefine permission: " + string));
                            return;
                    }
                }
            }
            for (String str : arrayList) {
                if (ActivityCompat.checkSelfPermission(context, str) != 0) {
                    jSCallback2.invoke(buildError("no permission: " + str));
                    return;
                }
            }
            jSCallback.invoke((Object) null);
        } catch (Throwable th) {
            jSCallback2.invoke(buildError(th.getMessage()));
        }
    }

    static class PerReceiver extends BroadcastReceiver {
        PermissionCallback mCallback;

        PerReceiver(PermissionCallback permissionCallback) {
            this.mCallback = permissionCallback;
        }

        public void onReceive(Context context, Intent intent) {
            boolean z = false;
            try {
                if (intent.getIntExtra("requestCode", 0) == 25) {
                    int[] intArrayExtra = intent.getIntArrayExtra(WXModule.GRANT_RESULTS);
                    String[] stringArrayExtra = intent.getStringArrayExtra(WXModule.PERMISSIONS);
                    int i = 0;
                    while (true) {
                        if (intArrayExtra == null || i >= intArrayExtra.length) {
                            z = true;
                        } else if (intArrayExtra[i] != 0) {
                            this.mCallback.onPermissionsDenied(stringArrayExtra[i]);
                            break;
                        } else {
                            i++;
                        }
                    }
                    if (z) {
                        this.mCallback.onPermissionsGranted();
                    }
                }
                LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
            } catch (Throwable th) {
                WXLogUtils.e(WXCalendarModule.TAG, th);
            }
        }
    }
}
