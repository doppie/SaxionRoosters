package dev.saxionroosters.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import dev.saxionroosters.R;
import dev.saxionroosters.extras.S;
import dev.saxionroosters.extras.Tools;
import dev.saxionroosters.model.College;
import dev.saxionroosters.model.Teacher;
import dev.saxionroosters.views.JelleTextView;

/**
 * Created by Doppie on 10-3-2016.
 */
@EFragment(R.layout.fragment_college_details)
public class CollegeDetailsFragment extends Fragment {

//    @ViewById(R.id.locationText)
//    protected TextView locationText;

    @ViewById(R.id.descriptionText)
    protected TextView descriptionText;

    @ViewById(R.id.timeText)
    protected TextView timeText;

    @ViewById(R.id.docentLayout)
    protected LinearLayout docentLayout;

    private College college;

    @AfterViews
    protected void init() {
        Bundle args = getArguments();
        college = (College) args.getSerializable("college");

        descriptionText.setText(college.getName() + "\n" + college.getType());
//        locationText.setText(college.getLocation());
        timeText.setText(college.getDate() + "\n" + college.getTime() + "\n\n" + college.getLocation());

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
            Log.e("debug", "Teacher: " + teacher.getName() + " id: " + teacher.getIdName());
            //inflate the item view, we will not use list here because we only load 1 ~ 10 items.
            View docentView = (View) LayoutInflater.from(getActivity()).inflate(R.layout.item_docent, null);
            TextView nameText = (TextView) docentView.findViewById(R.id.docentName);
            nameText.setText(teacher.getName());
            docentLayout.addView(docentView);
        }
    }

}
