package com.creativeminds.opinion.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.creativeminds.opinion.R;
import com.creativeminds.opinion.models.Poll;
import com.creativeminds.opinion.models.PollDetailsResponse;
import com.creativeminds.opinion.retrofit.APIClient;
import com.creativeminds.opinion.retrofit.APIInterface;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView name, email;
    SharedPreferences sharedPreferences;
    Button vote, createPoll, result, myPolls,mSurveys,mNewSurvey;
    APIInterface apiInterface;
    String message;
    int success;
    ProgressDialog p;
    Poll poll;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View v = navigationView.getHeaderView(0);
        name = (TextView) v.findViewById(R.id.name);
        email = (TextView) v.findViewById(R.id.email);
        createPoll = (Button) findViewById(R.id.create_poll);
        mSurveys = (Button) findViewById(R.id.survey_btn);
        mNewSurvey = (Button) findViewById(R.id.create_survey_btn);
        myPolls = (Button) findViewById(R.id.view_my_polls);
        vote = (Button) findViewById(R.id.vote);
        result = (Button) findViewById(R.id.view_results);
        name.setText(sharedPreferences.getString("name", "Name"));
        email.setText(sharedPreferences.getString("email", "Email"));
        setSupportActionBar(toolbar);

        result.setVisibility(View.INVISIBLE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        createPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreatePollActivity.class));
            }
        });

        mNewSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                final EditText editText = new EditText(MainActivity.this);
                editText.setHint("(2-4)");
                alertDialogBuilder.setView(editText);
                alertDialogBuilder.setTitle("Enter number of options for survey question? (min=2 and max=4)").setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (editText.getText().toString().equals("2") || editText.getText().toString().equals("3") || editText.getText().toString().equals("4")) {
                            startActivity(new Intent(MainActivity.this, NewSurveyActivity.class).putExtra("number",editText.getText().toString()));
                        }else{
                            Toast.makeText(MainActivity.this, "Enter proper number of options.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("Cancel", null).show();

            }
        });

        mSurveys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SurveyActivity.class));
            }
        });

        vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setTitle("Select").setMessage("How would you like to vote").setPositiveButton("Scan QR Code", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(new Intent(MainActivity.this, ScanQRActivity.class).putExtra("title", "Scan Poll QR Code"), 1);
                    }
                }).setNegativeButton("Enter Poll ID", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                        final EditText editText = new EditText(MainActivity.this);
                        editText.setHint("Poll ID");
                        alertDialogBuilder.setView(editText);
                        alertDialogBuilder.setTitle("Enter Poll ID").setPositiveButton("Validate", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (!editText.getText().toString().isEmpty()) {
                                    getPollDetails(editText.getText().toString());
                                }
                            }
                        }).setNegativeButton("Cancel", null).show();
                    }
                }).show();
            }
        });

        myPolls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PollsListActivity.class));
            }
        });


        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action


            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
            alertDialogBuilder.setTitle("Developed by:-")
                    .setMessage("Omkar Shinde\nMalhar Shinde\nGaurav Karmankar\nSwapnil Vaidha").show();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getPollDetails(String pid) {
        p = ProgressDialog.show(MainActivity.this, "Getting Poll Details", "Please wait...", true, false);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<PollDetailsResponse> call = apiInterface.getPollDetails(pid);
        call.enqueue(new Callback<PollDetailsResponse>() {
            @Override
            public void onResponse(Call<PollDetailsResponse> call, Response<PollDetailsResponse> response) {
                Log.d("URL", call.request().url().toString());
                if (response.isSuccessful()) {
                    success = response.body().getSuccess();
                    message = response.body().getMessage();
                    p.cancel();
                    if (success == 1) {
                        poll = response.body().getPoll();
                        if (!isPollExpired() && hasPollStarted() && checkLocationSpecific()) {
                            Intent intent = new Intent(MainActivity.this, VotingActivity.class);
                            intent.putExtra("poll", gson.toJson(poll));
                            startActivity(intent);
                        }
                        Log.d("POLL", "onResponse: " + poll.toString());
                    } else
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                } else {
                    p.cancel();
                    Toast.makeText(getApplicationContext(), "Error Creating Poll\n\n" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PollDetailsResponse> call, Throwable t) {
                Log.d("URL", call.request().url().toString());
                Toast.makeText(getApplicationContext(), "Error Creating Poll(Device Error)", Toast.LENGTH_SHORT).show();
                p.cancel();
                Log.e("RegisterActivity", t.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                Log.e("scan result", " " + contents);
                try {
                    poll = gson.fromJson(contents, Poll.class);
                    Intent intent = new Intent(MainActivity.this, VotingActivity.class);
                    intent.putExtra("poll", gson.toJson(poll));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Unsupported QR Code. Try Again.", Toast.LENGTH_SHORT).show();
                }

            }
            if (resultCode == RESULT_CANCELED) {
                //handle cancel
                Log.e("scan result", " cancle");
            }
        }

    }

    public boolean isPollExpired() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            Date endTime = sdf.parse(poll.getEndTime());
            Date currentTimeDate = new Date();
            //past = 1
            //future = -1
            if (currentTimeDate.compareTo(endTime) == -1) {
                return false;
            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setTitle("Deadline Passed").setMessage("Poll Voting Deadline Passed on " + endTime.toString())
                        .setPositiveButton("OK", null).show();
                //Toast.makeText(MainActivity.this, "Poll Voting Deadline Passed on " + endTime.toString(), Toast.LENGTH_LONG).show();
                return true;
            }

        } catch (ParseException ignored) {
            ignored.printStackTrace();
        }
        return true;
    }

    public boolean hasPollStarted() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            Date startTime = sdf.parse(poll.getCreationDate());
            Date currentTimeDate = new Date();
            if (currentTimeDate.compareTo(startTime) == 1) {
                return true;
            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setTitle("Not Started Yet").setMessage("Voting not started yet. Voting will begin on " + startTime.toString())
                        .setPositiveButton("OK", null).show();
                //Toast.makeText(MainActivity.this, "Voting not started yet. Voting will begin on " + startTime.toString(), Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (ParseException ignored) {
            ignored.printStackTrace();
        }
        return false;
    }

    public boolean checkLocationSpecific(){
        if(poll.getIsLocationSpecific().equals("true")){
            if(poll.getLocation().equals(sharedPreferences.getString("state","null"))){
                return true;
            }else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setTitle("Not Eligible").setMessage("Voting is Location Specific for " + poll.getLocation())
                        .setPositiveButton("OK", null).show();
                Toast.makeText(MainActivity.this, "Voting is Location Specific for "+poll.getLocation(), Toast.LENGTH_LONG).show();
                return false;
            }
        }else {
            return true;
        }
    }

}
