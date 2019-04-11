package com.creativeminds.opinion.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.creativeminds.opinion.R;
import com.creativeminds.opinion.adapters.CandidateListAdapter;
import com.creativeminds.opinion.adapters.SurveyListAdapter;
import com.creativeminds.opinion.models.CandidatesListResponse;
import com.creativeminds.opinion.models.Survey;
import com.creativeminds.opinion.models.SurveyListResponse;
import com.creativeminds.opinion.retrofit.APIClient;
import com.creativeminds.opinion.retrofit.APIInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SurveyActivity extends AppCompatActivity {

    private List<Survey> surveyList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SurveyListAdapter surveyListAdapter;
    String message;
    ProgressDialog p;
    int success;
    APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        recyclerView = (RecyclerView) findViewById(R.id.survey_recycler_view);

        surveyListAdapter = new SurveyListAdapter(SurveyActivity.this, surveyList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SurveyActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(surveyListAdapter);

        getAllSurveys();
    }

    public void getAllSurveys() {
        p = ProgressDialog.show(SurveyActivity.this, "Getting Poll Details", "Please wait...", true, false);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<SurveyListResponse> call = apiInterface.getAllSurveys();
        call.enqueue(new Callback<SurveyListResponse>() {
            @Override
            public void onResponse(Call<SurveyListResponse> call, Response<SurveyListResponse> response) {
                Log.d("URL", call.request().url().toString());
                if (response.isSuccessful()) {
                    success = response.body().getSuccess();
                    message = response.body().getMessage();
                    p.cancel();
                    if (success == 1) {
                        surveyList = response.body().getSurvey();
                        surveyListAdapter = new SurveyListAdapter(SurveyActivity.this, surveyList);
                        recyclerView.setAdapter(surveyListAdapter);
                        //Log.d("POLL", "onResponse: "+pollList.toString());
                    }else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    p.cancel();
                    Toast.makeText(getApplicationContext(), "Error getting surveys\n\n"+response.message(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<SurveyListResponse> call, Throwable t) {
                Log.d("URL", call.request().url().toString());
                Toast.makeText(getApplicationContext(), "Error getting surveys(Device Error)", Toast.LENGTH_SHORT).show();
                p.cancel();
                Log.e("RegisterActivity", t.toString());
                finish();
            }
        });
    }
}
