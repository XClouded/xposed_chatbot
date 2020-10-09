package com.taobao.weex.analyzer.core.inspector.view;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.taobao.android.dinamic.property.DAttrConstant;
import com.taobao.weex.dom.CSSShorthand;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.view.WXTextView;
import com.taobao.weex.ui.view.border.BorderDrawable;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

class ViewPropertiesSupplier {

    public interface BoxModelConstants {
        public static final String ALIGN_ITEMS = "alignItems";
        public static final String ALIGN_SELF = "alignSelf";
        public static final String AUTOFOCUS = "autofocus";
        public static final String AUTO_PLAY = "autoPlay";
        public static final String BACKGROUND_COLOR = "backgroundColor";
        public static final String BACKGROUND_IMAGE = "backgroundImage";
        public static final String BORDER_BOTTOM_COLOR = "borderBottomColor";
        public static final String BORDER_BOTTOM_LEFT_RADIUS = "borderBottomLeftRadius";
        public static final String BORDER_BOTTOM_RIGHT_RADIUS = "borderBottomRightRadius";
        public static final String BORDER_BOTTOM_STYLE = "borderBottomStyle";
        public static final String BORDER_BOTTOM_WIDTH = "borderBottomWidth";
        public static final String BORDER_COLOR = "borderColor";
        public static final String BORDER_LEFT_COLOR = "borderLeftColor";
        public static final String BORDER_LEFT_STYLE = "borderLeftStyle";
        public static final String BORDER_LEFT_WIDTH = "borderLeftWidth";
        public static final String BORDER_RADIUS = "borderRadius";
        public static final String BORDER_RIGHT_COLOR = "borderRightColor";
        public static final String BORDER_RIGHT_STYLE = "borderRightStyle";
        public static final String BORDER_RIGHT_WIDTH = "borderRightWidth";
        public static final String BORDER_STYLE = "borderStyle";
        public static final String BORDER_TOP_COLOR = "borderTopColor";
        public static final String BORDER_TOP_LEFT_RADIUS = "borderTopLeftRadius";
        public static final String BORDER_TOP_RIGHT_RADIUS = "borderTopRightRadius";
        public static final String BORDER_TOP_STYLE = "borderTopStyle";
        public static final String BORDER_TOP_WIDTH = "borderTopWidth";
        public static final String BORDER_WIDTH = "borderWidth";
        public static final String BOTTOM = "bottom";
        public static final String CHECKED = "checked";
        public static final String COLOR = "color";
        public static final String DEFAULT_HEIGHT = "defaultHeight";
        public static final String DEFAULT_WIDTH = "defaultWidth";
        public static final String DISABLED = "disabled";
        public static final String DISPLAY = "display";
        public static final String DISTANCE_Y = "dy";
        public static final String ELEVATION = "elevation";
        public static final String ELLIPSIS = "ellipsis";
        public static final String FILTER = "filter";
        public static final String FLEX = "flex";
        public static final String FLEX_DIRECTION = "flexDirection";
        public static final String FLEX_WRAP = "flexWrap";
        public static final String FONT_FACE = "fontFace";
        public static final String FONT_FAMILY = "fontFamily";
        public static final String FONT_SIZE = "fontSize";
        public static final String FONT_STYLE = "fontStyle";
        public static final String FONT_WEIGHT = "fontWeight";
        public static final String HEIGHT = "height";
        public static final String HREF = "href";
        public static final String IMAGE_QUALITY = "imageQuality";
        public static final String IMAGE_SHARPEN = "imageSharpen";
        public static final String INDEX = "index";
        public static final String INTERVAL = "interval";
        public static final String ITEM_COLOR = "itemColor";
        public static final String ITEM_SELECTED_COLOR = "itemSelectedColor";
        public static final String ITEM_SIZE = "itemSize";
        public static final String JUSTIFY_CONTENT = "justifyContent";
        public static final String LEFT = "left";
        public static final String LINES = "lines";
        public static final String LINE_HEIGHT = "lineHeight";
        public static final String LOADMOREOFFSET = "loadmoreoffset";
        public static final String LOADMORERETRY = "loadmoreretry";
        public static final String MARGIN = "margin";
        public static final String MARGIN_BOTTOM = "marginBottom";
        public static final String MARGIN_LEFT = "marginLeft";
        public static final String MARGIN_RIGHT = "marginRight";
        public static final String MARGIN_TOP = "marginTop";
        public static final String MAX = "max";
        public static final String MAXLENGTH = "maxlength";
        public static final String MAX_HEIGHT = "maxHeight";
        public static final String MAX_LENGTH = "maxLength";
        public static final String MAX_WIDTH = "maxWidth";
        public static final String MIN = "min";
        public static final String MIN_HEIGHT = "minHeight";
        public static final String MIN_WIDTH = "minWidth";
        public static final String NAV_BAR_VISIBILITY = "hidden";
        public static final String OFFSET_X_ACCURACY = "offsetXAccuracy";
        public static final String OFFSET_X_RATIO = "offsetXRatio";
        public static final String OPACITY = "opacity";
        public static final String OVERFLOW = "overflow";
        public static final String PADDING = "padding";
        public static final String PADDING_BOTTOM = "paddingBottom";
        public static final String PADDING_LEFT = "paddingLeft";
        public static final String PADDING_RIGHT = "paddingRight";
        public static final String PADDING_TOP = "paddingTop";
        public static final String PLACEHOLDER = "placeholder";
        public static final String PLACEHOLDER_COLOR = "placeholderColor";
        public static final String PLACE_HOLDER = "placeHolder";
        public static final String PLAY_STATUS = "playStatus";
        public static final String POSITION = "position";
        public static final String PREFIX = "prefix";
        public static final String PREVENT_MOVE_EVENT = "preventMoveEvent";
        public static final String PULLING_DISTANCE = "pullingDistance";
        public static final String QUALITY = "quality";
        public static final String RECYCLE_IMAGE = "recycleImage";
        public static final String RESIZE = "resize";
        public static final String RESIZE_MODE = "resizeMode";
        public static final String RIGHT = "right";
        public static final String ROWS = "rows";
        public static final String SCOPE = "scope";
        public static final String SCROLLABLE = "scrollable";
        public static final String SCROLL_DIRECTION = "scrollDirection";
        public static final String SHARPEN = "sharpen";
        public static final String SHOW_INDICATORS = "showIndicators";
        public static final String SHOW_LOADING = "show-loading";
        public static final String SHOW_SCROLLBAR = "showScrollbar";
        public static final String SINGLELINE = "singleline";
        public static final String SRC = "src";
        public static final String SUFFIX = "suffix";
        public static final String TEXT_ALIGN = "textAlign";
        public static final String TEXT_DECORATION = "textDecoration";
        public static final String TEXT_OVERFLOW = "textOverflow";
        public static final String TOP = "top";
        public static final String TYPE = "type";
        public static final String VALUE = "value";
        public static final String VIEW_HEIGHT = "viewHeight";
        public static final String VISIBILITY = "visibility";
        public static final String WIDTH = "width";
    }

