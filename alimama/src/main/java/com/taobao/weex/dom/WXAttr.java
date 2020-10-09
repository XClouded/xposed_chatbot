package com.taobao.weex.dom;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.annotation.UiThread;
import androidx.collection.ArrayMap;
import androidx.collection.SimpleArrayMap;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.WXImageSharpen;
import com.taobao.weex.dom.binding.ELUtils;
import com.taobao.weex.dom.binding.WXStatement;
import com.taobao.weex.el.parse.Parser;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class WXAttr implements Map<String, Object>, Cloneable {
    private static final long serialVersionUID = -2619357510079360946L;
    @NonNull
    private Map<String, Object> attr;
    private ArrayMap<String, Object> mBindingAttrs;
    private WXStatement mStatement;
    private Map<String, Object> writeAttr;

    public WXAttr() {
        this.attr = new HashMap();
    }

    public WXAttr(@NonNull Map<String, Object> map) {
        this.attr = map;
    }

    public WXAttr(@NonNull Map<String, Object> map, int i) {
        this.attr = map;
    }

    public static String getPrefix(Map<String, Object> map) {
        Object obj;
        if (map == null || (obj = map.get("prefix")) == null) {
            return null;
        }
        return obj.toString();
    }

    public static String getSuffix(Map<String, Object> map) {
        Object obj;
        if (map == null || (obj = map.get("suffix")) == null) {
            return null;
        }
        return obj.toString();
    }

    public static String getValue(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        Object obj = map.get("value");
        if (obj == null && (obj = map.get("content")) == null) {
            return null;
        }
        return obj.toString();
    }

    public WXImageQuality getImageQuality() {
        Object obj = get(containsKey("quality") ? "quality" : "imageQuality");
        WXImageQuality wXImageQuality = WXImageQuality.AUTO;
        if (obj != null) {
            String obj2 = obj.toString();
            if (!TextUtils.isEmpty(obj2)) {
                try {
                    return WXImageQuality.valueOf(obj2.toUpperCase(Locale.US));
                } catch (IllegalArgumentException unused) {
                    WXLogUtils.e("Image", "Invalid value image quality. Only low, normal, high, original are valid");
                }
            }
        }
        return wXImageQuality;
    }

    public WXImageSharpen getImageSharpen() {
        Object obj = get("sharpen");
        if (obj == null) {
            obj = get("imageSharpen");
        }
        if (obj == null) {
            return WXImageSharpen.UNSHARPEN;
        }
        return obj.toString().equals("sharpen") ? WXImageSharpen.SHARPEN : WXImageSharpen.UNSHARPEN;
    }

    public String getImageSrc() {
        Object obj = get("src");
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    public boolean canRecycled() {
        Object obj = get(Constants.Name.RECYCLE);
        if (obj == null) {
            return true;
        }
        try {
            return Boolean.parseBoolean(String.valueOf(obj));
        } catch (Exception e) {
            WXLogUtils.e("[WXAttr] recycle:", (Throwable) e);
            return true;
        }
    }

    public boolean showIndicators() {
        Object obj = get("showIndicators");
        if (obj == null) {
            return true;
        }
        try {
            return Boolean.parseBoolean(String.valueOf(obj));
        } catch (Exception e) {
            WXLogUtils.e("[WXAttr] showIndicators:", (Throwable) e);
            return true;
        }
    }

    public boolean autoPlay() {
        Object obj = get("autoPlay");
        if (obj == null) {
            return false;
        }
        try {
            return Boolean.parseBoolean(String.valueOf(obj));
        } catch (Exception e) {
            WXLogUtils.e("[WXAttr] autoPlay:", (Throwable) e);
            return false;
        }
    }

    public String getScope() {
        Object obj = get("scope");
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    public String getLoadMoreRetry() {
        Object obj = get("loadmoreretry");
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    public String getLoadMoreOffset() {
        Object obj = get("loadmoreoffset");
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    public String optString(String str) {
        if (!containsKey(str)) {
            return "";
        }
        Object obj = get(str);
        if (obj instanceof String) {
            return (String) obj;
        }
        return obj != null ? String.valueOf(obj) : "";
    }

    public boolean getIsRecycleImage() {
        Object obj = get("recycleImage");
        if (obj == null) {
            return true;
        }
        try {
            return Boolean.parseBoolean(String.valueOf(obj));
        } catch (Exception e) {
            WXLogUtils.e("[WXAttr] recycleImage:", (Throwable) e);
            return false;
        }
    }

    public String getScrollDirection() {
        Object obj = get("scrollDirection");
        if (obj == null) {
            return "vertical";
        }
        return obj.toString();
    }

    public int getOrientation() {
        String scrollDirection = getScrollDirection();
        if (!TextUtils.isEmpty(scrollDirection) && scrollDirection.equals(Constants.Value.HORIZONTAL)) {
            return 0;
        }
        Object obj = get("orientation");
        if (obj == null || !Constants.Value.HORIZONTAL.equals(obj.toString())) {
            return 1;
        }
        return 0;
    }

    public float getElevation(int i) {
        Object obj = get("elevation");
        if (obj == null) {
            return Float.NaN;
        }
        String obj2 = obj.toString();
        if (!TextUtils.isEmpty(obj2)) {
            return WXViewUtils.getRealSubPxByWidth(WXUtils.getFloat(obj2), i);
        }
        return 0.0f;
    }

    public float getColumnWidth() {
        Object obj = get(Constants.Name.COLUMN_WIDTH);
        if (obj == null) {
            return -1.0f;
        }
        String valueOf = String.valueOf(obj);
        if ("auto".equals(valueOf)) {
            return -1.0f;
        }
        try {
            float parseFloat = Float.parseFloat(valueOf);
            if (parseFloat > 0.0f) {
                return parseFloat;
            }
            return 0.0f;
        } catch (Exception e) {
            WXLogUtils.e("[WXAttr] getColumnWidth:", (Throwable) e);
            return -1.0f;
        }
    }

    public int getColumnCount() {
        Object obj = get(Constants.Name.COLUMN_COUNT);
        if (obj == null) {
            return -1;
        }
        String valueOf = String.valueOf(obj);
        if ("auto".equals(valueOf)) {
            return -1;
        }
        try {
            int parseInt = Integer.parseInt(valueOf);
            if (parseInt > 0) {
                return parseInt;
            }
            return -1;
        } catch (Exception e) {
            WXLogUtils.e("[WXAttr] getColumnCount:", (Throwable) e);
            return -1;
        }
    }

    public float getColumnGap() {
        Object obj = get(Constants.Name.COLUMN_GAP);
        if (obj == null) {
            return 32.0f;
        }
        String valueOf = String.valueOf(obj);
        if ("normal".equals(valueOf)) {
            return 32.0f;
        }
        try {
            float parseFloat = Float.parseFloat(valueOf);
            if (parseFloat >= 0.0f) {
                return parseFloat;
            }
            return -1.0f;
        } catch (Exception e) {
            WXLogUtils.e("[WXAttr] getColumnGap:", (Throwable) e);
            return 32.0f;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0036 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0037 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0039 A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getLayoutType() {
        /*
            r5 = this;
            java.lang.String r0 = "layout"
            java.lang.Object r0 = r5.get(r0)
            r1 = 1
            if (r0 != 0) goto L_0x000a
            return r1
        L_0x000a:
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ Exception -> 0x003b }
            r2 = -1
            int r3 = r0.hashCode()     // Catch:{ Exception -> 0x003b }
            r4 = 3181382(0x308b46, float:4.458066E-39)
            if (r3 == r4) goto L_0x0028
            r4 = 674874986(0x2839c66a, float:1.0312587E-14)
            if (r3 == r4) goto L_0x001e
            goto L_0x0032
        L_0x001e:
            java.lang.String r3 = "multi-column"
            boolean r0 = r0.equals(r3)     // Catch:{ Exception -> 0x003b }
            if (r0 == 0) goto L_0x0032
            r0 = 0
            goto L_0x0033
        L_0x0028:
            java.lang.String r3 = "grid"
            boolean r0 = r0.equals(r3)     // Catch:{ Exception -> 0x003b }
            if (r0 == 0) goto L_0x0032
            r0 = 1
            goto L_0x0033
        L_0x0032:
            r0 = -1
        L_0x0033:
            switch(r0) {
                case 0: goto L_0x0039;
                case 1: goto L_0x0037;
                default: goto L_0x0036;
            }
        L_0x0036:
            return r1
        L_0x0037:
            r0 = 2
            return r0
        L_0x0039:
            r0 = 3
            return r0
        L_0x003b:
            r0 = move-exception
            java.lang.String r2 = "[WXAttr] getLayoutType:"
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r2, (java.lang.Throwable) r0)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.dom.WXAttr.getLayoutType():int");
    }

    public boolean equals(Object obj) {
        return this.attr.equals(obj);
    }

    public int hashCode() {
        return this.attr.hashCode();
    }

    public void clear() {
        this.attr.clear();
    }

    public boolean containsKey(Object obj) {
        return this.attr.containsKey(obj);
    }

    public boolean containsValue(Object obj) {
        return this.attr.containsValue(obj);
    }

    @NonNull
    public Set<Map.Entry<String, Object>> entrySet() {
        return this.attr.entrySet();
    }

    public Object get(Object obj) {
        Object obj2;
        Map<String, Object> map = this.writeAttr;
        if (map == null || (obj2 = map.get(obj)) == null) {
            return this.attr.get(obj);
        }
        return obj2;
    }

    public boolean isEmpty() {
        return this.attr.isEmpty();
    }

    @NonNull
    public Set<String> keySet() {
        return this.attr.keySet();
    }

    public Object put(String str, Object obj) {
        if (addBindingAttrIfStatement(str, obj)) {
            return null;
        }
        return this.attr.put(str, obj);
    }

    public void putAll(Map<? extends String, ?> map) {
        if (this.writeAttr == null) {
            this.writeAttr = new ArrayMap();
        }
        this.writeAttr.putAll(map);
    }

    public Object remove(Object obj) {
        return this.attr.remove(obj);
    }

    public int size() {
        return this.attr.size();
    }

    @NonNull
    public Collection<Object> values() {
        return this.attr.values();
    }

    public ArrayMap<String, Object> getBindingAttrs() {
        return this.mBindingAttrs;
    }

    public WXStatement getStatement() {
        return this.mStatement;
    }

    public void setBindingAttrs(ArrayMap<String, Object> arrayMap) {
        this.mBindingAttrs = arrayMap;
    }

    public void setStatement(WXStatement wXStatement) {
        this.mStatement = wXStatement;
    }

    public void parseStatements() {
        if (this.attr != null) {
            this.attr = filterStatementsFromAttrs(this.attr);
        }
    }

    private Map<String, Object> filterStatementsFromAttrs(Map map) {
        if (map == null || map.size() == 0) {
            return map;
        }
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            if (ELUtils.COMPONENT_PROPS.equals(entry.getKey())) {
                entry.setValue(ELUtils.bindingBlock(entry.getValue()));
            } else if (addBindingAttrIfStatement((String) entry.getKey(), entry.getValue())) {
                it.remove();
            }
        }
        return map;
    }

    private boolean addBindingAttrIfStatement(String str, Object obj) {
        for (String equals : ELUtils.EXCLUDES_BINDING) {
            if (str.equals(equals)) {
                return false;
            }
        }
        if (ELUtils.isBinding(obj)) {
            if (this.mBindingAttrs == null) {
                this.mBindingAttrs = new ArrayMap<>();
            }
            this.mBindingAttrs.put(str, ELUtils.bindingBlock(obj));
            return true;
        } else if (WXStatement.WX_IF.equals(str)) {
            if (this.mStatement == null) {
                this.mStatement = new WXStatement();
            }
            if (obj != null) {
                this.mStatement.put(str, Parser.parse(obj.toString()));
            }
            return true;
        } else {
            if (WXStatement.WX_FOR.equals(str)) {
                if (this.mStatement == null) {
                    this.mStatement = new WXStatement();
                }
                Object vforBlock = ELUtils.vforBlock(obj);
                if (vforBlock != null) {
                    this.mStatement.put(str, vforBlock);
                    return true;
                }
            }
            if (WXStatement.WX_ONCE.equals(str)) {
                if (this.mStatement == null) {
                    this.mStatement = new WXStatement();
                }
                this.mStatement.put(str, true);
            }
            return false;
        }
    }

    public void skipFilterPutAll(Map<String, Object> map) {
        this.attr.putAll(map);
    }

    @UiThread
    public void mergeAttr() {
        if (this.writeAttr != null) {
            this.attr.putAll(this.writeAttr);
            this.writeAttr = null;
        }
    }

    public WXAttr clone() {
        WXAttr wXAttr = new WXAttr();
        wXAttr.skipFilterPutAll(this.attr);
        if (this.mBindingAttrs != null) {
            wXAttr.mBindingAttrs = new ArrayMap<>((SimpleArrayMap) this.mBindingAttrs);
        }
        if (this.mStatement != null) {
            wXAttr.mStatement = new WXStatement(this.mStatement);
        }
        return wXAttr;
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY})
    public String toString() {
        return this.attr.toString();
    }
}
