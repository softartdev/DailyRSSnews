package com.softartdev.dailyrssnews.videoYoutubeList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.softartdev.dailyrssnews.R;

public class VideoListActivity extends Activity {
    String[] video_ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        Intent intent = getIntent();
        video_ids = intent.getStringArrayExtra("video_ids");

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.video_list);
        recyclerView.setHasFixedSize(true);
        RecyclerAdapter adapter=new RecyclerAdapter(VideoListActivity.this, video_ids);
        recyclerView.setAdapter(adapter);
        //to use RecycleView, you need a layout manager. default is LinearLayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
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
}
