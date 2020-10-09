package com.taobao.weex.analyzer.core.debug;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.analyzer.R;
import com.taobao.weex.analyzer.UserTrack;
import com.taobao.weex.analyzer.core.debug.DebugController;
import com.taobao.weex.analyzer.core.debug.MDSDebugMessageReceiver;
import com.taobao.weex.analyzer.utils.DeviceUtils;
import com.taobao.weex.utils.WXLogUtils;
import java.util.Map;

public class MDSDebugEntranceView extends FrameLayout implements View.OnClickListener, MDSDebugMessageReceiver.OnReceiveDebugMsgListener {
    private static final String SERVER_HOST = "ws://mds.alibaba-inc.com/socket.ws/weexAnalyzer";
    private View mBtnAction;
    private View mBtnDisconnect;
    private Button mCode1;
    private Button mCode2;
    private Button mCode3;
    private Button mCode4;
    private String mCodeQuery = "";
    private TextView mConnectionStatus;
    /* access modifiers changed from: private */
    public TextView mCurCode;
    private MDSDebugMessageReceiver mDebugMessageReceiver;
    /* access modifiers changed from: private */
    public ViewGroup mInputContainer;
    /* access modifiers changed from: private */
    public ViewGroup mResultContainer;
    private View mThumbnailDoing;
    private View mThumbnailDone;

    public MDSDebugEntranceView(Context context) {
        super(context);
        init();
    }

