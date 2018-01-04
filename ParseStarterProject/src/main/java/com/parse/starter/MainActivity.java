/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;

import static com.parse.starter.R.id.passEditText;


public class MainActivity extends AppCompatActivity implements View.OnKeyListener, View.OnClickListener {


    boolean swap = true;
    EditText usernameEditText, passEditText;
    ImageView logoImageView,backgroundImageView;
    RelativeLayout relativeLayout;
    TextView switchTextView;
    Button button;
    Intent intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button  = (Button)findViewById(R.id.button);
        passEditText = (EditText) findViewById(R.id.passEditText);
        switchTextView = (TextView) findViewById(R.id.switchTextView);
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        logoImageView = (ImageView) findViewById(R.id.logoImageView);
        backgroundImageView = (ImageView) findViewById(R.id.backgroundImageView);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        passEditText.setOnKeyListener(this);
        logoImageView.setOnClickListener(this);
        backgroundImageView.setOnClickListener(this);
        relativeLayout.setOnClickListener(this);

        setTitle("Instagram");

        intent = new Intent(MainActivity.this, Main2Activity.class);

        //ParseUser.logOut();

        if(ParseUser.getCurrentUser() != null){
            startActivity(intent);
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    public void switchText(View view){
        if(swap){
            button.setText("Sign Up");
            switchTextView.setText("Sign In");
            swap = false;
        }else{
            button.setText("Sign In");
            switchTextView.setText("Sign Up");
            swap = true;
        }
    }

    public void buttonClick(View view){
        if(swap){
            signIn();
        }else{
            signUp();
        }
    }

    public void signIn(){
        ParseUser user = new ParseUser();
        user.logInInBackground(usernameEditText.getText().toString(), passEditText.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e == null){
                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, "Login unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //System.out.println("Sign In");
    }

    public void signUp(){

        if(usernameEditText.getText().toString().equalsIgnoreCase("") || passEditText.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(MainActivity.this, "Username and Password are required.", Toast.LENGTH_SHORT).show();
        } else{
            ParseUser user = new ParseUser();
            user.setUsername(usernameEditText.getText().toString());
            user.setPassword(passEditText.getText().toString());

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null){
                        Toast.makeText(MainActivity.this, "Account Created!", Toast.LENGTH_SHORT).show();
                        System.out.println("Save Successful");
                    }else{
                        Toast.makeText(MainActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        System.out.println(e.getMessage());
                    }
                }
            });
        }
        //System.out.println("Sign Up");
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
            if(swap){
                signIn();
            }else{
                signUp();
            }
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.logoImageView || view.getId() == R.id.backgroundImageView){
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
