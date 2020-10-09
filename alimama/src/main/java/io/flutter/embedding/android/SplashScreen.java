package io.flutter.embedding.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface SplashScreen {

    /* renamed from: io.flutter.embedding.android.SplashScreen$-CC  reason: invalid class name */
    public final /* synthetic */ class CC {
        @SuppressLint({"NewApi"})
        public static boolean $default$doesSplashViewRememberItsTransition(SplashScreen splashScreen) {
            return false;
        }

        @SuppressLint({"NewApi"})
        @Nullable
        public static Bundle $default$saveSplashScreenState(SplashScreen splashScreen) {
            return null;
        }
    }

    @Nullable
    View createSplashView(@NonNull Context context, @Nullable Bundle bundle);

    @SuppressLint({"NewApi"})
    boolean doesSplashViewRememberItsTransition();

    @SuppressLint({"NewApi"})
    @Nullable
    Bundle saveSplashScreenState();

    void transitionToFlutter(@NonNull Runnable runnable);
}