    public MDSDebugEntranceView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public MDSDebugEntranceView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mDebugMessageReceiver = MDSDebugMessageReceiver.createInstance(getContext().getApplicationContext(), this);
        MDSDebugService.launchBy(getContext(), (String) null, (String) null);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mDebugMessageReceiver.destroy();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.wxt_debug_entrance, this, true);
        this.mCode1 = (Button) findViewById(R.id.code_1);
        this.mCode2 = (Button) findViewById(R.id.code_2);
        this.mCode3 = (Button) findViewById(R.id.code_3);
        this.mCode4 = (Button) findViewById(R.id.code_4);
        this.mBtnAction = findViewById(R.id.btn_action);
        this.mCurCode = (TextView) findViewById(R.id.cur_code);
        this.mInputContainer = (ViewGroup) findViewById(R.id.input_container);
        this.mResultContainer = (ViewGroup) findViewById(R.id.result_container);
        this.mCode1.setOnClickListener(this);
        this.mCode2.setOnClickListener(this);
        this.mCode3.setOnClickListener(this);
        this.mCode4.setOnClickListener(this);
        this.mBtnAction.setOnClickListener(this);
        this.mBtnDisconnect = findViewById(R.id.btn_disconnect);
        this.mThumbnailDoing = findViewById(R.id.thumbnail_doing);
        this.mThumbnailDone = findViewById(R.id.thumbnail_done);
        this.mConnectionStatus = (TextView) findViewById(R.id.status_text);
        this.mBtnDisconnect.setOnClickListener(this);
        this.mInputContainer.setVisibility(4);
        this.mResultContainer.setVisibility(4);
        this.mBtnDisconnect.setVisibility(8);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.code_1) {
            this.mCodeQuery += "1";
        } else if (view.getId() == R.id.code_2) {
            this.mCodeQuery += "2";
        } else if (view.getId() == R.id.code_3) {
            this.mCodeQuery += "3";
        } else if (view.getId() == R.id.code_4) {
            this.mCodeQuery += "4";
        } else if (view.getId() == R.id.btn_action) {
            if (this.mCodeQuery.length() > 0) {
                this.mCodeQuery = this.mCodeQuery.substring(0, this.mCodeQuery.length() - 1);
            }
        } else if (view.getId() == R.id.btn_disconnect) {
            doDisconnect();
        }
        if (WXEnvironment.isApkDebugable()) {
            WXLogUtils.d("weex-analyzer", "code:" + this.mCodeQuery);
        }
        this.mCurCode.setText("请输入4位数调试码:" + this.mCodeQuery);
        if (this.mCodeQuery.length() == 4) {
            doConnect(this.mCodeQuery);
            this.mCodeQuery = "";
        }
    }

    private void doConnect(@NonNull final String str) {
        UserTrack.commit(getContext(), "wx_option_ladder", (Map<String, String>) null);
        this.mConnectionStatus.setText("正在连接中");
        this.mThumbnailDoing.setVisibility(0);
        this.mThumbnailDone.setVisibility(8);
        easeIn(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                MDSDebugEntranceView.this.connectToMDS(str);
            }
        });
    }

    private void doDisconnect() {
        MDSDebugService.stop(getContext());
    }

    /* access modifiers changed from: private */
    public void connectToMDS(@NonNull String str) {
        MDSDebugService.launchBy(getContext(), SERVER_HOST + "?uuid=" + str + "&deviceId=" + DeviceUtils.getDeviceId(getContext()) + "&type=ladder", DeviceUtils.getDeviceId(getContext()));
    }

    private void easeIn(@NonNull Animator.AnimatorListener animatorListener) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mInputContainer, "translationX", new float[]{(float) (-this.mInputContainer.getWidth())});
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.mResultContainer, "translationX", new float[]{0.0f});
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(600);
        animatorSet.playTogether(new Animator[]{ofFloat, ofFloat2});
        animatorSet.addListener(animatorListener);
        animatorSet.start();
    }

    /* access modifiers changed from: private */
    public void easeOut() {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mInputContainer, "translationX", new float[]{0.0f});
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.mResultContainer, "translationX", new float[]{(float) this.mResultContainer.getWidth()});
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(600);
        animatorSet.playTogether(new Animator[]{ofFloat, ofFloat2});
        animatorSet.start();
    }

    public void onMessageReceived(DebugController.ConnectionInfo connectionInfo) {
        if (connectionInfo != null) {
            switch (connectionInfo.state) {
                case 0:
                    setPositionByState(false);
                    this.mConnectionStatus.setText(connectionInfo.msg);
                    ((ImageView) this.mThumbnailDone).setImageResource(R.drawable.wxt_icon_error);
                    this.mThumbnailDone.setVisibility(0);
                    this.mThumbnailDoing.setVisibility(8);
                    this.mBtnDisconnect.setVisibility(8);
                    return;
                case 1:
                    setPositionByState(true);
                    this.mConnectionStatus.setText(connectionInfo.msg);
                    this.mThumbnailDone.setVisibility(8);
                    this.mThumbnailDoing.setVisibility(0);
                    this.mBtnDisconnect.setVisibility(8);
                    return;
                case 2:
                    setPositionByState(true);
                    this.mConnectionStatus.setText(connectionInfo.msg);
                    ((ImageView) this.mThumbnailDone).setImageResource(R.drawable.wxt_icon_done);
                    this.mBtnDisconnect.setVisibility(0);
                    this.mThumbnailDone.setVisibility(0);
                    this.mThumbnailDoing.setVisibility(8);
                    return;
                case 3:
                    setPositionByState(true);
                    this.mConnectionStatus.setText(connectionInfo.msg);
                    ((ImageView) this.mThumbnailDone).setImageResource(R.drawable.wxt_icon_error);
                    this.mThumbnailDone.setVisibility(0);
                    this.mThumbnailDoing.setVisibility(8);
                    this.mBtnDisconnect.setVisibility(8);
                    this.mResultContainer.postDelayed(new Runnable() {
                        public void run() {
                            MDSDebugEntranceView.this.mCurCode.setText("请输入4位数调试码:");
                            MDSDebugEntranceView.this.easeOut();
                        }
                    }, 500);
                    return;
                case 4:
                    setPositionByState(true);
                    this.mConnectionStatus.setText(connectionInfo.msg);
                    ((ImageView) this.mThumbnailDone).setImageResource(R.drawable.wxt_icon_error);
                    this.mThumbnailDone.setVisibility(0);
                    this.mThumbnailDoing.setVisibility(8);
                    this.mBtnDisconnect.setVisibility(8);
                    this.mResultContainer.postDelayed(new Runnable() {
                        public void run() {
                            MDSDebugEntranceView.this.mCurCode.setText("请输入4位数调试码:");
                            MDSDebugEntranceView.this.easeOut();
                        }
                    }, 500);
                    return;
                case 5:
                    setPositionByState(false);
                    this.mConnectionStatus.setText(connectionInfo.msg);
                    ((ImageView) this.mThumbnailDone).setImageResource(R.drawable.wxt_icon_error);
                    this.mThumbnailDone.setVisibility(0);
                    this.mThumbnailDoing.setVisibility(8);
                    this.mBtnDisconnect.setVisibility(8);
                    return;
                default:
                    return;
            }
        }
    }

    private void setPositionByState(final boolean z) {
        if (this.mInputContainer != null && this.mResultContainer != null) {
            this.mResultContainer.postDelayed(new Runnable() {
                public void run() {
                    if (z) {
                        MDSDebugEntranceView.this.mInputContainer.setTranslationX((float) (-MDSDebugEntranceView.this.mInputContainer.getWidth()));
                        MDSDebugEntranceView.this.mResultContainer.setTranslationX(0.0f);
                    } else {
                        MDSDebugEntranceView.this.mInputContainer.setTranslationX(0.0f);
                        MDSDebugEntranceView.this.mResultContainer.setTranslationX((float) MDSDebugEntranceView.this.mResultContainer.getWidth());
                    }
                    MDSDebugEntranceView.this.mInputContainer.setVisibility(0);
                    MDSDebugEntranceView.this.mResultContainer.setVisibility(0);
                }
            }, 0);
        }
    }
}
