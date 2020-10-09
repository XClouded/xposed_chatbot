package io.flutter.plugin.common;

import androidx.annotation.Nullable;
import java.util.Map;
import org.json.JSONObject;

public final class MethodCall {
    public final Object arguments;
    public final String method;

    public MethodCall(String str, Object obj) {
        this.method = str;
        this.arguments = obj;
    }

    public <T> T arguments() {
        return this.arguments;
    }

    @Nullable
    public <T> T argument(String str) {
        if (this.arguments == null) {
            return null;
        }
        if (this.arguments instanceof Map) {
            return ((Map) this.arguments).get(str);
        }
        if (this.arguments instanceof JSONObject) {
            return ((JSONObject) this.arguments).opt(str);
        }
        throw new ClassCastException();
    }

    public boolean hasArgument(String str) {
        if (this.arguments == null) {
            return false;
        }
        if (this.arguments instanceof Map) {
            return ((Map) this.arguments).containsKey(str);
        }
        if (this.arguments instanceof JSONObject) {
            return ((JSONObject) this.arguments).has(str);
        }
        throw new ClassCastException();
    }
}
