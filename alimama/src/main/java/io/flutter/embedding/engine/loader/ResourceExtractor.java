package io.flutter.embedding.engine.loader;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import androidx.annotation.NonNull;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

class ResourceExtractor {
    private static final String[] SUPPORTED_ABIS = getSupportedAbis();
    private static final String TAG = "ResourceExtractor";
    private static final String TIMESTAMP_PREFIX = "res_timestamp-";
    @NonNull
    private final AssetManager mAssetManager;
    @NonNull
    private final String mDataDirPath;
    private ExtractTask mExtractTask;
    @NonNull
    private final PackageManager mPackageManager;
    @NonNull
    private final String mPackageName;
    @NonNull
    private final HashSet<String> mResources = new HashSet<>();

    static long getVersionCode(@NonNull PackageInfo packageInfo) {
        if (Build.VERSION.SDK_INT >= 28) {
            return packageInfo.getLongVersionCode();
        }
        return (long) packageInfo.versionCode;
    }

    private static class ExtractTask extends AsyncTask<Void, Void, Void> {
        @NonNull
        private final AssetManager mAssetManager;
        @NonNull
        private final String mDataDirPath;
        @NonNull
        private final PackageManager mPackageManager;
        @NonNull
        private final String mPackageName;
        @NonNull
        private final HashSet<String> mResources;

