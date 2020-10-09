package com.taobao.android.preview;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.dinamicx.DXResult;
import com.taobao.android.dinamicx.DXRootView;
import com.taobao.android.dinamicx.DinamicXEngineRouter;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import java.util.HashMap;

public class DXTemplatePreviewAdapter extends RecyclerView.Adapter<PreviewViewHolder> {
    private final int EMPTY_VIEW_TYPE = -1;
    Context context;
    DinamicXEngineRouter engineRouter;
    private JSONArray jsonArray = new JSONArray();
    private HashMap<Integer, Integer> position2Type = new HashMap<>();
    private RecyclerView recyclerView;
    private HashMap<String, Integer> template2Type = new HashMap<>(128);
    private HashMap<Integer, DXTemplateItem> type2DinamicTemplate = new HashMap<>(128);
    private int viewTypeCounter = 0;

    public DXTemplatePreviewAdapter(Context context2, JSONArray jSONArray, RecyclerView recyclerView2, DinamicXEngineRouter dinamicXEngineRouter) {
        this.jsonArray.addAll(jSONArray);
        this.engineRouter = dinamicXEngineRouter;
        this.recyclerView = recyclerView2;
        this.context = context2;
        buildViewTypes();
    }

    public void updateData(JSONArray jSONArray) {
        if (this.jsonArray != null) {
            this.jsonArray.clear();
            this.jsonArray.addAll(jSONArray);
        } else {
            this.jsonArray = new JSONArray();
            this.jsonArray.addAll(jSONArray);
        }
        buildViewTypes();
    }

    private void buildViewTypes() {
        for (int i = 0; i < this.jsonArray.size(); i++) {
            DXTemplateItem dXTemplateItem = new DXTemplateItem();
            JSONObject jSONObject = (JSONObject) this.jsonArray.getJSONObject(i).get("template");
            dXTemplateItem.version = Long.parseLong(jSONObject.getString("version"));
            dXTemplateItem.name = jSONObject.getString("name");
            dXTemplateItem.templateUrl = jSONObject.getString("url");
            String identifier = dXTemplateItem.getIdentifier();
            if (this.template2Type.containsKey(identifier)) {
                this.position2Type.put(Integer.valueOf(i), this.template2Type.get(identifier));
            } else {
                DXTemplateItem fetchTemplate = this.engineRouter.fetchTemplate(dXTemplateItem);
                if (fetchTemplate == null) {
                    this.position2Type.put(Integer.valueOf(i), -1);
                } else {
                    String identifier2 = fetchTemplate.getIdentifier();
                    if (this.template2Type.containsKey(identifier2)) {
                        this.position2Type.put(Integer.valueOf(i), this.template2Type.get(identifier2));
                    } else {
                        this.viewTypeCounter++;
                        this.template2Type.put(identifier2, Integer.valueOf(this.viewTypeCounter));
                        this.type2DinamicTemplate.put(Integer.valueOf(this.viewTypeCounter), fetchTemplate);
                        this.position2Type.put(Integer.valueOf(i), Integer.valueOf(this.viewTypeCounter));
                    }
                }
            }
        }
    }

