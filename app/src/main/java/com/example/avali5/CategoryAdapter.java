package com.example.avali5;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<Category> categories;

    public CategoryAdapter(List<Category> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.categoryNameTextView.setText(category.getName());

        // Configura botão de editar
        holder.editCategoryButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
            builder.setTitle("Editar Categoria");

            final EditText input = new EditText(holder.itemView.getContext());
            input.setText(category.getName());
            builder.setView(input);

            builder.setPositiveButton("Salvar", (dialog, which) -> {
                String newName = input.getText().toString().trim();
                if (!newName.isEmpty()) {
                    category.setName(newName);
                    notifyItemChanged(position);
                }
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

            builder.show();
        });

        ItemAdapter itemAdapter = new ItemAdapter(category.getItems(), category);
        holder.itemsRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.itemsRecyclerView.setAdapter(itemAdapter);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryNameTextView;
        RecyclerView itemsRecyclerView;
        Button editCategoryButton;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryNameTextView = itemView.findViewById(R.id.categoryNameTextView);
            itemsRecyclerView = itemView.findViewById(R.id.itemsRecyclerView);
            editCategoryButton = itemView.findViewById(R.id.editCategoryButton);
        }
    }
}
