package com.nostra13.universalimageloader.core.download;

import anet.channel.util.HttpConstant;
import com.uc.webview.export.internal.setup.br;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public interface ImageDownloader {
    InputStream getStream(String str, Object obj) throws IOException;

    public enum Scheme {
        HTTP("http"),
        HTTPS("https"),
        FILE("file"),
        CONTENT("content"),
        ASSETS(br.ASSETS_DIR),
        DRAWABLE("drawable"),
        UNKNOWN("");
        
        private String scheme;
        private String uriPrefix;

        private Scheme(String str) {
            this.scheme = str;
            this.uriPrefix = str + HttpConstant.SCHEME_SPLIT;
        }

        public static Scheme ofUri(String str) {
            if (str != null) {
                for (Scheme scheme2 : values()) {
                    if (scheme2.belongsTo(str)) {
                        return scheme2;
                    }
                }
            }
            return UNKNOWN;
        }

        private boolean belongsTo(String str) {
            return str.toLowerCase(Locale.US).startsWith(this.uriPrefix);
        }

        public String wrap(String str) {
            return this.uriPrefix + str;
        }

        public String crop(String str) {
            if (belongsTo(str)) {
                return str.substring(this.uriPrefix.length());
            }
            throw new IllegalArgumentException(String.format("URI [%1$s] doesn't have expected scheme [%2$s]", new Object[]{str, this.scheme}));
        }
    }
}
