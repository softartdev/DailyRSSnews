package com.softartdev.dailyrssnews;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

    // Progress Dialog
    private ProgressDialog pDialog;
    // Array list for list view
    ArrayList<RSSItem> rssItemList = new ArrayList<RSSItem>();
    RSSParser rssParser = new RSSParser();
    List<RSSItem> rssItems = new ArrayList<RSSItem>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String rss_link = "http://www.e1.ru/news/rdf/full_img.xml";
        /**
         * Calling a backgroung thread will loads recent articles of a website
         * @param rss url of website
         * */
        new loadRSSFeedItems().execute(rss_link);
        // selecting single ListView item
        ListView lv = getListView();
        // Launching new screen on Selecting Single ListItem
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent in = new Intent(getApplicationContext(), ItemActivity.class);
                // getting page url
                String page_url = ((TextView) view.findViewById(R.id.page_url)).getText().toString();
                Toast.makeText(getApplicationContext(), page_url, Toast.LENGTH_SHORT).show();
                in.putExtra("page_url", page_url);
                startActivity(in);
            }
        });
    }
    /**
     * Background Async Task to get RSS Feed Items data from URL
     * */
    class loadRSSFeedItems extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(
                    MainActivity.this);
            pDialog.setMessage("Loading recent articles...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        /**
         * getting all recent articles and showing them in listview
         * */
        @Override
        protected String doInBackground(String... args) {
            // rss link url
            String rss_url = args[0];
            // list of rss items
            rssItems = rssParser.getRSSFeedItems(rss_url);
            // looping through each item
            for(RSSItem item : rssItems){
                String description = item.getDescription();
                // taking image url from description
                String photo = null;
                Matcher matcher = Pattern.compile("<img src=\"([^\"]+)").matcher(description);
                while (matcher.find()) {
                    photo = "http://www.e1.ru" + matcher.group(1);
                }
                // editing description
                Matcher matcher1 = Pattern.compile(">.+").matcher(description);
                while (matcher1.find()){
                    description = matcher1.group().substring(1);
                }
                // editing date
                String date = item.getPubdate();
                String formattedTime = Calendar.getInstance().getTime().toString();
                SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss Z", Locale.ENGLISH);
                SimpleDateFormat output = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm", Locale.getDefault());
                try {
                    formattedTime = output.format(df.parse(date)); // it's edited date
                } catch (ParseException e1) {formattedTime = date;}

                RSSItem rssItem = new RSSItem(item.getTitle(), item.getLink(), description, formattedTime, "guid", photo);
                rssItemList.add(rssItem);
            }
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed items into listview
                     * */
                    // updating listview
                    setListAdapter(new CustomListAdapter(MainActivity.this, rssItemList));
                }
            });
            return null;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String args) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
        }
    }
}
