package com.example.android.newsapp;

import static android.R.attr.author;

/**
 * Created by pkhotpanya on 6/23/17.
 */

public class NewsStory {

    private String newsTitle;
    private String newsSection;
    private String newsDate;
    private String newsUrl;

    public NewsStory(String title, String section, String date, String url) {
        newsTitle = title;
        newsSection = section;
        newsDate = date;
        newsUrl = url;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public String getNewsSection() {
        return newsSection;
    }

    public String getNewsDate() {
        return newsDate;
    }

    public String getNewsUrl() {
        return newsUrl;
    }
}
