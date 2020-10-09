package com.taobao.phenix.intf.event;

import com.taobao.phenix.intf.PhenixCreator;

public interface IRetryHandlerOnFailure {
    String getRetryUrl(PhenixCreator phenixCreator, Throwable th);
}
