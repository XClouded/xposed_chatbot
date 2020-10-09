package com.alimama.unionwl.uiframe.views.title;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alimama.unionwl.base.etaodrawee.EtaoDraweeView;
import com.alimama.unionwl.uiframe.R;
import com.alimama.unionwl.uiframe.views.text.IconFontData;
import com.alimama.unionwl.uiframe.views.text.IconFontWithDot;
import com.alimama.unionwl.uiframe.views.title.moreAction.MoreActionItem;
import com.alimama.unionwl.uiframe.views.title.moreAction.MoreActionViewController;
import com.alimama.unionwl.utils.LocalDisplay;
import com.alimama.unionwl.utils.UiUtils;
import in.srain.cube.views.TitleHeaderBar;
import java.util.List;

public class ISTitleHeaderBar extends TitleHeaderBar {
    public static final int HEADER_IMAGE_DEFAULT_HEIGHT = 18;
    public static final int HEADER_IMAGE_DEFAULT_WIDTH = 72;
    public static final int SIZE_TEXT = 18;
    public static final int SIZE_TEXT_ICON_FONT = 25;
    public static final int SIZE_TEXT_ICON_FONT_RETURN = 34;
    public static final int SIZE_TEXT_ICON_FONT_SETTING = 24;
    protected EtaoDraweeView mBackground;
    protected TextView mCloseTextView;
    protected int mCommonTextColor = getResources().getColor(R.color.is_color_999);
    protected TextView mLeftTextView;
    private List<MoreActionItem> mMoreActionItems;
    protected TextView mReturnView;
    protected TextView mRightTextView;
    protected int mTitleTextColor = getResources().getColor(R.color.is_color_999);

    public void enableReturn() {
    }

    public ISTitleHeaderBar(Context context) {
        super(context);
        init();
    }

