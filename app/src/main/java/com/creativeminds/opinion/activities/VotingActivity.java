package com.creativeminds.opinion.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.creativeminds.opinion.R;
import com.creativeminds.opinion.adapters.VoteCandidateAdapter;
import com.creativeminds.opinion.models.Candidate;
import com.creativeminds.opinion.models.CandidatesListResponse;
import com.creativeminds.opinion.models.Poll;
import com.creativeminds.opinion.retrofit.APIClient;
import com.creativeminds.opinion.retrofit.APIInterface;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VotingActivity extends AppCompatActivity {

    private List<Candidate> candidateList = new ArrayList<>();
    private RecyclerView recyclerView;
    private VoteCandidateAdapter voteCandidateAdapter;
    private TextView mPollTitle;
    private TextView mPollDesc;
    APIInterface apiInterface;
    int success;
    String message;
    ProgressDialog p;
    SharedPreferences sharedPreferences;
    Poll poll;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);

       //Toast.makeText(this, getIntent().getStringExtra("poll"), Toast.LENGTH_SHORT).show();
        poll = gson.fromJson(getIntent().getStringExtra("poll"),Poll.class);

        sharedPreferences = getSharedPreferences(getPackageName(),MODE_PRIVATE);

        recyclerView = (RecyclerView) findViewById(R.id.candidate_recycler_view);
        mPollTitle = (TextView) findViewById(R.id.poll_title);
        mPollDesc = (TextView) findViewById(R.id.poll_desc);

        mPollTitle.setText(poll.getTitle());
        mPollDesc.setText(poll.getDescription());

        voteCandidateAdapter = new VoteCandidateAdapter(VotingActivity.this, candidateList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(VotingActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(voteCandidateAdapter);

        getAllCandidatesOfPoll(poll.getPid());

    }

    public void getAllCandidatesOfPoll(String pid) {
        p = ProgressDialog.show(VotingActivity.this, "Getting Poll Details", "Please wait...", true, false);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<CandidatesListResponse> call = apiInterface.getAllCandidatesOfPoll(pid);
        call.enqueue(new Callback<CandidatesListResponse>() {
            @Override
            public void onResponse(Call<CandidatesListResponse> call, Response<CandidatesListResponse> response) {
                Log.d("URL", call.request().url().toString());
                if (response.isSuccessful()) {
                    success = response.body().getSuccess();
                    message = response.body().getMessage();
                    p.cancel();
                    if (success == 1) {
                        candidateList = response.body().getCandidates();
                        voteCandidateAdapter = new VoteCandidateAdapter(VotingActivity.this, candidateList);
                        recyclerView.setAdapter(voteCandidateAdapter);
                        //Log.d("POLL", "onResponse: "+pollList.toString());
                    }else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    p.cancel();
                    Toast.makeText(getApplicationContext(), "Error getting Poll details\n\n"+response.message(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<CandidatesListResponse> call, Throwable t) {
                Log.d("URL", call.request().url().toString());
                Toast.makeText(getApplicationContext(), "Error Creating Poll(Device Error)", Toast.LENGTH_SHORT).show();
                p.cancel();
                Log.e("RegisterActivity", t.toString());
                finish();
            }
        });
    }
}
