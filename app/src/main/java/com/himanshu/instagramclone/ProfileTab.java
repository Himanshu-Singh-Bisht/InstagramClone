package com.himanshu.instagramclone;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileTab extends Fragment
{
    // UI COMPONENTS USED BY THE PROFILE TAB FRAGMENT
    private EditText edtProfileName , edtProfileBio , edtProfileProfession , edtProfileHobbies , edtProfileFavSport;
    private Button btnUpdateInfo;

    public ProfileTab()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // return inflater.inflate(R.layout.fragment_profile_tab, container, false);
        View view = inflater.inflate(R.layout.fragment_profile_tab, container, false);

        // now initialise the components of the fragment

        edtProfileName = view.findViewById(R.id.edtProfileName);        // as here view will contain the reference to all the UI components present in fragment.
        edtProfileBio = view.findViewById(R.id.edtProfileBio);
        edtProfileProfession = view.findViewById(R.id.edtProfileProfession);
        edtProfileHobbies = view.findViewById(R.id.edtProfileHobbies);
        edtProfileFavSport = view.findViewById(R.id.edtProfileFavSport);

        btnUpdateInfo = view.findViewById(R.id.btnUpdateInfo);

        final ParseUser parseUser = ParseUser.getCurrentUser();       // gives the current parse user


//        edtProfileName.setText(parseUser.get("profileName")+ "");
//        edtProfileBio.setText(parseUser.get("profileBio") + "");
//        edtProfileProfession.setText(parseUser.get("profileProfession") + "");
//        edtProfileHobbies.setText(parseUser.get("profileHobbies") + "");
//        edtProfileFavSport.setText(parseUser.get("profileFavSport") + "");

        // IF THE USER ALREADY UPDATED THE VALUES CORRESPONDING TO THE EDIT TEXT THEN IT WILL SHOW THOSE UPDATED VALUES OTHERWISE EMPTY STRING
        if(parseUser.get("profileName") == null)        // IF VALUE RETURNED FROM SERVER IS NULL CORRESPONDING TO THE KEY
        {
            edtProfileName.setText("");
        }
        else
        {
            edtProfileName.setText(parseUser.get("profileName").toString());
        }

        if(parseUser.get("profileBio") == null)
        {
            edtProfileBio.setText("");
        }
        else
        {
            edtProfileBio.setText(parseUser.get("profileBio") + "");
        }

        if(parseUser.get("profileProfession") == null)
        {
            edtProfileProfession.setText("");
        }
        else
        {
            edtProfileProfession.setText(parseUser.get("profileProfession") + "");
        }

        if(parseUser.get("profileHobbies") == null)
        {
            edtProfileHobbies.setText("");
        }
        else
        {
            edtProfileHobbies.setText(parseUser.get("profileHobbies") + "");
        }

        if(parseUser.get("profileFavSport") == null)
        {
            edtProfileFavSport.setText("");
        }
        else
        {
            edtProfileFavSport.setText(parseUser.get("profileFavSport") + "");
        }


        // setting onClickListener
        btnUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                parseUser.put("profileName" , edtProfileName.getText().toString());       // value of Profile name updated on dashbboard for current parse user.
                parseUser.put("profileBio" , edtProfileBio.getText().toString());
                parseUser.put("profileProfession" , edtProfileProfession.getText().toString());
                parseUser.put("profileHobbies" , edtProfileHobbies.getText().toString());
                parseUser.put("profileFavSport" ,edtProfileFavSport.getText().toString());

                parseUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e)
                    {
                        if(e == null)
                        {
                            FancyToast.makeText(getContext() , "Info is updated" , Toast.LENGTH_SHORT , FancyToast.INFO , true).show();
                        }
                        else
                        {
                            FancyToast.makeText(getContext() , e.getMessage() , Toast.LENGTH_SHORT , FancyToast.ERROR , true).show(); // error message
                        }
                    }
                });
            }
        });

        return view;
    }
}
