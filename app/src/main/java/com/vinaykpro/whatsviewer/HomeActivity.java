package com.vinaykpro.whatsviewer;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.icu.util.Output;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    int STORAGE_READ_PERMISSION_CODE = 102;
    int STORAGE_WRITE_PERMISSION_CODE = 103;
    HomeFragmentAdapter adapter;
    FloatingActionButton addtextfilebutton;
    public static boolean isfilevalid = false;
    public static List<String> messageList = new ArrayList<>();
    String[] months = new String[] {"January","February","March","April","May","June","July","August","September","October","November","December"};
    public List<String> readabledates;
    public boolean changedatestillnow = false;
    public boolean useseconddateformat = false;
    String firstName,secondName = "",tablename;
    public static List<String> names;
    ConstraintLayout loadinglayout;
    Intent data;
    Uri uri;
    File f;
    String ss = "";
    public boolean isinterstitialadfinished = false;
    public boolean ischataddedandreadytolaunch = false;
    public boolean isintentfresh = true;
    public String temp = "";
    public String temp0 = "";
    public String date = "";
    public boolean istempavailable = false;
    public boolean istemp0available = false;
    public boolean isInNightMode = false;

    ImageView imgview;
    public String pathu = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/ic_fire.png";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.home_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_search:
                break;
            case R.id.switch_modes:
                SharedPreferences sharedPreferences = getSharedPreferences("SP",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if(isInNightMode) {
                    editor.putBoolean("darkMode",false);
                    editor.apply();
                    int c = AppCompatDelegate.getDefaultNightMode();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    editor.putBoolean("darkMode",true);
                    editor.apply();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                break;
            case R.id.clear_all:
                Toast.makeText(HomeActivity.this, "Please give your feedback about this app in the Google Play Store", Toast.LENGTH_SHORT).show();
                break;
            /*case R.id.settings:
                Toast.makeText(HomeActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                break;*/
        }
        return super.onOptionsItemSelected(item);
    }

    FragmentManager fm;
    AdView mAdView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /*File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        String sx = "Something went wrong";
        for (String name : Objects.requireNonNull(f.list())) {
            sx += "\n"+name;
        }
        mainfiles.setText(sx);*/
        /*SharedPreferences sharedPreferences = getSharedPreferences("SP",MODE_PRIVATE);*/
        /*pathu = Environment.getExternalStorageDirectory().getAbsolutePath()+"/whatsapp/images/profile.png";
        imgview.setImageURI(Uri.fromFile(new File(pathu)));*/

        /*if(Build.VERSION.SDK_INT > 29) {
            Intent i = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,Uri.fromParts("package",getPackageName(),null));
            startActivity(i);
        }*/

        MobileAds.initialize(HomeActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequestbanner = new AdRequest.Builder().build();
        mAdView.loadAd(adRequestbanner);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });
        //Toast.makeText(this, Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/attrs.xml", Toast.LENGTH_SHORT).show();
        /*File fn = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/WhatsViewer/files");
        if(isWriteStoragePermissionGranted()) {
            if (fn.mkdirs()) {
                Toast.makeText(this, "Folder created", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Folder not created", Toast.LENGTH_SHORT).show();
            }
        }*/


        /*if(f.exists())
        {
            Toast.makeText(this, "Exists", Toast.LENGTH_SHORT).show();
            if(f.delete()) {Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();}
        } else {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(this, "Not Exists", Toast.LENGTH_SHORT).show();
        }*/

        if(getNightMode())
        {
            isInNightMode = true;
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            isInNightMode = false;
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        Objects.requireNonNull(getSupportActionBar()).setElevation(0.0f);
        MySqllite database = new MySqllite(this);

        loadinglayout = findViewById(R.id.loadinglayout);
        addtextfilebutton = findViewById(R.id.floatingActionButton3);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager2);
        fm = getSupportFragmentManager();
        adapter = new HomeFragmentAdapter(fm,getLifecycle());
        viewPager2.setAdapter(adapter);
        names = new ArrayList<>();
        readabledates = new ArrayList<>();

        loadinglayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        addtextfilebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent()
                        .setType("text/plain")
                        .setAction(Intent.ACTION_GET_CONTENT);
                textFileOpener.launch(intent);
            }
        });

