package dev.saxionroosters.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dev.saxionroosters.R;
import dev.saxionroosters.extras.MapsTools;
import dev.saxionroosters.extras.S;
import dev.saxionroosters.extras.ThemeTools;
import dev.saxionroosters.extras.Tools;
import dev.saxionroosters.model.College;
import dev.saxionroosters.model.Location;
import dev.saxionroosters.model.Teacher;
import dev.saxionroosters.views.JelleTextView;

/**
 * Created by Doppie on 10-3-2016.
 */
@EFragment(R.layout.fragment_college_details)
public class CollegeDetailsFragment extends Fragment {


    @ViewById(R.id.descriptionText)
    protected TextView descriptionText;

    @ViewById(R.id.timeText)
    protected TextView timeText;

    @ViewById(R.id.locationText)
    protected TextView locationText;
    @ViewById(R.id.locationTitleText)
    protected TextView locationTitleText;

    @ViewById(R.id.docentLayout)
    protected LinearLayout docentLayout;

    @ViewById(R.id.mapsLayout)
    protected LinearLayout mapsLayout;

    private College college;

    @AfterViews
    protected void init() {
        Bundle args = getArguments();
        college = (College) args.getSerializable("college");

        //name and details
        descriptionText.setText(college.getName() + "\n" + college.getType());

        //time and date
        timeText.setText(college.getDate() + "\n" + college.getTime());

        //location
        if(college.getLocation() == null || college.getLocation().isEmpty()) {
            Tools.log("[Location] empty.");
            locationText.setText(getString(R.string.location_not_found));
            mapsLayout.setVisibility(View.GONE);
        } else {
            HashMap<String, Object> locationDetails = MapsTools.getLocationDetails(getContext(), college.getLocation());
            locationText.setText(locationDetails.get(S.DEPARTMENT) + ", "
                    + getString(R.string.floor) + " " + locationDetails.get(S.FLOOR) + ", "
                    + getString(R.string.room) + " " + locationDetails.get(S.ROOM));


            if(MapsTools.getEnschedeLocations().contains(
                    new Location((String) locationDetails.get(S.DEPARTMENT), (LatLng) locationDetails.get(S.COORDINATES)))) {
                locationTitleText.setText(getString(R.string.location) + " " + "@Enschede");
            } else if(MapsTools.getDeventerLocations().contains(
                    new Location((String) locationDetails.get(S.DEPARTMENT), (LatLng) locationDetails.get(S.COORDINATES)))){
                locationTitleText.setText(getString(R.string.location) + " " + "@Deventer");
            }

            MapsFragment mapsFragment = new MapsFragment_();
            Bundle mapArgs = new Bundle();
            mapArgs.putParcelable(S.COORDINATES, (LatLng) locationDetails.get(S.COORDINATES));
            mapArgs.putString(S.DEPARTMENT, (String) locationDetails.get(S.DEPARTMENT));
            mapsFragment.setArguments(mapArgs);
            getChildFragmentManager().beginTransaction().add(R.id.mapsLayout, mapsFragment).commit();
        }

        fillDocentLayout(college.getTeachers());
    }

    private void fillDocentLayout(ArrayList<Teacher> teachers) {
        //first empty the layout.
        docentLayout.removeAllViewsInLayout();

        //If we have no teachers, we want to tell this to the user.
        if(teachers.isEmpty()) {
            teachers.add(new Teacher(getString(R.string.no_teacher_available), S.UNKNOWN));
        }

        for(Teacher teacher : teachers) {
            Tools.log("Teacher: " + teacher.getName() + " id: " + teacher.getIdName());
            //inflate the item view, we will not use list here because we only load 1 ~ 10 items.
            View docentView = (View) LayoutInflater.from(getActivity()).inflate(R.layout.item_docent, null);
            TextView nameText = (TextView) docentView.findViewById(R.id.docentName);
            nameText.setText(teacher.getName());
            docentLayout.addView(docentView);
        }
    }

}
