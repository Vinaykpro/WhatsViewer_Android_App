package com.vinaykpro.whatsviewer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MediaViewerActivity extends AppCompatActivity {

    VideoView videoView;
    SeekBar seekBar;
    TextView fullduration,currentduration;
    ViewPager2 mediaViewPager;
    MediaFragmentsAdapter adapter;
    ConstraintLayout toolbarlayout;
    ImageView mainimage;
    List<String> mediaList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //View decorView = getWindow().getDecorView();

        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_media_viewer);
        toolbarlayout = findViewById(R.id.mediaviewertoolbar);
        mediaList = new ArrayList<>();
        Bundle b = getIntent().getExtras();
        String path = b.getString("path");
        mediaList.add(path);
        mediaList.add("storage/emulated/0/Download/aditya.mp4");
        mediaList.add("storage/emulated/0/Download/song.mp3");
        mediaList.add("storage/emulated/0/Download/Testimg.jpg");
        mediaViewPager = findViewById(R.id.mediaviewpager);
        mainimage = findViewById(R.id.imageView26);
        FragmentManager fm = getSupportFragmentManager();
        adapter = new MediaFragmentsAdapter(fm,getLifecycle(),this,mediaList,mediaViewPager);
        mediaViewPager.setAdapter(adapter);
        mediaViewPager.setPageTransformer(new DepthPageTransformer());
        Glide.with(this).load(new File(path)).into(mainimage);

        mediaViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
           @Override
            public void onPageSelected(int position) {
               super.onPageSelected(position);
               adapter.notifyDataSetChanged();
            }
        });
    }

    public String convertToMMSS(String duration)
    {
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis)%TimeUnit.MINUTES.toSeconds(1));
    }
    public void hideToolbar()
    {
        toolbarlayout.animate().alpha(0.0f);
    }

    public void showToolbar()
    {
        toolbarlayout.animate().alpha(1.0f);
    }

    public void ToggleFullScreenMode() {
        if(toolbarlayout.getAlpha() != 0.0f) hideToolbar(); else showToolbar();
    }

    /*public void ToggleFullScreenMode() {
        View decorView = getWindow().getDecorView();
        if (isInFullscreen()) {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        } else {
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public boolean isInFullscreen() {
        View decorView = getWindow().getDecorView();
        return ((decorView.getSystemUiVisibility() & View.SYSTEM_UI_FLAG_FULLSCREEN) | (decorView.getSystemUiVisibility() & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)) != 0;
    }*/
}

