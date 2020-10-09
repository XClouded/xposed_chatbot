package io.flutter.plugins;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import com.taobao.highavail.HighAvailPlugin;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.plugins.shim.ShimPluginRegistry;

@Keep
public final class GeneratedPluginRegistrant {
    public static void registerWith(@NonNull FlutterEngine flutterEngine) {
        HighAvailPlugin.registerWith(new ShimPluginRegistry(flutterEngine).registrarFor("com.taobao.highavail.HighAvailPlugin"));
    }
}
