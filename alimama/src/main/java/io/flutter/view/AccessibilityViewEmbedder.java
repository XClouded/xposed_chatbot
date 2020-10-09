package io.flutter.view;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import android.view.accessibility.AccessibilityRecord;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Keep
final class AccessibilityViewEmbedder {
    private static final String TAG = "AccessibilityBridge";
    private final Map<View, Rect> embeddedViewToDisplayBounds;
    private final SparseArray<ViewAndId> flutterIdToOrigin = new SparseArray<>();
    private int nextFlutterId;
    private final Map<ViewAndId, Integer> originToFlutterId;
    private final ReflectionAccessors reflectionAccessors = new ReflectionAccessors();
    private final View rootAccessibilityView;

    AccessibilityViewEmbedder(@NonNull View view, int i) {
        this.rootAccessibilityView = view;
        this.nextFlutterId = i;
        this.originToFlutterId = new HashMap();
        this.embeddedViewToDisplayBounds = new HashMap();
    }

    public AccessibilityNodeInfo getRootNode(@NonNull View view, int i, @NonNull Rect rect) {
        AccessibilityNodeInfo createAccessibilityNodeInfo = view.createAccessibilityNodeInfo();
        Long access$100 = this.reflectionAccessors.getSourceNodeId(createAccessibilityNodeInfo);
        if (access$100 == null) {
            return null;
        }
        this.embeddedViewToDisplayBounds.put(view, rect);
        cacheVirtualIdMappings(view, ReflectionAccessors.getVirtualNodeId(access$100.longValue()), i);
        return convertToFlutterNode(createAccessibilityNodeInfo, i, view);
    }

    @Nullable
    public AccessibilityNodeInfo createAccessibilityNodeInfo(int i) {
        AccessibilityNodeInfo createAccessibilityNodeInfo;
        ViewAndId viewAndId = this.flutterIdToOrigin.get(i);
        if (viewAndId == null || !this.embeddedViewToDisplayBounds.containsKey(viewAndId.view) || viewAndId.view.getAccessibilityNodeProvider() == null || (createAccessibilityNodeInfo = viewAndId.view.getAccessibilityNodeProvider().createAccessibilityNodeInfo(viewAndId.id)) == null) {
            return null;
        }
        return convertToFlutterNode(createAccessibilityNodeInfo, i, viewAndId.view);
    }

    @NonNull
    private AccessibilityNodeInfo convertToFlutterNode(@NonNull AccessibilityNodeInfo accessibilityNodeInfo, int i, @NonNull View view) {
        AccessibilityNodeInfo obtain = AccessibilityNodeInfo.obtain(this.rootAccessibilityView, i);
        obtain.setPackageName(this.rootAccessibilityView.getContext().getPackageName());
        obtain.setSource(this.rootAccessibilityView, i);
        obtain.setClassName(accessibilityNodeInfo.getClassName());
        copyAccessibilityFields(accessibilityNodeInfo, obtain);
        setFlutterNodesTranslateBounds(accessibilityNodeInfo, this.embeddedViewToDisplayBounds.get(view), obtain);
        addChildrenToFlutterNode(accessibilityNodeInfo, view, obtain);
        setFlutterNodeParent(accessibilityNodeInfo, view, obtain);
        return obtain;
    }

    private void setFlutterNodeParent(@NonNull AccessibilityNodeInfo accessibilityNodeInfo, @NonNull View view, @NonNull AccessibilityNodeInfo accessibilityNodeInfo2) {
        Long access$300 = this.reflectionAccessors.getParentNodeId(accessibilityNodeInfo);
        if (access$300 != null) {
            Integer num = this.originToFlutterId.get(new ViewAndId(view, ReflectionAccessors.getVirtualNodeId(access$300.longValue())));
            if (num != null) {
                accessibilityNodeInfo2.setParent(this.rootAccessibilityView, num.intValue());
            }
        }
    }

