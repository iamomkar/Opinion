package com.creativeminds.opinion.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.creativeminds.opinion.R;
import com.google.zxing.Result;


public class ScanAadharQRActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    private String decryptedQR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_fill_qr);
        if(getSupportActionBar() != null && getIntent().getStringExtra("title") != null)
            getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                ScanAadharQRActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(ScanQRActivity.this, result.getText(), Toast.LENGTH_SHORT).show();

                        Intent returnIntent = new Intent();
                        //returnIntent.putExtra("SCAN_RESULT",result.getText());
                        returnIntent.putExtra("SCAN_RESULT",result.getText());
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }


}
