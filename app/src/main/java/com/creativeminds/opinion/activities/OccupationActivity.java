package com.creativeminds.opinion.activities;

import android.app.ProgressDialog;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.creativeminds.opinion.R;
import com.creativeminds.opinion.models.OccupationDataItem;
import com.creativeminds.opinion.models.OccupationDataResponse;
import com.creativeminds.opinion.retrofit.APIClient;
import com.creativeminds.opinion.retrofit.APIInterface;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OccupationActivity extends AppCompatActivity {

    ProgressDialog p;
    String message;
    int success;
    APIInterface apiInterface;
    BarChart chart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_occupation);
         chart = findViewById(R.id.bar_chart);

         getAllCandidatesOfPoll(getIntent().getStringExtra("pid"));

    }
    public void getAllCandidatesOfPoll(String pid) {
        p = ProgressDialog.show(OccupationActivity.this, "Getting Occupation Statistics", "Please wait...", true, false);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<OccupationDataResponse> call = apiInterface.getOccupationDataOfPoll(pid);
        call.enqueue(new Callback<OccupationDataResponse>() {
            @Override
            public void onResponse(Call<OccupationDataResponse> call, Response<OccupationDataResponse> response) {
                Log.d("URL", call.request().url().toString());
                if (response.isSuccessful()) {
                    success = response.body().getSuccess();
                    message = response.body().getMessage();
                    p.cancel();
                    if (success == 1) {
                        String resp = response.body().toString();
                        List<OccupationDataItem> occupationDataItems = new ArrayList<OccupationDataItem>();
                        occupationDataItems = response.body().getOccupationData();
                        ArrayList occupationList = new ArrayList();
                        ArrayList votesList = new ArrayList();
                        for (int i = 0; i < occupationDataItems.size() ; i++){
                            votesList.add(new BarEntry(i+1,Integer.valueOf(occupationDataItems.get(i).getVotes())));
                            occupationList.add(occupationDataItems.get(i).getOccupation());
                        }
                        BarDataSet bardataset = new BarDataSet(votesList, "Occupation Wise Statistics");
                        chart.animateY(500);
                        BarData data = new BarData(bardataset);
                        data.setBarWidth(0.5f);
                        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
                        chart.setData(data);
                        Description description = new Description();
                        description.setText("Occupation Statistics");
                        chart.setDescription(description);
                        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(occupationList));
                        chart.setFitBars(true); // make the x-axis fit exactly all bars
                        chart.invalidate();

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
            public void onFailure(Call<OccupationDataResponse> call, Throwable t) {
                Log.d("URL", call.request().url().toString());
                Toast.makeText(getApplicationContext(), "Error Creating Poll(Device Error)", Toast.LENGTH_SHORT).show();
                p.cancel();
                Log.e("RegisterActivity", t.toString());
                finish();
            }
        });
    }
}
