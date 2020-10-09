package com.taobao.android.dinamic.tempate;

import android.os.AsyncTask;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.taobao.android.dinamic.log.DinamicLog;
import com.taobao.android.dinamic.tempate.manager.LayoutFileManager;
import com.taobao.android.dinamic.tempate.manager.TemplatePerfInfo;
import com.taobao.uikit.extend.component.unify.Toast.TBToast;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SerialTaskManager {
    private static final String TAG = "SerialTaskManager";
    private volatile DownLoadTask mActive;
    final ArrayDeque<DownLoadTask> mTasks = new ArrayDeque<>();

    public interface LayoutFileLoadListener {
        void onFinished(DownloadResult downloadResult);
    }

    public synchronized void execute(DownLoadTask downLoadTask) {
        SerialTaskManager unused = downLoadTask.taskManager = this;
        this.mTasks.offer(downLoadTask);
        if (this.mActive == null) {
            scheduleNext();
        }
    }

    /* access modifiers changed from: private */
    public synchronized void scheduleNext() {
        DownLoadTask poll = this.mTasks.poll();
        this.mActive = poll;
        if (poll != null) {
            this.mActive.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        }
    }

    public static final class DownLoadTask extends AsyncTask<Void, DownloadResult, DownloadResult> {
        private ArrayList<DinamicTemplate> alreadyExistTemplates = new ArrayList<>();
        /* access modifiers changed from: private */
        public ArrayList<DinamicTemplate> failedTemplates = new ArrayList<>();
        /* access modifiers changed from: private */
        public ArrayList<DinamicTemplate> finishedTemplates = new ArrayList<>();
        private long interval = TBToast.Duration.MEDIUM;
        private TimerTask intervalRun = new TimerTask() {
            public void run() {
                synchronized (DownLoadTask.this) {
                    if (!DownLoadTask.this.isFinished) {
                        try {
                            if (DownLoadTask.this.finishedTemplates.size() > 0 || DownLoadTask.this.failedTemplates.size() > 0) {
                                DownLoadTask.this.publishProgress(new DownloadResult[]{DownLoadTask.this.buildResult()});
                                DownLoadTask.this.finishedTemplates.clear();
                                DownLoadTask.this.failedTemplates.clear();
                            }
                        } catch (Exception e) {
                            DinamicLog.e(SerialTaskManager.TAG, (Throwable) e, "callback onFinished is error");
                        }
                    }
                }
            }
        };
        /* access modifiers changed from: private */
        public volatile boolean isFinished;
        private final LayoutFileManager layoutFileManager;
        LayoutFileLoadListener listener;
        String module;
        /* access modifiers changed from: private */
        public SerialTaskManager taskManager;
        List<DinamicTemplate> templates;
        private Timer timer;
        private ArrayList<DinamicTemplate> totalFailedTemplates = new ArrayList<>();
        private ArrayList<DinamicTemplate> totalFinishedTemplates = new ArrayList<>();

        public DownLoadTask(LayoutFileManager layoutFileManager2) {
            this.layoutFileManager = layoutFileManager2;
        }

        public DownLoadTask(LayoutFileManager layoutFileManager2, int i) {
            this.layoutFileManager = layoutFileManager2;
            this.interval = (long) i;
        }

        /* access modifiers changed from: private */
        public DownloadResult buildResult() {
            DownloadResult downloadResult = new DownloadResult();
            downloadResult.isFinished = this.isFinished;
            downloadResult.finishedTemplates = (ArrayList) this.finishedTemplates.clone();
            downloadResult.failedTemplates = (ArrayList) this.failedTemplates.clone();
            downloadResult.totalFinishedTemplates = (ArrayList) this.totalFinishedTemplates.clone();
            downloadResult.totalFailedTemplates = (ArrayList) this.totalFailedTemplates.clone();
            downloadResult.alreadyExistTemplates = (ArrayList) this.alreadyExistTemplates.clone();
            return downloadResult;
        }

        /* access modifiers changed from: protected */
        public DownloadResult doInBackground(Void... voidArr) {
            byte[] bArr;
            if (this.templates == null || this.templates.isEmpty()) {
                this.isFinished = true;
                return buildResult();
            }
            HashSet hashSet = new HashSet();
            for (DinamicTemplate next : this.templates) {
                if (next == null || TextUtils.isEmpty(next.templateUrl) || TextUtils.isEmpty(next.name) || TextUtils.isEmpty(next.version)) {
                    this.failedTemplates.add(next);
                    this.totalFailedTemplates.add(next);
                } else {
                    LayoutFileRequest checkNeedDownload = checkNeedDownload(next);
                    if (checkNeedDownload == null) {
                        this.alreadyExistTemplates.add(next);
                    } else {
                        hashSet.add(checkNeedDownload);
                    }
                }
            }
            if (!hashSet.isEmpty()) {
                this.timer = new Timer();
                this.timer.schedule(this.intervalRun, this.interval, this.interval);
                ArrayList arrayList = new ArrayList(hashSet);
                int size = arrayList.size();
                for (int i = 0; i < size; i++) {
                    LayoutFileRequest layoutFileRequest = (LayoutFileRequest) arrayList.get(i);
                    try {
                        bArr = this.layoutFileManager.getTemplateById(layoutFileRequest.dinamicTemplate, layoutFileRequest.layoutKey, layoutFileRequest.url, new TemplatePerfInfo(this.module));
                    } catch (Throwable th) {
                        DinamicLog.e(SerialTaskManager.TAG, "remote getTemplate", th);
                        bArr = null;
                    }
                    synchronized (this) {
                        if (bArr == null) {
                            try {
                                this.totalFailedTemplates.add(layoutFileRequest.dinamicTemplate);
                                this.failedTemplates.add(layoutFileRequest.dinamicTemplate);
                            } catch (Throwable th2) {
                                throw th2;
                            }
                        } else {
                            this.totalFinishedTemplates.add(layoutFileRequest.dinamicTemplate);
                            this.finishedTemplates.add(layoutFileRequest.dinamicTemplate);
                        }
                        if (i == size - 1) {
                            this.isFinished = true;
                            this.timer.cancel();
                        }
                    }
                }
            } else {
                this.isFinished = true;
            }
            return buildResult();
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(DownloadResult downloadResult) {
            try {
                this.listener.onFinished(downloadResult);
            } catch (Exception e) {
                DinamicLog.e(SerialTaskManager.TAG, (Throwable) e, "callback onFinished is error");
            } catch (Throwable th) {
                this.taskManager.scheduleNext();
                throw th;
            }
            this.taskManager.scheduleNext();
        }

        /* access modifiers changed from: protected */
        public void onProgressUpdate(DownloadResult... downloadResultArr) {
            try {
                this.listener.onFinished(downloadResultArr[0]);
            } catch (Exception e) {
                DinamicLog.e(SerialTaskManager.TAG, (Throwable) e, "callback onFinished is error");
            }
        }

        private LayoutFileRequest checkNeedDownload(@Nullable DinamicTemplate dinamicTemplate) {
            String templateKey = getTemplateKey(dinamicTemplate);
            if (TextUtils.isEmpty(templateKey) || this.layoutFileManager.readLocalLayoutFileAndUpdateDB(templateKey) != null) {
                return null;
            }
            LayoutFileRequest layoutFileRequest = new LayoutFileRequest();
            layoutFileRequest.layoutKey = templateKey;
            layoutFileRequest.url = dinamicTemplate.templateUrl;
            layoutFileRequest.dinamicTemplate = dinamicTemplate;
            return layoutFileRequest;
        }

        /* access modifiers changed from: package-private */
        public String getTemplateKey(DinamicTemplate dinamicTemplate) {
            return dinamicTemplate.name + "_" + dinamicTemplate.version;
        }
    }

    public static final class LayoutFileRequest {
        public DinamicTemplate dinamicTemplate;
        public String layoutKey;
        public String url;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            return TextUtils.equals(this.layoutKey, ((LayoutFileRequest) obj).layoutKey);
        }

        public int hashCode() {
            if (this.layoutKey == null) {
                return -1;
            }
            return this.layoutKey.hashCode();
        }
    }
}
