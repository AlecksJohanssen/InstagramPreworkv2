package com.example.alecksjohanssen.instagramviewer;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Comment;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    public final static String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";
     Button button;
    private ArrayList<InstagramPhotos> photos;
    private SwipeListAdapter adapter;
    private SwipeRefreshLayout swipeContainer;
    TextView textView;
    ListView listview;
    long id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.instalogo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        listview = (ListView) findViewById(R.id.lvPhotos);
        photos = new ArrayList<InstagramPhotos>();
        adapter = new SwipeListAdapter(this, photos);
        listview.setAdapter(adapter);
        //Fetch the popular photos
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                fetchTimelineAsync(0);
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        listview.setOnScrollListener(new InfiniteScrollRecycleView(5) {
            @Override
            public void loadMore(int page, int totalItemsCount) {
                fetchPopularPhotos();
            }
        });
        fetchPopularPhotos();
        onClickListener();
    }
    public void fetchTimelineAsync(int page) {
        AsyncHttpClient client2 = new AsyncHttpClient();
        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        client2.get(url, null, new JsonHttpResponseHandler() {
            //onSuccess(Worked)

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                JSONArray photoJSON = null;
                try {
                    adapter.clear();
                    photoJSON = response.getJSONArray("data");//array posts
                    //iterate array of posts
                    for (int i = 0; i < photoJSON.length(); i++) {
                        JSONObject photoJSon = photoJSON.getJSONObject(i);

                        //decode attribute of the json into a data model
                        InstagramPhotos photo = new InstagramPhotos();
                        photo.profile = photoJSon.getJSONObject("user").getString("profile_picture");
                        photo.username = photoJSon.getJSONObject("user").getString("username");
                        // -Caption: {"data" => [X] => "caption" => "text"}
                        photo.caption = photoJSon.getJSONObject("caption").getString("text");
                        photo.Created_Time = photoJSon.getJSONObject("caption").getLong("created_time");
                        photo.id = photoJSon.getString("id");
                        photo.imageUrl = photoJSon.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        //Height
                        photo.imageHeight = photoJSon.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        //Like Counts
                        photo.likesCount = photoJSon.getJSONObject("likes").getInt("count");

                        JSONArray jsArray = photoJSon.getJSONObject("comments").getJSONArray("data");
                        photo.comment = jsArray.getJSONObject(jsArray.length() - 1).getString("text");
                        photo.comment2 = jsArray.getJSONObject(jsArray.length() - 2).getString("text");
                        photo.usercomment = jsArray.getJSONObject(jsArray.length() - 1).getJSONObject("from").getString("username");
                        photo.usercomment2 = jsArray.getJSONObject(jsArray.length() - 2).getJSONObject("from").getString("username");


                        //Add decoded object to the Photos
                        photos.add(photo);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Call Back
                adapter.notifyDataSetChanged();

            }

            // Iterate each of the photo items and decode the items into java

            //onFailure
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            //Do Something
        });
        swipeContainer.setRefreshing(false);

    }

    public void fetchPopularPhotos() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        client.get(url, null, new JsonHttpResponseHandler() {
            //onSuccess(Worked)

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                JSONArray photoJSON = null;
                try {
                    photoJSON = response.getJSONArray("data");//array posts
                    //iterate array of posts
                    for (int i = 0; i < photoJSON.length(); i++) {
                        JSONObject photoJSon = photoJSON.getJSONObject(i);

                        //decode attribute of the json into a data model
                        InstagramPhotos photo = new InstagramPhotos();
                        photo.profile = photoJSon.getJSONObject("user").getString("profile_picture");
                        photo.username = photoJSon.getJSONObject("user").getString("username");
                        // -Caption: {"data" => [X] => "caption" => "text"}
                        photo.caption = photoJSon.getJSONObject("caption").getString("text");
                        photo.Created_Time = photoJSon.getJSONObject("caption").getLong("created_time");
                        photo.id = photoJSon.getString("id");
                        photo.imageUrl = photoJSon.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        //Height
                        photo.imageHeight = photoJSon.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        //Like Counts
                        photo.likesCount = photoJSon.getJSONObject("likes").getInt("count");
                        JSONArray jsArray = photoJSon.getJSONObject("comments").getJSONArray("data");
                        photo.comment = jsArray.getJSONObject(jsArray.length() - 1).getString("text");
                        photo.comment2 = jsArray.getJSONObject(jsArray.length() - 2).getString("text");
                        photo.usercomment = jsArray.getJSONObject(jsArray.length() - 1).getJSONObject("from").getString("username");
                        photo.usercomment2 = jsArray.getJSONObject(jsArray.length() - 2).getJSONObject("from").getString("username");
                        photos.add(photo);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Call Back
                adapter.notifyDataSetChanged();

            }

            // Iterate each of the photo items and decode the items into java

            //onFailure
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("SERVER ERROR", responseString);
                swipeContainer.setRefreshing(false);
            }

            //Do Something
        });
        swipeContainer.setRefreshing(false);
    }
    private void onClickListener() {
        listview.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Intent intent = new Intent(MainActivity.this, cmtactivity.class);
                        intent.putExtra("id", photos.get(position).id);
                        startActivity(intent);

                    }
                }
        );
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}