package com.taobao.phenix.chain;

import com.taobao.phenix.bitmap.BitmapProcessProducer;
import com.taobao.phenix.cache.disk.DiskCacheReader;
import com.taobao.phenix.cache.memory.MemoryCacheProducer;
import com.taobao.phenix.decode.DecodeProducer;
import com.taobao.phenix.intf.event.IPhenixListener;
import com.taobao.phenix.intf.event.MemCacheMissPhenixEvent;
import com.taobao.phenix.loader.file.LocalImageProducer;
import com.taobao.phenix.loader.network.NetworkImageProducer;
import com.taobao.phenix.request.ImageRequest;
import com.taobao.phenix.request.ImageStatistics;
import com.taobao.rxm.consume.Consumer;
import com.taobao.rxm.produce.ProducerListener;
import com.taobao.rxm.schedule.ScheduleResultWrapper;
import com.taobao.rxm.schedule.ScheduledAction;
import com.taobao.rxm.schedule.Scheduler;
import com.taobao.tcommon.core.RuntimeUtil;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PhenixProduceListener implements ProducerListener<ImageRequest> {
    private final ImageDecodingListener mImageDecodingListener;
    /* access modifiers changed from: private */
    public final IPhenixListener<MemCacheMissPhenixEvent> mMemMissListener;
    private Scheduler mMemMissScheduler;
    private Map<String, Long> mProduceTimeline = new ConcurrentHashMap();
    /* access modifiers changed from: private */
    public final ImageRequest mRequest;
    private ScheduledAction mScheduleAction;

    public static class MonitorNode {
        public ImageStatistics.FromType from;
        public String key;

        public MonitorNode(String str, ImageStatistics.FromType fromType) {
            this.key = str;
            this.from = fromType;
        }
    }

    public PhenixProduceListener(ImageRequest imageRequest, IPhenixListener<MemCacheMissPhenixEvent> iPhenixListener, ImageDecodingListener imageDecodingListener) {
        this.mMemMissListener = iPhenixListener;
        this.mRequest = imageRequest;
        this.mImageDecodingListener = imageDecodingListener;
    }

    public void setMemMissScheduler(Scheduler scheduler) {
        this.mMemMissScheduler = scheduler;
    }

    private MonitorNode getMonitorNodeFromProducer(Class cls, boolean z) {
        if (cls == MemoryCacheProducer.class) {
            return new MonitorNode(ImageStatistics.KEY_READ_MEMORY_CACHE, ImageStatistics.FromType.FROM_MEMORY_CACHE);
        }
        if (cls == LocalImageProducer.class) {
            return new MonitorNode(ImageStatistics.KEY_READ_LOCAL_FILE, ImageStatistics.FromType.FROM_LOCAL_FILE);
        }
        if (cls == DiskCacheReader.class) {
            return new MonitorNode(ImageStatistics.KEY_READ_DISK_CACHE, ImageStatistics.FromType.FROM_DISK_CACHE);
        }
        if (cls == NetworkImageProducer.class) {
            return new MonitorNode(z ? "download" : "connect", ImageStatistics.FromType.FROM_NETWORK);
        } else if (cls == BitmapProcessProducer.class) {
            return new MonitorNode(z ? ImageStatistics.KEY_BITMAP_PROCESS : ImageStatistics.KEY_BITMAP_SCALE, z ? ImageStatistics.FromType.FROM_UNKNOWN : ImageStatistics.FromType.FROM_LARGE_SCALE);
        } else if (cls == DecodeProducer.class) {
            return new MonitorNode(ImageStatistics.KEY_BITMAP_DECODE, ImageStatistics.FromType.FROM_UNKNOWN);
        } else {
            return null;
        }
    }

    public void onEnterIn(ImageRequest imageRequest, Class cls, boolean z, boolean z2) {
        MonitorNode monitorNodeFromProducer;
        if ((!z || z2) && (monitorNodeFromProducer = getMonitorNodeFromProducer(cls, z)) != null && monitorNodeFromProducer.key != null) {
            this.mProduceTimeline.put(monitorNodeFromProducer.key, Long.valueOf(0 - System.currentTimeMillis()));
            if (this.mImageDecodingListener != null && cls == DecodeProducer.class) {
                this.mImageDecodingListener.onDecodeStart((long) imageRequest.getId(), imageRequest.getPath());
            }
        }
    }

    public void onExitOut(ImageRequest imageRequest, Class cls, boolean z, boolean z2, boolean z3) {
        MonitorNode monitorNodeFromProducer;
        long currentTimeMillis = System.currentTimeMillis();
        callMemMissListenerIfNeed(cls, z, z2);
        if ((!z || z3) && (monitorNodeFromProducer = getMonitorNodeFromProducer(cls, z)) != null && monitorNodeFromProducer.key != null) {
            Long l = this.mProduceTimeline.get(monitorNodeFromProducer.key);
            if (l != null && l.longValue() < 0) {
                this.mProduceTimeline.put(monitorNodeFromProducer.key, Long.valueOf(currentTimeMillis + l.longValue()));
            }
            if (z2 && monitorNodeFromProducer.from != ImageStatistics.FromType.FROM_UNKNOWN) {
                this.mRequest.getStatistics().fromType(monitorNodeFromProducer.from);
            }
            if (this.mImageDecodingListener != null && cls == DecodeProducer.class) {
                this.mImageDecodingListener.onDecodeFinish((long) imageRequest.getId(), imageRequest.getPath());
            }
        }
    }

    private void callMemMissListenerIfNeed(Class cls, boolean z, boolean z2) {
        if (this.mMemMissListener != null && !z && !z2 && cls == MemoryCacheProducer.class) {
            if (this.mMemMissScheduler == null || (this.mMemMissScheduler.isScheduleMainThread() && RuntimeUtil.isMainThread())) {
                this.mMemMissListener.onHappen(new MemCacheMissPhenixEvent(this.mRequest.getPhenixTicket()));
                return;
            }
            if (this.mScheduleAction == null) {
                this.mScheduleAction = new ScheduledAction(3, (Consumer) null, (ScheduleResultWrapper) null) {
                    public void run(Consumer consumer, ScheduleResultWrapper scheduleResultWrapper) {
                        MemCacheMissPhenixEvent memCacheMissPhenixEvent = new MemCacheMissPhenixEvent(PhenixProduceListener.this.mRequest.getPhenixTicket());
                        memCacheMissPhenixEvent.setUrl(PhenixProduceListener.this.mRequest.getPath());
                        PhenixProduceListener.this.mMemMissListener.onHappen(memCacheMissPhenixEvent);
                    }
                };
            }
            this.mMemMissScheduler.schedule(this.mScheduleAction);
        }
    }

    public Map<String, Long> getProduceTimeline() {
        return this.mProduceTimeline;
    }
}
