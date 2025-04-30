package com.example.avali5;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private List<String> items;
    private Category category;
    private OnItemEditListener editListener;

    public interface OnItemEditListener {
        void onItemEdited(int position, String newItem);
    }

    public ItemAdapter(List<String> items, Category category) {
        this.items = items;
        this.category = category;
    }

    public void setOnItemEditListener(OnItemEditListener listener) {
        this.editListener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        String item = items.get(position);
        holder.itemTextView.setText(item);

        holder.editItemButton.setOnClickListener(v -> showEditDialog(holder, position, item));

        holder.itemTextView.setOnClickListener(v -> showEditDialog(holder, position, item));
    }

    private void showEditDialog(@NonNull ItemViewHolder holder, int position, String currentItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
        builder.setTitle("Editar Item");

        final EditText input = new EditText(holder.itemView.getContext());
        input.setText(currentItem);
        builder.setView(input);

        builder.setPositiveButton("Salvar", (dialog, which) -> {
            String newItem = input.getText().toString().trim();
            if (!newItem.isEmpty() && !newItem.equals(currentItem)) {
                items.set(position, newItem);

                category.setItems(items);

                notifyItemChanged(position);

                if (editListener != null) {
                    editListener.onItemEdited(position, newItem);
                }
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemTextView;
        ImageButton editItemButton;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTextView = itemView.findViewById(R.id.itemTextView);
            editItemButton = itemView.findViewById(R.id.editItemButton);
        }
    }
}