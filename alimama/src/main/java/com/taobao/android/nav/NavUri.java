package com.taobao.android.nav;

import android.net.Uri;

public class NavUri {
    protected Uri.Builder mBuilder = new Uri.Builder();

    public interface Schemed {
        NavUri host(String str);
    }

    public static Schemed scheme(String str) {
        NavUri navUri = new NavUri();
        navUri.mBuilder.scheme(str);
        return new Schemed(navUri) {
            final /* synthetic */ NavUri val$nav_uri;

            {
                this.val$nav_uri = r1;
            }

            public NavUri host(String str) {
                this.val$nav_uri.mBuilder.authority(str);
                return this.val$nav_uri;
            }
        };
    }

    public static NavUri host(String str) {
        NavUri navUri = new NavUri();
        navUri.mBuilder.scheme("http").authority(str);
        return navUri;
    }

    public NavUri path(String str) {
        this.mBuilder.path(str);
        return this;
    }

    public NavUri segment(String str) {
        this.mBuilder.appendEncodedPath(str);
        return this;
    }

    public NavUri segment(long j) {
        this.mBuilder.appendEncodedPath(String.valueOf(j));
        return this;
    }

    public NavUri segment(int i) {
        this.mBuilder.appendEncodedPath(String.valueOf(i));
        return this;
    }

    public NavUri param(String str, String str2) {
        this.mBuilder.appendQueryParameter(str, str2);
        return this;
    }

    public NavUri param(String str, long j) {
        this.mBuilder.appendQueryParameter(str, String.valueOf(j));
        return this;
    }

    public NavUri param(String str, int i) {
        this.mBuilder.appendQueryParameter(str, String.valueOf(i));
        return this;
    }

    public NavUri fragment(String str) {
        this.mBuilder.fragment(str);
        return this;
    }

    /* access modifiers changed from: package-private */
    public Uri build() {
        return this.mBuilder.build();
    }

    private NavUri() {
    }
}
