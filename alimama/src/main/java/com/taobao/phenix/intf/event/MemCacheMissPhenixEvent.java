package com.taobao.phenix.intf.event;

import com.taobao.phenix.intf.PhenixTicket;

public class MemCacheMissPhenixEvent extends PhenixEvent {
    public MemCacheMissPhenixEvent(PhenixTicket phenixTicket) {
        super(phenixTicket);
    }
}
