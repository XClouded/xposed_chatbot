package com.taobao.android.dinamic.constructor;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.taobao.android.dinamic.dinamic.DinamicViewAdvancedConstructor;
import com.taobao.android.dinamic.model.DinamicParams;
import com.taobao.android.dinamic.view.DHorizontalScrollLayout;
import java.util.ArrayList;
import java.util.Map;

public class DHorizontalScrollLayoutConstructor extends DinamicViewAdvancedConstructor {
    public View initializeViewWithModule(String str, Context context, AttributeSet attributeSet, DinamicParams dinamicParams) {
        return new DHorizontalScrollLayout(context, attributeSet, dinamicParams);
    }

    public void setAttributes(View view, Map<String, Object> map, ArrayList<String> arrayList, DinamicParams dinamicParams) {
        super.setAttributes(view, map, arrayList, dinamicParams);
    }
}
