package com.tunasoftware.tunasdk.java;

import static com.tunasoftware.tunasdk.java.utils.Extras.LOG_TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.tunasoftware.android.Tuna;
import com.tunasoftware.tuna.entities.TunaCard;
import com.tunasoftware.tuna.exceptions.TunaCardCanNotBeRemovedException;
import com.tunasoftware.tuna.exceptions.TunaCardDataMissedException;
import com.tunasoftware.tuna.exceptions.TunaCardNumberAlreadyTokenizedException;
import com.tunasoftware.tuna.exceptions.TunaConnectException;
import com.tunasoftware.tuna.exceptions.TunaCustomerDataMissedException;
import com.tunasoftware.tuna.exceptions.TunaException;
import com.tunasoftware.tuna.exceptions.TunaInvalidCardHolderNameException;
import com.tunasoftware.tuna.exceptions.TunaInvalidCardNumberException;
import com.tunasoftware.tuna.exceptions.TunaInvalidCardTokenException;
import com.tunasoftware.tuna.exceptions.TunaInvalidExpirationDateException;
import com.tunasoftware.tuna.exceptions.TunaInvalidPartnerTokenException;
import com.tunasoftware.tuna.exceptions.TunaPartnerGuidMissedException;
import com.tunasoftware.tuna.exceptions.TunaReachedMaxCardsByUserException;
import com.tunasoftware.tuna.exceptions.TunaReachedMaxSessionsByUserException;
import com.tunasoftware.tuna.exceptions.TunaRequestNullException;
import com.tunasoftware.tuna.exceptions.TunaRequestTokenMissedException;
import com.tunasoftware.tuna.exceptions.TunaSDKNotInitiatedException;
import com.tunasoftware.tuna.exceptions.TunaSessionExpiredException;
import com.tunasoftware.tuna.exceptions.TunaSessionInvalidException;
import com.tunasoftware.tuna.exceptions.TunaTimeoutException;
import com.tunasoftware.tuna.exceptions.TunaTokenCanNotBeRemovedException;
import com.tunasoftware.tuna.exceptions.TunaTokenNotFoundException;
import com.tunasoftware.tunasdk.R;
import com.tunasoftware.tunasdk.java.utils.Extras;

import org.jetbrains.annotations.NotNull;

public class JavaDetailCardActivity extends AppCompatActivity {

