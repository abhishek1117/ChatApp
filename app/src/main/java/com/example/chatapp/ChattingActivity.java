package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class ChattingActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView chatlistview;
    private ArrayList<String> chatlist;
    private ArrayAdapter adapter;
    private String selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        selectedUser = getIntent().getStringExtra("selectedUser");
        FancyToast.makeText(this, "Chat with "+selectedUser, Toast.LENGTH_SHORT, FancyToast.DEFAULT, true).show();

        findViewById(R.id.btnsend).setOnClickListener(this);

        chatlistview = findViewById(R.id.chatlistview);
        chatlist = new ArrayList();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, chatlist);
        chatlistview.setAdapter(adapter);
    try {
        ParseQuery<ParseObject> firstuserchatquery = ParseQuery.getQuery("chat");
        ParseQuery<ParseObject> seconduserchatquery = ParseQuery.getQuery("chat");

        firstuserchatquery.whereEqualTo("msgsender", ParseUser.getCurrentUser().getUsername());
        firstuserchatquery.whereEqualTo("msgreceiver", selectedUser);

        seconduserchatquery.whereEqualTo("msgsender", selectedUser);
        seconduserchatquery.whereEqualTo("msgreceiver", ParseUser.getCurrentUser().getUsername());

        ArrayList<ParseQuery<ParseObject>> allqueries = new ArrayList<>();
        allqueries.add(firstuserchatquery);
        allqueries.add(seconduserchatquery);

        ParseQuery<ParseObject> myquery = ParseQuery.or(allqueries);
        myquery.orderByAscending("createdAt");

        myquery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects.size() > 0 && e == null) {

                    for (ParseObject chatobject : objects) {

                        String message = chatobject.get("message") + "";
                        if (chatobject.get("msgsender").equals(ParseUser.getCurrentUser().getUsername())) {
                            message = ParseUser.getCurrentUser().getUsername() + ": " + message;
                        }
                        if (chatobject.get("msgreceiver").equals(ParseUser.getCurrentUser().getUsername())) {
                            message = selectedUser + ": " + message;
                        }

                        chatlist.add(message);
                    }

                    adapter.notifyDataSetChanged();
                }
            }
        });
    }catch (Exception e){
        e.printStackTrace();
    }
    }

    @Override
    public void onClick(View v) {
        final EditText edtsend = findViewById(R.id.edtsend);

        ParseObject chat = new ParseObject("chat");
        chat.put("msgsender", ParseUser.getCurrentUser().getUsername());
        chat.put("msgreceiver", selectedUser);
        chat.put("message", edtsend.getText().toString());
        chat.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if(e == null){

                    FancyToast.makeText(ChattingActivity.this, "Message sent!", Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                    chatlist.add(ParseUser.getCurrentUser().getUsername() + ": " + edtsend.getText().toString());
                    adapter.notifyDataSetChanged();
                    edtsend.setText("");

                }

            }
        });
    }
}
