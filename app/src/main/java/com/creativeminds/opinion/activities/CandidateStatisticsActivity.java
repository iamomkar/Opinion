package com.creativeminds.opinion.activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.creativeminds.opinion.R;

public class CandidateStatisticsActivity extends AppCompatActivity {
    private Button mAgeWiseId;
    private Button mLocationWiseId;
    private Button mGenderWiseId;
    private Button mOccupationWiseId;
    private String pid,cid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_statistics);
        mAgeWiseId = (Button) findViewById(R.id.age_wise_id);
        mLocationWiseId = (Button) findViewById(R.id.location_wise_id);
        mGenderWiseId = (Button) findViewById(R.id.gender_wise_id);
        mOccupationWiseId = (Button) findViewById(R.id.occupation_wise_id);
        pid = getIntent().getStringExtra("pid");
        cid = getIntent().getStringExtra("cid");

        mAgeWiseId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CandidateStatisticsActivity.this,AgeWiseActivity.class);
                intent.putExtra("pid",pid);
                intent.putExtra("cid",cid);
                startActivity(intent);
            }
        });

        mLocationWiseId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CandidateStatisticsActivity.this,LocationStatisticsActivity.class);
                intent.putExtra("pid",pid);
                intent.putExtra("cid",cid);
                startActivity(intent);
            }
        });

        mGenderWiseId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CandidateStatisticsActivity.this,GenderStatisticsActivity.class);
                intent.putExtra("pid",pid);
                intent.putExtra("cid",cid);
                startActivity(intent);
            }
        });

        mOccupationWiseId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CandidateStatisticsActivity.this,OccupationActivity.class);
                intent.putExtra("pid",pid);
                intent.putExtra("cid",cid);
                startActivity(intent);
            }
        });
    }
}
