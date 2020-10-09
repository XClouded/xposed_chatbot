package com.taobao.accs.client;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.taobao.accs.utl.ALog;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.json.JSONArray;
import org.json.JSONObject;

public class ClientManager {
    private static final int BINDED = 2;
    private static final int BINDING = 1;
    private static final String PRE_NAME = "ACCS_BIND";
    private static final String SP_BIND_KEY = "bind_status";
    private static final int UNBINDED = 4;
    private static final int UNBINDING = 3;
    private String SP_BIND_FILE_NAME = PRE_NAME;
    private String TAG = "ClientManager_";
    private long lastFlushTime;
    private ConcurrentMap<String, Integer> mBindStatus = new ConcurrentHashMap();
    private Context mContext;
    private ConcurrentMap<String, Set<String>> mUserBindStatus = new ConcurrentHashMap();

    public ClientManager(Context context, String str) {
        if (context != null) {
            this.TAG += str;
            this.mContext = context.getApplicationContext();
            this.SP_BIND_FILE_NAME = PRE_NAME + str;
            restoreClients();
            return;
        }
        throw new RuntimeException("Context is null!!");
    }

    public void onAppBind(String str) {
        Integer num = (Integer) this.mBindStatus.get(str);
        if (num == null || num.intValue() != 2) {
            this.mBindStatus.put(str, 2);
            saveClients(this.mContext, this.SP_BIND_FILE_NAME, this.lastFlushTime, this.mBindStatus);
        }
    }

    public void onAppUnbind(String str) {
        Integer num = (Integer) this.mBindStatus.get(str);
        if (num == null || num.intValue() != 4) {
            this.mBindStatus.put(str, 4);
            saveClients(this.mContext, this.SP_BIND_FILE_NAME, this.lastFlushTime, this.mBindStatus);
        }
    }

    public void onAppBinding(String str) {
        Integer num = (Integer) this.mBindStatus.get(str);
        if (num == null || num.intValue() != 1) {
            this.mBindStatus.put(str, 1);
            saveClients(this.mContext, this.SP_BIND_FILE_NAME, this.lastFlushTime, this.mBindStatus);
        }
    }

    public void onAppUnbinding(String str) {
        Integer num = (Integer) this.mBindStatus.get(str);
        if (num == null || num.intValue() != 3) {
            this.mBindStatus.put(str, 3);
            saveClients(this.mContext, this.SP_BIND_FILE_NAME, this.lastFlushTime, this.mBindStatus);
        }
    }

    public boolean isAppBinded(String str) {
        if (this.mBindStatus.isEmpty()) {
            restoreClients();
        }
        Integer num = (Integer) this.mBindStatus.get(str);
        ALog.i(this.TAG, "isAppBinded", "appStatus", num, "mBindStatus", this.mBindStatus);
        if (num == null || num.intValue() != 2) {
            return false;
        }
        return true;
    }

    public boolean isAppUnbinded(String str) {
        Integer num = (Integer) this.mBindStatus.get(str);
        return num != null && num.intValue() == 4;
    }

    public boolean isAppBinding(String str) {
        Integer num = (Integer) this.mBindStatus.get(str);
        if (num == null || num.intValue() != 1) {
            return false;
        }
        return true;
    }

    public boolean isAppUnbinding(String str) {
        Integer num = (Integer) this.mBindStatus.get(str);
        return num != null && num.intValue() == 3;
    }

    public void onUserBind(String str, String str2) {
        try {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            if (!TextUtils.isEmpty(str2)) {
                Set set = (Set) this.mUserBindStatus.get(str);
                if (set == null) {
                    set = new HashSet();
                }
                set.add(str2);
                this.mUserBindStatus.put(str, set);
            }
        } catch (Exception e) {
            String str3 = this.TAG;
            ALog.e(str3, this.TAG + e.toString(), new Object[0]);
            e.printStackTrace();
        }
    }

    public void onUserUnBind(String str, String str2) {
        try {
            if (!TextUtils.isEmpty(str)) {
                this.mUserBindStatus.remove(str);
            }
        } catch (Exception e) {
            String str3 = this.TAG;
            ALog.e(str3, this.TAG + e.toString(), new Object[0]);
            e.printStackTrace();
        }
    }

    public boolean isUserBinded(String str, String str2) {
        Set set;
        try {
            if (!TextUtils.isEmpty(str) && (set = (Set) this.mUserBindStatus.get(str)) != null && set.contains(str2)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            String str3 = this.TAG;
            ALog.e(str3, this.TAG + e.toString(), new Object[0]);
            e.printStackTrace();
        }
    }

    private void restoreClients() {
        try {
            String string = this.mContext.getSharedPreferences(this.SP_BIND_FILE_NAME, 0).getString(SP_BIND_KEY, (String) null);
            if (TextUtils.isEmpty(string)) {
                ALog.w(this.TAG, "restoreClients break as packages null", new Object[0]);
                return;
            }
            JSONArray jSONArray = new JSONArray(string);
            this.lastFlushTime = jSONArray.getLong(0);
            if (System.currentTimeMillis() < this.lastFlushTime + 86400000) {
                for (int i = 1; i < jSONArray.length(); i++) {
                    JSONObject jSONObject = jSONArray.getJSONObject(i);
                    this.mBindStatus.put(jSONObject.getString("p"), Integer.valueOf(jSONObject.getInt("s")));
                }
                ALog.i(this.TAG, "restoreClients success", "mBindStatus", this.mBindStatus);
                return;
            }
            ALog.i(this.TAG, "restoreClients expired", "lastFlushTime", Long.valueOf(this.lastFlushTime));
            this.lastFlushTime = 0;
        } catch (Exception e) {
            ALog.w(this.TAG, "restoreClients", e, new Object[0]);
        }
    }

    public static void saveClients(Context context, String str, long j, Map<String, Integer> map) {
        try {
            String[] strArr = (String[]) map.keySet().toArray(new String[0]);
            JSONArray jSONArray = new JSONArray();
            if (j <= 0 || j >= System.currentTimeMillis()) {
                double currentTimeMillis = (double) System.currentTimeMillis();
                double random = Math.random() * 8.64E7d;
                Double.isNaN(currentTimeMillis);
                jSONArray.put(currentTimeMillis - random);
            } else {
                jSONArray.put(j);
            }
            for (String str2 : strArr) {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("p", str2);
                jSONObject.put("s", map.get(str2).intValue());
                jSONArray.put(jSONObject);
            }
            SharedPreferences.Editor edit = context.getSharedPreferences(str, 0).edit();
            edit.putString(SP_BIND_KEY, jSONArray.toString());
            edit.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearClients() {
        try {
            SharedPreferences.Editor edit = this.mContext.getSharedPreferences(this.SP_BIND_FILE_NAME, 0).edit();
            edit.clear();
            edit.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
