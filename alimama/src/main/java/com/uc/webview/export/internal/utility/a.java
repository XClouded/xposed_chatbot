package com.uc.webview.export.internal.utility;

import androidx.core.view.InputDeviceCompat;
import com.ali.user.mobile.ui.WebConstant;
import com.alibaba.taffy.core.util.codec.MessageDigestAlgorithms;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.DigestException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import kotlin.UShort;

/* compiled from: U4Source */
public final class a {
    /* JADX WARNING: Removed duplicated region for block: B:30:0x005f  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0065  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean a(java.lang.String r10) throws java.io.IOException {
        /*
            r0 = 0
            r1 = 0
            java.io.RandomAccessFile r2 = new java.io.RandomAccessFile     // Catch:{ a -> 0x0063, all -> 0x005b }
            java.lang.String r3 = "r"
            r2.<init>(r10, r3)     // Catch:{ a -> 0x0063, all -> 0x005b }
            long r8 = r2.length()     // Catch:{ a -> 0x0059, all -> 0x0057 }
            r3 = 2147483647(0x7fffffff, double:1.060997895E-314)
            int r10 = (r8 > r3 ? 1 : (r8 == r3 ? 0 : -1))
            if (r10 <= 0) goto L_0x0018
            r2.close()
            return r0
        L_0x0018:
            java.nio.channels.FileChannel r4 = r2.getChannel()     // Catch:{ IOException -> 0x0042 }
            java.nio.channels.FileChannel$MapMode r5 = java.nio.channels.FileChannel.MapMode.READ_ONLY     // Catch:{ IOException -> 0x0042 }
            r6 = 0
            java.nio.MappedByteBuffer r10 = r4.map(r5, r6, r8)     // Catch:{ IOException -> 0x0042 }
            java.nio.ByteOrder r1 = java.nio.ByteOrder.LITTLE_ENDIAN     // Catch:{ a -> 0x0059, all -> 0x0057 }
            r10.order(r1)     // Catch:{ a -> 0x0059, all -> 0x0057 }
            int r1 = b((java.nio.ByteBuffer) r10)     // Catch:{ a -> 0x0059, all -> 0x0057 }
            long r3 = a((java.nio.ByteBuffer) r10, (int) r1)     // Catch:{ a -> 0x0059, all -> 0x0057 }
            int r1 = (int) r3     // Catch:{ a -> 0x0059, all -> 0x0057 }
            int r3 = c(r10, r1)     // Catch:{ a -> 0x0059, all -> 0x0057 }
            java.nio.ByteBuffer r10 = a((java.nio.ByteBuffer) r10, (int) r3, (int) r1)     // Catch:{ a -> 0x0059, all -> 0x0057 }
            e(r10)     // Catch:{ a -> 0x0059, all -> 0x0057 }
            r2.close()
            r10 = 1
            return r10
        L_0x0042:
            r10 = move-exception
            java.lang.Throwable r1 = r10.getCause()     // Catch:{ a -> 0x0059, all -> 0x0057 }
            boolean r1 = r1 instanceof java.lang.OutOfMemoryError     // Catch:{ a -> 0x0059, all -> 0x0057 }
            if (r1 == 0) goto L_0x004f
            r2.close()
            return r0
        L_0x004f:
            java.io.IOException r1 = new java.io.IOException     // Catch:{ a -> 0x0059, all -> 0x0057 }
            java.lang.String r3 = "Failed to memory-map APK"
            r1.<init>(r3, r10)     // Catch:{ a -> 0x0059, all -> 0x0057 }
            throw r1     // Catch:{ a -> 0x0059, all -> 0x0057 }
        L_0x0057:
            r10 = move-exception
            goto L_0x005d
        L_0x0059:
            r1 = r2
            goto L_0x0063
        L_0x005b:
            r10 = move-exception
            r2 = r1
        L_0x005d:
            if (r2 == 0) goto L_0x0062
            r2.close()
        L_0x0062:
            throw r10
        L_0x0063:
            if (r1 == 0) goto L_0x0068
            r1.close()
        L_0x0068:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.utility.a.a(java.lang.String):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0016  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.security.cert.X509Certificate[][] b(java.lang.String r3) throws com.uc.webview.export.internal.utility.a.C0026a, java.lang.SecurityException, java.io.IOException {
        /*
            r0 = 0
            java.io.RandomAccessFile r1 = new java.io.RandomAccessFile     // Catch:{ all -> 0x0013 }
            java.lang.String r2 = "r"
            r1.<init>(r3, r2)     // Catch:{ all -> 0x0013 }
            java.security.cert.X509Certificate[][] r3 = a((java.io.RandomAccessFile) r1)     // Catch:{ all -> 0x0010 }
            r1.close()
            return r3
        L_0x0010:
            r3 = move-exception
            r0 = r1
            goto L_0x0014
        L_0x0013:
            r3 = move-exception
        L_0x0014:
            if (r0 == 0) goto L_0x0019
            r0.close()
        L_0x0019:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.utility.a.b(java.lang.String):java.security.cert.X509Certificate[][]");
    }

    private static X509Certificate[][] a(RandomAccessFile randomAccessFile) throws C0026a, SecurityException, IOException {
        long length = randomAccessFile.length();
        if (length <= 2147483647L) {
            try {
                MappedByteBuffer map = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, length);
                map.load();
                return a((ByteBuffer) map);
            } catch (IOException e) {
                if (e.getCause() instanceof OutOfMemoryError) {
                    throw new C0026a("Failed to memory-map APK", e);
                }
                throw new IOException("Failed to memory-map APK", e);
            }
        } else {
            throw new IOException("File too large: " + randomAccessFile.length() + " bytes");
        }
    }

    public static X509Certificate[][] a(ByteBuffer byteBuffer) throws C0026a, SecurityException {
        ByteBuffer slice = byteBuffer.slice();
        slice.order(ByteOrder.LITTLE_ENDIAN);
        int b2 = b(slice);
        int a = (int) a(slice, b2);
        int c2 = c(slice, a);
        return a(slice, e(a(slice, c2, a)), c2, a, b2);
    }

    private static X509Certificate[][] a(ByteBuffer byteBuffer, ByteBuffer byteBuffer2, int i, int i2, int i3) throws SecurityException {
        HashMap hashMap = new HashMap();
        ArrayList arrayList = new ArrayList();
        try {
            CertificateFactory instance = CertificateFactory.getInstance("X.509");
            try {
                ByteBuffer c2 = c(byteBuffer2);
                int i4 = 0;
                while (c2.hasRemaining()) {
                    i4++;
                    try {
                        arrayList.add(a(c(c2), (Map<Integer, byte[]>) hashMap, instance));
                    } catch (IOException | SecurityException | BufferUnderflowException e) {
                        throw new SecurityException("Failed to parse/verify signer #" + i4 + " block", e);
                    }
                }
                if (i4 <= 0) {
                    throw new SecurityException("No signers found");
                } else if (!hashMap.isEmpty()) {
                    a((Map<Integer, byte[]>) hashMap, byteBuffer, i, i2, i3);
                    return (X509Certificate[][]) arrayList.toArray(new X509Certificate[arrayList.size()][]);
                } else {
                    throw new SecurityException("No content digests found");
                }
            } catch (IOException e2) {
                throw new SecurityException("Failed to read list of signers", e2);
            }
        } catch (CertificateException e3) {
            throw new RuntimeException("Failed to obtain X.509 CertificateFactory", e3);
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x006f, code lost:
        r12 = 0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.security.cert.X509Certificate[] a(java.nio.ByteBuffer r19, java.util.Map<java.lang.Integer, byte[]> r20, java.security.cert.CertificateFactory r21) throws java.lang.SecurityException, java.io.IOException {
        /*
            java.nio.ByteBuffer r0 = c((java.nio.ByteBuffer) r19)
            java.nio.ByteBuffer r1 = c((java.nio.ByteBuffer) r19)
            byte[] r2 = d(r19)
            java.util.ArrayList r3 = new java.util.ArrayList
            r3.<init>()
            r4 = -1
            r5 = 0
            r6 = 0
            r9 = r6
            r7 = 0
            r8 = -1
        L_0x0017:
            boolean r10 = r1.hasRemaining()
            r11 = 8
            r12 = 1
            if (r10 == 0) goto L_0x00bc
            int r7 = r7 + 1
            java.nio.ByteBuffer r10 = c((java.nio.ByteBuffer) r1)     // Catch:{ IOException | BufferUnderflowException -> 0x00a7 }
            int r13 = r10.remaining()     // Catch:{ IOException | BufferUnderflowException -> 0x00a7 }
            if (r13 < r11) goto L_0x009f
            int r11 = r10.getInt()     // Catch:{ IOException | BufferUnderflowException -> 0x00a7 }
            java.lang.Integer r13 = java.lang.Integer.valueOf(r11)     // Catch:{ IOException | BufferUnderflowException -> 0x00a7 }
            r3.add(r13)     // Catch:{ IOException | BufferUnderflowException -> 0x00a7 }
            switch(r11) {
                case 257: goto L_0x003c;
                case 258: goto L_0x003c;
                case 259: goto L_0x003c;
                case 260: goto L_0x003c;
                case 513: goto L_0x003c;
                case 514: goto L_0x003c;
                case 769: goto L_0x003c;
                case 770: goto L_0x003c;
                default: goto L_0x003a;
            }     // Catch:{ IOException | BufferUnderflowException -> 0x00a7 }
        L_0x003a:
            r13 = 0
            goto L_0x003d
        L_0x003c:
            r13 = 1
        L_0x003d:
            if (r13 != 0) goto L_0x0040
            goto L_0x0017
        L_0x0040:
            if (r8 == r4) goto L_0x0097
            int r13 = a((int) r11)     // Catch:{ IOException | BufferUnderflowException -> 0x00a7 }
            int r14 = a((int) r8)     // Catch:{ IOException | BufferUnderflowException -> 0x00a7 }
            switch(r13) {
                case 1: goto L_0x0067;
                case 2: goto L_0x0050;
                default: goto L_0x004d;
            }     // Catch:{ IOException | BufferUnderflowException -> 0x00a7 }
        L_0x004d:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException     // Catch:{ IOException | BufferUnderflowException -> 0x00a7 }
            goto L_0x0085
        L_0x0050:
            switch(r14) {
                case 1: goto L_0x0070;
                case 2: goto L_0x006f;
                default: goto L_0x0053;
            }     // Catch:{ IOException | BufferUnderflowException -> 0x00a7 }
        L_0x0053:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException     // Catch:{ IOException | BufferUnderflowException -> 0x00a7 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ IOException | BufferUnderflowException -> 0x00a7 }
            java.lang.String r2 = "Unknown digestAlgorithm2: "
            r1.<init>(r2)     // Catch:{ IOException | BufferUnderflowException -> 0x00a7 }
            r1.append(r14)     // Catch:{ IOException | BufferUnderflowException -> 0x00a7 }
            java.lang.String r1 = r1.toString()     // Catch:{ IOException | BufferUnderflowException -> 0x00a7 }
            r0.<init>(r1)     // Catch:{ IOException | BufferUnderflowException -> 0x00a7 }
            throw r0     // Catch:{ IOException | BufferUnderflowException -> 0x00a7 }
        L_0x0067:
            switch(r14) {
                case 1: goto L_0x006f;
                case 2: goto L_0x006d;
                default: goto L_0x006a;
            }     // Catch:{ IOException | BufferUnderflowException -> 0x00a7 }
        L_0x006a:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException     // Catch:{ IOException | BufferUnderflowException -> 0x00a7 }
            goto L_0x0073
        L_0x006d:
            r12 = -1
            goto L_0x0070
        L_0x006f:
            r12 = 0
        L_0x0070:
            if (r12 <= 0) goto L_0x0017
            goto L_0x0097
        L_0x0073:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ IOException | BufferUnderflowException -> 0x00a7 }
            java.lang.String r2 = "Unknown digestAlgorithm2: "
            r1.<init>(r2)     // Catch:{ IOException | BufferUnderflowException -> 0x00a7 }
            r1.append(r14)     // Catch:{ IOException | BufferUnderflowException -> 0x00a7 }
            java.lang.String r1 = r1.toString()     // Catch:{ IOException | BufferUnderflowException -> 0x00a7 }
            r0.<init>(r1)     // Catch:{ IOException | BufferUnderflowException -> 0x00a7 }
            throw r0     // Catch:{ IOException | BufferUnderflowException -> 0x00a7 }
        L_0x0085:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ IOException | BufferUnderflowException -> 0x00a7 }
            java.lang.String r2 = "Unknown digestAlgorithm1: "
            r1.<init>(r2)     // Catch:{ IOException | BufferUnderflowException -> 0x00a7 }
            r1.append(r13)     // Catch:{ IOException | BufferUnderflowException -> 0x00a7 }
            java.lang.String r1 = r1.toString()     // Catch:{ IOException | BufferUnderflowException -> 0x00a7 }
            r0.<init>(r1)     // Catch:{ IOException | BufferUnderflowException -> 0x00a7 }
            throw r0     // Catch:{ IOException | BufferUnderflowException -> 0x00a7 }
        L_0x0097:
            byte[] r8 = d(r10)     // Catch:{ IOException | BufferUnderflowException -> 0x00a7 }
            r9 = r8
            r8 = r11
            goto L_0x0017
        L_0x009f:
            java.lang.SecurityException r0 = new java.lang.SecurityException     // Catch:{ IOException | BufferUnderflowException -> 0x00a7 }
            java.lang.String r1 = "Signature record too short"
            r0.<init>(r1)     // Catch:{ IOException | BufferUnderflowException -> 0x00a7 }
            throw r0     // Catch:{ IOException | BufferUnderflowException -> 0x00a7 }
        L_0x00a7:
            r0 = move-exception
            java.lang.SecurityException r1 = new java.lang.SecurityException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "Failed to parse signature record #"
            r2.<init>(r3)
            r2.append(r7)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2, r0)
            throw r1
        L_0x00bc:
            if (r8 != r4) goto L_0x00d0
            if (r7 != 0) goto L_0x00c8
            java.lang.SecurityException r0 = new java.lang.SecurityException
            java.lang.String r1 = "No signatures found"
            r0.<init>(r1)
            throw r0
        L_0x00c8:
            java.lang.SecurityException r0 = new java.lang.SecurityException
            java.lang.String r1 = "No supported signatures found"
            r0.<init>(r1)
            throw r0
        L_0x00d0:
            switch(r8) {
                case 257: goto L_0x00f4;
                case 258: goto L_0x00f4;
                case 259: goto L_0x00f4;
                case 260: goto L_0x00f4;
                case 513: goto L_0x00f1;
                case 514: goto L_0x00f1;
                case 769: goto L_0x00ee;
                case 770: goto L_0x00ee;
                default: goto L_0x00d3;
            }
        L_0x00d3:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "Unknown signature algorithm: 0x"
            r1.<init>(r2)
            r2 = r8 & -1
            long r2 = (long) r2
            java.lang.String r2 = java.lang.Long.toHexString(r2)
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L_0x00ee:
            java.lang.String r1 = "DSA"
            goto L_0x00f6
        L_0x00f1:
            java.lang.String r1 = "EC"
            goto L_0x00f6
        L_0x00f4:
            java.lang.String r1 = "RSA"
        L_0x00f6:
            switch(r8) {
                case 257: goto L_0x0155;
                case 258: goto L_0x013e;
                case 259: goto L_0x0137;
                case 260: goto L_0x0130;
                case 513: goto L_0x0129;
                case 514: goto L_0x0122;
                case 769: goto L_0x011b;
                case 770: goto L_0x0114;
                default: goto L_0x00f9;
            }
        L_0x00f9:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "Unknown signature algorithm: 0x"
            r1.<init>(r2)
            r2 = r8 & -1
            long r2 = (long) r2
            java.lang.String r2 = java.lang.Long.toHexString(r2)
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L_0x0114:
            java.lang.String r4 = "SHA512withDSA"
            android.util.Pair r4 = android.util.Pair.create(r4, r6)
            goto L_0x016b
        L_0x011b:
            java.lang.String r4 = "SHA256withDSA"
            android.util.Pair r4 = android.util.Pair.create(r4, r6)
            goto L_0x016b
        L_0x0122:
            java.lang.String r4 = "SHA512withECDSA"
            android.util.Pair r4 = android.util.Pair.create(r4, r6)
            goto L_0x016b
        L_0x0129:
            java.lang.String r4 = "SHA256withECDSA"
            android.util.Pair r4 = android.util.Pair.create(r4, r6)
            goto L_0x016b
        L_0x0130:
            java.lang.String r4 = "SHA512withRSA"
            android.util.Pair r4 = android.util.Pair.create(r4, r6)
            goto L_0x016b
        L_0x0137:
            java.lang.String r4 = "SHA256withRSA"
            android.util.Pair r4 = android.util.Pair.create(r4, r6)
            goto L_0x016b
        L_0x013e:
            java.lang.String r4 = "SHA512withRSA/PSS"
            java.security.spec.PSSParameterSpec r7 = new java.security.spec.PSSParameterSpec
            java.lang.String r14 = "SHA-512"
            java.lang.String r15 = "MGF1"
            java.security.spec.MGF1ParameterSpec r16 = java.security.spec.MGF1ParameterSpec.SHA512
            r17 = 64
            r18 = 1
            r13 = r7
            r13.<init>(r14, r15, r16, r17, r18)
            android.util.Pair r4 = android.util.Pair.create(r4, r7)
            goto L_0x016b
        L_0x0155:
            java.lang.String r4 = "SHA256withRSA/PSS"
            java.security.spec.PSSParameterSpec r7 = new java.security.spec.PSSParameterSpec
            java.lang.String r14 = "SHA-256"
            java.lang.String r15 = "MGF1"
            java.security.spec.MGF1ParameterSpec r16 = java.security.spec.MGF1ParameterSpec.SHA256
            r17 = 32
            r18 = 1
            r13 = r7
            r13.<init>(r14, r15, r16, r17, r18)
            android.util.Pair r4 = android.util.Pair.create(r4, r7)
        L_0x016b:
            java.lang.Object r7 = r4.first
            java.lang.String r7 = (java.lang.String) r7
            java.lang.Object r4 = r4.second
            java.security.spec.AlgorithmParameterSpec r4 = (java.security.spec.AlgorithmParameterSpec) r4
            java.security.KeyFactory r1 = java.security.KeyFactory.getInstance(r1)     // Catch:{ InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | SignatureException | InvalidKeySpecException -> 0x02b3 }
            java.security.spec.X509EncodedKeySpec r10 = new java.security.spec.X509EncodedKeySpec     // Catch:{ InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | SignatureException | InvalidKeySpecException -> 0x02b3 }
            r10.<init>(r2)     // Catch:{ InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | SignatureException | InvalidKeySpecException -> 0x02b3 }
            java.security.PublicKey r1 = r1.generatePublic(r10)     // Catch:{ InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | SignatureException | InvalidKeySpecException -> 0x02b3 }
            java.security.Signature r10 = java.security.Signature.getInstance(r7)     // Catch:{ InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | SignatureException | InvalidKeySpecException -> 0x02b3 }
            r10.initVerify(r1)     // Catch:{ InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | SignatureException | InvalidKeySpecException -> 0x02b3 }
            if (r4 == 0) goto L_0x018c
            r10.setParameter(r4)     // Catch:{ InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | SignatureException | InvalidKeySpecException -> 0x02b3 }
        L_0x018c:
            r10.update(r0)     // Catch:{ InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | SignatureException | InvalidKeySpecException -> 0x02b3 }
            boolean r1 = r10.verify(r9)     // Catch:{ InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | SignatureException | InvalidKeySpecException -> 0x02b3 }
            if (r1 == 0) goto L_0x029c
            r0.clear()
            java.nio.ByteBuffer r1 = c((java.nio.ByteBuffer) r0)
            java.util.ArrayList r4 = new java.util.ArrayList
            r4.<init>()
            r7 = r6
            r6 = 0
        L_0x01a3:
            boolean r9 = r1.hasRemaining()
            if (r9 == 0) goto L_0x01e3
            int r6 = r6 + r12
            java.nio.ByteBuffer r9 = c((java.nio.ByteBuffer) r1)     // Catch:{ IOException | BufferUnderflowException -> 0x01ce }
            int r10 = r9.remaining()     // Catch:{ IOException | BufferUnderflowException -> 0x01ce }
            if (r10 < r11) goto L_0x01c6
            int r10 = r9.getInt()     // Catch:{ IOException | BufferUnderflowException -> 0x01ce }
            java.lang.Integer r13 = java.lang.Integer.valueOf(r10)     // Catch:{ IOException | BufferUnderflowException -> 0x01ce }
            r4.add(r13)     // Catch:{ IOException | BufferUnderflowException -> 0x01ce }
            if (r10 != r8) goto L_0x01a3
            byte[] r7 = d(r9)     // Catch:{ IOException | BufferUnderflowException -> 0x01ce }
            goto L_0x01a3
        L_0x01c6:
            java.io.IOException r0 = new java.io.IOException     // Catch:{ IOException | BufferUnderflowException -> 0x01ce }
            java.lang.String r1 = "Record too short"
            r0.<init>(r1)     // Catch:{ IOException | BufferUnderflowException -> 0x01ce }
            throw r0     // Catch:{ IOException | BufferUnderflowException -> 0x01ce }
        L_0x01ce:
            r0 = move-exception
            java.io.IOException r1 = new java.io.IOException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "Failed to parse digest record #"
            r2.<init>(r3)
            r2.append(r6)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2, r0)
            throw r1
        L_0x01e3:
            boolean r1 = r3.equals(r4)
            if (r1 == 0) goto L_0x0294
            int r1 = a((int) r8)
            java.lang.Integer r3 = java.lang.Integer.valueOf(r1)
            r4 = r20
            java.lang.Object r3 = r4.put(r3, r7)
            byte[] r3 = (byte[]) r3
            if (r3 == 0) goto L_0x021d
            boolean r3 = java.security.MessageDigest.isEqual(r3, r7)
            if (r3 == 0) goto L_0x0202
            goto L_0x021d
        L_0x0202:
            java.lang.SecurityException r0 = new java.lang.SecurityException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r1 = b((int) r1)
            r2.append(r1)
            java.lang.String r1 = " contents digest does not match the digest specified by a preceding signer"
            r2.append(r1)
            java.lang.String r1 = r2.toString()
            r0.<init>(r1)
            throw r0
        L_0x021d:
            java.nio.ByteBuffer r0 = c((java.nio.ByteBuffer) r0)
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            r3 = 0
        L_0x0227:
            boolean r4 = r0.hasRemaining()
            if (r4 == 0) goto L_0x025d
            int r3 = r3 + r12
            byte[] r4 = d(r0)
            java.io.ByteArrayInputStream r6 = new java.io.ByteArrayInputStream     // Catch:{ CertificateException -> 0x0248 }
            r6.<init>(r4)     // Catch:{ CertificateException -> 0x0248 }
            r7 = r21
            java.security.cert.Certificate r6 = r7.generateCertificate(r6)     // Catch:{ CertificateException -> 0x0248 }
            java.security.cert.X509Certificate r6 = (java.security.cert.X509Certificate) r6     // Catch:{ CertificateException -> 0x0248 }
            com.uc.webview.export.internal.utility.a$b r8 = new com.uc.webview.export.internal.utility.a$b
            r8.<init>(r6, r4)
            r1.add(r8)
            goto L_0x0227
        L_0x0248:
            r0 = move-exception
            java.lang.SecurityException r1 = new java.lang.SecurityException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r4 = "Failed to decode certificate #"
            r2.<init>(r4)
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2, r0)
            throw r1
        L_0x025d:
            boolean r0 = r1.isEmpty()
            if (r0 != 0) goto L_0x028c
            java.lang.Object r0 = r1.get(r5)
            java.security.cert.X509Certificate r0 = (java.security.cert.X509Certificate) r0
            java.security.PublicKey r0 = r0.getPublicKey()
            byte[] r0 = r0.getEncoded()
            boolean r0 = java.util.Arrays.equals(r2, r0)
            if (r0 == 0) goto L_0x0284
            int r0 = r1.size()
            java.security.cert.X509Certificate[] r0 = new java.security.cert.X509Certificate[r0]
            java.lang.Object[] r0 = r1.toArray(r0)
            java.security.cert.X509Certificate[] r0 = (java.security.cert.X509Certificate[]) r0
            return r0
        L_0x0284:
            java.lang.SecurityException r0 = new java.lang.SecurityException
            java.lang.String r1 = "Public key mismatch between certificate and signature record"
            r0.<init>(r1)
            throw r0
        L_0x028c:
            java.lang.SecurityException r0 = new java.lang.SecurityException
            java.lang.String r1 = "No certificates listed"
            r0.<init>(r1)
            throw r0
        L_0x0294:
            java.lang.SecurityException r0 = new java.lang.SecurityException
            java.lang.String r1 = "Signature algorithms don't match between digests and signatures records"
            r0.<init>(r1)
            throw r0
        L_0x029c:
            java.lang.SecurityException r0 = new java.lang.SecurityException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r7)
            java.lang.String r2 = " signature did not verify"
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L_0x02b3:
            r0 = move-exception
            java.lang.SecurityException r1 = new java.lang.SecurityException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "Failed to verify "
            r2.<init>(r3)
            r2.append(r7)
            java.lang.String r3 = " signature"
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2, r0)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.utility.a.a(java.nio.ByteBuffer, java.util.Map, java.security.cert.CertificateFactory):java.security.cert.X509Certificate[]");
    }

    private static void a(Map<Integer, byte[]> map, ByteBuffer byteBuffer, int i, int i2, int i3) throws SecurityException {
        if (!map.isEmpty()) {
            ByteBuffer a = a(byteBuffer, 0, i);
            ByteBuffer a2 = a(byteBuffer, i2, i3);
            byte[] bArr = new byte[(byteBuffer.capacity() - i3)];
            byteBuffer.position(i3);
            byteBuffer.get(bArr);
            ByteBuffer wrap = ByteBuffer.wrap(bArr);
            wrap.order(byteBuffer.order());
            long j = (long) i;
            t.a(wrap);
            int position = wrap.position() + 16;
            if (j < 0 || j > 4294967295L) {
                throw new IllegalArgumentException("uint32 value of out range: " + j);
            }
            wrap.putInt(wrap.position() + position, (int) j);
            int[] iArr = new int[map.size()];
            int i4 = 0;
            for (Integer intValue : map.keySet()) {
                iArr[i4] = intValue.intValue();
                i4++;
            }
            try {
                Map<Integer, byte[]> a3 = a(iArr, new ByteBuffer[]{a, a2, wrap});
                for (Map.Entry next : map.entrySet()) {
                    int intValue2 = ((Integer) next.getKey()).intValue();
                    if (!MessageDigest.isEqual((byte[]) next.getValue(), a3.get(Integer.valueOf(intValue2)))) {
                        throw new SecurityException(b(intValue2) + " digest of contents did not verify");
                    }
                }
            } catch (DigestException e) {
                throw new SecurityException("Failed to compute digest(s) of contents", e);
            }
        } else {
            throw new SecurityException("No digests provided");
        }
    }

    private static Map<Integer, byte[]> a(int[] iArr, ByteBuffer[] byteBufferArr) throws DigestException {
        int i;
        int[] iArr2 = iArr;
        int i2 = 0;
        int i3 = 0;
        while (true) {
            if (i2 >= 3) {
                break;
            }
            i3 += ((byteBufferArr[i2].remaining() + 1048576) - 1) / 1048576;
            i2++;
        }
        HashMap hashMap = new HashMap(i3);
        for (int i4 : iArr2) {
            byte[] bArr = new byte[((c(i4) * i3) + 5)];
            bArr[0] = 90;
            a(i3, bArr);
            hashMap.put(Integer.valueOf(i4), bArr);
        }
        byte[] bArr2 = new byte[5];
        bArr2[0] = -91;
        int i5 = 0;
        int i6 = 0;
        for (i = 3; i5 < i; i = 3) {
            ByteBuffer byteBuffer = byteBufferArr[i5];
            while (byteBuffer.hasRemaining()) {
                ByteBuffer b2 = b(byteBuffer, Math.min(byteBuffer.remaining(), 1048576));
                int length = iArr2.length;
                int i7 = 0;
                while (i7 < length) {
                    int i8 = iArr2[i7];
                    String b3 = b(i8);
                    try {
                        MessageDigest instance = MessageDigest.getInstance(b3);
                        b2.clear();
                        a(b2.remaining(), bArr2);
                        instance.update(bArr2);
                        instance.update(b2);
                        int c2 = c(i8);
                        int digest = instance.digest((byte[]) hashMap.get(Integer.valueOf(i8)), (i6 * c2) + 5, c2);
                        if (digest == c2) {
                            i7++;
                        } else {
                            throw new RuntimeException("Unexpected output size of " + instance.getAlgorithm() + " digest: " + digest);
                        }
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(b3 + " digest not supported", e);
                    }
                }
                i6++;
            }
            i5++;
        }
        HashMap hashMap2 = new HashMap(iArr2.length);
        for (Map.Entry entry : hashMap.entrySet()) {
            int intValue = ((Integer) entry.getKey()).intValue();
            byte[] bArr3 = (byte[]) entry.getValue();
            String b4 = b(intValue);
            try {
                hashMap2.put(Integer.valueOf(intValue), MessageDigest.getInstance(b4).digest(bArr3));
            } catch (NoSuchAlgorithmException e2) {
                throw new RuntimeException(b4 + " digest not supported", e2);
            }
        }
        return hashMap2;
    }

    private static int a(int i) {
        switch (i) {
            case 257:
            case WebConstant.WEBVIEW_CAPTCHA_RELOGIN:
            case InputDeviceCompat.SOURCE_DPAD:
            case 769:
                return 1;
            case WebConstant.OPEN_WEB_RESCODE:
            case WebConstant.QR_REGISTER_REQCODE:
            case 514:
            case 770:
                return 2;
            default:
                throw new IllegalArgumentException("Unknown signature algorithm: 0x" + Long.toHexString((long) (i & -1)));
        }
    }

    private static String b(int i) {
        switch (i) {
            case 1:
                return MessageDigestAlgorithms.SHA_256;
            case 2:
                return MessageDigestAlgorithms.SHA_512;
            default:
                throw new IllegalArgumentException("Unknown content digest algorthm: " + i);
        }
    }

    private static int c(int i) {
        switch (i) {
            case 1:
                return 32;
            case 2:
                return 64;
            default:
                throw new IllegalArgumentException("Unknown content digest algorthm: " + i);
        }
    }

    /* JADX INFO: finally extract failed */
    private static ByteBuffer a(ByteBuffer byteBuffer, int i, int i2) {
        if (i < 0) {
            throw new IllegalArgumentException("start: " + i);
        } else if (i2 >= i) {
            int capacity = byteBuffer.capacity();
            if (i2 <= byteBuffer.capacity()) {
                int limit = byteBuffer.limit();
                int position = byteBuffer.position();
                try {
                    byteBuffer.position(0);
                    byteBuffer.limit(i2);
                    byteBuffer.position(i);
                    ByteBuffer slice = byteBuffer.slice();
                    slice.order(byteBuffer.order());
                    byteBuffer.position(0);
                    byteBuffer.limit(limit);
                    byteBuffer.position(position);
                    return slice;
                } catch (Throwable th) {
                    byteBuffer.position(0);
                    byteBuffer.limit(limit);
                    byteBuffer.position(position);
                    throw th;
                }
            } else {
                throw new IllegalArgumentException("end > capacity: " + i2 + " > " + capacity);
            }
        } else {
            throw new IllegalArgumentException("end < start: " + i2 + " < " + i);
        }
    }

