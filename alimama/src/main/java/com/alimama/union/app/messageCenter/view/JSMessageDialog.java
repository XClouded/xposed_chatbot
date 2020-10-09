package com.alimama.union.app.messageCenter.view;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.LifecycleRegistryOwner;
import androidx.lifecycle.ViewModelProviders;
import com.alibaba.fastjson.JSON;
import com.alimama.moon.R;
import com.alimama.moon.utils.StringUtil;
import com.alimama.moon.web.WebActivity;
import com.alimama.union.app.messageCenter.model.JSMessage;
import com.alimama.union.app.messageCenter.viewmodel.AlertMessageViewModel;

public class JSMessageDialog extends DialogFragment implements LifecycleRegistryOwner {
    public static final String EXTRA_MSG = "com.alimama.union.app.messageCenter.view.JSMessageDialog.JSMessageDialog";
    private TextView actionView;
    private AlertMessageViewModel alertMessageViewModel;
    private TextView contentView;
    private JSMessage jsMessage;
    private LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    private TextView titleView;

    public static JSMessageDialog newInstance(String str) {
        JSMessageDialog jSMessageDialog = new JSMessageDialog();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_MSG, str);
        jSMessageDialog.setArguments(bundle);
        return jSMessageDialog;
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.jsMessage = (JSMessage) JSON.parseObject(arguments.getString(EXTRA_MSG), JSMessage.class);
        }
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.alertMessageViewModel = (AlertMessageViewModel) ViewModelProviders.of((Fragment) this).get(AlertMessageViewModel.class);
    }

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.dialog_jsmessage, viewGroup, false);
        this.titleView = (TextView) inflate.findViewById(R.id.title_text_view);
        this.contentView = (TextView) inflate.findViewById(R.id.content_text_view);
        this.actionView = (TextView) inflate.findViewById(R.id.btn_check_detail);
        this.titleView.setText(Html.fromHtml(this.jsMessage.getTitle()));
        this.contentView.setText(Html.fromHtml(this.jsMessage.getMessage()));
        if (StringUtil.isBlank(this.jsMessage.getButton())) {
            this.actionView.setText("我知道了");
        } else {
            this.actionView.setText(Html.fromHtml(this.jsMessage.getButton()));
        }
        this.actionView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                JSMessageDialog.this.doAction();
            }
        });
        return inflate;
    }

    /* access modifiers changed from: private */
    public void doAction() {
        if (!(this.jsMessage.getMsgType() == null || this.jsMessage.getMsgId() == null)) {
            this.alertMessageViewModel.readMessage(this.jsMessage.getMsgType(), this.jsMessage.getMsgId());
        }
        if (StringUtil.isNotBlank(this.jsMessage.getUrl())) {
            Intent intent = new Intent(getActivity(), WebActivity.class);
            intent.setData(Uri.parse(this.jsMessage.getUrl()));
            getActivity().startActivity(intent);
        }
        dismiss();
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
            window.setLayout(-1, -2);
            window.setGravity(17);
        }
    }

    public LifecycleRegistry getLifecycle() {
        return this.lifecycleRegistry;
    }
}
