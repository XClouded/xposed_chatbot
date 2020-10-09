package com.taobao.android.dinamic.dinamic;

import com.taobao.android.dinamic.tempate.DinamicTemplate;

public abstract class AbsDinamicMonitor {
    public abstract void trackBeforeBindData(String str, DinamicTemplate dinamicTemplate);

    public abstract void trackBeforeCreateView(String str, DinamicTemplate dinamicTemplate);
}
