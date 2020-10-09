package com.taobao.monitor.impl.data.newvisible;

import android.view.View;
import com.taobao.monitor.impl.util.TimeUtils;
import java.util.ArrayList;
import java.util.List;

public class PagePercentCalculate {
    private List<Area> areas = new ArrayList();
    private Area current = null;
    private final boolean needCal;
    private final float percent;

    public PagePercentCalculate(float f) {
        this.percent = f;
        if (Math.abs(1.0f - f) > 1.0E-4f) {
            this.needCal = true;
        } else {
            this.needCal = false;
        }
    }

    /* access modifiers changed from: package-private */
    public void beforeCalculate() {
        if (this.needCal) {
            this.current = new Area();
        }
    }

    /* access modifiers changed from: package-private */
    public void calculate(View view) {
        if (this.needCal) {
            Area area = this.current;
            float unused = area.area = area.area + ((float) (view.getWidth() * view.getHeight()));
        }
    }

    /* access modifiers changed from: package-private */
    public void afterCalculate() {
        if (this.needCal) {
            long unused = this.current.time = TimeUtils.currentTimeMillis();
            this.areas.add(this.current);
        }
    }

    /* access modifiers changed from: package-private */
    public long getPercentTime(long j) {
        if (!this.needCal) {
            return j;
        }
        Area area = this.current;
        int size = this.areas.size() - 2;
        while (size >= 0) {
            Area area2 = this.areas.get(size);
            if (area2.area / this.current.area <= this.percent) {
                break;
            }
            size++;
            area = area2;
        }
        return area.time;
    }

    private static class Area {
        /* access modifiers changed from: private */
        public float area;
        /* access modifiers changed from: private */
        public long time;

        private Area() {
        }
    }
}
