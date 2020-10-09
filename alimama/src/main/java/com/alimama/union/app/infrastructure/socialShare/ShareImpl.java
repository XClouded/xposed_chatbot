package com.alimama.union.app.infrastructure.socialShare;

public class ShareImpl implements Share {
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void doShare(android.content.Context r11, com.alimama.union.app.infrastructure.socialShare.ShareObj r12, final com.alimama.union.app.infrastructure.socialShare.Share.Callback r13) {
        /*
            r10 = this;
            com.alimama.union.app.infrastructure.image.download.UniversalImageDownloader r0 = new com.alimama.union.app.infrastructure.image.download.UniversalImageDownloader
            com.nostra13.universalimageloader.core.ImageLoader r1 = com.nostra13.universalimageloader.core.ImageLoader.getInstance()
            com.alimama.union.app.infrastructure.image.save.ExternalPublicStorageSaver r2 = com.alimama.union.app.infrastructure.image.save.ExternalPublicStorageSaver.getInstance()
            com.alimama.union.app.infrastructure.executor.AsyncTaskManager r3 = com.alimama.union.app.infrastructure.executor.AsyncTaskManager.getInstance()
            r0.<init>(r1, r2, r3)
            com.alimama.union.app.infrastructure.socialShare.ShareImpl$1 r9 = new com.alimama.union.app.infrastructure.socialShare.ShareImpl$1
            r9.<init>(r13)
            java.lang.String r13 = r12.getPlatform()
            int r1 = r13.hashCode()
            switch(r1) {
                case -1838124510: goto L_0x004b;
                case -1656144897: goto L_0x0041;
                case -951770676: goto L_0x0037;
                case -904024897: goto L_0x002c;
                case -393543490: goto L_0x0022;
                default: goto L_0x0021;
            }
        L_0x0021:
            goto L_0x0056
        L_0x0022:
            java.lang.String r1 = "qqfriend"
            boolean r13 = r13.equals(r1)
            if (r13 == 0) goto L_0x0056
            r13 = 3
            goto L_0x0057
        L_0x002c:
            java.lang.String r1 = "wxfriend"
            boolean r13 = r13.equals(r1)
            if (r13 == 0) goto L_0x0056
            r13 = 0
            goto L_0x0057
        L_0x0037:
            java.lang.String r1 = "qqzone"
            boolean r13 = r13.equals(r1)
            if (r13 == 0) goto L_0x0056
            r13 = 4
            goto L_0x0057
        L_0x0041:
            java.lang.String r1 = "sinaweibo"
            boolean r13 = r13.equals(r1)
            if (r13 == 0) goto L_0x0056
            r13 = 2
            goto L_0x0057
        L_0x004b:
            java.lang.String r1 = "wxtimeline"
            boolean r13 = r13.equals(r1)
            if (r13 == 0) goto L_0x0056
            r13 = 1
            goto L_0x0057
        L_0x0056:
            r13 = -1
        L_0x0057:
            switch(r13) {
                case 0: goto L_0x00b4;
                case 1: goto L_0x009e;
                case 2: goto L_0x0088;
                case 3: goto L_0x0072;
                case 4: goto L_0x005c;
                default: goto L_0x005a;
            }
        L_0x005a:
            goto L_0x00c9
        L_0x005c:
            com.alimama.union.app.infrastructure.socialShare.social.QQZoneShare r4 = new com.alimama.union.app.infrastructure.socialShare.social.QQZoneShare
            r4.<init>(r11, r0)
            java.lang.String r6 = r12.getText()
            java.util.List r7 = r12.getImages()
            java.lang.String r8 = r12.getUrl()
            r5 = r12
            r4.doShare(r5, r6, r7, r8, r9)
            goto L_0x00c9
        L_0x0072:
            com.alimama.union.app.infrastructure.socialShare.social.QQFriendShare r4 = new com.alimama.union.app.infrastructure.socialShare.social.QQFriendShare
            r4.<init>(r11, r0)
            java.lang.String r6 = r12.getText()
            java.util.List r7 = r12.getImages()
            java.lang.String r8 = r12.getUrl()
            r5 = r12
            r4.doShare(r5, r6, r7, r8, r9)
            goto L_0x00c9
        L_0x0088:
            com.alimama.union.app.infrastructure.socialShare.social.WeiboShare r4 = new com.alimama.union.app.infrastructure.socialShare.social.WeiboShare
            r4.<init>(r11, r0)
            java.lang.String r6 = r12.getText()
            java.util.List r7 = r12.getImages()
            java.lang.String r8 = r12.getUrl()
            r5 = r12
            r4.doShare(r5, r6, r7, r8, r9)
            goto L_0x00c9
        L_0x009e:
            com.alimama.union.app.infrastructure.socialShare.social.WeixinTimelineShare r4 = new com.alimama.union.app.infrastructure.socialShare.social.WeixinTimelineShare
            r4.<init>(r11, r0)
            java.lang.String r6 = r12.getText()
            java.util.List r7 = r12.getImages()
            java.lang.String r8 = r12.getUrl()
            r5 = r12
            r4.doShare(r5, r6, r7, r8, r9)
            goto L_0x00c9
        L_0x00b4:
            com.alimama.union.app.infrastructure.socialShare.social.WeixinFriendShare r4 = new com.alimama.union.app.infrastructure.socialShare.social.WeixinFriendShare
            r4.<init>(r11, r0)
            java.lang.String r6 = r12.getText()
            java.util.List r7 = r12.getImages()
            java.lang.String r8 = r12.getUrl()
            r5 = r12
            r4.doShare(r5, r6, r7, r8, r9)
        L_0x00c9:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alimama.union.app.infrastructure.socialShare.ShareImpl.doShare(android.content.Context, com.alimama.union.app.infrastructure.socialShare.ShareObj, com.alimama.union.app.infrastructure.socialShare.Share$Callback):void");
    }
}
