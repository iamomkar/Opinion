package com.creativeminds.opinion.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.creativeminds.opinion.R;
import com.creativeminds.opinion.models.LocationDataItem;
import com.creativeminds.opinion.models.LocationDataResponse;
import com.creativeminds.opinion.retrofit.APIClient;
import com.creativeminds.opinion.retrofit.APIInterface;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationStatisticsActivity extends AppCompatActivity {

    ProgressDialog p;
    String message;
    int success;
    APIInterface apiInterface;
    PieChart pieChart;
    RadarData radarData;
    RadarDataSet radarDataSet;
    ArrayList radarEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_statistics);

        pieChart = findViewById(R.id.chart);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(true);
        pieChart.setExtraOffsets(5,10,5,5);
        pieChart.setDragDecelerationFrictionCoef(0.9f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.animateY(1000, Easing.EaseInOutCubic);

        getAllCandidatesOfPoll(getIntent().getStringExtra("pid"));


    }

    public void getAllCandidatesOfPoll(String pid) {
        p = ProgressDialog.show(LocationStatisticsActivity.this, "Getting Occupation Statistics", "Please wait...", true, false);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<LocationDataResponse> call = apiInterface.getLocationDataOfPoll(pid);
        call.enqueue(new Callback<LocationDataResponse>() {
            @Override
            public void onResponse(Call<LocationDataResponse> call, Response<LocationDataResponse> response) {
                Log.d("URL", call.request().url().toString());
                if (response.isSuccessful()) {
                    success = response.body().getSuccess();
                    message = response.body().getMessage();
                    p.cancel();
                    if (success == 1) {
                        String resp = response.body().toString();

                        List<LocationDataItem> locDataItems = new ArrayList<LocationDataItem>();
                        locDataItems = response.body().getLocationData();
                        ArrayList locationlist = new ArrayList();
                        ArrayList votesList = new ArrayList();
                        List<PieEntry> entries = new ArrayList<>();
                        for (int i = 0; i < locDataItems.size() ; i++){
                            votesList.add(new BarEntry(i+1,Integer.valueOf(locDataItems.get(i).getVotes())));
                            locationlist.add(locDataItems.get(i).getCity());
                            entries.add(new PieEntry(Integer.valueOf(locDataItems.get(i).getVotes()), locDataItems.get(i).getCity()));
                        }
                        PieDataSet set = new PieDataSet(entries, "Gender Statistics");
                        set.setColors(ColorTemplate.COLORFUL_COLORS);
                        set.setSliceSpace(3f);
                        set.setSelectionShift(5f);
                        PieData data = new PieData(set);
                        data.setValueTextSize(10f);
                        data.setValueTextColor(Color.YELLOW);
                        pieChart.setData(data);
                        Description description = new Description();
                        description.setText("Location Wise Vote Statistics");
                        pieChart.setDescription(description);
                        pieChart.invalidate(); // refresh

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
            public void onFailure(Call<LocationDataResponse> call, Throwable t) {
                Log.d("URL", call.request().url().toString());
                Toast.makeText(getApplicationContext(), "Error Creating Poll(Device Error)", Toast.LENGTH_SHORT).show();
                p.cancel();
                Log.e("RegisterActivity", t.toString());
                finish();
            }
        });
    }
}
