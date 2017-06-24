package com.example.android.newsapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.format;
import static android.R.attr.resource;

/**
 * Created by pkhotpanya on 6/23/17.
 */

public class NewsStoryAdapter extends ArrayAdapter<NewsStory> {

    public NewsStoryAdapter(@NonNull Activity context, List<NewsStory> newsStories) {
        super(context, 0, newsStories);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listViewItem = convertView;
        ViewHolder holder;
        if (listViewItem == null) {
            listViewItem = LayoutInflater.from(getContext()).inflate(R.layout.layout_newsstory, parent, false);
            holder = new ViewHolder(listViewItem);
            listViewItem.setTag(holder);
        } else {
            holder = (ViewHolder) listViewItem.getTag();
        }

        NewsStory currentItem = getItem(position);
        holder.title.setText(currentItem.getNewsTitle());
        holder.section.setText(currentItem.getNewsSection());

        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = null;
        try {
            date = dateformat.parse(currentItem.getNewsDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat simplerFormat = new SimpleDateFormat("yyyy-MM-dd");
        String datetime = simplerFormat.format(date);

        holder.date.setText(datetime);

        return listViewItem;
    }

    public class ViewHolder {
        @BindView(R.id.textview_title)
        TextView title;
        @BindView(R.id.textview_section)
        TextView section;
        @BindView(R.id.textview_date)
        TextView date;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
