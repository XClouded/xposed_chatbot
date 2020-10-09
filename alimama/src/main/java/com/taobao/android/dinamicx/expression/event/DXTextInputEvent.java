package com.taobao.android.dinamicx.expression.event;

import android.text.Editable;

public class DXTextInputEvent extends DXEvent {
    private Editable text;

    public Editable getText() {
        return this.text;
    }

    public void setText(Editable editable) {
        this.text = editable;
    }

    public DXTextInputEvent(long j) {
        super(j);
    }
}
