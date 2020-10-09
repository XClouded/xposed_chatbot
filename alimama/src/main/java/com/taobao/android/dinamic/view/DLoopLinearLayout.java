package com.taobao.android.dinamic.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.dinamic.DViewGenerator;
import com.taobao.android.dinamic.DinamicTagKey;
import com.taobao.android.dinamic.DinamicViewUtils;
import com.taobao.android.dinamic.expression.DinamicExpression;
import com.taobao.android.dinamic.model.DinamicParams;
import com.taobao.android.dinamic.property.DinamicProperty;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DLoopLinearLayout extends DLinearLayout {
    private static final String TAG = "DLoopLinearLayout";
    final RecycledViewPool recycledPool = new RecycledViewPool();
    private Map<Integer, ViewInfo> templateViews = new LinkedHashMap();
    private int viewTypeFlag = 0;

    public DLoopLinearLayout(Context context) {
        super(context);
    }

    public DLoopLinearLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public DLoopLinearLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public Map<Integer, ViewInfo> cloneTemplateViews() {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.putAll(this.templateViews);
        return linkedHashMap;
    }

    public void setTemplateViews(Map<Integer, ViewInfo> map) {
        this.templateViews = map;
    }

    public void addView(View view, ViewGroup.LayoutParams layoutParams) {
        view.setLayoutParams(layoutParams);
        addViewInfo(view);
        removeBindList(view);
    }

    private void removeBindList(View view) {
        ArrayList arrayList = (ArrayList) getTag(DinamicTagKey.TAG_DINAMIC_BIND_DATA_LIST);
        DinamicProperty dinamicProperty = (DinamicProperty) view.getTag(DinamicTagKey.PROPERTY_KEY);
        if (!dinamicProperty.dinamicProperty.isEmpty() || !dinamicProperty.eventProperty.isEmpty()) {
            arrayList.remove(view);
        }
        if (isViewGroup(view)) {
            recursiveViewTree(view, arrayList);
        }
    }

    private boolean isViewGroup(View view) {
        if (view instanceof DLoopLinearLayout) {
            return false;
        }
        if ((view instanceof DLinearLayout) || (view instanceof DFrameLayout)) {
            return true;
        }
        return false;
    }

    private void recursiveViewTree(View view, ArrayList<View> arrayList) {
        ViewGroup viewGroup = (ViewGroup) view;
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = viewGroup.getChildAt(i);
            DinamicProperty dinamicProperty = (DinamicProperty) childAt.getTag(DinamicTagKey.PROPERTY_KEY);
            if (dinamicProperty != null && (!dinamicProperty.dinamicProperty.isEmpty() || !dinamicProperty.eventProperty.isEmpty())) {
                arrayList.remove(childAt);
            }
            if (isViewGroup(childAt)) {
                recursiveViewTree(childAt, arrayList);
            }
        }
    }

    private void addViewInfo(View view) {
        for (Map.Entry<Integer, ViewInfo> value : this.templateViews.entrySet()) {
            if (view == ((ViewInfo) value.getValue()).view) {
                return;
            }
        }
        DinamicProperty viewProperty = DinamicViewUtils.getViewProperty(view);
        ViewInfo viewInfo = new ViewInfo();
        viewInfo.view = view;
        viewInfo.viewType = this.viewTypeFlag;
        viewInfo.viewIdentify = viewProperty.viewIdentify;
        if (viewProperty.fixedProperty.containsKey("dFilter")) {
            viewInfo.filter = String.valueOf(viewProperty.fixedProperty.get("dFilter"));
        } else {
            viewInfo.filter = viewProperty.dinamicProperty.get("dFilter");
        }
        this.templateViews.put(Integer.valueOf(viewInfo.viewType), viewInfo);
        view.setTag(DinamicTagKey.VIEW_TYPE_KEY, Integer.valueOf(viewInfo.viewType));
        this.viewTypeFlag++;
    }

    public void bindListData(DinamicParams dinamicParams, List list) {
        bindChildView(dinamicParams, list);
    }

    public void bindChildView(DinamicParams dinamicParams, List list) {
        View view;
        if (this.templateViews.size() == 0 || list == null || list.size() == 0) {
            for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
                recyleView(childCount);
            }
            return;
        }
        if (getChildCount() > list.size()) {
            int childCount2 = getChildCount();
            while (true) {
                childCount2--;
                if (childCount2 < list.size()) {
                    break;
                }
                recyleView(childCount2);
            }
        }
        DViewGenerator viewGeneratorWithModule = DViewGenerator.viewGeneratorWithModule(dinamicParams.getModule());
        Object currentData = dinamicParams.getCurrentData();
        for (int i = 0; i < list.size(); i++) {
            dinamicParams.setCurrentData(getBindData(list.get(i)));
            int itemViewType = getItemViewType(dinamicParams);
            if (itemViewType == -1) {
                super.addView(new CompatibleView(getContext(), "no view match data"), i);
            } else {
                View view2 = null;
                if (i < getChildCount()) {
                    View childAt = getChildAt(i);
                    Integer num = (Integer) childAt.getTag(DinamicTagKey.VIEW_TYPE_KEY);
                    if (num == null || itemViewType != num.intValue()) {
                        recyleView(i);
                    } else {
                        view2 = childAt;
                    }
                }
                if (view == null) {
                    view = getCacheView(itemViewType);
                    if (view == null) {
                        ViewResult copyView = viewGeneratorWithModule.copyView(this.templateViews.get(Integer.valueOf(itemViewType)).view, getContext(), dinamicParams);
                        View view3 = copyView.getView();
                        view3.setTag(DinamicTagKey.TAG_ROOT_VIEW_RESULT, copyView);
                        view3.setTag(DinamicTagKey.VIEW_TYPE_KEY, Integer.valueOf(itemViewType));
                        view = view3;
                    }
                    super.addView(view, i, view.getLayoutParams());
                }
                ViewResult viewResult = (ViewResult) view.getTag(DinamicTagKey.TAG_ROOT_VIEW_RESULT);
                if (viewResult != null) {
                    viewGeneratorWithModule.bindDataLoopCloneView(viewResult.getBindDataList(), dinamicParams);
                }
            }
        }
        dinamicParams.setCurrentData(currentData);
    }

    private Object getBindData(Object obj) {
        if (!(obj instanceof String)) {
            return obj;
        }
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("value", obj);
        return jSONObject;
    }

    private View getCacheView(int i) {
        return this.recycledPool.getRecycledView(i);
    }

    public int getItemViewType(DinamicParams dinamicParams) {
        for (Map.Entry<Integer, ViewInfo> value : this.templateViews.entrySet()) {
            ViewInfo viewInfo = (ViewInfo) value.getValue();
            if (viewInfo.filter == null) {
                if (this.templateViews.size() == 1) {
                    return viewInfo.viewType;
                }
            } else if ("true".equals(viewInfo.filter)) {
                return viewInfo.viewType;
            } else {
                Object value2 = DinamicExpression.getValue(viewInfo.filter, viewInfo.viewIdentify, dinamicParams);
                if (value2 != null && (((value2 instanceof Boolean) && ((Boolean) value2).booleanValue()) || ((value2 instanceof String) && "true".equals(value2.toString())))) {
                    return viewInfo.viewType;
                }
            }
        }
        return -1;
    }

    private void recyleView(int i) {
        if (i < super.getChildCount()) {
            View childAt = super.getChildAt(i);
            super.removeViewAt(i);
            Integer num = (Integer) childAt.getTag(DinamicTagKey.VIEW_TYPE_KEY);
            if (num != null) {
                this.recycledPool.putRecycledView(num.intValue(), childAt);
            }
        }
    }

    class ViewInfo {
        String filter;
        View view;
        String viewIdentify;
        int viewType;

        ViewInfo() {
        }
    }

    public static class RecycledViewPool {
        private static final int DEFAULT_MAX_SCRAP = 10;
        private SparseIntArray mMaxScrap = new SparseIntArray();
        private SparseArray<ArrayList<View>> mScrap = new SparseArray<>();

        public void clear() {
            this.mScrap.clear();
        }

        public void setMaxRecycledViews(int i, int i2) {
            this.mMaxScrap.put(i, i2);
            ArrayList arrayList = this.mScrap.get(i);
            if (arrayList != null) {
                while (arrayList.size() > i2) {
                    arrayList.remove(arrayList.size() - 1);
                }
            }
        }

        public View getRecycledView(int i) {
            ArrayList arrayList = this.mScrap.get(i);
            if (arrayList == null || arrayList.isEmpty()) {
                return null;
            }
            int size = arrayList.size() - 1;
            View view = (View) arrayList.get(size);
            arrayList.remove(size);
            return view;
        }

        /* access modifiers changed from: package-private */
        public int size() {
            int i = 0;
            for (int i2 = 0; i2 < this.mScrap.size(); i2++) {
                ArrayList valueAt = this.mScrap.valueAt(i2);
                if (valueAt != null) {
                    i += valueAt.size();
                }
            }
            return i;
        }

        public void putRecycledView(int i, View view) {
            ArrayList<View> scrapHeapForType = getScrapHeapForType(i);
            if (this.mMaxScrap.get(i) > scrapHeapForType.size()) {
                scrapHeapForType.add(view);
            }
        }

        private ArrayList<View> getScrapHeapForType(int i) {
            ArrayList<View> arrayList = this.mScrap.get(i);
            if (arrayList == null) {
                arrayList = new ArrayList<>();
                this.mScrap.put(i, arrayList);
                if (this.mMaxScrap.indexOfKey(i) < 0) {
                    this.mMaxScrap.put(i, 10);
                }
            }
            return arrayList;
        }
    }
}
