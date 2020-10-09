package com.taobao.android.dinamicx.template.loader.binary;

import android.util.Log;

public class DXUiCodeLoader {
    private static final String TAG = "UiCodeLoader_TMTEST";
    private int endPos;

    public boolean loadFromBuffer(String str, int i, DXCodeReader dXCodeReader) {
        boolean z;
        if (!dXCodeReader.seekBy(i)) {
            z = false;
            Log.e(TAG, "seekBy error:" + i);
        } else {
            z = true;
        }
        this.endPos = dXCodeReader.getPos();
        return z;
    }

    public int getEndPos() {
        return this.endPos;
    }
}
