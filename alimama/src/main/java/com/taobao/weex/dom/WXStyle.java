package com.taobao.weex.dom;

import android.text.Layout;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.collection.ArrayMap;
import androidx.collection.SimpleArrayMap;
import com.taobao.weex.common.Constants;
import com.taobao.weex.dom.binding.ELUtils;
import com.taobao.weex.ui.component.WXTextDecoration;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class WXStyle implements Map<String, Object>, Cloneable {
    public static final int UNSET = -1;
    private static final long serialVersionUID = 611132641365274134L;
    private ArrayMap<String, Object> mBindingStyle;
    @Nullable
    private Map<String, Object> mPesudoResetStyleMap;
    @Nullable
    private Map<String, Map<String, Object>> mPesudoStyleMap;
    @NonNull
    private Map<String, Object> mStyles;

    public WXStyle() {
        this.mStyles = new ArrayMap();
    }

    public WXStyle(Map<String, Object> map) {
        this.mStyles = map;
        processPesudoClasses(this.mStyles);
    }

    public WXStyle(Map<String, Object> map, boolean z) {
        this();
        putAll(map, z);
    }

    @Nullable
    public String getBlur() {
        if (get("filter") == null) {
            return null;
        }
        return get("filter").toString().trim();
    }

    public static WXTextDecoration getTextDecoration(Map<String, Object> map) {
        Object obj;
        if (map == null || (obj = map.get("textDecoration")) == null) {
            return WXTextDecoration.NONE;
        }
        String obj2 = obj.toString();
        char c = 65535;
        int hashCode = obj2.hashCode();
        if (hashCode != -1171789332) {
            if (hashCode != -1026963764) {
                if (hashCode == 3387192 && obj2.equals("none")) {
                    c = 2;
                }
            } else if (obj2.equals("underline")) {
                c = 0;
            }
        } else if (obj2.equals("line-through")) {
            c = 1;
        }
        switch (c) {
            case 0:
                return WXTextDecoration.UNDERLINE;
            case 1:
                return WXTextDecoration.LINETHROUGH;
            case 2:
                return WXTextDecoration.NONE;
            default:
                return WXTextDecoration.INVALID;
        }
    }

    public static String getTextColor(Map<String, Object> map) {
        Object obj;
        if (map == null || (obj = map.get("color")) == null) {
            return "";
        }
        return obj.toString();
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int getFontWeight(java.util.Map<java.lang.String, java.lang.Object> r4) {
        /*
            r0 = 1
            r1 = 0
            if (r4 == 0) goto L_0x004f
            java.lang.String r2 = "fontWeight"
            java.lang.Object r4 = r4.get(r2)
            if (r4 == 0) goto L_0x004f
            java.lang.String r4 = r4.toString()
            r2 = -1
            int r3 = r4.hashCode()
            switch(r3) {
                case 53430: goto L_0x0041;
                case 54391: goto L_0x0037;
                case 55352: goto L_0x002d;
                case 56313: goto L_0x0023;
                case 3029637: goto L_0x0019;
                default: goto L_0x0018;
            }
        L_0x0018:
            goto L_0x004b
        L_0x0019:
            java.lang.String r3 = "bold"
            boolean r4 = r4.equals(r3)
            if (r4 == 0) goto L_0x004b
            r4 = 4
            goto L_0x004c
        L_0x0023:
            java.lang.String r3 = "900"
            boolean r4 = r4.equals(r3)
            if (r4 == 0) goto L_0x004b
            r4 = 3
            goto L_0x004c
        L_0x002d:
            java.lang.String r3 = "800"
            boolean r4 = r4.equals(r3)
            if (r4 == 0) goto L_0x004b
            r4 = 2
            goto L_0x004c
        L_0x0037:
            java.lang.String r3 = "700"
            boolean r4 = r4.equals(r3)
            if (r4 == 0) goto L_0x004b
            r4 = 1
            goto L_0x004c
        L_0x0041:
            java.lang.String r3 = "600"
            boolean r4 = r4.equals(r3)
            if (r4 == 0) goto L_0x004b
            r4 = 0
            goto L_0x004c
        L_0x004b:
            r4 = -1
        L_0x004c:
            switch(r4) {
                case 0: goto L_0x0050;
                case 1: goto L_0x0050;
                case 2: goto L_0x0050;
                case 3: goto L_0x0050;
                case 4: goto L_0x0050;
                default: goto L_0x004f;
            }
        L_0x004f:
            r0 = 0
        L_0x0050:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.dom.WXStyle.getFontWeight(java.util.Map):int");
    }

    public static int getFontStyle(Map<String, Object> map) {
        Object obj;
        if (map == null || (obj = map.get("fontStyle")) == null || !obj.toString().equals(Constants.Value.ITALIC)) {
            return 0;
        }
        return 2;
    }

    public static int getFontSize(Map<String, Object> map, int i) {
        if (map == null) {
            return (int) WXViewUtils.getRealPxByWidth(32.0f, i);
        }
        int i2 = WXUtils.getInt(map.get("fontSize"));
        if (i2 <= 0) {
            i2 = 32;
        }
        return (int) WXViewUtils.getRealPxByWidth((float) i2, i);
    }

    public static String getFontFamily(Map<String, Object> map) {
        Object obj;
        if (map == null || (obj = map.get("fontFamily")) == null) {
            return null;
        }
        return obj.toString();
    }

    public static Layout.Alignment getTextAlignment(Map<String, Object> map) {
        return getTextAlignment(map, false);
    }

    public static Layout.Alignment getTextAlignment(Map<String, Object> map, boolean z) {
        Layout.Alignment alignment = z ? Layout.Alignment.ALIGN_OPPOSITE : Layout.Alignment.ALIGN_NORMAL;
        String str = (String) map.get("textAlign");
        if (TextUtils.equals("left", str)) {
            return Layout.Alignment.ALIGN_NORMAL;
        }
        if (TextUtils.equals("center", str)) {
            return Layout.Alignment.ALIGN_CENTER;
        }
        return TextUtils.equals("right", str) ? Layout.Alignment.ALIGN_OPPOSITE : alignment;
    }

    public static TextUtils.TruncateAt getTextOverflow(Map<String, Object> map) {
        if (TextUtils.equals("ellipsis", (String) map.get("textOverflow"))) {
            return TextUtils.TruncateAt.END;
        }
        return null;
    }

    public static int getLines(Map<String, Object> map) {
        return WXUtils.getInt(map.get("lines"));
    }

    public static int getLineHeight(Map<String, Object> map, int i) {
        int i2;
        if (map != null && (i2 = WXUtils.getInt(map.get("lineHeight"))) > 0) {
            return (int) WXViewUtils.getRealPxByWidth((float) i2, i);
        }
        return -1;
    }

    public float getBorderRadius() {
        float f = WXUtils.getFloat(get("borderRadius"));
        if (WXUtils.isUndefined(f)) {
            return Float.NaN;
        }
        return f;
    }

    public String getBorderColor() {
        Object obj = get("borderColor");
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    public String getBorderStyle() {
        Object obj = get("borderStyle");
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    public boolean isSticky() {
        Object obj = get("position");
        if (obj == null) {
            return false;
        }
        return obj.toString().equals("sticky");
    }

    public boolean isFixed() {
        Object obj = get("position");
        if (obj == null) {
            return false;
        }
        return obj.toString().equals(Constants.Value.FIXED);
    }

    public float getLeft() {
        float f = WXUtils.getFloat(get("left"));
        if (WXUtils.isUndefined(f)) {
            return Float.NaN;
        }
        return f;
    }

    public float getRight() {
        float f = WXUtils.getFloat(get("right"));
        if (WXUtils.isUndefined(f)) {
            return Float.NaN;
        }
        return f;
    }

    public float getTop() {
        float f = WXUtils.getFloat(get("top"));
        if (WXUtils.isUndefined(f)) {
            return Float.NaN;
        }
        return f;
    }

    public float getBottom() {
        float f = WXUtils.getFloat(get("bottom"));
        if (WXUtils.isUndefined(f)) {
            return Float.NaN;
        }
        return f;
    }

    public String getBackgroundColor() {
        Object obj = get("backgroundColor");
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    public int getTimeFontSize() {
        int i = WXUtils.getInt(get("timeFontSize"));
        if (i <= 0) {
            return 32;
        }
        return i;
    }

    public float getOpacity() {
        Object obj = get("opacity");
        if (obj == null) {
            return 1.0f;
        }
        return WXUtils.getFloat(obj);
    }

    public String getOverflow() {
        Object obj = get("overflow");
        if (obj == null) {
            return "visible";
        }
        return obj.toString();
    }

    public boolean equals(Object obj) {
        return this.mStyles.equals(obj);
    }

    public int hashCode() {
        return this.mStyles.hashCode();
    }

    public void clear() {
        this.mStyles.clear();
    }

    public boolean containsKey(Object obj) {
        return this.mStyles.containsKey(obj);
    }

    public boolean containsValue(Object obj) {
        return this.mStyles.containsValue(obj);
    }

    @NonNull
    public Set<Map.Entry<String, Object>> entrySet() {
        return this.mStyles.entrySet();
    }

    public Object get(Object obj) {
        return this.mStyles.get(obj);
    }

    public boolean isEmpty() {
        return this.mStyles.isEmpty();
    }

    @NonNull
    public Set<String> keySet() {
        return this.mStyles.keySet();
    }

    public Object put(String str, Object obj) {
        if (addBindingStyleIfStatement(str, obj)) {
            return null;
        }
        return this.mStyles.put(str, obj);
    }

    public void putAll(Map<? extends String, ?> map) {
        this.mStyles.putAll(map);
    }

    public void putAll(Map<? extends String, ?> map, boolean z) {
        this.mStyles.putAll(map);
        if (!z) {
            processPesudoClasses(map);
        }
    }

    public void updateStyle(Map<? extends String, ?> map, boolean z) {
        parseBindingStylesStatements(map);
        putAll(map, z);
    }

    public Map<String, Object> getPesudoResetStyles() {
        if (this.mPesudoResetStyleMap == null) {
            this.mPesudoResetStyleMap = new ArrayMap();
        }
        return this.mPesudoResetStyleMap;
    }

    public Map<String, Map<String, Object>> getPesudoStyles() {
        if (this.mPesudoStyleMap == null) {
            this.mPesudoStyleMap = new ArrayMap();
        }
        return this.mPesudoStyleMap;
    }

    /* access modifiers changed from: package-private */
    public <T extends String, V> void processPesudoClasses(Map<T, V> map) {
        ArrayMap arrayMap = null;
        for (Map.Entry next : map.entrySet()) {
            String str = (String) next.getKey();
            int indexOf = str.indexOf(":");
            if (indexOf > 0) {
                initPesudoMapsIfNeed(map);
                String substring = str.substring(indexOf);
                if (substring.equals(Constants.PSEUDO.ENABLED)) {
                    String substring2 = str.substring(0, indexOf);
                    if (arrayMap == null) {
                        arrayMap = new ArrayMap();
                    }
                    arrayMap.put(substring2, next.getValue());
                    this.mPesudoResetStyleMap.put(substring2, next.getValue());
                } else {
                    String replace = substring.replace(Constants.PSEUDO.ENABLED, "");
                    Map map2 = this.mPesudoStyleMap.get(replace);
                    if (map2 == null) {
                        map2 = new ArrayMap();
                        this.mPesudoStyleMap.put(replace, map2);
                    }
                    map2.put(str.substring(0, indexOf), next.getValue());
                }
            }
        }
        if (arrayMap != null && !arrayMap.isEmpty()) {
            this.mStyles.putAll(arrayMap);
        }
    }

    public Object remove(Object obj) {
        return this.mStyles.remove(obj);
    }

    public int size() {
        return this.mStyles.size();
    }

    @NonNull
    public Collection<Object> values() {
        return this.mStyles.values();
    }

    private void initPesudoMapsIfNeed(Map<? extends String, ?> map) {
        if (this.mPesudoStyleMap == null) {
            this.mPesudoStyleMap = new ArrayMap();
        }
        if (this.mPesudoResetStyleMap == null) {
            this.mPesudoResetStyleMap = new ArrayMap();
        }
        if (this.mPesudoResetStyleMap.isEmpty()) {
            this.mPesudoResetStyleMap.putAll(map);
        }
    }

    public void parseStatements() {
        if (this.mStyles != null) {
            this.mStyles = parseBindingStylesStatements(this.mStyles);
        }
    }

    private Map<String, Object> parseBindingStylesStatements(Map map) {
        if (map == null || map.size() == 0) {
            return map;
        }
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            if (addBindingStyleIfStatement((String) entry.getKey(), entry.getValue())) {
                if (this.mPesudoStyleMap != null) {
                    this.mPesudoStyleMap.remove(entry.getKey());
                }
                if (this.mPesudoResetStyleMap != null) {
                    this.mPesudoResetStyleMap.remove(entry.getKey());
                }
                it.remove();
            }
        }
        return map;
    }

    private boolean addBindingStyleIfStatement(String str, Object obj) {
        if (!ELUtils.isBinding(obj)) {
            return false;
        }
        if (this.mBindingStyle == null) {
            this.mBindingStyle = new ArrayMap<>();
        }
        this.mBindingStyle.put(str, ELUtils.bindingBlock(obj));
        return true;
    }

    public ArrayMap<String, Object> getBindingStyle() {
        return this.mBindingStyle;
    }

    public WXStyle clone() {
        WXStyle wXStyle = new WXStyle();
        wXStyle.mStyles.putAll(this.mStyles);
        if (this.mBindingStyle != null) {
            wXStyle.mBindingStyle = new ArrayMap<>((SimpleArrayMap) this.mBindingStyle);
        }
        if (this.mPesudoStyleMap != null) {
            wXStyle.mPesudoStyleMap = new ArrayMap();
            for (Map.Entry next : this.mPesudoStyleMap.entrySet()) {
                ArrayMap arrayMap = new ArrayMap();
                arrayMap.putAll((Map) next.getValue());
                wXStyle.mPesudoStyleMap.put(next.getKey(), arrayMap);
            }
        }
        if (this.mPesudoResetStyleMap != null) {
            wXStyle.mPesudoResetStyleMap = new ArrayMap();
            wXStyle.mPesudoResetStyleMap.putAll(this.mPesudoResetStyleMap);
        }
        return wXStyle;
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY})
    public String toString() {
        return this.mStyles.toString();
    }
}
