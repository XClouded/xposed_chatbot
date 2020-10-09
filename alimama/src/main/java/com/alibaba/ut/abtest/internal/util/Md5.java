package com.alibaba.ut.abtest.internal.util;

import android.support.v4.media.session.PlaybackStateCompat;
import com.alibaba.taffy.core.util.codec.MessageDigestAlgorithms;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Md5 {
    private static char[] hexChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private Md5() {
    }

    static MessageDigest getDigest() {
        try {
            return MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] md5(byte[] bArr) {
        return getDigest().digest(bArr);
    }

    public static byte[] md5(ByteBuffer byteBuffer) {
        MessageDigest digest = getDigest();
        digest.update(byteBuffer);
        return digest.digest();
    }

    public static byte[] md5(String str) {
        return md5(str.getBytes());
    }

    public static String md5Hex(byte[] bArr) {
        return bytesToHexString(md5(bArr));
    }

    public static String md5Hex(ByteBuffer byteBuffer) {
        return bytesToHexString(md5(byteBuffer));
    }

    public static String md5Hex(String str) {
        return bytesToHexString(md5(str));
    }

    private static String bytesToHexString(byte[] bArr) {
        return bytesToHexString(bArr, (Character) null);
    }

    private static String bytesToHexString(byte[] bArr, Character ch) {
        StringBuffer stringBuffer = new StringBuffer(bArr.length * (ch == null ? 2 : 3));
        for (int i = 0; i < bArr.length; i++) {
            int i2 = (bArr[i] >>> 4) & 15;
            byte b = bArr[i] & 15;
            if (i > 0 && ch != null) {
                stringBuffer.append(ch.charValue());
            }
            stringBuffer.append(hexChars[i2]);
            stringBuffer.append(hexChars[b]);
        }
        return stringBuffer.toString();
    }

    public static String md5Hex(File file) throws IOException {
        if (file.exists()) {
            FileInputStream fileInputStream = null;
            try {
                FileInputStream fileInputStream2 = new FileInputStream(file);
                try {
                    String md5Hex = md5Hex((InputStream) fileInputStream2);
                    try {
                        fileInputStream2.close();
                    } catch (Exception unused) {
                    }
                    return md5Hex;
                } catch (IOException e) {
                    e = e;
                    fileInputStream = fileInputStream2;
                    try {
                        throw e;
                    } catch (Throwable th) {
                        th = th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    fileInputStream = fileInputStream2;
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (Exception unused2) {
                        }
                    }
                    throw th;
                }
            } catch (IOException e2) {
                e = e2;
                throw e;
            }
        } else {
            throw new FileNotFoundException(file.toString());
        }
    }

    public static String md5Hex(InputStream inputStream) throws IOException {
        long available = (long) inputStream.available();
        if (available < 512) {
            available = 512;
        }
        if (available > PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH) {
            available = 65536;
        }
        byte[] bArr = new byte[((int) available)];
        MessageDigest digest = getDigest();
        while (true) {
            int read = inputStream.read(bArr);
            if (read == -1) {
                return bytesToHexString(digest.digest());
            }
            digest.update(bArr, 0, read);
        }
    }
}
