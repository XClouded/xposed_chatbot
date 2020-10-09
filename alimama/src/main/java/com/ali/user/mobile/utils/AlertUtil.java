package com.ali.user.mobile.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ali.user.mobile.R;
import com.ali.user.mobile.helper.IDialogHelper;
import com.ali.user.mobile.model.AlertModel;

public class AlertUtil {
    public static void alertConfirm(Context context, AlertModel alertModel, IDialogHelper iDialogHelper) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.aliuser_alert_confirm_dialog, (ViewGroup) null);
        TextView textView = (TextView) inflate.findViewById(R.id.dialog_title);
        TextView textView2 = (TextView) inflate.findViewById(R.id.dialog_content);
        if (!TextUtils.isEmpty(alertModel.title)) {
            textView.setText(alertModel.title);
        }
        if (alertModel.content != null) {
            textView2.setText(alertModel.content);
        }
        iDialogHelper.alert((Activity) context, "", inflate, context.getString(R.string.aliuser_i_know), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }, (String) null, (DialogInterface.OnClickListener) null, false, (DialogInterface.OnCancelListener) null);
    }
}
