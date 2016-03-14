package com.example.alecksjohanssen.instagramviewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by AlecksJohanssen on 3/13/2016.
 */
public class cmtactivity extends AppCompatActivity {
    public final static String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";
    String id;
    private ArrayList<Comment> comt;
    private CommentAdapter adapter;
    ListView listview;
    TextView textView;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_comment);
        setContentView(R.layout.activity_cmtactivity);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        comt = new ArrayList<>();
        adapter = new CommentAdapter(this, comt);
        listview = (ListView) findViewById(R.id.listView3);
        listview.setAdapter(adapter);
        fetchComment();

    }
    public void fetchComment() {
        String url = "https://api.instagram.com/v1/media/" + id + "/comments?client_id=" + CLIENT_ID;
        //Create a network client
        AsyncHttpClient client2 = new AsyncHttpClient();
        //Trigger request
        client2.get(url, null, new JsonHttpResponseHandler() {
            //onSuccess(Worked)

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                JSONArray commentJSON = null;
                try {

                    commentJSON = response.getJSONArray("data");//array posts
                    //iterate array of posts

                    for (int i = 0; i < commentJSON.length(); i++) {
                        JSONObject commentJson = commentJSON.getJSONObject(i);
                        //decode attribute of the json into a data model

                        Comment cmt = new Comment();
                        cmt.usernamecomment = commentJson.getJSONObject("from").getString("username");
                        cmt.comments = commentJson.getString("text");
                        cmt.profile = commentJson.getJSONObject("from").getString("profile_picture");
                        cmt.Created_Time1 = commentJson.getLong("created_time");
                        //Add decoded object to the Photos.add(photo);
                        adapter.add(cmt);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
