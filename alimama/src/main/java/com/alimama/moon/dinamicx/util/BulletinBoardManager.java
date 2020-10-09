package com.alimama.moon.dinamicx.util;

import android.text.TextUtils;
import android.widget.LinearLayout;
import com.alimama.moon.dinamicx.model.BulletinBoardModel;
import com.alimama.moon.features.home.HomeFragment;
import com.alimama.moon.features.reports.ReportFragment;
import com.alimama.moon.ui.IBottomNavFragment;
import com.alimama.union.app.personalCenter.view.NewMineFragment;
import com.alimama.union.app.toolCenter.view.ToolFragment;
import com.alimama.union.app.webContainer.WebFragment;

public class BulletinBoardManager {
    private static BulletinBoardManager sInstance;
    public BulletinBoardModel mBulletinBoardModel;
    private LinearLayout mDxBulletinBoard;

    private BulletinBoardManager() {
    }

    public static BulletinBoardManager getInstance() {
        if (sInstance == null) {
            sInstance = new BulletinBoardManager();
        }
        return sInstance;
    }

    public void init(BulletinBoardModel bulletinBoardModel, LinearLayout linearLayout) {
        this.mBulletinBoardModel = bulletinBoardModel;
        this.mDxBulletinBoard = linearLayout;
    }

    public void showBulletinBoard(IBottomNavFragment iBottomNavFragment) {
        if (iBottomNavFragment != null && this.mBulletinBoardModel != null && this.mBulletinBoardModel.getSwitchData() != null && this.mDxBulletinBoard != null) {
            if (iBottomNavFragment instanceof HomeFragment) {
                if (TextUtils.equals(this.mBulletinBoardModel.getSwitchData().getIsShowDiscovery(), "true")) {
                    this.mDxBulletinBoard.setVisibility(0);
                } else {
                    this.mDxBulletinBoard.setVisibility(8);
                }
            } else if (iBottomNavFragment instanceof ToolFragment) {
                if (TextUtils.equals(this.mBulletinBoardModel.getSwitchData().getIsShowTool(), "true")) {
                    this.mDxBulletinBoard.setVisibility(0);
                } else {
                    this.mDxBulletinBoard.setVisibility(8);
                }
            } else if (iBottomNavFragment instanceof WebFragment) {
                if (TextUtils.equals(this.mBulletinBoardModel.getSwitchData().getIsShowMidH5Tab(), "true")) {
                    this.mDxBulletinBoard.setVisibility(0);
                } else {
                    this.mDxBulletinBoard.setVisibility(8);
                }
            } else if (iBottomNavFragment instanceof ReportFragment) {
                if (TextUtils.equals(this.mBulletinBoardModel.getSwitchData().getIsShowReport(), "true")) {
                    this.mDxBulletinBoard.setVisibility(0);
                } else {
                    this.mDxBulletinBoard.setVisibility(8);
                }
            } else if (!(iBottomNavFragment instanceof NewMineFragment)) {
            } else {
                if (TextUtils.equals(this.mBulletinBoardModel.getSwitchData().getIsShowMine(), "true")) {
                    this.mDxBulletinBoard.setVisibility(0);
                } else {
                    this.mDxBulletinBoard.setVisibility(8);
                }
            }
        }
    }

    public boolean isShowBulletinBoard() {
        if (this.mBulletinBoardModel == null || this.mBulletinBoardModel.getSwitchData() == null) {
            return false;
        }
        if (TextUtils.equals(this.mBulletinBoardModel.getSwitchData().getIsShowDiscovery(), "true") || TextUtils.equals(this.mBulletinBoardModel.getSwitchData().getIsShowTool(), "true") || TextUtils.equals(this.mBulletinBoardModel.getSwitchData().getIsShowMidH5Tab(), "true") || TextUtils.equals(this.mBulletinBoardModel.getSwitchData().getIsShowReport(), "true") || TextUtils.equals(this.mBulletinBoardModel.getSwitchData().getIsShowMine(), "true")) {
            return true;
        }
        return false;
    }
}
