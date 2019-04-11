package com.creativeminds.opinion.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.creativeminds.opinion.R;
import com.creativeminds.opinion.models.NormalResponse;
import com.creativeminds.opinion.models.PollCreatedResponse;
import com.creativeminds.opinion.retrofit.APIClient;
import com.creativeminds.opinion.retrofit.APIInterface;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewSurveyActivity extends AppCompatActivity {


    private EditText mQuestion;
    private EditText mOptionOne;
    private EditText mOptionTwo;
    private EditText mOptionThree;
    private EditText mOptionFour;
    private EditText mEndDate;
    private Button mCreateSurveyButton;
    int mYear,mMonth,mDay,mHour,mMinute,success;
    String date_time,question,opt1,opt2,opt3,opt4,created_by,startDate,endDate,message,numberOfOptions;
    ProgressDialog p;
    APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_survey);

        mQuestion = (EditText) findViewById(R.id.question);
        mOptionOne = (EditText) findViewById(R.id.option_one);
        mOptionTwo = (EditText) findViewById(R.id.option_two);
        mOptionThree = (EditText) findViewById(R.id.option_three);
        mOptionFour = (EditText) findViewById(R.id.option_four);
        mEndDate = (EditText) findViewById(R.id.end_date);
        mCreateSurveyButton = (Button) findViewById(R.id.create_survey_btn);

        numberOfOptions = getIntent().getStringExtra("number");

        if(numberOfOptions.equals("2")){
            mOptionThree.setVisibility(View.GONE);
            mOptionFour.setVisibility(View.GONE);
        }else if(numberOfOptions.equals("3")){
            mOptionFour.setVisibility(View.GONE);
        }

        mEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker(mEndDate);
            }
        });

        mCreateSurveyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(),MODE_PRIVATE);
                created_by = sharedPreferences.getString("uid","0");
                question = mQuestion.getText().toString();
                opt1 = mOptionOne.getText().toString();
                opt2 = mOptionTwo.getText().toString();
                opt3 = mOptionThree.getText().toString();
                opt4 = mOptionFour.getText().toString();
                endDate = mEndDate.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                startDate = sdf.format(new Date());

                if(question.isEmpty() || opt1.isEmpty() || opt2.isEmpty() || endDate.isEmpty()){
                    Toast.makeText(NewSurveyActivity.this, "Enter all fields", Toast.LENGTH_SHORT).show();
                }else if (numberOfOptions.equals("3") && opt3.isEmpty()){
                        Toast.makeText(NewSurveyActivity.this, "Enter option 3", Toast.LENGTH_SHORT).show();
                }else if (numberOfOptions.equals("4") && opt4.isEmpty()){
                        Toast.makeText(NewSurveyActivity.this, "Enter option 4", Toast.LENGTH_SHORT).show();
                }else {
                    addNewSurvey();
                }
            }
        });

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

    public void addNewSurvey() {
        p = ProgressDialog.show(NewSurveyActivity.this, "Adding new survey", "Please wait...", true, false);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<NormalResponse> call = apiInterface.addNewSurvey(created_by,numberOfOptions,question,opt1,opt2,opt3,opt4,startDate,endDate);
        call.enqueue(new Callback<NormalResponse>() {
            @Override
            public void onResponse(Call<NormalResponse> call, Response<NormalResponse> response) {
                Log.d("URL", call.request().url().toString());
                if (response.isSuccessful()) {
                    success = response.body().getSuccess();
                    message = response.body().getMessage();
                    p.cancel();
                    Toast.makeText(NewSurveyActivity.this, message, Toast.LENGTH_SHORT).show();
                    if (success == 1) {
                        startActivity(new Intent(NewSurveyActivity.this,SurveyActivity.class));
                        finish();
                    }
                } else {
                    p.cancel();
                    Toast.makeText(NewSurveyActivity.this, "Error Creating Survey\n\n"+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NormalResponse> call, Throwable t) {
                Log.d("URL", call.request().url().toString());
                Toast.makeText(NewSurveyActivity.this, "Error Creating Survey(Device Error)", Toast.LENGTH_SHORT).show();
                p.cancel();
                Log.e("RegisterActivity", t.toString());
            }
        });
    }
}
