package com.taobao.weex.adapter;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.MenuItemCompat;
import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.WXError;
import com.alibaba.aliweex.hc.bundle.WXActionBarMenuItem;
import com.alibaba.aliweex.hc.bundle.WXHCNavBarAdapter;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.festival.FestivalMgr;
import com.taobao.baseactivity.CustomBaseActivity;
import com.taobao.tao.util.AppcompatUtils;
import com.taobao.uikit.actionbar.ITBPublicMenu;
import com.taobao.uikit.actionbar.TBActionView;
import com.taobao.uikit.actionbar.TBPublicMenu;
import com.taobao.uikit.extend.feature.view.TIconFontTextView;
import com.taobao.weex.WXActivity;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXImageStrategy;
import com.taobao.weex.dom.WXImageQuality;
import com.taobao.weex.ui.component.richtext.node.RichTextNode;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXTBConstant;
import com.taobao.weex.utils.WXTBUtil;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TBNavBarAdapter extends WXHCNavBarAdapter {
    private static final String TAG = "TBNavBarAdapter";
    /* access modifiers changed from: private */
    public AppCompatActivity mActivity;
    private boolean mHasSetNavBarColor = false;
    private String mWeexUrl;

    public TBNavBarAdapter(AppCompatActivity appCompatActivity) {
        super(appCompatActivity);
        this.mActivity = appCompatActivity;
    }

    public void setWeexUrl(String str) {
        this.mWeexUrl = str;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0048, code lost:
        if (r6 == false) goto L_0x004c;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void push(android.app.Activity r4, java.lang.String r5, org.json.JSONObject r6) {
        /*
            r3 = this;
            android.os.Bundle r0 = new android.os.Bundle
            r0.<init>()
            java.lang.String r1 = "wx_options"
            java.lang.String r2 = r6.toString()
            com.alibaba.fastjson.JSONObject r2 = com.alibaba.fastjson.JSON.parseObject(r2)
            r0.putSerializable(r1, r2)
            com.taobao.android.nav.Nav r1 = com.taobao.android.nav.Nav.from(r4)
            com.taobao.android.nav.Nav r0 = r1.withExtras(r0)
            java.lang.String r5 = r5.trim()
            r0.toUri((java.lang.String) r5)
            java.lang.String r5 = "transform"
            java.lang.String r5 = r6.optString(r5)
            java.lang.String r0 = "animated"
            java.lang.Object r6 = r6.opt(r0)
            r0 = 0
            if (r6 == 0) goto L_0x004b
            boolean r1 = r6 instanceof java.lang.String
            r2 = 1
            if (r1 == 0) goto L_0x003c
            java.lang.String r6 = (java.lang.String) r6
            boolean r6 = java.lang.Boolean.parseBoolean(r6)
            goto L_0x0048
        L_0x003c:
            boolean r1 = r6 instanceof java.lang.Boolean
            if (r1 == 0) goto L_0x0047
            java.lang.Boolean r6 = (java.lang.Boolean) r6
            boolean r6 = r6.booleanValue()
            goto L_0x0048
        L_0x0047:
            r6 = 1
        L_0x0048:
            if (r6 != 0) goto L_0x004b
            goto L_0x004c
        L_0x004b:
            r2 = 0
        L_0x004c:
            if (r2 == 0) goto L_0x0052
            r4.overridePendingTransition(r0, r0)
            goto L_0x005d
        L_0x0052:
            java.lang.String r6 = "3d"
            boolean r5 = r6.equals(r5)
            if (r5 == 0) goto L_0x005d
            r4.overridePendingTransition(r0, r0)
        L_0x005d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.adapter.TBNavBarAdapter.push(android.app.Activity, java.lang.String, org.json.JSONObject):void");
    }

    public WXError show(WXSDKInstance wXSDKInstance, JSONObject jSONObject) {
        if (jSONObject == null) {
            return null;
        }
        jSONObject.getBooleanValue("animated");
        this.mActivity.getSupportActionBar().show();
        return null;
    }

    public WXError hide(WXSDKInstance wXSDKInstance, JSONObject jSONObject) {
        if (jSONObject == null) {
            return null;
        }
        jSONObject.getBooleanValue("animated");
        this.mActivity.getSupportActionBar().hide();
        return null;
    }

    public WXError setStyle(WXSDKInstance wXSDKInstance, JSONObject jSONObject) {
        if (this.mActivity.getSupportActionBar() == null || jSONObject == null) {
            return null;
        }
        try {
            int parseColor = Color.parseColor(jSONObject.getString("color"));
            Toolbar toolbar = (Toolbar) this.mActivity.findViewById(R.id.action_bar);
            if (toolbar != null) {
                toolbar.setTitleTextColor(parseColor);
                toolbar.setSubtitleTextColor(parseColor);
                Drawable navigationIcon = toolbar.getNavigationIcon();
                if (navigationIcon != null) {
                    Drawable mutate = DrawableCompat.wrap(navigationIcon).mutate();
                    DrawableCompat.setTint(mutate, parseColor);
                    toolbar.setNavigationIcon(mutate);
                }
                if (this.mActivity instanceof ITBPublicMenu) {
                    TBPublicMenu publicMenu = ((ITBPublicMenu) this.mActivity).getPublicMenu();
                    if (publicMenu != null) {
                        publicMenu.setActionViewIconColor(parseColor);
                    }
                } else {
                    Drawable overflowIcon = toolbar.getOverflowIcon();
                    if (overflowIcon != null) {
                        Drawable wrap = DrawableCompat.wrap(overflowIcon);
                        wrap.mutate();
                        DrawableCompat.setTint(wrap, parseColor);
                    }
                }
                this.mHasSetNavBarColor = true;
            }
        } catch (Exception unused) {
        }
        return null;
    }

    public WXError setBadgeStyle(WXSDKInstance wXSDKInstance, JSONObject jSONObject) {
        if (jSONObject == null) {
            return null;
        }
        if (!(this.mActivity instanceof WXActivity)) {
            WXError wXError = new WXError();
            wXError.result = "WX_NOT_SUPPORTED";
            wXError.message = "Only WXActivity support setBadgeStyle(), or your own activity should implement getOverflowMenuButton()";
            return wXError;
        }
        String string = jSONObject.getString(RichTextNode.STYLE);
        if (string == null) {
            WXError wXError2 = new WXError();
            wXError2.result = "WX_NOT_SUPPORTED";
            wXError2.message = "params error";
            return wXError2;
        }
        TBActionView tBActionView = ((WXActivity) this.mActivity).overflowButton;
        if (tBActionView != null) {
            try {
                if ("light".equals(string)) {
                    tBActionView.setMessageNumColor(getResources().getColor(R.color.uik_action_message_num_dark));
                    tBActionView.setMessageBackgroundColor(getResources().getColor(R.color.uik_action_message_bg_dark));
                } else if ("dark".equals(string)) {
                    tBActionView.setMessageNumColor(getResources().getColor(R.color.uik_action_message_num_normal));
                    tBActionView.setMessageBackgroundColor(getResources().getColor(R.color.uik_action_message_bg_normal));
                } else {
                    tBActionView.setMessageNumColor(getResources().getColor(R.color.uik_action_message_num_normal));
                    tBActionView.setMessageBackgroundColor(getResources().getColor(R.color.uik_action_message_bg_normal));
                }
            } catch (Throwable th) {
                WXLogUtils.e("exception in set badge style. ", WXLogUtils.getStackTrace(th));
            }
        }
        return null;
    }

    public WXError hasMenu(WXSDKInstance wXSDKInstance, JSONObject jSONObject) {
        if (jSONObject == null) {
            return null;
        }
        try {
            Boolean bool = jSONObject.getBoolean("show");
            if (this.mActivity instanceof CustomBaseActivity) {
                if (bool == null || bool.booleanValue()) {
                    this.mActivity.togglePublicMenu(true);
                } else {
                    this.mActivity.togglePublicMenu(false);
                }
                this.mActivity.supportInvalidateOptionsMenu();
                return null;
            }
        } catch (Throwable unused) {
        }
        WXError wXError = new WXError();
        wXError.message = "Activity not support";
        return wXError;
    }

    public WXError setTransparent(WXSDKInstance wXSDKInstance, JSONObject jSONObject) {
        if (this.mActivity.getSupportActionBar() == null || jSONObject == null) {
            return null;
        }
        String string = jSONObject.getString("transparence");
        if (this.mActivity instanceof WXActivity) {
            ((WXActivity) this.mActivity).setNaviTransparent(!"true".equals(string));
        }
        return null;
    }

    public WXError getHeight(WXSDKInstance wXSDKInstance) {
        Toolbar toolbar;
        if (this.mActivity.getSupportActionBar() == null || (toolbar = (Toolbar) this.mActivity.findViewById(R.id.action_bar)) == null) {
            return null;
        }
        WXError wXError = new WXError();
        wXError.result = String.valueOf(toolbar.getHeight());
        return wXError;
    }

    public WXError getStatusBarHeight(WXSDKInstance wXSDKInstance) {
        int identifier = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier <= 0) {
            return null;
        }
        int dimensionPixelSize = getResources().getDimensionPixelSize(identifier);
        WXError wXError = new WXError();
        wXError.result = String.valueOf(dimensionPixelSize);
        return wXError;
    }

    public WXError showMenu(WXSDKInstance wXSDKInstance, JSONObject jSONObject) {
        ViewGroup viewGroup;
        if (jSONObject == null) {
            return null;
        }
        if (!(this.mActivity instanceof WXActivity)) {
            WXError wXError = new WXError();
            wXError.result = "WX_NOT_SUPPORTED";
            wXError.message = "Only WXActivity support showMenu(), or your own activity should implement getOverflowMenuButton()";
            return wXError;
        }
        boolean equals = jSONObject != null ? "true".equals(jSONObject.get("cancelActualShow")) : false;
        try {
            WXActivity wXActivity = (WXActivity) this.mActivity;
            final ActionBar supportActionBar = wXActivity.getSupportActionBar();
            try {
                Method method = ActionBar.class.getMethod("setShowHideAnimationEnabled", new Class[]{Boolean.TYPE});
                method.setAccessible(true);
                method.invoke(supportActionBar, new Object[]{false});
            } catch (Throwable th) {
                WXLogUtils.e("exception in cancel action animation. ", WXLogUtils.getStackTrace(th));
            }
            try {
                int identifier = wXActivity.getResources().getIdentifier("action_bar", "id", this.mActivity.getPackageName());
                if (!(identifier == 0 || (viewGroup = (ViewGroup) wXActivity.findViewById(identifier)) == null)) {
                    for (int i = 0; i < 3; i++) {
                        View childAt = viewGroup.getChildAt(i);
                        if (childAt instanceof ImageButton) {
                            ((ImageButton) childAt).setImageDrawable(wXActivity.getResources().getDrawable(17170445));
                        } else if (childAt instanceof TextView) {
                            ((TextView) childAt).setText("");
                        } else if (childAt instanceof ActionMenuView) {
                            childAt.setVisibility(4);
                        }
                    }
                }
            } catch (Throwable th2) {
                WXLogUtils.e("exception in hide actionbar views. ", WXLogUtils.getStackTrace(th2));
            }
            final TBActionView tBActionView = wXActivity.overflowButton;
            if (tBActionView != null) {
                tBActionView.setVisibility(4);
            }
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("color", (Object) "#00ffffff");
            setStyle(wXSDKInstance, jSONObject2);
            final Handler handler = new Handler(Looper.getMainLooper());
            if (!equals) {
                if (supportActionBar != null) {
                    supportActionBar.show();
                }
                handler.postDelayed(new Runnable() {
                    public void run() {
                        if (tBActionView != null) {
                            tBActionView.performClick();
                        }
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                if (supportActionBar != null) {
                                    supportActionBar.hide();
                                }
                            }
                        }, 64);
                    }
                }, 64);
            } else {
                if (supportActionBar != null) {
                    supportActionBar.show();
                }
                handler.postDelayed(new Runnable() {
                    public void run() {
                        if (supportActionBar != null) {
                            supportActionBar.hide();
                        }
                    }
                }, 32);
            }
            return null;
        } catch (Exception e) {
            WXLogUtils.e("exception in cancel action animation. ", WXLogUtils.getStackTrace(e));
            WXError wXError2 = new WXError();
            wXError2.result = "ERROR";
            wXError2.message = e.getMessage();
            return wXError2;
        }
    }

    public boolean onCreateOptionsMenu(WXSDKInstance wXSDKInstance, Menu menu) {
        if (shouldSetNavigator()) {
            updateRightItem(menu);
            updateCustomTitle();
        }
        updateOverflowItems(menu);
        return true;
    }

    private void updateOverflowItems(Menu menu) {
        menu.removeGroup(R.id.navigation_bar_more_group);
        List<WXActionBarMenuItem> menuItemMore = getMenuItemMore();
        if (menuItemMore != null && !menuItemMore.isEmpty()) {
            int i = 0;
            for (final WXActionBarMenuItem next : menuItemMore) {
                final int i2 = R.id.navigation_bar_more_start_id + i;
                MenuItem add = menu.add(R.id.navigation_bar_more_group, i2, 0, next.title);
                if (next.iconResId > 0) {
                    add.setIcon(next.iconResId);
                } else if (next.iconFontId > 0) {
                    add.setTitle(getResources().getString(next.iconFontId) + ":" + next.title);
                } else if (next.iconBitmap != null && !next.iconBitmap.isRecycled()) {
                    add.setIcon(resizeIcon(new BitmapDrawable(getResources(), next.iconBitmap)));
                } else if (!TextUtils.isEmpty(next.href)) {
                    ImageView imageView = new ImageView(this.mActivity);
                    WXImageStrategy wXImageStrategy = new WXImageStrategy();
                    wXImageStrategy.isClipping = true;
                    wXImageStrategy.setImageListener(new WXImageStrategy.ImageListener() {
                        public void onImageFinish(String str, ImageView imageView, boolean z, Map map) {
                            WeakReference weakReference;
                            if (map != null && (weakReference = (WeakReference) map.get("drawable")) != null && weakReference.get() != null) {
                                TBPublicMenu publicMenu = ((ITBPublicMenu) TBNavBarAdapter.this.mActivity).getPublicMenu();
                                publicMenu.getExtraMenu(i2).setIconDrawable(TBNavBarAdapter.this.resizeIcon((BitmapDrawable) weakReference.get()));
                                publicMenu.notifyMenuChanged();
                            }
                        }
                    });
                    WXSDKEngine.getIWXImgLoaderAdapter().setImage(next.href, imageView, WXImageQuality.ORIGINAL, wXImageStrategy);
                }
                add.setIntent(next.data);
                MenuItemCompat.setShowAsAction(add, 8);
                add.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    /* JADX WARNING: Removed duplicated region for block: B:14:? A[RETURN, SYNTHETIC] */
                    /* JADX WARNING: Removed duplicated region for block: B:9:0x001c  */
                    /* Code decompiled incorrectly, please refer to instructions dump. */
                    public boolean onMenuItemClick(android.view.MenuItem r4) {
                        /*
                            r3 = this;
                            java.util.HashMap r0 = new java.util.HashMap
                            r0.<init>()
                            r1 = -1
                            if (r4 == 0) goto L_0x0019
                            android.content.Intent r2 = r4.getIntent()
                            if (r2 == 0) goto L_0x0019
                            android.content.Intent r4 = r4.getIntent()     // Catch:{ Exception -> 0x0019 }
                            java.lang.String r2 = "index"
                            int r4 = r4.getIntExtra(r2, r1)     // Catch:{ Exception -> 0x0019 }
                            goto L_0x001a
                        L_0x0019:
                            r4 = -1
                        L_0x001a:
                            if (r4 < 0) goto L_0x003e
                            java.lang.String r1 = "index"
                            java.lang.Integer r2 = java.lang.Integer.valueOf(r4)
                            r0.put(r1, r2)
                            com.alibaba.aliweex.hc.bundle.WXActionBarMenuItem r1 = r3
                            com.alibaba.aliweex.adapter.INavigationBarModuleAdapter$OnItemClickListener r1 = r1.itemClickListener
                            if (r1 == 0) goto L_0x0033
                            com.alibaba.aliweex.hc.bundle.WXActionBarMenuItem r0 = r3
                            com.alibaba.aliweex.adapter.INavigationBarModuleAdapter$OnItemClickListener r0 = r0.itemClickListener
                            r0.onClick(r4)
                            goto L_0x003e
                        L_0x0033:
                            com.taobao.weex.adapter.TBNavBarAdapter r4 = com.taobao.weex.adapter.TBNavBarAdapter.this
                            com.alibaba.aliweex.bundle.WeexPageFragment r4 = r4.getWeexPageFragment()
                            java.lang.String r1 = com.taobao.weex.utils.WXTBConstant.CLICK_MORE_ITEM
                            r4.fireEvent(r1, r0)
                        L_0x003e:
                            r4 = 1
                            return r4
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.adapter.TBNavBarAdapter.AnonymousClass4.onMenuItemClick(android.view.MenuItem):boolean");
                    }
                });
                i++;
            }
        }
    }

    public boolean isMainHC() {
        return !shouldSetNavigator();
    }

    private Resources getResources() {
        return this.mActivity.getResources();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0006, code lost:
        r1 = r7.mActivity.getSupportActionBar();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateCustomTitle() {
        /*
            r7 = this;
            com.alibaba.aliweex.hc.bundle.WXActionBarMenuItem r0 = r7.getMenuItemTitle()
            if (r0 == 0) goto L_0x00ae
            androidx.appcompat.app.AppCompatActivity r1 = r7.mActivity
            androidx.appcompat.app.ActionBar r1 = r1.getSupportActionBar()
            if (r1 == 0) goto L_0x00ae
            java.lang.String r2 = r0.title
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            r3 = 0
            r4 = 1
            if (r2 == 0) goto L_0x00a3
            r1.setDisplayShowTitleEnabled(r3)
            android.widget.ImageView r2 = new android.widget.ImageView
            androidx.appcompat.app.AppCompatActivity r3 = r7.mActivity
            r2.<init>(r3)
            boolean r3 = r0.stretch
            if (r3 == 0) goto L_0x002f
            android.view.ViewGroup$LayoutParams r3 = new android.view.ViewGroup$LayoutParams
            r5 = -1
            r3.<init>(r5, r5)
            r2.setLayoutParams(r3)
        L_0x002f:
            int r3 = r0.iconResId
            if (r3 <= 0) goto L_0x0039
            int r0 = r0.iconResId
            r2.setImageResource(r0)
            goto L_0x008e
        L_0x0039:
            int r3 = r0.iconFontId
            if (r3 <= 0) goto L_0x004d
            androidx.appcompat.app.AppCompatActivity r3 = r7.mActivity
            int r0 = r0.iconFontId
            android.graphics.drawable.BitmapDrawable r0 = getIconFontDrawable(r3, r0)
            android.graphics.drawable.BitmapDrawable r0 = r7.resizeIcon(r0)
            r2.setImageDrawable(r0)
            goto L_0x008e
        L_0x004d:
            android.graphics.Bitmap r3 = r0.iconBitmap
            if (r3 == 0) goto L_0x006c
            android.graphics.Bitmap r3 = r0.iconBitmap
            boolean r3 = r3.isRecycled()
            if (r3 != 0) goto L_0x006c
            android.graphics.drawable.BitmapDrawable r3 = new android.graphics.drawable.BitmapDrawable
            android.content.res.Resources r5 = r7.getResources()
            android.graphics.Bitmap r0 = r0.iconBitmap
            r3.<init>(r5, r0)
            android.graphics.drawable.BitmapDrawable r0 = r7.resizeIcon(r3)
            r2.setImageDrawable(r0)
            goto L_0x008e
        L_0x006c:
            java.lang.String r3 = r0.href
            boolean r3 = android.text.TextUtils.isEmpty(r3)
            if (r3 != 0) goto L_0x008e
            com.taobao.weex.common.WXImageStrategy r3 = new com.taobao.weex.common.WXImageStrategy
            r3.<init>()
            r3.isClipping = r4
            com.taobao.weex.adapter.TBNavBarAdapter$5 r5 = new com.taobao.weex.adapter.TBNavBarAdapter$5
            r5.<init>(r1, r0, r2)
            r3.setImageListener(r5)
            com.taobao.weex.adapter.IWXImgLoaderAdapter r5 = com.taobao.weex.WXSDKEngine.getIWXImgLoaderAdapter()
            java.lang.String r0 = r0.href
            com.taobao.weex.dom.WXImageQuality r6 = com.taobao.weex.dom.WXImageQuality.ORIGINAL
            r5.setImage(r0, r2, r6, r3)
        L_0x008e:
            r2.setClickable(r4)
            r1.setCustomView((android.view.View) r2)
            r1.setDisplayShowCustomEnabled(r4)
            r1.setDisplayShowHomeEnabled(r4)
            com.taobao.weex.adapter.TBNavBarAdapter$6 r0 = new com.taobao.weex.adapter.TBNavBarAdapter$6
            r0.<init>()
            r2.setOnClickListener(r0)
            goto L_0x00ae
        L_0x00a3:
            r1.setDisplayShowTitleEnabled(r4)
            r1.setDisplayShowCustomEnabled(r3)
            java.lang.String r0 = r0.title
            r1.setTitle((java.lang.CharSequence) r0)
        L_0x00ae:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.adapter.TBNavBarAdapter.updateCustomTitle():void");
    }

    private void updateRightItem(Menu menu) {
        final WXActionBarMenuItem menuItemRight = getMenuItemRight();
        try {
            menu.removeItem(R.id.navigation_bar_right_id);
        } catch (Throwable unused) {
        }
        if (menuItemRight != null) {
            final MenuItem add = menu.add(0, R.id.navigation_bar_right_id, 0, "");
            if (!TextUtils.isEmpty(menuItemRight.title)) {
                add.setTitle(menuItemRight.title);
            } else if (menuItemRight.iconResId > 0) {
                add.setIcon(menuItemRight.iconResId);
            } else if (menuItemRight.iconFontId > 0) {
                add.setTitle(AppcompatUtils.getMenuTitle("", menuItemRight.iconFontId));
            } else if (menuItemRight.iconBitmap != null && !menuItemRight.iconBitmap.isRecycled()) {
                add.setIcon(resizeIcon(new BitmapDrawable(getResources(), menuItemRight.iconBitmap)));
            } else if (!TextUtils.isEmpty(menuItemRight.href)) {
                ImageView imageView = new ImageView(this.mActivity);
                WXImageStrategy wXImageStrategy = new WXImageStrategy();
                wXImageStrategy.isClipping = true;
                wXImageStrategy.setImageListener(new WXImageStrategy.ImageListener() {
                    public void onImageFinish(String str, ImageView imageView, boolean z, Map map) {
                        WeakReference weakReference;
                        if (map != null && (weakReference = (WeakReference) map.get("drawable")) != null && weakReference.get() != null) {
                            add.setIcon(TBNavBarAdapter.this.resizeIcon((BitmapDrawable) weakReference.get()));
                        }
                    }
                });
                WXSDKEngine.getIWXImgLoaderAdapter().setImage(menuItemRight.href, imageView, WXImageQuality.ORIGINAL, wXImageStrategy);
            }
            add.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if (menuItemRight.itemClickListener != null) {
                        menuItemRight.itemClickListener.onClick(0);
                        return true;
                    }
                    TBNavBarAdapter.this.getWeexPageFragment().fireEvent(WXTBConstant.CLICK_RIGHT_ITEM, new HashMap());
                    return true;
                }
            });
            MenuItemCompat.setShowAsAction(add, 2);
        }
    }

    public static BitmapDrawable getIconFontDrawable(Activity activity, int i) {
        TIconFontTextView tIconFontTextView = new TIconFontTextView(activity);
        tIconFontTextView.setText(i);
        tIconFontTextView.setTextSize(24.0f);
        tIconFontTextView.getPaint().setFakeBoldText(true);
        tIconFontTextView.setTextColor(FestivalMgr.getInstance().getGlobalColor("actionbarTextColor", activity.getResources().getColor(R.color.abc_title_color)));
        try {
            tIconFontTextView.setTypeface(Typeface.createFromAsset(activity.getAssets(), "uik_iconfont.ttf"));
        } catch (RuntimeException e) {
            WXLogUtils.e(TAG, WXLogUtils.getStackTrace(e));
        }
        return new BitmapDrawable(activity.getResources(), WXTBUtil.convertViewToBitmap(tIconFontTextView));
    }

    private AssetManager getAssets() {
        return this.mActivity.getAssets();
    }

    /* access modifiers changed from: protected */
    public boolean shouldSetNavigator() {
        try {
            if ((this.mActivity instanceof WXActivity) && ((WXActivity) this.mActivity).isMainHc()) {
                return false;
            }
            String config = AliWeex.getInstance().getConfigAdapter().getConfig(WXHCNavBarAdapter.CONFIG_GROUP_WEEX_HC, "weex_main_hc_domain", "");
            if (TextUtils.isEmpty(config)) {
                return true;
            }
            for (String str : config.split(",")) {
                if (!TextUtils.isEmpty(this.mWeexUrl) && this.mWeexUrl.contains(str)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    /* access modifiers changed from: private */
    public BitmapDrawable resizeIcon(BitmapDrawable bitmapDrawable) {
        try {
            double height = (double) this.mActivity.getSupportActionBar().getHeight();
            Double.isNaN(height);
            int i = (int) (height * 0.6d);
            Bitmap bitmap = bitmapDrawable.getBitmap();
            int width = bitmap.getWidth();
            int height2 = bitmap.getHeight();
            float f = ((float) i) / ((float) height2);
            Matrix matrix = new Matrix();
            matrix.postScale(f, f);
            return new BitmapDrawable(getResources(), Bitmap.createBitmap(bitmap, 0, 0, width, height2, matrix, true));
        } catch (Throwable unused) {
            return bitmapDrawable;
        }
    }

    public boolean hasSetNavBarColor() {
        return this.mHasSetNavBarColor;
    }
}
