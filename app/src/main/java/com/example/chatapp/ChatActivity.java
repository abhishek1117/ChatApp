package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        setTitle("Current Users");

        listView = findViewById(R.id.listview);
        arrayList = new ArrayList();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeContainer);

        listView.setOnItemClickListener(ChatActivity.this);
        //final TextView txtloadingusers = findViewById(R.id.txtloadingusers);

    try {
        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {

                    if (objects.size() > 0) {

                        for (ParseUser user : objects) {

                            arrayList.add(user.getUsername());
                        }
                        listView.setAdapter(arrayAdapter);
                        //txtloadingusers.animate().alpha(0f).setDuration(2000);
                        listView.setVisibility(View.VISIBLE);
                    }

                }
            }
        });
    }catch (Exception e){

        e.printStackTrace();
    }

    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            try{

                ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
                parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
                parseQuery.whereNotContainedIn("username", arrayList);
                parseQuery.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        if(objects.size()>0){
                            if(e==null){
                                for(ParseUser user : objects){

                                    arrayList.add(user.getUsername());

                                }
                                arrayAdapter.notifyDataSetChanged();
                                if(swipeRefreshLayout.isRefreshing()){
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            }
                        }else{

                            if(swipeRefreshLayout.isRefreshing()) {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    }
                });

            }catch (Exception e){

                e.printStackTrace();

            }
        }
    });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(ChatActivity.this, ChattingActivity.class);
        intent.putExtra("selectedUser", arrayList.get(position));
        startActivity(intent);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.logoutUserItem) {
            ParseUser.getCurrentUser().logOut();
            finish();
            Intent intent = new Intent(ChatActivity.this, SignUp.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }
}