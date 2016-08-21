package dev.saxionroosters.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import dev.saxionroosters.R;
import dev.saxionroosters.activities.BaseActivity;
import dev.saxionroosters.activities.FeedbackActivity;
import dev.saxionroosters.activities.MainActivity;
import dev.saxionroosters.extras.AnalyticsTrackers;

/**
 * Created by Doppie on 7-3-2016.
 */
public class FeedbackDialog extends DialogFragment {


    private final static int DAYS_UNTIL_PROMPT = 3;
    private final static int LAUNCHES_UNTIL_PROMPT = 5;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.feedback) + " " + getString(R.string.app_name));
        builder.setMessage(getString(R.string.feedback_message));
        builder.setIcon(getResources().getDrawable(R.drawable.app_icon));
        builder.setPositiveButton(getString(R.string.like), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences prefs = getActivity().getSharedPreferences("feedback", 0);
                SharedPreferences.Editor editor = prefs.edit();
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                AnalyticsTrackers.sendEvent("Feedback", "Like");
                dialog.dismiss();
            }
        });
        builder.setNeutralButton(getString(R.string.contact), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                SharedPreferences prefs = getActivity().getSharedPreferences("feedback", 0);
                SharedPreferences.Editor editor = prefs.edit();
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }

                Intent i = new Intent(getActivity(), FeedbackActivity.class);
                startActivity(i);

                AnalyticsTrackers.sendEvent("Feedback", "Contact");
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getString(R.string.dislike), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences prefs = getActivity().getSharedPreferences("feedback", 0);
                SharedPreferences.Editor editor = prefs.edit();
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }

                Intent i = new Intent(getActivity(), FeedbackActivity.class);
                startActivity(i);

                AnalyticsTrackers.sendEvent("Feedback", "Dislike");
                dialog.dismiss();
            }
        });

        return builder.create();
    }

    public void app_launched(BaseActivity a) {
        SharedPreferences prefs = a.getSharedPreferences("feedback", 0);
        if (prefs.getBoolean("dontshowagain", false)) {
            return;
        }

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