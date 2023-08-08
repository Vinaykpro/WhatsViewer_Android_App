package com.vinaykpro.whatsviewer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.viewpager2.widget.ViewPager2;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    public EditText editText,editmsgedittext,searchlayoutedittext;
    public TextView onlineStatus,selectedcount,editmsgupdate;
    public ImageView send,profilepic;
    View backBtn;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    public static List<String> messageList;
    Adapter adapter;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String fname,sname,lastseen="online";
    String myname = "Noname";
    TextView username,onlinestatus;
    View v;
    String tablename;
    ConstraintLayout chatmenulayout,editmessagelayout,searchlayout;
    LinearLayout profilebar;
    ImageView backbtn,edit,copy,delete,info,editmsgbackbtn,searchlayoutbackbtn,searchlayoutupbutton,searchlayoutdownbutton,gotolast;
    MySqllite database;
    int usercount;

    private InterstitialAd mInterstitialAd;

    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new MySqllite(MainActivity.this);

        Bundle extrasu = getIntent().getExtras();
        boolean noad = extrasu.getBoolean("noad",false);

        MobileAds.initialize(MainActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView2);
        AdRequest adRequestbanner = new AdRequest.Builder().build();
        mAdView.loadAd(adRequestbanner);


        if(!noad) {
            int x = (int)Math.floor(Math.random()*100);
            if(x<=65) {
                showInterstitialAd();
            }
        }



        recyclerView = findViewById(R.id.backg);
        Objects.requireNonNull(getSupportActionBar()).hide();
        onlineStatus = findViewById(R.id.onlinestatus);
        editText = findViewById(R.id.editTextid);
        chatmenulayout = findViewById(R.id.chatmenulayout);

        backbtn = findViewById(R.id.imageView5);
        selectedcount = findViewById(R.id.textView2);
        edit = findViewById(R.id.imageView6);
        copy = findViewById(R.id.imageView);
        delete = findViewById(R.id.imageView2);
        info = findViewById(R.id.imageView4);

        editmessagelayout = findViewById(R.id.editmessagelayout);
        editmsgbackbtn = findViewById(R.id.imageView7);
        editmsgedittext = findViewById(R.id.editmessage);
        editmsgupdate = findViewById(R.id.textView9);

        searchlayout = findViewById(R.id.searchlayout);
        searchlayoutbackbtn = findViewById(R.id.searchlayoutbackbtn);
        searchlayoutedittext = findViewById(R.id.searchmessage);
        searchlayoutupbutton = findViewById(R.id.imageView9);
        searchlayoutdownbutton = findViewById(R.id.imageView10);

        gotolast = findViewById(R.id.gotolastmessage);

        searchlayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        editmessagelayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        if(HomeActivity.isfilevalid) {
        //messageList = HomeActivity.messageList;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fname = extras.getString("fname");
            sname = extras.getString("sname");
            myname = extras.getString("zname");
            lastseen = extras.getString("lastseen");
            tablename = extras.getString("tablename");
        }
        lastseen = database.getlastseen(tablename);

        if (myname == null) {
            myname = "No name";
        }
        if(lastseen == null) {
            lastseen = "online";
        }

        v = findViewById(R.id.include).getRootView();
        profilepic = (ImageView) v.findViewById(R.id.mainactivityprofilepic);
        username = (TextView) v.findViewById(R.id.textView);
        profilebar = (LinearLayout) v.findViewById(R.id.linearLayout2);
        onlineStatus = (TextView) v.findViewById(R.id.onlinestatus);

        onlineStatus.setText(lastseen);

        username.setText(myname);
         messageList = database.getChatData(tablename);
         initRecyclerView();
            //loadMessages();
        }

        ImageView callbtn = (ImageView) findViewById(R.id.voice_call);
        View backBtn = (View) findViewById(R.id.chat_back_button);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try { database.updatelastleftmessageindex(tablename,linearLayoutManager.findFirstCompletelyVisibleItemPosition()); } catch(Exception e) {};
                finish();
            }
        });

        send = findViewById(R.id.send);

        usercount = database.getusercount(tablename);

        profilebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,ProfileActivity.class);
                i.putExtra("tablename",tablename);
                i.putExtra("username",myname);
                i.putExtra("lastseen",lastseen);
                i.putExtra("usercount",usercount);
                startActivity(i);
            }
        });
        updatedata();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int total = linearLayoutManager.getItemCount();
                int lastitem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if(lastitem >= total-2) {
                    gotolast.setVisibility(View.GONE);
                } else {
                    gotolast.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void showInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(MainActivity.this,"ca-app-pub-2813592783630195/9571135356", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        mInterstitialAd.show(MainActivity.this);
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdClicked() {
                                // Called when a click is recorded for an ad.
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                mInterstitialAd = null;
                            }
                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                mInterstitialAd = null;
                            }

                            @Override
                            public void onAdImpression() {
                                // Called when an impression is recorded for an ad.
                                //Toast.makeText(HomeActivity.this, "Ad got impression", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                //Toast.makeText(HomeActivity.this, "Ad showed full screen content", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                    }
                });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initRecyclerView() {
        recyclerView = findViewById(R.id.backg);
        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new Adapter(messageList,tablename,fname,sname,this,chatmenulayout,backbtn,edit,copy,delete,info,selectedcount,editmessagelayout,editmsgbackbtn,editmsgedittext,editmsgupdate,searchlayout,searchlayoutbackbtn,searchlayoutedittext,searchlayoutupbutton,searchlayoutdownbutton,linearLayoutManager);
        recyclerView.setAdapter(adapter);
        ((SimpleItemAnimator) Objects.requireNonNull(recyclerView.getItemAnimator())).setSupportsChangeAnimations(false);
        adapter.notifyDataSetChanged();
        int lastmessageindex = database.getlastleftmessageindex(tablename);
        if(!(lastmessageindex > adapter.getItemCount()-1 || lastmessageindex < 0)) {
            linearLayoutManager.scrollToPositionWithOffset(lastmessageindex,5);
        }
    }

    @Override
    public void onBackPressed() {
        try { database.updatelastleftmessageindex(tablename,linearLayoutManager.findFirstCompletelyVisibleItemPosition()); } catch(Exception e) {};
        if(Adapter.count > 0 || Adapter.issearching) {
            backbtn.callOnClick();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        myname = database.getname(tablename);
        lastseen = database.getlastseen(tablename);

        username.setText(myname);
        onlineStatus.setText(lastseen);

        updatedata();
        super.onResume();
    }

    public void updatedata() {
        try {
                File f = new File(getApplicationContext().getCacheDir()+"/"+tablename+"dp.png");
                if(f.exists()) {
                    //Toast.makeText(this, Uri.fromFile(f)+"", Toast.LENGTH_SHORT).show();
                    Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                    profilepic.setImageBitmap(bitmap);
                } else {
                    if(usercount > 2) {
                        profilepic.setImageResource(R.drawable.groupdefaultdp);
                    } else {
                        profilepic.setImageResource(R.drawable.userdefaultdp);
                    }
                }
        } catch(Exception e) {
            Toast.makeText(this, e+"", Toast.LENGTH_SHORT).show();
        }
    }
}



