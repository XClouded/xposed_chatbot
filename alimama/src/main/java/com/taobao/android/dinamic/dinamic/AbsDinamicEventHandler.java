package com.taobao.android.dinamic.dinamic;

import android.view.View;
import java.util.ArrayList;

public abstract class AbsDinamicEventHandler implements DinamicEventHandler {
    public void handleEvent(View view, Object obj) {
    }

    public void prepareBindEvent(View view, Object obj, Object obj2) {
    }

    public void handleEvent(View view, String str, Object obj, Object obj2, Object obj3) {
        handleEvent(view, obj);
    }

    public void handleEvent(View view, String str, Object obj, Object obj2, Object obj3, ArrayList arrayList) {
        handleEvent(view, str, obj, obj2, obj3);
    }
}
