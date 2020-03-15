package org.sse.cbc.car;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jyn.vcview.VerificationCodeView;

public class ValidActivity extends AppCompatActivity
        implements VerificationCodeView.OnCodeFinishListener {

    private VerificationCodeView verificationCodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valid);
        verificationCodeView = findViewById(R.id.verification);
        verificationCodeView.setOnCodeFinishListener(this);
    }

    @Override
    public void onTextChange(View view, String content) {

    }

    @Override
    public void onComplete(View view, String content) {
        Intent intent = new Intent(ValidActivity.this, VoiceActivity.class);
        startActivity(intent);
    }
}
