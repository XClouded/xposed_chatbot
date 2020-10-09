package com.taobao.tao.log.godeye.api.command;

import com.alibaba.fastjson.JSONObject;
import com.taobao.android.tlog.protocol.OpCode;
import com.taobao.android.tlog.protocol.model.CommandInfo;
import com.taobao.android.tlog.protocol.model.GodeyeInfo;
import com.taobao.android.tlog.protocol.model.request.HeapDumpRequest;
import com.taobao.android.tlog.protocol.model.request.MethodTraceRequest;
import com.taobao.tao.log.TLogInitializer;
import com.taobao.tao.log.monitor.TLogStage;

public class TraceTask extends GodeyeBaseTask {
    private static final long defaultAllocMemoryLevel = 104857600;
    private static final double defaultThreshold = 0.6d;
    public Long allocMemoryLevel;
    public Boolean allowForeground;
    public Integer bufferSize = 0;
    public String filePath;
    public Integer maxTrys = 3;
    public Integer numTrys = 0;
    public String opCode;
    public String ossEndpoint;
    public String ossObjectKey;
    public String progress;
    public String requestId;
    public Long samplingInterval;
    public String sequence;
    public Double threshold;
    public String uploadId;

    public TraceTask(JSONObject jSONObject) {
        super(jSONObject);
        if (jSONObject.containsKey("samplingInterval")) {
            this.samplingInterval = jSONObject.getLong("samplingInterval");
        }
        if (jSONObject.containsKey("maxTrys")) {
            this.maxTrys = jSONObject.getInteger("maxTrys");
        }
        if (jSONObject.containsKey("uploadId")) {
            this.uploadId = jSONObject.getString("uploadId");
        }
        this.progress = TraceProgress.NOT_STARTED.name();
        this.filePath = null;
    }

    public TraceTask() {
    }

    public void toTraceTask(GodeyeInfo godeyeInfo) {
        CommandInfo commandInfo = godeyeInfo.commandInfo;
        if (commandInfo != null) {
            this.opCode = godeyeInfo.commandInfo.opCode;
            this.requestId = godeyeInfo.commandInfo.requestId;
            this.uploadId = godeyeInfo.uploadId;
            if (commandInfo.opCode.equals(OpCode.METHOD_TRACE_DUMP)) {
                MethodTraceRequest methodTraceRequest = new MethodTraceRequest();
                try {
                    methodTraceRequest.parse(commandInfo.data, commandInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                    TLogInitializer.getInstance().gettLogMonitor().stageError(TLogStage.MSG_HANDLE, "TLOG.TraceTask", (Throwable) e);
                }
                if (methodTraceRequest.start != null) {
                    this.start = methodTraceRequest.start;
                }
                if (methodTraceRequest.stop != null) {
                    this.stop = methodTraceRequest.stop;
                }
                this.sequence = godeyeInfo.commandInfo.requestId;
                this.numTrys = 0;
                if (methodTraceRequest.maxTrys != null) {
                    this.maxTrys = methodTraceRequest.maxTrys;
                } else {
                    this.maxTrys = 0;
                }
                if (methodTraceRequest.samplingInterval != null) {
                    this.samplingInterval = methodTraceRequest.samplingInterval;
                }
                this.numTrys = 0;
                if (this.maxTrys.intValue() == 0) {
                    this.maxTrys = 3;
                }
                this.filePath = godeyeInfo.filePath;
                if (godeyeInfo.progress != null) {
                    this.progress = godeyeInfo.progress;
                }
                if (this.progress == null) {
                    this.progress = TraceProgress.NOT_STARTED.name();
                }
                if (godeyeInfo.bufferSize != null) {
                    this.bufferSize = godeyeInfo.bufferSize;
                }
                if (this.bufferSize.intValue() == 0) {
                    this.bufferSize = 4194304;
                }
                if (this.samplingInterval == null) {
                    this.samplingInterval = 10000L;
                }
            } else if (commandInfo.opCode.equals(OpCode.HEAP_DUMP)) {
                HeapDumpRequest heapDumpRequest = new HeapDumpRequest();
                try {
                    heapDumpRequest.parse(commandInfo.data, commandInfo);
                } catch (Exception e2) {
                    e2.printStackTrace();
                    TLogInitializer.getInstance().gettLogMonitor().stageError(TLogStage.MSG_HANDLE, "TLOG.TraceTask", (Throwable) e2);
                }
                if (heapDumpRequest.start != null) {
                    this.start = heapDumpRequest.start;
                }
                Integer num = heapDumpRequest.heapSizeThreshold;
                if (num != null) {
                    this.threshold = Double.valueOf((double) num.intValue());
                } else {
                    this.threshold = Double.valueOf(defaultThreshold);
                }
                this.allocMemoryLevel = 104857600L;
            }
        }
    }

    public boolean isEmptyTask() {
        return this.start == null;
    }

    public TraceProgress getProgress() {
        return TraceProgress.fromName(this.progress);
    }
}
