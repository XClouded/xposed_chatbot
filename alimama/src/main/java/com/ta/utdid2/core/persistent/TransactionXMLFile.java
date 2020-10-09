package com.ta.utdid2.core.persistent;

import com.ta.utdid2.core.persistent.MySharedPreferences;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.WeakHashMap;

public class TransactionXMLFile {
    /* access modifiers changed from: private */
    public static final Object GLOBAL_COMMIT_LOCK = new Object();
    public static final int MODE_PRIVATE = 0;
    private File mPreferencesDir;
    private final Object mSync = new Object();
    private HashMap<File, MySharedPreferencesImpl> sSharedPrefs = new HashMap<>();

    public TransactionXMLFile(String str) {
        if (str == null || str.length() <= 0) {
            throw new RuntimeException("Directory can not be empty");
        }
        this.mPreferencesDir = new File(str);
    }

    private File makeFilename(File file, String str) {
        if (str.indexOf(File.separatorChar) < 0) {
            return new File(file, str);
        }
        throw new IllegalArgumentException("File " + str + " contains a path separator");
    }

    private File getPreferencesDir() {
        File file;
        synchronized (this.mSync) {
            file = this.mPreferencesDir;
        }
        return file;
    }

    private File getSharedPrefsFile(String str) {
        File preferencesDir = getPreferencesDir();
        return makeFilename(preferencesDir, str + ".xml");
    }

