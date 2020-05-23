package com.himanshu.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.parse.ParseUser;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        TextView txtWelcome = findViewById(R.id.txtWelcome);
        txtWelcome.setText("Welcome " + ParseUser.getCurrentUser().get("username"));


        // creating onClicklistener for the Logout button
        findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ParseUser.logOut();             // used to logout the user

                finish();           // used to finish current activity and go back to previous activity (activities can be seen as elements in stack)
            }
        });
    }
}
