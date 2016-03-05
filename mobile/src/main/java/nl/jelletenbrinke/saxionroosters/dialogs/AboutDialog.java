package nl.jelletenbrinke.saxionroosters.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import nl.jelletenbrinke.saxionroosters.R;
import nl.jelletenbrinke.saxionroosters.extras.S;

/**
 * Created by Doppie on 5-3-2016.
 */
public class AboutDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_about, null, false);

        FloatingActionButton gitButton = (FloatingActionButton) view.findViewById(R.id.gitButton);
        FloatingActionButton mailButton = (FloatingActionButton) view.findViewById(R.id.mailButton);

        gitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/doppie/SaxionRoosters"));
                startActivity(intent);
            }
        });
        mailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "saxionroosters.dev@gmail.com", null));
                startActivity(Intent.createChooser(intent, "Neem contact op met het ontwikkelteam achter Saxion Roosters."));
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.dialog_info_title));
        builder.setView(view);

        return builder.create();
    }
}
