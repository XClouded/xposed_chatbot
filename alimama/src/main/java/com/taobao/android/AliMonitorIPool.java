package com.taobao.android;

interface AliMonitorIPool {
    <T extends AliMonitorReusable> void offer(T t);

    <T extends AliMonitorReusable> T poll(Class<T> cls, Object... objArr);
}
