package com.taobao.android.dinamic.asyncrender;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.view.ViewGroup;
import com.taobao.android.dinamic.DViewGenerator;
import com.taobao.android.dinamic.tempate.DinamicTemplate;
import com.taobao.android.dinamic.view.DinamicError;
import com.taobao.android.dinamic.view.ViewResult;
import com.taobao.android.dinamicx.DXError;
import com.taobao.android.dinamicx.asyncrender.DXViewPoolManager;
import com.taobao.android.dinamicx.asyncrender.ViewContext;
import com.taobao.android.dinamicx.exception.DXExceptionUtil;
import com.taobao.android.dinamicx.log.DXRemoteLog;
import com.taobao.android.dinamicx.monitor.DXAppMonitor;
import com.taobao.android.dinamicx.monitor.DXMonitorConstant;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import java.lang.reflect.Field;
import java.util.List;

public class DPreRenderTask implements Runnable {
    private static final String TAG = "DPreRenderTask";
    Context context;
    String module;
    List<DinamicTemplate> templateList;

    public DPreRenderTask(Context context2, String str, List<DinamicTemplate> list) {
        this.templateList = list;
        this.module = str;
        this.context = context2.getApplicationContext();
    }

    public void run() {
        try {
            Field declaredField = Looper.class.getDeclaredField("sThreadLocal");
            declaredField.setAccessible(true);
            Object obj = declaredField.get(Looper.getMainLooper());
            if (obj instanceof ThreadLocal) {
                ((ThreadLocal) obj).set(Looper.getMainLooper());
            }
            preRenderTemplate(new ViewContext(this.context));
        } catch (Throwable th) {
            DXAppMonitor.trackerError("dinamicx", (DXTemplateItem) null, DXMonitorConstant.DX_MONITOR_ASYNC_RENDER, DXMonitorConstant.PRE_RENDER_2_0_CRASH, DXError.V2_PRE_RENDER_CRASH, DXExceptionUtil.getStackTrace(th));
        }
    }

    private void preRenderTemplate(ViewContext viewContext) {
        if (this.templateList != null) {
            for (DinamicTemplate next : this.templateList) {
                DXTemplateItem dXTemplateItem = null;
                ViewResult createView = DViewGenerator.viewGeneratorWithModule(this.module).createView(viewContext, (ViewGroup) null, next);
                if (createView.isRenderSuccess()) {
                    DXRemoteLog.remoteLogi("DinamicX", "DinamicX", "asyncCreateTemplateView success:" + next.name);
                    DXViewPoolManager.getInstance().cacheV2ViewResult(createView, next, this.module);
                } else {
                    String allErrorDescription = createView.getDinamicError().getAllErrorDescription();
                    DXRemoteLog.remoteLogi("DinamicX", "DinamicX", "asyncCreateTemplateView fail:\n" + allErrorDescription);
                    if (TextUtils.isEmpty(allErrorDescription) || (!allErrorDescription.contains(DinamicError.ERROR_CODE_TEMPLATE_FILE_LOST) && !allErrorDescription.contains(DinamicError.ERROR_CODE_TEMPLATE_NOT_FOUND))) {
                        if (next != null) {
                            dXTemplateItem = transformTemplateToV3(next);
                        }
                        String str = this.module;
                        DXAppMonitor.trackerError(str, dXTemplateItem, DXMonitorConstant.DX_MONITOR_ASYNC_RENDER, DXMonitorConstant.PRE_RENDER_2_0_FAIL, DXError.V2_PRE_RENDER_FAIL, "asyncCreateTemplateView fail" + createView.getDinamicError().getAllErrorDescription());
                    }
                }
            }
        }
    }

    private DXTemplateItem transformTemplateToV3(DinamicTemplate dinamicTemplate) {
        if (dinamicTemplate == null) {
            return null;
        }
        try {
            DXTemplateItem dXTemplateItem = new DXTemplateItem();
            dXTemplateItem.name = dinamicTemplate.name;
            if (!TextUtils.isEmpty(dinamicTemplate.version)) {
                dXTemplateItem.version = Long.parseLong(dinamicTemplate.version);
            } else {
                dXTemplateItem.version = -1;
            }
            dXTemplateItem.templateUrl = dinamicTemplate.templateUrl;
            return dXTemplateItem;
        } catch (Throwable unused) {
            return null;
        }
    }
}
