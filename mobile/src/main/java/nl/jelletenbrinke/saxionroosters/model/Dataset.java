package nl.jelletenbrinke.saxionroosters.model;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by Doppie on 26-2-2016.
 */
public class Dataset extends Application {

    //data
    private ArrayList<Week> currentWeeks;
    private ArrayList<Result> searchResults;


    @Override
    public void onCreate() {
        super.onCreate();
        currentWeeks = new ArrayList<>();
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
