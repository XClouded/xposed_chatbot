package com.taobao.android.dinamicx.template.db;

import com.taobao.android.dinamicx.template.db.DXDataBaseEntry;

@DXDataBaseEntry.Table("template_info")
class DXFileDataBaseEntry extends DXDataBaseEntry {
    static final DXDataBaseEntrySchema SCHEMA = new DXDataBaseEntrySchema(DXFileDataBaseEntry.class);
    @DXDataBaseEntry.Column(indexed = true, notNull = true, primaryKey = true, value = "biz_type")
    public String bizType;
    @DXDataBaseEntry.Column("extra_1")
    public String extra1;
    @DXDataBaseEntry.Column("extra_2")
    public String extra2;
    @DXDataBaseEntry.Column("extra_3")
    public String extra3;
    @DXDataBaseEntry.Column("extra_4")
    public String extra4;
    @DXDataBaseEntry.Column("extra_5")
    public String extra5;
    @DXDataBaseEntry.Column("extra_6")
    public String extra6;
    @DXDataBaseEntry.Column("extra_7")
    public String extra7;
    @DXDataBaseEntry.Column("extra_8")
    public String extra8;
    @DXDataBaseEntry.Column(notNull = true, value = "main_path")
    public String mainPath;
    @DXDataBaseEntry.Column(indexed = true, notNull = true, primaryKey = true, value = "name")
    public String name;
    @DXDataBaseEntry.Column("style_files")
    public String styleFiles;
    @DXDataBaseEntry.Column("url")
    public String url;
    @DXDataBaseEntry.Column(notNull = true, primaryKey = true, value = "version")
    public long version;

    interface Columns extends DXDataBaseEntry.Columns {
        public static final String BIZ_TYPE = "biz_type";
        public static final String EXTRA_1 = "extra_1";
        public static final String EXTRA_2 = "extra_2";
        public static final String EXTRA_3 = "extra_3";
        public static final String EXTRA_4 = "extra_4";
        public static final String EXTRA_5 = "extra_5";
        public static final String EXTRA_6 = "extra_6";
        public static final String EXTRA_7 = "extra_7";
        public static final String EXTRA_8 = "extra_8";
        public static final String MAIN_PATH = "main_path";
        public static final String NAME = "name";
        public static final String STYLE_FILES = "style_files";
        public static final String URL = "url";
        public static final String VERSION = "version";
    }

    DXFileDataBaseEntry() {
    }

    public String toString() {
        return "DXFileDataBaseEntry{bizType='" + this.bizType + '\'' + ", name='" + this.name + '\'' + ", version=" + this.version + ", mainPath='" + this.mainPath + '\'' + ", styleFiles='" + this.styleFiles + '\'' + ", url='" + this.url + '\'' + ", extra1='" + this.extra1 + '\'' + ", extra2='" + this.extra2 + '\'' + ", extra3='" + this.extra3 + '\'' + ", extra4='" + this.extra4 + '\'' + ", extra5='" + this.extra5 + '\'' + ", extra6='" + this.extra6 + '\'' + ", extra7='" + this.extra7 + '\'' + ", extra8='" + this.extra8 + '\'' + '}';
    }
}
