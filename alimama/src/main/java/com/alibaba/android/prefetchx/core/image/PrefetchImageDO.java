package com.alibaba.android.prefetchx.core.image;

import com.taobao.weex.el.parse.Operators;

public class PrefetchImageDO {
    public int denominator = 1;
    public boolean fullWidth;
    public boolean orignalSize;
    public String postfix;
    public int size = 0;
    public String url;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.url);
        if (this.postfix != null) {
            sb.append(this.postfix);
            sb.append(Operators.SPACE_STR);
        }
        if (this.fullWidth) {
            sb.append("fullWidth");
        }
        if (this.orignalSize) {
            sb.append("orignalSize");
        }
        if (1 != this.denominator) {
            sb.append("1/");
            sb.append(this.denominator);
        }
        if (this.size > 0) {
            sb.append(this.size);
            sb.append("px");
        }
        return sb.toString();
    }
}