    private void addChildrenToFlutterNode(@NonNull AccessibilityNodeInfo accessibilityNodeInfo, @NonNull View view, @NonNull AccessibilityNodeInfo accessibilityNodeInfo2) {
        int i;
        for (int i2 = 0; i2 < accessibilityNodeInfo.getChildCount(); i2++) {
            Long access$500 = this.reflectionAccessors.getChildId(accessibilityNodeInfo, i2);
            if (access$500 != null) {
                int access$200 = ReflectionAccessors.getVirtualNodeId(access$500.longValue());
                ViewAndId viewAndId = new ViewAndId(view, access$200);
                if (this.originToFlutterId.containsKey(viewAndId)) {
                    i = this.originToFlutterId.get(viewAndId).intValue();
                } else {
                    int i3 = this.nextFlutterId;
                    this.nextFlutterId = i3 + 1;
                    cacheVirtualIdMappings(view, access$200, i3);
                    i = i3;
                }
                accessibilityNodeInfo2.addChild(this.rootAccessibilityView, i);
            }
        }
    }

    private void cacheVirtualIdMappings(@NonNull View view, int i, int i2) {
        ViewAndId viewAndId = new ViewAndId(view, i);
        this.originToFlutterId.put(viewAndId, Integer.valueOf(i2));
        this.flutterIdToOrigin.put(i2, viewAndId);
    }

    private void setFlutterNodesTranslateBounds(@NonNull AccessibilityNodeInfo accessibilityNodeInfo, @NonNull Rect rect, @NonNull AccessibilityNodeInfo accessibilityNodeInfo2) {
        Rect rect2 = new Rect();
        accessibilityNodeInfo.getBoundsInParent(rect2);
        accessibilityNodeInfo2.setBoundsInParent(rect2);
        Rect rect3 = new Rect();
        accessibilityNodeInfo.getBoundsInScreen(rect3);
        rect3.offset(rect.left, rect.top);
        accessibilityNodeInfo2.setBoundsInScreen(rect3);
    }

    private void copyAccessibilityFields(@NonNull AccessibilityNodeInfo accessibilityNodeInfo, @NonNull AccessibilityNodeInfo accessibilityNodeInfo2) {
        accessibilityNodeInfo2.setAccessibilityFocused(accessibilityNodeInfo.isAccessibilityFocused());
        accessibilityNodeInfo2.setCheckable(accessibilityNodeInfo.isCheckable());
        accessibilityNodeInfo2.setChecked(accessibilityNodeInfo.isChecked());
        accessibilityNodeInfo2.setContentDescription(accessibilityNodeInfo.getContentDescription());
        accessibilityNodeInfo2.setEnabled(accessibilityNodeInfo.isEnabled());
        accessibilityNodeInfo2.setClickable(accessibilityNodeInfo.isClickable());
        accessibilityNodeInfo2.setFocusable(accessibilityNodeInfo.isFocusable());
        accessibilityNodeInfo2.setFocused(accessibilityNodeInfo.isFocused());
        accessibilityNodeInfo2.setLongClickable(accessibilityNodeInfo.isLongClickable());
        accessibilityNodeInfo2.setMovementGranularities(accessibilityNodeInfo.getMovementGranularities());
        accessibilityNodeInfo2.setPassword(accessibilityNodeInfo.isPassword());
        accessibilityNodeInfo2.setScrollable(accessibilityNodeInfo.isScrollable());
        accessibilityNodeInfo2.setSelected(accessibilityNodeInfo.isSelected());
        accessibilityNodeInfo2.setText(accessibilityNodeInfo.getText());
        accessibilityNodeInfo2.setVisibleToUser(accessibilityNodeInfo.isVisibleToUser());
        if (Build.VERSION.SDK_INT >= 18) {
            accessibilityNodeInfo2.setEditable(accessibilityNodeInfo.isEditable());
        }
        if (Build.VERSION.SDK_INT >= 19) {
            accessibilityNodeInfo2.setCanOpenPopup(accessibilityNodeInfo.canOpenPopup());
            accessibilityNodeInfo2.setCollectionInfo(accessibilityNodeInfo.getCollectionInfo());
            accessibilityNodeInfo2.setCollectionItemInfo(accessibilityNodeInfo.getCollectionItemInfo());
            accessibilityNodeInfo2.setContentInvalid(accessibilityNodeInfo.isContentInvalid());
            accessibilityNodeInfo2.setDismissable(accessibilityNodeInfo.isDismissable());
            accessibilityNodeInfo2.setInputType(accessibilityNodeInfo.getInputType());
            accessibilityNodeInfo2.setLiveRegion(accessibilityNodeInfo.getLiveRegion());
            accessibilityNodeInfo2.setMultiLine(accessibilityNodeInfo.isMultiLine());
            accessibilityNodeInfo2.setRangeInfo(accessibilityNodeInfo.getRangeInfo());
        }
        if (Build.VERSION.SDK_INT >= 21) {
            accessibilityNodeInfo2.setError(accessibilityNodeInfo.getError());
            accessibilityNodeInfo2.setMaxTextLength(accessibilityNodeInfo.getMaxTextLength());
        }
        if (Build.VERSION.SDK_INT >= 23) {
            accessibilityNodeInfo2.setContextClickable(accessibilityNodeInfo.isContextClickable());
        }
        if (Build.VERSION.SDK_INT >= 24) {
            accessibilityNodeInfo2.setDrawingOrder(accessibilityNodeInfo.getDrawingOrder());
            accessibilityNodeInfo2.setImportantForAccessibility(accessibilityNodeInfo.isImportantForAccessibility());
        }
        if (Build.VERSION.SDK_INT >= 26) {
            accessibilityNodeInfo2.setAvailableExtraData(accessibilityNodeInfo.getAvailableExtraData());
            accessibilityNodeInfo2.setHintText(accessibilityNodeInfo.getHintText());
            accessibilityNodeInfo2.setShowingHintText(accessibilityNodeInfo.isShowingHintText());
        }
    }

