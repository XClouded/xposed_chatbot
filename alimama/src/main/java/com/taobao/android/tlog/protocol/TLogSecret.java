package com.taobao.android.tlog.protocol;

import android.util.Log;
import com.taobao.android.tlog.protocol.utils.Base64;
import com.taobao.android.tlog.protocol.utils.MD5Utils;
import com.taobao.android.tlog.protocol.utils.RSAUtils;

public class TLogSecret {
    public static final Integer encryptionType = 0;
    private final String DEFAULT_RSAPUBLICKEY;
    private String TAG;
    private String mRc4EncryptSecret;
    private String mRsaPublicMd5;
    private String rsaPublishKey;

    private TLogSecret() {
        this.TAG = "TLogProtocol";
        this.DEFAULT_RSAPUBLICKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC1cueeBrz4VMOr09PxnO7ILYh6jDhcZwEUgzjPfwy2apBZIHAfGagR8LzN35O0UhvoeN570mJP4g0nLm2KL1H/K1NGYqT254w0sdV51kzO/yu+WcfgPkPLosnR1kVaPqiYKT2Bl1Yi85ZJwApO2y8lPmFwpIrMmKiTYqR2Gmd9nQIDAQAB";
        this.rsaPublishKey = null;
    }

    private static class CreateInstance {
        /* access modifiers changed from: private */
        public static TLogSecret instance = new TLogSecret();

        private CreateInstance() {
        }
    }

    public static synchronized TLogSecret getInstance() {
        TLogSecret access$100;
        synchronized (TLogSecret.class) {
            access$100 = CreateInstance.instance;
        }
        return access$100;
    }

    public String getRsaMd5Value() {
        if (this.mRsaPublicMd5 == null) {
            this.mRsaPublicMd5 = MD5Utils.encrypt(getRsaPublishKey().getBytes());
        }
        return this.mRsaPublicMd5;
    }

    public void setRsapublickey(String str) {
        if (str != null) {
            this.rsaPublishKey = str;
        }
    }

    public String getRsaPublishKey() {
        if (this.rsaPublishKey == null) {
            return "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC1cueeBrz4VMOr09PxnO7ILYh6jDhcZwEUgzjPfwy2apBZIHAfGagR8LzN35O0UhvoeN570mJP4g0nLm2KL1H/K1NGYqT254w0sdV51kzO/yu+WcfgPkPLosnR1kVaPqiYKT2Bl1Yi85ZJwApO2y8lPmFwpIrMmKiTYqR2Gmd9nQIDAQAB";
        }
        return this.rsaPublishKey;
    }

    public String getRc4EncryptSecretValue(String str) throws Exception {
        if (this.mRc4EncryptSecret == null) {
            this.mRc4EncryptSecret = Base64.encodeBase64String(RSAUtils.encryptByPublicKey(str.getBytes(), getRsaPublishKey()));
        }
        if (this.mRc4EncryptSecret != null) {
            return this.mRc4EncryptSecret;
        }
        Log.e(this.TAG, " rc4 Encrypt secret obtain failure ");
        return null;
    }

    public static void main(String[] strArr) {
        try {
            new String(RSAUtils.decryptByPrivateKey(Base64.decode(getInstance().getRc4EncryptSecretValue("123456776654")), "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALVy554GvPhUw6vT0/Gc7sgtiHqMOFxnARSDOM9/DLZqkFkgcB8ZqBHwvM3fk7RSG+h43nvSYk/iDScubYovUf8rU0ZipPbnjDSx1XnWTM7/K75Zx+A+Q8uiydHWRVo+qJgpPYGXViLzlknACk7bLyU+YXCkisyYqJNipHYaZ32dAgMBAAECgYBYcFwSOwiKJY6FxqaMIkiESyU1Tfj+mLn/DIJ5KFzC4KfguR3NGs0/iU4NLkco4ch2g8s1IPMIKo7spQWBD9VvrmrW4PBqjG2CoP5iVWYOnz5xDPllmUmMRzLs6voBn5vKgn/qZVHg5ElPh4Q57z2vzDNLzVMpmeFIBrpKz8iDhQJBAOUGRNUB3+O3JKO4/vfuDIIxPh/8xZNAR76Yj/QeL5ojO0gzPXrR5fAvvfRUMhHA4jV5iXqBwbu/A9isHXTZIEcCQQDK0hjFvBEFg8ocSdSCkk6wAUEqhci7i8cDUXc+RQn6xGsN0gnq+FjzIUWsFsj4ROhrFHp2K9U/QaeEgHbkWGj7AkBfkscksNyStbnXjPrx0ehsaEpJpP16XqfR9O6V7AbnZu51SdTNLUysd+/oRz6BxCFiOW7SrdWAGM1tHR5JxdY/AkAKJDlC4eWD/hQEGBj9Mm2m1Vk51Bi2cAXSf6dTwMX/+QRVW5RNYH+qIJbIRRdleqSYfhylfgmasSC8OmQ3hMgzAkAZTTBNMdfWkJAn063xGMGQYbuRoWZC8qz+au6DuFL5iSJ+OXcB8tc/Woi4JIVTKZLsHm0uclhE+ch3OPDVjzjB"));
        } catch (Exception unused) {
        }
    }
}
