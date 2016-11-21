package dev.saxionroosters;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import dev.saxionroosters.adapters.CollegeAdapter;
import dev.saxionroosters.extras.Tools;
import dev.saxionroosters.models.College;

public class MainActivity extends Activity implements WearableListView.ClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, DataApi.DataListener {

    private WearableListView listView;
    private GoogleApiClient googleClient;
    private WatchViewStub stub;
    private ProgressBar progressBar;
    private ImageView errorImageView;
    private TextView errorTextView;
    private Button errorRetryButton;
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
                errorImageView = (ImageView) stub.findViewById(R.id.errorImage);
                errorTextView = (TextView) stub.findViewById(R.id.errorText);
                errorRetryButton = (Button) stub.findViewById(R.id.retryButton);
                loadAdapter();
                sendDataRequest();
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
            // Hide error message
            errorImageView.setVisibility(View.GONE);
            errorTextView.setVisibility(View.GONE);
            errorRetryButton.setVisibility(View.GONE);

            // Make progressbar visible and change color to white
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);
            progressBar.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, android.graphics.PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void setErrorView(String errorText) {
        // Hide progressbar
        setProgressBarVisible(false);

        // Show imageview
        errorImageView.setVisibility(View.VISIBLE);
        errorImageView.setImageResource(R.mipmap.ic_error);

        // Show error text
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(errorText);

        // Show retry button
        errorRetryButton.setVisibility(View.VISIBLE);
        errorRetryButton.setText(getResources().getString(R.string.btn_retry));
        errorRetryButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataRequest();
            }
        });
    }

    private String formatDate(String inputDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd MMMM yyyy");
        Date date = null;
        try {
            date = sdf.parse(inputDate);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM");

        return formatter.format(date);
    }

    private void sendDataRequest() {
        setProgressBarVisible(true);

        // Send a request to the host (phone/tablet) to get new college-data
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/get").setUrgent();
        putDataMapReq.getDataMap().putString("refresh", "true");
        putDataMapReq.getDataMap().putLong("time", new Date().getTime());

        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest().setUrgent();
        Wearable.DataApi.putDataItem(googleClient, putDataReq);
        Tools.log("Wear client | Refresh request sent to host device");
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

                            final int[] color = {0};
                            final String[] lastDate = {null};
                            for (int i = 0; i < titleArray.size(); i++) {
                                final int finalI = i;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        int dateColor;
                                        int[] dateColors = { ContextCompat.getColor(getApplicationContext(), R.color.material_red_400), ContextCompat.getColor(getApplicationContext(), R.color.material_yellow_400), ContextCompat.getColor(getApplicationContext(), R.color.material_green_400), ContextCompat.getColor(getApplicationContext(), R.color.material_blue_400), ContextCompat.getColor(getApplicationContext(), R.color.material_purple_400)};

                                        String date = dateArray.get(finalI);
                                        if (dateArray.get(finalI).equals(lastDate[0]) || finalI == 0){
                                            dateColor = dateColors[color[0]];
                                        } else {
                                            // Date has changed, new color
                                            color[0]++;
                                            dateColor = dateColors[color[0]];
                                        }
                                        lastDate[0] = date;

                                        // If there are multiple rooms, replace the linebreaks
                                        String rooms = roomArray.get(finalI).replaceAll("\n", " - ");

                                        // Add the colleges to the adapter
                                        mAdapter.addItem(new College(titleArray.get(finalI), rooms, timeArray.get(finalI), formatDate(dateArray.get(finalI)), dateColor));
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
                                    setErrorView(getResources().getString(R.string.error_no_internet));
                                }
                            });
                            break;
                        // Unknown error... Give also a warning.
                        default:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setErrorView(getResources().getString(R.string.error_unknown));
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