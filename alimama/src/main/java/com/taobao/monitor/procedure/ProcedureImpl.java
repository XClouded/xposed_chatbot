package com.taobao.monitor.procedure;

import com.taobao.monitor.ProcedureGlobal;
import com.taobao.monitor.exception.ProcedureException;
import com.taobao.monitor.logger.Logger;
import com.taobao.monitor.procedure.model.Event;
import com.taobao.monitor.procedure.model.Stage;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ProcedureImpl implements IProcedureGroup, IValueCallback {
    private static final String TAG = "ProcedureImpl";
    private static volatile long count = System.currentTimeMillis();
    private final boolean independent;
    private IProcedureLifeCycle lifeCycle;
    /* access modifiers changed from: private */
    public final IProcedure parent;
    private final String session;
    private Status status = Status.INIT;
    private final List<IProcedure> subProcedures;
    private String topic;
    private final Value value;

    public interface IProcedureLifeCycle {
        void begin(Value value);

        void end(Value value);

        void event(Value value, Event event);

        void stage(Value value, Stage stage);
    }

    private enum Status {
        INIT,
        RUNNING,
        STOPPED
    }

    ProcedureImpl(String str, IProcedure iProcedure, boolean z, boolean z2) {
        long j = count;
        count = 1 + j;
        this.session = String.valueOf(j);
        this.topic = str;
        this.parent = iProcedure;
        this.independent = z;
        this.subProcedures = new LinkedList();
        this.value = new Value(str, z, z2);
        if (iProcedure != null) {
            this.value.addProperty("parentSession", iProcedure.topicSession());
        }
        this.value.addProperty("session", this.session);
    }

    public String topic() {
        return this.topic;
    }

    public String topicSession() {
        return this.session;
    }

    public IProcedure begin() {
        if (this.status == Status.INIT) {
            this.status = Status.RUNNING;
            if (this.parent instanceof IProcedureGroup) {
                ((IProcedureGroup) this.parent).addSubProcedure(this);
            }
            Logger.i(TAG, this.parent, this.topic, "begin()");
            if (this.lifeCycle != null) {
                this.lifeCycle.begin(this.value);
            }
        }
        return this;
    }

    public IProcedure event(String str, Map<String, Object> map) {
        if (str != null && isAlive()) {
            Event event = new Event(str, map);
            this.value.event(event);
            if (this.lifeCycle != null) {
                this.lifeCycle.event(this.value, event);
            }
            Logger.i(TAG, this.parent, this.topic, str);
        }
        return this;
    }

    public IProcedure stage(String str, long j) {
        if (str != null && isAlive()) {
            Stage stage = new Stage(str, j);
            this.value.stage(stage);
            if (this.lifeCycle != null) {
                this.lifeCycle.stage(this.value, stage);
            }
            Logger.i(TAG, this.parent, this.topic, stage);
        }
        return this;
    }

    public IProcedure addBiz(String str, Map<String, Object> map) {
        if (str != null && isAlive()) {
            this.value.addBiz(str, map);
            Logger.i(TAG, this.parent, this.topic, str);
        }
        return this;
    }

    public IProcedure addBizAbTest(String str, Map<String, Object> map) {
        if (str != null && isAlive()) {
            this.value.addBizAbTest(str, map);
            Logger.i(TAG, this.parent, this.topic, str);
        }
        return this;
    }

    public IProcedure addBizStage(String str, Map<String, Object> map) {
        if (str != null && isAlive()) {
            this.value.addBizStage(str, map);
            Logger.i(TAG, this.parent, this.topic, str);
        }
        return this;
    }

    public IProcedure addProperty(String str, Object obj) {
        if (isAlive()) {
            this.value.addProperty(str, obj);
        }
        return this;
    }

    public IProcedure addStatistic(String str, Object obj) {
        if (isAlive()) {
            this.value.addStatistic(str, obj);
        }
        return this;
    }

    public boolean isAlive() {
        return Status.STOPPED != this.status;
    }

    public IProcedure end() {
        return end(false);
    }

    public IProcedure end(boolean z) {
        if (this.status == Status.RUNNING) {
            synchronized (this.subProcedures) {
                for (IProcedure next : this.subProcedures) {
                    if (next instanceof ProcedureProxy) {
                        IProcedure base = ((ProcedureProxy) next).base();
                        if (base instanceof ProcedureImpl) {
                            ProcedureImpl procedureImpl = (ProcedureImpl) base;
                            if (procedureImpl.isAlive()) {
                                this.value.addSubValue(procedureImpl.value4Parent());
                            }
                            if (!procedureImpl.independent || z) {
                                base.end(z);
                            }
                        } else {
                            base.end(z);
                        }
                    } else {
                        next.end(z);
                    }
                }
            }
            if (this.parent instanceof IProcedureGroup) {
                ProcedureGlobal.instance().handler().post(new Runnable() {
                    public void run() {
                        ((IProcedureGroup) ProcedureImpl.this.parent).removeSubProcedure(ProcedureImpl.this);
                    }
                });
            }
            if (this.parent instanceof IValueCallback) {
                ((IValueCallback) this.parent).callback(value4Parent());
            }
            if (this.lifeCycle != null) {
                this.lifeCycle.end(this.value);
            }
            this.status = Status.STOPPED;
            Logger.i(TAG, this.parent, this.topic, "end()");
        }
        return this;
    }

    /* access modifiers changed from: protected */
    public Value value4Parent() {
        return this.value.summary();
    }

    public void addSubProcedure(IProcedure iProcedure) {
        if (iProcedure != null && isAlive()) {
            synchronized (this.subProcedures) {
                this.subProcedures.add(iProcedure);
            }
        }
    }

    public IProcedure parent() {
        return this.parent;
    }

    public void removeSubProcedure(IProcedure iProcedure) {
        if (iProcedure != null) {
            synchronized (this.subProcedures) {
                this.subProcedures.remove(iProcedure);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        super.finalize();
        if (this.status == Status.RUNNING) {
            Logger.throwException(new ProcedureException("Please call end function first!"));
        }
    }

    public Value value() {
        return this.value;
    }

    public ProcedureImpl setLifeCycle(IProcedureLifeCycle iProcedureLifeCycle) {
        this.lifeCycle = iProcedureLifeCycle;
        return this;
    }

    public String toString() {
        return this.topic;
    }

    public void callback(Value value2) {
        if (isAlive()) {
            this.value.addSubValue(value2);
        }
    }
}
