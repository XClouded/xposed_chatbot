package alimama.com.unwbase.interfaces;

public interface IMemoryCache extends IInitAction {
    Object getDateFromMemory(String str);

    void putDataToMemory(String str, Object obj);

    void removeDataFromMemory(String str);
}
