package anet.channel.util;

import android.util.Base64;
import java.net.InetSocketAddress;
import java.net.Proxy;

public class ProxySetting {
    public static ProxySetting proxySetting;
    private final String authAccount;
    private final String authPassword;
    private final Proxy proxy;

    public static void set(ProxySetting proxySetting2) {
        proxySetting = proxySetting2;
    }

    public static ProxySetting get() {
        return proxySetting;
    }

    public Proxy getProxy() {
        return this.proxy;
    }

    public ProxySetting(String str, int i, String str2, String str3) {
        this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(str, i));
        this.authAccount = str2;
        this.authPassword = str3;
    }

    public String getBasicAuthorization() {
        StringBuilder sb = new StringBuilder(32);
        sb.append(this.authAccount);
        sb.append(":");
        sb.append(this.authPassword);
        String encodeToString = Base64.encodeToString(sb.toString().getBytes(), 0);
        StringBuilder sb2 = new StringBuilder(64);
        sb2.append("Basic ");
        sb2.append(encodeToString);
        return sb2.toString();
    }
}
