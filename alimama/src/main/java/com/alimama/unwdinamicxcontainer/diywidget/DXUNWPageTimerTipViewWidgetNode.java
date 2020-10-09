package com.alimama.unwdinamicxcontainer.diywidget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.alimama.unwdinamicxcontainer.R;
import com.alimama.unwdinamicxcontainer.diywidget.model.CountDownTipItem;
import com.taobao.android.dinamicx.widget.DXWidgetNode;
import com.taobao.android.dinamicx.widget.IDXBuilderWidgetNode;

public class DXUNWPageTimerTipViewWidgetNode extends DXWidgetNode {
    public static final long DXUNWPAGETIMERTIPVIEW_CALCTIME = 1740203017711995690L;
    public static final long DXUNWPAGETIMERTIPVIEW_LEFTTIPS = 4395082590335071315L;
    public static final long DXUNWPAGETIMERTIPVIEW_RIGHTTIPS = 1061266481069019591L;
    public static final long DXUNWPAGETIMERTIPVIEW_UNWPAGETIMERTIPVIEW = -2994581161636737965L;
    private String calcTime;
    private String leftTips;
    private String rightTips;

    public static class Builder implements IDXBuilderWidgetNode {
        public DXWidgetNode build(Object obj) {
            return new DXUNWPageTimerTipViewWidgetNode();
        }
    }

    public DXWidgetNode build(Object obj) {
        return new DXUNWPageTimerTipViewWidgetNode();
    }

    public void onClone(DXWidgetNode dXWidgetNode, boolean z) {
        if (dXWidgetNode != null && (dXWidgetNode instanceof DXUNWPageTimerTipViewWidgetNode)) {
            super.onClone(dXWidgetNode, z);
            DXUNWPageTimerTipViewWidgetNode dXUNWPageTimerTipViewWidgetNode = (DXUNWPageTimerTipViewWidgetNode) dXWidgetNode;
            this.calcTime = dXUNWPageTimerTipViewWidgetNode.calcTime;
            this.leftTips = dXUNWPageTimerTipViewWidgetNode.leftTips;
            this.rightTips = dXUNWPageTimerTipViewWidgetNode.rightTips;
        }
    }

    /* access modifiers changed from: protected */
    public View onCreateView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.timer_row_layout, (ViewGroup) null);
    }

    /* access modifiers changed from: protected */
    public void onRenderView(Context context, View view) {
        super.onRenderView(context, view);
        render(view);
    }

    private void render(View view) {
        TextView textView = (TextView) view.findViewById(R.id.dxc_right_tips_view);
        DXCLimitRobCountDownTipView dXCLimitRobCountDownTipView = (DXCLimitRobCountDownTipView) view.findViewById(R.id.dxc_limit_rob_tip_count_down);
        ((TextView) view.findViewById(R.id.dxc_left_tips_view)).setText(this.leftTips);
        if (!this.rightTips.contains("{{TIME}}")) {
            textView.setVisibility(0);
            dXCLimitRobCountDownTipView.setVisibility(8);
            textView.setText(this.rightTips);
            return;
        }
        textView.setVisibility(8);
        dXCLimitRobCountDownTipView.setVisibility(0);
        dXCLimitRobCountDownTipView.notifyData(new CountDownTipItem(this.calcTime, this.rightTips));
    }

    /* access modifiers changed from: protected */
    public void onBindEvent(Context context, View view, long j) {
        super.onBindEvent(context, view, j);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
    }

    /* access modifiers changed from: protected */
    public void onSetStringAttribute(long j, String str) {
        if (j == DXUNWPAGETIMERTIPVIEW_CALCTIME) {
            this.calcTime = str;
        } else if (j == DXUNWPAGETIMERTIPVIEW_LEFTTIPS) {
            this.leftTips = str;
        } else if (j == DXUNWPAGETIMERTIPVIEW_RIGHTTIPS) {
            this.rightTips = str;
        } else {
            super.onSetStringAttribute(j, str);
        }
    }
}
