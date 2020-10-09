package com.alimama.union.app.taotokenConvert;

import androidx.annotation.NonNull;

public class TaoCodeDialogEvent {
    @NonNull
    private final DialogResult mDialogResult;

    public enum DialogResult {
        TAO_CODE_DIALOG_SHOW,
        TAO_CODE_DIALOG_NOT_SHOW
    }

    private TaoCodeDialogEvent(@NonNull DialogResult dialogResult) {
        this.mDialogResult = dialogResult;
    }

    public static TaoCodeDialogEvent dialogShow() {
        return new TaoCodeDialogEvent(DialogResult.TAO_CODE_DIALOG_SHOW);
    }

    public static TaoCodeDialogEvent dialogNotShow() {
        return new TaoCodeDialogEvent(DialogResult.TAO_CODE_DIALOG_NOT_SHOW);
    }

    @NonNull
    public DialogResult getmDialogResult() {
        return this.mDialogResult;
    }
}
