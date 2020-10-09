package com.ali.user.mobile.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.ali.user.mobile.ui.R;
import java.util.List;

public class MenuItemAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<MenuItem> menuItems;

    public long getItemId(int i) {
        return (long) i;
    }

    public MenuItemAdapter(Context context2, List<MenuItem> list) {
        this.context = context2;
        this.layoutInflater = LayoutInflater.from(context2);
        this.menuItems = list;
    }

    public int getCount() {
        return this.menuItems.size();
    }

    public Object getItem(int i) {
        if (i >= this.menuItems.size() || i < 0) {
            return null;
        }
        return this.menuItems.get(i);
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = this.layoutInflater.inflate(R.layout.aliuser_menu_item, (ViewGroup) null);
        }
        MenuItem menuItem = this.menuItems.get(i);
        TextView textView = (TextView) view.findViewById(R.id.aliuser_menu_item);
        textView.setText(menuItem.getText());
        MenuItemOnClickListener menuItemOnClickListener = menuItem.getMenuItemOnClickListener();
        if (menuItemOnClickListener != null) {
            textView.setOnClickListener(menuItemOnClickListener);
        }
        return view;
    }
}
