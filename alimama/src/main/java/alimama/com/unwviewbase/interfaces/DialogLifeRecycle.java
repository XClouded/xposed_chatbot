package alimama.com.unwviewbase.interfaces;

import alimama.com.unwviewbase.abstractview.UNWAbstractDialog;

public interface DialogLifeRecycle {
    void dismiss(UNWAbstractDialog uNWAbstractDialog, boolean z);

    void onShow(UNWAbstractDialog uNWAbstractDialog);
}
