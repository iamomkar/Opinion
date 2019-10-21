package com.creativeminds.opinion.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.creativeminds.opinion.R;
import com.creativeminds.opinion.adapters.PollListAdapter;
import com.creativeminds.opinion.models.Poll;
import com.creativeminds.opinion.models.PollListResponse;
import com.creativeminds.opinion.retrofit.APIClient;
import com.creativeminds.opinion.retrofit.APIInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PollsListActivity extends AppCompatActivity {


    private List<Poll> pollList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PollListAdapter pollListAdapter;
    APIInterface apiInterface;
    int success;
    String message;
    ProgressDialog p;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polls_list);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences(getPackageName(),MODE_PRIVATE);

        recyclerView = (RecyclerView) findViewById(R.id.polls_recycler_view);

        pollListAdapter = new PollListAdapter(this, pollList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pollListAdapter);

        String uid = sharedPreferences.getString("uid","-1");
        if(!uid.equals("-1"))
        getAllPollsByUser(uid);
    }

    public void getAllPollsByUser(String uid) {
        p = ProgressDialog.show(PollsListActivity.this, "Getting Poll Details", "Please wait...", true, false);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<PollListResponse> call = apiInterface.getAllPollsByUser(uid);
        call.enqueue(new Callback<PollListResponse>() {
            @Override
            public void onResponse(Call<PollListResponse> call, Response<PollListResponse> response) {
                Log.d("URL", call.request().url().toString());
                if (response.isSuccessful()) {
                    success = response.body().getSuccess();
                    message = response.body().getMessage();
                    p.cancel();
                    if (success == 1) {
                        pollList = response.body().getPolls();
                        pollListAdapter = new PollListAdapter(PollsListActivity.this, pollList);
                        recyclerView.setAdapter(pollListAdapter);
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
            public void onFailure(Call<PollListResponse> call, Throwable t) {
                Log.d("URL", call.request().url().toString());
                Toast.makeText(getApplicationContext(), "Error Creating Poll(Device Error)", Toast.LENGTH_SHORT).show();
                p.cancel();
                Log.e("RegisterActivity", t.toString());
                finish();
            }
        });
    }
}
