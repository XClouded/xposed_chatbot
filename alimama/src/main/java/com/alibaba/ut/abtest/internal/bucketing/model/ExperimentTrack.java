package com.alibaba.ut.abtest.internal.bucketing.model;

import android.text.TextUtils;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.ut.abtest.track.TrackId;
import com.taobao.weex.el.parse.Operators;
import java.io.Serializable;

public class ExperimentTrack implements Serializable {
    private static final long serialVersionUID = -4060896856005218741L;
    @JSONField(name = "global")
    private boolean appScope;
    @JSONField(name = "eventIds")
    private int[] eventIds;
    @JSONField(serialize = false)
    private long groupId;
    @JSONField(name = "pageNames")
    private String[] pageNames;
    @JSONField(serialize = false)
    private TrackId trackId;

    public static class PatternMatcher {
        private boolean ignoreBegin;
        private boolean ignoreEnd;
        private String pattern;

        public PatternMatcher(String str) {
            this.pattern = str;
            if (TextUtils.isEmpty(this.pattern)) {
                this.ignoreBegin = false;
                this.ignoreEnd = false;
                return;
            }
            if (this.pattern.startsWith(Operators.MOD)) {
                this.pattern = this.pattern.substring(1);
                this.ignoreBegin = true;
            }
            if (this.pattern.endsWith(Operators.MOD)) {
                this.pattern = this.pattern.substring(0, this.pattern.length() - 1);
                this.ignoreEnd = true;
            }
        }

        public boolean match(String str) {
            if (this.pattern == null || str == null) {
                return false;
            }
            if (this.ignoreBegin && this.ignoreEnd) {
                return str.contains(this.pattern);
            }
            if (this.ignoreBegin) {
                return str.endsWith(this.pattern);
            }
            if (this.ignoreEnd) {
                return str.startsWith(this.pattern);
            }
            return str.equals(this.pattern);
        }
    }

    public String[] getPageNames() {
        return this.pageNames;
    }

    public void setPageNames(String[] strArr) {
        this.pageNames = strArr;
    }

    public int[] getEventIds() {
        return this.eventIds;
    }

    public void setEventIds(int[] iArr) {
        this.eventIds = iArr;
    }

    public TrackId getTrackId() {
        return this.trackId;
    }

    public void setTrackId(TrackId trackId2) {
        this.trackId = trackId2;
    }

    public long getGroupId() {
        return this.groupId;
    }

    public void setGroupId(long j) {
        this.groupId = j;
    }

    public boolean isAppScope() {
        return this.appScope;
    }

    public void setAppScope(boolean z) {
        this.appScope = z;
    }
}
