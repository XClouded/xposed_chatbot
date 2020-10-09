package com.alimama.unwdinamicxcontainer.utils;

import alimama.com.unwbase.UNWManager;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alimama.unwdinamicxcontainer.model.dxcengine.UltronageDataInvalidType;
import com.alimama.unwdinamicxcontainer.presenter.dxcengine.IDXContainerPresenter;
import com.alimama.unwdinamicxcontainer.presenter.dxcengine.UNWDinamicXContainerEngine;
import com.taobao.android.dxcontainer.DXContainerModel;
import com.taobao.android.ultron.datamodel.imp.ProtocolConst;
import java.util.ArrayList;
import java.util.List;

public class DXContainerUtil {
    private static final String TAG = "DXContainerUtil";

    public static void fetchUpdateModelList(DXContainerModel dXContainerModel, DXContainerModel dXContainerModel2, String str, boolean z) {
        List<DXContainerModel> children = dXContainerModel.getChildren();
        for (int i = 0; i < children.size(); i++) {
            DXContainerModel dXContainerModel3 = children.get(i);
            if (TextUtils.equals(str, dXContainerModel3.getId())) {
                List<DXContainerModel> children2 = dXContainerModel3.getChildren();
                int i2 = 0;
                while (i2 < children2.size()) {
                    if (!TextUtils.equals(children2.get(i2).getId(), dXContainerModel2.getId())) {
                        i2++;
                    } else if (z) {
                        children2.add(i2, dXContainerModel2);
                        children2.remove(i2 + 1);
                        return;
                    } else {
                        return;
                    }
                }
                children2.add(dXContainerModel2);
            }
        }
    }

    public static boolean isRootModelContainsSubList(DXContainerModel dXContainerModel, String str, DXContainerModel dXContainerModel2) {
        boolean z = false;
        if (dXContainerModel == null || dXContainerModel.getChildren() == null) {
            return false;
        }
        List<DXContainerModel> children = dXContainerModel.getChildren();
        int i = 0;
        while (true) {
            if (i >= children.size()) {
                break;
            } else if (TextUtils.equals(str, children.get(i).getId())) {
                z = true;
                break;
            } else {
                i++;
            }
        }
        if (!z) {
            children.add(dXContainerModel2);
        }
        return z;
    }

    public static void sort(DXContainerModel dXContainerModel, UNWDinamicXContainerEngine.OrderType orderType) {
        List<DXContainerModel> children = dXContainerModel.getChildren();
        for (int i = 0; i < children.size(); i++) {
            DXContainerModel dXContainerModel2 = children.get(i);
            List<DXContainerModel> children2 = dXContainerModel2.getChildren();
            ArrayList arrayList = new ArrayList();
            for (int i2 = 0; i2 < children2.size(); i2++) {
                insertRightPos(arrayList, children2.get(i2), orderType);
            }
            dXContainerModel2.setChildren(arrayList);
        }
    }

    public static void insertRightPos(List<DXContainerModel> list, DXContainerModel dXContainerModel, UNWDinamicXContainerEngine.OrderType orderType) {
        int dXContainerModelIndex;
        if (list == null || (dXContainerModelIndex = getDXContainerModelIndex(dXContainerModel)) < 0) {
            return;
        }
        if (list.size() == 0) {
            list.add(dXContainerModel);
            return;
        }
        int i = 0;
        if (orderType == UNWDinamicXContainerEngine.OrderType.DESCENDING_ORDER) {
            while (i < list.size()) {
                if (dXContainerModelIndex > getDXContainerModelIndex(list.get(i))) {
                    list.add(i, dXContainerModel);
                    return;
                }
                i++;
            }
            list.add(dXContainerModel);
        } else if (orderType == UNWDinamicXContainerEngine.OrderType.ASCENDING_ORDER) {
            while (i < list.size()) {
                if (dXContainerModelIndex < getDXContainerModelIndex(list.get(i))) {
                    list.add(i, dXContainerModel);
                    return;
                }
                i++;
            }
            list.add(dXContainerModel);
        }
    }

    public static int getDXContainerModelIndex(DXContainerModel dXContainerModel) {
        String id = dXContainerModel.getId();
        int lastIndexOf = id.lastIndexOf("_");
        if (lastIndexOf <= 0) {
            return -1;
        }
        try {
            return Integer.parseInt(id.substring(lastIndexOf + 1));
        } catch (Exception unused) {
            return -1;
        }
    }

    public static boolean isUltronageDataValid(String str, IDXContainerPresenter iDXContainerPresenter) {
        if (TextUtils.isEmpty(str)) {
            UNWManager.getInstance().getLogger().error(TAG, "isUltronageDataValid", "ultronage is empty or null!");
            iDXContainerPresenter.ultronageDataInValid(UltronageDataInvalidType.EMPTY);
            return false;
        }
        try {
            JSONObject parseObject = JSON.parseObject(str);
            if (!(parseObject.getJSONObject("data").getJSONObject("container") == null || parseObject.getJSONObject("data").getJSONObject("data") == null)) {
                if (parseObject.getJSONObject("data").getJSONObject(ProtocolConst.KEY_HIERARCHY) != null) {
                    return true;
                }
            }
            UNWManager.getInstance().getLogger().error(TAG, "isUltronageDataValid", "container or data or hierarchy is lost");
            iDXContainerPresenter.ultronageDataInValid(UltronageDataInvalidType.STRUCTURE_LOST);
            return false;
        } catch (Exception e) {
            UNWManager.getInstance().getLogger().error(TAG, "isUltronageDataValid", e.getLocalizedMessage());
            iDXContainerPresenter.ultronageDataInValid(UltronageDataInvalidType.PARSE_EXCEPTION);
            return false;
        }
    }
}
