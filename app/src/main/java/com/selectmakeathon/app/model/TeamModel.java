package com.selectmakeathon.app.model;

import java.util.List;

public class TeamModel {

    private String teamName;
    private String teamId;
    private String teamLeader;
    private List<String> teamMembers;
    private List<String> memberRequests;
    private AbstractModel abstractModel;
    private boolean isSelected;

    public TeamModel() {

    }

    public TeamModel(String teamName, String teamId, String teamLeader, List<String> teamMembers, List<String> memberRequests, AbstractModel abstractModel, boolean isSelected) {
        this.teamName = teamName;
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

    public String getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(String teamLeader) {
        this.teamLeader = teamLeader;
    }

    public List<String> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<String> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public List<String> getMemberRequests() {
        return memberRequests;
    }

    public void setMemberRequests(List<String> memberRequests) {
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
