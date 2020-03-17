package org.sse.cbc.car;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.skyfishjy.library.RippleBackground;

public class VoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);

        final RippleBackground rippleBackground = findViewById(R.id.content);
        ImageView imageView = findViewById(R.id.centerImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rippleBackground.isRippleAnimationRunning()) {
                    rippleBackground.stopRippleAnimation();
                    startActivity(new Intent(VoiceActivity.this, StartActivity.class));
                } else {
                    rippleBackground.startRippleAnimation();
                }
            }
        });
    }
}
