package com.vinaykpro.whatsviewer;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;

public class TextFileSelector extends AppCompatActivity {

    Button chooseBtn;
    ImageView swapButton;
    TextView senderName;
    TextView recieverName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_file_selector);

        Objects.requireNonNull(getSupportActionBar()).hide();

        chooseBtn = findViewById(R.id.choose_btn);
        swapButton = findViewById(R.id.swap_names_btn);
        senderName = findViewById(R.id.sender_name);
        recieverName = findViewById(R.id.reciever_name);


        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent()
                        .setType("text/plain")
                        .setAction(Intent.ACTION_GET_CONTENT);
                textFileOpener.launch(intent);

            }
        });




    }

    ActivityResultLauncher<Intent> textFileOpener = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                Uri uri = data.getData();
                File f = new File(uri.toString());
                String ss = "";




                try
                {
                    InputStream in = getContentResolver().openInputStream(uri);
                    InputStreamReader inr = new InputStreamReader(in);
                    BufferedReader reader = new BufferedReader(inr);

                    int i = 1;
                    while (i<=2) {
                        i++;
                        ss += reader.readLine()+"\n\n\n";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(TextFileSelector.this, e+"", Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(TextFileSelector.this, ss, Toast.LENGTH_SHORT).show();
                //senderName.setText((Environment.getExternalStorageDirectory().getPath()+" and "+uri.getLastPathSegment()));
            }
        }
    });



}