package com.rd;

import android.os.Build;
import android.view.View;
import java.util.concurrent.atomic.AtomicInteger;

public class Utils {
    private static final AtomicInteger nextGeneratedId = new AtomicInteger(1);

    public static int generateViewId() {
        if (Build.VERSION.SDK_INT < 17) {
            return generateId();
        }
        return View.generateViewId();
    }

    private static int generateId() {
        int i;
        int i2;
        do {
            i = nextGeneratedId.get();
            i2 = i + 1;
            if (i2 > 16777215) {
                i2 = 1;
            }
        } while (!nextGeneratedId.compareAndSet(i, i2));
        return i;
    }
}
