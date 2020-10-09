package com.alimama.union.app.views;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import com.alimama.moon.R;

public class LoadingDialog extends Dialog {
    public LoadingDialog(@NonNull Context context) {
        super(context);
        initViews();
    }

    public LoadingDialog(@NonNull Context context, int i) {
        super(context, i);
        initViews();
    }

    private void initViews() {
        setContentView(R.layout.dialog_loading_layout);
    }
}
