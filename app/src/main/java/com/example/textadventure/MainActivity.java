package com.example.textadventure;

import android.content.Intent;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;


public class MainActivity extends AppCompatActivity {
    Button btn_start, btn_import;

    public static final String MY_PREFS = "prefs";
    public static final String MY_KEY = "pos";
    public static final String ITEM_KEY = "item";
    public static final String USER_ITEMS = "user_items";
    public static final String FLAG_DOORS = "flags";
    SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         setContentView(R.layout.activity_main);
        setupControls();

    }

    private void setupControls(){
        btn_start = findViewById(R.id.btn_start);
        btn_import = findViewById(R.id.btn_load);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), game_window.class);
                startActivity(intent);

            }
        });
        btn_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), game_window.class);

                sharedPrefs = getSharedPreferences(MY_PREFS, MODE_PRIVATE);
                int pos = sharedPrefs.getInt(MY_KEY, 0);

                intent.putExtra(MainActivity.MY_KEY,Integer.toString(pos));
                intent.putExtra(MainActivity.ITEM_KEY,sharedPrefs.getString(ITEM_KEY, ""));
                intent.putExtra(MainActivity.USER_ITEMS,sharedPrefs.getString(USER_ITEMS, ""));
                intent.putExtra(MainActivity.FLAG_DOORS,sharedPrefs.getString(FLAG_DOORS, ""));
                startActivity(intent);

            }
        });
    }





    public void log2cat(String test){
        Log.d("Files", test);
    }

    public void listdir(){

                String path = Environment.getExternalStorageDirectory().toString() + "/TextAdventure/Saves";
                Log.d("Files", "Path: " + path);
                File directory = new File(path);
                File[] files = directory.listFiles();
                Log.d("Files", "Size: " + files.length);
                for (int i = 0; i < files.length; i++) {
                    Log.d("Files", "FileName:" + files[i].getName());
                    if (files[i].isDirectory()) {
                        Log.d("Files", "Is Dir");
                    } else {
                        Log.d("Files", "Not a Dir");

                    }


        }

    }


}
