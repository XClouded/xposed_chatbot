package com.ali.ha.fulltrace.event;

import com.ali.ha.fulltrace.ProtocolConstants;

public class LauncherUsableEvent extends UsableEvent {
    public short getType() {
        return ProtocolConstants.EVENT_LAUNCHER_USABLE;
    }
}
