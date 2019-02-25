package com.selectmakeathon.app.model;

import android.icu.lang.UScript;

import java.util.List;

public class TeamModel {

    private String teamName;
    private String teamId;
    private UserModel teamLeader;
    private List<UserModel> teamMembers;
    private List<UserModel> memberRequests;
    private AbstractModel abstractModel;
    private boolean isSelected;

    public TeamModel() {

    }

    public TeamModel(String teamName, String teamId, UserModel teamLeader, List<UserModel> teamMembers, List<UserModel> memberRequests, AbstractModel abstractModel, boolean isSelected) { this.teamName = teamName;
        this.teamId = teamId;
        this.teamLeader = teamLeader;
        this.teamMembers = teamMembers;
        this.memberRequests = memberRequests;
        this.abstractModel = abstractModel;
        this.isSelected = isSelected;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public UserModel getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(UserModel teamLeader) {
        this.teamLeader = teamLeader;
    }

    public List<UserModel> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<UserModel> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public List<UserModel> getMemberRequests() {
        return memberRequests;
    }

    public void setMemberRequests(List<UserModel> memberRequests) {
        this.memberRequests = memberRequests;
    }

    public AbstractModel getAbstractModel() {
        return abstractModel;
    }

    public void setAbstractModel(AbstractModel abstractModel) {
        this.abstractModel = abstractModel;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
