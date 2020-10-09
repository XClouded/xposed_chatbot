package com.alimamaunion.support.debugmode;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;

public class InputDialog extends Dialog implements View.OnClickListener {
    private TextView mConfirm;
    private TextView mDismiss;
    private EditText mInputText;
    private InputTrigger mInputTrigger;

    public interface InputTrigger {
        void trigger(String str);
    }

    private void initView() {
    }

    public void setInputTrigger(InputTrigger inputTrigger) {
    }

    public InputDialog(@NonNull Context context) {
        super(context);
        initView();
    }

    public InputDialog(@NonNull Context context, int i) {
        super(context, i);
        initView();
    }

    public void onClick(View view) {
        dismiss();
    }
}
