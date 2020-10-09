package com.taobao.android;

import com.taobao.android.AliImageEvent;

public interface AliImageListener<T extends AliImageEvent> {
    boolean onHappen(T t);
}