    private Tuna tuna;
    private TunaCard card;
    private ProgressBar loading;
    private Button btnBind;
    private View cardModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_card);
        btnBind = findViewById(R.id.btnBind);
        cardModel = findViewById(R.id.cardModel);
        loading = findViewById(R.id.loading);

        Intent intent = getIntent();
        if (intent != null) {
            final String cardMaskedNumber = intent.getStringExtra(Extras.CARD_MASKED_NUMBER);
            final String cardHolderName = intent.getStringExtra(Extras.CARD_HOLDER_NAME);
            final String cardBrand = intent.getStringExtra(Extras.CARD_BRAND);
            final int cardExpirationMonth = intent.getIntExtra(Extras.CARD_EXPIRATION_MONTH, 0);
            final int cardExpirationYear = intent.getIntExtra(Extras.CARD_EXPIRATION_YEAR, 0);
            final String cardToken = intent.getStringExtra(Extras.CARD_TOKEN);

            card = new TunaCard(
                    cardToken,
                    cardBrand,
                    cardHolderName,
                    cardExpirationMonth,
                    cardExpirationYear,
                    cardMaskedNumber
            );

            ((TextView) findViewById(R.id.tvNumber)).setText(String.format("%s - %s", cardMaskedNumber.trim(), cardBrand));
            ((TextView) findViewById(R.id.tvHolderName)).setText(cardHolderName);
            ((TextView) findViewById(R.id.tvExpiration)).setText(String.format("%s/%s", cardExpirationMonth, cardExpirationYear));

            btnBind.setOnClickListener(view -> showDialog());
        }

        tuna = Tuna.getCurrentSession();
        if (tuna == null){
            Log.i("TUNA", "session has not been started");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail_card, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_card) {
            deleteCard();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {
        final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        final EditText editText = new EditText(this);
        editText.setLayoutParams(params);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
        editText.setHint("CVV");

        final int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
        final LinearLayout layout = new LinearLayout(this);
        layout.setLayoutParams(params);
        layout.addView(editText);
        layout.setPadding(padding, padding, padding, padding);

        new AlertDialog.Builder(this)
                .setTitle("Inform the cvv of the card")
                .setView(layout)
                .setNegativeButton("CANCEL", null)
                .setPositiveButton("OK", (dialog, which) -> bindCard(editText.getText().toString()))
                .create()
                .show();
    }

    private void handleError(Throwable e){
        if (e instanceof TunaSDKNotInitiatedException) {
            Log.e(LOG_TAG, "Error start session!!!", e);
        } else if (e instanceof TunaRequestNullException) {
            Log.e(LOG_TAG, "Error start session!!!", e);
        } else if (e instanceof TunaSessionExpiredException) {
            Log.e(LOG_TAG, "Error start session!!!", e);
        } else if (e instanceof TunaSessionInvalidException) {
            Log.e(LOG_TAG, "Error start session!!!", e);
        } else if (e instanceof TunaCardDataMissedException) {
            Log.e(LOG_TAG, "Error start session!!!", e);
        } else if (e instanceof TunaCardNumberAlreadyTokenizedException) {
            Log.e(LOG_TAG, "Error start session!!!", e);
        } else if (e instanceof TunaInvalidExpirationDateException) {
            Log.e(LOG_TAG, "Error start session!!!", e);
        } else if (e instanceof TunaInvalidCardNumberException) {
            Log.e(LOG_TAG, "Error start session!!!", e);
        } else if (e instanceof TunaTokenNotFoundException) {
            Log.e(LOG_TAG, "Error start session!!!", e);
        } else if (e instanceof TunaTokenCanNotBeRemovedException) {
            Log.e(LOG_TAG, "Error start session!!!", e);
        } else if (e instanceof TunaCardCanNotBeRemovedException) {
            Log.e(LOG_TAG, "Error start session!!!", e);
        } else if (e instanceof TunaPartnerGuidMissedException) {
            Log.e(LOG_TAG, "Error start session!!!", e);
        } else if (e instanceof TunaInvalidPartnerTokenException) {
            Log.e(LOG_TAG, "Error start session!!!", e);
        } else if (e instanceof TunaCustomerDataMissedException) {
            Log.e(LOG_TAG, "Error start session!!!", e);
        } else if (e instanceof TunaRequestTokenMissedException) {
            Log.e(LOG_TAG, "Error start session!!!", e);
        } else if (e instanceof TunaInvalidCardTokenException) {
            Log.e(LOG_TAG, "Error start session!!!", e);
        } else if (e instanceof TunaInvalidCardHolderNameException) {
            Log.e(LOG_TAG, "Error start session!!!", e);
        } else if (e instanceof TunaReachedMaxCardsByUserException) {
            Log.e(LOG_TAG, "Error start session!!!", e);
        } else if (e instanceof TunaReachedMaxSessionsByUserException) {
            Log.e(LOG_TAG, "Error start session!!!", e);
        } else if (e instanceof TunaConnectException) {
            Log.e(LOG_TAG, "Error start session!!!", e);
        } else if (e instanceof TunaTimeoutException) {
            Log.e(LOG_TAG, "Error start session!!!", e);
        } else if (e instanceof TunaException) {
            Log.e(LOG_TAG, "Error start session!!!", e);
        } else {
            Log.e(LOG_TAG, "Error start session!!!", e);
        }
    }

    private void bindCard(String cvv) {
        showLoading();
        tuna.bind(card, cvv, new Tuna.TunaRequestCallback<TunaCard>() {
            @Override
            public void onSuccess(TunaCard result) {
                hideLoading();

                Log.i(LOG_TAG, "Seconds until this bind expire" + result.secondsBindToExpire());
                Log.i(LOG_TAG, "Bind has expired" + result.bindHasExpired());

                Toast.makeText(JavaDetailCardActivity.this, "Bind card success!!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(@NotNull Throwable e) {
                hideLoading();
                handleError(e);
            }
        });
    }

    private void deleteCard() {
        showLoading();
        tuna.deleteCard(card, new Tuna.TunaRequestCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                hideLoading();
                Toast.makeText(JavaDetailCardActivity.this, "Card successfully deleted!!!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra(Extras.CARD_TOKEN, card.getToken());
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFailed(@NotNull Throwable e) {
                hideLoading();
                handleError(e);
            }
        });
    }

    private void showLoading() {
        loading.setVisibility(View.VISIBLE);
        btnBind.setVisibility(View.GONE);
        cardModel.setVisibility(View.GONE);
    }

    private void hideLoading() {
        btnBind.setVisibility(View.VISIBLE);
        cardModel.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
    }
}
