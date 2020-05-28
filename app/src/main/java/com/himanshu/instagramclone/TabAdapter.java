package com.himanshu.instagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter
{

    public TabAdapter(@NonNull FragmentManager fm)      // constructor
    {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int tabPosition)      // return the fragments(or tabs) we're dealing with
    {
        switch(tabPosition)
        {
            case 0:
                ProfileTab profileTab = new ProfileTab();
                return profileTab;

                // return new ProfileTab()
            case 1 :
                return new UserTab();
            case 2 :
                return new SharePictureTab();
            default:
                return null;
        }
    }

    @Override
    public int getCount()           // return the count of the total fragments(or tab) used.
    {
        return 3;
    }


    // NOW ALSO MAKING THE TITLE FOR EACH TAB
    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        switch(position)
        {
            case 0 :
                return "Profile";
            case 1 :
                return "User";
            case 2 :
                return "Share Picture";
            default :
                return  null;
        }
    }
}
