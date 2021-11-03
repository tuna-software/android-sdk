package com.tunasoftware.tunasdk.java;

import static com.tunasoftware.tunasdk.java.utils.Extras.CARD_BRAND;
import static com.tunasoftware.tunasdk.java.utils.Extras.CARD_EXPIRATION_MONTH;
import static com.tunasoftware.tunasdk.java.utils.Extras.CARD_EXPIRATION_YEAR;
import static com.tunasoftware.tunasdk.java.utils.Extras.CARD_HOLDER_NAME;
import static com.tunasoftware.tunasdk.java.utils.Extras.CARD_MASKED_NUMBER;
import static com.tunasoftware.tunasdk.java.utils.Extras.CARD_TOKEN;
import static com.tunasoftware.tunasdk.java.utils.Extras.LOG_TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tunasoftware.android.Tuna;
import com.tunasoftware.tuna.entities.TunaCard;
import com.tunasoftware.tunasdk.R;
import com.tunasoftware.tunasdk.java.adapters.ListCardsAdapter;
import com.tunasoftware.tunasdk.java.interfaces.RecyclerViewOnClickListener;
import com.tunasoftware.tunasdk.java.listners.RecyclerViewTouchListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class JavaListCardsActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_NEW_CARD = 1001;
    private static final int REQUEST_CODE_DETAIL_CARD = 1002;

    private View loading;

    private final ListCardsAdapter adapter = new ListCardsAdapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_cards);
        loading = findViewById(R.id.loading);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("List of cards");
        }

        findViewById(R.id.btnAddCard).setOnClickListener(view -> {
            Intent intent = new Intent(JavaListCardsActivity.this, JavaNewCardActivity.class);
            startActivityForResult(intent, REQUEST_CODE_NEW_CARD);
        });

        RecyclerView recyclerView = findViewById(R.id.listCards);
        recyclerView.setHasFixedSize(Boolean.TRUE);
        recyclerView.setLayoutManager(new LinearLayoutManager(JavaListCardsActivity.this));
        recyclerView.addItemDecoration(new DividerItemDecoration(JavaListCardsActivity.this, DividerItemDecoration.VERTICAL));
        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(JavaListCardsActivity.this, recyclerView, onRecyclerViewOnClickListener()));
        recyclerView.setAdapter(this.adapter);
        Tuna session = Tuna.getCurrentSession();
        if (session == null){
            getSessionIdFromBackend();
        } else {
            getCards(session);
        }
    }

    private void getSessionIdFromBackend(){
        showLoading();
        Tuna.getSandboxSessionId(new Tuna.TunaRequestCallback<String>() {
            @Override
            public void onSuccess(String result) {
                hideLoading();
                Tuna tuna = Tuna.startSession(result);
                getCards(tuna);
            }

            @Override
            public void onFailed(@NotNull Throwable e) {
                hideLoading();
                Log.e(LOG_TAG, "Error start session!!!", e);
            }
        });
    }


    private void getCards(Tuna tuna) {
        tuna.getCardList(new Tuna.TunaRequestCallback<List<TunaCard>>() {
            @Override
            public void onSuccess(List<TunaCard> result) {
                hideLoading();
                adapter.setItems(result);
            }

            @Override
            public void onFailed(@NotNull Throwable e) {
                hideLoading();
                Log.e(LOG_TAG, "Error get cards!!!", e);
            }
        });
    }

    private RecyclerViewOnClickListener onRecyclerViewOnClickListener() {
        return new RecyclerViewOnClickListener() {
            @Override
            public void onClick(View view, int position) {
                TunaCard card = adapter.getItem(position);
                if (card == null) return;

                Intent intent = new Intent(JavaListCardsActivity.this, JavaDetailCardActivity.class);
                intent.putExtra(CARD_MASKED_NUMBER, card.getMaskedNumber());
                intent.putExtra(CARD_HOLDER_NAME, card.getCardHolderName());
                intent.putExtra(CARD_BRAND, card.getBrand());
                intent.putExtra(CARD_EXPIRATION_MONTH, card.getExpirationMonth());
                intent.putExtra(CARD_EXPIRATION_YEAR, card.getExpirationYear());
                intent.putExtra(CARD_TOKEN, card.getToken());

                startActivityForResult(intent, REQUEST_CODE_DETAIL_CARD);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_NEW_CARD:
                    adapter.addItemTop(new TunaCard(
                            data.getStringExtra(CARD_TOKEN),
                            data.getStringExtra(CARD_BRAND),
                            data.getStringExtra(CARD_HOLDER_NAME),
                            data.getIntExtra(CARD_EXPIRATION_MONTH, 0),
                            data.getIntExtra(CARD_EXPIRATION_YEAR, 0),
                            data.getStringExtra(CARD_MASKED_NUMBER)));
                    break;
                case REQUEST_CODE_DETAIL_CARD:
                    adapter.deleteItemByToken(data.getStringExtra(CARD_TOKEN));
                    break;
            }
        }
    }

    private void showLoading(){
        loading.setVisibility(View.VISIBLE);
    }

    private void hideLoading(){
        loading.setVisibility(View.GONE);
    }
}
