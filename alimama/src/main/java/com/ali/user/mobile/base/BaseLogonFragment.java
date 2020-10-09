package com.ali.user.mobile.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.base.ui.BaseFragment;
import com.taobao.login4android.broadcast.LoginAction;
import com.taobao.login4android.broadcast.LoginBroadcastHelper;

public class BaseLogonFragment extends BaseFragment {
    protected boolean isLoginObserver;
    private BroadcastReceiver mLoginReceiver;

    /* access modifiers changed from: protected */
    public void doWhenReceiveFail() {
    }

    /* access modifiers changed from: protected */
    public void doWhenReceiveSuccess() {
    }

    /* access modifiers changed from: protected */
    public void doWhenReceivedCancel() {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (this.isLoginObserver) {
            this.mLoginReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    LoginAction valueOf;
                    if (intent != null && (valueOf = LoginAction.valueOf(intent.getAction())) != null) {
                        switch (AnonymousClass2.$SwitchMap$com$taobao$login4android$broadcast$LoginAction[valueOf.ordinal()]) {
                            case 1:
                                BaseLogonFragment.this.doWhenReceiveSuccess();
                                return;
                            case 2:
                                BaseLogonFragment.this.doWhenReceivedCancel();
                                return;
                            case 3:
                                BaseLogonFragment.this.doWhenReceiveFail();
                                return;
                            default:
                                return;
                        }
                    }
                }
            };
            LoginBroadcastHelper.registerLoginReceiver(DataProviderFactory.getApplicationContext(), this.mLoginReceiver);
        }
    }

    /* renamed from: com.ali.user.mobile.base.BaseLogonFragment$2  reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$taobao$login4android$broadcast$LoginAction = new int[LoginAction.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(10:0|1|2|3|4|5|6|7|8|10) */
        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        static {
            /*
                com.taobao.login4android.broadcast.LoginAction[] r0 = com.taobao.login4android.broadcast.LoginAction.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$taobao$login4android$broadcast$LoginAction = r0
                int[] r0 = $SwitchMap$com$taobao$login4android$broadcast$LoginAction     // Catch:{ NoSuchFieldError -> 0x0014 }
                com.taobao.login4android.broadcast.LoginAction r1 = com.taobao.login4android.broadcast.LoginAction.NOTIFY_LOGIN_SUCCESS     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = $SwitchMap$com$taobao$login4android$broadcast$LoginAction     // Catch:{ NoSuchFieldError -> 0x001f }
                com.taobao.login4android.broadcast.LoginAction r1 = com.taobao.login4android.broadcast.LoginAction.NOTIFY_LOGIN_CANCEL     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = $SwitchMap$com$taobao$login4android$broadcast$LoginAction     // Catch:{ NoSuchFieldError -> 0x002a }
                com.taobao.login4android.broadcast.LoginAction r1 = com.taobao.login4android.broadcast.LoginAction.NOTIFY_LOGIN_FAILED     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r0 = $SwitchMap$com$taobao$login4android$broadcast$LoginAction     // Catch:{ NoSuchFieldError -> 0x0035 }
                com.taobao.login4android.broadcast.LoginAction r1 = com.taobao.login4android.broadcast.LoginAction.NOTIFY_LOGOUT     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.ali.user.mobile.base.BaseLogonFragment.AnonymousClass2.<clinit>():void");
        }
    }

    public void onDestroy() {
        if (this.mLoginReceiver != null) {
            LoginBroadcastHelper.unregisterLoginReceiver(DataProviderFactory.getApplicationContext(), this.mLoginReceiver);
        }
        super.onDestroy();
    }
}
