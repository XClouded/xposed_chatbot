package com.taobao.android.dinamic.constructor;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import com.taobao.android.dinamic.DinamicTagKey;
import com.taobao.android.dinamic.dinamic.DinamicViewAdvancedConstructor;
import com.taobao.android.dinamic.model.DinamicParams;
import com.taobao.android.dinamic.property.DAttrConstant;
import com.taobao.android.dinamic.view.DLoopLinearLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DLoopLinearLayoutConstructor extends DinamicViewAdvancedConstructor {
    public static final String TAG = "DLoopLinearLayoutConstructor";

    public View initializeView(String str, Context context, AttributeSet attributeSet) {
        return new DLoopLinearLayout(context, attributeSet);
    }

    public void setAttributes(View view, Map<String, Object> map, ArrayList<String> arrayList, DinamicParams dinamicParams) {
        super.setAttributes(view, map, arrayList, dinamicParams);
        DLoopLinearLayout dLoopLinearLayout = (DLoopLinearLayout) view;
        if (arrayList.contains(DAttrConstant.LL_ORIENTATION)) {
            setOrientation(dLoopLinearLayout, (String) map.get(DAttrConstant.LL_ORIENTATION));
        }
        if (arrayList.contains(DAttrConstant.VIEW_LIST_DATA)) {
            bindListData(dLoopLinearLayout, dinamicParams, (List) map.get(DAttrConstant.VIEW_LIST_DATA));
        }
    }

    public void applyDefaultProperty(View view, Map<String, Object> map, DinamicParams dinamicParams) {
        super.applyDefaultProperty(view, map, dinamicParams);
        DLoopLinearLayout dLoopLinearLayout = (DLoopLinearLayout) view;
        dLoopLinearLayout.setBaselineAligned(false);
        if (!map.containsKey(DAttrConstant.LL_ORIENTATION)) {
            dLoopLinearLayout.setOrientation(1);
        }
        dLoopLinearLayout.setTag(DinamicTagKey.TAG_DINAMIC_BIND_DATA_LIST, dinamicParams.getViewResult().getBindDataList());
    }

    public void setOrientation(DLoopLinearLayout dLoopLinearLayout, String str) {
        if (!TextUtils.isEmpty(str)) {
            switch (Integer.valueOf(str).intValue()) {
                case 0:
                    dLoopLinearLayout.setOrientation(1);
                    return;
                case 1:
                    dLoopLinearLayout.setOrientation(0);
                    return;
                default:
                    return;
            }
        } else {
            dLoopLinearLayout.setOrientation(0);
        }
    }

    public void bindListData(DLoopLinearLayout dLoopLinearLayout, DinamicParams dinamicParams, List list) {
        dLoopLinearLayout.bindListData(dinamicParams, list);
    }
}
