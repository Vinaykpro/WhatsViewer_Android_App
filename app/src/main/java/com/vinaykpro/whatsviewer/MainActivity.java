package com.vinaykpro.whatsviewer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;

import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public EditText editText,editmsgedittext,searchlayoutedittext;
    public TextView onlineStatus,selectedcount,editmsgupdate;
    public ImageView send,profilepic;
    View backBtn;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    public static List<String> messageList;

    public List<String> datesList;
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

    TextView topnotemain;

    String selectedDateFormat;

    String[] months = new String[] {"January","February","March","April","May","June","July","August","September","October","November","December"};

    ImageView exportChatCloseBtn;
    ConstraintLayout exportChatLayout;



    private InterstitialAd mInterstitialAd;



    //AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HomeActivity.loadinglayout.setVisibility(View.GONE);

        database = new MySqllite(MainActivity.this);

        Bundle extrasu = getIntent().getExtras();
        boolean noad = extrasu.getBoolean("noad",false);

        //MobileAds.initialize(MainActivity.this);

        /*mAdView = findViewById(R.id.adView2);
        AdRequest adRequestbanner = new AdRequest.Builder().build();
        mAdView.loadAd(adRequestbanner);*/


        if(!noad) {
            int x = (int)Math.floor(Math.random()*100);
            if(x<=60) {
                //showInterstitialAd();
            }
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ((ProgressBar)findViewById(R.id.progressBar2)).getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.white), PorterDuff.Mode.SRC_IN);
        }


        topnotemain = findViewById(R.id.topnotemain);

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


        // export chat layout

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
         datesList = database.getChatDates(tablename);
         initRecyclerView();
         //loadMessages();
        }

        if(database.isTableExists(tablename+"dates")) {
            selectedDateFormat = database.getProfilePicture(tablename);
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

        if(getNightMode()) {
            topnotemain.setTextColor(ResourcesCompat.getColor(getResources(),R.color.lightwhite,null));
        } else {
            topnotemain.setTextColor(ResourcesCompat.getColor(getResources(),R.color.verylightblack,null));
        }

        topnotemain.setTranslationY(-120f);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int total = linearLayoutManager.getItemCount();
                //int firstitem = linearLayoutManager.findFirstVisibleItemPosition();

                int lastitem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if(lastitem >= total-2) {
                    gotolast.setVisibility(View.GONE);
                } else {
                    gotolast.setVisibility(View.VISIBLE);
                }
            }


            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        try {
                            if (topnotemain.getVisibility() != View.GONE) {
                                topnotemain.setVisibility(View.VISIBLE);
                                topnotemain.animate().translationY(-120f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        topnotemain.setVisibility(View.GONE);
                                        topnotemain.animate().setListener(null);
                                    }
                                });
                            }
                        } catch (Exception e) {}
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        int firstitem;
                        String s;
                        String date = "Date not found";
                        try {
                            if (topnotemain.getVisibility() != View.VISIBLE) {
                                topnotemain.setVisibility(View.VISIBLE);
                                topnotemain.animate().translationY(0.0f).setInterpolator(new AnticipateOvershootInterpolator()).setDuration(500);
                            }
                            //Toast.makeText(MainActivity.this, "Scrolling", Toast.LENGTH_SHORT).show();
                            firstitem = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                            s = messageList.get(firstitem);
                            if (canigetDate(s)) {
                                date = getDate(s);
                                topnotemain.setText(getReadableDate(date));
                            }
                        } catch (Exception e) {

                        }
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        try {
                            firstitem = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                            s = messageList.get(firstitem);
                            date = "Date not found";
                            if (canigetDate(s)) {
                                date = getDate(s);
                                topnotemain.setText(getReadableDate(date));
                            }
                        } catch (Exception e) {}
                        break;
                }
            }
        });

        /*recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });*/

        /*recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

        });*/
    }

    public boolean getNightMode()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("SP",MODE_PRIVATE);
        return sharedPreferences.getBoolean("darkMode",false);
    }

    private boolean canigetDate(String s) {
        int commaindex = s.indexOf(",");
        boolean condition = false;
        if(commaindex > 0 && commaindex <= 10 && s.indexOf('/') <=9) {
            String date = s.substring(0,commaindex);
            String[] test = date.split("/");
            if (test.length == 3) {
                condition = true;
            }
        }
        return condition;
    }

    private String getDate(String s) {
        return s.substring(0,s.indexOf(","));
    }

    private String getReadableDate(String s) {
        String f = selectedDateFormat;
        String tempS = s;
        s = s.trim();
        if(f!=null) {
            f = f.trim();
            try {
                int monthIndex;
                int yearTemp;
                String day, month, year;
                List<String> dateValues = new ArrayList<>();
                dateValues.add(s.substring(0, s.indexOf("/")));
                s = s.substring(s.indexOf("/") + 1);
                dateValues.add(s.substring(0, s.indexOf("/")));
                dateValues.add(s.substring(s.indexOf("/") + 1));

                List<String> formatValues = new ArrayList<>();
                formatValues.add(f.substring(0, f.indexOf("/")));
                f = f.substring(f.indexOf("/") + 1);
                formatValues.add(f.substring(0, f.indexOf("/")));
                formatValues.add(f.substring(f.indexOf("/") + 1));

                day = dateValues.get(formatValues.indexOf("day"));
                monthIndex = Integer.parseInt((dateValues.get(formatValues.indexOf("month"))).trim());
                month = months[monthIndex - 1];
                yearTemp = Integer.parseInt((dateValues.get(formatValues.indexOf("year"))).trim());
                if (yearTemp < 100)
                    yearTemp += 2000;
                year = yearTemp + "";

                return day + " " + month + " " + year;
            } catch (Exception e) {
                return tempS;
            }
        } else {
            try {
                return getReadableDateOldChats(s);
            } catch (Exception e) {
                return s;
            }
        }
    }

    private String getReadableDateOldChats(String s) {

        int day = Integer.parseInt(s.substring(0,s.indexOf("/")));
        s = s.substring(s.indexOf("/")+1);
        int month = Integer.parseInt(s.substring(0,s.indexOf("/")));
        if(month > 12 && day <= 12) {
            /*useseconddateformat = true;
            changedatestillnow = true;*/
            int temp = month;
            month = day;
            day = temp;
        }/* else if (useseconddateformat && day <= 12) {
            int temp = month;
            month = day;
            day = temp;
        }*/
        s = s.substring(s.indexOf("/")+1);
        int year = Integer.parseInt(s);
        if(year<100) {
            /*if(!changedatestillnow) {
                readabledates.add(day + " " + months[month - 1] + " 20" + year);
            }*/
            return day + " " + months[month - 1] + " 20" + year;
        }
        else {
            /*if(!changedatestillnow) {
                readabledates.add(day + " " + months[month - 1] + " " + year);
            }*/
            return day + " " + months[month - 1] + " " + year;
        }
    }

    public void loadInterstitialAd() {
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
                                //mInterstitialAd = loadInterstitialAd();
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

        // exportChat layout views
        TextView usernamepreviewexportchat,senderName,recieverName,exportChatToHtmlBtn;
        Switch showSenderName,showRecieverName;
        ImageView senderNameTick,chatNameTick,lightModeIcon,DarkModeIcon;
        RadioButton lightModeRadioBtn,darkModeRadioBtn;
        ConstraintLayout lightModeBg,darkModeBg,senderNameEditLayout,showSenderNameBg,showRecieverNameBg;
        EditText senderNameEditText,chatNameEditText;

        View v2 = findViewById(R.id.include2);
        usernamepreviewexportchat = (TextView) v2.findViewById(R.id.textView);
        senderName = findViewById(R.id.textView26);
        recieverName = findViewById(R.id.textView30);
        showSenderName = findViewById(R.id.switch2);
        showRecieverName = findViewById(R.id.switch12);
        senderNameTick = findViewById(R.id.imageView33);
        chatNameTick = findViewById(R.id.imageView30);
        lightModeIcon = findViewById(R.id.imageView31);
        DarkModeIcon = findViewById(R.id.imageView32);
        lightModeRadioBtn = findViewById(R.id.radioButton);
        darkModeRadioBtn = findViewById(R.id.radioButton2);
        lightModeBg = findViewById(R.id.constraintLayout14);
        darkModeBg = findViewById(R.id.constraintLayout13);
        senderNameEditLayout = findViewById(R.id.sendernamelayout);
        showSenderNameBg = findViewById(R.id.constraintLayout12);
        showRecieverNameBg = findViewById(R.id.constraintLayout11);
        senderNameEditText = findViewById(R.id.editTextText3);
        chatNameEditText = findViewById(R.id.editTextText);
        exportChatToHtmlBtn  = findViewById(R.id.watchadbutton);

        exportChatLayout = findViewById(R.id.mainexportchatlayout);
        exportChatCloseBtn  = findViewById(R.id.imageView28);

        recyclerView = findViewById(R.id.backg);
        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new Adapter(messageList,tablename,fname,sname,myname,this,chatmenulayout,backbtn,edit,copy,delete,info,selectedcount,editmessagelayout,editmsgbackbtn,editmsgedittext,editmsgupdate,searchlayout,searchlayoutbackbtn,searchlayoutedittext,searchlayoutupbutton,searchlayoutdownbutton,linearLayoutManager,usernamepreviewexportchat,senderName,recieverName,showSenderName,showRecieverName,senderNameTick,chatNameTick,lightModeIcon,DarkModeIcon,lightModeRadioBtn,darkModeRadioBtn,lightModeBg,darkModeBg,senderNameEditLayout,senderNameEditText,chatNameEditText,exportChatLayout,exportChatCloseBtn,showSenderNameBg,showRecieverNameBg,exportChatToHtmlBtn,datesList);
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
        } else if(exportChatLayout.getTranslationY()==0) {
            exportChatCloseBtn.callOnClick();
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



