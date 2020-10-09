package alimama.com.unwbase.interfaces;

public interface IDiskCache extends IInitAction {
    Object getDataFromDisk(String str);

    void putDataToDisk(String str, Object obj);
}
