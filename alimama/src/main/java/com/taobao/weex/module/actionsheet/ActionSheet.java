package com.taobao.weex.module.actionsheet;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alibaba.aliweex.R;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.module.actionsheet.ActionSheetDialog;
import com.taobao.weex.utils.WXLogUtils;
import java.lang.ref.WeakReference;
import java.net.URLDecoder;

public class ActionSheet {
    /* access modifiers changed from: private */
    public static WeakReference<ActionSheet> weakActionSheet;
    /* access modifiers changed from: private */
    public ActionHandler mActionHandler;
    private LinearLayout mActionSheetContainer;
    /* access modifiers changed from: private */
    public JSCallback mCallback;
    private String mCancelButtonTitle;
    private ViewGroup mContentView;
    private Context mContext;
    /* access modifiers changed from: private */
    public ActionSheetDialog mDialog;
    private TranslateAnimation mDismissAnimation;
    private SparseArray<String> mNormalButtons = new SparseArray<>();

    private interface ActionHandler {
        void onClick(ActionSheet actionSheet, int i, String str);
    }

    public ActionSheet(Context context) {
        this.mContentView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.actionsheet_container, (ViewGroup) null);
        this.mContentView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ActionSheet.this.dismissAndCallback();
            }
        });
        this.mActionSheetContainer = (LinearLayout) this.mContentView.findViewById(R.id.ly_action_sheet_container);
        this.mContext = context;
    }

    private void setActionHandler(ActionHandler actionHandler) {
        this.mActionHandler = actionHandler;
    }

    private void addAction(String str, int i, int i2) {
        this.mNormalButtons.put(i, str);
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(this.mContext).inflate(R.layout.actionsheet_button, (ViewGroup) null);
        TextView textView = (TextView) viewGroup.findViewById(R.id.btn_action_sheet_action);
        textView.setText(str);
        textView.setTag(R.id.action_sheet_msg, str);
        textView.setTag(R.id.action_sheet_index, Integer.valueOf(i));
        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ActionSheet.this.mActionHandler != null) {
                    ActionSheet.this.mActionHandler.onClick(ActionSheet.this, ((Integer) view.getTag(R.id.action_sheet_index)).intValue(), (String) view.getTag(R.id.action_sheet_msg));
                    ActionSheet.this.dismiss();
                }
            }
        });
        if (i2 == 1) {
            this.mActionSheetContainer.addView(viewGroup);
        } else if (i == 0) {
            textView.setBackgroundResource(R.drawable.actionsheet_button_first_bg);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
            layoutParams.setMargins(0, 0, 0, 1);
            this.mActionSheetContainer.addView(viewGroup, layoutParams);
        } else if (i == i2 - 1) {
            textView.setBackgroundResource(R.drawable.actionsheet_button_last_bg);
            this.mActionSheetContainer.addView(viewGroup);
        } else {
            textView.setBackgroundResource(R.drawable.actionsheet_button_normal_bg);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
            layoutParams2.setMargins(0, 0, 0, 1);
            this.mActionSheetContainer.addView(viewGroup, layoutParams2);
        }
    }

    private void show() {
        TextView textView = (TextView) this.mContentView.findViewById(R.id.btn_action_sheet_cancel);
        if (this.mCancelButtonTitle == null) {
            this.mCancelButtonTitle = this.mContext.getResources().getString(R.string.action_sheet_cancel_title);
        }
        textView.setText(this.mCancelButtonTitle);
        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ActionSheet.this.dismissAndCallback();
            }
        });
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Rect rect = new Rect();
        Activity activity = (Activity) this.mContext;
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        if (this.mDialog != null && this.mDialog.isShowing()) {
            this.mDialog.cancel();
        }
        this.mDialog = new ActionSheetDialog(this.mContext, R.style.ActionSheetStyle);
        this.mDialog.setBackPressHandler(new ActionSheetDialog.BackPressHandler() {
            public boolean onBackPressed() {
                if (ActionSheet.this.mDialog == null || !ActionSheet.this.mDialog.isShowing()) {
                    return false;
                }
                ActionSheet.this.dismissAndCallback();
                return true;
            }
        });
        this.mDialog.setContentView(this.mContentView);
        TranslateAnimation translateAnimation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 1.0f, 1, 0.0f);
        translateAnimation.setInterpolator(new DecelerateInterpolator());
        translateAnimation.setDuration(250);
        this.mContentView.startAnimation(translateAnimation);
        Window window = this.mDialog.getWindow();
        if (window != null) {
            window.setGravity(80);
            window.getAttributes().width = displayMetrics.widthPixels;
            window.getAttributes().height = displayMetrics.heightPixels - rect.top;
            this.mDialog.show();
        }
    }

    /* access modifiers changed from: private */
    public void dismiss() {
        if (this.mDialog != null && this.mDialog.isShowing()) {
            if (this.mDismissAnimation == null || this.mDismissAnimation.hasEnded()) {
                this.mDismissAnimation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 0.0f, 1, 1.0f);
                this.mDismissAnimation.setInterpolator(new AccelerateInterpolator());
                this.mDismissAnimation.setDuration(250);
                this.mDismissAnimation.setAnimationListener(new Animation.AnimationListener() {
                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationStart(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                        if (ActionSheet.this.mDialog != null && ActionSheet.this.mDialog.isShowing()) {
                            ActionSheet.this.mDialog.dismiss();
                            ActionSheet.weakActionSheet.clear();
                        }
                    }
                });
                this.mContentView.startAnimation(this.mDismissAnimation);
            }
        }
    }

    /* access modifiers changed from: private */
    public void dismissAndCallback() {
        dismiss();
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("index", (Object) Integer.valueOf(this.mNormalButtons.size()));
        jSONObject.put("msg", (Object) this.mCancelButtonTitle);
        if (this.mCallback != null) {
            this.mCallback.invoke(jSONObject);
        }
    }

    private void setCancelButtonTitle(String str) {
        this.mCancelButtonTitle = str;
    }

    public static void showActionSheet(Context context, String str, JSCallback jSCallback) {
        ActionSheet actionSheet;
        if (weakActionSheet == null || weakActionSheet.get() == null) {
            actionSheet = new ActionSheet(context);
            weakActionSheet = new WeakReference<>(actionSheet);
        } else {
            ActionSheet actionSheet2 = (ActionSheet) weakActionSheet.get();
            if (actionSheet2 != null) {
                actionSheet2.dismiss();
            }
            weakActionSheet.clear();
            actionSheet = new ActionSheet(context);
            weakActionSheet = new WeakReference<>(actionSheet);
        }
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONObject parseObject = JSON.parseObject(URLDecoder.decode(str, "utf-8"));
                JSONArray jSONArray = parseObject.getJSONArray("buttons");
                for (int i = 0; i < jSONArray.size(); i++) {
                    actionSheet.addAction((String) jSONArray.get(i), i, jSONArray.size());
                }
                String string = parseObject.getString("cancel");
                if (string != null) {
                    actionSheet.setCancelButtonTitle(string);
                }
            } catch (Exception e) {
                WXLogUtils.e("[ActionSheet] confirm param parse error ", (Throwable) e);
            }
            if (jSCallback != null) {
                actionSheet.mCallback = jSCallback;
                actionSheet.setActionHandler(new ActionHandler() {
                    public void onClick(ActionSheet actionSheet, int i, String str) {
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put("index", (Object) Integer.valueOf(i));
                        jSONObject.put("msg", (Object) str);
                        if (actionSheet != null && actionSheet.mCallback != null) {
                            actionSheet.mCallback.invoke(jSONObject);
                        }
                    }
                });
            }
        }
        actionSheet.show();
    }
}
