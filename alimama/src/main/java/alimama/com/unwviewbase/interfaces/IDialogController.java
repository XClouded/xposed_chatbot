package alimama.com.unwviewbase.interfaces;

import alimama.com.unwbase.interfaces.IInitAction;

public interface IDialogController<T> extends IInitAction {
    boolean commit(T t);

    void setSwitch(String str, String str2);

    void updateEvent(String str, String str2);

    void updateEvent(String str, String str2, boolean z);
}
