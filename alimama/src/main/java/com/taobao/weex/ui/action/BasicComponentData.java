package com.taobao.weex.ui.action;

import android.view.View;
import androidx.annotation.NonNull;
import com.taobao.ju.track.csv.CsvReader;
import com.taobao.weex.dom.CSSShorthand;
import com.taobao.weex.dom.WXAttr;
import com.taobao.weex.dom.WXEvent;
import com.taobao.weex.dom.WXStyle;
import com.taobao.weex.ui.component.list.template.jni.NativeRenderObjectUtils;
import com.taobao.weex.utils.WXUtils;
import java.util.Map;
import java.util.Set;

public class BasicComponentData<T extends View> {
    private WXAttr mAttributes;
    private CSSShorthand mBorders;
    public String mComponentType;
    private WXEvent mEvents;
    private CSSShorthand mMargins;
    private CSSShorthand mPaddings;
    public String mParentRef;
    public String mRef;
    private WXStyle mStyles;
    private long renderObjectPr = 0;

    public BasicComponentData(String str, String str2, String str3) {
        this.mRef = str;
        this.mComponentType = str2;
        this.mParentRef = str3;
    }

    public void addStyle(Map<String, Object> map) {
        addStyle(map, false);
    }

    public final void addStyle(Map<String, Object> map, boolean z) {
        if (map != null && !map.isEmpty()) {
            if (this.mStyles == null) {
                this.mStyles = new WXStyle(map);
            } else {
                this.mStyles.putAll(map, z);
            }
        }
    }

    public final void addAttr(Map<String, Object> map) {
        if (map != null && !map.isEmpty()) {
            if (this.mAttributes == null) {
                this.mAttributes = new WXAttr(map, 0);
            } else {
                this.mAttributes.putAll(map);
            }
        }
    }

    public final void addEvent(Set<String> set) {
        if (set != null && !set.isEmpty()) {
            if (this.mEvents == null) {
                this.mEvents = new WXEvent();
            }
            this.mEvents.addAll(set);
        }
    }

    public final void addShorthand(float[] fArr, CSSShorthand.TYPE type) {
        if (fArr == null) {
            fArr = new float[]{0.0f, 0.0f, 0.0f, 0.0f};
        }
        if (fArr.length == 4) {
            switch (type) {
                case MARGIN:
                    if (this.mMargins == null) {
                        this.mMargins = new CSSShorthand(fArr);
                        return;
                    } else {
                        this.mMargins.replace(fArr);
                        return;
                    }
                case PADDING:
                    if (this.mPaddings == null) {
                        this.mPaddings = new CSSShorthand(fArr);
                        return;
                    } else {
                        this.mPaddings.replace(fArr);
                        return;
                    }
                case BORDER:
                    if (this.mBorders == null) {
                        this.mBorders = new CSSShorthand(fArr);
                        return;
                    } else {
                        this.mBorders.replace(fArr);
                        return;
                    }
                default:
                    return;
            }
        }
    }

