package com.alibaba.aliweex.hc.bundle;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.WXError;
import com.alibaba.aliweex.adapter.INavigationBarModuleAdapter;
import com.alibaba.aliweex.bundle.WXNavBarAdapter;
import com.alibaba.aliweex.bundle.WeexPageFragment;
import com.alibaba.aliweex.utils.WXUtil;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.utils.WXLogUtils;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public abstract class WXHCNavBarAdapter extends WXNavBarAdapter {
    public static String CLICK_CENTER_ITEM = "clickcenteritem";
    public static String CLICK_LEFT_ITEM = "clickleftitem";
    public static String CLICK_MORE_ITEM = "clickmoreitem";
    public static String CLICK_RIGHT_ITEM = "clickrightitem";
    public static final String CONFIG_GROUP_WEEX_HC = "group_weex_hc";
    public static final String CONFIG_KEY_WEEX_MAIN_HC_DOMAIN = "weex_main_hc_domain";
    private static final String TAG = "WXNavBarAdapter";
    private List<WXActionBarMenuItem> menuItemList = null;
    private WXActionBarMenuItem menuItemRight = null;
    private WXActionBarMenuItem menuItemTitle = null;

    private enum NavigatorType {
        TITLE,
        MORE_ITEM,
        CLEAR_MORE_ITEM,
        RIGHT_ITEM,
        CLEAR_RIGHT_ITEM
    }

    public boolean clearNavBarLeftItem(String str) {
        return false;
    }

    public abstract void push(Activity activity, String str, JSONObject jSONObject);

    public boolean setNavBarLeftItem(String str) {
        return false;
    }

    public WXHCNavBarAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public WXActionBarMenuItem getMenuItemRight() {
        return this.menuItemRight;
    }

    public WXActionBarMenuItem getMenuItemTitle() {
        return this.menuItemTitle;
    }

    public List<WXActionBarMenuItem> getMenuItemMore() {
        return this.menuItemList;
    }

    public void destroy() {
        super.destroy();
        if (!(this.menuItemTitle == null || this.menuItemTitle.iconBitmap == null)) {
            this.menuItemTitle.iconBitmap.recycle();
            this.menuItemTitle = null;
        }
        if (!(this.menuItemRight == null || this.menuItemRight.iconBitmap == null)) {
            this.menuItemRight.iconBitmap.recycle();
            this.menuItemRight = null;
        }
        if (this.menuItemList != null && this.menuItemList.size() > 0) {
            for (WXActionBarMenuItem next : this.menuItemList) {
                if (next.iconBitmap != null) {
                    next.iconBitmap.recycle();
                }
            }
            this.menuItemList = null;
        }
    }

    public boolean setNavBarRightItem(String str) {
        return setNavBarRightItem(str, (INavigationBarModuleAdapter.OnItemClickListener) null);
    }

    public WXError setMoreItem(WXSDKInstance wXSDKInstance, com.alibaba.fastjson.JSONObject jSONObject, INavigationBarModuleAdapter.OnItemClickListener onItemClickListener) {
        if (setNavBarMoreItem(jSONObject.toJSONString(), onItemClickListener)) {
            return null;
        }
        WXError wXError = new WXError();
        wXError.result = "WX_ERROR";
        return wXError;
    }

    public WXError setTitle(WXSDKInstance wXSDKInstance, com.alibaba.fastjson.JSONObject jSONObject) {
        if (!shouldSetNavigator(NavigatorType.TITLE)) {
            WXError wXError = new WXError();
            wXError.result = "WX_FAILED";
            wXError.message = "can not set Navigator";
            return wXError;
        }
        String string = jSONObject.getString("title");
        String string2 = jSONObject.getString("icon");
        this.menuItemTitle = new WXActionBarMenuItem();
        if (!TextUtils.isEmpty(string2)) {
            if (!checkScheme(string2)) {
                WXError wXError2 = new WXError();
                wXError2.result = "WX_FAILED";
                wXError2.message = "schemeerror";
                return wXError2;
            }
            this.menuItemTitle.href = string2;
            getFragmentActivity().supportInvalidateOptionsMenu();
            return null;
        } else if (!TextUtils.isEmpty(string)) {
            this.menuItemTitle.title = string;
            getFragmentActivity().supportInvalidateOptionsMenu();
            return null;
        } else {
            WXError wXError3 = new WXError();
            wXError3.result = "WX_FAILED";
            wXError3.message = "paramerror";
            return wXError3;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00e0, code lost:
        if (r5.menuItemRight.setIconFontId(getFragmentActivity(), r6) >= 0) goto L_0x00e2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean setNavBarRightItem(java.lang.String r6, com.alibaba.aliweex.adapter.INavigationBarModuleAdapter.OnItemClickListener r7) {
        /*
            r5 = this;
            com.alibaba.aliweex.hc.bundle.WXHCNavBarAdapter$NavigatorType r0 = com.alibaba.aliweex.hc.bundle.WXHCNavBarAdapter.NavigatorType.RIGHT_ITEM
            boolean r0 = r5.shouldSetNavigator(r0)
            r1 = 0
            if (r0 != 0) goto L_0x000a
            return r1
        L_0x000a:
            boolean r0 = android.text.TextUtils.isEmpty(r6)
            if (r0 != 0) goto L_0x0123
            com.alibaba.aliweex.hc.bundle.WXActionBarMenuItem r0 = new com.alibaba.aliweex.hc.bundle.WXActionBarMenuItem     // Catch:{ Exception -> 0x0108 }
            r0.<init>()     // Catch:{ Exception -> 0x0108 }
            r5.menuItemRight = r0     // Catch:{ Exception -> 0x0108 }
            com.alibaba.aliweex.hc.bundle.WXActionBarMenuItem r0 = r5.menuItemRight     // Catch:{ Exception -> 0x0108 }
            r0.itemClickListener = r7     // Catch:{ Exception -> 0x0108 }
            r0 = 1
            if (r7 == 0) goto L_0x009a
            org.json.JSONObject r7 = new org.json.JSONObject     // Catch:{ Exception -> 0x0108 }
            r7.<init>(r6)     // Catch:{ Exception -> 0x0108 }
            java.lang.String r6 = "items"
            org.json.JSONArray r6 = r7.optJSONArray(r6)     // Catch:{ Exception -> 0x0108 }
            r2 = 0
            if (r6 == 0) goto L_0x0043
            int r3 = r6.length()     // Catch:{ Exception -> 0x0108 }
            if (r3 <= 0) goto L_0x0043
            org.json.JSONObject r6 = r6.getJSONObject(r1)     // Catch:{ Exception -> 0x0108 }
            java.lang.String r3 = "title"
            java.lang.String r3 = r6.optString(r3)     // Catch:{ Exception -> 0x0108 }
            java.lang.String r4 = "icon"
            java.lang.String r6 = r6.optString(r4)     // Catch:{ Exception -> 0x0108 }
            goto L_0x0045
        L_0x0043:
            r6 = r2
            r3 = r6
        L_0x0045:
            boolean r4 = android.text.TextUtils.isEmpty(r6)     // Catch:{ Exception -> 0x0108 }
            if (r4 == 0) goto L_0x005d
            boolean r4 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Exception -> 0x0108 }
            if (r4 == 0) goto L_0x005d
            java.lang.String r6 = "icon"
            java.lang.String r6 = r7.optString(r6)     // Catch:{ Exception -> 0x0108 }
            java.lang.String r3 = "title"
            java.lang.String r3 = r7.optString(r3)     // Catch:{ Exception -> 0x0108 }
        L_0x005d:
            boolean r7 = android.text.TextUtils.isEmpty(r6)     // Catch:{ Exception -> 0x0108 }
            if (r7 != 0) goto L_0x007d
            boolean r7 = android.text.TextUtils.isEmpty(r6)     // Catch:{ Exception -> 0x0108 }
            if (r7 == 0) goto L_0x006a
            return r1
        L_0x006a:
            boolean r7 = r5.checkScheme(r6)     // Catch:{ Exception -> 0x0108 }
            if (r7 != 0) goto L_0x0071
            return r1
        L_0x0071:
            com.alibaba.aliweex.hc.bundle.WXActionBarMenuItem r7 = r5.menuItemRight     // Catch:{ Exception -> 0x0108 }
            r7.href = r6     // Catch:{ Exception -> 0x0108 }
            androidx.fragment.app.FragmentActivity r6 = r5.getFragmentActivity()     // Catch:{ Exception -> 0x0108 }
            r6.supportInvalidateOptionsMenu()     // Catch:{ Exception -> 0x0108 }
            return r0
        L_0x007d:
            boolean r6 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Exception -> 0x0108 }
            if (r6 != 0) goto L_0x0090
            com.alibaba.aliweex.hc.bundle.WXActionBarMenuItem r6 = r5.menuItemRight     // Catch:{ Exception -> 0x0108 }
            r6.setTitle(r3)     // Catch:{ Exception -> 0x0108 }
            androidx.fragment.app.FragmentActivity r6 = r5.getFragmentActivity()     // Catch:{ Exception -> 0x0108 }
            r6.supportInvalidateOptionsMenu()     // Catch:{ Exception -> 0x0108 }
            return r0
        L_0x0090:
            r5.menuItemRight = r2     // Catch:{ Exception -> 0x0108 }
            androidx.fragment.app.FragmentActivity r6 = r5.getFragmentActivity()     // Catch:{ Exception -> 0x0108 }
            r6.supportInvalidateOptionsMenu()     // Catch:{ Exception -> 0x0108 }
            return r1
        L_0x009a:
            org.json.JSONObject r7 = new org.json.JSONObject     // Catch:{ Exception -> 0x0108 }
            r7.<init>(r6)     // Catch:{ Exception -> 0x0108 }
            java.lang.String r6 = "icon"
            java.lang.String r6 = r7.optString(r6)     // Catch:{ Exception -> 0x0108 }
            java.lang.String r2 = "iconFont"
            boolean r2 = r7.optBoolean(r2, r1)     // Catch:{ Exception -> 0x0108 }
            java.lang.String r3 = "title"
            java.lang.String r4 = ""
            java.lang.String r3 = r7.optString(r3, r4)     // Catch:{ Exception -> 0x0108 }
            java.lang.String r4 = "fromNative"
            boolean r7 = r7.optBoolean(r4, r1)     // Catch:{ Exception -> 0x0108 }
            boolean r4 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Exception -> 0x0108 }
            if (r4 != 0) goto L_0x00cc
            com.alibaba.aliweex.hc.bundle.WXActionBarMenuItem r6 = r5.menuItemRight     // Catch:{ Exception -> 0x0108 }
            r6.setTitle(r3)     // Catch:{ Exception -> 0x0108 }
            androidx.fragment.app.FragmentActivity r6 = r5.getFragmentActivity()     // Catch:{ Exception -> 0x0108 }
            r6.supportInvalidateOptionsMenu()     // Catch:{ Exception -> 0x0108 }
            return r0
        L_0x00cc:
            if (r7 == 0) goto L_0x00ef
            if (r2 == 0) goto L_0x00e6
            androidx.fragment.app.FragmentActivity r7 = r5.getFragmentActivity()     // Catch:{ Exception -> 0x0108 }
            if (r7 == 0) goto L_0x00e6
            com.alibaba.aliweex.hc.bundle.WXActionBarMenuItem r7 = r5.menuItemRight     // Catch:{ Exception -> 0x0108 }
            androidx.fragment.app.FragmentActivity r2 = r5.getFragmentActivity()     // Catch:{ Exception -> 0x0108 }
            int r6 = r7.setIconFontId(r2, r6)     // Catch:{ Exception -> 0x0108 }
            if (r6 < 0) goto L_0x00e4
        L_0x00e2:
            r6 = 1
            goto L_0x00fe
        L_0x00e4:
            r6 = 0
            goto L_0x00fe
        L_0x00e6:
            com.alibaba.aliweex.hc.bundle.WXActionBarMenuItem r7 = r5.menuItemRight     // Catch:{ Exception -> 0x0108 }
            int r6 = r7.setIconResId(r6)     // Catch:{ Exception -> 0x0108 }
            if (r6 < 0) goto L_0x00e4
            goto L_0x00e2
        L_0x00ef:
            com.alibaba.aliweex.hc.bundle.WXActionBarMenuItem r7 = r5.menuItemRight     // Catch:{ Exception -> 0x0108 }
            androidx.fragment.app.FragmentActivity r2 = r5.getFragmentActivity()     // Catch:{ Exception -> 0x0108 }
            int r2 = com.alibaba.aliweex.utils.WXUtil.getActionBarHeight(r2)     // Catch:{ Exception -> 0x0108 }
            float r2 = (float) r2     // Catch:{ Exception -> 0x0108 }
            boolean r6 = r7.setIconBitmap(r6, r2)     // Catch:{ Exception -> 0x0108 }
        L_0x00fe:
            if (r6 == 0) goto L_0x0107
            androidx.fragment.app.FragmentActivity r6 = r5.getFragmentActivity()     // Catch:{ Exception -> 0x0108 }
            r6.supportInvalidateOptionsMenu()     // Catch:{ Exception -> 0x0108 }
        L_0x0107:
            return r0
        L_0x0108:
            r6 = move-exception
            java.lang.String r7 = "WebAppInterface"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r2 = "setNavBarRightItem: param parse to JSON error, param="
            r0.append(r2)
            java.lang.String r6 = r6.getMessage()
            r0.append(r6)
            java.lang.String r6 = r0.toString()
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r7, (java.lang.String) r6)
        L_0x0123:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.aliweex.hc.bundle.WXHCNavBarAdapter.setNavBarRightItem(java.lang.String, com.alibaba.aliweex.adapter.INavigationBarModuleAdapter$OnItemClickListener):boolean");
    }

    private boolean setNavBarMoreItem(String str, INavigationBarModuleAdapter.OnItemClickListener onItemClickListener) {
        String str2;
        String str3;
        if (shouldSetNavigator(NavigatorType.MORE_ITEM) && !TextUtils.isEmpty(str)) {
            try {
                if (this.menuItemList == null) {
                    this.menuItemList = new ArrayList();
                } else {
                    this.menuItemList.clear();
                }
                if (onItemClickListener != null) {
                    JSONObject jSONObject = new JSONObject(str);
                    if (jSONObject.length() == 0) {
                        clearNavBarMoreItem("");
                        return true;
                    }
                    JSONArray optJSONArray = jSONObject.optJSONArray("items");
                    if (optJSONArray == null || optJSONArray.length() <= 0) {
                        WXActionBarMenuItem wXActionBarMenuItem = new WXActionBarMenuItem();
                        wXActionBarMenuItem.itemClickListener = onItemClickListener;
                        String str4 = null;
                        if (!TextUtils.isEmpty((CharSequence) null) || !TextUtils.isEmpty((CharSequence) null)) {
                            str3 = null;
                            str2 = null;
                        } else {
                            str4 = jSONObject.optString("icon");
                            str2 = jSONObject.optString("title");
                            str3 = jSONObject.optString("iconFontName");
                        }
                        if (!TextUtils.isEmpty(str4)) {
                            wXActionBarMenuItem.href = str4;
                        }
                        if (!TextUtils.isEmpty(str3)) {
                            wXActionBarMenuItem.setIconFontId(getFragmentActivity(), str3);
                        }
                        if (!TextUtils.isEmpty(str2)) {
                            wXActionBarMenuItem.setTitle(str2);
                        }
                        wXActionBarMenuItem.data = new Intent();
                        wXActionBarMenuItem.data.putExtra("index", 0);
                        this.menuItemList.add(wXActionBarMenuItem);
                        getFragmentActivity().supportInvalidateOptionsMenu();
                        return true;
                    }
                    for (int i = 0; i < optJSONArray.length(); i++) {
                        JSONObject jSONObject2 = optJSONArray.getJSONObject(i);
                        String string = jSONObject2.getString("title");
                        String string2 = jSONObject2.getString("icon");
                        WXActionBarMenuItem wXActionBarMenuItem2 = new WXActionBarMenuItem();
                        wXActionBarMenuItem2.itemClickListener = onItemClickListener;
                        wXActionBarMenuItem2.data = new Intent();
                        wXActionBarMenuItem2.data.putExtra("index", i);
                        if (!TextUtils.isEmpty(string2)) {
                            wXActionBarMenuItem2.href = string2;
                        }
                        if (!TextUtils.isEmpty(string)) {
                            wXActionBarMenuItem2.setTitle(string);
                        }
                        if (!checkScheme(wXActionBarMenuItem2.href)) {
                            return false;
                        }
                        this.menuItemList.add(wXActionBarMenuItem2);
                    }
                    getFragmentActivity().supportInvalidateOptionsMenu();
                    return true;
                }
                JSONObject jSONObject3 = new JSONObject(str);
                if (jSONObject3.length() == 0) {
                    clearNavBarMoreItem("");
                    return true;
                }
                JSONArray jSONArray = jSONObject3.getJSONArray("items");
                if (jSONArray != null && jSONArray.length() > 0) {
                    for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                        JSONObject jSONObject4 = jSONArray.getJSONObject(i2);
                        WXActionBarMenuItem wXActionBarMenuItem3 = new WXActionBarMenuItem();
                        String string3 = jSONObject4.getString("text");
                        if (!TextUtils.isEmpty(string3)) {
                            wXActionBarMenuItem3.title = string3;
                            boolean optBoolean = jSONObject4.optBoolean("fromNative", false);
                            boolean optBoolean2 = jSONObject4.optBoolean("iconFont", false);
                            String optString = jSONObject4.optString("icon");
                            if (!optBoolean) {
                                wXActionBarMenuItem3.setIconBitmap(optString, (float) WXUtil.getActionBarHeight(getFragmentActivity()));
                            } else if (optBoolean2) {
                                wXActionBarMenuItem3.setIconFontId(getFragmentActivity(), optString);
                            } else {
                                wXActionBarMenuItem3.setIconResId(optString);
                            }
                            wXActionBarMenuItem3.data = new Intent();
                            wXActionBarMenuItem3.data.putExtra("index", i2);
                            this.menuItemList.add(wXActionBarMenuItem3);
                        }
                    }
                }
                getFragmentActivity().supportInvalidateOptionsMenu();
                return true;
            } catch (Exception e) {
                WXLogUtils.e("WebAppInterface", "setNavBarRightItem: param parse to JSON error, param=" + e.getMessage());
            }
        }
        return false;
    }

    public boolean clearNavBarRightItem(String str) {
        if (!shouldSetNavigator(NavigatorType.CLEAR_RIGHT_ITEM)) {
            return false;
        }
        this.menuItemRight = null;
        getFragmentActivity().supportInvalidateOptionsMenu();
        return true;
    }

    public boolean setNavBarMoreItem(String str) {
        if (shouldSetNavigator(NavigatorType.MORE_ITEM) && !TextUtils.isEmpty(str)) {
            try {
                JSONArray jSONArray = new JSONObject(str).getJSONArray("items");
                if (jSONArray != null && jSONArray.length() > 0) {
                    clearNavBarMoreItem("");
                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject jSONObject = jSONArray.getJSONObject(i);
                        WXActionBarMenuItem wXActionBarMenuItem = new WXActionBarMenuItem();
                        String string = jSONObject.getString("text");
                        if (!TextUtils.isEmpty(string)) {
                            wXActionBarMenuItem.title = string;
                            boolean optBoolean = jSONObject.optBoolean("fromNative", false);
                            boolean optBoolean2 = jSONObject.optBoolean("iconFont", false);
                            String optString = jSONObject.optString("icon");
                            if (!optBoolean) {
                                wXActionBarMenuItem.setIconBitmap(optString, (float) WXUtil.getActionBarHeight(getFragmentActivity()));
                            } else if (!optBoolean2 || getFragmentActivity() == null) {
                                wXActionBarMenuItem.setIconResId(optString);
                            } else {
                                wXActionBarMenuItem.setIconFontId(getFragmentActivity(), optString);
                            }
                            wXActionBarMenuItem.data = new Intent();
                            wXActionBarMenuItem.data.putExtra("index", i);
                            if (this.menuItemList == null) {
                                this.menuItemList = new ArrayList();
                            }
                            this.menuItemList.add(wXActionBarMenuItem);
                        }
                    }
                }
                getFragmentActivity().supportInvalidateOptionsMenu();
                return true;
            } catch (Exception unused) {
                WXLogUtils.d("WXActivity", "setNavBarMoreItem: param decode error param=" + str);
            }
        }
        return false;
    }

    public boolean clearNavBarMoreItem(String str) {
        if (!shouldSetNavigator(NavigatorType.CLEAR_MORE_ITEM)) {
            return false;
        }
        if (this.menuItemList == null) {
            this.menuItemList = new ArrayList();
        } else {
            this.menuItemList.clear();
        }
        getFragmentActivity().supportInvalidateOptionsMenu();
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:39:0x00b9 A[Catch:{ Exception -> 0x00c4 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setNavBarTitle(java.lang.String r7) {
        /*
            r6 = this;
            com.alibaba.aliweex.hc.bundle.WXHCNavBarAdapter$NavigatorType r0 = com.alibaba.aliweex.hc.bundle.WXHCNavBarAdapter.NavigatorType.TITLE
            boolean r0 = r6.shouldSetNavigator(r0)
            r1 = 0
            if (r0 != 0) goto L_0x000a
            return r1
        L_0x000a:
            boolean r0 = android.text.TextUtils.isEmpty(r7)
            if (r0 != 0) goto L_0x00cf
            java.lang.String r0 = "utf-8"
            java.lang.String r7 = java.net.URLDecoder.decode(r7, r0)     // Catch:{ Exception -> 0x00c4 }
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ Exception -> 0x00c4 }
            r0.<init>(r7)     // Catch:{ Exception -> 0x00c4 }
            com.alibaba.aliweex.hc.bundle.WXActionBarMenuItem r7 = new com.alibaba.aliweex.hc.bundle.WXActionBarMenuItem     // Catch:{ Exception -> 0x00c4 }
            r7.<init>()     // Catch:{ Exception -> 0x00c4 }
            java.lang.String r2 = "title"
            java.lang.String r2 = r0.optString(r2)     // Catch:{ Exception -> 0x00c4 }
            boolean r3 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Exception -> 0x00c4 }
            r4 = 1
            if (r3 != 0) goto L_0x003b
            r7.setTitle(r2)     // Catch:{ Exception -> 0x00c4 }
            r6.menuItemTitle = r7     // Catch:{ Exception -> 0x00c4 }
            androidx.fragment.app.FragmentActivity r7 = r6.getFragmentActivity()     // Catch:{ Exception -> 0x00c4 }
            r7.supportInvalidateOptionsMenu()     // Catch:{ Exception -> 0x00c4 }
            goto L_0x00c2
        L_0x003b:
            java.lang.String r2 = "icon"
            boolean r2 = r0.has(r2)     // Catch:{ Exception -> 0x00c4 }
            if (r2 != 0) goto L_0x004b
            java.lang.String r0 = " "
            r7.setTitle(r0)     // Catch:{ Exception -> 0x00c4 }
            r6.menuItemTitle = r7     // Catch:{ Exception -> 0x00c4 }
            return r1
        L_0x004b:
            java.lang.String r2 = "icon"
            java.lang.String r2 = r0.optString(r2)     // Catch:{ Exception -> 0x00c4 }
            java.lang.String r3 = "iconType"
            java.lang.String r3 = r0.optString(r3)     // Catch:{ Exception -> 0x00c4 }
            boolean r5 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Exception -> 0x00c4 }
            if (r5 != 0) goto L_0x00c3
            boolean r5 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Exception -> 0x00c4 }
            if (r5 == 0) goto L_0x0064
            goto L_0x00c3
        L_0x0064:
            java.lang.String r5 = "stretch"
            boolean r0 = r0.optBoolean(r5)     // Catch:{ Exception -> 0x00c4 }
            r7.stretch = r0     // Catch:{ Exception -> 0x00c4 }
            java.lang.String r0 = "IconFont"
            boolean r0 = r3.equals(r0)     // Catch:{ Exception -> 0x00c4 }
            if (r0 == 0) goto L_0x0085
            androidx.fragment.app.FragmentActivity r0 = r6.getFragmentActivity()     // Catch:{ Exception -> 0x00c4 }
            if (r0 == 0) goto L_0x0085
            androidx.fragment.app.FragmentActivity r0 = r6.getFragmentActivity()     // Catch:{ Exception -> 0x00c4 }
            int r0 = r7.setIconFontId(r0, r2)     // Catch:{ Exception -> 0x00c4 }
            if (r0 < 0) goto L_0x00b6
            goto L_0x00b7
        L_0x0085:
            java.lang.String r0 = "Native"
            boolean r0 = r3.equals(r0)     // Catch:{ Exception -> 0x00c4 }
            if (r0 == 0) goto L_0x0094
            int r0 = r7.setIconResId(r2)     // Catch:{ Exception -> 0x00c4 }
            if (r0 < 0) goto L_0x00b6
            goto L_0x00b7
        L_0x0094:
            java.lang.String r0 = "Base64"
            boolean r0 = r3.equals(r0)     // Catch:{ Exception -> 0x00c4 }
            if (r0 == 0) goto L_0x00ab
            androidx.fragment.app.FragmentActivity r0 = r6.getFragmentActivity()     // Catch:{ Exception -> 0x00c4 }
            int r0 = com.alibaba.aliweex.utils.WXUtil.getActionBarHeight(r0)     // Catch:{ Exception -> 0x00c4 }
            float r0 = (float) r0     // Catch:{ Exception -> 0x00c4 }
            boolean r0 = r7.setIconBitmap(r2, r0)     // Catch:{ Exception -> 0x00c4 }
            r4 = r0
            goto L_0x00b7
        L_0x00ab:
            java.lang.String r0 = "URL"
            boolean r0 = r3.equals(r0)     // Catch:{ Exception -> 0x00c4 }
            if (r0 == 0) goto L_0x00b6
            r7.href = r2     // Catch:{ Exception -> 0x00c4 }
            goto L_0x00b7
        L_0x00b6:
            r4 = 0
        L_0x00b7:
            if (r4 == 0) goto L_0x00c2
            r6.menuItemTitle = r7     // Catch:{ Exception -> 0x00c4 }
            androidx.fragment.app.FragmentActivity r7 = r6.getFragmentActivity()     // Catch:{ Exception -> 0x00c4 }
            r7.supportInvalidateOptionsMenu()     // Catch:{ Exception -> 0x00c4 }
        L_0x00c2:
            return r4
        L_0x00c3:
            return r1
        L_0x00c4:
            r7 = move-exception
            java.lang.String r0 = "WXNavBarAdapter"
            java.lang.String r7 = com.taobao.weex.utils.WXLogUtils.getStackTrace(r7)
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r0, (java.lang.String) r7)
            return r1
        L_0x00cf:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.aliweex.hc.bundle.WXHCNavBarAdapter.setNavBarTitle(java.lang.String):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x000f, code lost:
        r0 = getFragmentActivity().getSupportFragmentManager();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.alibaba.aliweex.WXError setLeftItem(com.taobao.weex.WXSDKInstance r4, com.alibaba.fastjson.JSONObject r5, com.alibaba.aliweex.adapter.INavigationBarModuleAdapter.OnItemClickListener r6) {
        /*
            r3 = this;
            java.lang.String r4 = r5.toJSONString()
            boolean r4 = r3.setNavBarLeftItem(r4)
            androidx.fragment.app.FragmentActivity r0 = r3.getFragmentActivity()
            r1 = 0
            if (r0 == 0) goto L_0x0022
            androidx.fragment.app.FragmentActivity r0 = r3.getFragmentActivity()
            androidx.fragment.app.FragmentManager r0 = r0.getSupportFragmentManager()
            if (r0 == 0) goto L_0x0022
            java.lang.String r2 = com.alibaba.aliweex.bundle.WeexPageFragment.FRAGMENT_TAG
            androidx.fragment.app.Fragment r0 = r0.findFragmentByTag(r2)
            com.alibaba.aliweex.bundle.WeexPageFragment r0 = (com.alibaba.aliweex.bundle.WeexPageFragment) r0
            goto L_0x0023
        L_0x0022:
            r0 = r1
        L_0x0023:
            if (r4 != 0) goto L_0x0037
            if (r0 == 0) goto L_0x0037
            if (r5 == 0) goto L_0x0033
            int r4 = r5.size()
            if (r4 <= 0) goto L_0x0033
            r0.setBackPressedListener(r6)
            goto L_0x0036
        L_0x0033:
            r0.setBackPressedListener(r1)
        L_0x0036:
            r4 = 1
        L_0x0037:
            if (r4 != 0) goto L_0x0043
            com.alibaba.aliweex.WXError r4 = new com.alibaba.aliweex.WXError
            r4.<init>()
            java.lang.String r5 = "WX_ERROR"
            r4.result = r5
            return r4
        L_0x0043:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.aliweex.hc.bundle.WXHCNavBarAdapter.setLeftItem(com.taobao.weex.WXSDKInstance, com.alibaba.fastjson.JSONObject, com.alibaba.aliweex.adapter.INavigationBarModuleAdapter$OnItemClickListener):com.alibaba.aliweex.WXError");
    }

    public WXError setRightItem(WXSDKInstance wXSDKInstance, com.alibaba.fastjson.JSONObject jSONObject, INavigationBarModuleAdapter.OnItemClickListener onItemClickListener) {
        if (setNavBarRightItem(jSONObject.toJSONString(), onItemClickListener)) {
            return null;
        }
        WXError wXError = new WXError();
        wXError.result = "WX_ERROR";
        return wXError;
    }

    private boolean shouldSetNavigator(NavigatorType navigatorType) {
        switch (navigatorType) {
            case RIGHT_ITEM:
            case CLEAR_RIGHT_ITEM:
            case TITLE:
                return shouldSetNavigator();
            default:
                return true;
        }
    }

    /* access modifiers changed from: protected */
    public WeexPageFragment getWeexPageFragment() {
        Fragment findFragmentByTag = getFragmentActivity().getSupportFragmentManager().findFragmentByTag(WeexPageFragment.FRAGMENT_TAG);
        if (findFragmentByTag == null || !(findFragmentByTag instanceof WeexPageFragment)) {
            return null;
        }
        return (WeexPageFragment) findFragmentByTag;
    }

    /* access modifiers changed from: protected */
    public boolean shouldSetNavigator() {
        try {
            String config = AliWeex.getInstance().getConfigAdapter().getConfig(CONFIG_GROUP_WEEX_HC, "weex_main_hc_domain", "");
            if (TextUtils.isEmpty(config)) {
                return true;
            }
            for (String str : config.split(",")) {
                String originalUrl = getWeexPageFragment().getOriginalUrl();
                if (!TextUtils.isEmpty(originalUrl) && originalUrl.contains(str)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    private boolean checkScheme(String str) {
        String scheme = Uri.parse(str).getScheme();
        if (TextUtils.isEmpty(scheme)) {
            return false;
        }
        String lowerCase = scheme.toLowerCase();
        return lowerCase.equals("http") || lowerCase.equals("https") || lowerCase.equals("data") || lowerCase.equals("local");
    }
}
