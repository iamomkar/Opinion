package com.creativeminds.opinion.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SurveyListResponse{

	@SerializedName("success")
	private int success;

	@SerializedName("survey")
	private List<Survey> survey;

	@SerializedName("message")
	private String message;

	public void setSuccess(int success){
		this.success = success;
	}

	public int getSuccess(){
		return success;
	}

	public void setSurvey(List<Survey> survey){
		this.survey = survey;
	}

	public List<Survey> getSurvey(){
		return survey;
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
			"SurveyListResponse{" + 
			"success = '" + success + '\'' + 
			",survey = '" + survey + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}