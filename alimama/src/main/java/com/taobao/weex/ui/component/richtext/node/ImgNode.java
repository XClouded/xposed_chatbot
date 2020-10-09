package com.taobao.weex.ui.component.richtext.node;

import android.content.Context;
import android.net.Uri;
import android.text.SpannableStringBuilder;
import androidx.annotation.NonNull;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.DrawableStrategy;
import com.taobao.weex.ui.component.richtext.span.ImgSpan;
import com.taobao.weex.ui.component.richtext.span.ItemClickSpan;
import com.taobao.weex.utils.ImgURIUtil;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import java.util.LinkedList;
import java.util.Map;

class ImgNode extends RichTextNode {
    public static final String NODE_TYPE = "image";

    /* access modifiers changed from: protected */
    public boolean isInternalNode() {
        return false;
    }

    public String toString() {
        return "﻿";
    }

    static class ImgNodeCreator implements RichTextNodeCreator<ImgNode> {
        ImgNodeCreator() {
        }

        public ImgNode createRichTextNode(Context context, String str, String str2) {
            return new ImgNode(context, str, str2);
        }

        public ImgNode createRichTextNode(Context context, String str, String str2, String str3, Map<String, Object> map, Map<String, Object> map2) {
            return new ImgNode(context, str, str2, str3, map, map2);
        }
    }

    private ImgNode(Context context, String str, String str2) {
        super(context, str, str2);
    }

    private ImgNode(Context context, String str, String str2, String str3, Map<String, Object> map, Map<String, Object> map2) {
        super(context, str, str2, str3, map, map2);
    }

    /* access modifiers changed from: protected */
    public void updateSpans(SpannableStringBuilder spannableStringBuilder, int i) {
        WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(this.mInstanceId);
        if (WXSDKEngine.getDrawableLoader() != null && this.style.containsKey("width") && this.style.containsKey("height") && this.attr.containsKey("src") && sDKInstance != null) {
            LinkedList<Object> linkedList = new LinkedList<>();
            linkedList.add(createImgSpan(sDKInstance));
            if (this.attr.containsKey(RichTextNode.PSEUDO_REF)) {
                linkedList.add(new ItemClickSpan(this.mInstanceId, this.mComponentRef, this.attr.get(RichTextNode.PSEUDO_REF).toString()));
            }
            for (Object span : linkedList) {
                spannableStringBuilder.setSpan(span, 0, spannableStringBuilder.length(), createSpanFlag(i));
            }
        }
    }

    @NonNull
    private ImgSpan createImgSpan(WXSDKInstance wXSDKInstance) {
        int realPxByWidth = (int) WXViewUtils.getRealPxByWidth(WXUtils.getFloat(this.style.get("width")), wXSDKInstance.getInstanceViewPortWidth());
        int realPxByWidth2 = (int) WXViewUtils.getRealPxByWidth(WXUtils.getFloat(this.style.get("height")), wXSDKInstance.getInstanceViewPortWidth());
        ImgSpan imgSpan = new ImgSpan(realPxByWidth, realPxByWidth2);
        Uri rewriteUri = wXSDKInstance.rewriteUri(Uri.parse(this.attr.get("src").toString()), "image");
        if ("local".equals(rewriteUri.getScheme())) {
            imgSpan.setDrawable(ImgURIUtil.getDrawableFromLoaclSrc(this.mContext, rewriteUri), false);
        } else {
            DrawableStrategy drawableStrategy = new DrawableStrategy();
            drawableStrategy.width = realPxByWidth;
            drawableStrategy.height = realPxByWidth2;
            WXSDKEngine.getDrawableLoader().setDrawable(rewriteUri.toString(), imgSpan, drawableStrategy);
        }
        return imgSpan;
    }
}
