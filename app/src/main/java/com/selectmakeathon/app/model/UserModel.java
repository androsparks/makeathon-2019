package com.selectmakeathon.app.model;

import java.util.ArrayList;

public class UserModel {

    private String name;
    private String email;
    private String regNo;
    private String hashPassword;
    private String whatsNo;
    private String phNo;
    private boolean isVitian;
    private String collegeName;
    private boolean isHosteler;
    private String hostelBlock;
    private String roomNo;
    private String branch;
    private String gender;
    private ArrayList<String> skillSet;
    private boolean isLeader;
    private boolean isJoined;

    public UserModel() {

    }

    public UserModel(
            String name, String email, String regNo, String hashPassword, String whatsNo,
            String phNo, boolean isVitian, String collegeName, boolean isHosteler, String roomNo,
            String hostelBlock, String branch, String gender, ArrayList<String> skillSet,
            boolean isLeader, boolean isJoined
    ) {
        this.name = name;
        this.email = email;
        this.regNo = regNo;
        this.hashPassword = hashPassword;
        this.whatsNo = whatsNo;
        this.phNo = phNo;
        this.isVitian = isVitian;
        this.collegeName = collegeName;
        this.isHosteler = isHosteler;
        this.roomNo = roomNo;
        this.hostelBlock = hostelBlock;
        this.branch = branch;
        this.gender = gender;
        this.skillSet = skillSet;
        this.isLeader = isLeader;
        this.isJoined = isJoined;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getWhatsNo() {
        return whatsNo;
    }

    public void setWhatsNo(String whatsNo) {
        this.whatsNo = whatsNo;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getPhNo() {
        return phNo;
    }

    public void setPhNo(String phNo) {
        this.phNo = phNo;
    }

    public boolean isVitian() {
        return isVitian;
    }

    public void setVitian(boolean vitian) {
        isVitian = vitian;
    }

    public boolean isHosteler() {
        return isHosteler;
    }

    public void setHosteler(boolean hosteler) {
        isHosteler = hosteler;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getHostelBlock() {
        return hostelBlock;
    }

    public void setHostelBlock(String hostelBlock) {
        this.hostelBlock = hostelBlock;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public ArrayList<String> getSkillSet() {
        return skillSet;
    }

    public void setSkillSet(ArrayList<String> skillSet) {
        this.skillSet = skillSet;
    }

    public boolean isLeader() {
        return isLeader;
    }

    public void setLeader(boolean leader) {
        isLeader = leader;
    }

    public boolean isJoined() {
        return isJoined;
    }

    public void setJoined(boolean joined) {
        isJoined = joined;
    }
}
