package com.sndo.chatbot.tblm;

import android.text.TextUtils;

import de.robv.android.xposed.XposedHelpers;

//{"itemId":"603968965519", "itemName":"浴室挡水条卫生间隔水阻水条淋浴房可弯曲隔断自粘硅胶防水神器","picUrl":"//img.alicdn.com/bao/uploaded/i1/1923001282/O1CN01hNEHQm1LLCNn5n3kt_!!0-item_pic.jpg", "srcUrl":"https://uland.taobao.com/taolijin/detail?pid=mm_492050103_687500054_109455200491&relationId=&vegasCode=mExpubJ2&union_spk=mNkSNu1zXo26UUQQUY67vI%2BiiB9AdUwkNOu2BGvhOJ582sb2s732RKwsTu9VrUpx%2BvEo9qIciENtc%2FWeX0nw%2BfGwoGiM2ax%2F&srcAppkey=&unid=&ptl=floorId%3A32351%3Bapp_pvid%3A59590_11.186.132.66_561_1602495454680%3Btpp_pvid%3Af7261762-0820-49ca-9db8-be3ecec5f42a%3Bcps_campaign_id%3A&itemId=603968965519&union_lens=lensId%3AAPP%401602495454%40f7261762-0820-49ca-9db8-be3ecec5f42a_603968965519%40022ItNvT1zkxjfHDS6qu1snN&spm=a21wq.9116673.rebate.data1_6", "subTkCommissionAmount":"1.65", "vegasField":{"itemDrawLimitNum":"50", "itemDrawTotalNum":"50", "priceAfterAllRights":"11.69", "totalRightsFace":"3.11"}, "shareTaoToken":""}
public class MyFlashSaleBlockItem {
    public String itemId;
    public String itemName;
    public String lensId;
    public String picUrl; //图片url
    public String srcUrl; //详情页url
    public String subTkCommissionAmount; //赚取金额
    public MyVegasFieldItem vegasField;

    public String shareTaoToken; //分享口令

    public static class MyVegasFieldItem {
        public String itemDrawLimitNum; //剩余件数
        public String itemDrawTotalNum; //前50名
        public String priceAfterAllRights; //抢购价
        public String totalRightsFace; //优惠金额(优惠券+桃礼金)

        @Override
        public String toString() {
            return "MyVegasFieldItem{" +
                    "itemDrawLimitNum='" + itemDrawLimitNum + '\'' +
                    ", itemDrawTotalNum='" + itemDrawTotalNum + '\'' +
                    ", priceAfterAllRights='" + priceAfterAllRights + '\'' +
                    ", totalRightsFace='" + totalRightsFace + '\'' +
                    '}';
        }
    }


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
        if (!TextUtils.isEmpty(blockItem.picUrl) && !blockItem.picUrl.startsWith("http")) {
            blockItem.picUrl = "https:" + blockItem.picUrl;
        }
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
            MyVegasFieldItem myVegasFieldItem = new MyVegasFieldItem();
            myVegasFieldItem.itemDrawLimitNum = null != itemDrawLimitNum ? String.valueOf(itemDrawLimitNum) : null;
            myVegasFieldItem.itemDrawTotalNum = null != itemDrawTotalNum ? String.valueOf(itemDrawTotalNum) : null;
            myVegasFieldItem.priceAfterAllRights = null != priceAfterAllRights ? String.valueOf(priceAfterAllRights) : null;
            myVegasFieldItem.totalRightsFace = null != totalRightsFace ? String.valueOf(totalRightsFace) : null;

            blockItem.vegasField = myVegasFieldItem;

            System.out.println("vegasField.itemDrawLimitNum=" + itemDrawLimitNum);
            System.out.println("vegasField.itemDrawTotalNum=" + itemDrawTotalNum);
            System.out.println("vegasField.priceAfterAllRights=" + priceAfterAllRights);
            System.out.println("vegasField.totalRightsFace=" + totalRightsFace);
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
                ", vegasField=" + vegasField +
                ", shareTaoToken='" + shareTaoToken + '\'' +
                '}';
    }
}
