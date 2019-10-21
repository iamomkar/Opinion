package com.creativeminds.opinion.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.creativeminds.opinion.R;
import com.creativeminds.opinion.models.GenderDataItem;
import com.creativeminds.opinion.models.GenderDataResponse;
import com.creativeminds.opinion.retrofit.APIClient;
import com.creativeminds.opinion.retrofit.APIInterface;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenderStatisticsActivity extends AppCompatActivity {

    ProgressDialog p;
    String message;
    int success;
    APIInterface apiInterface;
    PieChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender_statistics);
        chart = (PieChart) findViewById(R.id.chart);

        getAllCandidatesOfPoll(getIntent().getStringExtra("pid"));


    }

    public void getAllCandidatesOfPoll(String pid) {
        p = ProgressDialog.show(GenderStatisticsActivity.this, "Getting Occupation Statistics", "Please wait...", true, false);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<GenderDataResponse> call = apiInterface.getGenderDataOfPoll(pid);
        call.enqueue(new Callback<GenderDataResponse>() {
            @Override
            public void onResponse(Call<GenderDataResponse> call, Response<GenderDataResponse> response) {
                Log.d("URL", call.request().url().toString());
                if (response.isSuccessful()) {
                    success = response.body().getSuccess();
                    message = response.body().getMessage();
                    p.cancel();
                    if (success == 1) {
                        String resp = response.body().toString();
                        List<GenderDataItem> genderDataItems = new ArrayList<GenderDataItem>();
                        genderDataItems = response.body().getGenderData();
                        ArrayList occupationList = new ArrayList();
                        ArrayList votesList = new ArrayList();
                        List<PieEntry> entries = new ArrayList<>();
                        for (int i = 0; i < genderDataItems.size() ; i++){
                            votesList.add(new BarEntry(i+1,Integer.valueOf(genderDataItems.get(i).getVotes())));
                            occupationList.add(genderDataItems.get(i).getGender());
                            entries.add(new PieEntry(Integer.valueOf(genderDataItems.get(i).getVotes()), genderDataItems.get(i).getGender()));
                        }
                        PieDataSet set = new PieDataSet(entries, "Gender Statistics");
                        set.setColors(ColorTemplate.COLORFUL_COLORS);
                        set.setSliceSpace(3f);
                        set.setSelectionShift(5f);
                        PieData data = new PieData(set);
                        data.setValueTextSize(10f);
                        data.setValueTextColor(Color.YELLOW);
                        chart.setData(data);
                        Description description = new Description();
                        description.setText("Gender Wise Vote Statistics");
                        chart.setDescription(description);
                        chart.invalidate(); // refresh

                        Log.d("POLL", "onResponse: " + resp);
                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    p.cancel();
                    Toast.makeText(getApplicationContext(), "Error getting Poll details\n\n" + response.message(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<GenderDataResponse> call, Throwable t) {
                Log.d("URL", call.request().url().toString());
                Toast.makeText(getApplicationContext(), "Error Creating Poll(Device Error)", Toast.LENGTH_SHORT).show();
                p.cancel();
                Log.e("RegisterActivity", t.toString());
                finish();
            }
        });
    }
}
