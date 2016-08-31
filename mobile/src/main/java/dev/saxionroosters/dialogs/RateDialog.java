package dev.saxionroosters.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Button;

import dev.saxionroosters.R;
import dev.saxionroosters.activities.BaseActivity;
import dev.saxionroosters.extras.AnalyticsTrackers;

/**
 * Created by Doppie on 7-3-2016.
 */
public class RateDialog extends DialogFragment {

    private final static int DAYS_UNTIL_PROMPT = 7;
    private final static int LAUNCHES_UNTIL_PROMPT = 7;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.rate) + " " + getString(R.string.app_name));
        builder.setMessage(getString(R.string.rate_my_app));
        builder.setPositiveButton(getString(R.string.ofcourse), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=dev.saxionroosters")));

                SharedPreferences prefs = getActivity().getSharedPreferences("ratedialog", 0);
                SharedPreferences.Editor editor = prefs.edit();
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                AnalyticsTrackers.sendEvent("Rate", "Yes");
                dialog.dismiss();
            }
        });
        builder.setNeutralButton(getString(R.string.contact), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Start mail app for support
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("*/mail");
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support) + " " + getString(R.string.app_name));
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.support_email)});

                startActivity(Intent.createChooser(i, getString(R.string.support_request_message)));
                AnalyticsTrackers.sendEvent("Rate", "Contact");
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getString(R.string.no_thanks), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences prefs = getActivity().getSharedPreferences("ratedialog", 0);
                SharedPreferences.Editor editor = prefs.edit();
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                AnalyticsTrackers.sendEvent("Rate", "No");
                dialog.dismiss();
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button negativebutton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                negativebutton.setTextColor(Color.BLACK);

                Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setTextColor(Color.BLACK);

                Button neutralButton = dialog.getButton(DialogInterface.BUTTON_NEUTRAL);
                neutralButton.setTextColor(Color.BLACK);
            }
        });



        return dialog;
    }

    public void app_launched(BaseActivity a) {
        SharedPreferences prefs = a.getSharedPreferences("ratedialog", 0);
        if (prefs.getBoolean("dontshowagain", false)) { return ; }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                this.show(a.getSupportFragmentManager(), "dialog");
            }
        }

        editor.commit();
    }
}