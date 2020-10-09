package com.taobao.uikit.extend.component.unify.Dialog;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.taobao.uikit.extend.R;

public class TBMaterialSimpleListAdapter extends ArrayAdapter<TBMaterialSimpleListItem> implements TBDialogAdapter {
    private TBMaterialDialog dialog;

    public long getItemId(int i) {
        return (long) i;
    }

    public boolean hasStableIds() {
        return true;
    }

    public TBMaterialSimpleListAdapter(Context context) {
        super(context, R.layout.uik_md_simplelist_item, 16908310);
    }

    public void setDialog(TBMaterialDialog tBMaterialDialog) {
        this.dialog = tBMaterialDialog;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        View view2 = super.getView(i, view, viewGroup);
        if (this.dialog != null) {
            TBMaterialSimpleListItem tBMaterialSimpleListItem = (TBMaterialSimpleListItem) getItem(i);
            ImageView imageView = (ImageView) view2.findViewById(16908294);
            if (tBMaterialSimpleListItem.getIcon() != null) {
                imageView.setImageDrawable(tBMaterialSimpleListItem.getIcon());
                imageView.setPadding(tBMaterialSimpleListItem.getIconPadding(), tBMaterialSimpleListItem.getIconPadding(), tBMaterialSimpleListItem.getIconPadding(), tBMaterialSimpleListItem.getIconPadding());
                imageView.getBackground().setColorFilter(tBMaterialSimpleListItem.getBackgroundColor(), PorterDuff.Mode.SRC_ATOP);
            } else {
                imageView.setVisibility(8);
            }
            TextView textView = (TextView) view2.findViewById(16908310);
            textView.setTextColor(this.dialog.getBuilder().getItemColor());
            textView.setText(tBMaterialSimpleListItem.getContent());
        }
        return view2;
    }
}
