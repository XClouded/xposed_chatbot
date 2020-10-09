package com.taobao.weex.devtools.inspector.elements.android;

import android.view.View;
import android.view.ViewDebug;
import com.taobao.weex.devtools.common.LogUtil;
import com.taobao.weex.devtools.common.StringUtil;
import com.taobao.weex.devtools.common.android.ResourcesUtil;
import com.taobao.weex.devtools.inspector.elements.AbstractChainedDescriptor;
import com.taobao.weex.devtools.inspector.elements.AttributeAccumulator;
import com.taobao.weex.devtools.inspector.elements.StyleAccumulator;
import com.taobao.weex.devtools.inspector.helper.IntegerFormatter;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.ui.component.WXComponent;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;

final class ViewDescriptor extends AbstractChainedDescriptor<View> implements HighlightableDescriptor {
    private static final String ID_NAME = "id";
    private static final String NONE_MAPPING = "<no mapping>";
    private static final String NONE_VALUE = "(none)";
    private final MethodInvoker mMethodInvoker;
    @GuardedBy("this")
    @Nullable
    private volatile List<ViewCSSProperty> mViewProperties;
    @Nullable
    private Pattern mWordBoundaryPattern;

    private Pattern getWordBoundaryPattern() {
        if (this.mWordBoundaryPattern == null) {
            this.mWordBoundaryPattern = Pattern.compile("(?<=\\p{Lower})(?=\\p{Upper})");
        }
        return this.mWordBoundaryPattern;
    }

    private List<ViewCSSProperty> getViewProperties() {
        if (this.mViewProperties == null) {
            synchronized (this) {
                if (this.mViewProperties == null) {
                    ArrayList arrayList = new ArrayList();
                    for (Method method : View.class.getDeclaredMethods()) {
                        ViewDebug.ExportedProperty exportedProperty = (ViewDebug.ExportedProperty) method.getAnnotation(ViewDebug.ExportedProperty.class);
                        if (exportedProperty != null) {
                            arrayList.add(new MethodBackedCSSProperty(method, convertViewPropertyNameToCSSName(method.getName()), exportedProperty));
                        }
                    }
                    for (Field field : View.class.getDeclaredFields()) {
                        ViewDebug.ExportedProperty exportedProperty2 = (ViewDebug.ExportedProperty) field.getAnnotation(ViewDebug.ExportedProperty.class);
                        if (exportedProperty2 != null) {
                            arrayList.add(new FieldBackedCSSProperty(field, convertViewPropertyNameToCSSName(field.getName()), exportedProperty2));
                        }
                    }
                    Collections.sort(arrayList, new Comparator<ViewCSSProperty>() {
                        public int compare(ViewCSSProperty viewCSSProperty, ViewCSSProperty viewCSSProperty2) {
                            return viewCSSProperty.getCSSName().compareTo(viewCSSProperty2.getCSSName());
                        }
                    });
                    this.mViewProperties = Collections.unmodifiableList(arrayList);
                }
            }
        }
        return this.mViewProperties;
    }

    public ViewDescriptor() {
        this(new MethodInvoker());
    }

    public ViewDescriptor(MethodInvoker methodInvoker) {
        this.mMethodInvoker = methodInvoker;
    }

    /* access modifiers changed from: protected */
    public String onGetNodeName(View view) {
        String name = view.getClass().getName();
        return StringUtil.removePrefix(name, "android.view.", StringUtil.removePrefix(name, "android.widget."));
    }

    /* access modifiers changed from: protected */
    public void onGetAttributes(View view, AttributeAccumulator attributeAccumulator) {
        String idAttribute = getIdAttribute(view);
        if (idAttribute != null) {
            attributeAccumulator.store("id", idAttribute);
        }
    }

    /* access modifiers changed from: protected */
    public void onSetAttributesAsText(View view, String str) {
        for (Map.Entry next : parseSetAttributesAsTextArg(str).entrySet()) {
            this.mMethodInvoker.invoke(view, "set" + capitalize((String) next.getKey()), (String) next.getValue());
        }
    }

    @Nullable
    private static String getIdAttribute(View view) {
        int id = view.getId();
        if (id == -1) {
            return null;
        }
        return ResourcesUtil.getIdStringQuietly(view, view.getResources(), id);
    }

