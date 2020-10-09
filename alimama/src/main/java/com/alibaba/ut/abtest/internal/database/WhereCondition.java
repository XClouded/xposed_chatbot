package com.alibaba.ut.abtest.internal.database;

import java.util.Collections;
import java.util.List;

public class WhereCondition {
    private final String text;
    private final Object[] values;

    public WhereCondition(String str, Object... objArr) {
        this.text = str;
        this.values = objArr;
    }

    /* access modifiers changed from: package-private */
    public void appendTo(StringBuilder sb) {
        sb.append(this.text);
    }

    /* access modifiers changed from: package-private */
    public void appendValuesTo(List<Object> list) {
        if (this.values != null) {
            Collections.addAll(list, this.values);
        }
    }

    public String getText() {
        return this.text;
    }

    public String[] getValues() {
        if (this.values == null || this.values.length <= 0) {
            return null;
        }
        String[] strArr = new String[this.values.length];
        for (int i = 0; i < this.values.length; i++) {
            strArr[i] = this.values[i].toString();
        }
        return strArr;
    }
}
