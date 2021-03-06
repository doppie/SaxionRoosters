package dev.saxionroosters.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import dev.saxionroosters.R;

/**
 * Created by Doppie on 5-3-2016.
 */
public class AboutDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_about, null, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        String title = getString(R.string.title_option_about);

        try {
            //Add the version name of the app.
            String versionName = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
            title += " (v" + versionName + ")";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        builder.setTitle(title);
        builder.setView(view);

        return builder.create();
    }
}
