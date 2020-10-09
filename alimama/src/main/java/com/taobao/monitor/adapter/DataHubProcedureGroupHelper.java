package com.taobao.monitor.adapter;

import com.taobao.monitor.procedure.IProcedure;
import com.taobao.monitor.procedure.ProcedureManagerProxy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

class DataHubProcedureGroupHelper {
    private static final ProcedureGroup groups = new ProcedureGroup();

    DataHubProcedureGroupHelper() {
    }

    public static IProcedure getCurrentProcedures() {
        groups.clear();
        groups.addSubProcedure(ProcedureManagerProxy.PROXY.getLauncherProcedure());
        groups.addSubProcedure(ProcedureManagerProxy.PROXY.getCurrentActivityProcedure());
        groups.addSubProcedure(ProcedureManagerProxy.PROXY.getCurrentFragmentProcedure());
        return groups;
    }

    public static class ProcedureGroup implements IProcedure {
        final ArrayList<IProcedure> procedures = new ArrayList<>();

        public String topic() {
            throw new UnsupportedOperationException();
        }

        public String topicSession() {
            throw new UnsupportedOperationException();
        }

        public IProcedure begin() {
            throw new UnsupportedOperationException();
        }

        public IProcedure event(String str, Map<String, Object> map) {
            Iterator<IProcedure> it = this.procedures.iterator();
            while (it.hasNext()) {
                it.next().event(str, map);
            }
            return this;
        }

        public IProcedure stage(String str, long j) {
            Iterator<IProcedure> it = this.procedures.iterator();
            while (it.hasNext()) {
                it.next().stage(str, j);
            }
            return this;
        }

        public IProcedure addBiz(String str, Map<String, Object> map) {
            Iterator<IProcedure> it = this.procedures.iterator();
            while (it.hasNext()) {
                it.next().addBiz(str, map);
            }
            return this;
        }

        public IProcedure addBizAbTest(String str, Map<String, Object> map) {
            Iterator<IProcedure> it = this.procedures.iterator();
            while (it.hasNext()) {
                it.next().addBizAbTest(str, map);
            }
            return this;
        }

        public IProcedure addBizStage(String str, Map<String, Object> map) {
            Iterator<IProcedure> it = this.procedures.iterator();
            while (it.hasNext()) {
                it.next().addBizStage(str, map);
            }
            return this;
        }

        public IProcedure addProperty(String str, Object obj) {
            Iterator<IProcedure> it = this.procedures.iterator();
            while (it.hasNext()) {
                it.next().addProperty(str, obj);
            }
            return this;
        }

        public IProcedure addStatistic(String str, Object obj) {
            Iterator<IProcedure> it = this.procedures.iterator();
            while (it.hasNext()) {
                it.next().addStatistic(str, obj);
            }
            return this;
        }

        public boolean isAlive() {
            throw new UnsupportedOperationException();
        }

        public IProcedure end() {
            throw new UnsupportedOperationException();
        }

        public IProcedure end(boolean z) {
            throw new UnsupportedOperationException();
        }

        public IProcedure parent() {
            throw new UnsupportedOperationException();
        }

        /* access modifiers changed from: private */
        public void addSubProcedure(IProcedure iProcedure) {
            if (iProcedure != null) {
                this.procedures.add(iProcedure);
            }
        }

        /* access modifiers changed from: private */
        public void clear() {
            this.procedures.clear();
        }
    }
}
