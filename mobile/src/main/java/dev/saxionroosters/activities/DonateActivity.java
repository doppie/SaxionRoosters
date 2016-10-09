package dev.saxionroosters.activities;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import dev.saxionroosters.R;
import dev.saxionroosters.extras.Tools;

/**
 * Created by jelle on 09/10/16.
 */
@EActivity(R.layout.activity_donate)
public class DonateActivity extends BaseActivity {

    @ViewById(R.id.denyButton)
    protected Button denyButton;

    @ViewById(R.id.donateButton)
    protected Button donateButton;

    @ViewById(R.id.closeButton)
    protected ImageView closeButton;

    @ViewById(R.id.text)
    protected TextView text;

    @AfterViews
    protected void init() {
        initUI();
        updateUI();
    }

    private void initUI() {
        denyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                billingProcessor.purchase(DonateActivity.this, "premium");
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    @UiThread
    public void updateUI() {
        if(Boolean.valueOf(storage.getObject("premium")) == true) {
            text.setText(getString(R.string.donate_thankyou));
            donateButton.setVisibility(View.GONE);
            denyButton.setText(getString(R.string.close));
        } else {
            denyButton.setText(getRandomText());
        }
    }

    private String getRandomText() {
        ArrayList<String> strings = new ArrayList<>();
        strings.add(getString(R.string.donate_cancel_1));
        strings.add(getString(R.string.donate_cancel_2));
        strings.add(getString(R.string.donate_cancel_3));
        strings.add(getString(R.string.donate_cancel_4));
        strings.add(getString(R.string.donate_cancel_5));
        strings.add(getString(R.string.donate_cancel_6));

        Random rand = new Random();
        return strings.get(rand.nextInt((5 - 0) + 1) + 0);
    }





}
