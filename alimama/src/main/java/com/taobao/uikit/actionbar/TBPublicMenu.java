package com.taobao.uikit.actionbar;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.RenderScript;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.taobao.util.Globals;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.taobao.android.launcher.config.ab.ABFeatures;
import com.taobao.phenix.bitmap.BitmapSupplier;
import com.taobao.phenix.compat.effects.RoundedCornersBitmapProcessor;
import com.taobao.tao.util.TBBitmapUtils;
import com.taobao.uikit.actionbar.TBLiteProgramAdapter;
import com.taobao.uikit.actionbar.TBPublicMenuAdapter;
import com.taobao.uikit.actionbar.TBPublicMenuItem;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

public class TBPublicMenu implements MenuItem.OnMenuItemClickListener {
    static final String TAG = "TBPublicMenu";
    static TBPublicMenu sCurrentPublicMenu;
    static boolean sDefaultInited = false;
    static ArrayList<TBPublicMenuItem> sDefaultPublicMenus = new ArrayList<>();
    static boolean sInited = false;
    static ArrayList<TBPublicMenuItem> sLitePrograms = new ArrayList<>();
    private static TBOnLiteProgramClickListener sOnLiteProgramClickListener;
    private static ArrayList<TBOnOverflowButtonClickListener> sOnOverflowButtonClickListeners = new ArrayList<>();
    private static TBOnPublicMenuClickListener sOnPublicMenuClickListener;
    static ArrayList<TBPublicMenuItem> sPublicMenus = new ArrayList<>();
    private static boolean sReset = false;
    WeakReference<Activity> mActivity;
    /* access modifiers changed from: private */
    public TBActionView mCustomOverflow;
    private Bundle mDefaultPageUserInfo;
    private TBOnPublicMenuClickListener mDefaultPublicMenuListener;
    protected ArrayList<TBPublicMenuItem> mExtensionMenu;
    protected String mExtensionTitle;
    protected ArrayList<TBPublicMenuItem> mExtraMenus;
    private ArrayList<MenuItemImpl> mFilteredMenus;
    ITBPublicMenu mItbPublicMenu;
    private TBLiteProgramAdapter mLiteProgramAdapter;
    private TBPublicMenuAdapter mMenuAdapter;
    @ColorInt
    private int mMenuIconColor;
    private ArrayList<MenuItemImpl> mMenuItems;
    protected boolean mNeedPublicMenu;
    protected boolean mNewMenu;
    private TBOnPublicMenuClickListener mOnCustomPublicMenuClickListener;
    /* access modifiers changed from: private */
    public PopupWindow mPopupMenu;
    private RenderScript mRenderScript;

    @Deprecated
    public interface TBOnLiteProgramClickListener {
        void onLiteProgramClicked(Context context, TBPublicMenuItem tBPublicMenuItem);
    }

    public interface TBOnOverflowButtonClickListener {
        void onOverflowButtonClicked();
    }

    public interface TBOnPublicMenuClickListener {
        void onPublicMenuItemClicked(TBPublicMenuItem tBPublicMenuItem);
    }

    public void setDefaultPageUserInfo(Bundle bundle) {
        this.mDefaultPageUserInfo = bundle;
    }

    private static void initDefaultMenu() {
        if (!sDefaultInited) {
            TBPublicMenuItem.Builder builder = new TBPublicMenuItem.Builder();
            builder.setTitle("ꂊ:消息").setMessageMode(TBPublicMenuItem.MessageMode.NONE).setUTControlName("wangxin").setNavUrl("http://m.taobao.com/go/msgcentercategory").setId(R.id.uik_menu_wangxin);
            TBPublicMenuItem build = builder.build();
            if (build != null) {
                sDefaultPublicMenus.add(build);
            }
            TBPublicMenuItem.Builder builder2 = new TBPublicMenuItem.Builder();
            builder2.setTitle("ꀚ:首页").setMessageMode(TBPublicMenuItem.MessageMode.NONE).setUTControlName("Home").setNavUrl("http://m.taobao.com/index.htm").setId(R.id.uik_menu_home);
            if (builder2.build() != null) {
                sDefaultPublicMenus.add(builder2.build());
            }
            TBPublicMenuItem.Builder builder3 = new TBPublicMenuItem.Builder();
            builder3.setTitle("떊:客服小蜜").setMessageMode(TBPublicMenuItem.MessageMode.TEXT).setUTControlName("help").setNavUrl(Globals.getApplication().getString(R.string.zh_helper_url)).setId(R.id.uik_menu_service);
            TBPublicMenuItem build2 = builder3.build();
            if (build2 != null) {
                sDefaultPublicMenus.add(build2);
            }
            Application application = Globals.getApplication();
            String string = application.getString(R.string.appcompat_feedback_url_old);
            if (ABFeatures.isBizOpen(application, "tucaoba")) {
                string = application.getString(R.string.appcompat_feedback_url);
            }
            TBPublicMenuItem.Builder builder4 = new TBPublicMenuItem.Builder();
            builder4.setTitle("끪:我要反馈").setMessageMode(TBPublicMenuItem.MessageMode.NONE).setUTControlName("feedback").setNavUrl(string).setId(R.id.uik_menu_feedback);
            TBPublicMenuItem build3 = builder4.build();
            if (build3 != null) {
                sDefaultPublicMenus.add(build3);
            }
            sDefaultInited = true;
        }
    }

