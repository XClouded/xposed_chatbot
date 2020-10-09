package com.sndo.chatbot.tblm;

import de.robv.android.xposed.XposedHelpers;

public class MyFlashSaleBlockItem {
    public String itemId;
    public String itemName;
    public String lensId;
    public String picUrl;
    public String srcUrl;
    public String subTkCommissionAmount;

    public String shareTaoToken; //分享口令


    public static MyFlashSaleBlockItem create(Object obj) {
        Object itemId = XposedHelpers.getObjectField(obj, "itemId");
        Object itemName = XposedHelpers.getObjectField(obj, "itemName");
        Object lensId = XposedHelpers.getObjectField(obj, "lensId");
        Object srcUrl = XposedHelpers.getObjectField(obj, "srcUrl");
        Object picUrl = XposedHelpers.getObjectField(obj, "picUrl");
        Object subTkCommissionAmount = XposedHelpers.getObjectField(obj, "subTkCommissionAmount");

        MyFlashSaleBlockItem blockItem = new MyFlashSaleBlockItem();
        blockItem.itemId = null != itemId ? String.valueOf(itemId) : null;
        blockItem.itemName = null != itemId ? String.valueOf(itemName) : null;
        blockItem.lensId = null != itemId ? String.valueOf(lensId) : null;
        blockItem.srcUrl = null != itemId ? String.valueOf(srcUrl) : null;
        blockItem.picUrl = null != itemId ? String.valueOf(picUrl) : null;
        blockItem.subTkCommissionAmount = null != itemId ? String.valueOf(subTkCommissionAmount) : null;


        /**
         *         public int itemDrawLimitNum;
         *         public int itemDrawTotalNum;
         *         public String priceAfterAllRights; //抢购价
         *         public String totalRightsFace;  //优惠金额(优惠券+桃礼金)
         */
        Object vegasField = XposedHelpers.getObjectField(obj, "vegasField");
        if (null != vegasField) {
            Object itemDrawLimitNum = XposedHelpers.getObjectField(vegasField, "itemDrawLimitNum");
            Object itemDrawTotalNum = XposedHelpers.getObjectField(vegasField, "itemDrawTotalNum");
            Object priceAfterAllRights = XposedHelpers.getObjectField(vegasField, "priceAfterAllRights");
            Object totalRightsFace = XposedHelpers.getObjectField(vegasField, "totalRightsFace");
            System.out.println("vegasField.itemDrawLimitNum=" + itemDrawLimitNum);
            System.out.println("vegasField.itemDrawTotalNum=" + itemDrawTotalNum);
            System.out.println("egasField.priceAfterAllRights=" + priceAfterAllRights);
            System.out.println("egasField.totalRightsFace=" + totalRightsFace);
        }
        return blockItem;
    }

    @Override
    public String toString() {
        return "MyFlashSaleBlockItem{" +
                "itemId='" + itemId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", lensId='" + lensId + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", srcUrl='" + srcUrl + '\'' +
                ", subTkCommissionAmount='" + subTkCommissionAmount + '\'' +
                ", shareTaoToken='" + shareTaoToken + '\'' +
                '}';
    }
}
