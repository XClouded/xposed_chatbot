package io.flutter.plugin.platform;

import android.annotation.SuppressLint;
import android.view.View;
import androidx.annotation.NonNull;

public interface PlatformView {

    /* renamed from: io.flutter.plugin.platform.PlatformView$-CC  reason: invalid class name */
    public final /* synthetic */ class CC {
        @SuppressLint({"NewApi"})
        public static void $default$onFlutterViewAttached(@NonNull PlatformView platformView, View view) {
        }

        @SuppressLint({"NewApi"})
        public static void $default$onFlutterViewDetached(PlatformView platformView) {
        }

        @SuppressLint({"NewApi"})
        public static void $default$onInputConnectionLocked(PlatformView platformView) {
        }

        @SuppressLint({"NewApi"})
        public static void $default$onInputConnectionUnlocked(PlatformView platformView) {
        }
    }

    void dispose();

    View getView();

    @SuppressLint({"NewApi"})
    void onFlutterViewAttached(@NonNull View view);

    @SuppressLint({"NewApi"})
    void onFlutterViewDetached();

    @SuppressLint({"NewApi"})
    void onInputConnectionLocked();

    @SuppressLint({"NewApi"})
    void onInputConnectionUnlocked();
}
