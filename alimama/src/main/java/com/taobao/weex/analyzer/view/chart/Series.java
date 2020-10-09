package com.taobao.weex.analyzer.view.chart;

import android.graphics.Canvas;
import com.taobao.weex.analyzer.view.chart.DataPointInterface;
import java.util.Iterator;

public interface Series<E extends DataPointInterface> {
    void draw(ChartView chartView, Canvas canvas);

    int getColor();

    double getHighestValueX();

    double getHighestValueY();

    double getLowestValueX();

    double getLowestValueY();

    String getTitle();

    Iterator<E> getValues(double d, double d2);

    boolean isEmpty();

    void onGraphViewAttached(ChartView chartView);

    void onTap(float f, float f2);

    void setOnDataPointTapListener(OnDataPointTapListener onDataPointTapListener);
}
