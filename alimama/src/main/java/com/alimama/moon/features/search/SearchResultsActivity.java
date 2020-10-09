package com.alimama.moon.features.search;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.alimama.moon.R;
import com.alimama.moon.features.search.CommonItemsAdapter;
import com.alimama.moon.features.search.SearchFilterPanel;
import com.alimama.moon.features.search.SortCategoryAdapter;
import com.alimama.moon.ui.BaseActivity;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.moon.utils.ActivityUtil;
import com.alimama.moon.utils.SpmProcessor;
import com.alimama.moon.view.BackToTopView;
import com.alimama.moon.view.FooterRecyclerView;
import com.alimama.moon.view.design.FooterStatusView;
import com.alimama.moon.view.design.PageStatusView;
import com.alimama.moon.web.WebPageIntentGenerator;
import com.alimama.union.app.configcenter.ConfigKeyList;
import com.alimama.union.app.infrastructure.socialShare.social.ShareUtils;
import com.alimama.union.app.pagerouter.AppPageInfo;
import com.alimama.union.app.pagerouter.IUTPage;
import com.alimama.union.app.pagerouter.MoonComponentManager;
import com.alimamaunion.base.configcenter.EtaoConfigCenter;
import com.alimamaunion.common.listpage.EndlessRecyclerOnScrollListener;
import java.util.List;
import org.greenrobot.eventbus.Subscribe;

