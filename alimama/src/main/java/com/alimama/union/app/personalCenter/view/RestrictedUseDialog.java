package com.alimama.union.app.personalCenter.view;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.alimama.moon.R;

public class RestrictedUseDialog extends DialogFragment {
    public static final String EXTRA_MSG_BODY = "com.alimama.union.app.personalCenter.view.RestrictedUseDialog.EXTRA_MSG_BODY";
    private String message;

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            this.message = getArguments().getString(EXTRA_MSG_BODY);
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.dialog_restricted_use, viewGroup, false);
        ((TextView) inflate.findViewById(R.id.content_text_view)).setText(this.message);
        inflate.findViewById(R.id.btn_check_detail).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                RestrictedUseDialog.this.dismiss();
            }
        });
        return inflate;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        Dialog onCreateDialog = super.onCreateDialog(bundle);
        onCreateDialog.setCanceledOnTouchOutside(false);
        onCreateDialog.getWindow().requestFeature(1);
        onCreateDialog.getWindow().getDecorView().setBackgroundResource(17170445);
        return onCreateDialog;
    }

    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setLayout(-2, -2);
            window.setGravity(17);
        }
    }
}
