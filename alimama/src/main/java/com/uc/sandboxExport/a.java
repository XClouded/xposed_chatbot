package com.uc.sandboxExport;

import android.os.Bundle;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.util.Log;
import com.uc.sandboxExport.IChildProcessSetup;

/* compiled from: U4Source */
final class a extends IChildProcessSetup.Stub {
    final /* synthetic */ SandboxedProcessService a;

    a(SandboxedProcessService sandboxedProcessService) {
        this.a = sandboxedProcessService;
    }

    public final IBinder preSetupConnection(Bundle bundle) {
        ParcelFileDescriptor[] parcelFileDescriptorArr;
        ParcelFileDescriptor parcelFileDescriptor = (ParcelFileDescriptor) bundle.getParcelable("dex.fd");
        Parcelable[] parcelableArray = bundle.getParcelableArray("lib.fd");
        ParcelFileDescriptor parcelFileDescriptor2 = (ParcelFileDescriptor) bundle.getParcelable("crash.fd");
        String a2 = this.a.c;
        Log.e(a2, "preSetupConnection, dex fd: " + parcelFileDescriptor);
        if (parcelableArray != null) {
            parcelFileDescriptorArr = new ParcelFileDescriptor[parcelableArray.length];
            System.arraycopy(parcelableArray, 0, parcelFileDescriptorArr, 0, parcelableArray.length);
        } else {
            parcelFileDescriptorArr = null;
        }
        try {
            this.a.init(parcelFileDescriptor, parcelFileDescriptorArr, parcelFileDescriptor2);
            return this.a.j;
        } catch (Throwable th) {
            Log.e(this.a.c, "preSetupConnection.init exception", th);
            throw th;
        }
    }
}
