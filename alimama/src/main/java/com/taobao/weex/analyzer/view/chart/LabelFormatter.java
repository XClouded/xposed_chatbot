package com.taobao.weex.analyzer.view.chart;

public interface LabelFormatter {
    String formatLabel(double d, boolean z);

    void setViewport(Viewport viewport);
}
