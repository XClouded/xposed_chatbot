package com.xiaomi.mipush.sdk;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import com.xiaomi.channel.commonutils.logger.b;

class bb implements ServiceConnection {
    final /* synthetic */ ay a;

    bb(ay ayVar) {
        this.a = ayVar;
    }

    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        synchronized (this.a) {
            Messenger unused = this.a.f45a = new Messenger(iBinder);
            boolean unused2 = this.a.c = false;
            for (Message send : ay.a(this.a)) {
                try {
                    ay.a(this.a).send(send);
                } catch (RemoteException e) {
                    b.a((Throwable) e);
                }
            }
            ay.a(this.a).clear();
        }
    }

    public void onServiceDisconnected(ComponentName componentName) {
        Messenger unused = this.a.f45a = null;
        boolean unused2 = this.a.c = false;
    }
}
