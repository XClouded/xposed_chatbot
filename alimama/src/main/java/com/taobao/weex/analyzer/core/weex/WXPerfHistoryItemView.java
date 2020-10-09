package com.taobao.weex.analyzer.core.weex;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;
import android.widget.Toast;
import com.taobao.weex.analyzer.R;
import com.taobao.weex.analyzer.utils.ViewUtils;
import com.taobao.weex.analyzer.view.chart.ChartView;
import com.taobao.weex.analyzer.view.chart.DataPoint;
import com.taobao.weex.analyzer.view.chart.DataPointInterface;
import com.taobao.weex.analyzer.view.chart.GridLabelRenderer;
import com.taobao.weex.analyzer.view.chart.LegendRenderer;
import com.taobao.weex.analyzer.view.chart.LineGraphSeries;
import com.taobao.weex.analyzer.view.chart.OnDataPointTapListener;
import com.taobao.weex.analyzer.view.chart.Series;
import com.taobao.weex.analyzer.view.chart.Viewport;
import com.taobao.weex.analyzer.view.overlay.AbstractBizItemView;
import com.taobao.weex.common.WXPerformance;
import com.taobao.weex.el.parse.Operators;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WXPerfHistoryItemView extends AbstractBizItemView<List<Performance>> implements OnDataPointTapListener {
    private TextView mAverageVal;
    private ChartView mGraphView;

    public WXPerfHistoryItemView(Context context) {
        super(context);
    }

    public WXPerfHistoryItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public WXPerfHistoryItemView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void prepareView() {
        this.mGraphView = (ChartView) findViewById(R.id.chart);
        this.mAverageVal = (TextView) findViewById(R.id.average);
        GridLabelRenderer gridLabelRenderer = this.mGraphView.getGridLabelRenderer();
        Viewport viewport = this.mGraphView.getViewport();
        viewport.setScalable(false);
        viewport.setScalableY(false);
        viewport.setScrollable(false);
        viewport.setScrollableY(false);
        this.mGraphView.setBackgroundColor(-1);
        gridLabelRenderer.setHorizontalLabelsColor(-16777216);
        gridLabelRenderer.setVerticalLabelsColor(-16777216);
        gridLabelRenderer.setHorizontalAxisTitleColor(-16777216);
        gridLabelRenderer.setVerticalAxisTitleColor(-16777216);
        this.mGraphView.setTitleColor(-16777216);
    }

    /* access modifiers changed from: protected */
    public int getLayoutResId() {
        return R.layout.wxt_panel_history_perf_view;
    }

    public void inflateData(List<Performance> list) {
        int i;
        double d;
        double d2;
        double d3;
        WXPerfHistoryItemView wXPerfHistoryItemView = this;
        int size = list.size();
        if (size != 0) {
            DataPoint[] dataPointArr = new DataPoint[size];
            DataPoint[] dataPointArr2 = new DataPoint[size];
            DataPoint[] dataPointArr3 = new DataPoint[size];
            long j = 0;
            long j2 = 0;
            long j3 = 0;
            double d4 = 480.0d;
            int i2 = 0;
            while (i2 < size) {
                Map<String, Double> measureMap = list.get(i2).getMeasureMap();
                if (measureMap != null) {
                    d = measureMap.get(WXPerformance.Measure.interactionTime.toString()).doubleValue();
                    i = size;
                    d2 = measureMap.get(WXPerformance.Measure.fsRenderTime.toString()).doubleValue();
                    d3 = measureMap.get(WXPerformance.Measure.networkTime.toString()).doubleValue();
                } else {
                    i = size;
                    d3 = 0.0d;
                    d2 = 0.0d;
                    d = 0.0d;
                }
                long j4 = j;
                double d5 = (double) i2;
                dataPointArr[i2] = new DataPoint(d5, d);
                dataPointArr2[i2] = new DataPoint(d5, d2);
                dataPointArr3[i2] = new DataPoint(d5, d3);
                d4 = Math.max(Math.max(d2, d3), Math.max(d, d4));
                double d6 = (double) j3;
                Double.isNaN(d6);
                long j5 = (long) (d6 + d);
                double d7 = (double) j4;
                Double.isNaN(d7);
                j = (long) (d7 + d2);
                double d8 = (double) j2;
                Double.isNaN(d8);
                i2++;
                j2 = (long) (d8 + d3);
                j3 = j5;
                size = i;
                wXPerfHistoryItemView = this;
            }
            int i3 = size;
            long j6 = j2;
            Viewport viewport = wXPerfHistoryItemView.mGraphView.getViewport();
            GridLabelRenderer gridLabelRenderer = wXPerfHistoryItemView.mGraphView.getGridLabelRenderer();
            gridLabelRenderer.setHumanRounding(false);
            gridLabelRenderer.setNumHorizontalLabels(i3 + 1);
            gridLabelRenderer.setNumVerticalLabels(9);
            viewport.setXAxisBoundsManual(true);
            viewport.setMinX(0.0d);
            int i4 = i3;
            viewport.setMaxX((double) i4);
            viewport.setYAxisBoundsManual(true);
            viewport.setMinY(0.0d);
            viewport.setMaxY(ViewUtils.findSuitableVal(d4, 8));
            LineGraphSeries lineGraphSeries = new LineGraphSeries(dataPointArr);
            LineGraphSeries lineGraphSeries2 = new LineGraphSeries(dataPointArr2);
            LineGraphSeries lineGraphSeries3 = new LineGraphSeries(dataPointArr3);
            lineGraphSeries.setTitle("可交互时间");
            lineGraphSeries2.setTitle("首屏时间");
            lineGraphSeries3.setTitle("网络时间");
            lineGraphSeries.setOnDataPointTapListener(wXPerfHistoryItemView);
            lineGraphSeries2.setOnDataPointTapListener(wXPerfHistoryItemView);
            lineGraphSeries3.setOnDataPointTapListener(wXPerfHistoryItemView);
            lineGraphSeries.setColor(Color.parseColor("#E91E63"));
            lineGraphSeries2.setColor(Color.parseColor("#9C27B0"));
            lineGraphSeries3.setColor(Color.parseColor("#CDDC39"));
            lineGraphSeries.setDrawDataPoints(true);
            lineGraphSeries2.setDrawDataPoints(true);
            lineGraphSeries3.setDrawDataPoints(true);
            lineGraphSeries.setAnimated(true);
            lineGraphSeries2.setAnimated(true);
            lineGraphSeries3.setAnimated(true);
            wXPerfHistoryItemView.mGraphView.addSeries(lineGraphSeries);
            wXPerfHistoryItemView.mGraphView.addSeries(lineGraphSeries2);
            wXPerfHistoryItemView.mGraphView.addSeries(lineGraphSeries3);
            LegendRenderer legendRenderer = wXPerfHistoryItemView.mGraphView.getLegendRenderer();
            legendRenderer.setVisible(true);
            legendRenderer.setBackgroundColor(0);
            legendRenderer.setAlign(LegendRenderer.LegendAlign.TOP);
            float f = (float) i4;
            wXPerfHistoryItemView.mAverageVal.setText(String.format(Locale.CHINA, getContext().getResources().getString(R.string.wxt_average), new Object[]{Float.valueOf(((float) j3) / f), Float.valueOf(((float) j) / f), Float.valueOf(((float) j6) / f)}));
        }
    }

    public void onTap(Series series, DataPointInterface dataPointInterface) {
        Context context = getContext();
        Toast.makeText(context, series.getTitle() + Operators.BRACKET_START_STR + dataPointInterface.getX() + "," + dataPointInterface.getY() + Operators.BRACKET_END_STR, 0).show();
    }
}
