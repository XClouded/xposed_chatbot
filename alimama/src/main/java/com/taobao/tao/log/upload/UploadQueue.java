package com.taobao.tao.log.upload;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UploadQueue {
    private String TAG;
    private Map<String, FileUploadListener> fileUploadListenerMap;

    private UploadQueue() {
        this.TAG = "TLOG.UploadQueue";
        this.fileUploadListenerMap = new ConcurrentHashMap();
    }

    private static class CreateInstance {
        /* access modifiers changed from: private */
        public static UploadQueue instance = new UploadQueue();

        private CreateInstance() {
        }
    }

    public static synchronized UploadQueue getInstance() {
        UploadQueue access$100;
        synchronized (UploadQueue.class) {
            access$100 = CreateInstance.instance;
        }
        return access$100;
    }

    public void pushListener(String str, FileUploadListener fileUploadListener) {
        if (str != null && fileUploadListener != null) {
            this.fileUploadListenerMap.put(str, fileUploadListener);
        }
    }

    public FileUploadListener popListener(String str) {
        FileUploadListener fileUploadListener = this.fileUploadListenerMap.get(str);
        if (fileUploadListener == null) {
            return null;
        }
        this.fileUploadListenerMap.remove(str);
        return fileUploadListener;
    }
}