    public final void addShorthand(Map<String, String> map) {
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, String> key : map.entrySet()) {
                String str = (String) key.getKey();
                char c = 65535;
                switch (str.hashCode()) {
                    case -1971292586:
                        if (str.equals("borderRightWidth")) {
                            c = 7;
                            break;
                        }
                        break;
                    case -1501175880:
                        if (str.equals("paddingLeft")) {
                            c = CsvReader.Letters.VERTICAL_TAB;
                            break;
                        }
                        break;
                    case -1452542531:
                        if (str.equals("borderTopWidth")) {
                            c = 6;
                            break;
                        }
                        break;
                    case -1290574193:
                        if (str.equals("borderBottomWidth")) {
                            c = 8;
                            break;
                        }
                        break;
                    case -1081309778:
                        if (str.equals("margin")) {
                            c = 0;
                            break;
                        }
                        break;
                    case -1044792121:
                        if (str.equals("marginTop")) {
                            c = 2;
                            break;
                        }
                        break;
                    case -806339567:
                        if (str.equals("padding")) {
                            c = 10;
                            break;
                        }
                        break;
                    case -289173127:
                        if (str.equals("marginBottom")) {
                            c = 4;
                            break;
                        }
                        break;
                    case -223992013:
                        if (str.equals("borderLeftWidth")) {
                            c = 9;
                            break;
                        }
                        break;
                    case 90130308:
                        if (str.equals("paddingTop")) {
                            c = CsvReader.Letters.FORM_FEED;
                            break;
                        }
                        break;
                    case 202355100:
                        if (str.equals("paddingBottom")) {
                            c = 14;
                            break;
                        }
                        break;
                    case 713848971:
                        if (str.equals("paddingRight")) {
                            c = 13;
                            break;
                        }
                        break;
                    case 741115130:
                        if (str.equals("borderWidth")) {
                            c = 5;
                            break;
                        }
                        break;
                    case 975087886:
                        if (str.equals("marginRight")) {
                            c = 3;
                            break;
                        }
                        break;
                    case 1970934485:
                        if (str.equals("marginLeft")) {
                            c = 1;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        addMargin(CSSShorthand.EDGE.ALL, WXUtils.fastGetFloat(map.get(str)));
                        break;
                    case 1:
                        addMargin(CSSShorthand.EDGE.LEFT, WXUtils.fastGetFloat(map.get(str)));
                        break;
                    case 2:
                        addMargin(CSSShorthand.EDGE.TOP, WXUtils.fastGetFloat(map.get(str)));
                        break;
                    case 3:
                        addMargin(CSSShorthand.EDGE.RIGHT, WXUtils.fastGetFloat(map.get(str)));
                        break;
                    case 4:
                        addMargin(CSSShorthand.EDGE.BOTTOM, WXUtils.fastGetFloat(map.get(str)));
                        break;
                    case 5:
                        addBorder(CSSShorthand.EDGE.ALL, WXUtils.fastGetFloat(map.get(str)));
                        break;
                    case 6:
                        addBorder(CSSShorthand.EDGE.TOP, WXUtils.fastGetFloat(map.get(str)));
                        break;
                    case 7:
                        addBorder(CSSShorthand.EDGE.RIGHT, WXUtils.fastGetFloat(map.get(str)));
                        break;
                    case 8:
                        addBorder(CSSShorthand.EDGE.BOTTOM, WXUtils.fastGetFloat(map.get(str)));
                        break;
                    case 9:
                        addBorder(CSSShorthand.EDGE.LEFT, WXUtils.fastGetFloat(map.get(str)));
                        break;
                    case 10:
                        addPadding(CSSShorthand.EDGE.ALL, WXUtils.fastGetFloat(map.get(str)));
                        break;
                    case 11:
                        addPadding(CSSShorthand.EDGE.LEFT, WXUtils.fastGetFloat(map.get(str)));
                        break;
                    case 12:
                        addPadding(CSSShorthand.EDGE.TOP, WXUtils.fastGetFloat(map.get(str)));
                        break;
                    case 13:
                        addPadding(CSSShorthand.EDGE.RIGHT, WXUtils.fastGetFloat(map.get(str)));
                        break;
                    case 14:
                        addPadding(CSSShorthand.EDGE.BOTTOM, WXUtils.fastGetFloat(map.get(str)));
                        break;
                }
            }
        }
    }

    private void addMargin(CSSShorthand.EDGE edge, float f) {
        if (this.mMargins == null) {
            this.mMargins = new CSSShorthand();
        }
        this.mMargins.set(edge, f);
    }

    private void addPadding(CSSShorthand.EDGE edge, float f) {
        if (this.mPaddings == null) {
            this.mPaddings = new CSSShorthand();
        }
        this.mPaddings.set(edge, f);
    }

    private void addBorder(CSSShorthand.EDGE edge, float f) {
        if (this.mBorders == null) {
            this.mBorders = new CSSShorthand();
        }
        this.mBorders.set(edge, f);
    }

    @NonNull
    public final WXStyle getStyles() {
        if (this.mStyles == null) {
            this.mStyles = new WXStyle();
        }
        return this.mStyles;
    }

    @NonNull
    public final WXAttr getAttrs() {
        if (this.mAttributes == null) {
            this.mAttributes = new WXAttr();
        }
        return this.mAttributes;
    }

    @NonNull
    public final WXEvent getEvents() {
        if (this.mEvents == null) {
            this.mEvents = new WXEvent();
        }
        return this.mEvents;
    }

    @NonNull
    public final CSSShorthand getMargin() {
        if (this.mMargins == null) {
            this.mMargins = new CSSShorthand();
        }
        return this.mMargins;
    }

    @NonNull
    public final CSSShorthand getPadding() {
        if (this.mPaddings == null) {
            this.mPaddings = new CSSShorthand();
        }
        return this.mPaddings;
    }

    @NonNull
    public CSSShorthand getBorder() {
        if (this.mBorders == null) {
            this.mBorders = new CSSShorthand();
        }
        return this.mBorders;
    }

    public final void setMargins(@NonNull CSSShorthand cSSShorthand) {
        this.mMargins = cSSShorthand;
    }

    public final void setPaddings(@NonNull CSSShorthand cSSShorthand) {
        this.mPaddings = cSSShorthand;
    }

    public final void setBorders(@NonNull CSSShorthand cSSShorthand) {
        this.mBorders = cSSShorthand;
    }

    public BasicComponentData clone() throws CloneNotSupportedException {
        BasicComponentData basicComponentData = new BasicComponentData(this.mRef, this.mComponentType, this.mParentRef);
        basicComponentData.setBorders(getBorder().clone());
        basicComponentData.setMargins(getMargin().clone());
        basicComponentData.setPaddings(getPadding().clone());
        if (this.mAttributes != null) {
            basicComponentData.mAttributes = this.mAttributes.clone();
        }
        if (this.mStyles != null) {
            basicComponentData.mStyles = this.mStyles.clone();
        }
        if (this.mEvents != null) {
            basicComponentData.mEvents = this.mEvents.clone();
        }
        if (this.renderObjectPr != 0) {
            basicComponentData.setRenderObjectPr(NativeRenderObjectUtils.nativeCopyRenderObject(this.renderObjectPr));
        }
        return basicComponentData;
    }

    public long getRenderObjectPr() {
        return this.renderObjectPr;
    }

    public boolean isRenderPtrEmpty() {
        return this.renderObjectPr == 0;
    }

    public synchronized void setRenderObjectPr(long j) {
        if (this.renderObjectPr != j) {
            if (this.renderObjectPr == 0) {
                this.renderObjectPr = j;
            } else {
                throw new RuntimeException("RenderObjectPr has " + j + " old renderObjectPtr " + this.renderObjectPr);
            }
        }
    }
}
