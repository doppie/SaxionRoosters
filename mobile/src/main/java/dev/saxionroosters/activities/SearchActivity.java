package dev.saxionroosters.activities;

import android.app.ProgressDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import dev.saxionroosters.R;
import dev.saxionroosters.adapters.OwnerAdapter;
import dev.saxionroosters.extras.HtmlRetriever;
import dev.saxionroosters.extras.S;
import dev.saxionroosters.extras.Tools;
import dev.saxionroosters.interfaces.ClickListener;
import dev.saxionroosters.model.Owner;

@EActivity(R.layout.activity_search)
public class SearchActivity extends BaseActivity implements ClickListener{

    @ViewById(R.id.searchText)
    protected EditText searchText;

    @ViewById(R.id.backButton)
    protected ImageView backButton;

    @ViewById(R.id.clearTextButton)
    protected ImageView clearTextButton;

    @ViewById(R.id.list)
    protected RecyclerView list;

    @ViewById(R.id.noResultsView)
    protected TextView noResultsView;

    @ViewById(R.id.loadingLayout)
    protected RelativeLayout loadingLayout;

    //adapters
    private OwnerAdapter ownerAdapter;
    private RecyclerView.LayoutManager listLayoutManager;


    @AfterViews
    protected void init() {
        String searchQuery = getIntent().getStringExtra(S.SEARCH_QUERY);
        if(searchQuery != null && !searchQuery.isEmpty()) {
            searchText.setText(searchQuery);
            clearTextButton.setVisibility(View.VISIBLE);
        }

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("debug", "onTextChanged: " + s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("debug", "afterTextChanged: " + s.toString());
                String query = s.toString();
                if (query.length() > 0) {
                    clearTextButton.setVisibility(View.VISIBLE);
                } else {
                    clearTextButton.setVisibility(View.INVISIBLE);
                }
                if (query.length() > 1) getSearchResults(query);

            }
        });

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String query = v.getText().toString();
                    if(query != null && query.length() > 1) getSearchResults(query);
                    return false;
                }
                return false;
            }
        });

        //fixed size always true: important for performance!
        list.setHasFixedSize(true);
        listLayoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(listLayoutManager);

        //This one is used whenever an item is removed or added to the adapter
        //It animates the item nicely :)
        list.setItemAnimator(new DefaultItemAnimator());

        updateUI();
    }

    @UiThread
    protected void updateUI() {
        Log.e("debug", "updateUI");

        ArrayList<Owner> searchResults = new ArrayList<>();

        //Adding our results to ownerAdapter
        if(storage.getSearchResults() != null && !storage.getSearchResults().isEmpty()) {
            searchResults = Tools.getResultsForOwnerAdapter(storage.getSearchResults(), true);
            noResultsView.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
            loadingLayout.setVisibility(View.GONE);
        } else {
            noResultsView.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
            loadingLayout.setVisibility(View.GONE);
        }
        list.setAdapter(null);
        ownerAdapter = new OwnerAdapter(searchResults, this);
        list.setAdapter(ownerAdapter);
    }

    @Background
    protected void getWeekPager(String query) {
        preExecute(true);

        HtmlRetriever retriever = new HtmlRetriever(this);
        String url = S.URL + S.QUERY + query;
        Object object = retriever.retrieveHtml(url, S.PARSE_WEEK_PAGER, query);

        postExecute(retriever, object);
    }

    @Background
    protected void getSearchResults(String query) {
        preExecute(false);


        HtmlRetriever retriever = new HtmlRetriever(this);
        String url = S.URL + S.QUERY + query;
        Object object = retriever.retrieveHtml(url, S.PARSE_SEARCH_RESULTS, query);

        postExecute(retriever, object);
    }

    @UiThread
    protected void postExecute(HtmlRetriever retriever, Object object) {
        retriever.onWeekPagerRetrieveCompleted(object);
        updateUI();
        if(dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @UiThread
    protected void preExecute(boolean dialogStyle) {

        if(!dialogStyle) {
            loadingLayout.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
            noResultsView.setVisibility(View.GONE);
        } else {
            dialog = new ProgressDialog(this);
            this.dialog.setMessage(getString(R.string.loading));
            this.dialog.show();
        }
    }

    @Override
    public void onClick(int position, boolean isLongClick) {
        Owner item = ownerAdapter.getData().get(position);
        if(!isLongClick) {
            String name = Tools.parseQueryFromName(item.getName());
            getWeekPager(name);
        }
    }

    @Click(R.id.backButton)
    void backButtonClicked() {
        super.onBackPressed();
    }

    @Click(R.id.clearTextButton)
    void setClearTextButtonClicked() {
        String query = searchText.getText().toString();
        if(query != null && query.length() > 0) {
            searchText.setText("");
            clearTextButton.setVisibility(View.INVISIBLE);
        }
    }
}
