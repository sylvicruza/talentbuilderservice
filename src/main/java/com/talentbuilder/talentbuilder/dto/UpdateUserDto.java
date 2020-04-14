package com.talentbuilder.talentbuilder.dto;

public class UpdateUserDto {

    private String firstName;
    private String lastName;
    private String biography;
    private String website;
    private String twitter;
    private String facebook;
    private String linkendin;
    private String assignMentor;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getLinkendin() {
        return linkendin;
    }

    public void setLinkendin(String linkendin) {
        this.linkendin = linkendin;
    }

    public String getAssignMentor() {
        return assignMentor;
    }

    public void setAssignMentor(String assignMentor) {
        this.assignMentor = assignMentor;
    }
}
