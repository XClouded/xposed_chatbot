package com.taobao.android.preview;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Keep;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.alibaba.android.umbrella.utils.UmbrellaConstants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.dinamic.R;
import com.taobao.android.dinamicx.DXEngineConfig;
import com.taobao.android.dinamicx.DinamicXEngineRouter;
import com.taobao.android.dinamicx.notification.DXNotificationResult;
import com.taobao.android.dinamicx.notification.IDXNotificationListener;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import com.taobao.android.dinamicx.template.download.HttpDownloader;
import com.vivo.push.PushClientConstants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Keep
public class DXTemplatePreviewActivity extends AppCompatActivity implements IDXNotificationListener {
    public static final String PREVIEW_DINAMIC_MODULE = "preview";
    public static final String PREVIEW_INFO = "previewInfo";
    public static final String PREVIEW_TAG = "DXTemplatePreviewActivity";
    private DXTemplatePreviewAdapter adapter;
    JSONArray array;
    DinamicXEngineRouter engineRouter;
    private RecyclerView rvMainContainer;

    @Keep
    public interface DXPreviewInterface extends Serializable {
        void previewEngineDidInitialized(DinamicXEngineRouter dinamicXEngineRouter);
    }

    public static class PreviewInfo {
        public String bundlePath;
        public String className;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_template_preview);
        log(UmbrellaConstants.LIFECYCLE_CREATE);
        overridePendingTransition(0, 0);
        this.rvMainContainer = (RecyclerView) findViewById(R.id.rv_main_container);
        ((TextView) findViewById(R.id.dinamic_preview_back)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DXTemplatePreviewActivity.this.finish();
                DXTemplatePreviewActivity.this.overridePendingTransition(0, 0);
            }
        });
        initEngineRouter();
        initRecyclerView();
        try {
            String stringExtra = getIntent().getStringExtra(PREVIEW_INFO);
            if (!TextUtils.isEmpty(stringExtra)) {
                log("onCreate info :" + stringExtra);
                downLoadMockData(stringExtra);
                return;
            }
            finish();
            log("onCreate info isEmpty");
        } catch (Throwable th) {
            finish();
            th.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        log(UmbrellaConstants.LIFECYCLE_RESUME);
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        log("onNewIntent");
        try {
            String stringExtra = intent.getStringExtra(PREVIEW_INFO);
            if (!TextUtils.isEmpty(stringExtra)) {
                log("onNewIntent" + stringExtra);
                downLoadMockData(stringExtra);
                return;
            }
            finish();
            log("onNewIntent info isEmpty");
        } catch (Throwable th) {
            finish();
            th.printStackTrace();
        }
    }

    private void initRecyclerView() {
        this.rvMainContainer.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
    }

    private void initEngineRouter() {
        this.engineRouter = new DinamicXEngineRouter(new DXEngineConfig(PREVIEW_DINAMIC_MODULE));
        this.engineRouter.registerNotificationListener(this);
    }

    /* access modifiers changed from: private */
    public void showErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage((CharSequence) "获取mock数据失败，是否重试？").setTitle((CharSequence) "提示");
        builder.setPositiveButton((CharSequence) "重试", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                DXTemplatePreviewActivity.this.downLoadMockData(DXTemplatePreviewActivity.this.getIntent().getStringExtra(DXTemplatePreviewActivity.PREVIEW_INFO));
            }
        });
        builder.setNegativeButton((CharSequence) "返回", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                DXTemplatePreviewActivity.this.finish();
            }
        });
        builder.create().show();
    }

    /* access modifiers changed from: private */
    public void refreshUI(JSONArray jSONArray) {
        log("refreshUI");
        this.array = jSONArray;
        if (this.adapter == null) {
            this.adapter = new DXTemplatePreviewAdapter(this, jSONArray, this.rvMainContainer, this.engineRouter);
            this.rvMainContainer.setAdapter(this.adapter);
            return;
        }
        this.adapter.updateData(jSONArray);
    }

    /* access modifiers changed from: private */
    public void downLoadTemplate(JSONArray jSONArray) {
        boolean z;
        ArrayList arrayList = new ArrayList(jSONArray.size());
        Iterator<Object> it = jSONArray.iterator();
        loop0:
        while (true) {
            z = false;
            while (true) {
                if (!it.hasNext()) {
                    break loop0;
                }
                DXTemplateItem dinamicTemplate = getDinamicTemplate((JSONObject) it.next());
                if (dinamicTemplate != null) {
                    arrayList.add(dinamicTemplate);
                }
                if (!z) {
                    if (this.engineRouter.fetchTemplate(dinamicTemplate) != null) {
                        z = true;
                    }
                }
            }
        }
        log("开始下载模版");
        this.engineRouter.downLoadTemplates(arrayList);
        if (z) {
            log("模版已经存在，直接刷新");
            this.adapter.notifyDataSetChanged();
        }
    }

    /* access modifiers changed from: private */
    public void downLoadMockData(String str) {
        new AsyncTask<String, Void, JSONArray>() {
            /* access modifiers changed from: protected */
            public JSONArray doInBackground(String... strArr) {
                try {
                    byte[] download = new HttpDownloader().download(strArr[0]);
                    String str = download != null ? new String(download) : null;
                    if (str == null) {
                        return null;
                    }
                    DXTemplatePreviewActivity dXTemplatePreviewActivity = DXTemplatePreviewActivity.this;
                    dXTemplatePreviewActivity.log("respnese.body =" + str);
                    return JSON.parseArray(str);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(JSONArray jSONArray) {
                if (jSONArray == null || jSONArray.size() <= 0) {
                    DXTemplatePreviewActivity.this.showErrorDialog();
                    return;
                }
                DXTemplatePreviewActivity.this.log("获取mock数据成功");
                DXTemplatePreviewActivity.this.gotoImplPreviewInterface(jSONArray);
                DXTemplatePreviewActivity.this.refreshUI(jSONArray);
                DXTemplatePreviewActivity.this.downLoadTemplate(jSONArray);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{str});
    }

    public static DXTemplateItem getDinamicTemplate(JSONObject jSONObject) {
        DXTemplateItem dXTemplateItem = new DXTemplateItem();
        JSONObject jSONObject2 = jSONObject.getJSONObject("template");
        dXTemplateItem.name = jSONObject2.getString("name");
        dXTemplateItem.version = Long.parseLong(jSONObject2.getString("version"));
        dXTemplateItem.templateUrl = jSONObject2.getString("url");
        return dXTemplateItem;
    }

    public void onNotificationListener(DXNotificationResult dXNotificationResult) {
        if (dXNotificationResult != null) {
            log("收到刷新请求开始刷新");
            refreshUI(this.array);
            this.adapter.notifyDataSetChanged();
        }
    }

    /* access modifiers changed from: private */
    public void gotoImplPreviewInterface(JSONArray jSONArray) {
        log("开始进行组建注册");
        List<PreviewInfo> previewInfoList = getPreviewInfoList(jSONArray);
        if (previewInfoList != null) {
            for (int i = 0; i < previewInfoList.size(); i++) {
                callMethod(previewInfoList.get(i));
            }
        }
    }

    private void callMethod(PreviewInfo previewInfo) {
        if (previewInfo != null && !TextUtils.isEmpty(previewInfo.className)) {
            try {
                Class<?> cls = Class.forName(previewInfo.className);
                cls.getMethod("previewEngineDidInitialized", new Class[]{DinamicXEngineRouter.class}).invoke(cls.newInstance(), new Object[]{this.engineRouter});
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    private List<PreviewInfo> getPreviewInfoList(JSONArray jSONArray) {
        JSONArray jSONArray2;
        if (jSONArray == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < jSONArray.size(); i++) {
            JSONObject jSONObject = jSONArray.getJSONObject(i).getJSONObject("__preview__");
            if (!(jSONObject == null || (jSONArray2 = jSONObject.getJSONArray("android")) == null)) {
                for (int i2 = 0; i2 < jSONArray2.size(); i2++) {
                    JSONObject jSONObject2 = jSONArray2.getJSONObject(i2);
                    if (jSONObject2 != null) {
                        PreviewInfo previewInfo = new PreviewInfo();
                        previewInfo.className = jSONObject2.getString(PushClientConstants.TAG_CLASS_NAME);
                        previewInfo.bundlePath = jSONObject2.getString("bundlerPath");
                        arrayList.add(previewInfo);
                    }
                }
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        log("onDestroy");
        this.engineRouter.getEngine().onDestroy();
    }

    /* access modifiers changed from: private */
    public void log(String str) {
        Log.e(PREVIEW_TAG, str + " : " + System.currentTimeMillis());
    }
}
