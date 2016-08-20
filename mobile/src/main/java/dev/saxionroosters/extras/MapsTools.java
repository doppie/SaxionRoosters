package dev.saxionroosters.extras;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

import dev.saxionroosters.R;
import dev.saxionroosters.model.Location;

/**
 * Created by Jelle on 20-8-2016.
 */
public class MapsTools {

    public static ArrayList<Location> getLocations() {
        ArrayList<Location> locations = new ArrayList<>();
        locations.addAll(getEnschedeLocations());
        locations.addAll(getDeventerLocations());

        return locations;
    }

    public static ArrayList<Location> getEnschedeLocations() {
        ArrayList<Location> locations = new ArrayList<>();

        locations.add(new Location("Schierbeek", new LatLng(52.22086, 6.88692)));
        locations.add(new Location("Epy Drost", new LatLng(52.21971, 6.8895)));
        locations.add(new Location("ROC De Maere", new LatLng(52.21949, 6.88890)));
        locations.add(new Location("Wolvecamp", new LatLng(52.22126, 6.88596)));
        locations.add(new Location("Haanstra", new LatLng(52.21970, 6.88927)));
        locations.add(new Location("Forum", new LatLng(52.22038, 6.88621)));
        locations.add(new Location("Elderink", new LatLng(52.22029, 6.88502)));
        locations.add(new Location("Randstad", new LatLng(52.22122, 6.88947)));
        locations.add(new Location("ITC", new LatLng(52.22368, 6.88574)));
        locations.add(new Location("Stork", new LatLng(52.21995, 6.88826)));
        locations.add(new Location("Hazemeyer", new LatLng(52.22035, 6.88908)));
        locations.add(new Location("Hofstede Crull", new LatLng(52.21996, 6.88827)));
        locations.add(new Location("Aintsworth", new LatLng(52.22014, 6.88813)));
        locations.add(new Location("Villa Serphos", new LatLng(52.22024, 6.88857)));

        return locations;
    }

    public static ArrayList<Location> getDeventerLocations() {
        ArrayList<Location> locations = new ArrayList<>();

        locations.add(new Location("D", new LatLng(52.25452, 6.16827)));
        locations.add(new Location("C", new LatLng(52.25415, 6.1684)));
        locations.add(new Location("B", new LatLng(52.2544, 6.16832)));
        locations.add(new Location("A", new LatLng(52.25445, 6.16695)));

        return locations;
    }


    public static HashMap<String, Object> getLocationDetails(Context context, String locationName) {
        HashMap<String, Object> locationDetails = new HashMap<>();

        if(locationName == null || locationName.isEmpty()) {
            Tools.log("[Location] crap its empty!");
            return locationDetails;
        }

        //get the department & its coordinates
        for(Location location : getLocations()) {

            if(location.getName().substring(0, 1).equals(locationName.substring(0, 1))) {
                locationDetails.put(S.DEPARTMENT, location.getName());
                locationDetails.put(S.COORDINATES, location.getCoordinates());

                //below are extra cases where the first letter differ from the name.
            } else if(location.getName().equals("Epy Drost") && locationName.substring(0,1).equals("G")) {
                //Glasgebouw is officially called Epy Drost.
                locationDetails.put(S.DEPARTMENT, location.getName());
                locationDetails.put(S.COORDINATES, location.getCoordinates());
            } else if(location.getName().equals("Aintsworth") && locationName.substring(0,1).equals("N")) {
                locationDetails.put(S.DEPARTMENT, location.getName());
                locationDetails.put(S.COORDINATES, location.getCoordinates());
            } else if(location.getName().equals("Stork") && locationName.substring(0,1).equals("N")) {
                locationDetails.put(S.DEPARTMENT, location.getName());
                locationDetails.put(S.COORDINATES, location.getCoordinates());
            } else if(location.getName().equals("Hofstede Crull") && locationName.substring(0,1).equals("P")) {
                locationDetails.put(S.DEPARTMENT, location.getName());
                locationDetails.put(S.COORDINATES, location.getCoordinates());
            } else if(location.getName().equals("Hazemeyer")  && locationName.substring(0,1).equals("Q")) {
                locationDetails.put(S.DEPARTMENT, location.getName());
                locationDetails.put(S.COORDINATES, location.getCoordinates());
            }
        }

        //get the floor
        locationDetails.put(S.FLOOR, locationName.substring(locationName.indexOf(".") -1, locationName.indexOf(".")));

        //get the room.
        locationDetails.put(S.ROOM, locationName.substring(locationName.indexOf(".") +1));

        Tools.log(locationDetails.toString());

        return locationDetails;
    }



}
