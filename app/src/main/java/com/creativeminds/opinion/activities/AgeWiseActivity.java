package com.creativeminds.opinion.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.creativeminds.opinion.R;
import com.creativeminds.opinion.models.AgeDataResponse;
import com.creativeminds.opinion.retrofit.APIClient;
import com.creativeminds.opinion.retrofit.APIInterface;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgeWiseActivity extends AppCompatActivity {

    ProgressDialog p;
    String message;
    int success;
    APIInterface apiInterface;
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_wise);
        pieChart = (PieChart) findViewById(R.id.pie_chart);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(true);
        pieChart.setExtraOffsets(5,10,5,5);
        pieChart.setDragDecelerationFrictionCoef(0.9f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.animateY(1000, Easing.EaseInOutCubic);

        /*if(getIntent().getStringExtra("cid").equals(null)){
            getAllCandidatesOfPoll(getIntent().getStringExtra("pid"));
        }else {
            getAllCandidatesOfPoll2(getIntent().getStringExtra("pid"),getIntent().getStringExtra("cid"));
        }*/

        getAllCandidatesOfPoll(getIntent().getStringExtra("pid"));

    }

    public void getAllCandidatesOfPoll(String pid) {
        p = ProgressDialog.show(AgeWiseActivity.this, "Getting Age Statistics", "Please wait...", true, false);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<AgeDataResponse> call = apiInterface.getAgeDataOfPoll(pid);
        call.enqueue(new Callback<AgeDataResponse>() {
            @Override
            public void onResponse(Call<AgeDataResponse> call, Response<AgeDataResponse> response) {
                Log.d("URL", call.request().url().toString());
                if (response.isSuccessful()) {
                    success = response.body().getSuccess();
                    message = response.body().getMessage();
                    p.cancel();
                    if (success == 1) {
                        String resp = response.body().toString();
                        List<PieEntry> entries = new ArrayList<>();
                        entries.add(new PieEntry(Integer.valueOf(response.body().getAgeBelow18()), "Below 18"));
                        entries.add(new PieEntry(Integer.valueOf(response.body().getAge19To25()), "19 - 25"));
                        entries.add(new PieEntry(Integer.valueOf(response.body().getAge26To50()), "26 - 50"));
                        entries.add(new PieEntry(Integer.valueOf(response.body().getAgeAbove50()), "Above 50"));
                        PieDataSet set = new PieDataSet(entries, "Age Statistics");
                        set.setColors(ColorTemplate.COLORFUL_COLORS);
                        set.setSliceSpace(3f);
                        set.setSelectionShift(5f);
                        PieData data = new PieData(set);
                        data.setValueTextSize(10f);
                        data.setValueTextColor(Color.YELLOW);
                        pieChart.setData(data);
                        Description description = new Description();
                        description.setText("Age Wise Vote Statistics");
                        pieChart.setDescription(description);
                        pieChart.invalidate();
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
            public void onFailure(Call<AgeDataResponse> call, Throwable t) {
                Log.d("URL", call.request().url().toString());
                Toast.makeText(getApplicationContext(), "Error Creating Poll(Device Error)", Toast.LENGTH_SHORT).show();
                p.cancel();
                Log.e("RegisterActivity", t.toString());
                finish();
            }
        });
    }

    public void getAllCandidatesOfPoll2(String pid,String cid) {
        p = ProgressDialog.show(AgeWiseActivity.this, "Getting Age Statistics", "Please wait...", true, false);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<AgeDataResponse> call = apiInterface.getAgeDataByCandidate(pid,cid);
        call.enqueue(new Callback<AgeDataResponse>() {
            @Override
            public void onResponse(Call<AgeDataResponse> call, Response<AgeDataResponse> response) {
                Log.d("URL", call.request().url().toString());
                if (response.isSuccessful()) {
                    success = response.body().getSuccess();
                    message = response.body().getMessage();
                    p.cancel();
                    if (success == 1) {
                        String resp = response.body().toString();
                        List<PieEntry> entries = new ArrayList<>();
                        entries.add(new PieEntry(Integer.valueOf(response.body().getAgeBelow18()), "Below 18"));
                        entries.add(new PieEntry(Integer.valueOf(response.body().getAge19To25()), "19 - 25"));
                        entries.add(new PieEntry(Integer.valueOf(response.body().getAge26To50()), "26 - 50"));
                        entries.add(new PieEntry(Integer.valueOf(response.body().getAgeAbove50()), "Above 50"));
                        PieDataSet set = new PieDataSet(entries, "Age Statistics");
                        set.setColors(ColorTemplate.COLORFUL_COLORS);
                        set.setSliceSpace(3f);
                        set.setSelectionShift(5f);
                        PieData data = new PieData(set);
                        data.setValueTextSize(10f);
                        data.setValueTextColor(Color.YELLOW);
                        pieChart.setData(data);
                        Description description = new Description();
                        description.setText("Age Wise Vote Statistics");
                        pieChart.setDescription(description);
                        pieChart.invalidate();
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
            public void onFailure(Call<AgeDataResponse> call, Throwable t) {
                Log.d("URL", call.request().url().toString());
                Toast.makeText(getApplicationContext(), "Error Creating Poll(Device Error)", Toast.LENGTH_SHORT).show();
                p.cancel();
                Log.e("RegisterActivity", t.toString());
                finish();
            }
        });
    }
}
