package com.taobao.weex.analyzer.core.logcat.ats;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.taobao.weex.analyzer.Config;
import com.taobao.weex.analyzer.R;
import com.taobao.weex.analyzer.core.logcat.ats.ATSMessageReceiver;
import com.taobao.weex.analyzer.core.logcat.ats.UploadManager;
import com.taobao.weex.analyzer.utils.DeviceUtils;
import com.taobao.weex.analyzer.utils.SDKUtils;
import com.taobao.weex.analyzer.view.overlay.IOverlayView;
import com.taobao.weex.analyzer.view.overlay.PermissionOverlayView;
import java.util.Locale;

public class LogUploadOverlayView extends PermissionOverlayView implements View.OnClickListener, UploadManager.Callback, ATSMessageReceiver.OnReceiveATSMsgListener {
    private boolean hasUploadLog = false;
    private boolean isConnected = false;
    private boolean isUploading = false;
    private ATSMessageReceiver mATSMessageReceiver;
    /* access modifiers changed from: private */
    public TextView mConsoleView;
    /* access modifiers changed from: private */
    public IOverlayView.OnCloseListener mOnCloseListener;
    private TextView mResultView;

    public boolean isPermissionGranted(@NonNull Config config) {
        return true;
    }

    public LogUploadOverlayView(Context context) {
        super(context);
        this.mWidth = -1;
    }

    public void setOnCloseListener(@Nullable IOverlayView.OnCloseListener onCloseListener) {
        this.mOnCloseListener = onCloseListener;
    }

