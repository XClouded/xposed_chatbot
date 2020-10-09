package com.taobao.aipc.core.channel;

import android.app.ActivityThread;
import android.content.ContentResolver;
import android.content.IContentProvider;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.system.Os;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.taobao.aipc.AIPC;
import com.taobao.aipc.constant.Constants;
import com.taobao.aipc.core.entity.CallbackMessage;
import com.taobao.aipc.core.entity.Message;
import com.taobao.aipc.core.entity.Reply;
import com.taobao.aipc.logs.IPCLog;
import com.taobao.aipc.utils.IPCUtils;
import com.taobao.aipc.utils.SerializeUtils;
import com.taobao.onlinemonitor.OnLineMonitor;
import java.util.ArrayList;
import java.util.List;

public class Channel {
    private static final String TAG = "Channel";
    private static ActivityThread activityThread;
    private static ContentResolver contentResolver;
    /* access modifiers changed from: private */
    public static volatile IBinder iBinder;
    private static Uri mainUri;
    private static Uri remoteUri;
    private static volatile Channel sInstance;

    private Channel() {
        contentResolver = AIPC.getContext().getContentResolver();
        String packageName = AIPC.getContext().getPackageName();
        mainUri = Uri.parse(IPCUtils.getAuthorities(packageName, Constants.URI_MAIN));
        remoteUri = Uri.parse(IPCUtils.getAuthorities(packageName, Constants.URI_REMOTE));
    }

    public static Channel getInstance() {
        if (sInstance == null) {
            synchronized (Channel.class) {
                if (sInstance == null) {
                    sInstance = new Channel();
                }
            }
        }
        return sInstance;
    }

    public Reply send(Message message) {
        try {
            Bundle bundle = new Bundle();
            bundle.putParcelable("message", message);
            Bundle call = call(remoteUri, true, "send", bundle);
            message.recycle();
            if (call == null || !call.containsKey(Constants.PARAM_REPLY)) {
                return null;
            }
            return (Reply) SerializeUtils.decode(call.getByteArray(Constants.PARAM_REPLY), Reply.class);
        } catch (Throwable th) {
            IPCLog.eTag(TAG, "send message to remote Error", th);
            return null;
        }
    }

    public void recycle(List<String> list) {
        try {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(Constants.PARAM_TIMESTAMPS, (ArrayList) list);
            call(remoteUri, true, Constants.METHOD_RECYCLE_REMOTE, bundle);
        } catch (Throwable th) {
            IPCLog.eTag(TAG, "recycle remote resource Error:", th);
        }
    }

    public void recycle(List<String> list, ArrayList<Integer> arrayList) {
        try {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(Constants.PARAM_TIMESTAMPS, (ArrayList) list);
            bundle.putIntegerArrayList(Constants.PARAM_INDEX, arrayList);
            call(mainUri, false, Constants.METHOD_RECYCLE_MAIN, bundle);
        } catch (Throwable th) {
            IPCLog.eTag(TAG, "recycle main resource Error:", th);
        }
    }

    public Reply callback(CallbackMessage callbackMessage) {
        try {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.PARAM_CALLBACK_MESSAGE, callbackMessage);
            Bundle call = call(mainUri, false, "callback", bundle);
            callbackMessage.recycle();
            if (call == null || !call.containsKey(Constants.PARAM_REPLY)) {
                return null;
            }
            return (Reply) SerializeUtils.decode(call.getByteArray(Constants.PARAM_REPLY), Reply.class);
        } catch (Throwable th) {
            IPCLog.eTag(TAG, "callback Error:", th);
            return null;
        }
    }

    private Bundle call(Uri uri, boolean z, String str, Bundle bundle) {
        String str2;
        IContentProvider acquireProvider;
        Bundle bundle2;
        IContentProvider iContentProvider = null;
        try {
            if (activityThread == null) {
                synchronized (Channel.class) {
                    if (activityThread == null) {
                        activityThread = ActivityThread.currentActivityThread();
                        if (activityThread == null) {
                            Bundle call = contentResolver.call(uri, str, "", bundle);
                            ActivityThread activityThread2 = activityThread;
                            return call;
                        }
                    }
                }
            }
            if (Build.VERSION.SDK_INT <= 16) {
                acquireProvider = activityThread.acquireProvider(AIPC.getContext(), uri.getAuthority(), false);
            } else if (Build.VERSION.SDK_INT < 21) {
                acquireProvider = activityThread.acquireProvider(AIPC.getContext(), uri.getAuthority(), Binder.getCallingUid() / OnLineMonitor.TASK_TYPE_FROM_BOOT, false);
            } else {
                acquireProvider = activityThread.acquireProvider(AIPC.getContext(), IPCUtils.getAuthorityWithoutUserId(uri.getAuthority()), IPCUtils.getUserIdFromAuthority(uri.getAuthority(), Os.getuid() / OnLineMonitor.TASK_TYPE_FROM_BOOT), false);
            }
            iContentProvider = acquireProvider;
            if (iContentProvider == null) {
                Bundle call2 = contentResolver.call(uri, str, "", bundle);
                if (!(activityThread == null || iContentProvider == null || activityThread.releaseProvider(iContentProvider, false))) {
                    IPCLog.e(TAG, "activityThread release provider error");
                }
                return call2;
            }
            if (Build.VERSION.SDK_INT >= 18) {
                bundle2 = iContentProvider.call(AIPC.getContext().getPackageName(), str, "", bundle);
            } else {
                bundle2 = iContentProvider.call(str, "", bundle);
            }
            if (iBinder == null && z) {
                synchronized (Channel.class) {
                    if (iBinder == null) {
                        iBinder = iContentProvider.asBinder();
                        iBinder.linkToDeath(new IBinder.DeathRecipient() {
                            public void binderDied() {
                                IBinder unused = Channel.iBinder = null;
                                LocalBroadcastManager.getInstance(AIPC.getContext()).sendBroadcast(new Intent(Constants.ACTION_DISCONNECT));
                            }
                        }, 0);
                    }
                }
            }
            if (!(activityThread == null || iContentProvider == null || activityThread.releaseProvider(iContentProvider, false))) {
                IPCLog.e(TAG, "activityThread release provider error");
            }
            return bundle2;
        } catch (Throwable th) {
            try {
                IPCLog.eTag(TAG, "content provider call Error:", th);
                return contentResolver.call(uri, str, "", bundle);
            } finally {
                if (!(activityThread == null || iContentProvider == null || activityThread.releaseProvider(iContentProvider, false))) {
                    str2 = "activityThread release provider error";
                    IPCLog.e(TAG, str2);
                }
            }
        }
    }
}
