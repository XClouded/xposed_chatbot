package com.taobao.weex.analyzer.view.chart;

import com.taobao.weex.el.parse.Operators;
import java.io.Serializable;

public class DataPoint implements DataPointInterface, Serializable {
    private static final long serialVersionUID = 1428263322645L;
    private double x;
    private double y;

    public DataPoint(double d, double d2) {
        this.x = d;
        this.y = d2;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public String toString() {
        return Operators.ARRAY_START_STR + this.x + "/" + this.y + Operators.ARRAY_END_STR;
    }
}
