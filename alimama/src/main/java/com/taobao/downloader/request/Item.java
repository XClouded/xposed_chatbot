package com.taobao.downloader.request;

public class Item {
    public String md5;
    public String name;
    public long size;
    public String url;

    public Item() {
    }

    public Item(String str) {
        this.url = str;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Item)) {
            return false;
        }
        Item item = (Item) obj;
        if (this.url == null ? item.url != null : !this.url.equals(item.url)) {
            return false;
        }
        if (this.name != null) {
            if (!this.name.equals(item.name)) {
                return false;
            }
            return true;
        } else if (item.name == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int i = 0;
        int hashCode = (this.url != null ? this.url.hashCode() : 0) * 31;
        if (this.name != null) {
            i = this.name.hashCode();
        }
        return hashCode + i;
    }

    public String toString() {
        return "DownloadItem{url='" + this.url + '\'' + ", size=" + this.size + ", md5='" + this.md5 + '\'' + ", name='" + this.name + '\'' + '}';
    }
}
