package com.alimama.union.app.infrastructure.socialShare.social;

import com.alimama.union.app.infrastructure.socialShare.ShareObj;
import java.util.List;

public interface SocialShare {

    public enum ErrorCode {
        IMAGE_PROCESS_ERROR,
        NOT_INSTALLED_ERROR
    }

    public interface ShareCallback {
        void onFailure(ErrorCode errorCode);

        void onSuccess();
    }

    void doShare(ShareObj shareObj, String str, List<String> list, String str2, ShareCallback shareCallback);
}
