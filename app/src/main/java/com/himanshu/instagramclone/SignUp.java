package com.himanshu.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class SignUp extends AppCompatActivity implements View.OnClickListener
{
    // UI COMPONENTS

    private EditText edtEnterEmail , edtUsername , edtPassword;
    private Button btnSignUp , btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // USED TO CHANGE THE TEXT PRESENT ON THE ACTION BAR
        setTitle("Sign Up");

        edtEnterEmail = findViewById(R.id.edtEnterEmail);
        edtUsername = findViewById(R.id.edtEnterUsername);
        edtPassword = findViewById(R.id.edtEnterPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        // creating on click Listener for the buttons (we don't need to do it by using the anonymous as we had already implemented the interface).
        btnSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        // if we want to signUp new user then we want to logout previous user if present
        if(ParseUser.getCurrentUser() != null)
        {
            //ParseUser.getCurrentUser().logOut();

            transitionToSocialMediaActivity();      // as we don't want user to get logged out we want the user to be at SocialMediaActivity
        }


        // SUPPOSE AFTER ENTERING ALL DETAILS THE USER PRESS ENTER INSTEAD OF THE SIGNUP THEN WE WANT IT TO STILL SIGNUP, SO WE ARE
        // MAKING ONKEYlISTENER FOR THE ENTER BUTTON
        edtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event)
            {
                if(keyCode == KeyEvent.KEYCODE_ENTER  &&  event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    onClick(btnSignUp);
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btnSignUp  :
                if(edtEnterEmail.getText().toString().equals(""))       // as EmailId is required to signUp
                {
                    FancyToast.makeText(SignUp.this , "Email Id is required",
                            FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                }
                else if(edtUsername.getText().toString().equals(""))        // as Username is required to signUp
                {
                    FancyToast.makeText(SignUp.this , "Username is required",
                            FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                }
                else if(edtPassword.getText().toString().equals(""))        // as Password is required to signUp
                {
                    FancyToast.makeText(SignUp.this , "Password is required",
                            FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                }
                else
                {
                    final ParseUser appUser = new ParseUser();
                    appUser.setEmail(edtEnterEmail.getText().toString());
                    appUser.setUsername(edtUsername.getText().toString());
                    appUser.setPassword(edtPassword.getText().toString());

                    // used to show the progress dialog
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Signing Up" + edtUsername.getText().toString());
                    progressDialog.show();


                    appUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                FancyToast.makeText(SignUp.this, appUser.getUsername() + " is signed up.",
                                        FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                            } else {
                                FancyToast.makeText(SignUp.this, e.getMessage(),
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                            }

                            progressDialog.dismiss();       // to dismiss the progress dialog.



                        }
                    });

                    transitionToSocialMediaActivity();          // as we want to go to SocialMediaActivity when we get Signned Up
                }

                break;

            case R.id.btnLogin:

                Intent intent = new Intent(SignUp.this , LoginActivity.class);      // used to change the activity
                startActivity(intent);

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


    // to make transition to the SocialMediaActivity when the user is Signned Up
    public void transitionToSocialMediaActivity()
    {
        Intent intent = new Intent(SignUp.this , SocialMediaActivity.class);
        startActivity(intent);
        finish();
    }
}