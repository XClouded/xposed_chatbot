package io.flutter.embedding.engine.loader;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.flutter.embedding.engine.FlutterJNI;
import io.flutter.util.PathUtils;
import io.flutter.view.VsyncWaiter;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class FlutterLoader {
    private static final String AOT_SHARED_LIBRARY_NAME = "aot-shared-library-name";
    private static final String DEFAULT_AOT_SHARED_LIBRARY_NAME = "libapp.so";
    private static final String DEFAULT_FLUTTER_ASSETS_DIR = "flutter_assets";
    private static final String DEFAULT_ISOLATE_SNAPSHOT_DATA = "isolate_snapshot_data";
    private static final String DEFAULT_KERNEL_BLOB = "kernel_blob.bin";
    private static final String DEFAULT_LIBRARY = "libflutter.so";
    private static final String DEFAULT_VM_SNAPSHOT_DATA = "vm_snapshot_data";
    private static final String FLUTTER_ASSETS_DIR_KEY = "flutter-assets-dir";
    private static final String ISOLATE_SNAPSHOT_DATA_KEY = "isolate-snapshot-data";
    private static final String PUBLIC_AOT_SHARED_LIBRARY_NAME = (FlutterLoader.class.getName() + '.' + AOT_SHARED_LIBRARY_NAME);
    private static final String PUBLIC_FLUTTER_ASSETS_DIR_KEY = (FlutterLoader.class.getName() + '.' + FLUTTER_ASSETS_DIR_KEY);
    private static final String PUBLIC_ISOLATE_SNAPSHOT_DATA_KEY = (FlutterLoader.class.getName() + '.' + ISOLATE_SNAPSHOT_DATA_KEY);
    private static final String PUBLIC_VM_SNAPSHOT_DATA_KEY = (FlutterLoader.class.getName() + '.' + VM_SNAPSHOT_DATA_KEY);
    private static final String SNAPSHOT_ASSET_PATH_KEY = "snapshot-asset-path";
    private static final String TAG = "FlutterLoader";
    private static final String VM_SNAPSHOT_DATA_KEY = "vm-snapshot-data";
    private static FlutterLoader instance;
    private String aotSharedLibraryName = DEFAULT_AOT_SHARED_LIBRARY_NAME;
    private String flutterAssetsDir = DEFAULT_FLUTTER_ASSETS_DIR;
    private boolean initialized = false;
    private String isolateSnapshotData = DEFAULT_ISOLATE_SNAPSHOT_DATA;
    /* access modifiers changed from: private */
    @Nullable
    public ResourceExtractor resourceExtractor;
    @Nullable
    private Settings settings;
    private String vmSnapshotData = DEFAULT_VM_SNAPSHOT_DATA;

    @NonNull
    public static FlutterLoader getInstance() {
        if (instance == null) {
            instance = new FlutterLoader();
        }
        return instance;
    }

    public void startInitialization(@NonNull Context context) {
        startInitialization(context, new Settings());
    }

    public void startInitialization(@NonNull Context context, @NonNull Settings settings2) {
        if (this.settings == null) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                Context applicationContext = context.getApplicationContext();
                this.settings = settings2;
                long uptimeMillis = SystemClock.uptimeMillis();
                initConfig(applicationContext);
                initResources(applicationContext);
                System.loadLibrary("flutter");
                VsyncWaiter.getInstance((WindowManager) applicationContext.getSystemService("window")).init();
                FlutterJNI.nativeRecordStartTimestamp(SystemClock.uptimeMillis() - uptimeMillis);
                return;
            }
            throw new IllegalStateException("startInitialization must be called on the main thread");
        }
    }

    public void ensureInitializationComplete(@NonNull Context context, @Nullable String[] strArr) {
        if (!this.initialized) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                throw new IllegalStateException("ensureInitializationComplete must be called on the main thread");
            } else if (this.settings != null) {
                try {
                    if (this.resourceExtractor != null) {
                        this.resourceExtractor.waitForCompletion();
                    }
                    ArrayList arrayList = new ArrayList();
                    arrayList.add("--icu-symbol-prefix=_binary_icudtl_dat");
                    ApplicationInfo applicationInfo = getApplicationInfo(context);
                    arrayList.add("--icu-native-lib-path=" + applicationInfo.nativeLibraryDir + File.separator + DEFAULT_LIBRARY);
                    if (strArr != null) {
                        Collections.addAll(arrayList, strArr);
                    }
                    arrayList.add("--aot-shared-library-name=" + this.aotSharedLibraryName);
                    arrayList.add("--aot-shared-library-name=" + applicationInfo.nativeLibraryDir + File.separator + this.aotSharedLibraryName);
                    StringBuilder sb = new StringBuilder();
                    sb.append("--cache-dir-path=");
                    sb.append(PathUtils.getCacheDirectory(context));
                    arrayList.add(sb.toString());
                    if (this.settings.getLogTag() != null) {
                        arrayList.add("--log-tag=" + this.settings.getLogTag());
                    }
                    FlutterJNI.nativeInit(context, (String[]) arrayList.toArray(new String[0]), (String) null, PathUtils.getFilesDir(context), PathUtils.getCacheDirectory(context));
                    this.initialized = true;
                } catch (Exception e) {
                    Log.e(TAG, "Flutter initialization failed.", e);
                    throw new RuntimeException(e);
                }
            } else {
                throw new IllegalStateException("ensureInitializationComplete must be called after startInitialization");
            }
        }
    }

    public void ensureInitializationCompleteAsync(@NonNull Context context, @Nullable String[] strArr, @NonNull Handler handler, @NonNull Runnable runnable) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("ensureInitializationComplete must be called on the main thread");
        } else if (this.settings == null) {
            throw new IllegalStateException("ensureInitializationComplete must be called after startInitialization");
        } else if (this.initialized) {
            handler.post(runnable);
        } else {
            final Context context2 = context;
            final String[] strArr2 = strArr;
            final Handler handler2 = handler;
            final Runnable runnable2 = runnable;
            new Thread(new Runnable() {
                public void run() {
                    if (FlutterLoader.this.resourceExtractor != null) {
                        FlutterLoader.this.resourceExtractor.waitForCompletion();
                    }
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        public void run() {
                            FlutterLoader.this.ensureInitializationComplete(context2.getApplicationContext(), strArr2);
                            handler2.post(runnable2);
                        }
                    });
                }
            }).start();
        }
    }

    @NonNull
    private ApplicationInfo getApplicationInfo(@NonNull Context context) {
        try {
            return context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void initConfig(@NonNull Context context) {
        Bundle bundle = getApplicationInfo(context).metaData;
        if (bundle != null) {
            this.aotSharedLibraryName = bundle.getString(PUBLIC_AOT_SHARED_LIBRARY_NAME, DEFAULT_AOT_SHARED_LIBRARY_NAME);
            this.flutterAssetsDir = bundle.getString(PUBLIC_FLUTTER_ASSETS_DIR_KEY, DEFAULT_FLUTTER_ASSETS_DIR);
            this.vmSnapshotData = bundle.getString(PUBLIC_VM_SNAPSHOT_DATA_KEY, DEFAULT_VM_SNAPSHOT_DATA);
            this.isolateSnapshotData = bundle.getString(PUBLIC_ISOLATE_SNAPSHOT_DATA_KEY, DEFAULT_ISOLATE_SNAPSHOT_DATA);
        }
    }

    private void initResources(@NonNull Context context) {
        new ResourceCleaner(context).start();
    }

    @NonNull
    public String findAppBundlePath() {
        return this.flutterAssetsDir;
    }

    @NonNull
    public String getLookupKeyForAsset(@NonNull String str) {
        return fullAssetPathFrom(str);
    }

    @NonNull
    public String getLookupKeyForAsset(@NonNull String str, @NonNull String str2) {
        return getLookupKeyForAsset("packages" + File.separator + str2 + File.separator + str);
    }

    @NonNull
    private String fullAssetPathFrom(@NonNull String str) {
        return this.flutterAssetsDir + File.separator + str;
    }

    public static class Settings {
        private String logTag;

        @Nullable
        public String getLogTag() {
            return this.logTag;
        }

        public void setLogTag(String str) {
            this.logTag = str;
        }
    }
}
