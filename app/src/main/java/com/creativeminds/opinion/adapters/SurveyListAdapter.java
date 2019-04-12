package com.creativeminds.opinion.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.creativeminds.opinion.R;
import com.creativeminds.opinion.activities.MyPollDetailsActivity;
import com.creativeminds.opinion.activities.SurveyActivity;
import com.creativeminds.opinion.models.Candidate;
import com.creativeminds.opinion.models.NormalResponse;
import com.creativeminds.opinion.models.Poll;
import com.creativeminds.opinion.models.Survey;
import com.creativeminds.opinion.models.SurveyListResponse;
import com.creativeminds.opinion.retrofit.APIClient;
import com.creativeminds.opinion.retrofit.APIInterface;
import com.google.gson.Gson;
import com.sasank.roundedhorizontalprogress.RoundedHorizontalProgressBar;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SurveyListAdapter extends RecyclerView.Adapter<SurveyListAdapter.MyViewHolder> {

    private Context mContext;
    private List<Survey> surveyList;
    private int success;
    private String message;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView question, endTime, opt1Text, opt2Text, opt3Text, opt4Text,opt1Percent,opt2Percent,opt3Percent,opt4Percent;
        FrameLayout opt1, opt2, opt3, opt4;
        RoundedHorizontalProgressBar progressBar1,progressBar2,progressBar3,progressBar4;

        public MyViewHolder(View view) {
            super(view);
            question = (TextView) view.findViewById(R.id.question);
            opt1Text = (TextView) view.findViewById(R.id.option_one_text);
            opt2Text = (TextView) view.findViewById(R.id.option_two_text);
            opt3Text = (TextView) view.findViewById(R.id.option_three_text);
            opt4Text = (TextView) view.findViewById(R.id.option_four_text);
            opt1Percent = (TextView) view.findViewById(R.id.option_one_percent);
            opt2Percent = (TextView) view.findViewById(R.id.option_two_percent);
            opt3Percent = (TextView) view.findViewById(R.id.option_three_percent);
            opt4Percent = (TextView) view.findViewById(R.id.option_four_percent);
            opt1 = (FrameLayout) view.findViewById(R.id.option_one_frame);
            opt2 = (FrameLayout) view.findViewById(R.id.option_two_frame);
            opt3 = (FrameLayout) view.findViewById(R.id.option_three_frame);
            opt4 = (FrameLayout) view.findViewById(R.id.option_four_frame);
            progressBar1 = (RoundedHorizontalProgressBar) view.findViewById(R.id.progress_bar_1);
            progressBar2 = (RoundedHorizontalProgressBar) view.findViewById(R.id.progress_bar_2);
            progressBar3 = (RoundedHorizontalProgressBar) view.findViewById(R.id.progress_bar_3);
            progressBar4 = (RoundedHorizontalProgressBar) view.findViewById(R.id.progress_bar_4);
            endTime = (TextView) view.findViewById(R.id.end_time);
        }
    }


    public SurveyListAdapter(Context mContext, List<Survey> _surveyList) {
        this.mContext = mContext;
        this.surveyList = _surveyList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_survey_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Survey survey = surveyList.get(position);
        sharedPreferences = mContext.getSharedPreferences(mContext.getPackageName(), Context.MODE_PRIVATE);

        int number = Integer.parseInt(survey.getNumberOfOptions());
        if (number == 2) {
            holder.opt3.setVisibility(View.GONE);
            holder.opt4.setVisibility(View.GONE);
        } else if (number == 3) {
            holder.opt4.setVisibility(View.GONE);
        }

        setPercentage(survey,holder.progressBar1,holder.progressBar2,holder.progressBar3,holder.progressBar4,holder.opt1Percent,holder.opt2Percent,holder.opt3Percent,holder.opt4Percent);

        if(checkIfAlreadyVotedInSurvey(survey.getSid())){
            holder.progressBar1.setVisibility(View.VISIBLE);
            holder.opt1Percent.setVisibility(View.VISIBLE);
            holder.progressBar2.setVisibility(View.VISIBLE);
            holder.opt2Percent.setVisibility(View.VISIBLE);
            holder.progressBar3.setVisibility(View.VISIBLE);
            holder.opt3Percent.setVisibility(View.VISIBLE);
            holder.progressBar4.setVisibility(View.VISIBLE);
            holder.opt4Percent.setVisibility(View.VISIBLE);
        }
        holder.question.setText(survey.getQuestion());
        holder.opt1Text.setText(survey.getOptionOne());
        holder.opt2Text.setText(survey.getOptionTwo());
        holder.opt3Text.setText(survey.getOptionThree());
        holder.opt4Text.setText(survey.getOptionFour());
        holder.endTime.setText("Valid till:- "+survey.getEndTime());

        holder.opt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkIfAlreadyVotedInSurvey(survey.getSid()))
                    addSurveyVote(survey, "1",holder.progressBar1,holder.progressBar2,holder.progressBar3,holder.progressBar4,holder.opt1Percent,holder.opt2Percent,holder.opt3Percent,holder.opt4Percent);
                else
                    Toast.makeText(mContext, "Already Voted in Survey", Toast.LENGTH_SHORT).show();
            }
        });

        holder.opt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkIfAlreadyVotedInSurvey(survey.getSid()))
                    addSurveyVote(survey, "2",holder.progressBar1,holder.progressBar2,holder.progressBar3,holder.progressBar4,holder.opt1Percent,holder.opt2Percent,holder.opt3Percent,holder.opt4Percent);
                else
                    Toast.makeText(mContext, "Already Voted in Survey", Toast.LENGTH_SHORT).show();            }
        });

        holder.opt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkIfAlreadyVotedInSurvey(survey.getSid()))
                    addSurveyVote(survey, "3",holder.progressBar1,holder.progressBar2,holder.progressBar3,holder.progressBar4,holder.opt1Percent,holder.opt2Percent,holder.opt3Percent,holder.opt4Percent);
                else
                    Toast.makeText(mContext, "Already Voted in Survey", Toast.LENGTH_SHORT).show();            }
        });

        holder.opt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkIfAlreadyVotedInSurvey(survey.getSid()))
                    addSurveyVote(survey, "4",holder.progressBar1,holder.progressBar2,holder.progressBar3,holder.progressBar4,holder.opt1Percent,holder.opt2Percent,holder.opt3Percent,holder.opt4Percent);
                else
                    Toast.makeText(mContext, "Already Voted in Survey", Toast.LENGTH_SHORT).show();            }
        });


    }

    @Override
    public int getItemCount() {
        return surveyList.size();
    }

    public void addSurveyVote(final Survey survey, final String voteNumber, final RoundedHorizontalProgressBar p1, final RoundedHorizontalProgressBar p2, final RoundedHorizontalProgressBar p3, final RoundedHorizontalProgressBar p4, final TextView per1, final TextView per2, final TextView per3, final TextView per4) {
        final ProgressDialog p = ProgressDialog.show(mContext, "Registering your vote", "Please wait...", true, false);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<NormalResponse> call = apiInterface.addSurveyVote(survey.getSid(), voteNumber);
        call.enqueue(new Callback<NormalResponse>() {
            @Override
            public void onResponse(Call<NormalResponse> call, Response<NormalResponse> response) {
                Log.d("URL", call.request().url().toString());
                if (response.isSuccessful()) {
                    success = response.body().getSuccess();
                    message = response.body().getMessage();
                    p.cancel();
                    if (success == 1) {
                        Set<String> sidList = sharedPreferences.getStringSet("sidList", null);
                        if (sidList == null) {
                            sidList = new HashSet<String>();
                        }
                        sidList.add(survey.getSid());
                        editor = sharedPreferences.edit();
                        editor.putStringSet("sidList",sidList);
                        editor.apply();
                        setPercentageAfterVote(survey,voteNumber,p1,p2,p3,p4,per1,per2,per3,per4);
                        p1.setVisibility(View.VISIBLE);
                        p2.setVisibility(View.VISIBLE);
                        p3.setVisibility(View.VISIBLE);
                        p4.setVisibility(View.VISIBLE);
                        per1.setVisibility(View.VISIBLE);
                        per2.setVisibility(View.VISIBLE);
                        per3.setVisibility(View.VISIBLE);
                        per4.setVisibility(View.VISIBLE);
                        Toast.makeText(mContext, message + sidList.toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    p.cancel();
                    Toast.makeText(mContext, "Error registering survey vote\n\n" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NormalResponse> call, Throwable t) {
                Log.d("URL", call.request().url().toString());
                Toast.makeText(mContext, "Error registering vote(Device Error)", Toast.LENGTH_SHORT).show();
                p.cancel();
                Log.e("SurveyListAdapter", t.toString());
            }
        });
    }

    private boolean checkIfAlreadyVotedInSurvey(String sid){
        Set<String> sidList = sharedPreferences.getStringSet("sidList", null);
        if (sidList == null) {
            return false;
        }else {
            return sidList.contains(sid);
        }
    }

    private void setPercentage(Survey survey,RoundedHorizontalProgressBar pb1,RoundedHorizontalProgressBar pb2,RoundedHorizontalProgressBar pb3,RoundedHorizontalProgressBar pb4,TextView p1,TextView p2,TextView p3,TextView p4){
        float op1 = Float.parseFloat(survey.getOptionOneVotes());
        float op2 = Float.parseFloat(survey.getOptionTwoVotes());
        float op3 = Float.parseFloat(survey.getOptionThreeVotes());
        float op4 = Float.parseFloat(survey.getOptionFourVotes());
        float total = op1 + op2 + op3 + op4;

        //Log.d("Count", "setPercentage: OPTION1 ="+String.valueOf(op1)+"\nOPTION2 ="+String.valueOf(op2)+"\nOPTION3 ="+String.valueOf(op3)+"\nOPTION4 ="+String.valueOf(op4)+"\nTOTAL ="+String.valueOf(total));

        float op1percent = (op1/total)*100;
        float op2percent = (op2/total)*100;
        float op3percent = (op3/total)*100;
        float op4percent = (op4/total)*100;

        //Log.d("Percent", "setPercentage: OPTION1 ="+String.valueOf(op1percent)+"\nOPTION2 ="+String.valueOf(op2percent)+"\nOPTION3 ="+String.valueOf(op3percent)+"\nOPTION4 ="+String.valueOf(op4percent)+"\nTOTAL ="+String.valueOf(op1percent+op2percent+op3percent+op4percent));

        pb1.setProgress(Math.round(op1percent));
        p1.setText(String.valueOf(Math.round(op1percent))+"%");
        pb2.setProgress(Math.round(op2percent));
        p2.setText(String.valueOf(Math.round(op2percent))+"%");
        pb3.setProgress(Math.round(op3percent));
        p3.setText(String.valueOf(Math.round(op3percent))+"%");
        pb4.setProgress(Math.round(op4percent));
        p4.setText(String.valueOf(Math.round(op4percent))+"%");

    }

    private void setPercentageAfterVote(Survey survey,String number,RoundedHorizontalProgressBar pb1,RoundedHorizontalProgressBar pb2,RoundedHorizontalProgressBar pb3,RoundedHorizontalProgressBar pb4,TextView p1,TextView p2,TextView p3,TextView p4){
        float op1 = Float.parseFloat(survey.getOptionOneVotes());
        float op2 = Float.parseFloat(survey.getOptionTwoVotes());
        float op3 = Float.parseFloat(survey.getOptionThreeVotes());
        float op4 = Float.parseFloat(survey.getOptionFourVotes());
        if(number.equals("1")){
            op1 += 1f;
        }else if(number.equals("2")){
            op2 += 1f;
        }else if (number.equals("3")){
            op3 += 1f;
        }else if(number.equals("4")){
            op4 += 1f;
        }
        float total = op1 + op2 + op3 + op4;

        //Log.d("Count", "setPercentage: OPTION1 ="+String.valueOf(op1)+"\nOPTION2 ="+String.valueOf(op2)+"\nOPTION3 ="+String.valueOf(op3)+"\nOPTION4 ="+String.valueOf(op4)+"\nTOTAL ="+String.valueOf(total));

        float op1percent = (op1/total)*100;
        float op2percent = (op2/total)*100;
        float op3percent = (op3/total)*100;
        float op4percent = (op4/total)*100;

        //Log.d("Percent", "setPercentage: OPTION1 ="+String.valueOf(op1percent)+"\nOPTION2 ="+String.valueOf(op2percent)+"\nOPTION3 ="+String.valueOf(op3percent)+"\nOPTION4 ="+String.valueOf(op4percent)+"\nTOTAL ="+String.valueOf(op1percent+op2percent+op3percent+op4percent));

        pb1.animateProgress(2000,0,Math.round(op1percent));
        pb2.animateProgress(2000,0,Math.round(op2percent));
        pb3.animateProgress(2000,0,Math.round(op3percent));
        pb4.animateProgress(2000,0,Math.round(op4percent));

        p1.setText(String.valueOf(Math.round(op1percent))+"%");
        p2.setText(String.valueOf(Math.round(op2percent))+"%");
        p3.setText(String.valueOf(Math.round(op3percent))+"%");
        p4.setText(String.valueOf(Math.round(op4percent))+"%");
    }


}
