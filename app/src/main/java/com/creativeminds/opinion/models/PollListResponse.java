package com.creativeminds.opinion.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class PollListResponse{

	@SerializedName("success")
	private int success;

	@SerializedName("polls")
	private List<Poll> polls;

	@SerializedName("message")
	private String message;

	public void setSuccess(int success){
		this.success = success;
	}

	public int getSuccess(){
		return success;
	}

	public void setPolls(List<Poll> polls){
		this.polls = polls;
	}

	public List<Poll> getPolls(){
		return polls;
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
			"PollListResponse{" + 
			"success = '" + success + '\'' + 
			",polls = '" + polls + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}