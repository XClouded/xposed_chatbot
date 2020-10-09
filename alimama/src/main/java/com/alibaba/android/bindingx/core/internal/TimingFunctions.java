package com.alibaba.android.bindingx.core.internal;

import android.view.animation.Interpolator;
import androidx.annotation.Keep;
import androidx.annotation.Nullable;
import androidx.core.view.animation.PathInterpolatorCompat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import org.json.JSONException;

@Keep
public class TimingFunctions {
    /* access modifiers changed from: private */
    public static final InnerCache<BezierInterpolatorWrapper> cache = new InnerCache<>(4);

    /* access modifiers changed from: private */
    public static double easeOutBounce(double d, double d2, double d3, double d4) {
        double d5 = d / d4;
        if (d5 < 0.36363636363636365d) {
            return (d3 * 7.5625d * d5 * d5) + d2;
        }
        if (d5 < 0.7272727272727273d) {
            double d6 = d5 - 0.5454545454545454d;
            return (d3 * ((7.5625d * d6 * d6) + 0.75d)) + d2;
        } else if (d5 < 0.9090909090909091d) {
            double d7 = d5 - 0.8181818181818182d;
            return (d3 * ((7.5625d * d7 * d7) + 0.9375d)) + d2;
        } else {
            double d8 = d5 - 0.9545454545454546d;
            return (d3 * ((7.5625d * d8 * d8) + 0.984375d)) + d2;
        }
    }

    private TimingFunctions() {
    }

