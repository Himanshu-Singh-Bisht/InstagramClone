package com.himanshu.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class UsersPost extends AppCompatActivity
{

    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_post);

        linearLayout = findViewById(R.id.linearLayout);


        Intent recievedIntentObject = getIntent();          // used to store the intent Object passed from userTab activity
        final String recievedUserName = recievedIntentObject.getStringExtra("username");      // same key to be passed which was passed earlier.

//        FancyToast.makeText(this , recievedUserName , FancyToast.LENGTH_SHORT ,  FancyToast.SUCCESS , true ).show();        // to show which user was tapped.


        // NOW, TO SET THE POSTS OF THE USER WHICH IS TAPPED.
        setTitle(recievedUserName + "'s Post");
        ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("Photo");

        parseQuery.whereEqualTo("username" , recievedUserName);     // parse query stores the object of Photo class which have username equal to recievedUserName

        parseQuery.orderByDescending("createdAt");          // order of post according to the time of their creation

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        // getting list of objects from the parseQuery
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e)
            {
                if(objects.size() > 0 && e == null)
                {
                    for(ParseObject post : objects)
                    {
                        final TextView postDescription = new TextView(UsersPost.this);
                        postDescription.setText((post.get("image_des") == null) ? "No Description Given" : post.get("image_des") + "");

                        // getting image from the server
                        ParseFile postPicture = (ParseFile) post.get("picture");
                        postPicture.getDataInBackground(new GetDataCallback()
                        {
                            @Override
                            public void done(byte[] data, ParseException e)
                            {
                                if(data != null && e == null)
                                {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data , 0 , data.length);
                                    ImageView postImageView = new ImageView(UsersPost.this);

                                    // as we want image view inside LinearLayout
                                    LinearLayout.LayoutParams imageView_params = new LinearLayout.LayoutParams
                                                            (ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);  // width and height

                                    imageView_params.setMargins(5 ,5 ,5 ,5);

                                    // setting the postImageView
                                    postImageView.setLayoutParams(imageView_params);
                                    postImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                    postImageView.setImageBitmap(bitmap);


                                    // making the parameters for the description (textview) of image
                                    LinearLayout.LayoutParams des_params = new LinearLayout.LayoutParams
                                            (ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);

                                    des_params.setMargins(5 ,5 , 5, 5);

                                    postDescription.setLayoutParams(des_params);
                                    postDescription.setGravity(Gravity.CENTER);
                                    postDescription.setBackgroundColor(Color.BLUE);
                                    postDescription.setTextColor(Color.WHITE);
                                    postDescription.setTextSize(30f);


                                    // ADDED image AND description TO THE LINEAR LAYOUT
                                    linearLayout.addView(postImageView);
                                    linearLayout.addView(postDescription);
                                }
                            }
                        });
                    }
                }
                else
                {
                    FancyToast.makeText(UsersPost.this , recievedUserName + " doesn't have any post!" ,
                            FancyToast.LENGTH_SHORT , FancyToast.INFO, true).show();

                    finish();       // to finish this activity and g back to user Tab activity
                }

                dialog.dismiss();       // to dismiss the dialog box
            }
        });

    }
}
