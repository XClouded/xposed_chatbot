package com.taobao.onlinemonitor;

import android.os.Debug;
import android.util.Log;
import com.vivo.push.PushClientConstants;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MemoryDetector {
    File mHeapDumpFile = new File(OnLineMonitor.sOnLineMonitorFileDir + "/heapdump.hprof");
    List<StaticVariable> mMemoryUsedList;
    long mTotalInstanceCount;
    long mTotalInstanceRetainedSize;
    long mTotalInstanceSize;
    int mTotalSingletonCount;
    int mTotalSingletonRetainedSize;
    int mTotalSingletonSize;
    int mTotalStaticCount;
    int mTotalStaticRetainedSize;
    int mTotalStaticSize;

    public static class LeakItem implements Serializable {
        public long analysisDurationMs;
        public String className;
        public String detailLeakTrace;
        public long retainedHeapSize;
        public String simpleLeakTrace;
    }

    public static class StaticVariable implements Serializable {
        public String className;
        public String fieldName;
        ArrayList<String> hardReferences;
        public boolean isSingleton;
        public long retainedHeapSize;
        public long shadowHeapSize;
        ArrayList<String> softReferences;
    }

    MemoryDetector() {
        if (this.mHeapDumpFile.exists()) {
            this.mHeapDumpFile.delete();
        }
        try {
            Runtime.getRuntime().runFinalization();
            System.gc();
        } catch (Throwable th) {
            Log.e("OnLineMonitor", "手动执行内存回收异常", th);
        }
        try {
            Thread.sleep(500);
            System.runFinalization();
        } catch (InterruptedException unused) {
            throw new AssertionError();
        }
    }

    /* access modifiers changed from: package-private */
    public List<LeakItem> detectLeak(List<Object> list) {
        Iterator it;
        Field field;
        Object obj;
        String str;
        List<Object> list2 = list;
        if (list2 == null) {
            return null;
        }
        try {
            Runtime.getRuntime().gc();
            Runtime.getRuntime().runFinalization();
            Runtime.getRuntime().gc();
        } catch (Throwable th) {
            Log.e("OnLineMonitor", "手动执行内存回收异常", th);
        }
        Log.e("OnLineMonitor", "分析泄漏: " + list2);
        ArrayList arrayList = new ArrayList(list.size());
        ReferenceQueue referenceQueue = new ReferenceQueue();
        try {
            Class<?> cls = Class.forName("com.squareup.leakcanary.KeyedWeakReference");
            Log.d("OnLineMonitor", cls.getName());
            char c = 0;
            char c2 = 1;
            Constructor<?> declaredConstructor = cls.getDeclaredConstructor(new Class[]{Object.class, String.class, String.class, ReferenceQueue.class});
            declaredConstructor.setAccessible(true);
            for (Object next : list) {
                String uuid = UUID.randomUUID().toString();
                declaredConstructor.newInstance(new Object[]{next, uuid, "", referenceQueue});
                arrayList.add(uuid);
            }
            list.clear();
            try {
                synchronized (this.mHeapDumpFile) {
                    Log.e("OnLineMonitor", "dumping memory...");
                    if (!this.mHeapDumpFile.exists()) {
                        Debug.dumpHprofData(this.mHeapDumpFile.getAbsolutePath());
                    }
                }
            } catch (Throwable unused) {
                Log.e("OnLineMonitor", "Failed to dump memory");
            }
            Log.e("OnLineMonitor", "Dump done, do Analyzer");
            ArrayList arrayList2 = new ArrayList();
            try {
                Class<?> cls2 = Class.forName("com.squareup.leakcanary.AndroidExcludedRefs");
                Class<?> cls3 = Class.forName("com.squareup.leakcanary.ExcludedRefs");
                Method declaredMethod = cls2.getDeclaredMethod("createAndroidDefaults", new Class[0]);
                declaredMethod.setAccessible(true);
                Object invoke = declaredMethod.invoke(cls2, new Object[0]);
                Method declaredMethod2 = invoke.getClass().getDeclaredMethod("build", new Class[0]);
                declaredMethod2.setAccessible(true);
                Object invoke2 = declaredMethod2.invoke(invoke, new Object[0]);
                Class<?> cls4 = Class.forName("com.squareup.leakcanary.HeapAnalyzer");
                Constructor<?> declaredConstructor2 = cls4.getDeclaredConstructor(new Class[]{cls3});
                declaredConstructor2.setAccessible(true);
                Method declaredMethod3 = cls4.getDeclaredMethod("checkForLeak", new Class[]{File.class, String.class});
                declaredMethod3.setAccessible(true);
                Object newInstance = declaredConstructor2.newInstance(new Object[]{invoke2});
                Class<?> cls5 = Class.forName("com.squareup.leakcanary.AnalysisResult");
                Field declaredField = cls5.getDeclaredField("leakFound");
                declaredField.setAccessible(true);
                Field declaredField2 = cls5.getDeclaredField(PushClientConstants.TAG_CLASS_NAME);
                declaredField2.setAccessible(true);
                Field declaredField3 = cls5.getDeclaredField("retainedHeapSize");
                declaredField3.setAccessible(true);
                Field declaredField4 = cls5.getDeclaredField("leakTrace");
                declaredField.setAccessible(true);
                Field declaredField5 = cls5.getDeclaredField("analysisDurationMs");
                declaredField5.setAccessible(true);
                Class<?> cls6 = Class.forName("com.squareup.leakcanary.LeakTrace");
                Method declaredMethod4 = cls6.getDeclaredMethod("toString", new Class[0]);
                declaredMethod4.setAccessible(true);
                cls6.getDeclaredMethod("toDetailedString", new Class[0]).setAccessible(true);
                Iterator it2 = arrayList.iterator();
                while (it2.hasNext()) {
                    String str2 = (String) it2.next();
                    try {
                        Object[] objArr = new Object[2];
                        objArr[c] = this.mHeapDumpFile;
                        objArr[c2] = str2;
                        Object invoke3 = declaredMethod3.invoke(newInstance, objArr);
                        boolean z = declaredField.getBoolean(invoke3);
                        if (invoke3 == null || !z) {
                            obj = newInstance;
                            field = declaredField5;
                            it = it2;
                            newInstance = obj;
                            declaredField5 = field;
                            it2 = it;
                            c = 0;
                            c2 = 1;
                        } else {
                            LeakItem leakItem = new LeakItem();
                            leakItem.className = (String) declaredField2.get(invoke3);
                            leakItem.retainedHeapSize = declaredField3.getLong(invoke3);
                            Object obj2 = declaredField4.get(invoke3);
                            if (obj2 != null) {
                                obj = newInstance;
                                try {
                                    leakItem.simpleLeakTrace = (String) declaredMethod4.invoke(obj2, new Object[0]);
                                } catch (Throwable unused2) {
                                }
                            } else {
                                obj = newInstance;
                            }
                            str = str2;
                            try {
                                leakItem.analysisDurationMs = declaredField5.getLong(invoke3);
                                arrayList2.add(leakItem);
                                if (OnLineMonitor.sOnLineMonitor.mOnlineStatistics != null) {
                                    int size = OnLineMonitor.sOnLineMonitor.mOnlineStatistics.size();
                                    int i = 0;
                                    while (i < size) {
                                        OnlineStatistics onlineStatistics = OnLineMonitor.sOnLineMonitor.mOnlineStatistics.get(i);
                                        if (onlineStatistics != null) {
                                            field = declaredField5;
                                            it = it2;
                                            try {
                                                onlineStatistics.onMemoryLeak(leakItem.className, leakItem.retainedHeapSize, leakItem.simpleLeakTrace);
                                            } catch (Throwable unused3) {
                                                Log.e("OnLineMonitor", "Failed to AnalysisResult" + str);
                                                newInstance = obj;
                                                declaredField5 = field;
                                                it2 = it;
                                                c = 0;
                                                c2 = 1;
                                            }
                                        } else {
                                            field = declaredField5;
                                            it = it2;
                                        }
                                        i++;
                                        declaredField5 = field;
                                        it2 = it;
                                    }
                                }
                                field = declaredField5;
                                it = it2;
                                Log.e("OnLineMonitor", "leak item: " + leakItem.className + "," + (((float) leakItem.retainedHeapSize) / 1024.0f) + " k" + "," + leakItem.simpleLeakTrace);
                            } catch (Throwable unused4) {
                                field = declaredField5;
                                it = it2;
                                Log.e("OnLineMonitor", "Failed to AnalysisResult" + str);
                                newInstance = obj;
                                declaredField5 = field;
                                it2 = it;
                                c = 0;
                                c2 = 1;
                            }
                            newInstance = obj;
                            declaredField5 = field;
                            it2 = it;
                            c = 0;
                            c2 = 1;
                        }
                    } catch (Throwable unused5) {
                        obj = newInstance;
                        field = declaredField5;
                        it = it2;
                        str = str2;
                        Log.e("OnLineMonitor", "Failed to AnalysisResult" + str);
                        newInstance = obj;
                        declaredField5 = field;
                        it2 = it;
                        c = 0;
                        c2 = 1;
                    }
                }
            } catch (Throwable th2) {
                Log.e("OnLineMonitor", "Failed to dump memory", th2);
            }
            if (!TraceDetail.sMemoryAnalysis) {
                this.mHeapDumpFile.delete();
            }
            return arrayList2;
        } catch (Throwable th3) {
            th3.printStackTrace();
            Log.e("OnLineMonitor", "LeakCanary error");
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public List<StaticOwner> findStaticVariables() {
        ArrayList arrayList;
        Method method;
        HashMap hashMap;
        Method method2;
        Method method3;
        Method method4;
        Method method5;
        Field field;
        Class<?> cls;
        int i;
        int i2;
        List list;
        Method method6;
        Method method7;
        Method method8;
        Method method9;
        Method method10;
        Method method11;
        Method method12;
        Method method13;
        Method method14;
        Method method15;
        Field field2;
        ArrayList arrayList2;
        Method method16;
        Method method17;
        HashMap hashMap2;
        Method method18;
        Method method19;
        Map map;
        Method method20;
        Field field3;
        ArrayList arrayList3;
        Method method21;
        Method method22;
        Method method23;
        Method method24;
        Method method25;
        String str;
        StaticVariable staticVariable;
        HashMap hashMap3;
        StaticOwner staticOwner;
        try {
            if (!this.mHeapDumpFile.exists()) {
                Debug.dumpHprofData(this.mHeapDumpFile.getAbsolutePath());
            }
            ArrayList arrayList4 = new ArrayList(1000);
            try {
                Constructor<?> declaredConstructor = Class.forName("com.squareup.haha.perflib.io.MemoryMappedFileBuffer").getDeclaredConstructor(new Class[]{File.class});
                declaredConstructor.setAccessible(true);
                Object newInstance = declaredConstructor.newInstance(new Object[]{this.mHeapDumpFile});
                Class<?> cls2 = Class.forName("com.squareup.haha.perflib.io.HprofBuffer");
                Class<?> cls3 = Class.forName("com.squareup.haha.perflib.HprofParser");
                Method declaredMethod = cls3.getDeclaredMethod("parse", new Class[0]);
                declaredMethod.setAccessible(true);
                Class<?> cls4 = Class.forName("com.squareup.haha.perflib.Snapshot");
                Method declaredMethod2 = cls4.getDeclaredMethod("computeDominators", new Class[0]);
                declaredMethod2.setAccessible(true);
                Method declaredMethod3 = cls4.getDeclaredMethod("getReachableInstances", new Class[0]);
                declaredMethod3.setAccessible(true);
                Constructor<?> declaredConstructor2 = cls3.getDeclaredConstructor(new Class[]{cls2});
                declaredConstructor2.setAccessible(true);
                Object invoke = declaredMethod.invoke(declaredConstructor2.newInstance(new Object[]{newInstance}), new Object[0]);
                Class<?> cls5 = Class.forName("com.squareup.haha.perflib.Instance");
                Method declaredMethod4 = cls5.getDeclaredMethod("getClassObj", new Class[0]);
                declaredMethod4.setAccessible(true);
                Method declaredMethod5 = cls5.getDeclaredMethod("getTotalRetainedSize", new Class[0]);
                declaredMethod5.setAccessible(true);
                Method declaredMethod6 = cls5.getDeclaredMethod("getSize", new Class[0]);
                declaredMethod6.setAccessible(true);
                Field declaredField = cls5.getDeclaredField("mHardReferences");
                declaredField.setAccessible(true);
                Field declaredField2 = cls5.getDeclaredField("mSoftReferences");
                declaredField2.setAccessible(true);
                Class<?> cls6 = Class.forName("com.squareup.haha.perflib.ClassObj");
                Method declaredMethod7 = cls6.getDeclaredMethod("getClassName", new Class[0]);
                declaredMethod7.setAccessible(true);
                Class<?> cls7 = Class.forName("com.squareup.haha.perflib.ArrayInstance");
                Class.forName("com.squareup.haha.perflib.Type");
                Method declaredMethod8 = cls6.getDeclaredMethod("getStaticFieldValues", new Class[0]);
                declaredMethod8.setAccessible(true);
                Class<?> cls8 = Class.forName("com.squareup.haha.perflib.Field");
                ArrayList arrayList5 = arrayList4;
                try {
                    Method declaredMethod9 = cls8.getDeclaredMethod("getType", new Class[0]);
                    declaredMethod9.setAccessible(true);
                    Method method26 = declaredMethod9;
                    Method declaredMethod10 = cls8.getDeclaredMethod("getName", new Class[0]);
                    declaredMethod10.setAccessible(true);
                    declaredMethod2.invoke(invoke, new Object[0]);
                    this.mMemoryUsedList = new ArrayList(1000);
                    List list2 = (List) declaredMethod3.invoke(invoke, new Object[0]);
                    int size = list2.size();
                    Method method27 = declaredMethod5;
                    this.mTotalInstanceCount = (long) size;
                    HashMap hashMap4 = new HashMap();
                    for (int i3 = 0; i3 < size; i3 = i + 1) {
                        Object obj = list2.get(i3);
                        Object invoke2 = declaredMethod4.invoke(obj, new Object[0]);
                        if (invoke2 == null) {
                            method9 = declaredMethod7;
                            method10 = declaredMethod10;
                            list = list2;
                        } else {
                            boolean isInstance = cls7.isInstance(obj);
                            list = list2;
                            String str2 = (String) declaredMethod7.invoke(invoke2, new Object[0]);
                            if (!str2.startsWith("com.taobao.onlinemonitor") && !str2.startsWith("char[]")) {
                                if (!str2.startsWith("boolean[]")) {
                                    i2 = size;
                                    Method method28 = method27;
                                    i = i3;
                                    long longValue = ((Long) method28.invoke(obj, new Object[0])).longValue();
                                    cls = cls7;
                                    Method method29 = declaredMethod4;
                                    HashMap hashMap5 = hashMap4;
                                    if (longValue >= ((long) OnLineMonitorApp.sInstanceOccupySize)) {
                                        StaticVariable staticVariable2 = new StaticVariable();
                                        staticVariable2.className = str2;
                                        staticVariable2.retainedHeapSize = longValue;
                                        method13 = method28;
                                        method14 = declaredMethod6;
                                        staticVariable2.shadowHeapSize = (long) ((Integer) declaredMethod6.invoke(obj, new Object[0])).intValue();
                                        this.mTotalInstanceCount++;
                                        method12 = declaredMethod7;
                                        method11 = declaredMethod10;
                                        this.mTotalInstanceSize += staticVariable2.shadowHeapSize;
                                        this.mTotalInstanceRetainedSize += longValue;
                                        ArrayList arrayList6 = (ArrayList) declaredField.get(obj);
                                        ArrayList arrayList7 = (ArrayList) declaredField2.get(obj);
                                        if (arrayList6 != null) {
                                            staticVariable2.hardReferences = new ArrayList<>(arrayList6.size());
                                            for (int i4 = 0; i4 < arrayList6.size(); i4++) {
                                                Object obj2 = arrayList6.get(i4);
                                                if (obj2 != null) {
                                                    staticVariable2.hardReferences.add(obj2.toString());
                                                }
                                            }
                                        }
                                        if (arrayList7 != null) {
                                            staticVariable2.softReferences = new ArrayList<>(arrayList7.size());
                                            for (int i5 = 0; i5 < arrayList7.size(); i5++) {
                                                Object obj3 = arrayList7.get(i5);
                                                if (obj3 != null) {
                                                    staticVariable2.softReferences.add(obj3.toString());
                                                }
                                            }
                                        }
                                        this.mMemoryUsedList.add(staticVariable2);
                                    } else {
                                        method12 = declaredMethod7;
                                        method11 = declaredMethod10;
                                        method13 = method28;
                                        method14 = declaredMethod6;
                                    }
                                    if (!(isInstance || (map = (Map) declaredMethod8.invoke(invoke2, new Object[0])) == null || map.size() == 0)) {
                                        try {
                                            StaticOwner staticOwner2 = null;
                                            for (Map.Entry entry : map.entrySet()) {
                                                Object key = entry.getKey();
                                                Method method30 = method;
                                                try {
                                                    Object invoke3 = method30.invoke(key, new Object[0]);
                                                    if (invoke3 == null || invoke3.toString().equals("OBJECT")) {
                                                        method23 = method11;
                                                        try {
                                                            String str3 = (String) method23.invoke(key, new Object[0]);
                                                            if (!str3.equals("$staticOverhead")) {
                                                                Object value = entry.getValue();
                                                                if (value != null) {
                                                                    method8 = method29;
                                                                    try {
                                                                        Object invoke4 = method8.invoke(value, new Object[0]);
                                                                        if (invoke4 == null) {
                                                                            method22 = method12;
                                                                        } else {
                                                                            method22 = method12;
                                                                            try {
                                                                                String str4 = (String) method22.invoke(invoke4, new Object[0]);
                                                                                if (!str4.startsWith("com.taobao.onlinemonitor")) {
                                                                                    method7 = method13;
                                                                                    try {
                                                                                        method5 = declaredMethod8;
                                                                                        field = declaredField2;
                                                                                    } catch (Throwable th) {
                                                                                        th = th;
                                                                                        method5 = declaredMethod8;
                                                                                        field = declaredField2;
                                                                                        method4 = method30;
                                                                                        method3 = method23;
                                                                                        method2 = method22;
                                                                                        arrayList = arrayList5;
                                                                                        hashMap = hashMap5;
                                                                                        method6 = method14;
                                                                                        try {
                                                                                            Log.e("OnLineMonitor", "分析内存异常", th);
                                                                                            arrayList5 = arrayList;
                                                                                            declaredMethod4 = method8;
                                                                                            method27 = method7;
                                                                                            declaredMethod6 = method6;
                                                                                            list2 = list;
                                                                                            size = i2;
                                                                                            cls7 = cls;
                                                                                            declaredField2 = field;
                                                                                            declaredMethod8 = method5;
                                                                                            method26 = method4;
                                                                                            declaredMethod10 = method3;
                                                                                            declaredMethod7 = method2;
                                                                                            hashMap4 = hashMap;
                                                                                        } catch (Throwable th2) {
                                                                                            th = th2;
                                                                                        }
                                                                                    }
                                                                                    try {
                                                                                        long longValue2 = ((Long) method7.invoke(value, new Object[0])).longValue();
                                                                                        method4 = method30;
                                                                                        method3 = method23;
                                                                                        if (longValue2 >= ((long) TraceDetail.sMemoryOccupySize)) {
                                                                                            try {
                                                                                                staticVariable = new StaticVariable();
                                                                                                staticVariable.fieldName = str3;
                                                                                                staticVariable.className = str4;
                                                                                                staticVariable.retainedHeapSize = longValue2;
                                                                                                try {
                                                                                                    method6 = method14;
                                                                                                    try {
                                                                                                        method2 = method22;
                                                                                                    } catch (Throwable th3) {
                                                                                                        th = th3;
                                                                                                        method2 = method22;
                                                                                                        arrayList = arrayList5;
                                                                                                        hashMap = hashMap5;
                                                                                                        Log.e("OnLineMonitor", "分析内存异常", th);
                                                                                                        arrayList5 = arrayList;
                                                                                                        declaredMethod4 = method8;
                                                                                                        method27 = method7;
                                                                                                        declaredMethod6 = method6;
                                                                                                        list2 = list;
                                                                                                        size = i2;
                                                                                                        cls7 = cls;
                                                                                                        declaredField2 = field;
                                                                                                        declaredMethod8 = method5;
                                                                                                        method26 = method4;
                                                                                                        declaredMethod10 = method3;
                                                                                                        declaredMethod7 = method2;
                                                                                                        hashMap4 = hashMap;
                                                                                                    }
                                                                                                } catch (Throwable th4) {
                                                                                                    th = th4;
                                                                                                    method2 = method22;
                                                                                                    arrayList = arrayList5;
                                                                                                    hashMap = hashMap5;
                                                                                                    method6 = method14;
                                                                                                    Log.e("OnLineMonitor", "分析内存异常", th);
                                                                                                    arrayList5 = arrayList;
                                                                                                    declaredMethod4 = method8;
                                                                                                    method27 = method7;
                                                                                                    declaredMethod6 = method6;
                                                                                                    list2 = list;
                                                                                                    size = i2;
                                                                                                    cls7 = cls;
                                                                                                    declaredField2 = field;
                                                                                                    declaredMethod8 = method5;
                                                                                                    method26 = method4;
                                                                                                    declaredMethod10 = method3;
                                                                                                    declaredMethod7 = method2;
                                                                                                    hashMap4 = hashMap;
                                                                                                }
                                                                                            } catch (Throwable th5) {
                                                                                                th = th5;
                                                                                                method2 = method22;
                                                                                                arrayList = arrayList5;
                                                                                                hashMap = hashMap5;
                                                                                                method6 = method14;
                                                                                                Log.e("OnLineMonitor", "分析内存异常", th);
                                                                                                arrayList5 = arrayList;
                                                                                                declaredMethod4 = method8;
                                                                                                method27 = method7;
                                                                                                declaredMethod6 = method6;
                                                                                                list2 = list;
                                                                                                size = i2;
                                                                                                cls7 = cls;
                                                                                                declaredField2 = field;
                                                                                                declaredMethod8 = method5;
                                                                                                method26 = method4;
                                                                                                declaredMethod10 = method3;
                                                                                                declaredMethod7 = method2;
                                                                                                hashMap4 = hashMap;
                                                                                            }
                                                                                            try {
                                                                                                staticVariable.shadowHeapSize = (long) ((Integer) method6.invoke(value, new Object[0])).intValue();
                                                                                                if (staticOwner2 == null) {
                                                                                                    staticOwner2 = new StaticOwner(staticVariable.className);
                                                                                                }
                                                                                                staticVariable.isSingleton = str4.equals(str2);
                                                                                                staticOwner2.addStaticVariable(staticVariable);
                                                                                                hashMap3 = hashMap5;
                                                                                            } catch (Throwable th6) {
                                                                                                th = th6;
                                                                                                arrayList = arrayList5;
                                                                                                hashMap = hashMap5;
                                                                                                Log.e("OnLineMonitor", "分析内存异常", th);
                                                                                                arrayList5 = arrayList;
                                                                                                declaredMethod4 = method8;
                                                                                                method27 = method7;
                                                                                                declaredMethod6 = method6;
                                                                                                list2 = list;
                                                                                                size = i2;
                                                                                                cls7 = cls;
                                                                                                declaredField2 = field;
                                                                                                declaredMethod8 = method5;
                                                                                                method26 = method4;
                                                                                                declaredMethod10 = method3;
                                                                                                declaredMethod7 = method2;
                                                                                                hashMap4 = hashMap;
                                                                                            }
                                                                                            try {
                                                                                                if (!hashMap3.containsKey(str2)) {
                                                                                                    hashMap3.put(str2, Boolean.FALSE);
                                                                                                    arrayList = arrayList5;
                                                                                                    try {
                                                                                                        arrayList.add(staticOwner2);
                                                                                                        this.mTotalStaticCount++;
                                                                                                        this.mTotalStaticRetainedSize = (int) (((long) this.mTotalStaticRetainedSize) + longValue2);
                                                                                                        staticOwner = staticOwner2;
                                                                                                        hashMap = hashMap3;
                                                                                                    } catch (Throwable th7) {
                                                                                                        th = th7;
                                                                                                        hashMap = hashMap3;
                                                                                                        Log.e("OnLineMonitor", "分析内存异常", th);
                                                                                                        arrayList5 = arrayList;
                                                                                                        declaredMethod4 = method8;
                                                                                                        method27 = method7;
                                                                                                        declaredMethod6 = method6;
                                                                                                        list2 = list;
                                                                                                        size = i2;
                                                                                                        cls7 = cls;
                                                                                                        declaredField2 = field;
                                                                                                        declaredMethod8 = method5;
                                                                                                        method26 = method4;
                                                                                                        declaredMethod10 = method3;
                                                                                                        declaredMethod7 = method2;
                                                                                                        hashMap4 = hashMap;
                                                                                                    }
                                                                                                    try {
                                                                                                        this.mTotalStaticSize = (int) (((long) this.mTotalStaticSize) + staticVariable.shadowHeapSize);
                                                                                                        if (staticVariable.isSingleton) {
                                                                                                            this.mTotalSingletonCount++;
                                                                                                            str = str2;
                                                                                                            this.mTotalSingletonSize = (int) (((long) this.mTotalSingletonSize) + staticVariable.shadowHeapSize);
                                                                                                            this.mTotalSingletonRetainedSize = (int) (((long) this.mTotalSingletonRetainedSize) + longValue2);
                                                                                                        } else {
                                                                                                            str = str2;
                                                                                                        }
                                                                                                    } catch (Throwable th8) {
                                                                                                        th = th8;
                                                                                                        Log.e("OnLineMonitor", "分析内存异常", th);
                                                                                                        arrayList5 = arrayList;
                                                                                                        declaredMethod4 = method8;
                                                                                                        method27 = method7;
                                                                                                        declaredMethod6 = method6;
                                                                                                        list2 = list;
                                                                                                        size = i2;
                                                                                                        cls7 = cls;
                                                                                                        declaredField2 = field;
                                                                                                        declaredMethod8 = method5;
                                                                                                        method26 = method4;
                                                                                                        declaredMethod10 = method3;
                                                                                                        declaredMethod7 = method2;
                                                                                                        hashMap4 = hashMap;
                                                                                                    }
                                                                                                } else {
                                                                                                    staticOwner = staticOwner2;
                                                                                                    hashMap = hashMap3;
                                                                                                    str = str2;
                                                                                                    arrayList = arrayList5;
                                                                                                }
                                                                                                staticOwner2 = staticOwner;
                                                                                            } catch (Throwable th9) {
                                                                                                th = th9;
                                                                                                hashMap = hashMap3;
                                                                                                arrayList = arrayList5;
                                                                                                Log.e("OnLineMonitor", "分析内存异常", th);
                                                                                                arrayList5 = arrayList;
                                                                                                declaredMethod4 = method8;
                                                                                                method27 = method7;
                                                                                                declaredMethod6 = method6;
                                                                                                list2 = list;
                                                                                                size = i2;
                                                                                                cls7 = cls;
                                                                                                declaredField2 = field;
                                                                                                declaredMethod8 = method5;
                                                                                                method26 = method4;
                                                                                                declaredMethod10 = method3;
                                                                                                declaredMethod7 = method2;
                                                                                                hashMap4 = hashMap;
                                                                                            }
                                                                                        } else {
                                                                                            str = str2;
                                                                                            method2 = method22;
                                                                                            arrayList = arrayList5;
                                                                                            hashMap = hashMap5;
                                                                                            method6 = method14;
                                                                                        }
                                                                                        arrayList5 = arrayList;
                                                                                        method29 = method8;
                                                                                        method13 = method7;
                                                                                        method14 = method6;
                                                                                        declaredField2 = field;
                                                                                        declaredMethod8 = method5;
                                                                                        method = method4;
                                                                                        method24 = method3;
                                                                                        method25 = method2;
                                                                                        hashMap5 = hashMap;
                                                                                        str2 = str;
                                                                                    } catch (Throwable th10) {
                                                                                        th = th10;
                                                                                        method4 = method30;
                                                                                        method3 = method23;
                                                                                        method2 = method22;
                                                                                        arrayList = arrayList5;
                                                                                        hashMap = hashMap5;
                                                                                        method6 = method14;
                                                                                        Log.e("OnLineMonitor", "分析内存异常", th);
                                                                                        arrayList5 = arrayList;
                                                                                        declaredMethod4 = method8;
                                                                                        method27 = method7;
                                                                                        declaredMethod6 = method6;
                                                                                        list2 = list;
                                                                                        size = i2;
                                                                                        cls7 = cls;
                                                                                        declaredField2 = field;
                                                                                        declaredMethod8 = method5;
                                                                                        method26 = method4;
                                                                                        declaredMethod10 = method3;
                                                                                        declaredMethod7 = method2;
                                                                                        hashMap4 = hashMap;
                                                                                    }
                                                                                }
                                                                            } catch (Throwable th11) {
                                                                                th = th11;
                                                                                method5 = declaredMethod8;
                                                                                field = declaredField2;
                                                                                method4 = method30;
                                                                                method3 = method23;
                                                                                method2 = method22;
                                                                                arrayList = arrayList5;
                                                                                hashMap = hashMap5;
                                                                                method6 = method14;
                                                                                method7 = method13;
                                                                                Log.e("OnLineMonitor", "分析内存异常", th);
                                                                                arrayList5 = arrayList;
                                                                                declaredMethod4 = method8;
                                                                                method27 = method7;
                                                                                declaredMethod6 = method6;
                                                                                list2 = list;
                                                                                size = i2;
                                                                                cls7 = cls;
                                                                                declaredField2 = field;
                                                                                declaredMethod8 = method5;
                                                                                method26 = method4;
                                                                                declaredMethod10 = method3;
                                                                                declaredMethod7 = method2;
                                                                                hashMap4 = hashMap;
                                                                            }
                                                                        }
                                                                    } catch (Throwable th12) {
                                                                        th = th12;
                                                                        method5 = declaredMethod8;
                                                                        field = declaredField2;
                                                                        method4 = method30;
                                                                        method3 = method23;
                                                                        arrayList = arrayList5;
                                                                        hashMap = hashMap5;
                                                                        method6 = method14;
                                                                        method7 = method13;
                                                                        method2 = method12;
                                                                        Log.e("OnLineMonitor", "分析内存异常", th);
                                                                        arrayList5 = arrayList;
                                                                        declaredMethod4 = method8;
                                                                        method27 = method7;
                                                                        declaredMethod6 = method6;
                                                                        list2 = list;
                                                                        size = i2;
                                                                        cls7 = cls;
                                                                        declaredField2 = field;
                                                                        declaredMethod8 = method5;
                                                                        method26 = method4;
                                                                        declaredMethod10 = method3;
                                                                        declaredMethod7 = method2;
                                                                        hashMap4 = hashMap;
                                                                    }
                                                                }
                                                            }
                                                            method8 = method29;
                                                            method22 = method12;
                                                        } catch (Throwable th13) {
                                                            th = th13;
                                                            method5 = declaredMethod8;
                                                            field = declaredField2;
                                                            method4 = method30;
                                                            method3 = method23;
                                                            arrayList = arrayList5;
                                                            method8 = method29;
                                                            hashMap = hashMap5;
                                                            method6 = method14;
                                                            method7 = method13;
                                                            method2 = method12;
                                                            Log.e("OnLineMonitor", "分析内存异常", th);
                                                            arrayList5 = arrayList;
                                                            declaredMethod4 = method8;
                                                            method27 = method7;
                                                            declaredMethod6 = method6;
                                                            list2 = list;
                                                            size = i2;
                                                            cls7 = cls;
                                                            declaredField2 = field;
                                                            declaredMethod8 = method5;
                                                            method26 = method4;
                                                            declaredMethod10 = method3;
                                                            declaredMethod7 = method2;
                                                            hashMap4 = hashMap;
                                                        }
                                                    } else {
                                                        method8 = method29;
                                                        method22 = method12;
                                                        method23 = method11;
                                                    }
                                                    method = method30;
                                                    method24 = method23;
                                                    method25 = method22;
                                                    method29 = method8;
                                                } catch (Throwable th14) {
                                                    th = th14;
                                                    method20 = declaredMethod8;
                                                    field3 = declaredField2;
                                                    method21 = method30;
                                                    arrayList3 = arrayList5;
                                                    method8 = method29;
                                                    hashMap = hashMap5;
                                                    method6 = method14;
                                                    method7 = method13;
                                                    method2 = method12;
                                                    method3 = method11;
                                                    Log.e("OnLineMonitor", "分析内存异常", th);
                                                    arrayList5 = arrayList;
                                                    declaredMethod4 = method8;
                                                    method27 = method7;
                                                    declaredMethod6 = method6;
                                                    list2 = list;
                                                    size = i2;
                                                    cls7 = cls;
                                                    declaredField2 = field;
                                                    declaredMethod8 = method5;
                                                    method26 = method4;
                                                    declaredMethod10 = method3;
                                                    declaredMethod7 = method2;
                                                    hashMap4 = hashMap;
                                                }
                                            }
                                        } catch (Throwable th15) {
                                            th = th15;
                                            method20 = declaredMethod8;
                                            field3 = declaredField2;
                                            arrayList3 = arrayList5;
                                            method21 = method;
                                            method8 = method29;
                                            hashMap = hashMap5;
                                            method6 = method14;
                                            method7 = method13;
                                            method2 = method12;
                                            method3 = method11;
                                            Log.e("OnLineMonitor", "分析内存异常", th);
                                            arrayList5 = arrayList;
                                            declaredMethod4 = method8;
                                            method27 = method7;
                                            declaredMethod6 = method6;
                                            list2 = list;
                                            size = i2;
                                            cls7 = cls;
                                            declaredField2 = field;
                                            declaredMethod8 = method5;
                                            method26 = method4;
                                            declaredMethod10 = method3;
                                            declaredMethod7 = method2;
                                            hashMap4 = hashMap;
                                        }
                                    }
                                    method15 = declaredMethod8;
                                    field2 = declaredField2;
                                    arrayList2 = arrayList5;
                                    method16 = method;
                                    method17 = method29;
                                    hashMap2 = hashMap5;
                                    method18 = method14;
                                    method19 = method13;
                                    method9 = method12;
                                    method10 = method11;
                                    arrayList5 = arrayList;
                                    declaredMethod4 = method8;
                                    method27 = method7;
                                    declaredMethod6 = method6;
                                    list2 = list;
                                    size = i2;
                                    cls7 = cls;
                                    declaredField2 = field;
                                    declaredMethod8 = method5;
                                    method26 = method4;
                                    declaredMethod10 = method3;
                                    declaredMethod7 = method2;
                                    hashMap4 = hashMap;
                                }
                            }
                            method9 = declaredMethod7;
                            method10 = declaredMethod10;
                        }
                        method15 = declaredMethod8;
                        field2 = declaredField2;
                        hashMap2 = hashMap4;
                        i = i3;
                        i2 = size;
                        cls = cls7;
                        arrayList2 = arrayList5;
                        method16 = method;
                        method19 = method27;
                        method18 = declaredMethod6;
                        method17 = declaredMethod4;
                        arrayList5 = arrayList;
                        declaredMethod4 = method8;
                        method27 = method7;
                        declaredMethod6 = method6;
                        list2 = list;
                        size = i2;
                        cls7 = cls;
                        declaredField2 = field;
                        declaredMethod8 = method5;
                        method26 = method4;
                        declaredMethod10 = method3;
                        declaredMethod7 = method2;
                        hashMap4 = hashMap;
                    }
                    arrayList = arrayList5;
                } catch (Throwable th16) {
                    th = th16;
                    arrayList = arrayList5;
                    Log.e("OnLineMonitor", "分析内存失败", th);
                    this.mHeapDumpFile.delete();
                    Collections.sort(arrayList, new MemoryComparator());
                    Collections.sort(this.mMemoryUsedList, new MemoryUsedComparator());
                    return arrayList;
                }
            } catch (Throwable th17) {
                th = th17;
                arrayList = arrayList4;
                Log.e("OnLineMonitor", "分析内存失败", th);
                this.mHeapDumpFile.delete();
                Collections.sort(arrayList, new MemoryComparator());
                Collections.sort(this.mMemoryUsedList, new MemoryUsedComparator());
                return arrayList;
            }
            this.mHeapDumpFile.delete();
            Collections.sort(arrayList, new MemoryComparator());
            if (this.mMemoryUsedList != null && this.mMemoryUsedList.size() > 0) {
                Collections.sort(this.mMemoryUsedList, new MemoryUsedComparator());
            }
            return arrayList;
        } catch (IOException e) {
            Log.e("OnLineMonitor", "Failed to dump memory", e);
            return null;
        }
    }

    static class MemoryComparator implements Comparator<StaticOwner> {
        MemoryComparator() {
        }

        public int compare(StaticOwner staticOwner, StaticOwner staticOwner2) {
            return (int) (staticOwner2.totalRetainedHeapSize - staticOwner.totalRetainedHeapSize);
        }
    }

    static class MemoryUsedComparator implements Comparator<StaticVariable> {
        MemoryUsedComparator() {
        }

        public int compare(StaticVariable staticVariable, StaticVariable staticVariable2) {
            return (int) (staticVariable2.retainedHeapSize - staticVariable.retainedHeapSize);
        }
    }

    public static class StaticOwner implements Serializable {
        public String className;
        public long totalRetainedHeapSize;
        public long totalShadowHeapSize;
        public List<StaticVariable> variables;

        public StaticOwner(String str) {
            this.className = str;
        }

        public void addStaticVariable(StaticVariable staticVariable) {
            if (this.variables == null) {
                this.variables = new ArrayList();
            }
            this.totalRetainedHeapSize += staticVariable.retainedHeapSize;
            this.totalShadowHeapSize += staticVariable.shadowHeapSize;
            this.variables.add(staticVariable);
        }
    }
}
