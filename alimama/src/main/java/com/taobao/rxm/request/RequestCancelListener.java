package com.taobao.rxm.request;

import com.taobao.rxm.request.RequestContext;

public interface RequestCancelListener<CONTEXT extends RequestContext> {
    void onCancel(CONTEXT context);
}
