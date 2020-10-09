package com.alimama.moon.features.search;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alimama.moon.App;
import com.alimama.moon.R;
import com.alimama.moon.features.search.SearchRealTimeSuggestAdapter;
import com.alimama.moon.features.search.network.SearchHotTagEvent;
import com.alimama.moon.features.search.network.SearchHotTagItem;
import com.alimama.moon.features.search.network.SearchHotTagRequest;
import com.alimama.moon.features.search.network.SearchRealTimeSugEvent;
import com.alimama.moon.features.search.network.SearchRealTimeSugRequest;
import com.alimama.moon.ui.BaseActivity;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.moon.utils.ISSharedPreferences;
import com.alimama.moon.utils.SpmProcessor;
import com.alimama.moon.utils.ToastUtil;
import com.alimama.moon.view.ClearableEditTextView;
import com.alimama.moon.web.WebPageIntentGenerator;
import com.alimama.union.app.pagerouter.AppPageInfo;
import com.alimama.union.app.pagerouter.IUTPage;
import com.alimama.union.app.pagerouter.MoonComponentManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SearchInputActivity extends BaseActivity implements View.OnClickListener, IUTPage {
    private static final int MAX_HISTORY_TAG = 10;
    private static final String SEARCH_HISTORY_TAG = "search_history_tag";
    public static final String SEARCH_RESULT_KEYWORD = "keyword";
    private static final String SEARCH_RESULT_TYPE = "type";
    private static final String TAG_GOODS = "goods";
    private static final String TAG_STORE = "store";
    private SearchRealTimeSuggestAdapter mAdapter;
    private TextView mBack;
    private View mDeleteView;
    private TextView mGoodsText;
    private View mHistoryContainer;
    private FlowLayout mHistoryTagGroups;
    private View mHotTagContainer;
    private FlowLayout mHotTagGroups;
    private PopupWindow mPopWindow;
    /* access modifiers changed from: private */
    public RecyclerView mRealTimeSuggest;
    /* access modifiers changed from: private */
    public TextView mSearchChoose;
    /* access modifiers changed from: private */
    public ImageView mSearchChooseImg;
    /* access modifiers changed from: private */
    public ClearableEditTextView mSearchInputEt;
    private TextView mStoreText;
    private View mTagContainer;
    private List<String> realTimeSugList = new ArrayList();

    public String getCurrentPageName() {
        return UTHelper.SearchInput.PAGE_NAME;
    }

    public String getCurrentSpmCnt() {
        return UTHelper.SearchInput.SPM_CNT;
    }

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        App.getAppComponent().inject((BaseActivity) this);
        initView();
        new SearchHotTagRequest().sendRequest();
        inflateHistoryTagView();
    }

    private void initView() {
        setContentView((int) R.layout.search_layout_input_content);
        this.mBack = (TextView) findViewById(R.id.search_input_clear_tv);
        this.mBack.setOnClickListener(this);
        this.mSearchChoose = (TextView) findViewById(R.id.search_input_choose_tv);
        this.mSearchChoose.setOnClickListener(this);
        this.mSearchChoose.setTag(TAG_GOODS);
        this.mSearchChooseImg = (ImageView) findViewById(R.id.search_input_choose_img);
        this.mSearchInputEt = (ClearableEditTextView) findViewById(R.id.search_input_et);
        this.mSearchInputEt.setOnClickListener(this);
        this.mHotTagContainer = findViewById(R.id.hot_search_container);
        this.mHistoryContainer = findViewById(R.id.history_container);
        this.mHistoryTagGroups = (FlowLayout) findViewById(R.id.search_history_tag_container);
        this.mHotTagGroups = (FlowLayout) findViewById(R.id.search_hot_suggest_tag_container);
        this.mDeleteView = findViewById(R.id.search_history_clear);
        this.mDeleteView.setOnClickListener(this);
        this.mTagContainer = findViewById(R.id.tag_container);
        this.mRealTimeSuggest = (RecyclerView) findViewById(R.id.search_realtime_suggest);
        this.mRealTimeSuggest.setLayoutManager(new LinearLayoutManager(this));
        this.mAdapter = new SearchRealTimeSuggestAdapter(this);
        this.mAdapter.setOnItemClickLitener(new SearchRealTimeSuggestAdapter.OnItemClickListener() {
            public void onItemClick(String str) {
                UTHelper.SearchInput.clickRealtimeSug();
                SearchInputActivity.this.gotoSearchResults(str, SearchInputActivity.this.mSearchChoose.getText().toString(), SearchInputActivity.this.mSearchChoose.getTag().toString());
                SearchInputActivity.this.updateHistoryTag(str);
            }
        });
        this.mRealTimeSuggest.setAdapter(this.mAdapter);
        this.mSearchInputEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (keyEvent == null || keyEvent.getAction() != 0) {
                    return false;
                }
                if (i != 3 && i != 0) {
                    return false;
                }
                String trim = SearchInputActivity.this.mSearchInputEt.getText() != null ? SearchInputActivity.this.mSearchInputEt.getText().toString().trim() : "";
                String charSequence = TextUtils.isEmpty(SearchInputActivity.this.mSearchInputEt.getHint()) ? "" : SearchInputActivity.this.mSearchInputEt.getHint().toString();
                String obj = SearchInputActivity.this.mSearchChoose.getTag() != null ? SearchInputActivity.this.mSearchChoose.getTag().toString() : SearchInputActivity.TAG_GOODS;
                SearchInputActivity.this.addSpmclick(obj);
                if (TextUtils.isEmpty(trim) && TextUtils.isEmpty(charSequence)) {
                    ToastUtil.showToast((Context) SearchInputActivity.this, SearchInputActivity.this.getResources().getString(R.string.search_input_keyword_empty));
                    return false;
                } else if (!TextUtils.isEmpty(trim) || TextUtils.isEmpty(charSequence)) {
                    SearchInputActivity.this.gotoSearchResults(trim, SearchInputActivity.this.mSearchChoose.getText().toString(), obj);
                    SearchInputActivity.this.updateHistoryTag(trim);
                    return false;
                } else {
                    SearchInputActivity.this.gotoSearchResults(charSequence, SearchInputActivity.this.mSearchChoose.getText().toString(), obj);
                    SearchInputActivity.this.updateHistoryTag(charSequence);
                    return false;
                }
            }
        });
        this.mSearchInputEt.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                String trim = charSequence.toString().trim();
                if (TextUtils.isEmpty(trim)) {
                    SearchInputActivity.this.mRealTimeSuggest.setVisibility(8);
                } else {
                    new SearchRealTimeSugRequest(trim).sendRequest();
                }
            }
        });
        this.mRealTimeSuggest.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                InputMethodManager inputMethodManager;
                if (i != 0 && (inputMethodManager = (InputMethodManager) SearchInputActivity.this.getSystemService("input_method")) != null) {
                    inputMethodManager.hideSoftInputFromWindow(SearchInputActivity.this.getWindow().getDecorView().getWindowToken(), 0);
                }
            }
        });
        handleIntent(getIntent());
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        inflateHistoryTagView();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goods_tv /*2131695504*/:
                UTHelper.SearchInput.clickSwitchSearchType();
                this.mSearchChoose.setTag(TAG_GOODS);
                setPopWindowStyle();
                this.mPopWindow.dismiss();
                this.mTagContainer.setVisibility(0);
                return;
            case R.id.store_tv /*2131695505*/:
                UTHelper.SearchInput.clickSwitchSearchType();
                this.mSearchChoose.setTag(TAG_STORE);
                setPopWindowStyle();
                this.mPopWindow.dismiss();
                this.mTagContainer.setVisibility(8);
                return;
            case R.id.search_input_choose_tv /*2131695507*/:
                UTHelper.SearchInput.clickSwitchSearchType();
                this.mSearchChooseImg.setBackground(getResources().getDrawable(R.drawable.search_up));
                showPopwindow();
                return;
            case R.id.search_input_clear_tv /*2131695510*/:
                UTHelper.SearchInput.clickCancel();
                finish();
                return;
            case R.id.search_history_clear /*2131695514*/:
                UTHelper.SearchInput.clickClearHistory();
                if (this.mHistoryTagGroups != null) {
                    this.mHistoryTagGroups.removeAllViews();
                }
                deleteHistoryTag();
                this.mHistoryContainer.setVisibility(8);
                return;
            default:
                return;
        }
    }

    private void handleIntent(@NonNull Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String string = extras.getString(SEARCH_RESULT_KEYWORD);
            if (!TextUtils.isEmpty(string)) {
                this.mSearchInputEt.setText(string);
                this.mSearchInputEt.setSelection(string.length());
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService("input_method");
                if (inputMethodManager != null) {
                    inputMethodManager.toggleSoftInput(1, 2);
                }
            }
        }
    }

    private void showPopwindow() {
        View inflate = getLayoutInflater().inflate(R.layout.search_input_popwindow_layout, (ViewGroup) null);
        this.mStoreText = (TextView) inflate.findViewById(R.id.store_tv);
        this.mGoodsText = (TextView) inflate.findViewById(R.id.goods_tv);
        setPopWindowStyle();
        this.mGoodsText.setOnClickListener(this);
        this.mStoreText.setOnClickListener(this);
        this.mPopWindow = new PopupWindow(inflate, -2, -2);
        this.mPopWindow.setTouchable(true);
        this.mPopWindow.setFocusable(true);
        this.mPopWindow.setOutsideTouchable(true);
        this.mPopWindow.showAsDropDown(this.mSearchChoose, 0, 40);
        this.mPopWindow.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() != 0) {
                    return false;
                }
                SearchInputActivity.this.mSearchChooseImg.setBackground(SearchInputActivity.this.getResources().getDrawable(R.drawable.search_drop));
                return false;
            }
        });
    }

    private void setPopWindowStyle() {
        if (TextUtils.equals(this.mSearchChoose.getTag().toString(), TAG_GOODS)) {
            this.mGoodsText.setBackgroundColor(getResources().getColor(R.color.search_input_popwindow_color));
            this.mGoodsText.setTextColor(getResources().getColor(R.color.search_input_choose_text_color));
            this.mStoreText.setBackgroundColor(getResources().getColor(R.color.white));
            this.mStoreText.setTextColor(getResources().getColor(R.color.search_input_text_color));
            this.mSearchChoose.setText(getResources().getString(R.string.search_goods));
            return;
        }
        this.mStoreText.setBackgroundColor(getResources().getColor(R.color.search_input_popwindow_color));
        this.mStoreText.setTextColor(getResources().getColor(R.color.search_input_choose_text_color));
        this.mGoodsText.setBackgroundColor(getResources().getColor(R.color.white));
        this.mGoodsText.setTextColor(getResources().getColor(R.color.search_input_text_color));
        this.mSearchChoose.setText(getResources().getString(R.string.search_store));
    }

    private void inflateHotTagView(List<SearchHotTagItem> list) {
        this.mHotTagGroups.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            final SearchHotTagItem searchHotTagItem = list.get(i);
            TextView textView = (TextView) LayoutInflater.from(this).inflate(R.layout.search_input_hot_tag_layout, (ViewGroup) null);
            if (!TextUtils.isEmpty(searchHotTagItem.getUrl())) {
                textView.setTextColor(getResources().getColor(R.color.common_red_color));
            }
            textView.setText(searchHotTagItem.getWord());
            textView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    UTHelper.SearchInput.clickRecoQuery();
                    if (TextUtils.isEmpty(searchHotTagItem.getUrl())) {
                        SearchInputActivity.this.gotoSearchResults(searchHotTagItem.getWord(), SearchInputActivity.this.mSearchChoose.getText().toString(), SearchInputActivity.this.mSearchChoose.getTag().toString());
                    } else {
                        MoonComponentManager.getInstance().getPageRouter().gotoPage(SpmProcessor.appendSpm(searchHotTagItem.getUrl(), UTHelper.SearchInput.SPM_CNT));
                    }
                    SearchInputActivity.this.updateHistoryTag(searchHotTagItem.getWord());
                }
            });
            this.mHotTagGroups.addView(textView);
        }
    }

    private void inflateHistoryTagView() {
        this.mHistoryTagGroups.removeAllViews();
        String string = new ISSharedPreferences(SEARCH_HISTORY_TAG).getString(SEARCH_HISTORY_TAG, "");
        if (TextUtils.isEmpty(string)) {
            this.mHistoryContainer.setVisibility(8);
            return;
        }
        this.mHistoryContainer.setVisibility(0);
        List list = (List) new Gson().fromJson(string, new TypeToken<List<String>>() {
        }.getType());
        for (int i = 0; i < list.size(); i++) {
            final String str = (String) list.get(i);
            TextView textView = (TextView) LayoutInflater.from(this).inflate(R.layout.search_input_hot_tag_layout, (ViewGroup) null);
            textView.setText(str);
            textView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    SearchInputActivity.this.gotoSearchResults(str, SearchInputActivity.this.mSearchChoose.getText().toString(), SearchInputActivity.this.mSearchChoose.getTag().toString());
                    SearchInputActivity.this.updateHistoryTag(str);
                    UTHelper.SearchInput.searchHistoryQuery();
                }
            });
            this.mHistoryTagGroups.addView(textView);
        }
    }

    @Subscribe
    public void onSearchHotTagEvent(SearchHotTagEvent searchHotTagEvent) {
        if (!searchHotTagEvent.isSuccess || searchHotTagEvent.dataResult == null || searchHotTagEvent.dataResult.hotTagItems == null || searchHotTagEvent.dataResult.hotTagItems.size() == 0) {
            this.mHotTagContainer.setVisibility(8);
            return;
        }
        this.mHotTagContainer.setVisibility(0);
        this.mSearchInputEt.setHint(searchHotTagEvent.dataResult.hotTagItems.get(0).getWord());
        inflateHotTagView(searchHotTagEvent.dataResult.hotTagItems);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchRealTimeSugEvent(SearchRealTimeSugEvent searchRealTimeSugEvent) {
        this.mRealTimeSuggest.setVisibility(0);
        this.realTimeSugList.clear();
        this.realTimeSugList.addAll(searchRealTimeSugEvent.dataResult.mList);
        this.mAdapter.setData(this.realTimeSugList);
        this.mAdapter.notifyDataSetChanged();
    }

    /* access modifiers changed from: private */
    public void updateHistoryTag(String str) {
        if (!TextUtils.isEmpty(str)) {
            String trim = str.trim();
            ISSharedPreferences iSSharedPreferences = new ISSharedPreferences(SEARCH_HISTORY_TAG);
            String string = iSSharedPreferences.getString(SEARCH_HISTORY_TAG, "");
            Gson gson = new Gson();
            List list = (List) gson.fromJson(string, new TypeToken<List<String>>() {
            }.getType());
            if (list == null) {
                list = new ArrayList();
            }
            for (int i = 0; i < list.size(); i++) {
                if (((String) list.get(i)).equals(trim)) {
                    list.remove(i);
                }
            }
            list.add(0, trim);
            if (list.size() > 10) {
                list = list.subList(0, 10);
            }
            iSSharedPreferences.putString(SEARCH_HISTORY_TAG, gson.toJson((Object) list)).commit();
        }
    }

    private void deleteHistoryTag() {
        new ISSharedPreferences(SEARCH_HISTORY_TAG).putString(SEARCH_HISTORY_TAG, "").commit();
    }

    /* access modifiers changed from: private */
    public void gotoSearchResults(String str, String str2, String str3) {
        if (TextUtils.equals(str3, TAG_GOODS)) {
            HashMap hashMap = new HashMap();
            hashMap.put(SEARCH_RESULT_KEYWORD, str);
            hashMap.put("type", str2);
            hashMap.put("spm", UTHelper.SearchInput.SPM_CNT);
            MoonComponentManager.getInstance().getPageRouter().gotoPage(SpmProcessor.createPageRouterUrl(AppPageInfo.PAGE_NAME_SEARCH_RESULTS, hashMap));
            return;
        }
        MoonComponentManager.getInstance().getPageRouter().gotoPage(WebPageIntentGenerator.getSearchResultStoreUrl().concat(Uri.encode(str)).concat("&spm=").concat(UTHelper.SearchInput.SPM_CNT));
    }

    /* access modifiers changed from: private */
    public void addSpmclick(String str) {
        if (TextUtils.equals(str, TAG_GOODS)) {
            UTHelper.SearchInput.searchItems();
        } else {
            UTHelper.SearchInput.searchShops();
        }
    }
}
