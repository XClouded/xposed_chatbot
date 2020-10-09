package com.taobao.orange.impl;

import android.os.RemoteException;
import anet.channel.request.ByteArrayEntry;
import anetwork.channel.Request;
import anetwork.channel.aidl.Connection;
import anetwork.channel.degrade.DegradableNetwork;
import anetwork.channel.entity.RequestImpl;
import anetwork.channel.entity.StringParam;
import com.taobao.orange.GlobalOrange;
import com.taobao.orange.inner.INetConnection;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TBNetConnection implements INetConnection {
    private Connection connection;
    private DegradableNetwork network;
    private Map<String, String> params;
    private Request request;

    public void openConnection(String str) throws IOException {
        this.network = new DegradableNetwork(GlobalOrange.context);
        this.request = new RequestImpl(str);
        this.request.setCharset("utf-8");
        this.request.setConnectTimeout(5000);
        this.request.setReadTimeout(5000);
        if (this.params != null && !this.params.isEmpty()) {
            ArrayList arrayList = new ArrayList();
            for (Map.Entry next : this.params.entrySet()) {
                arrayList.add(new StringParam((String) next.getKey(), (String) next.getValue()));
            }
            this.request.setParams(arrayList);
        }
    }

    public void setMethod(String str) throws ProtocolException {
        if (this.request != null) {
            this.request.setMethod(str);
        }
    }

    public void setParams(Map<String, String> map) {
        this.params = map;
    }

    public void addHeader(String str, String str2) {
        if (this.request != null) {
            this.request.addHeader(str, str2);
        }
    }

    public void setBody(byte[] bArr) throws IOException {
        if (this.request != null) {
            this.request.setBodyEntry(new ByteArrayEntry(bArr));
        }
    }

    public int getResponseCode() throws IOException {
        if (this.connection == null) {
            return 0;
        }
        try {
            return this.connection.getStatusCode();
        } catch (RemoteException e) {
            throw new IOException(e);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x0053 A[SYNTHETIC, Splitter:B:34:0x0053] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getResponse() throws java.io.IOException {
        /*
            r6 = this;
            anetwork.channel.aidl.Connection r0 = r6.connection
            r1 = 0
            if (r0 != 0) goto L_0x0006
            return r1
        L_0x0006:
            anetwork.channel.aidl.Connection r0 = r6.connection     // Catch:{ RemoteException -> 0x0046, all -> 0x0041 }
            anetwork.channel.aidl.ParcelableInputStream r0 = r0.getInputStream()     // Catch:{ RemoteException -> 0x0046, all -> 0x0041 }
            java.io.ByteArrayOutputStream r2 = new java.io.ByteArrayOutputStream     // Catch:{ RemoteException -> 0x003c, all -> 0x0037 }
            r2.<init>()     // Catch:{ RemoteException -> 0x003c, all -> 0x0037 }
            r1 = 2048(0x800, float:2.87E-42)
            byte[] r1 = new byte[r1]     // Catch:{ RemoteException -> 0x0035 }
        L_0x0015:
            int r3 = r0.read(r1)     // Catch:{ RemoteException -> 0x0035 }
            r4 = -1
            if (r3 == r4) goto L_0x0021
            r4 = 0
            r2.write(r1, r4, r3)     // Catch:{ RemoteException -> 0x0035 }
            goto L_0x0015
        L_0x0021:
            java.lang.String r1 = new java.lang.String     // Catch:{ RemoteException -> 0x0035 }
            byte[] r3 = r2.toByteArray()     // Catch:{ RemoteException -> 0x0035 }
            java.lang.String r4 = "utf-8"
            r1.<init>(r3, r4)     // Catch:{ RemoteException -> 0x0035 }
            if (r0 == 0) goto L_0x0031
            r0.close()     // Catch:{ RemoteException -> 0x0031 }
        L_0x0031:
            com.taobao.orange.util.OrangeUtils.close(r2)
            return r1
        L_0x0035:
            r1 = move-exception
            goto L_0x004a
        L_0x0037:
            r2 = move-exception
            r5 = r2
            r2 = r1
            r1 = r5
            goto L_0x0051
        L_0x003c:
            r2 = move-exception
            r5 = r2
            r2 = r1
            r1 = r5
            goto L_0x004a
        L_0x0041:
            r0 = move-exception
            r2 = r1
            r1 = r0
            r0 = r2
            goto L_0x0051
        L_0x0046:
            r0 = move-exception
            r2 = r1
            r1 = r0
            r0 = r2
        L_0x004a:
            java.io.IOException r3 = new java.io.IOException     // Catch:{ all -> 0x0050 }
            r3.<init>(r1)     // Catch:{ all -> 0x0050 }
            throw r3     // Catch:{ all -> 0x0050 }
        L_0x0050:
            r1 = move-exception
        L_0x0051:
            if (r0 == 0) goto L_0x0056
            r0.close()     // Catch:{ RemoteException -> 0x0056 }
        L_0x0056:
            com.taobao.orange.util.OrangeUtils.close(r2)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.orange.impl.TBNetConnection.getResponse():java.lang.String");
    }

    public Map<String, List<String>> getHeadFields() {
        if (this.connection == null) {
            return null;
        }
        try {
            return this.connection.getConnHeadFields();
        } catch (RemoteException unused) {
            return null;
        }
    }

    public void connect() throws IOException {
        this.connection = this.network.getConnection(this.request, (Object) null);
    }

    public void disconnect() {
        try {
            if (this.connection != null) {
                this.connection.cancel();
            }
        } catch (RemoteException unused) {
        }
    }
}