    private static void init() {
        initDefaultMenu();
        if (!sInited) {
            sPublicMenus.clear();
            for (int i = 0; i < sDefaultPublicMenus.size(); i++) {
                try {
                    sPublicMenus.add((TBPublicMenuItem) sDefaultPublicMenus.get(i).clone());
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
            sInited = true;
        }
    }

    private void updateBlurView(int i, int i2) {
        Bitmap captureView = TBBitmapUtils.captureView((Activity) this.mActivity.get(), ((Activity) this.mActivity.get()).getWindow().getDecorView().getRootView(), i, i2);
        if (captureView != null) {
            if (this.mRenderScript == null) {
                this.mRenderScript = RenderScript.create(((Activity) this.mActivity.get()).getBaseContext());
            }
            this.mPopupMenu.setBackgroundDrawable(RoundedBitmapDrawableFactory.create(((Activity) this.mActivity.get()).getResources(), TBBitmapUtils.blur(24, this.mRenderScript, captureView)));
            this.mPopupMenu.setBackgroundDrawable(RoundedBitmapDrawableFactory.create(((Activity) this.mActivity.get()).getResources(), new RoundedCornersBitmapProcessor(i, i2, (int) ((Activity) this.mActivity.get()).getResources().getDimension(R.dimen.uik_public_menu_new_bg_radius), 0, RoundedCornersBitmapProcessor.CornerType.BOTTOM).process("", new BitmapSupplier(), ((RoundedBitmapDrawable) this.mPopupMenu.getBackground()).getBitmap())));
            return;
        }
        this.mPopupMenu.setBackgroundDrawable(((Activity) this.mActivity.get()).getResources().getDrawable(R.drawable.uik_public_menu_bg));
    }

    public TBPublicMenu(@NonNull Activity activity) {
        this(activity, (ITBPublicMenu) null);
    }

    public TBPublicMenu(@NonNull Activity activity, ITBPublicMenu iTBPublicMenu) {
        this.mActivity = null;
        this.mExtraMenus = new ArrayList<>();
        this.mFilteredMenus = new ArrayList<>();
        this.mMenuItems = new ArrayList<>();
        this.mNeedPublicMenu = false;
        this.mNewMenu = false;
        this.mActivity = new WeakReference<>(activity);
        this.mItbPublicMenu = iTBPublicMenu;
        this.mMenuIconColor = ContextCompat.getColor(activity, R.color.uik_action_icon_normal);
        this.mMenuAdapter = new TBPublicMenuAdapter(this);
        this.mLiteProgramAdapter = new TBLiteProgramAdapter(this);
        if (sReset) {
            sInited = false;
            sReset = false;
        }
        init();
    }

    @Deprecated
    public void needNewMenu(boolean z) {
        this.mNewMenu = z;
    }

    public void togglePublicMenu(boolean z) {
        this.mNeedPublicMenu = z;
        if (!this.mNeedPublicMenu) {
            sReset = true;
        }
    }

    public Menu onCreateOptionsMenu(MenuInflater menuInflater, Menu menu) {
        this.mExtraMenus.clear();
        this.mFilteredMenus.clear();
        this.mMenuItems.clear();
        filterMenus(menu);
        if (this.mNeedPublicMenu && menu.findItem(R.id.uik_menu_overflow) == null) {
            menuInflater.inflate(R.menu.uik_menu_overflow_action, menu);
            final MenuItem findItem = menu.findItem(R.id.uik_menu_overflow);
            if (this.mCustomOverflow == null) {
                this.mCustomOverflow = new TBActionView((Context) this.mActivity.get());
            }
            this.mCustomOverflow.setTitle(findItem.getTitle().toString());
            this.mCustomOverflow.setIconColor(this.mMenuIconColor);
            findItem.setActionView(this.mCustomOverflow);
            this.mCustomOverflow.setId(R.id.uik_action_overflow);
            this.mCustomOverflow.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    TBPublicMenu.this.onMenuItemClick(findItem);
                }
            });
            findItem.setOnMenuItemClickListener(this);
            this.mCustomOverflow.onMessageUpdate(getPublicMenu(R.id.uik_menu_wangxin));
            this.mMenuItems.add((MenuItemImpl) findItem);
        }
        return menu;
    }

    public Menu onPrepareOptionsMenu(Menu menu) {
        if (this.mCustomOverflow != null) {
            filterMenus(menu);
        }
        return menu;
    }

    private Menu filterMenus(Menu menu) {
        MenuBuilder menuBuilder = (MenuBuilder) menu;
        int i = 0;
        while (true) {
            if (i >= menuBuilder.size()) {
                break;
            }
            MenuItemImpl menuItemImpl = (MenuItemImpl) menuBuilder.getItem(i);
            if (menuItemImpl != null) {
                if (menuItemImpl.requiresActionButton()) {
                    String charSequence = menuItemImpl.getTitle().toString();
                    if (charSequence.length() >= 2 && charSequence.indexOf(":") == 1) {
                        if (menuItemImpl.getActionView() != null && (menuItemImpl.getActionView() instanceof TBActionView)) {
                            ((TBActionView) menuItemImpl.getActionView()).setIconColor(this.mMenuIconColor);
                            break;
                        }
                        TBActionView tBActionView = new TBActionView((Context) this.mActivity.get());
                        tBActionView.setTitle(charSequence);
                        tBActionView.setIconColor(this.mMenuIconColor);
                        tBActionView.setContentDescription(charSequence.substring(charSequence.indexOf(":"), charSequence.length()));
                        menuItemImpl.setActionView((View) tBActionView);
                        TBPublicMenuItem.Builder builder = new TBPublicMenuItem.Builder();
                        builder.setId(menuItemImpl.getItemId()).setTitle(menuItemImpl.getTitle().toString()).setIcon(menuItemImpl.getIcon());
                        final TBPublicMenuItem build = builder.build();
                        tBActionView.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                TBPublicMenu.this.onPublicMenuClicked(build);
                            }
                        });
                        this.mMenuItems.add(menuItemImpl);
                    } else {
                        if (MenuItemCompat.getActionProvider(menuItemImpl) == null && menuItemImpl.getActionView() == null && menuItemImpl.getIcon() == null && !TextUtils.isEmpty(menuItemImpl.getTitle())) {
                            TBActionView tBActionView2 = new TBActionView((Context) this.mActivity.get());
                            tBActionView2.setTitle(charSequence);
                            tBActionView2.setIconColor(this.mMenuIconColor);
                            tBActionView2.setContentDescription(charSequence);
                            menuItemImpl.setActionView((View) tBActionView2);
                            TBPublicMenuItem.Builder builder2 = new TBPublicMenuItem.Builder();
                            builder2.setId(menuItemImpl.getItemId()).setTitle(menuItemImpl.getTitle().toString()).setIcon(menuItemImpl.getIcon());
                            final TBPublicMenuItem build2 = builder2.build();
                            tBActionView2.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View view) {
                                    TBPublicMenu.this.onPublicMenuClicked(build2);
                                }
                            });
                        }
                        this.mMenuItems.add(menuItemImpl);
                    }
                } else {
                    TBPublicMenuItem.Builder builder3 = new TBPublicMenuItem.Builder();
                    builder3.setId(menuItemImpl.getItemId()).setTitle(menuItemImpl.getTitle().toString()).setIcon(menuItemImpl.getIcon());
                    this.mExtraMenus.add(builder3.build());
                    this.mFilteredMenus.add(menuItemImpl);
                }
            }
            i++;
        }
        for (int i2 = 0; i2 < this.mFilteredMenus.size(); i2++) {
            MenuItem menuItem = null;
            MenuItemImpl menuItemImpl2 = this.mFilteredMenus.get(i2);
            if (menuItemImpl2 != null) {
                menuItem = menuBuilder.findItem(menuItemImpl2.getItemId());
            }
            if (menuItem != null && !menuItemImpl2.requiresActionButton()) {
                menuBuilder.removeItem(menuItem.getItemId());
            }
        }
        return menu;
    }

    public void onResume() {
        init();
        sCurrentPublicMenu = this;
        if (this.mCustomOverflow != null) {
            this.mCustomOverflow.onMessageUpdate(getPublicMenu(R.id.uik_menu_wangxin));
        }
    }

    public void notifyMenuChanged() {
        if (this.mMenuAdapter != null && this.mLiteProgramAdapter != null && this.mPopupMenu != null) {
            this.mMenuAdapter.notifyDataSetChanged();
            this.mLiteProgramAdapter.notifyDataSetChanged();
            if (this.mPopupMenu.isShowing()) {
                int measuredHeight = this.mPopupMenu.getContentView().findViewById(R.id.uik_public_menu_recent_rl).getMeasuredHeight();
                int bottom = this.mPopupMenu.getContentView().getBottom();
                int bottom2 = this.mPopupMenu.getContentView().findViewById(R.id.uik_public_menu_recent_rl).getBottom();
                if (!showRecentLiteProgram()) {
                    if (bottom == bottom2) {
                        this.mPopupMenu.update(this.mPopupMenu.getContentView().getMeasuredWidth(), this.mPopupMenu.getContentView().getMeasuredHeight() - measuredHeight);
                    }
                } else if (bottom < bottom2) {
                    this.mPopupMenu.update(this.mPopupMenu.getContentView().getMeasuredWidth(), this.mPopupMenu.getContentView().getMeasuredHeight() + measuredHeight);
                }
                this.mPopupMenu.showAtLocation(((Activity) this.mActivity.get()).getWindow().getDecorView(), 0, 0, 0);
            }
        }
    }

    public void onPause() {
        sCurrentPublicMenu = null;
    }

    public boolean onMenuItemClick(MenuItem menuItem) {
        View actionView;
        if (this.mActivity.get() == null || menuItem == null) {
            return true;
        }
        if (menuItem.getItemId() != R.id.uik_menu_overflow || (actionView = menuItem.getActionView()) == null || !(actionView instanceof TBActionView)) {
            return false;
        }
        onOverflowClicked((TBActionView) actionView);
        return true;
    }

    public static void addPublicMenu(TBPublicMenuItem tBPublicMenuItem) {
        addPublicMenu(tBPublicMenuItem, false);
    }

    public static void addPublicMenu(TBPublicMenuItem tBPublicMenuItem, boolean z) {
        if (tBPublicMenuItem == null || !tBPublicMenuItem.checkValidation()) {
            Log.e(TAG, "TBPublicMenuItem not right, please check!");
            return;
        }
        if (sPublicMenus == null) {
            sPublicMenus = new ArrayList<>();
        }
        sPublicMenus.add(tBPublicMenuItem);
        if (z && sDefaultPublicMenus != null) {
            sDefaultPublicMenus.add(tBPublicMenuItem);
        }
        sReset = true;
        if (sCurrentPublicMenu != null) {
            sCurrentPublicMenu.updateMenuData();
        }
    }

    @Deprecated
    public static void addLiteProgram(ArrayList<TBPublicMenuItem> arrayList) {
        if (arrayList != null && arrayList.size() > 0) {
            if (sLitePrograms == null) {
                sLitePrograms = new ArrayList<>();
            } else {
                sLitePrograms.clear();
            }
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                sLitePrograms.add(arrayList.get(i));
            }
            sReset = true;
            if (sCurrentPublicMenu != null) {
                sCurrentPublicMenu.updateMenuData();
            }
        }
    }

    public void setCustomOverflow(TBActionView tBActionView) {
        if (tBActionView != null) {
            this.mCustomOverflow = tBActionView;
            this.mCustomOverflow.setIconColor(this.mMenuIconColor);
            this.mCustomOverflow.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    TBPublicMenu.this.onOverflowClicked(TBPublicMenu.this.mCustomOverflow);
                }
            });
            this.mCustomOverflow.onMessageUpdate(getPublicMenu(R.id.uik_menu_wangxin));
        }
    }

    public TBActionView getCustomOverflow() {
        Context context;
        if (this.mCustomOverflow == null && (context = (Context) this.mActivity.get()) != null) {
            this.mCustomOverflow = new TBActionView(context);
        }
        return this.mCustomOverflow;
    }

    public void addCustomMenus(ArrayList<TBPublicMenuItem> arrayList, TBOnPublicMenuClickListener tBOnPublicMenuClickListener) {
        if (this.mExtraMenus != null) {
            this.mExtraMenus.addAll(arrayList);
            this.mOnCustomPublicMenuClickListener = tBOnPublicMenuClickListener;
        }
    }

    public void setExtensionTitle(String str) {
        this.mExtensionTitle = str;
    }

    public void setExtensionMenu(ArrayList<TBPublicMenuItem> arrayList) {
        if (arrayList != null && arrayList.size() > 0) {
            if (this.mExtensionMenu == null) {
                this.mExtensionMenu = new ArrayList<>();
            } else {
                this.mExtensionMenu.clear();
            }
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                this.mExtensionMenu.add(arrayList.get(i));
            }
            if (sCurrentPublicMenu != null) {
                sCurrentPublicMenu.updateMenuData();
            }
        }
    }

    /* access modifiers changed from: private */
    public void onOverflowClicked(@NonNull TBActionView tBActionView) {
        if (tBActionView != null) {
            if (sOnOverflowButtonClickListeners != null) {
                Iterator<TBOnOverflowButtonClickListener> it = sOnOverflowButtonClickListeners.iterator();
                while (it.hasNext()) {
                    it.next().onOverflowButtonClicked();
                }
            }
            showPopupMenu(tBActionView);
        }
    }

    public void show() {
        showPopupMenu((TBActionView) null);
    }

    public void hide() {
        if (this.mPopupMenu != null) {
            this.mPopupMenu.dismiss();
        }
    }

    /* access modifiers changed from: protected */
    public void showPopupMenu(final TBActionView tBActionView) {
        int i;
        try {
            if (this.mPopupMenu == null) {
                View inflate = ((Activity) this.mActivity.get()).getLayoutInflater().inflate(R.layout.uik_public_menu_new, (ViewGroup) null);
                inflate.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
                inflate.measure(0, 0);
                this.mPopupMenu = new PopupWindow(inflate, -1, -2, true);
                if (Build.VERSION.SDK_INT >= 14) {
                    try {
                        Field declaredField = PopupWindow.class.getDeclaredField("mLayoutInScreen");
                        declaredField.setAccessible(true);
                        declaredField.set(this.mPopupMenu, true);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e2) {
                        e2.printStackTrace();
                    }
                }
                RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.uik_public_menu_content);
                recyclerView.setLayoutManager(new GridLayoutManager((Context) this.mActivity.get(), 4));
                recyclerView.addItemDecoration(new TBPublicMenuAdapter.GridSpacingItemDecoration(4, (int) ((Activity) this.mActivity.get()).getResources().getDimension(R.dimen.uik_public_menu_item_new_space), false));
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(this.mMenuAdapter);
                this.mMenuAdapter.setOnItemClickListener(new TBPublicMenuAdapter.OnItemClickListener() {
                    public void onItemClick(TBPublicMenuItem tBPublicMenuItem) {
                        TBPublicMenu.this.onPublicMenuClicked(tBPublicMenuItem);
                    }
                });
                TextView textView = (TextView) inflate.findViewById(R.id.uik_public_menu_recent);
                if (!TextUtils.isEmpty(this.mExtensionTitle)) {
                    textView.setText(this.mExtensionTitle);
                }
                RecyclerView recyclerView2 = (RecyclerView) inflate.findViewById(R.id.uik_public_menu_lite_content);
                recyclerView2.setLayoutManager(new LinearLayoutManager((Context) this.mActivity.get(), 0, false));
                recyclerView2.addItemDecoration(new TBLiteProgramAdapter.LiteItemDecoration((int) ((Activity) this.mActivity.get()).getResources().getDimension(R.dimen.uik_public_menu_lite_right)));
                recyclerView2.setHasFixedSize(true);
                recyclerView2.setAdapter(this.mLiteProgramAdapter);
                this.mLiteProgramAdapter.setOnItemClickListener(new TBLiteProgramAdapter.OnItemClickListener() {
                    public void onItemClick(View view, int i) {
                        if (TBPublicMenu.this.mExtensionMenu != null && TBPublicMenu.this.mExtensionMenu.size() > 0) {
                            TBPublicMenu.this.onPublicMenuClicked(TBPublicMenu.this.mExtensionMenu.get(i));
                        } else if (TBPublicMenu.sLitePrograms != null && TBPublicMenu.sLitePrograms.size() > 0) {
                            TBPublicMenu.this.onPublicMenuClicked(TBPublicMenu.sLitePrograms.get(i));
                        }
                    }
                });
                inflate.findViewById(R.id.uik_public_menu_close).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        TBPublicMenu.this.mPopupMenu.dismiss();
                    }
                });
            }
            if (!(this.mActivity == null || this.mActivity.get() == null || ((Activity) this.mActivity.get()).isFinishing() || this.mPopupMenu == null || this.mPopupMenu.isShowing())) {
                this.mPopupMenu.setAnimationStyle(R.style.TBPublicMenuPopupMenuAnim);
                this.mMenuAdapter.notifyDataSetChanged();
                this.mLiteProgramAdapter.notifyDataSetChanged();
                showRecentLiteProgram();
                this.mPopupMenu.getContentView().measure(0, 0);
                DisplayMetrics displayMetrics = new DisplayMetrics();
                ((Activity) this.mActivity.get()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int i2 = displayMetrics.widthPixels;
                if (this.mPopupMenu.getHeight() > 0) {
                    i = this.mPopupMenu.getHeight();
                } else {
                    i = this.mPopupMenu.getContentView().getMeasuredHeight();
                }
                if (Build.VERSION.SDK_INT >= 17) {
                    updateBlurView(i2, i);
                    if (this.mPopupMenu.getBackground() != null) {
                        this.mPopupMenu.getBackground().setBounds(0, 0, i2, i);
                    } else {
                        this.mPopupMenu.setBackgroundDrawable(((Activity) this.mActivity.get()).getResources().getDrawable(R.drawable.uik_public_menu_bg));
                        this.mPopupMenu.getBackground().setBounds(0, 0, i2, i);
                    }
                } else {
                    this.mPopupMenu.setBackgroundDrawable(((Activity) this.mActivity.get()).getResources().getDrawable(R.drawable.uik_public_menu_bg));
                    this.mPopupMenu.getBackground().setBounds(0, 0, i2, i);
                }
                this.mPopupMenu.showAtLocation(((Activity) this.mActivity.get()).getWindow().getDecorView(), 0, 0, 0);
                if (tBActionView != null) {
                    tBActionView.onMessageUpdate((TBPublicMenuItem) null);
                }
            }
            this.mPopupMenu.setOnDismissListener(new PopupWindow.OnDismissListener() {
                public void onDismiss() {
                    if (tBActionView != null) {
                        tBActionView.onMessageUpdate(TBPublicMenu.getPublicMenu(R.id.uik_menu_wangxin));
                    }
                }
            });
        } catch (WindowManager.BadTokenException unused) {
            Log.e(TAG, "Error displaying menu! Activity maybe not running!");
        }
    }

    /* access modifiers changed from: protected */
    public boolean showRecentLiteProgram() {
        if (this.mPopupMenu == null || this.mLiteProgramAdapter == null) {
            return false;
        }
        View findViewById = this.mPopupMenu.getContentView().findViewById(R.id.uik_public_menu_recent_rl);
        View findViewById2 = this.mPopupMenu.getContentView().findViewById(R.id.uik_public_menu_recent);
        View findViewById3 = this.mPopupMenu.getContentView().findViewById(R.id.uik_public_menu_lite_content);
        if (!this.mNewMenu || this.mLiteProgramAdapter.getItemCount() <= 0) {
            findViewById.setVisibility(8);
            findViewById3.setVisibility(8);
            findViewById2.setVisibility(8);
            return false;
        }
        findViewById.setVisibility(0);
        findViewById3.setVisibility(0);
        findViewById2.setVisibility(0);
        return true;
    }

    /* access modifiers changed from: protected */
    public void closePopupMenu() {
        if (this.mPopupMenu != null && this.mPopupMenu.isShowing()) {
            this.mPopupMenu.dismiss();
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v22, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v48, resolved type: com.taobao.uikit.actionbar.ITBPublicMenu} */
    /* JADX WARNING: type inference failed for: r3v0 */
    /* JADX WARNING: type inference failed for: r3v56 */
    /* JADX WARNING: type inference failed for: r3v57 */
    /* JADX WARNING: type inference failed for: r3v58 */
    /* access modifiers changed from: private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onPublicMenuClicked(com.taobao.uikit.actionbar.TBPublicMenuItem r8) {
        /*
            r7 = this;
            if (r8 != 0) goto L_0x0003
            return
        L_0x0003:
            r7.closePopupMenu()
            int r0 = r8.getId()
            int r1 = com.taobao.uikit.actionbar.R.id.uik_menu_wangxin
            r2 = 0
            if (r0 != r1) goto L_0x003e
            int r8 = com.taobao.uikit.actionbar.R.id.uik_menu_wangxin
            com.taobao.uikit.actionbar.TBPublicMenuItem r8 = getPublicMenu(r8)
            if (r8 == 0) goto L_0x0390
            com.taobao.statistic.CT r0 = com.taobao.statistic.CT.Button
            java.lang.String r1 = "wangwang"
            java.lang.String[] r2 = new java.lang.String[r2]
            com.taobao.statistic.TBS.Adv.ctrlClicked((com.taobao.statistic.CT) r0, (java.lang.String) r1, (java.lang.String[]) r2)
            java.lang.ref.WeakReference<android.app.Activity> r0 = r7.mActivity
            java.lang.Object r0 = r0.get()
            android.content.Context r0 = (android.content.Context) r0
            com.taobao.android.nav.Nav r0 = com.taobao.android.nav.Nav.from(r0)
            java.lang.String r1 = r8.getNavUrl()
            r0.toUri((java.lang.String) r1)
            com.taobao.uikit.actionbar.TBPublicMenu$TBOnPublicMenuClickListener r0 = r7.mDefaultPublicMenuListener
            if (r0 == 0) goto L_0x0390
            com.taobao.uikit.actionbar.TBPublicMenu$TBOnPublicMenuClickListener r0 = r7.mDefaultPublicMenuListener
            r0.onPublicMenuItemClicked(r8)
            goto L_0x0390
        L_0x003e:
            int r1 = com.taobao.uikit.actionbar.R.id.uik_menu_home
            if (r0 != r1) goto L_0x0071
            int r8 = com.taobao.uikit.actionbar.R.id.uik_menu_home
            com.taobao.uikit.actionbar.TBPublicMenuItem r8 = getPublicMenu(r8)
            if (r8 == 0) goto L_0x0390
            java.lang.ref.WeakReference<android.app.Activity> r0 = r7.mActivity
            java.lang.Object r0 = r0.get()
            android.content.Context r0 = (android.content.Context) r0
            com.taobao.android.nav.Nav r0 = com.taobao.android.nav.Nav.from(r0)
            java.lang.String r1 = r8.getNavUrl()
            r0.toUri((java.lang.String) r1)
            com.taobao.statistic.CT r0 = com.taobao.statistic.CT.Button
            java.lang.String r1 = "Home"
            java.lang.String[] r2 = new java.lang.String[r2]
            com.taobao.statistic.TBS.Adv.ctrlClicked((com.taobao.statistic.CT) r0, (java.lang.String) r1, (java.lang.String[]) r2)
            com.taobao.uikit.actionbar.TBPublicMenu$TBOnPublicMenuClickListener r0 = r7.mDefaultPublicMenuListener
            if (r0 == 0) goto L_0x0390
            com.taobao.uikit.actionbar.TBPublicMenu$TBOnPublicMenuClickListener r0 = r7.mDefaultPublicMenuListener
            r0.onPublicMenuItemClicked(r8)
            goto L_0x0390
        L_0x0071:
            int r1 = com.taobao.uikit.actionbar.R.id.uik_menu_service
            r3 = 0
            if (r0 != r1) goto L_0x00dc
            java.lang.ref.WeakReference<android.app.Activity> r8 = r7.mActivity
            java.lang.Object r8 = r8.get()
            android.app.Activity r8 = (android.app.Activity) r8
            if (r8 == 0) goto L_0x0390
            boolean r0 = r8 instanceof com.taobao.uikit.actionbar.ITBPublicMenu
            if (r0 == 0) goto L_0x0088
            r3 = r8
            com.taobao.uikit.actionbar.ITBPublicMenu r3 = (com.taobao.uikit.actionbar.ITBPublicMenu) r3
            goto L_0x008e
        L_0x0088:
            com.taobao.uikit.actionbar.ITBPublicMenu r0 = r7.mItbPublicMenu
            if (r0 == 0) goto L_0x008e
            com.taobao.uikit.actionbar.ITBPublicMenu r3 = r7.mItbPublicMenu
        L_0x008e:
            if (r3 != 0) goto L_0x0091
            return
        L_0x0091:
            android.os.Bundle r0 = new android.os.Bundle
            r0.<init>()
            int r1 = com.taobao.uikit.actionbar.R.id.uik_menu_service
            com.taobao.uikit.actionbar.TBPublicMenuItem r1 = getPublicMenu(r1)
            if (r1 != 0) goto L_0x009f
            return
        L_0x009f:
            java.lang.String r4 = r1.getNavUrl()
            java.lang.String r5 = "H5Data"
            android.os.Bundle r6 = r3.pageUserInfo()
            if (r6 != 0) goto L_0x00b2
            android.os.Bundle r6 = r7.mDefaultPageUserInfo
            if (r6 == 0) goto L_0x00b2
            android.os.Bundle r3 = r7.mDefaultPageUserInfo
            goto L_0x00b6
        L_0x00b2:
            android.os.Bundle r3 = r3.pageUserInfo()
        L_0x00b6:
            r0.putBundle(r5, r3)
            java.lang.String r3 = r7.getAssembledUrl(r4)
            com.taobao.android.nav.Nav r8 = com.taobao.android.nav.Nav.from(r8)
            com.taobao.android.nav.Nav r8 = r8.withExtras(r0)
            r8.toUri((java.lang.String) r3)
            com.taobao.statistic.CT r8 = com.taobao.statistic.CT.Button
            java.lang.String r0 = "handService"
            java.lang.String[] r2 = new java.lang.String[r2]
            com.taobao.statistic.TBS.Adv.ctrlClicked((com.taobao.statistic.CT) r8, (java.lang.String) r0, (java.lang.String[]) r2)
            com.taobao.uikit.actionbar.TBPublicMenu$TBOnPublicMenuClickListener r8 = r7.mDefaultPublicMenuListener
            if (r8 == 0) goto L_0x0390
            com.taobao.uikit.actionbar.TBPublicMenu$TBOnPublicMenuClickListener r8 = r7.mDefaultPublicMenuListener
            r8.onPublicMenuItemClicked(r1)
            goto L_0x0390
        L_0x00dc:
            int r1 = com.taobao.uikit.actionbar.R.id.uik_menu_feedback
            if (r0 != r1) goto L_0x0265
            java.lang.ref.WeakReference<android.app.Activity> r8 = r7.mActivity
            java.lang.Object r8 = r8.get()
            android.app.Activity r8 = (android.app.Activity) r8
            if (r8 == 0) goto L_0x0390
            int r0 = com.taobao.uikit.actionbar.R.id.uik_menu_feedback
            com.taobao.uikit.actionbar.TBPublicMenuItem r0 = getPublicMenu(r0)
            if (r0 != 0) goto L_0x00f3
            return
        L_0x00f3:
            android.os.Bundle r1 = new android.os.Bundle
            r1.<init>()
            boolean r4 = r8 instanceof com.taobao.uikit.actionbar.ITBPublicMenu
            if (r4 == 0) goto L_0x0100
            r4 = r8
            com.taobao.uikit.actionbar.ITBPublicMenu r4 = (com.taobao.uikit.actionbar.ITBPublicMenu) r4
            goto L_0x0108
        L_0x0100:
            com.taobao.uikit.actionbar.ITBPublicMenu r4 = r7.mItbPublicMenu
            if (r4 == 0) goto L_0x0107
            com.taobao.uikit.actionbar.ITBPublicMenu r4 = r7.mItbPublicMenu
            goto L_0x0108
        L_0x0107:
            r4 = r3
        L_0x0108:
            if (r4 == 0) goto L_0x0242
            android.os.Bundle r4 = r4.pageUserInfo()
            if (r4 != 0) goto L_0x0116
            android.os.Bundle r5 = r7.mDefaultPageUserInfo
            if (r5 == 0) goto L_0x0116
            android.os.Bundle r4 = r7.mDefaultPageUserInfo
        L_0x0116:
            java.lang.String r5 = "H5Data"
            r1.putBundle(r5, r4)
            java.lang.String r5 = r0.getNavUrl()
            java.lang.String r5 = r7.getAssembledUrl(r5)
            boolean r6 = android.text.TextUtils.isEmpty(r5)
            if (r6 == 0) goto L_0x012a
            return
        L_0x012a:
            if (r4 == 0) goto L_0x0140
            java.lang.String r6 = "ZSUserHelper"
            android.os.Bundle r6 = r4.getBundle(r6)
            if (r6 == 0) goto L_0x0140
            java.lang.String r3 = "ZSUserHelper"
            android.os.Bundle r3 = r4.getBundle(r3)
            java.lang.String r4 = "_f"
            java.lang.String r3 = r3.getString(r4)
        L_0x0140:
            boolean r4 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Exception -> 0x0212 }
            r6 = -1
            if (r4 != 0) goto L_0x017e
            java.lang.String r4 = "?"
            int r4 = r5.indexOf(r4)     // Catch:{ Exception -> 0x0212 }
            if (r4 == r6) goto L_0x0167
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0212 }
            r4.<init>(r5)     // Catch:{ Exception -> 0x0212 }
            java.lang.String r5 = "&_f="
            r4.append(r5)     // Catch:{ Exception -> 0x0212 }
            java.lang.String r5 = "utf-8"
            java.lang.String r3 = java.net.URLEncoder.encode(r3, r5)     // Catch:{ Exception -> 0x0212 }
            r4.append(r3)     // Catch:{ Exception -> 0x0212 }
            java.lang.String r5 = r4.toString()     // Catch:{ Exception -> 0x0212 }
            goto L_0x017e
        L_0x0167:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0212 }
            r4.<init>(r5)     // Catch:{ Exception -> 0x0212 }
            java.lang.String r5 = "?_f="
            r4.append(r5)     // Catch:{ Exception -> 0x0212 }
            java.lang.String r5 = "utf-8"
            java.lang.String r3 = java.net.URLEncoder.encode(r3, r5)     // Catch:{ Exception -> 0x0212 }
            r4.append(r3)     // Catch:{ Exception -> 0x0212 }
            java.lang.String r5 = r4.toString()     // Catch:{ Exception -> 0x0212 }
        L_0x017e:
            android.content.Intent r3 = r8.getIntent()     // Catch:{ Exception -> 0x0212 }
            java.lang.String r4 = "?"
            int r4 = r5.indexOf(r4)     // Catch:{ Exception -> 0x0212 }
            if (r4 == r6) goto L_0x01ce
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0212 }
            r4.<init>(r5)     // Catch:{ Exception -> 0x0212 }
            java.lang.String r5 = "&from_page="
            r4.append(r5)     // Catch:{ Exception -> 0x0212 }
            android.content.ComponentName r5 = r8.getComponentName()     // Catch:{ Exception -> 0x0212 }
            java.lang.String r5 = r5.getShortClassName()     // Catch:{ Exception -> 0x0212 }
            java.lang.String r6 = "utf-8"
            java.lang.String r5 = java.net.URLEncoder.encode(r5, r6)     // Catch:{ Exception -> 0x0212 }
            r4.append(r5)     // Catch:{ Exception -> 0x0212 }
            java.lang.String r5 = "&from_url="
            r4.append(r5)     // Catch:{ Exception -> 0x0212 }
            if (r3 != 0) goto L_0x01af
            java.lang.String r3 = ""
            goto L_0x01c6
        L_0x01af:
            android.net.Uri r5 = r3.getData()     // Catch:{ Exception -> 0x0212 }
            if (r5 != 0) goto L_0x01b8
            java.lang.String r3 = ""
            goto L_0x01c0
        L_0x01b8:
            android.net.Uri r3 = r3.getData()     // Catch:{ Exception -> 0x0212 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0212 }
        L_0x01c0:
            java.lang.String r5 = "utf-8"
            java.lang.String r3 = java.net.URLEncoder.encode(r3, r5)     // Catch:{ Exception -> 0x0212 }
        L_0x01c6:
            r4.append(r3)     // Catch:{ Exception -> 0x0212 }
            java.lang.String r3 = r4.toString()     // Catch:{ Exception -> 0x0212 }
            goto L_0x0223
        L_0x01ce:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0212 }
            r4.<init>(r5)     // Catch:{ Exception -> 0x0212 }
            java.lang.String r5 = "?from_page="
            r4.append(r5)     // Catch:{ Exception -> 0x0212 }
            android.content.ComponentName r5 = r8.getComponentName()     // Catch:{ Exception -> 0x0212 }
            java.lang.String r5 = r5.getShortClassName()     // Catch:{ Exception -> 0x0212 }
            java.lang.String r6 = "utf-8"
            java.lang.String r5 = java.net.URLEncoder.encode(r5, r6)     // Catch:{ Exception -> 0x0212 }
            r4.append(r5)     // Catch:{ Exception -> 0x0212 }
            java.lang.String r5 = "&from_url="
            r4.append(r5)     // Catch:{ Exception -> 0x0212 }
            if (r3 != 0) goto L_0x01f3
            java.lang.String r3 = ""
            goto L_0x020a
        L_0x01f3:
            android.net.Uri r5 = r3.getData()     // Catch:{ Exception -> 0x0212 }
            if (r5 != 0) goto L_0x01fc
            java.lang.String r3 = ""
            goto L_0x0204
        L_0x01fc:
            android.net.Uri r3 = r3.getData()     // Catch:{ Exception -> 0x0212 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0212 }
        L_0x0204:
            java.lang.String r5 = "utf-8"
            java.lang.String r3 = java.net.URLEncoder.encode(r3, r5)     // Catch:{ Exception -> 0x0212 }
        L_0x020a:
            r4.append(r3)     // Catch:{ Exception -> 0x0212 }
            java.lang.String r3 = r4.toString()     // Catch:{ Exception -> 0x0212 }
            goto L_0x0223
        L_0x0212:
            r3 = move-exception
            java.lang.String r4 = r0.getNavUrl()
            java.lang.String r4 = r7.getAssembledUrl(r4)
            java.lang.String r4 = r7.getAssembledUrl(r4)
            r3.printStackTrace()
            r3 = r4
        L_0x0223:
            com.taobao.android.nav.Nav r8 = com.taobao.android.nav.Nav.from(r8)
            com.taobao.android.nav.Nav r8 = r8.withExtras(r1)
            r8.toUri((java.lang.String) r3)
            com.taobao.statistic.CT r8 = com.taobao.statistic.CT.Button
            java.lang.String r1 = "Feedback"
            java.lang.String[] r2 = new java.lang.String[r2]
            com.taobao.statistic.TBS.Adv.ctrlClicked((com.taobao.statistic.CT) r8, (java.lang.String) r1, (java.lang.String[]) r2)
            com.taobao.uikit.actionbar.TBPublicMenu$TBOnPublicMenuClickListener r8 = r7.mDefaultPublicMenuListener
            if (r8 == 0) goto L_0x0390
            com.taobao.uikit.actionbar.TBPublicMenu$TBOnPublicMenuClickListener r8 = r7.mDefaultPublicMenuListener
            r8.onPublicMenuItemClicked(r0)
            goto L_0x0390
        L_0x0242:
            com.taobao.android.nav.Nav r8 = com.taobao.android.nav.Nav.from(r8)
            com.taobao.android.nav.Nav r8 = r8.withExtras(r1)
            java.lang.String r1 = r0.getNavUrl()
            r8.toUri((java.lang.String) r1)
            com.taobao.statistic.CT r8 = com.taobao.statistic.CT.Button
            java.lang.String r1 = "Feedback"
            java.lang.String[] r2 = new java.lang.String[r2]
            com.taobao.statistic.TBS.Adv.ctrlClicked((com.taobao.statistic.CT) r8, (java.lang.String) r1, (java.lang.String[]) r2)
            com.taobao.uikit.actionbar.TBPublicMenu$TBOnPublicMenuClickListener r8 = r7.mDefaultPublicMenuListener
            if (r8 == 0) goto L_0x0390
            com.taobao.uikit.actionbar.TBPublicMenu$TBOnPublicMenuClickListener r8 = r7.mDefaultPublicMenuListener
            r8.onPublicMenuItemClicked(r0)
            goto L_0x0390
        L_0x0265:
            java.util.ArrayList<com.taobao.uikit.actionbar.TBPublicMenuItem> r1 = r7.mExtensionMenu
            if (r1 == 0) goto L_0x0290
            java.util.ArrayList<com.taobao.uikit.actionbar.TBPublicMenuItem> r1 = r7.mExtensionMenu
            boolean r1 = r1.contains(r8)
            if (r1 == 0) goto L_0x0290
            java.lang.String r0 = r8.getNavUrl()
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x0390
            java.lang.ref.WeakReference<android.app.Activity> r0 = r7.mActivity
            java.lang.Object r0 = r0.get()
            android.content.Context r0 = (android.content.Context) r0
            com.taobao.android.nav.Nav r0 = com.taobao.android.nav.Nav.from(r0)
            java.lang.String r8 = r8.getNavUrl()
            r0.toUri((java.lang.String) r8)
            goto L_0x0390
        L_0x0290:
            java.util.ArrayList<com.taobao.uikit.actionbar.TBPublicMenuItem> r1 = sLitePrograms
            if (r1 == 0) goto L_0x02cc
            java.util.ArrayList<com.taobao.uikit.actionbar.TBPublicMenuItem> r1 = sLitePrograms
            boolean r1 = r1.contains(r8)
            if (r1 == 0) goto L_0x02cc
            java.lang.String r0 = r8.getNavUrl()
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x02b9
            java.lang.ref.WeakReference<android.app.Activity> r0 = r7.mActivity
            java.lang.Object r0 = r0.get()
            android.content.Context r0 = (android.content.Context) r0
            com.taobao.android.nav.Nav r0 = com.taobao.android.nav.Nav.from(r0)
            java.lang.String r1 = r8.getNavUrl()
            r0.toUri((java.lang.String) r1)
        L_0x02b9:
            com.taobao.uikit.actionbar.TBPublicMenu$TBOnLiteProgramClickListener r0 = sOnLiteProgramClickListener
            if (r0 == 0) goto L_0x0390
            com.taobao.uikit.actionbar.TBPublicMenu$TBOnLiteProgramClickListener r0 = sOnLiteProgramClickListener
            java.lang.ref.WeakReference<android.app.Activity> r1 = r7.mActivity
            java.lang.Object r1 = r1.get()
            android.content.Context r1 = (android.content.Context) r1
            r0.onLiteProgramClicked(r1, r8)
            goto L_0x0390
        L_0x02cc:
            com.taobao.uikit.actionbar.TBPublicMenu$TBOnPublicMenuClickListener r1 = sOnPublicMenuClickListener
            if (r1 == 0) goto L_0x02d5
            com.taobao.uikit.actionbar.TBPublicMenu$TBOnPublicMenuClickListener r1 = sOnPublicMenuClickListener
            r1.onPublicMenuItemClicked(r8)
        L_0x02d5:
            com.taobao.uikit.actionbar.TBPublicMenu$TBOnPublicMenuClickListener r1 = r7.mOnCustomPublicMenuClickListener
            if (r1 == 0) goto L_0x02de
            com.taobao.uikit.actionbar.TBPublicMenu$TBOnPublicMenuClickListener r1 = r7.mOnCustomPublicMenuClickListener
            r1.onPublicMenuItemClicked(r8)
        L_0x02de:
            r1 = 0
        L_0x02df:
            java.util.ArrayList<androidx.appcompat.view.menu.MenuItemImpl> r3 = r7.mMenuItems
            int r3 = r3.size()
            if (r1 >= r3) goto L_0x0337
            java.util.ArrayList<androidx.appcompat.view.menu.MenuItemImpl> r3 = r7.mMenuItems
            java.lang.Object r3 = r3.get(r1)
            if (r3 == 0) goto L_0x0334
            java.util.ArrayList<androidx.appcompat.view.menu.MenuItemImpl> r3 = r7.mMenuItems
            java.lang.Object r3 = r3.get(r1)
            androidx.appcompat.view.menu.MenuItemImpl r3 = (androidx.appcompat.view.menu.MenuItemImpl) r3
            int r3 = r3.getItemId()
            if (r0 != r3) goto L_0x0334
            java.lang.String r3 = r8.getTitle()
            java.util.ArrayList<androidx.appcompat.view.menu.MenuItemImpl> r4 = r7.mMenuItems
            java.lang.Object r4 = r4.get(r1)
            androidx.appcompat.view.menu.MenuItemImpl r4 = (androidx.appcompat.view.menu.MenuItemImpl) r4
            java.lang.CharSequence r4 = r4.getTitle()
            boolean r3 = android.text.TextUtils.equals(r3, r4)
            if (r3 == 0) goto L_0x0334
            java.util.ArrayList<androidx.appcompat.view.menu.MenuItemImpl> r3 = r7.mMenuItems
            java.lang.Object r3 = r3.get(r1)
            androidx.appcompat.view.menu.MenuItemImpl r3 = (androidx.appcompat.view.menu.MenuItemImpl) r3
            boolean r3 = r3.invoke()
            if (r3 != 0) goto L_0x0334
            java.lang.ref.WeakReference<android.app.Activity> r3 = r7.mActivity
            java.lang.Object r3 = r3.get()
            android.app.Activity r3 = (android.app.Activity) r3
            java.util.ArrayList<androidx.appcompat.view.menu.MenuItemImpl> r4 = r7.mMenuItems
            java.lang.Object r4 = r4.get(r1)
            android.view.MenuItem r4 = (android.view.MenuItem) r4
            r3.onOptionsItemSelected(r4)
        L_0x0334:
            int r1 = r1 + 1
            goto L_0x02df
        L_0x0337:
            java.util.ArrayList<androidx.appcompat.view.menu.MenuItemImpl> r1 = r7.mFilteredMenus
            int r1 = r1.size()
            if (r2 >= r1) goto L_0x0390
            java.util.ArrayList<androidx.appcompat.view.menu.MenuItemImpl> r1 = r7.mFilteredMenus
            java.lang.Object r1 = r1.get(r2)
            if (r1 == 0) goto L_0x038d
            java.util.ArrayList<androidx.appcompat.view.menu.MenuItemImpl> r1 = r7.mFilteredMenus
            java.lang.Object r1 = r1.get(r2)
            androidx.appcompat.view.menu.MenuItemImpl r1 = (androidx.appcompat.view.menu.MenuItemImpl) r1
            int r1 = r1.getItemId()
            if (r0 != r1) goto L_0x038d
            java.lang.String r1 = r8.getTitle()
            java.util.ArrayList<androidx.appcompat.view.menu.MenuItemImpl> r3 = r7.mFilteredMenus
            java.lang.Object r3 = r3.get(r2)
            androidx.appcompat.view.menu.MenuItemImpl r3 = (androidx.appcompat.view.menu.MenuItemImpl) r3
            java.lang.CharSequence r3 = r3.getTitle()
            boolean r1 = android.text.TextUtils.equals(r1, r3)
            if (r1 == 0) goto L_0x038d
            java.util.ArrayList<androidx.appcompat.view.menu.MenuItemImpl> r8 = r7.mFilteredMenus
            java.lang.Object r8 = r8.get(r2)
            androidx.appcompat.view.menu.MenuItemImpl r8 = (androidx.appcompat.view.menu.MenuItemImpl) r8
            boolean r8 = r8.invoke()
            if (r8 != 0) goto L_0x038c
            java.lang.ref.WeakReference<android.app.Activity> r8 = r7.mActivity
            java.lang.Object r8 = r8.get()
            android.app.Activity r8 = (android.app.Activity) r8
            java.util.ArrayList<androidx.appcompat.view.menu.MenuItemImpl> r0 = r7.mFilteredMenus
            java.lang.Object r0 = r0.get(r2)
            android.view.MenuItem r0 = (android.view.MenuItem) r0
            r8.onOptionsItemSelected(r0)
        L_0x038c:
            return
        L_0x038d:
            int r2 = r2 + 1
            goto L_0x0337
        L_0x0390:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.uikit.actionbar.TBPublicMenu.onPublicMenuClicked(com.taobao.uikit.actionbar.TBPublicMenuItem):void");
    }

    public TBPublicMenuItem getExtraMenu(int i) {
        for (int i2 = 0; i2 < this.mExtraMenus.size(); i2++) {
            TBPublicMenuItem tBPublicMenuItem = this.mExtraMenus.get(i2);
            if (tBPublicMenuItem != null && tBPublicMenuItem.getId() == i) {
                return tBPublicMenuItem;
            }
        }
        return null;
    }

    public static TBPublicMenuItem getPublicMenu(int i) {
        init();
        for (int i2 = 0; i2 < sPublicMenus.size(); i2++) {
            TBPublicMenuItem tBPublicMenuItem = sPublicMenus.get(i2);
            if (tBPublicMenuItem != null && tBPublicMenuItem.getId() == i) {
                return tBPublicMenuItem;
            }
        }
        return null;
    }

    public static void removePublicMenu(int i) {
        removePublicMenu(i, false);
    }

    public static void removePublicMenu(int i, boolean z) {
        int i2 = 0;
        int i3 = 0;
        while (i3 < sPublicMenus.size()) {
            TBPublicMenuItem tBPublicMenuItem = sPublicMenus.get(i3);
            if (tBPublicMenuItem == null || tBPublicMenuItem.getId() != i) {
                i3++;
            } else {
                sPublicMenus.remove(i3);
                if (z) {
                    while (true) {
                        if (i2 < sDefaultPublicMenus.size()) {
                            TBPublicMenuItem tBPublicMenuItem2 = sDefaultPublicMenus.get(i2);
                            if (tBPublicMenuItem2 != null && tBPublicMenuItem2.getId() == i) {
                                sDefaultPublicMenus.remove(i2);
                                break;
                            }
                            i2++;
                        } else {
                            break;
                        }
                    }
                }
                sReset = true;
                if (sCurrentPublicMenu != null) {
                    sCurrentPublicMenu.updateMenuData();
                    return;
                }
                return;
            }
        }
    }

    public TBPublicMenuItem getCustomMenu(int i) {
        if (this.mExtraMenus == null) {
            return null;
        }
        for (int i2 = 0; i2 < this.mExtraMenus.size(); i2++) {
            if (this.mExtraMenus.get(i2).getId() == i) {
                return this.mExtraMenus.get(i2);
            }
        }
        return null;
    }

    private String getAssembledUrl(String str) {
        ITBPublicMenu iTBPublicMenu;
        String str2 = null;
        if (this.mActivity == null || this.mActivity.get() == null) {
            return null;
        }
        Activity activity = (Activity) this.mActivity.get();
        if (activity instanceof ITBPublicMenu) {
            iTBPublicMenu = (ITBPublicMenu) activity;
        } else {
            iTBPublicMenu = this.mItbPublicMenu != null ? this.mItbPublicMenu : null;
        }
        if (iTBPublicMenu == null) {
            return null;
        }
        Bundle bundle = new Bundle();
        bundle.putBundle("H5Data", (iTBPublicMenu.pageUserInfo() != null || this.mDefaultPageUserInfo == null) ? iTBPublicMenu.pageUserInfo() : this.mDefaultPageUserInfo);
        StringBuilder sb = new StringBuilder(str);
        try {
            if (assembleUrlString(bundle) != null) {
                str2 = URLEncoder.encode(assembleUrlString(bundle).toString(), "utf-8");
            }
        } catch (UnsupportedEncodingException unused) {
        }
        if (str2 != null) {
            if (str.indexOf(63) != -1) {
                sb.append("&args=");
                sb.append(str2);
            } else {
                sb.append("?args=");
                sb.append(str2);
            }
        }
        return sb.toString();
    }

    private JSONObject assembleUrlString(Bundle bundle) {
        JSONObject jSONObject = new JSONObject();
        for (String str : bundle.keySet()) {
            try {
                if (bundle.get(str) instanceof Bundle) {
                    jSONObject.put(str, assembleUrlString((Bundle) bundle.get(str)));
                } else {
                    jSONObject.put(str, bundle.get(str));
                }
            } catch (JSONException unused) {
                return null;
            }
        }
        return jSONObject;
    }

    public void setActionViewIconColor(@ColorInt int i) {
        this.mMenuIconColor = i;
        for (int i2 = 0; i2 < this.mMenuItems.size(); i2++) {
            if (this.mMenuItems.get(i2) != null && this.mMenuItems.get(i2).getActionView() != null && (this.mMenuItems.get(i2).getActionView() instanceof TBActionView)) {
                ((TBActionView) this.mMenuItems.get(i2).getActionView()).setIconColor(i);
            } else if (this.mMenuItems.get(i2).getActionView() != null && (this.mMenuItems.get(i2).getActionView() instanceof TextView)) {
                ((TextView) this.mMenuItems.get(i2).getActionView()).setTextColor(i);
            }
        }
        if (this.mCustomOverflow != null) {
            this.mCustomOverflow.setIconColor(this.mMenuIconColor);
        }
    }

    @Deprecated
    public static void updatePublicMenu(int i, TBPublicMenuItem tBPublicMenuItem) {
        updatePublicMenu(tBPublicMenuItem, true);
    }

    public static void updatePublicMenu(TBPublicMenuItem tBPublicMenuItem) {
        updatePublicMenu(tBPublicMenuItem, true);
    }

    public static void updatePublicMenu(TBPublicMenuItem tBPublicMenuItem, boolean z) {
        if (tBPublicMenuItem != null && getPublicMenu(tBPublicMenuItem.getId()) != null && tBPublicMenuItem.checkValidation()) {
            int i = 0;
            int i2 = 0;
            while (i2 < sPublicMenus.size()) {
                TBPublicMenuItem tBPublicMenuItem2 = sPublicMenus.get(i2);
                if (tBPublicMenuItem2 == null || tBPublicMenuItem2.getId() != tBPublicMenuItem.getId()) {
                    i2++;
                } else {
                    sPublicMenus.remove(tBPublicMenuItem2);
                    sPublicMenus.add(i2, tBPublicMenuItem);
                    if (z) {
                        while (true) {
                            if (i < sDefaultPublicMenus.size()) {
                                TBPublicMenuItem tBPublicMenuItem3 = sDefaultPublicMenus.get(i);
                                if (tBPublicMenuItem3 != null && tBPublicMenuItem3.getId() == tBPublicMenuItem.getId()) {
                                    sDefaultPublicMenus.remove(tBPublicMenuItem3);
                                    sDefaultPublicMenus.add(i, tBPublicMenuItem);
                                    break;
                                }
                                i++;
                            } else {
                                break;
                            }
                        }
                    }
                    sReset = true;
                    if (sCurrentPublicMenu != null) {
                        sCurrentPublicMenu.updateMenuData();
                        return;
                    }
                    return;
                }
            }
            if (sCurrentPublicMenu != null) {
                sCurrentPublicMenu.updateMenuData();
            }
        }
    }

    public static void init(ArrayList<TBPublicMenuItem> arrayList) {
        if (arrayList != null) {
            sPublicMenus.clear();
            sDefaultPublicMenus.clear();
            sDefaultPublicMenus.addAll(arrayList);
            sReset = true;
        }
    }

    private void updateMenuData() {
        notifyMenuChanged();
        if (this.mCustomOverflow != null) {
            this.mCustomOverflow.onMessageUpdate(getPublicMenu(R.id.uik_menu_wangxin));
        }
    }

    public static void addOnOverflowButtonClickListener(TBOnOverflowButtonClickListener tBOnOverflowButtonClickListener) {
        sOnOverflowButtonClickListeners.add(tBOnOverflowButtonClickListener);
    }

    public static void setOnPublicMenuClickListener(TBOnPublicMenuClickListener tBOnPublicMenuClickListener) {
        sOnPublicMenuClickListener = tBOnPublicMenuClickListener;
    }

    @Deprecated
    public static void setOnLiteProgramClickListener(TBOnLiteProgramClickListener tBOnLiteProgramClickListener) {
        sOnLiteProgramClickListener = tBOnLiteProgramClickListener;
    }

    public void setDefaultPublicMenuClickListener(TBOnPublicMenuClickListener tBOnPublicMenuClickListener) {
        this.mDefaultPublicMenuListener = tBOnPublicMenuClickListener;
    }
}
