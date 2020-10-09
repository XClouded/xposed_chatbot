package com.alibaba.ut.abtest.internal.bucketing.model;

import android.text.TextUtils;
import com.alibaba.ut.abtest.internal.util.TrackUtils;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ExperimentActivateGroup {
    private List<ExperimentGroup> groups;
    private ConcurrentHashMap<String, Long> trackId2ExperimentIds = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Long> trackId2GroupIds = new ConcurrentHashMap<>();
    private Set<String> trackIds;
    private Map<String, String> variations;

    public Map<String, String> getVariations() {
        return this.variations;
    }

    public void setVariations(Map<String, String> map) {
        this.variations = map;
    }

    public Set<String> getTrackIds() {
        return this.trackIds;
    }

    public void addTrackId(String str) {
        if (!TextUtils.isEmpty(str)) {
            if (this.trackIds == null) {
                this.trackIds = new LinkedHashSet();
            }
            this.trackIds.add(str);
        }
    }

    public List<ExperimentGroup> getGroups() {
        return this.groups;
    }

    public void addGroup(ExperimentGroup experimentGroup) {
        if (this.groups == null) {
            this.groups = new ArrayList();
        }
        this.groups.add(experimentGroup);
        String generateAbTrackId = TrackUtils.generateAbTrackId(experimentGroup.getReleaseId(), experimentGroup.getId());
        if (!TextUtils.isEmpty(generateAbTrackId)) {
            addTrackId(generateAbTrackId);
            this.trackId2ExperimentIds.put(generateAbTrackId, Long.valueOf(experimentGroup.getExperimentId()));
            this.trackId2GroupIds.put(generateAbTrackId, Long.valueOf(experimentGroup.getId()));
        }
    }

    public void addGroups(List<ExperimentGroup> list) {
        if (list != null) {
            for (ExperimentGroup addGroup : list) {
                addGroup(addGroup);
            }
        }
    }

    public Long getExperimentId(String str) {
        return this.trackId2ExperimentIds.get(str);
    }

    public Long getExperimentGroupId(String str) {
        return this.trackId2GroupIds.get(str);
    }
}
