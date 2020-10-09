package com.alimama.moon.features.search;

import android.text.TextUtils;
import androidx.core.app.NotificationCompat;
import com.ali.user.mobile.rpc.ApiConstants;
import com.alimama.moon.utils.UnionLensUtil;
import com.alimama.union.app.configproperties.EnvHelper;
import com.alimama.union.app.rxnetwork.ApiInfo;
import com.alimama.union.app.rxnetwork.RxMtopRequest;
import com.alimama.union.app.rxnetwork.RxMtopResponse;
import com.alimama.union.app.rxnetwork.RxPageRequest;
import com.alimamaunion.base.safejson.SafeJSONObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.greenrobot.eventbus.EventBus;

public class SearchRequest extends RxPageRequest<SearchResponse> implements RxMtopRequest.RxMtopResult<SearchResponse> {
    private static final int PAGE_SIZE = 20;
    private int mCurrentPageNum;
    private final SearchVariableParam mVariableParam = new SearchVariableParam();

    private static class SearchVariableParam {
        /* access modifiers changed from: private */
        public final Set<SearchOptionModel> extraFilters;
        /* access modifiers changed from: private */
        public String queryString;
        /* access modifiers changed from: private */
        public SearchSidePanelFilterParam sidePanelFilterParam;
        /* access modifiers changed from: private */
        public String sortBy;

        private SearchVariableParam() {
            this.extraFilters = new HashSet();
        }
    }

    public SearchRequest(String str, SearchOptionModel searchOptionModel) {
        super(ApiInfo.SEARCH_API);
        enablePost(true);
        String unused = this.mVariableParam.queryString = str;
        if (searchOptionModel != null) {
            String unused2 = this.mVariableParam.sortBy = searchOptionModel.getCondition();
        }
        appendParam("pNum", "0");
        appendParam("pSize", String.valueOf(20));
        appendParam("extendParam", UnionLensUtil.geneRidPvidJsonStr());
        if (EnvHelper.getInstance().isOnLineEnv()) {
            appendParam("qieId", "211");
        } else {
            appendParam("qieId", "15328");
        }
        setRxMtopResult(this);
        prepareParams();
    }

    public void setSortByCondition(SearchOptionModel searchOptionModel) {
        String unused = this.mVariableParam.sortBy = searchOptionModel == null ? "" : searchOptionModel.getCondition();
        prepareParams();
        queryFirstPage();
    }

    public void setFilterParams(SearchSidePanelFilterParam searchSidePanelFilterParam) {
        SearchSidePanelFilterParam unused = this.mVariableParam.sidePanelFilterParam = searchSidePanelFilterParam;
        prepareParams();
        queryFirstPage();
    }

    public void addSearchFilter(SearchOptionModel searchOptionModel) {
        if (searchOptionModel != null) {
            this.mVariableParam.extraFilters.add(searchOptionModel);
            prepareParams();
            queryFirstPage();
        }
    }

    public void removeSearchFilter(SearchOptionModel searchOptionModel) {
        if (searchOptionModel != null) {
            this.mVariableParam.extraFilters.remove(searchOptionModel);
            prepareParams();
            queryFirstPage();
        }
    }

    private void prepareParams() {
        SafeJSONObject safeJSONObject = new SafeJSONObject();
        safeJSONObject.put("q", this.mVariableParam.queryString);
        if (!TextUtils.isEmpty(this.mVariableParam.sortBy)) {
            safeJSONObject.put("sort", this.mVariableParam.sortBy);
        }
        ArrayList<SearchOptionModel> arrayList = new ArrayList<>(this.mVariableParam.extraFilters);
        if (this.mVariableParam.sidePanelFilterParam != null) {
            arrayList.addAll(this.mVariableParam.sidePanelFilterParam.getSelectedServices());
        }
        StringBuilder sb = new StringBuilder();
        for (SearchOptionModel condition : arrayList) {
            String condition2 = condition.getCondition();
            if (!TextUtils.isEmpty(condition2)) {
                String[] split = condition2.split(":");
                if (split.length == 2) {
                    if (split[0].equalsIgnoreCase(NotificationCompat.CATEGORY_SERVICE)) {
                        sb.append(split[1]);
                        sb.append(",");
                    } else {
                        safeJSONObject.put(split[0], split[1]);
                    }
                }
            }
        }
        String sb2 = sb.toString();
        if (!TextUtils.isEmpty(sb2)) {
            safeJSONObject.put(NotificationCompat.CATEGORY_SERVICE, sb2);
        }
        updateCommissionRange(safeJSONObject);
        updatePriceRange(safeJSONObject);
        safeJSONObject.put("highlight", false);
        safeJSONObject.put("terminal", ApiConstants.ApiField.MOBILE);
        appendParam("variableMap", safeJSONObject.toString());
    }

    private void updateCommissionRange(SafeJSONObject safeJSONObject) {
        if (this.mVariableParam.sidePanelFilterParam != null) {
            SearchSidePanelFilterParam access$300 = this.mVariableParam.sidePanelFilterParam;
            if (!TextUtils.isEmpty(access$300.getMinCommissionParamVal()) || !TextUtils.isEmpty(access$300.getMaxCommissionParamVal())) {
                safeJSONObject.put("tk_rate", String.format("%s~%s", new Object[]{access$300.getMinCommissionParamVal(), access$300.getMaxCommissionParamVal()}));
            }
        }
    }

    private void updatePriceRange(SafeJSONObject safeJSONObject) {
        if (this.mVariableParam.sidePanelFilterParam != null) {
            SearchSidePanelFilterParam access$300 = this.mVariableParam.sidePanelFilterParam;
            if (!TextUtils.isEmpty(access$300.getMinPrice()) || !TextUtils.isEmpty(access$300.getMaxPrice())) {
                safeJSONObject.put("price", String.format("%s~%s", new Object[]{access$300.getMinPrice(), access$300.getMaxPrice()}));
            }
        }
    }

    public SearchResponse decodeResult(SafeJSONObject safeJSONObject) {
        return SearchResponse.fromJson(safeJSONObject.optJSONObject("data"));
    }

    public void result(RxMtopResponse<SearchResponse> rxMtopResponse) {
        clearLoadingState();
        boolean z = false;
        setHasMore(rxMtopResponse.isReqSuccess && rxMtopResponse.result != null && ((SearchResponse) rxMtopResponse.result).getTotalCount() > (this.mCurrentPageNum + 1) * 20);
        EventBus eventBus = EventBus.getDefault();
        boolean z2 = rxMtopResponse.isReqSuccess && rxMtopResponse.result != null;
        if (this.mCurrentPageNum == 0) {
            z = true;
        }
        eventBus.post(new SearchResultsEvent(z2, z, rxMtopResponse.retCode, (SearchResponse) rxMtopResponse.result));
    }

    /* access modifiers changed from: protected */
    public void prepareNextParams(Map<String, String> map) {
        this.mCurrentPageNum++;
        appendParam("pNum", String.valueOf(this.mCurrentPageNum));
    }

    /* access modifiers changed from: protected */
    public void prepareFirstParams(Map<String, String> map) {
        this.mCurrentPageNum = 0;
        appendParam("pNum", String.valueOf(this.mCurrentPageNum));
    }
}
