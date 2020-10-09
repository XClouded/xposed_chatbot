package com.alibaba.ut.abtest.track;

import android.net.Uri;
import android.text.TextUtils;

public class TrackUriMapper {
    private long groupId;
    private String pageName;
    private Uri uri;

    public Uri getUri() {
        return this.uri;
    }

    public void setUri(Uri uri2) {
        this.uri = uri2;
    }

    public String getPageName() {
        return this.pageName;
    }

    public void setPageName(String str) {
        this.pageName = str;
    }

    public long getGroupId() {
        return this.groupId;
    }

    public void setGroupId(long j) {
        this.groupId = j;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TrackUriMapper)) {
            return false;
        }
        TrackUriMapper trackUriMapper = (TrackUriMapper) obj;
        if (trackUriMapper.groupId == this.groupId) {
            return TextUtils.equals(this.pageName, trackUriMapper.pageName);
        }
        return false;
    }

    public int hashCode() {
        if (this.pageName == null) {
            return super.hashCode() + ((int) this.groupId);
        }
        return this.pageName.hashCode() + ((int) this.groupId);
    }
}
