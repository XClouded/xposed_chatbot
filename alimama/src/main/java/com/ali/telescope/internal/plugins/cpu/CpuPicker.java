package com.ali.telescope.internal.plugins.cpu;

import com.ali.telescope.internal.looper.Loopers;
import com.ali.telescope.util.process.CpuTracker;
import java.util.ArrayList;
import java.util.List;

public class CpuPicker {
    List<CpuRecord> cpuRecords;
    int mCount = 0;
    CpuPickerCallback mCpuPickerCallback = null;
    int mInterval = 0;
    int mMaxCount = 0;
    Runnable mPickerRunnable = new Runnable() {
        public void run() {
            if (CpuPicker.this.mCount < CpuPicker.this.mMaxCount) {
                CpuPicker.this.cpuRecords.add(CpuTracker.generateCpuStat());
                CpuPicker.this.mCount++;
                Loopers.getTelescopeHandler().postDelayed(CpuPicker.this.mPickerRunnable, (long) CpuPicker.this.mInterval);
            }
            if (CpuPicker.this.mCount != 0 && CpuPicker.this.mCount == CpuPicker.this.mMaxCount) {
                CpuPicker.this.mCpuPickerCallback.onResult(CpuPicker.this.cpuRecords);
            }
        }
    };

    interface CpuPickerCallback {
        void onResult(List<CpuRecord> list);
    }

    CpuPicker(int i, int i2, CpuPickerCallback cpuPickerCallback) {
        this.mMaxCount = i;
        this.mInterval = i2;
        this.mCpuPickerCallback = cpuPickerCallback;
        this.cpuRecords = new ArrayList(this.mMaxCount);
    }

    public static void pick(int i, int i2, CpuPickerCallback cpuPickerCallback) {
        Loopers.getTelescopeHandler().post(new CpuPicker(i, i2, cpuPickerCallback).mPickerRunnable);
    }
}
