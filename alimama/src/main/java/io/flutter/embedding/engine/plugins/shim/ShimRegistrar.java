package io.flutter.embedding.engine.plugins.shim;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import io.flutter.Log;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.platform.PlatformViewRegistry;
import io.flutter.view.FlutterMain;
import io.flutter.view.FlutterNativeView;
import io.flutter.view.FlutterView;
import io.flutter.view.TextureRegistry;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class ShimRegistrar implements PluginRegistry.Registrar, FlutterPlugin, ActivityAware {
    private static final String TAG = "ShimRegistrar";
    private ActivityPluginBinding activityPluginBinding;
    private final Set<PluginRegistry.ActivityResultListener> activityResultListeners = new HashSet();
    private final Map<String, Object> globalRegistrarMap;
    private final Set<PluginRegistry.NewIntentListener> newIntentListeners = new HashSet();
    private FlutterPlugin.FlutterPluginBinding pluginBinding;
    private final String pluginId;
    private final Set<PluginRegistry.RequestPermissionsResultListener> requestPermissionsResultListeners = new HashSet();
    private final Set<PluginRegistry.UserLeaveHintListener> userLeaveHintListeners = new HashSet();
    private final Set<PluginRegistry.ViewDestroyListener> viewDestroyListeners = new HashSet();

    public ShimRegistrar(@NonNull String str, @NonNull Map<String, Object> map) {
        this.pluginId = str;
        this.globalRegistrarMap = map;
    }

    public Activity activity() {
        if (this.activityPluginBinding != null) {
            return this.activityPluginBinding.getActivity();
        }
        return null;
    }

    public Context context() {
        if (this.pluginBinding != null) {
            return this.pluginBinding.getApplicationContext();
        }
        return null;
    }

    public Context activeContext() {
        return this.activityPluginBinding == null ? context() : activity();
    }

    public BinaryMessenger messenger() {
        if (this.pluginBinding != null) {
            return this.pluginBinding.getBinaryMessenger();
        }
        return null;
    }

    public TextureRegistry textures() {
        if (this.pluginBinding != null) {
            return this.pluginBinding.getTextureRegistry();
        }
        return null;
    }

    public PlatformViewRegistry platformViewRegistry() {
        if (this.pluginBinding != null) {
            return this.pluginBinding.getPlatformViewRegistry();
        }
        return null;
    }

    public FlutterView view() {
        throw new UnsupportedOperationException("The new embedding does not support the old FlutterView.");
    }

    public String lookupKeyForAsset(String str) {
        return FlutterMain.getLookupKeyForAsset(str);
    }

    public String lookupKeyForAsset(String str, String str2) {
        return FlutterMain.getLookupKeyForAsset(str, str2);
    }

    public PluginRegistry.Registrar publish(Object obj) {
        this.globalRegistrarMap.put(this.pluginId, obj);
        return this;
    }

    public PluginRegistry.Registrar addRequestPermissionsResultListener(PluginRegistry.RequestPermissionsResultListener requestPermissionsResultListener) {
        this.requestPermissionsResultListeners.add(requestPermissionsResultListener);
        if (this.activityPluginBinding != null) {
            this.activityPluginBinding.addRequestPermissionsResultListener(requestPermissionsResultListener);
        }
        return this;
    }

    public PluginRegistry.Registrar addActivityResultListener(PluginRegistry.ActivityResultListener activityResultListener) {
        this.activityResultListeners.add(activityResultListener);
        if (this.activityPluginBinding != null) {
            this.activityPluginBinding.addActivityResultListener(activityResultListener);
        }
        return this;
    }

    public PluginRegistry.Registrar addNewIntentListener(PluginRegistry.NewIntentListener newIntentListener) {
        this.newIntentListeners.add(newIntentListener);
        if (this.activityPluginBinding != null) {
            this.activityPluginBinding.addOnNewIntentListener(newIntentListener);
        }
        return this;
    }

    public PluginRegistry.Registrar addUserLeaveHintListener(PluginRegistry.UserLeaveHintListener userLeaveHintListener) {
        this.userLeaveHintListeners.add(userLeaveHintListener);
        if (this.activityPluginBinding != null) {
            this.activityPluginBinding.addOnUserLeaveHintListener(userLeaveHintListener);
        }
        return this;
    }

    @NonNull
    public PluginRegistry.Registrar addViewDestroyListener(@NonNull PluginRegistry.ViewDestroyListener viewDestroyListener) {
        this.viewDestroyListeners.add(viewDestroyListener);
        return this;
    }

    public void onAttachedToEngine(@NonNull FlutterPlugin.FlutterPluginBinding flutterPluginBinding) {
        Log.v(TAG, "Attached to FlutterEngine.");
        this.pluginBinding = flutterPluginBinding;
    }

    public void onDetachedFromEngine(@NonNull FlutterPlugin.FlutterPluginBinding flutterPluginBinding) {
        Log.v(TAG, "Detached from FlutterEngine.");
        for (PluginRegistry.ViewDestroyListener onViewDestroy : this.viewDestroyListeners) {
            onViewDestroy.onViewDestroy((FlutterNativeView) null);
        }
        this.pluginBinding = null;
        this.activityPluginBinding = null;
    }

    public void onAttachedToActivity(@NonNull ActivityPluginBinding activityPluginBinding2) {
        Log.v(TAG, "Attached to an Activity.");
        this.activityPluginBinding = activityPluginBinding2;
        addExistingListenersToActivityPluginBinding();
    }

    public void onDetachedFromActivityForConfigChanges() {
        Log.v(TAG, "Detached from an Activity for config changes.");
        this.activityPluginBinding = null;
    }

    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding activityPluginBinding2) {
        Log.v(TAG, "Reconnected to an Activity after config changes.");
        this.activityPluginBinding = activityPluginBinding2;
        addExistingListenersToActivityPluginBinding();
    }

    public void onDetachedFromActivity() {
        Log.v(TAG, "Detached from an Activity.");
        this.activityPluginBinding = null;
    }

    private void addExistingListenersToActivityPluginBinding() {
        for (PluginRegistry.RequestPermissionsResultListener addRequestPermissionsResultListener : this.requestPermissionsResultListeners) {
            this.activityPluginBinding.addRequestPermissionsResultListener(addRequestPermissionsResultListener);
        }
        for (PluginRegistry.ActivityResultListener addActivityResultListener : this.activityResultListeners) {
            this.activityPluginBinding.addActivityResultListener(addActivityResultListener);
        }
        for (PluginRegistry.NewIntentListener addOnNewIntentListener : this.newIntentListeners) {
            this.activityPluginBinding.addOnNewIntentListener(addOnNewIntentListener);
        }
        for (PluginRegistry.UserLeaveHintListener addOnUserLeaveHintListener : this.userLeaveHintListeners) {
            this.activityPluginBinding.addOnUserLeaveHintListener(addOnUserLeaveHintListener);
        }
    }
}