    public boolean requestSendAccessibilityEvent(@NonNull View view, @NonNull View view2, @NonNull AccessibilityEvent accessibilityEvent) {
        AccessibilityEvent obtain = AccessibilityEvent.obtain(accessibilityEvent);
        Long access$600 = this.reflectionAccessors.getRecordSourceNodeId(accessibilityEvent);
        if (access$600 == null) {
            return false;
        }
        int access$200 = ReflectionAccessors.getVirtualNodeId(access$600.longValue());
        Integer num = this.originToFlutterId.get(new ViewAndId(view, access$200));
        if (num == null) {
            int i = this.nextFlutterId;
            this.nextFlutterId = i + 1;
            num = Integer.valueOf(i);
            cacheVirtualIdMappings(view, access$200, num.intValue());
        }
        obtain.setSource(this.rootAccessibilityView, num.intValue());
        obtain.setClassName(accessibilityEvent.getClassName());
        obtain.setPackageName(accessibilityEvent.getPackageName());
        for (int i2 = 0; i2 < obtain.getRecordCount(); i2++) {
            AccessibilityRecord record = obtain.getRecord(i2);
            Long access$6002 = this.reflectionAccessors.getRecordSourceNodeId(record);
            if (access$6002 == null) {
                return false;
            }
            ViewAndId viewAndId = new ViewAndId(view, ReflectionAccessors.getVirtualNodeId(access$6002.longValue()));
            if (!this.originToFlutterId.containsKey(viewAndId)) {
                return false;
            }
            record.setSource(this.rootAccessibilityView, this.originToFlutterId.get(viewAndId).intValue());
        }
        return this.rootAccessibilityView.getParent().requestSendAccessibilityEvent(view2, obtain);
    }

    public boolean performAction(int i, int i2, @Nullable Bundle bundle) {
        AccessibilityNodeProvider accessibilityNodeProvider;
        ViewAndId viewAndId = this.flutterIdToOrigin.get(i);
        if (viewAndId == null || (accessibilityNodeProvider = viewAndId.view.getAccessibilityNodeProvider()) == null) {
            return false;
        }
        return accessibilityNodeProvider.performAction(viewAndId.id, i2, bundle);
    }

    @Nullable
    public Integer getRecordFlutterId(@NonNull View view, @NonNull AccessibilityRecord accessibilityRecord) {
        Long access$600 = this.reflectionAccessors.getRecordSourceNodeId(accessibilityRecord);
        if (access$600 == null) {
            return null;
        }
        return this.originToFlutterId.get(new ViewAndId(view, ReflectionAccessors.getVirtualNodeId(access$600.longValue())));
    }