public class SearchResultsActivity extends BaseActivity implements View.OnClickListener, IUTPage {
    /* access modifiers changed from: private */
    public TextView mFilterOption;
    /* access modifiers changed from: private */
    public SearchFilterPanel mFilterPanel;
    private final View.OnClickListener mRefreshListener = new View.OnClickListener() {
        public void onClick(View view) {
            if (SearchResultsActivity.this.mSearchRequest != null) {
                SearchResultsActivity.this.mSearchPageView.onLoading();
                SearchResultsActivity.this.mSearchRequest.sendRequest();
            }
        }
    };
    /* access modifiers changed from: private */
    public FooterRecyclerView mResultsRecyclerView;
    private SearchInputPlaceholderView mSearchInputPlaceholder;
    /* access modifiers changed from: private */
    public PageStatusView mSearchPageView;
    /* access modifiers changed from: private */
    public SearchRequest mSearchRequest;
    private CommonItemsAdapter mSearchResultsAdapter;
    /* access modifiers changed from: private */
    public SortByDropDown mSortByDropdown;
    /* access modifiers changed from: private */
    public SortCategoryPanel mSortPanel;
    private final View.OnClickListener mTopSearchOptionClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            if ((view instanceof CheckedTextView) && (view.getTag() instanceof SearchOptionModel)) {
                CheckedTextView checkedTextView = (CheckedTextView) view;
                checkedTextView.toggle();
                SearchOptionModel searchOptionModel = (SearchOptionModel) view.getTag();
                if (checkedTextView.isChecked()) {
                    SearchResultsActivity.this.mSearchRequest.addSearchFilter(searchOptionModel);
                } else {
                    SearchResultsActivity.this.mSearchRequest.removeSearchFilter(searchOptionModel);
                }
                SearchResultsActivity.this.mSearchPageView.onLoading();
                UTHelper.SearchResultsPage.trackControlClick(searchOptionModel.getControlName());
            }
        }
    };

    public String getCurrentPageName() {
        return UTHelper.SearchResultsPage.PAGE_NAME;
    }

    public String getCurrentSpmCnt() {
        return UTHelper.SearchResultsPage.SPM_CNT;
    }

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        String str;
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_search_results);
        this.mSearchPageView = (PageStatusView) findViewById(R.id.page_status_view);
        this.mSearchPageView.setRefreshListener(this.mRefreshListener);
        this.mFilterOption = (TextView) findViewById(R.id.filter_option);
        this.mFilterOption.setOnClickListener(this);
        ActivityUtil.setupToolbar(this, (Toolbar) findViewById(R.id.toolbar), true);
        SearchOptionConfigModel fromJsonJson = SearchOptionConfigModel.fromJsonJson(EtaoConfigCenter.getInstance().getConfigResult(ConfigKeyList.UNION_SEARCH_OPTIONS));
        setupResultListView();
        setupTopSearchOptions(fromJsonJson, (LinearLayout) findViewById(R.id.ll_top_search_options_container));
        setupSideFilterPanel();
        setupSortByPanel(fromJsonJson);
        ((BackToTopView) findViewById(R.id.back_to_top)).bindRecyclerView(this.mResultsRecyclerView);
        this.mSearchPageView.onLoading();
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            str = "";
        } else {
            str = extras.getString(SearchInputActivity.SEARCH_RESULT_KEYWORD, "");
        }
        this.mSearchRequest = new SearchRequest(str, this.mSortByDropdown.getSortOption());
        this.mSearchRequest.queryFirstPage();
        this.mSearchInputPlaceholder = (SearchInputPlaceholderView) findViewById(R.id.tv_search_placeholder);
        this.mSearchInputPlaceholder.setInputPlaceholder(str);
        this.mSearchInputPlaceholder.setOnClickListener(this);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_search_placeholder) {
            finish();
            Bundle bundle = new Bundle();
            bundle.putString(SearchInputActivity.SEARCH_RESULT_KEYWORD, this.mSearchInputPlaceholder.getInputPlaceholder());
            MoonComponentManager.getInstance().getPageRouter().gotoPage(AppPageInfo.PAGE_SEARCH_INPUT, bundle);
        } else if (id == R.id.sort_by_dropdown) {
            if (!this.mSortPanel.isExpanding()) {
                this.mSortByDropdown.expand();
            }
            this.mSortPanel.toggle(findViewById(R.id.sort_by_category));
        } else if (id == R.id.filter_option) {
            if (this.mSortPanel.isExpanding()) {
                this.mSortPanel.toggle(findViewById(R.id.sort_by_category));
                return;
            }
            this.mFilterPanel.slideIn(this.mFilterOption, this);
            UTHelper.SearchResultsPage.trackOpenFilter();
        }
    }

    @Subscribe
    public void onEvent(SearchResultsEvent searchResultsEvent) {
        if (searchResultsEvent.isSuccess()) {
            SearchResponse response = searchResultsEvent.getResponse();
            if (searchResultsEvent.isFirstPage()) {
                this.mResultsRecyclerView.scrollToPosition(0);
                this.mSearchResultsAdapter.setData(response.getResultList());
            } else {
                this.mSearchResultsAdapter.appendData(response.getResultList());
            }
            if (this.mSearchRequest.isHasMore()) {
                this.mResultsRecyclerView.onLoadingMore();
            } else {
                this.mResultsRecyclerView.onNoMoreItems();
            }
            this.mSearchPageView.onContentLoaded();
        } else if (searchResultsEvent.isEmptyData()) {
            this.mSearchPageView.onEmptyData(R.string.search_no_data_status_title, R.string.search_no_data_status_message);
        } else if (searchResultsEvent.isNetworkError()) {
            this.mSearchPageView.onNetworkError();
        } else {
            this.mSearchPageView.onServerError();
        }
    }

    private void setupResultListView() {
        this.mResultsRecyclerView = (FooterRecyclerView) findViewById(R.id.rv_content);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        this.mResultsRecyclerView.setLayoutManager(linearLayoutManager);
        this.mSearchResultsAdapter = new CommonItemsAdapter(this, new CommonItemsAdapter.ClickInterceptor() {
            public void clickShare(String str, String str2, String str3, View view) {
                String destUrl = SpmProcessor.getDestUrl(str, UTHelper.SearchResultsPage.SPM_VAL_TO_SHARE, true);
                if (!TextUtils.isEmpty(destUrl)) {
                    UTHelper.SearchResultsPage.trackItemShare(str2, str3, str);
                    ShareUtils.showShare(view.getContext(), destUrl);
                }
            }

            public void clickItem(String str, String str2, String str3, View view) {
                String destUrl = SpmProcessor.getDestUrl(str, UTHelper.SearchResultsPage.SPM_VAL_ITEM_TO_SHARE, true);
                if (!TextUtils.isEmpty(destUrl)) {
                    UTHelper.SearchResultsPage.trackItemInfo(str2, str3, str);
                    MoonComponentManager.getInstance().getPageRouter().gotoPage(WebPageIntentGenerator.getItemLandingPage(destUrl));
                }
            }
        });
        this.mResultsRecyclerView.setAdapter(this.mSearchResultsAdapter);
        FooterStatusView footerStatusView = new FooterStatusView(this);
        footerStatusView.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        this.mResultsRecyclerView.setFooterView(footerStatusView);
        this.mResultsRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                if (view.getHeight() > 0 && view.getWidth() > 0) {
                    SearchResultsActivity.this.mResultsRecyclerView.removeOnLayoutChangeListener(this);
                    SearchResultsActivity.this.mSortPanel.setHeight(Math.abs(i4 - i2));
                }
            }
        });
        this.mResultsRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            public void onLoadMore(int i) {
                SearchResultsActivity.this.mSearchRequest.queryNextPage();
            }
        });
    }

    private void setupSortByPanel(@NonNull SearchOptionConfigModel searchOptionConfigModel) {
        this.mSortPanel = new SortCategoryPanel(this);
        this.mSortPanel.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                SearchResultsActivity.this.mSortByDropdown.collapse();
            }
        });
        this.mSortPanel.setLayoutManager(new LinearLayoutManager(this));
        SortCategoryAdapter sortCategoryAdapter = new SortCategoryAdapter();
        sortCategoryAdapter.setSortCategoryClickListener(new SortCategoryAdapter.SortByClickListener() {
            public void onSortCategoryClicked(SearchOptionModel searchOptionModel) {
                if (SearchResultsActivity.this.mSearchRequest != null) {
                    SearchResultsActivity.this.mSearchPageView.onLoading();
                    UTHelper.SearchResultsPage.trackControlClick(searchOptionModel.getControlName());
                    SearchResultsActivity.this.mSearchRequest.setSortByCondition(searchOptionModel);
                    SearchResultsActivity.this.mSortByDropdown.setSortBy(searchOptionModel);
                    SearchResultsActivity.this.mSortPanel.dismiss();
                }
            }
        });
        this.mSortPanel.setAdapter(sortCategoryAdapter);
        this.mSortByDropdown = (SortByDropDown) findViewById(R.id.sort_by_dropdown);
        this.mSortByDropdown.setOnClickListener(this);
        List<SearchOptionModel> sortByOptionList = searchOptionConfigModel.getSortByOptionList();
        sortCategoryAdapter.setSortCategories(sortByOptionList);
        if (!sortByOptionList.isEmpty() && sortByOptionList.get(0) != null) {
            this.mSortByDropdown.setSortBy(sortByOptionList.get(0));
        }
    }

    private void setupSideFilterPanel() {
        this.mFilterPanel = new SearchFilterPanel(this);
        this.mFilterPanel.setOnFilterConfirmListener(new SearchFilterPanel.OnSearchFilterConfirmListener() {
            public void onFilterConfirmed(@NonNull SearchSidePanelFilterParam searchSidePanelFilterParam) {
                SearchResultsActivity.this.mFilterPanel.dismiss();
                SearchResultsActivity.this.mSearchPageView.onLoading();
                SearchResultsActivity.this.mSearchRequest.setFilterParams(searchSidePanelFilterParam);
                SearchResultsActivity.this.mFilterOption.setSelected(searchSidePanelFilterParam.hasFilterOptionsOn());
                SearchResultsActivity.this.mFilterOption.setTextColor(ContextCompat.getColor(SearchResultsActivity.this.mFilterOption.getContext(), searchSidePanelFilterParam.hasFilterOptionsOn() ? R.color.common_red_color : R.color.search_result_title_text_color));
            }
        });
    }

    private void setupTopSearchOptions(@NonNull SearchOptionConfigModel searchOptionConfigModel, @NonNull LinearLayout linearLayout) {
        List<SearchOptionModel> topOptionList = searchOptionConfigModel.getTopOptionList();
        linearLayout.removeAllViews();
        for (SearchOptionModel next : topOptionList) {
            CheckedTextView checkedTextView = new CheckedTextView(this);
            checkedTextView.setText(next.getName());
            checkedTextView.setCheckMarkDrawable((Drawable) null);
            checkedTextView.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.bg_checkmark), (Drawable) null, (Drawable) null, (Drawable) null);
            checkedTextView.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.common_padding_4));
            checkedTextView.setGravity(17);
            checkedTextView.setLayoutParams(new ViewGroup.LayoutParams(-2, -1));
            checkedTextView.setOnClickListener(this.mTopSearchOptionClickListener);
            checkedTextView.setPadding(0, 0, getResources().getDimensionPixelOffset(R.dimen.common_padding_9), 0);
            checkedTextView.setTextColor(ContextCompat.getColor(this, R.color.search_result_title_text_color));
            checkedTextView.setTextSize(0, getResources().getDimension(R.dimen.common_text_size_12));
            linearLayout.addView(checkedTextView);
            checkedTextView.setTag(next);
        }
    }
}
