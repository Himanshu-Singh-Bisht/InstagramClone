package com.himanshu.instagramclone;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUpLoginActivity extends AppCompatActivity
{
    // UI COMPONENTS

    private EditText edtUserNameSignUp , edtPasswordSignUp , edtUserNameLogin , edtPasswordLogin;
    private Button btnSignUp , btnLogin ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_login_activity);

        edtUserNameSignUp = findViewById(R.id.edtUserNameSignUp);
        edtPasswordSignUp = findViewById(R.id.edtPasswordSignUp);
        edtUserNameLogin = findViewById(R.id.edtUserNameLogin);
        edtPasswordLogin = findViewById(R.id.edtPasswordLogin);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogin  = findViewById(R.id.btnLogin);

        // now creating onclickListener for btnSignUp
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final ParseUser appUser = new ParseUser();        // craetes user on the server (means object of parseUser type).
                appUser.setUsername(edtUserNameSignUp.getText().toString());  // setting up the User Name
                appUser.setPassword(edtPasswordSignUp.getText().toString());  // setting up the password

                appUser.signUpInBackground(new SignUpCallback()             // used to sign up in background
                {
                    @Override
                    public void done(ParseException e)
                    {
                        if(e == null)     // means no error occur while signing up
                        {
                            FancyToast.makeText(SignUpLoginActivity.this , appUser.get("username") + " is signed up successfully." ,
                                    FancyToast.LENGTH_SHORT , FancyToast.SUCCESS , true).show();
                        }
                        else
                        {
                            FancyToast.makeText(SignUpLoginActivity.this , e.getMessage() ,
                                    FancyToast.LENGTH_SHORT , FancyToast.ERROR , true).show();
                        }
                    }
                });
            }
        });


        // now creating onClickistener for btnLogin
        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ParseUser.logInInBackground(edtUserNameLogin.getText().toString(), edtPasswordLogin.getText().toString(),
                        new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e)
                            {
                                if(user != null && e == null)               // logged in successfully.
                                {
                                    FancyToast.makeText(SignUpLoginActivity.this , user.get("username") + " is logged in successfully." ,
                                            FancyToast.LENGTH_SHORT , FancyToast.SUCCESS , true).show();
                                }
                                else
                                {
                                    FancyToast.makeText(SignUpLoginActivity.this , e.getMessage() ,
                                            FancyToast.LENGTH_SHORT , FancyToast.ERROR , true).show();
                                }
                            }
                        });
            }
        });
    }
}
