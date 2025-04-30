package com.example.avali5;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button nextButton = findViewById(R.id.nextButton);

        // Animação do botão
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1f, 1.1f, 1f, 1.1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        scaleAnimation.setDuration(500);
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        scaleAnimation.setRepeatCount(Animation.INFINITE);

        nextButton.startAnimation(scaleAnimation);

        nextButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OptionsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_up, R.anim.fade_out);
        });
    }
}