    /*  JADX ERROR: IndexOutOfBoundsException in pass: RegionMakerVisitor
        java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
        	at java.util.ArrayList.rangeCheck(Unknown Source)
        	at java.util.ArrayList.get(Unknown Source)
        	at jadx.core.dex.nodes.InsnNode.getArg(InsnNode.java:101)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:611)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:561)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processExcHandler(RegionMaker.java:1043)
        	at jadx.core.dex.visitors.regions.RegionMaker.processTryCatchBlocks(RegionMaker.java:975)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:52)
        */
    public com.ta.utdid2.core.persistent.MySharedPreferences getMySharedPreferences(java.lang.String r5, int r6) {
        /*
            r4 = this;
            java.io.File r5 = r4.getSharedPrefsFile(r5)
            java.lang.Object r0 = GLOBAL_COMMIT_LOCK
            monitor-enter(r0)
            java.util.HashMap<java.io.File, com.ta.utdid2.core.persistent.TransactionXMLFile$MySharedPreferencesImpl> r1 = r4.sSharedPrefs     // Catch:{ all -> 0x00af }
            java.lang.Object r1 = r1.get(r5)     // Catch:{ all -> 0x00af }
            com.ta.utdid2.core.persistent.TransactionXMLFile$MySharedPreferencesImpl r1 = (com.ta.utdid2.core.persistent.TransactionXMLFile.MySharedPreferencesImpl) r1     // Catch:{ all -> 0x00af }
            if (r1 == 0) goto L_0x0019
            boolean r2 = r1.hasFileChanged()     // Catch:{ all -> 0x00af }
            if (r2 != 0) goto L_0x0019
            monitor-exit(r0)     // Catch:{ all -> 0x00af }
            return r1
        L_0x0019:
            monitor-exit(r0)     // Catch:{ all -> 0x00af }
            java.io.File r0 = makeBackupFile(r5)
            boolean r2 = r0.exists()
            if (r2 == 0) goto L_0x002a
            r5.delete()
            r0.renameTo(r5)
        L_0x002a:
            boolean r0 = r5.exists()
            r2 = 0
            if (r0 == 0) goto L_0x008b
            boolean r0 = r5.canRead()
            if (r0 == 0) goto L_0x008b
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch:{ XmlPullParserException -> 0x005a, Exception -> 0x0053 }
            r0.<init>(r5)     // Catch:{ XmlPullParserException -> 0x005a, Exception -> 0x0053 }
            java.util.HashMap r3 = com.ta.utdid2.core.persistent.XmlUtils.readMapXml(r0)     // Catch:{ XmlPullParserException -> 0x005b, Exception -> 0x004f, all -> 0x004c }
            r0.close()     // Catch:{ XmlPullParserException -> 0x004a, Exception -> 0x0048, all -> 0x004c }
            r0.close()     // Catch:{ Throwable -> 0x0046 }
        L_0x0046:
            r2 = r3
            goto L_0x008b
        L_0x0048:
            r2 = r3
            goto L_0x0054
        L_0x004a:
            r2 = r3
            goto L_0x005b
        L_0x004c:
            r5 = move-exception
            r2 = r0
            goto L_0x0080
        L_0x004f:
            goto L_0x0054
        L_0x0051:
            r5 = move-exception
            goto L_0x0080
        L_0x0053:
            r0 = r2
        L_0x0054:
            if (r0 == 0) goto L_0x008b
            r0.close()     // Catch:{ Throwable -> 0x008b }
            goto L_0x008b
        L_0x005a:
            r0 = r2
        L_0x005b:
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch:{ Exception -> 0x0077, all -> 0x006f }
            r3.<init>(r5)     // Catch:{ Exception -> 0x0077, all -> 0x006f }
            int r0 = r3.available()     // Catch:{ Exception -> 0x006d, all -> 0x006a }
            byte[] r0 = new byte[r0]     // Catch:{ Exception -> 0x006d, all -> 0x006a }
            r3.read(r0)     // Catch:{ Exception -> 0x006d, all -> 0x006a }
            goto L_0x007a
        L_0x006a:
            r5 = move-exception
            r2 = r3
            goto L_0x0071
        L_0x006d:
            goto L_0x0078
        L_0x006f:
            r5 = move-exception
            r2 = r0
        L_0x0071:
            if (r2 == 0) goto L_0x0076
            r2.close()     // Catch:{ Throwable -> 0x0076 }
        L_0x0076:
            throw r5     // Catch:{ all -> 0x0051 }
        L_0x0077:
            r3 = r0
        L_0x0078:
            if (r3 == 0) goto L_0x0086
        L_0x007a:
            r3.close()     // Catch:{ Throwable -> 0x0086, all -> 0x007e }
            goto L_0x0086
        L_0x007e:
            r5 = move-exception
            r2 = r3
        L_0x0080:
            if (r2 == 0) goto L_0x0085
            r2.close()     // Catch:{ Throwable -> 0x0085 }
        L_0x0085:
            throw r5
        L_0x0086:
            if (r3 == 0) goto L_0x008b
            r3.close()     // Catch:{ Throwable -> 0x008b }
        L_0x008b:
            java.lang.Object r3 = GLOBAL_COMMIT_LOCK
            monitor-enter(r3)
            if (r1 == 0) goto L_0x0096
            r1.replace(r2)     // Catch:{ all -> 0x0094 }
            goto L_0x00ab
        L_0x0094:
            r5 = move-exception
            goto L_0x00ad
        L_0x0096:
            java.util.HashMap<java.io.File, com.ta.utdid2.core.persistent.TransactionXMLFile$MySharedPreferencesImpl> r0 = r4.sSharedPrefs     // Catch:{ all -> 0x0094 }
            java.lang.Object r0 = r0.get(r5)     // Catch:{ all -> 0x0094 }
            r1 = r0
            com.ta.utdid2.core.persistent.TransactionXMLFile$MySharedPreferencesImpl r1 = (com.ta.utdid2.core.persistent.TransactionXMLFile.MySharedPreferencesImpl) r1     // Catch:{ all -> 0x0094 }
            if (r1 != 0) goto L_0x00ab
            com.ta.utdid2.core.persistent.TransactionXMLFile$MySharedPreferencesImpl r1 = new com.ta.utdid2.core.persistent.TransactionXMLFile$MySharedPreferencesImpl     // Catch:{ all -> 0x0094 }
            r1.<init>(r5, r6, r2)     // Catch:{ all -> 0x0094 }
            java.util.HashMap<java.io.File, com.ta.utdid2.core.persistent.TransactionXMLFile$MySharedPreferencesImpl> r6 = r4.sSharedPrefs     // Catch:{ all -> 0x0094 }
            r6.put(r5, r1)     // Catch:{ all -> 0x0094 }
        L_0x00ab:
            monitor-exit(r3)     // Catch:{ all -> 0x0094 }
            return r1
        L_0x00ad:
            monitor-exit(r3)     // Catch:{ all -> 0x0094 }
            throw r5
        L_0x00af:
            r5 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x00af }
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ta.utdid2.core.persistent.TransactionXMLFile.getMySharedPreferences(java.lang.String, int):com.ta.utdid2.core.persistent.MySharedPreferences");
    }

    /* access modifiers changed from: private */
    public static File makeBackupFile(File file) {
        return new File(file.getPath() + ".bak");
    }

    private static final class MySharedPreferencesImpl implements MySharedPreferences {
        private static final Object mContent = new Object();
        private boolean hasChange = false;
        private final File mBackupFile;
        private final File mFile;
        /* access modifiers changed from: private */
        public WeakHashMap<MySharedPreferences.OnSharedPreferenceChangeListener, Object> mListeners;
        /* access modifiers changed from: private */
        public Map mMap;
        private final int mMode;

        MySharedPreferencesImpl(File file, int i, Map map) {
            this.mFile = file;
            this.mBackupFile = TransactionXMLFile.makeBackupFile(file);
            this.mMode = i;
            this.mMap = map == null ? new HashMap() : map;
            this.mListeners = new WeakHashMap<>();
        }

        public boolean checkFile() {
            return this.mFile != null && new File(this.mFile.getAbsolutePath()).exists();
        }

        public void setHasChange(boolean z) {
            synchronized (this) {
                this.hasChange = z;
            }
        }

        public boolean hasFileChanged() {
            boolean z;
            synchronized (this) {
                z = this.hasChange;
            }
            return z;
        }

        public void replace(Map map) {
            if (map != null) {
                synchronized (this) {
                    this.mMap = map;
                }
            }
        }

        public void registerOnSharedPreferenceChangeListener(MySharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
            synchronized (this) {
                this.mListeners.put(onSharedPreferenceChangeListener, mContent);
            }
        }

        public void unregisterOnSharedPreferenceChangeListener(MySharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
            synchronized (this) {
                this.mListeners.remove(onSharedPreferenceChangeListener);
            }
        }

        public Map<String, ?> getAll() {
            HashMap hashMap;
            synchronized (this) {
                hashMap = new HashMap(this.mMap);
            }
            return hashMap;
        }

        public String getString(String str, String str2) {
            String str3;
            synchronized (this) {
                str3 = (String) this.mMap.get(str);
                if (str3 == null) {
                    str3 = str2;
                }
            }
            return str3;
        }

        public int getInt(String str, int i) {
            synchronized (this) {
                Integer num = (Integer) this.mMap.get(str);
                if (num != null) {
                    i = num.intValue();
                }
            }
            return i;
        }

        public long getLong(String str, long j) {
            synchronized (this) {
                Long l = (Long) this.mMap.get(str);
                if (l != null) {
                    j = l.longValue();
                }
            }
            return j;
        }

        public float getFloat(String str, float f) {
            synchronized (this) {
                Float f2 = (Float) this.mMap.get(str);
                if (f2 != null) {
                    f = f2.floatValue();
                }
            }
            return f;
        }

        public boolean getBoolean(String str, boolean z) {
            synchronized (this) {
                Boolean bool = (Boolean) this.mMap.get(str);
                if (bool != null) {
                    z = bool.booleanValue();
                }
            }
            return z;
        }

        public boolean contains(String str) {
            boolean containsKey;
            synchronized (this) {
                containsKey = this.mMap.containsKey(str);
            }
            return containsKey;
        }

        public final class EditorImpl implements MySharedPreferences.MyEditor {
            private boolean mClear = false;
            private final Map<String, Object> mModified = new HashMap();

            public EditorImpl() {
            }

            public MySharedPreferences.MyEditor putString(String str, String str2) {
                synchronized (this) {
                    this.mModified.put(str, str2);
                }
                return this;
            }

            public MySharedPreferences.MyEditor putInt(String str, int i) {
                synchronized (this) {
                    this.mModified.put(str, Integer.valueOf(i));
                }
                return this;
            }

            public MySharedPreferences.MyEditor putLong(String str, long j) {
                synchronized (this) {
                    this.mModified.put(str, Long.valueOf(j));
                }
                return this;
            }

            public MySharedPreferences.MyEditor putFloat(String str, float f) {
                synchronized (this) {
                    this.mModified.put(str, Float.valueOf(f));
                }
                return this;
            }

            public MySharedPreferences.MyEditor putBoolean(String str, boolean z) {
                synchronized (this) {
                    this.mModified.put(str, Boolean.valueOf(z));
                }
                return this;
            }

            public MySharedPreferences.MyEditor remove(String str) {
                synchronized (this) {
                    this.mModified.put(str, this);
                }
                return this;
            }

            public MySharedPreferences.MyEditor clear() {
                synchronized (this) {
                    this.mClear = true;
                }
                return this;
            }

            public boolean commit() {
                boolean z;
                ArrayList arrayList;
                HashSet<MySharedPreferences.OnSharedPreferenceChangeListener> hashSet;
                boolean access$400;
                synchronized (TransactionXMLFile.GLOBAL_COMMIT_LOCK) {
                    z = MySharedPreferencesImpl.this.mListeners.size() > 0;
                    arrayList = null;
                    if (z) {
                        arrayList = new ArrayList();
                        hashSet = new HashSet<>(MySharedPreferencesImpl.this.mListeners.keySet());
                    } else {
                        hashSet = null;
                    }
                    synchronized (this) {
                        if (this.mClear) {
                            MySharedPreferencesImpl.this.mMap.clear();
                            this.mClear = false;
                        }
                        for (Map.Entry next : this.mModified.entrySet()) {
                            String str = (String) next.getKey();
                            Object value = next.getValue();
                            if (value == this) {
                                MySharedPreferencesImpl.this.mMap.remove(str);
                            } else {
                                MySharedPreferencesImpl.this.mMap.put(str, value);
                            }
                            if (z) {
                                arrayList.add(str);
                            }
                        }
                        this.mModified.clear();
                    }
                    access$400 = MySharedPreferencesImpl.this.writeFileLocked();
                    if (access$400) {
                        MySharedPreferencesImpl.this.setHasChange(true);
                    }
                }
                if (z) {
                    for (int size = arrayList.size() - 1; size >= 0; size--) {
                        String str2 = (String) arrayList.get(size);
                        for (MySharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener : hashSet) {
                            if (onSharedPreferenceChangeListener != null) {
                                onSharedPreferenceChangeListener.onSharedPreferenceChanged(MySharedPreferencesImpl.this, str2);
                            }
                        }
                    }
                }
                return access$400;
            }
        }

        public MySharedPreferences.MyEditor edit() {
            return new EditorImpl();
        }

        private FileOutputStream createFileOutputStream(File file) {
            try {
                return new FileOutputStream(file);
            } catch (FileNotFoundException unused) {
                if (!file.getParentFile().mkdir()) {
                    return null;
                }
                try {
                    return new FileOutputStream(file);
                } catch (FileNotFoundException unused2) {
                    return null;
                }
            }
        }

        /* access modifiers changed from: private */
        public boolean writeFileLocked() {
            if (this.mFile.exists()) {
                if (this.mBackupFile.exists()) {
                    this.mFile.delete();
                } else if (!this.mFile.renameTo(this.mBackupFile)) {
                    return false;
                }
            }
            try {
                FileOutputStream createFileOutputStream = createFileOutputStream(this.mFile);
                if (createFileOutputStream == null) {
                    return false;
                }
                XmlUtils.writeMapXml(this.mMap, createFileOutputStream);
                createFileOutputStream.close();
                this.mBackupFile.delete();
                return true;
            } catch (Exception unused) {
                if (this.mFile.exists()) {
                    this.mFile.delete();
                }
                return false;
            }
        }
    }
}
