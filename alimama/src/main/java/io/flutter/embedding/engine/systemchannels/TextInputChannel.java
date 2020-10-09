package io.flutter.embedding.engine.systemchannels;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.flutter.Log;
import io.flutter.embedding.engine.dart.DartExecutor;
import io.flutter.plugin.common.JSONMethodCodec;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TextInputChannel {
    private static final String TAG = "TextInputChannel";
    @NonNull
    public final MethodChannel channel;
    private final MethodChannel.MethodCallHandler parsingMethodHandler = new MethodChannel.MethodCallHandler() {
        public void onMethodCall(@NonNull MethodCall methodCall, @NonNull MethodChannel.Result result) {
            if (TextInputChannel.this.textInputMethodHandler != null) {
                String str = methodCall.method;
                Object obj = methodCall.arguments;
                Log.v(TextInputChannel.TAG, "Received '" + str + "' message.");
                char c = 65535;
                switch (str.hashCode()) {
                    case -1779068172:
                        if (str.equals("TextInput.setPlatformViewClient")) {
                            c = 3;
                            break;
                        }
                        break;
                    case -1015421462:
                        if (str.equals("TextInput.setEditingState")) {
                            c = 4;
                            break;
                        }
                        break;
                    case -37561188:
                        if (str.equals("TextInput.setClient")) {
                            c = 2;
                            break;
                        }
                        break;
                    case 270476819:
                        if (str.equals("TextInput.hide")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 270803918:
                        if (str.equals("TextInput.show")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 1904427655:
                        if (str.equals("TextInput.clearClient")) {
                            c = 5;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        TextInputChannel.this.textInputMethodHandler.show();
                        result.success((Object) null);
                        return;
                    case 1:
                        TextInputChannel.this.textInputMethodHandler.hide();
                        result.success((Object) null);
                        return;
                    case 2:
                        try {
                            JSONArray jSONArray = (JSONArray) obj;
                            TextInputChannel.this.textInputMethodHandler.setClient(jSONArray.getInt(0), Configuration.fromJson(jSONArray.getJSONObject(1)));
                            result.success((Object) null);
                            return;
                        } catch (NoSuchFieldException | JSONException e) {
                            result.error("error", e.getMessage(), (Object) null);
                            return;
                        }
                    case 3:
                        TextInputChannel.this.textInputMethodHandler.setPlatformViewClient(((Integer) obj).intValue());
                        return;
                    case 4:
                        try {
                            TextInputChannel.this.textInputMethodHandler.setEditingState(TextEditState.fromJson((JSONObject) obj));
                            result.success((Object) null);
                            return;
                        } catch (JSONException e2) {
                            result.error("error", e2.getMessage(), (Object) null);
                            return;
                        }
                    case 5:
                        TextInputChannel.this.textInputMethodHandler.clearClient();
                        result.success((Object) null);
                        return;
                    default:
                        result.notImplemented();
                        return;
                }
            }
        }
    };
    /* access modifiers changed from: private */
    @Nullable
    public TextInputMethodHandler textInputMethodHandler;

    public interface TextInputMethodHandler {
        void clearClient();

        void hide();

        void setClient(int i, @NonNull Configuration configuration);

        void setEditingState(@NonNull TextEditState textEditState);

        void setPlatformViewClient(int i);

        void show();
    }

    public TextInputChannel(@NonNull DartExecutor dartExecutor) {
        this.channel = new MethodChannel(dartExecutor, "flutter/textinput", JSONMethodCodec.INSTANCE);
        this.channel.setMethodCallHandler(this.parsingMethodHandler);
    }

    public void requestExistingInputState() {
        this.channel.invokeMethod("TextInputClient.requestExistingInputState", (Object) null);
    }

    public void updateEditingState(int i, String str, int i2, int i3, int i4, int i5) {
        Log.v(TAG, "Sending message to update editing state: \nText: " + str + "\nSelection start: " + i2 + "\nSelection end: " + i3 + "\nComposing start: " + i4 + "\nComposing end: " + i5);
        HashMap hashMap = new HashMap();
        hashMap.put("text", str);
        hashMap.put("selectionBase", Integer.valueOf(i2));
        hashMap.put("selectionExtent", Integer.valueOf(i3));
        hashMap.put("composingBase", Integer.valueOf(i4));
        hashMap.put("composingExtent", Integer.valueOf(i5));
        this.channel.invokeMethod("TextInputClient.updateEditingState", Arrays.asList(new Serializable[]{Integer.valueOf(i), hashMap}));
    }

    public void newline(int i) {
        Log.v(TAG, "Sending 'newline' message.");
        this.channel.invokeMethod("TextInputClient.performAction", Arrays.asList(new Serializable[]{Integer.valueOf(i), "TextInputAction.newline"}));
    }

    public void go(int i) {
        Log.v(TAG, "Sending 'go' message.");
        this.channel.invokeMethod("TextInputClient.performAction", Arrays.asList(new Serializable[]{Integer.valueOf(i), "TextInputAction.go"}));
    }

    public void search(int i) {
        Log.v(TAG, "Sending 'search' message.");
        this.channel.invokeMethod("TextInputClient.performAction", Arrays.asList(new Serializable[]{Integer.valueOf(i), "TextInputAction.search"}));
    }

    public void send(int i) {
        Log.v(TAG, "Sending 'send' message.");
        this.channel.invokeMethod("TextInputClient.performAction", Arrays.asList(new Serializable[]{Integer.valueOf(i), "TextInputAction.send"}));
    }

    public void done(int i) {
        Log.v(TAG, "Sending 'done' message.");
        this.channel.invokeMethod("TextInputClient.performAction", Arrays.asList(new Serializable[]{Integer.valueOf(i), "TextInputAction.done"}));
    }

    public void next(int i) {
        Log.v(TAG, "Sending 'next' message.");
        this.channel.invokeMethod("TextInputClient.performAction", Arrays.asList(new Serializable[]{Integer.valueOf(i), "TextInputAction.next"}));
    }

    public void previous(int i) {
        Log.v(TAG, "Sending 'previous' message.");
        this.channel.invokeMethod("TextInputClient.performAction", Arrays.asList(new Serializable[]{Integer.valueOf(i), "TextInputAction.previous"}));
    }

    public void unspecifiedAction(int i) {
        Log.v(TAG, "Sending 'unspecified' message.");
        this.channel.invokeMethod("TextInputClient.performAction", Arrays.asList(new Serializable[]{Integer.valueOf(i), "TextInputAction.unspecified"}));
    }

    public void setTextInputMethodHandler(@Nullable TextInputMethodHandler textInputMethodHandler2) {
        this.textInputMethodHandler = textInputMethodHandler2;
    }

    public static class Configuration {
        @Nullable
        public final String actionLabel;
        public final boolean autocorrect;
        public final boolean enableSuggestions;
        @Nullable
        public final Integer inputAction;
        @NonNull
        public final InputType inputType;
        public final boolean obscureText;
        @NonNull
        public final TextCapitalization textCapitalization;

        public static Configuration fromJson(@NonNull JSONObject jSONObject) throws JSONException, NoSuchFieldException {
            String string = jSONObject.getString("inputAction");
            if (string != null) {
                return new Configuration(jSONObject.optBoolean("obscureText"), jSONObject.optBoolean("autocorrect", true), jSONObject.optBoolean("enableSuggestions"), TextCapitalization.fromValue(jSONObject.getString("textCapitalization")), InputType.fromJson(jSONObject.getJSONObject("inputType")), inputActionFromTextInputAction(string), jSONObject.isNull("actionLabel") ? null : jSONObject.getString("actionLabel"));
            }
            throw new JSONException("Configuration JSON missing 'inputAction' property.");
        }

        /* JADX WARNING: Can't fix incorrect switch cases order */
        @androidx.annotation.NonNull
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private static java.lang.Integer inputActionFromTextInputAction(@androidx.annotation.NonNull java.lang.String r9) {
            /*
                int r0 = r9.hashCode()
                r1 = 7
                r2 = 5
                r3 = 4
                r4 = 3
                r5 = 2
                r6 = 6
                r7 = 1
                r8 = 0
                switch(r0) {
                    case -810971940: goto L_0x0061;
                    case -737377923: goto L_0x0057;
                    case -737089298: goto L_0x004d;
                    case -737080013: goto L_0x0043;
                    case -736940669: goto L_0x0039;
                    case 469250275: goto L_0x002f;
                    case 1241689507: goto L_0x0025;
                    case 1539450297: goto L_0x001b;
                    case 2110497650: goto L_0x0010;
                    default: goto L_0x000f;
                }
            L_0x000f:
                goto L_0x006b
            L_0x0010:
                java.lang.String r0 = "TextInputAction.previous"
                boolean r9 = r9.equals(r0)
                if (r9 == 0) goto L_0x006b
                r9 = 8
                goto L_0x006c
            L_0x001b:
                java.lang.String r0 = "TextInputAction.newline"
                boolean r9 = r9.equals(r0)
                if (r9 == 0) goto L_0x006b
                r9 = 0
                goto L_0x006c
            L_0x0025:
                java.lang.String r0 = "TextInputAction.go"
                boolean r9 = r9.equals(r0)
                if (r9 == 0) goto L_0x006b
                r9 = 4
                goto L_0x006c
            L_0x002f:
                java.lang.String r0 = "TextInputAction.search"
                boolean r9 = r9.equals(r0)
                if (r9 == 0) goto L_0x006b
                r9 = 5
                goto L_0x006c
            L_0x0039:
                java.lang.String r0 = "TextInputAction.send"
                boolean r9 = r9.equals(r0)
                if (r9 == 0) goto L_0x006b
                r9 = 6
                goto L_0x006c
            L_0x0043:
                java.lang.String r0 = "TextInputAction.none"
                boolean r9 = r9.equals(r0)
                if (r9 == 0) goto L_0x006b
                r9 = 1
                goto L_0x006c
            L_0x004d:
                java.lang.String r0 = "TextInputAction.next"
                boolean r9 = r9.equals(r0)
                if (r9 == 0) goto L_0x006b
                r9 = 7
                goto L_0x006c
            L_0x0057:
                java.lang.String r0 = "TextInputAction.done"
                boolean r9 = r9.equals(r0)
                if (r9 == 0) goto L_0x006b
                r9 = 3
                goto L_0x006c
            L_0x0061:
                java.lang.String r0 = "TextInputAction.unspecified"
                boolean r9 = r9.equals(r0)
                if (r9 == 0) goto L_0x006b
                r9 = 2
                goto L_0x006c
            L_0x006b:
                r9 = -1
            L_0x006c:
                switch(r9) {
                    case 0: goto L_0x009c;
                    case 1: goto L_0x0097;
                    case 2: goto L_0x0092;
                    case 3: goto L_0x008d;
                    case 4: goto L_0x0088;
                    case 5: goto L_0x0083;
                    case 6: goto L_0x007e;
                    case 7: goto L_0x0079;
                    case 8: goto L_0x0074;
                    default: goto L_0x006f;
                }
            L_0x006f:
                java.lang.Integer r9 = java.lang.Integer.valueOf(r8)
                return r9
            L_0x0074:
                java.lang.Integer r9 = java.lang.Integer.valueOf(r1)
                return r9
            L_0x0079:
                java.lang.Integer r9 = java.lang.Integer.valueOf(r2)
                return r9
            L_0x007e:
                java.lang.Integer r9 = java.lang.Integer.valueOf(r3)
                return r9
            L_0x0083:
                java.lang.Integer r9 = java.lang.Integer.valueOf(r4)
                return r9
            L_0x0088:
                java.lang.Integer r9 = java.lang.Integer.valueOf(r5)
                return r9
            L_0x008d:
                java.lang.Integer r9 = java.lang.Integer.valueOf(r6)
                return r9
            L_0x0092:
                java.lang.Integer r9 = java.lang.Integer.valueOf(r8)
                return r9
            L_0x0097:
                java.lang.Integer r9 = java.lang.Integer.valueOf(r7)
                return r9
            L_0x009c:
                java.lang.Integer r9 = java.lang.Integer.valueOf(r7)
                return r9
            */
            throw new UnsupportedOperationException("Method not decompiled: io.flutter.embedding.engine.systemchannels.TextInputChannel.Configuration.inputActionFromTextInputAction(java.lang.String):java.lang.Integer");
        }

        public Configuration(boolean z, boolean z2, boolean z3, @NonNull TextCapitalization textCapitalization2, @NonNull InputType inputType2, @Nullable Integer num, @Nullable String str) {
            this.obscureText = z;
            this.autocorrect = z2;
            this.enableSuggestions = z3;
            this.textCapitalization = textCapitalization2;
            this.inputType = inputType2;
            this.inputAction = num;
            this.actionLabel = str;
        }
    }

    public static class InputType {
        public final boolean isDecimal;
        public final boolean isSigned;
        @NonNull
        public final TextInputType type;

        @NonNull
        public static InputType fromJson(@NonNull JSONObject jSONObject) throws JSONException, NoSuchFieldException {
            return new InputType(TextInputType.fromValue(jSONObject.getString("name")), jSONObject.optBoolean("signed", false), jSONObject.optBoolean("decimal", false));
        }

        public InputType(@NonNull TextInputType textInputType, boolean z, boolean z2) {
            this.type = textInputType;
            this.isSigned = z;
            this.isDecimal = z2;
        }
    }

    public enum TextInputType {
        TEXT("TextInputType.text"),
        DATETIME("TextInputType.datetime"),
        NUMBER("TextInputType.number"),
        PHONE("TextInputType.phone"),
        MULTILINE("TextInputType.multiline"),
        EMAIL_ADDRESS("TextInputType.emailAddress"),
        URL("TextInputType.url"),
        VISIBLE_PASSWORD("TextInputType.visiblePassword");
        
        @NonNull
        private final String encodedName;

        static TextInputType fromValue(@NonNull String str) throws NoSuchFieldException {
            for (TextInputType textInputType : values()) {
                if (textInputType.encodedName.equals(str)) {
                    return textInputType;
                }
            }
            throw new NoSuchFieldException("No such TextInputType: " + str);
        }

        private TextInputType(@NonNull String str) {
            this.encodedName = str;
        }
    }

    public enum TextCapitalization {
        CHARACTERS("TextCapitalization.characters"),
        WORDS("TextCapitalization.words"),
        SENTENCES("TextCapitalization.sentences"),
        NONE("TextCapitalization.none");
        
        @NonNull
        private final String encodedName;

        static TextCapitalization fromValue(@NonNull String str) throws NoSuchFieldException {
            for (TextCapitalization textCapitalization : values()) {
                if (textCapitalization.encodedName.equals(str)) {
                    return textCapitalization;
                }
            }
            throw new NoSuchFieldException("No such TextCapitalization: " + str);
        }

        private TextCapitalization(@NonNull String str) {
            this.encodedName = str;
        }
    }

    public static class TextEditState {
        public final int selectionEnd;
        public final int selectionStart;
        @NonNull
        public final String text;

        public static TextEditState fromJson(@NonNull JSONObject jSONObject) throws JSONException {
            return new TextEditState(jSONObject.getString("text"), jSONObject.getInt("selectionBase"), jSONObject.getInt("selectionExtent"));
        }

        public TextEditState(@NonNull String str, int i, int i2) {
            this.text = str;
            this.selectionStart = i;
            this.selectionEnd = i2;
        }
    }
}
