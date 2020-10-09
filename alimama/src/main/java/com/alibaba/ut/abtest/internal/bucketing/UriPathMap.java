package com.alibaba.ut.abtest.internal.bucketing;

import android.net.Uri;
import android.text.TextUtils;
import com.alibaba.ut.abtest.internal.ABConstants;
import com.alibaba.ut.abtest.internal.bucketing.model.ExperimentGroup;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class UriPathMap {
    private final String PATH_EMPTY_PLACEHOLDER = "<PATH-EMPTY>";
    private List<ExperimentGroup> anyGroups = new ArrayList();
    private final Object anyGroupsLock = new Object();
    private ConcurrentHashMap<String, List<ExperimentGroup>> map = new ConcurrentHashMap<>();
    private final Object valueLock = new Object();

    public void put(Uri uri, ExperimentGroup experimentGroup) {
        String uriPath = getUriPath(uri, "<PATH-EMPTY>");
        if (containsAnyOperator(uriPath)) {
            synchronized (this.anyGroupsLock) {
                this.anyGroups.add(experimentGroup);
            }
            return;
        }
        List list = this.map.get(uriPath);
        if (list == null) {
            synchronized (this.valueLock) {
                if (list == null) {
                    try {
                        list = new ArrayList();
                        this.map.put(uriPath, list);
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
        }
        synchronized (list) {
            list.add(experimentGroup);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0027, code lost:
        r0 = r9.map.values().iterator();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0035, code lost:
        if (r0.hasNext() == false) goto L_0x0069;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0037, code lost:
        r1 = r0.next();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x003d, code lost:
        if (r1 != null) goto L_0x0040;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0040, code lost:
        r2 = r9.valueLock;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0042, code lost:
        monitor-enter(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        r1 = r1.iterator();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x004b, code lost:
        if (r1.hasNext() == false) goto L_0x0064;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x004d, code lost:
        r3 = (com.alibaba.ut.abtest.internal.bucketing.model.ExperimentGroup) r1.next();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x005d, code lost:
        if (r3.getId() != r10.getId()) goto L_0x0047;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x005f, code lost:
        r1.remove();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0062, code lost:
        monitor-exit(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0063, code lost:
        return r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0064, code lost:
        monitor-exit(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0069, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.alibaba.ut.abtest.internal.bucketing.model.ExperimentGroup remove(com.alibaba.ut.abtest.internal.bucketing.model.ExperimentGroup r10) {
        /*
            r9 = this;
            java.lang.Object r0 = r9.anyGroupsLock
            monitor-enter(r0)
            java.util.List<com.alibaba.ut.abtest.internal.bucketing.model.ExperimentGroup> r1 = r9.anyGroups     // Catch:{ all -> 0x006b }
            java.util.Iterator r1 = r1.iterator()     // Catch:{ all -> 0x006b }
        L_0x0009:
            boolean r2 = r1.hasNext()     // Catch:{ all -> 0x006b }
            if (r2 == 0) goto L_0x0026
            java.lang.Object r2 = r1.next()     // Catch:{ all -> 0x006b }
            com.alibaba.ut.abtest.internal.bucketing.model.ExperimentGroup r2 = (com.alibaba.ut.abtest.internal.bucketing.model.ExperimentGroup) r2     // Catch:{ all -> 0x006b }
            long r3 = r2.getId()     // Catch:{ all -> 0x006b }
            long r5 = r10.getId()     // Catch:{ all -> 0x006b }
            int r7 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r7 != 0) goto L_0x0009
            r1.remove()     // Catch:{ all -> 0x006b }
            monitor-exit(r0)     // Catch:{ all -> 0x006b }
            return r2
        L_0x0026:
            monitor-exit(r0)     // Catch:{ all -> 0x006b }
            java.util.concurrent.ConcurrentHashMap<java.lang.String, java.util.List<com.alibaba.ut.abtest.internal.bucketing.model.ExperimentGroup>> r0 = r9.map
            java.util.Collection r0 = r0.values()
            java.util.Iterator r0 = r0.iterator()
        L_0x0031:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x0069
            java.lang.Object r1 = r0.next()
            java.util.List r1 = (java.util.List) r1
            if (r1 != 0) goto L_0x0040
            goto L_0x0031
        L_0x0040:
            java.lang.Object r2 = r9.valueLock
            monitor-enter(r2)
            java.util.Iterator r1 = r1.iterator()     // Catch:{ all -> 0x0066 }
        L_0x0047:
            boolean r3 = r1.hasNext()     // Catch:{ all -> 0x0066 }
            if (r3 == 0) goto L_0x0064
            java.lang.Object r3 = r1.next()     // Catch:{ all -> 0x0066 }
            com.alibaba.ut.abtest.internal.bucketing.model.ExperimentGroup r3 = (com.alibaba.ut.abtest.internal.bucketing.model.ExperimentGroup) r3     // Catch:{ all -> 0x0066 }
            long r4 = r3.getId()     // Catch:{ all -> 0x0066 }
            long r6 = r10.getId()     // Catch:{ all -> 0x0066 }
            int r8 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r8 != 0) goto L_0x0047
            r1.remove()     // Catch:{ all -> 0x0066 }
            monitor-exit(r2)     // Catch:{ all -> 0x0066 }
            return r3
        L_0x0064:
            monitor-exit(r2)     // Catch:{ all -> 0x0066 }
            goto L_0x0031
        L_0x0066:
            r10 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0066 }
            throw r10
        L_0x0069:
            r10 = 0
            return r10
        L_0x006b:
            r10 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x006b }
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.ut.abtest.internal.bucketing.UriPathMap.remove(com.alibaba.ut.abtest.internal.bucketing.model.ExperimentGroup):com.alibaba.ut.abtest.internal.bucketing.model.ExperimentGroup");
    }

    public List<ExperimentGroup> get(Uri uri) {
        boolean z;
        boolean z2;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        synchronized (this.anyGroupsLock) {
            for (ExperimentGroup next : this.anyGroups) {
                if (equalsUri(next.getUri(), uri)) {
                    arrayList2.add(next);
                }
            }
        }
        if (arrayList2.size() > 0) {
            for (ExperimentGroup next2 : this.anyGroups) {
                if (next2.getIgnoreUris() == null || next2.getIgnoreUris().isEmpty()) {
                    arrayList.add(next2);
                } else {
                    Iterator<Uri> it = next2.getIgnoreUris().iterator();
                    while (true) {
                        if (it.hasNext()) {
                            if (equalsUri(it.next(), uri)) {
                                z2 = true;
                                break;
                            }
                        } else {
                            z2 = false;
                            break;
                        }
                    }
                    if (!z2) {
                        arrayList.add(next2);
                    }
                }
            }
        }
        List<ExperimentGroup> list = this.map.get(getUriPath(uri, "<PATH-EMPTY>"));
        if (list != null && !list.isEmpty()) {
            synchronized (list) {
                for (ExperimentGroup experimentGroup : list) {
                    if (experimentGroup.getIgnoreUris() != null && !experimentGroup.getIgnoreUris().isEmpty()) {
                        Iterator<Uri> it2 = experimentGroup.getIgnoreUris().iterator();
                        while (true) {
                            if (it2.hasNext()) {
                                if (equalsUri(it2.next(), uri)) {
                                    z = true;
                                    break;
                                }
                            } else {
                                z = false;
                                break;
                            }
                        }
                        if (z) {
                        }
                    }
                    if (equalsUri(experimentGroup.getUri(), uri)) {
                        arrayList.add(experimentGroup);
                    }
                }
            }
        }
        return arrayList;
    }

    private String getUriPath(Uri uri, String str) {
        String path = uri.getPath();
        return TextUtils.isEmpty(path) ? str : path;
    }

    private boolean containsAnyOperator(String str) {
        return TextUtils.indexOf(str, ABConstants.Operator.URI_ANY) != -1;
    }

    public boolean equalsUriPath(Uri uri, Uri uri2) {
        String uriPath = getUriPath(uri, "");
        String uriPath2 = getUriPath(uri2, "");
        if (!containsAnyOperator(uriPath)) {
            return TextUtils.equals(uriPath, uriPath2);
        }
        String[] split = TextUtils.split(uriPath, "/");
        String[] split2 = TextUtils.split(uriPath2, "/");
        if (split == null) {
            return false;
        }
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (i < split.length && i2 < 50) {
            String str = split[i];
            String str2 = null;
            String str3 = (split2 == null || i3 >= split2.length) ? null : split2[i3];
            boolean equals = TextUtils.equals(str, ABConstants.Operator.URI_ANY);
            if (str3 == null) {
                if (equals) {
                    if (i == split.length - 1) {
                        return true;
                    }
                    i++;
                } else if (TextUtils.isEmpty(str) && i == split.length - 1) {
                    return true;
                } else {
                    if (!TextUtils.isEmpty(str)) {
                        return false;
                    }
                    i++;
                }
            } else if (equals) {
                int i4 = i + 1;
                if (i4 < split.length) {
                    str2 = split[i4];
                }
                if (str2 == null) {
                    i3++;
                } else if (TextUtils.equals(str2, str3)) {
                    i += 2;
                    i3++;
                } else {
                    i3++;
                }
            } else if (!TextUtils.equals(str, str3)) {
                return false;
            } else {
                i++;
                i3++;
            }
            i2++;
        }
        if (i3 >= split2.length - 1) {
            return true;
        }
        while (i3 < split2.length) {
            if (!TextUtils.isEmpty(split2[i3])) {
                return false;
            }
            i3++;
        }
        return true;
    }

    public boolean equalsUri(Uri uri, Uri uri2) {
        if (uri == uri2) {
            return true;
        }
        if (uri == null || uri2 == null || !equalsUriPath(uri, uri2)) {
            return false;
        }
        Set<String> queryParameterNames = uri.getQueryParameterNames();
        if (queryParameterNames == null || queryParameterNames.isEmpty()) {
            return true;
        }
        if (!uri2.getQueryParameterNames().containsAll(queryParameterNames)) {
            return false;
        }
        for (String next : queryParameterNames) {
            String queryParameter = uri.getQueryParameter(next);
            if (!TextUtils.isEmpty(queryParameter) && !TextUtils.equals(queryParameter, uri2.getQueryParameter(next))) {
                return false;
            }
        }
        return true;
    }
}
