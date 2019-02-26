package com.selectmakeathon.app.ui.main.myTeam;

import com.selectmakeathon.app.model.UserModel;

public interface PendingMemberListener {

    public void onAcceptUser(UserModel userModel);

    public void onRejectUser(UserModel userModel);

}
