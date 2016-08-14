package dev.saxionroosters.activities;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.maps.android.geometry.Point;
import com.google.maps.android.kml.KmlContainer;
import com.google.maps.android.kml.KmlGeometry;
import com.google.maps.android.kml.KmlGroundOverlay;
import com.google.maps.android.kml.KmlLayer;
import com.google.maps.android.kml.KmlPlacemark;
import com.google.maps.android.kml.KmlPoint;
import com.google.maps.android.kml.KmlPolygon;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import dev.saxionroosters.R;
import dev.saxionroosters.extras.Tools;

//TODO: WIP
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private String locationName = "Haanstra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            KmlLayer layer = new KmlLayer(mMap, R.raw.saxion_maps_test, getApplicationContext());
            layer.addLayerToMap();

            for(KmlContainer container : layer.getContainers()) {
                Tools.log(container.toString());
                for(KmlPlacemark placemark : container.getPlacemarks()) {

                    if(placemark.getGeometry() instanceof KmlGeometry) {
                        KmlGeometry geometry = placemark.getGeometry();

                        if(geometry instanceof KmlPoint) {
                            Tools.log("POINT");

                            if(placemark.getProperty("name").equals(locationName)) {
                                Tools.log("FOUND LOCATION");
                                KmlPoint point = (KmlPoint) geometry;
//
                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(point.getGeometryObject(), 18)));
                            }
                        } else if(geometry instanceof KmlPolygon) {
                            Tools.log("POLY");
                        }
                    }
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void calculatePointInsideOfPolygon() {
        //TODO: Example
//        Polygon polygon = Polygon.Builder()
//                .addVertex(new Point(1, 3))
//                .addVertex(new Point(2, 8))
//                .addVertex(new Point(5, 4))
//                .addVertex(new Point(5, 9))
//                .addVertex(new Point(7, 5))
//                .addVertex(new Point(6, 1))
//                .addVertex(new Point(3, 1))
//                .build();
//
//        And check whereas the point is inside the polygon:
//
//        Point point = new Point(4.5f, 7);
//        boolean contains = polygon.contains(point);
    }
}
