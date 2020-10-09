package com.taobao.android.dxcontainer;

import android.text.TextUtils;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DXContainerModelManager {
    private String bizType;
    Map<String, DXContainerModel> modelId2Model = new HashMap();
    Map<String, List<DXContainerModel>> modelTag2Model = new HashMap();

    DXContainerModelManager(String str) {
        this.bizType = str;
    }

    public DXContainerModel getDXCModelByID(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        DXContainerModel dXContainerModel = this.modelId2Model.get(str);
        if (dXContainerModel == null) {
            String str2 = this.bizType;
            DXContainerAppMonitor.trackerError(str2, (DXContainerModel) null, DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_MODEL, DXContainerErrorConstant.DX_CONTAINER_ERROR_GET_DXC_MODEL_BY_ID_MODEL_NOT_FOUND, "modelId=" + str + DXContainerErrorConstant.DX_CONTAINER_ERROR_MESSAGE_GET_DXC_MODEL_BY_ID_MODEL_NOT_FOUND);
        }
        return dXContainerModel;
    }

    public List<DXContainerModel> getDXCModelByTag(String str) {
        if (!TextUtils.isEmpty(str) && this.modelTag2Model.get(str) == null) {
            String str2 = this.bizType;
            DXContainerAppMonitor.trackerError(str2, (DXContainerModel) null, DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_MODEL, DXContainerErrorConstant.DX_CONTAINER_ERROR_GET_DXC_MODEL_BY_TAG_MODEL_NOT_FOUND, "tag=" + str + DXContainerErrorConstant.DX_CONTAINER_ERROR_MESSAGE_GET_DXC_MODEL_BY_TAG_MODEL_NOT_FOUND);
        }
        return null;
    }

    public void removeIdAndTag(DXContainerModel dXContainerModel) {
        List list;
        if (dXContainerModel != null) {
            this.modelId2Model.remove(dXContainerModel.getId());
            if (!TextUtils.isEmpty(dXContainerModel.getTag()) && (list = this.modelTag2Model.get(dXContainerModel.getTag())) != null) {
                list.remove(dXContainerModel);
            }
        }
    }

    public void addIdAndTag(DXContainerModel dXContainerModel) {
        this.modelId2Model.put(dXContainerModel.getId(), dXContainerModel);
        String tag = dXContainerModel.getTag();
        if (!TextUtils.isEmpty(tag)) {
            List list = this.modelTag2Model.get(dXContainerModel.getTag());
            if (list != null) {
                list.add(dXContainerModel);
                return;
            }
            ArrayList arrayList = new ArrayList();
            arrayList.add(dXContainerModel);
            this.modelTag2Model.put(tag, arrayList);
        }
    }

    public void updateDXCModelId(Map<String, DXContainerModel> map) {
        if (map != null && !map.isEmpty()) {
            this.modelId2Model.putAll(map);
        }
    }

    public void updateDXCModelTag(Map<String, List<DXContainerModel>> map) {
        if (map != null && !map.isEmpty()) {
            for (String next : map.keySet()) {
                List list = map.get(next);
                if (list != null && !list.isEmpty()) {
                    List list2 = this.modelTag2Model.get(next);
                    if (list2 != null) {
                        list2.addAll(list);
                    } else {
                        this.modelTag2Model.put(next, new ArrayList(list));
                    }
                }
            }
        }
    }

    public void traverseModel(DXContainerSingleRVManager dXContainerSingleRVManager, List<DXTemplateItem> list, Map<String, DXContainerModel> map, Map<String, List<DXContainerModel>> map2, DXContainerModel dXContainerModel) {
        if (dXContainerModel != null && list != null && map != null && map2 != null) {
            dXContainerModel.setSingleCManager(dXContainerSingleRVManager);
            DXTemplateItem templateItem = dXContainerModel.getTemplateItem();
            if (templateItem != null) {
                list.add(templateItem);
            }
            String id = dXContainerModel.getId();
            if (!TextUtils.isEmpty(id)) {
                map.put(id, dXContainerModel);
            }
            String tag = dXContainerModel.getTag();
            if (!TextUtils.isEmpty(tag)) {
                if (map2.get(tag) == null) {
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(dXContainerModel);
                    map2.put(tag, arrayList);
                } else {
                    map2.get(tag).add(dXContainerModel);
                }
            }
            if (dXContainerModel.getChildren() != null && dXContainerModel.getChildren().size() > 0) {
                for (DXContainerModel traverseModel : dXContainerModel.getChildren()) {
                    traverseModel(dXContainerSingleRVManager, list, map, map2, traverseModel);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void clear() {
        this.modelTag2Model.clear();
        this.modelId2Model.clear();
    }
}
