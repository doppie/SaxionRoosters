package dev.saxionroosters.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.Date;

import dev.saxionroosters.extras.HtmlRetriever;
import dev.saxionroosters.extras.S;
import dev.saxionroosters.extras.Storage;
import dev.saxionroosters.extras.Tools;
import dev.saxionroosters.model.College;
import dev.saxionroosters.model.Day;
import dev.saxionroosters.model.Owner;
import dev.saxionroosters.model.Week;

/**
 * Created by Wessel on 18-11-2016.
 */
public class WearService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, DataApi.DataListener {

    GoogleApiClient googleClient;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
        Tools.log("Wear host | WearService stopped.");
    }

    @Override
    public void onCreate() {
        Tools.log("Wear host | WearService started.");
        googleClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        googleClient.connect();
    }


    @Override
    public void onStart(Intent intent, int startid)
    {
        //do nothing
    }

    @Override
    public void onConnected(Bundle bundle) {

        Wearable.DataApi.addListener(googleClient, this).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                Tools.log("Wear host | Status: " + String.valueOf(status));
            }
        });
    }

    private class GetWeekColleges extends AsyncTask<Void, Void, Void> {

        Handler UIHandler = new Handler(Looper.getMainLooper());

        Storage storage = Storage.getInstance(getApplicationContext());

        ArrayList<String> titleArray = new ArrayList<String>();
        ArrayList<String> roomArray = new ArrayList<String>();
        ArrayList<String> timeArray = new ArrayList<String>();
        ArrayList<String> dateArray = new ArrayList<String>();
        private Week week;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            // get startup owner
            String startupOwnerName = (String) storage.getObject(S.SETTING_STARTUP_OWNER);
            String startupOwnerType = (String) storage.getObject(S.SETTING_STARTUP_OWNER_TYPE);

            if (startupOwnerName != null && !startupOwnerName.isEmpty() && startupOwnerType != null && !startupOwnerType.isEmpty()) {
                startupOwnerName = startupOwnerName.replaceAll("\"", "");

                startupOwnerType = (String) startupOwnerType.replaceAll("\"", "");
                Tools.log("DEBUG: startupOwnerType: " + startupOwnerType);

                Owner.OwnerType ownerType = null;
                if (startupOwnerType.equals("GROUP")) {
                    ownerType = Owner.OwnerType.GROUP;
                } else if (startupOwnerType.equals("TEACHER")) {
                    ownerType = Owner.OwnerType.TEACHER;
                } else if (startupOwnerType.equals("COURSE")) {
                    ownerType = Owner.OwnerType.COURSE;
                } else if (startupOwnerType.equals("ACADEMY")) {
                    ownerType = Owner.OwnerType.ACADEMY;
                } else {
                    Tools.log("ERROR: ownerType = " + ownerType);
                }
                Tools.log("DEBUG: " + ownerType);

                // create new week (one week only, the current one)
                week = new Week(new Owner(startupOwnerName, ownerType), "week", "0");

                startupOwnerName = Tools.getOwnerIdName(week.getOwner());
                Tools.log("DEBUG: ownerIdName = " + Tools.getOwnerIdName(week.getOwner()));
                Tools.log("DEBUG: ownerRepresentativeName = " + Tools.getOwnerRepresentativeName(week.getOwner()));

                HtmlRetriever retriever = new HtmlRetriever(week);
                String url = S.URL + S.SCHEDULE + "/" + ownerType.toString().toLowerCase() + ":" + startupOwnerName + "/" + S.WEEK_ID + ":" + week.getId();
                Object object = retriever.retrieveHtml(url, S.PARSE_WEEK);
                Week week = retriever.onWeekScheduleRetrieveCompleted(object);
                if(week != null) {
                    this.week = week;
                } else {
                    Tools.log("Wear host | Error whilst loading colleges :(");
                }

                for (int i = 0; i < week.getDays().size(); i++) {
                    Day day = week.getDays().get(i);

                    for (int j = 0; j < day.getColleges().size(); j++) {
                        College college = day.getColleges().get(j);
                        String title = college.getName();
                        Tools.log("Wear host | College name: " + title);
                        String room = college.getLocation();
                        String time = college.getTime();
                        String date = college.getDate();

                        titleArray.add(title);
                        roomArray.add(room);
                        timeArray.add(time);
                        dateArray.add(date);
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            UIHandler.post(new Runnable() {
                public void run() {

                    try {
                        if (titleArray.get(0) != null) {
                            Tools.log(String.valueOf(titleArray));
                            // Week succesfully loaded, send it to the watch
                            PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/data-" + new Date().getTime()).setUrgent();
                            DataMap map = putDataMapReq.getDataMap();
                            map.putLong("time", new Date().getTime());
                            map.putString("status", "success");
                            map.putStringArrayList("title", titleArray);
                            map.putStringArrayList("room", roomArray);
                            map.putStringArrayList("time", timeArray);
                            map.putStringArrayList("date", dateArray);

                            PutDataRequest putDataReq = putDataMapReq.asPutDataRequest().setUrgent();
                            Wearable.DataApi.putDataItem(googleClient, putDataReq);
                        } else {
                            // error whilst loading, send unknown error to watch
                            PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/data-" + new Date().getTime()).setUrgent();
                            DataMap map = putDataMapReq.getDataMap();
                            map.putLong("time", new Date().getTime());
                            map.putString("status", "unknownError");

                            PutDataRequest putDataReq = putDataMapReq.asPutDataRequest().setUrgent();
                            Wearable.DataApi.putDataItem(googleClient, putDataReq);
                        }
                    }catch (Exception ex) {
                        // No colleges found this week: throw a 'no colleges this week'-error
                        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/data-" + new Date().getTime()).setUrgent();
                        DataMap map = putDataMapReq.getDataMap();
                        map.putLong("time", new Date().getTime());
                        map.putString("status", "noCollegesThisWeek");

                        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest().setUrgent();
                        Wearable.DataApi.putDataItem(googleClient, putDataReq);
                    }
                }
            });
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {

        for (DataEvent event : dataEventBuffer){
            if(event.getType() == DataEvent.TYPE_CHANGED){
                //Tools.log("Wear client | DataItem changed: " + event.getDataItem().getUri());

                DataItem item = event.getDataItem();

                if(item.getUri().getPath().compareTo("/get") == 0 ){
                    Tools.log("Wear host | Refresh request received from client");
                    if(isNetworkAvailable(getApplicationContext())) {
                        new GetWeekColleges().execute();
                    }else{
                        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/data-" + new Date().getTime()).setUrgent();
                        putDataMapReq.getDataMap().putString("status", "failedNoInternet");
                        putDataMapReq.getDataMap().putLong("time", new Date().getTime());

                        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest().setUrgent();
                        Wearable.DataApi.putDataItem(googleClient, putDataReq);
                        Tools.log("Wear host | No network available");
                    }
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                Tools.log("Wear client | DataItem deleted: " + event.getDataItem().getUri());
            }
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(conMan.getActiveNetworkInfo() != null && conMan.getActiveNetworkInfo().isConnected())
            return true;
        else
            return false;
    }
}
