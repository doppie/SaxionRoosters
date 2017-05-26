package dev.saxionroosters.general;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import dev.saxionroosters.R;
import dev.saxionroosters.main.MainActivity;

/**
 * Created by Jelle on 19-8-2016.
 */
public class ThemeUtils {

    public static void activateTheme(Activity activity) {
        Intent i = new Intent(activity, MainActivity.class);
        //finish Affinity is only available > version 16

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) activity.finishAffinity();
        else Toast.makeText(activity, activity.getString(R.string.message_theme_visible_after_reset), Toast.LENGTH_LONG).show();

        activity.startActivity(i);
    }

    public static void onCreateSetTheme(Activity activity, String name) {
        Utils.log("[THEME_COLOR] " + name);
        if(name != null) name = name.replaceAll("\"", "");

        if (name == null || name.isEmpty() || name.equalsIgnoreCase("Saxion Theme")) {
            activity.setTheme(R.style.AppTheme_Saxion_NoActionBar);
        } else if (name.equalsIgnoreCase("Purple")) {
            activity.setTheme(R.style.AppTheme_Purple_NoActionBar);
        } else if (name.equalsIgnoreCase("Deep Purple")) {
            activity.setTheme(R.style.AppTheme_DeepPurple_NoActionBar);
        } else if (name.equalsIgnoreCase("Red")) {
            activity.setTheme(R.style.AppTheme_Red_NoActionBar);
        } else if (name.equalsIgnoreCase("Pink")) {
            activity.setTheme(R.style.AppTheme_Pink_NoActionBar);
        } else if (name.equalsIgnoreCase("Indigo")) {
            activity.setTheme(R.style.AppTheme_Indigo_NoActionBar);
        } else if (name.equalsIgnoreCase("Blue")) {
            activity.setTheme(R.style.AppTheme_Blue_NoActionBar);
        } else if (name.equalsIgnoreCase("LightBlue")) {
            activity.setTheme(R.style.AppTheme_LightBlue_NoActionBar);
        } else if (name.equalsIgnoreCase("Cyan")) {
            activity.setTheme(R.style.AppTheme_Cyan_NoActionBar);
        } else if (name.equalsIgnoreCase("Teal")) {
            activity.setTheme(R.style.AppTheme_Teal_NoActionBar);
        } else if (name.equalsIgnoreCase("Green")) {
            activity.setTheme(R.style.AppTheme_Green_NoActionBar);
        } else if (name.equalsIgnoreCase("Light Green")) {
            activity.setTheme(R.style.AppTheme_LightGreen_NoActionBar);
        } else if (name.equalsIgnoreCase("Lime")) {
            activity.setTheme(R.style.AppTheme_Lime_NoActionBar);
        } else if (name.equalsIgnoreCase("Light Yellow")) {
            activity.setTheme(R.style.AppTheme_LightYellow_NoActionBar);
        } else if (name.equalsIgnoreCase("Yellow")) {
            activity.setTheme(R.style.AppTheme_Yellow_NoActionBar);
        } else if (name.equalsIgnoreCase("Orange")) {
            activity.setTheme(R.style.AppTheme_Orange_NoActionBar);
        } else if (name.equalsIgnoreCase("Deep Orange")) {
            activity.setTheme(R.style.AppTheme_DeepOrange_NoActionBar);
        } else if (name.equalsIgnoreCase("Grey")) {
            activity.setTheme(R.style.AppTheme_Grey_NoActionBar);
        } else if (name.equalsIgnoreCase("Blue Grey")) {
            activity.setTheme(R.style.AppTheme_BlueGrey_NoActionBar);
        } else if (name.equalsIgnoreCase("Brown")) {
            activity.setTheme(R.style.AppTheme_Brown_NoActionBar);
        } else {
            Utils.log("[THEME_COLOR] Unknown theme: " + name);
        }
    }
}
