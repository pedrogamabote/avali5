package com.example.avali5;

import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CaesarCipherActivity extends AppCompatActivity {

    private SoundPool soundPool;
    private int soundId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caesar_cipher);

        // Configurar SoundPool para efeitos sonoros
        soundPool = new SoundPool.Builder().setMaxStreams(1).build();
        soundId = soundPool.load(this, R.raw.click_sound, 1);

        final EditText inputText = findViewById(R.id.inputText);
        final EditText keyInput = findViewById(R.id.keyInput);
        Button encodeButton = findViewById(R.id.encodeButton);
        Button decodeButton = findViewById(R.id.decodeButton);
        final TextView resultText = findViewById(R.id.resultText);

        encodeButton.setOnClickListener(v -> {
            String text = inputText.getText().toString();
            int key = Integer.parseInt(keyInput.getText().toString());
            String encoded = caesarCipher(text, key, true);
            resultText.setText(encoded);
            animateResult(resultText);
            soundPool.play(soundId, 1f, 1f, 0, 0, 1f);
        });

        decodeButton.setOnClickListener(v -> {
            String text = inputText.getText().toString();
            int key = Integer.parseInt(keyInput.getText().toString());
            String decoded = caesarCipher(text, key, false);
            resultText.setText(decoded);
            animateResult(resultText);
            soundPool.play(soundId, 1f, 1f, 0, 0, 1f);
        });
    }

    private String caesarCipher(String text, int key, boolean encode) {
        int shift = encode ? key : 26 - key;
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isUpperCase(c)) {
                c = (char) ('A' + (c - 'A' + shift) % 26);
            } else if (Character.isLowerCase(c)) {
                c = (char) ('a' + (c - 'a' + shift) % 26);
            }
            result.append(c);
        }
        return result.toString();
    }

    private void animateResult(View view) {
        AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f);
        fadeIn.setDuration(500);
        fadeIn.setFillAfter(true);

        ScaleAnimation scale = new ScaleAnimation(
                0.8f, 1.2f, 0.8f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        scale.setDuration(300);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(fadeIn);
        animationSet.addAnimation(scale);

        view.startAnimation(animationSet);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
    }
}
