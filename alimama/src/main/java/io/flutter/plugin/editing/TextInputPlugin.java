package io.flutter.plugin.editing;

import android.annotation.SuppressLint;
import android.os.Build;
import android.provider.Settings;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import com.uc.webview.export.extension.UCExtension;
import io.flutter.embedding.engine.dart.DartExecutor;
import io.flutter.embedding.engine.systemchannels.TextInputChannel;
import io.flutter.plugin.platform.PlatformViewsController;

public class TextInputPlugin {
    @Nullable
    private TextInputChannel.Configuration configuration;
    @NonNull
    private InputTarget inputTarget = new InputTarget(InputTarget.Type.NO_TARGET, 0);
    private boolean isInputConnectionLocked;
    @Nullable
    private InputConnection lastInputConnection;
    @Nullable
    private Editable mEditable;
    @NonNull
    private final InputMethodManager mImm;
    private boolean mRestartInputPending;
    /* access modifiers changed from: private */
    @NonNull
    public final View mView;
    @NonNull
    private PlatformViewsController platformViewsController;
    private final boolean restartAlwaysRequired;
    @NonNull
    private final TextInputChannel textInputChannel;

    public TextInputPlugin(View view, @NonNull DartExecutor dartExecutor, @NonNull PlatformViewsController platformViewsController2) {
        this.mView = view;
        this.mImm = (InputMethodManager) view.getContext().getSystemService("input_method");
        this.textInputChannel = new TextInputChannel(dartExecutor);
        this.textInputChannel.setTextInputMethodHandler(new TextInputChannel.TextInputMethodHandler() {
            public void show() {
                TextInputPlugin.this.showTextInput(TextInputPlugin.this.mView);
            }

            public void hide() {
                TextInputPlugin.this.hideTextInput(TextInputPlugin.this.mView);
            }

            public void setClient(int i, TextInputChannel.Configuration configuration) {
                TextInputPlugin.this.setTextInputClient(i, configuration);
            }

            public void setPlatformViewClient(int i) {
                TextInputPlugin.this.setPlatformViewTextInputClient(i);
            }

            public void setEditingState(TextInputChannel.TextEditState textEditState) {
                TextInputPlugin.this.setTextInputEditingState(TextInputPlugin.this.mView, textEditState);
            }

            public void clearClient() {
                TextInputPlugin.this.clearTextInputClient();
            }
        });
        this.textInputChannel.requestExistingInputState();
        this.platformViewsController = platformViewsController2;
        this.platformViewsController.attachTextInputPlugin(this);
        this.restartAlwaysRequired = isRestartAlwaysRequired();
    }

