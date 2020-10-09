package alimama.com.unwlottiedialog.views;

import alimama.com.unwlottiedialog.R;
import alimama.com.unwviewbase.abstractview.UNWAbstractDialog;
import alimama.com.unwviewbase.tool.DensityUtil;
import android.animation.StateListAnimator;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

public class UNWSysDialog extends UNWAbstractDialog {
    /* access modifiers changed from: private */
    public UNWDialogConfig config;

    public interface PrivacyCallBack {
        void callback();
    }

    public UNWSysDialog(@NonNull Context context, @NonNull UNWDialogConfig uNWDialogConfig) {
        super(context);
        this.config = uNWDialogConfig;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        View inflate = View.inflate(getContext(), R.layout.unwdialog_privacy_layout, (ViewGroup) null);
        TextView textView = (TextView) inflate.findViewById(R.id.privacy_dialog_title);
        TextView textView2 = (TextView) inflate.findViewById(R.id.privacy_dialog_content);
        textView.setGravity(17);
        LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.privacy_container);
        if (this.config != null) {
            textView2.setGravity(this.config.getContentGravity());
            textView.setText(this.config.getTitle());
            textView2.setText(this.config.getContent());
            if (!TextUtils.isEmpty(this.config.getSpanStr()) && this.config.getContent().contains(this.config.getSpanStr())) {
                SpannableString spannableString = new SpannableString(this.config.getContent());
                int indexOf = this.config.getContent().indexOf(this.config.getSpanStr());
                spannableString.setSpan(new PrivacyTextView(getContext(), this.config.getJumpUrl(), this.config.getTitle()), indexOf, this.config.getSpanStr().length() + indexOf, 33);
                textView2.setText(spannableString);
                textView2.setMovementMethod(LinkMovementMethod.getInstance());
            }
            if (this.config.isVertical()) {
                linearLayout.setOrientation(1);
            } else {
                linearLayout.setOrientation(0);
            }
            AppCompatButton appCompatButton = new AppCompatButton(getContext());
            if (Build.VERSION.SDK_INT >= 16) {
                if (!TextUtils.isEmpty(this.config.getRightText())) {
                    appCompatButton.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.unwcommon_btn_bg));
                    appCompatButton.setTextColor(Color.parseColor("#FF0033"));
                } else {
                    appCompatButton.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.unwcommon_btn_highlight_bg));
                    appCompatButton.setTextColor(-1);
                }
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(DensityUtil.dip2px(getContext(), this.config.isVertical() ? 175.0f : 125.0f), DensityUtil.dip2px(getContext(), 38.0f));
            layoutParams.gravity = 17;
            appCompatButton.setTextSize(1, 15.0f);
            appCompatButton.setGravity(17);
            appCompatButton.setPadding(0, 5, 0, 5);
            layoutParams.rightMargin = DensityUtil.dip2px(getContext(), 5.0f);
            appCompatButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    UNWSysDialog.this.dismiss();
                    if (UNWSysDialog.this.config.getLeftCallback() != null) {
                        UNWSysDialog.this.config.getLeftCallback().callback();
                    }
                }
            });
            appCompatButton.setText(this.config.getLeftText());
            if (Build.VERSION.SDK_INT >= 21) {
                appCompatButton.setStateListAnimator((StateListAnimator) null);
            }
            linearLayout.addView(appCompatButton, layoutParams);
            Button button = new Button(getContext());
            button.setBackgroundResource(R.drawable.unwcommon_btn_highlight_bg);
            button.setTextSize(1, 15.0f);
            button.setPadding(0, 5, 0, 5);
            button.setTextColor(-1);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(DensityUtil.dip2px(getContext(), 125.0f), DensityUtil.dip2px(getContext(), 38.0f));
            layoutParams2.gravity = 17;
            button.setGravity(17);
            layoutParams2.leftMargin = DensityUtil.dip2px(getContext(), 5.0f);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    UNWSysDialog.this.dismiss();
                    if (UNWSysDialog.this.config.getRightCallback() != null) {
                        UNWSysDialog.this.config.getRightCallback().callback();
                    }
                }
            });
            if (Build.VERSION.SDK_INT >= 21) {
                button.setStateListAnimator((StateListAnimator) null);
            }
            if (!TextUtils.isEmpty(this.config.getRightText())) {
                button.setText(this.config.getRightText());
                linearLayout.addView(button, layoutParams2);
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

    public void show() {
        super.show();
        if (this.config.getShowCallback() != null) {
            this.config.getShowCallback().callback();
        }
    }

    public class PrivacyTextView extends ClickableSpan {
        private Context context;
        private String title;
        private String url;

        public PrivacyTextView(Context context2, String str, String str2) {
            this.context = context2;
            this.url = str;
            this.title = str2;
        }

        public void updateDrawState(TextPaint textPaint) {
            super.updateDrawState(textPaint);
            textPaint.setColor(Color.parseColor("#FF0033"));
            textPaint.setUnderlineText(false);
        }

        public void onClick(View view) {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(this.url));
            if (intent.resolveActivity(this.context.getPackageManager()) != null) {
                this.context.startActivity(intent);
            }
        }
    }
}
