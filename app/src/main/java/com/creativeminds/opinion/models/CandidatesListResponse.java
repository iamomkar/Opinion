package com.creativeminds.opinion.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class CandidatesListResponse{

	@SerializedName("candidates")
	private List<Candidate> candidates;

	@SerializedName("success")
	private int success;

	@SerializedName("message")
	private String message;

	public void setCandidates(List<Candidate> candidates){
		this.candidates = candidates;
	}

	public List<Candidate> getCandidates(){
		return candidates;
	}

	public void setSuccess(int success){
		this.success = success;
	}

	public int getSuccess(){
		return success;
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
			"CandidatesListResponse{" + 
			"candidates = '" + candidates + '\'' + 
			",success = '" + success + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}