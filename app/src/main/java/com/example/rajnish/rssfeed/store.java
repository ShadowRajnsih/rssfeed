package com.example.rajnish.rssfeed;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Rajnish on 17-02-2018.
 */

public class store implements Serializable{
    ArrayList<String> title;
    ArrayList<String> description;
    ArrayList<String> date;
    public store(ArrayList<String> title,ArrayList<String> description,ArrayList<String> date)
    {
        this.title=title;
        this.description=description;
        this.date=date;


    }
    public ArrayList<String> getTitle()
    {
        return title;


    }
    public ArrayList<String> getDescription()
    {
        return description;


    }
    public ArrayList<String> getDate()
    {
        return date;


    }



}
