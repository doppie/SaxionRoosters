package dev.saxionroosters.fragments;

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
import com.google.maps.android.kml.KmlContainer;
import com.google.maps.android.kml.KmlGeometry;
import com.google.maps.android.kml.KmlLayer;
import com.google.maps.android.kml.KmlPlacemark;
import com.google.maps.android.kml.KmlPoint;
import com.google.maps.android.kml.KmlPolygon;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.ViewById;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import dev.saxionroosters.R;
import dev.saxionroosters.extras.S;
import dev.saxionroosters.extras.Tools;

/**
 * Created by Jelle on 16-8-2016.
 */
@EFragment(R.layout.fragment_maps)
public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap map;
    protected SupportMapFragment mapFragment;
    private KmlLayer mapLayer;

    private LatLng locationCoordinates;
    private String locationTitle;
    private Marker locationMarker;

    @AfterViews
    protected void init() {

        if(getArguments() != null) {
            locationCoordinates = getArguments().getParcelable(S.COORDINATES);
            locationTitle = getArguments().getString(S.DEPARTMENT);
        }

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        //important, we don't want the user to be able to drag the fragment.
        //The MapsFragment is intended as a preview of the full map.
        map.getUiSettings().setScrollGesturesEnabled(false);

        initializeLayer();

        LatLng focusPosition = getFocusPositionLatLng();

        if(focusPosition == null) Tools.log("Mehh, position not found :(");

        //position the camera
        if(locationCoordinates != null) map.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(locationCoordinates, 17)));
        if(locationCoordinates != null && locationTitle != null) {
            locationMarker = map.addMarker(new MarkerOptions().position(locationCoordinates).title(locationTitle));
            locationMarker.showInfoWindow();
        }
    }

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

    private LatLng getFocusPositionLatLng() {
        for(KmlContainer container : mapLayer.getContainers()) {
            for(KmlPlacemark placemark : container.getPlacemarks()) {

                KmlGeometry geometry = placemark.getGeometry();
                if(geometry instanceof KmlPoint) {
//                    if(placemark.getProperty("name").equals(locationName)) {
//                        return (LatLng) geometry.getGeometryObject();
//                    }
                }
            }
        }

        return null;
    }
}
