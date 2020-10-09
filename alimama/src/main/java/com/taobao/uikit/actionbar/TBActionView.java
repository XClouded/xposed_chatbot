package com.taobao.uikit.actionbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.appcompat.taobao.TBActionBar;
import androidx.appcompat.taobao.TIconFontTextView;
import androidx.core.content.ContextCompat;
import com.taobao.uikit.actionbar.TBPublicMenuItem;
import com.taobao.uikit.extend.feature.view.TUrlImageView;

public class TBActionView extends FrameLayout {
    private static final String TAG = "TBActionView";
    private Drawable mDotOrNumBackground;
    private Drawable mIcon;
    @ColorInt
    private int mIconColor;
    private int mIconSize;
    private TIconFontTextView mIconView;
    private TUrlImageView mImageView;
    private TBPublicMenuItem mMenuItem;
    @ColorInt
    private int mMessageBackgroundColor;
    private int mMessageDotHeight;
    private int mMessageDotMarginBottom;
    private int mMessageDotMarginLeft;
    private int mMessageDotWidth;
    @ColorInt
    private int mMessageNumColor;
    private int mMessageNumSize;
    private int mMessageOneNumHeigth;
    private int mMessageOneNumMarginBottom;
    private int mMessageOneNumMarginLeft;
    private int mMessageOneNumWdith;
    private TextView mMessageTextView;
    private int mMessageTwoNumHeight;
    private int mMessageTwoNumMarginBottom;
    private int mMessageTwoNumMarginLeft;
    private int mMessageTwoNumWidth;
    private int mMinimumHeight;
    private int mMinimumWidth;
    private TextView mTextView;
    private String mTitle;

    public TBActionView(Context context) {
        this(context, (AttributeSet) null);
    }

