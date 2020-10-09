package com.taobao.android.dxcontainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DXContainerError {
    public String bizType;
    public List<DXContainerErrorInfo> dxErrorInfoList = new ArrayList(10);

    public DXContainerError(String str) {
        this.bizType = str;
    }

    public String toString() {
        return "DXContainerError{bizType='" + this.bizType + '\'' + ", dxErrorInfoList=" + this.dxErrorInfoList + '}';
    }

    public static class DXContainerErrorInfo {
        public int code;
        public Map<String, String> extraParams;
        public String reason;
        public String serviceId;
        public long timeStamp = System.currentTimeMillis();

        public DXContainerErrorInfo(String str, int i) {
            this.serviceId = str;
            this.code = i;
        }

        public DXContainerErrorInfo(String str, int i, String str2) {
            this.serviceId = str;
            this.code = i;
            this.reason = str2;
        }

        public String toString() {
            return "DXContainerErrorInfo{timeStamp=" + this.timeStamp + ", serviceId='" + this.serviceId + '\'' + ", code=" + this.code + ", reason='" + this.reason + '\'' + ", extraParams=" + this.extraParams + '}';
        }
    }
}
