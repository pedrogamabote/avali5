package com.example.avali5;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        Button cipherButton = findViewById(R.id.cipherButton);
        Button rpsButton = findViewById(R.id.rpsButton);
        Button secretCodeButton = findViewById(R.id.secretCodeButton);
        Button randomizerButton = findViewById(R.id.randomizerButton);

        // Configurar animações para os botões
        Button[] buttons = {cipherButton, rpsButton, secretCodeButton, randomizerButton};
        for (Button button : buttons) {
            button.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).start();
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                            break;
                    }
                    return false;
                }
            });
        }

        cipherButton.setOnClickListener(v ->
                startActivity(new Intent(OptionsActivity.this, CaesarCipherActivity.class)));

        rpsButton.setOnClickListener(v ->
                startActivity(new Intent(OptionsActivity.this, RockPaperScissorsActivity.class)));

        secretCodeButton.setOnClickListener(v ->
                startActivity(new Intent(OptionsActivity.this, SecretCodeActivity.class)));

        randomizerButton.setOnClickListener(v ->
                startActivity(new Intent(OptionsActivity.this, WordRandomizerActivity.class)));
    }
}
