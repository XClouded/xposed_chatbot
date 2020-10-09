package com.taobao.android.dinamic;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import com.taobao.android.dinamic.asyncrender.DPreRenderTask;
import com.taobao.android.dinamic.dinamic.DinamicPerformMonitor;
import com.taobao.android.dinamic.dinamic.DinamicPreRenderThreadExecutor;
import com.taobao.android.dinamic.log.DinamicLog;
import com.taobao.android.dinamic.log.DinamicLogThread;
import com.taobao.android.dinamic.model.DinamicParams;
import com.taobao.android.dinamic.parser.DinamicParser;
import com.taobao.android.dinamic.property.DAttrUtils;
import com.taobao.android.dinamic.property.DinamicProperty;
import com.taobao.android.dinamic.tempate.DinamicTemplate;
import com.taobao.android.dinamic.view.CompatibleView;
import com.taobao.android.dinamic.view.DFrameLayout;
import com.taobao.android.dinamic.view.DLinearLayout;
import com.taobao.android.dinamic.view.DLoopLinearLayout;
import com.taobao.android.dinamic.view.DinamicError;
import com.taobao.android.dinamic.view.ViewResult;
import com.taobao.android.dinamicx.asyncrender.DXViewPoolManager;
import com.taobao.android.dinamicx.log.DXLog;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

public class DViewGenerator {
    public static final String TAG = "DViewGenerator";
    private ArrayDeque<View> bfsQueue = new ArrayDeque<>(16);
    /* access modifiers changed from: private */
    public String module = "default";

    public DViewGenerator(String str) {
        this.module = str;
    }

    public static DViewGenerator defaultViewGenerator() {
        return Dinamic.getModuleContainer("default").dViewGenerator;
    }

    public static DViewGenerator viewGeneratorWithModule(String str) {
        if (TextUtils.isEmpty(str)) {
            return Dinamic.getModuleContainer("default").dViewGenerator;
        }
        return Dinamic.getModuleContainer(str).dViewGenerator;
    }

    public ViewResult createView(Context context, ViewGroup viewGroup, DinamicTemplate dinamicTemplate, Object obj) {
        long nanoTime = System.nanoTime();
        if (context == null || dinamicTemplate == null || !dinamicTemplate.checkValid()) {
            ViewResult viewResult = new ViewResult(this.module);
            viewResult.setView((View) null);
            viewResult.setDinamicTemplate(dinamicTemplate);
            viewResult.getDinamicError().addErrorCodeWithInfo(DinamicError.ERROR_CODE_TEMPLATE_INFO_ERROR, dinamicTemplate != null ? dinamicTemplate.toString() : "context=null or exactTemplate=null");
            logCreateView(viewResult, System.nanoTime() - nanoTime);
            return viewResult;
        }
        beforeCreateView(dinamicTemplate);
        ViewResult viewResult2 = new ViewResult(this.module);
        XmlPullParser parser = DinamicParser.getParser(this.module, dinamicTemplate, viewResult2);
        long nanoTime2 = System.nanoTime();
        if (parser != null) {
            try {
                viewResult2.setDinamicTemplate(dinamicTemplate);
                viewResult2.setBindDataList(new ArrayList(20));
                DinamicParams.Builder builder = new DinamicParams.Builder();
                builder.withViewResult(viewResult2);
                builder.withModule(this.module);
                builder.withDinamicContext(obj);
                View inflate = DinamicInflater.from(context, builder.build()).inflate(parser, (ViewGroup) null);
                if (inflate instanceof CompatibleView) {
                    logCreateView(viewResult2, System.nanoTime() - nanoTime2);
                    return viewResult2;
                }
                DAttrUtils.handleRootViewLayoutParams(inflate, viewGroup);
                DinamicProperty dinamicProperty = (DinamicProperty) inflate.getTag(DinamicTagKey.PROPERTY_KEY);
                Object obj2 = dinamicProperty.fixedProperty.get(DinamicConstant.COMPILER_NAME);
                Object obj3 = dinamicProperty.fixedProperty.get(DinamicConstant.INTERPRETER_NAME);
                if (obj2 != null) {
                    dinamicTemplate.setCompilerVersion(String.valueOf(obj2));
                } else {
                    dinamicTemplate.setCompilerVersion(DinamicConstant.DEFAULT_VERSION);
                }
                if (obj3 != null) {
                    dinamicTemplate.setInterpreterVersion(String.valueOf(obj3));
                } else {
                    dinamicTemplate.setInterpreterVersion(DinamicConstant.DEFAULT_VERSION);
                }
                inflate.setTag("dinamicRootView");
                inflate.setTag(DinamicTagKey.TAG_ROOT_VIEW_RESULT, viewResult2);
                viewResult2.setView(inflate);
                logCreateView(viewResult2, System.nanoTime() - nanoTime2);
                return viewResult2;
            } catch (Throwable th) {
                viewResult2.setDinamicTemplate(dinamicTemplate);
                viewResult2.getDinamicError().addErrorCodeWithInfo("other", "inflateViewFailed");
                DinamicLog.e(TAG, this.module + "infalte dinamic view failed", th);
                logCreateView(viewResult2, System.nanoTime() - nanoTime2);
                return viewResult2;
            }
        } else {
            viewResult2.setDinamicTemplate(dinamicTemplate);
            viewResult2.getDinamicError().addErrorCodeWithInfo(DinamicError.ERROR_CODE_TEMPLATE_NOT_FOUND, dinamicTemplate.toString());
            logCreateView(viewResult2, System.nanoTime() - nanoTime2);
            return viewResult2;
        }
    }