    private static ByteBuffer b(ByteBuffer byteBuffer, int i) throws BufferUnderflowException {
        if (i >= 0) {
            int limit = byteBuffer.limit();
            int position = byteBuffer.position();
            int i2 = i + position;
            if (i2 < position || i2 > limit) {
                throw new BufferUnderflowException();
            }
            byteBuffer.limit(i2);
            try {
                ByteBuffer slice = byteBuffer.slice();
                slice.order(byteBuffer.order());
                byteBuffer.position(i2);
                return slice;
            } finally {
                byteBuffer.limit(limit);
            }
        } else {
            throw new IllegalArgumentException("size: " + i);
        }
    }

    private static ByteBuffer c(ByteBuffer byteBuffer) throws IOException {
        if (byteBuffer.remaining() >= 4) {
            int i = byteBuffer.getInt();
            if (i < 0) {
                throw new IllegalArgumentException("Negative length");
            } else if (i <= byteBuffer.remaining()) {
                return b(byteBuffer, i);
            } else {
                throw new IOException("Length-prefixed field longer than remaining buffer. Field length: " + i + ", remaining: " + byteBuffer.remaining());
            }
        } else {
            throw new IOException("Remaining buffer too short to contain length of length-prefixed field. Remaining: " + byteBuffer.remaining());
        }
    }

