package com.alibaba.motu.watch.mainRunLoop;

import android.util.Log;
import com.alibaba.motu.tbrest.SendService;
import com.alibaba.motu.tbrest.utils.Base64;
import com.alibaba.motu.tbrest.utils.GzipUtils;
import com.alibaba.motu.tbrest.utils.StringUtils;
import com.alibaba.motu.watch.IWatchListener;
import com.alibaba.motu.watch.MotuWatch;
import com.alibaba.motu.watch.WatchConfig;
import com.alibaba.motu.watch.stack.ThreadMsg;
import com.alibaba.motu.watch.stack.ThreadSerialization;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainLooperHandler extends Thread {
    private final String ACTIVITY_PATH = "_controller_path";
    private String activityPath = null;
    private String stackHashValue = null;

    public void run() {
        try {
            startDump();
        } catch (Exception e) {
            Log.e(WatchConfig.TAG, "main looper handler error.", e);
        }
    }

    public void startDump() {
        ThreadMsg New = ThreadMsg.New("", false);
        ThreadSerialization threadSerialization = new ThreadSerialization();
        String threadMsg = New.toString();
        String serialization = threadSerialization.serialization(New.getStackTraces(), false);
        String serialization2 = threadSerialization.serialization(ThreadMsg.getMainThread(), true);
        String format = String.format("%s", new Object[]{Integer.valueOf(StringUtils.hashCode(serialization2))});
        if (this.stackHashValue == null || format == null || !this.stackHashValue.equals(format)) {
            List<IWatchListener> myWatchListenerList = MotuWatch.getInstance().getMyWatchListenerList();
            String str = null;
            if (myWatchListenerList != null) {
                str = callBackListener(myWatchListenerList);
            }
            HashMap hashMap = new HashMap();
            if (threadMsg == null) {
                threadMsg = "-";
            }
            hashMap.put("exceptionType", threadMsg);
            if (str != null) {
                hashMap.put("callBackData", str);
            }
            if (serialization2 == null) {
                serialization2 = "-";
            }
            try {
                String encodeBase64String = Base64.encodeBase64String(GzipUtils.gzip(serialization2.getBytes()));
                if (encodeBase64String != null) {
                    hashMap.put("mainThread", encodeBase64String);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (serialization != null) {
                try {
                    byte[] gzip = GzipUtils.gzip(serialization.getBytes());
                    if (gzip != null) {
                        if (SendService.getInstance().sendRequest((String) null, System.currentTimeMillis(), this.activityPath, 61005, "ANDROID_MAINTHREAD_BLOCK", Base64.encodeBase64String(gzip), "-", hashMap).booleanValue()) {
                            Log.d(WatchConfig.TAG, "send main thread block success");
                        }
                    }
                } catch (Exception e2) {
                    Log.e(WatchConfig.TAG, "build main thread block err", e2);
                }
            }
        }
        this.stackHashValue = format;
    }

    private String callBackListener(List<IWatchListener> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            IWatchListener iWatchListener = list.get(i);
            try {
                iWatchListener.onWatch((Map<String, Object>) null);
                Map<String, String> onCatch = iWatchListener.onCatch();
                if (onCatch != null) {
                    String str = onCatch.get("_controller_path");
                    if (str != null) {
                        this.activityPath = str;
                    }
                    for (Map.Entry next : onCatch.entrySet()) {
                        if (!(next == null || next.getKey() == null || next.getValue() == null)) {
                            sb.append((String) next.getKey());
                            sb.append(" : ");
                            sb.append((String) next.getValue());
                            sb.append("\n");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
