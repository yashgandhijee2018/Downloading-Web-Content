package com.example.hp.downloadingwebcontent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String s;

    //creating a class for downloading contents in "background thread" which extends async task
    public class DownloadTask extends AsyncTask<String/*pass in a url*/ ,Void,String/*pass out a url*/>
    {
        //processing is done in this doInBackground....
        @Override
        protected String doInBackground(String... urls) //... are for an array of urls passed
        {
            String result="";
            URL url;
            HttpURLConnection urlConnection=null; //just like a browser


            try {
                url=new URL(urls[0]);//convert string to url format
                urlConnection= (HttpURLConnection) url.openConnection();//pass this to our pseudo browser
                InputStream in=urlConnection.getInputStream();//store the stream from browser
                InputStreamReader reader=new InputStreamReader(in);// passing that stream to reader

                int data=reader.read();//char by char in int format read first char
                while(data!=-1)//data==-1 is the end of stream
                {
                    char cur=(char)data;
                    result+=cur;
                    data=reader.read();//read next char
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;//returns info to main thread
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView=findViewById(R.id.textView);

        //create object of the class in main thread
        DownloadTask downloadTask=new DownloadTask();
        try {
            s=downloadTask.execute("https://www.udemy.com/course/the-complete-android-oreo-developer-course/learn/lecture/8339458#overview").get();//url is passed
            textView.setText(s);
            textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        } catch (Exception e) {
            textView.setText("There was an error while downloading web content");
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
