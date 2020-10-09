package com.ali.protodb.migration.sharedpreferences;

import java.util.HashSet;
import java.util.Set;

public class SharedPreferencesDelegateManager {
    public static final String ALIVFS_CONFIG_GROUP = "ali_database_es";
    public static final String ALIVFS_CONFIG_SP_DELEGATE_COMPACT = "sp_delegate_compact";
    private static final String ALIVFS_CONFIG_SP_DELEGATE_WHITE_LIST = "sp_delegate_white_list";
    private static final String ALIVFS_CONFIG_SP_FORCE_DOWNGRADE = "sp_delegate_downgrade";
    private static final String DELEGATE_CONFIG_FILE_NAME = "sp_delegate.cfg";
    private static final String DELEGATE_MIGRATE_FILE_NAME = "sp_migrate.cfg";
    private static final String TAG = "ProtoDB";
    private static final String WHITE_LIST_SEPARATOR = ",";
    private static volatile boolean delegateInitialized = false;
    private static Set<String> whiteListGroups = new HashSet();

    /* JADX WARNING: Removed duplicated region for block: B:28:0x0069 A[SYNTHETIC, Splitter:B:28:0x0069] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0072 A[SYNTHETIC, Splitter:B:33:0x0072] */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00a8 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00a9 A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.content.SharedPreferences getSharedPreferencesDelegate(android.content.Context r5, java.lang.String r6) {
        /*
            boolean r0 = delegateInitialized
            r1 = 0
            if (r0 != 0) goto L_0x0083
            java.lang.Class<com.ali.protodb.migration.sharedpreferences.SharedPreferencesDelegateManager> r0 = com.ali.protodb.migration.sharedpreferences.SharedPreferencesDelegateManager.class
            monitor-enter(r0)
            boolean r2 = delegateInitialized     // Catch:{ all -> 0x0080 }
            if (r2 != 0) goto L_0x007e
            java.io.File r2 = r5.getFilesDir()     // Catch:{ all -> 0x0080 }
            java.lang.String r2 = r2.getAbsolutePath()     // Catch:{ all -> 0x0080 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0080 }
            r3.<init>()     // Catch:{ all -> 0x0080 }
            r3.append(r2)     // Catch:{ all -> 0x0080 }
            java.lang.String r2 = java.io.File.pathSeparator     // Catch:{ all -> 0x0080 }
            r3.append(r2)     // Catch:{ all -> 0x0080 }
            java.lang.String r2 = "sp_delegate.cfg"
            r3.append(r2)     // Catch:{ all -> 0x0080 }
            java.lang.String r2 = r3.toString()     // Catch:{ all -> 0x0080 }
            java.io.File r3 = new java.io.File     // Catch:{ all -> 0x0080 }
            r3.<init>(r2)     // Catch:{ all -> 0x0080 }
            boolean r2 = r3.exists()     // Catch:{ all -> 0x0080 }
            if (r2 == 0) goto L_0x007b
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0062, all -> 0x005f }
            java.io.FileReader r4 = new java.io.FileReader     // Catch:{ IOException -> 0x0062, all -> 0x005f }
            r4.<init>(r3)     // Catch:{ IOException -> 0x0062, all -> 0x005f }
            r2.<init>(r4)     // Catch:{ IOException -> 0x0062, all -> 0x005f }
            java.lang.String r3 = r2.readLine()     // Catch:{ IOException -> 0x005d }
            if (r3 == 0) goto L_0x0054
            java.lang.String r4 = ","
            java.lang.String[] r3 = r3.split(r4)     // Catch:{ IOException -> 0x005d }
            java.util.Set<java.lang.String> r4 = whiteListGroups     // Catch:{ IOException -> 0x005d }
            java.util.List r3 = java.util.Arrays.asList(r3)     // Catch:{ IOException -> 0x005d }
            r4.addAll(r3)     // Catch:{ IOException -> 0x005d }
        L_0x0054:
            r2.close()     // Catch:{ IOException -> 0x0058 }
            goto L_0x007b
        L_0x0058:
            r2 = move-exception
        L_0x0059:
            r2.printStackTrace()     // Catch:{ all -> 0x0080 }
            goto L_0x007b
        L_0x005d:
            r3 = move-exception
            goto L_0x0064
        L_0x005f:
            r5 = move-exception
            r2 = r1
            goto L_0x0070
        L_0x0062:
            r3 = move-exception
            r2 = r1
        L_0x0064:
            r3.printStackTrace()     // Catch:{ all -> 0x006f }
            if (r2 == 0) goto L_0x007b
            r2.close()     // Catch:{ IOException -> 0x006d }
            goto L_0x007b
        L_0x006d:
            r2 = move-exception
            goto L_0x0059
        L_0x006f:
            r5 = move-exception
        L_0x0070:
            if (r2 == 0) goto L_0x007a
            r2.close()     // Catch:{ IOException -> 0x0076 }
            goto L_0x007a
        L_0x0076:
            r6 = move-exception
            r6.printStackTrace()     // Catch:{ all -> 0x0080 }
        L_0x007a:
            throw r5     // Catch:{ all -> 0x0080 }
        L_0x007b:
            r2 = 1
            delegateInitialized = r2     // Catch:{ all -> 0x0080 }
        L_0x007e:
            monitor-exit(r0)     // Catch:{ all -> 0x0080 }
            goto L_0x0083
        L_0x0080:
            r5 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0080 }
            throw r5
        L_0x0083:
            if (r6 == 0) goto L_0x00a9
            java.util.Set<java.lang.String> r0 = whiteListGroups
            int r0 = r0.size()
            if (r0 <= 0) goto L_0x00a9
            java.util.Set<java.lang.String> r0 = whiteListGroups
            boolean r0 = r0.contains(r6)
            if (r0 == 0) goto L_0x00a9
            java.lang.String r0 = "sp_delegate"
            boolean r0 = com.taobao.android.speed.TBSpeed.isSpeedEdition(r5, r0)
            if (r0 == 0) goto L_0x00a9
            com.ali.protodb.migration.sharedpreferences.SharedPreferencesDelegate r0 = new com.ali.protodb.migration.sharedpreferences.SharedPreferencesDelegate
            r0.<init>(r5, r6)
            com.ali.protodb.lsdb.LSDB r5 = com.ali.protodb.migration.sharedpreferences.SharedPreferencesDelegate.getLsdb()
            if (r5 == 0) goto L_0x00a9
            return r0
        L_0x00a9:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.protodb.migration.sharedpreferences.SharedPreferencesDelegateManager.getSharedPreferencesDelegate(android.content.Context, java.lang.String):android.content.SharedPreferences");
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x00ba  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00c0  */
    /* JADX WARNING: Removed duplicated region for block: B:38:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void updateConfig(android.content.Context r5) throws java.io.IOException {
        /*
            java.io.File r5 = r5.getFilesDir()
            java.lang.String r5 = r5.getAbsolutePath()
            com.taobao.orange.OrangeConfig r0 = com.taobao.orange.OrangeConfig.getInstance()
            java.lang.String r1 = "ali_database_es"
            java.lang.String r2 = "sp_delegate_white_list"
            java.lang.String r3 = ""
            java.lang.String r0 = r0.getConfig(r1, r2, r3)
            com.taobao.orange.OrangeConfig r1 = com.taobao.orange.OrangeConfig.getInstance()
            java.lang.String r2 = "ali_database_es"
            java.lang.String r3 = "sp_delegate_downgrade"
            java.lang.String r4 = ""
            java.lang.String r1 = r1.getConfig(r2, r3, r4)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r5)
            java.lang.String r3 = java.io.File.pathSeparator
            r2.append(r3)
            java.lang.String r3 = "sp_delegate.cfg"
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r5)
            java.lang.String r5 = java.io.File.pathSeparator
            r3.append(r5)
            java.lang.String r5 = "sp_migrate.cfg"
            r3.append(r5)
            java.lang.String r5 = r3.toString()
            java.io.File r3 = new java.io.File
            r3.<init>(r2)
            java.io.File r2 = new java.io.File
            r2.<init>(r5)
            java.lang.String r4 = "true"
            boolean r1 = r4.equals(r1)
            if (r1 == 0) goto L_0x0075
            boolean r5 = r3.exists()
            if (r5 == 0) goto L_0x006b
            r3.delete()
        L_0x006b:
            boolean r5 = r2.exists()
            if (r5 == 0) goto L_0x0074
            r2.delete()
        L_0x0074:
            return
        L_0x0075:
            if (r0 == 0) goto L_0x00c4
            int r1 = r0.length()
            if (r1 <= 0) goto L_0x00c4
            boolean r1 = r2.exists()
            if (r1 == 0) goto L_0x0086
            r2.delete()
        L_0x0086:
            r1 = 0
            java.io.FileWriter r2 = new java.io.FileWriter     // Catch:{ IOException -> 0x00b4 }
            r2.<init>(r5)     // Catch:{ IOException -> 0x00b4 }
            r2.write(r0)     // Catch:{ IOException -> 0x00ae, all -> 0x00ac }
            r2.flush()     // Catch:{ IOException -> 0x00ae, all -> 0x00ac }
            java.lang.String r5 = "ProtoDB"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00ae, all -> 0x00ac }
            r1.<init>()     // Catch:{ IOException -> 0x00ae, all -> 0x00ac }
            java.lang.String r3 = "received new sp delegate config: "
            r1.append(r3)     // Catch:{ IOException -> 0x00ae, all -> 0x00ac }
            r1.append(r0)     // Catch:{ IOException -> 0x00ae, all -> 0x00ac }
            java.lang.String r0 = r1.toString()     // Catch:{ IOException -> 0x00ae, all -> 0x00ac }
            android.util.Log.e(r5, r0)     // Catch:{ IOException -> 0x00ae, all -> 0x00ac }
            r2.close()
            goto L_0x00c4
        L_0x00ac:
            r5 = move-exception
            goto L_0x00be
        L_0x00ae:
            r5 = move-exception
            r1 = r2
            goto L_0x00b5
        L_0x00b1:
            r5 = move-exception
            r2 = r1
            goto L_0x00be
        L_0x00b4:
            r5 = move-exception
        L_0x00b5:
            r5.printStackTrace()     // Catch:{ all -> 0x00b1 }
            if (r1 == 0) goto L_0x00c4
            r1.close()
            goto L_0x00c4
        L_0x00be:
            if (r2 == 0) goto L_0x00c3
            r2.close()
        L_0x00c3:
            throw r5
        L_0x00c4:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.protodb.migration.sharedpreferences.SharedPreferencesDelegateManager.updateConfig(android.content.Context):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0069, code lost:
        if (r7 != null) goto L_0x005b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:95:0x01e3, code lost:
        if (r6 == null) goto L_0x01e8;
     */
    /* JADX WARNING: Removed duplicated region for block: B:102:0x0204  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x009f  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00a7  */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x01de  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean migrateData(android.app.Application r16) throws java.lang.NoSuchMethodException, java.lang.reflect.InvocationTargetException, java.lang.IllegalAccessException, java.io.IOException {
        /*
            r1 = r16
            java.io.File r0 = r16.getFilesDir()
            java.lang.String r0 = r0.getAbsolutePath()
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r0)
            java.lang.String r3 = java.io.File.pathSeparator
            r2.append(r3)
            java.lang.String r3 = "sp_delegate.cfg"
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r0)
            java.lang.String r0 = java.io.File.pathSeparator
            r3.append(r0)
            java.lang.String r0 = "sp_migrate.cfg"
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            java.io.File r3 = new java.io.File
            r3.<init>(r2)
            java.io.File r2 = new java.io.File
            r2.<init>(r0)
            java.lang.String r4 = ""
            boolean r0 = r2.exists()
            r5 = 1
            if (r0 == 0) goto L_0x0208
            r6 = 0
            java.io.BufferedReader r7 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0064, all -> 0x0061 }
            java.io.FileReader r0 = new java.io.FileReader     // Catch:{ IOException -> 0x0064, all -> 0x0061 }
            r0.<init>(r2)     // Catch:{ IOException -> 0x0064, all -> 0x0061 }
            r7.<init>(r0)     // Catch:{ IOException -> 0x0064, all -> 0x0061 }
            java.lang.String r0 = r7.readLine()     // Catch:{ IOException -> 0x005f }
            if (r0 == 0) goto L_0x005b
            r4 = r0
        L_0x005b:
            r7.close()
            goto L_0x006c
        L_0x005f:
            r0 = move-exception
            goto L_0x0066
        L_0x0061:
            r0 = move-exception
            goto L_0x0202
        L_0x0064:
            r0 = move-exception
            r7 = r6
        L_0x0066:
            r0.printStackTrace()     // Catch:{ all -> 0x0200 }
            if (r7 == 0) goto L_0x006c
            goto L_0x005b
        L_0x006c:
            java.lang.String r2 = ""
            boolean r0 = r3.exists()
            if (r0 == 0) goto L_0x0099
            java.io.BufferedReader r7 = new java.io.BufferedReader     // Catch:{ IOException -> 0x008d, all -> 0x008b }
            java.io.FileReader r0 = new java.io.FileReader     // Catch:{ IOException -> 0x008d, all -> 0x008b }
            r0.<init>(r3)     // Catch:{ IOException -> 0x008d, all -> 0x008b }
            r7.<init>(r0)     // Catch:{ IOException -> 0x008d, all -> 0x008b }
            java.lang.String r0 = r7.readLine()     // Catch:{ IOException -> 0x0089 }
            if (r0 == 0) goto L_0x0085
            r2 = r0
        L_0x0085:
            r7.close()
            goto L_0x0099
        L_0x0089:
            r0 = move-exception
            goto L_0x008f
        L_0x008b:
            r0 = move-exception
            goto L_0x0095
        L_0x008d:
            r0 = move-exception
            r7 = r6
        L_0x008f:
            r0.printStackTrace()     // Catch:{ all -> 0x0093 }
            goto L_0x0085
        L_0x0093:
            r0 = move-exception
            r6 = r7
        L_0x0095:
            r6.close()
            throw r0
        L_0x0099:
            boolean r0 = r2.equals(r4)
            if (r0 == 0) goto L_0x00a7
            java.lang.String r0 = "ProtoDB"
            java.lang.String r1 = "config file and migrate file are identical, migration is not necessary"
            android.util.Log.e(r0, r1)
            return r5
        L_0x00a7:
            java.util.HashSet r0 = new java.util.HashSet
            java.lang.String r7 = ","
            java.lang.String[] r2 = r2.split(r7)
            java.util.List r2 = java.util.Arrays.asList(r2)
            r0.<init>(r2)
            java.util.HashSet r2 = new java.util.HashSet
            java.lang.String r7 = ","
            java.lang.String[] r7 = r4.split(r7)
            java.util.List r7 = java.util.Arrays.asList(r7)
            r2.<init>(r7)
            com.ali.protodb.lsdb.LSDB r7 = com.ali.protodb.migration.sharedpreferences.SharedPreferencesDelegate.getLsdb()
            if (r7 != 0) goto L_0x00d1
            java.lang.String r7 = "sp_delegate"
            com.ali.protodb.lsdb.LSDB r7 = com.ali.protodb.lsdb.LSDB.open(r1, r7, r6)
        L_0x00d1:
            r8 = 0
            if (r7 != 0) goto L_0x00d5
            return r8
        L_0x00d5:
            java.lang.Class r9 = r16.getClass()
            java.lang.Class r9 = r9.getSuperclass()
            if (r9 != 0) goto L_0x00e0
            return r8
        L_0x00e0:
            java.lang.String r10 = "getSharedPreferences"
            r11 = 2
            java.lang.Class[] r12 = new java.lang.Class[r11]
            java.lang.Class<java.lang.String> r13 = java.lang.String.class
            r12[r8] = r13
            java.lang.Class r13 = java.lang.Integer.TYPE
            r12[r5] = r13
            java.lang.reflect.Method r9 = r9.getMethod(r10, r12)
            java.util.Iterator r10 = r0.iterator()
        L_0x00f5:
            boolean r12 = r10.hasNext()
            if (r12 == 0) goto L_0x0165
            java.lang.Object r12 = r10.next()
            java.lang.String r12 = (java.lang.String) r12
            boolean r13 = r2.contains(r12)
            if (r13 != 0) goto L_0x0162
            java.lang.Object[] r13 = new java.lang.Object[r11]
            r13[r8] = r12
            java.lang.Integer r14 = java.lang.Integer.valueOf(r8)
            r13[r5] = r14
            java.lang.Object r13 = r9.invoke(r1, r13)
            if (r13 != 0) goto L_0x0118
            return r8
        L_0x0118:
            android.content.SharedPreferences r13 = (android.content.SharedPreferences) r13
            android.content.SharedPreferences$Editor r13 = r13.edit()
            com.ali.protodb.lsdb.Key r14 = new com.ali.protodb.lsdb.Key
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            r15.<init>()
            r15.append(r12)
            java.lang.String r6 = "-"
            r15.append(r6)
            java.lang.String r6 = r15.toString()
            r14.<init>(r6)
            com.ali.protodb.lsdb.Key r6 = new com.ali.protodb.lsdb.Key
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            r15.<init>()
            r15.append(r12)
            java.lang.String r5 = "."
            r15.append(r5)
            java.lang.String r5 = r15.toString()
            r6.<init>(r5)
            com.ali.protodb.lsdb.Iterator r5 = r7.keyIterator(r14, r6)
        L_0x014e:
            java.lang.Object r6 = r5.next()
            com.ali.protodb.lsdb.Key r6 = (com.ali.protodb.lsdb.Key) r6
            if (r6 == 0) goto L_0x0162
            java.lang.String r14 = r7.getString(r6)
            java.lang.String r6 = com.ali.protodb.migration.sharedpreferences.SharedPreferencesDelegate.unwrapKey(r12, r6)
            r13.putString(r6, r14)
            goto L_0x014e
        L_0x0162:
            r5 = 1
            r6 = 0
            goto L_0x00f5
        L_0x0165:
            java.util.Iterator r2 = r2.iterator()
        L_0x0169:
            boolean r5 = r2.hasNext()
            if (r5 == 0) goto L_0x01c9
            java.lang.Object r5 = r2.next()
            java.lang.String r5 = (java.lang.String) r5
            boolean r6 = r0.contains(r5)
            if (r6 != 0) goto L_0x01c7
            java.lang.Object[] r6 = new java.lang.Object[r11]
            r6[r8] = r5
            java.lang.Integer r10 = java.lang.Integer.valueOf(r8)
            r12 = 1
            r6[r12] = r10
            java.lang.Object r6 = r9.invoke(r1, r6)
            if (r6 != 0) goto L_0x018d
            return r8
        L_0x018d:
            android.content.SharedPreferences r6 = (android.content.SharedPreferences) r6
            java.util.Map r6 = r6.getAll()
            java.util.Set r10 = r6.keySet()
            java.util.Iterator r10 = r10.iterator()
        L_0x019b:
            boolean r12 = r10.hasNext()
            if (r12 == 0) goto L_0x01c7
            java.lang.Object r12 = r10.next()
            java.lang.String r12 = (java.lang.String) r12
            if (r12 == 0) goto L_0x01c5
            java.lang.Object r13 = r6.get(r12)
            if (r13 != 0) goto L_0x01b8
            com.ali.protodb.lsdb.Key r12 = com.ali.protodb.migration.sharedpreferences.SharedPreferencesDelegate.wrapKey(r5, r12)
            r14 = 0
            r7.insertString(r12, r14)
            goto L_0x019b
        L_0x01b8:
            r14 = 0
            com.ali.protodb.lsdb.Key r12 = com.ali.protodb.migration.sharedpreferences.SharedPreferencesDelegate.wrapKey(r5, r12)
            java.lang.String r13 = r13.toString()
            r7.insertString(r12, r13)
            goto L_0x019b
        L_0x01c5:
            r14 = 0
            goto L_0x019b
        L_0x01c7:
            r14 = 0
            goto L_0x0169
        L_0x01c9:
            r14 = 0
            java.io.FileWriter r6 = new java.io.FileWriter     // Catch:{ Exception -> 0x01e2, all -> 0x01da }
            r6.<init>(r3)     // Catch:{ Exception -> 0x01e2, all -> 0x01da }
            r6.write(r4)     // Catch:{ Exception -> 0x01d8, all -> 0x01d6 }
            r6.flush()     // Catch:{ Exception -> 0x01d8, all -> 0x01d6 }
            goto L_0x01e5
        L_0x01d6:
            r0 = move-exception
            goto L_0x01dc
        L_0x01d8:
            goto L_0x01e3
        L_0x01da:
            r0 = move-exception
            r6 = r14
        L_0x01dc:
            if (r6 == 0) goto L_0x01e1
            r6.close()
        L_0x01e1:
            throw r0
        L_0x01e2:
            r6 = r14
        L_0x01e3:
            if (r6 == 0) goto L_0x01e8
        L_0x01e5:
            r6.close()
        L_0x01e8:
            java.lang.String r0 = "ProtoDB"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "merge sp delegate success: "
            r1.append(r2)
            r1.append(r4)
            java.lang.String r1 = r1.toString()
            android.util.Log.e(r0, r1)
            r1 = 1
            return r1
        L_0x0200:
            r0 = move-exception
            r6 = r7
        L_0x0202:
            if (r6 == 0) goto L_0x0207
            r6.close()
        L_0x0207:
            throw r0
        L_0x0208:
            r1 = 1
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.protodb.migration.sharedpreferences.SharedPreferencesDelegateManager.migrateData(android.app.Application):boolean");
    }
}
