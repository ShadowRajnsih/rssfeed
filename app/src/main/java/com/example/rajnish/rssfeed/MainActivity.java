package com.example.rajnish.rssfeed;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    public int isconnected=1;
    SwipeRefreshLayout srl;


    ReadRss readRss;

    Toast toast;
    ArrayList<String> titles,descriptions,dates;
    checkInternet ch = new checkInternet();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        srl=(SwipeRefreshLayout)findViewById(R.id.srl);
        Hawk.init(this).build();
        recyclerView = findViewById(R.id.recyclerview);
        titles = new ArrayList<>();
        dates = new ArrayList<>();
        descriptions = new ArrayList<>();

        ArrayList<FeedItem> feedItems;
        int i=0;
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!ch.isConnected(MainActivity.this))
                {
                    Toast.makeText(MainActivity.this,"First connect to internet",Toast.LENGTH_LONG).show();
                    srl.setRefreshing(false);
                }
                else{
                    final ReadRss readRss = new ReadRss(MainActivity.this, recyclerView);
                    readRss.execute();
                    srl.setRefreshing(false);
                }
            }
        });

        if (!ch.isConnected(MainActivity.this)) {
            Toast.makeText(MainActivity.this, "No Internet Connection \n Loading previous blogs",Toast.LENGTH_LONG).show();
            isconnected=0;
            feedItems=Hawk.get("rss");
            FeedsAdapter adapter = new FeedsAdapter(this, feedItems);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.addItemDecoration(new VerticalSpace(20));
            recyclerView.setAdapter(adapter);




        }
        else
        {

            final ReadRss readRss = new ReadRss(this, recyclerView);
            readRss.execute();


        }

    }
}