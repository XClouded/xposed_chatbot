package com.huawei.updatesdk.fileprovider;

import android.net.Uri;
import android.text.TextUtils;
import com.taobao.android.dinamicx.template.utils.DXTemplateNamePathUtil;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class b implements a {
    private final String a;
    private final HashMap<String, File> b = new HashMap<>();

    protected b(String str) {
        this.a = str;
    }

    public Uri a(File file) {
        try {
            String canonicalPath = file.getCanonicalPath();
            Map.Entry entry = null;
            for (Map.Entry next : this.b.entrySet()) {
                String path = ((File) next.getValue()).getPath();
                if (canonicalPath.startsWith(path) && (entry == null || path.length() > ((File) entry.getValue()).getPath().length())) {
                    entry = next;
                }
            }
            if (entry != null) {
                String path2 = ((File) entry.getValue()).getPath();
                String substring = canonicalPath.substring(path2.endsWith("/") ? path2.length() : path2.length() + 1);
                return new Uri.Builder().scheme("content").authority(this.a).encodedPath(Uri.encode((String) entry.getKey()) + DXTemplateNamePathUtil.DIR + Uri.encode(substring, "/")).build();
            }
            throw new IllegalArgumentException("wisedist: Failed to find configured root that contains");
        } catch (IOException unused) {
            throw new IllegalArgumentException("Failed to resolve canonical path for wisedist");
        }
    }

    public File a(Uri uri) {
        String encodedPath = uri.getEncodedPath();
        int indexOf = encodedPath.indexOf(47, 1);
        String decode = Uri.decode(encodedPath.substring(1, indexOf));
        String decode2 = Uri.decode(encodedPath.substring(indexOf + 1));
        File file = this.b.get(decode);
        if (file != null) {
            try {
                File canonicalFile = new File(file, decode2).getCanonicalFile();
                if (canonicalFile.getPath().startsWith(file.getPath())) {
                    return canonicalFile;
                }
                throw new SecurityException("wisedist: Resolved path jumped beyond configured root");
            } catch (IOException unused) {
                throw new IllegalArgumentException("wisedist: Failed to resolve canonical path for");
            }
        } else {
            throw new IllegalArgumentException("wisedist: Unable to find configured root for");
        }
    }

    public void a(String str, File file) {
        if (!TextUtils.isEmpty(str)) {
            try {
                this.b.put(str, file.getCanonicalFile());
            } catch (IOException unused) {
                throw new IllegalArgumentException("Failed to resolve canonical path for root");
            }
        } else {
            throw new IllegalArgumentException("wisedist Name must not be empty");
        }
    }
}
