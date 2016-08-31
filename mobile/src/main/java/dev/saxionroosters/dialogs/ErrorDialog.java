package dev.saxionroosters.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import dev.saxionroosters.R;
import dev.saxionroosters.activities.FeedbackActivity;
import dev.saxionroosters.extras.S;

/**
 * Created by Doppie on 26-2-2016.
 */
public class ErrorDialog extends DialogFragment{

    private TextView messageText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_error, null, false);

        messageText = (TextView) view.findViewById(R.id.messageText);
        messageText.setText(this.getArguments().getString(S.MESSAGE));


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(this.getArguments().getString(S.TITLE));
        builder.setView(view);

        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        builder.setNeutralButton(getString(R.string.feedback), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(getActivity(), FeedbackActivity.class);
                startActivity(i);
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
}
