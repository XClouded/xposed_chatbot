package com.alimama.unionwl.uiframe.views.title.moreAction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import com.alimama.unionwl.uiframe.R;
import com.alimama.unionwl.utils.LocalDisplay;
import java.util.List;

public class MoreActionViewController implements View.OnClickListener {
    /* access modifiers changed from: private */
    public List<MoreActionItem> mActionItems;
    private int mHeight = 0;
    private PopupWindow mMoreActionPopupWindow;
    private int mWidth = 0;

    public MoreActionViewController(View view, List<MoreActionItem> list) {
        this.mActionItems = list;
    }

    private MoreActionViewController(final View view, View view2, List<MoreActionItem> list) {
        this.mActionItems = list;
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MoreActionViewController.this.show(view);
            }
        });
    }

    public static MoreActionViewController render(View view, View view2, List<MoreActionItem> list) {
        if (list != null && list.size() != 0) {
            return new MoreActionViewController(view, view2, list);
        }
        view2.setVisibility(8);
        return null;
    }

    public static void renderDiarenderDialoglog(View view, List<MoreActionItem> list) {
        if (list != null && list.size() != 0) {
            new MoreActionViewController(view, list).showAsDialog(view);
        }
    }

    public void showAsDialog(View view) {
        if (this.mActionItems != null && this.mActionItems.size() != 0) {
            AnonymousClass2 r0 = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    if (i >= 0 && i < MoreActionViewController.this.mActionItems.size()) {
                        ((MoreActionItem) MoreActionViewController.this.mActionItems.get(i)).onClick(MoreActionViewController.this, (MoreActionItemView) null, (MoreActionItem) MoreActionViewController.this.mActionItems.get(i));
                    }
                }
            };
            String[] strArr = new String[this.mActionItems.size()];
            for (int i = 0; i < this.mActionItems.size(); i++) {
                strArr[i] = this.mActionItems.get(i).getTitle();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setItems(strArr, r0);
            builder.show().setCanceledOnTouchOutside(true);
        }
    }

    public void show(View view) {
        if (this.mActionItems != null && this.mActionItems.size() != 0) {
            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(view.getContext()).inflate(R.layout.header_bar_more_action_view, (ViewGroup) null);
            Context context = linearLayout.getContext();
            int i = 0;
            for (MoreActionItem moreActionItemView : this.mActionItems) {
                MoreActionItemView moreActionItemView2 = new MoreActionItemView(context, moreActionItemView);
                if (i == this.mActionItems.size() - 1) {
                    moreActionItemView2.setLast();
                }
                moreActionItemView2.setOnClickListener(this);
                linearLayout.addView(moreActionItemView2);
                i++;
            }
            this.mMoreActionPopupWindow = createMoreActionPopupWindow(view, linearLayout, this.mActionItems.size());
        }
    }

    private void closeMoreActionMenu() {
        if (this.mMoreActionPopupWindow != null) {
            this.mMoreActionPopupWindow.dismiss();
            this.mMoreActionPopupWindow = null;
        }
    }

    private PopupWindow createMoreActionPopupWindow(View view, View view2, int i) {
        view.getLocationOnScreen(new int[2]);
        this.mWidth = LocalDisplay.dp2px(134.0f);
        this.mHeight = LocalDisplay.dp2px((float) ((i * 34) + 15 + 12));
        PopupWindow popupWindow = new PopupWindow(view2, this.mWidth, this.mHeight);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(view, 0, 0);
        return popupWindow;
    }

    public void onClick(View view) {
        closeMoreActionMenu();
        if (view instanceof MoreActionItemView) {
            MoreActionItemView moreActionItemView = (MoreActionItemView) view;
            MoreActionItem moreActionItem = moreActionItemView.getMoreActionItem();
            moreActionItem.onClick(this, moreActionItemView, moreActionItem);
        }
    }
}
