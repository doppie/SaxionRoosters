package dev.saxionroosters.extras;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import dev.saxionroosters.R;
import dev.saxionroosters.adapters.DatabaseAdapter;
import dev.saxionroosters.model.Result;
import dev.saxionroosters.model.RoosterNotification;
import dev.saxionroosters.model.Week;

/**
 * Created by hugo on 27/02/16.
 */
@EBean(scope = EBean.Scope.Singleton)
public class Storage {

    private static final String PREFERENCE_NAME = "SAXIONROOSTERS";

    //data
    private ArrayList<Week> currentWeeks;
    private ArrayList<Result> searchResults;
    private ArrayList<RoosterNotification> notifications;

    private DatabaseAdapter dbAdapter;

    @Bean
    static Storage instance;

    @RootContext
    Context context;

    public static Storage getInstance(Context ctx) {
        if (instance == null)
            instance = new Storage(ctx);
        return instance;
    }

    private SharedPreferences getPreferences() {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    protected Storage(Context ctx) {
        this.context = ctx.getApplicationContext();
        currentWeeks = new ArrayList<>();
        searchResults = new ArrayList<>();
        notifications = new ArrayList<>();
        dbAdapter = DatabaseAdapter.getInstance(ctx);
//        notifications.addAll(dbAdapter.getNotifications());
    }

    public <T> T getObject(Class<T> pojo) {
        String str = getPreferences().getString(pojo.getSimpleName(), null);
        if (str == null) return null;
        return new Gson().fromJson(str, pojo);
    }

    public Object getObject(String key) {
        return getPreferences().getString(key, null);
    }

    public void saveObject(String key, Object pojo) {
        String str = null;
        if (pojo != null)
            str = new Gson().toJson(pojo);

        getPreferences().edit().putString(key, str).apply();
    }

    public void saveObject(Object pojo) {
        saveObject(pojo.getClass().getSimpleName(), pojo);
    }

    public void deleteObject(Class<?> pojo) {
        saveObject(pojo.getSimpleName(), null);
    }

    public void deleteStorage() {
        getPreferences().edit().clear().commit();
    }

    /**
     * Returns a clone of currentWeeks.
     * This way you cannot mess with this array outside of the Dataset
     * @return
     */
    public ArrayList<Week> getCurrentWeeks() {
        return (ArrayList<Week>) currentWeeks.clone();
    }

    public void setCurrentWeeks(ArrayList<Week> weeks) {
        this.currentWeeks = weeks;
    }

    public void updateWeekById(Week updatedWeek) {
        for(Week week : currentWeeks) {
            if(week.getId().equals(updatedWeek.getId()) && week.getName().equals(updatedWeek.getName())) {
                currentWeeks.set(currentWeeks.indexOf(week), updatedWeek);
                return;
            }
        }
    }

    public Week getWeekById(String id) {
        for(Week week : currentWeeks) {
            if(week.getId().equals(id)) return week;
        }
        return null;
    }

    /**
     * Returns a clone of searchResults.
     * This way you cannot mess with this array outside of the Dataset
     * @return
     */
    public ArrayList<Result> getSearchResults() {
        return (ArrayList<Result>) searchResults.clone();
    }

    public void setSearchResults(ArrayList<Result> searchResults) {
        this.searchResults = searchResults;
    }


    /**
     * DATABASE CALLS BELOW
     */
    public boolean addNotification(String title, String subtitle, Date showDate) {
        try {
            int id = dbAdapter.addNotification(title, subtitle, showDate);
            if(id > 0) {
                notifications.add(new RoosterNotification(id, title, subtitle, showDate));
                return true;
            }
        } catch (SQLException e) {
            Tools.log(e.getMessage());
        }

        return false;
    }

    public void removeNotification(int notificationId) {

    }
}
