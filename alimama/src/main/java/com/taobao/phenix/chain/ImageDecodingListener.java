package com.taobao.phenix.chain;

public interface ImageDecodingListener {
    void onDecodeFinish(long j, String str);

    void onDecodeStart(long j, String str);
}
