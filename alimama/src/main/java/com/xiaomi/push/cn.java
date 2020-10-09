package com.xiaomi.push;

import java.util.Comparator;
import org.apache.http.NameValuePair;

final class cn implements Comparator<NameValuePair> {
    cn() {
    }

    /* renamed from: a */
    public int compare(NameValuePair nameValuePair, NameValuePair nameValuePair2) {
        return nameValuePair.getName().compareTo(nameValuePair2.getName());
    }
}
