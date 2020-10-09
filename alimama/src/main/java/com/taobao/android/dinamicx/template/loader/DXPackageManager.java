package com.taobao.android.dinamicx.template.loader;

import android.content.Context;
import android.text.TextUtils;
import com.taobao.android.dinamicx.DXError;
import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.monitor.DXMonitorConstant;
import com.taobao.android.dinamicx.template.DXTemplateInfoManager;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import com.taobao.android.dinamicx.widget.DXWidgetNode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DXPackageManager {
    private DXBinaryLoader binaryLoader = new DXBinaryLoader();
    private DXFileManager fileManager = DXFileManager.getInstance();
    private Map<String, ILoader> loaders = new HashMap();

    private void registerLoader(String str, ILoader iLoader) {
        if (!this.loaders.containsKey(str)) {
            this.loaders.put(str, iLoader);
        }
    }

    private DXWidgetNode load(DXTemplateItem dXTemplateItem, String str, Map<String, String> map, DXRuntimeContext dXRuntimeContext, Context context) {
        List<DXError.DXErrorInfo> list;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        byte[] load = this.fileManager.load(str, dXRuntimeContext);
        if (load == null || load.length == 0) {
            if (!(dXRuntimeContext == null || dXRuntimeContext.getDxError() == null || (list = dXRuntimeContext.getDxError().dxErrorInfoList) == null)) {
                DXError.DXErrorInfo dXErrorInfo = new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_TEMPLATE, DXMonitorConstant.DX_MONITOR_TEMPLATE_READ, DXError.DX_TEMPLATE_LOAD_ERROR);
                if (load == null) {
                    dXErrorInfo.reason = "DXPackageManager load  bytes == null";
                } else {
                    dXErrorInfo.reason = "DXPackageManager load  bytes.len == 0";
                }
                list.add(dXErrorInfo);
            }
            DXTemplateInfoManager.getInstance().removeTemplate(dXRuntimeContext.getBizType(), dXTemplateItem);
            return null;
        }
        DXWidgetNode loadFromBuffer = this.binaryLoader.loadFromBuffer(load, dXRuntimeContext, context);
        if (map != null) {
            for (Map.Entry next : map.entrySet()) {
                if (this.loaders.containsKey(next.getKey())) {
                    this.loaders.get(next.getKey()).load(dXTemplateItem, this.fileManager, (String) next.getValue());
                }
            }
        }
        return loadFromBuffer;
    }

    public DXWidgetNode load(DXTemplateItem dXTemplateItem, DXRuntimeContext dXRuntimeContext, Context context) {
        return load(dXTemplateItem, dXTemplateItem.packageInfo.mainFilePath, dXTemplateItem.packageInfo.subFilePathDict, dXRuntimeContext, context);
    }
}
