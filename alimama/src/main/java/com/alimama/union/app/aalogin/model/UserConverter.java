package com.alimama.union.app.aalogin.model;

import com.alimama.union.app.aalogin.ILogin;
import com.alimama.union.app.network.response.UserGradedetailGetResponseData;
import com.alimama.union.app.personalCenter.model.GradeThreshold;

public final class UserConverter {
    public static User convertFromUserGradedetailGetResponseData(ILogin iLogin, UserGradedetailGetResponseData userGradedetailGetResponseData) {
        User user = new User();
        user.setUserId(iLogin.getUserId());
        user.setUserNick(iLogin.getNick());
        user.setAvatarLink(iLogin.getAvatarLink());
        user.setMemberId(String.valueOf(userGradedetailGetResponseData.getMemberId()));
        user.setType(userGradedetailGetResponseData.getType());
        user.setFinishTypeDisp(userGradedetailGetResponseData.getFinishTypeDisp());
        user.setFinishTypeDispDateStartIndex(userGradedetailGetResponseData.getFinishTypeDispDateStartIndex());
        user.setFinishTypeDispDateEndIndex(userGradedetailGetResponseData.getFinishTypeDispDateEndIndex());
        user.setGradeString(userGradedetailGetResponseData.getGradeString());
        user.setTaskStartTime(userGradedetailGetResponseData.getTaskStartTime());
        user.setTaskEndTime(userGradedetailGetResponseData.getTaskEndTime());
        user.setTaskStartShowTime(userGradedetailGetResponseData.getTaskStartShowTime());
        user.setTaskEndShowTime(userGradedetailGetResponseData.getTaskEndShowTime());
        user.setLastTaskStartTime(userGradedetailGetResponseData.getLastTaskStartTime());
        user.setLastTaskEndTime(userGradedetailGetResponseData.getLastTaskEndTime());
        user.setCurrentTotalOrder(userGradedetailGetResponseData.getCurrentTotalOrder());
        user.setCurrentTotalUV(userGradedetailGetResponseData.getCurrentTotalUV());
        user.setOrderFinishRate(userGradedetailGetResponseData.getOrderFinishRate());
        user.setUvFinishRate(userGradedetailGetResponseData.getUvFinishRate());
        user.setNextUpdateTime(userGradedetailGetResponseData.getNextUpdateTime());
        user.setButlerPrivilege(userGradedetailGetResponseData.getButlerPrivilege());
        user.setWalletPrivilege(userGradedetailGetResponseData.getWalletPrivilege());
        GradeThreshold gradeThreshold = new GradeThreshold();
        gradeThreshold.setValidOrderNum(userGradedetailGetResponseData.getThreshold().getValidOrderNum());
        gradeThreshold.setCheckOrderNum(userGradedetailGetResponseData.getThreshold().getCheckOrderNum());
        gradeThreshold.setMinValidOrderNum(userGradedetailGetResponseData.getThreshold().getMinValidOrderNum());
        gradeThreshold.setTotalUv(userGradedetailGetResponseData.getThreshold().getTotalUv());
        user.setThreshold(gradeThreshold);
        user.setIsInRisk(userGradedetailGetResponseData.getIsInRisk());
        user.setGrade(userGradedetailGetResponseData.getGrade());
        return user;
    }
}
