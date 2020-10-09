package com.alimama.moon.utils;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import com.alimama.moon.R;

public class ChangeSkinUtil {
    public static void updateGradientBackground(View view, Context context) {
        view.setBackgroundDrawable(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{context.getResources().getColor(R.color.home_bg_gradient_left), context.getResources().getColor(R.color.home_bg_gradient_right)}));
    }
}
