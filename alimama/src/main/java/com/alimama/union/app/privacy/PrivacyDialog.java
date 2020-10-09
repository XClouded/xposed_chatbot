package com.alimama.union.app.privacy;

import android.animation.StateListAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.alimama.moon.R;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.moon.web.WebActivity;
import com.alimama.unionwl.utils.LocalDisplay;

public class PrivacyDialog extends Dialog {
    private BottomBarType bottomBarType;
    /* access modifiers changed from: private */
    public PrivacyConfig config;
    private int dialogIndex;

    public enum BottomBarType {
        TwoButton,
        OneButton
    }

    public interface PrivacyCallBack {
        void callback();
    }

    public PrivacyDialog(@NonNull Context context, @NonNull PrivacyConfig privacyConfig, int i, BottomBarType bottomBarType2) {
        super(context);
        this.config = privacyConfig;
        this.dialogIndex = i;
        this.bottomBarType = bottomBarType2;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        View inflate = View.inflate(getContext(), R.layout.dialog_privacy_layout, (ViewGroup) null);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.permission_close_img);
        TextView textView = (TextView) inflate.findViewById(R.id.privacy_dialog_title);
        TextView textView2 = (TextView) inflate.findViewById(R.id.privacy_dialog_content);
        LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.privacy_container);
        if (this.config != null) {
            textView.setText(this.config.getTitle());
            textView2.setText(this.config.getContent());
            if (!TextUtils.isEmpty(this.config.getSpanStr()) && this.config.getContent().contains(this.config.getSpanStr())) {
                SpannableString spannableString = new SpannableString(this.config.getContent());
                int indexOf = this.config.getContent().indexOf(this.config.getSpanStr());
                spannableString.setSpan(new PrivacyTextView(getContext(), this.config.getJumpUrl(), this.dialogIndex), indexOf, this.config.getSpanStr().length() + indexOf, 33);
                textView2.setText(spannableString);
                textView2.setMovementMethod(LinkMovementMethod.getInstance());
            }
            if (this.config.isVertical()) {
                linearLayout.setOrientation(1);
            } else {
                linearLayout.setOrientation(0);
            }
            if (this.bottomBarType == BottomBarType.TwoButton) {
                imageView.setVisibility(8);
                Button button = new Button(getContext());
                if (Build.VERSION.SDK_INT >= 21) {
                    button.setStateListAnimator((StateListAnimator) null);
                }
                button.setBackgroundResource(R.drawable.privacy_dialog_left_btn);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LocalDisplay.dp2px(125.0f), LocalDisplay.dp2px(38.0f));
                layoutParams.gravity = 17;
                button.setTextSize(15.0f);
                button.setGravity(17);
                layoutParams.rightMargin = LocalDisplay.dp2px(5.0f);
                button.setTextColor(Color.parseColor("#FE4800"));
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (PrivacyDialog.this.config.getLeftCallback() != null) {
                            PrivacyDialog.this.config.getLeftCallback().callback();
                        }
                        PrivacyDialog.this.dismiss();
                    }
                });
                button.setText(this.config.getLeftText());
                linearLayout.addView(button, layoutParams);
                Button button2 = new Button(getContext());
                if (Build.VERSION.SDK_INT >= 21) {
                    button2.setStateListAnimator((StateListAnimator) null);
                }
                button2.setBackgroundResource(R.drawable.privacy_dialog_right_btn);
                button2.setTextSize(15.0f);
                button2.setTextColor(-1);
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LocalDisplay.dp2px(125.0f), LocalDisplay.dp2px(38.0f));
                layoutParams2.gravity = 17;
                button2.setGravity(17);
                layoutParams2.leftMargin = LocalDisplay.dp2px(5.0f);
                button2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (PrivacyDialog.this.config.getRightCallback() != null) {
                            PrivacyDialog.this.config.getRightCallback().callback();
                        }
                        PrivacyDialog.this.dismiss();
                    }
                });
                button2.setText(this.config.getRightText());
                linearLayout.addView(button2, layoutParams2);
            } else if (this.bottomBarType == BottomBarType.OneButton) {
                textView2.setGravity(17);
                if (this.config.isNotShowCloseImg()) {
                    imageView.setVisibility(8);
                } else {
                    imageView.setVisibility(0);
                }
                imageView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (PrivacyDialog.this.config.getTopRightCloseBtnCallback() != null) {
                            PrivacyDialog.this.config.getTopRightCloseBtnCallback().callback();
                        }
                        PrivacyDialog.this.dismiss();
                    }
                });
                Button button3 = new Button(getContext());
                if (Build.VERSION.SDK_INT >= 21) {
                    button3.setStateListAnimator((StateListAnimator) null);
                }
                button3.setBackgroundResource(R.drawable.privacy_dialog_right_btn);
                button3.setTextSize(15.0f);
                button3.setText(this.config.getCenterBtnText());
                button3.setTextColor(-1);
                LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(LocalDisplay.dp2px(265.0f), LocalDisplay.dp2px(38.0f));
                layoutParams3.gravity = 17;
                button3.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (PrivacyDialog.this.config.getCenterBtnCallback() != null) {
                            PrivacyDialog.this.config.getCenterBtnCallback().callback();
                        }
                        PrivacyDialog.this.dismiss();
                    }
                });
                linearLayout.addView(button3, layoutParams3);
            }
        }
        setContentView(inflate);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(0));
            getWindow().setLayout(-1, -1);
        }
    }

    public class PrivacyTextView extends ClickableSpan {
        private Context context;
        private int index;
        private String url;

        public PrivacyTextView(Context context2, String str, int i) {
            this.context = context2;
            this.url = str;
            this.index = i;
        }

        public void updateDrawState(TextPaint textPaint) {
            super.updateDrawState(textPaint);
            textPaint.setColor(Color.parseColor("#FE4800"));
        }

        public void onClick(View view) {
            UTHelper.PrivacyAndPermissionDialog.clickPrivacyDialog("privacy_text_dialog_num_" + this.index);
            Intent intent = new Intent(this.context, WebActivity.class);
            intent.setData(Uri.parse(this.url));
            this.context.startActivity(intent);
        }
    }
}
