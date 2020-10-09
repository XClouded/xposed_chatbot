package com.alimama.moon.features.home.network;

import android.text.TextUtils;
import com.alimama.moon.features.search.SearchResultsEvent;

public class HomeDataEvent {
    private static final String ERROR_NETWORK_AVAILABLE = "error_network_available";
    public boolean isSuccess;
    public HomeDataResponse mHomeData;
    public int mPosition;
    public String mRetCode;

    public boolean hasMore() {
        return this.mHomeData.isHasMore();
    }

    public boolean isEmptyData() {
        return TextUtils.equals(this.mRetCode, SearchResultsEvent.RET_CODE_EMPTY_DATA_RESULT);
    }

    public boolean isNetworkError() {
        return TextUtils.equals(this.mRetCode, "ANDROID_SYS_NETWORK_ERROR") || TextUtils.equals(this.mRetCode, ERROR_NETWORK_AVAILABLE);
    }
}
