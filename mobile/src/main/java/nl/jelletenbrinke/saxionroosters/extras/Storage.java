package nl.jelletenbrinke.saxionroosters.extras;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;

import nl.jelletenbrinke.saxionroosters.model.Result;
import nl.jelletenbrinke.saxionroosters.model.Week;

/**
 * Created by hugo on 27/02/16.
 */
@EBean(scope = EBean.Scope.Singleton)
public class Storage {

    private static final String PREFERENCE_NAME = "SAXIONROOSTERS";

    //data
    private ArrayList<Week> currentWeeks;
    private ArrayList<Result> searchResults;

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

}
