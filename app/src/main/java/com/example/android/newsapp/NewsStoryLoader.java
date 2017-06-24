package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by pkhotpanya on 6/23/17.
 */

public class NewsStoryLoader extends AsyncTaskLoader<List<NewsStory>> {

    private String newsUrl;

    public NewsStoryLoader(Context context, String url) {
        super(context);
        newsUrl = url;
    }

    @Override
    public List<NewsStory> loadInBackground() {
        return new QueryUtil().extractNewsStory(newsUrl);
    }

}
