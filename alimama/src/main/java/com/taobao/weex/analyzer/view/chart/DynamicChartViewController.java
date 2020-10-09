package com.taobao.weex.analyzer.view.chart;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

public class DynamicChartViewController {
    private ChartView mChartView;
    private int mMaxPoints;

    private DynamicChartViewController(ChartView chartView) {
        this.mChartView = chartView;
    }

    public void setMaxPoints(int i) {
        this.mMaxPoints = i;
    }

    public View getChartView() {
        return this.mChartView;
    }

    public void updateAxisY(double d, double d2, int i) {
        if (this.mChartView != null) {
            Viewport viewport = this.mChartView.getViewport();
            GridLabelRenderer gridLabelRenderer = this.mChartView.getGridLabelRenderer();
            viewport.setYAxisBoundsManual(true);
            if (i > 0) {
                gridLabelRenderer.setNumVerticalLabels(i);
            }
            viewport.setMinY(d);
            viewport.setMaxY(d2);
        }
    }

    public void appendPointAndInvalidate(double d, double d2) {
        ((LineGraphSeries) this.mChartView.getSeries().get(0)).appendData(new DataPoint(d, d2), true, this.mMaxPoints);
    }

    public void appendPointAndInvalidate2(double d, double d2) {
        if (this.mChartView.getSeries().size() >= 2) {
            ((LineGraphSeries) this.mChartView.getSeries().get(1)).appendData(new DataPoint(d, d2), true, this.mMaxPoints);
        }
    }

    public double getMaxY() {
        if (this.mChartView == null) {
            return 0.0d;
        }
        return this.mChartView.getViewport().getMaxY(false);
    }

    public double getMaxX() {
        if (this.mChartView == null) {
            return 0.0d;
        }
        return this.mChartView.getViewport().getMaxX(false);
    }

    public double getMinX() {
        if (this.mChartView == null) {
            return 0.0d;
        }
        return this.mChartView.getViewport().getMinX(false);
    }

    public double getMinY() {
        if (this.mChartView == null) {
            return 0.0d;
        }
        return this.mChartView.getViewport().getMinY(false);
    }

    public static class Builder {
        private boolean isFill = false;
        private int mAxisColor = -1;
        private Context mContext;
        private int mFill2Color = -16776961;
        private int mFillColor = -16711936;
        private int mLabelColor = -1;
        private LabelFormatter mLabelFormatter;
        private int mLine2Color = -16776961;
        private int mLineColor = -16711936;
        private int mMaxDataPoints = 0;
        private double mMaxX = -1.0d;
        private double mMaxY = -1.0d;
        private double mMinX = -1.0d;
        private double mMinY = -1.0d;
        private int mNumXLabels = 0;
        private int mNumYLabels = 0;
        private String mSeries2Title;
        private String mSeriesTitle;
        private String mTitle;
        private int mViewBackground = -16777216;
        private String mXAxisTitle = null;
        private String mYAxisTitle = null;

        public Builder(Context context) {
            this.mContext = context;
        }

