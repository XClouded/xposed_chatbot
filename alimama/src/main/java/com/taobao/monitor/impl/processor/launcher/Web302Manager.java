package com.taobao.monitor.impl.processor.launcher;

import java.util.HashSet;
import java.util.Set;

public class Web302Manager {
    private Set<String> web302Urls;

    private Web302Manager() {
        this.web302Urls = new HashSet();
        this.web302Urls.add("s.click.taobao.com");
    }

    public static Web302Manager instance() {
        return Holder.INSTANCE;
    }

    public void add302Url(String str) {
        this.web302Urls.add(str);
    }

    public boolean contains(String str) {
        if (str == null) {
            return false;
        }
        for (String contains : this.web302Urls) {
            if (str.contains(contains)) {
                return true;
            }
        }
        return false;
    }

    private static final class Holder {
        /* access modifiers changed from: private */
        public static final Web302Manager INSTANCE = new Web302Manager();

        private Holder() {
        }
    }
}
