package com.sharad.teammates.models;

public class Subject {
    String subject_id; //ID means Name in this Teamates
    String credit;
    String coordinator_name;

    public Subject() {

    }

    public Subject(String subject_id, String credit, String coordinator_name) {
        this.subject_id = subject_id;
        this.credit = credit;
        this.coordinator_name = coordinator_name;
    }


    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getCoordinator_name() {
        return coordinator_name;
    }

    public void setCoordinator_name(String coordinator_name) {
        this.coordinator_name = coordinator_name;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    @Override
    public String toString() {
        return "Subject{" +

                ", credit='" + credit + '\'' +
                ", coordinator_name='" + coordinator_name + '\'' +
                '}';
    }
}
