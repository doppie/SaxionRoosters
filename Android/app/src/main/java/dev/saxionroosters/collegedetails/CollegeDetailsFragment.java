package dev.saxionroosters.collegedetails;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dev.saxionroosters.R;
import dev.saxionroosters.collegedetails.map.MapFragment;
import dev.saxionroosters.collegedetails.map.MapTools;
import dev.saxionroosters.general.S;
import dev.saxionroosters.model.College;

/**
 * Created by Jelle on 22/05/2017.
 */

public class CollegeDetailsFragment extends Fragment {

    @BindView(R.id.descriptionText)
    TextView descriptionText;
    @BindView(R.id.timeText)
    TextView timeText;
    @BindView(R.id.locationText)
    TextView locationText;
    @BindView(R.id.locationTitleText)
    TextView locationTitleText;
    @BindView(R.id.professorLayout)
    LinearLayout professorLayout;
    @BindView(R.id.mapLayout)
    LinearLayout mapLayout;
    private Unbinder unbinder;

    private College college;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_details_college, container, false);
        unbinder = ButterKnife.bind(this, v);

        loadCollege();
        showCollege();
        return v;
    }

    private void loadCollege() {
        if(getArguments() != null) {
            college = (College) getArguments().getSerializable(S.COLLEGE);
        }
    }

    private void showCollege() {
        if(college == null) return;
        descriptionText.setText(college.getName() + "\n" + college.getNote());
        timeText.setText(college.getDate() + "\n" + college.getStart() + " - " + college.getEnd());

        if(college.getRoom() != null && !college.getRoom().isEmpty()) {
            HashMap<String, Object> locationDetails = MapTools.getLocationDetails(getContext(), college.getRoom());
            locationText.setText(locationDetails.get(S.DEPARTMENT) + ", "
                    + getString(R.string.floor) + " " + locationDetails.get(S.FLOOR) + ", "
                    + getString(R.string.room) + " " + locationDetails.get(S.ROOM));

            showMap(locationDetails);
        } else {
            locationText.setText(getString(R.string.error_no_location));
            mapLayout.setVisibility(View.GONE);
        }
    }

    private void showMap(HashMap<String, Object> locationDetails) {
        MapFragment mapFragment = new MapFragment();
        Bundle args = new Bundle();
        args.putParcelable(S.COORDINATES, (LatLng) locationDetails.get(S.COORDINATES));
        args.putString(S.DEPARTMENT, (String) locationDetails.get(S.DEPARTMENT));
        mapFragment.setArguments(args);

        getChildFragmentManager().beginTransaction().add(R.id.mapLayout, mapFragment).commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