        public DynamicChartViewController build() {
            LineGraphSeries lineGraphSeries;
            ChartView chartView = new ChartView(this.mContext);
            chartView.setTouchEnabled(false);
            GridLabelRenderer gridLabelRenderer = chartView.getGridLabelRenderer();
            LineGraphSeries lineGraphSeries2 = new LineGraphSeries();
            chartView.addSeries(lineGraphSeries2);
            Viewport viewport = chartView.getViewport();
            chartView.setBackgroundColor(this.mViewBackground);
            gridLabelRenderer.setHorizontalLabelsColor(this.mLabelColor);
            gridLabelRenderer.setVerticalLabelsColor(this.mLabelColor);
            gridLabelRenderer.setHorizontalAxisTitleColor(this.mLabelColor);
            gridLabelRenderer.setVerticalAxisTitleColor(this.mLabelColor);
            chartView.setTitleColor(this.mLabelColor);
            gridLabelRenderer.setGridColor(this.mAxisColor);
            lineGraphSeries2.setColor(this.mLineColor);
            if (!TextUtils.isEmpty(this.mSeriesTitle)) {
                lineGraphSeries2.setTitle(this.mSeriesTitle);
            }
            if (!TextUtils.isEmpty(this.mSeries2Title)) {
                lineGraphSeries = new LineGraphSeries();
                chartView.addSeries(lineGraphSeries);
                lineGraphSeries.setTitle(this.mSeries2Title);
                lineGraphSeries.setColor(this.mLine2Color);
            } else {
                lineGraphSeries = null;
            }
            lineGraphSeries2.setDrawBackground(this.isFill);
            if (this.isFill) {
                lineGraphSeries2.setBackgroundColor(this.mFillColor);
            }
            if (lineGraphSeries != null) {
                lineGraphSeries.setDrawBackground(this.isFill);
                if (this.isFill) {
                    lineGraphSeries.setBackgroundColor(this.mFill2Color);
                }
            }
            if (this.mMinX == -1.0d || this.mMaxX == -1.0d) {
                viewport.setXAxisBoundsManual(false);
            } else {
                viewport.setXAxisBoundsManual(true);
                viewport.setMinX(this.mMinX);
                viewport.setMaxX(this.mMaxX);
            }
            if (this.mMinY == -1.0d || this.mMaxY == -1.0d) {
                viewport.setYAxisBoundsManual(false);
            } else {
                viewport.setYAxisBoundsManual(true);
                viewport.setMinY(this.mMinY);
                viewport.setMaxY(this.mMaxY);
            }
            if (this.mNumXLabels != 0) {
                gridLabelRenderer.setNumHorizontalLabels(this.mNumXLabels);
            }
            if (this.mNumYLabels != 0) {
                gridLabelRenderer.setNumVerticalLabels(this.mNumYLabels);
            }
            if (this.mXAxisTitle != null) {
                gridLabelRenderer.setHorizontalAxisTitle(this.mXAxisTitle);
            }
            if (this.mYAxisTitle != null) {
                gridLabelRenderer.setVerticalAxisTitle(this.mYAxisTitle);
            }
            if (this.mTitle != null) {
                chartView.setTitle(this.mTitle);
            }
            if (this.mLabelFormatter != null) {
                gridLabelRenderer.setLabelFormatter(this.mLabelFormatter);
                gridLabelRenderer.setHumanRounding(false);
            }
            viewport.setScalable(false);
            viewport.setScalableY(false);
            viewport.setScrollable(false);
            viewport.setScrollableY(false);
            DynamicChartViewController dynamicChartViewController = new DynamicChartViewController(chartView);
            if (this.mMaxDataPoints > 0) {
                dynamicChartViewController.setMaxPoints(this.mMaxDataPoints);
            }
            return dynamicChartViewController;
        }

        public Builder labelFormatter(@Nullable LabelFormatter labelFormatter) {
            this.mLabelFormatter = labelFormatter;
            return this;
        }

        public Builder title(String str) {
            this.mTitle = str;
            return this;
        }

        public Builder lineTitle(@Nullable String str) {
            this.mSeriesTitle = str;
            return this;
        }

        public Builder lineTitle2(@Nullable String str) {
            this.mSeries2Title = str;
            return this;
        }

        public Builder backgroundColor(@ColorInt int i) {
            this.mViewBackground = i;
            return this;
        }

        public Builder fillColor(@ColorInt int i) {
            this.mFillColor = i;
            return this;
        }

        public Builder fillColor2(@ColorInt int i) {
            this.mFill2Color = i;
            return this;
        }

        public Builder labelColor(@ColorInt int i) {
            this.mLabelColor = i;
            return this;
        }

        public Builder axisColor(@ColorInt int i) {
            this.mAxisColor = i;
            return this;
        }

        public Builder lineColor(@ColorInt int i) {
            this.mLineColor = i;
            return this;
        }

        public Builder lineColor2(@ColorInt int i) {
            this.mLine2Color = i;
            return this;
        }

        public Builder isFill(boolean z) {
            this.isFill = z;
            return this;
        }

        public Builder minX(double d) {
            this.mMinX = d;
            return this;
        }

        public Builder maxX(double d) {
            this.mMaxX = d;
            return this;
        }

        public Builder minY(double d) {
            this.mMinY = d;
            return this;
        }

        public Builder maxY(double d) {
            this.mMaxY = d;
            return this;
        }

        public Builder numXLabels(int i) {
            this.mNumXLabels = i;
            return this;
        }

        public Builder numYLabels(int i) {
            this.mNumYLabels = i;
            return this;
        }

        public Builder titleOfAxisX(@Nullable String str) {
            this.mXAxisTitle = str;
            return this;
        }

        public Builder titleOfAxisY(@Nullable String str) {
            this.mYAxisTitle = str;
            return this;
        }

        public Builder maxDataPoints(int i) {
            this.mMaxDataPoints = i;
            return this;
        }
    }
}
