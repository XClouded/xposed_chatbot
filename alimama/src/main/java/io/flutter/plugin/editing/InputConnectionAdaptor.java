package io.flutter.plugin.editing;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Build;
import android.provider.Settings;
import android.text.DynamicLayout;
import android.text.Editable;
import android.text.Layout;
import android.text.Selection;
import android.text.TextPaint;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.CursorAnchorInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import io.flutter.Log;
import io.flutter.embedding.engine.systemchannels.TextInputChannel;

class InputConnectionAdaptor extends BaseInputConnection {
    private final boolean isSamsung;
    private int mBatchCount = 0;
    private final int mClient;
    private final Editable mEditable;
    private final EditorInfo mEditorInfo;
    private final View mFlutterView;
    private InputMethodManager mImm;
    private final Layout mLayout = new DynamicLayout(this.mEditable, new TextPaint(), Integer.MAX_VALUE, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
    private final TextInputChannel textInputChannel;

    public InputConnectionAdaptor(View view, int i, TextInputChannel textInputChannel2, Editable editable, EditorInfo editorInfo) {
        super(view, true);
        this.mFlutterView = view;
        this.mClient = i;
        this.textInputChannel = textInputChannel2;
        this.mEditable = editable;
        this.mEditorInfo = editorInfo;
        this.mImm = (InputMethodManager) view.getContext().getSystemService("input_method");
        this.isSamsung = isSamsung();
    }

    private void updateEditingState() {
        if (this.mBatchCount <= 0) {
            int selectionStart = Selection.getSelectionStart(this.mEditable);
            int selectionEnd = Selection.getSelectionEnd(this.mEditable);
            int composingSpanStart = BaseInputConnection.getComposingSpanStart(this.mEditable);
            int composingSpanEnd = BaseInputConnection.getComposingSpanEnd(this.mEditable);
            this.mImm.updateSelection(this.mFlutterView, selectionStart, selectionEnd, composingSpanStart, composingSpanEnd);
            this.textInputChannel.updateEditingState(this.mClient, this.mEditable.toString(), selectionStart, selectionEnd, composingSpanStart, composingSpanEnd);
        }
    }

    public Editable getEditable() {
        return this.mEditable;
    }

    public boolean beginBatchEdit() {
        this.mBatchCount++;
        return super.beginBatchEdit();
    }

    public boolean endBatchEdit() {
        boolean endBatchEdit = super.endBatchEdit();
        this.mBatchCount--;
        updateEditingState();
        return endBatchEdit;
    }

    public boolean commitText(CharSequence charSequence, int i) {
        boolean commitText = super.commitText(charSequence, i);
        updateEditingState();
        return commitText;
    }

    public boolean deleteSurroundingText(int i, int i2) {
        if (Selection.getSelectionStart(this.mEditable) == -1) {
            return true;
        }
        boolean deleteSurroundingText = super.deleteSurroundingText(i, i2);
        updateEditingState();
        return deleteSurroundingText;
    }

    public boolean setComposingRegion(int i, int i2) {
        boolean composingRegion = super.setComposingRegion(i, i2);
        updateEditingState();
        return composingRegion;
    }

    public boolean setComposingText(CharSequence charSequence, int i) {
        boolean z;
        if (charSequence.length() == 0) {
            z = super.commitText(charSequence, i);
        } else {
            z = super.setComposingText(charSequence, i);
        }
        updateEditingState();
        return z;
    }

    public boolean finishComposingText() {
        boolean finishComposingText = super.finishComposingText();
        if (this.isSamsung && Build.VERSION.SDK_INT >= 21) {
            CursorAnchorInfo.Builder builder = new CursorAnchorInfo.Builder();
            builder.setComposingText(-1, "");
            this.mImm.updateCursorAnchorInfo(this.mFlutterView, builder.build());
        }
        updateEditingState();
        return finishComposingText;
    }

    @SuppressLint({"NewApi"})
    private boolean isSamsung() {
        if (this.mImm.getCurrentInputMethodSubtype() == null || Build.VERSION.SDK_INT < 21 || !Build.MANUFACTURER.equals("samsung")) {
            return false;
        }
        return Settings.Secure.getString(this.mFlutterView.getContext().getContentResolver(), "default_input_method").contains("Samsung");
    }

    public boolean setSelection(int i, int i2) {
        boolean selection = super.setSelection(i, i2);
        updateEditingState();
        return selection;
    }

    private static int clampIndexToEditable(int i, Editable editable) {
        int max = Math.max(0, Math.min(editable.length(), i));
        if (max != i) {
            Log.d("flutter", "Text selection index was clamped (" + i + "->" + max + ") to remain in bounds. This may not be your fault, as some keyboards may select outside of bounds.");
        }
        return max;
    }

    public boolean sendKeyEvent(KeyEvent keyEvent) {
        if (keyEvent.getAction() == 0) {
            if (keyEvent.getKeyCode() == 67) {
                int clampIndexToEditable = clampIndexToEditable(Selection.getSelectionStart(this.mEditable), this.mEditable);
                int clampIndexToEditable2 = clampIndexToEditable(Selection.getSelectionEnd(this.mEditable), this.mEditable);
                if (clampIndexToEditable2 > clampIndexToEditable) {
                    Selection.setSelection(this.mEditable, clampIndexToEditable);
                    this.mEditable.delete(clampIndexToEditable, clampIndexToEditable2);
                    updateEditingState();
                    return true;
                } else if (clampIndexToEditable > 0) {
                    if (this.mLayout.isRtlCharAt(this.mLayout.getLineForOffset(clampIndexToEditable))) {
                        try {
                            Selection.extendRight(this.mEditable, this.mLayout);
                        } catch (IndexOutOfBoundsException unused) {
                            Selection.setSelection(this.mEditable, clampIndexToEditable, clampIndexToEditable - 1);
                        }
                    } else {
                        Selection.extendLeft(this.mEditable, this.mLayout);
                    }
                    int clampIndexToEditable3 = clampIndexToEditable(Selection.getSelectionStart(this.mEditable), this.mEditable);
                    int clampIndexToEditable4 = clampIndexToEditable(Selection.getSelectionEnd(this.mEditable), this.mEditable);
                    Selection.setSelection(this.mEditable, Math.min(clampIndexToEditable3, clampIndexToEditable4));
                    this.mEditable.delete(Math.min(clampIndexToEditable3, clampIndexToEditable4), Math.max(clampIndexToEditable3, clampIndexToEditable4));
                    updateEditingState();
                    return true;
                }
            } else if (keyEvent.getKeyCode() == 21) {
                int selectionStart = Selection.getSelectionStart(this.mEditable);
                int selectionEnd = Selection.getSelectionEnd(this.mEditable);
                if (selectionStart != selectionEnd || keyEvent.isShiftPressed()) {
                    setSelection(selectionStart, Math.max(selectionEnd - 1, 0));
                } else {
                    int max = Math.max(selectionStart - 1, 0);
                    setSelection(max, max);
                }
                return true;
            } else if (keyEvent.getKeyCode() == 22) {
                int selectionStart2 = Selection.getSelectionStart(this.mEditable);
                int selectionEnd2 = Selection.getSelectionEnd(this.mEditable);
                if (selectionStart2 != selectionEnd2 || keyEvent.isShiftPressed()) {
                    setSelection(selectionStart2, Math.min(selectionEnd2 + 1, this.mEditable.length()));
                } else {
                    int min = Math.min(selectionStart2 + 1, this.mEditable.length());
                    setSelection(min, min);
                }
                return true;
            } else if (keyEvent.getKeyCode() == 19) {
                if (Selection.getSelectionStart(this.mEditable) != Selection.getSelectionEnd(this.mEditable) || keyEvent.isShiftPressed()) {
                    Selection.extendUp(this.mEditable, this.mLayout);
                    setSelection(Selection.getSelectionStart(this.mEditable), Selection.getSelectionEnd(this.mEditable));
                } else {
                    Selection.moveUp(this.mEditable, this.mLayout);
                    int selectionStart3 = Selection.getSelectionStart(this.mEditable);
                    setSelection(selectionStart3, selectionStart3);
                }
                return true;
            } else if (keyEvent.getKeyCode() == 20) {
                if (Selection.getSelectionStart(this.mEditable) != Selection.getSelectionEnd(this.mEditable) || keyEvent.isShiftPressed()) {
                    Selection.extendDown(this.mEditable, this.mLayout);
                    setSelection(Selection.getSelectionStart(this.mEditable), Selection.getSelectionEnd(this.mEditable));
                } else {
                    Selection.moveDown(this.mEditable, this.mLayout);
                    int selectionStart4 = Selection.getSelectionStart(this.mEditable);
                    setSelection(selectionStart4, selectionStart4);
                }
                return true;
            } else if ((keyEvent.getKeyCode() == 66 || keyEvent.getKeyCode() == 160) && (131072 & this.mEditorInfo.inputType) == 0) {
                performEditorAction(this.mEditorInfo.imeOptions & 255);
                return true;
            } else {
                int unicodeChar = keyEvent.getUnicodeChar();
                if (unicodeChar != 0) {
                    int max2 = Math.max(0, Selection.getSelectionStart(this.mEditable));
                    int max3 = Math.max(0, Selection.getSelectionEnd(this.mEditable));
                    int min2 = Math.min(max2, max3);
                    int max4 = Math.max(max2, max3);
                    if (min2 != max4) {
                        this.mEditable.delete(min2, max4);
                    }
                    this.mEditable.insert(min2, String.valueOf((char) unicodeChar));
                    int i = min2 + 1;
                    setSelection(i, i);
                }
                return true;
            }
        }
        if (keyEvent.getAction() != 1 || (keyEvent.getKeyCode() != 59 && keyEvent.getKeyCode() != 60)) {
            return false;
        }
        int selectionEnd3 = Selection.getSelectionEnd(this.mEditable);
        setSelection(selectionEnd3, selectionEnd3);
        return true;
    }

    public boolean performContextMenuAction(int i) {
        if (i == 16908319) {
            setSelection(0, this.mEditable.length());
            return true;
        } else if (i == 16908320) {
            int selectionStart = Selection.getSelectionStart(this.mEditable);
            int selectionEnd = Selection.getSelectionEnd(this.mEditable);
            if (selectionStart != selectionEnd) {
                int min = Math.min(selectionStart, selectionEnd);
                int max = Math.max(selectionStart, selectionEnd);
                ((ClipboardManager) this.mFlutterView.getContext().getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("text label?", this.mEditable.subSequence(min, max)));
                this.mEditable.delete(min, max);
                setSelection(min, min);
            }
            return true;
        } else if (i == 16908321) {
            int selectionStart2 = Selection.getSelectionStart(this.mEditable);
            int selectionEnd2 = Selection.getSelectionEnd(this.mEditable);
            if (selectionStart2 != selectionEnd2) {
                ((ClipboardManager) this.mFlutterView.getContext().getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("text label?", this.mEditable.subSequence(Math.min(selectionStart2, selectionEnd2), Math.max(selectionStart2, selectionEnd2))));
            }
            return true;
        } else if (i != 16908322) {
            return false;
        } else {
            ClipData primaryClip = ((ClipboardManager) this.mFlutterView.getContext().getSystemService("clipboard")).getPrimaryClip();
            if (primaryClip != null) {
                CharSequence coerceToText = primaryClip.getItemAt(0).coerceToText(this.mFlutterView.getContext());
                int max2 = Math.max(0, Selection.getSelectionStart(this.mEditable));
                int max3 = Math.max(0, Selection.getSelectionEnd(this.mEditable));
                int min2 = Math.min(max2, max3);
                int max4 = Math.max(max2, max3);
                if (min2 != max4) {
                    this.mEditable.delete(min2, max4);
                }
                this.mEditable.insert(min2, coerceToText);
                int length = min2 + coerceToText.length();
                setSelection(length, length);
            }
            return true;
        }
    }

    public boolean performEditorAction(int i) {
        if (i != 7) {
            switch (i) {
                case 0:
                    this.textInputChannel.unspecifiedAction(this.mClient);
                    return true;
                case 1:
                    this.textInputChannel.newline(this.mClient);
                    return true;
                case 2:
                    this.textInputChannel.go(this.mClient);
                    return true;
                case 3:
                    this.textInputChannel.search(this.mClient);
                    return true;
                case 4:
                    this.textInputChannel.send(this.mClient);
                    return true;
                case 5:
                    this.textInputChannel.next(this.mClient);
                    return true;
                default:
                    this.textInputChannel.done(this.mClient);
                    return true;
            }
        } else {
            this.textInputChannel.previous(this.mClient);
            return true;
        }
    }
}
