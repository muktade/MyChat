package com.example.mychat.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.mychat.fragment.CallsFragment;
import com.example.mychat.fragment.ChatsFragment;
import com.example.mychat.fragment.StatusFragment;

public class FragmentAdapter extends FragmentPagerAdapter {
    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public FragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new ChatsFragment();
            case 1:
                return new CallsFragment();
            case 2:
                return new StatusFragment();
            default:
                return new ChatsFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if(position==0){
            title="Chats";
        }
        if(position==1){
            title="Calls";
        }
        if(position==2){
            title="Status";
        }

        return title;
    }
}
