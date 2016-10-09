package dev.saxionroosters.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Jelle on 20-8-2016.
 */
public class Location {

    private String name;
    private String abbrevation;
    private LatLng coordinates;

    public Location(String name, String abbr, LatLng coordinates) {
        this.name = name;
        this.abbrevation = abbr;
        this.coordinates = coordinates;
    }

    public String getName() {
        return name;
    }

    public String getAbbr() {
        return abbrevation;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }
}
