package com.example.textadventure;

import java.util.ArrayList;
import java.util.List;

public class Player
{
    static final String NOTHING = "NOTHING";

    private int playerPos;

    private String inventory;
    private List<Item> newinventory;

    public int getItem_in_hand() {
        return item_in_hand;
    }

    public void setItem_in_hand(int item_in_hand) {
        this.item_in_hand = item_in_hand;
    }

    private int item_in_hand;

    Player()
    {
        playerPos = 0;  // start in room 0
        inventory = NOTHING;

    }   //  Player()

    Player(int newPosition)
    {
        playerPos = newPosition;
        inventory = NOTHING;

    }   //  Player(int newPosition)

    public int getPlayerPos()
    {
        return playerPos;
    }

    public void setPlayerPos(int playerPos)
    {
        this.playerPos = playerPos;
    }

    public String getInventory()
    {
        return inventory;
    }

    public void setInventory(String inventory)
    {
        this.inventory = inventory;
    }

    public List<Item> getNewInventory()
    {
        return newinventory;
    }

    public String NewInvToString(){
        String returnstring = "";
        for(int i=0; i< newinventory.size();i++ ){
            returnstring+= newinventory.get(i).getName();
            if(i!= newinventory.size()-1){
                returnstring +=", ";
            }
        }
        return returnstring;
    }

    public void setNewInventory(List<Item> newinventory)
    {
        this.newinventory = newinventory;
    }
    public void addNewInv(Item newinventory)
    {   if (this.newinventory == null)
    {
        this.newinventory = new ArrayList<>();
    }
        this.newinventory.add(newinventory);
    }

    public boolean removeFromNewInv(String item_to_remove){
            int id_to_remove = -1;
        //This only remove the last element of the list with the good name
            for (int i = 0; i< newinventory.size();i++) {
                if (newinventory.get(i).getName().equals(item_to_remove)) {
                    id_to_remove =i;
                }
            }
            if(id_to_remove!=-1){
                newinventory.remove(newinventory.get(id_to_remove));
                return true;
            }
            else{
                return false;
            }

    }
    public void remove_from_Item(Item object){
        newinventory.remove(object);

    }
    public Item searchFromID(int u){
        Item returnitem = new Item("-1","");
        for(int i =0; i<newinventory.size();i++)
        {

            if(u == newinventory.get(i).getId()){

                returnitem =  newinventory.get(i);
                break;
            }

        }
        return returnitem;
    }

}   //  public class Player
