package dev.saxionroosters.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Jelle on 20-8-2016.
 */
public class Location {

    private String name;
    private LatLng coordinates;

    public Location(String name, LatLng coordinates) {
        this.name = name;
        this.coordinates = coordinates;
    }

    public String getName() {
        return name;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }
}
