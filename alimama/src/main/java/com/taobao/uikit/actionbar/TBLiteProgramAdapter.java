package com.taobao.uikit.actionbar;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.taobao.uikit.extend.feature.view.TUrlImageView;

public class TBLiteProgramAdapter extends RecyclerView.Adapter<ViewHolder> implements View.OnClickListener {
    private TBPublicMenu mMenu;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int i);
    }

    public TBLiteProgramAdapter(@NonNull TBPublicMenu tBPublicMenu) {
        this.mMenu = tBPublicMenu;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void onClick(View view) {
        if (this.mOnItemClickListener != null) {
            this.mOnItemClickListener.onItemClick(view, ((Integer) view.getTag()).intValue());
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.uik_lite_program_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(inflate);
        inflate.setOnClickListener(this);
        return viewHolder;
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        TBPublicMenuItem tBPublicMenuItem;
        if (this.mMenu.mExtensionMenu != null) {
            tBPublicMenuItem = this.mMenu.mExtensionMenu.get(i);
        } else {
            TBPublicMenu tBPublicMenu = this.mMenu;
            if (TBPublicMenu.sLitePrograms != null) {
                TBPublicMenu tBPublicMenu2 = this.mMenu;
                tBPublicMenuItem = TBPublicMenu.sLitePrograms.get(i);
            } else {
                tBPublicMenuItem = null;
            }
        }
        if (tBPublicMenuItem != null) {
            if (!TextUtils.isEmpty(tBPublicMenuItem.getIconUrl())) {
                viewHolder.mIconView.setImageDrawable((Drawable) null);
                viewHolder.mIconView.setImageUrl(tBPublicMenuItem.getIconUrl());
            } else if (tBPublicMenuItem.getIconDrawable() != null) {
                viewHolder.mIconView.setImageDrawable(tBPublicMenuItem.getIconDrawable());
            } else {
                viewHolder.mIconView.setImageDrawable((Drawable) null);
            }
            viewHolder.mIconView.invalidate();
            viewHolder.mTitleView.setText(tBPublicMenuItem.getTitle());
            viewHolder.itemView.setTag(Integer.valueOf(i));
        }
    }

    public int getItemCount() {
        if (this.mMenu == null) {
            return 0;
        }
        if (this.mMenu.mExtensionMenu != null) {
            return this.mMenu.mExtensionMenu.size();
        }
        TBPublicMenu tBPublicMenu = this.mMenu;
        if (TBPublicMenu.sLitePrograms == null) {
            return 0;
        }
        TBPublicMenu tBPublicMenu2 = this.mMenu;
        return TBPublicMenu.sLitePrograms.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TUrlImageView mIconView;
        public TextView mTitleView;

        public ViewHolder(View view) {
            super(view);
            this.mIconView = (TUrlImageView) view.findViewById(R.id.uik_lite_program_icon);
            this.mTitleView = (TextView) view.findViewById(R.id.uik_lite_program_title);
        }
    }

    public static class LiteItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public LiteItemDecoration(int i) {
            this.space = i;
        }

        public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
            if (recyclerView.getLayoutManager() != null && recyclerView.getChildAdapterPosition(view) != state.getItemCount() - 1) {
                rect.right = this.space;
            }
        }
    }
}
