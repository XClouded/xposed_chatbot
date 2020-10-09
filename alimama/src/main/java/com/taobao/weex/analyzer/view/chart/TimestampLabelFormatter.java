package com.taobao.weex.analyzer.view.chart;

import com.taobao.weex.ui.component.WXComponent;

public class TimestampLabelFormatter extends DefaultLabelFormatter {
    public String formatLabel(double d, boolean z) {
        if (z) {
            return formatTime(d);
        }
        return super.formatLabel(d, false);
    }

    private String formatTime(double d) {
        if (d < 60.0d) {
            return ((int) d) + "s";
        }
        int i = (int) (d % 60.0d);
        int i2 = (int) ((d / 60.0d) % 60.0d);
        int i3 = (int) (d / 3600.0d);
        StringBuilder sb = new StringBuilder();
        if (i3 > 0) {
            sb.append(i3);
            sb.append("h");
        }
        if (i2 > 0) {
            sb.append(i2);
            sb.append(WXComponent.PROP_FS_MATCH_PARENT);
        }
        if (i > 0) {
            sb.append(i);
            sb.append("s");
        }
        return sb.toString();
    }
}
