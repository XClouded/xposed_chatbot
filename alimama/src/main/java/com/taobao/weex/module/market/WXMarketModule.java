package com.taobao.weex.module.market;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.taobao.TBActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentActivity;
import com.alibaba.aliweex.hc.HCConfig;
import com.alibaba.aliweex.hc.IHCModuleAdapter;
import com.taobao.android.festival.FestivalMgr;
import com.taobao.android.nav.Nav;
import com.taobao.android.ultron.datamodel.imp.ProtocolConst;
import com.taobao.statistic.CT;
import com.taobao.statistic.TBS;
import com.taobao.uikit.actionbar.ITBPublicMenu;
import com.taobao.uikit.actionbar.TBPublicMenu;
import com.taobao.uikit.extend.feature.view.TUrlImageView;
import com.taobao.weex.adapter.R;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXResourceUtils;

public class WXMarketModule implements IHCModuleAdapter, View.OnClickListener {
    private HCConfig mHCPageConfig;

    public void updateActionBar(FragmentActivity fragmentActivity, HCConfig hCConfig) {
        if (fragmentActivity instanceof AppCompatActivity) {
            updateTBActionBar((AppCompatActivity) fragmentActivity, hCConfig);
        }
    }

    /* access modifiers changed from: package-private */
    public void updateTBActionBar(AppCompatActivity appCompatActivity, HCConfig hCConfig) {
        if (appCompatActivity != null && hCConfig != null && appCompatActivity.getSupportActionBar() != null) {
            ActionBar supportActionBar = appCompatActivity.getSupportActionBar();
            supportActionBar.setDisplayShowCustomEnabled(true);
            supportActionBar.setCustomView(R.layout.market_actionbar_layout);
            if (FestivalMgr.getInstance().isInValidTimeRange(ProtocolConst.KEY_GLOBAL, "actionBarBackgroundColor")) {
                FestivalMgr.getInstance().setBgUI4Actionbar(appCompatActivity, TBActionBar.ActionBarStyle.NORMAL);
            } else {
                supportActionBar.setBackgroundDrawable(new ColorDrawable(WXResourceUtils.getColor(hCConfig.naviBarBgColor)));
            }
            int globalColor = FestivalMgr.getInstance().getGlobalColor("actionbarTextColor", WXResourceUtils.getColor(hCConfig.naviBtnColor));
            Toolbar toolbar = (Toolbar) appCompatActivity.findViewById(R.id.action_bar);
            if (toolbar != null) {
                Drawable navigationIcon = toolbar.getNavigationIcon();
                if (navigationIcon != null) {
                    Drawable wrap = DrawableCompat.wrap(navigationIcon);
                    wrap.mutate();
                    DrawableCompat.setTint(wrap, globalColor);
                }
                if (appCompatActivity instanceof ITBPublicMenu) {
                    TBPublicMenu publicMenu = ((ITBPublicMenu) appCompatActivity).getPublicMenu();
                    if (publicMenu != null) {
                        publicMenu.setActionViewIconColor(globalColor);
                    }
                } else {
                    Drawable overflowIcon = toolbar.getOverflowIcon();
                    if (overflowIcon != null) {
                        Drawable wrap2 = DrawableCompat.wrap(overflowIcon);
                        wrap2.mutate();
                        DrawableCompat.setTint(wrap2, globalColor);
                    }
                }
            }
            FrameLayout frameLayout = (FrameLayout) appCompatActivity.findViewById(R.id.huichang_middle_ll);
            int i = 8;
            if (frameLayout != null) {
                for (int i2 = 0; i2 < frameLayout.getChildCount(); i2++) {
                    frameLayout.getChildAt(i2).setVisibility(8);
                }
            }
            TextView textView = (TextView) appCompatActivity.findViewById(R.id.huichang_middle_text);
            if (textView == null || TextUtils.isEmpty(hCConfig.naviCenterText)) {
                ImageView imageView = (ImageView) appCompatActivity.findViewById(R.id.huichang_base64);
                if (imageView == null || TextUtils.isEmpty(hCConfig.naviCenterBase64Img)) {
                    TUrlImageView tUrlImageView = (TUrlImageView) appCompatActivity.findViewById(R.id.huichang_middle_btn);
                    if (tUrlImageView != null && !TextUtils.isEmpty(hCConfig.naviCenterImg)) {
                        tUrlImageView.setVisibility(0);
                        tUrlImageView.setImageUrl(hCConfig.naviCenterImg);
                        tUrlImageView.setOnClickListener(this);
                    }
                } else {
                    imageView.setVisibility(0);
                    try {
                        byte[] decode = Base64.decode(hCConfig.naviCenterBase64Img.replace(' ', '+'), 0);
                        Bitmap decodeByteArray = BitmapFactory.decodeByteArray(decode, 0, decode.length);
                        if (decodeByteArray != null) {
                            imageView.setImageBitmap(decodeByteArray);
                            imageView.setOnClickListener(this);
                        }
                    } catch (IllegalArgumentException unused) {
                        WXLogUtils.e("ActionBarMenuItem", "base64 to byteArr decode fail");
                    }
                }
            } else {
                textView.setVisibility(0);
                textView.setText(hCConfig.naviCenterText);
                textView.setTextColor(FestivalMgr.getInstance().getGlobalColor("actionbarTextColor", WXResourceUtils.getColor(hCConfig.naviCenterTextColor)));
                textView.setOnClickListener(this);
            }
            TUrlImageView tUrlImageView2 = (TUrlImageView) appCompatActivity.findViewById(R.id.huichang_right_btn);
            if (tUrlImageView2 != null) {
                if (!TextUtils.isEmpty(hCConfig.naviRightImg)) {
                    i = 0;
                }
                tUrlImageView2.setVisibility(i);
                if (!TextUtils.isEmpty(hCConfig.naviRightImg)) {
                    tUrlImageView2.setImageUrl(hCConfig.naviRightImg);
                    tUrlImageView2.setOnClickListener(this);
                }
            }
            this.mHCPageConfig = hCConfig;
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.huichang_middle_btn || id == R.id.huichang_middle_text) {
            if (this.mHCPageConfig != null) {
                Nav.from(view.getContext()).toUri(this.mHCPageConfig.naviCenterClickURL);
                String str = this.mHCPageConfig.pageUt;
                String str2 = this.mHCPageConfig.naviCenterUt;
                utByPage(str, "Button", str2, "spm=" + this.mHCPageConfig.pageSpm);
            }
        } else if (id == R.id.huichang_right_btn && this.mHCPageConfig != null) {
            Nav.from(view.getContext()).toUri(this.mHCPageConfig.naviRightClickURL);
            String str3 = this.mHCPageConfig.pageUt;
            String str4 = this.mHCPageConfig.naviRightUt;
            utByPage(str3, "Button", str4, "spm=" + this.mHCPageConfig.pageSpm);
        }
    }

    private static void utByPage(String str, String str2, String str3, String str4) {
        if (str != null && str2 != null && str3 != null && str4 != null) {
            TBS.Adv.ctrlClickedOnPage(str, CT.valueOf(str2), str3, str4);
        }
    }
}
