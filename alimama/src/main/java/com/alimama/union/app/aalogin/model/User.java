package com.alimama.union.app.aalogin.model;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.alimama.union.app.personalCenter.model.GradeThreshold;
import java.util.Date;

@Entity
public class User {
    private String avatarLink;
    private Integer butlerPrivilege;
    private Long currentTotalOrder;
    private Long currentTotalUV;
    private String finishTypeDisp;
    private Integer finishTypeDispDateEndIndex;
    private Integer finishTypeDispDateStartIndex;
    private Integer grade;
    private String gradeString;
    private Integer isInRisk;
    private Date lastTaskEndTime;
    private Date lastTaskStartTime;
    private String memberId;
    private String nextUpdateTime;
    private Integer orderFinishRate;
    private String taskEndShowTime;
    private Date taskEndTime;
    private String taskStartShowTime;
    private Date taskStartTime;
    @Embedded
    private GradeThreshold threshold;
    private Integer type;
    @NonNull
    @PrimaryKey
    private String userId;
    private String userNick;
    private Integer uvFinishRate;
    private Integer walletPrivilege;

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String str) {
        this.userId = str;
    }

    public String getUserNick() {
        return this.userNick;
    }

    public void setUserNick(String str) {
        this.userNick = str;
    }

    public String getAvatarLink() {
        return this.avatarLink;
    }

    public void setAvatarLink(String str) {
        this.avatarLink = str;
    }

    public String getMemberId() {
        return this.memberId;
    }

    public void setMemberId(String str) {
        this.memberId = str;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer num) {
        this.type = num;
    }

    public String getFinishTypeDisp() {
        return this.finishTypeDisp;
    }

    public void setFinishTypeDisp(String str) {
        this.finishTypeDisp = str;
    }

    public Integer getFinishTypeDispDateStartIndex() {
        return this.finishTypeDispDateStartIndex;
    }

    public void setFinishTypeDispDateStartIndex(Integer num) {
        this.finishTypeDispDateStartIndex = num;
    }

    public Integer getFinishTypeDispDateEndIndex() {
        return this.finishTypeDispDateEndIndex;
    }

    public void setFinishTypeDispDateEndIndex(Integer num) {
        this.finishTypeDispDateEndIndex = num;
    }

    public String getGradeString() {
        return this.gradeString;
    }

    public void setGradeString(String str) {
        this.gradeString = str;
    }

    public Date getTaskStartTime() {
        return this.taskStartTime;
    }

    public void setTaskStartTime(Date date) {
        this.taskStartTime = date;
    }

    public Date getTaskEndTime() {
        return this.taskEndTime;
    }

    public void setTaskEndTime(Date date) {
        this.taskEndTime = date;
    }

    public String getTaskStartShowTime() {
        return this.taskStartShowTime;
    }

    public void setTaskStartShowTime(String str) {
        this.taskStartShowTime = str;
    }

    public String getTaskEndShowTime() {
        return this.taskEndShowTime;
    }

    public void setTaskEndShowTime(String str) {
        this.taskEndShowTime = str;
    }

    public Date getLastTaskStartTime() {
        return this.lastTaskStartTime;
    }

    public void setLastTaskStartTime(Date date) {
        this.lastTaskStartTime = date;
    }

    public Date getLastTaskEndTime() {
        return this.lastTaskEndTime;
    }

    public void setLastTaskEndTime(Date date) {
        this.lastTaskEndTime = date;
    }

    public Long getCurrentTotalOrder() {
        return this.currentTotalOrder;
    }

    public void setCurrentTotalOrder(Long l) {
        this.currentTotalOrder = l;
    }

    public Long getCurrentTotalUV() {
        return this.currentTotalUV;
    }

    public void setCurrentTotalUV(Long l) {
        this.currentTotalUV = l;
    }

    public Integer getOrderFinishRate() {
        return this.orderFinishRate;
    }

    public void setOrderFinishRate(Integer num) {
        this.orderFinishRate = num;
    }

    public Integer getUvFinishRate() {
        return this.uvFinishRate;
    }

    public void setUvFinishRate(Integer num) {
        this.uvFinishRate = num;
    }

    public String getNextUpdateTime() {
        return this.nextUpdateTime;
    }

    public void setNextUpdateTime(String str) {
        this.nextUpdateTime = str;
    }

    public GradeThreshold getThreshold() {
        return this.threshold;
    }

    public void setThreshold(GradeThreshold gradeThreshold) {
        this.threshold = gradeThreshold;
    }

    public Integer getButlerPrivilege() {
        return this.butlerPrivilege;
    }

    public void setButlerPrivilege(Integer num) {
        this.butlerPrivilege = num;
    }

    public Integer getWalletPrivilege() {
        return this.walletPrivilege;
    }

    public void setWalletPrivilege(Integer num) {
        this.walletPrivilege = num;
    }

    public Integer getIsInRisk() {
        return this.isInRisk;
    }

    public void setIsInRisk(Integer num) {
        this.isInRisk = num;
    }

    public Integer getGrade() {
        return this.grade;
    }

    public void setGrade(Integer num) {
        this.grade = num;
    }
}
