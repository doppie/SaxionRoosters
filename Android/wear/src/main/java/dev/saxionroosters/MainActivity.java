package dev.saxionroosters;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dev.saxionroosters.adapters.CollegeAdapter;
import dev.saxionroosters.extras.Tools;
import dev.saxionroosters.models.College;

public class MainActivity extends Activity implements WearableListView.ClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, DataApi.DataListener {

    private WearableListView listView;
    private GoogleApiClient googleClient;
    private WatchViewStub stub;
    private ProgressBar progressBar;
    private ArrayList<String> titleArray;
    private ArrayList<String> roomArray;
    private ArrayList<String> timeArray;
    private ArrayList<String> dateArray;

    private List<College> items = new ArrayList<>();
    private CollegeAdapter mAdapter = new CollegeAdapter(this, items);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleArray = new ArrayList<String>();
        roomArray = new ArrayList<String>();
        timeArray = new ArrayList<String>();
        dateArray = new ArrayList<String>();

        // Connect to GMS
        googleClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        stub = (WatchViewStub) findViewById(R.id.watch_view_stub);

        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                listView = (WearableListView) stub.findViewById(R.id.list_view);
                progressBar = (ProgressBar) stub.findViewById(R.id.progressBar);
                loadAdapter();
                setProgressBarVisible(true);
            }
        });


    }

    private void loadAdapter() {
        listView.setAdapter(mAdapter);
        listView.setClickListener(this);
    }


    private void setProgressBarVisible(Boolean visibility) {
        if (!visibility) {
            // Hide progressbar
            progressBar.setVisibility(View.GONE);
        } else {
            // Make progressbar visible and change color to white
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);
            progressBar.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, android.graphics.PorterDuff.Mode.SRC_ATOP);
        }
    }


    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {
        switch (viewHolder.getPosition()) {
            case 0:
                //Do something
                break;
        }
    }

    @Override
    public void onTopEmptyRegionClick() {
        // Prevent NullPointerException
    }

    @Override
    public void onConnected(Bundle bundle) {
        Tools.log("Wear client | Connected to Google Api Service");
        Wearable.DataApi.addListener(googleClient, this);

        // Send a request to the host (phone/tablet) to get new college-data
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/get").setUrgent();
        putDataMapReq.getDataMap().putString("refresh", "true");
        putDataMapReq.getDataMap().putLong("time", new Date().getTime());

        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest().setUrgent();
        Wearable.DataApi.putDataItem(googleClient, putDataReq);
        Tools.log("Wear client | Refresh request naar host device gestuurd");
    }

    @Override
    public void onConnectionSuspended(int i) {
        // do nothing
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // do nothing
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_DELETED) {
                Tools.log("Wear client | DataItem deleted: " + event.getDataItem().getUri());
            } else if (event.getType() == DataEvent.TYPE_CHANGED) {
                // We have received a changed Dataitem!

                final DataMap dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();

                String status = dataMap.getString("status");
                Tools.log("Wear client | Datamap status: " + status);

                if (status != null){
                    // Datamap has a status
                    switch (status) {
                        // Success! Fill the adapter with colleges.
                        case "success":

                            titleArray = dataMap.getStringArrayList("title");
                            roomArray = dataMap.getStringArrayList("room");
                            timeArray = dataMap.getStringArrayList("time");
                            dateArray = dataMap.getStringArrayList("date");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Remove all items (just in case)
                                    mAdapter.removeItems();
                                }
                            });

                            final String[] lastDate = {null};
                            for (int i = 0; i < titleArray.size(); i++) {
                                final int finalI = i;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        boolean dateheader;
                                        String date = dateArray.get(finalI);
                                        if (dateArray.get(finalI).equals(lastDate[0])){
                                            dateheader = false;
                                        } else {
                                            // Date has changed. Add a college with a date-header
                                            dateheader = true;
                                        }
                                        lastDate[0] = date;

                                        // Add the colleges to the adapter
                                        mAdapter.addItem(new College(titleArray.get(finalI), roomArray.get(finalI), timeArray.get(finalI), dateArray.get(finalI), dateheader));
                                        mAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                            setProgressBarVisible(false);
                            mAdapter.notifyDataSetChanged();
                            break;
                        // No internet... Give the user a warning.
                        case "failedNoInternet":
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (mAdapter != null) {
                                        mAdapter.removeItems();
                                        mAdapter.addItem(new College("Fout bij het laden", "", "", "", false));
                                        mAdapter.notifyDataSetChanged();
                                    }
                                    setProgressBarVisible(false);
                                }
                            });
                            break;
                        // Unexpected error... Give also a warning.
                        default:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (mAdapter != null) {
                                        mAdapter.removeItems();
                                        mAdapter.addItem(new College("Fout bij het laden", "", "", "", false));
                                        mAdapter.notifyDataSetChanged();
                                    }
                                    setProgressBarVisible(false);
                                }
                            });
                            break;
                    }
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleClient.connect();
    }

    @Override
    protected void onStop() {
        if (null != googleClient && googleClient.isConnected()) {
            Wearable.DataApi.removeListener(googleClient, this);
            googleClient.disconnect();
        }
        super.onStop();
    }
}