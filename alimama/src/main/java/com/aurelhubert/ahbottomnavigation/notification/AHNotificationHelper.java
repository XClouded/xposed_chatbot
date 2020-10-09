package com.aurelhubert.ahbottomnavigation.notification;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

public final class AHNotificationHelper {
    private AHNotificationHelper() {
    }

    public static int getTextColor(@NonNull AHNotification aHNotification, @ColorInt int i) {
        int textColor = aHNotification.getTextColor();
        return textColor == 0 ? i : textColor;
    }

    public static int getBackgroundColor(@NonNull AHNotification aHNotification, @ColorInt int i) {
        int backgroundColor = aHNotification.getBackgroundColor();
        return backgroundColor == 0 ? i : backgroundColor;
    }
}
