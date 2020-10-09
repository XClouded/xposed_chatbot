package com.taobao.accs.ut.monitor;

import anet.channel.statist.Dimension;
import anet.channel.statist.Measure;
import anet.channel.statist.Monitor;
import com.taobao.accs.utl.BaseMonitor;
import com.taobao.weex.common.Constants;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Monitor(module = "accs", monitorPoint = "netperformance")
public class NetPerformanceMonitor extends BaseMonitor {
    @Dimension
    public int accs_sdk_version;
    @Dimension
    public int accs_type;
    public String data_id;
    public String device_id;
    private long enter_queue_date;
    @Dimension
    public int error_code;
    @Dimension
    public String fail_reasons;
    @Dimension
    public String host;
    @Measure(constantValue = 0.0d, max = 60000.0d, min = 0.0d)
    public long in_queue_time;
    @Dimension
    public int msgType;
    @Measure(constantValue = 0.0d, max = 60000.0d, min = 0.0d)
    public long receive_accs_to_call_time;
    private long receive_ack_date;
    @Measure(constantValue = 0.0d, max = 60000.0d, min = 0.0d)
    public long receive_agoo_to_call_time;
    private long receive_data_time;
    @Measure(constantValue = 0.0d, max = 60000.0d, min = 0.0d)
    public long receive_to_call_back_time;
    @Dimension
    public String ret;
    @Dimension
    public int retry_times;
    @Measure(constantValue = 0.0d, max = 60000.0d, min = 0.0d)
    public long send_to_receive_time;
    @Dimension
    public String service_id = "none";
    private long start_send_date;
    @Measure(constantValue = 0.0d, max = 60000.0d, min = 0.0d)
    public long start_to_enter_queue_time;
    public long take_date;
    @Measure(constantValue = 0.0d, max = 60000.0d, min = 0.0d)
    public long talk_to_send_time;
    private long to_accs_time;
    private long to_agoo_time;
    private long to_bz_date;
    public long to_tnet_date;
    @Measure(constantValue = 0.0d, max = 60000.0d, min = 0.0d)
    public long total_time;

    public interface MsgType {
        public static final int BIND_APP = 1;
        public static final int BIND_SERVICE = 3;
        public static final int BIND_USER = 2;
        public static final int RECEIVE_DATA = 4;
        public static final int SEND_DATA = 0;

        @Retention(RetentionPolicy.SOURCE)
        public @interface Definition {
        }
    }

    private long computeTime(long j, long j2) {
        if (j <= 0 || j2 <= 0) {
            return 0;
        }
        return j2 - j;
    }

    public void setDeviceId(String str) {
        this.device_id = str;
    }

    public void setServiceId(String str) {
        this.service_id = str;
    }

    public void setDataId(String str) {
        this.data_id = str;
    }

    public void onSend() {
        this.start_send_date = System.currentTimeMillis();
    }

    public void onEnterQueueData() {
        this.enter_queue_date = System.currentTimeMillis();
    }

    public void onTakeFromQueue() {
        this.take_date = System.currentTimeMillis();
    }

    public void onSendData() {
        this.to_tnet_date = System.currentTimeMillis();
    }

    public void onReceiveData() {
        this.receive_data_time = System.currentTimeMillis();
    }

    public void onToAccsTime() {
        this.to_accs_time = System.currentTimeMillis();
    }

    public void onToAgooTime() {
        this.to_agoo_time = System.currentTimeMillis();
    }

    public void setRet(boolean z) {
        this.ret = z ? Constants.Name.Y : "n";
    }

    public void setFailReason(String str) {
        this.fail_reasons = str;
    }

    public void setMsgType(int i) {
        this.msgType = i;
    }

    public void setFailReason(int i) {
        this.error_code = i;
        if (i == 200) {
            return;
        }
        if (i != 300) {
            switch (i) {
                case -4:
                    setFailReason("msg too large");
                    return;
                case -3:
                    setFailReason("service not available");
                    return;
                case -2:
                    setFailReason("param error");
                    return;
                case -1:
                    setFailReason("network fail");
                    return;
                default:
                    setFailReason(String.valueOf(i));
                    return;
            }
        } else {
            setFailReason("app not bind");
        }
    }

    public void setHost(String str) {
        this.host = str;
    }

    public void onRecAck() {
        this.receive_ack_date = System.currentTimeMillis();
    }

    public void onToBizDate() {
        this.to_bz_date = System.currentTimeMillis();
    }

    public void setConnType(int i) {
        this.accs_type = i;
    }

    public boolean beforeCommit() {
        this.accs_sdk_version = com.taobao.accs.common.Constants.SDK_VERSION_CODE;
        this.total_time = computeTime(this.start_send_date, this.to_bz_date);
        this.start_to_enter_queue_time = computeTime(this.start_send_date, this.enter_queue_date);
        this.in_queue_time = computeTime(this.enter_queue_date, this.take_date);
        this.talk_to_send_time = computeTime(this.take_date, this.to_tnet_date);
        this.send_to_receive_time = computeTime(this.to_tnet_date, this.receive_ack_date);
        this.receive_to_call_back_time = computeTime(this.receive_ack_date, this.to_bz_date);
        this.receive_accs_to_call_time = computeTime(this.receive_data_time, this.to_accs_time);
        this.receive_agoo_to_call_time = computeTime(this.receive_data_time, this.to_agoo_time);
        return super.beforeCommit();
    }
}
