package com.taobao.uikit.extend.component;

import alimama.com.unwrouter.UNWRouter;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import com.alipay.sdk.cons.c;
import com.taobao.android.nav.Nav;
import com.taobao.monitor.terminator.ApmGodEye;
import com.taobao.uikit.extend.R;
import com.taobao.uikit.extend.component.error.AbsErrorFilter;
import com.taobao.uikit.extend.component.error.DefaultErrorFilter;
import com.taobao.uikit.extend.component.error.Error;
import com.taobao.uikit.extend.feature.view.TUrlImageView;
import com.taobao.uikit.extend.utils.NetUtil;
import com.taobao.weex.BuildConfig;
import java.net.URLEncoder;
import java.util.Map;

public class TBErrorView extends FrameLayout {
    private static final String[] DEFAULT_DIMENSION_SET = {UNWRouter.PAGE_NAME, "url", "title", "subtitle", "code", "mappingCode", "responseCode", c.n};
    private static final String[] DEFAULT_MEASURE_SET = {"value"};
    private static final String MODULE_NAME = "TBErrorView";
    private static final String MONITOR_POINT = "show_error";
    private static boolean isMonitorRegistered = false;
    private boolean hasReported;
    /* access modifiers changed from: private */
    public Error mError;
    private AbsErrorFilter mErrorFilter;
    private TextView mErrorInfoTextView;
    @DrawableRes
    private int mIconRes;
    private String mIconString;
    private TUrlImageView mIconView;
    private Button mLeftButton;
    private Button mRightButton;
    private Status mStatus;
    /* access modifiers changed from: private */
    public String mSubTitle;
    private TextView mSubTitleView;
    /* access modifiers changed from: private */
    public String mTitle;
    private TextView mTitleView;

    public enum ButtonType {
        BUTTON_LEFT,
        BUTTON_RIGHT,
        BUTTON_POSITIVE,
        BUTTON_NAGTIVE
    }

    public enum Status {
        STATUS_ERROR,
        STATUS_EMPTY
    }

    private void throwIllegal() {
    }

    @Deprecated
    public void setIcon(String str) {
    }

    @Deprecated
    public void setIconfont(int i) {
    }

    @Deprecated
    public void setIconfont(String str) {
    }

