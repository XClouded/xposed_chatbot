package com.xiaomi.clientreport.manager;

import android.content.Context;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.clientreport.data.Config;
import com.xiaomi.clientreport.data.EventClientReport;
import com.xiaomi.clientreport.data.PerfClientReport;
import com.xiaomi.clientreport.processor.IEventProcessor;
import com.xiaomi.clientreport.processor.IPerfProcessor;
import com.xiaomi.clientreport.processor.c;
import com.xiaomi.push.ai;
import com.xiaomi.push.az;
import com.xiaomi.push.ba;
import com.xiaomi.push.bb;
import com.xiaomi.push.bc;
import com.xiaomi.push.be;
import com.xiaomi.push.bh;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class a {
    private static volatile a a;

    /* renamed from: a  reason: collision with other field name */
    private Context f4a;

    /* renamed from: a  reason: collision with other field name */
    private Config f5a;

    /* renamed from: a  reason: collision with other field name */
    private IEventProcessor f6a;

    /* renamed from: a  reason: collision with other field name */
    private IPerfProcessor f7a;

    /* renamed from: a  reason: collision with other field name */
    private HashMap<String, HashMap<String, com.xiaomi.clientreport.data.a>> f8a = new HashMap<>();

    /* renamed from: a  reason: collision with other field name */
    private ExecutorService f9a = Executors.newSingleThreadExecutor();
    private HashMap<String, ArrayList<com.xiaomi.clientreport.data.a>> b = new HashMap<>();

    private a(Context context) {
        this.f4a = context;
    }

    public static a a(Context context) {
        if (a == null) {
            synchronized (a.class) {
                if (a == null) {
                    a = new a(context);
                }
            }
        }
        return a;
    }

    private void a(Runnable runnable, int i) {
        ai.a(this.f4a).a(runnable, i);
    }

    private void d() {
        if (a(this.f4a).a().isEventUploadSwitchOpen()) {
            ba baVar = new ba(this.f4a);
            int eventUploadFrequency = (int) a(this.f4a).a().getEventUploadFrequency();
            if (eventUploadFrequency < 1800) {
                eventUploadFrequency = 1800;
            }
            if (System.currentTimeMillis() - bh.a(this.f4a).a("sp_client_report_status", "event_last_upload_time", 0) > ((long) (eventUploadFrequency * 1000))) {
                ai.a(this.f4a).a((Runnable) new d(this, baVar), 10);
            }
            synchronized (a.class) {
                if (!ai.a(this.f4a).a((ai.a) baVar, eventUploadFrequency)) {
                    ai.a(this.f4a).a(100886);
                    ai.a(this.f4a).a((ai.a) baVar, eventUploadFrequency);
                }
            }
        }
    }

    private void e() {
        if (a(this.f4a).a().isPerfUploadSwitchOpen()) {
            bb bbVar = new bb(this.f4a);
            int perfUploadFrequency = (int) a(this.f4a).a().getPerfUploadFrequency();
            if (perfUploadFrequency < 1800) {
                perfUploadFrequency = 1800;
            }
            if (System.currentTimeMillis() - bh.a(this.f4a).a("sp_client_report_status", "perf_last_upload_time", 0) > ((long) (perfUploadFrequency * 1000))) {
                ai.a(this.f4a).a((Runnable) new e(this, bbVar), 15);
            }
            synchronized (a.class) {
                if (!ai.a(this.f4a).a((ai.a) bbVar, perfUploadFrequency)) {
                    ai.a(this.f4a).a(100887);
                    ai.a(this.f4a).a((ai.a) bbVar, perfUploadFrequency);
                }
            }
        }
    }

    public synchronized Config a() {
        if (this.f5a == null) {
            this.f5a = Config.defaultConfig(this.f4a);
        }
        return this.f5a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m21a() {
        a(this.f4a).d();
        a(this.f4a).e();
    }

    public void a(Config config, IEventProcessor iEventProcessor, IPerfProcessor iPerfProcessor) {
        this.f5a = config;
        this.f6a = iEventProcessor;
        this.f7a = iPerfProcessor;
        this.f6a.setEventMap(this.b);
        this.f7a.setPerfMap(this.f8a);
    }

    public void a(EventClientReport eventClientReport) {
        if (a().isEventUploadSwitchOpen()) {
            this.f9a.execute(new az(this.f4a, eventClientReport, this.f6a));
            a(new b(this), 30);
        }
    }

    public void a(PerfClientReport perfClientReport) {
        if (a().isPerfUploadSwitchOpen()) {
            this.f9a.execute(new az(this.f4a, perfClientReport, this.f7a));
            a(new c(this), 30);
        }
    }

    public void a(boolean z, boolean z2, long j, long j2) {
        if (this.f5a == null) {
            return;
        }
        if (z != this.f5a.isEventUploadSwitchOpen() || z2 != this.f5a.isPerfUploadSwitchOpen() || j != this.f5a.getEventUploadFrequency() || j2 != this.f5a.getPerfUploadFrequency()) {
            long eventUploadFrequency = this.f5a.getEventUploadFrequency();
            long perfUploadFrequency = this.f5a.getPerfUploadFrequency();
            Config build = Config.getBuilder().setAESKey(be.a(this.f4a)).setEventEncrypted(this.f5a.isEventEncrypted()).setEventUploadSwitchOpen(z).setEventUploadFrequency(j).setPerfUploadSwitchOpen(z2).setPerfUploadFrequency(j2).build(this.f4a);
            this.f5a = build;
            if (!this.f5a.isEventUploadSwitchOpen()) {
                ai.a(this.f4a).a(100886);
            } else if (eventUploadFrequency != build.getEventUploadFrequency()) {
                b.c(this.f4a.getPackageName() + "reset event job " + build.getEventUploadFrequency());
                d();
            }
            if (!this.f5a.isPerfUploadSwitchOpen()) {
                ai.a(this.f4a).a(100887);
            } else if (perfUploadFrequency != build.getPerfUploadFrequency()) {
                b.c(this.f4a.getPackageName() + "reset perf job " + build.getPerfUploadFrequency());
                e();
            }
        }
    }

    public void b() {
        if (a().isEventUploadSwitchOpen()) {
            bc bcVar = new bc();
            bcVar.a(this.f4a);
            bcVar.a((c) this.f6a);
            this.f9a.execute(bcVar);
        }
    }

    public void c() {
        if (a().isPerfUploadSwitchOpen()) {
            bc bcVar = new bc();
            bcVar.a((c) this.f7a);
            bcVar.a(this.f4a);
            this.f9a.execute(bcVar);
        }
    }
}
