package com.taobao.weex.appbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.collection.ArrayMap;
import androidx.core.graphics.drawable.DrawableCompat;
import com.alibaba.aliweex.utils.WXUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.uikit.actionbar.TBPublicMenu;
import com.taobao.weex.WXActivity;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.adapter.R;
import com.taobao.weex.dom.CSSShorthand;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.view.WXFrameLayout;
import com.taobao.weex.utils.WXResourceUtils;
import com.taobao.weex.utils.WXTBConstant;
import com.taobao.weex.utils.WXTBUtil;

public class AppbarComponent extends WXVContainer<Toolbar> {
    private static final String ICON = "icon";
    private static final String INDEX = "index";
    private static final String MORE_ITEMS = "moreItems";
    private static final String TEXT = "text";
    public static final String TYPE = "appbar";
    private FrameLayout content;
    private final int defaultNavWidth = WXEnvironment.getApplication().getResources().getDimensionPixelSize(R.dimen.appbar_nav_button_width);
    private final int defaultOverflowWidth = WXEnvironment.getApplication().getResources().getDimensionPixelSize(R.dimen.appbar_overflow_button_width);
    private JSONArray items;

    public AppbarComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    /* access modifiers changed from: protected */
    public Toolbar initComponentHostView(@NonNull Context context) {
        Toolbar toolbar = new Toolbar(context);
        toolbar.setMinimumHeight(0);
        toolbar.setContentInsetStartWithNavigation(this.defaultNavWidth);
        if (context instanceof AppCompatActivity) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) context;
            appCompatActivity.setSupportActionBar(toolbar);
            ActionBar supportActionBar = appCompatActivity.getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setDisplayHomeAsUpEnabled(true);
                supportActionBar.setDisplayShowTitleEnabled(false);
            }
        }
        this.content = new WXFrameLayout(context);
        toolbar.addView(this.content, new ViewGroup.LayoutParams(-1, -1));
        return toolbar;
    }

    public ViewGroup getRealView() {
        return this.content;
    }

    @SuppressLint({"RestrictedApi"})
    public void setPadding(CSSShorthand cSSShorthand, CSSShorthand cSSShorthand2) {
        CSSShorthand cSSShorthand3 = new CSSShorthand();
        for (CSSShorthand.EDGE edge : new CSSShorthand.EDGE[]{CSSShorthand.EDGE.ALL, CSSShorthand.EDGE.TOP, CSSShorthand.EDGE.BOTTOM}) {
            cSSShorthand3.set(edge, cSSShorthand.get(edge));
        }
        cSSShorthand3.set(CSSShorthand.EDGE.RIGHT, cSSShorthand.get(CSSShorthand.EDGE.RIGHT) - ((float) this.defaultOverflowWidth));
        cSSShorthand3.set(CSSShorthand.EDGE.LEFT, cSSShorthand.get(CSSShorthand.EDGE.LEFT) - ((float) this.defaultNavWidth));
        super.setPadding(cSSShorthand3, cSSShorthand2);
    }

    /* access modifiers changed from: protected */
    public boolean setProperty(String str, Object obj) {
        Toolbar toolbar;
        if (WXTBUtil.hasFestival() || !TextUtils.equals(str, "color")) {
            return super.setProperty(str, obj);
        }
        if (!(getContext() instanceof AppCompatActivity) || (toolbar = (Toolbar) getHostView()) == null) {
            return true;
        }
        int color = WXResourceUtils.getColor(obj.toString());
        if (getContext() instanceof WXActivity) {
            TBPublicMenu publicMenu = ((WXActivity) getContext()).getPublicMenu();
            if (publicMenu != null) {
                publicMenu.setActionViewIconColor(color);
            }
        } else {
            Drawable overflowIcon = toolbar.getOverflowIcon();
            if (overflowIcon != null) {
                Drawable wrap = DrawableCompat.wrap(overflowIcon);
                wrap.mutate();
                DrawableCompat.setTint(wrap, color);
            }
        }
        Drawable navigationIcon = toolbar.getNavigationIcon();
        if (navigationIcon == null) {
            return true;
        }
        Drawable wrap2 = DrawableCompat.wrap(navigationIcon);
        wrap2.mutate();
        DrawableCompat.setTint(wrap2, color);
        return true;
    }

    @WXComponentProp(name = "moreItems")
    public void moreItems(String str) {
        JSONArray parseArray = JSON.parseArray(str);
        if (parseArray != null && (getContext() instanceof Activity)) {
            this.items = parseArray;
            ((Activity) getContext()).invalidateOptionsMenu();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.removeGroup(R.id.appbar_more_group);
        if (this.items == null || !(getContext() instanceof Activity)) {
            return super.onCreateOptionsMenu(menu);
        }
        Activity activity = (Activity) getContext();
        for (final int i = 0; i < this.items.size(); i++) {
            JSONObject jSONObject = this.items.getJSONObject(i);
            String string = jSONObject.getString("text");
            String string2 = jSONObject.getString(ICON);
            if (!TextUtils.isEmpty(string) && !TextUtils.isEmpty(string2)) {
                int iconFontId = WXUtil.getIconFontId(getContext(), string2);
                MenuItem add = menu.add(R.id.appbar_more_group, 0, 0, string);
                add.setTitle(getContext().getString(iconFontId) + ":" + string);
                add.setShowAsAction(0);
                add.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        ArrayMap arrayMap = new ArrayMap();
                        arrayMap.put("index", Integer.valueOf(i));
                        WXSDKInstance instance = AppbarComponent.this.getInstance();
                        instance.fireEvent(instance.getRootComponent().getRef(), WXTBConstant.CLICK_MORE_ITEM, arrayMap);
                        return false;
                    }
                });
            }
        }
        return true;
    }
}
