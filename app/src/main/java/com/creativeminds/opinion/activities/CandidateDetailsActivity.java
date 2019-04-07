package com.creativeminds.opinion.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.creativeminds.opinion.R;
import com.creativeminds.opinion.models.Candidate;
import com.google.gson.Gson;

public class CandidateDetailsActivity extends AppCompatActivity {

    private ImageView mCandidateLogo;
    private TextView mCandidateName;
    private TextView mCandidateParty;
    private TextView mCandidateDob;
    private TextView mCandidateLocation;
    private TextView mCandidateGender;
    private TextView mCandidateOccupation;
    private TextView mCandidateManifesto;
    private Button mCloseBtn;
    private Candidate candidate;
    private Gson gson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_details);

        mCandidateLogo = (ImageView) findViewById(R.id.candidate_logo);
        mCandidateName = (TextView) findViewById(R.id.candidate_name);
        mCandidateParty = (TextView) findViewById(R.id.candidate_party);
        mCandidateDob = (TextView) findViewById(R.id.candidate_dob);
        mCandidateLocation = (TextView) findViewById(R.id.candidate_location);
        mCandidateGender = (TextView) findViewById(R.id.candidate_gender);
        mCandidateOccupation = (TextView) findViewById(R.id.candidate_occupation);
        mCandidateManifesto = (TextView) findViewById(R.id.candidate_manifesto);
        mCloseBtn = (Button) findViewById(R.id.close_btn);

        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(getIntent().getStringExtra("candidate") != null)
            candidate = gson.fromJson(getIntent().getStringExtra("candidate"),Candidate.class);

        mCandidateName.setText(candidate.getName());
        mCandidateParty.setText(candidate.getPartyName());
        mCandidateDob.setText("Date of Birth :- " + candidate.getDob());
        mCandidateLocation.setText("Location :- " + candidate.getLocation());
        mCandidateGender.setText("Gender :- " + candidate.getGender());
        mCandidateOccupation.setText("Occupation :- " + candidate.getOccupation());

    }
}
