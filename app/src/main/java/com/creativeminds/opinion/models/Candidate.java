package com.creativeminds.opinion.models;

import com.google.gson.annotations.SerializedName;

public class Candidate {

    @SerializedName("poll_position")
    private String pollPosition;

    @SerializedName("poll_id")
    private String pollId;

    @SerializedName("occupation")
    private String occupation;

    @SerializedName("party_name")
    private String partyName;

    @SerializedName("gender")
    private String gender;

    @SerializedName("dob")
    private String dob;

    @SerializedName("name")
    private String name;

    @SerializedName("location")
    private String location;

    @SerializedName("symbol_photo_url")
    private String symbolPhotoUrl;

    @SerializedName("cid")
    private String cid;

    @SerializedName("votes")
    private String votes;

    public void setPollPosition(String pollPosition) {
        this.pollPosition = pollPosition;
    }

    public String getPollPosition() {
        return pollPosition;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public String getPollId() {
        return pollId;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDob() {
        return dob;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public String getVotes() {
        return votes;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setSymbolPhotoUrl(String symbolPhotoUrl) {
        this.symbolPhotoUrl = symbolPhotoUrl;
    }

    public String getSymbolPhotoUrl() {
        return symbolPhotoUrl;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCid() {
        return cid;
    }

    @Override
    public String toString() {
        return
                "Candidate{" +
                        "poll_position = '" + pollPosition + '\'' +
                        ",poll_id = '" + pollId + '\'' +
                        ",occupation = '" + occupation + '\'' +
                        ",party_name = '" + partyName + '\'' +
                        ",gender = '" + gender + '\'' +
                        ",dob = '" + dob + '\'' +
                        ",name = '" + name + '\'' +
                        ",location = '" + location + '\'' +
                        ",votes = '" + votes + '\'' +
                        ",symbol_photo_url = '" + symbolPhotoUrl + '\'' +
                        ",cid = '" + cid + '\'' +
                        "}";
    }
}