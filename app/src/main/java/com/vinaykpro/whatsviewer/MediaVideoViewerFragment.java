package com.vinaykpro.whatsviewer;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MediaVideoViewerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MediaVideoViewerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageView mainimage,playbutton;
    VideoView videoView;
    ConstraintLayout extramessagelayout;
    SeekBar videoSeekbar;
    TextView fullDuration,currentDuration;

    public MediaVideoViewerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MediaVideoViewerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MediaVideoViewerFragment newInstance(String param1, String param2) {
        MediaVideoViewerFragment fragment = new MediaVideoViewerFragment();
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
        View v = inflater.inflate(R.layout.fragment_media_video_viewer, container, false);
        mainimage = v.findViewById(R.id.imageView27);
        videoView = v.findViewById(R.id.videoView);
        playbutton = v.findViewById(R.id.fragmentplaybuttonimg);
        extramessagelayout = v.findViewById(R.id.videomessagelayout);
        videoSeekbar = v.findViewById(R.id.seekBar4);
        fullDuration = v.findViewById(R.id.fragmentfulltimevideo);
        currentDuration = v.findViewById(R.id.fragmentcurrtimevideo);
        Handler handler = new Handler();
        if (getArguments() != null) {
            String path = getArguments().getString("path");
            videoView.setAlpha(0.0f);
            videoView.setVideoURI(Uri.fromFile(new File(path)));
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    int duration = videoView.getDuration();
                    fullDuration.setText(convertToMMSS(duration + ""));
                    videoSeekbar.setMax(duration);
                }

            });
            try {
                new Handler().postDelayed(new Runnable() {
                                              @Override
                                              public void run() {
                                                  Glide.with(requireContext()).load(new File(path)).into(mainimage);
                                              }
                                          }
                        , 300);
            } catch (Exception e) {
                Toast.makeText(requireContext(), e + "", Toast.LENGTH_SHORT).show();
            }
        }
            //videoView;
            //videoView.start();


        Runnable r = new Runnable() {
            @Override
            public void run() {
                if(extramessagelayout.getAlpha() != 0.0f)
                    mainimage.callOnClick();
            }
        };

        Runnable updateTimer = new Runnable() {
            @Override
            public void run() {
                int currPos = videoView.getCurrentPosition();
                currentDuration.setText(convertToMMSS(currPos+""));
                videoSeekbar.setProgress(currPos);
                handler.postDelayed(this,500);
            }
        };


        mainimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(extramessagelayout.getAlpha() == 0.0f)
                {
                    ((MediaViewerActivity)requireActivity()).showToolbar();
                    extramessagelayout.animate().alpha(1.0f);
                    //if(videoView.isPlaying()) {playbutton.animate().alpha(1.0f);}
                    playbutton.animate().alpha(1.0f);
                } else {
                    ((MediaViewerActivity)requireActivity()).hideToolbar();
                    /*if(videoView.isPlaying()) */{playbutton.animate().alpha(0.0f);}
                    extramessagelayout.animate().alpha(0.0f);
                }
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                playbutton.setImageResource(R.drawable.ic_play);
                ((MediaViewerActivity)requireActivity()).showToolbar();
                extramessagelayout.animate().alpha(1.0f);
                videoSeekbar.setProgress(0);
                if(playbutton.getAlpha() != 1.0f) {playbutton.setAlpha(1.0f);}
                videoView.seekTo(0);
                handler.removeCallbacks(updateTimer);
            }
        });


        playbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(playbutton.getAlpha() != 0.0f) {
                    if (videoView.isPlaying()) {
                        playbutton.setImageResource(R.drawable.ic_play);
                        videoView.pause();
                        handler.removeCallbacks(updateTimer);
                        handler.removeCallbacks(r);
                    } else {
                        playbutton.setImageResource(R.drawable.ic_pause_round_white);
                        if (videoView.getAlpha() == 0.0f) {
                            videoView.setAlpha(1.0f);
                        }
                        videoView.start();
                        handler.post(updateTimer);
                        handler.postDelayed(r,500);
                    }

                } else {
                    mainimage.callOnClick();
                }
            }
        });

        videoSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
                videoView.seekTo(videoSeekbar.getProgress());
            }
        });

        return v;
    }

    public static Uri getImageContentUri(Context context, File videoFile) {
        String filePath = videoFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Video.Media._ID },
                MediaStore.Video.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (videoFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Video.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public String convertToMMSS(String duration)
    {
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis)%TimeUnit.MINUTES.toSeconds(1));
    }
}