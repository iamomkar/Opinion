package com.creativeminds.opinion.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.creativeminds.opinion.R;
import com.creativeminds.opinion.activities.MyPollDetailsActivity;
import com.creativeminds.opinion.models.Candidate;
import com.creativeminds.opinion.models.Poll;
import com.creativeminds.opinion.models.Survey;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SurveyListAdapter extends RecyclerView.Adapter<SurveyListAdapter.MyViewHolder> {

    private Context mContext;
    private List<Survey> surveyList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView question,opt1,opt2,opt3,opt4,endTime;

        public MyViewHolder(View view) {
            super(view);
            question = (TextView) view.findViewById(R.id.question);
            opt1 = (TextView) view.findViewById(R.id.option_one);
            opt2 = (TextView) view.findViewById(R.id.option_two);
            opt3 = (TextView) view.findViewById(R.id.option_three);
            opt4 = (TextView) view.findViewById(R.id.option_four);
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
        //Log.d("Survey", "onBindViewHolder: "+survey.toString());
        int number = Integer.parseInt(survey.getNumberOfOptions());
        if(number == 2){
            holder.opt3.setVisibility(View.GONE);
            holder.opt4.setVisibility(View.GONE);
        }else if(number == 3){
            holder.opt4.setVisibility(View.GONE);
        }
        holder.question.setText(survey.getQuestion());
        holder.opt1.setText(survey.getOptionOne());
        holder.opt2.setText(survey.getOptionTwo());
        holder.opt3.setText(survey.getOptionThree());
        holder.opt4.setText(survey.getOptionFour());
        holder.endTime.setText(survey.getEndTime());

        holder.opt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return surveyList.size();
    }
}
