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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tunasoftware.android.Tuna;
import com.tunasoftware.tuna.entities.TunaCard;
import com.tunasoftware.tunasdk.R;

import org.jetbrains.annotations.NotNull;

public class JavaNewCardActivity extends AppCompatActivity {

    private ProgressBar loading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card);
        loading = findViewById(R.id.loading);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Add new card");
        }

        findViewById(R.id.btnGenerateCard).setOnClickListener(view -> addCardAndSave());
        ((CheckBox) findViewById(R.id.cbInformCvv)).setOnCheckedChangeListener((buttonView, isChecked) -> {
            findViewById(R.id.edtCardCvv).setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });
    }

    void addCardAndSave() {
        showLoading();
        Tuna tuna = Tuna.getCurrentSession();
        if (tuna != null) {
            addNewCard(tuna);
        } else {
            Log.i("TUNA", "session not started");
        }
    }

    private void addNewCard(Tuna tuna) {
        final String number = ((EditText) findViewById(R.id.edtCardNumber)).getText().toString();
        final String name = ((EditText) findViewById(R.id.edtCardHolderName)).getText().toString();
        final String month = ((EditText) findViewById(R.id.edtCardExpirationMonth)).getText().toString();
        final String year = ((EditText) findViewById(R.id.edtCardExpirationYear)).getText().toString();
        final String cvv = ((EditText) findViewById(R.id.edtCardCvv)).getText().toString();
        final boolean save = ((CheckBox) findViewById(R.id.cbSaveCard)).isChecked();

        if (cvv.isEmpty()) {
            tuna.addNewCard(number, name, Integer.parseInt(month), Integer.parseInt(year), save, tunaRequestCallback(!save));
        } else {
            tuna.addNewCard(number, name, Integer.parseInt(month), Integer.parseInt(year), cvv, save, tunaRequestCallback(!save));
        }
    }

    private Tuna.TunaRequestCallback<TunaCard> tunaRequestCallback(final boolean detail) {
        return new Tuna.TunaRequestCallback<TunaCard>() {
            @Override
            public void onSuccess(TunaCard result) {
                hideLoading();
                Toast.makeText(JavaNewCardActivity.this, "Card " + result.getMaskedNumber() + " successfully added!!!", Toast.LENGTH_LONG).show();

                Intent intent = detail ? new Intent(JavaNewCardActivity.this, JavaDetailCardActivity.class) : new Intent();
                intent.putExtra(CARD_TOKEN, result.getToken());
                intent.putExtra(CARD_MASKED_NUMBER, result.getMaskedNumber());
                intent.putExtra(CARD_HOLDER_NAME, result.getCardHolderName());
                intent.putExtra(CARD_BRAND, result.getBrand());
                intent.putExtra(CARD_EXPIRATION_MONTH, result.getExpirationMonth());
                intent.putExtra(CARD_EXPIRATION_YEAR, result.getExpirationYear());

                if (detail) {
                    startActivity(intent);
                    setResult(RESULT_CANCELED);
                } else {
                    setResult(RESULT_OK, intent);
                }

                finish();
            }

            @Override
            public void onFailed(@NotNull Throwable e) {
                hideLoading();
                Log.e(LOG_TAG, "Error generate new card!!!", e);
            }
        };
    }

    private void showLoading(){
        loading.setVisibility(View.VISIBLE);
    }

    private void hideLoading(){
        loading.setVisibility(View.GONE);
    }
}
