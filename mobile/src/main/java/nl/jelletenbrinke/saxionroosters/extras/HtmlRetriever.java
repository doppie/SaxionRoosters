package nl.jelletenbrinke.saxionroosters.extras;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

import nl.jelletenbrinke.saxionroosters.R;
import nl.jelletenbrinke.saxionroosters.activities.MainActivity;
import nl.jelletenbrinke.saxionroosters.activities.SearchActivity;
import nl.jelletenbrinke.saxionroosters.dialogs.ErrorDialog;
import nl.jelletenbrinke.saxionroosters.model.Dataset;
import nl.jelletenbrinke.saxionroosters.model.Result;
import nl.jelletenbrinke.saxionroosters.model.Week;

/**
 * Created by hugo on 27/02/16.
 */
public class HtmlRetriever {

    private Activity context;
    private Dataset dataset;
    private Week week;

    public HtmlRetriever(Activity context, Dataset dataset) {
        this.context = context;
        this.dataset = dataset;
    }

    public HtmlRetriever(Week week) {
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
                    return null;
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
                    String owner = url[2];
                    if(!weeks.isEmpty()) {
                        owner = weeks.get(0).getOwner().getName();
                    }
                    results.add(new Result(owner, "", ""));
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

    public void onRetrieveCompleted(Object obj) {
        if(obj == null) {
            if(!dataset.getCurrentWeeks().isEmpty()) {
                ((MainActivity) context).getToolbar().setTitle(dataset.getCurrentWeeks().get(0).getOwner().getName());
            } else {
                ((MainActivity) context).getToolbar().setTitle(context.getString(R.string.app_name));
            }
            ((MainActivity) context).getPager().setAdapter(null);
            Intent i = new Intent(context, SearchActivity.class);
            context.startActivity(i);
        } else if(obj instanceof ArrayList) {
            ArrayList<Object> arrayList = (ArrayList<Object>) obj;

            //an empty arraylist this means the result is bad.
            if(arrayList.isEmpty()) {
                return;
            }

            if(arrayList.get(0) instanceof Week) {
                //We received the empty weeks for the pager!
                ArrayList<Week> newWeeks = (ArrayList<Week>) obj;
                if(!dataset.getCurrentWeeks().isEmpty()) {
                    if(!dataset.getCurrentWeeks().get(0).getOwner().getName().equals(newWeeks.get(0).getOwner().getName())) {
                        dataset.setCurrentWeeks(newWeeks);
                    }
                } else {
                    dataset.setCurrentWeeks(newWeeks);
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
                dataset.setSearchResults(results);
            }
        } else if(obj instanceof Exception) {
            ErrorDialog dialog = new ErrorDialog();
            Bundle args = new Bundle();
            args.putString(S.MESSAGE, context.getString(R.string.error_message_no_internet));
            args.putString(S.TITLE, context.getString(R.string.error_title_no_internet));
            dialog.setArguments(args);
            dialog.show(((MainActivity) context).getSupportFragmentManager(), "dialog");
        }
    }

}
