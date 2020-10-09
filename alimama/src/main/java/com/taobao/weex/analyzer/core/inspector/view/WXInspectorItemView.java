package com.taobao.weex.analyzer.core.inspector.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import com.taobao.weex.analyzer.R;
import com.taobao.weex.analyzer.core.inspector.view.ViewInspectorManager;
import com.taobao.weex.analyzer.view.overlay.AbstractBizItemView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;

public class WXInspectorItemView extends AbstractBizItemView<ViewInspectorManager.InspectorInfo> {
    public static final String TYPE_NATIVE_LAYOUT = "native_layout";
    public static final String TYPE_VIRTUAL_DOM = "virtual_dom";
    private CSSBoxModelView mBoxView;
    private TextView mContent;
    private String mType;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }

    public WXInspectorItemView(Context context) {
        super(context);
    }

    public WXInspectorItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public WXInspectorItemView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void prepareView() {
        this.mContent = (TextView) findViewById(R.id.content);
        this.mBoxView = (CSSBoxModelView) findViewById(R.id.box_model);
    }

    /* access modifiers changed from: protected */
    public int getLayoutResId() {
        return R.layout.wxt_panel_inspector_view;
    }

    public void setType(String str) {
        this.mType = str;
    }

    public void inflateData(ViewInspectorManager.InspectorInfo inspectorInfo) {
        if (TYPE_VIRTUAL_DOM.equals(this.mType)) {
            this.mBoxView.setNative(false);
            applyInspectorInfoToBoxView(inspectorInfo.virtualViewInfo, this.mBoxView);
            StringBuilder sb = new StringBuilder();
            for (Map.Entry next : inspectorInfo.virtualViewInfo.entrySet()) {
                if (!TextUtils.isEmpty((CharSequence) next.getValue()) && !"0".equals(next.getValue())) {
                    sb.append((String) next.getKey());
                    sb.append(" : ");
                    sb.append((String) next.getValue());
                    sb.append("\n");
                }
            }
            this.mContent.setText(sb.toString());
        } else if (TYPE_NATIVE_LAYOUT.equals(this.mType)) {
            this.mBoxView.setNative(true);
            applyInspectorInfoToBoxView(inspectorInfo.nativeViewInfo, this.mBoxView);
            StringBuilder sb2 = new StringBuilder();
            for (Map.Entry next2 : inspectorInfo.nativeViewInfo.entrySet()) {
                if (!TextUtils.isEmpty((CharSequence) next2.getValue()) && !"0".equals(next2.getValue())) {
                    sb2.append((String) next2.getKey());
                    sb2.append(" : ");
                    sb2.append((String) next2.getValue());
                    sb2.append("\n");
                }
            }
            this.mContent.setText(sb2.toString());
        }
    }

    @VisibleForTesting
    static String getPureValue(@Nullable String str) {
        if (str == null || "".equals(str.trim())) {
            return "0";
        }
        String replaceAll = str.replaceAll("[^0-9.-]", "");
        int indexOf = replaceAll.indexOf(46);
        int length = replaceAll.length();
        if (indexOf < 0) {
            return replaceAll;
        }
        if (length - 1 <= indexOf) {
            return replaceAll.substring(0, indexOf);
        }
        try {
            return String.valueOf((int) ((double) Math.round(Double.valueOf(replaceAll).doubleValue())));
        } catch (Exception unused) {
            return replaceAll.substring(0, indexOf);
        }
    }

    private void applyInspectorInfoToBoxView(@NonNull Map<String, String> map, @NonNull CSSBoxModelView cSSBoxModelView) {
        if (!TextUtils.isEmpty(map.get("margin"))) {
            String pureValue = getPureValue(map.get("margin"));
            cSSBoxModelView.setMarginLeftText(pureValue);
            cSSBoxModelView.setMarginRightText(pureValue);
            cSSBoxModelView.setMarginTopText(pureValue);
            cSSBoxModelView.setMarginBottomText(pureValue);
        } else {
            cSSBoxModelView.setMarginLeftText(getPureValue(map.get("marginLeft")));
            cSSBoxModelView.setMarginRightText(getPureValue(map.get("marginRight")));
            cSSBoxModelView.setMarginTopText(getPureValue(map.get("marginTop")));
            cSSBoxModelView.setMarginBottomText(getPureValue(map.get("marginBottom")));
        }
        if (!TextUtils.isEmpty(map.get("borderWidth"))) {
            String pureValue2 = getPureValue(map.get("borderWidth"));
            cSSBoxModelView.setBorderLeftText(pureValue2);
            cSSBoxModelView.setBorderRightText(pureValue2);
            cSSBoxModelView.setBorderTopText(pureValue2);
            cSSBoxModelView.setBorderBottomText(pureValue2);
        } else {
            cSSBoxModelView.setBorderLeftText(getPureValue(map.get("borderLeftWidth")));
            cSSBoxModelView.setBorderRightText(getPureValue(map.get("borderRightWidth")));
            cSSBoxModelView.setBorderTopText(getPureValue(map.get("borderTopWidth")));
            cSSBoxModelView.setBorderBottomText(getPureValue(map.get("borderBottomWidth")));
        }
        if (!TextUtils.isEmpty(map.get("padding"))) {
            String pureValue3 = getPureValue(map.get("padding"));
            cSSBoxModelView.setPaddingLeftText(pureValue3);
            cSSBoxModelView.setPaddingRightText(pureValue3);
            cSSBoxModelView.setPaddingTopText(pureValue3);
            cSSBoxModelView.setPaddingBottomText(pureValue3);
        } else {
            cSSBoxModelView.setPaddingLeftText(getPureValue(map.get("paddingLeft")));
            cSSBoxModelView.setPaddingRightText(getPureValue(map.get("paddingRight")));
            cSSBoxModelView.setPaddingTopText(getPureValue(map.get("paddingTop")));
            cSSBoxModelView.setPaddingBottomText(getPureValue(map.get("paddingBottom")));
        }
        cSSBoxModelView.setWidthText(getPureValue(map.get("width")));
        cSSBoxModelView.setHeightText(getPureValue(map.get("height")));
        cSSBoxModelView.invalidate();
    }
}