    private static byte[] d(ByteBuffer byteBuffer) throws IOException {
        int i = byteBuffer.getInt();
        if (i < 0) {
            throw new IOException("Negative length");
        } else if (i <= byteBuffer.remaining()) {
            byte[] bArr = new byte[i];
            byteBuffer.get(bArr);
            return bArr;
        } else {
            throw new IOException("Underflow while reading length-prefixed value. Length: " + i + ", available: " + byteBuffer.remaining());
        }
    }

    private static void a(int i, byte[] bArr) {
        bArr[1] = (byte) (i & 255);
        bArr[2] = (byte) ((i >>> 8) & 255);
        bArr[3] = (byte) ((i >>> 16) & 255);
        bArr[4] = (byte) ((i >>> 24) & 255);
    }

    private static int c(ByteBuffer byteBuffer, int i) throws C0026a {
        f(byteBuffer);
        if (i < 32) {
            throw new C0026a("APK too small for APK Signing Block. ZIP Central Directory offset: " + i);
        } else if (byteBuffer.getLong(i - 16) == 2334950737559900225L && byteBuffer.getLong(i - 8) == 3617552046287187010L) {
            long j = byteBuffer.getLong(i - 24);
            if (j < 24 || j > 2147483639) {
                throw new C0026a("APK Signing Block size out of range: " + j);
            }
            int i2 = (int) j;
            int i3 = i - (i2 + 8);
            if (i3 >= 0) {
                long j2 = byteBuffer.getLong(i3);
                if (j2 == ((long) i2)) {
                    return i3;
                }
                throw new C0026a("APK Signing Block sizes in header and footer do not match: " + j2 + " vs " + i2);
            }
            throw new C0026a("APK Signing Block offset out of range: " + i3);
        } else {
            throw new C0026a("No APK Signing Block before ZIP Central Directory");
        }
    }

