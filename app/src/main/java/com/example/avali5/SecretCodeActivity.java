package com.example.avali5;

import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.avali5.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.view.ViewGroup;

public class SecretCodeActivity extends AppCompatActivity {

    private List<Integer> secretCode;
    private int attempts = 0;
    private AttemptsAdapter adapter;
    private long startTime;
    private SoundPool soundPool;
    private int successSoundId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secret_code);

        // Inicializando o SoundPool para o som de sucesso
        soundPool = new SoundPool.Builder().setMaxStreams(1).build();
        successSoundId = soundPool.load(this, R.raw.success_sound, 1);

        // Gerar código secreto de 4 dígitos (0-9)
        secretCode = generateSecretCode();
        startTime = System.currentTimeMillis();

        // Inicialização dos componentes
        RecyclerView attemptsRecyclerView = findViewById(R.id.attemptsRecyclerView);
        final EditText guessEditText = findViewById(R.id.guessEditText);
        Button submitButton = findViewById(R.id.submitButton);
        final TextView hintTextView = findViewById(R.id.hintTextView);

        // Configuração do adapter do RecyclerView
        adapter = new AttemptsAdapter();
        attemptsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        attemptsRecyclerView.setAdapter(adapter);

        // Configuração do botão de submissão
        submitButton.setOnClickListener(v -> {
            String guessText = guessEditText.getText().toString();
            if (guessText.length() != 4 || !guessText.matches("\\d+")) {
                Toast.makeText(this, "Digite 4 dígitos numéricos", Toast.LENGTH_SHORT).show();
                return;
            }

            attempts++;
            List<Integer> guess = new ArrayList<>();
            for (char c : guessText.toCharArray()) {
                guess.add(Character.getNumericValue(c));
            }

            // Verificar a tentativa
            List<Pair> result = checkGuess(guess);
            adapter.addAttempt(new Attempt(guess, result));
            attemptsRecyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);

            // Verificar se a tentativa está correta
            if (isAllCorrect(result)) {
                long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
                showSuccessDialog(attempts, elapsedTime);
                soundPool.play(successSoundId, 1f, 1f, 0, 0, 1f);
            } else {
                // Mostrar dicas
                StringBuilder hint = new StringBuilder("Dica: ");
                for (Pair pair : result) {
                    hint.append(pair.digit).append(" (").append(pair.status).append("), ");
                }
                hintTextView.setText(hint.substring(0, hint.length() - 2));
                hintTextView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            }

            // Limpar o campo de entrada após a tentativa
            guessEditText.getText().clear();
        });
    }

    // Método para gerar o código secreto
    private List<Integer> generateSecretCode() {
        List<Integer> code = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            code.add(random.nextInt(10));
        }
        return code;
    }

    // Método para verificar a tentativa
    private List<Pair> checkGuess(List<Integer> guess) {
        List<Pair> result = new ArrayList<>();
        for (int i = 0; i < guess.size(); i++) {
            int digit = guess.get(i);
            String status;
            if (digit == secretCode.get(i)) {
                status = "CORRETO";
            } else if (secretCode.contains(digit)) {
                status = "POSIÇÃO ERRADA";
            } else {
                status = "ERRADO";
            }
            result.add(new Pair(digit, status));
        }
        return result;
    }

    // Método para verificar se todas as tentativas estão corretas
    private boolean isAllCorrect(List<Pair> result) {
        for (Pair pair : result) {
            if (!pair.status.equals("CORRETO")) {
                return false;
            }
        }
        return true;
    }

    // Método para mostrar o diálogo de sucesso
    private void showSuccessDialog(int attempts, long elapsedTime) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_success, null);

        TextView attemptsTextView = dialogView.findViewById(R.id.attemptsTextView);
        TextView timeTextView = dialogView.findViewById(R.id.timeTextView);
        TextView secretMessage = dialogView.findViewById(R.id.secretMessage);

        attemptsTextView.setText("Tentativas: " + attempts);
        timeTextView.setText("Tempo: " + elapsedTime + "s");

        secretMessage.setVisibility(View.VISIBLE);
        secretMessage.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));

        new AlertDialog.Builder(this)
                .setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
    }

    // Classe para representar uma tentativa
    private static class Attempt {
        List<Integer> guess;
        List<Pair> result;

        Attempt(List<Integer> guess, List<Pair> result) {
            this.guess = guess;
            this.result = result;
        }
    }

    // Classe para representar o par de dígito e status
    private static class Pair {
        int digit;
        String status;

        Pair(int digit, String status) {
            this.digit = digit;
            this.status = status;
        }
    }

    // Adapter para as tentativas
    private class AttemptsAdapter extends RecyclerView.Adapter<AttemptsAdapter.AttemptViewHolder> {
        private final List<Attempt> attemptsList = new ArrayList<>();

        class AttemptViewHolder extends RecyclerView.ViewHolder {
            TextView guessText;
            TextView resultText;

            AttemptViewHolder(View view) {
                super(view);
                guessText = view.findViewById(R.id.guessText);
                resultText = view.findViewById(R.id.resultText);
            }
        }

        void addAttempt(Attempt attempt) {
            attemptsList.add(attempt);
            notifyItemInserted(attemptsList.size() - 1);
        }

        @Override
        public AttemptViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_attempt, parent, false);
            return new AttemptViewHolder(view);
        }

        @Override
        public void onBindViewHolder(AttemptViewHolder holder, int position) {
            Attempt attempt = attemptsList.get(position);
            StringBuilder guessStr = new StringBuilder("Tentativa " + (position + 1) + ": ");
            for (int digit : attempt.guess) {
                guessStr.append(digit);
            }
            holder.guessText.setText(guessStr.toString());

            StringBuilder resultStr = new StringBuilder();
            for (Pair pair : attempt.result) {
                resultStr.append(pair.digit).append(" (").append(pair.status).append("), ");
            }
            holder.resultText.setText(resultStr.substring(0, resultStr.length() - 2));
        }

        @Override
        public int getItemCount() {
            return attemptsList.size();
        }
    }
}
