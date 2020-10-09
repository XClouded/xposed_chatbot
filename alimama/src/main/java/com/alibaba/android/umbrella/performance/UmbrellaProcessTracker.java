package com.alibaba.android.umbrella.performance;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import androidx.annotation.Keep;
import com.alibaba.android.umbrella.performance.ProcessEvent;
import com.alibaba.android.umbrella.trace.UmbrellaSimple;
import com.alibaba.android.umbrella.trace.UmbrellaUtils;
import com.alibaba.android.umbrella.utils.UmbrellaConstants;
import java.lang.Thread;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Keep
public class UmbrellaProcessTracker {
    private static final ConcurrentHashMap<String, Long> sPageList = new ConcurrentHashMap<>();
    private static Handler trackHandler;
    private static HandlerThread trackHandlerThread = new HandlerThread(UmbrellaConstants.PERFORMANCE_HANDLER_THREAD_NAME);

    private static synchronized void init() {
        synchronized (UmbrellaProcessTracker.class) {
            if (trackHandlerThread.getState() == Thread.State.NEW) {
                trackHandlerThread.start();
                trackHandler = new Handler(trackHandlerThread.getLooper()) {
                    public void handleMessage(Message message) {
                        super.handleMessage(message);
                        ProcessEvent processEvent = (ProcessEvent) message.obj;
                        if (processEvent.eventType == 3) {
                            ProcessRecord.recordProcess(processEvent);
                        } else if (processEvent.eventType == 4) {
                            ProcessRecord.recordSubProcess(processEvent);
                        } else if (processEvent.eventType == 1) {
                            ProcessRecord.register(processEvent);
                        } else if (processEvent.eventType == 2) {
                            ProcessRecord.recordArgs(processEvent);
                        } else if (processEvent.eventType == 5) {
                            ProcessRecord.commitPerformance(processEvent);
                        } else if (processEvent.eventType == 7) {
                            ProcessRecord.setChildBizName(processEvent);
                        } else if (processEvent.eventType == 6) {
                            ProcessRecord.addAbInfo(processEvent);
                        } else if (processEvent.eventType == 8) {
                            ProcessRecord.recordOtherProcess(processEvent);
                        }
                    }
                };
            }
        }
    }

    public static void register(String str) {
        if (!UmbrellaUtils.checkForceCloseAndBizName(str)) {
            if ((UmbrellaSimple.isOpenGrayReport() && UmbrellaSimple.getIsGrayVersion()) || UmbrellaSimple.getPerformancePageSampleResult(str)) {
                if (trackHandlerThread.getState() == Thread.State.NEW) {
                    init();
                }
                recordBiz(str);
                sendProcessEvent(new ProcessEvent.ProcessEventBuilder(str).eventType(1).build());
            }
        }
    }

    public static void commit(String str) {
        if (!UmbrellaUtils.checkForceCloseAndBizName(str) && isContainBiz(str)) {
            removeBiz(str);
            sendProcessEvent(new ProcessEvent.ProcessEventBuilder(str, 0).eventType(5).build());
        }
    }

    public static void addProcess(String str, UmbrellaProcess umbrellaProcess, long j) {
        if (!UmbrellaUtils.checkForceCloseAndBizName(str) && umbrellaProcess != null && isContainBiz(str)) {
            sendProcessEvent(new ProcessEvent.ProcessEventBuilder(str).eventType(3).umbrellaProcess(umbrellaProcess).costTime(j).build());
        }
    }

    public static void addSubProcess(String str, UmbrellaProcess umbrellaProcess, String str2, long j) {
        if (!UmbrellaUtils.checkForceCloseAndBizName(str) && umbrellaProcess != null && !TextUtils.isEmpty(str2) && isContainBiz(str)) {
            sendProcessEvent(new ProcessEvent.ProcessEventBuilder(str).eventType(4).umbrellaProcess(umbrellaProcess).eventPoint(str2).costTime(j).build());
        }
    }

    public static void addArgs(String str, Map<String, String> map) {
        if (!UmbrellaUtils.checkForceCloseAndBizName(str) && map != null && map.size() >= 1 && isContainBiz(str)) {
            sendProcessEvent(new ProcessEvent.ProcessEventBuilder(str).eventType(2).args(map).build());
        }
    }

    public static void addArgs(String str, String str2, String str3) {
        if (!TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str3)) {
            HashMap hashMap = new HashMap();
            hashMap.put(str2, str3);
            addArgs(str, hashMap);
        }
    }

    public static void setChildBizName(String str, String str2) {
        if (!UmbrellaUtils.checkForceCloseAndBizName(str) && !TextUtils.isEmpty(str2) && isContainBiz(str)) {
            sendProcessEvent(new ProcessEvent.ProcessEventBuilder(str).eventType(7).childBizName(str2).build());
        }
    }

    public static void addAbTestInfo(String str, String str2, String str3) {
        if (!UmbrellaUtils.checkForceCloseAndBizName(str) && !TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str3) && isContainBiz(str)) {
            sendProcessEvent(new ProcessEvent.ProcessEventBuilder(str).eventType(6).abId(str2).abBucket(str3).build());
        }
    }

    public static void addPageLoad(String str, long j) {
        if (!UmbrellaUtils.checkForceCloseAndBizName(str) && isContainBiz(str)) {
            sendProcessEvent(new ProcessEvent.ProcessEventBuilder(str).eventType(3).umbrellaProcess(UmbrellaProcess.PAGELOAD).costTime(j).build());
        }
    }

    public static void addOtherProcess(String str, String str2, long j) {
        if (!UmbrellaUtils.checkForceCloseAndBizName(str) && !TextUtils.isEmpty(str2) && isContainBiz(str)) {
            sendProcessEvent(new ProcessEvent.ProcessEventBuilder(str).eventType(8).costTime(j).eventPoint(str2).build());
        }
    }

    private static void sendProcessEvent(ProcessEvent processEvent) {
        if (trackHandler != null) {
            Message obtainMessage = trackHandler.obtainMessage();
            obtainMessage.obj = processEvent;
            trackHandler.sendMessage(obtainMessage);
        }
    }

    private static boolean isContainBiz(String str) {
        Long l;
        if (!sPageList.containsKey(str) || (l = sPageList.get(str)) == null) {
            return false;
        }
        if (System.currentTimeMillis() - l.longValue() < UmbrellaConstants.PERFORMANCE_DATA_ALIVE) {
            return true;
        }
        removeBiz(str);
        return false;
    }

    private static void recordBiz(String str) {
        sPageList.put(str, Long.valueOf(System.currentTimeMillis()));
    }

    private static void removeBiz(String str) {
        sPageList.remove(str);
    }
}
