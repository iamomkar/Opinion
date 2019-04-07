package com.creativeminds.opinion.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.creativeminds.opinion.R;
import com.creativeminds.opinion.models.NormalResponse;
import com.creativeminds.opinion.models.Poll;
import com.creativeminds.opinion.models.PollCreatedResponse;
import com.creativeminds.opinion.retrofit.APIClient;
import com.creativeminds.opinion.retrofit.APIInterface;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import net.glxn.qrgen.android.QRCode;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePollActivity extends AppCompatActivity implements  Validator.ValidationListener{

    private LinearLayout mPollDetails;
    @NotEmpty
    private EditText mTitle;
    @NotEmpty
    private EditText mDescription;
    @NotEmpty
    private EditText mStartDate;
    @NotEmpty
    private EditText mEndDate;
    private Spinner mLocation;
    private CheckBox mIsLocationSpecific;
    private Button mCreatePoll;
    private LinearLayout mCandidateDetails;
    private TextView mCandidateNo;
    private EditText mCandidateName;
    private EditText mPartyName;
    private EditText mCandidateDob;
    private EditText mCandidateLocation;
    private EditText mCandidateOccupation;
    private LinearLayout mGenderBox;
    private RadioGroup mRadioGrp;
    private Button mAddCandidateBtn,mFinish;
    String date_time = "",message;
    int mYear,mMonth,mDay,mHour,mMinute,success,i=0;
    String title,description,startDate,endDate,location,isLocationSpecific,created_by,cName,cPartyName,cdob,cLocation,cOccupation,cGender,pollID;
    Validator validator;
    ProgressDialog p;
    APIInterface apiInterface;
    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    private static final Gson gson = new Gson();
    Poll poll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_poll);
        findViews();
        validator = new Validator(this);
        validator.setValidationListener(this);

        ArrayAdapter<String> adapterstate = new ArrayAdapter<String>(this, R.layout.spinner, getResources().getStringArray(R.array.india_states));
        adapterstate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLocation.setAdapter(adapterstate);

        mIsLocationSpecific.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mLocation.setVisibility(View.VISIBLE);
                }else {
                    mLocation.setVisibility(View.GONE);
                }
            }
        });
        mStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker(mStartDate);
            }
        });

        mEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker(mEndDate);
            }
        });

        mCreatePoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });

        mCandidateDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreatePollActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                mCandidateDob.setText(sdf.format(myCalendar.getTime()));
            }
        };

        mAddCandidateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateCandidate();
            }
        });

        mFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bitmap myBitmap = QRCode.from(gson.toJson(poll)).withSize(700,700).bitmap();
                ImageView myImage = new ImageView(CreatePollActivity.this);
                myImage.setImageBitmap(myBitmap);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CreatePollActivity.this);
                alertDialogBuilder.setView(myImage);
                alertDialogBuilder.setTitle("Generate").setMessage("Finish adding "+String.valueOf(i-1)+" candidates for the poll?Generate QR code to share with voters to vote?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(CreatePollActivity.this,ShowPollQRActivity.class);
                        intent.putExtra("pollid",pollID);
                        startActivity(intent);
                    }
                }).setNegativeButton("Cancel", null);
                alertDialogBuilder.show();
            }
        });
    }

    public  void  findViews(){
        mPollDetails = (LinearLayout) findViewById(R.id.create_poll);
        mTitle = (EditText) findViewById(R.id.title);
        mDescription = (EditText) findViewById(R.id.description);
        mStartDate = (EditText) findViewById(R.id.start_date);
        mEndDate = (EditText) findViewById(R.id.end_date);
        mLocation = (Spinner) findViewById(R.id.location);
        mIsLocationSpecific = (CheckBox)findViewById(R.id.is_location_specific);
        mCreatePoll = (Button)findViewById(R.id.create_poll_button);
        mCandidateDetails = (LinearLayout) findViewById(R.id.candidate_details);
        mCandidateNo = (TextView) findViewById(R.id.candidate_no);
        mCandidateName = (EditText) findViewById(R.id.candidate_name);
        mPartyName = (EditText) findViewById(R.id.party_name);
        mCandidateDob = (EditText) findViewById(R.id.candidate_dob);
        mCandidateLocation = (EditText) findViewById(R.id.candidate_location);
        mCandidateOccupation = (EditText) findViewById(R.id.candidate_occupation);
        mGenderBox = (LinearLayout) findViewById(R.id.gender_box);
        mRadioGrp = (RadioGroup) findViewById(R.id.radioGrp);
        mAddCandidateBtn = (Button) findViewById(R.id.add_candidate_btn);
        mFinish = (Button) findViewById(R.id.finish);
    }

    public void validateCandidate(){
        cName = mCandidateName.getText().toString();
        cdob = mCandidateDob.getText().toString();
        cLocation = mCandidateLocation.getText().toString();
        cOccupation = mCandidateOccupation.getText().toString();
        RadioButton selectedId = (RadioButton) findViewById(mRadioGrp.getCheckedRadioButtonId());
        cGender = selectedId.getText().toString();
        cPartyName = mPartyName.getText().toString();

        if(cName.isEmpty() || cdob.isEmpty() || cLocation.isEmpty() || cOccupation.isEmpty() || cGender.isEmpty() || cPartyName.isEmpty()){
            Toast.makeText(getApplicationContext(), "Enter All Fields", Toast.LENGTH_SHORT).show();
        }else{
            p = ProgressDialog.show(CreatePollActivity.this, "Adding Candidate", "Please wait...", true, false);
            addCandidate();
        }
    }

    private void datePicker(final EditText editText){

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        date_time = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        //*************Call Time Picker Here ********************
                        timePicker(editText);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void timePicker(final EditText editText){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        mHour = hourOfDay;
                        mMinute = minute;

                        editText.setText(date_time+" "+hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    @Override
    public void onValidationSucceeded() {
        title = mTitle.getText().toString();
        description = mDescription.getText().toString();
        startDate = mStartDate.getText().toString();
        endDate = mEndDate.getText().toString();
        if(!mIsLocationSpecific.isChecked()){
            isLocationSpecific = "false";
            location = "";
        }else {
            isLocationSpecific = "true";
            location = mLocation.getSelectedItem().toString();
        }
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(),MODE_PRIVATE);
        created_by = sharedPreferences.getString("uid","0");
        createNewPoll();
        p = ProgressDialog.show(CreatePollActivity.this, "Creating Poll", "Please wait...", true, false);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void createNewPoll() {
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<PollCreatedResponse> call = apiInterface.createNewPoll(created_by,title,description,startDate,endDate,"",isLocationSpecific,location,String.valueOf(i));
        call.enqueue(new Callback<PollCreatedResponse>() {
            @Override
            public void onResponse(Call<PollCreatedResponse> call, Response<PollCreatedResponse> response) {
                Log.d("URL", call.request().url().toString());
                if (response.isSuccessful()) {
                    success = response.body().getSuccess();
                    message = response.body().getMessage();
                    p.cancel();
                    if (success == 1) {
                        poll = response.body().getPoll();
                        mPollDetails.setVisibility(View.GONE);
                        mCandidateDetails.setVisibility(View.VISIBLE);
                        getSupportActionBar().setTitle(poll.getTitle());
                        pollID = poll.getPid();
                        i++;
                        mCandidateNo.setText("Add Candidate Number "+String.valueOf(i));
                    }
                    Toast.makeText(CreatePollActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    p.cancel();
                    Toast.makeText(CreatePollActivity.this, "Error Creating Poll\n\n"+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PollCreatedResponse> call, Throwable t) {
                Log.d("URL", call.request().url().toString());
                Toast.makeText(CreatePollActivity.this, "Error Creating Poll(Device Error)", Toast.LENGTH_SHORT).show();
                p.cancel();
                Log.e("RegisterActivity", t.toString());
            }
        });
    }

    public void addCandidate() {
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<NormalResponse> call = apiInterface.addCandidate(pollID,cName,cPartyName,String.valueOf(i),"",cGender,cOccupation,cdob,cLocation);
        call.enqueue(new Callback<NormalResponse>() {
            @Override
            public void onResponse(Call<NormalResponse> call, Response<NormalResponse> response) {
                Log.d("URL", call.request().url().toString());
                if (response.isSuccessful()) {
                    success = response.body().getSuccess();
                    message = response.body().getMessage();
                    p.cancel();
                    if (success == 1) {
                        i++;
                        mCandidateNo.setText("Add Candidate Number "+String.valueOf(i));
                        mCandidateName.setText("");
                        mCandidateDob.setText("");
                        mPartyName.setText("");
                        mCandidateLocation.setText("");
                        mCandidateOccupation.setText("");
                        if(i >= 3){
                            mFinish.setVisibility(View.VISIBLE);
                        }
                    }
                    Toast.makeText(CreatePollActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    p.cancel();
                    Toast.makeText(CreatePollActivity.this, "Error adding candidate\n\n"+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NormalResponse> call, Throwable t) {
                Log.d("URL", call.request().url().toString());
                Toast.makeText(CreatePollActivity.this, "Error Creating candidate(Device Error)", Toast.LENGTH_SHORT).show();
                p.cancel();
                Log.e("RegisterActivity", t.toString());
            }
        });
    }



}