    /* access modifiers changed from: protected */
    @NonNull
    public View onCreateView() {
        View inflate = View.inflate(this.mContext, R.layout.wxt_log_upload_view, (ViewGroup) null);
        View findViewById = inflate.findViewById(R.id.close);
        this.mConsoleView = (TextView) inflate.findViewById(R.id.console);
        this.mResultView = (TextView) inflate.findViewById(R.id.result);
        findViewById.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (LogUploadOverlayView.this.isViewAttached && LogUploadOverlayView.this.mOnCloseListener != null) {
                    LogUploadOverlayView.this.mOnCloseListener.close(LogUploadOverlayView.this);
                    LogUploadOverlayView.this.dismiss();
                    ATSUploadService.stop(view.getContext());
                }
            }
        });
        inflate.findViewById(R.id.start).setOnClickListener(this);
        inflate.findViewById(R.id.end).setOnClickListener(this);
        return inflate;
    }

    /* access modifiers changed from: protected */
    public void onShown() {
        this.mATSMessageReceiver = ATSMessageReceiver.createInstance(this.mContext, this);
        ATSUploadService.syncState(this.mContext);
    }

    /* access modifiers changed from: protected */
    public void onDismiss() {
        if (this.mATSMessageReceiver != null) {
            this.mATSMessageReceiver.destroy();
        }
        this.isConnected = false;
        this.isUploading = false;
        this.hasUploadLog = false;
    }

    public void onClick(View view) {
        if (view.getId() == R.id.start) {
            performStart(view.getContext());
        } else if (view.getId() == R.id.end) {
            performStop(view.getContext());
        }
    }

    private void performStart(Context context) {
        if (!this.isUploading || !this.isConnected) {
            ATSUploadService.startAndThenUpload(context, getAddress());
        } else {
            Toast.makeText(context, "日志正在上传，请不要重复操作!", 0).show();
        }
    }

    private void performStop(Context context) {
        if (!this.isConnected) {
            Toast.makeText(context, "连接尚未建立...", 0).show();
        } else if (!this.hasUploadLog) {
            Toast.makeText(context, "还未上传任何日志，建议继续采集日志...", 0).show();
        } else {
            if (this.isViewAttached) {
                this.mConsoleView.setText("日志已停止上传，等待服务端处理，请稍后...");
            }
            ATSUploadService.stopUpload(context);
        }
    }

    private String getAddress() {
        return String.format(Locale.getDefault(), "http://pre-plover.alibaba-inc.com/websocket/%s", new Object[]{generateUniqueToken()});
    }

    private String generateUniqueToken() {
        String str;
        if (this.mContext != null) {
            str = DeviceUtils.getDeviceId(this.mContext);
            if (str.length() > 4) {
                str = str.substring(0, 4);
            }
        } else {
            str = null;
        }
        String valueOf = String.valueOf(System.currentTimeMillis());
        if (str == null) {
            return valueOf;
        }
        return str + valueOf;
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onMessageReceived(com.taobao.weex.analyzer.core.logcat.ats.ATSMessageReceiver.ATSMessage r5) {
        /*
            r4 = this;
            if (r5 != 0) goto L_0x0003
            return
        L_0x0003:
            java.lang.String r0 = r5.state
            java.lang.String r1 = r5.message
            int r2 = r0.hashCode()
            r3 = -1
            switch(r2) {
                case -675214621: goto L_0x0056;
                case -242747386: goto L_0x004c;
                case 3417674: goto L_0x0042;
                case 94756344: goto L_0x0038;
                case 96784904: goto L_0x002e;
                case 234862790: goto L_0x0024;
                case 1008015708: goto L_0x001a;
                case 1031958570: goto L_0x0010;
                default: goto L_0x000f;
            }
        L_0x000f:
            goto L_0x0060
        L_0x0010:
            java.lang.String r2 = "before_connect"
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x0060
            r0 = 0
            goto L_0x0061
        L_0x001a:
            java.lang.String r2 = "before_disconnect"
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x0060
            r0 = 1
            goto L_0x0061
        L_0x0024:
            java.lang.String r2 = "before_upload_log"
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x0060
            r0 = 3
            goto L_0x0061
        L_0x002e:
            java.lang.String r2 = "error"
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x0060
            r0 = 7
            goto L_0x0061
        L_0x0038:
            java.lang.String r2 = "close"
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x0060
            r0 = 6
            goto L_0x0061
        L_0x0042:
            java.lang.String r2 = "open"
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x0060
            r0 = 2
            goto L_0x0061
        L_0x004c:
            java.lang.String r2 = "upload_log"
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x0060
            r0 = 4
            goto L_0x0061
        L_0x0056:
            java.lang.String r2 = "receive_oss_url"
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x0060
            r0 = 5
            goto L_0x0061
        L_0x0060:
            r0 = -1
        L_0x0061:
            switch(r0) {
                case 0: goto L_0x0085;
                case 1: goto L_0x0081;
                case 2: goto L_0x007d;
                case 3: goto L_0x0079;
                case 4: goto L_0x0071;
                case 5: goto L_0x006d;
                case 6: goto L_0x0069;
                case 7: goto L_0x0065;
                default: goto L_0x0064;
            }
        L_0x0064:
            goto L_0x0088
        L_0x0065:
            r4.onError(r1)
            goto L_0x0088
        L_0x0069:
            r4.onClose(r3, r1)
            goto L_0x0088
        L_0x006d:
            r4.onReceivedOSSUrl(r1)
            goto L_0x0088
        L_0x0071:
            int r0 = r5.count
            java.lang.String r5 = r5.message
            r4.onUploadLog(r0, r5)
            goto L_0x0088
        L_0x0079:
            r4.onBeforeUpload()
            goto L_0x0088
        L_0x007d:
            r4.onOpen()
            goto L_0x0088
        L_0x0081:
            r4.onBeforeDisconnect()
            goto L_0x0088
        L_0x0085:
            r4.onBeforeConnect()
        L_0x0088:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.analyzer.core.logcat.ats.LogUploadOverlayView.onMessageReceived(com.taobao.weex.analyzer.core.logcat.ats.ATSMessageReceiver$ATSMessage):void");
    }

    public void onBeforeConnect() {
        if (this.isViewAttached) {
            this.mConsoleView.setText("准备建立连接...");
            this.mResultView.setText("");
        }
    }

    public void onBeforeDisconnect() {
        if (this.isViewAttached) {
            this.mConsoleView.setText("连接正在断开...");
        }
    }

    public void onOpen() {
        this.isConnected = true;
        this.isUploading = false;
        this.hasUploadLog = false;
        if (this.isViewAttached) {
            this.mConsoleView.setText("连接已建立成功，准备上传日志");
        }
    }

    public void onReceivedOSSUrl(String str) {
        if (this.isViewAttached) {
            this.mResultView.setText(str);
            this.mConsoleView.setText("日志上报成功，并自动复制到剪贴板...");
        }
        if (this.mContext != null) {
            SDKUtils.copyToClipboard(this.mContext, str, false);
            if (this.isViewAttached) {
                Toast.makeText(this.mContext, "URL已自动复制到剪贴板!", 1).show();
            }
        }
    }

    public void onUploadLog(int i, String str) {
        this.isUploading = true;
        this.isConnected = true;
        this.hasUploadLog = true;
        if (this.isViewAttached) {
            this.mConsoleView.setText(String.format(Locale.getDefault(), "正在上传日志，已上传%d条", new Object[]{Integer.valueOf(i)}));
            this.mResultView.setText("");
        }
    }

    public void onBeforeUpload() {
        this.isUploading = true;
        this.isConnected = true;
    }

    public void onClose(int i, String str) {
        this.isConnected = false;
        this.hasUploadLog = false;
        if (this.isViewAttached) {
            this.mConsoleView.post(new Runnable() {
                public void run() {
                    LogUploadOverlayView.this.mConsoleView.setText("连接已断开，点击【开始】上报日志");
                }
            });
        }
    }

    public void onError(String str) {
        this.isConnected = false;
        this.hasUploadLog = false;
        if (this.isViewAttached) {
            this.mConsoleView.setText(str);
        }
    }
}
