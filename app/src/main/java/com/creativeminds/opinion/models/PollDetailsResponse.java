package com.creativeminds.opinion.models;

import com.google.gson.annotations.SerializedName;

public class PollDetailsResponse {

	@SerializedName("success")
	private int success;

	@SerializedName("poll")
	private Poll poll;

	@SerializedName("message")
	private String message;

	public void setSuccess(int success){
		this.success = success;
	}

	public int getSuccess(){
		return success;
	}

	public void setPoll(Poll poll){
		this.poll = poll;
	}

	public Poll getPoll(){
		return poll;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return 
			"PollDetailsResponse{" +
			"success = '" + success + '\'' + 
			",poll = '" + poll + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}