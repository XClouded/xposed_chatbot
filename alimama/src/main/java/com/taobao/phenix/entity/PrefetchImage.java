package com.taobao.phenix.entity;

import com.taobao.rxm.common.Releasable;

public class PrefetchImage implements Releasable {
    public boolean fromDisk;
    public long length;
    public String url;

    public void release() {
    }
}
