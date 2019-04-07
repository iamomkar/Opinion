package com.creativeminds.opinion.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.creativeminds.opinion.R;
import com.creativeminds.opinion.models.AadharCard;
import com.creativeminds.opinion.models.NormalResponse;
import com.creativeminds.opinion.retrofit.APIClient;
import com.creativeminds.opinion.retrofit.APIInterface;
import com.creativeminds.opinion.utils.AadhaarXMLParser;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Digits;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements Validator.ValidationListener {

    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;

    @Length(min = 3, message = "Enter Proper Name")
    private EditText mName;
    @NotEmpty
    @Email
    private EditText mEmail;
    @NotEmpty
    @Digits(integer = 10)
    @Length(min = 10, max = 10, message = "Invalid Phone Number")
    private EditText mPhone;
    @Digits(integer = 12)
    @NotEmpty
    private EditText mUidno;
    @Password(min = 8, scheme = Password.Scheme.ANY)
    private EditText mPassword;
    @ConfirmPassword
    private EditText mPassword2;
    private RadioGroup mRadioGrp;
    @NotEmpty(message = "Enter date of birth")
    private EditText mDobBox;
    private Spinner mOccupation;
    private Spinner mState;
    private Spinner mCity;
    @Digits(integer = 6)
    @Length(min = 6, max = 6, message = "Invalid Pin code")
    private EditText mPinCode;
    private Spinner mSecurityQuestion;
    @NotEmpty(message = "Please enter answer.")
    private EditText mSecurityAnswer;
    private Button mRegister, mAutoFill;
    private Validator validator;
    private RadioButton mRadioMale, mRadioFemale, mRadioTrans;
    APIInterface apiInterface;
    ProgressDialog p;
    String message, name, pass, uidno, gender, occupation, email, phone, state, city, pincode, photo_url, sec_question, sec_answer;
    String dob, latlong, verified, organization, creation_datetime;
    int success, randotp;
    List<String> stateList = new ArrayList<>();
    List<String> cityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        findViews();
        mRadioGrp.clearCheck();
        validator = new Validator(this);
        validator.setValidationListener(this);

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                mDobBox.setText(sdf.format(myCalendar.getTime()));
            }
        };
        mDobBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner, getResources().getStringArray(R.array.occupation));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mOccupation.setAdapter(adapter);

        ArrayAdapter<String> adapterstate = new ArrayAdapter<String>(this, R.layout.spinner, getResources().getStringArray(R.array.india_states));
        adapterstate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mState.setAdapter(adapterstate);

        ArrayAdapter<String> adaptercity = new ArrayAdapter<String>(this, R.layout.spinner, getResources().getStringArray(R.array.maharashtra_states));
        adaptercity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCity.setAdapter(adaptercity);

        ArrayAdapter<String> adaptersecques = new ArrayAdapter<String>(this, R.layout.spinner, getResources().getStringArray(R.array.security_questions));
        adaptersecques.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSecurityQuestion.setAdapter(adaptersecques);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });

        mAutoFill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(RegisterActivity.this, ScanQRActivity.class).putExtra("title","Scan Aadhaar Card "), 1);
            }
        });

    }

    public void findViews() {
        mName = (EditText) findViewById(R.id.name);
        mEmail = (EditText) findViewById(R.id.email);
        mPhone = (EditText) findViewById(R.id.phone);
        mUidno = (EditText) findViewById(R.id.uidno);
        mPassword = (EditText) findViewById(R.id.password);
        mPassword2 = (EditText) findViewById(R.id.password2);
        mRadioGrp = (RadioGroup) findViewById(R.id.radioGrp);
        mRadioMale = (RadioButton) findViewById(R.id.radioM);
        mRadioFemale = (RadioButton) findViewById(R.id.radioF);
        mRadioTrans = (RadioButton) findViewById(R.id.radioT);
        mDobBox = (EditText) findViewById(R.id.dob_box);
        mOccupation = (Spinner) findViewById(R.id.occupation);
        mState = (Spinner) findViewById(R.id.state);
        mCity = (Spinner) findViewById(R.id.city);
        mPinCode = (EditText) findViewById(R.id.pin_code);
        mSecurityQuestion = (Spinner) findViewById(R.id.security_question);
        mSecurityAnswer = (EditText) findViewById(R.id.security_answer);
        mRegister = (Button) findViewById(R.id.register);
        mAutoFill = (Button) findViewById(R.id.autofill);

    }

    @Override
    public void onValidationSucceeded() {
        name = mName.getText().toString();
        pass = mPassword2.getText().toString();
        uidno = mUidno.getText().toString();
        int checkedRadioButtonId = mRadioGrp.getCheckedRadioButtonId();
        RadioButton selectedId = (RadioButton) findViewById(checkedRadioButtonId);
        gender = selectedId.getText().toString();
        occupation = mOccupation.getSelectedItem().toString();
        email = mEmail.getText().toString();
        phone = mPhone.getText().toString();
        state = mState.getSelectedItem().toString();
        city = mCity.getSelectedItem().toString();
        pincode = mPinCode.getText().toString();
        photo_url = "empty";
        sec_question = mSecurityQuestion.getSelectedItem().toString();
        sec_answer = mSecurityAnswer.getText().toString();
        dob = mDobBox.getText().toString();
        latlong = "empty";
        verified = "empty";
        organization = "empty";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        creation_datetime = sdf.format(new Date());
        p = ProgressDialog.show(RegisterActivity.this, "Sending Verification Mail", "Sending...", true, false);
        sendVerificationMail();

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

    public void sendVerificationMail() {
        randotp = generateRandomIntIntRange(1000, 9999);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<NormalResponse> call = apiInterface.sendVerificationMail(mName.getText().toString(), mEmail.getText().toString(), String.valueOf(randotp));
        call.enqueue(new Callback<NormalResponse>() {
            @Override
            public void onResponse(Call<NormalResponse> call, Response<NormalResponse> response) {
                Log.d("URL", call.request().url().toString());
                if (response.isSuccessful()) {
                    success = response.body().getSuccess();
                    message = response.body().getMessage();
                    p.cancel();
                    if (success == 1) {
                        verifyOTP();
                    }
                    Toast.makeText(RegisterActivity.this, "Success:- " + success + " \n Message :- " + message, Toast.LENGTH_SHORT).show();
                } else {
                    p.cancel();
                    Toast.makeText(RegisterActivity.this, "Error Sending Mail Try Again\n\n" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NormalResponse> call, Throwable t) {
                Log.d("URL", call.request().url().toString());
                Toast.makeText(RegisterActivity.this, "Error Sending Verification Mail(Device Error)", Toast.LENGTH_SHORT).show();
                p.cancel();
                Log.e("RegisterActivity", t.toString());
            }
        });
    }

    public void registerNewUser() {
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<NormalResponse> call = apiInterface.registerNewUser(name, pass, uidno, gender, occupation, email, phone, state
                , city, pincode, sec_question, sec_answer, dob, creation_datetime, photo_url, latlong, verified, organization);
        call.enqueue(new Callback<NormalResponse>() {
            @Override
            public void onResponse(Call<NormalResponse> call, Response<NormalResponse> response) {
                Log.d("URL", call.request().url().toString());
                if (response.isSuccessful()) {
                    success = response.body().getSuccess();
                    message = response.body().getMessage();
                    p.cancel();
                    if (success == 1) {
                        finish();
                    }
                    Toast.makeText(RegisterActivity.this, "Success:- " + success + " \n Message :- " + message, Toast.LENGTH_SHORT).show();
                } else {
                    p.cancel();
                    Toast.makeText(RegisterActivity.this, "Error Sending Mail Try Again\n\n" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NormalResponse> call, Throwable t) {
                Log.d("URL", call.request().url().toString());
                Toast.makeText(RegisterActivity.this, "Error Sending Verification Mail(Device Error)", Toast.LENGTH_SHORT).show();
                p.cancel();
                Log.e("RegisterActivity", t.toString());
            }
        });
    }

    public void verifyOTP() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegisterActivity.this);
        final EditText editText = new EditText(RegisterActivity.this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setHint("Enter OTP");
        alertDialogBuilder.setView(editText);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Verify OTP").setMessage("Enter the One Time Password sent to your email id.(" + mEmail.getText().toString() + ")").setPositiveButton("Verify", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (editText.getText().toString().equals(String.valueOf(randotp))) {
                    p = ProgressDialog.show(RegisterActivity.this, "Registering you now...", "OTP successfully verified", true, false);
                    registerNewUser();
                } else {
                    Toast.makeText(RegisterActivity.this, "Wrong OTP Entered! Try Again.", Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();
    }

    public static int generateRandomIntIntRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                Toast.makeText(RegisterActivity.this, result, Toast.LENGTH_SHORT).show();

                String contents = data.getStringExtra("SCAN_RESULT");
                Log.e("scan result", " " + contents);
                try {
                    AadharCard newCard = new AadhaarXMLParser().parse(contents);

                    mName.setText(newCard.name);
                    mUidno.setText(newCard.uid);
                    mPinCode.setText(newCard.pincode);
                    mDobBox.setText(newCard.dob);
                    stateList.clear();
                    stateList.add(newCard.state);
                    ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(this, R.layout.spinner, stateList);
                    stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mState.setAdapter(stateAdapter);
                    cityList.clear();
                    cityList.add(newCard.dist);
                    ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(this, R.layout.spinner, cityList);
                    cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mCity.setAdapter(cityAdapter);

                    if (newCard.gender.equals("M") || newCard.gender.toLowerCase().equals("male"))
                        mRadioGrp.check(R.id.radioM);
                    else
                        mRadioGrp.check(R.id.radioF);

                } catch (XmlPullParserException e) {
                    Toast.makeText(getApplicationContext(), " Card Not Supported", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Persistence.storeAadhaarCard(this, newCard);
            }
            if (resultCode == RESULT_CANCELED) {
                //handle cancel
                Log.e("scan result", " cancle");
            }
        }

    }

}