    ViewPropertiesSupplier() {
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public Map<String, String> supplyPropertiesFromVirtualView(@NonNull WXComponent wXComponent) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Map.Entry next : wXComponent.getStyles().entrySet()) {
            if (next.getValue() != null) {
                linkedHashMap.put(next.getKey(), next.getValue().toString());
            }
        }
        linkedHashMap.put("marginLeft", String.valueOf(wXComponent.getMargin().get(CSSShorthand.EDGE.LEFT)));
        linkedHashMap.put("marginRight", String.valueOf(wXComponent.getMargin().get(CSSShorthand.EDGE.RIGHT)));
        linkedHashMap.put("marginTop", String.valueOf(wXComponent.getMargin().get(CSSShorthand.EDGE.TOP)));
        linkedHashMap.put("marginBottom", String.valueOf(wXComponent.getMargin().get(CSSShorthand.EDGE.BOTTOM)));
        linkedHashMap.put("paddingLeft", String.valueOf(wXComponent.getPadding().get(CSSShorthand.EDGE.LEFT)));
        linkedHashMap.put("paddingRight", String.valueOf(wXComponent.getPadding().get(CSSShorthand.EDGE.RIGHT)));
        linkedHashMap.put("paddingTop", String.valueOf(wXComponent.getPadding().get(CSSShorthand.EDGE.TOP)));
        linkedHashMap.put("paddingBottom", String.valueOf(wXComponent.getPadding().get(CSSShorthand.EDGE.BOTTOM)));
        linkedHashMap.put("borderLeftWidth", String.valueOf(wXComponent.getBorder().get(CSSShorthand.EDGE.LEFT)));
        linkedHashMap.put("borderRightWidth", String.valueOf(wXComponent.getBorder().get(CSSShorthand.EDGE.RIGHT)));
        linkedHashMap.put("borderTopWidth", String.valueOf(wXComponent.getBorder().get(CSSShorthand.EDGE.TOP)));
        linkedHashMap.put("borderBottomWidth", String.valueOf(wXComponent.getBorder().get(CSSShorthand.EDGE.BOTTOM)));
        for (Map.Entry next2 : wXComponent.getAttrs().entrySet()) {
            if (next2.getValue() != null) {
                linkedHashMap.put(next2.getKey(), next2.getValue().toString());
            }
        }
        return Collections.unmodifiableMap(linkedHashMap);
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public Map<String, String> supplyPropertiesFromNativeView(@NonNull View view) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put("left", String.valueOf(view.getLeft()));
        linkedHashMap.put("top", String.valueOf(view.getTop()));
        linkedHashMap.put("right", String.valueOf(view.getRight()));
        linkedHashMap.put("bottom", String.valueOf(view.getBottom()));
        linkedHashMap.put("width", String.valueOf(view.getWidth()));
        linkedHashMap.put("height", String.valueOf(view.getHeight()));
        linkedHashMap.put("paddingLeft", String.valueOf(view.getPaddingLeft()));
        linkedHashMap.put("paddingTop", String.valueOf(view.getPaddingTop()));
        linkedHashMap.put("paddingRight", String.valueOf(view.getPaddingRight()));
        linkedHashMap.put("paddingBottom", String.valueOf(view.getPaddingBottom()));
        linkedHashMap.put("visibility", view.getVisibility() == 0 ? "visible" : DAttrConstant.VISIBILITY_INVISIBLE);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null && (layoutParams instanceof ViewGroup.MarginLayoutParams)) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            linkedHashMap.put("marginLeft", String.valueOf(marginLayoutParams.leftMargin));
            linkedHashMap.put("marginTop", String.valueOf(marginLayoutParams.topMargin));
            linkedHashMap.put("marginRight", String.valueOf(marginLayoutParams.rightMargin));
            linkedHashMap.put("marginBottom", String.valueOf(marginLayoutParams.bottomMargin));
        }
        linkedHashMap.put("borderLeftWidth", (Object) null);
        linkedHashMap.put("borderRightWidth", (Object) null);
        linkedHashMap.put("borderTopWidth", (Object) null);
        linkedHashMap.put("borderBottomWidth", (Object) null);
        if (view instanceof WXTextView) {
            WXTextView wXTextView = (WXTextView) view;
            if (wXTextView.getText() != null) {
                linkedHashMap.put("value", wXTextView.getText().toString());
            }
        }
        if (view.getBackground() != null) {
            Drawable background = view.getBackground();
            if (background instanceof BorderDrawable) {
                BorderDrawable borderDrawable = (BorderDrawable) background;
                linkedHashMap.put("backgroundColor", String.format(Locale.CHINA, "#%06X", new Object[]{Integer.valueOf(borderDrawable.getColor() & 16777215)}));
                linkedHashMap.put("opacity", String.valueOf(borderDrawable.getOpacity()));
                int borderColorNative = getBorderColorNative(borderDrawable, CSSShorthand.EDGE.ALL);
                if (borderColorNative != 0) {
                    int i = borderColorNative & 16777215;
                    linkedHashMap.put("borderLeftColor", String.format(Locale.CHINA, "#%06X", new Object[]{Integer.valueOf(i)}));
                    linkedHashMap.put("borderTopColor", String.format(Locale.CHINA, "#%06X", new Object[]{Integer.valueOf(i)}));
                    linkedHashMap.put("borderRightColor", String.format(Locale.CHINA, "#%06X", new Object[]{Integer.valueOf(i)}));
                    linkedHashMap.put("borderBottomColor", String.format(Locale.CHINA, "#%06X", new Object[]{Integer.valueOf(i)}));
                } else {
                    linkedHashMap.put("borderLeftColor", String.format(Locale.CHINA, "#%06X", new Object[]{Integer.valueOf(getBorderColorNative(borderDrawable, CSSShorthand.EDGE.LEFT) & 16777215)}));
                    linkedHashMap.put("borderTopColor", String.format(Locale.CHINA, "#%06X", new Object[]{Integer.valueOf(getBorderColorNative(borderDrawable, CSSShorthand.EDGE.TOP) & 16777215)}));
                    linkedHashMap.put("borderRightColor", String.format(Locale.CHINA, "#%06X", new Object[]{Integer.valueOf(getBorderColorNative(borderDrawable, CSSShorthand.EDGE.RIGHT) & 16777215)}));
                    linkedHashMap.put("borderBottomColor", String.format(Locale.CHINA, "#%06X", new Object[]{Integer.valueOf(getBorderColorNative(borderDrawable, CSSShorthand.EDGE.BOTTOM) & 16777215)}));
                }
            }
        }
        return Collections.unmodifiableMap(linkedHashMap);
    }

    private static float getBorderWidthNative(@NonNull BorderDrawable borderDrawable, int i) {
        try {
            Method declaredMethod = borderDrawable.getClass().getDeclaredMethod("getBorderWidth", new Class[]{Integer.TYPE});
            declaredMethod.setAccessible(true);
            return ((Float) declaredMethod.invoke(borderDrawable, new Object[]{Integer.valueOf(i)})).floatValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0f;
        }
    }

    private static int getBorderColorNative(@NonNull BorderDrawable borderDrawable, CSSShorthand.EDGE edge) {
        try {
            Method declaredMethod = borderDrawable.getClass().getDeclaredMethod("getBorderColor", new Class[]{CSSShorthand.EDGE.class});
            declaredMethod.setAccessible(true);
            return ((Integer) declaredMethod.invoke(borderDrawable, new Object[]{edge})).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
