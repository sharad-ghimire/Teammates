package com.sharad.teammates.models;

public class Match {
    private String matchStudentId;
    private String studentName;
    private String profileImage;

    public Match(String studentUId, String studentName, String profileImage){
        this.matchStudentId = studentUId;
        this.studentName = studentName;
        this.profileImage = profileImage;

    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public Match(String matchStudentIdId) {
        this.matchStudentId = matchStudentIdId;
    }

    public String getMatchStudentId() {
        return matchStudentId;
    }

    public void setMatchStudentId(String matchStudentId) {
        this.matchStudentId = matchStudentId;
    }
}