    public View getViewForHighlighting(Object obj) {
        return (View) obj;
    }

    /* access modifiers changed from: protected */
    public void onGetStyles(View view, StyleAccumulator styleAccumulator) {
        List<ViewCSSProperty> viewProperties = getViewProperties();
        int size = viewProperties.size();
        for (int i = 0; i < size; i++) {
            ViewCSSProperty viewCSSProperty = viewProperties.get(i);
            try {
                getStyleFromValue(view, viewCSSProperty.getCSSName(), viewCSSProperty.getValue(view), viewCSSProperty.getAnnotation(), styleAccumulator);
            } catch (IllegalAccessException | InvocationTargetException e) {
                LogUtil.e(e, "failed to get style property " + viewCSSProperty.getCSSName() + " of element= " + view.toString());
            }
        }
    }

    private static boolean canIntBeMappedToString(@Nullable ViewDebug.ExportedProperty exportedProperty) {
        return (exportedProperty == null || exportedProperty.mapping() == null || exportedProperty.mapping().length <= 0) ? false : true;
    }

    private static String mapIntToStringUsingAnnotation(int i, @Nullable ViewDebug.ExportedProperty exportedProperty) {
        if (canIntBeMappedToString(exportedProperty)) {
            for (ViewDebug.IntToString intToString : exportedProperty.mapping()) {
                if (intToString.from() == i) {
                    return intToString.to();
                }
            }
            return NONE_MAPPING;
        }
        throw new IllegalStateException("Cannot map using this annotation");
    }

    private static boolean canFlagsBeMappedToString(@Nullable ViewDebug.ExportedProperty exportedProperty) {
        return (exportedProperty == null || exportedProperty.flagMapping() == null || exportedProperty.flagMapping().length <= 0) ? false : true;
    }

    private static String mapFlagsToStringUsingAnnotation(int i, @Nullable ViewDebug.ExportedProperty exportedProperty) {
        if (canFlagsBeMappedToString(exportedProperty)) {
            StringBuilder sb = null;
            boolean z = false;
            for (ViewDebug.FlagToString flagToString : exportedProperty.flagMapping()) {
                if (flagToString.outputIf() == ((flagToString.mask() & i) == flagToString.equals())) {
                    if (sb == null) {
                        sb = new StringBuilder();
                    }
                    if (z) {
                        sb.append(" | ");
                    }
                    sb.append(flagToString.name());
                    z = true;
                }
            }
            if (z) {
                return sb.toString();
            }
            return NONE_MAPPING;
        }
        throw new IllegalStateException("Cannot map using this annotation");
    }

    private static boolean isDefaultValue(Float f) {
        return f.floatValue() == 0.0f;
    }

    private static boolean isDefaultValue(Integer num, @Nullable ViewDebug.ExportedProperty exportedProperty) {
        if (canFlagsBeMappedToString(exportedProperty) || canIntBeMappedToString(exportedProperty) || num.intValue() != 0) {
            return false;
        }
        return true;
    }

