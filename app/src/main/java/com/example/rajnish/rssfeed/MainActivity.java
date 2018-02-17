package com.example.rajnish.rssfeed;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;

ArrayList <offlineFeedItem> offlineFeed;



    Toast toast;
    ArrayList<String> titles,descriptions,dates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerview);
        titles = new ArrayList<>();
        dates = new ArrayList<>();
        descriptions = new ArrayList<>();
        checkInternet ch = new checkInternet();
        if (!ch.isConnected(MainActivity.this)) {

            Toast.makeText(MainActivity.this, "No Internet Connection \n Loading previous blogs",Toast.LENGTH_LONG).show();
           /* try {
                FileInputStream fis = openFileInput("science.txt");
                ObjectInputStream ois = new ObjectInputStream(fis);
                store sd = (store) ois.readObject();
                titles = sd.getTitle();
                descriptions = sd.getDescription();
                dates = sd.getDate();
                for(int i=0;i<titles.size();i++)
                {
                offlineFeedItem offlineFeedItem = new offlineFeedItem(titles.get(i), descriptions.get(i), dates.get(i));
                offlineFeed.add(offlineFeedItem);

            }
            of=new offlineFeedAdapter();




                    } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }*/

        }
        else
        {

            ReadRss readRss = new ReadRss(this, recyclerView);
            readRss.execute();



        }
    }
}