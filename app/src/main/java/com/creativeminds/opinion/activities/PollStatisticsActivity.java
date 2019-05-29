package com.creativeminds.opinion.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.creativeminds.opinion.R;

public class PollStatisticsActivity extends AppCompatActivity {

    private Button mAgeWiseId;
    private Button mLocationWiseId;
    private Button mGenderWiseId;
    private Button mOccupationWiseId;
    private String pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_statistics);

        mAgeWiseId = (Button) findViewById(R.id.age_wise_id);
        mLocationWiseId = (Button) findViewById(R.id.location_wise_id);
        mGenderWiseId = (Button) findViewById(R.id.gender_wise_id);
        mOccupationWiseId = (Button) findViewById(R.id.occupation_wise_id);
        pid = getIntent().getStringExtra("pid");

        mAgeWiseId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PollStatisticsActivity.this,AgeWiseActivity.class);
                intent.putExtra("pid",pid);
                startActivity(intent);
            }
        });

        mLocationWiseId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mGenderWiseId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mOccupationWiseId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
