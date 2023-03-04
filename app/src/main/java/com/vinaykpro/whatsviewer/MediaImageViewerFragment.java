package com.vinaykpro.whatsviewer;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.google.android.gms.ads.nativead.MediaView;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MediaImageViewerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MediaImageViewerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public PhotoView mainimage;
    FrameLayout background;
    View clicktriggerer;
    WindowInsetsControllerCompat compat;
    LinearLayout extraMessageLayout;
    TextView extraMessage;

    public MediaImageViewerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ImageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MediaImageViewerFragment newInstance(String param1, String param2) {
        MediaImageViewerFragment fragment = new MediaImageViewerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String path = null;
        if (getArguments() != null) {
            path = getArguments().getString("path");
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        /*if(path!=null)
        {
            try {
                Glide.with(requireActivity()).load(new File(path)).into((ImageView) requireView().findViewById(R.id.mediaviewerimagefragment));
            } catch (Exception e) {
                Toast.makeText(requireActivity(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        }*/
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_media_image_viewer, container, false);
        mainimage = v.findViewById(R.id.mediaviewerimagefragment);
        background = v.findViewById(R.id.imgfragfullbackground);
        extraMessageLayout = v.findViewById(R.id.imagemessagelayout);
        extraMessage = v.findViewById(R.id.textView27);
        //compat = new WindowInsetsControllerCompat(requireActivity().getWindow(),requireActivity().getWindow().getDecorView());
        compat = WindowCompat.getInsetsController(requireActivity().getWindow(),requireActivity().getWindow().getDecorView());
        //clicktriggerer = v.findViewById(R.id.view2);
        mainimage.setMaximumScale(10.0f);
        mainimage.setMediumScale(2.5f);

        final boolean[] fullscreen = {false};

        PhotoViewAttacher photoViewAttacher = mainimage.getAttacher();
        mainimage.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
                if(!extraMessage.getText().equals("")) {
                    if (extraMessageLayout.getAlpha() == 0.0f) {
                        ((MediaViewerActivity) requireActivity()).showToolbar();
                        extraMessageLayout.animate().alpha(1.0f);
                    } else {
                        ((MediaViewerActivity) requireActivity()).hideToolbar();
                        extraMessageLayout.animate().alpha(0.0f);
                    }
                } else {
                   extraMessageLayout.setAlpha(0.0f);
                    ((MediaViewerActivity) requireActivity()).ToggleFullScreenMode();
                }
                //((MediaViewerActivity)requireActivity()).ToggleFullScreenMode();
                //if(requireActivity().getWindow().fl)
                /*if (!isStatusBarVisible()) {
                    requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    compat.hide(WindowInsetsCompat.Type.systemBars());
                    ((MediaViewerActivity)requireActivity()).hideToolbar();
                    extramessagelayout.animate().alpha(0.0f);
                } else {
                    requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    compat.show(WindowInsetsCompat.Type.systemBars());
                    ((MediaViewerActivity)requireActivity()).showToolbar();
                    extramessagelayout.animate().alpha(1.0f);
                }*/

                /*if(fullscreen[0])
                {
                    fullscreen[0] = false;
                    //View.SYSTEM_UI_FLAG_FULLSCREEN;
                    //compat.hide(WindowInsetsCompat.Type.systemBars());
                    ((MediaViewerActivity)requireActivity()).hideToolbar();
                    extramessagelayout.animate().alpha(0.0f);
                    //requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
                } else {
                    fullscreen[0] = true;
                    //compat.show(WindowInsetsCompat.Type.systemBars());
                    ((MediaViewerActivity)requireActivity()).showToolbar();
                    extramessagelayout.animate().alpha(1.0f);
                    //requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                }*/
                /*if(fullscreen[0])
                {
                    fullscreen[0] = false;
                } else {
                    fullscreen[0] = true;
                }*/
                
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent ev) {
                if (photoViewAttacher == null)
                    return false;
                try {
                    float scale = photoViewAttacher.getScale();
                    float x = ev.getX();
                    float y = ev.getY();

                    if (scale < photoViewAttacher.getMediumScale()) {
                        photoViewAttacher.setScale(photoViewAttacher.getMediumScale(), x, y, true);
                    } else {
                        photoViewAttacher.setScale(photoViewAttacher.getMinimumScale(), x, y, true);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    // Can sometimes happen when getX() and getY() is called
                    //Timer.e(e, "Error during double tab");
                }

                return true;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent motionEvent) {
                return false;
            }
        });
        String path = null;
        if (getArguments() != null) {
            path = getArguments().getString("path");
        }
        //path = "storage/emulated/0/Download/Crop1.jpg";
        if(path!=null)
        {
            try {
                Glide.with(requireActivity().getApplicationContext()).load(new File(path)).into(mainimage);
                } catch (Exception e) {
                Toast.makeText(requireActivity(), e.toString(), Toast.LENGTH_SHORT).show();
            }
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            {
            /*ContentResolver resolver = requireContext().getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,"Testimg");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE,"image/jpeg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH,"Pictures/WhatsViewer");

            Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);*/
                Uri uri = getImageContentUri(requireContext().getApplicationContext(),new File(path));
                Toast.makeText(requireActivity(), uri+"", Toast.LENGTH_SHORT).show();
                Glide.with(requireActivity().getApplicationContext()).load(uri).into(mainimage);
            }
        }
        /*assert this.getFragmentManager() != null;
        this.getFragmentManager().beginTransaction().setReorderingAllowed(true).addSharedElement(mainimage,mainimage.getTransitionName()).replace(container.getId(),new MediaImageViewerFragment(),MediaImageViewerFragment.class.getSimpleName()).addToBackStack(null).commit();
        */

        final View decorView = requireActivity().getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(
                new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int i) {
                        int height = decorView.getHeight();
                        //Log.i(TAG, "Current height: " + height);
                    }
                });

        return v;
    }

    public boolean isStatusBarVisible()
    {
        /*Rect rect = new Rect();
        Window window = requireActivity().getWindow();
        window.getDecorView().
        int height = rect.height();*/
        //return height != 0;
        return (requireActivity().getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN;
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }
}