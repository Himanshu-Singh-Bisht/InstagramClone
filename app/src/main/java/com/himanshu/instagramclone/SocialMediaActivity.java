package com.himanshu.instagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;

public class SocialMediaActivity extends AppCompatActivity
{

    private Toolbar toolbar;            // make sure you make object of that Toolbar which is used in the xml file.
    private ViewPager viewPager;
    private TabLayout tabLayout;            // added dependency by alt+shift+enter
    private TabAdapter tabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);

        setTitle("Social Media App");       // setting title of the Activity.

        toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);       // setting the toolbar as the action bar of the activity.

        viewPager =findViewById(R.id.viewPager);
        tabAdapter = new TabAdapter(getSupportFragmentManager());   // as the constructor was containing fm(Fragment Manager) type value
                                                                    // in tab adapter class
        viewPager.setAdapter(tabAdapter);                       // setting the tabAdapter for the viewPager

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager , false);        // used to set tablayout with viewPager
    }



    // FOR CREATING MENU AT THE ACTION BAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.my_menu , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId() == R.id.postImageItem)
        {
            // now checking if the permission is granted or not to access the external storage for the image.
            if(android.os.Build.VERSION.SDK_INT >= 23 &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]
                        {Manifest.permission.READ_EXTERNAL_STORAGE} , 3000);
            }
            else
            {
                captureImage();
            }
        }

        else if(item.getItemId() == R.id.logoutUserItem) // for logout functionality
        {
            ParseUser.getCurrentUser().logOut();
            finish();

            Intent intent = new Intent(SocialMediaActivity.this , SignUp.class);
            startActivity(intent);          // to get back to signUp page on logging out.
        }
        return super.onOptionsItemSelected(item);
    }


    // TO CHECK IF PERMISSION TO ACCESS THE EXTERNAL STORAGE IS GRANTED OR NOT.

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 3000)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                captureImage();
            }
        }
    }

    // METHOD TO CAPTURE IMAGE
    private void captureImage()
    {
        Intent intent = new Intent(Intent.ACTION_PICK , MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent , 4000);
    }

    // TO SEE THE RESULT OF THE ACTIVITY OF CAPTUREIMAGE()
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 4000 && resultCode == RESULT_OK && data != null)          // as data can't be null
        {
            try {

                Uri captureImage = data.getData();

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), captureImage);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

                byte[] bytes = byteArrayOutputStream.toByteArray();



                // now image can be uploaded to server so code it.

                ParseFile parseFile = new ParseFile("img.PNG", bytes);     // making parse file

                ParseObject parseObject = new ParseObject("Photo");
                parseObject.put("picture", parseFile);         // putting the parsFile containing image into picture column of Photo class
//                parseObject.put("image_des", edtDescription.getText().toString());

                parseObject.put("username", ParseUser.getCurrentUser().getUsername());     // setting the username column for this class(Photo).

                //  MAKING DIALOG BOX WHICH WILL SHOWED WHILE IMAGE IS UPLOADING.
                final ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage("Loading...");
                dialog.show();

                // CHECKING IF THE IMAGE IS UPLOADED OR NOT.
                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            FancyToast.makeText(SocialMediaActivity.this, "Picture Uploaded!", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                        } else {
                            FancyToast.makeText(SocialMediaActivity.this, "Unknown Error : " + e.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                        }

                        dialog.dismiss();   // to dismiss dialog box
                    }
                });
            }
            catch (Exception e)
            {
                e.getStackTrace();
            }
        }
    }
}
