package com.softartdev.dailyrssnews;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends BaseAdapter {
    private ArrayList<RSSItem> listData;
    private LayoutInflater layoutInflater;

    public CustomListAdapter(Context context, ArrayList<RSSItem> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.rss_item_list_row, null);
            holder = new ViewHolder();
            holder.headlineView = (TextView) convertView.findViewById(R.id.title);
            holder.reporterNameView = (TextView) convertView.findViewById(R.id.description);
            holder.reportedDateView = (TextView) convertView.findViewById(R.id.pub_date);
            holder.pageURL = (TextView) convertView.findViewById(R.id.page_url);
            holder.imageView = (ImageView) convertView.findViewById(R.id.photo);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RSSItem rssItem = listData.get(position);
        holder.headlineView.setText(rssItem.getTitle());
        holder.reporterNameView.setText(rssItem.getDescription());
        holder.reportedDateView.setText(rssItem.getPubdate());
        if (holder.imageView != null) {
            new ImageDownloaderTask(holder.imageView).execute(rssItem.getPhoto());
        }
        holder.pageURL.setText(rssItem.getLink());

        return convertView;
    }

    static class ViewHolder {
        TextView headlineView;
        TextView reporterNameView;
        TextView reportedDateView;
        ImageView imageView;
        TextView pageURL;
    }
}
