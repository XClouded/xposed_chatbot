package com.alibaba.aliweex.adapter.component;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.alibaba.aliweex.adapter.view.Elevator;
import com.alibaba.aliweex.adapter.view.ElevatorItem;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.utils.WXJsonUtils;
import com.taobao.weex.utils.WXLogUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WXTabHeader extends WXVContainer {
    private WXVContainer mContainer;
    private boolean mDataChanged;
    public Elevator mElevator;

    public WXTabHeader(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
        this.mContainer = wXVContainer;
    }

    /* access modifiers changed from: protected */
    public View initComponentHostView(@NonNull Context context) {
        this.mElevator = new Elevator(context);
        this.mElevator.setIWATabHeaderChanged(new Elevator.IWATabHeaderChanged() {
            public void changed() {
                WXTabHeader.this.invalidate();
            }
        });
        return this.mElevator.getRootView();
    }

    /* access modifiers changed from: private */
    public void invalidate() {
        if (this.mContainer != null && this.mContainer.getHostView() != null) {
            ViewGroup viewGroup = (ViewGroup) this.mContainer.getHostView();
            viewGroup.invalidate(getHostView().getLeft(), getHostView().getTop(), getHostView().getRight(), viewGroup.getScrollY() + getHostView().getHeight());
        }
    }

    @WXComponentProp(name = "textColor")
    public void setTextColor(String str) {
        if (!TextUtils.isEmpty(str) && str.startsWith("#")) {
            this.mElevator.setNormalColor(str);
        }
    }

    @WXComponentProp(name = "textHighlightColor")
    public void setTextHighlightColor(String str) {
        if (!TextUtils.isEmpty(str) && str.startsWith("#")) {
            this.mElevator.setSelectedColor(str);
        }
    }

    @WXComponentProp(name = "data")
    public void setData(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mDataChanged = true;
            refreshList(str);
        }
    }

    private void refreshList(String str) {
        if (this.mElevator != null) {
            this.mElevator.setTextHeight((int) getLayoutHeight());
            try {
                List<String> list = WXJsonUtils.getList(str, String.class);
                if (list.size() > 0) {
                    ArrayList arrayList = new ArrayList();
                    for (String elevatorItem : list) {
                        arrayList.add(new ElevatorItem(elevatorItem));
                    }
                    this.mElevator.setList(arrayList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addEvent(String str) {
        super.addEvent(str);
        if (getEvents() != null && getEvents().contains("select")) {
            getEvents().add(str);
            this.mElevator.setElevatorOnClickListener(new Elevator.ElevatorOnClicklistener() {
                public void OnClick(ElevatorItem elevatorItem) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("index", elevatorItem.getId() + "");
                    WXLogUtils.d("updateAttrs", "click:" + elevatorItem.getId());
                    WXTabHeader.this.getInstance().fireEvent(WXTabHeader.this.getRef(), "select", hashMap);
                }
            });
        }
    }

    @WXComponentProp(name = "backgroundColor")
    public void setBackgroundColor(String str) {
        if (!TextUtils.isEmpty(str) && str.startsWith("#")) {
            this.mElevator.setBackgroundColor(str);
        }
    }

    @WXComponentProp(name = "selectedIndex")
    public void setSelectedIndex(String str) {
        if (str != null && TextUtils.isDigitsOnly(str.trim())) {
            int parseInt = Integer.parseInt(str.trim());
            if (parseInt >= 0) {
                this.mElevator.setLocation(parseInt);
            }
            WXLogUtils.d("updateAttrs", "" + parseInt);
        }
    }

    /* access modifiers changed from: protected */
    public void onFinishLayout() {
        super.bindData(this);
        this.mElevator.reBindImage();
    }

    public void bindData(WXComponent wXComponent) {
        super.bindData(wXComponent);
        this.mElevator.reBindImage();
    }
}
