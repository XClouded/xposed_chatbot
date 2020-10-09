package com.alimama.moon.features.search.network;

public class SearchHotTagItem {
    private String url;
    private String word;

    public SearchHotTagItem(String str, String str2) {
        this.url = str2;
        this.word = str;
    }

    public String getWord() {
        return this.word;
    }

    public void setWord(String str) {
        this.word = str;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
    }
}
