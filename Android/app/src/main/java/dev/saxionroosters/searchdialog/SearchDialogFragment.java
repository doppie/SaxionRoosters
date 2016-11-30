package dev.saxionroosters.searchdialog;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dev.saxionroosters.R;
import dev.saxionroosters.general.PreferenceManager;
import dev.saxionroosters.general.Prefs;
import dev.saxionroosters.general.Tools;

/**
 * Created by jelle on 30/11/2016.
 */

public class SearchDialogFragment extends DialogFragment implements SearchDialogView {

    @BindView(R.id.searchText)
    protected EditText searchText;
    @BindView(R.id.clearTextButton)
    protected ImageView clearTextButton;
    @BindView(R.id.list)
    protected RecyclerView list;
    @BindView(R.id.titleText)
    protected TextView titleText;
    @BindView(R.id.subtitleText)
    protected TextView subtitleText;
    @BindView(R.id.loadingLayout)
    protected RelativeLayout loadingLayout;

    private Unbinder unbinder;
    private SearchDialogPresenter presenter;
    private SearchAdapter searchAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_searchdialog, container, false);
        unbinder = ButterKnife.bind(this, v);

        this.presenter = new SearchDialogPresenter(this);
        initUI();

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.pause();
    }

    @Override
    public void initUI() {
        searchAdapter = new SearchAdapter(getContext(), new ArrayList<SearchItem>());
        searchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SearchItem item = searchAdapter.getSuggestionsList().get(position);

                //start the MainActivity
                presenter.finishSelection(item);

            }
        });
        list.setHasFixedSize(false);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setItemAnimator(new DefaultItemAnimator());
        list.setAdapter(searchAdapter);

        clearTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearSearchInput();
            }
        });

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString();
                if (query.length() > 0) {
                    clearTextButton.setVisibility(View.VISIBLE);
                } else {
                    clearTextButton.setVisibility(View.INVISIBLE);
                }
                if (query.length() > 1) presenter.search(query);

            }
        });

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //on enter submit the search query.
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    //queries to the API are allowed with a minimum of 2 chars.
                    String query = v.getText().toString();
                    if (query.length() > 1) presenter.search(query);

                    closeKeyboard();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void clearSearchInput() {
        searchText.setText(""); //clear.
        clearTextButton.setVisibility(View.GONE);

        //refresh the search results.
        searchAdapter.setSuggestionsList(new ArrayList<SearchItem>());
        searchAdapter.getFilter().filter("");
        searchAdapter.notifyDataSetChanged();

        //our results will be empty.
        showEmptyMessage();
    }

    @Override
    public void showSearchResults(ArrayList<SearchItem> results, String query) {
        Tools.log("results: " + results.size());
        searchAdapter.setSuggestionsList(results);
        searchAdapter.getFilter().filter(query);
        searchAdapter.notifyDataSetChanged();
    }

    @Override
    public void closeKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void dismissEmptyMessage() {
        titleText.setVisibility(View.GONE);
        subtitleText.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyMessage() {
        titleText.setVisibility(View.VISIBLE);
        subtitleText.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadingLayout() {
        loadingLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissLoadingLayout() {
        loadingLayout.setVisibility(View.GONE);
    }

}
