package com.taobao.monitor.procedure;

import com.taobao.monitor.ProcedureGlobal;
import java.util.Map;

public class ProcedureProxy implements IProcedureGroup, IValueCallback {
    /* access modifiers changed from: private */
    public final ProcedureImpl base;

    public ProcedureProxy(ProcedureImpl procedureImpl) {
        if (procedureImpl != null) {
            this.base = procedureImpl;
            return;
        }
        throw new IllegalArgumentException();
    }

    public String topic() {
        return this.base.topic();
    }

    public String topicSession() {
        return this.base.topicSession();
    }

    public IProcedure begin() {
        async(new Runnable() {
            public void run() {
                ProcedureProxy.this.base.begin();
            }
        });
        return this;
    }

    public IProcedure event(final String str, final Map<String, Object> map) {
        async(new Runnable() {
            public void run() {
                ProcedureProxy.this.base.event(str, map);
            }
        });
        return this;
    }

    public IProcedure stage(final String str, final long j) {
        async(new Runnable() {
            public void run() {
                ProcedureProxy.this.base.stage(str, j);
            }
        });
        return this;
    }

    public IProcedure addBiz(final String str, final Map<String, Object> map) {
        async(new Runnable() {
            public void run() {
                ProcedureProxy.this.base.addBiz(str, map);
            }
        });
        return this;
    }

    public IProcedure addBizAbTest(final String str, final Map<String, Object> map) {
        async(new Runnable() {
            public void run() {
                ProcedureProxy.this.base.addBizAbTest(str, map);
            }
        });
        return this;
    }

    public IProcedure addBizStage(final String str, final Map<String, Object> map) {
        async(new Runnable() {
            public void run() {
                ProcedureProxy.this.base.addBizStage(str, map);
            }
        });
        return this;
    }

    public IProcedure addProperty(final String str, final Object obj) {
        async(new Runnable() {
            public void run() {
                ProcedureProxy.this.base.addProperty(str, obj);
            }
        });
        return this;
    }

    public IProcedure addStatistic(final String str, final Object obj) {
        async(new Runnable() {
            public void run() {
                ProcedureProxy.this.base.addStatistic(str, obj);
            }
        });
        return this;
    }

    public boolean isAlive() {
        return this.base.isAlive();
    }

    public IProcedure end() {
        async(new Runnable() {
            public void run() {
                ProcedureProxy.this.base.end();
            }
        });
        return this;
    }

    public IProcedure end(final boolean z) {
        async(new Runnable() {
            public void run() {
                ProcedureProxy.this.base.end(z);
            }
        });
        return this;
    }

    public IProcedure base() {
        return this.base;
    }

    public void removeSubProcedure(IProcedure iProcedure) {
        this.base.removeSubProcedure(iProcedure);
    }

    public void addSubProcedure(IProcedure iProcedure) {
        this.base.addSubProcedure(iProcedure);
    }

    public IProcedure parent() {
        return this.base.parent();
    }

    private void async(Runnable runnable) {
        ProcedureGlobal.instance().handler().post(runnable);
    }

    public void callback(Value value) {
        this.base.callback(value);
    }
}
