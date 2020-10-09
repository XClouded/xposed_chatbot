package com.alimama.union.app.contact.model;

import java.util.List;

public class BatchSms {
    private List<String> phones;
    private String text;

    public List<String> getPhones() {
        return this.phones;
    }

    public void setPhones(List<String> list) {
        this.phones = list;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String str) {
        this.text = str;
    }
}
