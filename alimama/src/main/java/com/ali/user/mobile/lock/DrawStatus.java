package com.ali.user.mobile.lock;

import com.ali.user.mobile.security.biz.R;

public enum DrawStatus {
    DEFAULT(R.string.aliuser_gesture_first_draw, R.color.aliuser_color_gray),
    ERROR_FIVE(R.string.aliuser_gesture_draw_error_five, R.color.aliuser_color_red),
    ERROR_DIFFERENT(R.string.aliuser_gesture_draw_error_different, R.color.aliuser_color_red),
    CORRECT_FISRT(R.string.aliuser_gesture_draw_again, R.color.aliuser_color_gray),
    CORRECT_SECOND(R.string.aliuser_gesture_set_success, R.color.aliuser_color_gray);
    
    private int colorId;
    private int strId;

    public int getStrId() {
        return this.strId;
    }

    public int getColorId() {
        return this.colorId;
    }

    private DrawStatus(int i, int i2) {
        this.strId = i;
        this.colorId = i2;
    }
}
