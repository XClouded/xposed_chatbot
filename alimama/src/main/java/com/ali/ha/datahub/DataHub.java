package com.ali.ha.datahub;

import java.util.HashMap;

public class DataHub {
    private SubProcedure mSubProcedure;
    private BizSubscriber mSubscriber;

    private DataHub() {
    }

    private static final class SingleInstanceHolder {
        public static final DataHub sInstance = new DataHub();

        private SingleInstanceHolder() {
        }
    }

    public static final DataHub getInstance() {
        return SingleInstanceHolder.sInstance;
    }

    public void publish(String str, HashMap<String, String> hashMap) {
        if (this.mSubscriber != null) {
            this.mSubscriber.pub(str, hashMap);
        }
    }

    public void publishABTest(String str, HashMap<String, String> hashMap) {
        if (this.mSubscriber != null) {
            this.mSubscriber.pubAB(str, hashMap);
        }
    }

    public void onStage(String str, String str2) {
        onStage(str, str2, System.currentTimeMillis());
    }

    public void onStage(String str, String str2, long j) {
        if (this.mSubscriber != null) {
            this.mSubscriber.onStage(str, str2, j);
        }
    }

    public void setCurrentBiz(String str, String str2) {
        if (this.mSubscriber != null) {
            this.mSubscriber.setMainBiz(str, str2);
        }
    }

    public void setCurrentBiz(String str) {
        if (this.mSubscriber != null) {
            this.mSubscriber.setMainBiz(str, (String) null);
        }
    }

    public void onBizDataReadyStage() {
        if (this.mSubscriber != null) {
            this.mSubscriber.onBizDataReadyStage();
        }
    }

    public void init(BizSubscriber bizSubscriber) {
        if (this.mSubscriber == null) {
            this.mSubscriber = bizSubscriber;
            this.mSubProcedure = new SubProcedure(this.mSubscriber);
        }
    }

    private SubProcedure subProcedure() {
        if (this.mSubProcedure == null) {
            this.mSubProcedure = new SubProcedure();
        }
        return this.mSubProcedure;
    }

    private static class SubProcedure {
        private BizSubscriber mSubscriber;

        private SubProcedure() {
        }

        private SubProcedure(BizSubscriber bizSubscriber) {
            this.mSubscriber = bizSubscriber;
        }

        public void onBegin(String str) {
            BizSubscriber bizSubscriber = this.mSubscriber;
        }

        public void onStage(String str, String str2) {
            BizSubscriber bizSubscriber = this.mSubscriber;
        }

        public void onEnd(String str) {
            BizSubscriber bizSubscriber = this.mSubscriber;
        }
    }
}
