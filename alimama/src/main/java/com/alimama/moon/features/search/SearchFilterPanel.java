package com.alimama.moon.features.search;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import com.alimama.moon.R;
import com.alimama.moon.features.search.FlowLayout;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.union.app.configcenter.ConfigKeyList;
import com.alimamaunion.base.configcenter.EtaoConfigCenter;
import java.math.BigDecimal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchFilterPanel extends RelativeLayout implements View.OnClickListener {
    private static final BigDecimal MAX_COMMISSION = new BigDecimal(100);
    /* access modifiers changed from: private */
    public static final Logger logger = LoggerFactory.getLogger((Class<?>) SearchFilterPanel.class);
    private final View.OnFocusChangeListener mEditFocusChangeListener;
    @Nullable
    private OnSearchFilterConfirmListener mFilterConfirmListener;
    private final SearchSidePanelFilterParam mFilterParam;
    private EditText mMaxCommissionEdit;
    private EditText mMaxPriceEdit;
    private EditText mMinCommissionEdit;
    private EditText mMinPriceEdit;
    private PopupWindow mPopupWindow;
    private final View.OnClickListener mServiceOptionClickListener;
    private FlowLayout mServiceOptionsContainer;

    public interface OnSearchFilterConfirmListener {
        void onFilterConfirmed(@NonNull SearchSidePanelFilterParam searchSidePanelFilterParam);
    }

    public SearchFilterPanel(Context context) {
        this(context, (AttributeSet) null);
    }

    public SearchFilterPanel(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SearchFilterPanel(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mFilterParam = new SearchSidePanelFilterParam();
        this.mServiceOptionClickListener = new View.OnClickListener() {
            public void onClick(View view) {
                if (!(view instanceof SearchServiceOptionView)) {
                    SearchFilterPanel.logger.warn("invalid view instance {} for service option click handler", (Object) view);
                    return;
                }
                SearchServiceOptionView searchServiceOptionView = (SearchServiceOptionView) view;
                SearchOptionModel searchOption = searchServiceOptionView.getSearchOption();
                if (searchOption == null) {
                    SearchFilterPanel.logger.warn("missing required filter data");
                    return;
                }
                searchServiceOptionView.setSelected(!searchServiceOptionView.isSelected());
                UTHelper.SearchResultsPage.trackControlClick(searchOption.getControlName());
            }
        };
        this.mEditFocusChangeListener = new View.OnFocusChangeListener() {
            public void onFocusChange(View view, boolean z) {
                if (z) {
                    switch (view.getId()) {
                        case R.id.edt_min_price:
                            UTHelper.SearchResultsPage.trackEditMinPrice();
                            return;
                        case R.id.edt_max_price:
                            UTHelper.SearchResultsPage.trackEditMaxPrice();
                            return;
                        case R.id.edt_min_commission:
                            UTHelper.SearchResultsPage.trackEditMinCommission();
                            return;
                        case R.id.edt_max_commission:
                            UTHelper.SearchResultsPage.trackEditMaxCommission();
                            return;
                        default:
                            return;
                    }
                }
            }
        };
        initViews(context);
    }

    private void initViews(@NonNull Context context) {
        inflate(context, R.layout.merge_search_filter_panel, this);
        setLayoutParams(new ViewGroup.LayoutParams(-2, -1));
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        this.mServiceOptionsContainer = (FlowLayout) findViewById(R.id.ll_services_container);
        this.mMinPriceEdit = (EditText) findViewById(R.id.edt_min_price);
        this.mMaxPriceEdit = (EditText) findViewById(R.id.edt_max_price);
        this.mMinCommissionEdit = (EditText) findViewById(R.id.edt_min_commission);
        this.mMaxCommissionEdit = (EditText) findViewById(R.id.edt_max_commission);
        this.mMinPriceEdit.setOnFocusChangeListener(this.mEditFocusChangeListener);
        this.mMaxPriceEdit.setOnFocusChangeListener(this.mEditFocusChangeListener);
        this.mMinCommissionEdit.setOnFocusChangeListener(this.mEditFocusChangeListener);
        this.mMaxCommissionEdit.setOnFocusChangeListener(this.mEditFocusChangeListener);
        findViewById(R.id.btn_reset_filters).setOnClickListener(this);
        findViewById(R.id.btn_confirm_filters).setOnClickListener(this);
        int dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.common_padding_12);
        setPadding(dimensionPixelOffset, 0, dimensionPixelOffset, dimensionPixelOffset);
        this.mPopupWindow = new PopupWindow(this, -2, -1, true);
        this.mPopupWindow.setFocusable(true);
        this.mPopupWindow.setTouchable(true);
        this.mPopupWindow.setOutsideTouchable(true);
        this.mPopupWindow.setInputMethodMode(0);
        this.mPopupWindow.setSoftInputMode(32);
        this.mPopupWindow.setAnimationStyle(R.style.SearchFilterPanelAnimStyle);
        this.mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                if (SearchFilterPanel.this.getContext() instanceof Activity) {
                    Activity activity = (Activity) SearchFilterPanel.this.getContext();
                    WindowManager.LayoutParams attributes = activity.getWindow().getAttributes();
                    attributes.alpha = 1.0f;
                    activity.getWindow().setAttributes(attributes);
                }
            }
        });
        configServices(SearchOptionConfigModel.fromJsonJson(EtaoConfigCenter.getInstance().getConfigResult(ConfigKeyList.UNION_SEARCH_OPTIONS)).getSidePanelFilters());
    }

    public void slideIn(View view, Activity activity) {
        WindowManager.LayoutParams attributes = activity.getWindow().getAttributes();
        attributes.alpha = 0.5f;
        activity.getWindow().setAttributes(attributes);
        activity.getWindow().addFlags(2);
        this.mPopupWindow.showAtLocation(view, GravityCompat.END, 0, 0);
        updateViewFromFilterParam();
    }

    private void configServices(List<SearchOptionModel> list) {
        if (list == null || list.isEmpty()) {
            this.mServiceOptionsContainer.setVisibility(8);
            return;
        }
        this.mServiceOptionsContainer.setVisibility(0);
        this.mServiceOptionsContainer.removeAllViews();
        int dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.search_filter_option_width);
        int dimensionPixelOffset2 = getResources().getDimensionPixelOffset(R.dimen.search_filter_option_height);
        for (SearchOptionModel searchOption : list) {
            SearchServiceOptionView searchServiceOptionView = new SearchServiceOptionView(getContext());
            searchServiceOptionView.setTextSize(2, 12.0f);
            searchServiceOptionView.setLayoutParams(new FlowLayout.LayoutParams(dimensionPixelOffset, dimensionPixelOffset2));
            searchServiceOptionView.setSearchOption(searchOption);
            searchServiceOptionView.setOnClickListener(this.mServiceOptionClickListener);
            this.mServiceOptionsContainer.addView(searchServiceOptionView);
        }
    }

    public void setOnFilterConfirmListener(OnSearchFilterConfirmListener onSearchFilterConfirmListener) {
        this.mFilterConfirmListener = onSearchFilterConfirmListener;
    }

    public void dismiss() {
        this.mPopupWindow.dismiss();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reset_filters:
                resetSearchFilters();
                return;
            case R.id.btn_confirm_filters:
                saveSelectedOptions();
                validateCommission();
                validatePrice();
                if (this.mFilterConfirmListener != null) {
                    this.mFilterConfirmListener.onFilterConfirmed(this.mFilterParam);
                }
                UTHelper.SearchResultsPage.trackConfirmFilters();
                return;
            default:
                return;
        }
    }

    private void updateViewFromFilterParam() {
        this.mMaxCommissionEdit.setText(this.mFilterParam.getMaxCommission());
        this.mMinCommissionEdit.setText(this.mFilterParam.getMinCommission());
        this.mMaxPriceEdit.setText(this.mFilterParam.getMaxPrice());
        this.mMinPriceEdit.setText(this.mFilterParam.getMinPrice());
        List<SearchOptionModel> selectedServices = this.mFilterParam.getSelectedServices();
        for (int i = 0; i < this.mServiceOptionsContainer.getChildCount(); i++) {
            if (this.mServiceOptionsContainer.getChildAt(i) instanceof SearchServiceOptionView) {
                SearchServiceOptionView searchServiceOptionView = (SearchServiceOptionView) this.mServiceOptionsContainer.getChildAt(i);
                searchServiceOptionView.setSelected(selectedServices.contains(searchServiceOptionView.getSearchOption()));
            }
        }
    }

    private void saveSelectedOptions() {
        List<SearchOptionModel> selectedServices = this.mFilterParam.getSelectedServices();
        selectedServices.clear();
        for (int i = 0; i < this.mServiceOptionsContainer.getChildCount(); i++) {
            if (this.mServiceOptionsContainer.getChildAt(i) instanceof SearchServiceOptionView) {
                SearchServiceOptionView searchServiceOptionView = (SearchServiceOptionView) this.mServiceOptionsContainer.getChildAt(i);
                if (searchServiceOptionView.isSelected()) {
                    selectedServices.add(searchServiceOptionView.getSearchOption());
                }
            }
        }
    }

    private void validatePrice() {
        BigDecimal bigDecimalFromEditText = getBigDecimalFromEditText(this.mMinPriceEdit);
        BigDecimal bigDecimalFromEditText2 = getBigDecimalFromEditText(this.mMaxPriceEdit);
        if (bigDecimalFromEditText == null || bigDecimalFromEditText2 == null || bigDecimalFromEditText2.compareTo(bigDecimalFromEditText) >= 0) {
            BigDecimal bigDecimal = bigDecimalFromEditText2;
            bigDecimalFromEditText2 = bigDecimalFromEditText;
            bigDecimalFromEditText = bigDecimal;
        }
        this.mFilterParam.setMaxPrice(bigDecimalFromEditText);
        this.mFilterParam.setMinPrice(bigDecimalFromEditText2);
    }

    private void validateCommission() {
        BigDecimal bigDecimalFromEditText = getBigDecimalFromEditText(this.mMinCommissionEdit);
        BigDecimal bigDecimalFromEditText2 = getBigDecimalFromEditText(this.mMaxCommissionEdit);
        if (bigDecimalFromEditText == null || bigDecimalFromEditText2 == null || bigDecimalFromEditText2.compareTo(bigDecimalFromEditText) >= 0) {
            BigDecimal bigDecimal = bigDecimalFromEditText2;
            bigDecimalFromEditText2 = bigDecimalFromEditText;
            bigDecimalFromEditText = bigDecimal;
        }
        if (bigDecimalFromEditText != null && bigDecimalFromEditText.compareTo(MAX_COMMISSION) > 0) {
            bigDecimalFromEditText = MAX_COMMISSION;
        }
        if (bigDecimalFromEditText2 != null && bigDecimalFromEditText2.compareTo(MAX_COMMISSION) > 0) {
            bigDecimalFromEditText2 = MAX_COMMISSION;
        }
        this.mFilterParam.setMaxCommission(bigDecimalFromEditText);
        this.mFilterParam.setMinCommission(bigDecimalFromEditText2);
    }

    private void resetSearchFilters() {
        for (int i = 0; i < this.mServiceOptionsContainer.getChildCount(); i++) {
            if (this.mServiceOptionsContainer.getChildAt(i) instanceof SearchServiceOptionView) {
                ((SearchServiceOptionView) this.mServiceOptionsContainer.getChildAt(i)).setSelected(false);
            }
        }
        this.mMaxCommissionEdit.setText("");
        this.mMinCommissionEdit.setText("");
        this.mMaxPriceEdit.setText("");
        this.mMinPriceEdit.setText("");
    }

    @Nullable
    private BigDecimal getBigDecimalFromEditText(EditText editText) {
        Editable text = editText.getText();
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        return new BigDecimal(text.toString());
    }
}
