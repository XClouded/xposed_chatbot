package com.taobao.monitor;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import com.taobao.monitor.procedure.ProcedureFactory;
import com.taobao.monitor.procedure.ProcedureManager;

public class ProcedureGlobal {
    public static final ProcedureFactory PROCEDURE_FACTORY = new ProcedureFactory();
    public static final ProcedureManager PROCEDURE_MANAGER = new ProcedureManager();
    private Context context;
    private final Handler handler;

    private ProcedureGlobal() {
        HandlerThread handlerThread = new HandlerThread("APM-Procedure");
        handlerThread.start();
        this.handler = new Handler(handlerThread.getLooper());
    }

    public static ProcedureGlobal instance() {
        return Holder.INSTANCE;
    }

    public Context context() {
        return this.context;
    }

    /* access modifiers changed from: package-private */
    public ProcedureGlobal setContext(Context context2) {
        this.context = context2;
        return this;
    }

    public Handler handler() {
        return this.handler;
    }

    private static class Holder {
        static final ProcedureGlobal INSTANCE = new ProcedureGlobal();

        private Holder() {
        }
    }
}