    public boolean onAccessibilityHoverEvent(int i, @NonNull MotionEvent motionEvent) {
        MotionEvent motionEvent2 = motionEvent;
        ViewAndId viewAndId = this.flutterIdToOrigin.get(i);
        if (viewAndId == null) {
            return false;
        }
        Rect rect = this.embeddedViewToDisplayBounds.get(viewAndId.view);
        int pointerCount = motionEvent.getPointerCount();
        MotionEvent.PointerProperties[] pointerPropertiesArr = new MotionEvent.PointerProperties[pointerCount];
        MotionEvent.PointerCoords[] pointerCoordsArr = new MotionEvent.PointerCoords[pointerCount];
        for (int i2 = 0; i2 < motionEvent.getPointerCount(); i2++) {
            pointerPropertiesArr[i2] = new MotionEvent.PointerProperties();
            motionEvent2.getPointerProperties(i2, pointerPropertiesArr[i2]);
            MotionEvent.PointerCoords pointerCoords = new MotionEvent.PointerCoords();
            motionEvent2.getPointerCoords(i2, pointerCoords);
            pointerCoordsArr[i2] = new MotionEvent.PointerCoords(pointerCoords);
            pointerCoordsArr[i2].x -= (float) rect.left;
            pointerCoordsArr[i2].y -= (float) rect.top;
        }
        return viewAndId.view.dispatchGenericMotionEvent(MotionEvent.obtain(motionEvent.getDownTime(), motionEvent.getEventTime(), motionEvent.getAction(), motionEvent.getPointerCount(), pointerPropertiesArr, pointerCoordsArr, motionEvent.getMetaState(), motionEvent.getButtonState(), motionEvent.getXPrecision(), motionEvent.getYPrecision(), motionEvent.getDeviceId(), motionEvent.getEdgeFlags(), motionEvent.getSource(), motionEvent.getFlags()));
    }

    private static class ViewAndId {
        final int id;
        final View view;

