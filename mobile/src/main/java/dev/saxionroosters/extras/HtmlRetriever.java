package dev.saxionroosters.extras;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

import dev.saxionroosters.R;
import dev.saxionroosters.activities.IntroActivity;
import dev.saxionroosters.activities.MainActivity;
import dev.saxionroosters.activities.SearchActivity;
import dev.saxionroosters.activities.SearchActivity_;
import dev.saxionroosters.dialogs.ErrorDialog;
import dev.saxionroosters.fragments.IntroFragment;
import dev.saxionroosters.model.Owner;
import dev.saxionroosters.model.Result;
import dev.saxionroosters.model.Week;

/**
 * Created by hugo on 27/02/16.
 */
public class HtmlRetriever {

    private Activity context;
    private Storage storage;
    private Week week;

    public HtmlRetriever(Activity context) {
        this.context = context;
        storage = Storage.getInstance(context);
    }

    public HtmlRetriever(Activity context, Week week) {
        this.context = context;
        storage = Storage.getInstance(context);
        this.week = week;
    }

    public Object retrieveHtml(String... url) {
        Log.e("debug", "Request: " + url[0]);

        try {
            //Get the document from the internet
            Document doc = Jsoup.connect(url[0]).get();

            //parse the html to a schedule object
            if(url[1] == S.PARSE_WEEK) {
                Week parsedWeek = HtmlParser.parseWeek(doc, week);
                return parsedWeek;
            } else if(url[1] == S.PARSE_WEEK_PAGER) {
                //If there is no pagination div, we know this is a list of results.
                //Else this is our final result.
                if(doc.select("div.pagination").isEmpty()) {
                    return url[2];
                } else {
                    ArrayList weeks = HtmlParser.parseWeekPager(doc);
                    return weeks;
                }
            } else if(url[1] == S.PARSE_SEARCH_RESULTS) {
                //If there is no pagination div, we know this is a list of results.
                //Else this is our final result.
                ArrayList<Result> results = new ArrayList<>();
                if(doc.select("div.pagination").isEmpty()) {
                    results.addAll(HtmlParser.parseSearchResults(doc));
                } else {
                    //url[2] contains our query string.
                    //TODO: this should be a full result object.
                    ArrayList<Week> weeks = HtmlParser.parseWeekPager(doc);
                    String ownerAbbreviation = url[2];
                    String ownerType = null;
                    if(!weeks.isEmpty()) {
                        ownerAbbreviation = weeks.get(0).getOwner().getName();
                        ownerType = weeks.get(0).getOwner().getTypeName();
                    }
                    results.add(new Result(ownerAbbreviation, "" , ownerType));
                }

                return results;
            }

        } catch (IOException e) {
            //TODO: errorhandling plssss.
            e.printStackTrace();
            return e;
        }
        return null;
    }

    public void onWeekPagerRetrieveCompleted(Object obj) {
        if(obj instanceof String) {
            if(context instanceof MainActivity) {
                Intent i = new Intent(context, SearchActivity_.class);
                i.putExtra(S.SEARCH_QUERY, (String) obj);
                context.startActivity(i);
            } else if(context instanceof SearchActivity) {
                storage.setSearchResults(new ArrayList<Result>());
            }

        } else if(obj instanceof ArrayList) {
            ArrayList<Object> arrayList = (ArrayList<Object>) obj;

            //an empty arraylist this means the result is bad.
            if(arrayList.isEmpty()) {
                storage.setSearchResults(new ArrayList<Result>());
                return;
            }

            if(arrayList.get(0) instanceof Week) {
                //We received the empty weeks for the pager!
                ArrayList<Week> newWeeks = (ArrayList<Week>) obj;
                if(!storage.getCurrentWeeks().isEmpty()) {
                    if(!storage.getCurrentWeeks().get(0).getOwner().getName().equals(newWeeks.get(0).getOwner().getName())) {
                        storage.setCurrentWeeks(newWeeks);
                    } else {
                        //add the new weeks if they don't exist to currentWeeks.
                    }
                } else {
                    storage.setCurrentWeeks(newWeeks);
                }
                if(context instanceof SearchActivity) ((SearchActivity) context).finish();
                else if(context instanceof IntroActivity) {

                    Owner owner = ((Week) arrayList.get(0)).getOwner();
                    storage.saveObject(S.SETTING_STARTUP_OWNER, Tools.getOwnerIdName(owner));
                    storage.saveObject(S.INTRO_COMPLETE, "true");
                    ((IntroActivity) context).finish();
                }
            } else if(arrayList.get(0) instanceof Result) {
                ArrayList<Result> results = (ArrayList<Result>) obj;
                //for now remove all courses, because we cannot handle them yet.
                for(int i = 0; i < results.size(); i++) {
                    Result r = results.get(i);
                    if(r.getType() == null || r.getType().equals(S.COURSE)) {
                        results.remove(r);
                    }
                }
                storage.setSearchResults(results);
            }
        } else if(obj instanceof Exception) {
            ErrorDialog dialog = new ErrorDialog();
            Bundle args = new Bundle();
            args.putString(S.MESSAGE, context.getString(R.string.error_message_no_internet));
            args.putString(S.TITLE, context.getString(R.string.error_title_no_internet));
            dialog.setArguments(args);
            dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "dialog");
        }
    }

    public Week onWeekScheduleRetrieveCompleted(Object object) {
        //We received a (full) week object show the schedule to the user :D
        if (object instanceof Week) {
            this.week = (Week) object;
            storage.updateWeekById(week);
            return week;
        } else if(object instanceof Exception) {
            return null;
        }
        return null;
    }

}