//        File f = new File(Environment.getDataDirectory().toURI());
//        try {
//            f.createNewFile();
//        } catch ( Exception e) {
//            Toast.makeText(this, e+"", Toast.LENGTH_SHORT).show();
//        }

        Intent intent = getIntent();
        if(intent != null && isintentfresh) {
            isReadStoragePermissionGranted();
            String action = intent.getAction();
            String type = intent.getType();
            Bundle extras = intent.getExtras();
            tablename = generateRandomTableName(10);
            if(Intent.ACTION_VIEW.equals(action)) {
                uri = intent.getData();
                getIntent().removeExtra("key");
                checkNopen(uri);
                loadInterstitialAd();
            } else if(Intent.ACTION_SEND.equals(action) && type!=null) {
                if(type.equalsIgnoreCase("text/plain")) {
                    uri = (Uri) extras.get(Intent.EXTRA_STREAM);
                    getIntent().removeExtra("key");
                    checkNopen(uri);
                    loadInterstitialAd();
                } else {
                    uri = (Uri) extras.get(Intent.EXTRA_STREAM);
                    String s = extras.get(Intent.EXTRA_STREAM).toString();
                    if(s.contains(".")) {
                        s = s.substring(s.lastIndexOf(".")+1);
                    }
                    if(s.equals("txt")) {
                        getIntent().removeExtra("key");
                        checkNopen(uri);
                        loadInterstitialAd();
                    } else {
                        tablename = null;
                        Toast.makeText(HomeActivity.this, "Unsupported file format", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if(Intent.ACTION_SEND_MULTIPLE.equals(action) && type !=null) {
                ArrayList<Uri> uriList = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
                ArrayList<String> filenames = new ArrayList<>();
                String s = uriList.get(0).getPath();
                String tempToast = "";

                for (Uri u : uriList) {
                    filenames.add(getFileName(u));
                }
                for (int i = 0; i < uriList.size(); i++) {
                    Uri u = uriList.get(i);
                    String fileName = filenames.get(i);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                        //contentValues.put(MediaStore.MediaColumns.MIME_TYPE,);

                        Uri contentUri;
                        //contentValues.put(MediaStore.MediaColumns.DATA, Environment.getExternalStorageDirectory().getAbsolutePath() + "WhatsViewer/Chats/" + tablename);
                        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/WhatsViewer/Chats/" + tablename);
                        contentUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;

                        ContentResolver resolver = getContentResolver();
                        Uri insertUri = resolver.insert(contentUri, contentValues);

                        try {
                            InputStream inputStream = resolver.openInputStream(u);
                            OutputStream outputStream = resolver.openOutputStream(insertUri);

                            if (inputStream != null && outputStream != null) {
                                byte[] buffer = new byte[4096];
                                int read;
                                while ((read = inputStream.read(buffer)) != -1) {
                                    outputStream.write(buffer, 0, read);
                                }
                                inputStream.close();
                                outputStream.close();
                            }

                        } catch (Exception e) {
                            Toast.makeText(this, e + "", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (isWriteStoragePermissionGranted()) {
                            new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"/WhatsViewer/chats/" + tablename + "/").mkdirs();
                            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"/WhatsViewer/chats/" + tablename + "/" + fileName);
                            try {
                                InputStream inputStream = getContentResolver().openInputStream(u);
                                OutputStream outputStream = new FileOutputStream(file);

                                byte[] buffer = new byte[4096];
                                int read;
                                while ((read = inputStream.read(buffer)) != -1) {
                                    outputStream.write(buffer, 0, read);
                                }

                                inputStream.close();
                                outputStream.close();

                            } catch (Exception e) {
                                Toast.makeText(this, e + "", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(HomeActivity.this, "Please grant write enternal storage permission", Toast.LENGTH_SHORT).show();
                        }
                    }
            }


                //Toast.makeText(this, tempToast, Toast.LENGTH_SHORT).show();
                if (s.contains(".")) {
                    s = s.substring(s.lastIndexOf(".")+1);
                }
                if(s.equals("txt") || uriList.get(0).getPath().contains("export_chat")) {
                    getIntent().removeExtra("key");
                    checkNopen(uriList.get(0));
                    loadInterstitialAd();
                } else {
                    tablename = null;
                    Toast.makeText(HomeActivity.this, "Unsupported files", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String getFileName(Uri uri)
    {
        Cursor cursor = getContentResolver().query(uri,null,null,null,null);
        cursor.moveToFirst();
        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        String fileName = cursor.getString(nameIndex);
        cursor.close();
        return fileName;
    }

    ActivityResultLauncher<Intent> textFileOpener = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == Activity.RESULT_OK) {
                data = result.getData();
                uri = data.getData();
                /*String filename = null;
                if(uri.getScheme().equals("content")) {
                    Cursor cursor = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        cursor = getContentResolver().query(uri,new String[] {OpenableColumns.DISPLAY_NAME},null,null);
                    try {
                        if(cursor !=null && cursor.moveToFirst()) {
                            filename = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        }
                    } catch (Exception e) {
                        Toast.makeText(HomeActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    } finally {
                        if(cursor!=null)
                         cursor.close();
                    }
                    }
                }
                Toast.makeText(HomeActivity.this, filename, Toast.LENGTH_SHORT).show();*/
                checkNopen(uri);
                loadInterstitialAd();
            }
        }
    });

    public void loadInterstitialAd() {
        isinterstitialadfinished = false;
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this,"ca-app-pub-2813592783630195/9571135356", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        mInterstitialAd.show(HomeActivity.this);
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
                                isinterstitialadfinished = true;
                                if(ischataddedandreadytolaunch) {
                                    //ischataddedandreadytolaunch = false;
                                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                                    intent.putExtra("fname", firstName);
                                    intent.putExtra("sname", secondName);
                                    intent.putExtra("zname", firstName);
                                    intent.putExtra("tablename", tablename);
                                    startActivity(intent);
                                    firstName = "";
                                    secondName = "";
                                }
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                mInterstitialAd = null;
                                isinterstitialadfinished = true;
                                if(ischataddedandreadytolaunch) {
                                    //ischataddedandreadytolaunch = false;
                                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                                    intent.putExtra("fname", firstName);
                                    intent.putExtra("sname", secondName);
                                    intent.putExtra("zname", firstName);
                                    intent.putExtra("tablename", tablename);
                                    startActivity(intent);
                                    firstName = "";
                                    secondName = "";
                                }
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
                        isinterstitialadfinished = true;
                        if(ischataddedandreadytolaunch) {
                                //ischataddedandreadytolaunch = false;
                                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                                intent.putExtra("fname", firstName);
                                intent.putExtra("sname", secondName);
                                intent.putExtra("zname", firstName);
                                intent.putExtra("tablename", tablename);
                                startActivity(intent);
                                firstName = "";
                                secondName = "";
                        }
                    }
                });
    }

    public void checkNopen(Uri uri) {
        {
            //f = new File(uri.toString());
            messageList.clear();
            ss = "";
            loadinglayout.setVisibility(View.VISIBLE);
            ischataddedandreadytolaunch = false;
            String filename = null;
            if(uri.getScheme().equals("content")) {
                Cursor cursor = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    cursor = getContentResolver().query(uri,new String[] {OpenableColumns.DISPLAY_NAME},null,null);
                    try {
                        if(cursor !=null && cursor.moveToFirst()) {
                            filename = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)+0);
                        }
                    } catch (Exception e) {
                        Toast.makeText(HomeActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    } finally {
                        if(cursor!=null)
                            cursor.close();
                    }
                }
            }
            Toast.makeText(HomeActivity.this, filename, Toast.LENGTH_SHORT).show();
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                     try {
                        InputStream in = getContentResolver().openInputStream(uri);
                        InputStreamReader inr = new InputStreamReader(in);
                        BufferedReader reader = new BufferedReader(inr);
                        int checkvalidity = 0;
                        /*temp = "";
                        temp0 = "";
                        date = "";
                        istempavailable = false;
                        istemp0available = false;*/
                        boolean isfirst = true;

                        names.clear();

                        while (reader.ready()) {
                            ss = reader.readLine();
                            if (canigetMessage(ss) && canigetName(ss) && canigetTime(ss)) {
                                if (!(names.contains(getName(ss)))) {
                                    names.add(getName(ss));
                                }
                                if (names.size() >= 1) {
                                    isfilevalid = true;
                                }
                                if (istempavailable) {
                                    if ((date.equals("") && canigetDate(temp)) || (canigetDate(temp) && !(getDate(temp).equals(date)))) {
                                        date = getDate(temp);
                                        messageList.add(getReadableDate(date));
                                    }
                                    messageList.add(temp);
                                    istempavailable = false;
                                    temp = "";
                                } else {
                                    if (!(temp0.equals(""))) {
                                        if ((date.equals("") && canigetDate(temp0)) || (canigetDate(temp0) && !(getDate(temp0).equals(date)))) {
                                            date = getDate(temp0);
                                            messageList.add(getReadableDate(date));
                                        }
                                        messageList.add(temp0);
                                        istemp0available = false;
                                    }
                                }
                                istemp0available = true;
                                temp0 = ss;
                                //isfilevalid = true;
                                temp = ss;
                            } else if (canigetTime(ss)) {
                                if (istemp0available) {
                                    if ((date.equals("") && canigetDate(temp0)) || (canigetDate(temp0) && !(getDate(temp0).equals(date)))) {
                                        date = getDate(temp0);
                                        messageList.add(getReadableDate(date));
                                    }
                                    messageList.add(temp0);
                                    istemp0available = false;
                                }

                                if ((date.equals("") && canigetDate(ss)) || (canigetDate(ss) && !(getDate(ss).equals(date)))) {
                                    date = getDate(ss);
                                    messageList.add(getReadableDate(date));
                                }
                                try {
                                    ss = ss.substring(ss.indexOf("-") + 2);
                                } catch (Exception e) {
                                }
                                messageList.add(ss);
                            } else {
                                istempavailable = true;
                                temp += "\n" + ss;
                            }

                            if (date.equals("") && canigetDate(ss)) {
                                date = getDate(ss);
                            }

                            checkvalidity++;
                            if (checkvalidity >= 30 && !isfilevalid) {
                                messageList.clear();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadinglayout.setVisibility(View.GONE);
                                        Toast.makeText(HomeActivity.this, "Invalid File", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;
                            }
                        }

                        if (istempavailable) {
                            messageList.add(temp);
                            istempavailable = false;
                        } else if (!(temp0.equals(""))) {
                            messageList.add(temp0);
                            istemp0available = false;
                        }
                        if (isfilevalid) {
                            prompt();
                            MySqllite sqllite = new MySqllite(HomeActivity.this);
                            //sqllite.addText(firstName);
                            if(tablename==null)
                            { tablename = generateRandomTableName(10); }
                            String templastseen = "online";
                            firstName = names.get(0);
                            if (names.size() == 2) {
                                secondName = names.get(1);
                            } else {
                                secondName = "";
                                for (int i = 0; i < names.size(); i++) {
                                    if (i == names.size() - 1) {
                                        secondName += names.get(i);
                                    } else {
                                        secondName += names.get(i) + "\n";
                                    }
                                }
                                if(names.size() != 1) {
                                    templastseen = "tap here for group info";
                                }
                            }

                            boolean t = sqllite.addText(firstName, tablename, firstName, secondName, firstName, names.size(), templastseen, "", "", null, 0,0);
                            /*if(changedatestillnow) {
                                for (int i=0;i<readabledates.size();i++) {
                                    messageList.set(messageList.indexOf(readabledates.get(i)),getFlippedDate(readabledates.get(i)));
                                }
                                readabledates.clear();
                                useseconddateformat = false;
                                changedatestillnow = false;
                            }*/
                            sqllite.addChatToContact(tablename, messageList);
                            ischataddedandreadytolaunch = true;
                            if(isinterstitialadfinished) {
                                isinterstitialadfinished = false;
                                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                                intent.putExtra("fname", firstName);
                                intent.putExtra("sname", secondName);
                                intent.putExtra("zname", firstName);
                                intent.putExtra("tablename", tablename);
                                ////startActivity(intent);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivity(intent);
                                    }
                                });
                                firstName = "";
                                secondName = "";
                            }
                        }
                    } catch (Exception e) {
                        isfilevalid = false;
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadinglayout.setVisibility(View.GONE);
                                Toast.makeText(HomeActivity.this, e + "", Toast.LENGTH_SHORT).show();
                            }
                        });
                        //throw new RuntimeException(e);
                     }
                }
            });
            t1.start();
        }
    }

    private void prompt() {
    }

    private boolean canigetMessage(String s) {
        int a = s.indexOf("-");
        int b = s.indexOf(":",a);
        return a != -1 && b != -1;
    }

    private boolean canigetName(String s) {
        int a = s.indexOf("-");
        int b = s.indexOf(":",a);
        return a != -1 && b != -1;
    }

    private boolean canigetTime(String s) {
        int a = s.indexOf(",");
        int b = s.indexOf("-",a);
        return (a!=-1 && b!=-1);
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

    public String getFlippedDate(String s) {
        String[] date = s.split(" ");
        int day = Integer.parseInt(date[0]);
        int month = getMonthIndex(date[1]);

        return getReadableDate(month+"/"+day+"/"+date[2]);
    }

    public int getMonthIndex(String s) {
        int index = 1;
        for (int i=0;i< months.length;i++) {
            if(s.equals(months[i])) {
                index = i+1;
            }
        }
        return index;
    }

    private String getMessage(String s) {
        int a = s.indexOf("-");
        int b = s.indexOf(":",a);
        return s.substring(b+2);

    }

    private String getName(String s) {
        int a = s.indexOf("-");
        int b = s.indexOf(":",a);

        return s.substring(a+2,b);

    }

    public static String generateRandomTableName(int len)
    {
        // ASCII range – alphanumeric (0-9, a-z, A-Z)
        final String chars = "abcdefghijklmnopqrstuvwxyz";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        // each iteration of the loop randomly chooses a character from the given
        // ASCII range and appends it to the `StringBuilder` instance

        for (int i = 0; i < len; i++)
        {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }

        return sb.toString();
    }

    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 103);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 102) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //resume tasks needing this permission
            } else {

            }

            /*case 103:
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    //resume tasks needing this permission
                }else{
                    finish();
                }
                break;*/
        }
    }

    public boolean getNightMode()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("SP",MODE_PRIVATE);
        return sharedPreferences.getBoolean("darkMode",false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ischataddedandreadytolaunch) {
            loadinglayout.setVisibility(View.GONE);
        }
    }

    /*ActivityResultLauncher<Intent> mediaOpener = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == Activity.RESULT_OK) {
                Intent i = result.getData();
                Uri uri = Objects.requireNonNull(i).getData();
                pathu = uri.toString();
                Toast.makeText(HomeActivity.this,pathu,Toast.LENGTH_SHORT).show();
                imgview.setImageURI(Uri.parse(pathu));
                *//*Share dPreferences sharedPreferences = getSharedPreferences("SP",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("path",pathu);
                editor.apply();*//*
            }
        }
    });*/

}