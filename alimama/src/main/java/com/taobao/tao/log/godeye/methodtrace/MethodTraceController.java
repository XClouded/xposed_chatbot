package com.taobao.tao.log.godeye.methodtrace;

import android.os.Build;
import android.os.Debug;
import com.alibaba.fastjson.JSONObject;
import com.taobao.tao.log.godeye.api.command.InstructionHandler;
import com.taobao.tao.log.godeye.api.command.ResponseData;
import com.taobao.tao.log.godeye.api.command.TraceProgress;
import com.taobao.tao.log.godeye.api.command.TraceTask;
import com.taobao.tao.log.godeye.api.control.AbsCommandController;
import com.taobao.tao.log.godeye.api.control.IGodeyeJointPointCenter;
import com.taobao.tao.log.godeye.methodtrace.file.TraceFileUploadListener;
import java.io.File;

public class MethodTraceController extends AbsCommandController implements InstructionHandler {
    private static final String opCode = "RDWP_METHOD_TRACE_DUMP";
    private final String mTraceDumpDir = (MethodTraceInitializer.sApplication.getExternalFilesDir((String) null) + File.separator + "RDWP_METHOD_TRACE_DUMP" + ".trace");
    private TraceTask mTraceTask;

    public InstructionHandler getInstructionHandler() {
        return this;
    }

    MethodTraceController() {
        super("RDWP_METHOD_TRACE_DUMP");
    }

    public void startTask() {
        if (this.mTraceTask != null && this.mTraceTask.getProgress() == TraceProgress.NOT_STARTED) {
            TraceTask traceTask = this.mTraceTask;
            if (traceTask.numTrys.intValue() >= traceTask.maxTrys.intValue()) {
                MethodTraceInitializer.sGodeye.defaultCommandManager().removeLocalCommand(this);
            } else if (Build.VERSION.SDK_INT > 19) {
                traceTask.numTrys = Integer.valueOf(traceTask.numTrys.intValue() + 1);
                saveTaskRunningStatus(this.mTraceTask);
                if (this.mTraceTask.samplingInterval.longValue() <= 0) {
                    Debug.startMethodTracing();
                } else {
                    Debug.startMethodTracingSampling(this.mTraceDumpDir, this.mTraceTask.bufferSize.intValue(), 10000);
                }
                traceTask.progress = TraceProgress.RUNNING.name();
                saveTaskRunningStatus(this.mTraceTask);
            } else if (this.mTraceTask.samplingInterval.longValue() <= 0) {
                Debug.startMethodTracing();
            }
        }
    }

    public void stopRunningTask() {
        if (this.mTraceTask != null && this.mTraceTask.getProgress() == TraceProgress.RUNNING) {
            Debug.stopMethodTracing();
            TraceTask traceTask = this.mTraceTask;
            traceTask.filePath = this.mTraceDumpDir;
            traceTask.progress = TraceProgress.COMPLETE.name();
            saveTaskRunningStatus(this.mTraceTask);
            MethodTraceInitializer.sGodeye.upload(this, this.mTraceDumpDir, new TraceFileUploadListener(this, this.mTraceTask));
        }
    }

    public void handleInstruction(TraceTask traceTask, boolean z) {
        if (!z) {
            try {
                stopRunningTask();
                MethodTraceInitializer.sGodeye.response(this, new ResponseData(2, "receive-new-command", (JSONObject) null));
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        this.mTraceTask = traceTask;
        if (this.mTraceTask.isEmptyTask()) {
            MethodTraceInitializer.sGodeye.defaultCommandManager().removeLocalCommand(this);
        } else {
            MethodTraceInitializer.sGodeye.defaultCommandManager().saveRawCommandString(this, this.mTraceTask);
        }
        installTraceTask(this.mTraceTask, z);
    }

    public void saveTaskRunningStatus(TraceTask traceTask) {
        MethodTraceInitializer.sGodeye.defaultCommandManager().saveRawCommandString(this, traceTask);
    }

    public String getUploadId() {
        return this.mTraceTask.uploadId;
    }

    public String getRequestId() {
        return this.mTraceTask.requestId;
    }

    /* access modifiers changed from: package-private */
    public void installTraceTask(TraceTask traceTask, boolean z) {
        if (verifyLocalTask(traceTask)) {
            setTraceTask(traceTask);
            MethodTraceInitializer.sGodeye.defaultGodeyeJointPointCenter().installJointPoints(traceTask.start, new IGodeyeJointPointCenter.GodeyeJointPointCallback() {
                public void doCallback() {
                    MethodTraceController.this.startTask();
                }
            }, traceTask.stop, new IGodeyeJointPointCenter.GodeyeJointPointCallback() {
                public void doCallback() {
                    MethodTraceController.this.stopRunningTask();
                }
            }, z);
        }
    }

    private boolean verifyLocalTask(TraceTask traceTask) {
        if (traceTask == null || traceTask.start == null || traceTask.stop == null) {
            return false;
        }
        if (traceTask.numTrys.intValue() >= traceTask.maxTrys.intValue()) {
            MethodTraceInitializer.sGodeye.defaultCommandManager().removeLocalCommand(this);
            return false;
        } else if (traceTask.getProgress() == TraceProgress.UPLOADED) {
            MethodTraceInitializer.sGodeye.defaultCommandManager().removeLocalCommand(this);
            return false;
        } else {
            if (traceTask.getProgress() == TraceProgress.RUNNING) {
                traceTask.progress = TraceProgress.NOT_STARTED.name();
            }
            if ((traceTask.getProgress() != TraceProgress.COMPLETE && traceTask.getProgress() != TraceProgress.EXCEPTION_ON_UPLOAD) || traceTask.filePath == null) {
                return true;
            }
            MethodTraceInitializer.sGodeye.upload(this, traceTask.filePath, new TraceFileUploadListener(this, traceTask));
            return false;
        }
    }

    public void setTraceTask(TraceTask traceTask) {
        this.mTraceTask = traceTask;
    }
}