    public int getItemViewType(int i) {
        return this.position2Type.get(Integer.valueOf(i)).intValue();
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x004a  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x006d  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0078  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.taobao.android.preview.PreviewViewHolder onCreateViewHolder(android.view.ViewGroup r5, int r6) {
        /*
            r4 = this;
            r0 = 0
            r1 = -1
            if (r6 != r1) goto L_0x000e
            android.content.Context r6 = r5.getContext()
            android.view.View r6 = getEmptyView(r6)
            r1 = r6
            goto L_0x0048
        L_0x000e:
            java.util.HashMap<java.lang.Integer, com.taobao.android.dinamicx.template.download.DXTemplateItem> r1 = r4.type2DinamicTemplate
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)
            java.lang.Object r6 = r1.get(r6)
            com.taobao.android.dinamicx.template.download.DXTemplateItem r6 = (com.taobao.android.dinamicx.template.download.DXTemplateItem) r6
            if (r6 == 0) goto L_0x0047
            com.taobao.android.dinamicx.DinamicXEngineRouter r1 = r4.engineRouter     // Catch:{ Exception -> 0x003d }
            android.content.Context r2 = r4.context     // Catch:{ Exception -> 0x003d }
            com.taobao.android.dinamicx.DXResult r1 = r1.createView(r2, r5, r6)     // Catch:{ Exception -> 0x003d }
            if (r1 == 0) goto L_0x0047
            T r2 = r1.result     // Catch:{ Exception -> 0x003d }
            if (r2 == 0) goto L_0x0047
            T r1 = r1.result     // Catch:{ Exception -> 0x003d }
            android.view.View r1 = (android.view.View) r1     // Catch:{ Exception -> 0x003d }
            boolean r6 = r4.isV3Template(r6)     // Catch:{ Exception -> 0x003b }
            if (r6 == 0) goto L_0x0048
            r6 = r1
            com.taobao.android.dinamicx.DXRootView r6 = (com.taobao.android.dinamicx.DXRootView) r6     // Catch:{ Exception -> 0x003b }
            r4.registerDXLifeCycle(r6)     // Catch:{ Exception -> 0x003b }
            goto L_0x0048
        L_0x003b:
            r6 = move-exception
            goto L_0x003f
        L_0x003d:
            r6 = move-exception
            r1 = r0
        L_0x003f:
            java.lang.String r2 = "DXTemplatePreviewActivity"
            java.lang.String r3 = "createViewHolder failed"
            android.util.Log.e(r2, r3, r6)
            goto L_0x0048
        L_0x0047:
            r1 = r0
        L_0x0048:
            if (r1 != 0) goto L_0x0060
            android.content.Context r6 = r5.getContext()
            android.view.View r1 = getEmptyView(r6)
            android.content.Context r5 = r5.getContext()
            java.lang.String r6 = "Preview template failed"
            r2 = 0
            android.widget.Toast r5 = android.widget.Toast.makeText(r5, r6, r2)
            r5.show()
        L_0x0060:
            com.taobao.android.preview.PreviewViewHolder r5 = new com.taobao.android.preview.PreviewViewHolder
            r5.<init>(r1, r0)
            android.view.View r6 = r5.itemView
            android.view.ViewGroup$LayoutParams r6 = r6.getLayoutParams()
            if (r6 == 0) goto L_0x0078
            androidx.recyclerview.widget.RecyclerView r0 = r4.recyclerView
            androidx.recyclerview.widget.RecyclerView$LayoutManager r0 = r0.getLayoutManager()
            androidx.recyclerview.widget.RecyclerView$LayoutParams r6 = r0.generateLayoutParams(r6)
            goto L_0x0082
        L_0x0078:
            androidx.recyclerview.widget.RecyclerView r6 = r4.recyclerView
            androidx.recyclerview.widget.RecyclerView$LayoutManager r6 = r6.getLayoutManager()
            androidx.recyclerview.widget.RecyclerView$LayoutParams r6 = r6.generateDefaultLayoutParams()
        L_0x0082:
            android.view.View r0 = r5.itemView
            r0.setLayoutParams(r6)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.preview.DXTemplatePreviewAdapter.onCreateViewHolder(android.view.ViewGroup, int):com.taobao.android.preview.PreviewViewHolder");
    }

    private boolean isV3Template(DXTemplateItem dXTemplateItem) {
        if (dXTemplateItem == null) {
            return false;
        }
        if (dXTemplateItem.getFileVersion() == 30000) {
            return true;
        }
        if (dXTemplateItem.getFileVersion() == 20000) {
            return false;
        }
        if (TextUtils.isEmpty(dXTemplateItem.templateUrl) || !dXTemplateItem.templateUrl.endsWith(".zip")) {
            return TextUtils.isEmpty(dXTemplateItem.templateUrl) && dXTemplateItem.version >= 0;
        }
        return true;
    }

    private void registerDXLifeCycle(final DXRootView dXRootView) {
        this.engineRouter.getEngine().registerDXRootViewLifeCycle(dXRootView, new DXRootView.DXRootViewLifeCycle() {
            /* access modifiers changed from: protected */
            public void onWindowVisibilityChanged(DXRootView dXRootView, int i) {
                if (i == 0) {
                    DXTemplatePreviewAdapter.this.engineRouter.getEngine().onRootViewAppear(dXRootView);
                } else {
                    DXTemplatePreviewAdapter.this.engineRouter.getEngine().onRootViewDisappear(dXRootView);
                }
            }

            /* access modifiers changed from: protected */
            public void onVisibilityChanged(@NonNull View view, int i) {
                if (i == 0) {
                    DXTemplatePreviewAdapter.this.engineRouter.getEngine().onRootViewAppear(dXRootView);
                } else {
                    DXTemplatePreviewAdapter.this.engineRouter.getEngine().onRootViewDisappear(dXRootView);
                }
            }

            /* access modifiers changed from: protected */
            public void onDetachedFromWindow(DXRootView dXRootView) {
                DXTemplatePreviewAdapter.this.engineRouter.getEngine().onRootViewDisappear(dXRootView);
            }

            /* access modifiers changed from: protected */
            public void onAttachedToWindow(DXRootView dXRootView) {
                DXTemplatePreviewAdapter.this.engineRouter.getEngine().onRootViewAppear(dXRootView);
            }
        });
    }

    public void onBindViewHolder(PreviewViewHolder previewViewHolder, int i) {
        if (getItemViewType(i) != -1) {
            try {
                DXResult<DXRootView> renderTemplate = this.engineRouter.renderTemplate(this.context, (JSONObject) this.jsonArray.get(i), (DXRootView) previewViewHolder.itemView, 0, 0, (Object) null);
                if (renderTemplate != null && renderTemplate.hasError()) {
                    Log.e("DinamicX", renderTemplate.getDxError().dxErrorInfoList.toString());
                }
            } catch (Exception e) {
                Log.e(DXTemplatePreviewActivity.PREVIEW_DINAMIC_MODULE, "bind failed", e);
            }
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) previewViewHolder.itemView.getLayoutParams();
            String string = this.jsonArray.getJSONObject(i).getJSONObject("template").getString("columnType");
            layoutParams.setFullSpan(TextUtils.equals(string, "one") || TextUtils.isEmpty(string));
        }
    }

    public int getItemCount() {
        if (this.jsonArray != null) {
            return this.jsonArray.size();
        }
        return 0;
    }

    public static View getEmptyView(Context context2) {
        FrameLayout frameLayout = new FrameLayout(context2);
        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(-1, 0));
        return frameLayout;
    }
}
