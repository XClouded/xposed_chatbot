package com.taobao.phenix.intf.event;

import com.taobao.phenix.common.UnitedLog;
import com.taobao.phenix.intf.PhenixTicket;
import com.taobao.tcommon.log.FLog;
import com.taobao.weex.el.parse.Operators;
import java.util.ArrayList;
import java.util.List;

public class PrefetchEvent extends PhenixEvent {
    public boolean allSucceeded;
    public int completeCount;
    public int completeSize;
    public int downloadCount;
    public int downloadSize;
    public final List<String> listOfFailed;
    public final List<String> listOfSucceeded;
    public final List<Throwable> listOfThrowable = new ArrayList();
    public int totalCount;

    public PrefetchEvent(List<String> list, List<String> list2) {
        super((PhenixTicket) null);
        this.listOfSucceeded = list;
        this.listOfFailed = list2;
    }

    public String toString() {
        if (!UnitedLog.isLoggable(3)) {
            return "PrefetchEvent@Release";
        }
        return "PrefetchEvent@" + Integer.toHexString(hashCode()) + "(totalCount:" + this.totalCount + ", completeCount:" + this.completeCount + ", completeSize:" + FLog.unitSize((long) this.completeSize) + ", allSucceeded:" + this.allSucceeded + ", succeeded:" + this.listOfSucceeded.size() + ", failed:" + this.listOfFailed.size() + Operators.BRACKET_END_STR;
    }
}
