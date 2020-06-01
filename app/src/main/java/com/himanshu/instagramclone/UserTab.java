package com.himanshu.instagramclone;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserTab extends Fragment {

    private ListView listView;
    private ArrayList arrayList;

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
}
