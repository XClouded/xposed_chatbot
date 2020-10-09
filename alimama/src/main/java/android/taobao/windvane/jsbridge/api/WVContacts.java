package android.taobao.windvane.jsbridge.api;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.runtimepermission.PermissionProposer;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.thread.WVThreadPool;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class WVContacts extends WVApiPlugin {
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE = "phone";
    private static final String[] PHONES_PROJECTION = {"display_name", "data1"};
    private static final String TAG = "WVContacts";
    private WVCallBackContext mCallback = null;

    public boolean execute(String str, final String str2, final WVCallBackContext wVCallBackContext) {
        boolean z;
        if ("choose".equals(str)) {
            try {
                PermissionProposer.buildPermissionTask(this.mContext, new String[]{"android.permission.READ_CONTACTS"}).setTaskOnPermissionGranted(new Runnable() {
                    public void run() {
                        WVThreadPool.getInstance().execute(new Runnable() {
                            public void run() {
                                WVContacts.this.choose(str2, wVCallBackContext);
                            }
                        });
                    }
                }).setTaskOnPermissionDenied(new Runnable() {
                    public void run() {
                        WVResult wVResult = new WVResult();
                        wVResult.addData("msg", "NO_PERMISSION");
                        wVCallBackContext.error(wVResult);
                    }
                }).execute();
            } catch (Exception unused) {
            }
        } else if ("find".equals(str)) {
            PermissionProposer.buildPermissionTask(this.mContext, new String[]{"android.permission.READ_CONTACTS"}).setTaskOnPermissionGranted(new Runnable() {
                public void run() {
                    WVThreadPool.getInstance().execute(new Runnable() {
                        public void run() {
                            WVContacts.this.find(str2, wVCallBackContext);
                        }
                    });
                }
            }).setTaskOnPermissionDenied(new Runnable() {
                public void run() {
                    WVResult wVResult = new WVResult();
                    wVResult.addData("msg", "NO_PERMISSION");
                    wVCallBackContext.error(wVResult);
                }
            }).execute();
        } else if (!"authStatus".equals(str)) {
            return false;
        } else {
            try {
                z = new JSONObject(str2).optBoolean("autoAskAuth", true);
            } catch (JSONException unused2) {
                TaoLog.e("WVContacts", "authStatus when parse params to JSON error, params=" + str2);
                z = true;
            }
            if (!z) {
                authStatus(wVCallBackContext);
            } else {
                PermissionProposer.buildPermissionTask(this.mContext, new String[]{"android.permission.READ_CONTACTS"}).setTaskOnPermissionGranted(new Runnable() {
                    public void run() {
                        WVThreadPool.getInstance().execute(new Runnable() {
                            public void run() {
                                WVContacts.this.authStatus(wVCallBackContext);
                            }
                        });
                    }
                }).setTaskOnPermissionDenied(new Runnable() {
                    public void run() {
                        WVResult wVResult = new WVResult();
                        wVResult.addData("msg", "NO_PERMISSION");
                        wVCallBackContext.error(wVResult);
                    }
                }).execute();
            }
        }
        WVEventService.getInstance().onEvent(3014);
        return true;
    }

    /* access modifiers changed from: private */
    public void choose(String str, WVCallBackContext wVCallBackContext) {
        this.mCallback = wVCallBackContext;
        Intent intent = new Intent("android.intent.action.PICK", ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        if (this.mContext instanceof Activity) {
            try {
                ((Activity) this.mContext).startActivityForResult(intent, 4003);
            } catch (Exception e) {
                TaoLog.e("WVContacts", "open pick activity fail, " + e.getMessage());
                wVCallBackContext.error();
            }
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x003c  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x004c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void find(java.lang.String r7, android.taobao.windvane.jsbridge.WVCallBackContext r8) {
        /*
            r6 = this;
            r0 = 0
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ JSONException -> 0x001e }
            r1.<init>(r7)     // Catch:{ JSONException -> 0x001e }
            java.lang.String r2 = "filter"
            org.json.JSONObject r1 = r1.optJSONObject(r2)     // Catch:{ JSONException -> 0x001e }
            if (r1 == 0) goto L_0x001b
            java.lang.String r2 = "name"
            java.lang.String r2 = r1.optString(r2)     // Catch:{ JSONException -> 0x001e }
            java.lang.String r3 = "phone"
            java.lang.String r1 = r1.optString(r3)     // Catch:{ JSONException -> 0x001f }
            goto L_0x0036
        L_0x001b:
            r1 = r0
            r2 = r1
            goto L_0x0036
        L_0x001e:
            r2 = r0
        L_0x001f:
            java.lang.String r1 = "WVContacts"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "find contacts when parse params to JSON error, params="
            r3.append(r4)
            r3.append(r7)
            java.lang.String r7 = r3.toString()
            android.taobao.windvane.util.TaoLog.e(r1, r7)
            r1 = r0
        L_0x0036:
            java.util.List r7 = r6.getPhoneContacts(r0, r2, r1)
            if (r7 != 0) goto L_0x004c
            java.lang.String r7 = "WVContacts"
            java.lang.String r0 = "find contacts failed"
            android.taobao.windvane.util.TaoLog.w(r7, r0)
            android.taobao.windvane.jsbridge.WVResult r7 = new android.taobao.windvane.jsbridge.WVResult
            r7.<init>()
            r8.error((android.taobao.windvane.jsbridge.WVResult) r7)
            return
        L_0x004c:
            android.taobao.windvane.jsbridge.WVResult r0 = new android.taobao.windvane.jsbridge.WVResult
            r0.<init>()
            org.json.JSONArray r1 = new org.json.JSONArray
            r1.<init>()
            java.util.Iterator r7 = r7.iterator()     // Catch:{ JSONException -> 0x007d }
        L_0x005a:
            boolean r2 = r7.hasNext()     // Catch:{ JSONException -> 0x007d }
            if (r2 == 0) goto L_0x0098
            java.lang.Object r2 = r7.next()     // Catch:{ JSONException -> 0x007d }
            android.taobao.windvane.jsbridge.api.WVContacts$ContactInfo r2 = (android.taobao.windvane.jsbridge.api.WVContacts.ContactInfo) r2     // Catch:{ JSONException -> 0x007d }
            org.json.JSONObject r3 = new org.json.JSONObject     // Catch:{ JSONException -> 0x007d }
            r3.<init>()     // Catch:{ JSONException -> 0x007d }
            java.lang.String r4 = "name"
            java.lang.String r5 = r2.name     // Catch:{ JSONException -> 0x007d }
            r3.put(r4, r5)     // Catch:{ JSONException -> 0x007d }
            java.lang.String r4 = "phone"
            java.lang.String r2 = r2.number     // Catch:{ JSONException -> 0x007d }
            r3.put(r4, r2)     // Catch:{ JSONException -> 0x007d }
            r1.put(r3)     // Catch:{ JSONException -> 0x007d }
            goto L_0x005a
        L_0x007d:
            r7 = move-exception
            java.lang.String r2 = "WVContacts"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "put contacts error, "
            r3.append(r4)
            java.lang.String r7 = r7.getMessage()
            r3.append(r7)
            java.lang.String r7 = r3.toString()
            android.taobao.windvane.util.TaoLog.e(r2, r7)
        L_0x0098:
            java.lang.String r7 = "contacts"
            r0.addData((java.lang.String) r7, (org.json.JSONArray) r1)
            r8.success((android.taobao.windvane.jsbridge.WVResult) r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.jsbridge.api.WVContacts.find(java.lang.String, android.taobao.windvane.jsbridge.WVCallBackContext):void");
    }

    /* access modifiers changed from: private */
    public void authStatus(final WVCallBackContext wVCallBackContext) {
        new AsyncTask<Void, Integer, Void>() {
            /* access modifiers changed from: protected */
            public Void doInBackground(Void... voidArr) {
                Cursor cursor;
                WVResult wVResult = new WVResult();
                try {
                    cursor = WVContacts.this.mContext.getContentResolver().query(Uri.parse("content://com.android.contacts/contacts"), new String[]{"_id"}, (String) null, (String[]) null, (String) null);
                } catch (Exception unused) {
                    cursor = null;
                }
                if (cursor == null) {
                    wVResult.addData("isAuthed", "0");
                } else {
                    wVResult.addData("isAuthed", "1");
                }
                wVCallBackContext.success(wVResult);
                if (cursor != null) {
                    try {
                        cursor.close();
                    } catch (Exception unused2) {
                    }
                }
                return null;
            }
        }.execute(new Void[0]);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        Uri data;
        if (i == 4003 && this.mCallback != null) {
            if (i2 == -1) {
                if (intent == null || (data = intent.getData()) == null) {
                    TaoLog.w("WVContacts", "contact data is null");
                    return;
                }
                String lastPathSegment = data.getLastPathSegment();
                if (!TextUtils.isEmpty(lastPathSegment)) {
                    List<ContactInfo> phoneContacts = getPhoneContacts(lastPathSegment, (String) null, (String) null);
                    if (phoneContacts == null || phoneContacts.isEmpty()) {
                        TaoLog.w("WVContacts", "contact result is empty");
                        this.mCallback.error(new WVResult());
                        return;
                    }
                    ContactInfo contactInfo = phoneContacts.get(0);
                    if (!TextUtils.isEmpty(contactInfo.number)) {
                        WVResult wVResult = new WVResult();
                        wVResult.addData("name", contactInfo.name);
                        wVResult.addData(KEY_PHONE, contactInfo.number);
                        this.mCallback.success(wVResult);
                        return;
                    }
                }
            }
            if (TaoLog.getLogStatus()) {
                TaoLog.d("WVContacts", "choose contact failed");
            }
            this.mCallback.error(new WVResult());
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x00db  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00f7  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0181 A[SYNTHETIC, Splitter:B:57:0x0181] */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x018e A[SYNTHETIC, Splitter:B:65:0x018e] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.List<android.taobao.windvane.jsbridge.api.WVContacts.ContactInfo> getPhoneContacts(java.lang.String r13, java.lang.String r14, java.lang.String r15) {
        /*
            r12 = this;
            boolean r0 = android.taobao.windvane.util.TaoLog.getLogStatus()
            if (r0 == 0) goto L_0x002c
            java.lang.String r0 = "WVContacts"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "contactId: "
            r1.append(r2)
            r1.append(r13)
            java.lang.String r2 = " filterName: "
            r1.append(r2)
            r1.append(r14)
            java.lang.String r2 = " filterNumber: "
            r1.append(r2)
            r1.append(r15)
            java.lang.String r1 = r1.toString()
            android.taobao.windvane.util.TaoLog.d(r0, r1)
        L_0x002c:
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r1 = 0
            boolean r2 = android.text.TextUtils.isEmpty(r13)     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            r3 = 0
            r4 = 1
            if (r2 != 0) goto L_0x0044
            java.lang.String r14 = "_id= ?"
            java.lang.String[] r15 = new java.lang.String[r4]     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            r15[r3] = r13     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            r8 = r14
        L_0x0041:
            r9 = r15
            goto L_0x00ca
        L_0x0044:
            boolean r13 = android.text.TextUtils.isEmpty(r14)     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            if (r13 != 0) goto L_0x0088
            boolean r13 = android.text.TextUtils.isEmpty(r15)     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            if (r13 != 0) goto L_0x0088
            java.lang.String r13 = "display_name like ? AND data1 like ?"
            r2 = 2
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            r5.<init>()     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            java.lang.String r6 = "%"
            r5.append(r6)     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            r5.append(r14)     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            java.lang.String r14 = "%"
            r5.append(r14)     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            java.lang.String r14 = r5.toString()     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            r2[r3] = r14     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            r14.<init>()     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            java.lang.String r5 = "%"
            r14.append(r5)     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            r14.append(r15)     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            java.lang.String r15 = "%"
            r14.append(r15)     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            java.lang.String r14 = r14.toString()     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            r2[r4] = r14     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            r8 = r13
            r9 = r2
            goto L_0x00ca
        L_0x0088:
            boolean r13 = android.text.TextUtils.isEmpty(r14)     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            if (r13 != 0) goto L_0x00ac
            java.lang.String r13 = "display_name like ?"
            java.lang.String[] r15 = new java.lang.String[r4]     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            r2.<init>()     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            java.lang.String r5 = "%"
            r2.append(r5)     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            r2.append(r14)     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            java.lang.String r14 = "%"
            r2.append(r14)     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            java.lang.String r14 = r2.toString()     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            r15[r3] = r14     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            r8 = r13
            goto L_0x0041
        L_0x00ac:
            java.lang.String r14 = "data1 like ?"
            java.lang.String[] r13 = new java.lang.String[r4]     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            r2.<init>()     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            java.lang.String r5 = "%"
            r2.append(r5)     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            r2.append(r15)     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            java.lang.String r15 = "%"
            r2.append(r15)     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            java.lang.String r15 = r2.toString()     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            r13[r3] = r15     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            r9 = r13
            r8 = r14
        L_0x00ca:
            android.content.Context r13 = r12.mContext     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            android.content.ContentResolver r5 = r13.getContentResolver()     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            android.net.Uri r6 = android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            java.lang.String[] r7 = PHONES_PROJECTION     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            r10 = 0
            android.database.Cursor r13 = r5.query(r6, r7, r8, r9, r10)     // Catch:{ Exception -> 0x0163, all -> 0x0161 }
            if (r13 != 0) goto L_0x00f7
            java.lang.String r14 = "WVContacts"
            java.lang.String r15 = "cursor is null."
            android.util.Log.w(r14, r15)     // Catch:{ Exception -> 0x00f2, all -> 0x00ed }
            if (r13 == 0) goto L_0x00ec
            r13.close()     // Catch:{ Exception -> 0x00e8 }
            goto L_0x00ec
        L_0x00e8:
            r13 = move-exception
            r13.printStackTrace()
        L_0x00ec:
            return r1
        L_0x00ed:
            r14 = move-exception
            r1 = r13
            r13 = r14
            goto L_0x018c
        L_0x00f2:
            r14 = move-exception
            r11 = r14
            r14 = r13
            r13 = r11
            goto L_0x0165
        L_0x00f7:
            java.lang.String r14 = "WVContacts"
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00f2, all -> 0x00ed }
            r15.<init>()     // Catch:{ Exception -> 0x00f2, all -> 0x00ed }
            java.lang.String r2 = "find contacts record "
            r15.append(r2)     // Catch:{ Exception -> 0x00f2, all -> 0x00ed }
            int r2 = r13.getCount()     // Catch:{ Exception -> 0x00f2, all -> 0x00ed }
            r15.append(r2)     // Catch:{ Exception -> 0x00f2, all -> 0x00ed }
            java.lang.String r15 = r15.toString()     // Catch:{ Exception -> 0x00f2, all -> 0x00ed }
            android.util.Log.d(r14, r15)     // Catch:{ Exception -> 0x00f2, all -> 0x00ed }
        L_0x0111:
            boolean r14 = r13.moveToNext()     // Catch:{ Exception -> 0x00f2, all -> 0x00ed }
            if (r14 == 0) goto L_0x0156
            java.lang.String r14 = r13.getString(r3)     // Catch:{ Exception -> 0x00f2, all -> 0x00ed }
            java.lang.String r15 = r13.getString(r4)     // Catch:{ Exception -> 0x00f2, all -> 0x00ed }
            boolean r2 = android.text.TextUtils.isEmpty(r14)     // Catch:{ Exception -> 0x00f2, all -> 0x00ed }
            if (r2 == 0) goto L_0x012b
            boolean r2 = android.text.TextUtils.isEmpty(r15)     // Catch:{ Exception -> 0x00f2, all -> 0x00ed }
            if (r2 != 0) goto L_0x0137
        L_0x012b:
            android.taobao.windvane.jsbridge.api.WVContacts$ContactInfo r2 = new android.taobao.windvane.jsbridge.api.WVContacts$ContactInfo     // Catch:{ Exception -> 0x00f2, all -> 0x00ed }
            r2.<init>()     // Catch:{ Exception -> 0x00f2, all -> 0x00ed }
            r2.name = r14     // Catch:{ Exception -> 0x00f2, all -> 0x00ed }
            r2.number = r15     // Catch:{ Exception -> 0x00f2, all -> 0x00ed }
            r0.add(r2)     // Catch:{ Exception -> 0x00f2, all -> 0x00ed }
        L_0x0137:
            java.lang.String r2 = "WVContacts"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00f2, all -> 0x00ed }
            r5.<init>()     // Catch:{ Exception -> 0x00f2, all -> 0x00ed }
            java.lang.String r6 = "displayName: "
            r5.append(r6)     // Catch:{ Exception -> 0x00f2, all -> 0x00ed }
            r5.append(r14)     // Catch:{ Exception -> 0x00f2, all -> 0x00ed }
            java.lang.String r14 = " phoneNumber: "
            r5.append(r14)     // Catch:{ Exception -> 0x00f2, all -> 0x00ed }
            r5.append(r15)     // Catch:{ Exception -> 0x00f2, all -> 0x00ed }
            java.lang.String r14 = r5.toString()     // Catch:{ Exception -> 0x00f2, all -> 0x00ed }
            android.util.Log.d(r2, r14)     // Catch:{ Exception -> 0x00f2, all -> 0x00ed }
            goto L_0x0111
        L_0x0156:
            if (r13 == 0) goto L_0x0160
            r13.close()     // Catch:{ Exception -> 0x015c }
            goto L_0x0160
        L_0x015c:
            r13 = move-exception
            r13.printStackTrace()
        L_0x0160:
            return r0
        L_0x0161:
            r13 = move-exception
            goto L_0x018c
        L_0x0163:
            r13 = move-exception
            r14 = r1
        L_0x0165:
            java.lang.String r15 = "WVContacts"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x018a }
            r0.<init>()     // Catch:{ all -> 0x018a }
            java.lang.String r2 = "query phone error, "
            r0.append(r2)     // Catch:{ all -> 0x018a }
            java.lang.String r13 = r13.getMessage()     // Catch:{ all -> 0x018a }
            r0.append(r13)     // Catch:{ all -> 0x018a }
            java.lang.String r13 = r0.toString()     // Catch:{ all -> 0x018a }
            android.taobao.windvane.util.TaoLog.e(r15, r13)     // Catch:{ all -> 0x018a }
            if (r14 == 0) goto L_0x0189
            r14.close()     // Catch:{ Exception -> 0x0185 }
            goto L_0x0189
        L_0x0185:
            r13 = move-exception
            r13.printStackTrace()
        L_0x0189:
            return r1
        L_0x018a:
            r13 = move-exception
            r1 = r14
        L_0x018c:
            if (r1 == 0) goto L_0x0196
            r1.close()     // Catch:{ Exception -> 0x0192 }
            goto L_0x0196
        L_0x0192:
            r14 = move-exception
            r14.printStackTrace()
        L_0x0196:
            throw r13
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.jsbridge.api.WVContacts.getPhoneContacts(java.lang.String, java.lang.String, java.lang.String):java.util.List");
    }

    private class ContactInfo {
        String name;
        String number;

        private ContactInfo() {
        }
    }
}
