package com.alimama.moon.features.home.network;

import android.text.TextUtils;
import com.alimama.moon.features.home.item.Constants;
import com.alimama.moon.features.home.item.HomeTabCateItem;
import com.alimamaunion.base.safejson.SafeJSONArray;
import com.alimamaunion.base.safejson.SafeJSONObject;
import com.alimamaunion.common.listpage.CommonItemInfo;
import com.taobao.android.ultron.datamodel.imp.ProtocolConst;
import java.util.ArrayList;
import java.util.List;

public class HomeDataResponse {
    private static final String COMMON_ITEM = "common_item";
    private static final String RECOMMEND_ITEM = "recommend_item";
    private static final String RECOMMEND_TITLE = "recommend_item_title";
    private static final String SALES_CARD = "union_sales_card";
    private static final String TAB_CATEGORY = "union_tab_category";
    private static final String UNION_REBATE = "union_rebate";
    private static boolean mHasWorshipHasMoreChanged = false;
    private static boolean sIsInsertApprenticeTitle = false;
    private static boolean sIsInsertRecommentTitle = false;
    private boolean mHasMore;
    private boolean mHasUnionRebate;
    private boolean mHasWorship;
    public List<CommonItemInfo> mHomeDataList = new ArrayList();
    public List<HomeTabCateItem> mHomeTabList = new ArrayList();
    private boolean mWorshipHasMore;

    public HomeDataResponse(SafeJSONObject safeJSONObject, int i, HomeIndexRequest homeIndexRequest) {
        this.mHomeDataList.clear();
        this.mHasUnionRebate = false;
        this.mHasWorship = false;
        SafeJSONObject optJSONObject = safeJSONObject.optJSONObject("data");
        this.mHasMore = TextUtils.equals("1", optJSONObject.optString(ProtocolConst.KEY_HAS_MORE));
        this.mWorshipHasMore = TextUtils.equals("1", optJSONObject.optString("worshipHasMore"));
        SafeJSONArray optJSONArray = optJSONObject.optJSONArray("blocks");
        for (int i2 = 0; i2 < optJSONArray.length(); i2++) {
            SafeJSONObject optJSONObject2 = optJSONArray.optJSONObject(i2);
            String optString = optJSONObject2.optString("type");
            if (TextUtils.equals(optString, TAB_CATEGORY)) {
                parseHomeTab(optJSONObject2.optJSONArray("data"));
            } else if (TextUtils.equals(optString, SALES_CARD)) {
                parseItems(optJSONObject2.optJSONArray("data"), optString);
            } else if (TextUtils.equals(optString, RECOMMEND_ITEM)) {
                if (!sIsInsertRecommentTitle) {
                    sIsInsertRecommentTitle = true;
                    insertRecommendTitle();
                }
                parseItems(optJSONObject2.optJSONArray("data"), optString);
            } else if (TextUtils.equals(optString, COMMON_ITEM)) {
                parseItems(optJSONObject2.optJSONArray("data"), optString);
            } else if (TextUtils.equals(optString, Constants.APPRENTICE_TYPE_NAME)) {
                this.mHasWorship = true;
                if (!sIsInsertApprenticeTitle) {
                    sIsInsertApprenticeTitle = true;
                    insertApprenticeTitle();
                }
                parseItems(optJSONObject2.optJSONArray("data"), optString);
            } else {
                CommonItemInfo createCommonItem = CommonItemInfo.createCommonItem(optString, optJSONObject2);
                if (createCommonItem != null) {
                    this.mHomeDataList.add(createCommonItem);
                }
            }
            if (TextUtils.equals(optString, "union_rebate")) {
                this.mHasUnionRebate = true;
            }
        }
    }

    public boolean hasUnionRebate() {
        return this.mHasUnionRebate;
    }

    public boolean hasWorship() {
        return this.mHasWorship;
    }

    public boolean shouldResetPageNum() {
        if (mHasWorshipHasMoreChanged || this.mWorshipHasMore || !this.mHasMore) {
            return false;
        }
        mHasWorshipHasMoreChanged = true;
        return true;
    }

    public static void reset() {
        sIsInsertRecommentTitle = false;
        sIsInsertApprenticeTitle = false;
        mHasWorshipHasMoreChanged = false;
    }

    public boolean isWorshipHasMore() {
        return this.mWorshipHasMore;
    }

    public boolean isHasMore() {
        return this.mHasMore || this.mWorshipHasMore;
    }

    private void parseHomeTab(SafeJSONArray safeJSONArray) {
        for (int i = 0; i < safeJSONArray.length(); i++) {
            HomeTabCateItem homeTabCateItem = new HomeTabCateItem();
            homeTabCateItem.setTitle(safeJSONArray.optJSONObject(i).optString("name"));
            homeTabCateItem.setType(safeJSONArray.optJSONObject(i).optString("type"));
            homeTabCateItem.setFloorId(safeJSONArray.optJSONObject(i).optString("floorId"));
            homeTabCateItem.setQieId(safeJSONArray.optJSONObject(i).optString("qieId"));
            homeTabCateItem.setSpm(safeJSONArray.optJSONObject(i).optString("spm"));
            this.mHomeTabList.add(homeTabCateItem);
        }
    }

    private void parseItems(SafeJSONArray safeJSONArray, String str) {
        for (int i = 0; i < safeJSONArray.length(); i++) {
            CommonItemInfo createCommonItem = CommonItemInfo.createCommonItem(str, safeJSONArray.optJSONObject(i));
            if (createCommonItem != null) {
                this.mHomeDataList.add(createCommonItem);
            }
        }
    }

    private void insertRecommendTitle() {
        CommonItemInfo createCommonItem = CommonItemInfo.createCommonItem(RECOMMEND_TITLE, new SafeJSONObject());
        if (createCommonItem != null) {
            this.mHomeDataList.add(createCommonItem);
        }
    }

    private void insertApprenticeTitle() {
        CommonItemInfo createCommonItem = CommonItemInfo.createCommonItem(Constants.APPRENTICE_TITLE_TYPE_NAME, new SafeJSONObject());
        if (createCommonItem != null) {
            this.mHomeDataList.add(createCommonItem);
        }
    }
}
