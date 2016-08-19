package dev.saxionroosters.extras;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import dev.saxionroosters.R;
import dev.saxionroosters.activities.MainActivity;
import dev.saxionroosters.activities.MainActivity_;

/**
 * Created by Jelle on 19-8-2016.
 */
public class ThemeTools {

    public static void activateTheme(Activity activity) {
        Intent i = new Intent(activity, MainActivity_.class);
        //finish Affinity is only available > version 16

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) activity.finishAffinity();
        else Toast.makeText(activity, activity.getString(R.string.theme_visible_after_reset), Toast.LENGTH_LONG).show();

        activity.startActivity(i);
    }

    public static void onCreateSetTheme(Activity activity, String name) {
        Tools.log("[THEME] " + name);
        name = name.replaceAll("\"", "");

        if (name == null || name.isEmpty() || name.equals("Saxion")) {
            activity.setTheme(R.style.AppTheme_Saxion_NoActionBar);
        } else if (name.equals("Purple")) {
            activity.setTheme(R.style.AppTheme_Purple_NoActionBar);
        } else if (name.equals("Deep Purple")) {
            activity.setTheme(R.style.AppTheme_DeepPurple_NoActionBar);
        } else if (name.equals("Red")) {
            activity.setTheme(R.style.AppTheme_Red_NoActionBar);
        } else if (name.equals("Pink")) {
            activity.setTheme(R.style.AppTheme_Pink_NoActionBar);
        } else if (name.equals("Indigo")) {
            activity.setTheme(R.style.AppTheme_Indigo_NoActionBar);
        } else if (name.equals("Blue")) {
            activity.setTheme(R.style.AppTheme_Blue_NoActionBar);
        } else if (name.equals("LightBlue")) {
            activity.setTheme(R.style.AppTheme_LightBlue_NoActionBar);
        } else if (name.equals("Cyan")) {
            activity.setTheme(R.style.AppTheme_Cyan_NoActionBar);
        } else if (name.equals("Teal")) {
            activity.setTheme(R.style.AppTheme_Teal_NoActionBar);
        } else if (name.equals("Green")) {
            activity.setTheme(R.style.AppTheme_Green_NoActionBar);
        } else if (name.equals("Light Green")) {
            activity.setTheme(R.style.AppTheme_LightGreen_NoActionBar);
        } else if (name.equals("Lime")) {
            activity.setTheme(R.style.AppTheme_Lime_NoActionBar);
        } else if (name.equals("Light Yellow")) {
            activity.setTheme(R.style.AppTheme_LightYellow_NoActionBar);
        } else if (name.equals("Yellow")) {
            activity.setTheme(R.style.AppTheme_Yellow_NoActionBar);
        } else if (name.equals("Orange")) {
            activity.setTheme(R.style.AppTheme_Orange_NoActionBar);
        } else if (name.equals("Deep Orange")) {
            activity.setTheme(R.style.AppTheme_DeepOrange_NoActionBar);
        } else if (name.equals("Grey")) {
            activity.setTheme(R.style.AppTheme_Grey_NoActionBar);
        } else if (name.equals("Blue Grey")) {
            activity.setTheme(R.style.AppTheme_BlueGrey_NoActionBar);
        } else if (name.equals("Brown")) {
            activity.setTheme(R.style.AppTheme_Brown_NoActionBar);
        } else {
            Tools.log("[THEME] Unknown theme: " + name);
        }
    }
}
