package com.selectmakeathon.app.model;

import java.util.List;

public class Problems {

    List<ProblemTrack> problemTracks;

}

class ProblemTrack {

    List<ProblemStatements> problemStatements;

}

class ProblemStatements {

    private String id;
    private String problemStatement;
    private String company;
    private int numOfTeams;

}
