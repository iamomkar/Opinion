package com.creativeminds.opinion.models;

import com.google.gson.annotations.SerializedName;

public class Survey {

	@SerializedName("question")
	private String question;

	@SerializedName("option_two")
	private String optionTwo;

	@SerializedName("option_three_votes")
	private String optionThreeVotes;

	@SerializedName("end_time")
	private String endTime;

	@SerializedName("option_four")
	private String optionFour;

	@SerializedName("created_by")
	private String createdBy;

	@SerializedName("sid")
	private String sid;

	@SerializedName("start_time")
	private String startTime;

	@SerializedName("option_two_votes")
	private String optionTwoVotes;

	@SerializedName("number_of_options")
	private String numberOfOptions;

	@SerializedName("option_four_votes")
	private String optionFourVotes;

	@SerializedName("option_one")
	private String optionOne;

	@SerializedName("option_three")
	private String optionThree;

	@SerializedName("option_one_votes")
	private String optionOneVotes;

	public void setQuestion(String question){
		this.question = question;
	}

	public String getQuestion(){
		return question;
	}

	public void setOptionTwo(String optionTwo){
		this.optionTwo = optionTwo;
	}

	public String getOptionTwo(){
		return optionTwo;
	}

	public void setOptionThreeVotes(String optionThreeVotes){
		this.optionThreeVotes = optionThreeVotes;
	}

	public String getOptionThreeVotes(){
		return optionThreeVotes;
	}

	public void setEndTime(String endTime){
		this.endTime = endTime;
	}

	public String getEndTime(){
		return endTime;
	}

	public void setOptionFour(String optionFour){
		this.optionFour = optionFour;
	}

	public String getOptionFour(){
		return optionFour;
	}

	public void setCreatedBy(String createdBy){
		this.createdBy = createdBy;
	}

	public String getCreatedBy(){
		return createdBy;
	}

	public void setSid(String sid){
		this.sid = sid;
	}

	public String getSid(){
		return sid;
	}

	public void setStartTime(String startTime){
		this.startTime = startTime;
	}

	public String getStartTime(){
		return startTime;
	}

	public void setOptionTwoVotes(String optionTwoVotes){
		this.optionTwoVotes = optionTwoVotes;
	}

	public String getOptionTwoVotes(){
		return optionTwoVotes;
	}

	public void setNumberOfOptions(String numberOfOptions){
		this.numberOfOptions = numberOfOptions;
	}

	public String getNumberOfOptions(){
		return numberOfOptions;
	}

	public void setOptionFourVotes(String optionFourVotes){
		this.optionFourVotes = optionFourVotes;
	}

	public String getOptionFourVotes(){
		return optionFourVotes;
	}

	public void setOptionOne(String optionOne){
		this.optionOne = optionOne;
	}

	public String getOptionOne(){
		return optionOne;
	}

	public void setOptionThree(String optionThree){
		this.optionThree = optionThree;
	}

	public String getOptionThree(){
		return optionThree;
	}

	public void setOptionOneVotes(String optionOneVotes){
		this.optionOneVotes = optionOneVotes;
	}

	public String getOptionOneVotes(){
		return optionOneVotes;
	}

	@Override
 	public String toString(){
		return 
			"Survey{" +
			"question = '" + question + '\'' + 
			",option_two = '" + optionTwo + '\'' + 
			",option_three_votes = '" + optionThreeVotes + '\'' + 
			",end_time = '" + endTime + '\'' + 
			",option_four = '" + optionFour + '\'' + 
			",created_by = '" + createdBy + '\'' + 
			",sid = '" + sid + '\'' + 
			",start_time = '" + startTime + '\'' + 
			",option_two_votes = '" + optionTwoVotes + '\'' + 
			",number_of_options = '" + numberOfOptions + '\'' + 
			",option_four_votes = '" + optionFourVotes + '\'' + 
			",option_one = '" + optionOne + '\'' + 
			",option_three = '" + optionThree + '\'' + 
			",option_one_votes = '" + optionOneVotes + '\'' + 
			"}";
		}
}