package alimama.com.unwviewbase.tool;

import alimama.com.unwbase.tools.UNWLog;
import alimama.com.unwviewbase.abstractview.UNWAbstractDialog;
import android.app.Dialog;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShowDialogActionQueue {
    private static ShowDialogActionQueue sInstance;
    private boolean isShow;
    private List<SoftReference<UNWAbstractDialog>> mActionList = new ArrayList();

    public static ShowDialogActionQueue getInstance() {
        if (sInstance == null) {
            sInstance = new ShowDialogActionQueue();
        }
        return sInstance;
    }

    public void tryToShow(UNWAbstractDialog uNWAbstractDialog) {
        if (uNWAbstractDialog != null) {
            boolean z = false;
            Iterator<SoftReference<UNWAbstractDialog>> it = this.mActionList.iterator();
            while (it.hasNext()) {
                Dialog dialog = (Dialog) it.next().get();
                if (dialog == null) {
                    it.remove();
                } else if (dialog.equals(uNWAbstractDialog)) {
                    z = true;
                }
            }
            if (!z) {
                this.mActionList.add(new SoftReference(uNWAbstractDialog));
            }
            if (this.mActionList.size() != 0 && !this.isShow) {
                Iterator<SoftReference<UNWAbstractDialog>> it2 = this.mActionList.iterator();
                while (it2.hasNext()) {
                    SoftReference next = it2.next();
                    if (!(next == null || next.get() == null)) {
                        this.isShow = ((UNWAbstractDialog) next.get()).doShow();
                        if (!this.isShow) {
                            it2.remove();
                        } else {
                            return;
                        }
                    }
                }
            }
        }
    }

    public void tryToPopUp(UNWAbstractDialog uNWAbstractDialog) {
        if (uNWAbstractDialog != null) {
            Iterator<SoftReference<UNWAbstractDialog>> it = this.mActionList.iterator();
            while (it.hasNext()) {
                Dialog dialog = (Dialog) it.next().get();
                if (dialog == null || dialog.equals(uNWAbstractDialog)) {
                    it.remove();
                }
            }
            this.isShow = false;
            if (this.mActionList.size() != 0) {
                UNWLog.error("aaaaaa  tryToShow");
                tryToShow((UNWAbstractDialog) this.mActionList.get(0).get());
            }
        }
    }
}
