package com.sndo.chatbot.tblm;

public class TabItem {
    public String floorId;
    public String qieId;
    public String spm;
    public String title;
    public String type;

    @Override
    public String toString() {
        return "TabItem{" +
                "floorId='" + floorId + '\'' +
                ", qieId='" + qieId + '\'' +
                ", spm='" + spm + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
