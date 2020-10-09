package com.alimama.union.app.personalCenter.view;

import android.content.Context;
import android.widget.CompoundButton;
import androidx.appcompat.app.AppCompatDialog;

public class EnvSelectorDialog extends AppCompatDialog implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "EnvSelectorDialog";

    public EnvSelectorDialog(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0063  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0082  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0086  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x008a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onCreate(android.os.Bundle r7) {
        /*
            r6 = this;
            super.onCreate(r7)
            r7 = 2130903152(0x7f030070, float:1.7413114E38)
            r6.setContentView((int) r7)
            r7 = 2131690071(0x7f0f0257, float:1.9009175E38)
            android.view.View r7 = r6.findViewById(r7)
            android.widget.RadioButton r7 = (android.widget.RadioButton) r7
            r0 = 2131690072(0x7f0f0258, float:1.9009177E38)
            android.view.View r0 = r6.findViewById(r0)
            android.widget.RadioButton r0 = (android.widget.RadioButton) r0
            r1 = 2131690073(0x7f0f0259, float:1.900918E38)
            android.view.View r1 = r6.findViewById(r1)
            android.widget.RadioButton r1 = (android.widget.RadioButton) r1
            com.alimama.union.app.configproperties.EnvHelper r2 = com.alimama.union.app.configproperties.EnvHelper.getInstance()
            java.lang.String r2 = r2.getCurrentApiEnv()
            int r3 = r2.hashCode()
            r4 = -1012222381(0xffffffffc3aab653, float:-341.4244)
            r5 = 1
            if (r3 == r4) goto L_0x0055
            r4 = -318370553(0xffffffffed060d07, float:-2.5929213E27)
            if (r3 == r4) goto L_0x004b
            r4 = 95458899(0x5b09653, float:1.6606181E-35)
            if (r3 == r4) goto L_0x0041
            goto L_0x005f
        L_0x0041:
            java.lang.String r3 = "debug"
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x005f
            r2 = 0
            goto L_0x0060
        L_0x004b:
            java.lang.String r3 = "prepare"
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x005f
            r2 = 1
            goto L_0x0060
        L_0x0055:
            java.lang.String r3 = "online"
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x005f
            r2 = 2
            goto L_0x0060
        L_0x005f:
            r2 = -1
        L_0x0060:
            switch(r2) {
                case 0: goto L_0x008a;
                case 1: goto L_0x0086;
                case 2: goto L_0x0082;
                default: goto L_0x0063;
            }
        L_0x0063:
            java.lang.String r2 = "EnvSelectorDialog"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Unknown env "
            r3.append(r4)
            com.alimama.union.app.configproperties.EnvHelper r4 = com.alimama.union.app.configproperties.EnvHelper.getInstance()
            java.lang.String r4 = r4.getCurrentApiEnv()
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            android.util.Log.w(r2, r3)
            goto L_0x008d
        L_0x0082:
            r1.setChecked(r5)
            goto L_0x008d
        L_0x0086:
            r0.setChecked(r5)
            goto L_0x008d
        L_0x008a:
            r7.setChecked(r5)
        L_0x008d:
            r7.setOnCheckedChangeListener(r6)
            java.lang.String r2 = "debug"
            r7.setTag(r2)
            r0.setOnCheckedChangeListener(r6)
            java.lang.String r7 = "prepare"
            r0.setTag(r7)
            r1.setOnCheckedChangeListener(r6)
            java.lang.String r7 = "online"
            r1.setTag(r7)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alimama.union.app.personalCenter.view.EnvSelectorDialog.onCreate(android.os.Bundle):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x003e  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0055  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onCheckedChanged(android.widget.CompoundButton r5, boolean r6) {
        /*
            r4 = this;
            java.lang.Object r5 = r5.getTag()
            java.lang.String r5 = (java.lang.String) r5
            int r0 = r5.hashCode()
            r1 = -1012222381(0xffffffffc3aab653, float:-341.4244)
            r2 = 0
            r3 = 1
            if (r0 == r1) goto L_0x0030
            r1 = -318370553(0xffffffffed060d07, float:-2.5929213E27)
            if (r0 == r1) goto L_0x0026
            r1 = 95458899(0x5b09653, float:1.6606181E-35)
            if (r0 == r1) goto L_0x001c
            goto L_0x003a
        L_0x001c:
            java.lang.String r0 = "debug"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x003a
            r0 = 0
            goto L_0x003b
        L_0x0026:
            java.lang.String r0 = "prepare"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x003a
            r0 = 1
            goto L_0x003b
        L_0x0030:
            java.lang.String r0 = "online"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x003a
            r0 = 2
            goto L_0x003b
        L_0x003a:
            r0 = -1
        L_0x003b:
            switch(r0) {
                case 0: goto L_0x0055;
                case 1: goto L_0x0055;
                case 2: goto L_0x0055;
                default: goto L_0x003e;
            }
        L_0x003e:
            android.content.Context r6 = r4.getContext()
            android.content.Context r0 = r4.getContext()
            r1 = 2131297369(0x7f090459, float:1.821268E38)
            java.lang.Object[] r3 = new java.lang.Object[r3]
            r3[r2] = r5
            java.lang.String r5 = r0.getString(r1, r3)
            com.alimama.moon.utils.ToastUtil.showToast((android.content.Context) r6, (java.lang.String) r5)
            goto L_0x006b
        L_0x0055:
            if (r6 == 0) goto L_0x006b
            com.taobao.login4android.Login.logout()
            com.taobao.login4android.Login.login(r3)
            com.alimama.union.app.configproperties.EnvHelper r6 = com.alimama.union.app.configproperties.EnvHelper.getInstance()
            r6.changeApiEnv(r5)
            android.content.Context r5 = r4.getContext()
            com.alimama.union.app.configproperties.EnvHelper.restartApp(r5)
        L_0x006b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alimama.union.app.personalCenter.view.EnvSelectorDialog.onCheckedChanged(android.widget.CompoundButton, boolean):void");
    }
}
