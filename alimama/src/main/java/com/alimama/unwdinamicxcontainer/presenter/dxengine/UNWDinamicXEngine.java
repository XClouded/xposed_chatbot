package com.alimama.unwdinamicxcontainer.presenter.dxengine;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.IEtaoLogger;
import alimama.com.unweventparse.constants.EventConstants;
import android.content.Context;
import android.view.View;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alimama.unwdinamicxcontainer.event.BaseEvent;
import com.alimama.unwdinamicxcontainer.event.IDXEvent;
import com.alimama.unwdinamicxcontainer.event.IDXEventRegister;
import com.alimama.unwdinamicxcontainer.model.dxengine.DXEngineDataModel;
import com.taobao.android.dinamicx.DXAbsEventHandler;
import com.taobao.android.dinamicx.DXEngineConfig;
import com.taobao.android.dinamicx.DXResult;
import com.taobao.android.dinamicx.DXRootView;
import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.DinamicXEngine;
import com.taobao.android.dinamicx.expression.event.DXEvent;
import com.taobao.android.dinamicx.notification.DXNotificationResult;
import com.taobao.android.dinamicx.notification.IDXNotificationListener;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import com.taobao.android.dinamicx.template.utils.DXHashUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UNWDinamicXEngine implements IDinamicXEngine, IDXEventRegister {
    private final String TAG = "UNWDinamicXEngine";
    private String mBizType;
    private Context mContext;
    /* access modifiers changed from: private */
    public JSONObject mData;
    /* access modifiers changed from: private */
    public DXEngineDataModel mDataModel;
    private DinamicXEngine mDinamicXEngine;
    /* access modifiers changed from: private */
    public HashMap<String, IDXEvent> mHashMap;
    /* access modifiers changed from: private */
    public IDXEnginePresenter mPresenter;
    private List<DXTemplateItem> mTemplateList;

    public UNWDinamicXEngine(Context context, String str, IDXEnginePresenter iDXEnginePresenter) {
        UNWManager.getInstance().getLogger().info("UNWDinamicXEngine", "UNWDinamicXEngine", "constructor");
        this.mContext = context;
        this.mBizType = str;
        this.mPresenter = iDXEnginePresenter;
        initDXEngine();
        initData();
        registerEventHandler();
        registerCommonEvents();
    }

    public void registerEventHandler() {
        this.mDinamicXEngine.registerEventHandler(DXHashUtil.hash("UNWCommonClick"), new DXAbsEventHandler() {
            public void handleEvent(DXEvent dXEvent, Object[] objArr, DXRuntimeContext dXRuntimeContext) {
                try {
                    JSONArray parseArray = JSONObject.parseArray(objArr[0].toString());
                    for (int i = 0; i < parseArray.size(); i++) {
                        JSONObject jSONObject = (JSONObject) parseArray.get(i);
                        String string = jSONObject.getString("type");
                        if (UNWDinamicXEngine.this.mHashMap.get(string) != null) {
                            ((IDXEvent) UNWDinamicXEngine.this.mHashMap.get(string)).executeEvent(jSONObject.toJSONString());
                        }
                    }
                } catch (Exception e) {
                    IEtaoLogger logger = UNWManager.getInstance().getLogger();
                    logger.error("UNWDinamicXEngine", "registerEventHandler", "Exception is: " + e.toString());
                }
            }
        });
    }

    private void registerCommonEvents() {
        registerEvent(EventConstants.UT.NAME, new BaseEvent());
        registerEvent(EventConstants.pageRouter.NAME, new BaseEvent());
    }

    private void initDXEngine() {
        this.mDinamicXEngine = new DinamicXEngine(new DXEngineConfig.Builder(this.mBizType).withDowngradeType(1).withPipelineCacheMaxCount(500).build());
        this.mDinamicXEngine.registerNotificationListener(new IDXNotificationListener() {
            public void onNotificationListener(DXNotificationResult dXNotificationResult) {
                if (dXNotificationResult.finishedTemplateItems != null && dXNotificationResult.finishedTemplateItems.size() > 0) {
                    for (DXTemplateItem access$100 : dXNotificationResult.finishedTemplateItems) {
                        DXTemplateItem access$1002 = UNWDinamicXEngine.this.fetchTemplate(access$100);
                        if (access$1002 != null) {
                            DXResult access$400 = UNWDinamicXEngine.this.renderTemplate((DXRootView) UNWDinamicXEngine.this.createView(access$1002).result, UNWDinamicXEngine.this.mData);
                            if (access$400 == null || access$400.result == null) {
                                UNWDinamicXEngine.this.mPresenter.renderFailed(access$1002.toString());
                                UNWManager.getInstance().getLogger().error("UNWDinamicXEngine", "renderOnNotificationListener", "render Failed");
                            } else {
                                UNWDinamicXEngine.this.mPresenter.renderSuccess((View) access$400.result);
                                UNWManager.getInstance().getLogger().success("UNWDinamicXEngine", "renderOnNotificationListener");
                            }
                        }
                    }
                } else if (dXNotificationResult.failedTemplateItems != null && dXNotificationResult.failedTemplateItems.size() > 0) {
                    for (DXTemplateItem next : dXNotificationResult.failedTemplateItems) {
                        if (next.equals(UNWDinamicXEngine.this.generateTemplate(UNWDinamicXEngine.this.mDataModel.getTemplateJsonData()))) {
                            UNWDinamicXEngine.this.mPresenter.renderFailed(next.toString());
                            IEtaoLogger logger = UNWManager.getInstance().getLogger();
                            logger.error("UNWDinamicXEngine", "downloadFailedOnNotificationListener", "download Failed info: " + next.toString());
                        }
                    }
                }
            }
        });
    }

    private void initData() {
        this.mHashMap = new HashMap<>();
    }

    public void onDestroy() {
        this.mDinamicXEngine.onDestroy();
    }

    public void render(DXEngineDataModel dXEngineDataModel) {
        this.mDataModel = dXEngineDataModel;
        this.mData = JSON.parseObject(dXEngineDataModel.getFieldsJsonData());
        DXTemplateItem fetchTemplate = fetchTemplate(generateTemplate(dXEngineDataModel.getTemplateJsonData()));
        if (fetchTemplate != null) {
            UNWManager.getInstance().getLogger().success("UNWDinamicXEngine", "fetchedTemplate render");
            DXResult<DXRootView> renderTemplate = renderTemplate((DXRootView) createView(fetchTemplate).result, this.mData);
            if (renderTemplate == null || renderTemplate.result == null) {
                this.mPresenter.renderFailed(fetchTemplate.toString());
                UNWManager.getInstance().getLogger().error("UNWDinamicXEngine", "render", "render Failed");
                return;
            }
            this.mPresenter.renderSuccess((View) renderTemplate.result);
            UNWManager.getInstance().getLogger().success("UNWDinamicXEngine", "render");
            return;
        }
        UNWManager.getInstance().getLogger().info("UNWDinamicXEngine", "fetchedTemplate render", "fetchedTemplate failed, need downloadTemplates");
        downloadTemplates(dXEngineDataModel.getTemplateJsonData());
    }

    public void render(DXEngineDataModel dXEngineDataModel, IDXEnginePresenter iDXEnginePresenter) {
        this.mDataModel = dXEngineDataModel;
        this.mData = JSON.parseObject(dXEngineDataModel.getFieldsJsonData());
        DXTemplateItem fetchTemplate = fetchTemplate(generateTemplate(dXEngineDataModel.getTemplateJsonData()));
        if (fetchTemplate != null) {
            UNWManager.getInstance().getLogger().success("UNWDinamicXEngine", "fetchedTemplate render IDXEnginePresenter");
            DXResult<DXRootView> renderTemplate = renderTemplate((DXRootView) createView(fetchTemplate).result, this.mData);
            if (renderTemplate == null || renderTemplate.result == null) {
                iDXEnginePresenter.renderFailed(fetchTemplate.toString());
                UNWManager.getInstance().getLogger().error("UNWDinamicXEngine", "renderIDXEnginePresenter", "render Failed");
                return;
            }
            iDXEnginePresenter.renderSuccess((View) renderTemplate.result);
            UNWManager.getInstance().getLogger().success("UNWDinamicXEngine", "renderIDXEnginePresenter");
            return;
        }
        UNWManager.getInstance().getLogger().info("UNWDinamicXEngine", "render IDXEnginePresenter", "fetchedTemplate failed, need downloadTemplates");
        downloadTemplates(dXEngineDataModel.getTemplateJsonData());
    }

    public void registerEvent(String str, IDXEvent iDXEvent) {
        this.mHashMap.put(str, iDXEvent);
    }

    private JSONObject downloadTemplates(String str) {
        JSONObject parseObject = JSON.parseObject(str);
        this.mTemplateList = new ArrayList();
        try {
            DXTemplateItem dXTemplateItem = new DXTemplateItem();
            dXTemplateItem.name = parseObject.getString("name");
            dXTemplateItem.version = Long.parseLong(parseObject.getString("version"));
            dXTemplateItem.templateUrl = parseObject.getString("url");
            this.mTemplateList.add(dXTemplateItem);
            this.mDinamicXEngine.downLoadTemplates(this.mTemplateList);
        } catch (Exception e) {
            this.mPresenter.renderFailed(str);
            IEtaoLogger logger = UNWManager.getInstance().getLogger();
            logger.error("UNWDinamicXEngine", "downloadTemplates", "Exception: " + e.toString());
        }
        return parseObject;
    }

    /* access modifiers changed from: private */
    public DXTemplateItem generateTemplate(String str) {
        JSONObject parseObject = JSON.parseObject(str);
        DXTemplateItem dXTemplateItem = new DXTemplateItem();
        try {
            dXTemplateItem.name = parseObject.getString("name");
            dXTemplateItem.version = Long.parseLong(parseObject.getString("version"));
            dXTemplateItem.templateUrl = parseObject.getString("url");
        } catch (Exception e) {
            IEtaoLogger logger = UNWManager.getInstance().getLogger();
            logger.error("UNWDinamicXEngine", "generateTemplate", "Exception: " + e.toString());
            this.mPresenter.renderFailed(str);
        }
        return dXTemplateItem;
    }

    /* access modifiers changed from: private */
    public DXTemplateItem fetchTemplate(DXTemplateItem dXTemplateItem) {
        return this.mDinamicXEngine.fetchTemplate(dXTemplateItem);
    }

    /* access modifiers changed from: private */
    public DXResult<DXRootView> createView(DXTemplateItem dXTemplateItem) {
        return this.mDinamicXEngine.createView(this.mContext, dXTemplateItem);
    }

    /* access modifiers changed from: private */
    public DXResult<DXRootView> renderTemplate(DXRootView dXRootView, JSONObject jSONObject) {
        return this.mDinamicXEngine.renderTemplate(dXRootView, jSONObject);
    }

    private void reset() {
        this.mDinamicXEngine.reset();
    }
}
