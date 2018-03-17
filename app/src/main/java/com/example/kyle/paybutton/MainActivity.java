package com.example.kyle.paybutton;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigDecimal;

import io.mpos.Mpos;
import io.mpos.accessories.AccessoryFamily;
import io.mpos.accessories.parameters.AccessoryParameters;
import io.mpos.provider.ProviderMode;
import io.mpos.transactions.Transaction;
import io.mpos.transactions.parameters.TransactionParameters;
import io.mpos.ui.shared.MposUi;

public class MainActivity extends AppCompatActivity {

    private View.OnClickListener onClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button payButton = findViewById(R.id.pay);
        payButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                paymentButtonClicked();

            }
        });

        Button refundButton = findViewById(R.id.refund);
        refundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refundButtonClicked();
            }
        });

    }

    public void paymentButtonClicked() {
        MposUi ui = MposUi.initialize(this, ProviderMode.MOCK,
                "63865dbb-a703-4771-89ac-18555c92fa43", "HAgifi4fcG6INCQd0Hi0BfzQdFDi9cIO");

        AccessoryParameters accessoryParameters = new AccessoryParameters.Builder(AccessoryFamily.MOCK)
                .mocked()
                .build();
        ui.getConfiguration().setTerminalParameters(accessoryParameters);

        TransactionParameters transactionParameters = new TransactionParameters.Builder()
                .charge(
                        new BigDecimal(5.00),
                    io.mpos.transactions.Currency.EUR
                )
                .subject("34th Street Deli")
                .customIdentifier("1991")
                .build();

        Intent intent = ui.createTransactionIntent(transactionParameters);
        startActivityForResult(intent, MposUi.REQUEST_CODE_PAYMENT);
    }

    public void refundButtonClicked() {
        TransactionParameters parameters = new TransactionParameters.Builder()
                .refund("1991")
                .amountAndCurrency(
                        new BigDecimal(5.00), io.mpos.transactions.Currency.EUR)
                .build();

        Intent intent = MposUi.getInitializedInstance().createTransactionIntent(parameters);
        startActivity(intent);

    }

    public void setOnClickListener (View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MposUi.REQUEST_CODE_PAYMENT) {
            if (resultCode == MposUi.RESULT_CODE_APPROVED) {

                Toast.makeText(this, "Transaction approved", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(this, "Transaction was declined, aborted, or failed",
                        Toast.LENGTH_LONG).show();
            }

            Transaction transaction = MposUi.getInitializedInstance().getTransaction();


        }
    }
}









