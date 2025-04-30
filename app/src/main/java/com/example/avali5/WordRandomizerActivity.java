package com.example.avali5;

import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordRandomizerActivity extends AppCompatActivity {

    private final List<Category> categories = new ArrayList<>();
    private CategoryAdapter adapter;
    private SoundPool soundPool;
    private int randomizeSoundId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_randomizer);

        soundPool = new SoundPool.Builder().setMaxStreams(1).build();
        randomizeSoundId = soundPool.load(this, R.raw.click_sound, 1);

        RecyclerView categoriesRecyclerView = findViewById(R.id.categoriesRecyclerView);
        Button randomizeButton = findViewById(R.id.randomizeButton);
        final TextView resultTextView = findViewById(R.id.resultTextView);
        Button addCategoryButton = findViewById(R.id.addCategoryButton);

        adapter = new CategoryAdapter(categories);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoriesRecyclerView.setAdapter(adapter);

        addSampleCategories();

        randomizeButton.setOnClickListener(v -> {
            if (!categories.isEmpty()) {
                Category randomCategory = categories.get(new Random().nextInt(categories.size()));
                if (!randomCategory.getItems().isEmpty()) {
                    String randomItem = randomCategory.getItems().get(
                            new Random().nextInt(randomCategory.getItems().size()));
                    showResult(randomCategory, randomItem, resultTextView);
                } else {
                    Toast.makeText(this, "A categoria " + randomCategory.getName() + " está vazia",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Adicione categorias primeiro", Toast.LENGTH_SHORT).show();
            }
        });

        addCategoryButton.setOnClickListener(v -> showAddCategoryDialog());
    }

    private void showResult(Category category, String item, TextView resultTextView) {
        resultTextView.setText("Categoria: " + category.getName() + "\nItem: " + item);

        // Animação do resultado
        AnimationSet animSet = new AnimationSet(true);
        animSet.addAnimation(new AlphaAnimation(0f, 1f) {{ setDuration(500); }});
        animSet.addAnimation(new ScaleAnimation(
                0.5f, 1.2f, 0.5f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        ) {{ setDuration(300); }});
        resultTextView.startAnimation(animSet);

        soundPool.play(randomizeSoundId, 1f, 1f, 0, 0, 1f);
    }

    private void addSampleCategories() {
        List<String> fruitsItems = new ArrayList<>();
        fruitsItems.add("Maçã");
        fruitsItems.add("Banana");
        fruitsItems.add("Laranja");
        fruitsItems.add("Uva");
        Category fruits = new Category("Frutas", fruitsItems);

        List<String> colorsItems = new ArrayList<>();
        colorsItems.add("Vermelho");
        colorsItems.add("Azul");
        colorsItems.add("Verde");
        colorsItems.add("Amarelo");
        Category colors = new Category("Cores", colorsItems);

        List<String> countriesItems = new ArrayList<>();
        countriesItems.add("Brasil");
        countriesItems.add("EUA");
        countriesItems.add("Japão");
        countriesItems.add("França");
        Category countries = new Category("Países", countriesItems);

        categories.add(fruits);
        categories.add(colors);
        categories.add(countries);
        adapter.notifyDataSetChanged();
    }

    private void showAddCategoryDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_category, null);
        final EditText categoryNameEditText = dialogView.findViewById(R.id.categoryNameEditText);
        final EditText itemsEditText = dialogView.findViewById(R.id.itemsEditText);

        new AlertDialog.Builder(this)
                .setTitle("Adicionar Categoria")
                .setView(dialogView)
                .setPositiveButton("Adicionar", (dialog, which) -> {
                    String name = categoryNameEditText.getText().toString();
                    if (!name.isEmpty()) {
                        String[] itemsArray = itemsEditText.getText().toString().split(",");
                        List<String> items = new ArrayList<>();
                        for (String item : itemsArray) {
                            String trimmedItem = item.trim();
                            if (!trimmedItem.isEmpty()) {
                                items.add(trimmedItem);
                            }
                        }
                        categories.add(new Category(name, items));
                        adapter.notifyItemInserted(categories.size() - 1);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
    }
}