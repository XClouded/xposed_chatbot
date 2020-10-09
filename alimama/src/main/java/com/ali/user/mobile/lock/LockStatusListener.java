package com.ali.user.mobile.lock;

import java.util.ArrayList;

public interface LockStatusListener {
    void finishMove(ArrayList<Cell> arrayList);

    void startMove();
}
