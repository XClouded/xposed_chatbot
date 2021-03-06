package io.flutter.embedding.engine;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.flutter.Log;
import io.flutter.embedding.engine.dart.DartExecutor;
import io.flutter.embedding.engine.loader.FlutterLoader;
import io.flutter.embedding.engine.plugins.PluginRegistry;
import io.flutter.embedding.engine.plugins.activity.ActivityControlSurface;
import io.flutter.embedding.engine.plugins.broadcastreceiver.BroadcastReceiverControlSurface;
import io.flutter.embedding.engine.plugins.contentprovider.ContentProviderControlSurface;
import io.flutter.embedding.engine.plugins.service.ServiceControlSurface;
import io.flutter.embedding.engine.renderer.FlutterRenderer;
import io.flutter.embedding.engine.systemchannels.AccessibilityChannel;
import io.flutter.embedding.engine.systemchannels.KeyEventChannel;
import io.flutter.embedding.engine.systemchannels.LifecycleChannel;
import io.flutter.embedding.engine.systemchannels.LocalizationChannel;
import io.flutter.embedding.engine.systemchannels.NavigationChannel;
import io.flutter.embedding.engine.systemchannels.PlatformChannel;
import io.flutter.embedding.engine.systemchannels.SettingsChannel;
import io.flutter.embedding.engine.systemchannels.SystemChannel;
import io.flutter.embedding.engine.systemchannels.TextInputChannel;
import io.flutter.plugin.external_adapter_image.ExternalAdapterImageProvider;
import io.flutter.plugin.platform.PlatformViewsController;
import java.util.HashSet;
import java.util.Set;

public class FlutterEngine {
    private static final String TAG = "FlutterEngine";
    @NonNull
    private final AccessibilityChannel accessibilityChannel;
    @NonNull
    private final DartExecutor dartExecutor;
    @NonNull
    private final EngineLifecycleListener engineLifecycleListener;
    /* access modifiers changed from: private */
    @NonNull
    public final Set<EngineLifecycleListener> engineLifecycleListeners;
    @NonNull
    private final FlutterJNI flutterJNI;
    @NonNull
    private final KeyEventChannel keyEventChannel;
    @NonNull
    private final LifecycleChannel lifecycleChannel;
    @NonNull
    private final LocalizationChannel localizationChannel;
    @NonNull
    private final NavigationChannel navigationChannel;
    @NonNull
    private final PlatformChannel platformChannel;
    /* access modifiers changed from: private */
    @NonNull
    public final PlatformViewsController platformViewsController;
    @NonNull
    private final FlutterEnginePluginRegistry pluginRegistry;
    @NonNull
    private final FlutterRenderer renderer;
    @NonNull
    private final SettingsChannel settingsChannel;
    @NonNull
    private final SystemChannel systemChannel;
    @NonNull
    private final TextInputChannel textInputChannel;

    public interface EngineLifecycleListener {
        void onPreEngineRestart();
    }

    public FlutterEngine(@NonNull Context context) {
        this(context, (String[]) null);
    }

    public FlutterEngine(@NonNull Context context, @Nullable String[] strArr) {
        this(context, FlutterLoader.getInstance(), new FlutterJNI(), strArr, true);
    }

    public FlutterEngine(@NonNull Context context, @Nullable String[] strArr, boolean z) {
        this(context, FlutterLoader.getInstance(), new FlutterJNI(), strArr, z);
    }

    public FlutterEngine(@NonNull Context context, @NonNull FlutterLoader flutterLoader, @NonNull FlutterJNI flutterJNI2) {
        this(context, flutterLoader, flutterJNI2, (String[]) null, true);
    }

    public FlutterEngine(@NonNull Context context, @NonNull FlutterLoader flutterLoader, @NonNull FlutterJNI flutterJNI2, @Nullable String[] strArr, boolean z) {
        this(context, flutterLoader, flutterJNI2, new PlatformViewsController(), strArr, z);
    }

