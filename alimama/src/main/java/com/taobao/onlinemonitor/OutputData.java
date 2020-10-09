package com.taobao.onlinemonitor;

import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import com.alibaba.android.umbrella.link.export.UMLLCons;
import com.taobao.android.dinamicx.bindingx.DXBindingXConstant;
import com.taobao.onlinemonitor.MemoryDetector;
import com.taobao.onlinemonitor.OnLineMonitor;
import com.taobao.onlinemonitor.TraceDetail;
import com.taobao.weex.BuildConfig;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OutputData implements Serializable {
    public List<OnLineMonitor.ActivityRuntimeInfo> activities;
    public List<TraceDetail.MethodInfo> activitymanagertrace;
    public List<ThreadInfo> allthreads;
    public List<String> arrayListLeakObject;
    public ArrayList<TraceDetail.NewThreadInfo> asynctaskinfo;
    public HashMap<String, Object> basic = new HashMap<>();
    public List<BackgroundAppInfo> bgapps;
    public String bootLifecycle;
    public List<TraceDetail.PinCpuTime> bootcpu;
    public List<TraceDetail.BroadCastInfo> broadcasttrace;
    public List<String> componentcallbacks;
    public List<ThreadInfo> lastthreads;
    public String leakLifecycle;
    public List<String> leak_onlinelifecyclelist;
    public List<String> leak_onlinenotifylist;
    public List<String> leakbroadcast;
    public List<LocalBroadReceiverInfo> leaklocalbroadcast;
    public List<String> leakservice;
    public List<HashMap<String, Object>> loadedsharedpreference;
    public List<Object> loginfos;
    public ArrayList<OnLineMonitor.ThreadIoInfo> mBlockGuardThreadInfo;
    public List<Float> mBootCpuPercentTimestamps;
    public Map<String, String> mBootDiffThreadMap;
    public List<Long> mBootMajorFaults;
    public SparseIntArray mBootMemoryLevels;
    public List<Float> mBootPerCpuLoads;
    public SparseIntArray mBootPidCpuPercents;
    public ArrayList<OnLineMonitor.ResourceUsedInfo> mBootResourceUsedInfoList;
    public SparseIntArray mBootRunningPidScores;
    public SparseIntArray mBootRunningSysScores;
    public SparseIntArray mBootRunningThreadsCount;
    public SparseIntArray mBootSysCpuPercents;
    public SparseIntArray mBootSysIoWaitCounts;
    public SparseIntArray mBootSysIoWaitPercent;
    public SparseIntArray mBootSysIoWaitSums;
    public List<Float> mBootSysLoads1Min;
    public List<Float> mBootSysLoads5Min;
    public SparseIntArray mBootSysSchedWaitCounts;
    public SparseIntArray mBootSysSchedWaitSums;
    public SparseIntArray mBootSysThreadsCount;
    public SparseIntArray mBootVmThreadsCount;
    public HashMap<String, Integer> mCloseGuardInfo;
    public List<Float> mCpuPercentTimestamps;
    public ArrayList<Object> mDbStats;
    public ArrayList<OnLineMonitor.BundleInfo> mInstallBundleInfoList;
    public HashMap<String, Integer> mMainThreadBlockGuardInfo;
    public List<Long> mMajorFaults;
    public SparseIntArray mMemoryLevels;
    public List<Float> mPerCpuLoads;
    public SparseIntArray mPidCpuPercentRecords;
    public SparseIntArray mRunningFinalizerCount;
    public SparseIntArray mRunningPidScores;
    public SparseIntArray mRunningSysScores;
    public SparseIntArray mRunningThreadsCount;
    public Map<String, Integer> mSharedpreferenceKeyFreq;
    public Map<String, Integer> mSharedpreferenceQueuedWork;
    public ArrayList<OnLineMonitor.BundleInfo> mStartBundleInfoList;
    public SparseIntArray mSysCpuPercentRecords;
    public SparseIntArray mSysIoWaitCounts;
    public SparseIntArray mSysIoWaitPercent;
    public SparseIntArray mSysIoWaitSums;
    public List<Float> mSysLoads1Min;
    public List<Float> mSysLoads5Min;
    public SparseIntArray mSysSchedWaitCounts;
    public SparseIntArray mSysSchedWaitSums;
    public SparseIntArray mSysThreadsCount;
    public SparseIntArray mVmThreadsCount;
    public ArrayList<OnLineMonitor.ThreadIoInfo> mWakeLockInfoList;
    public List<TraceDetail.ThreadStackTraceTime> mainthreadtime;
    public List<MemoryDetector.LeakItem> memoryleak;
    public List<MemoryDetector.StaticOwner> memstaticlist;
    public List<MemoryDetector.StaticVariable> memusedlist;
    public ArrayList<TraceDetail.NewThreadInfo> newthreadinfo;
    public List<TraceDetail.MethodInfo> onlinebackforgroundnotify;
    public List<TraceDetail.MethodInfo> onlinebootnotify;
    public List<TraceDetail.MethodInfo> onlinelifecyclelist;
    public List<TraceDetail.MethodInfo> onlinelifecycletimelist;
    public List<TraceDetail.MethodInfo> onlinenotifylist;
    public List<TraceDetail.MethodInfo> onlinenotifytimelist;
    public List<TraceDetail.ServiceInfo> servicetrace;
    public List<HashMap<String, Object>> sharedpreference;
    public List<TraceDetail.NewThreadInfo> sharedpreference_trace;
    public List<TraceDetail.NewThreadInfo> sofiles;
    public List<Object> stacktracelist;
    public List<ThreadInfo> statisticsthread;
    public List<TraceDetail.ThreadPoolInfo> threadpoolinfo;
    public List<TraceDetail.NewThreadInfo> threadprioritylist;
    public List<Object> throwablelist;

    public static class BackgroundAppInfo implements Serializable {
        int id;
        int javaMem;
        String processName;
        List<String> serviceInfo;
        int sharedMem;
        int totalMem;
    }

    public static class LocalBroadReceiverInfo implements Serializable {
        public List<String> actions;
        public String receiverName;
    }

    public String asJsData() {
        String replace = toJSONObject(this).toString().replace("\\n", "<br>").replace("\\0000", "").replace(DXBindingXConstant.SINGLE_QUOTE, "\\\"");
        return "var raw_data=" + replace;
    }

    private static JSONObject toJSONObject(Object obj) {
        JSONObject jSONObject = new JSONObject();
        if (obj instanceof Collection) {
            return jSONObject;
        }
        if (obj instanceof Map) {
            return map2JSONObject((Map) obj);
        }
        if (obj == null) {
            return null;
        }
        try {
            for (Field field : obj.getClass().getDeclaredFields()) {
                if (!Modifier.isFinal(field.getModifiers())) {
                    field.setAccessible(true);
                    String name = field.getName();
                    Object obj2 = field.get(obj);
                    Class<?> type = field.getType();
                    if (Collection.class.isAssignableFrom(type)) {
                        if (obj2 == null) {
                            jSONObject.put(name, new JSONArray());
                        } else {
                            jSONObject.put(name, toJSONArray((Collection) obj2));
                        }
                    } else if (!type.isArray()) {
                        if (!(obj2 instanceof Map)) {
                            if (!(obj2 instanceof HashMap)) {
                                if (obj2 instanceof SparseArray) {
                                    jSONObject.put(name, sparseArray2JSONObject((SparseArray) obj2));
                                } else if (obj2 instanceof SparseIntArray) {
                                    JSONArray jSONArray = new JSONArray();
                                    for (int i = 0; i < ((SparseIntArray) obj2).size(); i++) {
                                        jSONArray.put(((SparseIntArray) obj2).get(i));
                                    }
                                    jSONObject.put(name, jSONArray);
                                } else if (Number.class.isAssignableFrom(type)) {
                                    jSONObject.put(name, obj2);
                                } else if (obj2 == null) {
                                    jSONObject.put(name, BuildConfig.buildJavascriptFrameworkVersion);
                                } else if (shouldGoInside(obj2.getClass())) {
                                    jSONObject.put(name, toJSONObject(obj2));
                                } else {
                                    jSONObject.put(name, obj2);
                                }
                            }
                        }
                        jSONObject.put(name, map2JSONObject((Map) obj2));
                    } else if (obj2 == null) {
                        jSONObject.put(name, new JSONArray());
                    } else {
                        if (!type.getComponentType().isPrimitive() && !Number.class.isAssignableFrom(type.getComponentType())) {
                            if (!type.getComponentType().isAssignableFrom(String.class)) {
                                if (type.getComponentType() != StackTraceElement.class || obj2 == null) {
                                    jSONObject.put(name, toJSONArray(Arrays.asList((Object[]) obj2)));
                                } else {
                                    try {
                                        StringBuilder sb = new StringBuilder();
                                        for (int i2 = 0; i2 < Array.getLength(obj2); i2++) {
                                            sb.append(Array.get(obj2, i2) + "<br>");
                                        }
                                        jSONObject.put(name, sb.toString());
                                    } catch (Throwable th) {
                                        th.printStackTrace();
                                    }
                                }
                            }
                        }
                        int length = Array.getLength(obj2);
                        JSONArray jSONArray2 = new JSONArray();
                        for (int i3 = 0; i3 < length; i3++) {
                            jSONArray2.put(Array.get(obj2, i3));
                        }
                        jSONObject.put(name, jSONArray2);
                    }
                }
            }
            return jSONObject;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private static Object toJSONValue(Object obj, Class cls) {
        if (!Collection.class.isAssignableFrom(cls)) {
            int i = 0;
            if (cls.isArray()) {
                if (obj == null) {
                    return new JSONArray();
                }
                if (cls.getComponentType().isPrimitive() || Number.class.isAssignableFrom(cls.getComponentType()) || cls.getComponentType().isAssignableFrom(String.class)) {
                    int length = Array.getLength(obj);
                    JSONArray jSONArray = new JSONArray();
                    while (i < length) {
                        jSONArray.put(Array.get(obj, i));
                        i++;
                    }
                    return jSONArray;
                } else if (cls.getComponentType() != StackTraceElement.class) {
                    return toJSONArray(Arrays.asList((Object[]) obj));
                } else {
                    StringBuilder sb = new StringBuilder();
                    while (i < Array.getLength(obj)) {
                        sb.append(Array.get(obj, i) + "<br>");
                        i++;
                    }
                    return sb.toString();
                }
            } else if ((obj instanceof Map) || (obj instanceof HashMap)) {
                return map2JSONObject((Map) obj);
            } else {
                if (obj instanceof SparseArray) {
                    return sparseArray2JSONObject((SparseArray) obj);
                }
                if (obj instanceof SparseIntArray) {
                    JSONArray jSONArray2 = new JSONArray();
                    while (true) {
                        SparseIntArray sparseIntArray = (SparseIntArray) obj;
                        if (i >= sparseIntArray.size()) {
                            return jSONArray2;
                        }
                        jSONArray2.put(sparseIntArray.get(i));
                        i++;
                    }
                } else if (Number.class.isAssignableFrom(cls)) {
                    return obj;
                } else {
                    if (obj == null) {
                        return null;
                    }
                    if (shouldGoInside(obj.getClass())) {
                        return toJSONObject(obj);
                    }
                    return obj;
                }
            }
        } else if (obj == null) {
            return new JSONArray();
        } else {
            return toJSONArray((Collection) obj);
        }
    }

    private static boolean shouldGoInside(Class cls) {
        return cls != null && cls.getName().startsWith("com.taobao.onlinemonitor") && !cls.getSimpleName().equals("OnLineMonitor") && !cls.getSimpleName().equals("TraceDetail") && !cls.equals(StringBuilder.class);
    }

    private static JSONObject map2JSONObject(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        JSONObject jSONObject = new JSONObject();
        for (Map.Entry next : map.entrySet()) {
            try {
                Object value = next.getValue();
                String str = ((String) next.getKey()).toString();
                if (str.equals("mActivityLifeCycleName")) {
                    Log.d(UMLLCons.FEATURE_TYPE_DEBUG, "mActivityLifeCycleName");
                }
                if (value == null) {
                    jSONObject.put(str, (Object) null);
                } else if (shouldGoInside(value.getClass())) {
                    jSONObject.put(str, toJSONObject(value));
                } else {
                    jSONObject.put(str, toJSONValue(value, value.getClass()));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jSONObject;
    }

    private static JSONArray toJSONArray(Collection collection) {
        if (collection == null) {
            return new JSONArray();
        }
        JSONArray jSONArray = new JSONArray();
        try {
            for (Object next : collection) {
                if (next != null) {
                    if (next instanceof Collection) {
                        jSONArray.put(toJSONArray((Collection) next));
                    } else {
                        if (!(next instanceof String) && !next.getClass().isPrimitive()) {
                            if (!Number.class.isAssignableFrom(next.getClass())) {
                                jSONArray.put(toJSONObject(next));
                            }
                        }
                        jSONArray.put(next);
                    }
                }
            }
        } catch (Throwable unused) {
        }
        return jSONArray;
    }

    public static JSONObject sparseArray2JSONObject(SparseArray sparseArray) {
        JSONObject jSONObject = new JSONObject();
        for (int i = 0; i < sparseArray.size(); i++) {
            int keyAt = sparseArray.keyAt(i);
            try {
                jSONObject.put(String.valueOf(keyAt), sparseArray.valueAt(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jSONObject;
    }
}
