package com.alimama.moon.features.search;

import android.text.TextUtils;

public class SearchResultsEvent {
    public static final String RET_CODE_EMPTY_DATA_RESULT = "FAIL_BIZ_EMPTY_RESULT";
    public static final String RET_CODE_NETWORK_ERROR = "ANDROID_SYS_NETWORK_ERROR";
    private final boolean mIsFirstPage;
    private final boolean mIsSuccess;
    private final SearchResponse mResponse;
    private final String mRetCode;

    public SearchResultsEvent(boolean z, boolean z2, String str, SearchResponse searchResponse) {
        this.mIsSuccess = z;
        this.mIsFirstPage = z2;
        this.mRetCode = str;
        this.mResponse = searchResponse;
    }

    public boolean isSuccess() {
        return this.mIsSuccess;
    }

    public boolean isFirstPage() {
        return this.mIsFirstPage;
    }

    public boolean isEmptyData() {
        return TextUtils.equals(this.mRetCode, RET_CODE_EMPTY_DATA_RESULT);
    }

    public boolean isNetworkError() {
        return TextUtils.equals(this.mRetCode, "ANDROID_SYS_NETWORK_ERROR");
    }

    public SearchResponse getResponse() {
        return this.mResponse;
    }
}
