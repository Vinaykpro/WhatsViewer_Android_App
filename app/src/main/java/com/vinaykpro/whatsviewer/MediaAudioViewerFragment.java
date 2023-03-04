package com.vinaykpro.whatsviewer;

import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.SharedElementCallback;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MediaAudioViewerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MediaAudioViewerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageView playButton, background;
    SeekBar audioSeekbar;
    TextView fullDuration;
    public static MediaPlayer mediaPlayer;

    public MediaAudioViewerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MediaAudioViewerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MediaAudioViewerFragment newInstance(String param1, String param2) {
        MediaAudioViewerFragment fragment = new MediaAudioViewerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_media_audio_viewer, container, false);
        playButton = v.findViewById(R.id.audiofragmentplaybutton);
        background = v.findViewById(R.id.imageView23);
        audioSeekbar = v.findViewById(R.id.seekBar4);
        fullDuration = v.findViewById(R.id.fragmentfulltimeaudio);
        Handler handler = new Handler();
        Runnable updateTimer = new Runnable() {
            @Override
            public void run() {
                audioSeekbar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this,500);
            }
        };
        if (getArguments() != null) {
            String path = getArguments().getString("path");
            mediaPlayer = MediaPlayer.create(requireContext(), Uri.fromFile(new File(path)));
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    int duration = mediaPlayer.getDuration();
                    fullDuration.setText(convertToMMSS(duration+""));
                    audioSeekbar.setMax(duration);
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.seekTo(0);
                    audioSeekbar.setProgress(0);
                    handler.removeCallbacks(updateTimer);
                    playButton.setImageResource(R.drawable.ic_play);
                }
            });
        }

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer!=null) {
                    if(mediaPlayer.isPlaying()) { mediaPlayer.pause(); playButton.setImageResource(R.drawable.ic_play); handler.removeCallbacks(updateTimer); }
                    else { mediaPlayer.start(); playButton.setImageResource(R.drawable.ic_pause_round_white); handler.post(updateTimer); }
                }
            }
        });

        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playButton.callOnClick();
            }
        });

        audioSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeCallbacks(updateTimer);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                handler.post(updateTimer);
                mediaPlayer.seekTo(audioSeekbar.getProgress());
            }
        });


        return v;
      }

    public String convertToMMSS(String duration)
    {
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis)%TimeUnit.MINUTES.toSeconds(1));
    }

    }