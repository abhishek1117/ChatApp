package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText edtemail,edtpassword;
    private Button btnlogin,btnsignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Login");
        edtemail = findViewById(R.id.edtemaillogin);
        edtpassword = findViewById(R.id.edtpasswordlogin);
        edtpassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){

                    onClick(btnsignup);
                }
                return false;
            }
        });
        btnlogin = findViewById(R.id.btnloginlogin);
        btnsignup = findViewById(R.id.btnsignuplogin);
        btnlogin.setOnClickListener(this);
        btnsignup.setOnClickListener(this);
        if(ParseUser.getCurrentUser()!=null){

            ParseUser.getCurrentUser().logOut();

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnloginlogin:

                if(edtemail.getText().toString().equals("")||edtpassword.getText().toString().equals("")){
                    FancyToast.makeText(Login.this, "All the fields need to be filled",
                            Toast.LENGTH_LONG, FancyToast.INFO, true).show();
                }
                else {
                    ParseUser.logInInBackground(edtemail.getText().toString(), edtpassword.getText().toString(),
                            new LogInCallback() {
                                @Override
                                public void done(ParseUser user, ParseException e) {
                                    if (user != null && e == null) {

                                        FancyToast.makeText(Login.this, user.getUsername() + " is logged in!",
                                                Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                                        transitiontosocialmediaactivity();

                                    }
                                }
                            });
                }
                break;
            case R.id.btnsignuplogin:
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
                finish();
                break;

        }

    }
    public void rootlayouttapped(View view){
        try {

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }catch(Exception e){

            e.printStackTrace();

        }
    }
    private void transitiontosocialmediaactivity(){

        Intent intent = new Intent(Login.this, ChatActivity.class);
        startActivity(intent);
        finish();
    }
}

