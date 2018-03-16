package com.rajramchandani.hackernews;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main3Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Intent i = getIntent();
        ArrayList<String> arr = new ArrayList<String>();
        final ArrayList<String> urls = new ArrayList<String>();
        ListView listView = (ListView)findViewById(R.id.listview);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.select_dialog_item,arr);
        listView.setAdapter(arrayAdapter);
        DownloadTask task=new DownloadTask();
        String result=null;
        try {

            result=task.execute("https://news.ycombinator.com/best").get();
            Pattern p = Pattern.compile("\"storylink\">(.*?)</a>");
            Matcher m = p.matcher(result);
            while(m.find())
            {
                arr.add(m.group(1));
                arrayAdapter.notifyDataSetChanged();
            }

            Pattern pattern = Pattern.compile("class=\"title\"><a href=\"(.*?)\"");
            Matcher matcher = pattern.matcher(result);
            while (matcher.find())
            {
                urls.add(matcher.group(1));
                Log.i("info",matcher.group(1));
            }
        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(getApplicationContext(),Main4Activity.class);
                i.putExtra("url",urls.get(position));
                startActivity(i);


            }
        });


    }



    public class DownloadTask extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... urls) /*varargs*/ {
            String result="";
            URL url;
            HttpURLConnection urlConnection=null;
            try{

                url=new URL(urls[0]);
                urlConnection=(HttpURLConnection) url.openConnection();
                InputStream in=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while(data!=-1)
                {
                    char current=(char)data;
                    result+=current;
                    data=reader.read();


                }

                return result;

            }

            catch(Exception e)
            {
                e.printStackTrace();
            }

            return "Failed";
        }


    }

}
