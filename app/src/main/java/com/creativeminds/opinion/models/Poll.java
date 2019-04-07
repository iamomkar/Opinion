package com.creativeminds.opinion.models;

import com.google.gson.annotations.SerializedName;

public class Poll{

	@SerializedName("number_candidates")
	private String numberCandidates;

	@SerializedName("is_location_specific")
	private String isLocationSpecific;

	@SerializedName("end_time")
	private String endTime;

	@SerializedName("description")
	private String description;

	@SerializedName("pid")
	private String pid;

	@SerializedName("location")
	private String location;

	@SerializedName("creation_date")
	private String creationDate;

	@SerializedName("photo_url")
	private String photoUrl;

	@SerializedName("title")
	private String title;

	@SerializedName("created_by")
	private String createdBy;

	public void setNumberCandidates(String numberCandidates){
		this.numberCandidates = numberCandidates;
	}

	public String getNumberCandidates(){
		return numberCandidates;
	}

	public void setIsLocationSpecific(String isLocationSpecific){
		this.isLocationSpecific = isLocationSpecific;
	}

	public String getIsLocationSpecific(){
		return isLocationSpecific;
	}

	public void setEndTime(String endTime){
		this.endTime = endTime;
	}

	public String getEndTime(){
		return endTime;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setPid(String pid){
		this.pid = pid;
	}

	public String getPid(){
		return pid;
	}

	public void setLocation(String location){
		this.location = location;
	}

	public String getLocation(){
		return location;
	}

	public void setCreationDate(String creationDate){
		this.creationDate = creationDate;
	}

	public String getCreationDate(){
		return creationDate;
	}

	public void setPhotoUrl(String photoUrl){
		this.photoUrl = photoUrl;
	}

	public String getPhotoUrl(){
		return photoUrl;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setCreatedBy(String createdBy){
		this.createdBy = createdBy;
	}

	public String getCreatedBy(){
		return createdBy;
	}

	@Override
 	public String toString(){
		return 
			"Poll{" + 
			"number_candidates = '" + numberCandidates + '\'' + 
			",is_location_specific = '" + isLocationSpecific + '\'' + 
			",end_time = '" + endTime + '\'' + 
			",description = '" + description + '\'' + 
			",pid = '" + pid + '\'' + 
			",location = '" + location + '\'' + 
			",creation_date = '" + creationDate + '\'' + 
			",photo_url = '" + photoUrl + '\'' + 
			",title = '" + title + '\'' + 
			",created_by = '" + createdBy + '\'' + 
			"}";
		}
}