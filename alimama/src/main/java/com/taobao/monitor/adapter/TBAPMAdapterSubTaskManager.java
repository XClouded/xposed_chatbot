package com.taobao.monitor.adapter;

import android.os.Looper;
import android.os.SystemClock;
import com.taobao.monitor.ProcedureGlobal;
import com.taobao.monitor.impl.util.TimeUtils;
import com.taobao.monitor.procedure.IProcedure;
import com.taobao.monitor.procedure.ProcedureConfig;
import com.taobao.monitor.procedure.ProcedureFactoryProxy;
import com.taobao.monitor.procedure.ProcedureManagerProxy;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TBAPMAdapterSubTaskManager {
    /* access modifiers changed from: private */
    public static boolean isPendingState = true;
    /* access modifiers changed from: private */
    public static Map<String, SubTask> mPendingTasks = new HashMap();
    /* access modifiers changed from: private */
    public static Map<String, IProcedure> sProcedureHashMap = new HashMap();

    private static class SubTask {
        /* access modifiers changed from: private */
        public long cpuEndTime;
        /* access modifiers changed from: private */
        public long cpuStartTime;
        /* access modifiers changed from: private */
        public long endTime;
        /* access modifiers changed from: private */
        public boolean isMainThread;
        /* access modifiers changed from: private */
        public long startTime;
        /* access modifiers changed from: private */
        public String threadName;

        private SubTask() {
        }
    }

    public static void onStartTask(String str) {
        final long currentTimeMillis = TimeUtils.currentTimeMillis();
        final long currentThreadTimeMillis = SystemClock.currentThreadTimeMillis();
        final String name = Thread.currentThread().getName();
        final boolean z = Thread.currentThread() == Looper.getMainLooper().getThread();
        final String str2 = str;
        async(new Runnable() {
            public void run() {
                if (!TBAPMAdapterSubTaskManager.isPendingState) {
                    ProcedureConfig build = new ProcedureConfig.Builder().setIndependent(false).setUpload(false).setParentNeedStats(false).setParent(ProcedureManagerProxy.PROXY.getLauncherProcedure()).build();
                    ProcedureFactoryProxy procedureFactoryProxy = ProcedureFactoryProxy.PROXY;
                    IProcedure createProcedure = procedureFactoryProxy.createProcedure("/" + str2, build);
                    TBAPMAdapterSubTaskManager.sProcedureHashMap.put(str2, createProcedure);
                    createProcedure.begin();
                    createProcedure.stage("taskStart", currentTimeMillis);
                    createProcedure.stage("cpuStartTime", currentThreadTimeMillis);
                    createProcedure.addProperty("threadName", name);
                    createProcedure.addProperty("isMainThread", Boolean.valueOf(z));
                } else if (!TBAPMAdapterSubTaskManager.mPendingTasks.keySet().contains(str2)) {
                    SubTask subTask = new SubTask();
                    long unused = subTask.startTime = currentTimeMillis;
                    long unused2 = subTask.cpuStartTime = currentThreadTimeMillis;
                    boolean unused3 = subTask.isMainThread = z;
                    String unused4 = subTask.threadName = name;
                    TBAPMAdapterSubTaskManager.mPendingTasks.put(str2, subTask);
                }
            }
        });
    }

    public static void onEndTask(String str) {
        final long currentTimeMillis = TimeUtils.currentTimeMillis();
        final long currentThreadTimeMillis = SystemClock.currentThreadTimeMillis();
        final String str2 = str;
        async(new Runnable() {
            public void run() {
                if (!TBAPMAdapterSubTaskManager.isPendingState) {
                    IProcedure iProcedure = (IProcedure) TBAPMAdapterSubTaskManager.sProcedureHashMap.get(str2);
                    SubTask subTask = (SubTask) TBAPMAdapterSubTaskManager.mPendingTasks.get(str2);
                    if (iProcedure == null && subTask != null) {
                        ProcedureConfig build = new ProcedureConfig.Builder().setIndependent(false).setUpload(false).setParentNeedStats(false).setParent(ProcedureManagerProxy.PROXY.getLauncherProcedure()).build();
                        ProcedureFactoryProxy procedureFactoryProxy = ProcedureFactoryProxy.PROXY;
                        iProcedure = procedureFactoryProxy.createProcedure("/" + str2, build);
                        iProcedure.begin();
                        iProcedure.stage("taskStart", subTask.startTime);
                        iProcedure.stage("cpuStartTime", subTask.cpuStartTime);
                        iProcedure.addProperty("isMainThread", Boolean.valueOf(subTask.isMainThread));
                        iProcedure.addProperty("threadName", subTask.threadName);
                        TBAPMAdapterSubTaskManager.mPendingTasks.remove(str2);
                    }
                    if (iProcedure != null) {
                        iProcedure.stage("taskEnd", currentTimeMillis);
                        iProcedure.stage("cpuEndTime", currentThreadTimeMillis);
                        iProcedure.end();
                        TBAPMAdapterSubTaskManager.sProcedureHashMap.remove(str2);
                    }
                } else if (TBAPMAdapterSubTaskManager.mPendingTasks.keySet().contains(str2)) {
                    SubTask subTask2 = (SubTask) TBAPMAdapterSubTaskManager.mPendingTasks.get(str2);
                    long unused = subTask2.endTime = currentTimeMillis;
                    long unused2 = subTask2.cpuEndTime = currentThreadTimeMillis;
                }
            }
        });
    }

    protected static void transferPendingTasks() {
        async(new Runnable() {
            public void run() {
                Iterator it = TBAPMAdapterSubTaskManager.mPendingTasks.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    String str = (String) entry.getKey();
                    SubTask subTask = (SubTask) entry.getValue();
                    if (subTask.endTime != 0) {
                        ProcedureConfig build = new ProcedureConfig.Builder().setIndependent(false).setUpload(false).setParentNeedStats(false).setParent(ProcedureManagerProxy.PROXY.getLauncherProcedure()).build();
                        ProcedureFactoryProxy procedureFactoryProxy = ProcedureFactoryProxy.PROXY;
                        IProcedure createProcedure = procedureFactoryProxy.createProcedure("/" + str, build);
                        createProcedure.begin();
                        createProcedure.stage("taskStart", subTask.startTime);
                        createProcedure.stage("cpuStartTime", subTask.cpuStartTime);
                        createProcedure.addProperty("isMainThread", Boolean.valueOf(subTask.isMainThread));
                        createProcedure.addProperty("threadName", subTask.threadName);
                        createProcedure.stage("taskEnd", subTask.endTime);
                        createProcedure.stage("cpuEndTime", subTask.cpuEndTime);
                        createProcedure.end();
                        it.remove();
                    }
                }
                boolean unused = TBAPMAdapterSubTaskManager.isPendingState = false;
            }
        });
    }

    private static void async(Runnable runnable) {
        ProcedureGlobal.instance().handler().post(runnable);
    }
}
