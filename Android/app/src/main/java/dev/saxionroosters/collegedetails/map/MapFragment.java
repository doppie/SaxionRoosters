package dev.saxionroosters.collegedetails.map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.kml.KmlLayer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import dev.saxionroosters.R;
import dev.saxionroosters.general.S;

/**
 * Created by Jelle on 23/02/2017.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap map;
    protected SupportMapFragment mapFragment;
    private KmlLayer mapLayer;

    private LatLng locationCoordinates;
    private String locationTitle;
    private Marker locationMarker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        if (getArguments() != null) {
            locationCoordinates = getArguments().getParcelable(S.COORDINATES);
            locationTitle = getArguments().getString(S.DEPARTMENT);
        }

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        //important, we don't want the user to be able to drag the fragment.
        //The MapsFragment is intended as a preview of the full map.
        map.getUiSettings().setScrollGesturesEnabled(false);

        initializeLayer();

        //position the camera
        if (locationCoordinates != null)
            map.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(locationCoordinates, 17)));
        if (locationCoordinates != null && locationTitle != null) {
            locationMarker = map.addMarker(new MarkerOptions().position(locationCoordinates).title(locationTitle));
            locationMarker.showInfoWindow();
        }
    }

    /**
     * Initializes the Google Maps overlay to draw the highlighted/colored Saxion buildings.
     */
    private void initializeLayer() {
        try {
            mapLayer = new KmlLayer(map, R.raw.saxion_maps, getActivity());
            mapLayer.addLayerToMap();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }
}
