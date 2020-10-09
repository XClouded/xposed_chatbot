package com.alimama.share.listeners;

import com.alimama.share.beans.Platform;
import com.alimama.share.beans.State;

public interface ShareCallBack {
    void end(Platform platform, State state, String str);

    void start();
}
