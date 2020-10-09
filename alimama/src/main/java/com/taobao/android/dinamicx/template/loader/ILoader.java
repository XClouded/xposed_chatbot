package com.taobao.android.dinamicx.template.loader;

import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import com.taobao.android.dinamicx.widget.DXWidgetNode;

public interface ILoader {
    DXWidgetNode load(DXTemplateItem dXTemplateItem, DXFileManager dXFileManager, String str);
}