    public FlutterEngine(@NonNull Context context, @NonNull FlutterLoader flutterLoader, @NonNull FlutterJNI flutterJNI2, @NonNull PlatformViewsController platformViewsController2, @Nullable String[] strArr, boolean z) {
        this.engineLifecycleListeners = new HashSet();
        this.engineLifecycleListener = new EngineLifecycleListener() {
            public void onPreEngineRestart() {
                Log.v(FlutterEngine.TAG, "onPreEngineRestart()");
                for (EngineLifecycleListener onPreEngineRestart : FlutterEngine.this.engineLifecycleListeners) {
                    onPreEngineRestart.onPreEngineRestart();
                }
                FlutterEngine.this.platformViewsController.onPreEngineRestart();
            }
        };
        this.flutterJNI = flutterJNI2;
        flutterLoader.startInitialization(context.getApplicationContext());
        flutterLoader.ensureInitializationComplete(context, strArr);
        flutterJNI2.addEngineLifecycleListener(this.engineLifecycleListener);
        attachToJni();
        this.dartExecutor = new DartExecutor(flutterJNI2, context.getAssets());
        this.dartExecutor.onAttachedToJNI();
        this.renderer = new FlutterRenderer(flutterJNI2);
        this.accessibilityChannel = new AccessibilityChannel(this.dartExecutor, flutterJNI2);
        this.keyEventChannel = new KeyEventChannel(this.dartExecutor);
        this.lifecycleChannel = new LifecycleChannel(this.dartExecutor);
        this.localizationChannel = new LocalizationChannel(this.dartExecutor);
        this.navigationChannel = new NavigationChannel(this.dartExecutor);
        this.platformChannel = new PlatformChannel(this.dartExecutor);
        this.settingsChannel = new SettingsChannel(this.dartExecutor);
        this.systemChannel = new SystemChannel(this.dartExecutor);
        this.textInputChannel = new TextInputChannel(this.dartExecutor);
        this.platformViewsController = platformViewsController2;
        this.pluginRegistry = new FlutterEnginePluginRegistry(context.getApplicationContext(), this, flutterLoader);
        if (z) {
            registerPlugins();
        }
    }

    private void attachToJni() {
        Log.v(TAG, "Attaching to JNI.");
        this.flutterJNI.attachToNative(false);
        if (!isAttachedToJni()) {
            throw new RuntimeException("FlutterEngine failed to attach to its native Object reference.");
        }
    }

    private boolean isAttachedToJni() {
        return this.flutterJNI.isAttached();
    }

    private void registerPlugins() {
        try {
            Class.forName("io.flutter.plugins.GeneratedPluginRegistrant").getDeclaredMethod("registerWith", new Class[]{FlutterEngine.class}).invoke((Object) null, new Object[]{this});
        } catch (Exception unused) {
            Log.w(TAG, "Tried to automatically register plugins with FlutterEngine (" + this + ") but could not find and invoke the GeneratedPluginRegistrant.");
        }
    }

    public void destroy() {
        Log.v(TAG, "Destroying.");
        this.pluginRegistry.destroy();
        this.dartExecutor.onDetachedFromJNI();
        this.flutterJNI.removeEngineLifecycleListener(this.engineLifecycleListener);
        this.flutterJNI.detachFromNativeAndReleaseResources();
    }

    public void addEngineLifecycleListener(@NonNull EngineLifecycleListener engineLifecycleListener2) {
        this.engineLifecycleListeners.add(engineLifecycleListener2);
    }

    public void removeEngineLifecycleListener(@NonNull EngineLifecycleListener engineLifecycleListener2) {
        this.engineLifecycleListeners.remove(engineLifecycleListener2);
    }

    @NonNull
    public DartExecutor getDartExecutor() {
        return this.dartExecutor;
    }

    @NonNull
    public FlutterRenderer getRenderer() {
        return this.renderer;
    }

    @NonNull
    public AccessibilityChannel getAccessibilityChannel() {
        return this.accessibilityChannel;
    }

    @NonNull
    public KeyEventChannel getKeyEventChannel() {
        return this.keyEventChannel;
    }

    @NonNull
    public LifecycleChannel getLifecycleChannel() {
        return this.lifecycleChannel;
    }

    @NonNull
    public LocalizationChannel getLocalizationChannel() {
        return this.localizationChannel;
    }

    @NonNull
    public NavigationChannel getNavigationChannel() {
        return this.navigationChannel;
    }

    @NonNull
    public PlatformChannel getPlatformChannel() {
        return this.platformChannel;
    }

    @NonNull
    public SettingsChannel getSettingsChannel() {
        return this.settingsChannel;
    }

    @NonNull
    public SystemChannel getSystemChannel() {
        return this.systemChannel;
    }

    @NonNull
    public TextInputChannel getTextInputChannel() {
        return this.textInputChannel;
    }

    @NonNull
    public PluginRegistry getPlugins() {
        return this.pluginRegistry;
    }

    @NonNull
    public PlatformViewsController getPlatformViewsController() {
        return this.platformViewsController;
    }

    @NonNull
    public ActivityControlSurface getActivityControlSurface() {
        return this.pluginRegistry;
    }

    @NonNull
    public ServiceControlSurface getServiceControlSurface() {
        return this.pluginRegistry;
    }

    @NonNull
    public BroadcastReceiverControlSurface getBroadcastReceiverControlSurface() {
        return this.pluginRegistry;
    }

    @NonNull
    public ContentProviderControlSurface getContentProviderControlSurface() {
        return this.pluginRegistry;
    }

    public static void installExternalAdapterImageProvider(@NonNull ExternalAdapterImageProvider externalAdapterImageProvider) {
        FlutterJNI.installExternalAdapterImageProvider(externalAdapterImageProvider);
    }
}
