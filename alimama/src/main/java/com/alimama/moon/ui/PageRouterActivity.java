package com.alimama.moon.ui;

import alimama.com.unwrouter.UNWRouter;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.alimama.moon.App;
import com.alimama.union.app.pagerouter.MoonComponentManager;
import javax.inject.Inject;

public class PageRouterActivity extends AppCompatActivity {
    @Inject
    UNWRouter pageRouter;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        App.getAppComponent().inject(this);
        MoonComponentManager.getInstance().getPageRouter().onCreate(this);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        MoonComponentManager.getInstance().getPageRouter().onResume(this);
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        MoonComponentManager.getInstance().getPageRouter().onStop(this);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        MoonComponentManager.getInstance().getPageRouter().onPause(this);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        MoonComponentManager.getInstance().getPageRouter().onDestroy(this);
    }
}
