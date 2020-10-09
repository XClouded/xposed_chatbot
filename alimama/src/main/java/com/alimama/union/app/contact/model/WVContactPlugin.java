package com.alimama.union.app.contact.model;

import android.content.Context;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import com.alibaba.fastjson.JSON;
import com.alimama.moon.App;
import com.alimama.moon.eventbus.IEventBus;
import com.alimama.moon.eventbus.ISubscriber;
import com.alimama.moon.web.WebActivity;
import com.alimama.union.app.contact.model.ContactEvent;
import com.alimama.union.app.infrastructure.permission.Permission;
import com.alimama.union.app.privacy.PermissionInterface;
import com.alimama.union.app.privacy.PrivacyPermissionManager;
import com.alimama.union.app.privacy.PrivacyUtil;
import java.lang.ref.SoftReference;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WVContactPlugin extends WVApiPlugin implements ISubscriber, PermissionInterface {
    public static final String HY_NO_PERMISSION_READ_CONTACTS = "HY_NO_PERMISSION_READ_CONTACTS";
    private SoftReference<String> cacheParams;
    private SoftReference<WVCallBackContext> cacheWVCallBackContext;
    @Inject
    ContactService contactService;
    WebActivity context;
    @Inject
    IEventBus eventBus;
    private Logger logger = LoggerFactory.getLogger((Class<?>) WVContactPlugin.class);
    String params;
    @Inject
    @Named("READ_CONTACTS")
    Permission permission;
    private PrivacyPermissionManager privacyPermissionManager;
    WVCallBackContext wvCallBackContext;

    public WVContactPlugin() {
        this.logger.info("constructor");
        App.getAppComponent().inject(this);
        this.eventBus.register(this);
    }

    public void onDestroy() {
        super.onDestroy();
        this.logger.info("onDestroy");
        this.eventBus.unregister(this);
    }

    public void openPermissionRequest() {
        int intValue = JSON.parseObject(this.params).getInteger("size").intValue();
        switch (this.permission.checkPermission(this.context)) {
            case -1:
                if (this.permission.shouldShowPermissionRationale(this.context)) {
                    this.logger.info("HY_NO_PERMISSION, user has rejected");
                    this.wvCallBackContext.error(new WVResult(HY_NO_PERMISSION_READ_CONTACTS));
                    return;
                }
                this.logger.info("HY_NO_PERMISSION, request permission");
                this.cacheParams = new SoftReference<>(this.params);
                this.cacheWVCallBackContext = new SoftReference<>(this.wvCallBackContext);
                this.permission.requestPermission(this.context);
                return;
            case 0:
                this.logger.info("HY_SUCCESS, has permission");
                List<Contact> queryLocalContacts = this.contactService.queryLocalContacts(this.context, intValue);
                WVResult wVResult = new WVResult();
                wVResult.addData("contacts", JSON.toJSON(queryLocalContacts));
                this.wvCallBackContext.success(wVResult);
                return;
            default:
                return;
        }
    }

    public void closePermissionRequest() {
        this.cacheWVCallBackContext = new SoftReference<>(this.wvCallBackContext);
        if (this.cacheWVCallBackContext != null && this.cacheWVCallBackContext.get() != null) {
            this.cacheWVCallBackContext.get().error(new WVResult(HY_NO_PERMISSION_READ_CONTACTS));
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x003e A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x003f  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0043  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean execute(java.lang.String r8, java.lang.String r9, android.taobao.windvane.jsbridge.WVCallBackContext r10) {
        /*
            r7 = this;
            org.slf4j.Logger r0 = r7.logger
            java.lang.String r1 = "execute, action: {}, params: {}, WVCallBackContext: {}"
            r2 = 3
            java.lang.Object[] r2 = new java.lang.Object[r2]
            r3 = 0
            r2[r3] = r8
            r4 = 1
            r2[r4] = r9
            java.lang.String r5 = r10.getToken()
            r6 = 2
            r2[r6] = r5
            r0.info((java.lang.String) r1, (java.lang.Object[]) r2)
            int r0 = r8.hashCode()
            r1 = 1230345531(0x4955953b, float:874835.7)
            if (r0 == r1) goto L_0x0030
            r1 = 1979902129(0x7602e8b1, float:6.6378724E32)
            if (r0 == r1) goto L_0x0026
            goto L_0x003a
        L_0x0026:
            java.lang.String r0 = "sendSms"
            boolean r8 = r8.equals(r0)
            if (r8 == 0) goto L_0x003a
            r8 = 1
            goto L_0x003b
        L_0x0030:
            java.lang.String r0 = "queryContacts"
            boolean r8 = r8.equals(r0)
            if (r8 == 0) goto L_0x003a
            r8 = 0
            goto L_0x003b
        L_0x003a:
            r8 = -1
        L_0x003b:
            switch(r8) {
                case 0: goto L_0x0043;
                case 1: goto L_0x003f;
                default: goto L_0x003e;
            }
        L_0x003e:
            return r3
        L_0x003f:
            r7.sendSms(r9, r10)
            return r4
        L_0x0043:
            r7.queryContacts(r9, r10)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alimama.union.app.contact.model.WVContactPlugin.execute(java.lang.String, java.lang.String, android.taobao.windvane.jsbridge.WVCallBackContext):boolean");
    }

    private void queryContacts(String str, WVCallBackContext wVCallBackContext) {
        WebActivity webActivity = (WebActivity) wVCallBackContext.getWebview().getContext();
        this.privacyPermissionManager = new PrivacyPermissionManager((Context) webActivity, (PermissionInterface) this);
        this.context = webActivity;
        this.wvCallBackContext = wVCallBackContext;
        this.params = str;
        boolean shouldShowPermissionRationale = this.permission.shouldShowPermissionRationale(webActivity);
        if (PrivacyUtil.hasContactPermission(webActivity) || shouldShowPermissionRationale) {
            openPermissionRequest();
        } else {
            this.privacyPermissionManager.showReadContactPermissionDialog();
        }
    }

    private void sendSms(String str, WVCallBackContext wVCallBackContext) {
        this.contactService.sendSms(wVCallBackContext.getWebview().getContext(), (BatchSms) JSON.parseObject(str, BatchSms.class));
    }

    @Subscribe
    public void onPermissionGranted(ContactEvent.PermissionGranted permissionGranted) {
        if (this.cacheParams != null && this.cacheParams.get() != null && this.cacheWVCallBackContext != null && this.cacheWVCallBackContext.get() != null) {
            queryContacts(this.cacheParams.get(), this.cacheWVCallBackContext.get());
        }
    }

    @Subscribe
    public void onPermissionDenied(ContactEvent.PermissionDenied permissionDenied) {
        if (this.cacheWVCallBackContext != null && this.cacheWVCallBackContext.get() != null) {
            this.cacheWVCallBackContext.get().error(new WVResult(HY_NO_PERMISSION_READ_CONTACTS));
        }
    }
}
