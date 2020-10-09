package com.alibaba.android.umbrella.performance;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessEntity implements Serializable {
    public List<String> abTest;
    public Map<String, String> args;
    public Map<String, Long> bindData;
    public String bizName;
    public String childBizName;
    public Map<String, Long> createView;
    public Map<String, Long> dataParse;
    public Map<String, Long> init;
    public Map<String, Long> lifeCycle;
    public Map<String, Long> netWork;
    public Map<String, Long> otherProcess;
    public long pageLoad;
    public Map<String, Long> process;
    private long registerTime;

    public ProcessEntity(String str) {
        this(str, System.currentTimeMillis());
    }

    public ProcessEntity(String str, long j) {
        this.bizName = str;
        this.registerTime = j;
        this.process = new HashMap();
        this.init = new HashMap();
        this.lifeCycle = new HashMap();
        this.netWork = new HashMap();
        this.dataParse = new HashMap();
        this.createView = new HashMap();
        this.bindData = new HashMap();
        this.abTest = new ArrayList();
        this.args = new HashMap();
        this.otherProcess = new HashMap();
    }

    public void addProcess(String str, long j) {
        if (this.process != null && !this.process.containsKey(str) && !TextUtils.isEmpty(str) && j > 0) {
            this.process.put(str, Long.valueOf(j));
        }
    }

    public void addInit(String str, long j) {
        if (this.init != null && !this.init.containsKey(str) && !TextUtils.isEmpty(str) && j > 0) {
            this.init.put(str, Long.valueOf(j));
        }
    }

    public void addLifeCycle(String str, long j) {
        if (this.lifeCycle != null && !this.lifeCycle.containsKey(str) && !TextUtils.isEmpty(str) && j > 0) {
            this.lifeCycle.put(str, Long.valueOf(j));
        }
    }

    public void addNetwork(String str, long j) {
        if (this.netWork != null && !this.netWork.containsKey(str) && !TextUtils.isEmpty(str) && j > 0) {
            this.netWork.put(str, Long.valueOf(j));
        }
    }

    public void addDataParse(String str, long j) {
        if (this.dataParse != null && !this.dataParse.containsKey(str) && !TextUtils.isEmpty(str) && j > 0) {
            this.dataParse.put(str, Long.valueOf(j));
        }
    }

    public void addCreateView(String str, long j) {
        if (this.createView != null && !TextUtils.isEmpty(str) && j > 0) {
            if (this.createView.containsKey(str)) {
                this.createView.put(str, Long.valueOf((j + this.createView.get(str).longValue()) / 2));
                return;
            }
            this.createView.put(str, Long.valueOf(j));
        }
    }

    public void addBindView(String str, long j) {
        if (this.bindData != null && !TextUtils.isEmpty(str) && j > 0) {
            if (this.bindData.containsKey(str)) {
                this.bindData.put(str, Long.valueOf((j + this.bindData.get(str).longValue()) / 2));
                return;
            }
            this.bindData.put(str, Long.valueOf(j));
        }
    }

    public void addArgs(String str, String str2) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            this.args.put(str, str2);
        }
    }

    public void addOtherProcess(String str, long j) {
        if (this.otherProcess != null && !TextUtils.isEmpty(str) && j >= 0) {
            this.otherProcess.put(str, Long.valueOf(j));
        }
    }

    public void setChildBizName(String str) {
        this.childBizName = str;
    }

    public void addAbTest(String str, String str2) {
        if (this.abTest != null && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            List<String> list = this.abTest;
            list.add(str + ":" + str2);
        }
    }

    public void addPageLoad(long j) {
        this.pageLoad = j;
    }

    public void addArgs(Map<String, String> map) {
        if (map != null && map.size() > 0) {
            this.args.putAll(map);
        }
    }

    public String toJsonString() {
        return JSON.toJSONString(this);
    }

    /* access modifiers changed from: package-private */
    public long getRegisterTime() {
        return this.registerTime;
    }
}
