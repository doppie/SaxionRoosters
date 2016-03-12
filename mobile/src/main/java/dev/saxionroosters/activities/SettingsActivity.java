package dev.saxionroosters.activities;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import dev.saxionroosters.R;
import dev.saxionroosters.extras.S;
import dev.saxionroosters.extras.Tools;

/**
 * Created by hugo on 12/03/16.
 */
@EActivity(R.layout.activity_settings)
public class SettingsActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

    @ViewById(R.id.classButton)
    protected Button classButton;

    @AfterViews
    protected void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setSubtitle("Instellingen");

        classButton.setText(((String) storage.getObject(S.SETTING_STARTUP_OWNER)).replace("\"", ""));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(Tools.isLollipop()) supportFinishAfterTransition();
        else finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

}