    public TBActionView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mMenuItem = null;
        inflate(context, R.layout.uik_public_menu_action_view, this);
        this.mIconView = (TIconFontTextView) findViewById(R.id.uik_public_menu_action_icon);
        this.mImageView = (TUrlImageView) findViewById(R.id.uik_public_menu_action_image);
        this.mTextView = (TextView) findViewById(R.id.uik_public_menu_action_text);
        this.mMessageTextView = (TextView) findViewById(R.id.uik_public_menu_action_message);
        initAttrs(context, attributeSet);
        onMessageUpdate((TBPublicMenuItem) null);
    }

    private void initAttrs(Context context, AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.TBActionView, 0, 0);
            try {
                this.mMinimumHeight = (int) obtainStyledAttributes.getDimension(R.styleable.TBActionView_uikMinHeight, context.getResources().getDimension(R.dimen.uik_action_view_height));
                this.mMinimumWidth = (int) obtainStyledAttributes.getDimension(R.styleable.TBActionView_uikMinWidth, context.getResources().getDimension(R.dimen.uik_action_view_width));
                this.mTitle = obtainStyledAttributes.getString(R.styleable.TBActionView_uikTitle);
                this.mIcon = obtainStyledAttributes.getDrawable(R.styleable.TBActionView_uikIcon);
                this.mIconColor = obtainStyledAttributes.getColor(R.styleable.TBActionView_uikIconColor, ContextCompat.getColor(context, R.color.uik_action_icon_normal));
                this.mIconSize = (int) obtainStyledAttributes.getDimension(R.styleable.TBActionView_uikIconSize, context.getResources().getDimension(R.dimen.uik_action_icon_height));
                this.mMessageNumColor = obtainStyledAttributes.getColor(R.styleable.TBActionView_uikMessageTextColor, ContextCompat.getColor(context, R.color.uik_action_message_num_normal));
                this.mMessageNumSize = (int) obtainStyledAttributes.getDimension(R.styleable.TBActionView_uikMessageTextSize, context.getResources().getDimension(R.dimen.uik_action_message_num_size));
                this.mMessageBackgroundColor = obtainStyledAttributes.getColor(R.styleable.TBActionView_uikMessageBackgroundColor, ContextCompat.getColor(context, R.color.uik_action_message_bg_normal));
                this.mMessageDotHeight = (int) obtainStyledAttributes.getDimension(R.styleable.TBActionView_uikMessageDotHeight, context.getResources().getDimension(R.dimen.uik_action_message_dot_height));
                this.mMessageDotWidth = (int) obtainStyledAttributes.getDimension(R.styleable.TBActionView_uikMessageDotWidth, context.getResources().getDimension(R.dimen.uik_action_message_dot_width));
                this.mMessageDotMarginBottom = (int) obtainStyledAttributes.getDimension(R.styleable.TBActionView_uikMessageDotMarginBottom, context.getResources().getDimension(R.dimen.uik_action_message_dot_margin_bottom));
                this.mMessageDotMarginLeft = (int) obtainStyledAttributes.getDimension(R.styleable.TBActionView_uikMessageDotMarginLeft, context.getResources().getDimension(R.dimen.uik_action_message_dot_margin_left));
                this.mMessageOneNumHeigth = (int) obtainStyledAttributes.getDimension(R.styleable.TBActionView_uikMessageOneNumHeight, context.getResources().getDimension(R.dimen.uik_action_message_one_num_height));
                this.mMessageOneNumWdith = (int) obtainStyledAttributes.getDimension(R.styleable.TBActionView_uikMessageOneNumWidth, context.getResources().getDimension(R.dimen.uik_action_message_one_num_width));
                this.mMessageOneNumMarginBottom = (int) obtainStyledAttributes.getDimension(R.styleable.TBActionView_uikMessageOneNumMarginBottom, context.getResources().getDimension(R.dimen.uik_action_message_one_num_margin_bottom));
                this.mMessageOneNumMarginLeft = (int) obtainStyledAttributes.getDimension(R.styleable.TBActionView_uikMessageOneNumMarginLeft, context.getResources().getDimension(R.dimen.uik_action_message_one_num_margin_left));
                this.mMessageTwoNumHeight = (int) obtainStyledAttributes.getDimension(R.styleable.TBActionView_uikMessageTwoNumHeight, context.getResources().getDimension(R.dimen.uik_action_message_two_num_height));
                this.mMessageTwoNumWidth = (int) obtainStyledAttributes.getDimension(R.styleable.TBActionView_uikMessageTwoNumWidth, context.getResources().getDimension(R.dimen.uik_action_message_two_num_width));
                this.mMessageTwoNumMarginBottom = (int) obtainStyledAttributes.getDimension(R.styleable.TBActionView_uikMessageTwoNumMarginBottom, context.getResources().getDimension(R.dimen.uik_action_message_two_num_margin_bottom));
                this.mMessageTwoNumMarginLeft = (int) obtainStyledAttributes.getDimension(R.styleable.TBActionView_uikMessageTwoNumMarginLeft, context.getResources().getDimension(R.dimen.uik_action_message_two_num_margin_left));
            } finally {
                obtainStyledAttributes.recycle();
            }
        } else {
            this.mMinimumHeight = (int) context.getResources().getDimension(R.dimen.uik_action_view_height);
            this.mMinimumWidth = (int) context.getResources().getDimension(R.dimen.uik_action_view_width);
            this.mTitle = "ꂍ:消息";
            this.mIcon = null;
            this.mIconColor = ContextCompat.getColor(context, R.color.uik_action_icon_normal);
            this.mIconSize = (int) context.getResources().getDimension(R.dimen.uik_action_icon_height);
            this.mMessageNumColor = ContextCompat.getColor(context, R.color.uik_action_message_num_normal);
            this.mMessageNumSize = (int) context.getResources().getDimension(R.dimen.uik_action_message_num_size);
            this.mMessageBackgroundColor = ContextCompat.getColor(context, R.color.uik_action_message_bg_normal);
            this.mMessageDotHeight = (int) context.getResources().getDimension(R.dimen.uik_action_message_dot_height);
            this.mMessageDotWidth = (int) context.getResources().getDimension(R.dimen.uik_action_message_dot_width);
            this.mMessageDotMarginBottom = (int) context.getResources().getDimension(R.dimen.uik_action_message_dot_margin_bottom);
            this.mMessageDotMarginLeft = (int) context.getResources().getDimension(R.dimen.uik_action_message_dot_margin_left);
            this.mMessageOneNumHeigth = (int) context.getResources().getDimension(R.dimen.uik_action_message_one_num_height);
            this.mMessageOneNumWdith = (int) context.getResources().getDimension(R.dimen.uik_action_message_one_num_width);
            this.mMessageOneNumMarginBottom = (int) context.getResources().getDimension(R.dimen.uik_action_message_one_num_margin_bottom);
            this.mMessageOneNumMarginLeft = (int) context.getResources().getDimension(R.dimen.uik_action_message_one_num_margin_left);
            this.mMessageTwoNumHeight = (int) context.getResources().getDimension(R.dimen.uik_action_message_two_num_height);
            this.mMessageTwoNumWidth = (int) context.getResources().getDimension(R.dimen.uik_action_message_two_num_width);
            this.mMessageTwoNumMarginBottom = (int) context.getResources().getDimension(R.dimen.uik_action_message_two_num_margin_bottom);
            this.mMessageTwoNumMarginLeft = (int) context.getResources().getDimension(R.dimen.uik_action_message_two_num_margin_left);
        }
        setMinimumHeight(this.mMinimumHeight);
        setMinimumWidth(this.mMinimumWidth);
        this.mIconView.setTextSize(0, (float) this.mIconSize);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mImageView.getLayoutParams();
        layoutParams.height = this.mIconSize;
        layoutParams.width = this.mIconSize;
        this.mImageView.setLayoutParams(layoutParams);
        this.mIconView.setTextColor(this.mIconColor);
        this.mTextView.setTextColor(this.mIconColor);
        this.mMessageTextView.setTextColor(this.mMessageNumColor);
        this.mMessageTextView.setTextSize(0, (float) this.mMessageNumSize);
        if (this.mIcon != null) {
            this.mImageView.setImageDrawable(this.mIcon);
            this.mImageView.setVisibility(0);
            this.mIconView.setVisibility(8);
            this.mTextView.setVisibility(8);
        } else if (!TextUtils.isEmpty(this.mTitle)) {
            setTitle(this.mTitle);
        }
        this.mDotOrNumBackground = getContext().getResources().getDrawable(R.drawable.uik_action_message_dot_bg);
        updateMessageView(false);
    }

    public void onMessageUpdate(TBPublicMenuItem tBPublicMenuItem) {
        if (tBPublicMenuItem == null || tBPublicMenuItem.checkValidation()) {
            this.mMenuItem = tBPublicMenuItem;
            if (tBPublicMenuItem == null || tBPublicMenuItem.getMessageMode() == TBPublicMenuItem.MessageMode.NONE) {
                this.mMessageTextView.setVisibility(8);
            } else {
                updateMessageView(false);
            }
        } else {
            Log.e(TAG, "Something wrong with you action view!");
        }
    }

    private void updateMessageView(boolean z) {
        if (this.mMenuItem != null) {
            if (!z) {
                if (this.mMessageTextView.getVisibility() == 0 && this.mMessageTextView.getText().equals(this.mMenuItem.getMessage())) {
                    return;
                }
                if (this.mMessageTextView.getVisibility() == 0 && this.mMenuItem.getMessageMode() == TBPublicMenuItem.MessageMode.DOT_WITH_NUMBER && this.mMessageTextView.getText().equals("···") && Integer.valueOf(this.mMenuItem.getMessage()).intValue() > 99) {
                    return;
                }
            }
            this.mMessageTextView.setTextColor(this.mMessageNumColor);
            setContentDescription(getContentDescription() + this.mMenuItem.getMessage());
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mMessageTextView.getLayoutParams();
            switch (this.mMenuItem.getMessageMode()) {
                case DOT_ONLY:
                    if (TextUtils.isEmpty(this.mMenuItem.getMessage())) {
                        this.mMessageTextView.setVisibility(8);
                        break;
                    } else {
                        updateMessageBackground(R.drawable.uik_action_message_dot_bg);
                        layoutParams.height = this.mMessageDotHeight;
                        layoutParams.width = this.mMessageDotWidth;
                        layoutParams.topMargin = 0;
                        layoutParams.leftMargin = this.mMessageDotMarginLeft;
                        layoutParams.bottomMargin = this.mMessageDotMarginBottom;
                        layoutParams.rightMargin = 0;
                        layoutParams.gravity = 17;
                        this.mMessageTextView.setLayoutParams(layoutParams);
                        this.mMessageTextView.setText("");
                        this.mMessageTextView.setVisibility(0);
                        break;
                    }
                case DOT_WITH_NUMBER:
                    int intValue = Integer.valueOf(this.mMenuItem.getMessage()).intValue();
                    if (intValue <= 99) {
                        if (intValue < 10) {
                            if (intValue <= 0) {
                                this.mMessageTextView.setVisibility(8);
                                break;
                            } else {
                                updateMessageBackground(R.drawable.uik_action_message_dot_bg);
                                layoutParams.height = this.mMessageOneNumHeigth;
                                layoutParams.width = this.mMessageOneNumWdith;
                                layoutParams.topMargin = 0;
                                layoutParams.leftMargin = this.mMessageOneNumMarginLeft;
                                layoutParams.bottomMargin = this.mMessageOneNumMarginBottom;
                                layoutParams.rightMargin = 0;
                                layoutParams.gravity = 17;
                                this.mMessageTextView.setText(String.valueOf(this.mMenuItem.getMessage()));
                                this.mMessageTextView.setLayoutParams(layoutParams);
                                this.mMessageTextView.setVisibility(0);
                                break;
                            }
                        } else {
                            updateMessageBackground(R.drawable.uik_action_message_more_bg);
                            layoutParams.height = this.mMessageTwoNumHeight;
                            layoutParams.width = this.mMessageTwoNumWidth;
                            layoutParams.topMargin = 0;
                            layoutParams.leftMargin = this.mMessageTwoNumMarginLeft;
                            layoutParams.bottomMargin = this.mMessageTwoNumMarginBottom;
                            layoutParams.rightMargin = 0;
                            layoutParams.gravity = 17;
                            this.mMessageTextView.setText(String.valueOf(this.mMenuItem.getMessage()));
                            this.mMessageTextView.setLayoutParams(layoutParams);
                            this.mMessageTextView.setVisibility(0);
                            break;
                        }
                    } else {
                        updateMessageBackground(R.drawable.uik_action_message_more_bg);
                        layoutParams.height = this.mMessageTwoNumHeight;
                        layoutParams.width = this.mMessageTwoNumWidth;
                        layoutParams.topMargin = 0;
                        layoutParams.leftMargin = this.mMessageTwoNumMarginLeft;
                        layoutParams.bottomMargin = this.mMessageTwoNumMarginBottom;
                        layoutParams.rightMargin = 0;
                        layoutParams.gravity = 17;
                        if (Build.MANUFACTURER.equals("Xiaomi")) {
                            this.mMessageTextView.setText("•••");
                        } else {
                            this.mMessageTextView.setText("···");
                        }
                        this.mMessageTextView.setLayoutParams(layoutParams);
                        this.mMessageTextView.setVisibility(0);
                        break;
                    }
                case TEXT:
                    if (TextUtils.isEmpty(this.mMenuItem.getMessage())) {
                        this.mMessageTextView.setVisibility(8);
                        break;
                    } else {
                        updateMessageBackground(R.drawable.uik_action_message_more_bg);
                        layoutParams.height = this.mMessageTwoNumHeight;
                        layoutParams.width = -2;
                        layoutParams.topMargin = 0;
                        layoutParams.leftMargin = this.mMessageTwoNumMarginLeft;
                        layoutParams.bottomMargin = this.mMessageTwoNumMarginBottom;
                        layoutParams.rightMargin = 0;
                        layoutParams.gravity = 17;
                        this.mMessageTextView.setLayoutParams(layoutParams);
                        this.mMessageTextView.setText(this.mMenuItem.getMessage());
                        this.mMessageTextView.setVisibility(0);
                        break;
                    }
                case NONE:
                    this.mMessageTextView.setVisibility(8);
                    break;
            }
            this.mMessageTextView.invalidate();
            this.mMessageTextView.requestLayout();
        }
    }

    private void updateMessageBackground(@DrawableRes int i) {
        this.mDotOrNumBackground = getContext().getResources().getDrawable(i);
        this.mDotOrNumBackground.mutate();
        if (this.mDotOrNumBackground != null && (this.mDotOrNumBackground instanceof GradientDrawable)) {
            ((GradientDrawable) this.mDotOrNumBackground).setColor(this.mMessageBackgroundColor);
        }
        if (Build.VERSION.SDK_INT < 16) {
            this.mMessageTextView.setBackgroundDrawable(this.mDotOrNumBackground);
        } else {
            this.mMessageTextView.setBackground(this.mDotOrNumBackground);
        }
    }

    public void setTitle(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if (str.length() < 2 || str.charAt(1) != ':') {
            this.mTextView.setText(str);
            this.mTextView.setTextColor(this.mIconColor);
            this.mTextView.setVisibility(0);
            this.mIconView.setVisibility(8);
            this.mImageView.setVisibility(8);
            return;
        }
        this.mIconView.setText(str.substring(0, 1));
        if (str.length() > 2) {
            setContentDescription(str.subSequence(2, str.length()));
        }
        this.mIconView.setTextColor(this.mIconColor);
        this.mIconView.setVisibility(0);
        this.mImageView.setVisibility(8);
        this.mTextView.setVisibility(8);
    }

    public void setIconColor(@ColorInt int i) {
        this.mIconColor = i;
        if (this.mIconView != null) {
            this.mIconView.setTextColor(i);
            this.mTextView.setTextColor(i);
        }
    }

    public void setMessageNumColor(@ColorInt int i) {
        this.mMessageNumColor = i;
        if (this.mMessageTextView != null) {
            this.mMessageTextView.setTextColor(i);
        }
    }

    public void setMessageBorderColor(@ColorInt int i) {
        updateMessageView(true);
    }

    public void setMessageBackgroundColor(@ColorInt int i) {
        this.mMessageBackgroundColor = i;
        updateMessageView(true);
    }

    /* access modifiers changed from: protected */
    public void setDrawable(Drawable drawable) {
        if (drawable != null) {
            this.mIcon = drawable;
            this.mImageView.setImageDrawable(drawable);
            this.mImageView.setVisibility(0);
            this.mIconView.setVisibility(8);
            this.mTextView.setVisibility(8);
        }
    }

    public void switchActionStyle(TBActionBar.ActionBarStyle actionBarStyle) {
        switch (actionBarStyle) {
            case DARK:
                this.mIconColor = getContext().getResources().getColor(R.color.uik_action_icon_dark);
                this.mMessageBackgroundColor = getContext().getResources().getColor(R.color.uik_action_message_bg_dark);
                this.mMessageNumColor = getContext().getResources().getColor(R.color.uik_action_message_num_dark);
                break;
            case NORMAL:
                this.mIconColor = getContext().getResources().getColor(R.color.uik_action_icon_normal);
                this.mMessageBackgroundColor = getContext().getResources().getColor(R.color.uik_action_message_bg_normal);
                this.mMessageNumColor = getContext().getResources().getColor(R.color.uik_action_message_num_normal);
                break;
        }
        this.mIconView.setTextColor(this.mIconColor);
        this.mTextView.setTextColor(this.mIconColor);
        updateMessageView(true);
    }

    public View getIconView() {
        return this.mIconView;
    }
}
