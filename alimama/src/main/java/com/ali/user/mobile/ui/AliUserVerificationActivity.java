package com.ali.user.mobile.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.ali.user.mobile.base.ui.BaseActivity;
import com.ali.user.mobile.callback.RpcRequestCallback;
import com.ali.user.mobile.data.DataRepository;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.register.model.OceanRegisterParam;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.rpc.RpcResponse;
import com.ali.user.mobile.rpc.register.model.OceanRegisterResponseData;
import com.ali.user.mobile.rpc.register.model.OceanRegisterResult;
import com.ali.user.mobile.service.ServiceFactory;
import com.ali.user.mobile.service.StorageService;
import com.ali.user.mobile.ui.widget.AppleView;
import com.ali.user.mobile.ui.widget.CartView;
import com.taobao.orange.OConstant;
import com.taobao.wireless.security.sdk.SecurityGuardManager;
import com.taobao.wireless.security.sdk.nocaptcha.INoCaptchaComponent;
import java.util.HashMap;
import java.util.Properties;

public class AliUserVerificationActivity extends BaseActivity {
    private static final int INIT_START = 10000;
    public static final String LOG_TAG = "VerifyActivity";
    public static String PAGE_NAME = "Page_Slide";
    private static final int VERIFY_ANIMATION_END = 10002;
    private static final int VERIFY_ANIMATION_START = 10001;
    private static final int VERIFY_START = 10003;
    public static int mScreenHeight;
    public static int mScreenWidth;
    /* access modifiers changed from: private */
    public IActivityCallback callback;
    Handler handler = new Handler() {
        public void handleMessage(Message message) {
            int bottom;
            int i;
            Bundle data = message.getData();
            HashMap hashMap = new HashMap();
            int i2 = data.getInt("status");
            int i3 = data.getInt("errorCode");
            float f = data.getFloat("x1");
            float f2 = data.getFloat("y1");
            float f3 = data.getFloat("x2");
            float f4 = data.getFloat("y2");
            String string = data.getString("token");
            String string2 = data.getString("sig");
            String string3 = data.getString("sessionId");
            int i4 = message.what;
            switch (i4) {
                case 1:
                    switch (i2) {
                        case 101:
                            AliUserVerificationActivity.this.tipsTV.setVisibility(0);
                            AliUserVerificationActivity.this.tips2TV.setVisibility(0);
                            AliUserVerificationActivity.this.mAppleView.initPostion(f, f2);
                            AliUserVerificationActivity.this.mCartView.initPostion(f3, f4);
                            if (AliUserVerificationActivity.this.mCartView.getParent() != null) {
                                AliUserVerificationActivity.this.mVerifyRootView.removeView(AliUserVerificationActivity.this.mCartView);
                            }
                            AliUserVerificationActivity.this.mVerifyRootView.addView(AliUserVerificationActivity.this.mCartView);
                            if (AliUserVerificationActivity.this.mAppleView.getParent() != null) {
                                AliUserVerificationActivity.this.mVerifyRootView.removeView(AliUserVerificationActivity.this.mAppleView);
                            }
                            AliUserVerificationActivity.this.mVerifyRootView.addView(AliUserVerificationActivity.this.mAppleView, AliUserVerificationActivity.this.lp);
                            return;
                        case 104:
                            if (AliUserVerificationActivity.this.callback != null) {
                                hashMap.put("token", string);
                                hashMap.put("sig", string2);
                                hashMap.put("sessionId", string3);
                                AliUserVerificationActivity.this.callback.onResult(104, hashMap);
                                return;
                            }
                            return;
                        case 105:
                            if (AliUserVerificationActivity.this.callback != null) {
                                hashMap.put("errorCode", String.valueOf(i3));
                                AliUserVerificationActivity.this.callback.onResult(105, hashMap);
                            }
                            if (1207 == i3) {
                                Toast.makeText(AliUserVerificationActivity.this, AliUserVerificationActivity.this.getString(R.string.aliuser_network_error), 1).show();
                                return;
                            }
                            return;
                        default:
                            return;
                    }
                case 2:
                    switch (i2) {
                        case 100:
                            AliUserVerificationActivity.this.mVerifyRootView.removeView(AliUserVerificationActivity.this.mCartView);
                            AliUserVerificationActivity.this.mVerifyRootView.removeView(AliUserVerificationActivity.this.mAppleView);
                            AliUserVerificationActivity.this.tipsTV.setVisibility(4);
                            AliUserVerificationActivity.this.tips2TV.setVisibility(4);
                            return;
                        case 102:
                        case 104:
                            if (AliUserVerificationActivity.this.callback != null) {
                                hashMap.put("token", string);
                                hashMap.put("sig", string2);
                                hashMap.put("sessionID", string3);
                                AliUserVerificationActivity.this.callback.onResult(102, hashMap);
                                return;
                            }
                            return;
                        case 103:
                            AliUserVerificationActivity.this.tipsTV.setVisibility(0);
                            AliUserVerificationActivity.this.tips2TV.setVisibility(0);
                            AliUserVerificationActivity.this.mAppleView.initPostion(f, f2);
                            AliUserVerificationActivity.this.mCartView.initPostion(f3, f4);
                            if (AliUserVerificationActivity.this.mCartView.getParent() != null) {
                                AliUserVerificationActivity.this.mVerifyRootView.removeView(AliUserVerificationActivity.this.mCartView);
                            }
                            AliUserVerificationActivity.this.mVerifyRootView.addView(AliUserVerificationActivity.this.mCartView);
                            if (AliUserVerificationActivity.this.mAppleView.getParent() != null) {
                                AliUserVerificationActivity.this.mVerifyRootView.removeView(AliUserVerificationActivity.this.mAppleView);
                            }
                            AliUserVerificationActivity.this.mVerifyRootView.addView(AliUserVerificationActivity.this.mAppleView, AliUserVerificationActivity.this.lp);
                            return;
                        case 105:
                            if (AliUserVerificationActivity.this.callback != null) {
                                hashMap.put("errorCode", String.valueOf(i3));
                                AliUserVerificationActivity.this.callback.onResult(105, hashMap);
                            }
                            if (1207 == i3) {
                                Toast.makeText(AliUserVerificationActivity.this, AliUserVerificationActivity.this.getString(R.string.aliuser_network_error), 1).show();
                                return;
                            }
                            return;
                        default:
                            return;
                    }
                default:
                    switch (i4) {
                        case 10000:
                            int i5 = 290;
                            if (AliUserVerificationActivity.this.mCartView != null) {
                                i5 = AliUserVerificationActivity.this.mCartView.getCartWidth();
                            }
                            if (AliUserVerificationActivity.this.mVerifyRootView.getBottom() <= 0) {
                                if ((AliUserVerificationActivity.mScreenHeight - AliUserVerificationActivity.this.getStatusBarHeight()) - 112 > i5) {
                                    bottom = ((AliUserVerificationActivity.mScreenHeight - AliUserVerificationActivity.this.getStatusBarHeight()) - 112) - i5;
                                } else {
                                    bottom = (AliUserVerificationActivity.mScreenHeight - AliUserVerificationActivity.this.getStatusBarHeight()) - 112;
                                }
                            } else if (AliUserVerificationActivity.this.mVerifyRootView.getBottom() - AliUserVerificationActivity.this.mVerifyRootView.getTop() > i5) {
                                bottom = (AliUserVerificationActivity.this.mVerifyRootView.getBottom() - AliUserVerificationActivity.this.mVerifyRootView.getTop()) - i5;
                            } else {
                                bottom = AliUserVerificationActivity.this.mVerifyRootView.getBottom() - AliUserVerificationActivity.this.mVerifyRootView.getTop();
                            }
                            int i6 = bottom;
                            if (AliUserVerificationActivity.mScreenWidth > i5) {
                                i = AliUserVerificationActivity.mScreenWidth - i5;
                            } else {
                                i = AliUserVerificationActivity.mScreenWidth;
                            }
                            if (AliUserVerificationActivity.this.ncComponent != null) {
                                AliUserVerificationActivity.this.ncComponent.initNoCaptcha(AliUserVerificationActivity.this.mAppKey, AliUserVerificationActivity.class.getSimpleName(), i, i6, 5, AliUserVerificationActivity.this.handler);
                                return;
                            }
                            return;
                        case AliUserVerificationActivity.VERIFY_ANIMATION_START /*10001*/:
                            AliUserVerificationActivity.this.mCartView.setStatus(1);
                            AliUserVerificationActivity.this.mCartView.invalidate();
                            AliUserVerificationActivity.this.mAppleView.setStatus(1);
                            AliUserVerificationActivity.this.mAppleView.setPositionFinish(AliUserVerificationActivity.this.mCartView.getCenterX(), AliUserVerificationActivity.this.mCartView.getCenterY());
                            AliUserVerificationActivity.this.mAppleView.invalidate();
                            sendEmptyMessageDelayed(10002, 200);
                            return;
                        case 10002:
                            AliUserVerificationActivity.this.mCartView.setStatus(2);
                            AliUserVerificationActivity.this.mCartView.invalidate();
                            AliUserVerificationActivity.this.mAppleView.setStatus(2);
                            AliUserVerificationActivity.this.mAppleView.setPositionEnd(AliUserVerificationActivity.this.mCartView.getCenterX1(), AliUserVerificationActivity.this.mCartView.getCenterY1());
                            AliUserVerificationActivity.this.mAppleView.invalidate();
                            sendEmptyMessageDelayed(AliUserVerificationActivity.VERIFY_START, 300);
                            return;
                        case AliUserVerificationActivity.VERIFY_START /*10003*/:
                            AliUserVerificationActivity.this.ncComponent.noCaptchaVerification(AliUserVerificationActivity.this.mAppKey);
                            return;
                        default:
                            return;
                    }
            }
        }
    };
    Runnable initTask = new Runnable() {
        public void run() {
            AliUserVerificationActivity.this.handler.sendEmptyMessage(10000);
        }
    };
    boolean isFingerValid = false;
    /* access modifiers changed from: private */
    public FrameLayout.LayoutParams lp;
    /* access modifiers changed from: private */
    public String mAppKey;
    /* access modifiers changed from: private */
    public AppleView mAppleView;
    /* access modifiers changed from: private */
    public CartView mCartView;
    /* access modifiers changed from: private */
    public FrameLayout mVerifyRootView;
    /* access modifiers changed from: private */
    public INoCaptchaComponent ncComponent;
    private String sdkSessionId;
    /* access modifiers changed from: private */
    public TextView tips2TV;
    /* access modifiers changed from: private */
    public TextView tipsTV;

