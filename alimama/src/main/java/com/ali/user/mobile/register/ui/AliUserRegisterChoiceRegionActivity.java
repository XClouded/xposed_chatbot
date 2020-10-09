package com.ali.user.mobile.register.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import com.ali.user.mobile.base.helper.SDKExceptionHelper;
import com.ali.user.mobile.base.ui.BaseActivity;
import com.ali.user.mobile.data.LoginComponent;
import com.ali.user.mobile.exception.RpcException;
import com.ali.user.mobile.model.MtopCountryCodeContextResult;
import com.ali.user.mobile.model.MtopRegisterInitcontextResponseData;
import com.ali.user.mobile.model.RegionInfo;
import com.ali.user.mobile.register.RegistConstants;
import com.ali.user.mobile.register.model.BaseRegistRequest;
import com.ali.user.mobile.register.service.impl.UserRegisterServiceImpl;
import com.ali.user.mobile.ui.R;
import com.ali.user.mobile.ui.widget.AUBladeView;
import com.ali.user.mobile.ui.widget.AUPinnedHeaderListView;
import com.ali.user.mobile.utils.BackgroundExecutor;
import com.ali.user.mobile.utils.CountryCodeUtil;
import com.ali.user.mobile.utils.MainThreadExecutor;
import com.alibaba.aliweex.adapter.module.location.ILocatable;
import com.alibaba.analytics.utils.MapUtils;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTHitBuilders;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import org.json.JSONObject;

public class AliUserRegisterChoiceRegionActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    public static final int CHOOSE_REGION_REQUEST = 2001;
    private boolean isFromJSBridge;
    /* access modifiers changed from: private */
    public boolean isFromLogin;
    private RegionAdapter mAdapter;
    protected String mErrorMessage;
    protected String mHotCountryTitle;
    private List<String> mLetterList;
    /* access modifiers changed from: private */
    public HashMap<String, Integer> mLetterMap;
    protected AUBladeView mLetterView;
    private List<RegionInfo> mList;
    protected AUPinnedHeaderListView mListView;
    protected String mLocale;
    private String mTitle;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        if (getIntent() != null) {
            try {
                this.isFromLogin = getIntent().getBooleanExtra("from_login", false);
                this.isFromJSBridge = getIntent().getBooleanExtra("from_jsbridge", false);
                this.mTitle = getIntent().getStringExtra("title");
                this.mHotCountryTitle = getIntent().getStringExtra("hotCountryTitle");
                this.mLocale = getIntent().getStringExtra("locale");
                this.mErrorMessage = getIntent().getStringExtra(ILocatable.ERROR_MSG);
            } catch (Throwable unused) {
            }
        }
        super.onCreate(bundle);
    }

    /* access modifiers changed from: protected */
    public int getLayoutContent() {
        return R.layout.ali_user_register_region;
    }

    /* access modifiers changed from: protected */
    public void initViews() {
        this.mLetterView = (AUBladeView) findViewById(R.id.contacts_letters_list);
        this.mListView = (AUPinnedHeaderListView) findViewById(R.id.register_list);
        if (getSupportActionBar() != null) {
            if (TextUtils.isEmpty(this.mTitle)) {
                this.mTitle = getResources().getString(R.string.aliuser_choose_region);
            }
            getSupportActionBar().setTitle((CharSequence) this.mTitle);
        }
        init();
    }

    private void init() {
        this.mList = getIntent().getParcelableArrayListExtra(RegistConstants.REGION_INFO);
        this.mLetterMap = (HashMap) getIntent().getSerializableExtra(RegistConstants.LETTER);
        this.mLetterList = getIntent().getStringArrayListExtra(RegistConstants.LETTER_STR);
        if (this.mLetterMap == null) {
            this.mLetterMap = new HashMap<>();
        }
        if (this.mLetterList == null) {
            this.mLetterList = new ArrayList();
        }
        if (this.mList == null || this.mLetterMap == null || this.mLetterList == null || this.mLetterList.size() <= 0) {
            getCountryCode();
        } else {
            setupAdapter();
        }
    }

    private void setupAdapter() {
        this.mAdapter = new RegionAdapter(this, this.mList);
        this.mListView.setAdapter((ListAdapter) this.mAdapter);
        this.mListView.setPinnedHeaderView(LayoutInflater.from(this).inflate(R.layout.aliuser_contact_list_head, this.mListView, false));
        this.mListView.setOnItemClickListener(this);
        this.mLetterView.setOnItemClickListener(new AUBladeView.OnItemClickListener() {
            public void onItemClick(String str) {
                int intValue;
                if (str != null && AliUserRegisterChoiceRegionActivity.this.mLetterMap.containsKey(str) && (intValue = ((Integer) AliUserRegisterChoiceRegionActivity.this.mLetterMap.get(str)).intValue()) != -1) {
                    AliUserRegisterChoiceRegionActivity.this.mListView.setSelection(intValue);
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void getCountryCode() {
        BackgroundExecutor.execute(new Runnable() {
            public void run() {
                MtopRegisterInitcontextResponseData mtopRegisterInitcontextResponseData;
                try {
                    AliUserRegisterChoiceRegionActivity.this.startAnimation();
                    HashMap hashMap = new HashMap();
                    BaseRegistRequest baseRegistRequest = new BaseRegistRequest();
                    baseRegistRequest.ext = hashMap;
                    if (AliUserRegisterChoiceRegionActivity.this.isFromLogin) {
                        mtopRegisterInitcontextResponseData = (MtopRegisterInitcontextResponseData) LoginComponent.getInstance().getCountryList(AliUserRegisterChoiceRegionActivity.this.mLocale);
                    } else {
                        mtopRegisterInitcontextResponseData = UserRegisterServiceImpl.getInstance().countryCodeRes(baseRegistRequest);
                    }
                    AliUserRegisterChoiceRegionActivity.this.getCountryCodeReq(mtopRegisterInitcontextResponseData);
                } catch (RpcException e) {
                    AliUserRegisterChoiceRegionActivity.this.stopAnimation();
                    SDKExceptionHelper.getInstance().rpcExceptionHandler(e);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void getCountryCodeReq(final MtopRegisterInitcontextResponseData mtopRegisterInitcontextResponseData) {
        MainThreadExecutor.execute(new Runnable() {
            public void run() {
                AliUserRegisterChoiceRegionActivity.this.afterCountryCode(mtopRegisterInitcontextResponseData);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void afterCountryCode(MtopRegisterInitcontextResponseData mtopRegisterInitcontextResponseData) {
        dismissProgressDialog();
        if (mtopRegisterInitcontextResponseData != null && mtopRegisterInitcontextResponseData.returnValue != null) {
            if (((MtopCountryCodeContextResult) mtopRegisterInitcontextResponseData.returnValue).countrycodes != null) {
                if (TextUtils.isEmpty(this.mHotCountryTitle)) {
                    this.mHotCountryTitle = getResources().getString(R.string.aliuser_common_region);
                }
                this.mList = CountryCodeUtil.fillData(this.mHotCountryTitle, ((MtopCountryCodeContextResult) mtopRegisterInitcontextResponseData.returnValue).countrycodes, this.mLetterMap, this.mLetterList);
                if (this.mList == null || this.mLetterMap == null || this.mLetterList == null || this.mLetterList.size() <= 0) {
                    if (TextUtils.isEmpty(this.mErrorMessage)) {
                        this.mErrorMessage = getResources().getString(R.string.aliuser_network_error);
                    }
                    toast(this.mErrorMessage, 3000);
                    return;
                }
                setupAdapter();
                return;
            }
            toast(mtopRegisterInitcontextResponseData.message, 3000);
        }
    }

    /* access modifiers changed from: package-private */
    public void startAnimation() {
        showProgress("");
    }

    /* access modifiers changed from: package-private */
    public void stopAnimation() {
        MainThreadExecutor.execute(new Runnable() {
            public void run() {
                AliUserRegisterChoiceRegionActivity.this.dismissProgressDialog();
            }
        });
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        RegionInfo regionInfo = this.mList.get(i);
        Intent intent = new Intent();
        if (!this.isFromJSBridge) {
            intent.putExtra(RegistConstants.REGION_INFO, regionInfo);
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("checkPattern", regionInfo.checkPattern);
            jSONObject.put("code", regionInfo.code);
            jSONObject.put("pinyin", regionInfo.pinyin);
            jSONObject.put("domain", regionInfo.domain);
            jSONObject.put("name", regionInfo.name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        intent.putExtra("regionString", jSONObject.toString());
        Properties properties = new Properties();
        properties.put("position", String.valueOf(i));
        properties.put("countryCode", regionInfo.domain);
        UTHitBuilders.UTCustomHitBuilder uTCustomHitBuilder = new UTHitBuilders.UTCustomHitBuilder("List_Reg_selectCountry");
        uTCustomHitBuilder.setProperties(MapUtils.convertPropertiesToMap(properties));
        UTAnalytics.getInstance().getDefaultTracker().send(uTCustomHitBuilder.build());
        setResult(-1, intent);
        finish();
    }

    public void onBackPressed() {
        setResult(0);
        finish();
    }
}
