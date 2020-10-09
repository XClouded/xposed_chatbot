package com.taobao.tao.log.task;

import android.util.Log;
import com.taobao.android.tlog.protocol.model.CommandInfo;
import com.taobao.android.tlog.protocol.model.request.LogUploadRequest;
import com.taobao.android.tlog.protocol.model.request.base.LogFeature;
import com.taobao.tao.log.TLogInitializer;
import com.taobao.tao.log.TLogNative;
import com.taobao.tao.log.TLogUtils;
import com.taobao.tao.log.monitor.TLogMonitor;
import com.taobao.tao.log.monitor.TLogStage;
import java.util.ArrayList;
import java.util.List;

public class LogUploadRequestTask implements ICommandTask {
    private String TAG = "TLOG.LogUploadRequestTask";

    public ICommandTask execute(CommandInfo commandInfo) {
        TLogInitializer.getInstance().gettLogMonitor().stageInfo(TLogStage.MSG_HANDLE, this.TAG, "消息处理：服务端请求上传文件");
        TLogNative.appenderFlushData(false);
        try {
            LogUploadRequest logUploadRequest = new LogUploadRequest();
            logUploadRequest.parse(commandInfo.data, commandInfo);
            String str = logUploadRequest.uploadId;
            LogFeature[] logFeatureArr = logUploadRequest.logFeatures;
            Boolean bool = logUploadRequest.allowNotWifi;
            if (bool == null || bool.booleanValue() || Boolean.valueOf(TLogUtils.checkNetworkIsWifi(TLogInitializer.getInstance().getContext())).booleanValue()) {
                TLogMonitor tLogMonitor = TLogInitializer.getInstance().gettLogMonitor();
                String str2 = TLogStage.MSG_HANDLE;
                String str3 = this.TAG;
                tLogMonitor.stageInfo(str2, str3, "消息处理：服务端请求上传文件,是否允许非wifi上传：" + bool);
                List<String> findFile = findFile(logFeatureArr);
                if (findFile == null || findFile.size() <= 0) {
                    return null;
                }
                ApplyTokenRequestTask.execute(str, findFile, "application/x-tlog");
                return null;
            }
            LogUploadReplyTask.executeFailure(commandInfo, str, (String) null, "1", "405", "NotWifi", (String) null);
            return this;
        } catch (Exception e) {
            Log.e(this.TAG, "execute error", e);
            TLogInitializer.getInstance().gettLogMonitor().stageError(TLogStage.MSG_HANDLE, this.TAG, (Throwable) e);
            return null;
        }
    }

    private List<String> findFile(LogFeature[] logFeatureArr) {
        ArrayList arrayList = new ArrayList();
        if (logFeatureArr == null) {
            Log.e(this.TAG, "log features is null ");
            return null;
        }
        for (LogFeature logFeature : logFeatureArr) {
            String str = logFeature.appenderName;
            if (str == null) {
                str = TLogInitializer.getInstance().getNameprefix();
            }
            String str2 = logFeature.suffix;
            Integer num = logFeature.maxHistory;
            List<String> filePath = (str2 == null || str2.length() <= 0) ? null : TLogUtils.getFilePath(str2, 0);
            List<String> filePath2 = (str == null || str.length() <= 0) ? null : TLogUtils.getFilePath(str, 0, TLogUtils.getDays(num));
            if (filePath != null) {
                for (String next : filePath) {
                    if (!arrayList.contains(next)) {
                        arrayList.add(next);
                    }
                }
            }
            if (filePath2 != null) {
                for (String next2 : filePath2) {
                    if (!arrayList.contains(next2)) {
                        arrayList.add(next2);
                    }
                }
            }
        }
        return arrayList;
    }
}
