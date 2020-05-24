package com.himanshu.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
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
            ParseUser.getCurrentUser().logOut();
        }
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.btnLoginActivity :
                ParseUser.logInInBackground(edtLoginEmail.getText().toString(), edtLoginPassword.getText().toString(),
                        new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {
                                if(user != null && e == null)
                                {
                                    FancyToast.makeText(LoginActivity.this , user.getUsername() + " is Logged In Successfully.",
                                            FancyToast.LENGTH_SHORT , FancyToast.SUCCESS , true).show();
                                }
                                else
                                {
                                    FancyToast.makeText(LoginActivity.this , e.getMessage(),
                                            FancyToast.LENGTH_SHORT , FancyToast.ERROR , true).show();
                                }
                            }
                        });
                break;

            case R.id.btnSignUpLoginActivity :

                finish();           // used to finish the current activity and go back to previous activity.

                break;
        }
    }
}
