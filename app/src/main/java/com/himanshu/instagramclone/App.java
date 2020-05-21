package com.himanshu.instagramclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        // INITILIAZING THE PARSE SERVER WITH APPLICATIONID , CLIENTKEY AND SERVER
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("kYmRCmfZeCOVPttAgUtPubeWTkUvTQ7IVaHBzfww")      // APPLICATION ID PASTED FROM THE BACK4APP SERVER SETTINGS.
                // if defined
                .clientKey("4CiXXxKGRKYV3iHQVklUQgAz8PcYIUkHpDyfrAMF")          // CLIENTKEY PASTED FROM THE BACK4APP SERVER SETTINGS.
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