    public interface IActivityCallback {
        void onNotifyBackPressed();

        void onResult(int i, HashMap<String, String> hashMap);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return false;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mAppKey = ((StorageService) ServiceFactory.getService(StorageService.class)).getAppKey(0);
        if (getIntent() != null) {
            this.sdkSessionId = getIntent().getStringExtra("sid");
        }
        this.callback = new IActivityCallback() {
            public void onNotifyBackPressed() {
            }

            public void onResult(int i, HashMap<String, String> hashMap) {
                AliUserVerificationActivity.this.handleResult(i, hashMap);
            }
        };
    }

    /* access modifiers changed from: protected */
    public int getLayoutContent() {
        return R.layout.aliuser_activity_verification;
    }

    /* access modifiers changed from: protected */
    public void initViews() {
        super.initViews();
        try {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(R.string.aliuser_verification);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        this.mVerifyRootView = (FrameLayout) findViewById(R.id.aliuser_verify_root);
        this.tipsTV = (TextView) findViewById(R.id.aliuser_verify_tips);
        this.tips2TV = (TextView) findViewById(R.id.aliuser_verify_tips2);
        this.mAppleView = new AppleView(this);
        this.lp = new FrameLayout.LayoutParams(-2, -2);
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        mScreenWidth = displayMetrics.widthPixels;
        mScreenHeight = displayMetrics.heightPixels;
        getWindowManager().getDefaultDisplay().getMetrics(new DisplayMetrics());
        if (mScreenWidth > mScreenHeight) {
            int i = mScreenHeight;
            mScreenHeight = mScreenWidth;
            mScreenWidth = i;
        }
        this.mCartView = new CartView(this);
        this.mCartView.setMinimumHeight(300);
        this.mCartView.setMinimumWidth(300);
        this.mAppleView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case 0:
                        if (motionEvent.getX() >= AliUserVerificationActivity.this.mAppleView.getInitLeft() && motionEvent.getX() <= AliUserVerificationActivity.this.mAppleView.getInitRight() && motionEvent.getY() >= AliUserVerificationActivity.this.mAppleView.getInitTop() && motionEvent.getY() <= AliUserVerificationActivity.this.mAppleView.getInitBottom()) {
                            AliUserVerificationActivity.this.isFingerValid = true;
                            break;
                        } else {
                            AliUserVerificationActivity.this.isFingerValid = false;
                            break;
                        }
                        break;
                    case 1:
                    case 3:
                        if (motionEvent.getX() >= AliUserVerificationActivity.this.mCartView.getInitLeft() && motionEvent.getX() <= AliUserVerificationActivity.this.mCartView.getInitRight() && motionEvent.getY() >= AliUserVerificationActivity.this.mCartView.getInitTop() && motionEvent.getY() <= AliUserVerificationActivity.this.mCartView.getInitBottom()) {
                            if (AliUserVerificationActivity.this.isFingerValid) {
                                AliUserVerificationActivity.this.requestForVerification();
                                break;
                            } else {
                                return true;
                            }
                        } else {
                            AliUserVerificationActivity.this.mAppleView.resetPostion();
                            AliUserVerificationActivity.this.mAppleView.invalidate();
                            break;
                        }
                    case 2:
                        if (motionEvent.getX() >= AliUserVerificationActivity.this.mAppleView.getRadiusTouch() && motionEvent.getX() <= ((float) AliUserVerificationActivity.mScreenWidth) - AliUserVerificationActivity.this.mAppleView.getRadiusTouch() && motionEvent.getY() >= AliUserVerificationActivity.this.mAppleView.getRadiusTouch() && motionEvent.getY() <= ((float) (AliUserVerificationActivity.this.mVerifyRootView.getBottom() - AliUserVerificationActivity.this.mVerifyRootView.getTop())) - AliUserVerificationActivity.this.mAppleView.getRadiusTouch() && AliUserVerificationActivity.this.isFingerValid) {
                            AliUserVerificationActivity.this.mAppleView.setPosition(motionEvent.getX(), motionEvent.getY());
                            AliUserVerificationActivity.this.mAppleView.invalidate();
                            AliUserVerificationActivity.this.ncComponent.putNoCaptchaTraceRecord(motionEvent);
                            break;
                        } else {
                            return true;
                        }
                }
                return true;
            }
        });
        this.ncComponent = SecurityGuardManager.getInstance(getApplicationContext()).getNoCaptchaComp();
        getWindow().getDecorView().post(new Runnable() {
            public void run() {
                AliUserVerificationActivity.this.handler.post(AliUserVerificationActivity.this.initTask);
            }
        });
    }

    public void onStop() {
        super.onStop();
        this.handler.removeCallbacks(this.initTask);
    }

    public void onBackPressed() {
        UserTrackAdapter.sendControlUT(PAGE_NAME, "Button-Back");
        if (this.callback != null) {
            this.callback.onNotifyBackPressed();
        }
        super.onBackPressed();
    }

    public void requestForVerification() {
        this.handler.sendEmptyMessage(VERIFY_ANIMATION_START);
    }

    /* access modifiers changed from: protected */
    public int getStatusBarHeight() {
        return Resources.getSystem().getDimensionPixelSize(Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android"));
    }

    /* access modifiers changed from: private */
    public void handleResult(int i, HashMap<String, String> hashMap) {
        if (i != 102) {
            toast(getString(R.string.aliuser_sever_error), 0);
            slideUT(ApiConstants.UTConstants.UT_SUCCESS_F, hashMap.get("errorCode"), "");
            setResult(0);
            finish();
        } else if (TextUtils.isEmpty(this.sdkSessionId)) {
            Intent intent = new Intent();
            intent.putExtra("token", hashMap.get("token"));
            intent.putExtra("sig", hashMap.get("sig"));
            intent.putExtra("sid", hashMap.get("sessionID"));
            setResult(-1, intent);
            slideUT(ApiConstants.UTConstants.UT_SUCCESS_T, OConstant.CODE_POINT_EXP_LOAD_CACHE, "");
            finish();
        } else {
            OceanRegisterParam oceanRegisterParam = new OceanRegisterParam();
            oceanRegisterParam.ncSessionId = hashMap.get("sessionID");
            oceanRegisterParam.ncSignature = hashMap.get("sig");
            oceanRegisterParam.ncToken = hashMap.get("token");
            oceanRegisterParam.sessionId = this.sdkSessionId;
            DataRepository.captchaCheck(oceanRegisterParam, new RpcRequestCallback() {
                public void onSuccess(RpcResponse rpcResponse) {
                    OceanRegisterResponseData oceanRegisterResponseData = (OceanRegisterResponseData) rpcResponse;
                    Intent intent = new Intent();
                    if (!(oceanRegisterResponseData == null || oceanRegisterResponseData.returnValue == null)) {
                        intent.putExtra("registerToken", ((OceanRegisterResult) oceanRegisterResponseData.returnValue).registerToken);
                        intent.putExtra("sessionId", ((OceanRegisterResult) oceanRegisterResponseData.returnValue).sdkSessionId);
                    }
                    if (oceanRegisterResponseData != null) {
                        intent.putExtra("actionType", oceanRegisterResponseData.actionType);
                    }
                    AliUserVerificationActivity.this.setResult(-1, intent);
                    AliUserVerificationActivity.this.slideUT(ApiConstants.UTConstants.UT_SUCCESS_T, "3000", oceanRegisterResponseData == null ? "" : oceanRegisterResponseData.actionType);
                    AliUserVerificationActivity.this.finish();
                }

                public void onSystemError(RpcResponse rpcResponse) {
                    AliUserVerificationActivity.this.onVerifyFail(rpcResponse);
                    AliUserVerificationActivity.this.setResult(0);
                    AliUserVerificationActivity.this.slideUT(ApiConstants.UTConstants.UT_SUCCESS_F, "406", "");
                    AliUserVerificationActivity.this.finish();
                }

                public void onError(RpcResponse rpcResponse) {
                    AliUserVerificationActivity.this.onVerifyFail(rpcResponse);
                    AliUserVerificationActivity.this.setResult(0);
                    AliUserVerificationActivity.this.slideUT(ApiConstants.UTConstants.UT_SUCCESS_F, "406", "");
                    AliUserVerificationActivity.this.finish();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void slideUT(String str, String str2, String str3) {
        Properties properties = new Properties();
        properties.put("is_success", str);
        if (TextUtils.equals(ApiConstants.UTConstants.UT_SUCCESS_F, str)) {
            properties.put("errorCode", str2 + "");
        }
        if (!TextUtils.isEmpty(str3)) {
            properties.put("actionType", str3);
        }
        UserTrackAdapter.sendUT(PAGE_NAME, ApiConstants.UTConstants.UT_TYPE_SLIDE_RESULT, properties);
    }

    /* access modifiers changed from: private */
    public void onVerifyFail(RpcResponse rpcResponse) {
        if (rpcResponse == null || rpcResponse.code == 4) {
            toast(getString(R.string.aliuser_sever_error), 0);
        } else {
            toast(rpcResponse.message, 0);
        }
    }

    public static void startVerifyActivity(Activity activity, String str, int i) {
        Intent intent = new Intent(activity, AliUserVerificationActivity.class);
        intent.putExtra("sid", str);
        activity.startActivityForResult(intent, i);
    }
}
