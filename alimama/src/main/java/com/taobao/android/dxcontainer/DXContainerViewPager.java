package com.taobao.android.dxcontainer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Space;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import java.util.List;

@SuppressLint({"ViewConstructor"})
public class DXContainerViewPager extends ViewPager {
    public final String TAG = "TabPerfectViewPager";
    /* access modifiers changed from: private */
    public SparseArray<DXContainerSingleRVManager> engineHashMap = new SparseArray<>();
    private boolean isNewData = false;
    /* access modifiers changed from: private */
    public DXContainerEngine rootEngine;
    private TabAdapter tabAdapter;
    private ViewPager.OnPageChangeListener tabIndicator = null;
    /* access modifiers changed from: private */
    public DXContainerModel vpDXCModel;

    public void needRefresh(boolean z) {
        this.isNewData = z;
    }

    public DXContainerViewPager(Context context, DXContainerEngine dXContainerEngine) {
        super(context);
        this.rootEngine = dXContainerEngine;
        if (this.rootEngine != null) {
            setTabIndicator(this.rootEngine.getTabIndicator());
            ViewPager.OnPageChangeListener tabChangeListener = this.rootEngine.getTabChangeListener();
            if (tabChangeListener != null) {
                addOnPageChangeListener(tabChangeListener);
            }
            this.rootEngine.setTabViewPager(this);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x001d, code lost:
        r1 = r4.engineHashMap.valueAt(r0);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void resetState() {
        /*
            r4 = this;
            com.taobao.android.dxcontainer.DXContainerViewPager$TabAdapter r0 = r4.tabAdapter
            if (r0 == 0) goto L_0x0038
            com.taobao.android.dxcontainer.DXContainerViewPager$TabAdapter r0 = r4.tabAdapter
            int r0 = r0.getCount()
            if (r0 != 0) goto L_0x000d
            goto L_0x0038
        L_0x000d:
            r0 = 0
        L_0x000e:
            android.util.SparseArray<com.taobao.android.dxcontainer.DXContainerSingleRVManager> r1 = r4.engineHashMap
            int r1 = r1.size()
            if (r0 >= r1) goto L_0x0037
            int r1 = r4.getCurrentItem()
            if (r0 != r1) goto L_0x001d
            goto L_0x0034
        L_0x001d:
            android.util.SparseArray<com.taobao.android.dxcontainer.DXContainerSingleRVManager> r1 = r4.engineHashMap
            java.lang.Object r1 = r1.valueAt(r0)
            com.taobao.android.dxcontainer.DXContainerSingleRVManager r1 = (com.taobao.android.dxcontainer.DXContainerSingleRVManager) r1
            if (r1 != 0) goto L_0x0028
            goto L_0x0034
        L_0x0028:
            androidx.recyclerview.widget.RecyclerView r2 = r1.getContentView()
            com.taobao.android.dxcontainer.DXContainerViewPager$1 r3 = new com.taobao.android.dxcontainer.DXContainerViewPager$1
            r3.<init>(r1)
            r2.post(r3)
        L_0x0034:
            int r0 = r0 + 1
            goto L_0x000e
        L_0x0037:
            return
        L_0x0038:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dxcontainer.DXContainerViewPager.resetState():void");
    }

    public void setTabIndicator(ViewPager.OnPageChangeListener onPageChangeListener) {
        if (this.tabIndicator != onPageChangeListener && onPageChangeListener != null) {
            if (this.tabIndicator != null) {
                removeOnPageChangeListener(this.tabIndicator);
            }
            addOnPageChangeListener(onPageChangeListener);
            this.tabIndicator = onPageChangeListener;
        }
    }

    public void bindData(DXContainerModel dXContainerModel) {
        if (this.vpDXCModel != dXContainerModel) {
            this.vpDXCModel = dXContainerModel;
            if (this.tabAdapter != null) {
                this.tabAdapter.notifyDataSetChanged();
            }
        }
        if (this.tabAdapter == null) {
            this.tabAdapter = new TabAdapter();
            setAdapter(this.tabAdapter);
            setCurrentItem(this.rootEngine.getDefaultSelectedTab(), false);
        }
        if (this.isNewData) {
            this.isNewData = false;
            if (this.tabAdapter != null) {
                this.tabAdapter.notifyDataSetChanged();
                setCurrentItem(this.rootEngine.getDefaultSelectedTab(), false);
                updateEachEngineData();
            }
        }
    }

    private void updateEachEngineData() {
        DXContainerSingleRVManager valueAt;
        List<DXContainerModel> children = this.vpDXCModel.getChildren();
        if (children != null) {
            for (int i = 0; i < children.size(); i++) {
                if (i < this.engineHashMap.size() && (valueAt = this.engineHashMap.valueAt(i)) != null) {
                    valueAt.initData(children.get(i));
                }
            }
        }
    }

    public class TabAdapter extends PagerAdapter {
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        public TabAdapter() {
        }

        public int getItemPosition(Object obj) {
            Object tag = ((View) obj).getTag(R.id.dxc_viewpager_index);
            if (tag != null && (tag instanceof Integer) && ((Integer) tag).intValue() < getCount()) {
                return -1;
            }
            return -2;
        }

        public int getCount() {
            if (DXContainerViewPager.this.vpDXCModel == null || DXContainerViewPager.this.vpDXCModel.getChildren() == null) {
                return 0;
            }
            return DXContainerViewPager.this.vpDXCModel.getChildren().size();
        }

        public Object instantiateItem(ViewGroup viewGroup, int i) {
            if (DXContainerViewPager.this.rootEngine == null) {
                return new Space(viewGroup.getContext());
            }
            DXContainerSingleRVManager dXContainerSingleRVManager = (DXContainerSingleRVManager) DXContainerViewPager.this.engineHashMap.get(i);
            if (dXContainerSingleRVManager == null) {
                dXContainerSingleRVManager = DXContainerViewPager.this.rootEngine.getTabManager().getDXNContainerSingleCManagerByViewPage(i);
                DXContainerViewPager.this.engineHashMap.put(i, dXContainerSingleRVManager);
                if (i == DXContainerViewPager.this.getCurrentItem() && DXContainerViewPager.this.rootEngine.getContainerWrapper() != null) {
                    DXContainerViewPager.this.rootEngine.getContainerWrapper().setCurrentChild(dXContainerSingleRVManager.getContentView());
                }
            }
            RecyclerView contentView = dXContainerSingleRVManager.getContentView();
            contentView.setTag(R.id.dxc_viewpager_index, Integer.valueOf(i));
            if (contentView.getParent() != null) {
                ((ViewGroup) contentView.getParent()).removeView(contentView);
            }
            dXContainerSingleRVManager.initData(DXContainerViewPager.this.vpDXCModel.getChildren().get(i));
            viewGroup.addView(contentView);
            return contentView;
        }

        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            viewGroup.removeView((RecyclerView) obj);
        }
    }

    public ViewGroup getCurrentPage(int i) {
        DXContainerSingleRVManager dXContainerSingleRVManager = this.engineHashMap.get(i);
        if (dXContainerSingleRVManager != null) {
            return dXContainerSingleRVManager.getContentView();
        }
        return null;
    }
}
