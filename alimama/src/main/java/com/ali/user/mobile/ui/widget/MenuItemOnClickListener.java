package com.ali.user.mobile.ui.widget;

import android.view.View;

public abstract class MenuItemOnClickListener implements View.OnClickListener {
    private BottomMenuFragment bottomMenuFragment;
    private MenuItem menuItem;

    public abstract void onClickMenuItem(View view, MenuItem menuItem2);

    public MenuItemOnClickListener(BottomMenuFragment bottomMenuFragment2, MenuItem menuItem2) {
        this.bottomMenuFragment = bottomMenuFragment2;
        this.menuItem = menuItem2;
    }

    public void onClick(View view) {
        if (this.bottomMenuFragment != null && this.bottomMenuFragment.isVisible()) {
            try {
                this.bottomMenuFragment.dismiss();
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        onClickMenuItem(view, this.menuItem);
    }
}
