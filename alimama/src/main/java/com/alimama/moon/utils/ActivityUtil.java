package com.alimama.moon.utils;

import android.app.Activity;
import android.view.View;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.alimama.moon.R;

public class ActivityUtil {
    public static void displayFragment(@NonNull FragmentActivity fragmentActivity, @IdRes int i, @NonNull Fragment fragment, @NonNull String str) {
        FragmentManager supportFragmentManager = fragmentActivity.getSupportFragmentManager();
        if (!supportFragmentManager.isStateSaved()) {
            supportFragmentManager.beginTransaction().replace(i, fragment, str).commitAllowingStateLoss();
        }
    }

    public static void setupToolbar(@NonNull final Activity activity, @NonNull Toolbar toolbar, boolean z) {
        if (activity instanceof AppCompatActivity) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
            appCompatActivity.setSupportActionBar(toolbar);
            if (appCompatActivity.getSupportActionBar() != null) {
                appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
            if (z) {
                toolbar.setNavigationIcon((int) R.drawable.ic_ab_back);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        activity.finish();
                    }
                });
            }
        }
    }
}
