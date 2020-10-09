package com.ali.user.mobile.ui.widget;

public class MenuItem {
    private MenuItemOnClickListener menuItemOnClickListener;
    private String text;

    public MenuItem() {
    }

    public MenuItem(String str, MenuItemOnClickListener menuItemOnClickListener2) {
        this.text = str;
        this.menuItemOnClickListener = menuItemOnClickListener2;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String str) {
        this.text = str;
    }

    public MenuItemOnClickListener getMenuItemOnClickListener() {
        return this.menuItemOnClickListener;
    }

    public void setMenuItemOnClickListener(MenuItemOnClickListener menuItemOnClickListener2) {
        this.menuItemOnClickListener = menuItemOnClickListener2;
    }
}
