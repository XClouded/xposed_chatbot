package com.taobao.phenix.intf.event;

import com.taobao.phenix.intf.event.PhenixEvent;

public interface IPhenixListener<T extends PhenixEvent> {
    boolean onHappen(T t);
}
