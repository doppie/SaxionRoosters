package nl.jelletenbrinke.saxionroosters.extras;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

import nl.jelletenbrinke.saxionroosters.interfaces.OnAsyncTaskCompleted;
import nl.jelletenbrinke.saxionroosters.model.Result;
import nl.jelletenbrinke.saxionroosters.model.Week;

/**
 * Created by Doppie on 24-2-2016.
 */
public class NetworkAsyncTask extends AsyncTask<String, Void, Object> {

    private OnAsyncTaskCompleted listener;

    private boolean showProgressDialog = false;
    private ProgressDialog dialog;

    public NetworkAsyncTask(OnAsyncTaskCompleted listener, Activity activity, boolean showProgressDialog) {
        this.listener = listener;
        this.showProgressDialog = showProgressDialog;
        if(showProgressDialog) dialog = new ProgressDialog(activity);
    }

    @Override
    protected Object doInBackground(String... url) {


        try {
            //Get the document from the internet
            Document doc = Jsoup.connect(url[0]).get();

            //parse the html to a schedule object
            if(url[1] == S.PARSE_WEEK) {
                Week week = HtmlParser.parseWeek(doc, url[2], url[3]);
                return week;
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
                    results.add(new Result(url[2], "", ""));
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

    @Override
    protected void onPostExecute(Object object) {
        //Send the object to the attached listener.
        if(showProgressDialog) dialog.dismiss();
        listener.onAsyncTaskCompleted(object);

    }

    @Override
    protected void onPreExecute() {
        if(showProgressDialog) {
            this.dialog.setMessage("Debug..");
            this.dialog.show();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {}

}