    public static Object linear() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
                double doubleValue = ((Double) arrayList.get(0)).doubleValue();
                double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
                double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
                double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
                return Double.valueOf((doubleValue3 * (Math.min(doubleValue, doubleValue4) / doubleValue4)) + doubleValue2);
            }
        };
    }

    public static Object cubicBezier() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
                ArrayList<Object> arrayList2 = arrayList;
                double doubleValue = ((Double) arrayList2.get(0)).doubleValue();
                double doubleValue2 = ((Double) arrayList2.get(1)).doubleValue();
                double doubleValue3 = ((Double) arrayList2.get(2)).doubleValue();
                double doubleValue4 = ((Double) arrayList2.get(3)).doubleValue();
                double doubleValue5 = ((Double) arrayList2.get(4)).doubleValue();
                double doubleValue6 = ((Double) arrayList2.get(5)).doubleValue();
                double doubleValue7 = ((Double) arrayList2.get(6)).doubleValue();
                double doubleValue8 = ((Double) arrayList2.get(7)).doubleValue();
                double min = Math.min(doubleValue, doubleValue4);
                if (min == doubleValue4) {
                    return Double.valueOf(doubleValue2 + doubleValue3);
                }
                float f = (float) doubleValue5;
                float f2 = (float) doubleValue6;
                float f3 = (float) doubleValue7;
                float f4 = (float) doubleValue8;
                BezierInterpolatorWrapper access$000 = TimingFunctions.isCacheHit(f, f2, f3, f4);
                if (access$000 == null) {
                    access$000 = new BezierInterpolatorWrapper(f, f2, f3, f4);
                    TimingFunctions.cache.add(access$000);
                }
                double interpolation = (double) access$000.getInterpolation((float) (min / doubleValue4));
                Double.isNaN(interpolation);
                return Double.valueOf((doubleValue3 * interpolation) + doubleValue2);
            }
        };
    }

    /* access modifiers changed from: private */
    @Nullable
    public static BezierInterpolatorWrapper isCacheHit(float f, float f2, float f3, float f4) {
        for (BezierInterpolatorWrapper next : cache.getAll()) {
            if (Float.compare(next.x1, f) == 0 && Float.compare(next.x2, f3) == 0 && Float.compare(next.y1, f2) == 0 && Float.compare(next.y2, f4) == 0) {
                return next;
            }
        }
        return null;
    }

    public static Object easeInQuad() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
                double doubleValue = ((Double) arrayList.get(0)).doubleValue();
                double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
                double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
                double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
                double min = Math.min(doubleValue, doubleValue4) / doubleValue4;
                return Double.valueOf((doubleValue3 * min * min) + doubleValue2);
            }
        };
    }

    public static Object easeOutQuad() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
                double doubleValue = ((Double) arrayList.get(0)).doubleValue();
                double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
                double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
                double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
                double min = Math.min(doubleValue, doubleValue4) / doubleValue4;
                return Double.valueOf(((-doubleValue3) * min * (min - 2.0d)) + doubleValue2);
            }
        };
    }

    public static Object easeInOutQuad() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
                double doubleValue = ((Double) arrayList.get(0)).doubleValue();
                double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
                double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
                double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
                double min = Math.min(doubleValue, doubleValue4) / (doubleValue4 / 2.0d);
                if (min < 1.0d) {
                    return Double.valueOf(((doubleValue3 / 2.0d) * min * min) + doubleValue2);
                }
                double d = min - 1.0d;
                return Double.valueOf((((-doubleValue3) / 2.0d) * ((d * (d - 2.0d)) - 1.0d)) + doubleValue2);
            }
        };
    }

    public static Object easeInCubic() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
                double doubleValue = ((Double) arrayList.get(0)).doubleValue();
                double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
                double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
                double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
                double min = Math.min(doubleValue, doubleValue4) / doubleValue4;
                return Double.valueOf((doubleValue3 * min * min * min) + doubleValue2);
            }
        };
    }

    public static Object easeOutCubic() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
                double doubleValue = ((Double) arrayList.get(0)).doubleValue();
                double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
                double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
                double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
                double min = (Math.min(doubleValue, doubleValue4) / doubleValue4) - 1.0d;
                return Double.valueOf((doubleValue3 * ((min * min * min) + 1.0d)) + doubleValue2);
            }
        };
    }

    public static Object easeInOutCubic() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
                double doubleValue = ((Double) arrayList.get(0)).doubleValue();
                double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
                double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
                double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
                double min = Math.min(doubleValue, doubleValue4) / (doubleValue4 / 2.0d);
                if (min < 1.0d) {
                    return Double.valueOf(((doubleValue3 / 2.0d) * min * min * min) + doubleValue2);
                }
                double d = min - 2.0d;
                return Double.valueOf(((doubleValue3 / 2.0d) * ((d * d * d) + 2.0d)) + doubleValue2);
            }
        };
    }

    public static Object easeInQuart() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
                double doubleValue = ((Double) arrayList.get(0)).doubleValue();
                double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
                double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
                double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
                double min = Math.min(doubleValue, doubleValue4) / doubleValue4;
                return Double.valueOf((doubleValue3 * min * min * min * min) + doubleValue2);
            }
        };
    }

    public static Object easeOutQuart() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
                double doubleValue = ((Double) arrayList.get(0)).doubleValue();
                double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
                double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
                double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
                double min = (Math.min(doubleValue, doubleValue4) / doubleValue4) - 1.0d;
                return Double.valueOf(((-doubleValue3) * ((((min * min) * min) * min) - 1.0d)) + doubleValue2);
            }
        };
    }

    public static Object easeInOutQuart() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
                double doubleValue = ((Double) arrayList.get(0)).doubleValue();
                double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
                double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
                double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
                double min = Math.min(doubleValue, doubleValue4) / (doubleValue4 / 2.0d);
                if (min < 1.0d) {
                    return Double.valueOf(((doubleValue3 / 2.0d) * min * min * min * min) + doubleValue2);
                }
                double d = min - 2.0d;
                return Double.valueOf((((-doubleValue3) / 2.0d) * ((((d * d) * d) * d) - 2.0d)) + doubleValue2);
            }
        };
    }

    public static Object easeInQuint() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
                double doubleValue = ((Double) arrayList.get(0)).doubleValue();
                double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
                double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
                double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
                double min = Math.min(doubleValue, doubleValue4) / doubleValue4;
                return Double.valueOf((doubleValue3 * min * min * min * min * min) + doubleValue2);
            }
        };
    }

    public static Object easeOutQuint() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
                double doubleValue = ((Double) arrayList.get(0)).doubleValue();
                double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
                double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
                double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
                double min = (Math.min(doubleValue, doubleValue4) / doubleValue4) - 1.0d;
                return Double.valueOf((doubleValue3 * ((min * min * min * min * min) + 1.0d)) + doubleValue2);
            }
        };
    }

    public static Object easeInOutQuint() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
                double doubleValue = ((Double) arrayList.get(0)).doubleValue();
                double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
                double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
                double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
                double min = Math.min(doubleValue, doubleValue4) / (doubleValue4 / 2.0d);
                if (min < 1.0d) {
                    return Double.valueOf(((doubleValue3 / 2.0d) * min * min * min * min * min) + doubleValue2);
                }
                double d = min - 2.0d;
                return Double.valueOf(((doubleValue3 / 2.0d) * ((d * d * d * d * d) + 2.0d)) + doubleValue2);
            }
        };
    }

    public static Object easeInSine() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
                double doubleValue = ((Double) arrayList.get(0)).doubleValue();
                double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
                double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
                double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
                return Double.valueOf(((-doubleValue3) * Math.cos((Math.min(doubleValue, doubleValue4) / doubleValue4) * 1.5707963267948966d)) + doubleValue3 + doubleValue2);
            }
        };
    }

    public static Object easeOutSine() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
                double doubleValue = ((Double) arrayList.get(0)).doubleValue();
                double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
                double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
                double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
                return Double.valueOf((doubleValue3 * Math.sin((Math.min(doubleValue, doubleValue4) / doubleValue4) * 1.5707963267948966d)) + doubleValue2);
            }
        };
    }

    public static Object easeInOutSine() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
                double doubleValue = ((Double) arrayList.get(0)).doubleValue();
                double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
                double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
                double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
                return Double.valueOf((((-doubleValue3) / 2.0d) * (Math.cos((Math.min(doubleValue, doubleValue4) * 3.141592653589793d) / doubleValue4) - 1.0d)) + doubleValue2);
            }
        };
    }

    public static Object easeInExpo() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
                double doubleValue = ((Double) arrayList.get(0)).doubleValue();
                double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
                double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
                double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
                double min = Math.min(doubleValue, doubleValue4);
                if (min != 0.0d) {
                    doubleValue2 += doubleValue3 * Math.pow(2.0d, ((min / doubleValue4) - 1.0d) * 10.0d);
                }
                return Double.valueOf(doubleValue2);
            }
        };
    }

    public static Object easeOutExpo() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
                double d;
                double doubleValue = ((Double) arrayList.get(0)).doubleValue();
                double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
                double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
                double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
                double min = Math.min(doubleValue, doubleValue4);
                if (min == doubleValue4) {
                    d = doubleValue2 + doubleValue3;
                } else {
                    d = doubleValue2 + (doubleValue3 * ((-Math.pow(2.0d, (min * -10.0d) / doubleValue4)) + 1.0d));
                }
                return Double.valueOf(d);
            }
        };
    }

    public static Object easeInOutExpo() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
                double doubleValue = ((Double) arrayList.get(0)).doubleValue();
                double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
                double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
                double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
                double min = Math.min(doubleValue, doubleValue4);
                if (min == 0.0d) {
                    return Double.valueOf(doubleValue2);
                }
                if (min == doubleValue4) {
                    return Double.valueOf(doubleValue2 + doubleValue3);
                }
                double d = min / (doubleValue4 / 2.0d);
                if (d < 1.0d) {
                    return Double.valueOf(((doubleValue3 / 2.0d) * Math.pow(2.0d, (d - 1.0d) * 10.0d)) + doubleValue2);
                }
                return Double.valueOf(((doubleValue3 / 2.0d) * ((-Math.pow(2.0d, (d - 1.0d) * -10.0d)) + 2.0d)) + doubleValue2);
            }
        };
    }

    public static Object easeInCirc() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
                double doubleValue = ((Double) arrayList.get(0)).doubleValue();
                double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
                double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
                double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
                double min = Math.min(doubleValue, doubleValue4) / doubleValue4;
                return Double.valueOf(((-doubleValue3) * (Math.sqrt(1.0d - (min * min)) - 1.0d)) + doubleValue2);
            }
        };
    }

    public static Object easeOutCirc() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
                double doubleValue = ((Double) arrayList.get(0)).doubleValue();
                double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
                double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
                double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
                double min = (Math.min(doubleValue, doubleValue4) / doubleValue4) - 1.0d;
                return Double.valueOf((doubleValue3 * Math.sqrt(1.0d - (min * min))) + doubleValue2);
            }
        };
    }

    public static Object easeInOutCirc() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
                double doubleValue = ((Double) arrayList.get(0)).doubleValue();
                double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
                double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
                double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
                double min = Math.min(doubleValue, doubleValue4) / (doubleValue4 / 2.0d);
                if (min < 1.0d) {
                    return Double.valueOf((((-doubleValue3) / 2.0d) * (Math.sqrt(1.0d - (min * min)) - 1.0d)) + doubleValue2);
                }
                double d = min - 2.0d;
                return Double.valueOf(((doubleValue3 / 2.0d) * (Math.sqrt(1.0d - (d * d)) + 1.0d)) + doubleValue2);
            }
        };
    }

    public static Object easeInElastic() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
                double asin;
                ArrayList<Object> arrayList2 = arrayList;
                double doubleValue = ((Double) arrayList2.get(0)).doubleValue();
                double doubleValue2 = ((Double) arrayList2.get(1)).doubleValue();
                double doubleValue3 = ((Double) arrayList2.get(2)).doubleValue();
                double doubleValue4 = ((Double) arrayList2.get(3)).doubleValue();
                double min = Math.min(doubleValue, doubleValue4);
                if (min == 0.0d) {
                    return Double.valueOf(doubleValue2);
                }
                double d = min / doubleValue4;
                if (d == 1.0d) {
                    return Double.valueOf(doubleValue2 + doubleValue3);
                }
                double d2 = 0.3d * doubleValue4;
                if (doubleValue3 < Math.abs(doubleValue3)) {
                    asin = d2 / 4.0d;
                } else {
                    asin = (d2 / 6.283185307179586d) * Math.asin(doubleValue3 / doubleValue3);
                }
                double d3 = d - 1.0d;
                return Double.valueOf((-(doubleValue3 * Math.pow(2.0d, d3 * 10.0d) * Math.sin((((d3 * doubleValue4) - asin) * 6.283185307179586d) / d2))) + doubleValue2);
            }
        };
    }

    public static Object easeOutElastic() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) {
                double d;
                ArrayList<Object> arrayList2 = arrayList;
                double doubleValue = ((Double) arrayList2.get(0)).doubleValue();
                double doubleValue2 = ((Double) arrayList2.get(1)).doubleValue();
                double doubleValue3 = ((Double) arrayList2.get(2)).doubleValue();
                double doubleValue4 = ((Double) arrayList2.get(3)).doubleValue();
                double min = Math.min(doubleValue, doubleValue4);
                if (min == 0.0d) {
                    return Double.valueOf(doubleValue2);
                }
                double d2 = min / doubleValue4;
                if (d2 == 1.0d) {
                    return Double.valueOf(doubleValue2 + doubleValue3);
                }
                double d3 = 0.3d * doubleValue4;
                if (doubleValue3 < Math.abs(doubleValue3)) {
                    d = d3 / 4.0d;
                } else {
                    d = (d3 / 6.283185307179586d) * Math.asin(doubleValue3 / doubleValue3);
                }
                return Double.valueOf((Math.pow(2.0d, d2 * -10.0d) * doubleValue3 * Math.sin((((d2 * doubleValue4) - d) * 6.283185307179586d) / d3)) + doubleValue3 + doubleValue2);
            }
        };
    }

    public static Object easeInOutElastic() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) {
                double d;
                ArrayList<Object> arrayList2 = arrayList;
                double doubleValue = ((Double) arrayList2.get(0)).doubleValue();
                double doubleValue2 = ((Double) arrayList2.get(1)).doubleValue();
                double doubleValue3 = ((Double) arrayList2.get(2)).doubleValue();
                double doubleValue4 = ((Double) arrayList2.get(3)).doubleValue();
                double min = Math.min(doubleValue, doubleValue4);
                if (min == 0.0d) {
                    return Double.valueOf(doubleValue2);
                }
                double d2 = min / (doubleValue4 / 2.0d);
                if (d2 == 2.0d) {
                    return Double.valueOf(doubleValue2 + doubleValue3);
                }
                double d3 = 0.44999999999999996d * doubleValue4;
                if (doubleValue3 < Math.abs(doubleValue3)) {
                    d = d3 / 4.0d;
                } else {
                    d = (d3 / 6.283185307179586d) * Math.asin(doubleValue3 / doubleValue3);
                }
                if (d2 < 1.0d) {
                    double d4 = d2 - 1.0d;
                    return Double.valueOf((doubleValue3 * Math.pow(2.0d, d4 * 10.0d) * Math.sin((((d4 * doubleValue4) - d) * 6.283185307179586d) / d3) * -0.5d) + doubleValue2);
                }
                double d5 = d2 - 1.0d;
                return Double.valueOf((Math.pow(2.0d, -10.0d * d5) * doubleValue3 * Math.sin((((d5 * doubleValue4) - d) * 6.283185307179586d) / d3) * 0.5d) + doubleValue3 + doubleValue2);
            }
        };
    }

    public static Object easeInBack() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
                double doubleValue = ((Double) arrayList.get(0)).doubleValue();
                double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
                double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
                double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
                double min = Math.min(doubleValue, doubleValue4) / doubleValue4;
                return Double.valueOf((doubleValue3 * min * min * ((2.70158d * min) - 1.70158d)) + doubleValue2);
            }
        };
    }

    public static Object easeOutBack() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
                double doubleValue = ((Double) arrayList.get(0)).doubleValue();
                double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
                double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
                double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
                double min = (Math.min(doubleValue, doubleValue4) / doubleValue4) - 1.0d;
                return Double.valueOf((doubleValue3 * ((min * min * ((2.70158d * min) + 1.70158d)) + 1.0d)) + doubleValue2);
            }
        };
    }

    public static Object easeInOutBack() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
                double doubleValue = ((Double) arrayList.get(0)).doubleValue();
                double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
                double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
                double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
                double min = Math.min(doubleValue, doubleValue4) / (doubleValue4 / 2.0d);
                if (min < 1.0d) {
                    return Double.valueOf(((doubleValue3 / 2.0d) * min * min * ((3.5949095d * min) - 2.5949095d)) + doubleValue2);
                }
                double d = min - 2.0d;
                return Double.valueOf(((doubleValue3 / 2.0d) * ((d * d * ((3.5949095d * d) + 2.5949095d)) + 2.0d)) + doubleValue2);
            }
        };
    }

    public static Object easeInBounce() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
                double doubleValue = ((Double) arrayList.get(0)).doubleValue();
                double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
                double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
                double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
                return Double.valueOf(TimingFunctions.easeInBounce(Math.min(doubleValue, doubleValue4), doubleValue2, doubleValue3, doubleValue4));
            }
        };
    }

    public static Object easeOutBounce() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
                double doubleValue = ((Double) arrayList.get(0)).doubleValue();
                double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
                double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
                double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
                return Double.valueOf(TimingFunctions.easeOutBounce(Math.min(doubleValue, doubleValue4), doubleValue2, doubleValue3, doubleValue4));
            }
        };
    }

    public static Object easeInOutBounce() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
                ArrayList<Object> arrayList2 = arrayList;
                double doubleValue = ((Double) arrayList2.get(0)).doubleValue();
                double doubleValue2 = ((Double) arrayList2.get(1)).doubleValue();
                double doubleValue3 = ((Double) arrayList2.get(2)).doubleValue();
                double doubleValue4 = ((Double) arrayList2.get(3)).doubleValue();
                double min = Math.min(doubleValue, doubleValue4);
                if (min < doubleValue4 / 2.0d) {
                    return Double.valueOf((TimingFunctions.easeInBounce(min * 2.0d, 0.0d, doubleValue3, doubleValue4) * 0.5d) + doubleValue2);
                }
                return Double.valueOf((TimingFunctions.easeOutBounce((min * 2.0d) - doubleValue4, 0.0d, doubleValue3, doubleValue4) * 0.5d) + (doubleValue3 * 0.5d) + doubleValue2);
            }
        };
    }

    /* access modifiers changed from: private */
    public static double easeInBounce(double d, double d2, double d3, double d4) {
        return (d3 - easeOutBounce(d4 - d, 0.0d, d3, d4)) + d2;
    }

    private static class InnerCache<T> {
        private final ArrayDeque<T> deque;

        InnerCache(int i) {
            this.deque = new ArrayDeque<>(i);
        }

        /* access modifiers changed from: package-private */
        public void add(T t) {
            if (this.deque.size() >= 4) {
                this.deque.removeFirst();
                this.deque.addLast(t);
                return;
            }
            this.deque.addLast(t);
        }

        /* access modifiers changed from: package-private */
        public Deque<T> getAll() {
            return this.deque;
        }
    }

    private static class BezierInterpolatorWrapper implements Interpolator {
        private Interpolator mInnerInterpolator;
        float x1;
        float x2;
        float y1;
        float y2;

        BezierInterpolatorWrapper(float f, float f2, float f3, float f4) {
            this.x1 = f;
            this.y1 = f2;
            this.x2 = f3;
            this.y2 = f4;
            this.mInnerInterpolator = PathInterpolatorCompat.create(f, f2, f3, f4);
        }

        public float getInterpolation(float f) {
            return this.mInnerInterpolator.getInterpolation(f);
        }
    }
}
