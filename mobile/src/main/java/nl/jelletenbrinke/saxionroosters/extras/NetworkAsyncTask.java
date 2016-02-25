package nl.jelletenbrinke.saxionroosters.extras;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

import nl.jelletenbrinke.saxionroosters.interfaces.OnAsyncTaskCompleted;
import nl.jelletenbrinke.saxionroosters.model.Week;

/**
 * Created by Doppie on 24-2-2016.
 */
public class NetworkAsyncTask extends AsyncTask<String, Void, Object> {

    private OnAsyncTaskCompleted listener;

    public NetworkAsyncTask(OnAsyncTaskCompleted listener) {
        this.listener = listener;
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
                ArrayList weeks = HtmlParser.parseWeekPager(doc);
                return weeks;
            }

        } catch (IOException e) {
            //TODO: errorhandling plssss.
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object object) {
        //Send the object to the attached listener.
        listener.onAsyncTaskCompleted(object);
    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Void... values) {}

}
