package mtopsdk.mtop.upload;

import android.content.Context;
import android.os.Handler;
import android.util.Pair;
import com.uploader.export.IUploaderManager;
import com.uploader.export.IUploaderTask;
import com.uploader.export.UploaderCreator;
import com.uploader.portal.UploaderDependencyImpl;
import com.uploader.portal.UploaderLogImpl;
import com.uploader.portal.UploaderStatisticsImpl;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import mtopsdk.common.util.RemoteConfig;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.global.SDKConfig;
import mtopsdk.mtop.upload.domain.UploadConstants;
import mtopsdk.mtop.upload.domain.UploadFileInfo;
import mtopsdk.mtop.upload.util.FileUploadThreadPoolExecutorFactory;

public class FileUploadMgr {
    private static final String TAG = "mtopsdk.FileUploadMgr";
    /* access modifiers changed from: private */
    public ConcurrentHashMap<UploadFileInfo, Pair<DefaultFileUploadListenerWrapper, IUploaderTask>> uploadTasks;
    /* access modifiers changed from: private */
    public IUploaderManager uploaderManager;

    private static class FileUploadMgrHolder {
        public static final FileUploadMgr instance = new FileUploadMgr();

        private FileUploadMgrHolder() {
        }
    }

    private FileUploadMgr() {
        this.uploaderManager = null;
        if (this.uploadTasks == null) {
            this.uploadTasks = new ConcurrentHashMap<>();
        }
        if (this.uploaderManager == null) {
            try {
                Context globalContext = SDKConfig.getInstance().getGlobalContext();
                this.uploaderManager = UploaderCreator.get();
                if (!this.uploaderManager.isInitialized()) {
                    UploaderLogImpl uploaderLogImpl = new UploaderLogImpl();
                    uploaderLogImpl.setEnableTLog(RemoteConfig.getInstance().enableArupTlog);
                    this.uploaderManager.initialize(globalContext, new UploaderDependencyImpl(globalContext, new UploaderEnvironmentAdapter(globalContext), uploaderLogImpl, new UploaderStatisticsImpl()));
                }
            } catch (Exception e) {
                TBSdkLog.e(TAG, "init IUploaderMananger error.", (Throwable) e);
            }
        }
    }

    public static final FileUploadMgr getInstance() {
        return FileUploadMgrHolder.instance;
    }

    @Deprecated
    public void addTask(UploadFileInfo uploadFileInfo, FileUploadListener fileUploadListener) {
        if (fileUploadListener == null) {
            TBSdkLog.e(TAG, "add upload task failed,listener is invalid");
        } else {
            addTask(uploadFileInfo, (FileUploadBaseListener) new DefaultFileUploadListener(fileUploadListener));
        }
    }

    public void addTask(final UploadFileInfo uploadFileInfo, FileUploadBaseListener fileUploadBaseListener) {
        if (fileUploadBaseListener == null) {
            TBSdkLog.e(TAG, "add upload task failed,listener is invalid");
        } else if (uploadFileInfo == null || !uploadFileInfo.isValid()) {
            TBSdkLog.e(TAG, "add upload task failed,fileInfo is invalid");
            fileUploadBaseListener.onError(UploadConstants.ERRTYPE_ILLEGAL_FILE_ERROR, UploadConstants.ERRCODE_FILE_INVALID, UploadConstants.ERRMSG_FILE_INVALID);
        } else {
            DefaultFileUploadListenerWrapper defaultFileUploadListenerWrapper = new DefaultFileUploadListenerWrapper(fileUploadBaseListener);
            if (!RemoteConfig.getInstance().degradeBizcodeSets.contains(uploadFileInfo.getBizCode())) {
                AnonymousClass1 r6 = new IUploaderTask() {
                    public String getFileType() {
                        return null;
                    }

                    public Map<String, String> getMetaInfo() {
                        return null;
                    }

                    public String getBizType() {
                        return uploadFileInfo.getBizCode();
                    }

                    public String getFilePath() {
                        return uploadFileInfo.getFilePath();
                    }
                };
                if (!this.uploadTasks.containsKey(uploadFileInfo)) {
                    this.uploadTasks.put(uploadFileInfo, new Pair(defaultFileUploadListenerWrapper, r6));
                    this.uploaderManager.uploadAsync(r6, new TaskListenerAdapter(uploadFileInfo, defaultFileUploadListenerWrapper), (Handler) null);
                }
            } else if (!this.uploadTasks.containsKey(uploadFileInfo)) {
                this.uploadTasks.put(uploadFileInfo, new Pair(defaultFileUploadListenerWrapper, (Object) null));
                FileUploadThreadPoolExecutorFactory.submitUploadTask(new FileUploadTask(uploadFileInfo, defaultFileUploadListenerWrapper));
            }
        }
    }

    @Deprecated
    public void addTask(UploadFileInfo uploadFileInfo, FileUploadBaseListener fileUploadBaseListener, boolean z) {
        addTask(uploadFileInfo, fileUploadBaseListener);
    }

    public void addTask(List<UploadFileInfo> list) {
        if (list == null || list.size() < 1) {
            TBSdkLog.e(TAG, "add upload task failed,fileInfoList is invalid");
            return;
        }
        for (UploadFileInfo next : list) {
            if (next != null) {
                addTask(next, next.getListener());
            }
        }
    }

    public void removeTask(final UploadFileInfo uploadFileInfo) {
        try {
            FileUploadThreadPoolExecutorFactory.submitRemoveTask(new Runnable() {
                public void run() {
                    if (uploadFileInfo == null || !uploadFileInfo.isValid()) {
                        TBSdkLog.e(FileUploadMgr.TAG, "remove upload task failed,fileInfo is invalid");
                    } else if (FileUploadMgr.this.uploadTasks.containsKey(uploadFileInfo)) {
                        Pair pair = (Pair) FileUploadMgr.this.uploadTasks.get(uploadFileInfo);
                        ((DefaultFileUploadListenerWrapper) pair.first).cancel();
                        FileUploadMgr.this.uploadTasks.remove(uploadFileInfo);
                        IUploaderTask iUploaderTask = (IUploaderTask) pair.second;
                        if (iUploaderTask != null) {
                            FileUploadMgr.this.uploaderManager.cancelAsync(iUploaderTask);
                        }
                        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                            TBSdkLog.d(FileUploadMgr.TAG, "remove upload task succeed." + uploadFileInfo.toString());
                        }
                    }
                }
            });
        } catch (Exception e) {
            TBSdkLog.e(TAG, "add removeTask to removeTaskPool error", (Throwable) e);
        }
    }

    /* access modifiers changed from: protected */
    public void removeArupTask(final UploadFileInfo uploadFileInfo) {
        try {
            FileUploadThreadPoolExecutorFactory.submitRemoveTask(new Runnable() {
                public void run() {
                    if (uploadFileInfo == null || !uploadFileInfo.isValid()) {
                        TBSdkLog.e(FileUploadMgr.TAG, "remove upload task failed,fileInfo is invalid");
                    } else {
                        FileUploadMgr.this.uploadTasks.remove(uploadFileInfo);
                    }
                }
            });
        } catch (Exception e) {
            TBSdkLog.e(TAG, "add removeTask to removeTaskPool error", (Throwable) e);
        }
    }

    public void destroy() {
        this.uploadTasks.clear();
    }
}
