package dev.saxionroosters.collegedetails.map;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

import dev.saxionroosters.general.S;

/**
 * Created by Jelle on 22/05/2017.
 */

public class MapTools {

    public static ArrayList<SaxionLocation> getLocations() {
        ArrayList<SaxionLocation> locations = new ArrayList<>();
        locations.addAll(getEnschedeLocations());
        locations.addAll(getDeventerLocations());

        return locations;
    }

    public static ArrayList<SaxionLocation> getEnschedeLocations() {
        ArrayList<SaxionLocation> locations = new ArrayList<>();

        locations.add(new SaxionLocation("Schierbeek", "S", new LatLng(52.22086, 6.88692)));
        locations.add(new SaxionLocation("Epy Drost", "G", new LatLng(52.21971, 6.8895)));
        locations.add(new SaxionLocation("ROC De Maere", "M", new LatLng(52.21949, 6.88890)));
        locations.add(new SaxionLocation("Wolvecamp", "W", new LatLng(52.22126, 6.88596)));
        locations.add(new SaxionLocation("Haanstra", "H", new LatLng(52.221022, 6.884651)));
        locations.add(new SaxionLocation("Forum", "F", new LatLng(52.22038, 6.88621)));
        locations.add(new SaxionLocation("Elderink", "E", new LatLng(52.22029, 6.88502)));
        locations.add(new SaxionLocation("Randstad", "R", new LatLng(52.22122, 6.88947)));
        locations.add(new SaxionLocation("ITC", "I", new LatLng(52.22368, 6.88574)));
        locations.add(new SaxionLocation("Stork", "O", new LatLng(52.21995, 6.88826)));
        locations.add(new SaxionLocation("Hazemeyer", "Q", new LatLng(52.22035, 6.88908)));
        locations.add(new SaxionLocation("Hofstede Crull", "P", new LatLng(52.21996, 6.88827)));
        locations.add(new SaxionLocation("Aintsworth", "N", new LatLng(52.22014, 6.88813)));
        locations.add(new SaxionLocation("Villa Serphos", "V", new LatLng(52.22024, 6.88857)));

        return locations;
    }

    public static boolean isEnschedeLocation(String name) {
        for (SaxionLocation l : getEnschedeLocations()) {
            if (l.getAbbr().equals(name.substring(0, 1))) return true;
        }
        return false;
    }

    public static boolean isDeventerLocation(String name) {
        for (SaxionLocation l : getDeventerLocations()) {
            if (l.getAbbr().equals(name.substring(0, 1))) return true;
        }
        return false;
    }


    public static ArrayList<SaxionLocation> getDeventerLocations() {
        ArrayList<SaxionLocation> locations = new ArrayList<>();

        locations.add(new SaxionLocation("D", "D", new LatLng(52.25452, 6.16827)));
        locations.add(new SaxionLocation("C", "C", new LatLng(52.25415, 6.1684)));
        locations.add(new SaxionLocation("B", "B", new LatLng(52.2544, 6.16832)));
        locations.add(new SaxionLocation("A", "A", new LatLng(52.25445, 6.16695)));

        return locations;
    }


    public static HashMap<String, Object> getLocationDetails(Context context, String locationName) {
        HashMap<String, Object> locationDetails = new HashMap<>();

        if (locationName == null || locationName.isEmpty()) {
            return locationDetails;
        }

        //get the department & its coordinates
        for (SaxionLocation location : getLocations()) {

            if (location.getAbbr().equals(locationName.substring(0, 1))) {
                locationDetails.put(S.DEPARTMENT, location.getName());
                locationDetails.put(S.COORDINATES, location.getCoordinates());
            }
        }

        //get the floor
        if (locationName.indexOf(".") < locationName.length())
            locationDetails.put(S.FLOOR, locationName.substring(locationName.indexOf(".") - 1, locationName.indexOf(".")));

        //get the room.
        if (locationName.indexOf(".") + 1 < locationName.length())
            locationDetails.put(S.ROOM, locationName.substring(locationName.indexOf(".") + 1));

        return locationDetails;
    }
}
