package com.huawei.updatesdk.support.f;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.huawei.updatesdk.support.e.c;
import com.huawei.updatesdk.support.e.d;

public class a {
    /* access modifiers changed from: private */
    public b a;
    private Context b;
    private String c;
    private CharSequence d;
    private AlertDialog e;
    private AlertDialog.Builder f;
    /* access modifiers changed from: private */
    public DialogInterface.OnShowListener g;
    /* access modifiers changed from: private */
    public DialogInterface.OnDismissListener h;

    /* renamed from: com.huawei.updatesdk.support.f.a$a  reason: collision with other inner class name */
    public enum C0020a {
        CONFIRM,
        CANCEL
    }

    public interface b {
        void a();
    }

    protected a(Context context, String str, CharSequence charSequence) {
        this.b = context;
        this.c = str;
        this.d = charSequence;
        this.f = new AlertDialog.Builder(context);
        this.f.setTitle(this.c);
        this.f.setPositiveButton(d.b(context, "upsdk_third_app_dl_sure_cancel_download"), (DialogInterface.OnClickListener) null);
        this.f.setNegativeButton(d.b(context, "upsdk_cancel"), (DialogInterface.OnClickListener) null);
        this.f.setMessage(this.d);
    }

    public static a a(Context context, String str, CharSequence charSequence) {
        return (!(context instanceof Activity) || !((Activity) context).isFinishing()) ? new a(context, str, charSequence) : new c(context, str, charSequence);
    }

    public void a() {
        if (this.f != null) {
            this.f.setNegativeButton((CharSequence) null, (DialogInterface.OnClickListener) null);
        }
    }

    public void a(int i, int i2) {
        if (c.a().b() >= 11) {
            Button button = null;
            if (this.e != null) {
                button = this.e.getButton(-1);
            }
            if (button != null) {
                button.setBackgroundResource(i);
                button.setTextColor(this.b.getResources().getColor(i2));
            }
        }
    }

    public void a(DialogInterface.OnDismissListener onDismissListener) {
        this.h = onDismissListener;
    }

    public void a(DialogInterface.OnKeyListener onKeyListener) {
        if (this.e != null) {
            this.e.setOnKeyListener(onKeyListener);
        }
    }

    public void a(DialogInterface.OnShowListener onShowListener) {
        this.g = onShowListener;
    }

    public void a(View view) {
        ImageView imageView;
        if (this.f != null) {
            int b2 = c.a().b();
            if ((b2 < 11 || b2 >= 17) && (imageView = (ImageView) view.findViewById(d.a(view.getContext(), "divider"))) != null) {
                imageView.setVisibility(8);
            }
            this.f.setMessage((CharSequence) null);
            this.f.setView(view);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x001c A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x001d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a(com.huawei.updatesdk.support.f.a.C0020a r2, java.lang.String r3) {
        /*
            r1 = this;
            android.app.AlertDialog r0 = r1.e
            if (r0 != 0) goto L_0x0005
            return
        L_0x0005:
            com.huawei.updatesdk.support.f.a$a r0 = com.huawei.updatesdk.support.f.a.C0020a.CONFIRM
            if (r2 != r0) goto L_0x0011
            android.app.AlertDialog r2 = r1.e
            r0 = -1
        L_0x000c:
            android.widget.Button r2 = r2.getButton(r0)
            goto L_0x001a
        L_0x0011:
            com.huawei.updatesdk.support.f.a$a r0 = com.huawei.updatesdk.support.f.a.C0020a.CANCEL
            if (r2 != r0) goto L_0x0019
            android.app.AlertDialog r2 = r1.e
            r0 = -2
            goto L_0x000c
        L_0x0019:
            r2 = 0
        L_0x001a:
            if (r2 != 0) goto L_0x001d
            return
        L_0x001d:
            r2.setText(r3)
            r3 = 1
            r2.setAllCaps(r3)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.updatesdk.support.f.a.a(com.huawei.updatesdk.support.f.a$a, java.lang.String):void");
    }

    public void a(b bVar) {
        if (this.b == null || ((Activity) this.b).isFinishing()) {
            if (bVar != null) {
                bVar.a();
            }
        } else if (!b()) {
            try {
                this.e = this.f.create();
                this.e.setCanceledOnTouchOutside(false);
                this.e.setOnShowListener(new DialogInterface.OnShowListener() {
                    public void onShow(DialogInterface dialogInterface) {
                        AlertDialog alertDialog = (AlertDialog) dialogInterface;
                        alertDialog.getButton(-1).setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                if (a.this.a != null) {
                                    a.this.a.a();
                                }
                            }
                        });
                        Button button = alertDialog.getButton(-2);
                        if (button != null) {
                            button.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View view) {
                                    if (a.this.a != null) {
                                        a.this.a.b();
                                    }
                                }
                            });
                        }
                        if (a.this.g != null) {
                            a.this.g.onShow(dialogInterface);
                        }
                    }
                });
                this.e.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (a.this.h != null) {
                            a.this.h.onDismiss(dialogInterface);
                        }
                    }
                });
                this.e.show();
            } catch (Exception e2) {
                if (bVar != null) {
                    bVar.a();
                }
                com.huawei.updatesdk.sdk.a.c.a.a.a.a("BaseAlertDialog", "show dlg error, e: ", e2);
            }
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("show dlg error, mContext = ");
            sb.append(this.b);
            sb.append(", mContext.isFinishing is ");
            sb.append(this.b == null ? "mContext == null" : Boolean.valueOf(((Activity) this.b).isFinishing()));
            com.huawei.updatesdk.sdk.a.c.a.a.a.d("BaseAlertDialog", sb.toString());
        }
    }

    public void a(b bVar) {
        this.a = bVar;
    }

    public void a(boolean z) {
        if (this.e != null) {
            this.e.setCancelable(z);
        }
    }

    public boolean b() {
        return this.e != null && this.e.isShowing();
    }

    public void c() {
        try {
            if (this.e != null) {
                this.e.dismiss();
                this.e = null;
            }
        } catch (IllegalArgumentException unused) {
            com.huawei.updatesdk.sdk.a.c.a.a.a.a("BaseAlertDialog", "dialog dismiss IllegalArgumentException");
        }
    }
}
