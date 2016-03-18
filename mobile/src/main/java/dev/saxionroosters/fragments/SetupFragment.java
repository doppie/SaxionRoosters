package dev.saxionroosters.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import dev.saxionroosters.R;
import dev.saxionroosters.activities.BaseActivity;
import dev.saxionroosters.adapters.OwnerAdapter;
import dev.saxionroosters.adapters.SimpleOwnerAdapter;
import dev.saxionroosters.extras.HtmlRetriever;
import dev.saxionroosters.extras.S;
import dev.saxionroosters.extras.Storage;
import dev.saxionroosters.extras.Tools;
import dev.saxionroosters.interfaces.ClickListener;
import dev.saxionroosters.model.Owner;

/**
 * Created by Doppie on 11-3-2016.
 */
@EFragment(R.layout.fragment_setup)
public class SetupFragment extends DialogFragment implements ClickListener {

    @ViewById(R.id.searchText)
    protected EditText searchText;

    @ViewById(R.id.clearTextButton)
    protected ImageView clearTextButton;

    @ViewById(R.id.list)
    protected RecyclerView list;

    @ViewById(R.id.titleText)
    protected TextView titleText;

    @ViewById(R.id.subtitleText)
    protected TextView subtitleText;

    @ViewById(R.id.loadingLayout)
    protected RelativeLayout loadingLayout;

    //adapters
    private SimpleOwnerAdapter simpleOwnerAdapter;
    private RecyclerView.LayoutManager listLayoutManager;

    private ProgressDialog dialog;

    @Bean
    protected Storage storage;

    private String viewType = S.FRAGMENT;

    @AfterViews
    protected void init() {

        viewType = getArguments().getString(S.VIEW_TYPE);

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
                    if (query != null && query.length() > 1) getSearchResults(query);

                    //Try to close the keyboard :)
                    View view = getActivity().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    return true;
                }
                return false;
            }
        });

        //fixed size always true: important for performance!
        list.setHasFixedSize(true);
        listLayoutManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(listLayoutManager);

        //This one is used whenever an item is removed or added to the adapter
        //It animates the item nicely :)
        list.setItemAnimator(new DefaultItemAnimator());

        if(viewType.equals(S.DIALOG))   getDialog().setTitle("Selecteer rooster");

    }

    @UiThread
    protected void updateUI() {
        Log.e("debug", "updateUI");

        ArrayList<Owner> searchResults = new ArrayList<>();

        //Adding our results to ownerAdapter
        if(storage.getSearchResults() != null && !storage.getSearchResults().isEmpty()) {
            searchResults = Tools.getResultsForOwnerAdapter(storage.getSearchResults(), false);
            titleText.setVisibility(View.GONE);
            subtitleText.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
            loadingLayout.setVisibility(View.GONE);
        } else {
            titleText.setVisibility(View.VISIBLE);
            subtitleText.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
            loadingLayout.setVisibility(View.GONE);
        }
        list.setAdapter(null);
        simpleOwnerAdapter = new SimpleOwnerAdapter(searchResults, this);
        list.setAdapter(simpleOwnerAdapter);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        ((BaseActivity) getActivity()).updateUI();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);

        ((BaseActivity) getActivity()).updateUI();
    }

    @Background
    protected void getWeekPager(String query) {
        preExecute(true);

        HtmlRetriever retriever = new HtmlRetriever(getActivity());
        String url = S.URL + S.QUERY + query;
        Object object = retriever.retrieveHtml(url, S.PARSE_WEEK_PAGER, query);

        postExecute(retriever, object);
    }

    @Background
    protected void getSearchResults(String query) {
        preExecute(false);


        HtmlRetriever retriever = new HtmlRetriever(getActivity());
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
            titleText.setVisibility(View.GONE);
            subtitleText.setVisibility(View.GONE);
        } else {
            dialog = new ProgressDialog(getActivity());
            this.dialog.setMessage(getString(R.string.loading));
            this.dialog.show();
        }
    }

    @Override
    public void onClick(View v, int position, boolean isLongClick) {
        Owner item = simpleOwnerAdapter.getData().get(position);
        if(!isLongClick) {
            String name = Tools.parseQueryFromName(item.getName());
            if(viewType.equals(S.FRAGMENT)) {
                getWeekPager(name);
            } else if(viewType.equals(S.DIALOG)) {
                storage.saveObject(S.SETTING_STARTUP_OWNER, name);
                dismiss();
            }
        }
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
