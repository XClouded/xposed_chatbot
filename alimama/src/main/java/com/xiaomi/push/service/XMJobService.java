package com.xiaomi.push.service;

import android.annotation.TargetApi;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.at;
import com.xiaomi.push.ew;

public class XMJobService extends Service {
    static Service a;

    /* renamed from: a  reason: collision with other field name */
    private IBinder f803a = null;

    @TargetApi(21)
    static class a extends JobService {
        Binder a;

        /* renamed from: a  reason: collision with other field name */
        private Handler f804a;

        /* renamed from: com.xiaomi.push.service.XMJobService$a$a  reason: collision with other inner class name */
        private static class C0032a extends Handler {
            JobService a;

            C0032a(JobService jobService) {
                super(jobService.getMainLooper());
                this.a = jobService;
            }

            public void handleMessage(Message message) {
                if (message.what == 1) {
                    JobParameters jobParameters = (JobParameters) message.obj;
                    b.a("Job finished " + jobParameters.getJobId());
                    this.a.jobFinished(jobParameters, false);
                    if (jobParameters.getJobId() == 1) {
                        ew.a(false);
                    }
                }
            }
        }

        a(Service service) {
            this.a = null;
            this.a = (Binder) at.a((Object) this, "onBind", new Intent());
            at.a((Object) this, "attachBaseContext", service);
        }

        public boolean onStartJob(JobParameters jobParameters) {
            b.a("Job started " + jobParameters.getJobId());
            Intent intent = new Intent(this, XMPushService.class);
            intent.setAction("com.xiaomi.push.timer");
            intent.setPackage(getPackageName());
            startService(intent);
            if (this.f804a == null) {
                this.f804a = new C0032a(this);
            }
            this.f804a.sendMessage(Message.obtain(this.f804a, 1, jobParameters));
            return true;
        }

        public boolean onStopJob(JobParameters jobParameters) {
            b.a("Job stop " + jobParameters.getJobId());
            return false;
        }
    }

    static Service a() {
        return a;
    }

    public IBinder onBind(Intent intent) {
        return this.f803a != null ? this.f803a : new Binder();
    }

    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= 21) {
            this.f803a = new a(this).a;
        }
        a = this;
    }

    public void onDestroy() {
        super.onDestroy();
        a = null;
    }
}
