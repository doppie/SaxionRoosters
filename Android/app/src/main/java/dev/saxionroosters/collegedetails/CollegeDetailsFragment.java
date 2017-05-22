package dev.saxionroosters.collegedetails;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dev.saxionroosters.R;
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
//        professorLayout
    }

    private void showMap() {
        if(college.getLocation() != null && !college.getLocation().isEmpty()) {

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
