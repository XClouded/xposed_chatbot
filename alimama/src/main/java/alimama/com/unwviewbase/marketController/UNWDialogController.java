package alimama.com.unwviewbase.marketController;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.IEtaoLogger;
import alimama.com.unwbase.interfaces.IRouter;
import alimama.com.unwbase.interfaces.ISharedPreference;
import alimama.com.unwbase.tools.UNWLog;
import alimama.com.unwviewbase.abstractview.UNWAbstractDialog;
import alimama.com.unwviewbase.interfaces.DialogLifeRecycle;
import alimama.com.unwviewbase.interfaces.IDialogController;
import android.text.TextUtils;
import java.lang.ref.SoftReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class UNWDialogController implements IDialogController<UNWAbstractDialog>, DialogLifeRecycle {
    private static final String FILE_NAME = "popup";
    private static final String TAG = "UNWDialogController";
    private static UNWDialogController sInstance = new UNWDialogController();
    public Map<String, SoftReference<UNWAbstractDialog>> bizList = new HashMap();
    private Map<String, String> bizMap = new HashMap();
    public Map<String, SoftReference<UNWAbstractDialog>> conflictList = new HashMap();
    private BizInterrupt interrupt;
    private SoftReference<UNWAbstractDialog> showingDialog = null;
    private Map<String, String> switchMap = new HashMap();

    public enum InterruptStyle {
        NOInterrupt,
        Interrupt,
        InterruptAndPut
    }

    public void init() {
    }

    private UNWDialogController() {
    }

    public SoftReference<UNWAbstractDialog> getShowingDialog() {
        return this.showingDialog;
    }

    public static UNWDialogController getInstance() {
        return sInstance;
    }

    public void init(BizInterrupt bizInterrupt) {
        this.interrupt = bizInterrupt;
    }

    public void init(BizInterrupt bizInterrupt, Map<String, String> map) {
        this.interrupt = bizInterrupt;
        this.switchMap = map;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00b8, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x0104, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean commit(alimama.com.unwviewbase.abstractview.UNWAbstractDialog r9) {
        /*
            r8 = this;
            monitor-enter(r8)
            r0 = 0
            if (r9 != 0) goto L_0x0006
            monitor-exit(r8)
            return r0
        L_0x0006:
            r9.setRecycle(r8)     // Catch:{ all -> 0x014e }
            long r2 = r9.fatigueTime     // Catch:{ ParseException -> 0x0019 }
            java.lang.String r4 = r9.uuid     // Catch:{ ParseException -> 0x0019 }
            java.lang.String r5 = r9.type     // Catch:{ ParseException -> 0x0019 }
            java.lang.String r6 = r9.starttime     // Catch:{ ParseException -> 0x0019 }
            java.lang.String r7 = r9.endtime     // Catch:{ ParseException -> 0x0019 }
            r1 = r8
            boolean r1 = r1.dateCheckCanShow(r2, r4, r5, r6, r7)     // Catch:{ ParseException -> 0x0019 }
            goto L_0x001e
        L_0x0019:
            r1 = move-exception
            r1.printStackTrace()     // Catch:{ all -> 0x014e }
            r1 = 0
        L_0x001e:
            if (r1 != 0) goto L_0x0038
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x014e }
            r1.<init>()     // Catch:{ all -> 0x014e }
            java.lang.String r9 = r9.type     // Catch:{ all -> 0x014e }
            r1.append(r9)     // Catch:{ all -> 0x014e }
            java.lang.String r9 = "疲劳期未过"
            r1.append(r9)     // Catch:{ all -> 0x014e }
            java.lang.String r9 = r1.toString()     // Catch:{ all -> 0x014e }
            r8.log(r9)     // Catch:{ all -> 0x014e }
            monitor-exit(r8)
            return r0
        L_0x0038:
            java.lang.String r1 = "UNWDialogController"
            java.lang.String r2 = r9.type     // Catch:{ all -> 0x014e }
            alimama.com.unwbase.tools.UNWLog.error(r1, r2)     // Catch:{ all -> 0x014e }
            java.lang.String r1 = r9.type     // Catch:{ all -> 0x014e }
            boolean r1 = r8.isSwitchCanShow(r1)     // Catch:{ all -> 0x014e }
            if (r1 != 0) goto L_0x005f
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x014e }
            r1.<init>()     // Catch:{ all -> 0x014e }
            java.lang.String r9 = r9.type     // Catch:{ all -> 0x014e }
            r1.append(r9)     // Catch:{ all -> 0x014e }
            java.lang.String r9 = "开关拦截"
            r1.append(r9)     // Catch:{ all -> 0x014e }
            java.lang.String r9 = r1.toString()     // Catch:{ all -> 0x014e }
            r8.log(r9)     // Catch:{ all -> 0x014e }
            monitor-exit(r8)
            return r0
        L_0x005f:
            java.util.List<java.lang.String> r1 = r9.pageName     // Catch:{ all -> 0x014e }
            boolean r1 = r8.pageCheckCanShow(r1)     // Catch:{ all -> 0x014e }
            if (r1 != 0) goto L_0x007f
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x014e }
            r1.<init>()     // Catch:{ all -> 0x014e }
            java.lang.String r9 = r9.type     // Catch:{ all -> 0x014e }
            r1.append(r9)     // Catch:{ all -> 0x014e }
            java.lang.String r9 = "在指定界面不弹"
            r1.append(r9)     // Catch:{ all -> 0x014e }
            java.lang.String r9 = r1.toString()     // Catch:{ all -> 0x014e }
            r8.log(r9)     // Catch:{ all -> 0x014e }
            monitor-exit(r8)
            return r0
        L_0x007f:
            alimama.com.unwviewbase.marketController.UNWDialogController$InterruptStyle r1 = alimama.com.unwviewbase.marketController.UNWDialogController.InterruptStyle.NOInterrupt     // Catch:{ all -> 0x014e }
            alimama.com.unwviewbase.marketController.BizInterrupt r2 = r8.interrupt     // Catch:{ all -> 0x014e }
            if (r2 == 0) goto L_0x008d
            alimama.com.unwviewbase.marketController.BizInterrupt r1 = r8.interrupt     // Catch:{ all -> 0x014e }
            java.util.Map<java.lang.String, java.lang.String> r2 = r8.bizMap     // Catch:{ all -> 0x014e }
            alimama.com.unwviewbase.marketController.UNWDialogController$InterruptStyle r1 = r1.isInterrupt(r2, r9)     // Catch:{ all -> 0x014e }
        L_0x008d:
            alimama.com.unwviewbase.marketController.UNWDialogController$InterruptStyle r2 = alimama.com.unwviewbase.marketController.UNWDialogController.InterruptStyle.NOInterrupt     // Catch:{ all -> 0x014e }
            if (r1 == r2) goto L_0x00b9
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x014e }
            r2.<init>()     // Catch:{ all -> 0x014e }
            java.lang.String r3 = r9.type     // Catch:{ all -> 0x014e }
            r2.append(r3)     // Catch:{ all -> 0x014e }
            java.lang.String r3 = "业务拦截了"
            r2.append(r3)     // Catch:{ all -> 0x014e }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x014e }
            r8.log(r2)     // Catch:{ all -> 0x014e }
            alimama.com.unwviewbase.marketController.UNWDialogController$InterruptStyle r2 = alimama.com.unwviewbase.marketController.UNWDialogController.InterruptStyle.InterruptAndPut     // Catch:{ all -> 0x014e }
            if (r1 != r2) goto L_0x00b7
            java.util.Map<java.lang.String, java.lang.ref.SoftReference<alimama.com.unwviewbase.abstractview.UNWAbstractDialog>> r1 = r8.bizList     // Catch:{ all -> 0x014e }
            java.lang.String r2 = r9.type     // Catch:{ all -> 0x014e }
            java.lang.ref.SoftReference r3 = new java.lang.ref.SoftReference     // Catch:{ all -> 0x014e }
            r3.<init>(r9)     // Catch:{ all -> 0x014e }
            r1.put(r2, r3)     // Catch:{ all -> 0x014e }
        L_0x00b7:
            monitor-exit(r8)
            return r0
        L_0x00b9:
            int r1 = r9.priority     // Catch:{ all -> 0x014e }
            alimama.com.unwviewbase.marketController.UNWDialogController$InterruptStyle r1 = r8.isHasShowing(r1)     // Catch:{ all -> 0x014e }
            alimama.com.unwviewbase.marketController.UNWDialogController$InterruptStyle r2 = alimama.com.unwviewbase.marketController.UNWDialogController.InterruptStyle.NOInterrupt     // Catch:{ all -> 0x014e }
            if (r1 == r2) goto L_0x0105
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x014e }
            r2.<init>()     // Catch:{ all -> 0x014e }
            java.lang.String r3 = r9.type     // Catch:{ all -> 0x014e }
            r2.append(r3)     // Catch:{ all -> 0x014e }
            java.lang.String r3 = "弹窗冲突"
            r2.append(r3)     // Catch:{ all -> 0x014e }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x014e }
            r8.log(r2)     // Catch:{ all -> 0x014e }
            alimama.com.unwviewbase.marketController.UNWDialogController$InterruptStyle r2 = alimama.com.unwviewbase.marketController.UNWDialogController.InterruptStyle.InterruptAndPut     // Catch:{ all -> 0x014e }
            if (r1 != r2) goto L_0x0103
            java.lang.ref.SoftReference<alimama.com.unwviewbase.abstractview.UNWAbstractDialog> r1 = r8.showingDialog     // Catch:{ all -> 0x014e }
            java.lang.Object r1 = r1.get()     // Catch:{ all -> 0x014e }
            if (r1 == 0) goto L_0x00f7
            java.lang.ref.SoftReference<alimama.com.unwviewbase.abstractview.UNWAbstractDialog> r1 = r8.showingDialog     // Catch:{ all -> 0x014e }
            java.lang.Object r1 = r1.get()     // Catch:{ all -> 0x014e }
            alimama.com.unwviewbase.abstractview.UNWAbstractDialog r1 = (alimama.com.unwviewbase.abstractview.UNWAbstractDialog) r1     // Catch:{ all -> 0x014e }
            java.lang.String r1 = r1.type     // Catch:{ all -> 0x014e }
            java.lang.String r2 = r9.type     // Catch:{ all -> 0x014e }
            boolean r1 = android.text.TextUtils.equals(r1, r2)     // Catch:{ all -> 0x014e }
            if (r1 != 0) goto L_0x0103
        L_0x00f7:
            java.util.Map<java.lang.String, java.lang.ref.SoftReference<alimama.com.unwviewbase.abstractview.UNWAbstractDialog>> r1 = r8.conflictList     // Catch:{ all -> 0x014e }
            java.lang.String r2 = r9.type     // Catch:{ all -> 0x014e }
            java.lang.ref.SoftReference r3 = new java.lang.ref.SoftReference     // Catch:{ all -> 0x014e }
            r3.<init>(r9)     // Catch:{ all -> 0x014e }
            r1.put(r2, r3)     // Catch:{ all -> 0x014e }
        L_0x0103:
            monitor-exit(r8)
            return r0
        L_0x0105:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x014e }
            r0.<init>()     // Catch:{ all -> 0x014e }
            java.lang.String r1 = r9.type     // Catch:{ all -> 0x014e }
            r0.append(r1)     // Catch:{ all -> 0x014e }
            java.lang.String r1 = "开始展示了"
            r0.append(r1)     // Catch:{ all -> 0x014e }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x014e }
            r8.log(r0)     // Catch:{ all -> 0x014e }
            alimama.com.unwbase.UNWManager r0 = alimama.com.unwbase.UNWManager.getInstance()     // Catch:{ all -> 0x014e }
            java.lang.Class<alimama.com.unwbase.interfaces.IEtaoLogger> r1 = alimama.com.unwbase.interfaces.IEtaoLogger.class
            java.lang.Object r0 = r0.getService(r1)     // Catch:{ all -> 0x014e }
            alimama.com.unwbase.interfaces.IEtaoLogger r0 = (alimama.com.unwbase.interfaces.IEtaoLogger) r0     // Catch:{ all -> 0x014e }
            java.lang.String r1 = "UNWDialogController"
            java.lang.String r2 = "UNWDialogController"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x014e }
            r3.<init>()     // Catch:{ all -> 0x014e }
            java.lang.String r4 = r9.type     // Catch:{ all -> 0x014e }
            r3.append(r4)     // Catch:{ all -> 0x014e }
            java.lang.String r4 = "开始展示了"
            r3.append(r4)     // Catch:{ all -> 0x014e }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x014e }
            r0.info(r1, r2, r3)     // Catch:{ all -> 0x014e }
            java.lang.ref.SoftReference r0 = new java.lang.ref.SoftReference     // Catch:{ all -> 0x014e }
            r0.<init>(r9)     // Catch:{ all -> 0x014e }
            r8.showingDialog = r0     // Catch:{ all -> 0x014e }
            r9.show()     // Catch:{ all -> 0x014e }
            r9 = 1
            monitor-exit(r8)
            return r9
        L_0x014e:
            r9 = move-exception
            monitor-exit(r8)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: alimama.com.unwviewbase.marketController.UNWDialogController.commit(alimama.com.unwviewbase.abstractview.UNWAbstractDialog):boolean");
    }

    private boolean dateCheckCanShow(long j, String str, String str2, String str3, String str4) throws ParseException {
        ISharedPreference iSharedPreference;
        if (TextUtils.isEmpty(str2)) {
            return true;
        }
        if (!(j == 0 || TextUtils.isEmpty(str) || (iSharedPreference = (ISharedPreference) UNWManager.getInstance().getService(ISharedPreference.class)) == null)) {
            long j2 = iSharedPreference.getLong("popup", str2, 0);
            String string = iSharedPreference.getString("popup", str2 + "content", "");
            if (j == -1) {
                if (!TextUtils.isEmpty(string) && TextUtils.equals(string, str)) {
                    return false;
                }
            } else if (j2 + j > System.currentTimeMillis()) {
                return false;
            }
        }
        if (!TextUtils.isEmpty(str3) && !TextUtils.isEmpty(str4) && !TextUtils.isEmpty(str)) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.US);
                Date date = new Date(System.currentTimeMillis());
                if (!date.after(simpleDateFormat.parse(str3)) || !date.before(simpleDateFormat.parse(str4))) {
                    return false;
                }
                return true;
            } catch (Throwable unused) {
            }
        }
        return true;
    }

    public void afterShowMarDialog(String str, String str2) {
        ISharedPreference iSharedPreference = (ISharedPreference) UNWManager.getInstance().getService(ISharedPreference.class);
        iSharedPreference.putLong("popup", str, System.currentTimeMillis()).apply();
        iSharedPreference.putString("popup", str + "content", str2).apply();
    }

    private boolean isSwitchCanShow(String str) {
        if (!TextUtils.isEmpty(str) && this.switchMap != null && this.switchMap.get(str) != null && TextUtils.equals(this.switchMap.get(str), "false")) {
            return false;
        }
        return true;
    }

    public boolean pageCheckCanShow(List<String> list) {
        IRouter iRouter;
        if (list == null || list.size() == 0 || (iRouter = (IRouter) UNWManager.getInstance().getService(IRouter.class)) == null || iRouter.getCurrentActivity() == null) {
            return true;
        }
        return !list.contains(iRouter.getCurrentActivity().getClass().getName());
    }

    public void updateEvent(String str, String str2) {
        updateEvent(str, str2, false);
    }

    public void updateEvent(String str, String str2, boolean z) {
        this.bizMap.put(str, str2);
        log("业务事件更新");
        if (z) {
            log("业务事件更新,检查拦截队列");
            for (String next : this.bizList.keySet()) {
                SoftReference softReference = this.bizList.get(next);
                if (softReference != null && commit((UNWAbstractDialog) softReference.get())) {
                    this.bizList.remove(next);
                    return;
                }
            }
        }
    }

    public void checkConflict() {
        log("检查冲突队列");
        for (String next : this.conflictList.keySet()) {
            SoftReference softReference = this.conflictList.get(next);
            if (softReference != null && commit((UNWAbstractDialog) softReference.get())) {
                this.conflictList.remove(next);
                return;
            }
        }
    }

    private InterruptStyle isHasShowing(int i) {
        if (this.showingDialog == null || this.showingDialog.get() == null || !this.showingDialog.get().isShowing()) {
            return InterruptStyle.NOInterrupt;
        }
        int i2 = this.showingDialog.get().priority;
        if (i2 == 0 || i2 <= i) {
            return InterruptStyle.Interrupt;
        }
        close();
        if (this.showingDialog.get().fatigueTime != 0) {
            String str = this.showingDialog.get().type;
            if (!TextUtils.isEmpty(str)) {
                ISharedPreference iSharedPreference = (ISharedPreference) UNWManager.getInstance().getService(ISharedPreference.class);
                iSharedPreference.putLong("popup", str, 0).apply();
                iSharedPreference.putString("popup", str + "content", "").apply();
            }
        }
        UNWAbstractDialog uNWAbstractDialog = this.showingDialog.get();
        this.conflictList.put(uNWAbstractDialog.type, new SoftReference(uNWAbstractDialog));
        this.showingDialog = null;
        return InterruptStyle.NOInterrupt;
    }

    private void close() {
        if (this.showingDialog != null && this.showingDialog.get() != null && this.showingDialog.get().isShowing()) {
            log(this.showingDialog.get().type + "弹窗被关闭了");
            this.showingDialog.get().unactiveDismiss();
        }
    }

    public void setSwitch(String str, String str2) {
        this.switchMap.put(str, str2);
    }

    public void dismiss(UNWAbstractDialog uNWAbstractDialog, boolean z) {
        if (z) {
            checkConflict();
        }
    }

    public void onShow(UNWAbstractDialog uNWAbstractDialog) {
        if (uNWAbstractDialog != null && uNWAbstractDialog.fatigueTime != 0) {
            afterShowMarDialog(uNWAbstractDialog.type, uNWAbstractDialog.uuid);
        }
    }

    private void log(String str) {
        UNWLog.error(TAG, str);
        ((IEtaoLogger) UNWManager.getInstance().getService(IEtaoLogger.class)).info(TAG, TAG, str);
    }
}