    private static ByteBuffer e(ByteBuffer byteBuffer) throws C0026a {
        f(byteBuffer);
        ByteBuffer a = a(byteBuffer, 8, byteBuffer.capacity() - 24);
        int i = 0;
        while (a.hasRemaining()) {
            i++;
            if (a.remaining() >= 8) {
                long j = a.getLong();
                if (j < 4 || j > 2147483647L) {
                    throw new C0026a("APK Signing Block entry #" + i + " size out of range: " + j);
                }
                int i2 = (int) j;
                int position = a.position() + i2;
                if (i2 > a.remaining()) {
                    throw new C0026a("APK Signing Block entry #" + i + " size out of range: " + i2 + ", available: " + a.remaining());
                } else if (a.getInt() == 1896449818) {
                    return b(a, i2 - 4);
                } else {
                    a.position(position);
                }
            } else {
                throw new C0026a("Insufficient data to read size of APK Signing Block entry #" + i);
            }
        }
        throw new C0026a("No APK Signature Scheme v2 block in APK Signing Block");
    }

    private static void f(ByteBuffer byteBuffer) {
        if (byteBuffer.order() != ByteOrder.LITTLE_ENDIAN) {
            throw new IllegalArgumentException("ByteBuffer byte order must be little endian");
        }
    }

