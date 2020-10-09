package com.taobao.android.preview;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Keep;
import com.taobao.android.dinamicx.DXAbsEventHandler;
import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.DinamicXEngineRouter;
import com.taobao.android.dinamicx.expression.event.DXCheckBoxEvent;
import com.taobao.android.dinamicx.expression.event.DXEvent;
import com.taobao.android.dinamicx.template.utils.DXHashUtil;
import com.taobao.android.preview.DXTemplatePreviewActivity;

@Keep
public class DXPreviewInterfaceImpl implements DXTemplatePreviewActivity.DXPreviewInterface {
    public void previewEngineDidInitialized(final DinamicXEngineRouter dinamicXEngineRouter) {
        Log.e("shandian", "反射调用previewEngineDidInitialized");
        dinamicXEngineRouter.registerEventHandler(DXHashUtil.hash("test"), new DXAbsEventHandler() {
            public void handleEvent(DXEvent dXEvent, Object[] objArr, DXRuntimeContext dXRuntimeContext) {
                if (dXEvent instanceof DXCheckBoxEvent) {
                    Log.i("lx", "checked=" + ((DXCheckBoxEvent) dXEvent).isChecked());
                }
                String str = null;
                if (objArr != null) {
                    str = objArr.toString();
                }
                DinamicXEngineRouter dinamicXEngineRouter = dinamicXEngineRouter;
                Context applicationContext = DinamicXEngineRouter.getApplicationContext();
                Toast.makeText(applicationContext, "收到点击 参数为: " + str, 0).show();
            }
        });
    }
}
