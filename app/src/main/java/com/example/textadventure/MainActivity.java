package com.example.textadventure;

import android.content.Intent;

import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    Button btn_start, btn_import;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         setContentView(R.layout.activity_main);
        setupControls();

    }

    private void setupControls(){
        btn_start = findViewById(R.id.btn_start);
        btn_import = findViewById(R.id.btn_import);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openDirectory(Uri.parse("package:" + BuildConfig.APPLICATION_ID));
/*                Intent newactivity = new Intent(MainActivity.this, game_window.class);
                startActivity(newactivity);*/
            }
        });
        btn_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openfile();

            }
        });
    }


    public void openfile(){
        // Request code for selecting a PDF document.
        final int PICK_PDF_FILE = 2;


        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/xml");

        // Optionally, specify a URI for the file that should appear in the
        // system file picker when it loads.
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, Uri.parse("package:" + BuildConfig.APPLICATION_ID));

        startActivityForResult(intent, PICK_PDF_FILE);

    }

    public void openDirectory(Uri  uriToLoad) {
        // Choose a directory using the system's file picker.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);

        // Optionally, specify a URI for the directory that should be opened in
        // the system file picker when it loads.
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uriToLoad);
        log2cat(intent.toString());
        startActivity(intent);

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


    public void readXMLFile()
    {
        int pos = 0; // May be use this variable, to keep track of what position of the array of Room Objects.
        try
        {
            int room_count = 0;

            XmlResourceParser xpp = getResources().getXml(R.xml.map1);
            xpp.next();
            int eventType = xpp.getEventType();
            String elemtext = null;

            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                if (eventType == XmlPullParser.START_TAG)
                {
                    String elemName = xpp.getName();
                    if (elemName.equals("dungeon"))
                    {
                        String titleAttr = xpp.getAttributeValue(null,"title");
                        String authorAttr = xpp.getAttributeValue(null,"author");

                    } // if (elemName.equals("dungeon"))

                    if (elemName.equals("room"))
                    {
                        room_count = room_count + 1;
                    }
                    if (elemName.equals("north"))
                    {
                        elemtext = "north";
                    }
                    if (elemName.equals("east"))
                    {
                        elemtext = "east";
                    }
                    if (elemName.equals("south"))
                    {
                        elemtext = "south";
                    }
                    if (elemName.equals("west"))
                    {
                        elemtext = "west";
                    }
                    if (elemName.equals("description"))
                    {
                        elemtext = "description";
                    }
                } // if (eventType == XmlPullParser.START_TAG)
                // You will need to add code in this section to read each element of the XML file
                // And then store the value in the current Room Object.
                // NOTE: This method initTheDungeon() creates and array of Room Objects, ready to be populated!
                // As you can see at the moment the data/text is displayed in the LogCat Window
                // Hint: xpp.getText()
                else if (eventType == XmlPullParser.TEXT)
                {
                    if (elemtext.equals("north"))
                    {
                        Log.w("ROOM", "north = " + xpp.getText());
                        Integer.valueOf(xpp.getText());
                    }/*
                    else if (elemtext.equals("east"))
                    {
                        Log.w("ROOM", "east = " + xpp.getText());
                        thedungeon[room_count-1].setEast(Integer.valueOf(xpp.getText()));
                    }
                    else if (elemtext.equals("south"))
                    {
                        Log.w("ROOM", "south = " + xpp.getText());
                        thedungeon[room_count-1].setSouth(Integer.valueOf(xpp.getText()));
                    }
                    else if (elemtext.equals("west"))
                    {
                        Log.w("ROOM", "west = " + xpp.getText());
                        thedungeon[room_count-1].setWest(Integer.valueOf(xpp.getText()));
                    }
                    else if (elemtext.equals("description"))
                    {
                        Log.w("ROOM", "description = " + xpp.getText());
                        thedungeon[room_count-1].setDescription( xpp.getText() );
                    }*/
                } // else if (eventType == XmlPullParser.TEXT)

                eventType = xpp.next();

            } // while (eventType != XmlPullParser.END_DOCUMENT)
        } // try
        catch (XmlPullParserException e)
        {

        }
        catch (IOException e)
        {
        }
    } // public void readXMLFile()


}
