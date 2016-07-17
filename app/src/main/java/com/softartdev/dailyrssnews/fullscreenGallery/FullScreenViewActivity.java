package com.softartdev.dailyrssnews.fullscreenGallery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.softartdev.dailyrssnews.R;

public class FullScreenViewActivity extends Activity{

    private FullScreenImageAdapter adapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_view);

        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);
        String[] images_urls = i.getStringArrayExtra("images urls");

        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new FullScreenImageAdapter(FullScreenViewActivity.this, images_urls);
        viewPager.setAdapter(adapter);

        // displaying selected image first
        viewPager.setCurrentItem(position);
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
