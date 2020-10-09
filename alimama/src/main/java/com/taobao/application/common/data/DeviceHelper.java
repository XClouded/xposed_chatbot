package com.taobao.application.common.data;

public class DeviceHelper extends AbstractHelper {
    public void setMobileModel(String str) {
        this.preferences.putString("mobileModel", str);
    }

    public void setDeviceLevel(int i) {
        this.preferences.putInt("deviceLevel", i);
    }

    public void setCpuScore(int i) {
        this.preferences.putInt("cpuScore", i);
    }

    public void setMemScore(int i) {
        this.preferences.putInt("memScore", i);
    }

    public void setOldDeviceScore(int i) {
        this.preferences.putInt("oldDeviceScore", i);
    }

    public void setCpuBrand(String str) {
        this.preferences.putString("cpuBrand", str);
    }

    public void setCpuModel(String str) {
        this.preferences.putString("cpuModel", str);
    }

    public void setGpuBrand(String str) {
        this.preferences.putString("gpuBrand", str);
    }

    public void setGpuModel(String str) {
        this.preferences.putString("gpuModel", str);
    }
}
