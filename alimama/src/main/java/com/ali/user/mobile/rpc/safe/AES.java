package com.ali.user.mobile.rpc.safe;

import android.annotation.TargetApi;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import com.taobao.login4android.constants.LoginStatus;
import java.security.Key;
import java.security.KeyStore;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

public class AES {
    public static final String ALGORITHM = "AES";
    public static final String ANDROID_KEYSTORE = "AndroidKeyStore";
    public static final String BLOCK_MODE = "CBC";
    public static final String MY_KEY = "com.ali.user.sdk.fingerprint";
    public static final String PADDING = "PKCS7Padding";
    final KeyStore mKeyStore = KeyStore.getInstance(ANDROID_KEYSTORE);

    public AES() throws Exception {
        this.mKeyStore.load((KeyStore.LoadStoreParameter) null);
    }

    @TargetApi(23)
    public Cipher getCipher(boolean z) throws Exception {
        Key key = getKey();
        Cipher instance = Cipher.getInstance("AES/CBC/PKCS7Padding");
        try {
            instance.init(3, key);
            LoginStatus.resetFingerPrintEntrolled();
        } catch (KeyPermanentlyInvalidatedException e) {
            this.mKeyStore.deleteEntry(MY_KEY);
            if (z) {
                getCipher(false);
                LoginStatus.compareAndSetNewFingerPrintEntrolled(false, true);
            } else {
                throw new Exception("create cipher failed", e);
            }
        }
        return instance;
    }

    /* access modifiers changed from: package-private */
    public Key getKey() throws Exception {
        if (!this.mKeyStore.isKeyEntry(MY_KEY)) {
            createKey();
        }
        return this.mKeyStore.getKey(MY_KEY, (char[]) null);
    }

    /* access modifiers changed from: package-private */
    @TargetApi(23)
    public void createKey() throws Exception {
        KeyGenerator instance = KeyGenerator.getInstance("AES", ANDROID_KEYSTORE);
        instance.init(new KeyGenParameterSpec.Builder(MY_KEY, 3).setBlockModes(new String[]{BLOCK_MODE}).setEncryptionPaddings(new String[]{PADDING}).setUserAuthenticationRequired(true).build());
        instance.generateKey();
    }
}
