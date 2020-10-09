package com.alibaba.aliweex.bubble;

import android.view.View;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

public class SpringUtils {
    public static SpringAnimation createSpring(View view, DynamicAnimation.ViewProperty viewProperty, float f, float f2, float f3) {
        SpringAnimation springAnimation = new SpringAnimation(view, viewProperty);
        SpringForce springForce = new SpringForce(f);
        springForce.setStiffness(f2);
        springForce.setDampingRatio(f3);
        springAnimation.setSpring(springForce);
        return springAnimation;
    }
}