    /* renamed from: com.uc.webview.export.internal.utility.a$a  reason: collision with other inner class name */
    /* compiled from: U4Source */
    public static class C0026a extends Exception {
        public C0026a(String str) {
            super(str);
        }

        public C0026a(String str, Throwable th) {
            super(str, th);
        }
    }

    /* compiled from: U4Source */
    static class b extends c {
        private byte[] a;

        public b(X509Certificate x509Certificate, byte[] bArr) {
            super(x509Certificate);
            this.a = bArr;
        }

        public final byte[] getEncoded() throws CertificateEncodingException {
            return this.a;
        }
    }

    /* compiled from: U4Source */
    static class c extends X509Certificate {
        private final X509Certificate a;

        public c(X509Certificate x509Certificate) {
            this.a = x509Certificate;
        }

        public Set<String> getCriticalExtensionOIDs() {
            return this.a.getCriticalExtensionOIDs();
        }

        public byte[] getExtensionValue(String str) {
            return this.a.getExtensionValue(str);
        }

        public Set<String> getNonCriticalExtensionOIDs() {
            return this.a.getNonCriticalExtensionOIDs();
        }

        public boolean hasUnsupportedCriticalExtension() {
            return this.a.hasUnsupportedCriticalExtension();
        }

        public void checkValidity() throws CertificateExpiredException, CertificateNotYetValidException {
            this.a.checkValidity();
        }

