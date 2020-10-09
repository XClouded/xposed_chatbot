package com.taobao.android.dinamicx.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.collection.LongSparseArray;
import androidx.core.view.InputDeviceCompat;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.dinamic.R;
import com.taobao.android.dinamicx.DXDarkModeCenter;
import com.taobao.android.dinamicx.DXError;
import com.taobao.android.dinamicx.DXPipelineCacheManager;
import com.taobao.android.dinamicx.DXRenderPipeline;
import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.DinamicXEngine;
import com.taobao.android.dinamicx.bindingx.DXBindingXSpec;
import com.taobao.android.dinamicx.exception.DXExceptionUtil;
import com.taobao.android.dinamicx.expression.DXEventNode;
import com.taobao.android.dinamicx.expression.DXExprNode;
import com.taobao.android.dinamicx.expression.DXSerialBlockNode;
import com.taobao.android.dinamicx.expression.event.DXEvent;
import com.taobao.android.dinamicx.model.DXLayoutParamAttribute;
import com.taobao.android.dinamicx.model.DXLongSparseArray;
import com.taobao.android.dinamicx.monitor.DXAppMonitor;
import com.taobao.android.dinamicx.monitor.DXMonitorConstant;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import com.taobao.android.dinamicx.template.loader.binary.DXHashConstant;
import com.taobao.android.dinamicx.widget.event.DXControlEventCenter;
import com.taobao.android.dinamicx.widget.event.DXPipelineScheduleEvent;
import com.taobao.android.dinamicx.widget.utils.DXScreenTool;
import com.taobao.weex.el.parse.Operators;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DXWidgetNode implements IDXBuilderWidgetNode {
    public static final int ACCESSIBILITY_AUTO = 3;
    public static final int ACCESSIBILITY_DEF = -1;
    public static final int ACCESSIBILITY_OFF = 0;
    public static final int ACCESSIBILITY_OFF_CHILD = 2;
    public static final int ACCESSIBILITY_ON = 1;
    public static final int BORDER_TYPE_DASH = 1;
    public static final int BORDER_TYPE_NORMAL = 0;
    public static final int DIRECTION_NOT_SET = -1;
    public static final int DXGRAVITY_RLT_DELTA = 6;
    public static final int DXGravityCenter = 4;
    public static final int DXGravityCenterBottom = 5;
    public static final int DXGravityCenterTop = 3;
    public static final int DXGravityLeftBottom = 2;
    public static final int DXGravityLeftCenter = 1;
    public static final int DXGravityLeftTop = 0;
    public static final int DXGravityRightBottom = 8;
    public static final int DXGravityRightCenter = 7;
    public static final int DXGravityRightTop = 6;
    public static final int DX_WIDGET_NODE_ATTR_PARSED = 2;
    public static final int DX_WIDGET_NODE_BIND_CHILD_CALLED = 4096;
    public static final int DX_WIDGET_NODE_FLATTEND = 128;
    public static final int DX_WIDGET_NODE_FORCE_LAYOUT = 16384;
    public static final int DX_WIDGET_NODE_IS_PRE_RENDERED = 8192;
    public static final int DX_WIDGET_NODE_LAID_OUT = 32;
    public static final int DX_WIDGET_NODE_MEASURED = 8;
    public static final int DX_WIDGET_NODE_NEED_FLATTEN = 64;
    public static final int DX_WIDGET_NODE_NEED_LAYOUT = 16;
    public static final int DX_WIDGET_NODE_NEED_MEASURE = 4;
    public static final int DX_WIDGET_NODE_NEED_PARSE = 1;
    public static final int DX_WIDGET_NODE_NEED_RENDER = 256;
    public static final int DX_WIDGET_NODE_PARSED = 32768;
    public static final int DX_WIDGET_NODE_PARSE_IN_MEASURE = 1024;
    public static final int DX_WIDGET_NODE_RENDERED = 512;
    public static final int DX_WIDGET_NODE_VISIBILITY_PARSED = 2048;
    public static final int GONE = 2;
    public static final int INVISIBLE = 1;
    public static final int IS_ACCESSIBILITY_FALSE = 0;
    public static final int IS_ACCESSIBILITY_TRUE = 1;
    public static final int LAYOUT_DIRECTION_LTR = 0;
    public static final int LAYOUT_DIRECTION_RTL = 1;
    public static final int LAYOUT_GRAVITY_INIT_MASK = 1;
    public static final int LISTDATA_INIT_MASK = 2;
    public static final int MATCH_CONTENT = -2;
    public static final int MATCH_PARENT = -1;
    public static final int MEASURED_HEIGHT_STATE_SHIFT = 16;
    public static final int MEASURED_SIZE_MASK = 16777215;
    public static final int MEASURED_STATE_MASK = -16777216;
    public static final int MEASURED_STATE_TOO_SMALL = 16777216;
    public static final int NO = 0;
    public static final int TAG_WIDGET_NODE = R.id.dinamicXWidgetNodeTag;
    public static final int VISIBLE = 0;
    public static final int YES = 1;
    private static ThreadLocal<DXLayoutParamAttribute> attributeThreadLocal = new ThreadLocal<>();
    private static boolean sAlwaysRemeasureExactly = false;
    private int DEFAULT;
    int accessibility;
    String accessibilityText;
    float alpha;
    String animation;
    private int autoId;
    int backGroundColor;
    private GradientInfo backgroundGradient;
    Map<String, DXBindingXSpec> bindingXExecutingMap;
    Map<String, DXBindingXSpec> bindingXSpecMap;
    int borderColor;
    int borderType;
    int borderWidth;
    int bottom;
    int childGravity;
    List<DXWidgetNode> children;
    private int childrenCount;
    boolean clipChildren;
    int cornerRadius;
    int cornerRadiusLeftBottom;
    int cornerRadiusLeftTop;
    int cornerRadiusRightBottom;
    int cornerRadiusRightTop;
    DXRuntimeContext dXRuntimeContext;
    private HashMap<String, Integer> darkModeColorMap;
    int dashGap;
    int dashWidth;
    private DXLongSparseArray<DXExprNode> dataParsersExprNode;
    private int direction;
    private boolean disableDarkMode;
    int enabled;
    private DXLongSparseArray<Map<String, Integer>> enumMap;
    private DXLongSparseArray<DXExprNode> eventHandlersExprNode;
    private boolean hasHandleDark;
    boolean isFlatten;
    private int lastAutoId;
    int layoutGravity;
    int layoutHeight;
    int layoutWidth;
    int left;
    int marginBottom;
    int marginLeft;
    int marginRight;
    int marginTop;
    int measuredHeight;
    int measuredWidth;
    int minHeight;
    int minWidth;
    boolean needSetBackground;
    int oldHeightMeasureSpec;
    int oldWidthMeasureSpec;
    int paddingBottom;
    int paddingLeft;
    int paddingRight;
    int paddingTop;
    DXWidgetNode parentWidget;
    int privateFlags;
    int propertyInitFlag;
    private DXLayoutParamAttribute realLayoutAttribute;
    private WeakReference<DXWidgetNode> referenceNode;
    int right;
    float rotationX;
    float rotationY;
    float rotationZ;
    float scaleX;
    float scaleY;
    private WeakReference<DXWidgetNode> sourceWidgetWR;
    int top;
    float translateX;
    float translateY;
    String userId;
    int visibility;
    private WeakReference<View> weakView;
    double weight;

    @Retention(RetentionPolicy.SOURCE)
    public @interface DXGravity {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DXNodePropertyInitMask {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DXWidgetNodeStatFlag {
    }

    public static int combineMeasuredStates(int i, int i2) {
        return i | i2;
    }

    public static int getAbsoluteGravity(int i, int i2) {
        if (i2 == 0 || i2 != 1) {
            return i;
        }
        switch (i) {
            case 0:
            case 1:
            case 2:
                return i + 6;
            case 6:
            case 7:
            case 8:
                return i - 6;
            default:
                return i;
        }
    }

    /* access modifiers changed from: protected */
    public boolean extraHandleDark() {
        return false;
    }

    public Object getDefaultValueForAttr(long j) {
        return null;
    }

    public double getDefaultValueForDoubleAttr(long j) {
        return 0.0d;
    }

    public int getDefaultValueForIntAttr(long j) {
        if (DXHashConstant.DX_VIEW_WIDTH == j || DXHashConstant.DX_VIEW_HEIGHT == j || DXHashConstant.DX_VIEW_MARGINLEFT == j || DXHashConstant.DX_VIEW_MARGINRIGHT == j || DXHashConstant.DX_VIEW_MARGINTOP == j || DXHashConstant.DX_VIEW_MARGINBOTTOM == j || DXHashConstant.DX_VIEW_PADDINGLEFT == j || DXHashConstant.DX_VIEW_PADDINGRIGHT == j || DXHashConstant.DX_VIEW_PADDINGTOP == j || DXHashConstant.DX_VIEW_PADDINGBOTTOM == j || DXHashConstant.DX_VIEW_GRAVITY == j || DXHashConstant.DX_VIEW_CHILDGRAVITY == j || DXHashConstant.DX_VIEW_DIRECTION == j || DXHashConstant.DX_VIEW_VISIBILITY == j || DXHashConstant.DX_VIEW_BORDERWIDTH == j || DXHashConstant.DX_VIEW_BORDERCOLOR == j || DXHashConstant.DX_VIEW_BORDERTYPE == j || DXHashConstant.DX_VIEW_DISABLEDARKMODE == j) {
            return 0;
        }
        if (DXHashConstant.DX_VIEW_ALPHA == j || DXHashConstant.DX_VIEW_ENABLED == j) {
            return 1;
        }
        return (DXHashConstant.DX_VIEW_CORNERRADIUS == j || DXHashConstant.DX_VIEW_CORNERRADIUSLEFTTOP == j || DXHashConstant.DX_VIEW_CORNERRADIUSRIGHTTOP == j || DXHashConstant.DX_VIEW_CORNERRADIUSLEFTBOTTOM == j || DXHashConstant.DX_VIEW_CORNERRADIUSRIGHTBOTTOM == j) ? 0 : 0;
    }

    public JSONArray getDefaultValueForListAttr(long j) {
        return null;
    }

    public long getDefaultValueForLongAttr(long j) {
        return 0;
    }

    public JSONObject getDefaultValueForMapAttr(long j) {
        return null;
    }

    public Object getDefaultValueForObjectAttr(long j) {
        return null;
    }

    public String getDefaultValueForStringAttr(long j) {
        return "";
    }

    /* access modifiers changed from: package-private */
    public int getNextLocationOffset(DXWidgetNode dXWidgetNode) {
        return 0;
    }

    public void onBeforeBindChildData() {
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
    }

    /* access modifiers changed from: protected */
    public void onRenderView(Context context, View view) {
    }

    /* access modifiers changed from: protected */
    public void onSetDoubleAttribute(long j, double d) {
    }

    /* access modifiers changed from: protected */
    public void onSetIntAttribute(long j, int i) {
    }

    /* access modifiers changed from: protected */
    public void onSetListAttribute(long j, JSONArray jSONArray) {
    }

    /* access modifiers changed from: protected */
    public void onSetLongAttribute(long j, long j2) {
    }

    /* access modifiers changed from: protected */
    public void onSetMapAttribute(long j, JSONObject jSONObject) {
    }

    /* access modifiers changed from: protected */
    public void onSetObjAttribute(long j, Object obj) {
    }

    /* access modifiers changed from: protected */
    public void onSetStringAttribute(long j, String str) {
    }

    public DXWidgetNode() {
        this.layoutGravity = 0;
        this.childGravity = 0;
        this.alpha = 1.0f;
        this.cornerRadius = 0;
        this.cornerRadiusLeftTop = 0;
        this.cornerRadiusRightTop = 0;
        this.cornerRadiusLeftBottom = 0;
        this.cornerRadiusRightBottom = 0;
        this.borderWidth = -1;
        this.borderColor = 0;
        this.borderType = 0;
        this.dashWidth = -1;
        this.dashGap = -1;
        this.backGroundColor = 0;
        this.clipChildren = true;
        this.DEFAULT = 0;
        this.translateX = (float) this.DEFAULT;
        this.translateY = (float) this.DEFAULT;
        this.scaleX = 1.0f;
        this.scaleY = 1.0f;
        this.rotationX = (float) this.DEFAULT;
        this.rotationY = (float) this.DEFAULT;
        this.rotationZ = (float) this.DEFAULT;
        this.disableDarkMode = false;
        this.oldWidthMeasureSpec = Integer.MIN_VALUE;
        this.oldHeightMeasureSpec = Integer.MIN_VALUE;
        this.visibility = 0;
        this.layoutGravity = 0;
        this.childGravity = 0;
        this.direction = -1;
        this.alpha = 1.0f;
        this.accessibility = -1;
        this.enabled = 1;
    }

    public int getVisibility() {
        return this.visibility;
    }

    public int getVirtualChildCount() {
        return this.childrenCount;
    }

    public DXWidgetNode getChildAt(int i) {
        if (i < 0 || i >= this.childrenCount || this.children == null) {
            return null;
        }
        return this.children.get(i);
    }

    /* access modifiers changed from: protected */
    public int getSuggestedMinimumHeight() {
        return this.minHeight;
    }

    public final void layout(int i, int i2, int i3, int i4) {
        try {
            if ((this.privateFlags & 4) != 0) {
                onMeasure(this.oldWidthMeasureSpec, this.oldHeightMeasureSpec);
                this.privateFlags &= -5;
                this.privateFlags |= 8;
            }
            boolean frame = setFrame(i, i2, i3, i4);
            if (frame || (this.privateFlags & 16) == 16) {
                onLayout(frame, i, i2, i3, i4);
                this.privateFlags &= -17;
            }
            this.privateFlags &= -16385;
            this.privateFlags |= 32;
        } catch (Exception e) {
            if (DinamicXEngine.isDebug()) {
                e.printStackTrace();
            }
            DXRuntimeContext dXRuntimeContext2 = getDXRuntimeContext();
            if (dXRuntimeContext2 != null && dXRuntimeContext2.getDxError() != null) {
                DXError.DXErrorInfo dXErrorInfo = new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE_DETAIL, DXMonitorConstant.DX_MONITOR_SERVICE_ID_PERFORM_LAYOUT, DXError.DXERROR_DETAIL_ON_LAYOUT_ERROR);
                dXErrorInfo.reason = DXExceptionUtil.getStackTrace(e);
                dXRuntimeContext2.getDxError().dxErrorInfoList.add(dXErrorInfo);
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean setFrame(int i, int i2, int i3, int i4) {
        if (this.left == i && this.right == i3 && this.top == i2 && this.bottom == i4) {
            return false;
        }
        this.left = i;
        this.top = i2;
        this.right = i3;
        this.bottom = i4;
        return true;
    }

    public List<DXWidgetNode> getChildren() {
        return this.children;
    }

    public int getChildrenCount() {
        return this.childrenCount;
    }

    public final void addChild(DXWidgetNode dXWidgetNode) {
        addChild(dXWidgetNode, true);
    }

    public final void addChild(DXWidgetNode dXWidgetNode, boolean z) {
        if (dXWidgetNode != null && dXWidgetNode != this) {
            if (this.children == null) {
                this.children = new ArrayList();
                this.childrenCount = 0;
            }
            this.children.add(dXWidgetNode);
            this.childrenCount++;
            dXWidgetNode.parentWidget = this;
            if (this.dXRuntimeContext != null && z) {
                dXWidgetNode.dXRuntimeContext = this.dXRuntimeContext.cloneWithWidgetNode(dXWidgetNode);
            }
        }
    }

    public void insertChild(DXWidgetNode dXWidgetNode, int i) {
        insertChild(dXWidgetNode, i, true);
    }

    public void insertChild(DXWidgetNode dXWidgetNode, int i, boolean z) {
        if (dXWidgetNode != null && dXWidgetNode != this && i <= this.childrenCount) {
            if (this.children == null) {
                this.children = new ArrayList();
                this.childrenCount = 0;
            }
            this.children.add(i, dXWidgetNode);
            this.childrenCount++;
            dXWidgetNode.parentWidget = this;
            if (this.dXRuntimeContext != null && z) {
                dXWidgetNode.dXRuntimeContext = this.dXRuntimeContext.cloneWithWidgetNode(dXWidgetNode);
            }
        }
    }

    public void removeAllChild() {
        if (this.children == null) {
            this.childrenCount = 0;
            return;
        }
        this.children.clear();
        this.childrenCount = 0;
    }

    public void removeChildWithAutoId(int i) {
        if (this.children != null && this.childrenCount != 0) {
            for (int i2 = 0; i2 < this.childrenCount; i2++) {
                if (this.children.get(i2).autoId == i) {
                    this.children.remove(i2);
                    this.childrenCount--;
                    return;
                }
            }
        }
    }

    public int replaceChild(DXWidgetNode dXWidgetNode, DXWidgetNode dXWidgetNode2) {
        if (!(this instanceof DXLayout) || dXWidgetNode2 == null) {
            return -1;
        }
        int i = 0;
        while (true) {
            if (i >= getChildrenCount()) {
                i = -1;
                break;
            } else if (getChildAt(i).getAutoId() == dXWidgetNode2.getAutoId()) {
                break;
            } else {
                i++;
            }
        }
        if (i != -1) {
            removeChildWithAutoId(dXWidgetNode2.getAutoId());
            insertChild(dXWidgetNode, i);
        }
        return i;
    }

    public void newEventHandlersExprNode(int i) {
        this.eventHandlersExprNode = new DXLongSparseArray<>(i);
    }

    public LongSparseArray<DXExprNode> getEventHandlersExprNode() {
        return this.eventHandlersExprNode;
    }

    public void newDataParsersExprNode(int i) {
        this.dataParsersExprNode = new DXLongSparseArray<>(i);
    }

    public LongSparseArray<DXExprNode> getDataParsersExprNode() {
        return this.dataParsersExprNode;
    }

    public void newEnumMap() {
        this.enumMap = new DXLongSparseArray<>();
    }

    public DXLongSparseArray<Map<String, Integer>> getEnumMap() {
        return this.enumMap;
    }

    public int getAutoId() {
        return this.autoId;
    }

    public void setAutoId(int i) {
        this.autoId = i;
    }

    public DXRuntimeContext getDXRuntimeContext() {
        return this.dXRuntimeContext;
    }

    public void setDXRuntimeContext(DXRuntimeContext dXRuntimeContext2) {
        this.dXRuntimeContext = dXRuntimeContext2;
    }

    public WeakReference<View> getWRView() {
        return this.weakView;
    }

    public void setWRView(WeakReference<View> weakReference) {
        this.weakView = weakReference;
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0026 A[Catch:{ Throwable -> 0x007f }] */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0028 A[Catch:{ Throwable -> 0x007f }] */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x003d A[Catch:{ Throwable -> 0x007f }] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x003f A[Catch:{ Throwable -> 0x007f }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x004e A[Catch:{ Throwable -> 0x007f }] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x005a A[ADDED_TO_REGION, Catch:{ Throwable -> 0x007f }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void measure(int r8, int r9) {
        /*
            r7 = this;
            int r0 = r7.privateFlags     // Catch:{ Throwable -> 0x007f }
            r1 = 16384(0x4000, float:2.2959E-41)
            r0 = r0 & r1
            r2 = 0
            r3 = 1
            if (r0 != r1) goto L_0x000b
            r0 = 1
            goto L_0x000c
        L_0x000b:
            r0 = 0
        L_0x000c:
            int r1 = r7.oldWidthMeasureSpec     // Catch:{ Throwable -> 0x007f }
            if (r8 != r1) goto L_0x0017
            int r1 = r7.oldHeightMeasureSpec     // Catch:{ Throwable -> 0x007f }
            if (r9 == r1) goto L_0x0015
            goto L_0x0017
        L_0x0015:
            r1 = 0
            goto L_0x0018
        L_0x0017:
            r1 = 1
        L_0x0018:
            int r4 = com.taobao.android.dinamicx.widget.DXWidgetNode.DXMeasureSpec.getMode(r8)     // Catch:{ Throwable -> 0x007f }
            r5 = 1073741824(0x40000000, float:2.0)
            if (r4 != r5) goto L_0x0028
            int r4 = com.taobao.android.dinamicx.widget.DXWidgetNode.DXMeasureSpec.getMode(r9)     // Catch:{ Throwable -> 0x007f }
            if (r4 != r5) goto L_0x0028
            r4 = 1
            goto L_0x0029
        L_0x0028:
            r4 = 0
        L_0x0029:
            int r5 = r7.getMeasuredWidth()     // Catch:{ Throwable -> 0x007f }
            int r6 = com.taobao.android.dinamicx.widget.DXWidgetNode.DXMeasureSpec.getSize(r8)     // Catch:{ Throwable -> 0x007f }
            if (r5 != r6) goto L_0x003f
            int r5 = r7.getMeasuredHeight()     // Catch:{ Throwable -> 0x007f }
            int r6 = com.taobao.android.dinamicx.widget.DXWidgetNode.DXMeasureSpec.getSize(r9)     // Catch:{ Throwable -> 0x007f }
            if (r5 != r6) goto L_0x003f
            r5 = 1
            goto L_0x0040
        L_0x003f:
            r5 = 0
        L_0x0040:
            r7.oldWidthMeasureSpec = r8     // Catch:{ Throwable -> 0x007f }
            r7.oldHeightMeasureSpec = r9     // Catch:{ Throwable -> 0x007f }
            if (r4 == 0) goto L_0x005a
            r6 = 1024(0x400, float:1.435E-42)
            boolean r6 = r7.getStatInPrivateFlags(r6)     // Catch:{ Throwable -> 0x007f }
            if (r6 == 0) goto L_0x005a
            int r8 = com.taobao.android.dinamicx.widget.DXWidgetNode.DXMeasureSpec.getSize(r8)     // Catch:{ Throwable -> 0x007f }
            int r9 = com.taobao.android.dinamicx.widget.DXWidgetNode.DXMeasureSpec.getSize(r9)     // Catch:{ Throwable -> 0x007f }
            r7.setMeasuredDimension(r8, r9)     // Catch:{ Throwable -> 0x007f }
            return
        L_0x005a:
            if (r1 == 0) goto L_0x0065
            boolean r1 = sAlwaysRemeasureExactly     // Catch:{ Throwable -> 0x007f }
            if (r1 != 0) goto L_0x0064
            if (r4 == 0) goto L_0x0064
            if (r5 != 0) goto L_0x0065
        L_0x0064:
            r2 = 1
        L_0x0065:
            if (r0 != 0) goto L_0x0069
            if (r2 == 0) goto L_0x00b0
        L_0x0069:
            r7.onMeasure(r8, r9)     // Catch:{ Throwable -> 0x007f }
            int r8 = r7.privateFlags     // Catch:{ Throwable -> 0x007f }
            r8 = r8 & -5
            r7.privateFlags = r8     // Catch:{ Throwable -> 0x007f }
            int r8 = r7.privateFlags     // Catch:{ Throwable -> 0x007f }
            r8 = r8 | 16
            r7.privateFlags = r8     // Catch:{ Throwable -> 0x007f }
            int r8 = r7.privateFlags     // Catch:{ Throwable -> 0x007f }
            r8 = r8 | 8
            r7.privateFlags = r8     // Catch:{ Throwable -> 0x007f }
            goto L_0x00b0
        L_0x007f:
            r8 = move-exception
            boolean r9 = com.taobao.android.dinamicx.DinamicXEngine.isDebug()
            if (r9 == 0) goto L_0x0089
            r8.printStackTrace()
        L_0x0089:
            com.taobao.android.dinamicx.DXRuntimeContext r9 = r7.getDXRuntimeContext()
            if (r9 == 0) goto L_0x00b0
            com.taobao.android.dinamicx.DXError r0 = r9.getDxError()
            if (r0 == 0) goto L_0x00b0
            com.taobao.android.dinamicx.DXError$DXErrorInfo r0 = new com.taobao.android.dinamicx.DXError$DXErrorInfo
            java.lang.String r1 = "Pipeline_Detail"
            java.lang.String r2 = "Pipeline_Detail_PerformMeasure"
            r3 = 80006(0x13886, float:1.12112E-40)
            r0.<init>(r1, r2, r3)
            java.lang.String r8 = com.taobao.android.dinamicx.exception.DXExceptionUtil.getStackTrace(r8)
            r0.reason = r8
            com.taobao.android.dinamicx.DXError r8 = r9.getDxError()
            java.util.List<com.taobao.android.dinamicx.DXError$DXErrorInfo> r8 = r8.dxErrorInfoList
            r8.add(r0)
        L_0x00b0:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamicx.widget.DXWidgetNode.measure(int, int):void");
    }

    public static int resolveSize(int i, int i2) {
        return resolveSizeAndState(i, i2, 0) & 16777215;
    }

    public static int resolveSizeAndState(int i, int i2, int i3) {
        int mode = DXMeasureSpec.getMode(i2);
        int size = DXMeasureSpec.getSize(i2);
        if (mode != Integer.MIN_VALUE) {
            if (mode == 1073741824) {
                i = size;
            }
        } else if (size < i) {
            i = 16777216 | size;
        }
        return i | (-16777216 & i3);
    }

    public static int getDefaultSize(int i, int i2) {
        int mode = View.MeasureSpec.getMode(i2);
        return (mode == Integer.MIN_VALUE || (mode != 0 && mode == 1073741824)) ? View.MeasureSpec.getSize(i2) : i;
    }

    public final int getMeasuredState() {
        return (this.measuredWidth & -16777216) | ((this.measuredHeight >> 16) & InputDeviceCompat.SOURCE_ANY);
    }

    /* access modifiers changed from: package-private */
    public final DXWidgetNode getVirtualChildAt(int i) {
        return getChildAt(i);
    }

    public final int getMeasuredHeight() {
        return this.measuredHeight & 16777215;
    }

    public final int getMeasuredWidth() {
        return this.measuredWidth & 16777215;
    }

    public final int getMeasuredWidthAndState() {
        return this.measuredWidth;
    }

    public final int getMeasuredHeightAndState() {
        return this.measuredHeight;
    }

    public int getWidth() {
        return this.right - this.left;
    }

    public int getHeight() {
        return this.bottom - this.top;
    }

    /* access modifiers changed from: protected */
    public final int getSuggestedMinimumWidth() {
        return this.minWidth;
    }

    public final void setMeasuredDimension(int i, int i2) {
        this.measuredWidth = i;
        this.measuredHeight = i2;
    }

    private View getRealView() {
        if (this.weakView == null) {
            return null;
        }
        return (View) this.weakView.get();
    }

    public final View createView(Context context) {
        View realView = getRealView();
        if (realView != null) {
            return realView;
        }
        View onCreateView = getReferenceNode().onCreateView(context);
        onCreateView.setTag(TAG_WIDGET_NODE, this);
        this.weakView = new WeakReference<>(onCreateView);
        this.privateFlags |= 256;
        return onCreateView;
    }

    public final void renderView(Context context) {
        try {
            View realView = getRealView();
            if (realView != null) {
                boolean z = false;
                if ((this.privateFlags & 256) != 0) {
                    setRealViewVisibility(realView, this.visibility);
                    if (realView.getAlpha() != this.alpha) {
                        realView.setAlpha(this.alpha);
                    }
                    if (this.enabled == 1) {
                        z = true;
                    }
                    if (realView.isEnabled() != z) {
                        realView.setEnabled(z);
                    }
                    renderTransformedProperty(realView);
                    setAccessibility(realView);
                    DXWidgetNode referenceNode2 = getReferenceNode();
                    referenceNode2.setBackground(realView);
                    referenceNode2.onRenderView(context, realView);
                    if (Build.VERSION.SDK_INT >= 17 && (realView instanceof ViewGroup)) {
                        realView.setLayoutDirection(getDirection());
                    }
                    referenceNode2.setForceDark(realView);
                }
                this.privateFlags &= -257;
                this.privateFlags |= 512;
            }
        } catch (Exception e) {
            if (DinamicXEngine.isDebug()) {
                e.printStackTrace();
            }
            DXRuntimeContext dXRuntimeContext2 = getDXRuntimeContext();
            if (dXRuntimeContext2 != null && dXRuntimeContext2.getDxError() != null) {
                DXError.DXErrorInfo dXErrorInfo = new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_RENDER, DXMonitorConstant.DX_MONITOR_SERVICE_ID_RENDER_DETAIL, 90002);
                dXErrorInfo.reason = DXExceptionUtil.getStackTrace(e);
                dXRuntimeContext2.getDxError().dxErrorInfoList.add(dXErrorInfo);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void renderTransformedProperty(View view) {
        if (this.translateX != view.getTranslationX()) {
            view.setTranslationX(this.translateX);
        }
        if (this.translateY != view.getTranslationY()) {
            view.setTranslationY(this.translateY);
        }
        if (this.rotationX != view.getRotationX()) {
            view.setRotationX(this.rotationX);
        }
        if (this.rotationY != view.getRotationY()) {
            view.setRotationY(this.rotationY);
        }
        if (this.rotationZ != view.getRotation()) {
            view.setRotation(this.rotationZ);
        }
        if (this.scaleX != view.getScaleX()) {
            view.setScaleX(this.scaleX);
        }
        if (this.scaleY != view.getScaleY()) {
            view.setScaleY(this.scaleY);
        }
    }

    public void setBackground(View view) {
        int i;
        int i2;
        View view2 = view;
        if (this.needSetBackground) {
            int tryFetchDarkModeColor = tryFetchDarkModeColor("backgroundColor", 1, this.backGroundColor);
            int tryFetchDarkModeColor2 = tryFetchDarkModeColor("borderColor", 2, this.borderColor);
            Drawable background = view.getBackground();
            if (this.borderType == 1) {
                if (this.dashWidth <= -1 && DinamicXEngine.getApplicationContext() != null) {
                    this.dashWidth = DXScreenTool.ap2px(DinamicXEngine.getApplicationContext(), 6.0f);
                }
                if (this.dashGap <= -1 && DinamicXEngine.getApplicationContext() != null) {
                    this.dashGap = DXScreenTool.ap2px(DinamicXEngine.getApplicationContext(), 4.0f);
                }
                i2 = this.dashWidth;
                i = this.dashGap;
            } else {
                i2 = 0;
                i = 0;
            }
            if (background != null && (background instanceof GradientDrawable)) {
                GradientDrawable gradientDrawable = (GradientDrawable) background;
                gradientDrawable.setColor(tryFetchDarkModeColor);
                if (this.cornerRadius > 0) {
                    gradientDrawable.setCornerRadius((float) this.cornerRadius);
                } else {
                    gradientDrawable.setCornerRadii(new float[]{(float) this.cornerRadiusLeftTop, (float) this.cornerRadiusLeftTop, (float) this.cornerRadiusRightTop, (float) this.cornerRadiusRightTop, (float) this.cornerRadiusRightBottom, (float) this.cornerRadiusRightBottom, (float) this.cornerRadiusLeftBottom, (float) this.cornerRadiusLeftBottom});
                }
                if (this.borderWidth > 0 && tryFetchDarkModeColor2 != 0) {
                    gradientDrawable.setStroke(this.borderWidth, tryFetchDarkModeColor2, (float) i2, (float) i);
                } else if (this.borderWidth > 0 && tryFetchDarkModeColor2 == 0) {
                    gradientDrawable.setStroke(0, 0, 0.0f, 0.0f);
                }
                if (this.backgroundGradient != null && this.backgroundGradient.getGradientType() == 0) {
                    gradientDrawable.setGradientType(this.backgroundGradient.getGradientType());
                    if (Build.VERSION.SDK_INT >= 16) {
                        gradientDrawable.setOrientation(this.backgroundGradient.getLinearGradientDirection());
                        gradientDrawable.setColors(this.backgroundGradient.getLinearGradientColors());
                    }
                }
            } else if (hasCornerRadius() || tryFetchDarkModeColor2 != 0 || this.borderWidth > 0 || this.backgroundGradient != null) {
                GradientDrawable gradientDrawable2 = new GradientDrawable();
                if (this.cornerRadius > 0) {
                    gradientDrawable2.setCornerRadius((float) this.cornerRadius);
                } else {
                    gradientDrawable2.setCornerRadii(new float[]{(float) this.cornerRadiusLeftTop, (float) this.cornerRadiusLeftTop, (float) this.cornerRadiusRightTop, (float) this.cornerRadiusRightTop, (float) this.cornerRadiusRightBottom, (float) this.cornerRadiusRightBottom, (float) this.cornerRadiusLeftBottom, (float) this.cornerRadiusLeftBottom});
                }
                gradientDrawable2.setShape(0);
                gradientDrawable2.setColor(tryFetchDarkModeColor);
                if (this.borderWidth > 0 && tryFetchDarkModeColor2 != 0) {
                    gradientDrawable2.setStroke(this.borderWidth, tryFetchDarkModeColor2, (float) i2, (float) i);
                } else if (this.borderWidth > 0 && tryFetchDarkModeColor2 == 0) {
                    gradientDrawable2.setStroke(0, 0, 0.0f, 0.0f);
                }
                if (this.backgroundGradient != null && this.backgroundGradient.getGradientType() == 0) {
                    gradientDrawable2.setGradientType(this.backgroundGradient.getGradientType());
                    if (Build.VERSION.SDK_INT >= 16) {
                        gradientDrawable2.setOrientation(this.backgroundGradient.getLinearGradientDirection());
                        gradientDrawable2.setColors(this.backgroundGradient.getLinearGradientColors());
                    }
                }
                if (Build.VERSION.SDK_INT >= 16) {
                    view2.setBackground(gradientDrawable2);
                } else {
                    view2.setBackgroundDrawable(gradientDrawable2);
                }
            } else {
                view2.setBackgroundColor(tryFetchDarkModeColor);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setAccessibility(View view) {
        if (Build.VERSION.SDK_INT < 16) {
            view.setContentDescription("");
        } else if (this.accessibility != -1 && this.accessibility != 3) {
            if (this.accessibilityText != null) {
                view.setContentDescription(this.accessibilityText);
            }
            if (this.accessibility == 1) {
                view.setImportantForAccessibility(1);
                view.setFocusable(true);
            } else if (this.accessibility == 2) {
                view.setImportantForAccessibility(4);
            } else {
                view.setImportantForAccessibility(2);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setRealViewVisibility(View view, int i) {
        if (view != null) {
            int i2 = 0;
            switch (i) {
                case 1:
                    i2 = 4;
                    break;
                case 2:
                    i2 = 8;
                    break;
            }
            if (view.getVisibility() != i2) {
                view.setVisibility(i2);
            }
        }
    }

    public void setRealViewLayoutParam(View view) {
        ViewGroup.LayoutParams layoutParams;
        if (view != null) {
            if (this.parentWidget == null || !(this.parentWidget instanceof DXLayout)) {
                ViewGroup.LayoutParams layoutParams2 = view.getLayoutParams();
                if (layoutParams2 == null) {
                    layoutParams2 = new ViewGroup.LayoutParams(getMeasuredWidth(), getMeasuredHeight());
                } else {
                    layoutParams2.width = getMeasuredWidth();
                    layoutParams2.height = getMeasuredHeight();
                }
                view.setLayoutParams(layoutParams2);
                return;
            }
            this.realLayoutAttribute = attributeThreadLocal.get();
            if (this.realLayoutAttribute == null) {
                this.realLayoutAttribute = new DXLayoutParamAttribute();
                attributeThreadLocal.set(this.realLayoutAttribute);
            }
            this.realLayoutAttribute.widthAttr = getMeasuredWidth();
            this.realLayoutAttribute.heightAttr = getMeasuredHeight();
            DXLayout dXLayout = (DXLayout) this.parentWidget;
            ViewGroup.LayoutParams layoutParams3 = view.getLayoutParams();
            if (layoutParams3 == null) {
                layoutParams = dXLayout.generateLayoutParams(this.realLayoutAttribute);
            } else {
                layoutParams = dXLayout.generateLayoutParams(this.realLayoutAttribute, layoutParams3);
            }
            view.setLayoutParams(layoutParams);
        }
    }

    public final Object shallowClone(@NonNull DXRuntimeContext dXRuntimeContext2, boolean z) {
        DXWidgetNode build = build((Object) null);
        if (build == null) {
            return null;
        }
        if (dXRuntimeContext2 != null) {
            build.dXRuntimeContext = dXRuntimeContext2.cloneWithWidgetNode(build);
        }
        build.onClone(this, z);
        return build;
    }

    public final DXWidgetNode deepClone(DXRuntimeContext dXRuntimeContext2) {
        DXWidgetNode dXWidgetNode = (DXWidgetNode) shallowClone(dXRuntimeContext2, true);
        if (this.children != null) {
            dXWidgetNode.children = new ArrayList();
            for (int i = 0; i < this.children.size(); i++) {
                dXWidgetNode.addChild(this.children.get(i).deepClone(dXRuntimeContext2));
            }
        }
        return dXWidgetNode;
    }

    public void onClone(DXWidgetNode dXWidgetNode, boolean z) {
        this.userId = dXWidgetNode.userId;
        this.autoId = dXWidgetNode.autoId;
        this.eventHandlersExprNode = dXWidgetNode.eventHandlersExprNode;
        this.dataParsersExprNode = dXWidgetNode.dataParsersExprNode;
        this.enumMap = dXWidgetNode.enumMap;
        this.privateFlags = dXWidgetNode.privateFlags;
        this.isFlatten = dXWidgetNode.isFlatten;
        this.needSetBackground = dXWidgetNode.needSetBackground;
        this.animation = dXWidgetNode.animation;
        this.propertyInitFlag = dXWidgetNode.propertyInitFlag;
        this.layoutWidth = dXWidgetNode.layoutWidth;
        this.layoutHeight = dXWidgetNode.layoutHeight;
        this.measuredWidth = dXWidgetNode.measuredWidth;
        this.measuredHeight = dXWidgetNode.measuredHeight;
        this.left = dXWidgetNode.left;
        this.top = dXWidgetNode.top;
        this.right = dXWidgetNode.right;
        this.bottom = dXWidgetNode.bottom;
        this.weight = dXWidgetNode.weight;
        this.marginLeft = dXWidgetNode.marginLeft;
        this.marginTop = dXWidgetNode.marginTop;
        this.marginRight = dXWidgetNode.marginRight;
        this.marginBottom = dXWidgetNode.marginBottom;
        this.paddingLeft = dXWidgetNode.paddingLeft;
        this.paddingTop = dXWidgetNode.paddingTop;
        this.paddingRight = dXWidgetNode.paddingRight;
        this.paddingBottom = dXWidgetNode.paddingBottom;
        this.visibility = dXWidgetNode.visibility;
        this.layoutGravity = dXWidgetNode.layoutGravity;
        this.childGravity = dXWidgetNode.childGravity;
        this.direction = dXWidgetNode.direction;
        this.alpha = dXWidgetNode.alpha;
        this.cornerRadius = dXWidgetNode.cornerRadius;
        this.cornerRadiusLeftTop = dXWidgetNode.cornerRadiusLeftTop;
        this.cornerRadiusRightTop = dXWidgetNode.cornerRadiusRightTop;
        this.cornerRadiusLeftBottom = dXWidgetNode.cornerRadiusLeftBottom;
        this.cornerRadiusRightBottom = dXWidgetNode.cornerRadiusRightBottom;
        this.borderWidth = dXWidgetNode.borderWidth;
        this.borderColor = dXWidgetNode.borderColor;
        this.borderType = dXWidgetNode.borderType;
        this.dashWidth = dXWidgetNode.dashWidth;
        this.dashGap = dXWidgetNode.dashGap;
        this.backGroundColor = dXWidgetNode.backGroundColor;
        this.accessibility = dXWidgetNode.accessibility;
        this.accessibilityText = dXWidgetNode.accessibilityText;
        this.enabled = dXWidgetNode.enabled;
        this.minHeight = dXWidgetNode.minHeight;
        this.minWidth = dXWidgetNode.minWidth;
        this.translateX = dXWidgetNode.translateX;
        this.translateY = dXWidgetNode.translateY;
        this.scaleX = dXWidgetNode.scaleX;
        this.scaleY = dXWidgetNode.scaleY;
        this.rotationX = dXWidgetNode.rotationX;
        this.bindingXSpecMap = dXWidgetNode.bindingXSpecMap;
        this.bindingXExecutingMap = dXWidgetNode.bindingXExecutingMap;
        this.lastAutoId = dXWidgetNode.lastAutoId;
        this.sourceWidgetWR = dXWidgetNode.sourceWidgetWR;
        this.clipChildren = dXWidgetNode.clipChildren;
        this.backgroundGradient = dXWidgetNode.backgroundGradient;
        this.darkModeColorMap = dXWidgetNode.darkModeColorMap;
        this.disableDarkMode = dXWidgetNode.disableDarkMode;
    }

    public final void invalidateParseCache() {
        this.privateFlags &= -3;
        this.privateFlags |= 1;
        if (this.parentWidget != null) {
            this.parentWidget.invalidateParseCache();
        }
    }

    public final void setNeedParse() {
        this.privateFlags &= -3;
        this.privateFlags |= 1;
        if (this.parentWidget != null) {
            this.parentWidget.setNeedParse();
            return;
        }
        DXRuntimeContext dXRuntimeContext2 = getDXRuntimeContext();
        if (dXRuntimeContext2 != null) {
            DXRenderPipeline dxRenderPipeline = dXRuntimeContext2.getDxRenderPipeline();
            DXControlEventCenter dxControlEventCenter = dXRuntimeContext2.getDxControlEventCenter();
            if (dxRenderPipeline != null && dxControlEventCenter != null) {
                DXPipelineCacheManager pipelineCacheManager = dxRenderPipeline.getPipelineCacheManager();
                if (pipelineCacheManager != null) {
                    pipelineCacheManager.removeCache(dXRuntimeContext2.getCacheIdentify());
                }
                DXPipelineScheduleEvent dXPipelineScheduleEvent = new DXPipelineScheduleEvent();
                dXPipelineScheduleEvent.stage = 2;
                dXPipelineScheduleEvent.sender = this;
                dxControlEventCenter.postEventDelay(dXPipelineScheduleEvent);
            }
        }
    }

    public final void invalidateLayoutCache() {
        this.privateFlags |= 16384;
        this.privateFlags &= -41;
        if (this.parentWidget != null) {
            this.parentWidget.invalidateLayoutCache();
        }
    }

    public final void setNeedLayout() {
        this.privateFlags |= 16384;
        this.privateFlags &= -41;
        if (this.parentWidget != null) {
            this.parentWidget.setNeedLayout();
            return;
        }
        DXRuntimeContext dXRuntimeContext2 = getDXRuntimeContext();
        if (dXRuntimeContext2 != null) {
            DXRenderPipeline dxRenderPipeline = dXRuntimeContext2.getDxRenderPipeline();
            DXControlEventCenter dxControlEventCenter = dXRuntimeContext2.getDxControlEventCenter();
            if (dxRenderPipeline != null && dxControlEventCenter != null) {
                DXPipelineCacheManager pipelineCacheManager = dxRenderPipeline.getPipelineCacheManager();
                if (pipelineCacheManager != null) {
                    pipelineCacheManager.removeCache(dXRuntimeContext2.getCacheIdentify());
                }
                DXPipelineScheduleEvent dXPipelineScheduleEvent = new DXPipelineScheduleEvent();
                dXPipelineScheduleEvent.stage = 3;
                dXPipelineScheduleEvent.sender = this;
                dxControlEventCenter.postEventDelay(dXPipelineScheduleEvent);
            }
        }
    }

    public final void requestLayout() {
        this.privateFlags |= 16384;
        this.privateFlags &= -41;
        if (this.parentWidget != null) {
            this.parentWidget.requestLayout();
            return;
        }
        DXRuntimeContext dXRuntimeContext2 = getDXRuntimeContext();
        if (dXRuntimeContext2 != null) {
            DXRenderPipeline dxRenderPipeline = dXRuntimeContext2.getDxRenderPipeline();
            DXControlEventCenter dxControlEventCenter = dXRuntimeContext2.getDxControlEventCenter();
            if (dxRenderPipeline != null && dxControlEventCenter != null) {
                DXPipelineCacheManager pipelineCacheManager = dxRenderPipeline.getPipelineCacheManager();
                if (pipelineCacheManager != null) {
                    pipelineCacheManager.removeCache(dXRuntimeContext2.getCacheIdentify());
                }
                DXPipelineScheduleEvent dXPipelineScheduleEvent = new DXPipelineScheduleEvent();
                dXPipelineScheduleEvent.stage = 3;
                dXPipelineScheduleEvent.sender = this;
                dxControlEventCenter.postEvent(dXPipelineScheduleEvent);
            }
        }
    }

    public final void setNeedRender(Context context) {
        this.privateFlags |= 256;
        renderView(context);
    }

    public boolean hasCornerRadius() {
        return this.cornerRadiusLeftTop > 0 || this.cornerRadiusRightBottom > 0 || this.cornerRadiusLeftBottom > 0 || this.cornerRadiusRightTop > 0 || this.cornerRadius > 0;
    }

    public final void setIntAttribute(long j, int i) {
        if (DXHashConstant.DX_VIEW_WIDTH == j) {
            this.layoutWidth = i;
        } else if (DXHashConstant.DX_VIEW_HEIGHT == j) {
            this.layoutHeight = i;
        } else if (DXHashConstant.DX_VIEW_MARGINLEFT == j) {
            this.marginLeft = i;
        } else if (DXHashConstant.DX_VIEW_MARGINRIGHT == j) {
            this.marginRight = i;
        } else if (DXHashConstant.DX_VIEW_MARGINTOP == j) {
            this.marginTop = i;
        } else if (DXHashConstant.DX_VIEW_MARGINBOTTOM == j) {
            this.marginBottom = i;
        } else if (DXHashConstant.DX_VIEW_PADDINGLEFT == j) {
            this.paddingLeft = i;
        } else if (DXHashConstant.DX_VIEW_PADDINGRIGHT == j) {
            this.paddingRight = i;
        } else if (DXHashConstant.DX_VIEW_PADDINGTOP == j) {
            this.paddingTop = i;
        } else if (DXHashConstant.DX_VIEW_PADDINGBOTTOM == j) {
            this.paddingBottom = i;
        } else if (DXHashConstant.DX_VIEW_GRAVITY == j && i >= 0 && i <= 8) {
            this.layoutGravity = i;
            this.propertyInitFlag |= 1;
        } else if (DXHashConstant.DX_VIEW_CHILDGRAVITY == j && i >= 0 && i <= 8) {
            this.childGravity = i;
        } else if (DXHashConstant.DX_VIEW_DIRECTION == j && (i == 0 || i == 1)) {
            setDirection(i);
        } else if (DXHashConstant.DX_VIEW_VISIBILITY == j && (i == 0 || i == 1 || i == 2)) {
            this.visibility = i;
        } else if (DXHashConstant.DX_VIEW_CORNERRADIUS == j) {
            this.cornerRadius = i;
            this.needSetBackground = true;
        } else if (DXHashConstant.DX_VIEW_CORNERRADIUSLEFTTOP == j) {
            this.cornerRadiusLeftTop = i;
            this.needSetBackground = true;
        } else if (DXHashConstant.DX_VIEW_CORNERRADIUSRIGHTTOP == j) {
            this.cornerRadiusRightTop = i;
            this.needSetBackground = true;
        } else if (DXHashConstant.DX_VIEW_CORNERRADIUSLEFTBOTTOM == j) {
            this.cornerRadiusLeftBottom = i;
            this.needSetBackground = true;
        } else if (DXHashConstant.DX_VIEW_CORNERRADIUSRIGHTBOTTOM == j) {
            this.cornerRadiusRightBottom = i;
            this.needSetBackground = true;
        } else if (DXHashConstant.DX_VIEW_BORDERWIDTH == j) {
            this.borderWidth = i;
            this.needSetBackground = true;
        } else if (DXHashConstant.DX_VIEW_BORDERCOLOR == j) {
            this.borderColor = i;
            this.needSetBackground = true;
        } else if (DXHashConstant.DX_VIEW_BORDERTYPE == j) {
            this.borderType = i;
            this.needSetBackground = true;
        } else if (DXHashConstant.DX_VIEW_BORDERDASHWIDTH == j) {
            this.dashWidth = i;
        } else if (DXHashConstant.DX_VIEW_BORDERDASHGAP == j) {
            this.dashGap = i;
        } else if (DXHashConstant.DX_VIEW_ACCESSIBLILLITY == j) {
            this.accessibility = i;
        } else if (DXHashConstant.DX_VIEW_BACKGROUNDCOLOR == j) {
            this.backGroundColor = i;
            this.needSetBackground = true;
        } else if (DXHashConstant.DX_VIEW_ENABLED == j) {
            this.enabled = i;
        } else if (DXHashConstant.DX_VIEW_MINHEIHT == j) {
            this.minHeight = i;
        } else if (DXHashConstant.DX_VIEW_MINWIDTH == j) {
            this.minWidth = i;
        } else {
            boolean z = false;
            if (j == DXHashConstant.DX_VIEW_CLIPSTOBOUNDS) {
                if (i == 1) {
                    z = true;
                }
                this.clipChildren = z;
            } else if (j == DXHashConstant.DX_VIEW_DISABLEDARKMODE) {
                if (i != 0) {
                    z = true;
                }
                this.disableDarkMode = z;
            } else {
                onSetIntAttribute(j, i);
            }
        }
    }

    public final void setObjAttribute(long j, Object obj) {
        if (obj != null) {
            if (j == DXHashConstant.DX_VIEW_BACKGROUND_GRADIENT) {
                this.backgroundGradient = (GradientInfo) obj;
                this.needSetBackground = true;
            } else if (j == DXHashConstant.DX_VIEW_DARKMODECOLORMAP) {
                this.darkModeColorMap = (HashMap) obj;
            } else {
                onSetObjAttribute(j, obj);
            }
        }
    }

    public void setDoubleAttribute(long j, double d) {
        if (DXHashConstant.DX_VIEW_WEIGHT == j) {
            this.weight = d;
        } else if (DXHashConstant.DX_VIEW_ALPHA == j) {
            this.alpha = (float) d;
        } else {
            onSetDoubleAttribute(j, d);
        }
    }

    public void setStringAttribute(long j, String str) {
        if (DXHashConstant.DX_VIEW_USERID == j) {
            this.userId = str;
        } else if (DXHashConstant.DX_VIEW_ACCESSIBILITYTEXT == j) {
            this.accessibilityText = str;
        } else if (j == DXHashConstant.DX_VIEW_ANIMATION) {
            this.animation = str;
        } else {
            onSetStringAttribute(j, str);
        }
    }

    public void setLongAttribute(long j, long j2) {
        onSetLongAttribute(j, j2);
    }

    public void setListAttribute(long j, JSONArray jSONArray) {
        onSetListAttribute(j, jSONArray);
    }

    public void setMapAttribute(long j, JSONObject jSONObject) {
        onSetMapAttribute(j, jSONObject);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), i), getDefaultSize(getSuggestedMinimumHeight(), i2));
    }

    /* access modifiers changed from: protected */
    public View onCreateView(Context context) {
        return new View(context);
    }

    public final void bindEvent(Context context) {
        if (this.eventHandlersExprNode != null) {
            View realView = getRealView();
            for (int i = 0; i < this.eventHandlersExprNode.size(); i++) {
                getReferenceNode().onBindEvent(context, realView, this.eventHandlersExprNode.keyAt(i));
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onBindEvent(Context context, View view, long j) {
        if (this.enabled == 1) {
            if (j == DXHashConstant.DX_VIEWEVENT_ONTAP) {
                view.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        DXWidgetNode.this.onTapEvent();
                    }
                });
            } else if (j == DXHashConstant.DX_VIEWEVENT_ONLONGTAP) {
                view.setOnLongClickListener(new View.OnLongClickListener() {
                    public boolean onLongClick(View view) {
                        DXWidgetNode.this.onLongTap();
                        return true;
                    }
                });
            }
        }
        if (j == DXHashConstant.DX_VIEWEVENT_ON_BIND_DATA) {
            bindDataEvent();
        }
        prePareBindEvent(j);
    }

    private void prePareBindEvent(long j) {
        DXEvent dXEvent = new DXEvent(j);
        dXEvent.setPrepareBind(true);
        postEvent(dXEvent);
    }

    /* access modifiers changed from: package-private */
    public final void onTapEvent() {
        postEvent(new DXEvent(DXHashConstant.DX_VIEWEVENT_ONTAP));
    }

    /* access modifiers changed from: package-private */
    public void onLongTap() {
        postEvent(new DXEvent(DXHashConstant.DX_VIEWEVENT_ONLONGTAP));
    }

    /* access modifiers changed from: package-private */
    public void bindDataEvent() {
        postEvent(new DXEvent(DXHashConstant.DX_VIEWEVENT_ON_BIND_DATA));
    }

    public void sendBroadcastEvent(DXEvent dXEvent) {
        if (dXEvent != null) {
            if (getReferenceNode() != null) {
                postEvent(dXEvent);
            }
            if (getChildrenCount() > 0) {
                for (DXWidgetNode sendBroadcastEvent : getChildren()) {
                    sendBroadcastEvent.sendBroadcastEvent(dXEvent);
                }
            }
        }
    }

    public final boolean postEvent(DXEvent dXEvent) {
        if (!this.isFlatten) {
            return onEvent(dXEvent);
        }
        return getReferenceNode().onEvent(dXEvent);
    }

    /* access modifiers changed from: protected */
    public boolean onEvent(DXEvent dXEvent) {
        DXExprNode dXExprNode;
        try {
            if (this.eventHandlersExprNode != null) {
                if (dXEvent != null) {
                    DXExprNode dXExprNode2 = this.eventHandlersExprNode.get(dXEvent.getEventId());
                    if (dXExprNode2 == null) {
                        return false;
                    }
                    if ((dXExprNode2 instanceof DXEventNode) || (dXExprNode2 instanceof DXSerialBlockNode)) {
                        dXExprNode2.evaluate(dXEvent, getDXRuntimeContext());
                        return true;
                    }
                    DXRuntimeContext dXRuntimeContext2 = getDXRuntimeContext();
                    if (!(dXRuntimeContext2 == null || dXRuntimeContext2.getDxError() == null)) {
                        dXRuntimeContext2.getDxError().dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_EVENT, DXMonitorConstant.DX_MONITOR_EVENT_EXCETION_CAST_EXCEPTION, DXError.EVENT_DXEXPRNODE_CAST_ERROR, "eventid" + dXEvent.getEventId() + " exprNode id " + dXExprNode2.exprId + " exprNode name " + dXExprNode2.name));
                    }
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            DXExceptionUtil.printStack(e);
            String str = "dinamicx";
            DXTemplateItem dXTemplateItem = null;
            if (getDXRuntimeContext() != null) {
                str = getDXRuntimeContext().getBizType();
                dXTemplateItem = getDXRuntimeContext().getDxTemplateItem();
            }
            String str2 = str;
            DXTemplateItem dXTemplateItem2 = dXTemplateItem;
            String str3 = "";
            if (dXEvent != null) {
                str3 = str3 + "eventId : " + dXEvent.getEventId();
                if (!(this.eventHandlersExprNode == null || (dXExprNode = this.eventHandlersExprNode.get(dXEvent.getEventId())) == null)) {
                    str3 = str3 + " exprNode id " + dXExprNode.exprId + " exprNode name " + dXExprNode.name;
                }
            }
            DXAppMonitor.trackerError(str2, dXTemplateItem2, DXMonitorConstant.DX_MONITOR_PIPELINE, DXMonitorConstant.DX_MONITOR_SERVICE_ID_PIPELINE_ONEVENT, DXError.ONEVENT_CRASH, str3 + " crash stack: " + DXExceptionUtil.getStackTrace(e));
            return false;
        }
    }

    public DXWidgetNode queryWidgetNodeByAutoId(int i) {
        return queryRootWidgetNode().queryWTByAutoId(i);
    }

    public DXWidgetNode queryWidgetNodeByUserId(String str) {
        return queryRootWidgetNode().queryWTByUserId(str);
    }

    public DXWidgetNode queryWTByUserId(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (str.equals(this.userId)) {
            return this;
        }
        if (getChildrenCount() > 0) {
            for (DXWidgetNode queryWTByUserId : getChildren()) {
                DXWidgetNode queryWTByUserId2 = queryWTByUserId.queryWTByUserId(str);
                if (queryWTByUserId2 != null) {
                    return queryWTByUserId2;
                }
            }
        }
        return null;
    }

    public DXWidgetNode queryWTByAutoId(int i) {
        if (this.autoId == i) {
            return this;
        }
        if (getChildrenCount() <= 0) {
            return null;
        }
        for (DXWidgetNode queryWTByAutoId : getChildren()) {
            DXWidgetNode queryWTByAutoId2 = queryWTByAutoId.queryWTByAutoId(i);
            if (queryWTByAutoId2 != null) {
                return queryWTByAutoId2;
            }
        }
        return null;
    }

    public DXWidgetNode queryRootWidgetNode() {
        DXWidgetNode dXWidgetNode = this;
        while (dXWidgetNode.getParentWidget() != null) {
            dXWidgetNode = dXWidgetNode.getParentWidget();
        }
        return dXWidgetNode;
    }

    public void bindRuntimeContext(DXRuntimeContext dXRuntimeContext2) {
        bindRuntimeContext(dXRuntimeContext2, false);
    }

    public void bindRuntimeContext(DXRuntimeContext dXRuntimeContext2, boolean z) {
        if (dXRuntimeContext2 != null) {
            Object obj = null;
            int i = 0;
            if (z) {
                obj = this.dXRuntimeContext.getSubData();
                i = this.dXRuntimeContext.getSubdataIndex();
            }
            if (this.dXRuntimeContext != dXRuntimeContext2) {
                this.dXRuntimeContext = dXRuntimeContext2.cloneWithWidgetNode(this);
                if (z) {
                    this.dXRuntimeContext.setSubData(obj);
                    this.dXRuntimeContext.setSubdataIndex(i);
                }
            }
            if (this.childrenCount > 0) {
                for (DXWidgetNode bindRuntimeContext : this.children) {
                    bindRuntimeContext.bindRuntimeContext(dXRuntimeContext2, z);
                }
            }
        }
    }

    public int indexOf(DXWidgetNode dXWidgetNode) {
        if (dXWidgetNode == null) {
            return -1;
        }
        for (int i = 0; i < getChildrenCount(); i++) {
            if (getChildAt(i).getAutoId() == dXWidgetNode.getAutoId()) {
                return i;
            }
        }
        return -1;
    }

    /* access modifiers changed from: protected */
    public int getLayoutDirection() {
        return getDirection();
    }

    public int getLeftMarginWithDirection() {
        if (getDirection() == 1) {
            return this.marginRight;
        }
        return this.marginLeft;
    }

    public int getRightMarginWithDirection() {
        if (getDirection() == 1) {
            return this.marginLeft;
        }
        return this.marginRight;
    }

    public int getPaddingLeftWithDirection() {
        if (getDirection() == 1) {
            return this.paddingRight;
        }
        return this.paddingLeft;
    }

    public int getPaddingRightWithDirection() {
        if (getDirection() == 1) {
            return this.paddingLeft;
        }
        return this.paddingRight;
    }

    public static class Builder implements IDXBuilderWidgetNode {
        public DXWidgetNode build(Object obj) {
            return new DXWidgetNode();
        }
    }

    public DXWidgetNode build(Object obj) {
        return new DXWidgetNode();
    }

    public static class DXMeasureSpec {
        public static final int AT_MOST = Integer.MIN_VALUE;
        public static final int EXACTLY = 1073741824;
        private static final int MODE_MASK = -1073741824;
        private static final int MODE_SHIFT = 30;
        public static final int UNSPECIFIED = 0;

        @Retention(RetentionPolicy.SOURCE)
        public @interface MeasureSpecMode {
        }

        @SuppressLint({"WrongConstant"})
        public static int getMode(int i) {
            return i & MODE_MASK;
        }

        public static int getSize(int i) {
            return i & 1073741823;
        }

        public static int makeMeasureSpec(@IntRange(from = 0, to = 1073741823) int i, int i2) {
            return (i & 1073741823) | (i2 & MODE_MASK);
        }

        public static int makeSafeMeasureSpec(int i, int i2) {
            return makeMeasureSpec(i, i2);
        }

        static int adjust(int i, int i2) {
            int mode = getMode(i);
            int size = getSize(i);
            if (mode == 0) {
                return makeMeasureSpec(size, 0);
            }
            int i3 = size + i2;
            if (i3 < 0) {
                i3 = 0;
            }
            return makeMeasureSpec(i3, mode);
        }

        public static String toString(int i) {
            int mode = getMode(i);
            int size = getSize(i);
            StringBuilder sb = new StringBuilder("MeasureSpec: ");
            if (mode == 0) {
                sb.append("UNSPECIFIED ");
            } else if (mode == 1073741824) {
                sb.append("EXACTLY ");
            } else if (mode == Integer.MIN_VALUE) {
                sb.append("AT_MOST ");
            } else {
                sb.append(mode);
                sb.append(Operators.SPACE_STR);
            }
            sb.append(size);
            return sb.toString();
        }
    }

    public int getLeft() {
        return this.left;
    }

    public int getRight() {
        return this.right;
    }

    public int getTop() {
        return this.top;
    }

    public int getBottom() {
        return this.bottom;
    }

    public void setLeft(int i) {
        this.left = i;
    }

    public void setRight(int i) {
        this.right = i;
    }

    public void setTop(int i) {
        this.top = i;
    }

    public void setBottom(int i) {
        this.bottom = i;
    }

    public float getAlpha() {
        return this.alpha;
    }

    public void setAlpha(float f) {
        this.alpha = f;
    }

    public int getBorderWidth() {
        return this.borderWidth;
    }

    public void setBorderWidth(int i) {
        if (this.borderWidth != i) {
            this.borderWidth = i;
            this.needSetBackground = true;
        }
    }

    public int getBorderColor() {
        return this.borderColor;
    }

    public void setBorderColor(int i) {
        if (i != this.borderColor) {
            this.borderColor = i;
            this.needSetBackground = true;
        }
    }

    public int getBorderType() {
        return this.borderType;
    }

    public void setBorderType(int i) {
        if (this.borderType != this.borderType) {
            this.borderType = this.borderType;
            this.needSetBackground = true;
        }
    }

    public int getBackGroundColor() {
        return this.backGroundColor;
    }

    public void setBackGroundColor(int i) {
        if (i != this.backGroundColor) {
            this.backGroundColor = i;
            this.needSetBackground = true;
        }
    }

    public String getAccessibilityText() {
        return this.accessibilityText;
    }

    public void setAccessibilityText(String str) {
        this.accessibilityText = str;
    }

    public int getAccessibility() {
        return this.accessibility;
    }

    public void setAccessibility(int i) {
        this.accessibility = i;
    }

    public boolean hasAccessibilityOn() {
        return this.accessibility == 1;
    }

    public boolean hasAccessibilityAuto() {
        return this.accessibility == 3;
    }

    public int getCornerRadius() {
        return this.cornerRadius;
    }

    public void setCornerRadius(int i) {
        if (this.cornerRadius != i) {
            this.cornerRadius = i;
            this.needSetBackground = true;
        }
    }

    public void setCornerRadius(int i, int i2, int i3, int i4) {
        this.cornerRadiusLeftTop = i;
        this.cornerRadiusRightTop = i2;
        this.cornerRadiusLeftBottom = i3;
        this.cornerRadiusRightBottom = i4;
        this.needSetBackground = true;
    }

    public int getCornerRadiusLeftTop() {
        return this.cornerRadiusLeftTop;
    }

    public int getCornerRadiusRightTop() {
        return this.cornerRadiusRightTop;
    }

    public int getCornerRadiusLeftBottom() {
        return this.cornerRadiusLeftBottom;
    }

    public int getCornerRadiusRightBottom() {
        return this.cornerRadiusRightBottom;
    }

    public void setEventHandlersExprNode(DXLongSparseArray<DXExprNode> dXLongSparseArray) {
        this.eventHandlersExprNode = dXLongSparseArray;
    }

    public void setDataParsersExprNode(DXLongSparseArray<DXExprNode> dXLongSparseArray) {
        this.dataParsersExprNode = dXLongSparseArray;
    }

    public void setEnumMap(DXLongSparseArray<Map<String, Integer>> dXLongSparseArray) {
        this.enumMap = dXLongSparseArray;
    }

    public DXWidgetNode getParentWidget() {
        return this.parentWidget;
    }

    public void setParentWidget(DXWidgetNode dXWidgetNode) {
        this.parentWidget = dXWidgetNode;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String str) {
        this.userId = str;
    }

    public int getLayoutWidth() {
        return this.layoutWidth;
    }

    public void setLayoutWidth(int i) {
        this.layoutWidth = i;
    }

    public int getLayoutHeight() {
        return this.layoutHeight;
    }

    public void setLayoutHeight(int i) {
        this.layoutHeight = i;
    }

    public double getWeight() {
        return this.weight;
    }

    public void setWeight(double d) {
        this.weight = d;
    }

    public void setMarginBottom(int i) {
        this.marginBottom = i;
    }

    public int getPaddingLeft() {
        return this.paddingLeft;
    }

    public void setPaddingLeft(int i) {
        this.paddingLeft = i;
    }

    public int getPaddingRight() {
        return this.paddingRight;
    }

    public void setPaddingRight(int i) {
        this.paddingRight = i;
    }

    public int getPaddingBottom() {
        return this.paddingBottom;
    }

    public void setPaddingBottom(int i) {
        this.paddingBottom = i;
    }

    public int getPaddingTop() {
        return this.paddingTop;
    }

    public void setPaddingTop(int i) {
        this.paddingTop = i;
    }

    public void setVisibility(int i) {
        this.visibility = i;
    }

    public int getLayoutGravity() {
        return this.layoutGravity;
    }

    public void setLayoutGravity(int i) {
        this.layoutGravity = i;
        this.propertyInitFlag |= 1;
    }

    public int getChildGravity() {
        return this.childGravity;
    }

    public void setChildGravity(int i) {
        this.childGravity = i;
    }

    public int getDirection() {
        if (this.direction != -1) {
            return this.direction;
        }
        if (this.dXRuntimeContext != null) {
            return this.dXRuntimeContext.getParentDirectionSpec();
        }
        return 0;
    }

    public void setDirection(int i) {
        this.direction = i;
    }

    public int getEnabled() {
        return this.enabled;
    }

    public void setEnabled(int i) {
        this.enabled = i;
    }

    public int getMinHeight() {
        return this.minHeight;
    }

    public void setMinHeight(int i) {
        this.minHeight = i;
    }

    public int getMinWidth() {
        return this.minWidth;
    }

    public void setMinWidth(int i) {
        this.minWidth = i;
    }

    public boolean isFlatten() {
        return this.isFlatten;
    }

    public void setFlatten(boolean z) {
        this.isFlatten = z;
    }

    public boolean isNeedSetBackground() {
        return this.needSetBackground;
    }

    public void setNeedSetBackground(boolean z) {
        this.needSetBackground = z;
    }

    public void setCornerRadiusLeftTop(int i) {
        this.cornerRadiusLeftTop = i;
    }

    public void setCornerRadiusRightTop(int i) {
        this.cornerRadiusRightTop = i;
    }

    public void setCornerRadiusLeftBottom(int i) {
        this.cornerRadiusLeftBottom = i;
    }

    public void setCornerRadiusRightBottom(int i) {
        this.cornerRadiusRightBottom = i;
    }

    public int getMarginLeft() {
        return this.marginLeft;
    }

    public void setMarginLeft(int i) {
        this.marginLeft = i;
    }

    public int getMarginTop() {
        return this.marginTop;
    }

    public void setMarginTop(int i) {
        this.marginTop = i;
    }

    public int getMarginRight() {
        return this.marginRight;
    }

    public void setMarginRight(int i) {
        this.marginRight = i;
    }

    public int getMarginBottom() {
        return this.marginBottom;
    }

    public String getAnimation() {
        return this.animation;
    }

    public void setAnimation(String str) {
        this.animation = str;
    }

    public float getTranslateX() {
        return this.translateX;
    }

    public void setTranslateX(float f) {
        this.translateX = f;
    }

    public float getTranslateY() {
        return this.translateY;
    }

    public void setTranslateY(float f) {
        this.translateY = f;
    }

    public float getScaleX() {
        return this.scaleX;
    }

    public void setScaleX(float f) {
        this.scaleX = f;
    }

    public float getScaleY() {
        return this.scaleY;
    }

    public void setScaleY(float f) {
        this.scaleY = f;
    }

    public float getRotationX() {
        return this.rotationX;
    }

    public void setRotationX(float f) {
        this.rotationX = f;
    }

    public float getRotationY() {
        return this.rotationY;
    }

    public void setRotationY(float f) {
        this.rotationY = f;
    }

    public float getRotationZ() {
        return this.rotationZ;
    }

    public void setRotationZ(float f) {
        this.rotationZ = f;
    }

    public boolean isDisableDarkMode() {
        return this.disableDarkMode;
    }

    public void setDisableDarkMode(boolean z) {
        this.disableDarkMode = z;
    }

    public Map<String, DXBindingXSpec> getBindingXSpecMap() {
        return this.bindingXSpecMap;
    }

    public void setBindingXSpecMap(Map<String, DXBindingXSpec> map) {
        this.bindingXSpecMap = map;
    }

    public void setStatFlag(int i) {
        this.privateFlags = i | this.privateFlags;
    }

    public void unsetStatFlag(int i) {
        this.privateFlags = (i ^ -1) & this.privateFlags;
    }

    public boolean getStatInPrivateFlags(int i) {
        return (this.privateFlags & i) == i;
    }

    public int getLastAutoId() {
        return this.lastAutoId;
    }

    public void setLastAutoId(int i) {
        this.lastAutoId = i;
    }

    public DXWidgetNode getSourceWidget() {
        if (this.sourceWidgetWR == null) {
            return null;
        }
        return (DXWidgetNode) this.sourceWidgetWR.get();
    }

    public void setSourceWidget(DXWidgetNode dXWidgetNode) {
        if (dXWidgetNode != null) {
            this.sourceWidgetWR = new WeakReference<>(dXWidgetNode);
        }
    }

    public GradientInfo getBackgroundGradient() {
        return this.backgroundGradient;
    }

    public void setBackgroundGradient(GradientInfo gradientInfo) {
        this.backgroundGradient = gradientInfo;
    }

    public DXWidgetNode getReferenceNode() {
        if (this.referenceNode == null) {
            return null;
        }
        return (DXWidgetNode) this.referenceNode.get();
    }

    public void setReferenceNode(DXWidgetNode dXWidgetNode) {
        if (dXWidgetNode == null) {
            this.referenceNode = null;
        } else {
            this.referenceNode = new WeakReference<>(dXWidgetNode);
        }
    }

    public boolean isClipChildren() {
        return this.clipChildren;
    }

    public void removeBindingXSpec(DXBindingXSpec dXBindingXSpec) {
        if (this.bindingXExecutingMap != null && dXBindingXSpec != null && !TextUtils.isEmpty(dXBindingXSpec.name)) {
            this.bindingXExecutingMap.remove(dXBindingXSpec.name);
        }
    }

    public void putBindingXExecutingSpec(DXBindingXSpec dXBindingXSpec) {
        if (dXBindingXSpec != null && !TextUtils.isEmpty(dXBindingXSpec.name)) {
            if (this.bindingXExecutingMap == null) {
                this.bindingXExecutingMap = new HashMap();
            }
            this.bindingXExecutingMap.put(dXBindingXSpec.name, dXBindingXSpec);
        }
    }

    public boolean hasExecutingAnimationSpec() {
        if (this.bindingXExecutingMap != null && this.bindingXExecutingMap.size() > 0) {
            return true;
        }
        return false;
    }

    public void setBindingXExecutingMap(Map<String, DXBindingXSpec> map) {
        this.bindingXExecutingMap = map;
    }

    public Map<String, DXBindingXSpec> getBindingXExecutingMap() {
        return this.bindingXExecutingMap;
    }

    public boolean containsExecutingAnimationSpec(String str) {
        if (this.bindingXExecutingMap == null || this.bindingXExecutingMap.size() == 0 || TextUtils.isEmpty(str)) {
            return false;
        }
        return this.bindingXExecutingMap.containsKey(str);
    }

    public static class GradientInfo {
        private int gradientType = -1;
        private int[] linearGradientColors;
        private GradientDrawable.Orientation linearGradientDirection;

        public GradientDrawable.Orientation getLinearGradientDirection() {
            return this.linearGradientDirection;
        }

        public void setLinearGradientDirection(GradientDrawable.Orientation orientation) {
            this.linearGradientDirection = orientation;
        }

        public int[] getLinearGradientColors() {
            return this.linearGradientColors;
        }

        public void setLinearGradientColors(int[] iArr) {
            this.linearGradientColors = iArr;
        }

        public int getGradientType() {
            return this.gradientType;
        }

        public void setGradientType(int i) {
            this.gradientType = i;
        }
    }

    /* access modifiers changed from: protected */
    public int tryFetchDarkModeColor(String str, int i, @ColorInt int i2) {
        Integer num;
        if (!needHandleDark()) {
            return i2;
        }
        if (this.darkModeColorMap != null && (num = this.darkModeColorMap.get(str)) != null) {
            this.hasHandleDark = true;
            return num.intValue();
        } else if (!DXDarkModeCenter.hasSwitchInterface()) {
            return i2;
        } else {
            this.hasHandleDark = true;
            return DXDarkModeCenter.switchDarkModeColor(i, i2);
        }
    }

    /* access modifiers changed from: protected */
    public boolean needHandleDark() {
        return DXDarkModeCenter.isSupportDarkMode() && DXDarkModeCenter.isDark() && !isDisableDarkMode();
    }

    /* access modifiers changed from: protected */
    public void setForceDark(View view) {
        if (DXDarkModeCenter.isSupportDarkMode() && DXDarkModeCenter.isDark()) {
            if (this.disableDarkMode) {
                DXDarkModeCenter.disableForceDark(view);
            } else if (extraHandleDark() || this.hasHandleDark) {
                DXDarkModeCenter.disableForceDark(view);
            }
        }
    }
}
