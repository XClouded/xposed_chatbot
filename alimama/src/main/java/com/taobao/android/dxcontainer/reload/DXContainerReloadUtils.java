package com.taobao.android.dxcontainer.reload;

import android.content.Context;
import android.text.TextUtils;
import com.taobao.android.dxcontainer.DXContainerModel;
import com.taobao.android.dxcontainer.layout.DXContainerLayoutConstant;
import com.taobao.android.dxcontainer.layout.DXContainerLayoutManager;
import com.taobao.android.dxcontainer.render.DXContainerViewTypeGenerator;
import com.taobao.android.dxcontainer.vlayout.LayoutHelper;
import com.taobao.android.dxcontainer.vlayout.layout.StickyLayoutHelper;
import java.util.ArrayList;
import java.util.List;

public class DXContainerReloadUtils {
    static DXContainerModel addDefaultLayout(DXContainerModel dXContainerModel) {
        if (dXContainerModel == null) {
            return null;
        }
        DXContainerModel dXContainerModel2 = new DXContainerModel();
        dXContainerModel2.setLayoutType("linear");
        dXContainerModel2.setChildren(new ArrayList());
        dXContainerModel2.getChildren().add(dXContainerModel);
        return dXContainerModel2;
    }

    static void findLayoutExceptRoot(DXContainerModel dXContainerModel, List<DXContainerModel> list) {
        if (dXContainerModel != null) {
            if ("linear".equals(dXContainerModel.getLayoutType())) {
                for (DXContainerModel findLayoutDFS : dXContainerModel.getChildren()) {
                    findLayoutDFS(findLayoutDFS, list);
                }
                return;
            }
            findLayoutDFS(dXContainerModel, list);
        }
    }

    static void findLayoutDFS(DXContainerModel dXContainerModel, List<DXContainerModel> list) {
        if (dXContainerModel != null) {
            if (TextUtils.isEmpty(dXContainerModel.getLayoutType())) {
                List<DXContainerModel> children = dXContainerModel.getChildren();
                if (children == null || children.isEmpty()) {
                    list.add(addDefaultLayout(dXContainerModel));
                    return;
                }
                for (int i = 0; i < children.size(); i++) {
                    DXContainerModel dXContainerModel2 = children.get(i);
                    if (!TextUtils.isEmpty(dXContainerModel2.getLayoutType())) {
                        list.add(dXContainerModel2);
                    } else {
                        findLayoutDFS(dXContainerModel2, list);
                    }
                }
                return;
            }
            list.add(dXContainerModel);
        }
    }

    static void findFinalChildren(DXContainerModel dXContainerModel, List<DXContainerModel> list) {
        if (dXContainerModel != null) {
            if (DXContainerLayoutConstant.DXC_LAYOUT_TABCONTENT.equals(dXContainerModel.getLayoutType())) {
                list.add(dXContainerModel);
                return;
            }
            List<DXContainerModel> children = dXContainerModel.getChildren();
            if (children != null && !children.isEmpty()) {
                for (int i = 0; i < children.size(); i++) {
                    DXContainerModel dXContainerModel2 = children.get(i);
                    if (dXContainerModel2.getChildren() == null || dXContainerModel2.getChildren().isEmpty()) {
                        list.add(dXContainerModel2);
                    } else {
                        findFinalChildren(dXContainerModel2, list);
                    }
                }
            }
        }
    }

    static boolean updateViewType(DXContainerViewTypeGenerator dXContainerViewTypeGenerator, List<DXContainerModel> list) {
        if (dXContainerViewTypeGenerator == null || list == null || list.size() <= 0) {
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            dXContainerViewTypeGenerator.modelToViewType(i, list.get(i));
        }
        return true;
    }

    public static List<LayoutHelper> traversalTree(Context context, DXContainerModel dXContainerModel, List<DXContainerModel> list, DXContainerLayoutManager dXContainerLayoutManager, DXContainerViewTypeGenerator dXContainerViewTypeGenerator) {
        LayoutHelper layoutHelper;
        if (list == null) {
            list = new ArrayList<>();
        } else {
            list.clear();
        }
        ArrayList arrayList = new ArrayList();
        ArrayList<DXContainerModel> arrayList2 = new ArrayList<>();
        findLayoutExceptRoot(dXContainerModel, arrayList2);
        for (DXContainerModel dXContainerModel2 : arrayList2) {
            ArrayList arrayList3 = new ArrayList();
            findFinalChildren(dXContainerModel2, arrayList3);
            list.addAll(arrayList3);
            String layoutType = dXContainerModel2.getLayoutType();
            if (dXContainerModel2.getLayoutHelper() == null) {
                layoutHelper = dXContainerLayoutManager.getIDXCLayout(layoutType).createDXCLayout(context, dXContainerModel2.getStyleModel());
                if (dXContainerModel2.getEngine() != null && (layoutHelper instanceof StickyLayoutHelper)) {
                    ((StickyLayoutHelper) layoutHelper).setStickyListener(dXContainerModel2.getEngine().getStickyListener());
                }
                dXContainerModel2.setLayoutHelper(layoutHelper);
            } else {
                layoutHelper = dXContainerModel2.getLayoutHelper();
            }
            layoutHelper.setItemCount(arrayList3.size());
            arrayList.add(layoutHelper);
        }
        if (dXContainerViewTypeGenerator != null) {
            for (int i = 0; i < list.size(); i++) {
                dXContainerViewTypeGenerator.modelToViewType(i, list.get(i));
            }
        }
        return arrayList;
    }
}
