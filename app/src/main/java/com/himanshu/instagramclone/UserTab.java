package com.himanshu.instagramclone;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;


/**
 * A simple {@link Fragment} subclass.
 *
 */

                //  IMPLEMENTED ONiTEMcLICKlISTENER SUCH THAT WHEN WE TAP ON ANY ITEM (USER) WE GET ALL POSTS OF ITEM.
public class UserTab extends Fragment implements AdapterView.OnItemClickListener , AdapterView.OnItemLongClickListener{

    private ListView listView;
    private ArrayList<String> arrayList;

    private ArrayAdapter arrayAdapter;          // used as controller b/w listView and arrayList
    public UserTab()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user_tab, container, false);

        listView = view.findViewById(R.id.listView);
        listView.setOnItemClickListener(UserTab.this);      // calling on setOnItemClickListener() for the items of Listview
        listView.setOnItemLongClickListener(UserTab.this);      //  calling on setOnItemLongListener() for the items of ListView

        arrayList = new ArrayList();

        arrayAdapter = new ArrayAdapter(getContext() , android.R.layout.simple_list_item_1 , arrayList);        // as we want arrayAdapter to adapt the arrayList

        final TextView txtLoadingUsers = view.findViewById(R.id.txtLoadingUsers);


        final ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();

        parseQuery.whereNotEqualTo("username" , ParseUser.getCurrentUser().getUsername());      // as we don't query to contain the current user as
                                                                                               // it is already interacting with the app.

        parseQuery.findInBackground(new FindCallback<ParseUser>()           // to get data from server and save it to arrayList
        {
            @Override
            public void done(List<ParseUser> users, ParseException e)
            {
                if(e == null)
                {
                    if(users.size() > 0)
                    {
                        for(ParseUser user : users)
                        {
                            arrayList.add(user.getUsername());      // saving username of the object to the arrayList
                        }

                        listView.setAdapter(arrayAdapter);          // setting the adapter for the listView which is arrayAdapter which adapts ArrayList

                        // ONCE THE LISTVIEW HAS GOT THE DATA FROM THE SERVER WE WANT TEXTVIEW TO VANISH AND LISTVIEW TO BECOME VISIBLE.
                        txtLoadingUsers.animate().alpha(0).setDuration(2000);

                        listView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        return view;
    }

    // FOR THE TAPPING ON ITEMS (USERS)
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Intent intent = new Intent(getContext() , UsersPost.class);
        intent.putExtra("username" , arrayList.get(position));         // used to pass username from usertab activity to the userPost activity.(position refers to the index here).
        startActivity(intent);
    }


    // TO USE LONG HOLD FUNCTIONALITY ON ITEM (USERS) PRESENT IN USER TAB
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
    {
        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereEqualTo("username" , arrayList.get(position));

        parseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e)
            {
                if(user != null && e == null)
                {
//                    FancyToast.makeText(getContext() , user.get("profileProfession") + "" ,
//                            FancyToast.LENGTH_SHORT , FancyToast.SUCCESS , true).show();

                    final PrettyDialog prettyDialog = new PrettyDialog(getContext());         // using pretty dialog to display the content
                    prettyDialog.setTitle(user.getUsername() + "'s Info")
                            .setMessage(user.get("profileBio")  + "\n"
                                        + user.get("profileProfession") + "\n"
                                        + user.get("profileHobbies") + "\n"
                                        + user.get("profileFavSport"))
                            .setIcon(R.drawable.person)
                            .addButton("OK",   // button text
                                    R.color.pdlg_color_white,       // button text color
                                    R.color.pdlg_color_green,       // button background color
                                    new PrettyDialogCallback()      // button onClickListener()
                                    {
                                        @Override
                                        public void onClick()
                                        {
                                            prettyDialog.dismiss();     // when ok button is pressed
                                        }
                                    }

                            ).show();
                }
            }
        });
        return true;
    }
}
