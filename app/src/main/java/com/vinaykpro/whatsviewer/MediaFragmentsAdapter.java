package com.vinaykpro.whatsviewer;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextPaint;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.viewpager2.adapter.FragmentViewHolder;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.adapter.FragmentViewHolder;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class MediaFragmentsAdapter extends FragmentStateAdapter {
    List<String> mediaList;
    ViewPager2 pager;
    Context c;
    public MediaFragmentsAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, Context c, List<String> mediaList, ViewPager2 pager) {
        super(fragmentManager, lifecycle);
        this.mediaList = mediaList;
        this.c = c;
        this.pager = pager;
    }

    public  MediaImageViewerFragment mif = new MediaImageViewerFragment();
    public  MediaVideoViewerFragment mvf = new MediaVideoViewerFragment();
    public MediaAudioViewerFragment maf = new MediaAudioViewerFragment();
    //public ToolsFragment tf = new ToolsFragment();

    public Fragment getFragment(int position)
    {
        if(getItemViewType(position) == 0)
        {
            return mif;
        } else if(getItemViewType(position) == 1) {
            return mvf;
        } else {
            return  maf;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mediaList.get(position).contains(".jpg"))
        {
            return 0;
        } else if(mediaList.get(position).contains(".mp4"))
        {
            return 1;
        }
        return 2;
    }

    @Override
    public void onBindViewHolder(@NonNull FragmentViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if(pager.getCurrentItem()==position)
        {
            if(getItemViewType(position) == 0)
            {
                PhotoView mainimage = holder.itemView.findViewById(R.id.mediaviewerimagefragment);
                try { mainimage.setScale(mainimage.getMinimumScale()); } catch (Exception e) {
                    //Toast.makeText(c, e+"", Toast.LENGTH_SHORT).show();
                }
            } else if(getItemViewType(position) == 1)
            {
                try {
                    ImageView playButton = holder.itemView.findViewById(R.id.fragmentplaybuttonimg);
                    playButton.setImageResource(R.drawable.ic_play);
                } catch (Exception e) {
                    Toast.makeText(c, e+"", Toast.LENGTH_SHORT).show();
                }
                //VideoView videoView = holder.itemView.findViewById(R.id.videoView);
            } else {
                //Toast.makeText(c, "music", Toast.LENGTH_SHORT).show();
            }
        } else {
            if(getItemViewType(position) == 0)
            {
                PhotoView mainImage = holder.itemView.findViewById(R.id.mediaviewerimagefragment);
                try { mainImage.setScale(mainImage.getMinimumScale()); } catch (Exception e) {
                    //Toast.makeText(c, e+"", Toast.LENGTH_SHORT).show();
                }
            } else if(getItemViewType(position) == 1)
            {
                try { VideoView videoView = holder.itemView.findViewById(R.id.videoView);
                ImageView playButton = holder.itemView.findViewById(R.id.fragmentplaybuttonimg);
                if(videoView.isPlaying())
                {
                    playButton.callOnClick();
                } videoView.setAlpha(0.0f); } catch (Exception e) {
                    Toast.makeText(c, e+"", Toast.LENGTH_SHORT).show();
                }
            } else {
                try {
                    ImageView playButton = holder.itemView.findViewById(R.id.audiofragmentplaybutton);
                    if(MediaAudioViewerFragment.mediaPlayer.isPlaying())
                    {
                        MediaAudioViewerFragment.mediaPlayer.pause();
                        playButton.setImageResource(R.drawable.ic_play);
                    }
                } catch(Exception e) {
                    Toast.makeText(c, e+"", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(c, "music", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("path",mediaList.get(position));
        switch(getItemViewType(position)) {
            case 0:
                mif = new MediaImageViewerFragment();
                mif.setArguments(bundle);
                return mif;
            case 1:
                mvf = new MediaVideoViewerFragment();
                mvf.setArguments(bundle);
                return mvf;
        }
        maf = new MediaAudioViewerFragment();
        maf.setArguments(bundle);
        return maf;
        //return mif;
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }
}
