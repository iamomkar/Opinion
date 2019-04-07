package com.creativeminds.opinion.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.creativeminds.opinion.R;
import com.creativeminds.opinion.models.Poll;
import com.creativeminds.opinion.models.PollDetailsResponse;
import com.creativeminds.opinion.retrofit.APIClient;
import com.creativeminds.opinion.retrofit.APIInterface;
import com.google.gson.Gson;

import net.glxn.qrgen.android.QRCode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowPollQRActivity extends AppCompatActivity {

    ProgressDialog p;
    APIInterface apiInterface;
    int success;
    String message,pollID;
    Poll poll;
    ImageView pollQRView;
    Button saveBtn,shareBtn;
    Bitmap myBitmap;
    private static final Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_poll_qr);

        pollID = getIntent().getStringExtra("pollid");
        getPollDetails(pollID);
        pollQRView = (ImageView)findViewById(R.id.poll_qr_view);
        saveBtn = (Button)findViewById(R.id.save_qr_btn);
        shareBtn = (Button)findViewById(R.id.share_qr_btn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String path = Environment.getExternalStorageDirectory().toString();
                    OutputStream fOut = null;
                    File file = new File(path, "Poll " + pollID + ".jpg"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
                    fOut = new FileOutputStream(file);
                    myBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                    fOut.flush(); // Not really required
                    fOut.close(); // do not forget to close the stream
                    MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
                    Toast.makeText(ShowPollQRActivity.this, "QR Code Saved Successfully", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(ShowPollQRActivity.this, "Failed to Save QR Code", Toast.LENGTH_SHORT).show();
                }
            }
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), myBitmap,"Poll QR Code", null);
                Uri bitmapUri = Uri.parse(bitmapPath);
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setType("image/png");
                startActivity(intent);
            }
        });


    }

    public void getPollDetails(String pid) {
        p = ProgressDialog.show(ShowPollQRActivity.this, "Getting Poll Details", "Please wait...", true, false);
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
                         myBitmap = QRCode.from(gson.toJson(poll)).withSize(1000,1000).bitmap();
                        pollQRView.setImageBitmap(myBitmap);
                        Log.d("POLL", "onResponse: "+poll.toString());
                    }else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    p.cancel();
                    Toast.makeText(getApplicationContext(), "Error getting Poll details\n\n"+response.message(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<PollDetailsResponse> call, Throwable t) {
                Log.d("URL", call.request().url().toString());
                Toast.makeText(getApplicationContext(), "Error Creating Poll(Device Error)", Toast.LENGTH_SHORT).show();
                p.cancel();
                Log.e("RegisterActivity", t.toString());
                finish();
            }
        });
    }
}
