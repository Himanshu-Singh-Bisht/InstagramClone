package com.himanshu.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{
    // UI COMPONENTS
    private EditText edtLoginEmail , edtLoginPassword;
    private Button btnSignUpLoginActivity , btnLoginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Log In");         // used to change the title on the action bar.

        edtLoginEmail = findViewById(R.id.edtLoginEmail);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);
        btnLoginActivity  = findViewById(R.id.btnLoginActivity);
        btnSignUpLoginActivity = findViewById(R.id.btnSignUpLoginActivity);


        // setting onClickListener for the button
        btnLoginActivity.setOnClickListener(this);
        btnSignUpLoginActivity.setOnClickListener(this);

        if(ParseUser.getCurrentUser() != null)
        {
            // ParseUser.getCurrentUser().logOut();
            transitionToSocialMediaActivity();          // as we don't want user to get logged out we want the user to be at SocialMediaActivity
        }



        // SUPPOSE AFTER ENTERING ALL DETAILS THE USER PRESS ENTER INSTEAD OF THE SIGNUP THEN WE WANT IT TO STILL SIGNUP, SO WE ARE
        // MAKING ONKEYlISTENER FOR THE ENTER BUTTON
        edtLoginPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event)
            {
                if(keyCode == KeyEvent.KEYCODE_ENTER  &&  event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    onClick(btnSignUpLoginActivity);
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.btnLoginActivity :
                if(edtLoginEmail.getText().toString().equals(""))
                {
                    FancyToast.makeText(LoginActivity.this , "Email Id is required",
                            FancyToast.LENGTH_SHORT , FancyToast.SUCCESS , true).show();
                }
                else if(edtLoginPassword.getText().toString().equals(""))
                {
                    FancyToast.makeText(LoginActivity.this , "Password is required",
                            FancyToast.LENGTH_SHORT , FancyToast.SUCCESS , true).show();
                }
                else
                {
                    ParseUser.logInInBackground(edtLoginEmail.getText().toString(), edtLoginPassword.getText().toString(),
                            new LogInCallback() {
                                @Override
                                public void done(ParseUser user, ParseException e) {
                                    if (user != null && e == null) {
                                        FancyToast.makeText(LoginActivity.this, user.getUsername() + " is Logged In Successfully.",
                                                FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                                    } else {
                                        FancyToast.makeText(LoginActivity.this, e.getMessage(),
                                                FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                                    }
                                }
                            });

                    transitionToSocialMediaActivity(); // as after logged in we want to go to SocialMediaActivity
                    break;
                }
            case R.id.btnSignUpLoginActivity :

                finish();           // used to finish the current activity and go back to previous activity.

                break;
        }
    }


    // AS WE WANT THAT WHEN THE USER TAPS ON THE LAYOUT THEN KEYBOARD HIDES (IF PRESENT).
    public void rootLayoutTapped(View view)
    {
        try     // try and catch is used because if keyboard is not present on screen and user taps the layout then it causes the error.
        {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken() , 0);
        }
        catch (Exception e)
        {
            e.printStackTrace();        // prints error to the logcat
        }

    }



    // to make transition to the SocialMediaActivity when the user is Looged In
    public void transitionToSocialMediaActivity()
    {
        Intent intent = new Intent(LoginActivity.this , SocialMediaActivity.class);
        startActivity(intent);
    }
}
