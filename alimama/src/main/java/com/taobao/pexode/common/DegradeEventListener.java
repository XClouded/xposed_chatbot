package com.taobao.pexode.common;

public interface DegradeEventListener {
    void onDegraded2NoAshmem(boolean z);

    void onDegraded2NoInBitmap(boolean z);

    void onDegraded2System(boolean z);
}
