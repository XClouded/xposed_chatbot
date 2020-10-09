package com.alimama.union.app.share;

import android.os.Bundle;
import com.alimama.moon.utils.SpmProcessor;
import com.alimama.union.app.share.flutter.ImageSaverPlugin;
import com.alimama.union.app.share.flutter.ImmediateSharePlugin;
import com.alimama.union.app.share.flutter.ShareInfoEventChannel;
import com.alimama.union.app.share.flutter.SharePlugin;
import com.alimama.union.app.share.flutter.SharedPreferencePlugin;
import com.alimama.union.app.share.flutter.UTPlugin;
import io.flutter.app.FlutterFragmentActivity;
import io.flutter.view.FlutterView;

public class ShareFlutterActivity extends FlutterFragmentActivity {
    private static final String TAG = "ShareFlutterActivity";

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        FlutterView flutterView = getFlutterView();
        SharedPreferencePlugin.register(flutterView);
        SharePlugin.register(this, flutterView);
        ImmediateSharePlugin.register(this, flutterView);
        ImageSaverPlugin.register(this, flutterView);
        UTPlugin.register(flutterView);
        ShareInfoEventChannel.register(flutterView);
        getFlutterView().setZOrderOnTop(true);
        getFlutterView().getHolder().setFormat(-3);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        SpmProcessor.pageAppear(this, "union/union_share");
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        SpmProcessor.pageDisappear(this, "a21wq.11162860");
    }
}