        public void checkValidity(Date date) throws CertificateExpiredException, CertificateNotYetValidException {
            this.a.checkValidity(date);
        }

        public int getVersion() {
            return this.a.getVersion();
        }

        public BigInteger getSerialNumber() {
            return this.a.getSerialNumber();
        }

        public Principal getIssuerDN() {
            return this.a.getIssuerDN();
        }

        public Principal getSubjectDN() {
            return this.a.getSubjectDN();
        }

        public Date getNotBefore() {
            return this.a.getNotBefore();
        }

        public Date getNotAfter() {
            return this.a.getNotAfter();
        }

        public byte[] getTBSCertificate() throws CertificateEncodingException {
            return this.a.getTBSCertificate();
        }

        public byte[] getSignature() {
            return this.a.getSignature();
        }

        public String getSigAlgName() {
            return this.a.getSigAlgName();
        }

        public String getSigAlgOID() {
            return this.a.getSigAlgOID();
        }

        public byte[] getSigAlgParams() {
            return this.a.getSigAlgParams();
        }

        public boolean[] getIssuerUniqueID() {
            return this.a.getIssuerUniqueID();
        }

        public boolean[] getSubjectUniqueID() {
            return this.a.getSubjectUniqueID();
        }

        public boolean[] getKeyUsage() {
            return this.a.getKeyUsage();
        }

