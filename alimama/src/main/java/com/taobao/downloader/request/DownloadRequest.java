package com.taobao.downloader.request;

import android.text.TextUtils;
import com.taobao.downloader.util.LogUtil;
import java.util.ArrayList;
import java.util.List;

public class DownloadRequest {
    public List<Item> downloadList;
    public Param downloadParam;

    public DownloadRequest(String str) {
        Item item = new Item();
        item.url = str;
        this.downloadList = new ArrayList(1);
        this.downloadList.add(item);
        this.downloadParam = new Param();
    }

    public DownloadRequest(String... strArr) {
        this.downloadList = new ArrayList();
        for (String str : strArr) {
            Item item = new Item();
            item.url = str;
            this.downloadList.add(item);
        }
        this.downloadParam = new Param();
    }

    public DownloadRequest() {
    }

    public boolean validate() {
        if (this.downloadParam == null || this.downloadList == null || this.downloadList.isEmpty()) {
            LogUtil.warn("downloader", "param is not complete", new Object[0]);
            return false;
        } else if (TextUtils.isEmpty(this.downloadParam.fileStorePath)) {
            LogUtil.warn("downloader", "lack of store path", new Object[0]);
            return false;
        } else {
            for (Item item : this.downloadList) {
                if (TextUtils.isEmpty(item.url)) {
                    LogUtil.warn("downloader", "lack of download url", new Object[0]);
                    return false;
                }
            }
            ArrayList arrayList = new ArrayList();
            for (Item next : this.downloadList) {
                if (!arrayList.contains(next)) {
                    arrayList.add(next);
                }
            }
            this.downloadList = arrayList;
            return true;
        }
    }
}
