package com.ali.alihadeviceevaluator.opengl;

import android.app.ActivityManager;
import android.content.Context;

public class AliHAOpenGL {
    public float mOpenGLVersion = 0.0f;
    public int mScore;

    @Deprecated
    public int getScore(float f) {
        double d = (double) f;
        if (d > 4.0d) {
            return 10;
        }
        if (d >= 4.0d) {
            return 9;
        }
        if (d >= 3.2d) {
            return 8;
        }
        if (d >= 3.1d) {
            return 7;
        }
        if (d >= 3.0d) {
            return 6;
        }
        return d >= 2.0d ? 3 : 8;
    }

    public void generateOpenGLVersion(Context context) {
        if (this.mOpenGLVersion == 0.0f && context != null) {
            float f = 2.0f;
            try {
                String glEsVersion = ((ActivityManager) context.getSystemService("activity")).getDeviceConfigurationInfo().getGlEsVersion();
                if (glEsVersion != null) {
                    f = Float.parseFloat(glEsVersion);
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
            this.mOpenGLVersion = f;
            this.mScore = getScore(this.mOpenGLVersion);
        }
    }
}
