package com.taobao.weex.analyzer.core.inspector.network;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.taobao.weex.analyzer.Config;
import com.taobao.weex.analyzer.R;
import com.taobao.weex.analyzer.core.inspector.network.NetworkEventInspector;
import com.taobao.weex.analyzer.view.overlay.AbstractResizableOverlayView;
import com.taobao.weex.analyzer.view.overlay.IOverlayView;
import java.util.Map;

public class NetworkInspectorView extends AbstractResizableOverlayView {
    private static final int COLOR_DISABLED = -1912602624;
    private static final int COLOR_ENABLED = -1127359431;
    /* access modifiers changed from: private */
    public boolean isSizeMenuOpened;
    /* access modifiers changed from: private */
    public int mCurPanel = 0;
    private NetworkEventInspector mNetworkEventInspector;
    /* access modifiers changed from: private */
    public IOverlayView.OnCloseListener mOnCloseListener;
    /* access modifiers changed from: private */
    public DisplayNetworkEventItemView mPanelHttp;
    /* access modifiers changed from: private */
    public DisplayNetworkEventItemView mPanelImage;
    /* access modifiers changed from: private */
    public DisplayNetworkEventItemView mPanelMtop;
    /* access modifiers changed from: private */
    public TextView mTabHttp;
    /* access modifiers changed from: private */
    public TextView mTabImage;
    /* access modifiers changed from: private */
    public TextView mTabMtop;

    public NetworkInspectorView(Context context, Config config) {
        super(context, config);
        this.mWidth = -1;
    }

    public void setOnCloseListener(@Nullable IOverlayView.OnCloseListener onCloseListener) {
        this.mOnCloseListener = onCloseListener;
    }

