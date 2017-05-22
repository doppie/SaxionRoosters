package dev.saxionroosters.collegedetails.map;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Jelle on 22/05/2017.
 */

public class SaxionLocation {

    private String name;
    private String abbrevation;
    private LatLng coordinates;

    public SaxionLocation(String name, String abbr, LatLng coordinates) {
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
