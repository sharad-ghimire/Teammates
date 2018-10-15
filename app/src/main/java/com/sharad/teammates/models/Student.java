package com.sharad.teammates.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Student implements Serializable {

    private String user_id;
    private String name;
    private String age;
    private String email;
    private String uni_id;
    private String major_id;
    private String subjects;
    private String student_id;
    private String profile_image;
    private String is_coordinator;
    private String bio;
    private String interest;
    private String skills;

    public Student(String user_id, String name, String age, String email, String uni_id, String major_id, String subjects, String student_id, String profile_image, String is_coordinator, String bio, String interest, String skills) {
        this.user_id = user_id;
        this.name = name;
        this.age = age;
        this.email = email;
        this.uni_id = uni_id;
        this.major_id = major_id;
        this.subjects = subjects;
        this.student_id = student_id;
        this.profile_image = profile_image;
        this.is_coordinator = is_coordinator;
        this.bio = bio;
        this.interest = interest;
        this.skills = skills;
    }

    public Student() {

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUni_id() {
        return uni_id;
    }

    public void setUni_id(String uni_id) {
        this.uni_id = uni_id;
    }

    public String getMajor_id() {
        return major_id;
    }

    public void setMajor_id(String major_id) {
        this.major_id = major_id;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getIs_coordinator() {
        return is_coordinator;
    }

    public void setIs_coordinator(String is_coordinator) {
        this.is_coordinator = is_coordinator;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }
}
