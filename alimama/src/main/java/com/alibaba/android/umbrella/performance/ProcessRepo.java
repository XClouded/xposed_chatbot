package com.alibaba.android.umbrella.performance;

import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;

public class ProcessRepo {
    public static final int INIT_SIZE = 5;
    private static ProcessRepo instance;
    private HashMap<String, ProcessEntity> entities;

    public static ProcessRepo getInstance() {
        if (instance == null) {
            instance = new ProcessRepo(5);
        }
        return instance;
    }

    private ProcessRepo(int i) {
        this.entities = new HashMap<>(i);
    }

    public void add(ProcessEntity processEntity) {
        if (processEntity != null && !TextUtils.isEmpty(processEntity.bizName)) {
            if (this.entities.containsKey(processEntity.bizName)) {
                this.entities.remove(processEntity);
            }
            this.entities.put(processEntity.bizName, processEntity);
        }
    }

    public void remove(ProcessEntity processEntity) {
        if (processEntity != null && !TextUtils.isEmpty(processEntity.bizName) && this.entities.containsKey(processEntity.bizName)) {
            this.entities.remove(processEntity.bizName);
        }
    }

    public void remove(int i) {
        if (i >= 0 && i < this.entities.size()) {
            this.entities.remove(Integer.valueOf(i));
        }
    }

    public ProcessEntity getProcessEntity(String str) {
        if (TextUtils.isEmpty(str) || this.entities == null || !this.entities.containsKey(str)) {
            return null;
        }
        return this.entities.get(str);
    }

    public Map<String, ProcessEntity> getProcessMap() {
        return this.entities;
    }

    public boolean isContains(String str) {
        return !TextUtils.isEmpty(str) && this.entities != null && this.entities.containsKey(str);
    }
}
