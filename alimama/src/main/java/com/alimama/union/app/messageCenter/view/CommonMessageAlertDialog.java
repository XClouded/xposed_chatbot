package com.alimama.union.app.messageCenter.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
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
import com.alimama.moon.web.WebActivity;
import com.alimama.moon.web.WebPageIntentGenerator;
import com.alimama.union.app.messageCenter.model.AlertMessage;
import com.alimama.union.app.messageCenter.model.AlertMessageTypeEnum;
import com.alimama.union.app.messageCenter.viewmodel.AlertMessageViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonMessageAlertDialog extends DialogFragment implements LifecycleRegistryOwner {
    public static final String EXTRA_MSG = "com.alimama.union.app.messageCenter.view.CommonMessageAlertDialog.EXTRA_MSG";
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) CommonMessageAlertDialog.class);
    /* access modifiers changed from: private */
    public AlertMessage alertMessage;
    /* access modifiers changed from: private */
    public AlertMessageViewModel alertMessageViewModel;
    private ImageView closeView;
    private TextView contentView;
    private LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    private ImageView smileView;
    private TextView titleView;

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            this.alertMessage = (AlertMessage) JSON.parseObject(getArguments().getString(EXTRA_MSG), AlertMessage.class);
        }
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.alertMessageViewModel = (AlertMessageViewModel) ViewModelProviders.of((Fragment) this).get(AlertMessageViewModel.class);
    }

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.dialog_grade_change, viewGroup, false);
        this.smileView = (ImageView) inflate.findViewById(R.id.smile);
        updateSmileView(this.smileView, this.alertMessage);
        this.titleView = (TextView) inflate.findViewById(R.id.title_text_view);
        this.contentView = (TextView) inflate.findViewById(R.id.content_text_view);
        this.titleView.setText(this.alertMessage.getTitle());
        this.contentView.setText(Html.fromHtml(this.alertMessage.getContent()));
        ((TextView) inflate.findViewById(R.id.btn_check_detail)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CommonMessageAlertDialog.this.alertMessageViewModel.readMessage(CommonMessageAlertDialog.this.alertMessage.getMsgType(), CommonMessageAlertDialog.this.alertMessage.getId());
                CommonMessageAlertDialog.this.showThresholdPage();
                CommonMessageAlertDialog.this.dismiss();
            }
        });
        this.closeView = (ImageView) inflate.findViewById(R.id.btn_close);
        this.closeView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CommonMessageAlertDialog.this.alertMessageViewModel.readMessage(CommonMessageAlertDialog.this.alertMessage.getMsgType(), CommonMessageAlertDialog.this.alertMessage.getId());
                CommonMessageAlertDialog.this.dismiss();
            }
        });
        return inflate;
    }

    private void updateSmileView(ImageView imageView, AlertMessage alertMessage2) {
        AlertMessageTypeEnum valueOf = AlertMessageTypeEnum.valueOf(alertMessage2.getMsgType());
        if (valueOf == null) {
            logger.warn("alertMessage.msgType invalid: {}", (Object) alertMessage2.getMsgType());
            return;
        }
        switch (valueOf) {
            case ENTER_REVIEW_PERIOD:
                imageView.setImageResource(R.drawable.ic_smile_face);
                return;
            case QUIT_REVIEW_PERIOD:
                imageView.setImageResource(R.drawable.ic_cry_face);
                return;
            case PRE_DOWNGRADE:
                imageView.setImageResource(R.drawable.ic_cry_face);
                return;
            case DOWNGRADE:
                imageView.setImageResource(R.drawable.ic_cry_face);
                return;
            case UPGRADE:
                imageView.setImageResource(R.drawable.ic_smile_face);
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public void showThresholdPage() {
        Intent intent = new Intent(getActivity(), WebActivity.class);
        intent.setData(WebPageIntentGenerator.getThresholdUri().buildUpon().appendQueryParameter("title", "规则与权限").build());
        getActivity().startActivity(intent);
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
        super.onResume();
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setLayout(-2, -2);
            window.setGravity(17);
        }
    }

    public LifecycleRegistry getLifecycle() {
        return this.lifecycleRegistry;
    }
}
