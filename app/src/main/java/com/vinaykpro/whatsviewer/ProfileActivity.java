package com.vinaykpro.whatsviewer;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Objects;
import java.util.Random;

public class ProfileActivity extends AppCompatActivity {

    EditText name,number,onlinestatus;
    ImageView backbtn;
    TextView blockname,reportname;
    String tablename,sname,sonlinestatus;
    ImageView profilepic;
    MySqllite database;
    int usercount;
    public static Uri dp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Objects.requireNonNull(getSupportActionBar()).hide();

        database = new MySqllite(this);
        profilepic = findViewById(R.id.profile);
        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
        onlinestatus = findViewById(R.id.textView10);
        blockname = findViewById(R.id.blockname);
        reportname = findViewById(R.id.reportname);
        backbtn = findViewById(R.id.imageView18);

        Bundle extras = getIntent().getExtras();
        tablename = extras.getString("tablename");
        sname = extras.getString("username");
        sonlinestatus = extras.getString("lastseen");
        usercount = extras.getInt("usercount");

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        try {
                File f = new File(getApplicationContext().getCacheDir()+"/"+tablename+"dp.png");
                if(f.exists()) {
                    profilepic.setImageURI(Uri.fromFile(f));
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

        name.setText(sname);
        onlinestatus.setText(sonlinestatus);
        blockname.setText("Block "+sname);
        reportname.setText("Report "+sname);

        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                mediaOpener.launch(gallery);
            }
        });

        profilepic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                File f = new File(getApplicationContext().getCacheDir()+"/"+tablename+"dp.png");
                if(f.exists()) { f.delete();
                profilepic.setImageResource(R.drawable.userdefaultdp);
                    Toast.makeText(ProfileActivity.this, "Profile Picture Removed Successfully", Toast.LENGTH_SHORT).show(); }
                return true;
            }
        });

    }

    ActivityResultLauncher<Intent> mediaOpener = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == Activity.RESULT_OK) {
                Intent i = result.getData();
                Uri uri = Objects.requireNonNull(i).getData();
                Bitmap bitmap = null;
                ContentResolver contentResolver = getContentResolver();
                try {
                    if(Build.VERSION.SDK_INT < 28) {
                        bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri);
                    } else {
                        ImageDecoder.Source source = ImageDecoder.createSource(contentResolver, uri);
                        bitmap = ImageDecoder.decodeBitmap(source);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                OutputStream fos;
                String sd = "";
                File dest = null;

                try {
                    dest = new File(getApplicationContext().getCacheDir(),tablename+"dp.png");
                    fos = new FileOutputStream(dest);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, fos);
                    fos.flush();
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ProfileActivity.this,e+"",Toast.LENGTH_SHORT).show();
                }
                profilepic.setImageBitmap(bitmap);
            }
        }
    });

    /**/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        database.updatechatname(tablename,name.getText().toString());
        database.updatelastseen(tablename,onlinestatus.getText().toString());
        //database.update
    }

    @Override
    protected void onPause() {
        database.updatechatname(tablename,name.getText().toString());
        database.updatelastseen(tablename,onlinestatus.getText().toString());
        super.onPause();
    }
}