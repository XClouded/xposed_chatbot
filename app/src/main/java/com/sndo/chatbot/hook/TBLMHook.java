package com.sndo.chatbot.hook;

import android.app.AlarmManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.TimeUtils;

import com.sndo.chatbot.R;
import com.sndo.chatbot.tblm.MyFlashSaleBlockItem;
import com.sndo.chatbot.tblm.TabItem;


import org.json.JSONObject;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class TBLMHook implements IXposedHookLoadPackage {

    private static final String PROCESS_NAME = "com.alimama.moon";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        System.out.println("进程：" + lpparam.processName);
        if (!lpparam.processName.equals(PROCESS_NAME)) {
            return;
        }
        System.out.println("进入淘宝联盟进程：" + lpparam.processName);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startHook(lpparam);
            }
        }, 5000);

    }

    Class<?> SpmProcessorCls;
    Class<?> ShareInfoRequest2Cls;
    Class<?> shareUtilCls;
    Class<?> requestCls;

    private void startHook(XC_LoadPackage.LoadPackageParam lpparam) {
        System.out.println("当前线程=" + Thread.currentThread().getName());
        requestCls = XposedHelpers.findClassIfExists("com.alimama.moon.features.home.network.HomeIndexRequest", lpparam.classLoader);
        System.out.println("requestCls=" + requestCls);

        SpmProcessorCls = XposedHelpers.findClassIfExists("com.alimama.moon.utils.SpmProcessor", lpparam.classLoader);
        System.out.println("SpmProcessorCls=" + SpmProcessorCls);

        ShareInfoRequest2Cls = XposedHelpers.findClassIfExists("com.alimama.union.app.share.flutter.network.ShareInfoRequest2", lpparam.classLoader);
        System.out.println("ShareInfoRequest2Cls=" + ShareInfoRequest2Cls);

        shareUtilCls = XposedHelpers.findClassIfExists("com.alimama.union.app.infrastructure.socialShare.social.ShareUtils", lpparam.classLoader);
        System.out.println("shareUtilCls=" + shareUtilCls);


        Class<?> responseCls = XposedHelpers.findClassIfExists("com.alimama.union.app.rxnetwork.RxMtopResponse", lpparam.classLoader);
        System.out.println("responseCls=" + responseCls);

        //处理响应
        try {
            XposedHelpers.findAndHookMethod(requestCls, "result", responseCls, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam hookParam) throws Throwable {
                    Object response = hookParam.args[0];
                    System.out.println("response=" + response);
                    Object result = XposedHelpers.getObjectField(response, "result");
                    System.out.println("result=" + result);
                    if (null == result) {
                        return;
                    }
                    String simpleName = result.getClass().getSimpleName();
                    System.out.println("simpleName=" + simpleName);
                    if (TextUtils.equals(simpleName, "HomeDataResponse")) {

//                    private boolean mHasMore;
//                    public List<CommonItemInfo> mHomeDataList = new ArrayList();
//                    public List<HomeTabCateItem> mHomeTabList = new ArrayList();

                        boolean isReqSuccess = XposedHelpers.getBooleanField(response, "isReqSuccess");
                        System.out.println("isReqSuccess=" + isReqSuccess);
                        if (isReqSuccess) {

                            List mHomeDataList = (List) XposedHelpers.getObjectField(result, "mHomeDataList");
                            System.out.println("mHomeDataList=" + mHomeDataList);
                            parseDataList(mHomeDataList);

//                            List mHomeTabList = (List) XposedHelpers.getObjectField(result, "mHomeTabList");
//                            System.out.println("mHomeTabList=" + mHomeTabList);
//                            parseTabList(mHomeTabList);

                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        //淘宝口令
//        try {
//            Class<?> TaoCodeReqManagerCls = XposedHelpers.findClassIfExists("com.alimama.union.app.share.TaoCodeReqManager", lpparam.classLoader);
//            System.out.println("TaoCodeReqManagerCls=" + TaoCodeReqManagerCls);
//            XposedHelpers.findAndHookMethod(TaoCodeReqManagerCls, "result", responseCls, new XC_MethodHook() {
//                @Override
//                protected void afterHookedMethod(MethodHookParam hookParam) throws Throwable {
//                    Object response = hookParam.args[0];
//                    System.out.println("response=" + response);
//                    Object result = XposedHelpers.getObjectField(response, "result");
//                    System.out.println("result=" + result); //TaoCodeResponse
//                    if (null == result) {
//                        return;
//                    }
//
//                    boolean isReqSuccess = XposedHelpers.getBooleanField(response, "isReqSuccess");
//                    System.out.println("isReqSuccess=" + isReqSuccess);
//                    if (isReqSuccess) {
//                        Object mTaoCode = XposedHelpers.getObjectField(result, "mTaoCode");
//                        Object mTinyLink = XposedHelpers.getObjectField(result, "mTinyLink");
//
//                        System.out.println("mTaoCode=" + mTaoCode);
//                        System.out.println("mTinyLink=" + mTinyLink);
//                    }
//                }
//
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        //淘宝口令2  分享页面是ShareFlutterActivity
        try {
            Class<?> SafeJSONObjectCls = XposedHelpers.findClassIfExists(" com.alimamaunion.base.safejson.SafeJSONObject", lpparam.classLoader);
            XposedHelpers.findAndHookMethod(ShareInfoRequest2Cls, "decodeResult", SafeJSONObjectCls, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam hookParam) throws Throwable {
                    Object data = hookParam.args[0];
                    System.out.println("data=" + data);
                    JSONObject jsonObject = new JSONObject(String.valueOf(data));
                    JSONObject dataJson = jsonObject.optJSONObject("data");
                    String status = dataJson.optString("status");
                    if (TextUtils.equals(status, "NORMAL")) {
                        String shareTaoToken = dataJson.optString("shareTaoToken");
                        String itemId = dataJson.optString("itemId");
                        try {
                            if (TextUtils.isEmpty(itemId)) {
                                String clickUrl = dataJson.optString("clickUrl");
                                itemId = Uri.parse(clickUrl).getQueryParameter("itemId");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        System.out.println("itemId=" + itemId + ",shareTaoToken=" + shareTaoToken);

                        MyFlashSaleBlockItem blockItem = blockMap.get(itemId);
                        if (null != blockItem) {
                            blockItem.shareTaoToken = shareTaoToken;
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        startTask();
    }

    List<TabItem> tabItems = new ArrayList<>();

    private void parseTabList(List mHomeTabList) {
        if (null != mHomeTabList && !mHomeTabList.isEmpty()) {
            tabItems.clear();
            for (Object item : mHomeTabList) {
                /**
                 *     private String floorId;
                 *     private String qieId;
                 *     private String spm;
                 *     private String title;
                 *     private String type;
                 */

                Object floorId = XposedHelpers.getObjectField(item, "floorId");
                Object qieId = XposedHelpers.getObjectField(item, "qieId");
                Object spm = XposedHelpers.getObjectField(item, "spm");
                Object title = XposedHelpers.getObjectField(item, "title");
                Object type = XposedHelpers.getObjectField(item, "type");

                TabItem tabItem = new TabItem();
                tabItem.floorId = null != floorId ? String.valueOf(floorId) : null;
                tabItem.qieId = null != qieId ? String.valueOf(qieId) : null;
                tabItem.spm = null != spm ? String.valueOf(spm) : null;
                tabItem.title = null != title ? String.valueOf(title) : null;
                tabItem.type = null != type ? String.valueOf(type) : null;

                tabItems.add(tabItem);
            }

            System.out.println("tabItems=" + tabItems);
        }
    }

    private void parseDataList(List mHomeDataList) {
        if (null != mHomeDataList && !mHomeDataList.isEmpty()) {
            for (Object item : mHomeDataList) {
//                private static Class[] baseParam = {String.class, Integer.TYPE, JSONObject.class};
//                private static CommonItemInfo[] sCommonItemInfos = new CommonItemInfo[0];
//                public CommonBaseItem commonBaseItem;
//                public Class<? extends CommonBaseViewHolder> holderClass;
//                public int priority;
//                public int spanSize;
//                public String type;
//                public Class<? extends CommonBaseItem> viewClass;
//                public int viewType;

//                Object baseParam = XposedHelpers.getObjectField(item, "baseParam");
//                Object sCommonItemInfos = XposedHelpers.getObjectField(item, "sCommonItemInfos");
                Object commonBaseItem = XposedHelpers.getObjectField(item, "commonBaseItem");
                Object holderClass = XposedHelpers.getObjectField(item, "holderClass");
//                Object priority = XposedHelpers.getObjectField(item, "priority");
//                Object spanSize = XposedHelpers.getObjectField(item, "spanSize");
//                Object type = XposedHelpers.getObjectField(item, "type");
//                Object viewClass = XposedHelpers.getObjectField(item, "viewClass");
//                Object viewType = XposedHelpers.getObjectField(item, "viewType");

                System.out.println("-----------------------------");
//                System.out.println("baseParam=" + baseParam);
//                System.out.println("sCommonItemInfos=" + sCommonItemInfos);
                System.out.println("commonBaseItem=" + commonBaseItem);
                System.out.println("holderClass=" + holderClass);
//                System.out.println("viewClass=" + viewClass);
//                System.out.println("priority=" + priority);
//                System.out.println("spanSize=" + spanSize);
//                System.out.println("type=" + type);
//                System.out.println("viewType=" + viewType);

                /**
                 * commonBaseItem 有多种类型，参考com.alimama.moon.features.home.item包
                 */

                String simpleName = commonBaseItem.getClass().getSimpleName();
                switch (simpleName) {
                    case "HomeCommonTabItem":
//                        parseHomeCommonTabItem(commonBaseItem);
                        break;
                    case "HomeBannerItem":
                        break;
                    case "HomeCircleNavItem":
                        break;
                    case "HomeSaleBlockItem":
                        break;
                    case "HomeFlashSaleBlock":
                        /**
                         * 09-29 16:50:10.808 2375-2375/com.alimama.moon I/System.out: commonBaseItem=com.alimama.moon.features.home.item.HomeFlashSaleBlock@d601a58
                         * 09-29 16:50:10.808 2375-2375/com.alimama.moon I/System.out: holderClass=class com.alimama.moon.features.home.viewholder.HomeFlashSaleViewHolder
                         * 09-29 16:50:10.808 2375-2375/com.alimama.moon I/System.out: viewClass=class com.alimama.moon.features.home.item.HomeFlashSaleBlock
                         */
                        System.out.println("--HomeFlashSaleBlock");

                        Object moreSrc = XposedHelpers.getObjectField(commonBaseItem, "moreSrc");
                        Object name = XposedHelpers.getObjectField(commonBaseItem, "name");
                        Object ruleUrl = XposedHelpers.getObjectField(commonBaseItem, "ruleUrl");
                        System.out.println("moreSrc=" + moreSrc);
                        System.out.println("name=" + name);
                        System.out.println("ruleUrl=" + ruleUrl);

                        Map<String, MyFlashSaleBlockItem> blockMap = new HashMap<>();
                        List list = (List) XposedHelpers.getObjectField(commonBaseItem, "itemList");
                        for (Object obj : list) {
                            /**
                             *     public String itemId;
                             *     public String itemName;
                             *     public String lensId;
                             *     public String picUrl;
                             *     public String srcUrl;
                             *     public String subTkCommissionAmount;
                             *     public VegasFieldItem vegasField;
                             */
                            System.out.println("----------");
                            MyFlashSaleBlockItem blockItem = MyFlashSaleBlockItem.create(obj);
                            System.out.println("blockItem=" + blockItem);
                            blockMap.put(blockItem.itemId, blockItem);
                        }
                        getHomeShareInfo(blockMap);
                        break;
                    case "HomeCardItem":
//                         list= (List) XposedHelpers.getObjectField(commonBaseItem,"itemList");
//                        for(Object obj:list){
//                            Object itemName = XposedHelpers.getObjectField(obj, "itemName");
//                            System.out.println("itemName="+itemName);
//                        }
                        break;
                    case "HomeRecommendTitleItem":
                        break;
                    case "HomeRecommendItem":
                        break;
                }

            }
        }
    }

    private void parseHomeCommonTabItem(Object commonBaseItem) {
        Object goodItem = XposedHelpers.getObjectField(commonBaseItem, "baseGoodsItem");
        parseGoods(goodItem);
    }

    private void parseGoods(Object good) {
        /**
         *     public String auctionIconUrl;
         *     public String calTkCommission;
         *     public String couponAmount;
         *     public String floorID;
         *     public String itemDrawLimitNum;
         *     public String itemDrawTotalNum;
         *     public String itemId;
         *     public String itemName;
         *     public String lensId;
         *     public String monthSellCount;
         *     public String pic;
         *     public String price;
         *     public String priceAfterAllRights;
         *     public String priceAfterCoupon;
         *     public String promotionPrice;
         *     public String recommendReasons;
         *     public String subTkCommissionAmount;
         *     public String tkCommissionAmount;
         *     public String totalRightsFace;
         *     public String url;
         */

        //解析商品
        Object itemId = XposedHelpers.getObjectField(good, "itemId");
        Object itemName = XposedHelpers.getObjectField(good, "itemName");
        Object url = XposedHelpers.getObjectField(good, "url");

        System.out.println("--------商品数据-----");
        System.out.println("itemId=" + itemId);
        System.out.println("itemName=" + itemName);
        System.out.println("url=" + url);

        if (String.valueOf(itemName).contains("现做buh蛋黄酥雪媚娘麻薯糕点心网红零食办公室小吃休闲食品")) {
            Object destUrl = XposedHelpers.callStaticMethod(SpmProcessorCls, "getDestUrl", url, "a21wq.9116673.0.0", false);
            System.out.println("destUrl=" + destUrl);

            XposedHelpers.callStaticMethod(shareUtilCls, "addSpmUrl", String.valueOf(destUrl));
            getShareInfo(destUrl);
        }
    }

    private void getShareInfo(Object url) {
        try {
            Object shareRequest = XposedHelpers.newInstance(
                    ShareInfoRequest2Cls,
                    new Class[]{String.class},
                    url);
            System.out.println("shareRequest=" + shareRequest);
            XposedHelpers.callMethod(shareRequest, "sendRequest");
            System.out.println("sendRequest");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //-------------------------------------------------------------------

    /**
     * 0-7点之间不获取
     * 间隔：1小时+(0-10分钟)
     */
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            Calendar instance = Calendar.getInstance();
//            int hourOfDay = instance.get(Calendar.HOUR_OF_DAY);
//            System.out.println("hourOfDay=" + hourOfDay);
//            if (hourOfDay <= 7) { //
//                handler.sendEmptyMessageDelayed(1, getDelayTime());
//                return;
//            }
//            sendRequest();
//            handler.sendEmptyMessageDelayed(1, getDelayTime());
//        }
//    };
    private void startTask() {
//        handler.sendEmptyMessageDelayed(1, 5 * 1000);
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Calendar instance = Calendar.getInstance();
                int hourOfDay = instance.get(Calendar.HOUR_OF_DAY);
                if (hourOfDay <= 7) {
                    return;
                }
                sendRequest();
            }
        }, 5 * 1000, getDelayTime());
    }

    private long getDelayTime() {
        long randomTime = Math.round(Math.random() * 10 * 60 * 1000); // 0-10分钟
        long delayTime = AlarmManager.INTERVAL_HOUR + randomTime;
        return delayTime;
    }

    private void sendRequest() {
        //发现
        Object discoveryRequest = null;
        try {
            discoveryRequest = XposedHelpers.newInstance(
                    requestCls,
                    new Class[]{int.class, String.class, String.class, String.class, String.class},
                    0, "discovery", "", "", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("discoveryRequest=" + discoveryRequest);
        if (null == discoveryRequest) {
            return;
        }

        //发起请求
        try {
            XposedHelpers.callMethod(discoveryRequest, "queryFirstPage");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static Map<String, MyFlashSaleBlockItem> blockMap;

    //
    private void getHomeShareInfo(Map<String, MyFlashSaleBlockItem> map) {
        System.out.println("getHomeShareInfo size=" + map.size());
        blockMap = map;

        int index = 1;
        for (Map.Entry<String, MyFlashSaleBlockItem> entry : blockMap.entrySet()) {
            String key = entry.getKey();
            MyFlashSaleBlockItem value = entry.getValue();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    System.out.println("---------------");
                    System.out.println("itemId=" + value.itemId + ",item.name=" + value.itemName);
                    Object destUrl = XposedHelpers.callStaticMethod(SpmProcessorCls, "getDestUrl", value.srcUrl, "a21wq.9116673.rebate", false);
                    System.out.println("destUrl=" + destUrl);
                    XposedHelpers.callStaticMethod(shareUtilCls, "addSpmUrl", String.valueOf(destUrl));
                    getShareInfo(destUrl);
                }
            }, index * 1000);
            index += 2;
        }
    }


}