    public TBErrorView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        TypedArray obtainStyledAttributes;
        this.mIconRes = -1;
        this.mStatus = Status.STATUS_ERROR;
        this.hasReported = false;
        if (!(attributeSet == null || (obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.TBErrorView, i, 0)) == null)) {
            this.mIconString = obtainStyledAttributes.getString(R.styleable.TBErrorView_uik_errorIcon);
            this.mTitle = obtainStyledAttributes.getString(R.styleable.TBErrorView_uik_errorTitle);
            this.mSubTitle = obtainStyledAttributes.getString(R.styleable.TBErrorView_uik_errorSubTitle);
            obtainStyledAttributes.recycle();
        }
        this.mErrorFilter = new DefaultErrorFilter(getContext(), getResources().getString(R.string.uik_default_rule));
        initErrorView(LayoutInflater.from(getContext()).inflate(R.layout.uik_error, this, true));
    }

    public TBErrorView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TBErrorView(Context context) {
        this(context, (AttributeSet) null, 0);
    }

    private void initErrorView(View view) {
        this.mIconView = (TUrlImageView) view.findViewById(R.id.uik_error_icon);
        this.mTitleView = (TextView) view.findViewById(R.id.uik_error_title);
        this.mSubTitleView = (TextView) view.findViewById(R.id.uik_error_subTitle);
        this.mLeftButton = (Button) findViewById(R.id.uik_errorButtonPos);
        this.mRightButton = (Button) findViewById(R.id.uik_errorButtonNag);
        this.mErrorInfoTextView = (TextView) findViewById(R.id.uik_mapping_code);
        this.mRightButton.setOnClickListener(new GoToReportListener());
    }

    private void updateErrorView() {
        if (this.mStatus == Status.STATUS_ERROR) {
            if (this.mIconRes >= 0) {
                this.mIconView.setPlaceHoldForeground(ContextCompat.getDrawable(getContext(), this.mIconRes));
                this.mIconView.setImageUrl((String) null);
            } else if (!TextUtils.isEmpty(this.mIconString)) {
                this.mIconView.setPlaceHoldForeground((Drawable) null);
                this.mIconView.setImageUrl(this.mIconString);
            } else {
                this.mIconRes = R.drawable.uik_sys_error_icon;
                this.mIconView.setPlaceHoldForeground(ContextCompat.getDrawable(getContext(), this.mIconRes));
                this.mIconView.setImageUrl((String) null);
            }
        } else if (this.mStatus == Status.STATUS_EMPTY) {
            if (this.mIconRes >= 0) {
                this.mIconView.setPlaceHoldForeground(ContextCompat.getDrawable(getContext(), this.mIconRes));
                this.mIconView.setImageUrl((String) null);
            } else if (!TextUtils.isEmpty(this.mIconString)) {
                this.mIconView.setPlaceHoldForeground((Drawable) null);
                this.mIconView.setImageUrl(this.mIconString);
            } else {
                this.mIconRes = R.drawable.uik_sys_error_icon;
                this.mIconView.setPlaceHoldForeground(ContextCompat.getDrawable(getContext(), this.mIconRes));
                this.mIconView.setImageUrl((String) null);
            }
        }
        if (TextUtils.isEmpty(this.mTitle)) {
            if (this.mStatus == Status.STATUS_EMPTY) {
                this.mTitle = getContext().getString(R.string.uik_default_empty_title);
            } else {
                this.mTitle = getContext().getString(R.string.uik_default_error_title);
            }
        }
        this.mTitleView.setText(this.mTitle);
        if (TextUtils.isEmpty(this.mSubTitle)) {
            if (this.mStatus == Status.STATUS_EMPTY) {
                this.mSubTitle = getContext().getString(R.string.uik_default_empty_subtitle);
            } else if (this.mError == null || TextUtils.isEmpty(this.mError.errorMsg)) {
                this.mSubTitle = getContext().getString(R.string.uik_default_error_subtitle);
            } else {
                this.mSubTitle = this.mError.errorMsg;
            }
        }
        this.mSubTitleView.setText(this.mSubTitle);
        if (this.mStatus != Status.STATUS_ERROR || this.mError == null) {
            if (this.mStatus == Status.STATUS_EMPTY) {
                this.mRightButton.setVisibility(8);
                this.mErrorInfoTextView.setVisibility(4);
            }
        } else if (!TextUtils.isEmpty(this.mError.errorCode) || !TextUtils.isEmpty(this.mError.mappingCode)) {
            this.mErrorInfoTextView.setVisibility(0);
            this.mErrorInfoTextView.setText(TextUtils.isEmpty(this.mError.mappingCode) ? this.mError.errorCode : this.mError.mappingCode);
        } else {
            this.mErrorInfoTextView.setVisibility(4);
        }
        invalidate();
    }

    public void setIcon(@DrawableRes int i) {
        this.mIconRes = i;
        this.mIconString = null;
        filterIcon();
        updateErrorView();
    }

    public void setIconUrl(String str) {
        this.mIconString = str;
        this.mIconRes = -1;
        filterIcon();
        updateErrorView();
    }

    public void setTitle(CharSequence charSequence) {
        if (charSequence != null) {
            this.mTitle = charSequence.toString();
            filterTitle();
            updateErrorView();
        }
    }

    public void setSubTitle(CharSequence charSequence) {
        if (charSequence != null) {
            this.mSubTitle = charSequence.toString();
            filterSubTitle();
            updateErrorView();
        }
    }

    public void setButton(ButtonType buttonType, CharSequence charSequence, View.OnClickListener onClickListener) {
        Button button;
        switch (buttonType) {
            case BUTTON_LEFT:
            case BUTTON_POSITIVE:
                button = this.mLeftButton;
                break;
            default:
                button = null;
                break;
        }
        if (button != null) {
            button.setText(charSequence);
            button.setOnClickListener(onClickListener);
            button.setVisibility(onClickListener == null ? 8 : 0);
        }
    }

    public void setButtonVisibility(ButtonType buttonType, int i) {
        switch (buttonType) {
            case BUTTON_LEFT:
            case BUTTON_POSITIVE:
                if (this.mLeftButton != null) {
                    this.mLeftButton.setVisibility(i);
                    return;
                }
                return;
            case BUTTON_RIGHT:
            case BUTTON_NAGTIVE:
                if (this.mRightButton != null) {
                    this.mRightButton.setVisibility(i);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void setStatus(Status status) {
        if (status != null) {
            this.mStatus = status;
            if (this.mStatus == Status.STATUS_EMPTY) {
                if (this.mIconRes < 0) {
                    this.mIconRes = R.drawable.uik_sys_error_icon;
                }
                if (this.mSubTitle == null) {
                    this.mSubTitle = getContext().getString(R.string.uik_default_empty_subtitle);
                }
                if (this.mTitle == null) {
                    this.mTitle = getContext().getString(R.string.uik_default_empty_title);
                }
            }
            filterIcon();
            filterTitle();
            filterSubTitle();
            updateErrorView();
            if (!NetUtil.isNetworkConnected(getContext()) && this.mRightButton != null) {
                this.mRightButton.setVisibility(8);
            }
        }
    }

    public void setError(Error error) {
        if (error != null) {
            this.mError = error;
            filterIcon();
            filterTitle();
            filterSubTitle();
            updateErrorView();
            if (!NetUtil.isNetworkConnected(getContext()) && this.mRightButton != null) {
                this.mRightButton.setVisibility(8);
            }
            if (!this.hasReported) {
                try {
                    report();
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        }
    }

    private void filterIcon() {
        if (!NetUtil.isNetworkConnected(getContext())) {
            this.mIconRes = R.drawable.uik_error_icon;
        } else if (this.mStatus == Status.STATUS_EMPTY && this.mIconRes < 0) {
            this.mIconRes = R.drawable.uik_sys_error_icon;
        } else if (this.mError != null && this.mStatus == Status.STATUS_ERROR) {
            this.mIconRes = this.mErrorFilter.filterIcon(this.mError);
        }
    }

    private void filterSubTitle() {
        if (!NetUtil.isNetworkConnected(getContext())) {
            this.mSubTitle = getContext().getString(R.string.uik_network_error_subtitle);
        } else if (this.mStatus == Status.STATUS_EMPTY && TextUtils.isEmpty(this.mSubTitle)) {
            this.mSubTitle = getContext().getString(R.string.uik_default_empty_subtitle);
        } else if (this.mError != null && this.mStatus == Status.STATUS_ERROR) {
            this.mSubTitle = this.mErrorFilter.filterSubTitle(this.mError, this.mSubTitle);
        }
    }

    private void filterTitle() {
        if (!NetUtil.isNetworkConnected(getContext())) {
            this.mTitle = getContext().getString(R.string.uik_network_error_title);
        } else if (this.mStatus == Status.STATUS_EMPTY && TextUtils.isEmpty(this.mTitle)) {
            this.mTitle = getContext().getString(R.string.uik_default_empty_title);
        } else if (this.mError != null && this.mStatus == Status.STATUS_ERROR) {
            this.mTitle = this.mErrorFilter.filterTitle(this.mError, this.mTitle);
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mStatus == Status.STATUS_ERROR && this.mError == null) {
            throwIllegal();
        }
    }

    private class GoToReportListener implements View.OnClickListener {
        private GoToReportListener() {
        }

        public void onClick(View view) {
            String str;
            String str2 = "";
            if (TBErrorView.this.mError != null) {
                str2 = TextUtils.isEmpty(TBErrorView.this.mError.errorCode) ? "" : TBErrorView.this.mError.errorCode;
            }
            String name = TBErrorView.this.getContext().getClass().getName();
            try {
                Object[] objArr = new Object[4];
                objArr[0] = TBErrorView.this.mTitle + "," + TBErrorView.this.mSubTitle;
                objArr[1] = str2;
                objArr[2] = name;
                objArr[3] = TBErrorView.this.mError != null ? URLEncoder.encode(TBErrorView.this.mError.toJSON(), "UTF-8") : "empty";
                str = String.format("https://market.m.taobao.com/markets/client/feedback_detail?wh_weex=true&seCate=BUG&cont=%1s&errCode=%2s&fromPage=%3s&error=%4s", objArr);
            } catch (Exception unused) {
                str = String.format("https://market.m.taobao.com/markets/client/feedback_detail?wh_weex=true&seCate=BUG&cont=%1s&errCode=%2s&fromPage=%3s&error=%4s", new Object[]{TBErrorView.this.mTitle + "," + TBErrorView.this.mSubTitle, str2, name, "empty"});
            }
            Nav.from(TBErrorView.this.getContext()).toUri(str);
        }
    }

    public void setVisibility(int i) {
        super.setVisibility(i);
        if (i == 0 && !this.hasReported) {
            try {
                report();
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        if (i == 0 && this.mError != null) {
            ApmGodEye.onException("UI", "showError", this.mError.errorCode, new Map[]{this.mError.toMap()});
        }
    }

    private void report() {
        doReport(this.mTitle, this.mSubTitle, getContext() != null ? getContext().getClass().getName() : BuildConfig.buildJavascriptFrameworkVersion, this.mError);
        this.hasReported = true;
    }

    public static void doReport(String str, String str2, String str3, Error error) {
        String str4 = BuildConfig.buildJavascriptFrameworkVersion;
        if (error != null && !TextUtils.isEmpty(error.url)) {
            str4 = error.url;
        }
        String str5 = BuildConfig.buildJavascriptFrameworkVersion;
        if (error != null && !TextUtils.isEmpty(error.apiName)) {
            str5 = error.apiName;
        }
        String str6 = BuildConfig.buildJavascriptFrameworkVersion;
        if (error != null && !TextUtils.isEmpty(error.errorCode)) {
            str6 = error.errorCode;
        }
        String str7 = BuildConfig.buildJavascriptFrameworkVersion;
        if (error != null && !TextUtils.isEmpty(error.mappingCode)) {
            str7 = error.mappingCode;
        }
        String str8 = "0";
        if (error != null) {
            str8 = String.valueOf(error.responseCode);
        }
        if (!isMonitorRegistered) {
            isMonitorRegistered = true;
            DimensionSet create = DimensionSet.create();
            for (String addDimension : DEFAULT_DIMENSION_SET) {
                create.addDimension(addDimension);
            }
            MeasureSet create2 = MeasureSet.create();
            for (String addMeasure : DEFAULT_MEASURE_SET) {
                create2.addMeasure(addMeasure);
            }
            AppMonitor.register(MODULE_NAME, MONITOR_POINT, create2, create);
        }
        DimensionValueSet create3 = DimensionValueSet.create();
        if (TextUtils.isEmpty(str3)) {
            str3 = BuildConfig.buildJavascriptFrameworkVersion;
        }
        create3.setValue(UNWRouter.PAGE_NAME, str3);
        create3.setValue("pageURL", str4);
        if (TextUtils.isEmpty(str)) {
            str = BuildConfig.buildJavascriptFrameworkVersion;
        }
        create3.setValue("title", str);
        if (TextUtils.isEmpty(str2)) {
            str2 = BuildConfig.buildJavascriptFrameworkVersion;
        }
        create3.setValue("subtitle", str2);
        create3.setValue("code", str6);
        create3.setValue("mappingCode", str7);
        create3.setValue("responseCode", str8);
        create3.setValue(c.n, str5);
        MeasureValueSet create4 = MeasureValueSet.create();
        create4.setValue("value", 0.0d);
        AppMonitor.Stat.commit(MODULE_NAME, MONITOR_POINT, create3, create4);
    }
}
