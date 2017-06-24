package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsStory>>{

    private static final String NEWS_URL = "http://content.guardianapis.com/search?q=trump&api-key=test";
    TextView textViewResponse;
    ProgressBar progressBarWorking;
    NewsStoryAdapter newsStoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResponse = (TextView) findViewById(R.id.textview_responsemessage);
        progressBarWorking = (ProgressBar) findViewById(R.id.progressbar_working);
        ListView newsList = (ListView) findViewById(R.id.listview_list);
        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                NewsStory currentItem = (NewsStory) adapterView.getItemAtPosition(position);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(currentItem.getNewsUrl())));
            }
        });
        newsStoryAdapter = new NewsStoryAdapter(this, new ArrayList<NewsStory>());
        newsList.setAdapter(newsStoryAdapter);
        newsList.setEmptyView(textViewResponse);

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            getLoaderManager().initLoader(0, null, this).forceLoad();
        } else {
            progressBarWorking.setVisibility(View.GONE);
            textViewResponse.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<NewsStory>> onCreateLoader(int i, Bundle bundle) {
        return new NewsStoryLoader(this, NEWS_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsStory>> loader, List<NewsStory> newsStories) {
        progressBarWorking.setVisibility(View.GONE);
        newsStoryAdapter.clear();

        if (newsStories != null && !newsStories.isEmpty()){
            newsStoryAdapter.addAll(newsStories);
        } else {
            textViewResponse.setText(R.string.unable_to_retrieve_news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsStory>> loader) {
        progressBarWorking.setVisibility(View.VISIBLE);
        textViewResponse.setText("");
        newsStoryAdapter.clear();
    }

}