    @NonNull
    public InputMethodManager getInputMethodManager() {
        return this.mImm;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public Editable getEditable() {
        return this.mEditable;
    }

    public void lockPlatformViewInputConnection() {
        if (this.inputTarget.type == InputTarget.Type.PLATFORM_VIEW) {
            this.isInputConnectionLocked = true;
        }
    }

    public void unlockPlatformViewInputConnection() {
        this.isInputConnectionLocked = false;
    }

    public void destroy() {
        this.platformViewsController.detachTextInputPlugin();
    }

    private static int inputTypeFromTextInputType(TextInputChannel.InputType inputType, boolean z, boolean z2, boolean z3, TextInputChannel.TextCapitalization textCapitalization) {
        int i;
        if (inputType.type == TextInputChannel.TextInputType.DATETIME) {
            return 4;
        }
        if (inputType.type == TextInputChannel.TextInputType.NUMBER) {
            int i2 = 2;
            if (inputType.isSigned) {
                i2 = 4098;
            }
            return inputType.isDecimal ? i2 | 8192 : i2;
        } else if (inputType.type == TextInputChannel.TextInputType.PHONE) {
            return 3;
        } else {
            int i3 = 1;
            if (inputType.type == TextInputChannel.TextInputType.MULTILINE) {
                i3 = 131073;
            } else if (inputType.type == TextInputChannel.TextInputType.EMAIL_ADDRESS) {
                i3 = 33;
            } else if (inputType.type == TextInputChannel.TextInputType.URL) {
                i3 = 17;
            } else if (inputType.type == TextInputChannel.TextInputType.VISIBLE_PASSWORD) {
                i3 = 145;
            }
            if (z) {
                i = 524288 | i3 | 128;
            } else {
                if (z2) {
                    i3 |= 32768;
                }
                i = !z3 ? 524288 | i3 : i3;
            }
            if (textCapitalization == TextInputChannel.TextCapitalization.CHARACTERS) {
                return i | 4096;
            }
            if (textCapitalization == TextInputChannel.TextCapitalization.WORDS) {
                return i | 8192;
            }
            return textCapitalization == TextInputChannel.TextCapitalization.SENTENCES ? i | 16384 : i;
        }
    }

    public InputConnection createInputConnection(View view, EditorInfo editorInfo) {
        int i;
        if (this.inputTarget.type == InputTarget.Type.NO_TARGET) {
            this.lastInputConnection = null;
            return null;
        } else if (this.inputTarget.type != InputTarget.Type.PLATFORM_VIEW) {
            editorInfo.inputType = inputTypeFromTextInputType(this.configuration.inputType, this.configuration.obscureText, this.configuration.autocorrect, this.configuration.enableSuggestions, this.configuration.textCapitalization);
            editorInfo.imeOptions = UCExtension.EXTEND_INPUT_TYPE_IDCARD;
            if (this.configuration.inputAction == null) {
                i = (131072 & editorInfo.inputType) != 0 ? 1 : 6;
            } else {
                i = this.configuration.inputAction.intValue();
            }
            if (this.configuration.actionLabel != null) {
                editorInfo.actionLabel = this.configuration.actionLabel;
                editorInfo.actionId = i;
            }
            editorInfo.imeOptions = i | editorInfo.imeOptions;
            InputConnectionAdaptor inputConnectionAdaptor = new InputConnectionAdaptor(view, this.inputTarget.id, this.textInputChannel, this.mEditable, editorInfo);
            editorInfo.initialSelStart = Selection.getSelectionStart(this.mEditable);
            editorInfo.initialSelEnd = Selection.getSelectionEnd(this.mEditable);
            this.lastInputConnection = inputConnectionAdaptor;
            return this.lastInputConnection;
        } else if (this.isInputConnectionLocked) {
            return this.lastInputConnection;
        } else {
            this.lastInputConnection = this.platformViewsController.getPlatformViewById(Integer.valueOf(this.inputTarget.id)).onCreateInputConnection(editorInfo);
            return this.lastInputConnection;
        }
    }

    @Nullable
    public InputConnection getLastInputConnection() {
        return this.lastInputConnection;
    }

    public void clearPlatformViewClient(int i) {
        if (this.inputTarget.type == InputTarget.Type.PLATFORM_VIEW && this.inputTarget.id == i) {
            this.inputTarget = new InputTarget(InputTarget.Type.NO_TARGET, 0);
            hideTextInput(this.mView);
            this.mImm.restartInput(this.mView);
            this.mRestartInputPending = false;
        }
    }

    /* access modifiers changed from: private */
    public void showTextInput(View view) {
        view.requestFocus();
        this.mImm.showSoftInput(view, 0);
    }

    /* access modifiers changed from: private */
    public void hideTextInput(View view) {
        this.mImm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void setTextInputClient(int i, TextInputChannel.Configuration configuration2) {
        this.inputTarget = new InputTarget(InputTarget.Type.FRAMEWORK_CLIENT, i);
        this.configuration = configuration2;
        this.mEditable = Editable.Factory.getInstance().newEditable("");
        this.mRestartInputPending = true;
        unlockPlatformViewInputConnection();
    }

    /* access modifiers changed from: private */
    public void setPlatformViewTextInputClient(int i) {
        this.mView.requestFocus();
        this.inputTarget = new InputTarget(InputTarget.Type.PLATFORM_VIEW, i);
        this.mImm.restartInput(this.mView);
        this.mRestartInputPending = false;
    }

    private void applyStateToSelection(TextInputChannel.TextEditState textEditState) {
        int i = textEditState.selectionStart;
        int i2 = textEditState.selectionEnd;
        if (i < 0 || i > this.mEditable.length() || i2 < 0 || i2 > this.mEditable.length()) {
            Selection.removeSelection(this.mEditable);
        } else {
            Selection.setSelection(this.mEditable, i, i2);
        }
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void setTextInputEditingState(View view, TextInputChannel.TextEditState textEditState) {
        if (!textEditState.text.equals(this.mEditable.toString())) {
            this.mEditable.replace(0, this.mEditable.length(), textEditState.text);
        }
        applyStateToSelection(textEditState);
        if (this.restartAlwaysRequired || this.mRestartInputPending) {
            this.mImm.restartInput(view);
            this.mRestartInputPending = false;
            return;
        }
        this.mImm.updateSelection(this.mView, Math.max(Selection.getSelectionStart(this.mEditable), 0), Math.max(Selection.getSelectionEnd(this.mEditable), 0), BaseInputConnection.getComposingSpanStart(this.mEditable), BaseInputConnection.getComposingSpanEnd(this.mEditable));
    }

    @SuppressLint({"NewApi"})
    private boolean isRestartAlwaysRequired() {
        if (this.mImm.getCurrentInputMethodSubtype() == null || Build.VERSION.SDK_INT < 21 || !Build.MANUFACTURER.equals("samsung")) {
            return false;
        }
        return Settings.Secure.getString(this.mView.getContext().getContentResolver(), "default_input_method").contains("Samsung");
    }

    /* access modifiers changed from: private */
    public void clearTextInputClient() {
        if (this.inputTarget.type != InputTarget.Type.PLATFORM_VIEW) {
            this.inputTarget = new InputTarget(InputTarget.Type.NO_TARGET, 0);
            unlockPlatformViewInputConnection();
        }
    }

    private static class InputTarget {
        int id;
        @NonNull
        Type type;

        enum Type {
            NO_TARGET,
            FRAMEWORK_CLIENT,
            PLATFORM_VIEW
        }

        public InputTarget(@NonNull Type type2, int i) {
            this.type = type2;
            this.id = i;
        }
    }
}
