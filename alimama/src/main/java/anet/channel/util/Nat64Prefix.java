package anet.channel.util;

import java.net.Inet6Address;

public class Nat64Prefix {
    public Inet6Address mPrefix;
    public int mPrefixLength;

    public Nat64Prefix(Inet6Address inet6Address, int i) {
        this.mPrefixLength = i;
        this.mPrefix = inet6Address;
    }

    public String toString() {
        return this.mPrefix.getHostAddress() + "/" + this.mPrefixLength;
    }
}