    /* access modifiers changed from: protected */
    @NonNull
    public View onCreateView() {
        View inflate = View.inflate(this.mContext, R.layout.wxt_network_inspector_view, (ViewGroup) null);
        inflate.findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (NetworkInspectorView.this.isViewAttached) {
                    switch (NetworkInspectorView.this.mCurPanel) {
                        case 0:
                            if (NetworkInspectorView.this.mPanelMtop.getListAdapter() != null) {
                                NetworkInspectorView.this.mPanelMtop.getListAdapter().clear();
                                return;
                            }
                            return;
                        case 1:
                            if (NetworkInspectorView.this.mPanelHttp.getListAdapter() != null) {
                                NetworkInspectorView.this.mPanelHttp.getListAdapter().clear();
                                return;
                            }
                            return;
                        case 2:
                            if (NetworkInspectorView.this.mPanelImage.getListAdapter() != null) {
                                NetworkInspectorView.this.mPanelImage.getListAdapter().clear();
                                return;
                            }
                            return;
                        default:
                            return;
                    }
                }
            }
        });
        final View findViewById = inflate.findViewById(R.id.hold);
        findViewById.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (NetworkInspectorView.this.isViewAttached) {
                    if (NetworkInspectorView.this.mPanelMtop.getListAdapter().isHoldModeEnabled()) {
                        NetworkInspectorView.this.mPanelMtop.getListAdapter().setHoldModeEnabled(false);
                        ((TextView) findViewById).setText("hold(off)");
                    } else {
                        NetworkInspectorView.this.mPanelMtop.getListAdapter().setHoldModeEnabled(true);
                        ((TextView) findViewById).setText("hold(on)");
                    }
                    if (NetworkInspectorView.this.mPanelImage.getListAdapter().isHoldModeEnabled()) {
                        NetworkInspectorView.this.mPanelImage.getListAdapter().setHoldModeEnabled(false);
                    } else {
                        NetworkInspectorView.this.mPanelImage.getListAdapter().setHoldModeEnabled(true);
                    }
                    if (NetworkInspectorView.this.mPanelHttp.getListAdapter().isHoldModeEnabled()) {
                        NetworkInspectorView.this.mPanelHttp.getListAdapter().setHoldModeEnabled(false);
                    } else {
                        NetworkInspectorView.this.mPanelHttp.getListAdapter().setHoldModeEnabled(true);
                    }
                }
            }
        });
        inflate.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (NetworkInspectorView.this.isViewAttached && NetworkInspectorView.this.mOnCloseListener != null) {
                    NetworkInspectorView.this.mOnCloseListener.close(NetworkInspectorView.this);
                    NetworkInspectorView.this.dismiss();
                }
            }
        });
        this.mTabMtop = (TextView) inflate.findViewById(R.id.tab_mtop);
        this.mTabHttp = (TextView) inflate.findViewById(R.id.tab_http);
        this.mTabImage = (TextView) inflate.findViewById(R.id.tab_image);
        this.mPanelMtop = (DisplayNetworkEventItemView) inflate.findViewById(R.id.panel_mtop);
        this.mPanelHttp = (DisplayNetworkEventItemView) inflate.findViewById(R.id.panel_http);
        this.mPanelImage = (DisplayNetworkEventItemView) inflate.findViewById(R.id.panel_image);
        this.mTabMtop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                NetworkInspectorView.this.mTabMtop.setBackgroundColor(NetworkInspectorView.COLOR_ENABLED);
                NetworkInspectorView.this.mTabHttp.setBackgroundColor(NetworkInspectorView.COLOR_DISABLED);
                NetworkInspectorView.this.mTabImage.setBackgroundColor(NetworkInspectorView.COLOR_DISABLED);
                NetworkInspectorView.this.mPanelMtop.setVisibility(0);
                NetworkInspectorView.this.mPanelHttp.setVisibility(4);
                NetworkInspectorView.this.mPanelImage.setVisibility(4);
                int unused = NetworkInspectorView.this.mCurPanel = 0;
            }
        });
        this.mTabImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                NetworkInspectorView.this.mTabImage.setBackgroundColor(NetworkInspectorView.COLOR_ENABLED);
                NetworkInspectorView.this.mTabMtop.setBackgroundColor(NetworkInspectorView.COLOR_DISABLED);
                NetworkInspectorView.this.mTabHttp.setBackgroundColor(NetworkInspectorView.COLOR_DISABLED);
                NetworkInspectorView.this.mPanelImage.setVisibility(0);
                NetworkInspectorView.this.mPanelMtop.setVisibility(4);
                NetworkInspectorView.this.mPanelHttp.setVisibility(4);
                int unused = NetworkInspectorView.this.mCurPanel = 2;
            }
        });
        this.mTabHttp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                NetworkInspectorView.this.mTabHttp.setBackgroundColor(NetworkInspectorView.COLOR_ENABLED);
                NetworkInspectorView.this.mTabMtop.setBackgroundColor(NetworkInspectorView.COLOR_DISABLED);
                NetworkInspectorView.this.mTabImage.setBackgroundColor(NetworkInspectorView.COLOR_DISABLED);
                NetworkInspectorView.this.mPanelHttp.setVisibility(0);
                NetworkInspectorView.this.mPanelMtop.setVisibility(4);
                NetworkInspectorView.this.mPanelImage.setVisibility(4);
                int unused = NetworkInspectorView.this.mCurPanel = 1;
            }
        });
        final ViewGroup viewGroup = (ViewGroup) inflate.findViewById(R.id.size_content);
        RadioGroup radioGroup = (RadioGroup) inflate.findViewById(R.id.height_group);
        ((TextView) inflate.findViewById(R.id.size)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                boolean unused = NetworkInspectorView.this.isSizeMenuOpened = !NetworkInspectorView.this.isSizeMenuOpened;
                if (NetworkInspectorView.this.isSizeMenuOpened) {
                    viewGroup.setVisibility(0);
                } else {
                    viewGroup.setVisibility(8);
                }
            }
        });
        setViewSize(this.mViewSize, this.mPanelHttp.getContentView(), false);
        setViewSize(this.mViewSize, this.mPanelImage.getContentView(), false);
        setViewSize(this.mViewSize, this.mPanelMtop.getContentView(), false);
        switch (this.mViewSize) {
            case 0:
                ((RadioButton) inflate.findViewById(R.id.height_small)).setChecked(true);
                break;
            case 1:
                ((RadioButton) inflate.findViewById(R.id.height_medium)).setChecked(true);
                break;
            case 2:
                ((RadioButton) inflate.findViewById(R.id.height_large)).setChecked(true);
                break;
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.height_small) {
                    int unused = NetworkInspectorView.this.mViewSize = 0;
                } else if (i == R.id.height_medium) {
                    int unused2 = NetworkInspectorView.this.mViewSize = 1;
                } else if (i == R.id.height_large) {
                    int unused3 = NetworkInspectorView.this.mViewSize = 2;
                }
                NetworkInspectorView.this.setViewSize(NetworkInspectorView.this.mViewSize, NetworkInspectorView.this.mPanelHttp.getContentView(), true);
                NetworkInspectorView.this.setViewSize(NetworkInspectorView.this.mViewSize, NetworkInspectorView.this.mPanelImage.getContentView(), true);
                NetworkInspectorView.this.setViewSize(NetworkInspectorView.this.mViewSize, NetworkInspectorView.this.mPanelMtop.getContentView(), true);
            }
        });
        return inflate;
    }

    /* access modifiers changed from: protected */
    public void onShown() {
        this.mNetworkEventInspector = NetworkEventInspector.createInstance(this.mContext, (NetworkEventInspector.OnMessageReceivedListener) new NetworkEventInspector.OnMessageReceivedListener() {
            public void onMessageReceived(NetworkEventInspector.MessageBean messageBean) {
                if (messageBean != null) {
                    Map<String, String> map = messageBean.extendProps;
                    if (map != null) {
                        if ("mtop".equalsIgnoreCase(map.get("bizType"))) {
                            if (NetworkInspectorView.this.mPanelMtop.getListAdapter() != null) {
                                NetworkInspectorView.this.mPanelMtop.getListAdapter().addMessage(messageBean);
                            }
                        } else if ("http".equalsIgnoreCase(map.get("bizType"))) {
                            if (NetworkInspectorView.this.mPanelHttp.getListAdapter() != null) {
                                NetworkInspectorView.this.mPanelHttp.getListAdapter().addMessage(messageBean);
                            }
                        } else if ("image".equalsIgnoreCase(map.get("bizType")) && NetworkInspectorView.this.mPanelImage.getListAdapter() != null) {
                            NetworkInspectorView.this.mPanelImage.getListAdapter().addMessage(messageBean);
                        }
                    } else if (NetworkInspectorView.this.mPanelMtop.getListAdapter() != null) {
                        NetworkInspectorView.this.mPanelMtop.getListAdapter().addMessage(messageBean);
                    }
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onDismiss() {
        if (this.mNetworkEventInspector != null) {
            this.mNetworkEventInspector.destroy();
            this.mNetworkEventInspector = null;
        }
    }

    public boolean isPermissionGranted(@NonNull Config config) {
        return !config.getIgnoreOptions().contains(Config.TYPE_MTOP_INSPECTOR);
    }
}
