package com.example.avali5;

import android.media.SoundPool;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class RockPaperScissorsActivity extends AppCompatActivity {

    private int userScore = 0;
    private int computerScore = 0;
    private SoundPool soundPool;
    private int winSoundId;
    private int loseSoundId;
    private int drawSoundId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rock_paper_scissors);

        soundPool = new SoundPool.Builder().setMaxStreams(3).build();
        winSoundId = soundPool.load(this, R.raw.win_sound, 1);
        loseSoundId = soundPool.load(this, R.raw.lose_sound, 1);
        drawSoundId = soundPool.load(this, R.raw.draw_sound, 1);

        ImageButton rockButton = findViewById(R.id.rockButton);
        ImageButton paperButton = findViewById(R.id.paperButton);
        ImageButton scissorsButton = findViewById(R.id.scissorsButton);
        final TextView resultTextView = findViewById(R.id.resultTextView);
        final TextView userScoreTextView = findViewById(R.id.userScoreTextView);
        final TextView computerScoreTextView = findViewById(R.id.computerScoreTextView);
        final ImageView computerChoiceImageView = findViewById(R.id.computerChoiceImageView);

        final String[] choices = {"PEDRA", "PAPEL", "TESOURA"};
        final int[] choiceDrawables = {
                R.drawable.ic_rock,
                R.drawable.ic_paper,
                R.drawable.ic_scissors
        };

        rockButton.setOnClickListener(v -> onChoiceSelected("PEDRA", choices, choiceDrawables,
                resultTextView, userScoreTextView, computerScoreTextView, computerChoiceImageView));

        paperButton.setOnClickListener(v -> onChoiceSelected("PAPEL", choices, choiceDrawables,
                resultTextView, userScoreTextView, computerScoreTextView, computerChoiceImageView));

        scissorsButton.setOnClickListener(v -> onChoiceSelected("TESOURA", choices, choiceDrawables,
                resultTextView, userScoreTextView, computerScoreTextView, computerChoiceImageView));
    }

    private void onChoiceSelected(String userChoice, String[] choices, int[] choiceDrawables,
                                  TextView resultTextView, TextView userScoreTextView,
                                  TextView computerScoreTextView, ImageView computerChoiceImageView) {
        String computerChoice = choices[(int) (Math.random() * choices.length)];
        computerChoiceImageView.setImageResource(choiceDrawables[getIndex(choices, computerChoice)]);

        String result = determineWinner(userChoice, computerChoice);
        resultTextView.setText("Você escolheu " + userChoice + "\nComputador escolheu " +
                computerChoice + "\n\n" + result);

        updateScores(userScoreTextView, computerScoreTextView);
        resultTextView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.bounce));

        if (userScore == 2 || computerScore == 2) {
            String finalResult = userScore > computerScore ?
                    "Você venceu o jogo!" : "Computador venceu o jogo!";

            new AlertDialog.Builder(this)
                    .setTitle("Fim de Jogo")
                    .setMessage(finalResult)
                    .setPositiveButton("Jogar novamente", (dialog, which) -> {
                        userScore = 0;
                        computerScore = 0;
                        updateScores(userScoreTextView, computerScoreTextView);
                        resultTextView.setText("Escolha uma opção abaixo");
                        computerChoiceImageView.setImageResource(0);
                    })
                    .setNegativeButton("Sair", (dialog, which) -> finish())
                    .show();
        }
    }

    private String determineWinner(String userChoice, String computerChoice) {
        if (userChoice.equals(computerChoice)) {
            soundPool.play(drawSoundId, 1f, 1f, 0, 0, 1f);
            return "Empate!";
        } else if ((userChoice.equals("PEDRA") && computerChoice.equals("TESOURA")) ||
                (userChoice.equals("PAPEL") && computerChoice.equals("PEDRA")) ||
                (userChoice.equals("TESOURA") && computerChoice.equals("PAPEL"))) {
            userScore++;
            soundPool.play(winSoundId, 1f, 1f, 0, 0, 1f);
            return "Você ganhou!";
        } else {
            computerScore++;
            soundPool.play(loseSoundId, 1f, 1f, 0, 0, 1f);
            return "Você perdeu!";
        }
    }

    private void updateScores(TextView userScoreTextView, TextView computerScoreTextView) {
        userScoreTextView.setText("Você: " + userScore);
        computerScoreTextView.setText("Computador: " + computerScore);
    }

    private int getIndex(String[] array, String value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(value)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
    }
}
