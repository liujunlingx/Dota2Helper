package com.sanzhs.dota2helper;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchHistoryTable;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;
import com.sanzhs.dota2helper.fragment.Fragment1;
import com.sanzhs.dota2helper.fragment.Fragment3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanzhs on 2017/12/5.
 */

public class SearchActivity extends Activity{

    private SearchView mSearchView;
    private SearchHistoryTable mHistoryDatabase = new SearchHistoryTable(this);

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);

        final List<SearchItem> suggestionsList = new ArrayList<>();

        final SearchAdapter searchAdapter = new SearchAdapter(this, suggestionsList);
        searchAdapter.setSuggestionsList(suggestionsList);

        searchAdapter.addOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
                CharSequence text = textView.getText();
                mHistoryDatabase.addItem(new SearchItem(text));

                doSearch(text.toString());
            }
        });

        mSearchView = (SearchView) findViewById(R.id.searchView);
        mSearchView.setHint(R.string.searchHint);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mHistoryDatabase.addItem(new SearchItem(query));
                doSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mSearchView.setAdapter(searchAdapter);

        mHistoryDatabase.setHistorySize(3);
    }

    private void doSearch(String query){
        Intent intent = new Intent(getApplication(),MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("playerId",query);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}
