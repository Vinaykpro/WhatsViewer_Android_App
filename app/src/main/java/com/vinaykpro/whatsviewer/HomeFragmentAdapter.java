package com.vinaykpro.whatsviewer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class HomeFragmentAdapter extends FragmentStateAdapter {
    public HomeFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {
            case 1:
                return new StatusFragment();
            case 2:
                return new CallsFragment();
        }
        return new ChatsFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