    public ISTitleHeaderBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public ISTitleHeaderBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        this.mLeftTextView = (TextView) findViewById(R.id.header_view_left_text_view);
        this.mReturnView = (TextView) findViewById(R.id.header_view_return_text_view);
        this.mCloseTextView = (TextView) findViewById(R.id.header_view_close_text_view);
        this.mRightTextView = (TextView) findViewById(R.id.header_view_right_text);
        this.mBackground = (EtaoDraweeView) findViewById(R.id.header_view_bg);
        getLeftViewContainer().setVisibility(8);
        getRightViewContainer().setVisibility(8);
    }

    public void setTitle(String str) {
        this.mCenterTitleTextView.setTextColor(this.mTitleTextColor);
        super.setTitle(str);
    }

    public void setTitle(String str, int i) {
        setTitleTextColor(i);
        super.setTitle(str);
    }

    public void setTitle(String str, int i, int i2) {
        this.mCenterTitleTextView.setVisibility(0);
        if (i2 >= 0) {
            this.mCenterTitleTextView.setTextSize(1, (float) i2);
        } else if (IconFontData.sMap.containsValue(str)) {
            this.mCenterTitleTextView.setTextSize(1, 25.0f);
        } else {
            this.mCenterTitleTextView.setTextSize(1, 18.0f);
        }
        this.mCenterTitleTextView.setTextColor(i);
        this.mCenterTitleTextView.setText(str);
    }

    public IconFontWithDot addRightFunction(String str, int i, View.OnClickListener onClickListener) {
        IconFontWithDot iconFontWithDot = new IconFontWithDot(getContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.setMargins(0, 0, LocalDisplay.dp2px(5.0f), 0);
        RelativeLayout rightViewContainer = getRightViewContainer();
        layoutParams.addRule(15);
        int childCount = rightViewContainer.getChildCount();
        if (childCount >= 3) {
            return null;
        }
        if (childCount > 0) {
            layoutParams.addRule(0, rightViewContainer.getChildAt(rightViewContainer.getChildCount() - 1).getId());
        } else {
            layoutParams.addRule(11);
        }
        iconFontWithDot.setTextSize(1, 26.0f);
        if (i != 0) {
            iconFontWithDot.setTextColor(getResources().getColor(i));
        } else {
            iconFontWithDot.setTextColor(getResources().getColor(R.color.is_color_999));
        }
        iconFontWithDot.setText(str);
        iconFontWithDot.setOnClickListener(onClickListener);
        rightViewContainer.addView(iconFontWithDot, layoutParams);
        return iconFontWithDot;
    }

    public IconFontWithDot addRightFunction(String str, View.OnClickListener onClickListener) {
        return addRightFunction(str, 0, onClickListener);
    }

    public void setHeaderText(String str, int i) {
        setTitle(str, i);
        TextView textView = new TextView(getContext());
        textView.setText(str);
        textView.setTextColor(i);
        textView.setLayoutParams(new RelativeLayout.LayoutParams(-2, -2));
        textView.setSingleLine();
        textView.setTextSize(17.0f);
        setCustomizedCenterView((View) textView);
    }

    public void setHeaderImage(String str, int i, int i2) {
        EtaoDraweeView etaoDraweeView = new EtaoDraweeView(getContext());
        etaoDraweeView.setAnyImageUrl(str);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LocalDisplay.dp2px((i == 0 || i2 == 0) ? 72.0f : (float) ((i * 18) / i2)), LocalDisplay.dp2px(18.0f));
        etaoDraweeView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        etaoDraweeView.setLayoutParams(layoutParams);
        setCustomizedCenterView((View) etaoDraweeView);
    }

    public void setLeftText(String str) {
        setLeftText(str, this.mCommonTextColor, -1);
    }

    public void setLeftText(String str, int i, int i2) {
        getLeftViewContainer().setVisibility(0);
        this.mLeftTextView.setVisibility(0);
        if (i2 >= 0) {
            this.mLeftTextView.setTextSize(1, (float) i2);
        } else if (IconFontData.sMap.containsValue(str)) {
            this.mLeftTextView.setTextSize(1, 25.0f);
        } else {
            this.mLeftTextView.setTextSize(1, 18.0f);
        }
        this.mLeftTextView.setTextColor(i);
        this.mLeftTextView.setText(str);
    }

    public TextView getLeftTextView() {
        return this.mLeftTextView;
    }

    public TextView getReturnView() {
        return this.mReturnView;
    }

    public TextView getCloseTextView() {
        return this.mCloseTextView;
    }

    public void setCloseTextViewClickListener(View.OnClickListener onClickListener) {
        this.mCloseTextView.setOnClickListener(onClickListener);
    }

    public void setRightTextViewClickListener(View.OnClickListener onClickListener) {
        this.mRightTextView.setOnClickListener(onClickListener);
    }

    public void setCloseVisible(boolean z) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getCenterViewContainer().getLayoutParams();
        int dimension = (int) getContext().getResources().getDimension(R.dimen.title_margin_small);
        if (z) {
            dimension = (int) getContext().getResources().getDimension(R.dimen.title_margin_big);
            this.mCloseTextView.setVisibility(0);
        } else {
            this.mCloseTextView.setVisibility(8);
        }
        layoutParams.setMargins(dimension, 0, dimension, 0);
        getCenterViewContainer().setLayoutParams(layoutParams);
    }

    public void setRightText(String str) {
        setRightText(str, this.mCommonTextColor, -1);
    }

    public void setRightText(String str, int i, int i2) {
        getRightViewContainer().setVisibility(0);
        this.mRightTextView.setVisibility(0);
        if (i2 >= 0) {
            this.mRightTextView.setTextSize(1, (float) i2);
        } else if (IconFontData.sMap.containsValue(str)) {
            this.mRightTextView.setTextSize(25.0f);
        } else {
            this.mRightTextView.setTextSize(18.0f);
        }
        this.mRightTextView.setTextColor(i);
        UiUtils.display(this.mRightTextView, str);
    }

    public void setRightText(int i) {
        setRightText(getContext().getString(i));
    }

    public TextView getRightText() {
        return this.mRightTextView;
    }

    public void setHeaderBarBackGroudResource(int i) {
        View findViewById = findViewById(R.id.is_title_bar_view);
        if (findViewById != null) {
            findViewById.setBackgroundResource(i);
        }
    }

    public void setHeaderBackgroundImg(String str) {
        this.mBackground.setAnyImageUrl(str);
        this.mBackground.setVisibility(0);
    }

    public void setHeaderBarBackGroudColor(int i) {
        View findViewById = findViewById(R.id.is_title_bar_view);
        if (findViewById != null) {
            findViewById.setBackgroundColor(i);
        }
    }

    public Drawable getBackground() {
        return findViewById(R.id.is_title_bar_view).getBackground();
    }

    /* access modifiers changed from: protected */
    public int getHeaderViewLayoutId() {
        return R.layout.is_views_header_bar_title;
    }

    public void setCommonTextColor(int i) {
        this.mCommonTextColor = i;
        if (this.mLeftTextView != null) {
            this.mLeftTextView.setTextColor(i);
        }
        if (this.mReturnView != null) {
            this.mReturnView.setTextColor(i);
        }
        if (this.mCloseTextView != null) {
            this.mCloseTextView.setTextColor(i);
        }
        if (this.mRightTextView != null) {
            this.mRightTextView.setTextColor(i);
        }
        setTitleTextColor(i);
        for (int i2 = 0; i2 < getRightViewContainer().getChildCount(); i2++) {
            View childAt = getRightViewContainer().getChildAt(i2);
            if (childAt instanceof TextView) {
                ((TextView) childAt).setTextColor(i);
            }
        }
    }

    public void setTitleTextColor(int i) {
        this.mTitleTextColor = i;
        this.mCenterTitleTextView.setTextColor(this.mTitleTextColor);
    }

    public void enableRightMoreActionView() {
        if (this.mMoreActionItems == null || this.mMoreActionItems.size() == 0) {
            this.mRightTextView.setVisibility(8);
        }
        setRightText(R.string.icon_font_more);
        MoreActionViewController.render(getRightViewContainer(), this.mRightTextView, this.mMoreActionItems);
    }

    public void setMenus(List<MoreActionItem> list) {
        this.mMoreActionItems = list;
    }

    public List<MoreActionItem> getMenus() {
        return this.mMoreActionItems;
    }
}
