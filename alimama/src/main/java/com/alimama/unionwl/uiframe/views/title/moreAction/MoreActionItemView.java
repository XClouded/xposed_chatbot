package com.alimama.unionwl.uiframe.views.title.moreAction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alimama.unionwl.uiframe.R;

@SuppressLint({"ViewConstructor"})
public class MoreActionItemView extends LinearLayout {
    private View mDivision;
    private ImageView mIconImageView;
    private MoreActionItem mMoreActionItem;
    private TextView mTitleTextView;

    public MoreActionItemView(Context context, MoreActionItem moreActionItem) {
        super(context);
        this.mMoreActionItem = moreActionItem;
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.header_bar_more_action_view_item, this);
        this.mIconImageView = (ImageView) findViewById(R.id.header_bar_more_action_view_item_image);
        this.mTitleTextView = (TextView) findViewById(R.id.header_bar_more_action_view_item_title);
        this.mDivision = findViewById(R.id.header_bar_division);
        this.mIconImageView.setImageResource(this.mMoreActionItem.getIcon());
        this.mTitleTextView.setText(this.mMoreActionItem.getTitle());
    }

    public MoreActionItem getMoreActionItem() {
        return this.mMoreActionItem;
    }

    public void setLast() {
        this.mDivision.setVisibility(8);
    }
}