        private ViewAndId(View view2, int i) {
            this.view = view2;
            this.id = i;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ViewAndId)) {
                return false;
            }
            ViewAndId viewAndId = (ViewAndId) obj;
            if (this.id != viewAndId.id || !this.view.equals(viewAndId.view)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return ((this.view.hashCode() + 31) * 31) + this.id;
        }
    }

    private static class ReflectionAccessors {
        @Nullable
        private final Field childNodeIdsField;
        @Nullable
        private final Method getChildId;
        @Nullable
        private final Method getParentNodeId;
        @Nullable
        private final Method getRecordSourceNodeId;
        @Nullable
        private final Method getSourceNodeId;
        @Nullable
        private final Method longArrayGetIndex;

        /* access modifiers changed from: private */
        public static int getVirtualNodeId(long j) {
            return (int) (j >> 32);
        }

        private static boolean isBitSet(long j, int i) {
            return (j & (1 << i)) != 0;
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v1, resolved type: java.lang.reflect.Method} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v2, resolved type: java.lang.reflect.Field} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v5, resolved type: java.lang.reflect.Field} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v3, resolved type: java.lang.reflect.Method} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v6, resolved type: java.lang.reflect.Method} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v9, resolved type: java.lang.reflect.Method} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v9, resolved type: java.lang.reflect.Field} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v11, resolved type: java.lang.reflect.Method} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v12, resolved type: java.lang.reflect.Method} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v15, resolved type: java.lang.reflect.Field} */
        /* JADX WARNING: Multi-variable type inference failed */
        @android.annotation.SuppressLint({"PrivateApi"})
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private ReflectionAccessors() {
            /*
                r9 = this;
                r9.<init>()
                r0 = 0
                r1 = 0
                java.lang.Class<android.view.accessibility.AccessibilityNodeInfo> r2 = android.view.accessibility.AccessibilityNodeInfo.class
                java.lang.String r3 = "getSourceNodeId"
                java.lang.Class[] r4 = new java.lang.Class[r0]     // Catch:{ NoSuchMethodException -> 0x0010 }
                java.lang.reflect.Method r2 = r2.getMethod(r3, r4)     // Catch:{ NoSuchMethodException -> 0x0010 }
                goto L_0x0018
            L_0x0010:
                java.lang.String r2 = "AccessibilityBridge"
                java.lang.String r3 = "can't invoke AccessibilityNodeInfo#getSourceNodeId with reflection"
                android.util.Log.w(r2, r3)
                r2 = r1
            L_0x0018:
                java.lang.Class<android.view.accessibility.AccessibilityRecord> r3 = android.view.accessibility.AccessibilityRecord.class
                java.lang.String r4 = "getSourceNodeId"
                java.lang.Class[] r5 = new java.lang.Class[r0]     // Catch:{ NoSuchMethodException -> 0x0023 }
                java.lang.reflect.Method r3 = r3.getMethod(r4, r5)     // Catch:{ NoSuchMethodException -> 0x0023 }
                goto L_0x002b
            L_0x0023:
                java.lang.String r3 = "AccessibilityBridge"
                java.lang.String r4 = "can't invoke AccessibiiltyRecord#getSourceNodeId with reflection"
                android.util.Log.w(r3, r4)
                r3 = r1
            L_0x002b:
                int r4 = android.os.Build.VERSION.SDK_INT
                r5 = 26
                r6 = 1
                if (r4 > r5) goto L_0x0061
                java.lang.Class<android.view.accessibility.AccessibilityNodeInfo> r4 = android.view.accessibility.AccessibilityNodeInfo.class
                java.lang.String r5 = "getParentNodeId"
                java.lang.Class[] r7 = new java.lang.Class[r0]     // Catch:{ NoSuchMethodException -> 0x003d }
                java.lang.reflect.Method r4 = r4.getMethod(r5, r7)     // Catch:{ NoSuchMethodException -> 0x003d }
                goto L_0x0045
            L_0x003d:
                java.lang.String r4 = "AccessibilityBridge"
                java.lang.String r5 = "can't invoke getParentNodeId with reflection"
                android.util.Log.w(r4, r5)
                r4 = r1
            L_0x0045:
                java.lang.Class<android.view.accessibility.AccessibilityNodeInfo> r5 = android.view.accessibility.AccessibilityNodeInfo.class
                java.lang.String r7 = "getChildId"
                java.lang.Class[] r6 = new java.lang.Class[r6]     // Catch:{ NoSuchMethodException -> 0x0055 }
                java.lang.Class r8 = java.lang.Integer.TYPE     // Catch:{ NoSuchMethodException -> 0x0055 }
                r6[r0] = r8     // Catch:{ NoSuchMethodException -> 0x0055 }
                java.lang.reflect.Method r0 = r5.getMethod(r7, r6)     // Catch:{ NoSuchMethodException -> 0x0055 }
                r5 = r1
                goto L_0x005e
            L_0x0055:
                java.lang.String r0 = "AccessibilityBridge"
                java.lang.String r5 = "can't invoke getChildId with reflection"
                android.util.Log.w(r0, r5)
                r0 = r1
                r5 = r0
            L_0x005e:
                r1 = r4
                r4 = r5
                goto L_0x008b
            L_0x0061:
                java.lang.Class<android.view.accessibility.AccessibilityNodeInfo> r4 = android.view.accessibility.AccessibilityNodeInfo.class
                java.lang.String r5 = "mChildNodeIds"
                java.lang.reflect.Field r4 = r4.getDeclaredField(r5)     // Catch:{ ClassNotFoundException | NoSuchFieldException | NoSuchMethodException | NullPointerException -> 0x0081 }
                r4.setAccessible(r6)     // Catch:{ ClassNotFoundException | NoSuchFieldException | NoSuchMethodException | NullPointerException -> 0x0081 }
                java.lang.String r5 = "android.util.LongArray"
                java.lang.Class r5 = java.lang.Class.forName(r5)     // Catch:{ ClassNotFoundException | NoSuchFieldException | NoSuchMethodException | NullPointerException -> 0x0081 }
                java.lang.String r7 = "get"
                java.lang.Class[] r6 = new java.lang.Class[r6]     // Catch:{ ClassNotFoundException | NoSuchFieldException | NoSuchMethodException | NullPointerException -> 0x0081 }
                java.lang.Class r8 = java.lang.Integer.TYPE     // Catch:{ ClassNotFoundException | NoSuchFieldException | NoSuchMethodException | NullPointerException -> 0x0081 }
                r6[r0] = r8     // Catch:{ ClassNotFoundException | NoSuchFieldException | NoSuchMethodException | NullPointerException -> 0x0081 }
                java.lang.reflect.Method r0 = r5.getMethod(r7, r6)     // Catch:{ ClassNotFoundException | NoSuchFieldException | NoSuchMethodException | NullPointerException -> 0x0081 }
                r5 = r0
                r0 = r1
                goto L_0x008b
            L_0x0081:
                java.lang.String r0 = "AccessibilityBridge"
                java.lang.String r4 = "can't access childNodeIdsField with reflection"
                android.util.Log.w(r0, r4)
                r0 = r1
                r4 = r0
                r5 = r4
            L_0x008b:
                r9.getSourceNodeId = r2
                r9.getParentNodeId = r1
                r9.getRecordSourceNodeId = r3
                r9.getChildId = r0
                r9.childNodeIdsField = r4
                r9.longArrayGetIndex = r5
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: io.flutter.view.AccessibilityViewEmbedder.ReflectionAccessors.<init>():void");
        }

        /* access modifiers changed from: private */
        @Nullable
        public Long getSourceNodeId(@NonNull AccessibilityNodeInfo accessibilityNodeInfo) {
            if (this.getSourceNodeId == null) {
                return null;
            }
            try {
                return (Long) this.getSourceNodeId.invoke(accessibilityNodeInfo, new Object[0]);
            } catch (IllegalAccessException e) {
                Log.w(AccessibilityViewEmbedder.TAG, e);
                return null;
            } catch (InvocationTargetException e2) {
                Log.w(AccessibilityViewEmbedder.TAG, e2);
                return null;
            }
        }

        /* access modifiers changed from: private */
        @Nullable
        public Long getChildId(@NonNull AccessibilityNodeInfo accessibilityNodeInfo, int i) {
            if (this.getChildId == null && (this.childNodeIdsField == null || this.longArrayGetIndex == null)) {
                return null;
            }
            if (this.getChildId != null) {
                try {
                    return (Long) this.getChildId.invoke(accessibilityNodeInfo, new Object[]{Integer.valueOf(i)});
                } catch (IllegalAccessException e) {
                    Log.w(AccessibilityViewEmbedder.TAG, e);
                    return null;
                } catch (InvocationTargetException e2) {
                    Log.w(AccessibilityViewEmbedder.TAG, e2);
                    return null;
                }
            } else {
                try {
                    return Long.valueOf(((Long) this.longArrayGetIndex.invoke(this.childNodeIdsField.get(accessibilityNodeInfo), new Object[]{Integer.valueOf(i)})).longValue());
                } catch (IllegalAccessException e3) {
                    Log.w(AccessibilityViewEmbedder.TAG, e3);
                    return null;
                } catch (ArrayIndexOutOfBoundsException | InvocationTargetException e4) {
                    Log.w(AccessibilityViewEmbedder.TAG, e4);
                    return null;
                }
            }
        }

        /* access modifiers changed from: private */
        @Nullable
        public Long getParentNodeId(@NonNull AccessibilityNodeInfo accessibilityNodeInfo) {
            if (this.getParentNodeId != null) {
                try {
                    return Long.valueOf(((Long) this.getParentNodeId.invoke(accessibilityNodeInfo, new Object[0])).longValue());
                } catch (IllegalAccessException e) {
                    Log.w(AccessibilityViewEmbedder.TAG, e);
                } catch (InvocationTargetException e2) {
                    Log.w(AccessibilityViewEmbedder.TAG, e2);
                }
            }
            return yoinkParentIdFromParcel(accessibilityNodeInfo);
        }

        @Nullable
        private static Long yoinkParentIdFromParcel(AccessibilityNodeInfo accessibilityNodeInfo) {
            Long l = null;
            if (Build.VERSION.SDK_INT < 26) {
                Log.w(AccessibilityViewEmbedder.TAG, "Unexpected Android version. Unable to find the parent ID.");
                return null;
            }
            AccessibilityNodeInfo obtain = AccessibilityNodeInfo.obtain(accessibilityNodeInfo);
            Parcel obtain2 = Parcel.obtain();
            obtain2.setDataPosition(0);
            obtain.writeToParcel(obtain2, 0);
            obtain2.setDataPosition(0);
            long readLong = obtain2.readLong();
            if (isBitSet(readLong, 0)) {
                obtain2.readInt();
            }
            if (isBitSet(readLong, 1)) {
                obtain2.readLong();
            }
            if (isBitSet(readLong, 2)) {
                obtain2.readInt();
            }
            if (isBitSet(readLong, 3)) {
                l = Long.valueOf(obtain2.readLong());
            }
            obtain2.recycle();
            return l;
        }

        /* access modifiers changed from: private */
        @Nullable
        public Long getRecordSourceNodeId(@NonNull AccessibilityRecord accessibilityRecord) {
            if (this.getRecordSourceNodeId == null) {
                return null;
            }
            try {
                return (Long) this.getRecordSourceNodeId.invoke(accessibilityRecord, new Object[0]);
            } catch (IllegalAccessException e) {
                Log.w(AccessibilityViewEmbedder.TAG, e);
                return null;
            } catch (InvocationTargetException e2) {
                Log.w(AccessibilityViewEmbedder.TAG, e2);
                return null;
            }
        }
    }
}
