package com.softartdev.dailyrssnews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.softartdev.dailyrssnews.fullscreenGallery.FullScreenViewActivity;
import com.softartdev.dailyrssnews.videoYoutubeList.RecyclerAdapter;
import com.softartdev.dailyrssnews.videoYoutubeList.VideoListActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ItemActivity extends Activity {
    String page_url;
    TextView url;
    Button video_ids_and_urls;
    Gallery gallery;
    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    public String[] images_urls;
    public String[] video_ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        if (savedInstanceState != null) {
            page_url = savedInstanceState.getString("page_url");
        } else {
            // get item url from intent
            Intent in = getIntent();
            page_url = in.getStringExtra("page_url");
        }
        // set item url on page
        url = (TextView) findViewById(R.id.item_url);//INVISIBLE NOW
        video_ids_and_urls = (Button) findViewById(R.id.video_ids);//INVISIBLE NOW

        //set recyclerview for video list
        recyclerView = (RecyclerView) findViewById(R.id.videolist_recycler_view);
        //recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        // Using JSoup library to parse the html source code
        ItemDownload itemDownload = new ItemDownload();
        itemDownload.execute();

        // start video activity
        video_ids_and_urls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                video_ids_and_urls.append("\n" + "clicked");
                Intent intent = new Intent(ItemActivity.this, VideoListActivity.class);
                intent.putExtra("video_ids", video_ids);
                startActivity(intent);
            }
        });
    }

    //TODO: implements onResume and onRestart methods for saving state activity on the Bundle
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putString("page_url", page_url);//no necessary now
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class ItemDownload extends AsyncTask<Void,Void,Void> {
        Document doc;
        Elements newscontent;
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                doc = Jsoup.connect(page_url).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);
            // set item title to page
            TextView title = (TextView) findViewById(R.id.item_title);
            title.setText(doc.title());
            // finding newscontent
            newscontent = doc.select("div.news");
            //description
            String newsText = newscontent.text();
            // check if newscontent found or not
            if (newscontent.size() > 0) {
                //media content
                images_urls = new String[newscontent.select("img").size()];
                //getting array of images urls
                for (int i=0; i < images_urls.length; i++){
                    images_urls[i] = "http://www.e1.ru" + newscontent.select("img").get(i).attr("src");
                    //add media content urls on top page
                    url.append("\n" + "photo: " + images_urls[i]);  }
                video_ids = new String[newscontent.select("[frameborder]").size()];
                //getting array of videos urls
                for (int i=0; i < video_ids.length; i++){
                    video_ids[i] = newscontent.select("[frameborder]").get(i).attr("src").substring(30);
                    video_ids_and_urls.append("\n" + "\n" +
                            // add in text view//INVISIBLE NOW
                            "id: " + video_ids[i] + "\n" +
                            "url: " + newscontent.select("[frameborder]").get(i).attr("src"));
                }
            } else {        newsText = doc.text();      }
            //set item description on page
            TextView description = (TextView) findViewById(R.id.item_description);
            description.setText(newsText);
            // inflate gallery
            gallery = (Gallery) findViewById(R.id.gallery);
            final GalleryImageAdapter galleryImageAdapter = new GalleryImageAdapter(ItemActivity.this);
            gallery.setAdapter(galleryImageAdapter);
            gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    //Toast.makeText(ItemActivity.this, "click on position " + position, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ItemActivity.this, FullScreenViewActivity.class);
                    intent.putExtra("images urls", images_urls);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
            });
            //inflate videolist recyclerview
            adapter = new RecyclerAdapter(ItemActivity.this, video_ids);
            recyclerView.setAdapter(adapter);
        }
    }

    public class GalleryImageAdapter extends BaseAdapter {
        private Context context;
        public GalleryImageAdapter(Context context){
            this.context = context;
        }
        @Override
        public int getCount() {
            return images_urls.length;
        }
        @Override
        public Object getItem(int position) {
            return position;
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ImageView imageView = new ImageView(context);
            new ImageDownloaderTask(imageView).execute(images_urls[position]);
            imageView.setPadding(5, 5, 5, 5);
            imageView.setLayoutParams(new Gallery.LayoutParams(250, 250));
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            return imageView;
        }
    }

}
