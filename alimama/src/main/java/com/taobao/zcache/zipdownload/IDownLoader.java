package com.taobao.zcache.zipdownload;

import java.lang.Thread;

public interface IDownLoader {
    void cancelTask(boolean z);

    Thread.State getDownLoaderStatus();
}
