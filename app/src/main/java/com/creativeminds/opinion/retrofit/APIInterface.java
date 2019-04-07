package com.creativeminds.opinion.retrofit;

import com.creativeminds.opinion.models.CandidatesListResponse;
import com.creativeminds.opinion.models.Poll;
import com.creativeminds.opinion.models.PollDetailsResponse;
import com.creativeminds.opinion.models.LoginResponse;
import com.creativeminds.opinion.models.NormalResponse;
import com.creativeminds.opinion.models.PollCreatedResponse;
import com.creativeminds.opinion.models.PollListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("add_new_user.php")
    Call<NormalResponse> registerNewUser(@Query("name") String name,@Query("pass") String pass,@Query("uidno") String uidno,@Query("gender") String gender,@Query("occupation") String occupation,@Query("email") String email,@Query("phone") String phone,@Query("state") String state,@Query("city") String city,@Query("pincode") String pincode,@Query("sec_question") String sec_question
            ,@Query("sec_answer") String sec_answer,@Query("dob") String dob,@Query("creation_datetime") String creation_datetime,@Query("photo_url") String photo_url,@Query("latlong") String latlong,@Query("verified") String verified,@Query("organization") String organisation);

    @GET("sendmail.php")
    Call<NormalResponse> sendVerificationMail(@Query("name") String name,@Query("email") String email,@Query("otp") String otp);

    @GET("login.php")
    Call<LoginResponse> checkLogin(@Query("email") String email,@Query("password") String password);

    @GET("create_new_poll.php")
    Call<PollCreatedResponse> createNewPoll(@Query("created_by") String createdBy, @Query("title") String title,@Query("description") String description, @Query("creation_date") String creationDate,@Query("end_time") String endDate, @Query("photo_url") String photoURL,@Query("is_location_specific") String isLocationSpecific, @Query("location") String location,@Query("number_candidates")String numberOfCandidate);

    @GET("create_votes_table.php")
    Call<NormalResponse> createVotesTable(@Query("pid") String pid);

    @GET("add_new_candidate.php")
    Call<NormalResponse> addCandidate(@Query("poll_id") String pollID, @Query("name") String candidateName,@Query("party_name") String partyName, @Query("poll_position") String pollPosition,@Query("symbol_photo_url") String photoURL, @Query("gender") String gender,@Query("occupation") String occupation, @Query("dob") String dob,@Query("location")String location);

    @GET("get_poll_details.php")
    Call<PollDetailsResponse> getPollDetails(@Query("pid") String pid);

    @GET("get_all_polls_by_user.php")
    Call<PollListResponse> getAllPollsByUser(@Query("uid") String uid);

    @GET("get_all_candidates_of_poll.php")
    Call<CandidatesListResponse> getAllCandidatesOfPoll(@Query("pid") String pid);

    @GET("add_vote.php")
    Call<NormalResponse> addVote(@Query("uid") String uid,@Query("pid") String pid,@Query("cid") String cid,@Query("gender") String gender,@Query("occupation") String occupation,@Query("state") String state,@Query("city") String city,@Query("pincode") String pincode,@Query("age") String age,@Query("latlong") String latlong);

    @GET("get_poll_votes.php")
    Call<CandidatesListResponse> getPollVotesList(@Query("pid") String pid);



}

