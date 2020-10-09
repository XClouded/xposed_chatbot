package com.alimama.moon.features.home.network;

import android.text.TextUtils;
import com.alimama.moon.features.home.item.Constants;
import com.alimama.moon.utils.CommonUtils;
import com.alimama.moon.utils.UnionLensUtil;
import com.alimama.union.app.logger.BusinessMonitorLogger;
import com.alimama.union.app.rxnetwork.ApiInfo;
import com.alimama.union.app.rxnetwork.RxMtopRequest;
import com.alimama.union.app.rxnetwork.RxMtopResponse;
import com.alimama.union.app.rxnetwork.RxPageRequest;
import com.alimamaunion.base.safejson.SafeJSONObject;
import java.util.Map;
import org.greenrobot.eventbus.EventBus;

public class HomeIndexRequest extends RxPageRequest<HomeDataResponse> implements RxMtopRequest.RxMtopResult<HomeDataResponse> {
    private static final int PAGE_SIZE = 20;
    private static final String PAGING_PARAM_NUM = "pageSize";
    private static final String PAGING_PARAM_START_INDEX = "pageNum";
    private String cateId;
    private HomeDataResponse mHomeDataResponse;
    private int mPageNum;
    private int mPosition;

    public HomeIndexRequest(int i, String str, String str2, String str3, String str4) {
        super(ApiInfo.API_HOME_INDEX);
        appendParam("cateId", str);
        this.cateId = str;
        appendParam("extendParam", UnionLensUtil.geneRidPvidJsonStr());
        this.mPosition = i;
        if (!TextUtils.isEmpty(str4)) {
            appendParam("spm", str4);
        }
        if (!TextUtils.isEmpty(str3) && TextUtils.isEmpty(str2)) {
            appendParam("qieId", str3);
        }
        if (TextUtils.isEmpty(str3) && !TextUtils.isEmpty(str2)) {
            appendParam("floorId", str2);
        }
        setRxMtopResult(this);
    }

    /* access modifiers changed from: protected */
    public void prepareFirstParams(Map<String, String> map) {
        map.put(PAGING_PARAM_START_INDEX, "0");
        map.put("pageSize", String.valueOf(20));
        HomeDataResponse.reset();
        this.mPageNum = 0;
    }

    /* access modifiers changed from: protected */
    public void prepareNextParams(Map<String, String> map) {
        int safeIntValue = CommonUtils.getSafeIntValue(map.get(PAGING_PARAM_START_INDEX)) + 1;
        if (this.mHomeDataResponse == null) {
            map.put(PAGING_PARAM_START_INDEX, String.valueOf(this.mPageNum));
            return;
        }
        if (this.mHomeDataResponse.isWorshipHasMore()) {
            map.put("pageNumBlockType", Constants.APPRENTICE_TYPE_NAME);
        } else {
            map.put("pageNumBlockType", "");
        }
        if (this.mHomeDataResponse.shouldResetPageNum()) {
            this.mPageNum = 1;
        } else {
            this.mPageNum = safeIntValue;
        }
        map.put(PAGING_PARAM_START_INDEX, String.valueOf(this.mPageNum));
    }

    public HomeDataResponse decodeResult(SafeJSONObject safeJSONObject) {
        this.mHomeDataResponse = new HomeDataResponse(safeJSONObject, this.mPageNum, this);
        if (TextUtils.equals(this.cateId, "discovery") && this.mPageNum == 0 && !this.mHomeDataResponse.hasUnionRebate() && !this.mHomeDataResponse.hasWorship()) {
            BusinessMonitorLogger.UnionRebate.show("HomeFragment");
        }
        return this.mHomeDataResponse;
    }

    public void result(RxMtopResponse<HomeDataResponse> rxMtopResponse) {
        HomeDataEvent homeDataEvent = new HomeDataEvent();
        homeDataEvent.isSuccess = rxMtopResponse.isReqSuccess;
        homeDataEvent.mHomeData = (HomeDataResponse) rxMtopResponse.result;
        homeDataEvent.mRetCode = rxMtopResponse.retCode;
        homeDataEvent.mPosition = this.mPosition;
        EventBus.getDefault().post(homeDataEvent);
    }
}
