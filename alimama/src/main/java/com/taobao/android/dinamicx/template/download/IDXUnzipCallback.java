package com.taobao.android.dinamicx.template.download;

import java.util.Map;

public interface IDXUnzipCallback {
    void onUnzipFinished(DXTemplateItem dXTemplateItem, Map<String, byte[]> map);
}