    private String convertViewPropertyNameToCSSName(String str) {
        String[] split = getWordBoundaryPattern().split(str);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            if (!split[i].equals("get") && !split[i].equals(WXComponent.PROP_FS_MATCH_PARENT)) {
                sb.append(split[i].toLowerCase());
                if (i < split.length - 1) {
                    sb.append('-');
                }
            }
        }
        return sb.toString();
    }

    private void getStyleFromValue(View view, String str, Object obj, @Nullable ViewDebug.ExportedProperty exportedProperty, StyleAccumulator styleAccumulator) {
        if (str.equals("id")) {
            getIdStyle(view, styleAccumulator);
        } else if (obj instanceof Integer) {
            getStyleFromInteger(str, (Integer) obj, exportedProperty, styleAccumulator);
        } else if (obj instanceof Float) {
            getStyleFromFloat(str, (Float) obj, exportedProperty, styleAccumulator);
        } else {
            getStylesFromObject(view, str, obj, exportedProperty, styleAccumulator);
        }
    }

    private void getIdStyle(View view, StyleAccumulator styleAccumulator) {
        String idAttribute = getIdAttribute(view);
        if (idAttribute == null) {
            styleAccumulator.store("id", NONE_VALUE, false);
        } else {
            styleAccumulator.store("id", idAttribute, false);
        }
    }

    private void getStyleFromInteger(String str, Integer num, @Nullable ViewDebug.ExportedProperty exportedProperty, StyleAccumulator styleAccumulator) {
        String format = IntegerFormatter.getInstance().format(num, exportedProperty);
        if (canIntBeMappedToString(exportedProperty)) {
            styleAccumulator.store(str, format + " (" + mapIntToStringUsingAnnotation(num.intValue(), exportedProperty) + Operators.BRACKET_END_STR, false);
        } else if (canFlagsBeMappedToString(exportedProperty)) {
            styleAccumulator.store(str, format + " (" + mapFlagsToStringUsingAnnotation(num.intValue(), exportedProperty) + Operators.BRACKET_END_STR, false);
        } else {
            styleAccumulator.store(str, format, isDefaultValue(num, exportedProperty));
        }
    }

    private void getStyleFromFloat(String str, Float f, @Nullable ViewDebug.ExportedProperty exportedProperty, StyleAccumulator styleAccumulator) {
        styleAccumulator.store(str, String.valueOf(f), isDefaultValue(f));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x006a, code lost:
        if (r8.equals("topMargin") != false) goto L_0x0078;
     */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x007b  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0083  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0086  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0089  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x008c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void getStylesFromObject(android.view.View r15, java.lang.String r16, java.lang.Object r17, @javax.annotation.Nullable android.view.ViewDebug.ExportedProperty r18, com.taobao.weex.devtools.inspector.elements.StyleAccumulator r19) {
        /*
            r14 = this;
            r1 = r17
            if (r18 == 0) goto L_0x00e1
            boolean r2 = r18.deepExport()
            if (r2 == 0) goto L_0x00e1
            if (r1 != 0) goto L_0x000e
            goto L_0x00e1
        L_0x000e:
            java.lang.Class r2 = r17.getClass()
            java.lang.reflect.Field[] r2 = r2.getFields()
            int r3 = r2.length
            r4 = 0
            r5 = 0
        L_0x0019:
            if (r5 >= r3) goto L_0x00df
            r6 = r2[r5]
            int r7 = r6.getModifiers()
            boolean r7 = java.lang.reflect.Modifier.isStatic(r7)
            if (r7 == 0) goto L_0x002a
            r7 = r14
            goto L_0x00b6
        L_0x002a:
            r7 = 1
            r6.setAccessible(r7)     // Catch:{ IllegalAccessException -> 0x00ba }
            java.lang.Object r11 = r6.get(r1)     // Catch:{ IllegalAccessException -> 0x00ba }
            java.lang.String r8 = r6.getName()
            r9 = -1
            int r10 = r8.hashCode()
            r12 = -599904534(0xffffffffdc3e2eea, float:-2.14127313E17)
            if (r10 == r12) goto L_0x006d
            r12 = -414179485(0xffffffffe7501f63, float:-9.828312E23)
            if (r10 == r12) goto L_0x0064
            r7 = 1928835221(0x72f7b095, float:9.812003E30)
            if (r10 == r7) goto L_0x005a
            r7 = 2064613305(0x7b0f7fb9, float:7.45089E35)
            if (r10 == r7) goto L_0x0050
            goto L_0x0077
        L_0x0050:
            java.lang.String r7 = "bottomMargin"
            boolean r7 = r8.equals(r7)
            if (r7 == 0) goto L_0x0077
            r7 = 0
            goto L_0x0078
        L_0x005a:
            java.lang.String r7 = "leftMargin"
            boolean r7 = r8.equals(r7)
            if (r7 == 0) goto L_0x0077
            r7 = 2
            goto L_0x0078
        L_0x0064:
            java.lang.String r10 = "topMargin"
            boolean r10 = r8.equals(r10)
            if (r10 == 0) goto L_0x0077
            goto L_0x0078
        L_0x006d:
            java.lang.String r7 = "rightMargin"
            boolean r7 = r8.equals(r7)
            if (r7 == 0) goto L_0x0077
            r7 = 3
            goto L_0x0078
        L_0x0077:
            r7 = -1
        L_0x0078:
            switch(r7) {
                case 0: goto L_0x008c;
                case 1: goto L_0x0089;
                case 2: goto L_0x0086;
                case 3: goto L_0x0083;
                default: goto L_0x007b;
            }
        L_0x007b:
            java.lang.String r7 = r18.prefix()
            if (r7 != 0) goto L_0x0091
        L_0x0081:
            r7 = r14
            goto L_0x00a1
        L_0x0083:
            java.lang.String r7 = "margin-right"
            goto L_0x008e
        L_0x0086:
            java.lang.String r7 = "margin-left"
            goto L_0x008e
        L_0x0089:
            java.lang.String r7 = "margin-top"
            goto L_0x008e
        L_0x008c:
            java.lang.String r7 = "margin-bottom"
        L_0x008e:
            r10 = r7
            r7 = r14
            goto L_0x00a6
        L_0x0091:
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            r9.append(r7)
            r9.append(r8)
            java.lang.String r8 = r9.toString()
            goto L_0x0081
        L_0x00a1:
            java.lang.String r8 = r14.convertViewPropertyNameToCSSName(r8)
            r10 = r8
        L_0x00a6:
            java.lang.Class<android.view.ViewDebug$ExportedProperty> r8 = android.view.ViewDebug.ExportedProperty.class
            java.lang.annotation.Annotation r6 = r6.getAnnotation(r8)
            r12 = r6
            android.view.ViewDebug$ExportedProperty r12 = (android.view.ViewDebug.ExportedProperty) r12
            r8 = r14
            r9 = r15
            r13 = r19
            r8.getStyleFromValue(r9, r10, r11, r12, r13)
        L_0x00b6:
            int r5 = r5 + 1
            goto L_0x0019
        L_0x00ba:
            r0 = move-exception
            r7 = r14
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "failed to get property of name: \""
            r2.append(r3)
            r3 = r16
            r2.append(r3)
            java.lang.String r3 = "\" of object: "
            r2.append(r3)
            java.lang.String r1 = java.lang.String.valueOf(r17)
            r2.append(r1)
            java.lang.String r1 = r2.toString()
            com.taobao.weex.devtools.common.LogUtil.e((java.lang.Throwable) r0, (java.lang.String) r1)
            return
        L_0x00df:
            r7 = r14
            return
        L_0x00e1:
            r7 = r14
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.devtools.inspector.elements.android.ViewDescriptor.getStylesFromObject(android.view.View, java.lang.String, java.lang.Object, android.view.ViewDebug$ExportedProperty, com.taobao.weex.devtools.inspector.elements.StyleAccumulator):void");
    }

    private static String capitalize(String str) {
        if (str == null || str.length() == 0 || Character.isTitleCase(str.charAt(0))) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str);
        sb.setCharAt(0, Character.toTitleCase(sb.charAt(0)));
        return sb.toString();
    }

    private final class FieldBackedCSSProperty extends ViewCSSProperty {
        private final Field mField;

        public FieldBackedCSSProperty(Field field, String str, @Nullable ViewDebug.ExportedProperty exportedProperty) {
            super(str, exportedProperty);
            this.mField = field;
            this.mField.setAccessible(true);
        }

        public Object getValue(View view) throws InvocationTargetException, IllegalAccessException {
            return this.mField.get(view);
        }
    }

    private final class MethodBackedCSSProperty extends ViewCSSProperty {
        private final Method mMethod;

        public MethodBackedCSSProperty(Method method, String str, @Nullable ViewDebug.ExportedProperty exportedProperty) {
            super(str, exportedProperty);
            this.mMethod = method;
            this.mMethod.setAccessible(true);
        }

        public Object getValue(View view) throws InvocationTargetException, IllegalAccessException {
            return this.mMethod.invoke(view, new Object[0]);
        }
    }

    private abstract class ViewCSSProperty {
        private final ViewDebug.ExportedProperty mAnnotation;
        private final String mCSSName;

        public abstract Object getValue(View view) throws InvocationTargetException, IllegalAccessException;

        public ViewCSSProperty(String str, @Nullable ViewDebug.ExportedProperty exportedProperty) {
            this.mCSSName = str;
            this.mAnnotation = exportedProperty;
        }

        public final String getCSSName() {
            return this.mCSSName;
        }

        @Nullable
        public final ViewDebug.ExportedProperty getAnnotation() {
            return this.mAnnotation;
        }
    }
}
