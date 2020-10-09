package anet.channel.security;

import android.content.Context;
import android.text.TextUtils;
import anet.channel.util.HMacUtil;

class NoSecurityGuardImpl implements ISecurity {
    private String appSecret = null;

    public byte[] decrypt(Context context, String str, String str2, byte[] bArr) {
        return null;
    }

    public byte[] getBytes(Context context, String str) {
        return null;
    }

    public boolean isSecOff() {
        return true;
    }

    public boolean saveBytes(Context context, String str, byte[] bArr) {
        return false;
    }

    NoSecurityGuardImpl(String str) {
        this.appSecret = str;
    }

    public String sign(Context context, String str, String str2, String str3) {
        if (!TextUtils.isEmpty(this.appSecret) && ISecurity.SIGN_ALGORITHM_HMAC_SHA1.equalsIgnoreCase(str)) {
            return HMacUtil.hmacSha1Hex(this.appSecret.getBytes(), str3.getBytes());
        }
        return null;
    }
}
