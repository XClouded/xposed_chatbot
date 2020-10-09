package com.taobao.uikit.actionbar;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.taobao.uikit.extend.feature.view.TIconFontTextView;
import com.taobao.uikit.extend.feature.view.TUrlImageView;

public class TBPublicMenuAdapter extends RecyclerView.Adapter<ViewHolder> implements View.OnClickListener {
    private TBPublicMenu mMenu;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(TBPublicMenuItem tBPublicMenuItem);
    }

    public TBPublicMenuAdapter(@NonNull TBPublicMenu tBPublicMenu) {
        this.mMenu = tBPublicMenu;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void onClick(View view) {
        if (this.mOnItemClickListener != null) {
            this.mOnItemClickListener.onItemClick((TBPublicMenuItem) view.getTag());
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.uik_public_menu_item_new, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(inflate);
        inflate.setOnClickListener(this);
        return viewHolder;
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        TBPublicMenuItem tBPublicMenuItem;
        if (i < TBPublicMenu.sPublicMenus.size()) {
            tBPublicMenuItem = TBPublicMenu.sPublicMenus.get(i);
        } else {
            tBPublicMenuItem = i < TBPublicMenu.sPublicMenus.size() + this.mMenu.mExtraMenus.size() ? this.mMenu.mExtraMenus.get(i - TBPublicMenu.sPublicMenus.size()) : null;
        }
        viewHolder.itemView.setTag(tBPublicMenuItem);
        if (tBPublicMenuItem != null) {
            if (!TextUtils.isEmpty(tBPublicMenuItem.mIconUrl)) {
                viewHolder.mIconView.setVisibility(8);
                viewHolder.mIconImageView.setVisibility(0);
                viewHolder.mIconImageView.setImageDrawable((Drawable) null);
                viewHolder.mIconImageView.setImageUrl(tBPublicMenuItem.mIconUrl);
                viewHolder.mIconView.setText("");
            } else if (tBPublicMenuItem.mIconDrawable != null) {
                viewHolder.mIconView.setVisibility(8);
                viewHolder.mIconImageView.setVisibility(0);
                viewHolder.mIconImageView.setImageDrawable(tBPublicMenuItem.mIconDrawable);
                viewHolder.mIconView.setText("");
            } else if (!TextUtils.isEmpty(tBPublicMenuItem.mTitle)) {
                viewHolder.mIconView.setVisibility(0);
                viewHolder.mIconImageView.setVisibility(8);
                if (tBPublicMenuItem.getTitle().length() < 2 || tBPublicMenuItem.getTitle().charAt(1) != ':') {
                    viewHolder.mIconView.setText("");
                } else {
                    viewHolder.mIconView.setText(tBPublicMenuItem.getTitle().substring(0, tBPublicMenuItem.getTitle().indexOf(":")));
                }
                viewHolder.mIconImageView.setImageDrawable((Drawable) null);
                viewHolder.mIconView.setVisibility(0);
                viewHolder.mIconImageView.setVisibility(8);
            } else {
                viewHolder.mIconImageView.setImageDrawable((Drawable) null);
                viewHolder.mIconView.setText("");
            }
            viewHolder.mIconView.invalidate();
            viewHolder.mIconImageView.invalidate();
            if (TextUtils.isEmpty(tBPublicMenuItem.getTitle())) {
                viewHolder.mTitleView.setText("");
            } else if (tBPublicMenuItem.getTitle().length() < 2 || tBPublicMenuItem.getTitle().charAt(1) != ':') {
                viewHolder.mTitleView.setText(tBPublicMenuItem.getTitle());
            } else {
                viewHolder.mTitleView.setText(tBPublicMenuItem.getTitle().substring(tBPublicMenuItem.getTitle().indexOf(":") + 1, tBPublicMenuItem.getTitle().length()));
            }
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) viewHolder.mMessageView.getLayoutParams();
            if (tBPublicMenuItem.getMessageMode() == null) {
                viewHolder.mMessageView.setText("");
                viewHolder.mMessageView.setVisibility(8);
                return;
            }
            switch (tBPublicMenuItem.getMessageMode()) {
                case DOT_ONLY:
                    if (!TextUtils.isEmpty(tBPublicMenuItem.mMessage) && !"0".equals(tBPublicMenuItem.mMessage)) {
                        viewHolder.mMessageView.setVisibility(0);
                        viewHolder.mMessageView.setBackgroundResource(R.drawable.uik_action_message_dot_bg);
                        layoutParams.height = (int) viewHolder.itemView.getResources().getDimension(R.dimen.uik_public_menu_message_dot_height);
                        layoutParams.width = (int) viewHolder.itemView.getResources().getDimension(R.dimen.uik_public_menu_message_dot_width);
                        layoutParams.leftMargin = (int) viewHolder.itemView.getResources().getDimension(R.dimen.uik_public_menu_message_margin_left);
                        viewHolder.mMessageView.setLayoutParams(layoutParams);
                        viewHolder.mMessageView.setText("");
                        return;
                    }
                    return;
                case DOT_WITH_NUMBER:
                    try {
                        int intValue = Integer.valueOf(tBPublicMenuItem.mMessage).intValue();
                        if (intValue > 99) {
                            viewHolder.mMessageView.setVisibility(0);
                            if (Build.MANUFACTURER.equals("Xiaomi")) {
                                viewHolder.mMessageView.setText("•••");
                            } else {
                                viewHolder.mMessageView.setText("···");
                            }
                            viewHolder.mMessageView.setBackgroundResource(R.drawable.uik_action_message_more_bg);
                            layoutParams.height = (int) viewHolder.itemView.getResources().getDimension(R.dimen.uik_public_menu_message_one_height);
                            layoutParams.width = (int) viewHolder.itemView.getResources().getDimension(R.dimen.uik_public_menu_message_two_width);
                            layoutParams.leftMargin = (int) viewHolder.itemView.getResources().getDimension(R.dimen.uik_public_menu_message_margin_left);
                            viewHolder.mMessageView.setLayoutParams(layoutParams);
                            return;
                        } else if (intValue >= 10) {
                            viewHolder.mMessageView.setVisibility(0);
                            viewHolder.mMessageView.setText(String.valueOf(intValue));
                            viewHolder.mMessageView.setBackgroundResource(R.drawable.uik_action_message_more_bg);
                            layoutParams.height = (int) viewHolder.itemView.getResources().getDimension(R.dimen.uik_public_menu_message_one_height);
                            layoutParams.width = (int) viewHolder.itemView.getResources().getDimension(R.dimen.uik_public_menu_message_two_width);
                            layoutParams.leftMargin = (int) viewHolder.itemView.getResources().getDimension(R.dimen.uik_public_menu_message_margin_left);
                            viewHolder.mMessageView.setLayoutParams(layoutParams);
                            return;
                        } else if (intValue > 0) {
                            viewHolder.mMessageView.setVisibility(0);
                            viewHolder.mMessageView.setText(String.valueOf(intValue));
                            viewHolder.mMessageView.setBackgroundResource(R.drawable.uik_action_message_dot_bg);
                            layoutParams.height = (int) viewHolder.itemView.getResources().getDimension(R.dimen.uik_public_menu_message_one_height);
                            layoutParams.width = (int) viewHolder.itemView.getResources().getDimension(R.dimen.uik_public_menu_message_one_width);
                            layoutParams.leftMargin = (int) viewHolder.itemView.getResources().getDimension(R.dimen.uik_public_menu_message_margin_left);
                            viewHolder.mMessageView.setLayoutParams(layoutParams);
                            return;
                        } else {
                            viewHolder.mMessageView.setVisibility(8);
                            return;
                        }
                    } catch (NumberFormatException unused) {
                        viewHolder.mMessageView.setText("");
                        viewHolder.mMessageView.setVisibility(8);
                        return;
                    }
                case TEXT:
                    if (!TextUtils.isEmpty(tBPublicMenuItem.mMessage)) {
                        viewHolder.mMessageView.setVisibility(0);
                        viewHolder.mMessageView.setText(tBPublicMenuItem.mMessage);
                        viewHolder.mMessageView.setBackgroundResource(R.drawable.uik_action_message_more_bg);
                        layoutParams.height = (int) viewHolder.itemView.getResources().getDimension(R.dimen.uik_public_menu_message_one_height);
                        layoutParams.width = (int) viewHolder.itemView.getResources().getDimension(R.dimen.uik_public_menu_message_text_width);
                        layoutParams.leftMargin = (int) viewHolder.itemView.getResources().getDimension(R.dimen.uik_public_menu_message_text_margin_left);
                        viewHolder.mMessageView.setLayoutParams(layoutParams);
                        viewHolder.mMessageView.setVisibility(0);
                        return;
                    }
                    viewHolder.mMessageView.setText("");
                    viewHolder.mMessageView.setVisibility(8);
                    return;
                case NONE:
                    viewHolder.mMessageView.setText("");
                    viewHolder.mMessageView.setVisibility(8);
                    return;
                default:
                    return;
            }
        }
    }

    public int getItemCount() {
        return TBPublicMenu.sPublicMenus.size() + this.mMenu.mExtraMenus.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TUrlImageView mIconImageView;
        TIconFontTextView mIconView;
        TextView mMessageView;
        TextView mTitleView;

        public ViewHolder(View view) {
            super(view);
            this.mIconView = (TIconFontTextView) view.findViewById(R.id.uik_public_menu_item_icon);
            this.mIconImageView = (TUrlImageView) view.findViewById(R.id.uik_public_menu_item_image);
            this.mTitleView = (TextView) view.findViewById(R.id.uik_public_menu_item_title);
            this.mMessageView = (TextView) view.findViewById(R.id.uik_public_menu_item_message);
        }
    }

    public static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private boolean includeEdge;
        private int spacing;
        private int spanCount;

        public GridSpacingItemDecoration(int i, int i2, boolean z) {
            this.spanCount = i;
            this.spacing = i2;
            this.includeEdge = z;
        }

        public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
            int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
            int i = childAdapterPosition % this.spanCount;
            if (this.includeEdge) {
                rect.left = this.spacing - ((this.spacing * i) / this.spanCount);
                rect.right = ((i + 1) * this.spacing) / this.spanCount;
                if (childAdapterPosition < this.spanCount) {
                    rect.top = this.spacing;
                }
                rect.bottom = this.spacing;
                return;
            }
            rect.left = (this.spacing * i) / this.spanCount;
            rect.right = this.spacing - (((i + 1) * this.spacing) / this.spanCount);
            if (childAdapterPosition >= this.spanCount) {
                rect.top = this.spacing;
            }
        }
    }
}
