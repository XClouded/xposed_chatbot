package com.alimama.moon.dinamicx.event;

import android.view.View;
import com.alimama.moon.dinamicx.util.BulletinBoardManager;
import com.alimama.unwdinamicxcontainer.event.IDXEvent;

public class CloseBulletinBoardEvent implements IDXEvent {
    private View mBulletinBoardView;

    public CloseBulletinBoardEvent(View view) {
        this.mBulletinBoardView = view;
    }

    public void executeEvent(String str) {
        if (this.mBulletinBoardView != null) {
            this.mBulletinBoardView.setVisibility(8);
            if (BulletinBoardManager.getInstance().mBulletinBoardModel != null && BulletinBoardManager.getInstance().mBulletinBoardModel.getSwitchData() != null) {
                BulletinBoardManager.getInstance().mBulletinBoardModel.getSwitchData().setIsShowDiscovery("false");
                BulletinBoardManager.getInstance().mBulletinBoardModel.getSwitchData().setIsShowTool("false");
                BulletinBoardManager.getInstance().mBulletinBoardModel.getSwitchData().setIsShowMidH5Tab("false");
                BulletinBoardManager.getInstance().mBulletinBoardModel.getSwitchData().setIsShowReport("false");
                BulletinBoardManager.getInstance().mBulletinBoardModel.getSwitchData().setIsShowMine("false");
            }
        }
    }
}
