package com.alimama.moon.ui;

import alimama.com.unwlottiedialog.LottieData;
import alimama.com.unwlottiedialog.LottieDialogCallback;
import alimama.com.unwlottiedialog.UNWLottieDialog;
import alimama.com.unwviewbase.abstractview.UNWAbstractDialog;
import alimama.com.unwviewbase.marketController.UNWDialogController;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.LifecycleRegistryOwner;
import com.ali.user.mobile.login.model.LoginConstant;
import com.alibaba.android.umbrella.utils.UmbrellaConstants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alimama.moon.App;
import com.alimama.moon.R;
import com.alimama.moon.config.MoonConfigCenter;
import com.alimama.moon.config.OrangeConfigCenterManager;
import com.alimama.moon.dao.SettingManager;
import com.alimama.moon.dinamicx.event.CloseBulletinBoardEvent;
import com.alimama.moon.dinamicx.model.BulletinBoardModel;
import com.alimama.moon.dinamicx.util.BulletinBoardManager;
import com.alimama.moon.features.home.HomeFragment;
import com.alimama.moon.features.home.HomeUtil;
import com.alimama.moon.features.home.item.Constants;
import com.alimama.moon.features.home.item.HomeApprenticeItem;
import com.alimama.moon.features.home.item.HomeApprenticeTitleItem;
import com.alimama.moon.features.home.item.HomeBannerItem;
import com.alimama.moon.features.home.item.HomeCardItem;
import com.alimama.moon.features.home.item.HomeCircleNavItem;
import com.alimama.moon.features.home.item.HomeCommonTabItem;
import com.alimama.moon.features.home.item.HomeFlashSaleBlock;
import com.alimama.moon.features.home.item.HomeRecommendItem;
import com.alimama.moon.features.home.item.HomeRecommendTitleItem;
import com.alimama.moon.features.home.item.HomeResourcePlaceItem;
import com.alimama.moon.features.home.item.HomeSaleBlockItem;
import com.alimama.moon.features.home.theme.HomeThemeDataItem;
import com.alimama.moon.features.home.theme.HomeThemeDataManager;
import com.alimama.moon.features.home.view.BottomNavItemView;
import com.alimama.moon.features.home.view.IBottomNavItem;
import com.alimama.moon.features.home.viewholder.HomeApprenticeAreaTitleViewHolder;
import com.alimama.moon.features.home.viewholder.HomeApprenticeAreaViewHolder;
import com.alimama.moon.features.home.viewholder.HomeBannerViewHolder;
import com.alimama.moon.features.home.viewholder.HomeCardViewHolder;
import com.alimama.moon.features.home.viewholder.HomeCircleNavViewHolder;
import com.alimama.moon.features.home.viewholder.HomeCommonItemViewHolder;
import com.alimama.moon.features.home.viewholder.HomeFlashSaleViewHolder;
import com.alimama.moon.features.home.viewholder.HomeRecommendTitleViewHolder;
import com.alimama.moon.features.home.viewholder.HomeRecommendViewHolder;
import com.alimama.moon.features.home.viewholder.HomeResourcePlaceViewHolder;
import com.alimama.moon.features.home.viewholder.HomeSaleBlockViewHolder;
import com.alimama.moon.features.search.SearchInputPlaceholderView;
import com.alimama.moon.features.search.network.SearchHotTagEvent;
import com.alimama.moon.features.search.network.SearchHotTagRequest;
import com.alimama.moon.init.UpdateUtils;
import com.alimama.moon.service.BeanContext;
import com.alimama.moon.ui.IBottomNavContract;
import com.alimama.moon.ui.fragment.DiscoveryFragment;
import com.alimama.moon.update.NewVersionForceUpdateEvent;
import com.alimama.moon.update.NewVersionRemindEvent;
import com.alimama.moon.update.UpdateActivity;
import com.alimama.moon.update.UpdateCenter;
import com.alimama.moon.update.UpdateRemindDialog;
import com.alimama.moon.usertrack.BottomNavUTHelper;
import com.alimama.moon.usertrack.DXUTHelper;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.moon.utils.ActivityUtil;
import com.alimama.moon.utils.StatusBarUtils;
import com.alimama.moon.web.MoonJSBridge;
import com.alimama.union.app.aalogin.ILogin;
import com.alimama.union.app.configcenter.ConfigKeyList;
import com.alimama.union.app.logger.BusinessMonitorLogger;
import com.alimama.union.app.messagelist.CommonFooterProcess;
import com.alimama.union.app.messagelist.MessageListItemViewHolder;
import com.alimama.union.app.messagelist.network.MessageListCountRequest;
import com.alimama.union.app.messagelist.network.MessageListCountResponse;
import com.alimama.union.app.messagelist.network.MessageListItem;
import com.alimama.union.app.pagerouter.AppPageInfo;
import com.alimama.union.app.pagerouter.MoonComponentManager;
import com.alimama.union.app.rxnetwork.RxMtopRequest;
import com.alimama.union.app.rxnetwork.RxMtopResponse;
import com.alimama.union.app.sceneCenter.holder.SceneTabItemViewHolder;
import com.alimama.union.app.sceneCenter.item.SceneTabItem;
import com.alimama.union.app.taotokenConvert.TaoCodeDialogEvent;
import com.alimama.unionwl.utils.LocalDisplay;
import com.alimama.unwdinamicxcontainer.model.dxengine.DXEngineDataModel;
import com.alimama.unwdinamicxcontainer.presenter.dxengine.IDXEnginePresenter;
import com.alimama.unwdinamicxcontainer.presenter.dxengine.UNWDinamicXEngine;
import com.alimamaunion.base.configcenter.EtaoConfigCenter;
import com.alimamaunion.base.safejson.SafeJSONObject;
import com.alimamaunion.common.listpage.CommonBaseItem;
import com.alimamaunion.common.listpage.CommonItemFactory;
import com.alimamaunion.common.listpage.CommonItemInfo;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.uc.webview.export.media.MessageID;
import com.ut.mini.UTAnalytics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BottomNavActivity extends BaseActivity implements IBottomNavItem, IBottomNavContract.IBottomNavView, LifecycleRegistryOwner, View.OnClickListener {
    private static int COMMON_TYPE_BASE = 0;
    private static final String TAG = "BottomNavActivity";
    public static IBottomNavFragment currentFragment;
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) BottomNavActivity.class);
    public static int mCurrentIndex = 0;
    public static boolean sIsSwitchMidH5Tab = true;
    public static int sPageCount = 5;
    /* access modifiers changed from: private */
    public BottomNavFragmentAdapter adapter;
    private LifecycleRegistry lifecycleRegistry;
    @Inject
    ILogin login;
    private LinearLayout mBottomNavContainer;
    private List<BottomNavItemView> mBottomNavItemList;
    private BulletinBoardModel mBulletinBoardModel;
    /* access modifiers changed from: private */
    public LinearLayout mDXBulletinBoard;
    /* access modifiers changed from: private */
    public int mDisplayedUnreadMessageCount = 0;
    private RelativeLayout mHomeContainer;
    private int mItemHeight;
    private LinearLayout.LayoutParams mItemParams;
    private int mItemWidth;
    private ImageView mMessageIconView;
    private TextView mMessageNumTextView;
    private SearchInputPlaceholderView mSearchContainer;
    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private UNWDinamicXEngine mUNWDinamicXEngine;
    private final Handler mUiHandler = new Handler(Looper.getMainLooper());
    private IBottomNavContract.IBottomNavPresenter presenter;
    private boolean taoCodeDialogShow = false;
    @Inject
    UpdateCenter updateCenter;
    private AHBottomNavigationViewPager viewPager;

    static {
        int i = COMMON_TYPE_BASE;
        COMMON_TYPE_BASE = i + 1;
        CommonItemFactory.registItem(new CommonItemInfo("scene_banner", i, SceneTabItem.class, SceneTabItemViewHolder.class, 0, 10));
        int i2 = COMMON_TYPE_BASE;
        COMMON_TYPE_BASE = i2 + 1;
        CommonItemFactory.registItem(new CommonItemInfo("message_center", i2, MessageListItem.class, MessageListItemViewHolder.class, 0, 20));
        int i3 = COMMON_TYPE_BASE;
        COMMON_TYPE_BASE = i3 + 1;
        CommonItemFactory.registItem(new CommonItemInfo("union_sales_card", i3, HomeCardItem.class, HomeCardViewHolder.class, 0, 20));
        int i4 = COMMON_TYPE_BASE;
        COMMON_TYPE_BASE = i4 + 1;
        CommonItemFactory.registItem(new CommonItemInfo("recommend_item", i4, HomeRecommendItem.class, HomeRecommendViewHolder.class, 0, 10));
        int i5 = COMMON_TYPE_BASE;
        COMMON_TYPE_BASE = i5 + 1;
        CommonItemFactory.registItem(new CommonItemInfo("common_item", i5, HomeCommonTabItem.class, HomeCommonItemViewHolder.class, 0, 20));
        CommonItemFactory.registItem(new CommonItemInfo("footer", CommonItemInfo.FOOT_TYPE, CommonBaseItem.class, CommonFooterProcess.class));
        int i6 = COMMON_TYPE_BASE;
        COMMON_TYPE_BASE = i6 + 1;
        CommonItemFactory.registItem(new CommonItemInfo("recommend_item_title", i6, HomeRecommendTitleItem.class, HomeRecommendTitleViewHolder.class));
        int i7 = COMMON_TYPE_BASE;
        COMMON_TYPE_BASE = i7 + 1;
        CommonItemFactory.registItem(new CommonItemInfo(Constants.APPRENTICE_TITLE_TYPE_NAME, i7, HomeApprenticeTitleItem.class, HomeApprenticeAreaTitleViewHolder.class));
        int i8 = COMMON_TYPE_BASE;
        COMMON_TYPE_BASE = i8 + 1;
        CommonItemFactory.registItem(new CommonItemInfo(Constants.APPRENTICE_TYPE_NAME, i8, HomeApprenticeItem.class, HomeApprenticeAreaViewHolder.class));
        int i9 = COMMON_TYPE_BASE;
        COMMON_TYPE_BASE = i9 + 1;
        CommonItemFactory.registItem(new CommonItemInfo(Constants.BANNER_TYPE_NAME, i9, HomeBannerItem.class, HomeBannerViewHolder.class));
        int i10 = COMMON_TYPE_BASE;
        COMMON_TYPE_BASE = i10 + 1;
        CommonItemFactory.registItem(new CommonItemInfo(Constants.CIRCLE_POINT_TYPE_NAME, i10, HomeCircleNavItem.class, HomeCircleNavViewHolder.class));
        int i11 = COMMON_TYPE_BASE;
        COMMON_TYPE_BASE = i11 + 1;
        CommonItemFactory.registItem(new CommonItemInfo(Constants.SALE_BLOCK_TYPE_NAME, i11, HomeSaleBlockItem.class, HomeSaleBlockViewHolder.class));
        int i12 = COMMON_TYPE_BASE;
        COMMON_TYPE_BASE = i12 + 1;
        CommonItemFactory.registItem(new CommonItemInfo(Constants.FLASH_REBATE_TYPE_NAME, i12, HomeFlashSaleBlock.class, HomeFlashSaleViewHolder.class));
        int i13 = COMMON_TYPE_BASE;
        COMMON_TYPE_BASE = i13 + 1;
        CommonItemFactory.registItem(new CommonItemInfo(Constants.RESOURCE_PLACE_TOP_LIST_TYPE_NAME, i13, HomeResourcePlaceItem.class, HomeResourcePlaceViewHolder.class));
    }

    public void onCreate(Bundle bundle) {
        OrangeConfigCenterManager.getInstance().pullOrangeConfig();
        mCurrentIndex = 0;
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(bundle);
        this.lifecycleRegistry = new LifecycleRegistry(this);
        logger.info(UmbrellaConstants.LIFECYCLE_CREATE);
        App.getAppComponent().inject(this);
        setContentView((int) R.layout.activity_bottom_nav);
        UTAnalytics.getInstance().getDefaultTracker().skipPage(this);
        HomeThemeDataManager.getInstance().parseTheme();
        initView();
        updateToolbarBg();
        new SearchHotTagRequest().sendRequest();
        this.mUiHandler.postDelayed(new Runnable() {
            public void run() {
                UpdateUtils.checkUpdate(BottomNavActivity.this, false);
            }
        }, 2000);
    }

    private void initView() {
        this.mDXBulletinBoard = (LinearLayout) findViewById(R.id.home_dx_bulletin_board_ll);
        ActivityUtil.setupToolbar(this, (Toolbar) findViewById(R.id.toolbar), false);
        this.mSearchContainer = (SearchInputPlaceholderView) findViewById(R.id.search_input_placeholder);
        this.mToolbarTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        this.mMessageNumTextView = (TextView) findViewById(R.id.message_num_icon);
        this.mHomeContainer = (RelativeLayout) findViewById(R.id.home_tab_container);
        this.mToolbar = (Toolbar) findViewById(R.id.toolbar);
        this.mSearchContainer.setOnClickListener(this);
        this.mMessageIconView = (ImageView) findViewById(R.id.message_entrance);
        this.mMessageIconView.setOnClickListener(this);
        this.mBottomNavContainer = (LinearLayout) findViewById(R.id.bottom_navigation_container);
        this.viewPager = (AHBottomNavigationViewPager) findViewById(R.id.view_pager);
        int i = 4;
        this.viewPager.setOffscreenPageLimit(4);
        sIsSwitchMidH5Tab = OrangeConfigCenterManager.getInstance().isSwitchMidH5Tab();
        if (sIsSwitchMidH5Tab) {
            i = 5;
        }
        sPageCount = i;
        this.adapter = new BottomNavFragmentAdapter(this);
        this.viewPager.setAdapter(this.adapter);
        this.presenter = new BottomNavPresenter(this, this.login);
        if (!(getIntent() == null || getIntent().getExtras() == null)) {
            String string = getIntent().getExtras().getString("url");
            if (!TextUtils.isEmpty(string)) {
                MoonComponentManager.getInstance().getPageRouter().gotoPage(string);
            }
        }
        initTabNavBarData();
        initDXEngine();
    }

    private void initDXEngine() {
        this.mUNWDinamicXEngine = new UNWDinamicXEngine(this, "Home", new IDXEnginePresenter() {
            public void renderFailed(String str) {
            }

            public void renderSuccess(View view) {
                BottomNavActivity.this.mDXBulletinBoard.addView(view);
                BulletinBoardManager.getInstance().showBulletinBoard(BottomNavActivity.this.adapter.getCurrentFragment(BottomNavActivity.mCurrentIndex));
                DXUTHelper.renderDXBulletinBoardSuccess(DXUTHelper.DX_BULLETIN_BOARD_PAGE_NAME, "showDXBulletinBoard");
            }
        });
        try {
            this.mBulletinBoardModel = (BulletinBoardModel) JSONObject.parseObject(OrangeConfigCenterManager.getInstance().fetchBulletinBoardData(), BulletinBoardModel.class);
            BulletinBoardManager.getInstance().init(this.mBulletinBoardModel, this.mDXBulletinBoard);
            if (this.mBulletinBoardModel != null && BulletinBoardManager.getInstance().isShowBulletinBoard()) {
                this.mUNWDinamicXEngine.render(new DXEngineDataModel(this.mBulletinBoardModel.getTemplate(), this.mBulletinBoardModel.getFieldData()));
            }
        } catch (Exception unused) {
        }
        this.mUNWDinamicXEngine.registerEvent("closeClick", new CloseBulletinBoardEvent(this.mDXBulletinBoard));
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String string = intent.getExtras().getString(MoonJSBridge.JUMP_TO_HOME_PAGE_NUM_KEY);
        boolean z = false;
        boolean z2 = intent.getExtras().getBoolean(MoonJSBridge.JUMP_TO_HOME_PAGE_IS_REFRESH_KEY, false);
        int homeTabIndexByPageName = HomeUtil.getHomeTabIndexByPageName(string);
        if (mCurrentIndex == homeTabIndexByPageName) {
            z = true;
        }
        onClickBottomNavItem(homeTabIndexByPageName, z);
        if (z2) {
            currentFragment = this.adapter.getCurrentFragment(homeTabIndexByPageName);
            if (currentFragment != null) {
                currentFragment.refresh();
            }
        }
    }

    private void updateToolBar() {
        this.mToolbar.setBackgroundDrawable(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{getResources().getColor(R.color.home_bg_gradient_left), getResources().getColor(R.color.home_bg_gradient_right)}));
    }

    private void initTabNavBarData() {
        this.mItemWidth = LocalDisplay.SCREEN_WIDTH_PIXELS / sPageCount;
        this.mItemHeight = LocalDisplay.dp2px(50.0f);
        this.mItemParams = new LinearLayout.LayoutParams(this.mItemWidth, this.mItemHeight);
        this.mBottomNavItemList = new ArrayList(sPageCount);
        this.mBottomNavContainer.removeAllViews();
        LayoutInflater from = LayoutInflater.from(this);
        for (int i = 0; i < sPageCount; i++) {
            BottomNavItemView bottomNavItemView = new BottomNavItemView(this);
            this.mBottomNavContainer.addView(bottomNavItemView.create(from, i), this.mItemParams);
            this.mBottomNavItemList.add(bottomNavItemView);
        }
    }

    public void onClickBottomNavItem(int i, boolean z) {
        UTHelper.sendControlHit(BottomNavUTHelper.parseControlNameByIndex(i));
        this.presenter.selectBottomNavTab(i, z);
        updateBottomTabBarUI(i);
        mCurrentIndex = i;
        showUserGuide();
    }

    private void updateBottomTabBarUI(int i) {
        if (i != mCurrentIndex) {
            for (int i2 = 0; i2 < this.mBottomNavItemList.size(); i2++) {
                BottomNavItemView bottomNavItemView = this.mBottomNavItemList.get(i2);
                if (i2 == i) {
                    bottomNavItemView.updateSelectedState(this);
                } else if (i2 == mCurrentIndex) {
                    bottomNavItemView.updateUnSelectedState(this);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onPostCreate(@Nullable Bundle bundle) {
        super.onPostCreate(bundle);
        new MessageListCountRequest().sendRequest(new RxMtopRequest.RxMtopResult<MessageListCountResponse>() {
            public void result(RxMtopResponse<MessageListCountResponse> rxMtopResponse) {
                if (rxMtopResponse.isReqSuccess && rxMtopResponse.result != null) {
                    int unused = BottomNavActivity.this.mDisplayedUnreadMessageCount = ((MessageListCountResponse) rxMtopResponse.result).unRead;
                    BottomNavActivity.this.updateNotifyMessageBadge(Integer.valueOf(BottomNavActivity.this.mDisplayedUnreadMessageCount));
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        super.onRestart();
        logger.info("onRestart");
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        logger.info(UmbrellaConstants.LIFECYCLE_START);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        showUserGuide();
        currentFragment = this.adapter.getCurrentFragment(mCurrentIndex);
        displayFragment(currentFragment);
        if (this.taoCodeDialogShow) {
            showNewMarketDialog();
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        logger.info(MessageID.onPause);
        currentFragment = this.adapter.getCurrentFragment(mCurrentIndex);
        if (currentFragment != null) {
            currentFragment.willBeHidden();
        }
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        logger.info("onSaveInstanceState");
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        logger.info(MessageID.onStop);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        logger.info("onDestroy");
    }

    public boolean onSelectBottomNavTab(int i, boolean z) {
        IBottomNavFragment currentFragment2 = this.adapter.getCurrentFragment(BottomNavItemView.sPreTabIndex);
        currentFragment = this.adapter.getCurrentFragment(i);
        if (!z || currentFragment == null) {
            if (currentFragment2 != null) {
                currentFragment2.willBeHidden();
            }
            if (isHomeFragment(currentFragment)) {
                updateNotifyMessageBadge(Integer.valueOf(this.mDisplayedUnreadMessageCount));
            } else {
                this.mHomeContainer.setVisibility(8);
            }
            this.viewPager.setCurrentItem(i, false);
            displayFragment(currentFragment);
            return true;
        }
        currentFragment.refresh();
        return true;
    }

    public void showUserGuide() {
        if (mCurrentIndex == 2 && ((SettingManager) BeanContext.get(SettingManager.class)).isMidTabUserGuideFirst() && sIsSwitchMidH5Tab && TextUtils.equals(OrangeConfigCenterManager.getInstance().getMideH5TabModel().getType(), "goods")) {
            startActivity(new Intent(this, UserGuideSingeActivity.class));
        }
    }

    public void setPresenter(IBottomNavContract.IBottomNavPresenter iBottomNavPresenter) {
        this.presenter = iBottomNavPresenter;
    }

    @Subscribe
    public void onNewVersionForceUpdateEvent(NewVersionForceUpdateEvent newVersionForceUpdateEvent) {
        Intent intent = new Intent(this, UpdateActivity.class);
        intent.putExtra(UpdateActivity.EXTRA_INFO, JSON.toJSONString(newVersionForceUpdateEvent.getInfo()));
        startActivity(intent);
        finish();
    }

    @Subscribe
    public void onNewVersionRemindEvent(NewVersionRemindEvent newVersionRemindEvent) {
        UpdateRemindDialog.show(this, newVersionRemindEvent.getInfo());
    }

    private void displayFragment(@Nullable IBottomNavFragment iBottomNavFragment) {
        if (iBottomNavFragment != null) {
            iBottomNavFragment.willBeDisplayed();
            BulletinBoardManager.getInstance().showBulletinBoard(iBottomNavFragment);
            if (isHomeFragment(iBottomNavFragment)) {
                this.mToolbarTitle.setVisibility(8);
                this.mHomeContainer.setVisibility(0);
                updateToolbarBg();
                return;
            }
            this.mToolbarTitle.setVisibility(0);
            this.mToolbarTitle.setText(iBottomNavFragment.currFragmentTitle());
            this.mHomeContainer.setVisibility(8);
            this.mToolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.actbar_bg));
            StatusBarUtils.setGradientColor(this, R.drawable.status_bar_white_bg);
        }
    }

    @Subscribe
    public void onHomeThemeDataItem(HomeThemeDataItem homeThemeDataItem) {
        if (isHomeFragment(this.adapter.getCurrentFragment(mCurrentIndex))) {
            updateToolbarBg();
        }
    }

    private void updateToolbarBg() {
        if (!MoonConfigCenter.isHomeWeexSwitchOn()) {
            try {
                if (HomeThemeDataManager.getInstance().isSwitchConfigCenterTheme()) {
                    HomeThemeDataItem homeThemeDataItem = HomeThemeDataManager.getInstance().themeDataItem;
                    GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor(homeThemeDataItem.statusBarStartColor), Color.parseColor(homeThemeDataItem.statusBarEndColor)});
                    StatusBarUtils.setGradientColor(this, R.drawable.status_bar_bg);
                    this.mToolbar.setBackgroundDrawable(gradientDrawable);
                } else {
                    StatusBarUtils.setGradientColor(this, R.drawable.status_bar_default_bg);
                    this.mToolbar.setBackground(ContextCompat.getDrawable(this, R.color.home_tab_bg));
                }
                this.mMessageIconView.setImageResource(R.drawable.ic_message_entrance);
            } catch (Exception unused) {
            }
        } else {
            setWeexTheme();
        }
    }

    private void setWeexTheme() {
        updateToolBar();
        this.mMessageIconView.setImageResource(R.drawable.ic_message_entrance_weex);
        StatusBarUtils.setGradientColor(this, R.drawable.status_bar_white_bg);
    }

    private boolean isHomeFragment(IBottomNavFragment iBottomNavFragment) {
        return (iBottomNavFragment instanceof HomeFragment) || (iBottomNavFragment instanceof DiscoveryFragment);
    }

    /* access modifiers changed from: private */
    public void updateNotifyMessageBadge(Integer num) {
        if (num == null || num.intValue() <= 0) {
            this.mMessageNumTextView.setVisibility(8);
            this.mMessageIconView.setVisibility(0);
            return;
        }
        String valueOf = String.valueOf(num);
        if (num.intValue() > 99) {
            valueOf = "99+";
        }
        this.mMessageNumTextView.setText(valueOf);
        this.mMessageNumTextView.setVisibility(0);
        this.mMessageIconView.setVisibility(0);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.message_entrance /*2131689822*/:
            case R.id.message_num_icon /*2131689823*/:
                UTHelper.HomePage.toMessagesClicked();
                this.mDisplayedUnreadMessageCount = 0;
                MoonComponentManager.getInstance().getPageRouter().gotoPage(AppPageInfo.PAGE_MESSAGE_LIST);
                updateNotifyMessageBadge(Integer.valueOf(this.mDisplayedUnreadMessageCount));
                return;
            case R.id.search_input_placeholder /*2131689824*/:
                UTHelper.HomePage.toSearchClicked();
                MoonComponentManager.getInstance().getPageRouter().gotoPage(AppPageInfo.PAGE_SEARCH_INPUT);
                return;
            default:
                logger.warn("unrecognized view clicked {}", (Object) Integer.valueOf(view.getId()));
                return;
        }
    }

    public LifecycleRegistry getLifecycle() {
        if (this.lifecycleRegistry == null) {
            this.lifecycleRegistry = new LifecycleRegistry(this);
        }
        return this.lifecycleRegistry;
    }

    @Subscribe
    public void onSearchHotTagEvent(SearchHotTagEvent searchHotTagEvent) {
        if (searchHotTagEvent.isSuccess && searchHotTagEvent.dataResult != null && searchHotTagEvent.dataResult.hotTagItems != null && searchHotTagEvent.dataResult.hotTagItems.size() != 0) {
            this.mSearchContainer.setInputPlaceholder(searchHotTagEvent.dataResult.hotTagItems.get(0).getWord());
        }
    }

    private void showNewMarketDialog() {
        currentFragment = this.adapter.getCurrentFragment(mCurrentIndex);
        if (isHomeFragment(currentFragment)) {
            String configResult = EtaoConfigCenter.getInstance().getConfigResult(ConfigKeyList.UNION_HOME_MARKETING);
            if (!TextUtils.isEmpty(configResult)) {
                try {
                    SafeJSONObject safeJSONObject = new SafeJSONObject(configResult);
                    final LottieData lottieData = new LottieData();
                    lottieData.url = safeJSONObject.optString("url");
                    lottieData.img = safeJSONObject.optString("img");
                    lottieData.height = safeJSONObject.optInt("height");
                    lottieData.width = safeJSONObject.optInt("width");
                    final HashMap hashMap = new HashMap();
                    hashMap.put("url", lottieData.url);
                    UNWLottieDialog uNWLottieDialog = new UNWLottieDialog(this, lottieData, new LottieDialogCallback() {
                        public boolean clickBg() {
                            return true;
                        }

                        public boolean clickClose() {
                            return true;
                        }

                        public boolean clickContent() {
                            UTHelper.sendControlHit(UTHelper.PAGE_NAME_HOME_MARKET, UTHelper.CONTROL_NAME_CLICK_AD, hashMap);
                            MoonComponentManager.getInstance().getPageRouter().gotoPage(lottieData.url);
                            return true;
                        }

                        public boolean startShow() {
                            UTHelper.sendControlHit(UTHelper.PAGE_NAME_HOME_MARKET, UTHelper.CONTROL_NAME_MARKET_SHOW, hashMap);
                            BusinessMonitorLogger.Marketing.show(BottomNavActivity.TAG);
                            return true;
                        }
                    });
                    uNWLottieDialog.type = "homeMarket";
                    uNWLottieDialog.uuid = lottieData.url;
                    uNWLottieDialog.fatigueTime = 86400000;
                    uNWLottieDialog.starttime = safeJSONObject.optString(LoginConstant.START_TIME);
                    uNWLottieDialog.endtime = safeJSONObject.optString("endTime");
                    UNWDialogController.getInstance().commit((UNWAbstractDialog) uNWLottieDialog);
                } catch (Exception unused) {
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTaoCodeDialogEvent(TaoCodeDialogEvent taoCodeDialogEvent) {
        switch (taoCodeDialogEvent.getmDialogResult()) {
            case TAO_CODE_DIALOG_SHOW:
                this.taoCodeDialogShow = true;
                return;
            case TAO_CODE_DIALOG_NOT_SHOW:
                showNewMarketDialog();
                return;
            default:
                return;
        }
    }
}
