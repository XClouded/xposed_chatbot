package com.alimama.moon.features.home;

import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import com.alimama.moon.R;
import com.alimama.moon.features.home.item.HomeRefreshData;
import com.alimama.moon.features.home.item.HomeTabCateItem;
import com.alimama.moon.features.home.network.HomeDataEvent;
import com.alimama.moon.features.home.network.HomeIndexRequest;
import com.alimama.moon.features.home.network.HomeTabDataEvent;
import com.alimama.moon.features.home.theme.HomeThemeDataItem;
import com.alimama.moon.features.home.theme.HomeThemeDataManager;
import com.alimama.moon.features.home.view.HomeSliderBanner;
import com.alimama.moon.ui.fragment.BaseFragment;
import com.alimama.moon.view.design.PageStatusView;
import com.alimama.union.app.messagelist.CommonFooterProcess;
import com.alimama.unionwl.uiframe.views.base.ISPtrFrameLayout;
import com.alimama.unionwl.uiframe.views.base.ISPtrHeaderView;
import com.alimamaunion.common.listpage.CommonItemInfo;
import com.alimamaunion.common.listpage.CommonRecyclerAdapter;
import com.alimamaunion.common.listpage.EndlessRecyclerOnScrollListener;
import in.srain.cube.ptr.PtrDefaultHandler;
import in.srain.cube.ptr.PtrFrameLayout;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class HomeCateFragment extends BaseFragment {
    private static final int JUMP_TO = 5;
    private static final String TAB_FLOOR_ID = "floorId";
    private static final String TAB_POSITION = "position";
    private static final String TAB_QIEID = "qieId";
    private static final String TAB_SPM = "spm";
    private static final String TAB_TYPE = "type";
    private CommonRecyclerAdapter mAdapter;
    private String mFloorId;
    private ISPtrHeaderView mHeaderView;
    /* access modifiers changed from: private */
    public HomeIndexRequest mHomeIndexRequest;
    private String mHomeTabType;
    /* access modifiers changed from: private */
    public List<CommonItemInfo> mItemList = new ArrayList();
    /* access modifiers changed from: private */
    public int mItemPos = 0;
    /* access modifiers changed from: private */
    public GridLayoutManager mLayoutManager;
    /* access modifiers changed from: private */
    public int mPosition;
    private String mQieId;
    /* access modifiers changed from: private */
    public RecyclerView mRecyclerView;
    private final View.OnClickListener mRefreshListener = new View.OnClickListener() {
        public void onClick(View view) {
            if (HomeCateFragment.this.mHomeIndexRequest != null) {
                HomeCateFragment.this.mStatusView.onLoading();
                HomeCateFragment.this.mHomeIndexRequest.queryFirstPage();
                HomeCateFragment.this.mItemList.clear();
            }
        }
    };
    private String mSpm;
    /* access modifiers changed from: private */
    public PageStatusView mStatusView;
    /* access modifiers changed from: private */
    public HomeTabCateItem mTabCateItem;
    private final View.OnClickListener mTopListener = new View.OnClickListener() {
        public void onClick(View view) {
            Object tag;
            if (view != null && (tag = view.getTag()) != null && (tag instanceof Integer)) {
                HomeCateFragment.this.smoothMoveToPosition(((Integer) tag).intValue());
            }
        }
    };
    /* access modifiers changed from: private */
    public ImageView mTopView;
    private ISPtrFrameLayout ptrFrameLayout;

    public static HomeCateFragment newInstance(int i, String str, String str2, String str3, String str4) {
        HomeCateFragment homeCateFragment = new HomeCateFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TAB_FLOOR_ID, str2);
        bundle.putString("type", str);
        bundle.putString(TAB_QIEID, str3);
        bundle.putString("spm", str4);
        bundle.putInt("position", i);
        homeCateFragment.setArguments(bundle);
        return homeCateFragment;
    }

    public View returnCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_home_cate, (ViewGroup) null);
        this.mRecyclerView = (RecyclerView) inflate.findViewById(R.id.home_cate_recyclerview);
        this.mStatusView = (PageStatusView) inflate.findViewById(R.id.home_cate_statusview);
        this.mStatusView.setRefreshListener(this.mRefreshListener);
        this.ptrFrameLayout = (ISPtrFrameLayout) inflate.findViewById(R.id.pull_refresh_layout);
        this.mTopView = (ImageView) inflate.findViewById(R.id.back_to_top);
        this.mTopView.setOnClickListener(this.mTopListener);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mHomeTabType = arguments.getString("type");
            this.mFloorId = arguments.getString(TAB_FLOOR_ID);
            this.mQieId = arguments.getString(TAB_QIEID);
            this.mSpm = arguments.getString("spm");
            this.mPosition = arguments.getInt("position");
        }
        initView();
        return inflate;
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        this.mStatusView.onLoading();
        this.mHomeIndexRequest = new HomeIndexRequest(this.mPosition, this.mHomeTabType, this.mFloorId, this.mQieId, this.mSpm);
        this.mHomeIndexRequest.queryFirstPage();
        this.mItemList.clear();
    }

    private void initView() {
        this.mAdapter = new CommonRecyclerAdapter();
        this.mAdapter.setEnableFooter(true);
        this.mAdapter.setFooterProcesser(new CommonFooterProcess());
        this.mLayoutManager = new GridLayoutManager(getActivity(), 20);
        this.mLayoutManager.setSpanSizeLookup(new CommonGridSpanLookup(this.mAdapter));
        this.mRecyclerView.setLayoutManager(this.mLayoutManager);
        this.mRecyclerView.addItemDecoration(new HomeItemDecoration(this.mAdapter));
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(this.mLayoutManager) {
            public void onLoadMore(int i) {
                if (HomeCateFragment.this.mPosition == 0 && HomeCateFragment.this.mTabCateItem != null) {
                    if (!TextUtils.isEmpty(HomeCateFragment.this.mTabCateItem.getFloorId())) {
                        HomeCateFragment.this.mHomeIndexRequest.appendParam(HomeCateFragment.TAB_FLOOR_ID, HomeCateFragment.this.mTabCateItem.getFloorId());
                    } else {
                        HomeCateFragment.this.mHomeIndexRequest.appendParam(HomeCateFragment.TAB_QIEID, HomeCateFragment.this.mTabCateItem.getQieId());
                    }
                    HomeCateFragment.this.mHomeIndexRequest.appendParam("spm", HomeCateFragment.this.mTabCateItem.getSpm());
                }
                HomeCateFragment.this.mHomeIndexRequest.queryNextPage();
            }

            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int i) {
                super.onScrollStateChanged(recyclerView, i);
                if (i == 0) {
                    int unused = HomeCateFragment.this.mItemPos = HomeCateFragment.this.mLayoutManager.findFirstVisibleItemPosition();
                    int unused2 = HomeCateFragment.this.mItemPos = HomeCateFragment.this.mItemPos < 0 ? HomeCateFragment.this.mLayoutManager.findFirstVisibleItemPosition() : HomeCateFragment.this.mItemPos;
                    if (HomeCateFragment.this.mItemPos > 5) {
                        HomeCateFragment.this.mTopView.setVisibility(0);
                        HomeCateFragment.this.mTopView.setTag(0);
                        return;
                    }
                    HomeCateFragment.this.mTopView.setVisibility(8);
                }
            }
        });
        this.mHeaderView = new ISPtrHeaderView(getContext());
        this.ptrFrameLayout.initView(this.mHeaderView, this.mHeaderView);
        updatePtrFrameLayoutBg();
        this.ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            public boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View view, View view2) {
                if (HomeSliderBanner.isOperateHomeSliderBanner) {
                    return false;
                }
                return super.checkCanDoRefresh(ptrFrameLayout, HomeCateFragment.this.mRecyclerView, view2);
            }

            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                HomeCateFragment.this.mHomeIndexRequest.queryFirstPage();
                HomeCateFragment.this.mItemList.clear();
            }
        });
    }

    private void updatePtrFrameLayoutBg() {
        GradientDrawable gradientDrawable;
        try {
            if (HomeThemeDataManager.getInstance().isSwitchConfigCenterTheme()) {
                HomeThemeDataItem homeThemeDataItem = HomeThemeDataManager.getInstance().themeDataItem;
                gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor(homeThemeDataItem.tabStartColor), Color.parseColor(homeThemeDataItem.tabEndColor)});
                this.mHeaderView.updatePtrHeaderViewBgByStringColor(homeThemeDataItem.tabStartColor, homeThemeDataItem.tabEndColor);
                this.mHeaderView.updatePtrHeaderViewTextColor(HomeThemeDataManager.getInstance().themeDataItem.refreshBgTextColor);
            } else {
                gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{getResources().getColor(R.color.home_tab_bg), getResources().getColor(R.color.home_tab_bg)});
                this.mHeaderView.updatePtrHeaderViewBgByIntColor(R.color.home_tab_bg, R.color.home_tab_bg);
                this.mHeaderView.updatePtrHeaderViewTextColor("#FFFFFF");
            }
            this.ptrFrameLayout.setBackgroundDrawable(gradientDrawable);
        } catch (Exception unused) {
        }
    }

    @Subscribe
    public void onRefresh(HomeRefreshData homeRefreshData) {
        if (homeRefreshData != null && homeRefreshData.isRefresh()) {
            this.mHomeIndexRequest.queryFirstPage();
            this.mItemList.clear();
        }
    }

    @Subscribe
    public void onHomeThemeDataItem(HomeThemeDataItem homeThemeDataItem) {
        updatePtrFrameLayoutBg();
    }

    @Subscribe
    public void onHomeDataEvent(HomeDataEvent homeDataEvent) {
        if (this.mPosition == homeDataEvent.mPosition) {
            this.ptrFrameLayout.refreshComplete();
            this.mHomeIndexRequest.clearLoadingState();
            if (this.mHomeIndexRequest.isFirstPage()) {
                if (!homeDataEvent.isSuccess) {
                    if (homeDataEvent.isNetworkError()) {
                        this.mStatusView.onNetworkError();
                        return;
                    } else {
                        this.mStatusView.onServerError();
                        return;
                    }
                } else if (homeDataEvent.mHomeData.mHomeDataList.isEmpty()) {
                    this.mStatusView.onServerError();
                    return;
                } else {
                    HomeThemeDataManager.getInstance().parseTheme();
                    EventBus.getDefault().post(HomeThemeDataManager.getInstance().themeDataItem);
                }
            }
            this.mStatusView.onContentLoaded();
            if (homeDataEvent.isSuccess) {
                this.mHomeIndexRequest.setHasMore(homeDataEvent.hasMore());
                this.mAdapter.setFinish(!homeDataEvent.hasMore());
                if (homeDataEvent.mHomeData.mHomeDataList.isEmpty()) {
                    this.mAdapter.notifyItemChanged(this.mAdapter.getItemCount() - 1);
                } else {
                    int itemCount = this.mAdapter.getItemCount();
                    this.mItemList.addAll(homeDataEvent.mHomeData.mHomeDataList);
                    this.mAdapter.notifyResult(this.mHomeIndexRequest.isFirstPage(), homeDataEvent.mHomeData.mHomeDataList);
                    if (this.mHomeIndexRequest.isFirstPage()) {
                        this.mAdapter.notifyDataSetChanged();
                    } else {
                        this.mAdapter.notifyItemRangeInserted(itemCount, homeDataEvent.mHomeData.mHomeDataList.size());
                    }
                }
            } else {
                this.mHomeIndexRequest.setHasMore(false);
                this.mAdapter.setFinish(true);
                this.mAdapter.notifyItemChanged(this.mAdapter.getItemCount() - 1);
            }
            if (!homeDataEvent.mHomeData.mHomeTabList.isEmpty()) {
                this.mTabCateItem = homeDataEvent.mHomeData.mHomeTabList.get(0);
                HomeTabDataEvent homeTabDataEvent = new HomeTabDataEvent();
                homeTabDataEvent.mHomeTabList = homeDataEvent.mHomeData.mHomeTabList;
                EventBus.getDefault().post(homeTabDataEvent);
            }
        }
    }

    /* access modifiers changed from: private */
    public void smoothMoveToPosition(int i) {
        AnonymousClass5 r0 = new LinearSmoothScroller(getActivity()) {
            /* access modifiers changed from: protected */
            public int getVerticalSnapPreference() {
                return -1;
            }

            public PointF computeScrollVectorForPosition(int i) {
                int access$1000 = HomeCateFragment.this.calculateScrollDirectionForPosition(i);
                if (access$1000 == 0) {
                    return null;
                }
                if (HomeCateFragment.this.mLayoutManager.getOrientation() == 0) {
                    return new PointF((float) access$1000, 0.0f);
                }
                return new PointF(0.0f, (float) access$1000);
            }
        };
        r0.setTargetPosition(i);
        if (i == 0) {
            if (this.mAdapter.getItemCount() > 5 && this.mItemPos > 5) {
                this.mLayoutManager.scrollToPosition(5);
            }
            this.mLayoutManager.startSmoothScroll(r0);
        }
    }

    /* access modifiers changed from: private */
    public int calculateScrollDirectionForPosition(int i) {
        if (this.mLayoutManager.getChildCount() == 0) {
            return -1;
        }
        if (i < getFirstChildPosition()) {
            return -1;
        }
        return 1;
    }

    private int getFirstChildPosition() {
        if (this.mLayoutManager.getChildCount() == 0) {
            return 0;
        }
        return this.mLayoutManager.getPosition(this.mLayoutManager.getChildAt(0));
    }
}
