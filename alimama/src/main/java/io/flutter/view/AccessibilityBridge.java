package io.flutter.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.graphics.Rect;
import android.net.Uri;
import android.opengl.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import io.flutter.embedding.engine.systemchannels.AccessibilityChannel;
import io.flutter.plugin.platform.PlatformViewsAccessibilityDelegate;
import io.flutter.util.Predicate;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AccessibilityBridge extends AccessibilityNodeProvider {
    private static final int ACTION_SHOW_ON_SCREEN = 16908342;
    private static int FIRST_RESOURCE_ID = 267386881;
    private static final int MIN_ENGINE_GENERATED_NODE_ID = 65536;
    private static final int ROOT_NODE_ID = 0;
    private static final float SCROLL_EXTENT_FOR_INFINITY = 100000.0f;
    private static final float SCROLL_POSITION_CAP_FOR_INFINITY = 70000.0f;
    private static final String TAG = "AccessibilityBridge";
    /* access modifiers changed from: private */
    @NonNull
    public final AccessibilityChannel accessibilityChannel;
    /* access modifiers changed from: private */
    public int accessibilityFeatureFlags = 0;
    @Nullable
    private SemanticsNode accessibilityFocusedSemanticsNode;
    /* access modifiers changed from: private */
    @NonNull
    public final AccessibilityManager accessibilityManager;
    /* access modifiers changed from: private */
    public final AccessibilityChannel.AccessibilityMessageHandler accessibilityMessageHandler = new AccessibilityChannel.AccessibilityMessageHandler() {
        public void announce(@NonNull String str) {
            AccessibilityBridge.this.rootAccessibilityView.announceForAccessibility(str);
        }

        public void onTap(int i) {
            AccessibilityBridge.this.sendAccessibilityEvent(i, 1);
        }

        public void onLongPress(int i) {
            AccessibilityBridge.this.sendAccessibilityEvent(i, 2);
        }

        public void onTooltip(@NonNull String str) {
            AccessibilityEvent access$200 = AccessibilityBridge.this.obtainAccessibilityEvent(0, 32);
            access$200.getText().add(str);
            AccessibilityBridge.this.sendAccessibilityEvent(access$200);
        }

        public void updateCustomAccessibilityActions(ByteBuffer byteBuffer, String[] strArr) {
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            AccessibilityBridge.this.updateCustomAccessibilityActions(byteBuffer, strArr);
        }

        public void updateSemantics(ByteBuffer byteBuffer, String[] strArr) {
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            AccessibilityBridge.this.updateSemantics(byteBuffer, strArr);
        }
    };
    private final AccessibilityManager.AccessibilityStateChangeListener accessibilityStateChangeListener = new AccessibilityManager.AccessibilityStateChangeListener() {
        public void onAccessibilityStateChanged(boolean z) {
            if (z) {
                AccessibilityBridge.this.accessibilityChannel.setAccessibilityMessageHandler(AccessibilityBridge.this.accessibilityMessageHandler);
                AccessibilityBridge.this.accessibilityChannel.onAndroidAccessibilityEnabled();
            } else {
                AccessibilityBridge.this.accessibilityChannel.setAccessibilityMessageHandler((AccessibilityChannel.AccessibilityMessageHandler) null);
                AccessibilityBridge.this.accessibilityChannel.onAndroidAccessibilityDisabled();
            }
            if (AccessibilityBridge.this.onAccessibilityChangeListener != null) {
                AccessibilityBridge.this.onAccessibilityChangeListener.onAccessibilityChanged(z, AccessibilityBridge.this.accessibilityManager.isTouchExplorationEnabled());
            }
        }
    };
    @NonNull
    private final AccessibilityViewEmbedder accessibilityViewEmbedder;
    private final ContentObserver animationScaleObserver = new ContentObserver(new Handler()) {
        public void onChange(boolean z) {
            onChange(z, (Uri) null);
        }

        public void onChange(boolean z, Uri uri) {
            String str;
            if (Build.VERSION.SDK_INT < 17) {
                str = null;
            } else {
                str = Settings.Global.getString(AccessibilityBridge.this.contentResolver, "transition_animation_scale");
            }
            if (str != null && str.equals("0")) {
                int unused = AccessibilityBridge.this.accessibilityFeatureFlags = AccessibilityBridge.this.accessibilityFeatureFlags | AccessibilityFeature.DISABLE_ANIMATIONS.value;
            } else {
                int unused2 = AccessibilityBridge.this.accessibilityFeatureFlags = AccessibilityBridge.this.accessibilityFeatureFlags & (AccessibilityFeature.DISABLE_ANIMATIONS.value ^ -1);
            }
            AccessibilityBridge.this.sendLatestAccessibilityFlagsToFlutter();
        }
    };
    /* access modifiers changed from: private */
    @NonNull
    public final ContentResolver contentResolver;
    @NonNull
    private final Map<Integer, CustomAccessibilityAction> customAccessibilityActions = new HashMap();
    private Integer embeddedAccessibilityFocusedNodeId;
    private Integer embeddedInputFocusedNodeId;
    @NonNull
    private final List<Integer> flutterNavigationStack = new ArrayList();
    @NonNull
    private final Map<Integer, SemanticsNode> flutterSemanticsTree = new HashMap();
    @Nullable
    private SemanticsNode hoveredObject;
    @Nullable
    private SemanticsNode inputFocusedSemanticsNode;
    @Nullable
    private SemanticsNode lastInputFocusedSemanticsNode;
    @NonNull
    private Integer lastLeftFrameInset = 0;
    /* access modifiers changed from: private */
    @Nullable
    public OnAccessibilityChangeListener onAccessibilityChangeListener;
    @NonNull
    private final PlatformViewsAccessibilityDelegate platformViewsAccessibilityDelegate;
    private int previousRouteId = 0;
    /* access modifiers changed from: private */
    @NonNull
    public final View rootAccessibilityView;
    @RequiresApi(19)
    @TargetApi(19)
    private final AccessibilityManager.TouchExplorationStateChangeListener touchExplorationStateChangeListener;

    public interface OnAccessibilityChangeListener {
        void onAccessibilityChanged(boolean z, boolean z2);
    }

    static /* synthetic */ boolean lambda$shouldSetCollectionInfo$0(SemanticsNode semanticsNode, SemanticsNode semanticsNode2) {
        return semanticsNode2 == semanticsNode;
    }

    public AccessibilityBridge(@NonNull View view, @NonNull AccessibilityChannel accessibilityChannel2, @NonNull final AccessibilityManager accessibilityManager2, @NonNull ContentResolver contentResolver2, PlatformViewsAccessibilityDelegate platformViewsAccessibilityDelegate2) {
        this.rootAccessibilityView = view;
        this.accessibilityChannel = accessibilityChannel2;
        this.accessibilityManager = accessibilityManager2;
        this.contentResolver = contentResolver2;
        this.platformViewsAccessibilityDelegate = platformViewsAccessibilityDelegate2;
        this.accessibilityStateChangeListener.onAccessibilityStateChanged(accessibilityManager2.isEnabled());
        this.accessibilityManager.addAccessibilityStateChangeListener(this.accessibilityStateChangeListener);
        if (Build.VERSION.SDK_INT >= 19) {
            this.touchExplorationStateChangeListener = new AccessibilityManager.TouchExplorationStateChangeListener() {
                public void onTouchExplorationStateChanged(boolean z) {
                    if (z) {
                        int unused = AccessibilityBridge.this.accessibilityFeatureFlags = AccessibilityBridge.this.accessibilityFeatureFlags | AccessibilityFeature.ACCESSIBLE_NAVIGATION.value;
                    } else {
                        AccessibilityBridge.this.onTouchExplorationExit();
                        int unused2 = AccessibilityBridge.this.accessibilityFeatureFlags = AccessibilityBridge.this.accessibilityFeatureFlags & (AccessibilityFeature.ACCESSIBLE_NAVIGATION.value ^ -1);
                    }
                    AccessibilityBridge.this.sendLatestAccessibilityFlagsToFlutter();
                    if (AccessibilityBridge.this.onAccessibilityChangeListener != null) {
                        AccessibilityBridge.this.onAccessibilityChangeListener.onAccessibilityChanged(accessibilityManager2.isEnabled(), z);
                    }
                }
            };
            this.touchExplorationStateChangeListener.onTouchExplorationStateChanged(accessibilityManager2.isTouchExplorationEnabled());
            this.accessibilityManager.addTouchExplorationStateChangeListener(this.touchExplorationStateChangeListener);
        } else {
            this.touchExplorationStateChangeListener = null;
        }
        if (Build.VERSION.SDK_INT >= 17) {
            this.animationScaleObserver.onChange(false);
            this.contentResolver.registerContentObserver(Settings.Global.getUriFor("transition_animation_scale"), false, this.animationScaleObserver);
        }
        if (platformViewsAccessibilityDelegate2 != null) {
            platformViewsAccessibilityDelegate2.attachAccessibilityBridge(this);
        }
        this.accessibilityViewEmbedder = new AccessibilityViewEmbedder(view, 65536);
    }

    public void release() {
        if (this.platformViewsAccessibilityDelegate != null) {
            this.platformViewsAccessibilityDelegate.detachAccessibiltyBridge();
        }
        setOnAccessibilityChangeListener((OnAccessibilityChangeListener) null);
        this.accessibilityManager.removeAccessibilityStateChangeListener(this.accessibilityStateChangeListener);
        if (Build.VERSION.SDK_INT >= 19) {
            this.accessibilityManager.removeTouchExplorationStateChangeListener(this.touchExplorationStateChangeListener);
        }
        this.contentResolver.unregisterContentObserver(this.animationScaleObserver);
    }

    public boolean isAccessibilityEnabled() {
        return this.accessibilityManager.isEnabled();
    }

    public boolean isTouchExplorationEnabled() {
        return this.accessibilityManager.isTouchExplorationEnabled();
    }

    public void setOnAccessibilityChangeListener(@Nullable OnAccessibilityChangeListener onAccessibilityChangeListener2) {
        this.onAccessibilityChangeListener = onAccessibilityChangeListener2;
    }

    /* access modifiers changed from: private */
    public void sendLatestAccessibilityFlagsToFlutter() {
        this.accessibilityChannel.setAccessibilityFeatures(this.accessibilityFeatureFlags);
    }

    private boolean shouldSetCollectionInfo(SemanticsNode semanticsNode) {
        return semanticsNode.scrollChildren > 0 && (SemanticsNode.nullableHasAncestor(this.accessibilityFocusedSemanticsNode, 
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: ?: RETURN  
              (wrap: boolean : ?: TERNARYnull = (((wrap: int : 0x0000: INVOKE  (r0v0 int) = 
              (r3v0 'semanticsNode' io.flutter.view.AccessibilityBridge$SemanticsNode)
             io.flutter.view.AccessibilityBridge.SemanticsNode.access$1200(io.flutter.view.AccessibilityBridge$SemanticsNode):int type: STATIC) > (0 int) && ((wrap: boolean : 0x000d: INVOKE  (r3v2 boolean) = 
              (wrap: io.flutter.view.AccessibilityBridge$SemanticsNode : 0x0006: IGET  (r0v1 io.flutter.view.AccessibilityBridge$SemanticsNode) = (r2v0 'this' io.flutter.view.AccessibilityBridge A[THIS]) io.flutter.view.AccessibilityBridge.accessibilityFocusedSemanticsNode io.flutter.view.AccessibilityBridge$SemanticsNode)
              (wrap: io.flutter.view.-$$Lambda$AccessibilityBridge$JBDpWWlF54eN0gel8Jn1hY7yOmE : 0x000a: CONSTRUCTOR  (r1v0 io.flutter.view.-$$Lambda$AccessibilityBridge$JBDpWWlF54eN0gel8Jn1hY7yOmE) = 
              (r3v0 'semanticsNode' io.flutter.view.AccessibilityBridge$SemanticsNode)
             call: io.flutter.view.-$$Lambda$AccessibilityBridge$JBDpWWlF54eN0gel8Jn1hY7yOmE.<init>(io.flutter.view.AccessibilityBridge$SemanticsNode):void type: CONSTRUCTOR)
             io.flutter.view.AccessibilityBridge.SemanticsNode.access$1300(io.flutter.view.AccessibilityBridge$SemanticsNode, io.flutter.util.Predicate):boolean type: STATIC) == true || (wrap: boolean : 0x0017: INVOKE  (r3v5 boolean) = 
              (wrap: io.flutter.view.AccessibilityBridge$SemanticsNode : 0x0013: IGET  (r3v4 io.flutter.view.AccessibilityBridge$SemanticsNode) = (r2v0 'this' io.flutter.view.AccessibilityBridge A[THIS]) io.flutter.view.AccessibilityBridge.accessibilityFocusedSemanticsNode io.flutter.view.AccessibilityBridge$SemanticsNode)
              (wrap: io.flutter.view.-$$Lambda$AccessibilityBridge$_xgAbWhyKyeHrMGUVUYzbSFEeBY : 0x0015: SGET  (r0v2 io.flutter.view.-$$Lambda$AccessibilityBridge$_xgAbWhyKyeHrMGUVUYzbSFEeBY) =  io.flutter.view.-$$Lambda$AccessibilityBridge$_xgAbWhyKyeHrMGUVUYzbSFEeBY.INSTANCE io.flutter.view.-$$Lambda$AccessibilityBridge$_xgAbWhyKyeHrMGUVUYzbSFEeBY)
             io.flutter.view.AccessibilityBridge.SemanticsNode.access$1300(io.flutter.view.AccessibilityBridge$SemanticsNode, io.flutter.util.Predicate):boolean type: STATIC) == false))) ? true : false)
             in method: io.flutter.view.AccessibilityBridge.shouldSetCollectionInfo(io.flutter.view.AccessibilityBridge$SemanticsNode):boolean, dex: classes4.dex
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:256)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:221)
            	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:109)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
            	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
            	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:211)
            	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:204)
            	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:318)
            	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:271)
            	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:240)
            	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(Unknown Source)
            	at java.util.ArrayList.forEach(Unknown Source)
            	at java.util.stream.SortedOps$RefSortingSink.end(Unknown Source)
            	at java.util.stream.Sink$ChainedReference.end(Unknown Source)
            	at java.util.stream.AbstractPipeline.copyInto(Unknown Source)
            	at java.util.stream.AbstractPipeline.wrapAndCopyInto(Unknown Source)
            	at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(Unknown Source)
            	at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(Unknown Source)
            	at java.util.stream.AbstractPipeline.evaluate(Unknown Source)
            	at java.util.stream.ReferencePipeline.forEach(Unknown Source)
            	at jadx.core.codegen.ClassGen.addInnerClsAndMethods(ClassGen.java:236)
            	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:227)
            	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:112)
            	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:78)
            	at jadx.core.codegen.CodeGen.wrapCodeGen(CodeGen.java:44)
            	at jadx.core.codegen.CodeGen.generateJavaCode(CodeGen.java:33)
            	at jadx.core.codegen.CodeGen.generate(CodeGen.java:21)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:61)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
            Caused by: jadx.core.utils.exceptions.CodegenException: Error generate insn: ?: TERNARYnull = (((wrap: int : 0x0000: INVOKE  (r0v0 int) = 
              (r3v0 'semanticsNode' io.flutter.view.AccessibilityBridge$SemanticsNode)
             io.flutter.view.AccessibilityBridge.SemanticsNode.access$1200(io.flutter.view.AccessibilityBridge$SemanticsNode):int type: STATIC) > (0 int) && ((wrap: boolean : 0x000d: INVOKE  (r3v2 boolean) = 
              (wrap: io.flutter.view.AccessibilityBridge$SemanticsNode : 0x0006: IGET  (r0v1 io.flutter.view.AccessibilityBridge$SemanticsNode) = (r2v0 'this' io.flutter.view.AccessibilityBridge A[THIS]) io.flutter.view.AccessibilityBridge.accessibilityFocusedSemanticsNode io.flutter.view.AccessibilityBridge$SemanticsNode)
              (wrap: io.flutter.view.-$$Lambda$AccessibilityBridge$JBDpWWlF54eN0gel8Jn1hY7yOmE : 0x000a: CONSTRUCTOR  (r1v0 io.flutter.view.-$$Lambda$AccessibilityBridge$JBDpWWlF54eN0gel8Jn1hY7yOmE) = 
              (r3v0 'semanticsNode' io.flutter.view.AccessibilityBridge$SemanticsNode)
             call: io.flutter.view.-$$Lambda$AccessibilityBridge$JBDpWWlF54eN0gel8Jn1hY7yOmE.<init>(io.flutter.view.AccessibilityBridge$SemanticsNode):void type: CONSTRUCTOR)
             io.flutter.view.AccessibilityBridge.SemanticsNode.access$1300(io.flutter.view.AccessibilityBridge$SemanticsNode, io.flutter.util.Predicate):boolean type: STATIC) == true || (wrap: boolean : 0x0017: INVOKE  (r3v5 boolean) = 
              (wrap: io.flutter.view.AccessibilityBridge$SemanticsNode : 0x0013: IGET  (r3v4 io.flutter.view.AccessibilityBridge$SemanticsNode) = (r2v0 'this' io.flutter.view.AccessibilityBridge A[THIS]) io.flutter.view.AccessibilityBridge.accessibilityFocusedSemanticsNode io.flutter.view.AccessibilityBridge$SemanticsNode)
              (wrap: io.flutter.view.-$$Lambda$AccessibilityBridge$_xgAbWhyKyeHrMGUVUYzbSFEeBY : 0x0015: SGET  (r0v2 io.flutter.view.-$$Lambda$AccessibilityBridge$_xgAbWhyKyeHrMGUVUYzbSFEeBY) =  io.flutter.view.-$$Lambda$AccessibilityBridge$_xgAbWhyKyeHrMGUVUYzbSFEeBY.INSTANCE io.flutter.view.-$$Lambda$AccessibilityBridge$_xgAbWhyKyeHrMGUVUYzbSFEeBY)
             io.flutter.view.AccessibilityBridge.SemanticsNode.access$1300(io.flutter.view.AccessibilityBridge$SemanticsNode, io.flutter.util.Predicate):boolean type: STATIC) == false))) ? true : false in method: io.flutter.view.AccessibilityBridge.shouldSetCollectionInfo(io.flutter.view.AccessibilityBridge$SemanticsNode):boolean, dex: classes4.dex
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:256)
            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:123)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:107)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:314)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
            	... 29 more
            Caused by: jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x000d: INVOKE  (r3v2 boolean) = 
              (wrap: io.flutter.view.AccessibilityBridge$SemanticsNode : 0x0006: IGET  (r0v1 io.flutter.view.AccessibilityBridge$SemanticsNode) = (r2v0 'this' io.flutter.view.AccessibilityBridge A[THIS]) io.flutter.view.AccessibilityBridge.accessibilityFocusedSemanticsNode io.flutter.view.AccessibilityBridge$SemanticsNode)
              (wrap: io.flutter.view.-$$Lambda$AccessibilityBridge$JBDpWWlF54eN0gel8Jn1hY7yOmE : 0x000a: CONSTRUCTOR  (r1v0 io.flutter.view.-$$Lambda$AccessibilityBridge$JBDpWWlF54eN0gel8Jn1hY7yOmE) = 
              (r3v0 'semanticsNode' io.flutter.view.AccessibilityBridge$SemanticsNode)
             call: io.flutter.view.-$$Lambda$AccessibilityBridge$JBDpWWlF54eN0gel8Jn1hY7yOmE.<init>(io.flutter.view.AccessibilityBridge$SemanticsNode):void type: CONSTRUCTOR)
             io.flutter.view.AccessibilityBridge.SemanticsNode.access$1300(io.flutter.view.AccessibilityBridge$SemanticsNode, io.flutter.util.Predicate):boolean type: STATIC in method: io.flutter.view.AccessibilityBridge.shouldSetCollectionInfo(io.flutter.view.AccessibilityBridge$SemanticsNode):boolean, dex: classes4.dex
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:256)
            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:123)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:107)
            	at jadx.core.codegen.ConditionGen.wrap(ConditionGen.java:95)
            	at jadx.core.codegen.ConditionGen.addCompare(ConditionGen.java:117)
            	at jadx.core.codegen.ConditionGen.add(ConditionGen.java:57)
            	at jadx.core.codegen.ConditionGen.wrap(ConditionGen.java:84)
            	at jadx.core.codegen.ConditionGen.addAndOr(ConditionGen.java:151)
            	at jadx.core.codegen.ConditionGen.add(ConditionGen.java:70)
            	at jadx.core.codegen.ConditionGen.wrap(ConditionGen.java:84)
            	at jadx.core.codegen.ConditionGen.addAndOr(ConditionGen.java:151)
            	at jadx.core.codegen.ConditionGen.add(ConditionGen.java:70)
            	at jadx.core.codegen.ConditionGen.add(ConditionGen.java:46)
            	at jadx.core.codegen.InsnGen.makeTernary(InsnGen.java:948)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:476)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:231)
            	... 33 more
            Caused by: jadx.core.utils.exceptions.CodegenException: Error generate insn: ?: ONE_ARG  
              (wrap: boolean : 0x0000: INVOKE  (r0v1 boolean) = 
              (wrap: io.flutter.view.AccessibilityBridge$SemanticsNode : 0x0006: IGET  (r0v1 io.flutter.view.AccessibilityBridge$SemanticsNode) = (r2v0 'this' io.flutter.view.AccessibilityBridge A[THIS]) io.flutter.view.AccessibilityBridge.accessibilityFocusedSemanticsNode io.flutter.view.AccessibilityBridge$SemanticsNode)
              (wrap: io.flutter.view.-$$Lambda$AccessibilityBridge$JBDpWWlF54eN0gel8Jn1hY7yOmE : 0x000a: CONSTRUCTOR  (r1v0 io.flutter.view.-$$Lambda$AccessibilityBridge$JBDpWWlF54eN0gel8Jn1hY7yOmE) = 
              (r3v0 'semanticsNode' io.flutter.view.AccessibilityBridge$SemanticsNode)
             call: io.flutter.view.-$$Lambda$AccessibilityBridge$JBDpWWlF54eN0gel8Jn1hY7yOmE.<init>(io.flutter.view.AccessibilityBridge$SemanticsNode):void type: CONSTRUCTOR)
             io.flutter.view.AccessibilityBridge.SemanticsNode.nullableHasAncestor(io.flutter.view.AccessibilityBridge$SemanticsNode, io.flutter.util.Predicate):boolean type: STATIC)
             in method: io.flutter.view.AccessibilityBridge.shouldSetCollectionInfo(io.flutter.view.AccessibilityBridge$SemanticsNode):boolean, dex: classes4.dex
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:256)
            	at jadx.core.codegen.InsnGen.inlineMethod(InsnGen.java:924)
            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:684)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:368)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:231)
            	... 48 more
            Caused by: jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0000: INVOKE  (r0v1 boolean) = 
              (wrap: io.flutter.view.AccessibilityBridge$SemanticsNode : 0x0006: IGET  (r0v1 io.flutter.view.AccessibilityBridge$SemanticsNode) = (r2v0 'this' io.flutter.view.AccessibilityBridge A[THIS]) io.flutter.view.AccessibilityBridge.accessibilityFocusedSemanticsNode io.flutter.view.AccessibilityBridge$SemanticsNode)
              (wrap: io.flutter.view.-$$Lambda$AccessibilityBridge$JBDpWWlF54eN0gel8Jn1hY7yOmE : 0x000a: CONSTRUCTOR  (r1v0 io.flutter.view.-$$Lambda$AccessibilityBridge$JBDpWWlF54eN0gel8Jn1hY7yOmE) = 
              (r3v0 'semanticsNode' io.flutter.view.AccessibilityBridge$SemanticsNode)
             call: io.flutter.view.-$$Lambda$AccessibilityBridge$JBDpWWlF54eN0gel8Jn1hY7yOmE.<init>(io.flutter.view.AccessibilityBridge$SemanticsNode):void type: CONSTRUCTOR)
             io.flutter.view.AccessibilityBridge.SemanticsNode.nullableHasAncestor(io.flutter.view.AccessibilityBridge$SemanticsNode, io.flutter.util.Predicate):boolean type: STATIC in method: io.flutter.view.AccessibilityBridge.shouldSetCollectionInfo(io.flutter.view.AccessibilityBridge$SemanticsNode):boolean, dex: classes4.dex
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:256)
            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:123)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:107)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:98)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:480)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:231)
            	... 52 more
            Caused by: jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x000a: CONSTRUCTOR  (r1v0 io.flutter.view.-$$Lambda$AccessibilityBridge$JBDpWWlF54eN0gel8Jn1hY7yOmE) = 
              (r3v0 'semanticsNode' io.flutter.view.AccessibilityBridge$SemanticsNode)
             call: io.flutter.view.-$$Lambda$AccessibilityBridge$JBDpWWlF54eN0gel8Jn1hY7yOmE.<init>(io.flutter.view.AccessibilityBridge$SemanticsNode):void type: CONSTRUCTOR in method: io.flutter.view.AccessibilityBridge.shouldSetCollectionInfo(io.flutter.view.AccessibilityBridge$SemanticsNode):boolean, dex: classes4.dex
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:256)
            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:123)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:107)
            	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:787)
            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:728)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:368)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:231)
            	... 57 more
            Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: io.flutter.view.-$$Lambda$AccessibilityBridge$JBDpWWlF54eN0gel8Jn1hY7yOmE, state: NOT_LOADED
            	at jadx.core.dex.nodes.ClassNode.ensureProcessed(ClassNode.java:260)
            	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:606)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:364)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:231)
            	... 63 more
            */
        /*
            this = this;
            int r0 = r3.scrollChildren
            if (r0 <= 0) goto L_0x001f
            io.flutter.view.AccessibilityBridge$SemanticsNode r0 = r2.accessibilityFocusedSemanticsNode
            io.flutter.view.-$$Lambda$AccessibilityBridge$JBDpWWlF54eN0gel8Jn1hY7yOmE r1 = new io.flutter.view.-$$Lambda$AccessibilityBridge$JBDpWWlF54eN0gel8Jn1hY7yOmE
            r1.<init>(r3)
            boolean r3 = io.flutter.view.AccessibilityBridge.SemanticsNode.nullableHasAncestor(r0, r1)
            if (r3 != 0) goto L_0x001d
            io.flutter.view.AccessibilityBridge$SemanticsNode r3 = r2.accessibilityFocusedSemanticsNode
            io.flutter.view.-$$Lambda$AccessibilityBridge$_xgAbWhyKyeHrMGUVUYzbSFEeBY r0 = io.flutter.view.$$Lambda$AccessibilityBridge$_xgAbWhyKyeHrMGUVUYzbSFEeBY.INSTANCE
            boolean r3 = io.flutter.view.AccessibilityBridge.SemanticsNode.nullableHasAncestor(r3, r0)
            if (r3 != 0) goto L_0x001f
        L_0x001d:
            r3 = 1
            goto L_0x0020
        L_0x001f:
            r3 = 0
        L_0x0020:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: io.flutter.view.AccessibilityBridge.shouldSetCollectionInfo(io.flutter.view.AccessibilityBridge$SemanticsNode):boolean");
    }

    @SuppressLint({"NewApi"})
    public AccessibilityNodeInfo createAccessibilityNodeInfo(int i) {
        int i2;
        if (i >= 65536) {
            return this.accessibilityViewEmbedder.createAccessibilityNodeInfo(i);
        }
        boolean z = false;
        if (i == -1) {
            AccessibilityNodeInfo obtain = AccessibilityNodeInfo.obtain(this.rootAccessibilityView);
            this.rootAccessibilityView.onInitializeAccessibilityNodeInfo(obtain);
            if (this.flutterSemanticsTree.containsKey(0)) {
                obtain.addChild(this.rootAccessibilityView, 0);
            }
            return obtain;
        }
        SemanticsNode semanticsNode = this.flutterSemanticsTree.get(Integer.valueOf(i));
        if (semanticsNode == null) {
            return null;
        }
        if (semanticsNode.platformViewId != -1) {
            return this.accessibilityViewEmbedder.getRootNode(this.platformViewsAccessibilityDelegate.getPlatformViewById(Integer.valueOf(semanticsNode.platformViewId)), semanticsNode.id, semanticsNode.getGlobalRect());
        }
        AccessibilityNodeInfo obtain2 = AccessibilityNodeInfo.obtain(this.rootAccessibilityView, i);
        if (Build.VERSION.SDK_INT >= 18) {
            obtain2.setViewIdResourceName("");
        }
        obtain2.setPackageName(this.rootAccessibilityView.getContext().getPackageName());
        obtain2.setClassName("android.view.View");
        obtain2.setSource(this.rootAccessibilityView, i);
        obtain2.setFocusable(semanticsNode.isFocusable());
        if (this.inputFocusedSemanticsNode != null) {
            obtain2.setFocused(this.inputFocusedSemanticsNode.id == i);
        }
        if (this.accessibilityFocusedSemanticsNode != null) {
            obtain2.setAccessibilityFocused(this.accessibilityFocusedSemanticsNode.id == i);
        }
        if (semanticsNode.hasFlag(Flag.IS_TEXT_FIELD)) {
            obtain2.setPassword(semanticsNode.hasFlag(Flag.IS_OBSCURED));
            if (!semanticsNode.hasFlag(Flag.IS_READ_ONLY)) {
                obtain2.setClassName("android.widget.EditText");
            }
            if (Build.VERSION.SDK_INT >= 18) {
                obtain2.setEditable(!semanticsNode.hasFlag(Flag.IS_READ_ONLY));
                if (!(semanticsNode.textSelectionBase == -1 || semanticsNode.textSelectionExtent == -1)) {
                    obtain2.setTextSelection(semanticsNode.textSelectionBase, semanticsNode.textSelectionExtent);
                }
                if (Build.VERSION.SDK_INT > 18 && this.accessibilityFocusedSemanticsNode != null && this.accessibilityFocusedSemanticsNode.id == i) {
                    obtain2.setLiveRegion(1);
                }
            }
            if (semanticsNode.hasAction(Action.MOVE_CURSOR_FORWARD_BY_CHARACTER)) {
                obtain2.addAction(256);
                i2 = 1;
            } else {
                i2 = 0;
            }
            if (semanticsNode.hasAction(Action.MOVE_CURSOR_BACKWARD_BY_CHARACTER)) {
                obtain2.addAction(512);
                i2 |= 1;
            }
            if (semanticsNode.hasAction(Action.MOVE_CURSOR_FORWARD_BY_WORD)) {
                obtain2.addAction(256);
                i2 |= 2;
            }
            if (semanticsNode.hasAction(Action.MOVE_CURSOR_BACKWARD_BY_WORD)) {
                obtain2.addAction(512);
                i2 |= 2;
            }
            obtain2.setMovementGranularities(i2);
            if (Build.VERSION.SDK_INT >= 21 && semanticsNode.maxValueLength >= 0) {
                int length = semanticsNode.value == null ? 0 : semanticsNode.value.length();
                int unused = semanticsNode.currentValueLength;
                int unused2 = semanticsNode.maxValueLength;
                obtain2.setMaxTextLength((length - semanticsNode.currentValueLength) + semanticsNode.maxValueLength);
            }
        }
        if (Build.VERSION.SDK_INT > 18) {
            if (semanticsNode.hasAction(Action.SET_SELECTION)) {
                obtain2.addAction(131072);
            }
            if (semanticsNode.hasAction(Action.COPY)) {
                obtain2.addAction(16384);
            }
            if (semanticsNode.hasAction(Action.CUT)) {
                obtain2.addAction(65536);
            }
            if (semanticsNode.hasAction(Action.PASTE)) {
                obtain2.addAction(32768);
            }
        }
        if (semanticsNode.hasFlag(Flag.IS_BUTTON) || semanticsNode.hasFlag(Flag.IS_LINK)) {
            obtain2.setClassName("android.widget.Button");
        }
        if (semanticsNode.hasFlag(Flag.IS_IMAGE)) {
            obtain2.setClassName("android.widget.ImageView");
        }
        if (Build.VERSION.SDK_INT > 18 && semanticsNode.hasAction(Action.DISMISS)) {
            obtain2.setDismissable(true);
            obtain2.addAction(1048576);
        }
        if (semanticsNode.parent != null) {
            obtain2.setParent(this.rootAccessibilityView, semanticsNode.parent.id);
        } else {
            obtain2.setParent(this.rootAccessibilityView);
        }
        Rect access$1500 = semanticsNode.getGlobalRect();
        if (semanticsNode.parent != null) {
            Rect access$15002 = semanticsNode.parent.getGlobalRect();
            Rect rect = new Rect(access$1500);
            rect.offset(-access$15002.left, -access$15002.top);
            obtain2.setBoundsInParent(rect);
        } else {
            obtain2.setBoundsInParent(access$1500);
        }
        obtain2.setBoundsInScreen(access$1500);
        obtain2.setVisibleToUser(true);
        obtain2.setEnabled(!semanticsNode.hasFlag(Flag.HAS_ENABLED_STATE) || semanticsNode.hasFlag(Flag.IS_ENABLED));
        if (semanticsNode.hasAction(Action.TAP)) {
            if (Build.VERSION.SDK_INT < 21 || semanticsNode.onTapOverride == null) {
                obtain2.addAction(16);
                obtain2.setClickable(true);
            } else {
                obtain2.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, semanticsNode.onTapOverride.hint));
                obtain2.setClickable(true);
            }
        }
        if (semanticsNode.hasAction(Action.LONG_PRESS)) {
            if (Build.VERSION.SDK_INT < 21 || semanticsNode.onLongPressOverride == null) {
                obtain2.addAction(32);
                obtain2.setLongClickable(true);
            } else {
                obtain2.addAction(new AccessibilityNodeInfo.AccessibilityAction(32, semanticsNode.onLongPressOverride.hint));
                obtain2.setLongClickable(true);
            }
        }
        if (semanticsNode.hasAction(Action.SCROLL_LEFT) || semanticsNode.hasAction(Action.SCROLL_UP) || semanticsNode.hasAction(Action.SCROLL_RIGHT) || semanticsNode.hasAction(Action.SCROLL_DOWN)) {
            obtain2.setScrollable(true);
            if (semanticsNode.hasFlag(Flag.HAS_IMPLICIT_SCROLLING)) {
                if (semanticsNode.hasAction(Action.SCROLL_LEFT) || semanticsNode.hasAction(Action.SCROLL_RIGHT)) {
                    if (Build.VERSION.SDK_INT <= 19 || !shouldSetCollectionInfo(semanticsNode)) {
                        obtain2.setClassName("android.widget.HorizontalScrollView");
                    } else {
                        obtain2.setCollectionInfo(AccessibilityNodeInfo.CollectionInfo.obtain(0, semanticsNode.scrollChildren, false));
                    }
                } else if (Build.VERSION.SDK_INT <= 18 || !shouldSetCollectionInfo(semanticsNode)) {
                    obtain2.setClassName("android.widget.ScrollView");
                } else {
                    obtain2.setCollectionInfo(AccessibilityNodeInfo.CollectionInfo.obtain(semanticsNode.scrollChildren, 0, false));
                }
            }
            if (semanticsNode.hasAction(Action.SCROLL_LEFT) || semanticsNode.hasAction(Action.SCROLL_UP)) {
                obtain2.addAction(4096);
            }
            if (semanticsNode.hasAction(Action.SCROLL_RIGHT) || semanticsNode.hasAction(Action.SCROLL_DOWN)) {
                obtain2.addAction(8192);
            }
        }
        if (semanticsNode.hasAction(Action.INCREASE) || semanticsNode.hasAction(Action.DECREASE)) {
            obtain2.setClassName("android.widget.SeekBar");
            if (semanticsNode.hasAction(Action.INCREASE)) {
                obtain2.addAction(4096);
            }
            if (semanticsNode.hasAction(Action.DECREASE)) {
                obtain2.addAction(8192);
            }
        }
        if (semanticsNode.hasFlag(Flag.IS_LIVE_REGION) && Build.VERSION.SDK_INT > 18) {
            obtain2.setLiveRegion(1);
        }
        boolean access$1800 = semanticsNode.hasFlag(Flag.HAS_CHECKED_STATE);
        boolean access$18002 = semanticsNode.hasFlag(Flag.HAS_TOGGLED_STATE);
        if (access$1800 || access$18002) {
            z = true;
        }
        obtain2.setCheckable(z);
        if (access$1800) {
            obtain2.setChecked(semanticsNode.hasFlag(Flag.IS_CHECKED));
            obtain2.setContentDescription(semanticsNode.getValueLabelHint());
            if (semanticsNode.hasFlag(Flag.IS_IN_MUTUALLY_EXCLUSIVE_GROUP)) {
                obtain2.setClassName("android.widget.RadioButton");
            } else {
                obtain2.setClassName("android.widget.CheckBox");
            }
        } else if (access$18002) {
            obtain2.setChecked(semanticsNode.hasFlag(Flag.IS_TOGGLED));
            obtain2.setClassName("android.widget.Switch");
            obtain2.setContentDescription(semanticsNode.getValueLabelHint());
        } else if (!semanticsNode.hasFlag(Flag.SCOPES_ROUTE)) {
            obtain2.setText(semanticsNode.getValueLabelHint());
        }
        obtain2.setSelected(semanticsNode.hasFlag(Flag.IS_SELECTED));
        if (Build.VERSION.SDK_INT >= 28) {
            obtain2.setHeading(semanticsNode.hasFlag(Flag.IS_HEADER));
        }
        if (this.accessibilityFocusedSemanticsNode == null || this.accessibilityFocusedSemanticsNode.id != i) {
            obtain2.addAction(64);
        } else {
            obtain2.addAction(128);
        }
        if (Build.VERSION.SDK_INT >= 21 && semanticsNode.customAccessibilityActions != null) {
            for (CustomAccessibilityAction customAccessibilityAction : semanticsNode.customAccessibilityActions) {
                obtain2.addAction(new AccessibilityNodeInfo.AccessibilityAction(customAccessibilityAction.resourceId, customAccessibilityAction.label));
            }
        }
        for (SemanticsNode semanticsNode2 : semanticsNode.childrenInTraversalOrder) {
            if (!semanticsNode2.hasFlag(Flag.IS_HIDDEN)) {
                obtain2.addChild(this.rootAccessibilityView, semanticsNode2.id);
            }
        }
        return obtain2;
    }

    public boolean performAction(int i, int i2, @Nullable Bundle bundle) {
        if (i >= 65536) {
            boolean performAction = this.accessibilityViewEmbedder.performAction(i, i2, bundle);
            if (performAction && i2 == 128) {
                this.embeddedAccessibilityFocusedNodeId = null;
            }
            return performAction;
        }
        SemanticsNode semanticsNode = this.flutterSemanticsTree.get(Integer.valueOf(i));
        boolean z = false;
        if (semanticsNode == null) {
            return false;
        }
        switch (i2) {
            case 16:
                this.accessibilityChannel.dispatchSemanticsAction(i, Action.TAP);
                return true;
            case 32:
                this.accessibilityChannel.dispatchSemanticsAction(i, Action.LONG_PRESS);
                return true;
            case 64:
                this.accessibilityChannel.dispatchSemanticsAction(i, Action.DID_GAIN_ACCESSIBILITY_FOCUS);
                sendAccessibilityEvent(i, 32768);
                if (this.accessibilityFocusedSemanticsNode == null) {
                    this.rootAccessibilityView.invalidate();
                }
                this.accessibilityFocusedSemanticsNode = semanticsNode;
                if (semanticsNode.hasAction(Action.INCREASE) || semanticsNode.hasAction(Action.DECREASE)) {
                    sendAccessibilityEvent(i, 4);
                }
                return true;
            case 128:
                this.accessibilityChannel.dispatchSemanticsAction(i, Action.DID_LOSE_ACCESSIBILITY_FOCUS);
                sendAccessibilityEvent(i, 65536);
                this.accessibilityFocusedSemanticsNode = null;
                this.embeddedAccessibilityFocusedNodeId = null;
                return true;
            case 256:
                if (Build.VERSION.SDK_INT < 18) {
                    return false;
                }
                return performCursorMoveAction(semanticsNode, i, bundle, true);
            case 512:
                if (Build.VERSION.SDK_INT < 18) {
                    return false;
                }
                return performCursorMoveAction(semanticsNode, i, bundle, false);
            case 4096:
                if (semanticsNode.hasAction(Action.SCROLL_UP)) {
                    this.accessibilityChannel.dispatchSemanticsAction(i, Action.SCROLL_UP);
                } else if (semanticsNode.hasAction(Action.SCROLL_LEFT)) {
                    this.accessibilityChannel.dispatchSemanticsAction(i, Action.SCROLL_LEFT);
                } else if (!semanticsNode.hasAction(Action.INCREASE)) {
                    return false;
                } else {
                    String unused = semanticsNode.value = semanticsNode.increasedValue;
                    sendAccessibilityEvent(i, 4);
                    this.accessibilityChannel.dispatchSemanticsAction(i, Action.INCREASE);
                }
                return true;
            case 8192:
                if (semanticsNode.hasAction(Action.SCROLL_DOWN)) {
                    this.accessibilityChannel.dispatchSemanticsAction(i, Action.SCROLL_DOWN);
                } else if (semanticsNode.hasAction(Action.SCROLL_RIGHT)) {
                    this.accessibilityChannel.dispatchSemanticsAction(i, Action.SCROLL_RIGHT);
                } else if (!semanticsNode.hasAction(Action.DECREASE)) {
                    return false;
                } else {
                    String unused2 = semanticsNode.value = semanticsNode.decreasedValue;
                    sendAccessibilityEvent(i, 4);
                    this.accessibilityChannel.dispatchSemanticsAction(i, Action.DECREASE);
                }
                return true;
            case 16384:
                this.accessibilityChannel.dispatchSemanticsAction(i, Action.COPY);
                return true;
            case 32768:
                this.accessibilityChannel.dispatchSemanticsAction(i, Action.PASTE);
                return true;
            case 65536:
                this.accessibilityChannel.dispatchSemanticsAction(i, Action.CUT);
                return true;
            case 131072:
                if (Build.VERSION.SDK_INT < 18) {
                    return false;
                }
                HashMap hashMap = new HashMap();
                if (bundle != null && bundle.containsKey(AccessibilityNodeInfoCompat.ACTION_ARGUMENT_SELECTION_START_INT) && bundle.containsKey(AccessibilityNodeInfoCompat.ACTION_ARGUMENT_SELECTION_END_INT)) {
                    z = true;
                }
                if (z) {
                    hashMap.put("base", Integer.valueOf(bundle.getInt(AccessibilityNodeInfoCompat.ACTION_ARGUMENT_SELECTION_START_INT)));
                    hashMap.put("extent", Integer.valueOf(bundle.getInt(AccessibilityNodeInfoCompat.ACTION_ARGUMENT_SELECTION_END_INT)));
                } else {
                    hashMap.put("base", Integer.valueOf(semanticsNode.textSelectionExtent));
                    hashMap.put("extent", Integer.valueOf(semanticsNode.textSelectionExtent));
                }
                this.accessibilityChannel.dispatchSemanticsAction(i, Action.SET_SELECTION, hashMap);
                return true;
            case 1048576:
                this.accessibilityChannel.dispatchSemanticsAction(i, Action.DISMISS);
                return true;
            case ACTION_SHOW_ON_SCREEN /*16908342*/:
                this.accessibilityChannel.dispatchSemanticsAction(i, Action.SHOW_ON_SCREEN);
                return true;
            default:
                CustomAccessibilityAction customAccessibilityAction = this.customAccessibilityActions.get(Integer.valueOf(i2 - FIRST_RESOURCE_ID));
                if (customAccessibilityAction == null) {
                    return false;
                }
                this.accessibilityChannel.dispatchSemanticsAction(i, Action.CUSTOM_ACTION, Integer.valueOf(customAccessibilityAction.id));
                return true;
        }
    }

    @RequiresApi(18)
    @TargetApi(18)
    private boolean performCursorMoveAction(@NonNull SemanticsNode semanticsNode, int i, @NonNull Bundle bundle, boolean z) {
        int i2 = bundle.getInt(AccessibilityNodeInfoCompat.ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT);
        boolean z2 = bundle.getBoolean(AccessibilityNodeInfoCompat.ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN);
        switch (i2) {
            case 1:
                if (z && semanticsNode.hasAction(Action.MOVE_CURSOR_FORWARD_BY_CHARACTER)) {
                    this.accessibilityChannel.dispatchSemanticsAction(i, Action.MOVE_CURSOR_FORWARD_BY_CHARACTER, Boolean.valueOf(z2));
                    return true;
                } else if (z || !semanticsNode.hasAction(Action.MOVE_CURSOR_BACKWARD_BY_CHARACTER)) {
                    return false;
                } else {
                    this.accessibilityChannel.dispatchSemanticsAction(i, Action.MOVE_CURSOR_BACKWARD_BY_CHARACTER, Boolean.valueOf(z2));
                    return true;
                }
            case 2:
                if (z && semanticsNode.hasAction(Action.MOVE_CURSOR_FORWARD_BY_WORD)) {
                    this.accessibilityChannel.dispatchSemanticsAction(i, Action.MOVE_CURSOR_FORWARD_BY_WORD, Boolean.valueOf(z2));
                    return true;
                } else if (z || !semanticsNode.hasAction(Action.MOVE_CURSOR_BACKWARD_BY_WORD)) {
                    return false;
                } else {
                    this.accessibilityChannel.dispatchSemanticsAction(i, Action.MOVE_CURSOR_BACKWARD_BY_WORD, Boolean.valueOf(z2));
                    return true;
                }
            default:
                return false;
        }
    }

    public AccessibilityNodeInfo findFocus(int i) {
        switch (i) {
            case 1:
                if (this.inputFocusedSemanticsNode != null) {
                    return createAccessibilityNodeInfo(this.inputFocusedSemanticsNode.id);
                }
                if (this.embeddedInputFocusedNodeId != null) {
                    return createAccessibilityNodeInfo(this.embeddedInputFocusedNodeId.intValue());
                }
                break;
            case 2:
                break;
            default:
                return null;
        }
        if (this.accessibilityFocusedSemanticsNode != null) {
            return createAccessibilityNodeInfo(this.accessibilityFocusedSemanticsNode.id);
        }
        if (this.embeddedAccessibilityFocusedNodeId != null) {
            return createAccessibilityNodeInfo(this.embeddedAccessibilityFocusedNodeId.intValue());
        }
        return null;
    }

    private SemanticsNode getRootSemanticsNode() {
        return this.flutterSemanticsTree.get(0);
    }

    /* access modifiers changed from: private */
    public SemanticsNode getOrCreateSemanticsNode(int i) {
        SemanticsNode semanticsNode = this.flutterSemanticsTree.get(Integer.valueOf(i));
        if (semanticsNode != null) {
            return semanticsNode;
        }
        SemanticsNode semanticsNode2 = new SemanticsNode(this);
        int unused = semanticsNode2.id = i;
        this.flutterSemanticsTree.put(Integer.valueOf(i), semanticsNode2);
        return semanticsNode2;
    }

    /* access modifiers changed from: private */
    public CustomAccessibilityAction getOrCreateAccessibilityAction(int i) {
        CustomAccessibilityAction customAccessibilityAction = this.customAccessibilityActions.get(Integer.valueOf(i));
        if (customAccessibilityAction != null) {
            return customAccessibilityAction;
        }
        CustomAccessibilityAction customAccessibilityAction2 = new CustomAccessibilityAction();
        int unused = customAccessibilityAction2.id = i;
        int unused2 = customAccessibilityAction2.resourceId = FIRST_RESOURCE_ID + i;
        this.customAccessibilityActions.put(Integer.valueOf(i), customAccessibilityAction2);
        return customAccessibilityAction2;
    }

    public boolean onAccessibilityHoverEvent(MotionEvent motionEvent) {
        if (!this.accessibilityManager.isTouchExplorationEnabled() || this.flutterSemanticsTree.isEmpty()) {
            return false;
        }
        SemanticsNode access$3700 = getRootSemanticsNode().hitTest(new float[]{motionEvent.getX(), motionEvent.getY(), 0.0f, 1.0f});
        if (access$3700.platformViewId != -1) {
            return this.accessibilityViewEmbedder.onAccessibilityHoverEvent(access$3700.id, motionEvent);
        }
        if (motionEvent.getAction() == 9 || motionEvent.getAction() == 7) {
            handleTouchExploration(motionEvent.getX(), motionEvent.getY());
        } else if (motionEvent.getAction() == 10) {
            onTouchExplorationExit();
        } else {
            Log.d("flutter", "unexpected accessibility hover event: " + motionEvent);
            return false;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void onTouchExplorationExit() {
        if (this.hoveredObject != null) {
            sendAccessibilityEvent(this.hoveredObject.id, 256);
            this.hoveredObject = null;
        }
    }

    private void handleTouchExploration(float f, float f2) {
        SemanticsNode access$3700;
        if (!this.flutterSemanticsTree.isEmpty() && (access$3700 = getRootSemanticsNode().hitTest(new float[]{f, f2, 0.0f, 1.0f})) != this.hoveredObject) {
            if (access$3700 != null) {
                sendAccessibilityEvent(access$3700.id, 128);
            }
            if (this.hoveredObject != null) {
                sendAccessibilityEvent(this.hoveredObject.id, 256);
            }
            this.hoveredObject = access$3700;
        }
    }

    /* access modifiers changed from: package-private */
    public void updateCustomAccessibilityActions(@NonNull ByteBuffer byteBuffer, @NonNull String[] strArr) {
        String str;
        while (byteBuffer.hasRemaining()) {
            CustomAccessibilityAction orCreateAccessibilityAction = getOrCreateAccessibilityAction(byteBuffer.getInt());
            int unused = orCreateAccessibilityAction.overrideId = byteBuffer.getInt();
            int i = byteBuffer.getInt();
            String str2 = null;
            if (i == -1) {
                str = null;
            } else {
                str = strArr[i];
            }
            String unused2 = orCreateAccessibilityAction.label = str;
            int i2 = byteBuffer.getInt();
            if (i2 != -1) {
                str2 = strArr[i2];
            }
            String unused3 = orCreateAccessibilityAction.hint = str2;
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r11v18, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v23, resolved type: io.flutter.view.AccessibilityBridge$SemanticsNode} */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateSemantics(@androidx.annotation.NonNull java.nio.ByteBuffer r10, @androidx.annotation.NonNull java.lang.String[] r11) {
        /*
            r9 = this;
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
        L_0x0005:
            boolean r1 = r10.hasRemaining()
            if (r1 == 0) goto L_0x0033
            int r1 = r10.getInt()
            io.flutter.view.AccessibilityBridge$SemanticsNode r1 = r9.getOrCreateSemanticsNode(r1)
            r1.updateWith(r10, r11)
            io.flutter.view.AccessibilityBridge$Flag r2 = io.flutter.view.AccessibilityBridge.Flag.IS_HIDDEN
            boolean r2 = r1.hasFlag(r2)
            if (r2 == 0) goto L_0x001f
            goto L_0x0005
        L_0x001f:
            io.flutter.view.AccessibilityBridge$Flag r2 = io.flutter.view.AccessibilityBridge.Flag.IS_FOCUSED
            boolean r2 = r1.hasFlag(r2)
            if (r2 == 0) goto L_0x0029
            r9.inputFocusedSemanticsNode = r1
        L_0x0029:
            boolean r2 = r1.hadPreviousConfig
            if (r2 == 0) goto L_0x0005
            r0.add(r1)
            goto L_0x0005
        L_0x0033:
            java.util.HashSet r10 = new java.util.HashSet
            r10.<init>()
            io.flutter.view.AccessibilityBridge$SemanticsNode r11 = r9.getRootSemanticsNode()
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            r2 = 1
            r3 = 0
            if (r11 == 0) goto L_0x008b
            r4 = 16
            float[] r4 = new float[r4]
            android.opengl.Matrix.setIdentityM(r4, r3)
            int r5 = android.os.Build.VERSION.SDK_INT
            r6 = 23
            if (r5 < r6) goto L_0x0085
            android.view.View r5 = r9.rootAccessibilityView
            android.view.WindowInsets r5 = r5.getRootWindowInsets()
            if (r5 == 0) goto L_0x0085
            java.lang.Integer r6 = r9.lastLeftFrameInset
            int r7 = r5.getSystemWindowInsetLeft()
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)
            boolean r6 = r6.equals(r7)
            if (r6 != 0) goto L_0x0070
            boolean unused = r11.globalGeometryDirty = r2
            boolean unused = r11.inverseTransformDirty = r2
        L_0x0070:
            int r5 = r5.getSystemWindowInsetLeft()
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)
            r9.lastLeftFrameInset = r5
            java.lang.Integer r5 = r9.lastLeftFrameInset
            int r5 = r5.intValue()
            float r5 = (float) r5
            r6 = 0
            android.opengl.Matrix.translateM(r4, r3, r5, r6, r6)
        L_0x0085:
            r11.updateRecursively(r4, r10, r3)
            r11.collectRoutes(r1)
        L_0x008b:
            java.util.Iterator r11 = r1.iterator()
            r4 = 0
            r5 = r4
        L_0x0091:
            boolean r6 = r11.hasNext()
            if (r6 == 0) goto L_0x00af
            java.lang.Object r6 = r11.next()
            io.flutter.view.AccessibilityBridge$SemanticsNode r6 = (io.flutter.view.AccessibilityBridge.SemanticsNode) r6
            java.util.List<java.lang.Integer> r7 = r9.flutterNavigationStack
            int r8 = r6.id
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)
            boolean r7 = r7.contains(r8)
            if (r7 != 0) goto L_0x0091
            r5 = r6
            goto L_0x0091
        L_0x00af:
            if (r5 != 0) goto L_0x00c3
            int r11 = r1.size()
            if (r11 <= 0) goto L_0x00c3
            int r11 = r1.size()
            int r11 = r11 - r2
            java.lang.Object r11 = r1.get(r11)
            r5 = r11
            io.flutter.view.AccessibilityBridge$SemanticsNode r5 = (io.flutter.view.AccessibilityBridge.SemanticsNode) r5
        L_0x00c3:
            if (r5 == 0) goto L_0x00d6
            int r11 = r5.id
            int r6 = r9.previousRouteId
            if (r11 == r6) goto L_0x00d6
            int r11 = r5.id
            r9.previousRouteId = r11
            r9.sendWindowChangeEvent(r5)
        L_0x00d6:
            java.util.List<java.lang.Integer> r11 = r9.flutterNavigationStack
            r11.clear()
            java.util.Iterator r11 = r1.iterator()
        L_0x00df:
            boolean r1 = r11.hasNext()
            if (r1 == 0) goto L_0x00f9
            java.lang.Object r1 = r11.next()
            io.flutter.view.AccessibilityBridge$SemanticsNode r1 = (io.flutter.view.AccessibilityBridge.SemanticsNode) r1
            java.util.List<java.lang.Integer> r5 = r9.flutterNavigationStack
            int r1 = r1.id
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)
            r5.add(r1)
            goto L_0x00df
        L_0x00f9:
            java.util.Map<java.lang.Integer, io.flutter.view.AccessibilityBridge$SemanticsNode> r11 = r9.flutterSemanticsTree
            java.util.Set r11 = r11.entrySet()
            java.util.Iterator r11 = r11.iterator()
        L_0x0103:
            boolean r1 = r11.hasNext()
            if (r1 == 0) goto L_0x0122
            java.lang.Object r1 = r11.next()
            java.util.Map$Entry r1 = (java.util.Map.Entry) r1
            java.lang.Object r1 = r1.getValue()
            io.flutter.view.AccessibilityBridge$SemanticsNode r1 = (io.flutter.view.AccessibilityBridge.SemanticsNode) r1
            boolean r5 = r10.contains(r1)
            if (r5 != 0) goto L_0x0103
            r9.willRemoveSemanticsNode(r1)
            r11.remove()
            goto L_0x0103
        L_0x0122:
            r9.sendWindowContentChangeEvent(r3)
            java.util.Iterator r10 = r0.iterator()
        L_0x0129:
            boolean r11 = r10.hasNext()
            if (r11 == 0) goto L_0x0318
            java.lang.Object r11 = r10.next()
            io.flutter.view.AccessibilityBridge$SemanticsNode r11 = (io.flutter.view.AccessibilityBridge.SemanticsNode) r11
            boolean r0 = r11.didScroll()
            if (r0 == 0) goto L_0x01fa
            int r0 = r11.id
            r1 = 4096(0x1000, float:5.74E-42)
            android.view.accessibility.AccessibilityEvent r0 = r9.obtainAccessibilityEvent(r0, r1)
            float r1 = r11.scrollPosition
            float r5 = r11.scrollExtentMax
            float r6 = r11.scrollExtentMax
            boolean r6 = java.lang.Float.isInfinite(r6)
            r7 = 1200142336(0x4788b800, float:70000.0)
            r8 = 1203982336(0x47c35000, float:100000.0)
            if (r6 == 0) goto L_0x0167
            int r5 = (r1 > r7 ? 1 : (r1 == r7 ? 0 : -1))
            if (r5 <= 0) goto L_0x0164
            r1 = 1200142336(0x4788b800, float:70000.0)
        L_0x0164:
            r5 = 1203982336(0x47c35000, float:100000.0)
        L_0x0167:
            float r6 = r11.scrollExtentMin
            boolean r6 = java.lang.Float.isInfinite(r6)
            if (r6 == 0) goto L_0x017e
            float r5 = r5 + r8
            r6 = -947341312(0xffffffffc788b800, float:-70000.0)
            int r7 = (r1 > r6 ? 1 : (r1 == r6 ? 0 : -1))
            if (r7 >= 0) goto L_0x017c
            r1 = -947341312(0xffffffffc788b800, float:-70000.0)
        L_0x017c:
            float r1 = r1 + r8
            goto L_0x0188
        L_0x017e:
            float r6 = r11.scrollExtentMin
            float r5 = r5 - r6
            float r6 = r11.scrollExtentMin
            float r1 = r1 - r6
        L_0x0188:
            io.flutter.view.AccessibilityBridge$Action r6 = io.flutter.view.AccessibilityBridge.Action.SCROLL_UP
            boolean r6 = r11.hadAction(r6)
            if (r6 != 0) goto L_0x01b2
            io.flutter.view.AccessibilityBridge$Action r6 = io.flutter.view.AccessibilityBridge.Action.SCROLL_DOWN
            boolean r6 = r11.hadAction(r6)
            if (r6 == 0) goto L_0x0199
            goto L_0x01b2
        L_0x0199:
            io.flutter.view.AccessibilityBridge$Action r6 = io.flutter.view.AccessibilityBridge.Action.SCROLL_LEFT
            boolean r6 = r11.hadAction(r6)
            if (r6 != 0) goto L_0x01a9
            io.flutter.view.AccessibilityBridge$Action r6 = io.flutter.view.AccessibilityBridge.Action.SCROLL_RIGHT
            boolean r6 = r11.hadAction(r6)
            if (r6 == 0) goto L_0x01ba
        L_0x01a9:
            int r1 = (int) r1
            r0.setScrollX(r1)
            int r1 = (int) r5
            r0.setMaxScrollX(r1)
            goto L_0x01ba
        L_0x01b2:
            int r1 = (int) r1
            r0.setScrollY(r1)
            int r1 = (int) r5
            r0.setMaxScrollY(r1)
        L_0x01ba:
            int r1 = r11.scrollChildren
            if (r1 <= 0) goto L_0x01f7
            int r1 = r11.scrollChildren
            r0.setItemCount(r1)
            int r1 = r11.scrollIndex
            r0.setFromIndex(r1)
            java.util.List r1 = r11.childrenInHitTestOrder
            java.util.Iterator r1 = r1.iterator()
            r5 = 0
        L_0x01d7:
            boolean r6 = r1.hasNext()
            if (r6 == 0) goto L_0x01ee
            java.lang.Object r6 = r1.next()
            io.flutter.view.AccessibilityBridge$SemanticsNode r6 = (io.flutter.view.AccessibilityBridge.SemanticsNode) r6
            io.flutter.view.AccessibilityBridge$Flag r7 = io.flutter.view.AccessibilityBridge.Flag.IS_HIDDEN
            boolean r6 = r6.hasFlag(r7)
            if (r6 != 0) goto L_0x01d7
            int r5 = r5 + 1
            goto L_0x01d7
        L_0x01ee:
            int r1 = r11.scrollIndex
            int r1 = r1 + r5
            int r1 = r1 - r2
            r0.setToIndex(r1)
        L_0x01f7:
            r9.sendAccessibilityEvent(r0)
        L_0x01fa:
            io.flutter.view.AccessibilityBridge$Flag r0 = io.flutter.view.AccessibilityBridge.Flag.IS_LIVE_REGION
            boolean r0 = r11.hasFlag(r0)
            if (r0 == 0) goto L_0x020f
            boolean r0 = r11.didChangeLabel()
            if (r0 == 0) goto L_0x020f
            int r0 = r11.id
            r9.sendWindowContentChangeEvent(r0)
        L_0x020f:
            io.flutter.view.AccessibilityBridge$SemanticsNode r0 = r9.accessibilityFocusedSemanticsNode
            if (r0 == 0) goto L_0x0246
            io.flutter.view.AccessibilityBridge$SemanticsNode r0 = r9.accessibilityFocusedSemanticsNode
            int r0 = r0.id
            int r1 = r11.id
            if (r0 != r1) goto L_0x0246
            io.flutter.view.AccessibilityBridge$Flag r0 = io.flutter.view.AccessibilityBridge.Flag.IS_SELECTED
            boolean r0 = r11.hadFlag(r0)
            if (r0 != 0) goto L_0x0246
            io.flutter.view.AccessibilityBridge$Flag r0 = io.flutter.view.AccessibilityBridge.Flag.IS_SELECTED
            boolean r0 = r11.hasFlag(r0)
            if (r0 == 0) goto L_0x0246
            int r0 = r11.id
            r1 = 4
            android.view.accessibility.AccessibilityEvent r0 = r9.obtainAccessibilityEvent(r0, r1)
            java.util.List r1 = r0.getText()
            java.lang.String r5 = r11.label
            r1.add(r5)
            r9.sendAccessibilityEvent(r0)
        L_0x0246:
            io.flutter.view.AccessibilityBridge$SemanticsNode r0 = r9.inputFocusedSemanticsNode
            if (r0 == 0) goto L_0x027a
            io.flutter.view.AccessibilityBridge$SemanticsNode r0 = r9.inputFocusedSemanticsNode
            int r0 = r0.id
            int r1 = r11.id
            if (r0 != r1) goto L_0x027a
            io.flutter.view.AccessibilityBridge$SemanticsNode r0 = r9.lastInputFocusedSemanticsNode
            if (r0 == 0) goto L_0x0268
            io.flutter.view.AccessibilityBridge$SemanticsNode r0 = r9.lastInputFocusedSemanticsNode
            int r0 = r0.id
            io.flutter.view.AccessibilityBridge$SemanticsNode r1 = r9.inputFocusedSemanticsNode
            int r1 = r1.id
            if (r0 == r1) goto L_0x027a
        L_0x0268:
            io.flutter.view.AccessibilityBridge$SemanticsNode r0 = r9.inputFocusedSemanticsNode
            r9.lastInputFocusedSemanticsNode = r0
            int r0 = r11.id
            r1 = 8
            android.view.accessibility.AccessibilityEvent r0 = r9.obtainAccessibilityEvent(r0, r1)
            r9.sendAccessibilityEvent(r0)
            goto L_0x0280
        L_0x027a:
            io.flutter.view.AccessibilityBridge$SemanticsNode r0 = r9.inputFocusedSemanticsNode
            if (r0 != 0) goto L_0x0280
            r9.lastInputFocusedSemanticsNode = r4
        L_0x0280:
            io.flutter.view.AccessibilityBridge$SemanticsNode r0 = r9.inputFocusedSemanticsNode
            if (r0 == 0) goto L_0x0129
            io.flutter.view.AccessibilityBridge$SemanticsNode r0 = r9.inputFocusedSemanticsNode
            int r0 = r0.id
            int r1 = r11.id
            if (r0 != r1) goto L_0x0129
            io.flutter.view.AccessibilityBridge$Flag r0 = io.flutter.view.AccessibilityBridge.Flag.IS_TEXT_FIELD
            boolean r0 = r11.hadFlag(r0)
            if (r0 == 0) goto L_0x0129
            io.flutter.view.AccessibilityBridge$Flag r0 = io.flutter.view.AccessibilityBridge.Flag.IS_TEXT_FIELD
            boolean r0 = r11.hasFlag(r0)
            if (r0 == 0) goto L_0x0129
            io.flutter.view.AccessibilityBridge$SemanticsNode r0 = r9.accessibilityFocusedSemanticsNode
            if (r0 == 0) goto L_0x02b2
            io.flutter.view.AccessibilityBridge$SemanticsNode r0 = r9.accessibilityFocusedSemanticsNode
            int r0 = r0.id
            io.flutter.view.AccessibilityBridge$SemanticsNode r1 = r9.inputFocusedSemanticsNode
            int r1 = r1.id
            if (r0 != r1) goto L_0x0129
        L_0x02b2:
            java.lang.String r0 = r11.previousValue
            if (r0 == 0) goto L_0x02bd
            java.lang.String r0 = r11.previousValue
            goto L_0x02bf
        L_0x02bd:
            java.lang.String r0 = ""
        L_0x02bf:
            java.lang.String r1 = r11.value
            if (r1 == 0) goto L_0x02ca
            java.lang.String r1 = r11.value
            goto L_0x02cc
        L_0x02ca:
            java.lang.String r1 = ""
        L_0x02cc:
            int r5 = r11.id
            android.view.accessibility.AccessibilityEvent r0 = r9.createTextChangedEvent(r5, r0, r1)
            if (r0 == 0) goto L_0x02d9
            r9.sendAccessibilityEvent(r0)
        L_0x02d9:
            int r0 = r11.previousTextSelectionBase
            int r5 = r11.textSelectionBase
            if (r0 != r5) goto L_0x02ed
            int r0 = r11.previousTextSelectionExtent
            int r5 = r11.textSelectionExtent
            if (r0 == r5) goto L_0x0129
        L_0x02ed:
            int r0 = r11.id
            r5 = 8192(0x2000, float:1.14794E-41)
            android.view.accessibility.AccessibilityEvent r0 = r9.obtainAccessibilityEvent(r0, r5)
            java.util.List r5 = r0.getText()
            r5.add(r1)
            int r5 = r11.textSelectionBase
            r0.setFromIndex(r5)
            int r11 = r11.textSelectionExtent
            r0.setToIndex(r11)
            int r11 = r1.length()
            r0.setItemCount(r11)
            r9.sendAccessibilityEvent(r0)
            goto L_0x0129
        L_0x0318:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.flutter.view.AccessibilityBridge.updateSemantics(java.nio.ByteBuffer, java.lang.String[]):void");
    }

    private AccessibilityEvent createTextChangedEvent(int i, String str, String str2) {
        AccessibilityEvent obtainAccessibilityEvent = obtainAccessibilityEvent(i, 16);
        obtainAccessibilityEvent.setBeforeText(str);
        obtainAccessibilityEvent.getText().add(str2);
        int i2 = 0;
        while (i2 < str.length() && i2 < str2.length() && str.charAt(i2) == str2.charAt(i2)) {
            i2++;
        }
        if (i2 >= str.length() && i2 >= str2.length()) {
            return null;
        }
        obtainAccessibilityEvent.setFromIndex(i2);
        int length = str.length() - 1;
        int length2 = str2.length() - 1;
        while (length >= i2 && length2 >= i2 && str.charAt(length) == str2.charAt(length2)) {
            length--;
            length2--;
        }
        obtainAccessibilityEvent.setRemovedCount((length - i2) + 1);
        obtainAccessibilityEvent.setAddedCount((length2 - i2) + 1);
        return obtainAccessibilityEvent;
    }

    /* access modifiers changed from: private */
    public void sendAccessibilityEvent(int i, int i2) {
        if (this.accessibilityManager.isEnabled()) {
            sendAccessibilityEvent(obtainAccessibilityEvent(i, i2));
        }
    }

    /* access modifiers changed from: private */
    public void sendAccessibilityEvent(@NonNull AccessibilityEvent accessibilityEvent) {
        if (this.accessibilityManager.isEnabled()) {
            this.rootAccessibilityView.getParent().requestSendAccessibilityEvent(this.rootAccessibilityView, accessibilityEvent);
        }
    }

    private void sendWindowChangeEvent(@NonNull SemanticsNode semanticsNode) {
        AccessibilityEvent obtainAccessibilityEvent = obtainAccessibilityEvent(semanticsNode.id, 32);
        obtainAccessibilityEvent.getText().add(semanticsNode.getRouteName());
        sendAccessibilityEvent(obtainAccessibilityEvent);
    }

    private void sendWindowContentChangeEvent(int i) {
        AccessibilityEvent obtainAccessibilityEvent = obtainAccessibilityEvent(i, 2048);
        if (Build.VERSION.SDK_INT >= 19) {
            obtainAccessibilityEvent.setContentChangeTypes(1);
        }
        sendAccessibilityEvent(obtainAccessibilityEvent);
    }

    /* access modifiers changed from: private */
    public AccessibilityEvent obtainAccessibilityEvent(int i, int i2) {
        AccessibilityEvent obtain = AccessibilityEvent.obtain(i2);
        obtain.setPackageName(this.rootAccessibilityView.getContext().getPackageName());
        obtain.setSource(this.rootAccessibilityView, i);
        return obtain;
    }

    private void willRemoveSemanticsNode(SemanticsNode semanticsNode) {
        SemanticsNode unused = semanticsNode.parent = null;
        if (this.accessibilityFocusedSemanticsNode == semanticsNode) {
            sendAccessibilityEvent(this.accessibilityFocusedSemanticsNode.id, 65536);
            this.accessibilityFocusedSemanticsNode = null;
        }
        if (this.inputFocusedSemanticsNode == semanticsNode) {
            this.inputFocusedSemanticsNode = null;
        }
        if (this.hoveredObject == semanticsNode) {
            this.hoveredObject = null;
        }
    }

    public void reset() {
        this.flutterSemanticsTree.clear();
        if (this.accessibilityFocusedSemanticsNode != null) {
            sendAccessibilityEvent(this.accessibilityFocusedSemanticsNode.id, 65536);
        }
        this.accessibilityFocusedSemanticsNode = null;
        this.hoveredObject = null;
        sendWindowContentChangeEvent(0);
    }

    public enum Action {
        TAP(1),
        LONG_PRESS(2),
        SCROLL_LEFT(4),
        SCROLL_RIGHT(8),
        SCROLL_UP(16),
        SCROLL_DOWN(32),
        INCREASE(64),
        DECREASE(128),
        SHOW_ON_SCREEN(256),
        MOVE_CURSOR_FORWARD_BY_CHARACTER(512),
        MOVE_CURSOR_BACKWARD_BY_CHARACTER(1024),
        SET_SELECTION(2048),
        COPY(4096),
        CUT(8192),
        PASTE(16384),
        DID_GAIN_ACCESSIBILITY_FOCUS(32768),
        DID_LOSE_ACCESSIBILITY_FOCUS(65536),
        CUSTOM_ACTION(131072),
        DISMISS(262144),
        MOVE_CURSOR_FORWARD_BY_WORD(524288),
        MOVE_CURSOR_BACKWARD_BY_WORD(1048576);
        
        public final int value;

        private Action(int i) {
            this.value = i;
        }
    }

    private enum Flag {
        HAS_CHECKED_STATE(1),
        IS_CHECKED(2),
        IS_SELECTED(4),
        IS_BUTTON(8),
        IS_TEXT_FIELD(16),
        IS_FOCUSED(32),
        HAS_ENABLED_STATE(64),
        IS_ENABLED(128),
        IS_IN_MUTUALLY_EXCLUSIVE_GROUP(256),
        IS_HEADER(512),
        IS_OBSCURED(1024),
        SCOPES_ROUTE(2048),
        NAMES_ROUTE(4096),
        IS_HIDDEN(8192),
        IS_IMAGE(16384),
        IS_LIVE_REGION(32768),
        HAS_TOGGLED_STATE(65536),
        IS_TOGGLED(131072),
        HAS_IMPLICIT_SCROLLING(262144),
        IS_READ_ONLY(1048576),
        IS_FOCUSABLE(2097152),
        IS_LINK(4194304);
        
        final int value;

        private Flag(int i) {
            this.value = i;
        }
    }

    private enum AccessibilityFeature {
        ACCESSIBLE_NAVIGATION(1),
        INVERT_COLORS(2),
        DISABLE_ANIMATIONS(4);
        
        final int value;

        private AccessibilityFeature(int i) {
            this.value = i;
        }
    }

    private enum TextDirection {
        UNKNOWN,
        LTR,
        RTL;

        public static TextDirection fromInt(int i) {
            switch (i) {
                case 1:
                    return RTL;
                case 2:
                    return LTR;
                default:
                    return UNKNOWN;
            }
        }
    }

    private static class CustomAccessibilityAction {
        /* access modifiers changed from: private */
        public String hint;
        /* access modifiers changed from: private */
        public int id = -1;
        /* access modifiers changed from: private */
        public String label;
        /* access modifiers changed from: private */
        public int overrideId = -1;
        /* access modifiers changed from: private */
        public int resourceId = -1;

        CustomAccessibilityAction() {
        }
    }

    private static class SemanticsNode {
        final AccessibilityBridge accessibilityBridge;
        private int actions;
        private float bottom;
        /* access modifiers changed from: private */
        public List<SemanticsNode> childrenInHitTestOrder = new ArrayList();
        /* access modifiers changed from: private */
        public List<SemanticsNode> childrenInTraversalOrder = new ArrayList();
        /* access modifiers changed from: private */
        public int currentValueLength;
        /* access modifiers changed from: private */
        public List<CustomAccessibilityAction> customAccessibilityActions;
        /* access modifiers changed from: private */
        public String decreasedValue;
        private int flags;
        /* access modifiers changed from: private */
        public boolean globalGeometryDirty = true;
        private Rect globalRect;
        private float[] globalTransform;
        /* access modifiers changed from: private */
        public boolean hadPreviousConfig = false;
        private String hint;
        /* access modifiers changed from: private */
        public int id = -1;
        /* access modifiers changed from: private */
        public String increasedValue;
        private float[] inverseTransform;
        /* access modifiers changed from: private */
        public boolean inverseTransformDirty = true;
        /* access modifiers changed from: private */
        public String label;
        private float left;
        /* access modifiers changed from: private */
        public int maxValueLength;
        /* access modifiers changed from: private */
        public CustomAccessibilityAction onLongPressOverride;
        /* access modifiers changed from: private */
        public CustomAccessibilityAction onTapOverride;
        /* access modifiers changed from: private */
        public SemanticsNode parent;
        /* access modifiers changed from: private */
        public int platformViewId;
        private int previousActions;
        private int previousFlags;
        private String previousLabel;
        private float previousScrollExtentMax;
        private float previousScrollExtentMin;
        private float previousScrollPosition;
        /* access modifiers changed from: private */
        public int previousTextSelectionBase;
        /* access modifiers changed from: private */
        public int previousTextSelectionExtent;
        /* access modifiers changed from: private */
        public String previousValue;
        private float right;
        /* access modifiers changed from: private */
        public int scrollChildren;
        /* access modifiers changed from: private */
        public float scrollExtentMax;
        /* access modifiers changed from: private */
        public float scrollExtentMin;
        /* access modifiers changed from: private */
        public int scrollIndex;
        /* access modifiers changed from: private */
        public float scrollPosition;
        private TextDirection textDirection;
        /* access modifiers changed from: private */
        public int textSelectionBase;
        /* access modifiers changed from: private */
        public int textSelectionExtent;
        private float top;
        private float[] transform;
        /* access modifiers changed from: private */
        public String value;

        private void log(@NonNull String str, boolean z) {
        }

        /* access modifiers changed from: private */
        public static boolean nullableHasAncestor(SemanticsNode semanticsNode, Predicate<SemanticsNode> predicate) {
            return (semanticsNode == null || semanticsNode.getAncestor(predicate) == null) ? false : true;
        }

        SemanticsNode(@NonNull AccessibilityBridge accessibilityBridge2) {
            this.accessibilityBridge = accessibilityBridge2;
        }

        private SemanticsNode getAncestor(Predicate<SemanticsNode> predicate) {
            for (SemanticsNode semanticsNode = this.parent; semanticsNode != null; semanticsNode = semanticsNode.parent) {
                if (predicate.test(semanticsNode)) {
                    return semanticsNode;
                }
            }
            return null;
        }

        /* access modifiers changed from: private */
        public boolean hasAction(@NonNull Action action) {
            return (action.value & this.actions) != 0;
        }

        /* access modifiers changed from: private */
        public boolean hadAction(@NonNull Action action) {
            return (action.value & this.previousActions) != 0;
        }

        /* access modifiers changed from: private */
        public boolean hasFlag(@NonNull Flag flag) {
            return (flag.value & this.flags) != 0;
        }

        /* access modifiers changed from: private */
        public boolean hadFlag(@NonNull Flag flag) {
            return (flag.value & this.previousFlags) != 0;
        }

        /* access modifiers changed from: private */
        public boolean didScroll() {
            return !Float.isNaN(this.scrollPosition) && !Float.isNaN(this.previousScrollPosition) && this.previousScrollPosition != this.scrollPosition;
        }

        /* access modifiers changed from: private */
        public boolean didChangeLabel() {
            if (this.label == null && this.previousLabel == null) {
                return false;
            }
            if (this.label == null || this.previousLabel == null || !this.label.equals(this.previousLabel)) {
                return true;
            }
            return false;
        }

        /* access modifiers changed from: private */
        public void updateWith(@NonNull ByteBuffer byteBuffer, @NonNull String[] strArr) {
            String str;
            String str2;
            String str3;
            String str4;
            String str5;
            this.hadPreviousConfig = true;
            this.previousValue = this.value;
            this.previousLabel = this.label;
            this.previousFlags = this.flags;
            this.previousActions = this.actions;
            this.previousTextSelectionBase = this.textSelectionBase;
            this.previousTextSelectionExtent = this.textSelectionExtent;
            this.previousScrollPosition = this.scrollPosition;
            this.previousScrollExtentMax = this.scrollExtentMax;
            this.previousScrollExtentMin = this.scrollExtentMin;
            this.flags = byteBuffer.getInt();
            this.actions = byteBuffer.getInt();
            this.maxValueLength = byteBuffer.getInt();
            this.currentValueLength = byteBuffer.getInt();
            this.textSelectionBase = byteBuffer.getInt();
            this.textSelectionExtent = byteBuffer.getInt();
            this.platformViewId = byteBuffer.getInt();
            this.scrollChildren = byteBuffer.getInt();
            this.scrollIndex = byteBuffer.getInt();
            this.scrollPosition = byteBuffer.getFloat();
            this.scrollExtentMax = byteBuffer.getFloat();
            this.scrollExtentMin = byteBuffer.getFloat();
            int i = byteBuffer.getInt();
            if (i == -1) {
                str = null;
            } else {
                str = strArr[i];
            }
            this.label = str;
            int i2 = byteBuffer.getInt();
            if (i2 == -1) {
                str2 = null;
            } else {
                str2 = strArr[i2];
            }
            this.value = str2;
            int i3 = byteBuffer.getInt();
            if (i3 == -1) {
                str3 = null;
            } else {
                str3 = strArr[i3];
            }
            this.increasedValue = str3;
            int i4 = byteBuffer.getInt();
            if (i4 == -1) {
                str4 = null;
            } else {
                str4 = strArr[i4];
            }
            this.decreasedValue = str4;
            int i5 = byteBuffer.getInt();
            if (i5 == -1) {
                str5 = null;
            } else {
                str5 = strArr[i5];
            }
            this.hint = str5;
            this.textDirection = TextDirection.fromInt(byteBuffer.getInt());
            this.left = byteBuffer.getFloat();
            this.top = byteBuffer.getFloat();
            this.right = byteBuffer.getFloat();
            this.bottom = byteBuffer.getFloat();
            if (this.transform == null) {
                this.transform = new float[16];
            }
            for (int i6 = 0; i6 < 16; i6++) {
                this.transform[i6] = byteBuffer.getFloat();
            }
            this.inverseTransformDirty = true;
            this.globalGeometryDirty = true;
            int i7 = byteBuffer.getInt();
            this.childrenInTraversalOrder.clear();
            this.childrenInHitTestOrder.clear();
            for (int i8 = 0; i8 < i7; i8++) {
                SemanticsNode access$5900 = this.accessibilityBridge.getOrCreateSemanticsNode(byteBuffer.getInt());
                access$5900.parent = this;
                this.childrenInTraversalOrder.add(access$5900);
            }
            for (int i9 = 0; i9 < i7; i9++) {
                SemanticsNode access$59002 = this.accessibilityBridge.getOrCreateSemanticsNode(byteBuffer.getInt());
                access$59002.parent = this;
                this.childrenInHitTestOrder.add(access$59002);
            }
            int i10 = byteBuffer.getInt();
            if (i10 == 0) {
                this.customAccessibilityActions = null;
                return;
            }
            if (this.customAccessibilityActions == null) {
                this.customAccessibilityActions = new ArrayList(i10);
            } else {
                this.customAccessibilityActions.clear();
            }
            for (int i11 = 0; i11 < i10; i11++) {
                CustomAccessibilityAction access$6000 = this.accessibilityBridge.getOrCreateAccessibilityAction(byteBuffer.getInt());
                if (access$6000.overrideId == Action.TAP.value) {
                    this.onTapOverride = access$6000;
                } else if (access$6000.overrideId == Action.LONG_PRESS.value) {
                    this.onLongPressOverride = access$6000;
                } else {
                    this.customAccessibilityActions.add(access$6000);
                }
                this.customAccessibilityActions.add(access$6000);
            }
        }

        private void ensureInverseTransform() {
            if (this.inverseTransformDirty) {
                this.inverseTransformDirty = false;
                if (this.inverseTransform == null) {
                    this.inverseTransform = new float[16];
                }
                if (!Matrix.invertM(this.inverseTransform, 0, this.transform, 0)) {
                    Arrays.fill(this.inverseTransform, 0.0f);
                }
            }
        }

        /* access modifiers changed from: private */
        public Rect getGlobalRect() {
            return this.globalRect;
        }

        /* access modifiers changed from: private */
        public SemanticsNode hitTest(float[] fArr) {
            float f = fArr[3];
            float f2 = fArr[0] / f;
            float f3 = fArr[1] / f;
            if (f2 < this.left || f2 >= this.right || f3 < this.top || f3 >= this.bottom) {
                return null;
            }
            float[] fArr2 = new float[4];
            for (SemanticsNode next : this.childrenInHitTestOrder) {
                if (!next.hasFlag(Flag.IS_HIDDEN)) {
                    next.ensureInverseTransform();
                    Matrix.multiplyMV(fArr2, 0, next.inverseTransform, 0, fArr, 0);
                    SemanticsNode hitTest = next.hitTest(fArr2);
                    if (hitTest != null) {
                        return hitTest;
                    }
                }
            }
            return this;
        }

        /* access modifiers changed from: private */
        public boolean isFocusable() {
            if (hasFlag(Flag.SCOPES_ROUTE)) {
                return false;
            }
            if (hasFlag(Flag.IS_FOCUSABLE)) {
                return true;
            }
            if ((((((Action.SCROLL_RIGHT.value | Action.SCROLL_LEFT.value) | Action.SCROLL_UP.value) | Action.SCROLL_DOWN.value) ^ -1) & this.actions) == 0 && this.flags == 0 && ((this.label == null || this.label.isEmpty()) && ((this.value == null || this.value.isEmpty()) && (this.hint == null || this.hint.isEmpty())))) {
                return false;
            }
            return true;
        }

        /* access modifiers changed from: private */
        public void collectRoutes(List<SemanticsNode> list) {
            if (hasFlag(Flag.SCOPES_ROUTE)) {
                list.add(this);
            }
            for (SemanticsNode collectRoutes : this.childrenInTraversalOrder) {
                collectRoutes.collectRoutes(list);
            }
        }

        /* access modifiers changed from: private */
        public String getRouteName() {
            if (hasFlag(Flag.NAMES_ROUTE) && this.label != null && !this.label.isEmpty()) {
                return this.label;
            }
            for (SemanticsNode routeName : this.childrenInTraversalOrder) {
                String routeName2 = routeName.getRouteName();
                if (routeName2 != null && !routeName2.isEmpty()) {
                    return routeName2;
                }
            }
            return null;
        }

        /* access modifiers changed from: private */
        public void updateRecursively(float[] fArr, Set<SemanticsNode> set, boolean z) {
            set.add(this);
            if (this.globalGeometryDirty) {
                z = true;
            }
            if (z) {
                if (this.globalTransform == null) {
                    this.globalTransform = new float[16];
                }
                Matrix.multiplyMM(this.globalTransform, 0, fArr, 0, this.transform, 0);
                float[] fArr2 = new float[4];
                fArr2[2] = 0.0f;
                fArr2[3] = 1.0f;
                float[] fArr3 = new float[4];
                float[] fArr4 = new float[4];
                float[] fArr5 = new float[4];
                float[] fArr6 = new float[4];
                fArr2[0] = this.left;
                fArr2[1] = this.top;
                transformPoint(fArr3, this.globalTransform, fArr2);
                fArr2[0] = this.right;
                fArr2[1] = this.top;
                transformPoint(fArr4, this.globalTransform, fArr2);
                fArr2[0] = this.right;
                fArr2[1] = this.bottom;
                transformPoint(fArr5, this.globalTransform, fArr2);
                fArr2[0] = this.left;
                fArr2[1] = this.bottom;
                transformPoint(fArr6, this.globalTransform, fArr2);
                if (this.globalRect == null) {
                    this.globalRect = new Rect();
                }
                this.globalRect.set(Math.round(min(fArr3[0], fArr4[0], fArr5[0], fArr6[0])), Math.round(min(fArr3[1], fArr4[1], fArr5[1], fArr6[1])), Math.round(max(fArr3[0], fArr4[0], fArr5[0], fArr6[0])), Math.round(max(fArr3[1], fArr4[1], fArr5[1], fArr6[1])));
                this.globalGeometryDirty = false;
            }
            for (SemanticsNode updateRecursively : this.childrenInTraversalOrder) {
                updateRecursively.updateRecursively(this.globalTransform, set, z);
            }
        }

        private void transformPoint(float[] fArr, float[] fArr2, float[] fArr3) {
            Matrix.multiplyMV(fArr, 0, fArr2, 0, fArr3, 0);
            float f = fArr[3];
            fArr[0] = fArr[0] / f;
            fArr[1] = fArr[1] / f;
            fArr[2] = fArr[2] / f;
            fArr[3] = 0.0f;
        }

        private float min(float f, float f2, float f3, float f4) {
            return Math.min(f, Math.min(f2, Math.min(f3, f4)));
        }

        private float max(float f, float f2, float f3, float f4) {
            return Math.max(f, Math.max(f2, Math.max(f3, f4)));
        }

        /* access modifiers changed from: private */
        public String getValueLabelHint() {
            StringBuilder sb = new StringBuilder();
            for (String str : new String[]{this.value, this.label, this.hint}) {
                if (str != null && str.length() > 0) {
                    if (sb.length() > 0) {
                        sb.append(AVFSCacheConstants.COMMA_SEP);
                    }
                    sb.append(str);
                }
            }
            if (sb.length() > 0) {
                return sb.toString();
            }
            return null;
        }
    }

    public boolean externalViewRequestSendAccessibilityEvent(View view, View view2, AccessibilityEvent accessibilityEvent) {
        Integer recordFlutterId;
        if (!this.accessibilityViewEmbedder.requestSendAccessibilityEvent(view, view2, accessibilityEvent) || (recordFlutterId = this.accessibilityViewEmbedder.getRecordFlutterId(view, accessibilityEvent)) == null) {
            return false;
        }
        int eventType = accessibilityEvent.getEventType();
        if (eventType == 8) {
            this.embeddedInputFocusedNodeId = recordFlutterId;
            this.inputFocusedSemanticsNode = null;
            return true;
        } else if (eventType == 128) {
            this.hoveredObject = null;
            return true;
        } else if (eventType == 32768) {
            this.embeddedAccessibilityFocusedNodeId = recordFlutterId;
            this.accessibilityFocusedSemanticsNode = null;
            return true;
        } else if (eventType != 65536) {
            return true;
        } else {
            this.embeddedInputFocusedNodeId = null;
            this.embeddedAccessibilityFocusedNodeId = null;
            return true;
        }
    }
}
