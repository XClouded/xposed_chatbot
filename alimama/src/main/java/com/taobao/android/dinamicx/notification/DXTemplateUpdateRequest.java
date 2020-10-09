package com.taobao.android.dinamicx.notification;

import com.alibaba.fastjson.JSONObject;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;

public class DXTemplateUpdateRequest {
    public static final int REASON_DOWNGRADE = 1000;
    public static final int REASON_DOWNLOAD_FAIL = 1003;
    public static final int REASON_DOWNLOAD_SUCCESS = 1002;
    public static final int REASON_TEMPLATE_NOT_MATCH = 1001;
    public JSONObject data;
    public Object dxUserContext;
    public DXTemplateItem item;
    public int reason;

    public String toString() {
        return "DXTemplateUpdateRequest{item=" + this.item + ", data=" + this.data + ", reason='" + this.reason + '\'' + ", dxUserContext=" + this.dxUserContext + '}';
    }
}
