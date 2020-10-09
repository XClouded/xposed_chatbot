package com.alimama.moon.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import com.alimama.moon.R;

public class CommonDialog extends DialogFragment {
    public static final String EXTRA_ERROR = "com.alimama.moon.ui.CommonDialog.EXTRA_ERROR";
    private String error;

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            this.error = getArguments().getString(EXTRA_ERROR);
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.dialog_common, viewGroup, false);
        TextView textView = (TextView) inflate.findViewById(R.id.content1);
        if (!TextUtils.isEmpty(this.error)) {
            textView.setText(this.error);
        }
        inflate.findViewById(R.id.btn_i_got_it).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FragmentActivity activity = CommonDialog.this.getActivity();
                if (activity != null && !activity.getSupportFragmentManager().isStateSaved()) {
                    CommonDialog.this.dismiss();
                }
            }
        });
        return inflate;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        Dialog onCreateDialog = super.onCreateDialog(bundle);
        onCreateDialog.setCanceledOnTouchOutside(false);
        onCreateDialog.getWindow().requestFeature(1);
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
