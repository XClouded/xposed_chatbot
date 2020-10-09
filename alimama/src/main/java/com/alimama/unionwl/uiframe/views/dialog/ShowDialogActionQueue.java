package com.alimama.unionwl.uiframe.views.dialog;

import android.app.Dialog;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShowDialogActionQueue {
    private static ShowDialogActionQueue sInstance;
    private boolean isShow;
    private List<SoftReference<ISDialog>> mActionList = new ArrayList();

    public static ShowDialogActionQueue getInstance() {
        if (sInstance == null) {
            sInstance = new ShowDialogActionQueue();
        }
        return sInstance;
    }

    public void tryToShow(ISDialog iSDialog) {
        if (iSDialog != null) {
            boolean z = false;
            Iterator<SoftReference<ISDialog>> it = this.mActionList.iterator();
            while (it.hasNext()) {
                Dialog dialog = (Dialog) it.next().get();
                if (dialog == null) {
                    it.remove();
                } else if (dialog.equals(iSDialog)) {
                    z = true;
                }
            }
            if (!z) {
                this.mActionList.add(new SoftReference(iSDialog));
            }
            if (this.mActionList.size() != 0 && !this.isShow) {
                Iterator<SoftReference<ISDialog>> it2 = this.mActionList.iterator();
                while (it2.hasNext()) {
                    this.isShow = ((ISDialog) it2.next().get()).doShow();
                    if (!this.isShow) {
                        it2.remove();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    public void tryToPopUp(ISDialog iSDialog) {
        if (iSDialog != null) {
            Iterator<SoftReference<ISDialog>> it = this.mActionList.iterator();
            while (it.hasNext()) {
                Dialog dialog = (Dialog) it.next().get();
                if (dialog == null || dialog.equals(iSDialog)) {
                    it.remove();
                }
            }
            this.isShow = false;
            if (this.mActionList.size() != 0) {
                tryToShow((ISDialog) this.mActionList.get(0).get());
            }
        }
    }
}
