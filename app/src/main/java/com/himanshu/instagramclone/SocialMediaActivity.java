package com.himanshu.instagramclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

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
}
