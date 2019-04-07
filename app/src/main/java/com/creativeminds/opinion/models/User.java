package com.creativeminds.opinion.models;

import com.google.gson.annotations.SerializedName;

public class User{

	@SerializedName("uid")
	private String uid;

	@SerializedName("sec_answer")
	private String secAnswer;

	@SerializedName("pincode")
	private String pincode;

	@SerializedName("sec_question")
	private String secQuestion;

	@SerializedName("occupation")
	private String occupation;

	@SerializedName("gender")
	private String gender;

	@SerializedName("pass")
	private String pass;

	@SerializedName("city")
	private String city;

	@SerializedName("latlong")
	private String latlong;

	@SerializedName("verified")
	private String verified;

	@SerializedName("uidno")
	private String uidno;

	@SerializedName("phone")
	private String phone;

	@SerializedName("dob")
	private String dob;

	@SerializedName("organization")
	private String organization;

	@SerializedName("name")
	private String name;

	@SerializedName("creation_datetime")
	private String creationDatetime;

	@SerializedName("state")
	private String state;

	@SerializedName("photo_url")
	private String photoUrl;

	@SerializedName("email")
	private String email;

    public void setUid(String uid){
        this.uid = uid;
    }

    public String getUid(){
        return uid;
    }

	public void setSecAnswer(String secAnswer){
		this.secAnswer = secAnswer;
	}

	public String getSecAnswer(){
		return secAnswer;
	}

	public void setPincode(String pincode){
		this.pincode = pincode;
	}

	public String getPincode(){
		return pincode;
	}

	public void setSecQuestion(String secQuestion){
		this.secQuestion = secQuestion;
	}

	public String getSecQuestion(){
		return secQuestion;
	}

	public void setOccupation(String occupation){
		this.occupation = occupation;
	}

	public String getOccupation(){
		return occupation;
	}

	public void setGender(String gender){
		this.gender = gender;
	}

	public String getGender(){
		return gender;
	}

	public void setPass(String pass){
		this.pass = pass;
	}

	public String getPass(){
		return pass;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getCity(){
		return city;
	}

	public void setLatlong(String latlong){
		this.latlong = latlong;
	}

	public String getLatlong(){
		return latlong;
	}

	public void setVerified(String verified){
		this.verified = verified;
	}

	public String getVerified(){
		return verified;
	}

	public void setUidno(String uidno){
		this.uidno = uidno;
	}

	public String getUidno(){
		return uidno;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setDob(String dob){
		this.dob = dob;
	}

	public String getDob(){
		return dob;
	}

	public void setOrganization(String organization){
		this.organization = organization;
	}

	public String getOrganization(){
		return organization;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setCreationDatetime(String creationDatetime){
		this.creationDatetime = creationDatetime;
	}

	public String getCreationDatetime(){
		return creationDatetime;
	}

	public void setState(String state){
		this.state = state;
	}

	public String getState(){
		return state;
	}

	public void setPhotoUrl(String photoUrl){
		this.photoUrl = photoUrl;
	}

	public String getPhotoUrl(){
		return photoUrl;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	@Override
 	public String toString(){
		return 
			"User{" + "uid = '"+uid+ '\''+
			"sec_answer = '" + secAnswer + '\'' + 
			",pincode = '" + pincode + '\'' + 
			",sec_question = '" + secQuestion + '\'' + 
			",occupation = '" + occupation + '\'' + 
			",gender = '" + gender + '\'' + 
			",pass = '" + pass + '\'' + 
			",city = '" + city + '\'' + 
			",latlong = '" + latlong + '\'' + 
			",verified = '" + verified + '\'' + 
			",uidno = '" + uidno + '\'' + 
			",phone = '" + phone + '\'' + 
			",dob = '" + dob + '\'' + 
			",organization = '" + organization + '\'' + 
			",name = '" + name + '\'' + 
			",creation_datetime = '" + creationDatetime + '\'' + 
			",state = '" + state + '\'' + 
			",photo_url = '" + photoUrl + '\'' + 
			",email = '" + email + '\'' + 
			"}";
		}
}