    public ViewResult createView(Context context, ViewGroup viewGroup, DinamicTemplate dinamicTemplate) {
        return createView(context, viewGroup, dinamicTemplate, (Object) null);
    }

    public ViewResult bindData(View view, Object obj, Object obj2) {
        return bindDataWrap(view, obj, false, obj2);
    }

    public ViewResult bindData(View view, Object obj) {
        return bindDataWrap(view, obj, false, (Object) null);
    }

    public ViewResult bindDataWithRoop(View view, Object obj) {
        return bindDataWrap(view, obj, true, (Object) null);
    }

    private void beforeCreateView(DinamicTemplate dinamicTemplate) {
        if (DRegisterCenter.shareCenter().getMonitor() != null && DinamicLogThread.checkInit()) {
            DRegisterCenter.shareCenter().getMonitor().trackBeforeCreateView(this.module, dinamicTemplate);
        }
    }

    private void beforeBindData(DinamicTemplate dinamicTemplate) {
        if (DRegisterCenter.shareCenter().getMonitor() != null && DinamicLogThread.checkInit()) {
            DRegisterCenter.shareCenter().getMonitor().trackBeforeBindData(this.module, dinamicTemplate);
        }
    }

    private void logCreateView(final ViewResult viewResult, final long j) {
        if (DRegisterCenter.shareCenter().getPerformMonitor() != null && DinamicLogThread.checkInit()) {
            DinamicLogThread.threadHandler.postTask(new Runnable() {
                public void run() {
                    if (Dinamic.isDebugable()) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("createView template=");
                        sb.append(viewResult.getDinamicTemplate());
                        sb.append("consumimg=");
                        double d = (double) j;
                        Double.isNaN(d);
                        sb.append(d / 1000000.0d);
                        DinamicLog.d(Dinamic.TAG, sb.toString());
                    }
                    DinamicPerformMonitor performMonitor = DRegisterCenter.shareCenter().getPerformMonitor();
                    String access$000 = DViewGenerator.this.module;
                    DinamicTemplate dinamicTemplate = viewResult.getDinamicTemplate();
                    boolean isRenderSuccess = viewResult.isRenderSuccess();
                    DinamicError dinamicError = viewResult.isRenderSuccess() ? null : viewResult.getDinamicError();
                    double d2 = (double) j;
                    Double.isNaN(d2);
                    performMonitor.trackCreateView(access$000, dinamicTemplate, isRenderSuccess, dinamicError, d2 / 1000000.0d);
                }
            });
        }
    }

    public String getModule() {
        return this.module;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v7, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v8, resolved type: com.taobao.android.dinamic.view.ViewResult} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.taobao.android.dinamic.view.ViewResult bindDataWrap(android.view.View r1, java.lang.Object r2, boolean r3, java.lang.Object r4) {
        /*
            r0 = this;
            if (r1 == 0) goto L_0x006e
            if (r2 != 0) goto L_0x0005
            goto L_0x006e
        L_0x0005:
            int r3 = com.taobao.android.dinamic.DinamicTagKey.TAG_ROOT_VIEW_RESULT
            java.lang.Object r3 = r1.getTag(r3)
            com.taobao.android.dinamic.view.ViewResult r3 = (com.taobao.android.dinamic.view.ViewResult) r3
            if (r3 != 0) goto L_0x0048
            java.lang.String r3 = "dinamicRootView"
            android.view.View r1 = r1.findViewWithTag(r3)
            if (r1 != 0) goto L_0x002a
            com.taobao.android.dinamic.view.ViewResult r1 = new com.taobao.android.dinamic.view.ViewResult
            java.lang.String r2 = r0.module
            r1.<init>(r2)
            com.taobao.android.dinamic.view.DinamicError r2 = r1.getDinamicError()
            java.lang.String r3 = "other"
            java.lang.String r4 = "binddata rootView or data is null"
            r2.addErrorCodeWithInfo(r3, r4)
            return r1
        L_0x002a:
            int r3 = com.taobao.android.dinamic.DinamicTagKey.TAG_ROOT_VIEW_RESULT
            java.lang.Object r1 = r1.getTag(r3)
            r3 = r1
            com.taobao.android.dinamic.view.ViewResult r3 = (com.taobao.android.dinamic.view.ViewResult) r3
            if (r3 != 0) goto L_0x0048
            com.taobao.android.dinamic.view.ViewResult r1 = new com.taobao.android.dinamic.view.ViewResult
            java.lang.String r2 = r0.module
            r1.<init>(r2)
            com.taobao.android.dinamic.view.DinamicError r2 = r1.getDinamicError()
            java.lang.String r3 = "other"
            java.lang.String r4 = "binddata rootView or data is null"
            r2.addErrorCodeWithInfo(r3, r4)
            return r1
        L_0x0048:
            com.taobao.android.dinamic.tempate.DinamicTemplate r1 = r3.getDinamicTemplate()
            r0.beforeBindData(r1)
            com.taobao.android.dinamic.model.DinamicParams$Builder r1 = new com.taobao.android.dinamic.model.DinamicParams$Builder
            r1.<init>()
            r1.withDinamicContext(r4)
            java.lang.String r4 = r0.module
            r1.withModule(r4)
            r1.withViewResult(r3)
            r1.withOriginalData(r2)
            r1.withCurrentData(r2)
            com.taobao.android.dinamic.model.DinamicParams r1 = r1.build()
            com.taobao.android.dinamic.view.ViewResult r1 = r0.bindDataKernel(r1)
            return r1
        L_0x006e:
            com.taobao.android.dinamic.view.ViewResult r1 = new com.taobao.android.dinamic.view.ViewResult
            java.lang.String r2 = r0.module
            r1.<init>(r2)
            com.taobao.android.dinamic.view.DinamicError r2 = r1.getDinamicError()
            java.lang.String r3 = "other"
            java.lang.String r4 = "binddata rootView or data is null"
            r2.addErrorCodeWithInfo(r3, r4)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamic.DViewGenerator.bindDataWrap(android.view.View, java.lang.Object, boolean, java.lang.Object):com.taobao.android.dinamic.view.ViewResult");
    }

    private ViewResult bindDataKernel(DinamicParams dinamicParams) {
        long nanoTime = System.nanoTime();
        ViewResult viewResult = dinamicParams.getViewResult();
        Iterator<View> it = viewResult.getBindDataList().iterator();
        while (it.hasNext()) {
            View next = it.next();
            try {
                DinamicDataBinder.bindData(next, dinamicParams);
            } catch (Throwable unused) {
                DinamicError dinamicError = viewResult.getDinamicError();
                dinamicError.addErrorCodeWithInfo("other", next.getClass() + "bind data failed;");
            }
        }
        logBindData(viewResult, System.nanoTime() - nanoTime);
        return viewResult;
    }

    private void logBindData(final ViewResult viewResult, final long j) {
        if (DRegisterCenter.shareCenter().getPerformMonitor() != null && DinamicLogThread.checkInit()) {
            DinamicLogThread.threadHandler.postTask(new Runnable() {
                public void run() {
                    if (Dinamic.isDebugable()) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("bindData template=");
                        sb.append(viewResult.getDinamicTemplate());
                        sb.append("consuming=");
                        double d = (double) j;
                        Double.isNaN(d);
                        sb.append(d / 1000000.0d);
                        DinamicLog.d(Dinamic.TAG, sb.toString());
                    }
                    DinamicPerformMonitor performMonitor = DRegisterCenter.shareCenter().getPerformMonitor();
                    String access$000 = DViewGenerator.this.module;
                    DinamicTemplate dinamicTemplate = viewResult.getDinamicTemplate();
                    boolean isBindDataSuccess = viewResult.isBindDataSuccess();
                    DinamicError dinamicError = viewResult.isBindDataSuccess() ? null : viewResult.getDinamicError();
                    double d2 = (double) j;
                    Double.isNaN(d2);
                    performMonitor.trackBindData(access$000, dinamicTemplate, isBindDataSuccess, dinamicError, d2 / 1000000.0d);
                }
            });
        }
    }

    public void bindDataLoopCloneView(ArrayList<View> arrayList, DinamicParams dinamicParams) {
        Iterator<View> it = arrayList.iterator();
        while (it.hasNext()) {
            View next = it.next();
            try {
                DinamicDataBinder.bindData(next, dinamicParams);
            } catch (Throwable unused) {
                DinamicError dinamicError = dinamicParams.getViewResult().getDinamicError();
                dinamicError.addErrorCodeWithInfo("other", next.getClass() + "bind data failed;");
            }
        }
    }

    private boolean isNeedCopyChildView(View view) {
        if (DinamicConstant.DONOT_NEED_BIND_CHILD.equals(view.getTag()) || (view instanceof DLoopLinearLayout)) {
            return false;
        }
        if ((view instanceof DLinearLayout) || (view instanceof DFrameLayout)) {
            return true;
        }
        return false;
    }

    public ViewResult copyView(View view, Context context, DinamicParams dinamicParams) {
        ViewResult viewResult = new ViewResult(this.module);
        viewResult.setBindDataList(new ArrayList(20));
        View cloneView = DinamicViewCreator.cloneView(context, view, viewResult, dinamicParams);
        if (cloneView == null) {
            viewResult.setView((View) null);
            return viewResult;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null) {
            cloneView.setLayoutParams(layoutParams);
        }
        if (!(view instanceof ViewGroup)) {
            viewResult.setView(cloneView);
            return viewResult;
        }
        buildViewTree(context, view, cloneView, viewResult, dinamicParams);
        viewResult.setView(cloneView);
        return viewResult;
    }

    private void buildViewTree(Context context, View view, View view2, ViewResult viewResult, DinamicParams dinamicParams) {
        ViewGroup viewGroup = (ViewGroup) view;
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = viewGroup.getChildAt(i);
            View cloneView = DinamicViewCreator.cloneView(context, childAt, viewResult, dinamicParams);
            if (cloneView != null) {
                ((ViewGroup) view2).addView(cloneView, childAt.getLayoutParams());
                if (isNeedCopyChildView(childAt)) {
                    buildViewTree(context, childAt, cloneView, viewResult, dinamicParams);
                }
            }
        }
    }

    public void preRender(Context context, List<DinamicTemplate> list) {
        if (context != null && list != null && list.size() != 0) {
            DinamicPreRenderThreadExecutor.executor(new DPreRenderTask(context, this.module, list));
        }
    }

    public ViewResult preCreateView(Context context, ViewGroup viewGroup, DinamicTemplate dinamicTemplate, Object obj) {
        if (dinamicTemplate == null || context == null) {
            return null;
        }
        ViewResult obtainV2View = DXViewPoolManager.getInstance().obtainV2View(context, dinamicTemplate, this.module);
        if (obtainV2View == null) {
            return createView(context, viewGroup, dinamicTemplate, obj);
        }
        DXLog.print("命中2.0预加载view:  " + dinamicTemplate.toString());
        return obtainV2View;
    }

    public void clearPreRenderViewPoolCache() {
        DXViewPoolManager.getInstance().clearV2Cache(this.module);
    }
}
