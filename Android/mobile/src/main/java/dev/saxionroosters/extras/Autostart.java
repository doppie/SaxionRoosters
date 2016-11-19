package dev.saxionroosters.extras;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import dev.saxionroosters.services.WearService;

/**
 * Created by Wessel on 18-11-2016.
 */

public class Autostart extends BroadcastReceiver
{
    public void onReceive(Context c, Intent i)
    {
        Intent intent = new Intent(c, WearService.class);
        c.startService(intent);
    }
}