        public int getBasicConstraints() {
            return this.a.getBasicConstraints();
        }

        public byte[] getEncoded() throws CertificateEncodingException {
            return this.a.getEncoded();
        }

        public void verify(PublicKey publicKey) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
            this.a.verify(publicKey);
        }

        public void verify(PublicKey publicKey, String str) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
            this.a.verify(publicKey, str);
        }

        public String toString() {
            return this.a.toString();
        }

        public PublicKey getPublicKey() {
            return this.a.getPublicKey();
        }
    }

    private static int b(ByteBuffer byteBuffer) throws C0026a {
        int i;
        t.a(byteBuffer);
        int capacity = byteBuffer.capacity();
        if (capacity >= 22) {
            int i2 = capacity - 22;
            int min = Math.min(i2, 65535);
            int i3 = 0;
            while (true) {
                if (i3 >= min) {
                    break;
                }
                i = i2 - i3;
                if (byteBuffer.getInt(i) == 101010256 && (byteBuffer.getShort(i + 20) & UShort.MAX_VALUE) == i3) {
                    break;
                }
                i3++;
            }
        } else {
            System.out.println("File size smaller than EOCD min size");
        }
        i = -1;
        if (i != -1) {
            return i;
        }
        throw new C0026a("Not an APK file: ZIP End of Central Directory record not found");
    }

    private static long a(ByteBuffer byteBuffer, int i) throws C0026a {
        t.a(byteBuffer);
        int i2 = i - 20;
        if (!(i2 >= 0 && byteBuffer.getInt(i2) == 117853008)) {
            ByteBuffer a = a(byteBuffer, i, byteBuffer.capacity());
            t.a(a);
            long a2 = t.a(a, a.position() + 16);
            long j = (long) i;
            if (a2 < j) {
                t.a(a);
                if (t.a(a, a.position() + 12) + a2 == j) {
                    return a2;
                }
                throw new C0026a("ZIP Central Directory is not immediately followed by End of Central Directory");
            }
            throw new C0026a("ZIP Central Directory offset out of range: " + a2 + ". ZIP End of Central Directory offset: " + i);
        }
        throw new C0026a("ZIP64 APK not supported");
    }
}
