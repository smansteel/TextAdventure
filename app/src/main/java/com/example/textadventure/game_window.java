package com.example.textadventure;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Message;
import android.os.strictmode.CleartextNetworkViolation;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class game_window extends AppCompatActivity
{
    // used for save player pos
    public static final String MY_PREFS = "prefs";
    public static final String MY_KEY = "pos";
    public static final String ITEM_KEY = "item";
    public static final String USER_ITEMS = "user_items";
    public static final String FLAG_DOORS = "flags";
    SharedPreferences sharedPrefs;



    static final int NUM_OF_ROOMS = 22;
    Room[] thedungeon;  // array of room objects called thedungeon

    Player thePlayer;   // int playerpos is now in this class !!!
    //int playerPos = 0;

    //controls
    TextView descriptionTextView;
    TextView item_in_hand;
    Item current_item;

    Button northButton;
    Button eastButton;
    Button southButton;
    Button westButton;
    Button btn_cycle_hand;

    TextView playerInventoryTextView;
    TextView roomInventoryTextView;

    Button pickupButton;
    Button dropButton;

    Button saveButton;
    List<Item> itemslist = new ArrayList<>();

    ImageView imageView;
    String items;
    boolean hasextra = false;
    boolean[] flag_doors = new boolean[] {false,false,false};
    String[] playerinvtoadd = new String[]{};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            hasextra = true;
            String value = extras.getString(MY_KEY);
            Log.v("PRINT", extras.getString(ITEM_KEY)+" ");
            items = extras.getString(ITEM_KEY);
            Log.v("PRINT", extras.getString(FLAG_DOORS)+" ");
            String[] booltoparse = extras.getString(FLAG_DOORS).split("\\^");
            for(int i =0; i<booltoparse.length; i++){
                flag_doors[i] = Boolean.parseBoolean(booltoparse[i]);
            }
            Log.v("PRINT", extras.getString(USER_ITEMS)+" ");



            initPlayerWithLoad( Integer.parseInt( value ) );
           Log.d("SAVE", "onCreate: "+extras.getString(USER_ITEMS).contains("^"));
            if((extras.getString(USER_ITEMS) != null )&&(extras.getString(USER_ITEMS).contains("^"))) {

                playerinvtoadd = extras.getString(USER_ITEMS).split("\\^");

            }
            else if (extras.getString(USER_ITEMS) != null){
                playerinvtoadd = new String[]{extras.getString(USER_ITEMS)};
            }

        }
        else
        {
            initPlayer();   // player pos = 0 Aka New Game

        }   //  if (extras != null)


        initTheDungeon();


        readXMLFile();
        setupControls();
        Log.v("ZEUBI", hasextra+" ");
        if(hasextra){
            String[] items_list= items.split("\\^");
            Log.d("DEBUG", "item length"+items_list.length +"dj length"+thedungeon.length);
            for(int i = 0; i<=thedungeon.length-1;i++)
            {
                thedungeon[i].setInventory(Integer.parseInt(items_list[i]));

            }
            Log.d("SAVE", "Arrivé a la boucle for "+ playerinvtoadd.length);
            for (String s : playerinvtoadd) {
                Log.d("SAVE", "onCreate: "+s);
                Log.d("SAVE", "onCreate: "+find_item(Integer.parseInt(s)).getName());
                thePlayer.addNewInv(find_item(Integer.parseInt(s)));
            }

        }







        updateRoomInformation();

    }   //  protected void onCreate(Bundle savedInstanceState)

    protected void initPlayer()
    {
        thePlayer = new Player();

    }   //  protected void initPlayer()

    protected void initPlayerWithLoad(int loadPosition)
    {
        thePlayer = new Player( loadPosition );




    }   //  protected void initPlayerWithLoad(int loadPosition)


    protected void setupControls()
    {

        setContentView(R.layout.activity_game_window);
        descriptionTextView = findViewById(R.id.descriptionTextView);

        thePlayer.setNewInventory(new ArrayList<>());


        northButton = findViewById(R.id.northButton);
        northButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                thePlayer.setPlayerPos( thedungeon[thePlayer.getPlayerPos()].getNorth() );  // move north

                updateRoomInformation();
            }
        });

        eastButton = findViewById(R.id.eastButton);
        eastButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if ((thePlayer.getPlayerPos()== 4) &&(!flag_doors[0])){
                    if((find_item(1)== thePlayer.searchFromID(1))){
                        thePlayer.setPlayerPos( thedungeon[thePlayer.getPlayerPos()].getEast()  );
                        thePlayer.getNewInventory().remove(thePlayer.searchFromID(1));
                        flag_doors[0] = true;// move east
                    }
                    else{
                        AlertDialog.Builder builder
                                = new AlertDialog
                                .Builder(game_window.this);

                        builder.setMessage("Find the key and come back");

                        builder.setTitle("Door is Locked");

                        builder.setCancelable(true);

                        builder.setPositiveButton(
                                "OK",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which)
                                    {

                                    }
                                });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                }
                else{
                    thePlayer.setPlayerPos( thedungeon[thePlayer.getPlayerPos()].getEast()  );  // move east
                }


                updateRoomInformation();
            }
        });


        southButton = findViewById(R.id.southButton);
        southButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if ((thePlayer.getPlayerPos())== 9&& (!flag_doors[1])){

                    if((find_item(1)== thePlayer.searchFromID(1))){
                        thePlayer.setPlayerPos( thedungeon[thePlayer.getPlayerPos()].getSouth()  );
                        thePlayer.getNewInventory().remove(thePlayer.searchFromID(1));
                        flag_doors[1] = true;
                    }
                    else{
                        AlertDialog.Builder builder
                                = new AlertDialog
                                .Builder(game_window.this);

                        builder.setMessage("Find the key and come back");

                        builder.setTitle("Door is Locked");

                        builder.setCancelable(true);

                        builder.setPositiveButton(
                                "OK",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which)
                                    {

                                    }
                                });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                }
                else{
                    thePlayer.setPlayerPos( thedungeon[thePlayer.getPlayerPos()].getSouth()  );  // move south
                }


                updateRoomInformation();
            }
        });

        westButton = findViewById(R.id.westButton);
        westButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (thePlayer.getPlayerPos()== 19 && (!flag_doors[2])){
                    if((find_item(2)== thePlayer.searchFromID(2))){
                        thePlayer.setPlayerPos( thedungeon[thePlayer.getPlayerPos()].getWest()  );
                        thePlayer.getNewInventory().remove(thePlayer.searchFromID(2));
                        flag_doors[2] = true;// move east
                    }
                    else{
                        AlertDialog.Builder builder
                                = new AlertDialog
                                .Builder(game_window.this);

                        builder.setMessage("Find the key and come back");

                        builder.setTitle("Door is Locked");

                        builder.setCancelable(true);

                        builder.setPositiveButton(
                                "OK",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which)
                                    {

                                    }
                                });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                }
                else{
                    thePlayer.setPlayerPos( thedungeon[thePlayer.getPlayerPos()].getWest()  );  // move east
                }
                // move west

                updateRoomInformation();
            }
        });

        playerInventoryTextView = findViewById(R.id.playerInventoryTextView);

        roomInventoryTextView = findViewById(R.id.roomInventoryTextView);

        pickupButton = findViewById(R.id.pickupButton);
        pickupButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if ((thedungeon[thePlayer.getPlayerPos()].getInventory() != -1))
                {
                    for(int i =0; i<itemslist.size();i++)
                    {
                        if(thedungeon[thePlayer.getPlayerPos()].getInventory() == itemslist.get(i).getId()){
                            thePlayer.addNewInv(itemslist.get(i));
                            updateHand(thePlayer);
                            refreshhandconten();
                            break;
                        }
                    }

                    thedungeon[thePlayer.getPlayerPos()].setInventory( -1 );

                    if(thePlayer.NewInvToString().equals("")){
                        playerInventoryTextView.setText( "Player Inventory is empty"  );
                    }else{
                        playerInventoryTextView.setText( "Player Inventory = " + thePlayer.NewInvToString()  );
                    }

                    roomInventoryTextView.setText("Room Inventory is empty" );



                }
            }
        });

        dropButton = findViewById(R.id.dropButton);
        dropButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d("TAG", String.valueOf(thePlayer.getPlayerPos()));
                boolean flag_done = false;

                if ((thedungeon[thePlayer.getPlayerPos()].getInventory() == -1))

                {



                    if ((thePlayer.getItem_in_hand() != -1)&&(thePlayer.getNewInventory()!=null)){
                        thedungeon[thePlayer.getPlayerPos()].setInventory(thePlayer.getNewInventory().get(thePlayer.getItem_in_hand()).getId());
                        thePlayer.remove_from_Item(thePlayer.getNewInventory().get(thePlayer.getItem_in_hand()));
                        updateHand(thePlayer);
                        refreshhandconten();
                        flag_done = true;
                    }




                    if (!flag_done){
                        AlertDialog.Builder builder
                                = new AlertDialog
                                .Builder(game_window.this);

                        builder.setMessage("You have nothing to drop");

                        builder.setTitle("Hand Empty");

                        builder.setCancelable(true);

                        builder.setPositiveButton(
                                "OK",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which)
                                    {

                                    }
                                });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }

                    if(thePlayer.NewInvToString().equals("")){
                        playerInventoryTextView.setText( "Player Inventory is empty"  );
                    }else{
                        playerInventoryTextView.setText( "Player Inventory = " + thePlayer.NewInvToString()  );
                    }

                    roomInventoryTextView.setText("Room Inventory = " + find_item(thedungeon[thePlayer.getPlayerPos()].getInventory()).getName()  );
                }
                else{
                    AlertDialog.Builder builder
                            = new AlertDialog
                            .Builder(game_window.this);

                    builder.setMessage("Try dropping the item in another room");

                    builder.setTitle("Room is not empty");

                    builder.setCancelable(true);

                    builder.setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which)
                                {

                                }
                            });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });

        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                savePosition();
            }
        });

        imageView = findViewById(R.id.imageView);
        imageView.setVisibility(View.INVISIBLE);

        item_in_hand = findViewById(R.id.item_in_hand);

        btn_cycle_hand = findViewById(R.id.btn_cycle_hand);
        btn_cycle_hand.setText("Next Item");
        btn_cycle_hand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cycle_hand();


            }
        });

        updateRoomInformation();

    }   //  protected void setupControls()

    protected void updateRoomInformation()
    {
        descriptionTextView.setText(thedungeon[thePlayer.getPlayerPos()].getDescription());
        refreshhandconten();
        checkForKey();

        validDirections( thePlayer.getPlayerPos() );

        if(thePlayer.NewInvToString().equals("")){
            playerInventoryTextView.setText( "Player Inventory is empty"  );
        }else{
            playerInventoryTextView.setText( "Player Inventory = " + thePlayer.NewInvToString()  );
        }

        roomInventoryTextView.setText("Room Inventory = " + find_item(thedungeon[thePlayer.getPlayerPos()].getInventory()).getName()  );

        //displayRoomImage( thePlayer.getPlayerPos() );

        displayRoomImageV2( thePlayer.getPlayerPos() );

    }   //  protected void updateRoomInformation()

    // using a if else if block of code
    protected void displayRoomImage(int currentPos)
    {
/*        if (currentPos == 0)
        {
            imageView.setImageResource(R.drawable.room0);
        }
        else if (currentPos == 1)
        {
            imageView.setImageResource(R.drawable.room1);
        }
        else if (currentPos == 2)
        {
            imageView.setImageResource(R.drawable.room2);
        }
        else if (currentPos  == 3)
        {
            imageView.setImageResource(R.drawable.room3);
        }*/

    }   //  protected void displayRoomImage()

    protected void displayRoomImageV2(int currentPos)
    {
        int[] imageList = {R.drawable.door, R.drawable.dungeon, R.drawable.key, R.drawable.bats, R.drawable.graal};

        // only some rooms have images 1 ;49

        //imageView.setImageResource(   imageList[currentPos] );
        imageView.setVisibility(View.VISIBLE);
        if(currentPos== 1|currentPos== 17){
            imageView.setImageResource(imageList[2]);
        }else if (currentPos== 4|currentPos== 10){
        imageView.setImageResource(imageList[0]); }
        else if (currentPos== 0){
            imageView.setImageResource(imageList[1]); }
        else if (currentPos== 15){
            imageView.setImageResource(imageList[4]); }
        else if (currentPos== 16){
            imageView.setImageResource(imageList[3]);
            MediaPlayer ring= MediaPlayer.create(game_window.this,R.raw.bats);
            ring.start();}
        else{
            imageView.setVisibility(View.INVISIBLE);
        }

    }   //  protected void displayRoomImageV2(int currentPos)

    protected void checkForKey()
    {   //I am not doing this by properly writing it down on the xml by lack of time
        if ((find_item(2)== thePlayer.searchFromID(2)) && (thePlayer.getPlayerPos() == 19))
        {
            // unlock the east door in room 1
            thedungeon[thePlayer.getPlayerPos()].setEast(15);

            // keys is removed !!!
            thePlayer.getNewInventory().remove(thePlayer.searchFromID(2)) ;

            DisplayMessage("Final room - River has been emptied");
        }
        if ((find_item(1)== thePlayer.searchFromID(2)) && (thePlayer.getPlayerPos() == 8))
        {
            // unlock the east door in room 1
            thedungeon[thePlayer.getPlayerPos()].setEast(12);

            // keys is removed !!!
            thePlayer.getNewInventory().remove(thePlayer.searchFromID(2)) ;

            DisplayMessage("East Door has been Unlocked!");
        }
        if ((find_item(1)== thePlayer.searchFromID(2)) && (thePlayer.getPlayerPos() == 10))
        {
            // unlock the east door in room 1
            thedungeon[thePlayer.getPlayerPos()].setEast(11);

            // keys is removed !!!
            thePlayer.getNewInventory().remove(thePlayer.searchFromID(2)) ;

            DisplayMessage("Room1 - East Door has been Unlocked!");
        }

    }   //  protected void checkForKey()

    public void savePosition()
    {
        sharedPrefs = getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPrefs.edit();
        String key2 = "";
        Log.d("DEBUG", String.valueOf(thedungeon.length));
        for(int i = 0; i<thedungeon.length;i++)
        {
            key2 +=thedungeon[i].getInventory();
            if( i!=thedungeon.length-1){
                key2 +="^";
            }

        }
        String key3 = "";
        for(int i = 0; i<thePlayer.getNewInventory().size();i++)
        {
            Log.d("SAVE", "savePosition: "+thePlayer.getNewInventory().get(i));
            if(thePlayer.getNewInventory().get(i).getId()!=-1)
                key3 +=thePlayer.getNewInventory().get(i).getId()+"";
                if( i!=thePlayer.getNewInventory().size()-1){
                    key3 +="^";
                }

        }
        String key4 = "";

        for(int i = 0; i<flag_doors.length;i++)
        {
            key4 +=flag_doors[i]+"";
            if( i!=flag_doors.length-1){
                key4 +="^";
            }

        }


        edit.putString(FLAG_DOORS, key4);
        edit.putString(USER_ITEMS, key3);
        edit.putInt(MY_KEY, thePlayer.getPlayerPos() );
        edit.putString(ITEM_KEY, key2 );
        edit.commit();

        DisplayMessage("Game Saved !");

    } // public void savePosition()

    public void DisplayMessage(CharSequence text)
    {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    } // public void DisplayMessage(CharSequence text)

    protected void validDirections(int newPlayerPos)
    {
        if (thedungeon[newPlayerPos].getNorth() == Room.NO_EXIT)
        {
            northButton.setEnabled(false);
        }
        else
        {
            northButton.setEnabled(true);
        }

        if (thedungeon[newPlayerPos].getEast() == Room.NO_EXIT)
        {
            eastButton.setEnabled(false);
        }
        else
        {
            eastButton.setEnabled(true);
        }

        if (thedungeon[newPlayerPos].getSouth() == Room.NO_EXIT)
        {
            southButton.setEnabled(false);
        }
        else
        {
            southButton.setEnabled(true);
        }

        if (thedungeon[newPlayerPos].getWest() == Room.NO_EXIT)
        {
            westButton.setEnabled(false);
        }
        else
        {
            westButton.setEnabled(true);
        }

    }   //  protected void validDirections(int newPlayerPos)

    protected void initTheDungeon()
    {
        thedungeon = new Room[NUM_OF_ROOMS];
        for (int pos = 0; pos < NUM_OF_ROOMS; pos++)
        {
            thedungeon[pos] = new Room();
        }

    } // public static void initTheDungeon()

    public void displayRooms()
    {
        Log.w("display ROOM", "**** start of display rooms ****");

        for (int pos = 0; pos < NUM_OF_ROOMS; pos++)
        {
            Log.w("POS AKA SLOT","pos = " + pos);
            Log.w("display ROOM", "North = " + thedungeon[pos].getNorth());
            Log.w("display ROOM", "East = " + thedungeon[pos].getEast());
            Log.w("display ROOM", "South = " + thedungeon[pos].getSouth());
            Log.w("display ROOM", "West = " + thedungeon[pos].getWest());

            Log.w("display ROOM", "Description = " + thedungeon[pos].getDescription());

            Log.w(" "," ");
        }

        Log.w("display ROOM", "**** end of display rooms ****");

    } // public void displayRooms() {

    public void readXMLFile()
    {
        int pos = 0; // May be use this variable, to keep track of what position of the array of Room Objects.
        itemslist.add(new Item("-1", ""));
        Log.v("ZEUBI", hasextra+" ");
        try
        {
            int room_count = 0;

            XmlResourceParser xpp = getResources().getXml(R.xml.dungeon);   // open xml file to read
            xpp.next();
            int eventType = xpp.getEventType();
            String elemtext = null;

            while (eventType != XmlPullParser.END_DOCUMENT) // AKA End of File
            {
                if (eventType == XmlPullParser.START_TAG)
                {
                    String elemName = xpp.getName();
                    if (elemName.equals("dungeon")) // do not == to compare String objects !!!
                    {

                        String titleAttr = xpp.getAttributeValue(null,"title");


                    } // if (elemName.equals("dungeon"))

                    if (elemName.equals("item")) // do not == to compare String objects !!!
                    {

                        elemtext = "item";


                    }
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
                    if (elemName.equals("id"))  // for storing inventory
                    {
                        elemtext = "id";
                    }
                    if (elemName.equals("name"))  // for storing inventory
                    {
                        elemtext = "name";
                    }
                    if (elemName.equals("room_item"))  // for storing inventory
                    {
                        elemtext = "room_item";
                    }

                } // if (eventType == XmlPullParser.START_TAG)

                // Hint: xpp.getText()
                else if (eventType == XmlPullParser.TEXT)
                {
                    if (elemtext.equals("north"))
                    {
                        Log.w("ROOM", "north = " + xpp.getText());
                        thedungeon[room_count-1].setNorth( Integer.valueOf(xpp.getText()));
                    }
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
                    }
                    else if (elemtext.equals("id"))   // added to setup inventory
                    {

                        if (itemslist.get(itemslist.size()-1).getId()== -1){
                            itemslist.get(itemslist.size()-1).setId(Integer.parseInt(xpp.getText()));
                        }
                        else{
                            itemslist.add(new Item(xpp.getText(),""));
                        }
                    }
                    else if (elemtext.equals("name"))   // added to setup inventory
                    {
                        if (itemslist.get(itemslist.size() - 1).getName().equals("")){
                            itemslist.get(itemslist.size()-1).setName(xpp.getText());
                        }
                        else{
                            itemslist.add(new Item("-1",xpp.getText()));
                        }
                    }
                    else if (elemtext.equals("room_item"))
                    {
                        Log.d("Test", "readXMLFile: "+xpp.getText());
                        thedungeon[room_count-1].setInventory( Integer.parseInt(xpp.getText()) );
                    }

                } // else if (eventType == XmlPullParser.TEXT)

                eventType = xpp.next();

            } // while (eventType != XmlPullParser.END_DOCUMENT)
        } // try
        catch (XmlPullParserException | IOException e)
        {

        }


    } // public void readXMLFile()
    public Item find_item(int id){
        Item returnitem = new Item("-1","");

        for(Item i : itemslist)
        {

            if(id == i.getId()){
                returnitem=  i;

            }

        }
        return returnitem;
    }

    public void updateHand(Player thePlayer){
        if (thePlayer.getNewInventory().size() == 0){
            thePlayer.setItem_in_hand(-1);
        }
        else{
            thePlayer.setItem_in_hand(thePlayer.getNewInventory().size()-1);
        }
    }

    public void cycle_hand(){
        if(thePlayer.getNewInventory().size()!=0){
            Log.d("patate", "thePlayer.getItem_in_hand()+1 "+thePlayer.getItem_in_hand()+1+"thePlayer.getNewInventory().size() "+thePlayer.getNewInventory().size());
            thePlayer.setItem_in_hand((thePlayer.getItem_in_hand()+1)%thePlayer.getNewInventory().size());
            item_in_hand.setText(thePlayer.getNewInventory().get(thePlayer.getItem_in_hand()).getName());}

        else{
            thePlayer.setItem_in_hand(-1) ;
            item_in_hand.setText("No item currently in the hand");
        }
    }
    public void refreshhandconten(){
        if(thePlayer.getNewInventory().size()!=0){
            thePlayer.setItem_in_hand(thePlayer.getItem_in_hand()%thePlayer.getNewInventory().size());
            item_in_hand.setText(thePlayer.getNewInventory().get(thePlayer.getItem_in_hand()).getName());}

        else{
            item_in_hand.setText("No item currently in the hand");
        }
    }

}   //  public class MainActivity extends AppCompatActivity