        ExtractTask(@NonNull String str, @NonNull HashSet<String> hashSet, @NonNull String str2, @NonNull PackageManager packageManager, @NonNull AssetManager assetManager) {
            this.mDataDirPath = str;
            this.mResources = hashSet;
            this.mAssetManager = assetManager;
            this.mPackageName = str2;
            this.mPackageManager = packageManager;
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(Void... voidArr) {
            File file = new File(this.mDataDirPath);
            String access$000 = ResourceExtractor.checkTimestamp(file, this.mPackageManager, this.mPackageName);
            if (access$000 == null) {
                return null;
            }
            ResourceExtractor.deleteFiles(this.mDataDirPath, this.mResources);
            if (extractAPK(file) && access$000 != null) {
                try {
                    new File(file, access$000).createNewFile();
                } catch (IOException unused) {
                    Log.w(ResourceExtractor.TAG, "Failed to write resource timestamp");
                }
            }
            return null;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0053, code lost:
            r2 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x0054, code lost:
            r5 = null;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x0058, code lost:
            r5 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x0059, code lost:
            r6 = r5;
            r5 = r2;
            r2 = r6;
         */
        @androidx.annotation.WorkerThread
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private boolean extractAPK(@androidx.annotation.NonNull java.io.File r8) {
            /*
                r7 = this;
                java.util.HashSet<java.lang.String> r0 = r7.mResources
                java.util.Iterator r0 = r0.iterator()
            L_0x0006:
                boolean r1 = r0.hasNext()
                if (r1 == 0) goto L_0x009e
                java.lang.Object r1 = r0.next()
                java.lang.String r1 = (java.lang.String) r1
                java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ FileNotFoundException -> 0x009b, IOException -> 0x0077 }
                r2.<init>()     // Catch:{ FileNotFoundException -> 0x009b, IOException -> 0x0077 }
                java.lang.String r3 = "assets/"
                r2.append(r3)     // Catch:{ FileNotFoundException -> 0x009b, IOException -> 0x0077 }
                r2.append(r1)     // Catch:{ FileNotFoundException -> 0x009b, IOException -> 0x0077 }
                r2.toString()     // Catch:{ FileNotFoundException -> 0x009b, IOException -> 0x0077 }
                java.io.File r2 = new java.io.File     // Catch:{ FileNotFoundException -> 0x009b, IOException -> 0x0077 }
                r2.<init>(r8, r1)     // Catch:{ FileNotFoundException -> 0x009b, IOException -> 0x0077 }
                boolean r3 = r2.exists()     // Catch:{ FileNotFoundException -> 0x009b, IOException -> 0x0077 }
                if (r3 == 0) goto L_0x002e
                goto L_0x0006
            L_0x002e:
                java.io.File r3 = r2.getParentFile()     // Catch:{ FileNotFoundException -> 0x009b, IOException -> 0x0077 }
                if (r3 == 0) goto L_0x003b
                java.io.File r3 = r2.getParentFile()     // Catch:{ FileNotFoundException -> 0x009b, IOException -> 0x0077 }
                r3.mkdirs()     // Catch:{ FileNotFoundException -> 0x009b, IOException -> 0x0077 }
            L_0x003b:
                android.content.res.AssetManager r3 = r7.mAssetManager     // Catch:{ FileNotFoundException -> 0x009b, IOException -> 0x0077 }
                java.io.InputStream r1 = r3.open(r1)     // Catch:{ FileNotFoundException -> 0x009b, IOException -> 0x0077 }
                r3 = 0
                java.io.FileOutputStream r4 = new java.io.FileOutputStream     // Catch:{ Throwable -> 0x0068 }
                r4.<init>(r2)     // Catch:{ Throwable -> 0x0068 }
                io.flutter.embedding.engine.loader.ResourceExtractor.copy(r1, r4)     // Catch:{ Throwable -> 0x0056, all -> 0x0053 }
                r4.close()     // Catch:{ Throwable -> 0x0068 }
                if (r1 == 0) goto L_0x0006
                r1.close()     // Catch:{ FileNotFoundException -> 0x009b, IOException -> 0x0077 }
                goto L_0x0006
            L_0x0053:
                r2 = move-exception
                r5 = r3
                goto L_0x005c
            L_0x0056:
                r2 = move-exception
                throw r2     // Catch:{ all -> 0x0058 }
            L_0x0058:
                r5 = move-exception
                r6 = r5
                r5 = r2
                r2 = r6
            L_0x005c:
                if (r5 == 0) goto L_0x0062
                r4.close()     // Catch:{ Throwable -> 0x0065 }
                goto L_0x0065
            L_0x0062:
                r4.close()     // Catch:{ Throwable -> 0x0068 }
            L_0x0065:
                throw r2     // Catch:{ Throwable -> 0x0068 }
            L_0x0066:
                r2 = move-exception
                goto L_0x006b
            L_0x0068:
                r2 = move-exception
                r3 = r2
                throw r3     // Catch:{ all -> 0x0066 }
            L_0x006b:
                if (r1 == 0) goto L_0x0076
                if (r3 == 0) goto L_0x0073
                r1.close()     // Catch:{ Throwable -> 0x0076 }
                goto L_0x0076
            L_0x0073:
                r1.close()     // Catch:{ FileNotFoundException -> 0x009b, IOException -> 0x0077 }
            L_0x0076:
                throw r2     // Catch:{ FileNotFoundException -> 0x009b, IOException -> 0x0077 }
            L_0x0077:
                r8 = move-exception
                java.lang.String r0 = "ResourceExtractor"
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                r1.<init>()
                java.lang.String r2 = "Exception unpacking resources: "
                r1.append(r2)
                java.lang.String r8 = r8.getMessage()
                r1.append(r8)
                java.lang.String r8 = r1.toString()
                android.util.Log.w(r0, r8)
                java.lang.String r8 = r7.mDataDirPath
                java.util.HashSet<java.lang.String> r0 = r7.mResources
                io.flutter.embedding.engine.loader.ResourceExtractor.deleteFiles(r8, r0)
                r8 = 0
                return r8
            L_0x009b:
                goto L_0x0006
            L_0x009e:
                r8 = 1
                return r8
            */
            throw new UnsupportedOperationException("Method not decompiled: io.flutter.embedding.engine.loader.ResourceExtractor.ExtractTask.extractAPK(java.io.File):boolean");
        }
    }

    ResourceExtractor(@NonNull String str, @NonNull String str2, @NonNull PackageManager packageManager, @NonNull AssetManager assetManager) {
        this.mDataDirPath = str;
        this.mPackageName = str2;
        this.mPackageManager = packageManager;
        this.mAssetManager = assetManager;
    }

    /* access modifiers changed from: package-private */
    public ResourceExtractor addResource(@NonNull String str) {
        this.mResources.add(str);
        return this;
    }

    /* access modifiers changed from: package-private */
    public ResourceExtractor addResources(@NonNull Collection<String> collection) {
        this.mResources.addAll(collection);
        return this;
    }

    /* access modifiers changed from: package-private */
    public ResourceExtractor start() {
        this.mExtractTask = new ExtractTask(this.mDataDirPath, this.mResources, this.mPackageName, this.mPackageManager, this.mAssetManager);
        this.mExtractTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        return this;
    }

    /* access modifiers changed from: package-private */
    public void waitForCompletion() {
        if (this.mExtractTask != null) {
            try {
                this.mExtractTask.get();
            } catch (InterruptedException | CancellationException | ExecutionException unused) {
                deleteFiles(this.mDataDirPath, this.mResources);
            }
        }
    }

    private static String[] getExistingTimestamps(File file) {
        return file.list(new FilenameFilter() {
            public boolean accept(File file, String str) {
                return str.startsWith(ResourceExtractor.TIMESTAMP_PREFIX);
            }
        });
    }

    /* access modifiers changed from: private */
    public static void deleteFiles(@NonNull String str, @NonNull HashSet<String> hashSet) {
        File file = new File(str);
        Iterator<String> it = hashSet.iterator();
        while (it.hasNext()) {
            File file2 = new File(file, it.next());
            if (file2.exists()) {
                file2.delete();
            }
        }
        String[] existingTimestamps = getExistingTimestamps(file);
        if (existingTimestamps != null) {
            for (String file3 : existingTimestamps) {
                new File(file, file3).delete();
            }
        }
    }

    /* access modifiers changed from: private */
    public static String checkTimestamp(@NonNull File file, @NonNull PackageManager packageManager, @NonNull String str) {
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(str, 0);
            if (packageInfo == null) {
                return TIMESTAMP_PREFIX;
            }
            String str2 = TIMESTAMP_PREFIX + getVersionCode(packageInfo) + "-" + packageInfo.lastUpdateTime;
            String[] existingTimestamps = getExistingTimestamps(file);
            if (existingTimestamps == null) {
                return str2;
            }
            int length = existingTimestamps.length;
            if (existingTimestamps.length != 1 || !str2.equals(existingTimestamps[0])) {
                return str2;
            }
            return null;
        } catch (PackageManager.NameNotFoundException unused) {
            return TIMESTAMP_PREFIX;
        }
    }

    /* access modifiers changed from: private */
    public static void copy(@NonNull InputStream inputStream, @NonNull OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[16384];
        while (true) {
            int read = inputStream.read(bArr);
            if (read >= 0) {
                outputStream.write(bArr, 0, read);
            } else {
                return;
            }
        }
    }

    private static String[] getSupportedAbis() {
        if (Build.VERSION.SDK_INT >= 21) {
            return Build.SUPPORTED_ABIS;
        }
        ArrayList arrayList = new ArrayList(Arrays.asList(new String[]{Build.CPU_ABI, Build.CPU_ABI2}));
        arrayList.removeAll(Arrays.asList(new String[]{null, ""}));
        return (String[]) arrayList.toArray(new String[0]);
    }
}
