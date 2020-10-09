package anetwork.channel.aidl.adapter;

import android.os.RemoteException;
import anet.channel.util.ALog;
import anetwork.channel.Response;
import anetwork.channel.aidl.ParcelableFuture;
import com.alibaba.android.umbrella.utils.UmbrellaConstants;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureResponse implements Future<Response> {
    private static final String TAG = "anet.FutureResponse";
    private ParcelableFuture delegate;
    private Response response;

    public FutureResponse(ParcelableFuture parcelableFuture) {
        this.delegate = parcelableFuture;
    }

    public FutureResponse(Response response2) {
        this.response = response2;
    }

    public boolean cancel(boolean z) {
        if (this.delegate == null) {
            return false;
        }
        try {
            return this.delegate.cancel(z);
        } catch (RemoteException e) {
            ALog.w(TAG, "[cancel]", (String) null, e, new Object[0]);
            return false;
        }
    }

    public boolean isCancelled() {
        try {
            return this.delegate.isCancelled();
        } catch (RemoteException e) {
            ALog.w(TAG, "[isCancelled]", (String) null, e, new Object[0]);
            return false;
        }
    }

    public boolean isDone() {
        try {
            return this.delegate.isDone();
        } catch (RemoteException e) {
            ALog.w(TAG, "[isDone]", (String) null, e, new Object[0]);
            return true;
        }
    }

    public Response get() throws InterruptedException, ExecutionException {
        if (this.response != null) {
            return this.response;
        }
        if (this.delegate != null) {
            try {
                return this.delegate.get(UmbrellaConstants.PERFORMANCE_DATA_ALIVE);
            } catch (RemoteException e) {
                ALog.w(TAG, "[get]", (String) null, e, new Object[0]);
            }
        }
        return null;
    }

    public Response get(long j, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        if (this.response != null) {
            return this.response;
        }
        if (this.delegate != null) {
            try {
                return this.delegate.get(j);
            } catch (RemoteException e) {
                ALog.w(TAG, "[get(long timeout, TimeUnit unit)]", (String) null, e, new Object[0]);
            }
        }
        return null;
    }
}
