package com.tunasoftware.tunasdk.java.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.tunasoftware.tuna.entities.TunaCard;
import com.tunasoftware.tunasdk.R;

import java.util.ArrayList;
import java.util.List;

public class ListCardsAdapter extends RecyclerView.Adapter<ListCardsAdapter.ViewHolder> {

    private final List<TunaCard> mList = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.model_card, viewGroup, Boolean.FALSE);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(this.getItem(i));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Nullable
    public TunaCard getItem(int i) {
        return i > this.mList.size() - 1 ? null : this.mList.get(i);
    }

    public void setItems(List<TunaCard> cards) {
        this.mList.clear();
        this.mList.addAll(cards);
        this.notifyDataSetChanged();
    }

    public void addItemTop(TunaCard card) {
        this.mList.add(0, card);
        this.notifyItemInserted(0);
    }

    public void deleteItemByToken(String token) {
        if (token == null || token.isEmpty()) return;
        for (int i = 0; i < mList.size(); i++) {
            TunaCard card = mList.get(i);
            if (token.equals(card.getToken())) {
                mList.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTextViewNumber, mTextViewHolderName, mTextViewExpiration;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mTextViewNumber = itemView.findViewById(R.id.tvNumber);
            this.mTextViewHolderName = itemView.findViewById(R.id.tvHolderName);
            this.mTextViewExpiration = itemView.findViewById(R.id.tvExpiration);
        }

        void bind(TunaCard card) {
            this.clear();
            if (card == null) return;

            this.mTextViewNumber.setText(String.format("%s - %s", card.getMaskedNumber().trim(), card.getBrand()));
            this.mTextViewHolderName.setText(card.getCardHolderName());
            this.mTextViewExpiration.setText(String.format("%s/%s", card.getExpirationMonth(), card.getExpirationYear()));
        }

        private void clear() {
            this.mTextViewNumber.setText(null);
            this.mTextViewHolderName.setText(null);
            this.mTextViewExpiration.setText(null);
        }
    }
}