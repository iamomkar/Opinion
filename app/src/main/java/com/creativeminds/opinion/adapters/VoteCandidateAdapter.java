package com.creativeminds.opinion.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.creativeminds.opinion.R;
import com.creativeminds.opinion.activities.CandidateDetailsActivity;
import com.creativeminds.opinion.activities.CreatePollActivity;
import com.creativeminds.opinion.activities.ShowPollQRActivity;
import com.creativeminds.opinion.activities.VoteSuccessActivity;
import com.creativeminds.opinion.activities.VotingActivity;
import com.creativeminds.opinion.models.Candidate;
import com.creativeminds.opinion.models.NormalResponse;
import com.creativeminds.opinion.models.Poll;
import com.creativeminds.opinion.retrofit.APIClient;
import com.creativeminds.opinion.retrofit.APIInterface;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VoteCandidateAdapter extends RecyclerView.Adapter<VoteCandidateAdapter.MyViewHolder> {

    private Context mContext;
    private List<Candidate> candidateList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,party;
        public Button vote;
        public ImageView logo;
        public CardView candidateCard;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.candidate_name);
            party = (TextView) view.findViewById(R.id.candidate_party);
            logo = (ImageView)view.findViewById(R.id.candidate_logo);
            vote = (Button)view.findViewById(R.id.vote_candidate_btn);
            candidateCard = (CardView)view.findViewById(R.id.candidate_card);
        }
    }


    public VoteCandidateAdapter(Context mContext, List<Candidate> candidates) {
        this.mContext = mContext;
        this.candidateList = candidates;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vote_candidate_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,int position) {
        final Candidate candidate = candidateList.get(position);
        holder.name.setText(candidate.getName());
        holder.party.setText(candidate.getPartyName());
        final int i = position;
        holder.vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addVote(candidateList.get(i).getCid(),candidateList.get(i).getPollId());
            }
        });
        holder.candidateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                mContext.startActivity(new Intent(mContext, CandidateDetailsActivity.class).putExtra("candidate",gson.toJson(candidate)));
            }
        });
    }


    @Override
    public int getItemCount() {
        return candidateList.size();
    }

    public void addVote(String cid,String pid) {
       final ProgressDialog p = ProgressDialog.show(mContext, "Creating Poll", "Please wait...", true, false);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(mContext.getPackageName(),Context.MODE_PRIVATE);
        String age = getAge(sharedPreferences.getString("dob",""));
        if(age == null)
            return;
        Call<NormalResponse> call = apiInterface.addVote(sharedPreferences.getString("uid","0"),pid,cid,sharedPreferences.getString("gender",""),
                sharedPreferences.getString("occupation",""),sharedPreferences.getString("state",""),sharedPreferences.getString("city",""),
                sharedPreferences.getString("pinCode",""),age,"");
        call.enqueue(new Callback<NormalResponse>() {
            @Override
            public void onResponse(Call<NormalResponse> call, Response<NormalResponse> response) {
                Log.d("URL", call.request().url().toString());
                if (response.isSuccessful()) {
                    int success = response.body().getSuccess();
                    String message = response.body().getMessage();
                    p.cancel();
                    if (success == 1) {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        mContext.startActivity(new Intent(mContext, VoteSuccessActivity.class));
                        ((Activity)mContext).finish();
                    }else {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        ((Activity)mContext).finish();
                    }

                } else {
                    p.cancel();
                    Toast.makeText(mContext, "Error Voting\n\n"+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NormalResponse> call, Throwable t) {
                Log.d("URL", call.request().url().toString());
                Toast.makeText(mContext, "Error Voting(Device Error)", Toast.LENGTH_SHORT).show();
                p.cancel();
                Log.e("RegisterActivity", t.toString());
            }
        });
    }

    private String getAge(String date){

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        try {
            dob.setTime(sdf.parse(date));
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(mContext, "Cannot Parse Date of Birth", Toast.LENGTH_SHORT).show();
            return null;
        }

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }
}
