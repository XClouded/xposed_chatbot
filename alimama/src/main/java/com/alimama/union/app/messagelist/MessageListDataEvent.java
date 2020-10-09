package com.alimama.union.app.messagelist;

import android.text.TextUtils;
import com.alimama.moon.features.search.SearchResultsEvent;
import com.alimama.union.app.messagelist.network.MessageListResponse;

public class MessageListDataEvent {
    public MessageListResponse dataResult;
    public boolean isSuccess;
    public String mRetCode;

    public boolean hasMore() {
        return this.dataResult.isHasMore();
    }

    public boolean isEmptyData() {
        return TextUtils.equals(this.mRetCode, SearchResultsEvent.RET_CODE_EMPTY_DATA_RESULT);
    }

    public boolean isNetworkError() {
        return TextUtils.equals(this.mRetCode, "ANDROID_SYS_NETWORK_ERROR");
    }
}
