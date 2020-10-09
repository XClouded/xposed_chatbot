package com.alimama.unwdinamicxcontainer;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.IInitAction;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import com.alimama.unwdinamicxcontainer.diywidget.DXUNWCouponViewWidgetNode;
import com.alimama.unwdinamicxcontainer.diywidget.DXUNWFinalPriceViewWidgetNode;
import com.alimama.unwdinamicxcontainer.diywidget.DXUNWPageTimerTipViewWidgetNode;
import com.alimama.unwdinamicxcontainer.diywidget.DXUNWProgressViewWidgetNode;
import com.alimama.unwdinamicxcontainer.diywidget.DXWNWDiscountTagViewWidgetNode;
import com.taobao.android.dinamicx.DXGlobalInitConfig;
import com.taobao.android.dinamicx.model.DXLongSparseArray;
import com.taobao.android.dinamicx.template.download.HttpDownloader;
import com.taobao.android.dxcontainer.AliDXContainer;
import com.taobao.android.dxcontainer.DXContainerGlobalInitConfig;
import com.taobao.android.dxcontainer.DXContainerRecyclerViewOption;
import com.taobao.android.dxcontainer.IDXContainerRecyclerViewInterface;
import com.taobao.uikit.feature.view.TRecyclerView;

public class UNWDinamicXContainerManager implements IInitAction {
    private Context mContext;

    public void init() {
        this.mContext = UNWManager.getInstance().application;
        DXLongSparseArray dXLongSparseArray = new DXLongSparseArray(6);
        dXLongSparseArray.put(DXWNWDiscountTagViewWidgetNode.DXWNWDISCOUNTTAGVIEW_WNWDISCOUNTTAGVIEW, new DXWNWDiscountTagViewWidgetNode.Builder());
        dXLongSparseArray.put(DXUNWCouponViewWidgetNode.DXUNWCOUPONVIEW_UNWCOUPONVIEW, new DXUNWCouponViewWidgetNode.Builder());
        dXLongSparseArray.put(DXUNWProgressViewWidgetNode.DXUNWPROGRESSVIEW_UNWPROGRESSVIEW, new DXUNWProgressViewWidgetNode.Builder());
        dXLongSparseArray.put(DXUNWPageTimerTipViewWidgetNode.DXUNWPAGETIMERTIPVIEW_UNWPAGETIMERTIPVIEW, new DXUNWPageTimerTipViewWidgetNode.Builder());
        dXLongSparseArray.put(DXUNWFinalPriceViewWidgetNode.DXUNWFINALPRICEVIEW_UNWFINALPRICEVIEW, new DXUNWFinalPriceViewWidgetNode.Builder());
        DXLongSparseArray dXLongSparseArray2 = new DXLongSparseArray(5);
        DXGlobalInitConfig.Builder builder = new DXGlobalInitConfig.Builder();
        builder.withDxWidgetMap(dXLongSparseArray);
        builder.withDxEventHandlerMap(dXLongSparseArray2);
        builder.withDxDownloader(new HttpDownloader());
        AliDXContainer.initWithAliDinamicX(this.mContext, new DXContainerGlobalInitConfig.Builder().withRecyclerViewBuilder(new IDXContainerRecyclerViewInterface() {
            public boolean setRecyclerViewAttr(RecyclerView recyclerView, DXContainerRecyclerViewOption dXContainerRecyclerViewOption) {
                return false;
            }

            public RecyclerView newRecyclerView(Context context, DXContainerRecyclerViewOption dXContainerRecyclerViewOption) {
                return new TRecyclerView(context);
            }
        }), builder, false);
    }
}
