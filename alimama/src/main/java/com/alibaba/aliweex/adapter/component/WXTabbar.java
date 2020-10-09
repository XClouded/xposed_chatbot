package com.alibaba.aliweex.adapter.component;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alibaba.aliweex.R;
import com.alibaba.aliweex.adapter.component.TabLayout;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.adapter.IWXImgLoaderAdapter;
import com.taobao.weex.common.WXImageStrategy;
import com.taobao.weex.dom.WXImageQuality;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXEmbed;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.component.list.template.TemplateDom;
import com.taobao.weex.ui.view.border.BorderDrawable;
import com.taobao.weex.utils.WXResourceUtils;
import com.taobao.weex.utils.WXViewUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WXTabbar extends WXVContainer<TabLayout> implements TabLayout.OnTabSelectedListener {
    public static final String EVENT_TABSELECTED = "tabselected";
    public static final String SELECT_INDEX = "selectedIndex";
    public static final String TAB_ITEMS = "tabItems";
    private BorderDrawable mBackgroundDrawable;
    protected List<TabItem> mItems = new ArrayList();

    public void onTabReselected(TabLayout.Tab tab) {
    }

    public WXTabbar(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    /* access modifiers changed from: protected */
    public TabLayout initComponentHostView(Context context) {
        TabLayout tabLayout = new TabLayout(context, this);
        tabLayout.setOnTabSelectedListener(this);
        return tabLayout;
    }

    @WXComponentProp(name = "selectedIndex")
    public void setSelectIndex(int i) {
        TabLayout tabLayout;
        TabLayout.Tab tabAt;
        if (i >= 0 && i < this.mItems.size() && (tabLayout = (TabLayout) getHostView()) != null && (tabAt = tabLayout.getTabAt(i)) != null) {
            tabAt.select();
        }
    }

    /* access modifiers changed from: protected */
    public void onFinishLayout() {
        super.onFinishLayout();
        TabLayout tabLayout = (TabLayout) getHostView();
        if (tabLayout != null) {
            tabLayout.updateSize();
        }
    }

    private int getSelectedIndex() {
        Object obj = getAttrs().get(SELECT_INDEX);
        if (obj == null) {
            return 0;
        }
        return Integer.parseInt((String) obj);
    }

    @WXComponentProp(name = "tabItems")
    public void setTabItems(String str) {
        JSONArray parseArray = JSON.parseArray(str);
        TabLayout tabLayout = (TabLayout) getHostView();
        tabLayout.removeAllTabs();
        this.mItems.clear();
        if (parseArray != null && parseArray.size() != 0) {
            int selectedIndex = getSelectedIndex();
            int size = parseArray.size();
            int i = 0;
            while (i < size) {
                TabItem create = TabItem.create(parseArray.getJSONObject(i), getContext(), getInstance());
                create.setSelectedState(false);
                this.mItems.add(create);
                tabLayout.addTab(tabLayout.newTab().setCustomView(create.getView()), i == selectedIndex);
                i++;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void updateTabState(int i, boolean z) {
        WXEmbed.EmbedManager embedManager;
        WXEmbed embed;
        TabItem tabItem = this.mItems.get(i);
        tabItem.setSelectedState(z);
        if (!z && tabItem.mBadge != null) {
            tabItem.mBadge.setVisibility(4);
        }
        if ((getInstance() instanceof WXEmbed.EmbedManager) && (embedManager = (WXEmbed.EmbedManager) getInstance()) != null && (embed = embedManager.getEmbed(tabItem.getItemId())) != null) {
            embed.setVisibility(z ? "visible" : "hidden");
        }
    }

    public void onTabSelected(TabLayout.Tab tab) {
        updateTabState(tab.getPosition(), true);
        HashMap hashMap = new HashMap(2);
        hashMap.put("index", Integer.valueOf(tab.getPosition()));
        hashMap.put("timeStamp", Long.valueOf(System.currentTimeMillis()));
        HashMap hashMap2 = new HashMap();
        HashMap hashMap3 = new HashMap();
        hashMap3.put(SELECT_INDEX, Integer.valueOf(tab.getPosition()));
        hashMap2.put(TemplateDom.KEY_ATTRS, hashMap3);
        getInstance().fireEvent(getRef(), EVENT_TABSELECTED, hashMap, hashMap2);
    }

    public void onTabUnselected(TabLayout.Tab tab) {
        updateTabState(tab.getPosition(), false);
    }

    public void updateProperties(Map<String, Object> map) {
        super.updateProperties(map);
    }

    protected static class TabItem {
        public static final int DEFAULT_FONTSIZE = 24;
        public static final int DEFAULT_ICON_SIZE = 68;
        public static final String FONT_SIZE = "fontSize";
        public static final String ICON_SIZE = "iconSize";
        TextView mBadge;
        String mIcon;
        ImageView mImage;
        WXSDKInstance mInstance;
        String mItemId;
        String mSelectedIcon;
        TextView mText;
        int mTitleColor;
        int mTitleSelectedColor;
        View mView;

        private TabItem() {
        }

        public View getView() {
            return this.mView;
        }

        public void setSelectedState(boolean z) {
            this.mText.setTextColor(z ? this.mTitleSelectedColor : this.mTitleColor);
            loadIcon(z);
        }

        public String getItemId() {
            return this.mItemId;
        }

        private void loadIcon(boolean z) {
            IWXImgLoaderAdapter imgLoaderAdapter = this.mInstance.getImgLoaderAdapter();
            if (imgLoaderAdapter != null) {
                imgLoaderAdapter.setImage(z ? this.mSelectedIcon : this.mIcon, this.mImage, WXImageQuality.ORIGINAL, new WXImageStrategy());
            }
        }

        public static TabItem create(JSONObject jSONObject, Context context, WXSDKInstance wXSDKInstance) {
            JSONObject jSONObject2 = jSONObject;
            Context context2 = context;
            TabItem tabItem = new TabItem();
            tabItem.mInstance = wXSDKInstance;
            String string = jSONObject2.getString("title");
            int color = WXResourceUtils.getColor(jSONObject2.getString("titleColor"));
            int color2 = WXResourceUtils.getColor(jSONObject2.getString("titleSelectedColor"));
            String string2 = jSONObject2.getString("image");
            String string3 = jSONObject2.getString("selectedImage");
            int intValue = jSONObject2.getIntValue("badge");
            int intValue2 = jSONObject2.containsKey(ICON_SIZE) ? jSONObject2.getIntValue(ICON_SIZE) : 68;
            int i = 24;
            if (jSONObject2.containsKey("fontSize")) {
                i = jSONObject2.getIntValue("fontSize");
            }
            tabItem.mItemId = jSONObject2.getString(WXEmbed.ITEM_ID);
            tabItem.mTitleColor = color;
            tabItem.mTitleSelectedColor = color2;
            tabItem.mIcon = string2;
            tabItem.mSelectedIcon = string3;
            LinearLayout linearLayout = new LinearLayout(context2);
            linearLayout.setOrientation(1);
            linearLayout.setHorizontalGravity(17);
            linearLayout.setGravity(48);
            linearLayout.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
            RelativeLayout relativeLayout = new RelativeLayout(context2);
            relativeLayout.setGravity(17);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
            ImageView imageView = new ImageView(context2);
            imageView.setId(R.id.tabbar_image);
            int round = Math.round(WXViewUtils.getRealPxByWidth((float) intValue2));
            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(round, round);
            layoutParams.height = round;
            layoutParams2.addRule(14);
            imageView.setLayoutParams(layoutParams2);
            relativeLayout.addView(imageView);
            TextView textView = null;
            if (intValue > 0) {
                textView = new TextView(context2);
                RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(-2, -2);
                layoutParams3.addRule(1, R.id.tabbar_image);
                layoutParams3.topMargin = (int) TypedValue.applyDimension(1, -5.0f, context.getResources().getDisplayMetrics());
                textView.setTextColor(-1);
                textView.setText(String.valueOf(intValue));
                textView.setTextSize(1, 10.0f);
                textView.setGravity(17);
                textView.setBackgroundResource(R.drawable.badge);
                relativeLayout.addView(textView, layoutParams3);
            }
            linearLayout.addView(relativeLayout, layoutParams);
            TextView textView2 = new TextView(context2);
            textView2.setText(string);
            textView2.setTextSize(0, WXViewUtils.getRealPxByWidth((float) i));
            LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(-2, -2);
            layoutParams4.gravity = 1;
            textView2.setGravity(1);
            textView2.setLayoutParams(layoutParams4);
            textView2.setTextColor(color);
            linearLayout.addView(textView2);
            tabItem.mText = textView2;
            tabItem.mImage = imageView;
            tabItem.mBadge = textView;
            tabItem.mView = linearLayout;
            return tabItem;
        }
    }
}
