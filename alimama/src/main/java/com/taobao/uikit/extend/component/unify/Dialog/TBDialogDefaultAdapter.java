package com.taobao.uikit.extend.component.unify.Dialog;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.LayoutRes;
import androidx.core.content.ContextCompat;
import com.taobao.uikit.extend.R;
import com.taobao.uikit.extend.utils.TintHelper;

public class TBDialogDefaultAdapter extends BaseAdapter {
    private final TBMaterialDialog dialog;
    private final GravityEnum itemGravity;
    @LayoutRes
    private final int layout;

    public long getItemId(int i) {
        return (long) i;
    }

    public boolean hasStableIds() {
        return true;
    }

    public TBDialogDefaultAdapter(TBMaterialDialog tBMaterialDialog, @LayoutRes int i) {
        this.dialog = tBMaterialDialog;
        this.layout = i;
        this.itemGravity = tBMaterialDialog.mBuilder.itemsGravity;
    }

    public int getCount() {
        if (this.dialog.mBuilder.items != null) {
            return this.dialog.mBuilder.items.length;
        }
        return 0;
    }

    public Object getItem(int i) {
        return this.dialog.mBuilder.items[i];
    }

    @SuppressLint({"WrongViewCast"})
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(this.dialog.getContext()).inflate(this.layout, viewGroup, false);
        }
        TextView textView = (TextView) view.findViewById(R.id.uik_mdTitle);
        switch (this.dialog.listType) {
            case SINGLE:
                RadioButton radioButton = (RadioButton) view.findViewById(R.id.uik_mdControl);
                boolean z = this.dialog.mBuilder.selectedIndex == i;
                TintHelper.setTint(radioButton, this.dialog.mBuilder.widgetColor);
                radioButton.setChecked(z);
                break;
            case MULTI:
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.uik_mdControl);
                boolean contains = this.dialog.selectedIndicesList.contains(Integer.valueOf(i));
                TintHelper.setTint(checkBox, this.dialog.mBuilder.widgetColor);
                checkBox.setChecked(contains);
                break;
        }
        textView.setText(this.dialog.mBuilder.items[i].getText());
        TBSimpleListItemType type = this.dialog.mBuilder.items[i].getType();
        if (type == null) {
            textView.setTextColor(this.dialog.mBuilder.itemColor);
        } else if (type == TBSimpleListItemType.NORMAL) {
            textView.setTextColor(ContextCompat.getColor(view.getContext(), R.color.uik_mdListItemNormal));
        } else if (type == TBSimpleListItemType.ALERT) {
            textView.setTextColor(ContextCompat.getColor(view.getContext(), R.color.uik_mdListItemAlert));
        }
        view.setTag(i + ":" + this.dialog.mBuilder.items[i]);
        ViewGroup viewGroup2 = (ViewGroup) view;
        setupGravity(viewGroup2);
        if (this.dialog.mBuilder.itemIds != null) {
            if (i < this.dialog.mBuilder.itemIds.length) {
                view.setId(this.dialog.mBuilder.itemIds[i]);
            } else {
                view.setId(-1);
            }
        }
        if (Build.VERSION.SDK_INT >= 21 && viewGroup2.getChildCount() == 2) {
            if (viewGroup2.getChildAt(0) instanceof CompoundButton) {
                viewGroup2.getChildAt(0).setBackground((Drawable) null);
            } else if (viewGroup2.getChildAt(1) instanceof CompoundButton) {
                viewGroup2.getChildAt(1).setBackground((Drawable) null);
            }
        }
        return view;
    }

    @TargetApi(17)
    private void setupGravity(ViewGroup viewGroup) {
        ((LinearLayout) viewGroup).setGravity(this.itemGravity.getGravityInt() | 16);
        if (viewGroup.getChildCount() != 2) {
            return;
        }
        if (this.itemGravity == GravityEnum.END && !isRTL() && (viewGroup.getChildAt(0) instanceof CompoundButton)) {
            CompoundButton compoundButton = (CompoundButton) viewGroup.getChildAt(0);
            viewGroup.removeView(compoundButton);
            TextView textView = (TextView) viewGroup.getChildAt(0);
            viewGroup.removeView(textView);
            textView.setPadding(textView.getPaddingRight(), textView.getPaddingTop(), textView.getPaddingLeft(), textView.getPaddingBottom());
            viewGroup.addView(textView);
            viewGroup.addView(compoundButton);
        } else if (this.itemGravity == GravityEnum.START && isRTL() && (viewGroup.getChildAt(1) instanceof CompoundButton)) {
            CompoundButton compoundButton2 = (CompoundButton) viewGroup.getChildAt(1);
            viewGroup.removeView(compoundButton2);
            TextView textView2 = (TextView) viewGroup.getChildAt(0);
            viewGroup.removeView(textView2);
            textView2.setPadding(textView2.getPaddingRight(), textView2.getPaddingTop(), textView2.getPaddingRight(), textView2.getPaddingBottom());
            viewGroup.addView(compoundButton2);
            viewGroup.addView(textView2);
        }
    }

    @TargetApi(17)
    private boolean isRTL() {
        if (Build.VERSION.SDK_INT >= 17 && this.dialog.getBuilder().getContext().getResources().getConfiguration().getLayoutDirection() == 1) {
            return true;
        }
        return false;
    }
}
