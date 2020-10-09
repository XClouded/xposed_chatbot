package com.alimamaunion.base.configcenter;

public interface IConfigCenterCache {
    ConfigData read(String str);

    void remove(String str);

    void write(String str, ConfigData configData);
}
