package com.example.rajnish.rssfeed;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.NoEncryption;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import io.paperdb.Paper;

/**
 * Created by Rajnish on 16-02-2018.
 */

public class ReadRss extends AsyncTask<Void, Void, Void> {
    Context context;
  ArrayList<String> title,description,date;
    String address = "http://www.sciencemag.org/rss/news_current.xml";
    ProgressDialog progressDialog;
    ArrayList<FeedItem> feedItems;
    RecyclerView recyclerView;
    URL url;

    MainActivity mainActivity=new MainActivity();

    public ReadRss(Context context, RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.context = context;
        Paper.init(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
    }

    //before fetching of rss statrs show progress to user
    @Override
    protected void onPreExecute() {
//        progressDialog.show();
        super.onPreExecute();
    }


    //This method will execute in background so in this method download rss feeds
    @Override
    protected Void doInBackground(Void... params) {
        //call process xml method to process document we downloaded from getData() method
        feedItems = new ArrayList<>();
        if(mainActivity.isconnected==0)
        {
            //feedItems=Paper.book().read("rss",new ArrayList<FeedItem>());
            feedItems=Hawk.get("rss");
            System.out.println(feedItems.get(0).getDescription());
            FeedsAdapter adapter = new FeedsAdapter(context, feedItems);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.addItemDecoration(new VerticalSpace(20));
            recyclerView.setAdapter(adapter);
        }
        else
            ProcessXml(Getdata());

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        FeedsAdapter adapter = new FeedsAdapter(context, feedItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new VerticalSpace(20));
        recyclerView.setAdapter(adapter);
        try
        {
            new File("science.txt").delete();
            FileOutputStream fos=new FileOutputStream("science.txt",true);
            ObjectOutputStream oos=new ObjectOutputStream(fos);
            store sd=new store(title,description,date);
            oos.writeObject(sd);
            oos.close();
            fos.close();

        }
        catch (FileNotFoundException e )
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    // In this method we will process Rss feed  document we downloaded to parse useful information from it
    public void ProcessXml(Document data) {

        if (data != null) {

            Element root = data.getDocumentElement();
            Node channel = root.getChildNodes().item(1);
            NodeList items = channel.getChildNodes();
            for (int i = 0; i < items.getLength(); i++) {
                Node cureentchild = items.item(i);
                if (cureentchild.getNodeName().equalsIgnoreCase("item")) {
                    FeedItem item = new FeedItem();
                    NodeList itemchilds = cureentchild.getChildNodes();
                    for (int j = 0; j < itemchilds.getLength(); j++) {
                        Node cureent = itemchilds.item(j);
                        if (cureent.getNodeName().equalsIgnoreCase("title")) {
                            item.setTitle(cureent.getTextContent());

                        } else if (cureent.getNodeName().equalsIgnoreCase("description")) {
                            item.setDescription(cureent.getTextContent());
                        } else if (cureent.getNodeName().equalsIgnoreCase("pubDate")) {
                            item.setPubDate(cureent.getTextContent());
                        } else if (cureent.getNodeName().equalsIgnoreCase("link")) {
                            item.setLink(cureent.getTextContent());
                        } else if (cureent.getNodeName().equalsIgnoreCase("media:thumbnail")) {
                            //this will return us thumbnail url
                            String url = cureent.getAttributes().item(0).getTextContent();
                            item.setThumbnailUrl(url);
                        }
                    }
                    feedItems.add(item);

                }
            }

            //Paper.book().write("rss",feedItems);
            if(Hawk.contains("rss"))
                Hawk.delete("rss");
            Hawk.put("rss",feedItems);

        }
    }

    //This method will download rss feed document from specified url
    public Document Getdata() {
        try {
            url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document xmlDoc = builder.parse(inputStream);
            return xmlDoc;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
