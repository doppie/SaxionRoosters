package dev.saxionroosters.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import dev.saxionroosters.R;
import dev.saxionroosters.model.College;
import dev.saxionroosters.model.Teacher;
import dev.saxionroosters.views.JelleTextView;

/**
 * Created by Doppie on 10-3-2016.
 */
@EFragment(R.layout.fragment_college_details)
public class CollegeDetailsFragment extends Fragment {

    @ViewById(R.id.teachersText)
    protected JelleTextView teachersText;

    @ViewById(R.id.describeText)
    protected JelleTextView describeText;

    private College college;

    @AfterViews
    protected void init() {
        Bundle args = getArguments();
        college = (College) args.getSerializable("college");
        List<Teacher> teachers = college.getTeachers();

        if(teachers.size() == 1) {
            if(teachers.get(0).getName().equals("")) {
                teachersText.setText(getString(R.string.no_teachers));
            }
            else {
                describeText.setText(getString(R.string.teacher_text));
                teachersText.setText(college.getTeachers().get(0).getName());
            }
        }
        else {
            describeText.setText(getString(R.string.teachers_text));
            String teachersString = "";
            for(Teacher t : teachers) {
                teachersString = teachersString + t.getName() + "\n";
            }
            teachersText.setText(teachersString);
        }
    }

}
