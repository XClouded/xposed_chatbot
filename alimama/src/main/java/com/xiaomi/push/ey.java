package com.xiaomi.push;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.SystemClock;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.ew;
import com.xiaomi.push.service.XMJobService;

@TargetApi(21)
public class ey implements ew.a {
    JobScheduler a;

    /* renamed from: a  reason: collision with other field name */
    Context f325a;

    /* renamed from: a  reason: collision with other field name */
    private boolean f326a = false;

    ey(Context context) {
        this.f325a = context;
        this.a = (JobScheduler) context.getSystemService("jobscheduler");
    }

    public void a() {
        this.f326a = false;
        this.a.cancel(1);
    }

    /* access modifiers changed from: package-private */
    public void a(long j) {
        JobInfo.Builder builder = new JobInfo.Builder(1, new ComponentName(this.f325a.getPackageName(), XMJobService.class.getName()));
        builder.setMinimumLatency(j);
        builder.setOverrideDeadline(j);
        builder.setRequiredNetworkType(1);
        builder.setPersisted(false);
        JobInfo build = builder.build();
        b.c("schedule Job = " + build.getId() + " in " + j);
        this.a.schedule(builder.build());
    }

    public void a(boolean z) {
        if (z || this.f326a) {
            long b = (long) fs.b();
            if (z) {
                a();
                b -= SystemClock.elapsedRealtime() % b;
            }
            this.f326a = true;
            a(b);
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m285a() {
        return this.f326a;
    }
}
