package com.vinaykpro.whatsviewer;

import static android.content.Context.MODE_PRIVATE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Adapter extends RecyclerView.Adapter {

    List<String> messageList;
    List<String> colorList;
    String userName1;
    String userName2;

    String chatName;

    public Context context;
    public static int count = 0;
    public static boolean issearching = false;
    public boolean isNightMode = false;
    public int membercount = 0;
    int leastindex = 0;
    int nearestone = 0;
    Spinner sendernamechoosingspinner;
    LinearLayout selectsenderlayout;
    ConstraintLayout chatmenulayout,editmessagelayout,searchlayout,maininputlayout,blacklayoutselectsender;
    LinearLayoutManager linearLayoutManager;
    List<SentViewHolder> selectedsitems = new ArrayList<>();
    List<SentMediaViewHolder> selectedmediasitems = new ArrayList<>();
    List<RecievedViewHolder> selectedritems = new ArrayList<>();
    List<RecievedMediaViewHolder> selectedmediaritems = new ArrayList<>();
    List<String> indexes = new ArrayList<>();
    List<String> searchedIndexes = new ArrayList<>();
    List<String> deletableitems = new ArrayList<>();
    List<String> groupnamesforadapter = new ArrayList<>();
    boolean selectionmode = false,editmode = false;
    boolean isthisagroup = false;
    Vibrator v;
    ClipboardManager clipboard;
    ClipData cliptext;
    InputMethodManager inputMethodManager;
    PopupMenu popupMenu;
    MySqllite database;
    List<String> groupmemebernames;
    MediaPlayer audioPlayer;
    int currentAudioIndex = -1;

    DisplayMetrics displayMetrics;

    float dpHeight;
    float dpWidth;

    int maxwidthinpx;
    int maxheightinpx;

    ImageView backbtn,edit,copy,delete,info,editmsgbackbtn,searchlayoutbackbtn,searchlayoutupbutton,searchlayoutdownbutton,threedots,gotolastmessage;
    EditText editmsgedittext,searchlayoutedittext;

    TextView selectedcount,editmsgupdate;
    String tablename;

    public static int triggeredposition = -1;
    String searchedword = "";
    private Handler myHandler;

    // exportChat layout views
    TextView usernamepreviewexportchat,senderName,recieverName,exportChatToHtmlBtn;
    Switch showSenderName,showRecieverName;
    ImageView senderNameTick,chatNameTick,lightModeIcon,DarkModeIcon;
    RadioButton lightModeRadioBtn,darkModeRadioBtn;
    ConstraintLayout lightModeBg,darkModeBg,senderNameEditLayout,showSenderNameBg,showRecieverNameBg;
    EditText senderNameEditText,chatNameEditText;

    ConstraintLayout exportChatLayout; static ImageView exportChatCloseBtn;

    boolean showSenderNameBool=false,showRecieverNameBool=false,isChatThemeDark=false;

    List<String> datesFormatList;
    String selectedDateFormat;

    String[] months = new String[] {"January","February","March","April","May","June","July","August","September","October","November","December"};

    ConstraintLayout dtl1,dtl2,dtl3,dtl4,dtl5,dtl6;
    TextView dt1,dt2,dt3,dt4,dt5,dt6;
    ImageView dateTick;

    ImageView expLock; ProgressBar expBtnLoading;
    boolean isChatUnlocked = false;

    private RewardedAd mRewardAd;


    public Adapter(List<String> messageList,String tablename,String fname,String sname,String chatname,Context context,ConstraintLayout chatmenulayout, ImageView backbtn, ImageView edit, ImageView copy, ImageView delete, ImageView info,TextView selectedcount,ConstraintLayout editmessagelayout,ImageView editmsgbackbtn,EditText editmsgedittext,TextView editmsgupdate,ConstraintLayout searchlayout,ImageView searchlayoutbackbtn,EditText searchlayoutedittext,ImageView searchlayoutupbutton,ImageView searchlayoutdownbutton,LinearLayoutManager linearLayoutManager,TextView usernamepreviewexportchat,TextView senderName,TextView recieverName, Switch showSenderName,Switch showRecieverName, ImageView senderNameTick,ImageView chatNameTick,ImageView lightModeIcon,ImageView DarkModeIcon, RadioButton lightModeRadioBtn,RadioButton darkModeRadioBtn, ConstraintLayout lightModeBg,ConstraintLayout darkModeBg,ConstraintLayout senderNameEditLayout, EditText senderNameEditText,EditText chatNameEditText,ConstraintLayout exportChatLayout,ImageView exportChatCloseBtn, ConstraintLayout showSenderNameBg, ConstraintLayout showRecieverNameBg,TextView exportChatToHtmlBtn,List<String> datesList) {
        this.messageList = messageList;
        this.userName1 = fname;
        this.userName2= sname;
        this.chatName = chatname;
        this.context = context;
        this.chatmenulayout = chatmenulayout;
        this.backbtn = backbtn;
        this.edit = edit;
        this.copy = copy;
        this.delete = delete;
        this.info = info;
        this.selectedcount = selectedcount;
        this.tablename = tablename;
        this.editmessagelayout = editmessagelayout;
        this.editmsgbackbtn = editmsgbackbtn;
        this.editmsgedittext = editmsgedittext;
        this.editmsgupdate = editmsgupdate;
        this.searchlayout = searchlayout;
        this.searchlayoutbackbtn = searchlayoutbackbtn;
        this.searchlayoutedittext = searchlayoutedittext;
        this.searchlayoutupbutton = searchlayoutupbutton;
        this.searchlayoutdownbutton = searchlayoutdownbutton;
        this.linearLayoutManager = linearLayoutManager;
        this.usernamepreviewexportchat = usernamepreviewexportchat;
        this.senderName = senderName;
        this.recieverName = recieverName;
        this.showSenderName = showSenderName;
        this.showRecieverName = showRecieverName;
        this.senderNameTick = senderNameTick;
        this.chatNameTick = chatNameTick;
        this.lightModeIcon = lightModeIcon;
        this.DarkModeIcon = DarkModeIcon;
        this.lightModeRadioBtn = lightModeRadioBtn;
        this.darkModeRadioBtn = darkModeRadioBtn;
        this.lightModeBg = lightModeBg;
        this.darkModeBg = darkModeBg;
        this.senderNameEditLayout = senderNameEditLayout;
        this.senderNameEditText = senderNameEditText;
        this.chatNameEditText = chatNameEditText;
        this.exportChatLayout = exportChatLayout;
        this.exportChatCloseBtn = exportChatCloseBtn;
        this.showSenderNameBg = showSenderNameBg;
        this.showRecieverNameBg = showRecieverNameBg;
        this.exportChatToHtmlBtn = exportChatToHtmlBtn;
        this.datesFormatList = datesList;
        this.dtl1 = ((MainActivity)context).findViewById(R.id.dtl1);
        this.dtl2 = ((MainActivity)context).findViewById(R.id.dtl2);
        this.dtl3 = ((MainActivity)context).findViewById(R.id.dtl3);
        this.dtl4 = ((MainActivity)context).findViewById(R.id.dtl4);
        this.dtl5 = ((MainActivity)context).findViewById(R.id.dtl5);
        this.dtl6 = ((MainActivity)context).findViewById(R.id.dtl6);
        this.dt1 = ((MainActivity)context).findViewById(R.id.dt1);
        this.dt2 = ((MainActivity)context).findViewById(R.id.dt2);
        this.dt3 = ((MainActivity)context).findViewById(R.id.dt3);
        this.dt4 = ((MainActivity)context).findViewById(R.id.dt4);
        this.dt5 = ((MainActivity)context).findViewById(R.id.dt5);
        this.dt6 = ((MainActivity)context).findViewById(R.id.dt6);
        this.dateTick = ((MainActivity)context).findViewById(R.id.imageView34);
        this.expLock = ((MainActivity)context).findViewById(R.id.imageView35);
        this.expBtnLoading = ((MainActivity)context).findViewById(R.id.progressBar2);
        myHandler = new Handler();
        displayMetrics = ((MainActivity)context).getResources().getDisplayMetrics();
        dpHeight = displayMetrics.heightPixels/displayMetrics.density;
        dpWidth = displayMetrics.widthPixels/displayMetrics.density;
        maxwidthinpx = (int)(displayMetrics.widthPixels*0.7);
        maxheightinpx = (int)(displayMetrics.heightPixels*0.5);
        gotolastmessage = ((MainActivity)context).findViewById(R.id.gotolastmessage);
        sendernamechoosingspinner = ((MainActivity)context).findViewById(R.id.spinner);
        selectsenderlayout = ((MainActivity)context).findViewById(R.id.choosesenderlayout);
        blacklayoutselectsender = ((MainActivity)context).findViewById(R.id.blanklayout);
        maininputlayout = ((MainActivity)context).findViewById(R.id.relativeLayout);
        database = new MySqllite(context);
        threedots = ((MainActivity) context).findViewById(R.id.three_dots);
        popupMenu = new PopupMenu(context,threedots);
        popupMenu.inflate(R.menu.chat_popup_menu);
        inputMethodManager = (InputMethodManager) ((MainActivity) this.context).getSystemService(Context.INPUT_METHOD_SERVICE);
        v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        clipboard = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
        colorList = new ArrayList<>();

        //groupmemebernames = new ArrayList<>();
        membercount = database.getusercount(tablename);
        if(membercount != 2) {
            isthisagroup = true;
            groupmemebernames = database.getUserNames(tablename);
            ArrayAdapter<String> membersadapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,groupmemebernames);
            membersadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sendernamechoosingspinner.setAdapter(membersadapter);

            sendernamechoosingspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String s = adapterView.getItemAtPosition(i).toString();
                    if (!s.equals(userName1)) {
                        groupmemebernames.remove(s + "");
                        if (!groupmemebernames.contains(userName1)) {
                            groupmemebernames.add(userName1);
                        }
                        userName1 = s;
                        notifyDataSetChanged();
                    }
                     blacklayoutselectsender.setVisibility(View.GONE);
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) { }
            });

            blacklayoutselectsender.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    blacklayoutselectsender.setVisibility(View.GONE);
                    return true;
                }
            });

            Random random = new Random();
            for(int i=0;i<groupmemebernames.size();i++) {
                String colorCode;
                if(isNightMode) {
                    colorCode = String.format("#%06X", (int) (Math.random() * 0xAAAAAA) + 0x555555);
                } else {
                    colorCode = String.format("#%06X", (int) (Math.random() * 0x888888) + 0x777777);
                }
                colorList.add(colorCode);
            }
            //membersradiogroup.add
            //Toast.makeText(context, "count : "+database.getusercount(tablename)+" listsize : "+groupmemebernames.get(0), Toast.LENGTH_SHORT).show();
        }

        if(database.isTableExists(tablename+"dates")) {
            datesFormatList = database.getChatDates(tablename);

            List<String> allAvailableFormats = new ArrayList<>();
            allAvailableFormats.add("day/month/year");
            allAvailableFormats.add("day/year/month");
            allAvailableFormats.add("month/day/year");
            allAvailableFormats.add("month/year/day");
            allAvailableFormats.add("year/month/day");
            allAvailableFormats.add("year/day/month");

            selectedDateFormat = database.getProfilePicture(tablename);
            TextView otherFormatsView = (TextView)((MainActivity)context).findViewById(R.id.otherformatstext);

            int i=0;
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) dateTick.getLayoutParams();

            for(;i<datesFormatList.size();i++) {
                String dateF = datesFormatList.get(i);
                allAvailableFormats.remove(dateF);
                switch (i) {
                    case 0:
                        ((TextView) ((MainActivity) context).findViewById(R.id.dt1)).setText(dateF);
                        if (selectedDateFormat.equals(dateF)) {
                            deselectAllDateFormats();
                            dtl1.setBackground(AppCompatResources.getDrawable(context, R.drawable.green_selected_bg));
                            params.topToTop = R.id.dtl1;
                            params.bottomToBottom = R.id.dtl1;
                            params.startToStart = R.id.dtl1;
                            dateTick.setLayoutParams(params);
                        }
                        if (i == datesFormatList.size() - 1) {
                            ConstraintLayout.LayoutParams textParams = (ConstraintLayout.LayoutParams) otherFormatsView.getLayoutParams();
                            textParams.topToBottom = R.id.dtl1;
                            otherFormatsView.setLayoutParams(textParams);
                        }
                        break;
                    case 1:
                        ((TextView) ((MainActivity) context).findViewById(R.id.dt2)).setText(dateF);
                        if (selectedDateFormat.equals(dateF)) {
                            deselectAllDateFormats();
                            dtl2.setBackground(AppCompatResources.getDrawable(context, R.drawable.green_selected_bg));
                            params.topToTop = R.id.dtl2;
                            params.bottomToBottom = R.id.dtl2;
                            params.startToStart = R.id.dtl2;
                            dateTick.setLayoutParams(params);
                        }
                        if (i == datesFormatList.size() - 1) {
                            ConstraintLayout.LayoutParams textParams = (ConstraintLayout.LayoutParams) otherFormatsView.getLayoutParams();
                            textParams.topToBottom = R.id.dtl2;
                            otherFormatsView.setLayoutParams(textParams);
                        }
                        break;
                    case 2:
                        ((TextView) ((MainActivity) context).findViewById(R.id.dt3)).setText(dateF);
                        if (selectedDateFormat.equals(dateF)) {
                            deselectAllDateFormats();
                            dtl3.setBackground(AppCompatResources.getDrawable(context, R.drawable.green_selected_bg));
                            params.topToTop = R.id.dtl3;
                            params.bottomToBottom = R.id.dtl3;
                            params.startToStart = R.id.dtl3;
                            dateTick.setLayoutParams(params);
                        }
                        if (i == datesFormatList.size() - 1) {
                            ConstraintLayout.LayoutParams textParams = (ConstraintLayout.LayoutParams) otherFormatsView.getLayoutParams();
                            textParams.topToBottom = R.id.dtl3;
                            otherFormatsView.setLayoutParams(textParams);
                        }
                        break;
                    case 3:
                        ((TextView) ((MainActivity) context).findViewById(R.id.dt4)).setText(dateF);
                        if (selectedDateFormat.equals(dateF)) {
                            deselectAllDateFormats();
                            dtl4.setBackground(AppCompatResources.getDrawable(context, R.drawable.green_selected_bg));
                            params.topToTop = R.id.dtl4;
                            params.bottomToBottom = R.id.dtl4;
                            params.startToStart = R.id.dtl4;
                            dateTick.setLayoutParams(params);
                        }
                        if (i == datesFormatList.size() - 1) {
                            ConstraintLayout.LayoutParams textParams = (ConstraintLayout.LayoutParams) otherFormatsView.getLayoutParams();
                            textParams.topToBottom = R.id.dtl4;
                            otherFormatsView.setLayoutParams(textParams);
                        }
                        break;
                    case 4:
                        ((TextView) ((MainActivity) context).findViewById(R.id.dt5)).setText(dateF);
                        if (selectedDateFormat.equals(dateF)) {
                            deselectAllDateFormats();
                            dtl5.setBackground(AppCompatResources.getDrawable(context, R.drawable.green_selected_bg));
                            params.topToTop = R.id.dtl5;
                            params.bottomToBottom = R.id.dtl5;
                            params.startToStart = R.id.dtl5;
                            dateTick.setLayoutParams(params);
                        }
                        if (i == datesFormatList.size() - 1) {
                            ConstraintLayout.LayoutParams textParams = (ConstraintLayout.LayoutParams) otherFormatsView.getLayoutParams();
                            textParams.topToBottom = R.id.dtl5;
                            otherFormatsView.setLayoutParams(textParams);
                        }
                        break;
                    case 5:
                        ((TextView) ((MainActivity) context).findViewById(R.id.dt6)).setText(dateF);
                        if (selectedDateFormat.equals(dateF)) {
                            deselectAllDateFormats();
                            dtl6.setBackground(AppCompatResources.getDrawable(context, R.drawable.green_selected_bg));
                            params.topToTop = R.id.dtl6;
                            params.bottomToBottom = R.id.dtl6;
                            params.startToStart = R.id.dtl6;
                            dateTick.setLayoutParams(params);
                        }
                        otherFormatsView.setVisibility(View.GONE);
                        break;
                }
            }

            if(i<6) {
                int j = 0;
                for (; i < 6; i++) {
                    String dateF = allAvailableFormats.get(j);
                    switch (i) {
                        case 0:
                            ((TextView) ((MainActivity) context).findViewById(R.id.dt1)).setText(dateF);
                            if (selectedDateFormat.equals(dateF)) {
                                deselectAllDateFormats();
                                dtl1.setBackground(AppCompatResources.getDrawable(context, R.drawable.green_selected_bg));
                                params.topToTop = R.id.dtl1;
                                params.bottomToBottom = R.id.dtl1;
                                params.startToStart = R.id.dtl1;
                                dateTick.setLayoutParams(params);
                            }
                            if(j==0) {
                                ConstraintLayout.LayoutParams lParams = (ConstraintLayout.LayoutParams) dtl1.getLayoutParams();
                                lParams.topToBottom = R.id.otherformatstext;
                                dtl1.setLayoutParams(lParams);
                            }
                            break;
                        case 1:
                            ((TextView) ((MainActivity) context).findViewById(R.id.dt2)).setText(dateF);
                            if (selectedDateFormat.equals(dateF)) {
                                deselectAllDateFormats();
                                dtl2.setBackground(AppCompatResources.getDrawable(context, R.drawable.green_selected_bg));
                                params.topToTop = R.id.dtl2;
                                params.bottomToBottom = R.id.dtl2;
                                params.startToStart = R.id.dtl2;
                                dateTick.setLayoutParams(params);
                            }
                            if(j==0) {
                                ConstraintLayout.LayoutParams lParams = (ConstraintLayout.LayoutParams) dtl2.getLayoutParams();
                                lParams.topToBottom = R.id.otherformatstext;
                                dtl2.setLayoutParams(lParams);
                            }
                            break;
                        case 2:
                            ((TextView) ((MainActivity) context).findViewById(R.id.dt3)).setText(dateF);
                            if (selectedDateFormat.equals(dateF)) {
                                deselectAllDateFormats();
                                dtl3.setBackground(AppCompatResources.getDrawable(context, R.drawable.green_selected_bg));
                                params.topToTop = R.id.dtl3;
                                params.bottomToBottom = R.id.dtl3;
                                params.startToStart = R.id.dtl3;
                                dateTick.setLayoutParams(params);
                            }
                            if(j==0) {
                                ConstraintLayout.LayoutParams lParams = (ConstraintLayout.LayoutParams) dtl3.getLayoutParams();
                                lParams.topToBottom = R.id.otherformatstext;
                                dtl3.setLayoutParams(lParams);
                            }
                            break;
                        case 3:
                            ((TextView) ((MainActivity) context).findViewById(R.id.dt4)).setText(dateF);
                            if (selectedDateFormat.equals(dateF)) {
                                deselectAllDateFormats();
                                dtl4.setBackground(AppCompatResources.getDrawable(context, R.drawable.green_selected_bg));
                                params.topToTop = R.id.dtl4;
                                params.bottomToBottom = R.id.dtl4;
                                params.startToStart = R.id.dtl4;
                                dateTick.setLayoutParams(params);
                            }
                            if(j==0) {
                                ConstraintLayout.LayoutParams lParams = (ConstraintLayout.LayoutParams) dtl4.getLayoutParams();
                                lParams.topToBottom = R.id.otherformatstext;
                                dtl4.setLayoutParams(lParams);
                            }
                            break;
                        case 4:
                            ((TextView) ((MainActivity) context).findViewById(R.id.dt5)).setText(dateF);
                            if (selectedDateFormat.equals(dateF)) {
                                deselectAllDateFormats();
                                dtl5.setBackground(AppCompatResources.getDrawable(context, R.drawable.green_selected_bg));
                                params.topToTop = R.id.dtl5;
                                params.bottomToBottom = R.id.dtl5;
                                params.startToStart = R.id.dtl5;
                                dateTick.setLayoutParams(params);
                            }
                            if(j==0) {
                                ConstraintLayout.LayoutParams lParams = (ConstraintLayout.LayoutParams) dtl5.getLayoutParams();
                                lParams.topToBottom = R.id.otherformatstext;
                                dtl5.setLayoutParams(lParams);
                            }
                            break;
                        case 5:
                            ((TextView) ((MainActivity) context).findViewById(R.id.dt6)).setText(dateF);
                            if (selectedDateFormat.equals(dateF)) {
                                deselectAllDateFormats();
                                dtl6.setBackground(AppCompatResources.getDrawable(context, R.drawable.green_selected_bg));
                                params.topToTop = R.id.dtl6;
                                params.bottomToBottom = R.id.dtl6;
                                params.startToStart = R.id.dtl6;
                                dateTick.setLayoutParams(params);
                            }
                            if(j==0) {
                                ConstraintLayout.LayoutParams lParams = (ConstraintLayout.LayoutParams) dtl6.getLayoutParams();
                                lParams.topToBottom = R.id.otherformatstext;
                                dtl6.setLayoutParams(lParams);
                            }
                            break;
                    }
                    j++;
                }
            } else {
                otherFormatsView.setVisibility(View.GONE);
            }

        }

        isNightMode = getNightMode();
        isChatThemeDark = isNightMode;
    }

    private String getMessage(String s) {
        int a = s.indexOf("-");
        int b = s.indexOf(":",a);
        return s.substring(b+2);
    }

    private String getName(String s) {
        int a = s.indexOf("-");
        int b = s.indexOf(":",a);

        return s.substring(a+2,b);
    }

    private boolean canigetMessage(String s) {
        int a = s.indexOf("-");
        int b = s.indexOf(":",a);
        return a != -1 && b != -1;
    }

    private boolean canigetName(String s) {
        int a = s.indexOf("-");
        int b = s.indexOf(":",a);
        return a != -1 && b != -1;
    }

    private boolean canigetTime(String s) {
        int a = s.indexOf(",");
        int b = s.indexOf("-",a);
        return (a!=-1 && b!=-1 );
    }

    private String getTime(String s) {
        int a = s.indexOf(",");
        int b = s.indexOf("-", a);
        return s.substring(a + 2, b - 1);
    }

    private String getDate(String s) {
        return s.substring(0,s.indexOf(","));
    }


    @Override
    public int getItemViewType(int position) {
        String name;
        boolean isMedia = false;
        if(canigetName(messageList.get(position)))
        {
            name = getName(messageList.get(position));
            String message = getMessage(messageList.get(position));
            if(message.contains("(file attached)"))
            {
                //isMedia = true;
            }
        }
        else
        {
            return 2;
        }

        if (name.equals(userName1)) {
            if(isMedia)
                return 3;
            return  0;
        }
        if(isthisagroup) {
            if (groupmemebernames.contains(name)) {
                if(isMedia)
                    return 4;
                return  1;
            } else {
                return 2;
            }
        } else if(name.equals(userName2)) {
            if(isMedia)
                return 4;
            return  1;
        }
            return 2;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;

        /*Setting last message on home screen for chat*/
        if(canigetName(messageList.get(messageList.size()-1)) && canigetTime(messageList.get(messageList.size()-1))) {
            String lastname = getName(messageList.get(messageList.size() - 1));
            String lastmessage = getMessage(messageList.get(messageList.size() - 1));
            String lastmessagetime = getTime(messageList.get(messageList.size() - 1));
            if(lastmessage.length() >30) {
                lastmessage = lastmessage.replaceAll("\n","");
                lastmessage = lastmessage.substring(0, 30) + "...";
            }

            try {
                if (lastname.equals(userName1)) {
                    database.updateblueticks(tablename, 1);
                } else {
                    database.updateblueticks(tablename, 0);
                }
            } catch(Exception e) {}

            try {
                database.updatelastmessage(tablename, lastmessage);
                database.updatelastmessagetime(tablename, lastmessagetime);
            } catch(Exception e) {}
        }


        ((MainActivity)context).findViewById(R.id.formatdateslayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.animate().alpha(0.0f).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.GONE);
                        view.animate().setListener(null);
                    }
                });
            }
        });

        threedots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu.show();
            }
        });

        gotolastmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayoutManager.scrollToPosition(messageList.size()-1);
            }
        });

        showSenderName.setChecked(showSenderNameBool);
        showRecieverName.setChecked(showRecieverNameBool);



        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.viewcontact:
                        Intent i = new Intent(context,ProfileActivity.class);
                        i.putExtra("tablename",tablename);
                        i.putExtra("username",database.getname(tablename));
                        i.putExtra("lastseen",database.getlastseen(tablename));
                        i.putExtra("usercount",database.getusercount(tablename));
                        context.startActivity(i);
                        break;
                    case R.id.swapusers:
                        if(membercount == 2) {
                            String temp = userName1;
                            userName1 = userName2;
                            userName2 = temp;
                            database.updatefirstname(tablename, userName1);
                            database.updatesecondname(tablename, userName2);
                            notifyDataSetChanged();
                            Toast.makeText(context, "Swapped Sender and Reciever", Toast.LENGTH_SHORT).show();
                        } else {
                            blacklayoutselectsender.setVisibility(View.VISIBLE);
                        }

                        break;
                    case R.id.search:
                        issearching = true;
                        maininputlayout.setVisibility(View.GONE);
                        searchlayout.setVisibility(View.VISIBLE);
                        //Toast.makeText(context, "Search", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.exportchat:
                        usernamepreviewexportchat.setText(chatName);
                        senderName.setText(userName1);
                        if(isthisagroup) {
                            showRecieverName.setChecked(true);
                            showRecieverName.setEnabled(false);
                            recieverName.setVisibility(View.VISIBLE);
                            recieverName.setText(("Member Name"));
                        } else {
                            recieverName.setText(userName2);
                        }
                        senderNameEditText.setText(userName1);
                        chatNameEditText.setText(chatName);

                        // opening export chat layout
                        exportChatLayout.setVisibility(View.VISIBLE);
                        exportChatLayout.setTranslationY((float)displayMetrics.heightPixels);
                        exportChatLayout.animate().translationY(0.0f).setInterpolator(new FastOutSlowInInterpolator()).setDuration(300);
                        return true;
                    case R.id.gotofirstmessage:
                        linearLayoutManager.scrollToPosition(0);
                        //Toast.makeText(context, "Go to first message", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.fixdates:
                        if(selectedDateFormat==null)
                        {
                            Toast.makeText(context,"This feature is only available for new chats, please reimport to use this feature.",Toast.LENGTH_LONG).show();
                            break;
                        }
                        ConstraintLayout l = ((MainActivity)context).findViewById(R.id.formatdateslayout);
                        l.setAlpha(0.0f);
                        l.setVisibility(View.VISIBLE);
                        l.animate().alpha(1.0f);
                        break;
                    case R.id.clearchat:
                        //Toast.makeText(context, "Clear chat", Toast.LENGTH_SHORT).show();
                        File f = new File(((MainActivity) context).getApplicationContext().getCacheDir()+"/"+tablename+"dp.png");
                        if(f.exists()) { f.delete(); }
                        database.deleteChat(tablename);
                        ((MainActivity) context).finish();
                        return true;
                    default:
                        return false;
                }
                return false;
            }
        });

        showSenderNameBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!showSenderNameBool) {
                    senderName.setVisibility(View.VISIBLE);
                    senderNameEditLayout.setVisibility(View.VISIBLE);
                } else {
                    senderName.setVisibility(View.GONE);
                    senderNameEditLayout.setVisibility(View.GONE);
                }
                showSenderNameBool = !showSenderNameBool;
                showSenderName.setChecked(showSenderNameBool);
            }
        });

        showRecieverNameBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isthisagroup) { return; }

                if(!showRecieverNameBool) {
                    recieverName.setVisibility(View.VISIBLE);
                } else {
                    recieverName.setVisibility(View.GONE);
                }
                showRecieverNameBool = !showRecieverNameBool;
                showRecieverName.setChecked(showRecieverNameBool);
            }
        });

        senderNameTick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                senderName.setText(senderNameEditText.getText());
            }
        });

        chatNameTick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recieverName.setText(chatNameEditText.getText());
                usernamepreviewexportchat.setText(chatNameEditText.getText());
            }
        });

        if(isNightMode) {
            lightModeIcon.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_light));
            DarkModeIcon.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_dark));
        } else {
            lightModeIcon.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_light2));
            DarkModeIcon.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_dark2));
        }

        lightModeBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isChatThemeDark = false;
                lightModeBg.setBackground(AppCompatResources.getDrawable(context,R.drawable.green_selected_bg));
                lightModeRadioBtn.setChecked(true);
                darkModeBg.setBackground(null);
                darkModeRadioBtn.setChecked(false);
            }
        });

        darkModeBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isChatThemeDark = true;
                lightModeBg.setBackground(null);
                lightModeRadioBtn.setChecked(false);
                darkModeBg.setBackground(AppCompatResources.getDrawable(context, R.drawable.green_selected_bg));
                darkModeRadioBtn.setChecked(true);
            }
        });


        exportChatCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportChatLayout.animate().translationY((float)displayMetrics.heightPixels).setInterpolator(new FastOutSlowInInterpolator()).setDuration(300);
            }
        });

        exportChatToHtmlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(isChatUnlocked) {
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            exportThisChat();
                        }
                    });
                    t.start();
                    ((MainActivity) context).findViewById(R.id.exportingscreen).setVisibility(View.VISIBLE);
                    expLock.setVisibility(View.VISIBLE);
                    isChatUnlocked = false;
                    exportChatToHtmlBtn.setText("Export Chat to HTML [Watch Ad]");
                } else {
                    loadRewardAd();
                }
            }
        });

        dtl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deselectAllDateFormats();
                dtl1.setBackground(AppCompatResources.getDrawable(context,R.drawable.green_selected_bg));
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) dateTick.getLayoutParams();
                params.topToTop = R.id.dtl1;
                params.bottomToBottom = R.id.dtl1;
                params.startToStart = R.id.dtl1;
                dateTick.setLayoutParams(params);
                database.updateProfilePicture(tablename,dt1.getText().toString());
                ((MainActivity)context).selectedDateFormat = dt1.getText().toString();
                selectedDateFormat = dt1.getText().toString();
                notifyDataSetChanged();
            }
        });

        dtl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deselectAllDateFormats();
                dtl2.setBackground(AppCompatResources.getDrawable(context,R.drawable.green_selected_bg));
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) dateTick.getLayoutParams();
                params.topToTop = R.id.dtl2;
                params.bottomToBottom = R.id.dtl2;
                params.startToStart = R.id.dtl2;
                dateTick.setLayoutParams(params);
                database.updateProfilePicture(tablename,dt2.getText().toString());
                ((MainActivity)context).selectedDateFormat = dt2.getText().toString();
                selectedDateFormat = dt2.getText().toString();
                notifyDataSetChanged();
            }
        });

        dtl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deselectAllDateFormats();
                dtl3.setBackground(AppCompatResources.getDrawable(context,R.drawable.green_selected_bg));
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) dateTick.getLayoutParams();
                params.topToTop = R.id.dtl3;
                params.bottomToBottom = R.id.dtl3;
                params.startToStart = R.id.dtl3;
                dateTick.setLayoutParams(params);
                database.updateProfilePicture(tablename,dt3.getText().toString());
                ((MainActivity)context).selectedDateFormat = dt3.getText().toString();
                selectedDateFormat = dt3.getText().toString();
                notifyDataSetChanged();
            }
        });

        dtl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deselectAllDateFormats();
                dtl4.setBackground(AppCompatResources.getDrawable(context,R.drawable.green_selected_bg));
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) dateTick.getLayoutParams();
                params.topToTop = R.id.dtl4;
                params.bottomToBottom = R.id.dtl4;
                params.startToStart = R.id.dtl4;
                dateTick.setLayoutParams(params);
                database.updateProfilePicture(tablename,dt4.getText().toString());
                ((MainActivity)context).selectedDateFormat = dt4.getText().toString();
                selectedDateFormat = dt4.getText().toString();
                notifyDataSetChanged();
            }
        });

        dtl5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deselectAllDateFormats();
                dtl5.setBackground(AppCompatResources.getDrawable(context,R.drawable.green_selected_bg));
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) dateTick.getLayoutParams();
                params.topToTop = R.id.dtl5;
                params.bottomToBottom = R.id.dtl5;
                params.startToStart = R.id.dtl5;
                dateTick.setLayoutParams(params);
                database.updateProfilePicture(tablename,dt5.getText().toString());
                selectedDateFormat = dt5.getText().toString();
                ((MainActivity)context).selectedDateFormat = dt5.getText().toString();
                notifyDataSetChanged();
            }
        });

        dtl6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deselectAllDateFormats();
                dtl6.setBackground(AppCompatResources.getDrawable(context,R.drawable.green_selected_bg));
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) dateTick.getLayoutParams();
                params.topToTop = R.id.dtl6;
                params.bottomToBottom = R.id.dtl6;
                params.startToStart = R.id.dtl6;
                dateTick.setLayoutParams(params);
                database.updateProfilePicture(tablename,dt6.getText().toString());
                selectedDateFormat = dt6.getText().toString();
                ((MainActivity)context).selectedDateFormat = dt6.getText().toString();
                notifyDataSetChanged();
            }
        });



        backbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if(count > 0) {
                    for(int i=0;i<selectedsitems.size();i++) {
                        selectedsitems.get(i).isselected = false;
                        selectedsitems.get(i).fullbackgroundlayout.setForeground(null);
                    }
                    for(int i=0;i<selectedmediasitems.size();i++)
                    {
                        selectedmediasitems.get(i).isselected = false;
                        selectedmediasitems.get(i).fullbackgroundlayout.setForeground(null);
                    }
                    for(int i=0;i<selectedritems.size();i++) {
                        selectedritems.get(i).isselected = false;
                        selectedritems.get(i).fullbackgroundlayout.setForeground(null);
                    }
                    for(int i=0;i<selectedmediaritems.size();i++)
                    {
                        selectedmediaritems.get(i).isselected = false;
                        selectedmediaritems.get(i).fullbackgroundlayout.setForeground(null);
                    }
                    selectionmode = false;
                    count = 0;
                    selectedsitems.clear();
                    selectedritems.clear();
                    selectedmediasitems.clear();
                    selectedmediaritems.clear();
                    indexes.clear();
                    triggeredposition = -1;
                    searchedword = "";
                    editmessagelayout.setVisibility(View.GONE);
                    hideThisViewByAlpha(chatmenulayout);
                    searchlayout.setVisibility(View.GONE);
                }
                if(issearching) {
                    issearching = false;
                    triggeredposition = -1;
                    searchedIndexes.clear();
                    maininputlayout.setVisibility(View.VISIBLE);
                    searchlayout.setVisibility(View.GONE);
                }
            }
        });

        copy.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                String s = "";
                if(count > 0) {
                    Collections.sort(indexes,new Comparator<String>() {
                        @Override
                        public int compare(String s, String t1) {
                            return Integer.parseInt(s) - Integer.parseInt(t1);
                        }
                    });
                    int totalC = indexes.size();
                    for(int i=0;i<totalC;i++) {
                        String ss = messageList.get(Integer.parseInt(indexes.get(i)));
                        if((i+1)>=totalC)
                            s += getMessage(ss);
                        else
                            s += getMessage(ss)+"\n";
                    }
                    /*int x = (int)Math.floor(Math.random()*100);
                    if(x<=75) {
                        //((MainActivity) context).showInterstitialAd();
                    }*/
                    cliptext = ClipData.newPlainText("copied messages",s);
                    clipboard.setPrimaryClip(cliptext);
                    if(count==1)
                        Toast.makeText(context,"Message copied",Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(context,count+" Messages copied",Toast.LENGTH_SHORT).show();
                    backbtn.callOnClick();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Collections.sort(indexes,new Comparator<String>() {
                    @Override
                    public int compare(String s, String t1) {
                        return Integer.parseInt(s) - Integer.parseInt(t1);
                    }
                });

                try {
                    int count2 = 0;
                    for (int i = 0; i < indexes.size(); i++) {
                        deletableitems.add(messageList.get(Integer.parseInt(indexes.get(i)) - count2));
                        messageList.remove(Integer.parseInt(indexes.get(i)) - count2);
                        count2++;
                        notifyItemRemoved(Integer.parseInt(indexes.get(i)) - count2 + 1);
                    }
                    /*int x = (int)Math.floor(Math.random()*100);
                    if(x<=75) {
                        //((MainActivity) context).showInterstitialAd();
                    }*/
                    if (count == 1)
                        Toast.makeText(context, "Message deleted", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(context, count + " Messages deleted", Toast.LENGTH_SHORT).show();
                    try { deletefromtable(deletableitems); } catch(Exception e) {}
                    backbtn.callOnClick();
                    backbtn.setClickable(false);
                } catch (Exception e) {}
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count == 1) {
                    editmode = true;
                    hideThisViewByAlpha(chatmenulayout);
                    editmessagelayout.setVisibility(View.VISIBLE);
                }
            }
        });

        editmsgupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editmsgedittext.getText().toString().equals("") && count == 1 && editmode) {
                    try {
                        String s = messageList.get(Integer.parseInt(indexes.get(0)));
                        String newmsg = getDate(s) + ", " + getTime(s) + " - " + getName(s) + ": " + editmsgedittext.getText().toString();
                        try { updatemessage(s, newmsg); } catch (Exception e) {}
                        messageList.set(Integer.parseInt(indexes.get(0)), newmsg);
                        notifyItemChanged(Integer.parseInt(indexes.get(0)));
                        backbtn.callOnClick();
                    } catch (Exception e) {}
                }
            }
        });

        editmsgbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editmode = false;
                backbtn.callOnClick();
            }
        });

        //editmsgedittext.setImeActionLabel("Search",KeyEvent.ACTION_DOWN);
        searchlayoutedittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEARCH) {
                    String text = searchlayoutedittext.getText().toString().toLowerCase();
                    if (!(text.equals("")) && notempty(text)) {
                        Thread searchThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                searchedIndexes.clear();
                                for (int j = 0; j < messageList.size(); j++) {
                                    String s = messageList.get(j);
                                    if (canigetMessage(s) && canigetName(s) && canigetTime(s)) {
                                        s = getMessage(s);
                                        if(s.toLowerCase().contains(text)) {
                                            searchedIndexes.add(String.valueOf(j));
                                        }
                                    } else if (s.toLowerCase().contains(text) && s.length() <= 20 && s.split(" ").length == 3) {
                                        searchedIndexes.add(String.valueOf(j));
                                    }
                                }
                                ((MainActivity) context).runOnUiThread(new Runnable() {
                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                    @Override
                                    public void run() {
                                        if (searchedIndexes.size() == 0) {
                                            if(triggeredposition != -1) {
                                                triggeredposition = -1;
                                            }
                                            Toast.makeText(context, "No results found", Toast.LENGTH_SHORT).show();
                                        } else {
                                            leastindex = 0;
                                            nearestone = messageList.size();
                                            for (int i = 0; i < searchedIndexes.size(); i++) {
                                                int currentvisibleindex = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                                                int currentsearchedindex = Integer.parseInt(searchedIndexes.get(i));
                                                if (currentvisibleindex > currentsearchedindex) {
                                                    if (nearestone > currentvisibleindex - currentsearchedindex) {
                                                        nearestone = currentvisibleindex - currentsearchedindex;
                                                        leastindex = i;
                                                    }
                                                } else {
                                                    if (nearestone > currentsearchedindex - currentvisibleindex) {
                                                        nearestone = currentsearchedindex - currentvisibleindex;
                                                        leastindex = i;
                                                    }
                                                }
                                            }
                                            linearLayoutManager.scrollToPositionWithOffset(Integer.parseInt(searchedIndexes.get(leastindex)),50);
                                            //linearLayoutManager.scrollToPosition(Integer.parseInt(searchedIndexes.get(leastindex)));
                                            notifyItemChanged(Integer.parseInt(searchedIndexes.get(leastindex)));
                                            triggeredposition = Integer.parseInt(searchedIndexes.get(leastindex));
                                            searchedword = text;
                                        }
                                        inputMethodManager.hideSoftInputFromWindow(((MainActivity) context).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                    }
                                });
                            }
                        });
                        searchThread.start();
                    }
                    return false;
                }
                return false;
            }
        });

        searchlayoutupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int max = searchedIndexes.size();
                if(leastindex > 0 && leastindex < max) {
                    leastindex--;
                    ((MainActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            linearLayoutManager.scrollToPositionWithOffset(Integer.parseInt(searchedIndexes.get(leastindex)),50);
                            notifyItemChanged(Integer.parseInt(searchedIndexes.get(leastindex)));
                            //linearLayoutManager.scrollToPosition(Integer.parseInt(searchedIndexes.get(leastindex)));
                            triggeredposition = Integer.parseInt(searchedIndexes.get(leastindex));
                        }
                    });
                } else if(triggeredposition != -1){
                    Toast.makeText(context, "No results above", Toast.LENGTH_SHORT).show();
                }
            }
        });

        searchlayoutdownbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int max = searchedIndexes.size();
                if (leastindex >= 0 && leastindex < max - 1) {
                    leastindex++;
                    ((MainActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            linearLayoutManager.scrollToPositionWithOffset(Integer.parseInt(searchedIndexes.get(leastindex)),50);
                            notifyItemChanged(Integer.parseInt(searchedIndexes.get(leastindex)));
                            //linearLayoutManager.scrollToPosition(Integer.parseInt(searchedIndexes.get(leastindex)));
                            triggeredposition = Integer.parseInt(searchedIndexes.get(leastindex));
                        }
                    });
                } else if(triggeredposition != -1){
                    Toast.makeText(context, "No results below", Toast.LENGTH_SHORT).show();
                }
            }
        });

        searchlayoutbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchlayoutedittext.setText("");
                try {
                    inputMethodManager.hideSoftInputFromWindow(((MainActivity) context).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (Exception e) {}
                backbtn.callOnClick();
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, linearLayoutManager.findFirstVisibleItemPosition()+"", Toast.LENGTH_SHORT).show();
            }
        });

        if (viewType == 0) {
            view = layoutInflater.inflate(R.layout.new_sender_message_bg, parent, false);
            return new SentViewHolder(view);
        } else if (viewType == 1) {
            view = layoutInflater.inflate(R.layout.new_reciever_message_bg, parent, false);
            return new RecievedViewHolder(view);
        } else if(viewType == 2){
            view = layoutInflater.inflate(R.layout.note_middle, parent, false);
            return new NoteViewHolder(view);
        }  else if(viewType == 3){
            view = layoutInflater.inflate(R.layout.new_sender_media_message_bg, parent, false);
            return new SentMediaViewHolder(view);
        }  else if(viewType == 4){
            view = layoutInflater.inflate(R.layout.new_reciever_media_message_bg, parent, false);
            return new RecievedMediaViewHolder(view);
        } else {
            view = layoutInflater.inflate(R.layout.note_middle, parent, false);
            return new NoteViewHolder(view);
        }
    }

    private void loadRewardAd() {
        exportChatToHtmlBtn.setText("Loading Ad...");
        expBtnLoading.setVisibility(View.VISIBLE);
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(((MainActivity)context), "ca-app-pub-2813592783630195/5974233711",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        exportChatToHtmlBtn.setText("Export Chat to HTML [Watch AD]");
                        expBtnLoading.setVisibility(View.GONE);
                        Toast.makeText(context, "Failed to Load an Ad make sure you have an active internet connection and try again", Toast.LENGTH_SHORT).show();
                        mRewardAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        mRewardAd = ad;
                        mRewardAd.show(((MainActivity)context), new OnUserEarnedRewardListener() {
                            @Override
                            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                exportChatToHtmlBtn.setText("Export Chat to HTML");
                                expBtnLoading.setVisibility(View.GONE);
                                expLock.setVisibility(View.GONE);
                                isChatUnlocked = true;
                            }
                        });
                        mRewardAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();
                                exportChatToHtmlBtn.callOnClick();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                super.onAdFailedToShowFullScreenContent(adError);
                                exportChatToHtmlBtn.setText("Export Chat to HTML [Watch AD]");
                                expBtnLoading.setVisibility(View.GONE);
                                Toast.makeText(context, "Failed to Show Ad make sure you have an active internet connection and try again", Toast.LENGTH_SHORT).show();
                                mRewardAd = null;
                            }
                        });
                    }
                });
    }

    private void deselectAllDateFormats() {
        dtl1.setBackground(null);
        dtl2.setBackground(null);
        dtl3.setBackground(null);
        dtl4.setBackground(null);
        dtl5.setBackground(null);
        dtl6.setBackground(null);
    }


    private void exportThisChat() {
        String startData = "<html> <head> <title>Chat Exported by WhatsViewer</title> <meta charset=\"UTF-8\"> <meta http-equiv=\"X-UA-Compatible\" content=\"IE=Edge\"> <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, user-scalable=no\">\n" +
                "\n" +
                "<style>\n" +
                "    html {\n" +
                "  box-sizing: border-box;\n" +
                "  height: 100%;\n" +
                "  margin: 0;\n" +
                "  padding: 0;\n" +
                "}\n" +
                "\n" +
                ".chat {\n" +
                "\n" +
                "}\n" +
                "\n" +
                ".chat-container {\n" +
                "\n" +
                "}\n" +
                "\n" +
                "body {\n" +
                "  -webkit-font-smoothing: antialiased;\n" +
                "  -moz-osx-font-smoothing: grayscale;\n" +
                "  font-family: \"Roboto\", sans-serif;\n" +
                "  margin: 0;\n" +
                "  padding: 0;\n" +
                "  height: 100%;\n" +
                "  background:#e7dcd2;\n" +
                "}\n" +
                "\n" +
                ".user-bar {\n" +
                "  height: 55px;\n" +
                "  background: #005e54;\n" +
                "  color: #fff;\n" +
                "  font-size: 24px;\n" +
                "  position: absolute;\n" +
                "  width:100%;\n" +
                "  z-index: 1;\n" +
                "}\n" +
                "\n" +
                ".user-bar .avatar {\n" +
                "  margin: 0 0 0 5px;\n" +
                "  padding-top:9px;\n" +
                "  width: 36px;\n" +
                "  height: 36px;\n" +
                "  margin-left:12px;\n" +
                "}\n" +
                "\n" +
                ".user-bar .avatar svg {\n" +
                "  border-radius: 50%;\n" +
                "  box-shadow: 0 1px 0 rgba(255, 255, 255, 0.1);\n" +
                "  display: inline;width: 100%;\n" +
                "}\n" +
                "\n" +
                ".user-bar .name {\n" +
                "  position:absolute;\n" +
                "  top:10px;\n" +
                "  left:50px;\n" +
                "  font-size: 17px;\n" +
                "  font-weight: 600;\n" +
                "  text-overflow: ellipsis;\n" +
                "  letter-spacing: 0.3px;\n" +
                "  margin: 0 0 0 8px;\n" +
                "  overflow: hidden;\n" +
                "  white-space: nowrap;\n" +
                "  width: auto;\n" +
                "}\n" +
                "\n" +
                ".user-bar .status {\n" +
                "  margin-left:1px;\n" +
                "  display: block;\n" +
                "  font-size: 13px;\n" +
                "  font-weight: 400;\n" +
                "  letter-spacing: 0;\n" +
                "  width:auto;\n" +
                "}\n" +
                "\n" +
                "\n" +
                ".actionsbar {\n" +
                "    position:absolute;\n" +
                "    height:55px;\n" +
                "    top:0;\n" +
                "    right:0;\n" +
                "}\n" +
                "\n" +
                "\n" +
                ".actionsbar svg {\n" +
                "    height:25px;\n" +
                "    width:25px;\n" +
                "    margin:15px;\n" +
                "    margin-left:5px;\n" +
                "    margin-right:8px;\n" +
                "}\n" +
                "\n" +
                "#tdots {\n" +
                "    margin-left:0px;\n" +
                "}\n" +
                "\n" +
                ".conversation {\n" +
                "  position: absolute;\n" +
                "  background: #e7dcd2\n" +
                "  top:110px;\n" +
                "  z-index: 0;\n" +
                "  padding:0px;\n" +
                "}\n" +
                "\n" +
                ".conversation ::-webkit-scrollbar {\n" +
                "  transition: all .5s;\n" +
                "  width: 5px;\n" +
                "  height: 1px;\n" +
                "  z-index: 10;\n" +
                "}\n" +
                "\n" +
                ".conversation ::-webkit-scrollbar-track {\n" +
                "  background: transparent;\n" +
                "}\n" +
                "\n" +
                ".conversation ::-webkit-scrollbar-thumb {\n" +
                "  background: #b3ada7;\n" +
                "}\n" +
                "\n" +
                ".conversation .conversation-container {\n" +
                "  position: relative;\n" +
                "  top:55px;\n" +
                "  box-shadow: inset 0 10px 10px -10px #000000;\n" +
                "  overflow-x: hidden;\n" +
                "  padding:0px 16px;\n" +
                "  margin-bottom: 5px;\n" +
                "  height:calc(100vh - 115px);\n" +
                "  z-index:3;\n" +
                "  width:calc(100vw - 32px);\n" +
                "}\n" +
                "\n" +
                "\n" +
                ".note {\n" +
                "  clear: both;\n" +
                "  line-height: 18px;\n" +
                "  padding: 6px;\n" +
                "  padding-left: 8px;\n" +
                "  position: relative;\n" +
                "  margin-top: 5px;\n" +
                "  padding: 8px;\n" +
                "  text-align: center;\n" +
                "  background-color: #f7f7f0;\n" +
                "  border-radius: 10px;\n" +
                "  font-size: 12px;\n" +
                "  font-weight: 300;\n" +
                "  width: fit-content;\n" +
                "  max-width: 80vw;\n" +
                "  margin-left: auto;\n" +
                "  margin-right: auto;\n" +
                "}\n" +
                "\n" +
                ".message {\n" +
                "  color: #000;\n" +
                "  clear: both;\n" +
                "  line-height: 18px;\n" +
                "  font-size: 15px;\n" +
                "  padding: 6px;\n" +
                "  padding-left:8px;\n" +
                "  position: relative;\n" +
                "  margin: 2px 0;\n" +
                "  margin-top:5px;\n" +
                "  max-width: 80%;\n" +
                "  word-wrap: break-word;\n" +
                "}\n" +
                "\n" +
                ".message2 {\n" +
                "  color: #000;\n" +
                "  clear: both;\n" +
                "  line-height: 18px;\n" +
                "  font-size: 15px;\n" +
                "  padding: 6px;\n" +
                "  padding-left:8px;\n" +
                "  position: relative;\n" +
                "  margin: 2px 0;\n" +
                "  margin-top:0.5px;\n" +
                "  max-width: 80%;\n" +
                "  word-wrap: break-word;\n" +
                "}\n" +
                "\n" +
                ".message:after {\n" +
                "  position: absolute;\n" +
                "  content: \"\";\n" +
                "  width: 0;\n" +
                "  height: 0;\n" +
                "  border-style: solid;\n" +
                "}\n" +
                "\n" +
                ".metadata {\n" +
                "  display: inline-block;\n" +
                "  float: right;\n" +
                "  padding: 0 0 0 7px;\n" +
                "  position: relative;\n" +
                "  bottom: -4px;\n" +
                "}\n" +
                ".metadata .time {\n" +
                "  color: rgba(0, 0, 0, .45);\n" +
                "  font-size: 11px;\n" +
                "  display: inline-block;\n" +
                "}\n" +
                "\n" +
                ".metadata .tick {\n" +
                "  display: inline-block;\n" +
                "  margin-left: 2px;\n" +
                "  position: relative;\n" +
                "  top: 4px;\n" +
                "  height: 16px;\n" +
                "  width: 16px;\n" +
                "  background-image: url(\"data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='15'%3E%3Cpath d='M15.01 3.316l-.478-.372a.365.365 0 0 0-.51.063L8.666 9.88a.32.32 0 0 1-.484.032l-.358-.325a.32.32 0 0 0-.484.032l-.378.48a.418.418 0 0 0 .036.54l1.32 1.267a.32.32 0 0 0 .484-.034l6.272-8.048a.366.366 0 0 0-.064-.512zm-4.1 0l-.478-.372a.365.365 0 0 0-.51.063L4.566 9.88a.32.32 0 0 1-.484.032L1.892 7.77a.366.366 0 0 0-.516.005l-.423.433a.364.364 0 0 0 .006.514l3.255 3.185a.32.32 0 0 0 .484-.033l6.272-8.048a.365.365 0 0 0-.063-.51z' fill='%234fc3f7'/%3E%3C/svg%3E\"); background-repeat: no-repeat;\n" +
                "}\n" +
                "\n" +
                ".metadata .tick svg:first-child {\n" +
                "  -webkit-backface-visibility: hidden;\n" +
                "          backface-visibility: hidden;\n" +
                "  -webkit-transform: perspective(800px) rotateY(180deg);\n" +
                "          transform: perspective(800px) rotateY(180deg);\n" +
                "}\n" +
                "\n" +
                ".metadata .tick svg:last-child {\n" +
                "  -webkit-backface-visibility: hidden;\n" +
                "          backface-visibility: hidden;\n" +
                "  -webkit-transform: perspective(800px) rotateY(0deg);\n" +
                "          transform: perspective(800px) rotateY(0deg);\n" +
                "}\n" +
                "\n" +
                ".metadata .tick-animation svg:first-child {\n" +
                "  -webkit-transform: perspective(800px) rotateY(0);\n" +
                "          transform: perspective(800px) rotateY(0);\n" +
                "}\n" +
                "\n" +
                ".metadata .tick-animation svg:last-child {\n" +
                "  -webkit-transform: perspective(800px) rotateY(-179.9deg);\n" +
                "transform: perspective(800px) rotateY(-179.9deg);\n" +
                "}\n" +
                "\n" +
                ".message:first-child {\n" +
                "  margin: 16px 0 8px;\n" +
                "}\n" +
                "\n" +
                ".message.received {\n" +
                "  background: #fff;\n" +
                "  border-radius: 0px 8px 8px 8px;\n" +
                "  float: left;\n" +
                "}\n" +
                "\n" +
                ".message2.received {\n" +
                "  background: #fff;\n" +
                "  border-radius: 8px 8px 8px 8px;\n" +
                "  float: left;\n" +
                "}\n" +
                "\n" +
                ".message.received .metadata {\n" +
                "  padding: 0 0 0 16px;\n" +
                "}\n" +
                "\n" +
                ".message.received:after {\n" +
                "  border-width: 0px 10px 10px 0;\n" +
                "  border-radius:5px 0px 0px 0px;\n" +
                "  border-color: transparent #fff transparent transparent;\n" +
                "  top: 0;left: -10px;\n" +
                "}\n" +
                "\n" +
                ".message.sent {\n" +
                "  background: #e1ffd8;\n" +
                "  border-radius: 8px 0px 8px 8px;\n" +
                "  float: right;\n" +
                "}\n" +
                "\n" +
                ".message2.sent {\n" +
                "  background: #e1ffd8;\n" +
                "  border-radius: 8px 8px 8px 8px;\n" +
                "  float: right;\n" +
                "}\n" +
                "\n" +
                ".message.sent:after {\n" +
                "  border-width: 0px 0px 10px 10px;\n" +
                "  border-radius:0px 5px 0px 0px;\n" +
                "  border-color: transparent transparent transparent #e1ffd8;\n" +
                "  top: 0;\n" +
                "  right: -9px;\n" +
                "}\n" +
                "\n" +
                ".marvel-device .status-bar {\n" +
                "    display: none;\n" +
                "  }\n" +
                "\n" +
                "\n" +
                ".convochatbg {\n" +
                "   top:calc(100vh - 60px);\n" +
                "   position: absolute;\n" +
                "   background: #e7dcd2;\n" +
                "   height:60px;\n" +
                "   bottom:0px;\n" +
                "   width: 100vw;\n" +
                "}\n" +
                "\n" +
                ".editedtag {\n" +
                "    color:grey;\n" +
                "}\n" +
                "\n" +
                "\n" +
                ".msginput { position: relative; background: #fff; height: 45px; top:7.5px; left:6.5px; margin-right: 66px; border-radius: 50px; } #mic { position: absolute; width: 47px; height: 47px; top:6.5px; right: 6.5px; } #emoji { position: absolute; height: 28px; width: 28px; top:8.5px; left:8.5px; } #messagetxt { position: absolute; color: #666; font-weight: 500; left:43px; top:-1.5px; } #camera { position: absolute; height: 28px; width: 28px; top:8.5px; right:8.5px; } #rupee { position: absolute; height: 28px; width: 28px; top:8.5px; right:49px; } #docs { position: absolute; height: 28px; width: 28px; top:8.5px; right:86.5px; }\n";


        String endCSS = "</style>\n";

        String endCSSDarkMode = "\n" +
                ".user-bar , .msginput , .message.sent , .message.received , .message2.sent , .message2.received , .note{\n" +
                "    background-color:#1b2c35;\n" +
                "    color:#fff;\n" +
                "}\n" +
                "\n" +
                "\n" +
                ".message.sent , .message2.sent {\n" +
                "  background-color: #005F4A;\n" +
                "}\n" +
                "\n" +
                ".note {\n" +
                "    color : #ddd;\n" +
                "}\n" +
                "\n" +
                ".editedtag {\n" +
                "    color:grey;\n" +
                "}\n" +
                "\n" +
                ".msginput {\n" +
                "    background-color:#1b2c35;\n" +
                "}\n" +
                "\n" +
                "body , .convochatbg {\n" +
                "    background:#000;\n" +
                "}\n" +
                "\n" +
                ".message.sent:after {\n" +
                "    border-color: transparent transparent transparent #005F4A;\n" +
                "}" +
                "\n" +
                ".message.received:after {\n" +
                "    border-color: transparent #1b2c35 transparent transparent;\n" +
                "}\n" +
                "\n" +
                ".metadata .time {\n" +
                "    color:#aaa;\n" +
                "}\n" +
                ".editedtag {\n" +
                "    color:#aaa;\n" +
                "}\n" +
                "</style>\n";

        String bodyStartTillName = "\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "\n" +
                "<div class=\"chat\">\n" +
                "     <div class=\"chat-container\">\n" +
                "            <div id=\"call\" class=\"user-bar\">\n" +
                "                <div class=\"back\">\n" +
                "                <i class=\"zmdi zmdi-arrow-left\"></i>\n" +
                "              </div>\n" +
                "              <div class=\"avatar\">\n" +
                "        <svg     version=\"1.1\"     xmlns=\"http://www.w3.org/2000/svg\"     xmlns:xlink=\"http://www.w3.org/1999/xlink\"     x=\"0%\" y=\"0%\"     width=\"100%\" height=\"100%\"     viewBox=\"0 0 24.0 24.0\"     enable-background=\"new 0 0 24.0 24.0\"     xml:space=\"preserve\">     <path         fill=\"#CDD8E0\"         stroke=\"#000000\"         fill-opacity=\"1.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"0.0\"         stroke-linejoin=\"miter\"         stroke-linecap=\"square\"         d=\"M11.98,0.19C18.54,0.19,23.86,5.51,23.86,12.07C23.86,18.63,18.54,23.95,11.98,23.95C5.42,23.95,0.10,18.63,0.10,12.07C0.10,5.51,5.42,0.19,11.98,0.19z\"/>     <path         fill=\"#FFFFFF\"         stroke=\"#000000\"         fill-opacity=\"1.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"0.0\"         stroke-linejoin=\"miter\"         stroke-linecap=\"square\"         d=\"M11.93,6.50C14.17,6.50,15.99,8.32,15.99,10.56C15.99,12.80,14.17,14.62,11.93,14.62C9.69,14.62,7.87,12.80,7.87,10.56C7.87,8.32,9.69,6.50,11.93,6.50z\"/>     <path         fill=\"#FFFFFF\"         stroke=\"#000000\"         fill-opacity=\"1.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"0.0\"         stroke-linejoin=\"miter\"         stroke-linecap=\"square\"         d=\"M4.21,20.40C5.45,15.85,15.91,13.33,19.60,20.42C16.60,24.14,8.16,24.74,4.21,20.40z\"/> </svg>\n" +
                "              </div>\n" +
                "              <div class=\"name\">\n" +
                "                <span id=\"name\">";
        // enters name
        String afterNameLastSeen = "</span>\n" +
                "                <span class=\"status\">";
        // enters last seen
        String afterLastSeenTillBody = "</span>\n" +
                "              </div>\n" +
                "              <div class=\"actionsbar\">\n" +
                "\n" +
                "\n" +
                "<svg     version=\"1.1\"     xmlns=\"http://www.w3.org/2000/svg\"     xmlns:xlink=\"http://www.w3.org/1999/xlink\"     x=\"0%\" y=\"0%\"     width=\"100%\" height=\"100%\"     viewBox=\"0 0 24.0 24.0\"     enable-background=\"new 0 0 24.0 24.0\"     xml:space=\"preserve\">     <path         fill=\"#FFFFFF\"         stroke=\"#000000\"         fill-opacity=\"1.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"0.0\"         stroke-linejoin=\"miter\"         stroke-linecap=\"square\"         d=\"M5.03,6.56L14.22,6.56A2.31 2.31 0 0 1 16.53,8.88L16.53,16.30A2.31 2.31 0 0 1 14.22,18.62L5.03,18.62A2.31 2.31 0 0 1 2.72,16.30L2.72,8.88A2.31 2.31 0 0 1 5.03,6.56z\"/>     <path         fill=\"#FFFFFF\"         stroke=\"#000000\"         fill-opacity=\"1.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"0.0\"         stroke-linejoin=\"miter\"         stroke-linecap=\"square\"         d=\"M21.38,16.08C21.38,17.46,20.45,17.32,19.87,16.83L17.41,14.78Q17.04,14.41,17.04,13.83L17.04,10.94Q17.04,10.47,17.41,10.09L19.87,8.03C20.39,7.56,21.44,7.77,21.44,8.93L21.38,16.08z\"/> </svg>\n" +
                "\n" +
                "<svg\n" +
                "    version=\"1.1\"\n" +
                "    xmlns=\"http://www.w3.org/2000/svg\"\n" +
                "    xmlns:xlink=\"http://www.w3.org/1999/xlink\"\n" +
                "    x=\"0%\" y=\"0%\"\n" +
                "    width=\"100%\" height=\"100%\"\n" +
                "    viewBox=\"0 0 24.0 24.0\"\n" +
                "    enable-background=\"new 0 0 24.0 24.0\"\n" +
                "    xml:space=\"preserve\">\n" +
                "    <path\n" +
                "        fill=\"#FFFFFF\"\n" +
                "        stroke=\"#000000\"\n" +
                "        fill-opacity=\"1.000\"\n" +
                "        stroke-opacity=\"1.000\"\n" +
                "        fill-rule=\"nonzero\"\n" +
                "        stroke-width=\"0.0\"\n" +
                "        stroke-linejoin=\"miter\"\n" +
                "        stroke-linecap=\"square\"\n" +
                "        d=\"M20.00,15.52L19.95,18.47C20.00,19.26,19.46,19.81,18.77,19.88C11.36,20.56,3.56,13.33,3.99,5.06Q4.10,3.96,5.34,3.96L7.74,3.96Q9.13,3.96,9.31,5.54L9.53,8.03Q9.60,8.99,8.79,9.42C7.10,10.32,7.36,11.16,8.09,12.18Q9.78,14.37,12.49,16.09C14.12,17.06,13.98,14.44,15.17,14.44L19.04,14.68Q20.01,14.77,20.00,15.52z\"/>\n" +
                "</svg>\n" +
                "\n" +
                "<svg id=\"tdots\"\n" +
                "    version=\"1.1\"\n" +
                "    xmlns=\"http://www.w3.org/2000/svg\"\n" +
                "    xmlns:xlink=\"http://www.w3.org/1999/xlink\"\n" +
                "    x=\"0%\" y=\"0%\"\n" +
                "    width=\"100%\" height=\"100%\"\n" +
                "    viewBox=\"0 0 24.0 24.0\"\n" +
                "    enable-background=\"new 0 0 24.0 24.0\"\n" +
                "    xml:space=\"preserve\">\n" +
                "    <path\n" +
                "        fill=\"#FFFFFF\"\n" +
                "        stroke=\"#000000\"\n" +
                "        fill-opacity=\"1.000\"\n" +
                "        stroke-opacity=\"1.000\"\n" +
                "        fill-rule=\"nonzero\"\n" +
                "        stroke-width=\"0.0\"\n" +
                "        stroke-linejoin=\"miter\"\n" +
                "        stroke-linecap=\"square\"\n" +
                "        d=\"M11.69,4.38C12.81,4.38,13.72,5.28,13.72,6.41C13.72,7.53,12.81,8.43,11.69,8.43C10.57,8.43,9.66,7.53,9.66,6.41C9.66,5.28,10.57,4.38,11.69,4.38z\"/>\n" +
                "    <path\n" +
                "        fill=\"#FFFFFF\"\n" +
                "        stroke=\"#000000\"\n" +
                "        fill-opacity=\"1.000\"\n" +
                "        stroke-opacity=\"1.000\"\n" +
                "        fill-rule=\"nonzero\"\n" +
                "        stroke-width=\"0.0\"\n" +
                "        stroke-linejoin=\"miter\"\n" +
                "        stroke-linecap=\"square\"\n" +
                "        d=\"M11.69,9.83C12.81,9.83,13.72,10.74,13.72,11.86C13.72,12.98,12.81,13.89,11.69,13.89C10.57,13.89,9.66,12.98,9.66,11.86C9.66,10.74,10.57,9.83,11.69,9.83z\"/>\n" +
                "    <path\n" +
                "        fill=\"#FFFFFF\"\n" +
                "        stroke=\"#000000\"\n" +
                "        fill-opacity=\"1.000\"\n" +
                "        stroke-opacity=\"1.000\"\n" +
                "        fill-rule=\"nonzero\"\n" +
                "        stroke-width=\"0.0\"\n" +
                "        stroke-linejoin=\"miter\"\n" +
                "        stroke-linecap=\"square\"\n" +
                "        d=\"M11.69,15.48C12.81,15.48,13.72,16.39,13.72,17.51C13.72,18.63,12.81,19.54,11.69,19.54C10.57,19.54,9.66,18.63,9.66,17.51C9.66,16.39,10.57,15.48,11.69,15.48z\"/>\n" +
                "</svg>\n" +
                "\n" +
                "              </div>\n" +
                "\n" +
                "            </div>\n" +
                "            <div class=\"conversation\">\n" +
                "              <div class=\"conversation-container\">\n" +
                "            <span id=\"ap\">\n" +
                "            </span>";

        String endData = "</div>\n" +
                "\n" +
                "            <div class=\"convochatbg\">\n" +
                "            <div class=\"msginput\">\n" +
                "\n" +
                "            <!-- Emoji svg -->\n" +
                "            <svg id=\"emoji\"     version=\"1.1\"     xmlns=\"http://www.w3.org/2000/svg\"     xmlns:xlink=\"http://www.w3.org/1999/xlink\"     x=\"0%\" y=\"0%\"     width=\"100%\" height=\"100%\"     viewBox=\"0 0 24.0 24.0\"     enable-background=\"new 0 0 24.0 24.0\"     xml:space=\"preserve\">\n" +
                "            <path         fill=\"#FFFFFF\"         stroke=\"#86949B\"         fill-opacity=\"0.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"0.8010312\"         stroke-linejoin=\"miter\"         stroke-linecap=\"square\"         d=\"M11.96,3.06C16.99,3.06,21.07,7.13,21.07,12.16C21.07,17.19,16.99,21.27,11.96,21.27C6.93,21.27,2.85,17.19,2.85,12.16C2.85,7.13,6.93,3.06,11.96,3.06z\"/>     <path         fill=\"#86949B\"         stroke=\"#000000\"         fill-opacity=\"1.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"0.0\"         stroke-linejoin=\"miter\"         stroke-linecap=\"square\"         d=\"M9.34,8.47C9.92,8.47,10.39,9.21,10.39,10.12C10.39,11.02,9.92,11.76,9.34,11.76C8.75,11.76,8.28,11.02,8.28,10.12C8.28,9.21,8.75,8.47,9.34,8.47z\"/>     <path         fill=\"#86949B\"         stroke=\"#000000\"         fill-opacity=\"1.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"0.0\"         stroke-linejoin=\"miter\"         stroke-linecap=\"square\"         d=\"M14.56,8.47C15.14,8.47,15.61,9.21,15.61,10.12C15.61,11.02,15.14,11.76,14.56,11.76C13.98,11.76,13.50,11.02,13.50,10.12C13.50,9.21,13.98,8.47,14.56,8.47z\"/>     <path         fill=\"#86949B\"         stroke=\"#000000\"         fill-opacity=\"1.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"0.0\"         stroke-linejoin=\"miter\"         stroke-linecap=\"square\"         d=\"M7.07,14.64C9.02,18.94,14.56,18.94,17.09,14.47Q17.56,13.19,16.38,13.31Q11.96,13.80,7.54,13.28Q6.33,13.28,7.07,14.64z\"/>     <path         fill=\"#FFFFFF\"         stroke=\"#000000\"         fill-opacity=\"1.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"0.0\"         stroke-linejoin=\"miter\"         stroke-linecap=\"square\"         d=\"M8.33,14.76C10.39,16.16,13.95,16.16,15.77,14.79Q16.88,13.80,15.52,14.12Q11.91,14.45,8.50,14.06Q7.07,13.80,8.33,14.76z\"/> </svg> <!-- Message Text --> <p id=\"messagetxt\">Message</p> <!-- Camera Emoji --> <svg id=\"camera\"     version=\"1.1\"     xmlns=\"http://www.w3.org/2000/svg\"     xmlns:xlink=\"http://www.w3.org/1999/xlink\"     x=\"0%\" y=\"0%\"     width=\"100%\" height=\"100%\"     viewBox=\"0 0 24.0 24.0\"     enable-background=\"new 0 0 24.0 24.0\"     xml:space=\"preserve\">     <path         fill=\"#86949B\"         stroke=\"#86949B\"         fill-opacity=\"1.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"0.0\"         stroke-linejoin=\"miter\"         stroke-linecap=\"square\"         d=\"M6.84,6.11L8.47,4.15Q9.17,3.19,9.78,3.18L14.03,3.21Q14.62,3.21,15.11,3.77L17.13,6.16\"/>     <path         fill=\"#86949B\"         stroke=\"#2A2A2A\"         fill-opacity=\"1.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"0.0\"         stroke-linejoin=\"miter\"         stroke-linecap=\"square\"         d=\"M5.96,5.65L18.00,5.65A3.19 3.19 0 0 1 21.19,8.84L21.19,16.47A3.19 3.19 0 0 1 18.00,19.66L5.96,19.66A3.19 3.19 0 0 1 2.77,16.47L2.77,8.84A3.19 3.19 0 0 1 5.96,5.65z\"/>     <path         fill=\"#86949B\"         stroke=\"#FFFFFF\"         fill-opacity=\"1.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"1.08\"         stroke-linejoin=\"miter\"         stroke-linecap=\"square\"         d=\"M11.98,8.26C14.00,8.26,15.64,9.90,15.64,11.92C15.64,13.94,14.00,15.58,11.98,15.58C9.96,15.58,8.32,13.94,8.32,11.92C8.32,9.90,9.96,8.26,11.98,8.26z\"/> </svg> <!-- Rupee Emoji --> <svg id=\"rupee\"     version=\"1.1\"     xmlns=\"http://www.w3.org/2000/svg\"     xmlns:xlink=\"http://www.w3.org/1999/xlink\"     x=\"0%\" y=\"0%\"     width=\"100%\" height=\"100%\"     viewBox=\"0 0 24.0 24.0\"     enable-background=\"new 0 0 24.0 24.0\"     xml:space=\"preserve\">     <path         fill=\"#86949B\"         stroke=\"#000000\"         fill-opacity=\"1.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"0.0\"         stroke-linejoin=\"miter\"         stroke-linecap=\"round\"         d=\"M11.91,3.49C16.96,3.49,21.05,7.58,21.05,12.63C21.05,17.68,16.96,21.77,11.91,21.77C6.87,21.77,2.77,17.68,2.77,12.63C2.77,7.58,6.87,3.49,11.91,3.49z\"/>     <path         fill=\"#000000\"         stroke=\"#FFFFFF\"         fill-opacity=\"0.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"1.13919\"         stroke-linejoin=\"miter\"         stroke-linecap=\"round\"         d=\"M8.80,8.69L14.89,8.69\"/>     <path         fill=\"#000000\"         stroke=\"#FFFFFF\"         fill-opacity=\"0.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"1.13919\"         stroke-linejoin=\"miter\"         stroke-linecap=\"round\"         d=\"M8.80,11.10L14.89,11.10\"/>     <path         fill=\"#000000\"         stroke=\"#FFFFFF\"         fill-opacity=\"0.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"1.13919\"         stroke-linejoin=\"miter\"         stroke-linecap=\"round\"         d=\"M11.67,8.69C13.40,10.27,13.13,13.40,9.14,13.40\"/>     <path         fill=\"#000000\"         stroke=\"#FFFFFF\"         fill-opacity=\"0.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"1.13919\"         stroke-linejoin=\"miter\"         stroke-linecap=\"round\"         d=\"M9.23,13.50L12.37,17.26\"/> </svg> <!-- Docs Emoji --> <svg id=\"docs\"     version=\"1.1\"     xmlns=\"http://www.w3.org/2000/svg\"     xmlns:xlink=\"http://www.w3.org/1999/xlink\"     x=\"0%\" y=\"0%\"     width=\"100%\" height=\"100%\"     viewBox=\"0 0 24.0 24.0\"     enable-background=\"new 0 0 24.0 24.0\"     xml:space=\"preserve\">     <path         fill=\"#000000\"         stroke=\"#86949B\"         fill-opacity=\"0.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"1.08\"         stroke-linejoin=\"miter\"         stroke-linecap=\"square\"         d=\"M12.66,5.77L20.28,13.36C23.27,16.09,17.20,23.05,13.90,19.75L3.98,9.82C1.55,7.26,6.44,3.26,8.49,5.44L15.76,13.01C17.09,14.22,15.46,16.54,13.54,14.87L8.70,10.18\"/> </svg> </div> <svg id=\"mic\" version=\"1.1\"     xmlns=\"http://www.w3.org/2000/svg\"     xmlns:xlink=\"http://www.w3.org/1999/xlink\"     x=\"0%\" y=\"0%\"     width=\"100%\" height=\"100%\"     viewBox=\"0 0 24.0 24.0\"     enable-background=\"new 0 0 24.0 24.0\"     xml:space=\"preserve\">     <path         fill=\"#2BA783\"         stroke=\"#000000\"         fill-opacity=\"1.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"0.0\"         stroke-linejoin=\"miter\"         stroke-linecap=\"square\"         d=\"M12.11,0.98C18.26,0.98,23.25,5.97,23.25,12.13C23.25,18.28,18.26,23.27,12.11,23.27C5.95,23.27,0.96,18.28,0.96,12.13C0.96,5.97,5.95,0.98,12.11,0.98z\"/>     <path         fill=\"#FFFFFF\"         stroke=\"#FFFFFF\"         fill-opacity=\"0.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"2.6880002\"         stroke-linejoin=\"miter\"         stroke-linecap=\"round\"         d=\"M12.13,8.84L12.13,11.87\"/>     <path         fill=\"#FFFFFF\"         stroke=\"#FFFFFF\"         fill-opacity=\"0.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"0.84000003\"         stroke-linejoin=\"miter\"         stroke-linecap=\"square\"         d=\"M9.31,12.30C9.55,15.43,14.65,15.33,15.00,12.30\"/>     <path         fill=\"#FFFFFF\"         stroke=\"#FFFFFF\"         fill-opacity=\"0.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"0.84000003\"         stroke-linejoin=\"miter\"         stroke-linecap=\"square\"         d=\"M12.16,14.71L12.16,16.39\"/> </svg>\n" +
                "            </div>\n" +
                "    </div>\n" +
                "</div>\n" +
                "\n" +
                "</div> </div> </div> </div> </div>\n" +
                "\n" +
                "\n" +
                "\n" +
                "</body>\n" +
                " </html>\n";

        String endDataDarkMode = "</div>\n" +
                "                \n" +
                "            <div class=\"convochatbg\"> \n" +
                "            <div class=\"msginput\">\n" +
                "            <!-- Emoji svg --> \n" +
                "            <svg id=\"emoji\"     version=\"1.1\"     xmlns=\"http://www.w3.org/2000/svg\"     xmlns:xlink=\"http://www.w3.org/1999/xlink\"     x=\"0%\" y=\"0%\"     width=\"100%\" height=\"100%\"     viewBox=\"0 0 24.0 24.0\"     enable-background=\"new 0 0 24.0 24.0\"     xml:space=\"preserve\">     \n" +
                "            <path         fill=\"#1b2c35\"         stroke=\"#eee\"         fill-opacity=\"0.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"0.8010312\"         stroke-linejoin=\"miter\"         stroke-linecap=\"square\"         d=\"M11.96,3.06C16.99,3.06,21.07,7.13,21.07,12.16C21.07,17.19,16.99,21.27,11.96,21.27C6.93,21.27,2.85,17.19,2.85,12.16C2.85,7.13,6.93,3.06,11.96,3.06z\"/>     <path         fill=\"#eee\"         stroke=\"#000000\"         fill-opacity=\"1.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"0.0\"         stroke-linejoin=\"miter\"         stroke-linecap=\"square\"         d=\"M9.34,8.47C9.92,8.47,10.39,9.21,10.39,10.12C10.39,11.02,9.92,11.76,9.34,11.76C8.75,11.76,8.28,11.02,8.28,10.12C8.28,9.21,8.75,8.47,9.34,8.47z\"/>     <path         fill=\"#ccc\"         stroke=\"#000000\"         fill-opacity=\"1.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"0.0\"         stroke-linejoin=\"miter\"         stroke-linecap=\"square\"         d=\"M14.56,8.47C15.14,8.47,15.61,9.21,15.61,10.12C15.61,11.02,15.14,11.76,14.56,11.76C13.98,11.76,13.50,11.02,13.50,10.12C13.50,9.21,13.98,8.47,14.56,8.47z\"/>     <path         fill=\"#eee\"         stroke=\"#000000\"         fill-opacity=\"1.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"0.0\"         stroke-linejoin=\"miter\"         stroke-linecap=\"square\"         d=\"M7.07,14.64C9.02,18.94,14.56,18.94,17.09,14.47Q17.56,13.19,16.38,13.31Q11.96,13.80,7.54,13.28Q6.33,13.28,7.07,14.64z\"/>     <path         fill=\"#1b2c35\"         stroke=\"#000000\"         fill-opacity=\"1.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"0.0\"         stroke-linejoin=\"miter\"         stroke-linecap=\"square\"         d=\"M8.33,14.76C10.39,16.16,13.95,16.16,15.77,14.79Q16.88,13.80,15.52,14.12Q11.91,14.45,8.50,14.06Q7.07,13.80,8.33,14.76z\"/> </svg> <!-- Message Text --> <p id=\"messagetxt\">Message</p> <!-- Camera Emoji --> <svg id=\"camera\"     version=\"1.1\"     xmlns=\"http://www.w3.org/2000/svg\"     xmlns:xlink=\"http://www.w3.org/1999/xlink\"     x=\"0%\" y=\"0%\"     width=\"100%\" height=\"100%\"     viewBox=\"0 0 24.0 24.0\"     enable-background=\"new 0 0 24.0 24.0\"     xml:space=\"preserve\">     <path         fill=\"#ccc\"         stroke=\"#ccc\"         fill-opacity=\"1.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"0.0\"         stroke-linejoin=\"miter\"         stroke-linecap=\"square\"         d=\"M6.84,6.11L8.47,4.15Q9.17,3.19,9.78,3.18L14.03,3.21Q14.62,3.21,15.11,3.77L17.13,6.16\"/>     <path         fill=\"#ccc\"         stroke=\"#2A2A2A\"         fill-opacity=\"1.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"0.0\"         stroke-linejoin=\"miter\"         stroke-linecap=\"square\"         d=\"M5.96,5.65L18.00,5.65A3.19 3.19 0 0 1 21.19,8.84L21.19,16.47A3.19 3.19 0 0 1 18.00,19.66L5.96,19.66A3.19 3.19 0 0 1 2.77,16.47L2.77,8.84A3.19 3.19 0 0 1 5.96,5.65z\"/>     <path         fill=\"#ccc\"         stroke=\"#1b2c35\"         fill-opacity=\"1.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"1.08\"         stroke-linejoin=\"miter\"         stroke-linecap=\"square\"         d=\"M11.98,8.26C14.00,8.26,15.64,9.90,15.64,11.92C15.64,13.94,14.00,15.58,11.98,15.58C9.96,15.58,8.32,13.94,8.32,11.92C8.32,9.90,9.96,8.26,11.98,8.26z\"/> </svg> <!-- Rupee Emoji --> <svg id=\"rupee\"     version=\"1.1\"     xmlns=\"http://www.w3.org/2000/svg\"     xmlns:xlink=\"http://www.w3.org/1999/xlink\"     x=\"0%\" y=\"0%\"     width=\"100%\" height=\"100%\"     viewBox=\"0 0 24.0 24.0\"     enable-background=\"new 0 0 24.0 24.0\"     xml:space=\"preserve\">     <path         fill=\"#ccc\"         stroke=\"#000000\"         fill-opacity=\"1.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"0.0\"         stroke-linejoin=\"miter\"         stroke-linecap=\"round\"         d=\"M11.91,3.49C16.96,3.49,21.05,7.58,21.05,12.63C21.05,17.68,16.96,21.77,11.91,21.77C6.87,21.77,2.77,17.68,2.77,12.63C2.77,7.58,6.87,3.49,11.91,3.49z\"/>     <path         fill=\"#000000\"         stroke=\"#1b2c35\"         fill-opacity=\"0.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"1.13919\"         stroke-linejoin=\"miter\"         stroke-linecap=\"round\"         d=\"M8.80,8.69L14.89,8.69\"/>     <path         fill=\"#000000\"         stroke=\"#1b2c35\"         fill-opacity=\"0.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"1.13919\"         stroke-linejoin=\"miter\"         stroke-linecap=\"round\"         d=\"M8.80,11.10L14.89,11.10\"/>     <path         fill=\"#000000\"         stroke=\"#1b2c35\"         fill-opacity=\"0.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"1.13919\"         stroke-linejoin=\"miter\"         stroke-linecap=\"round\"         d=\"M11.67,8.69C13.40,10.27,13.13,13.40,9.14,13.40\"/>     <path         fill=\"#000000\"         stroke=\"#1b2c35\"         fill-opacity=\"0.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"1.13919\"         stroke-linejoin=\"miter\"         stroke-linecap=\"round\"         d=\"M9.23,13.50L12.37,17.26\"/> </svg> <!-- Docs Emoji --> <svg id=\"docs\"     version=\"1.1\"     xmlns=\"http://www.w3.org/2000/svg\"     xmlns:xlink=\"http://www.w3.org/1999/xlink\"     x=\"0%\" y=\"0%\"     width=\"100%\" height=\"100%\"     viewBox=\"0 0 24.0 24.0\"     enable-background=\"new 0 0 24.0 24.0\"     xml:space=\"preserve\">     <path         fill=\"#000000\"         stroke=\"#ccc\"         fill-opacity=\"0.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"1.08\"         stroke-linejoin=\"miter\"         stroke-linecap=\"square\"         d=\"M12.66,5.77L20.28,13.36C23.27,16.09,17.20,23.05,13.90,19.75L3.98,9.82C1.55,7.26,6.44,3.26,8.49,5.44L15.76,13.01C17.09,14.22,15.46,16.54,13.54,14.87L8.70,10.18\"/> </svg> </div> <svg id=\"mic\" version=\"1.1\"     xmlns=\"http://www.w3.org/2000/svg\"     xmlns:xlink=\"http://www.w3.org/1999/xlink\"     x=\"0%\" y=\"0%\"     width=\"100%\" height=\"100%\"     viewBox=\"0 0 24.0 24.0\"     enable-background=\"new 0 0 24.0 24.0\"     xml:space=\"preserve\">     <path         fill=\"#2BA783\"         stroke=\"#000000\"         fill-opacity=\"1.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"0.0\"         stroke-linejoin=\"miter\"         stroke-linecap=\"square\"         d=\"M12.11,0.98C18.26,0.98,23.25,5.97,23.25,12.13C23.25,18.28,18.26,23.27,12.11,23.27C5.95,23.27,0.96,18.28,0.96,12.13C0.96,5.97,5.95,0.98,12.11,0.98z\"/>     <path         fill=\"#FFFFFF\"         stroke=\"#FFFFFF\"         fill-opacity=\"0.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"2.6880002\"         stroke-linejoin=\"miter\"         stroke-linecap=\"round\"         d=\"M12.13,8.84L12.13,11.87\"/>     <path         fill=\"#FFFFFF\"         stroke=\"#FFFFFF\"         fill-opacity=\"0.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"0.84000003\"         stroke-linejoin=\"miter\"         stroke-linecap=\"square\"         d=\"M9.31,12.30C9.55,15.43,14.65,15.33,15.00,12.30\"/>     <path         fill=\"#FFFFFF\"         stroke=\"#FFFFFF\"         fill-opacity=\"0.000\"         stroke-opacity=\"1.000\"         fill-rule=\"nonzero\"         stroke-width=\"0.84000003\"         stroke-linejoin=\"miter\"         stroke-linecap=\"square\"         d=\"M12.16,14.71L12.16,16.39\"/> </svg> \n" +
                "            </div>\n" +
                "    </div> \n" +
                "</div> \n" +
                "</div> </div> </div> </div> </div>\n" +
                "</body>\n" +
                " </html>";


        String noteStart = "<div class=\"note\">";
        String noteEnd = "</div>";
        // noteStart + date + noteEnd

        String firstSenderMsgStart = "<div class='message sent'>";
        String secondSenderMsgStart = "<div class='message2 sent'>";
        String showNameStartTillColor = "<span style=\"position:relative; font-weight:500;color:";
        String showNameColorEndNameStart = "; top:-3px;\">";
        String showNameEnd = "</span><br>";
        String timeStart = "<span class='metadata'> <span class='time'>";
        String editedTag = "<span class='editedtag'>edited  </span>";
        String senderMsgEnding = "</span><span class='tick'></span></span></div>";
        /* firstSenderMsgStart + showNameStartTillColor + color + showNameColorEnd + name + showNameEnd + message + timeStart + editedTag(optional) + time + senderMsgEnding;
           secondSenderMsgStart + message + timeStart + editedTag(optional) + time + senderMsgEnding;*/

        String firstRecieverMsgStart = "<div class='message received'>";
        String secondRecieverMsgStart = "<div class='message2 received'>";
        String recieverMsgEnding = "</span></span></div>";
        /* firstRecieverMsgStart + showNameStartTillColor + color + showNameColorEnd + name + showNameEnd + message + timeStart + editedTag(optional) + time + recieverMsgEnding;
           secondRecieverMsgStart + message + timeStart + editedTag(optional) + time + recieverMsgEnding;*/

        if(isChatThemeDark) {
            startData += endCSSDarkMode + bodyStartTillName + usernamepreviewexportchat.getText() + afterNameLastSeen + "WhatsViewer by Vinaykpro" + afterLastSeenTillBody;
        } else {
            startData += endCSS + bodyStartTillName + usernamepreviewexportchat.getText() + afterNameLastSeen + "WhatsViewer by Vinaykpro" + afterLastSeenTillBody;
        }

        List<String> colorList2 = new ArrayList<>();
        if(isNightMode!=isChatThemeDark && isthisagroup) {
            if(isChatThemeDark) {
                for(int i=0;i<membercount;i++) {
                    colorList2.add(String.format("#%06X", (int) (Math.random() * 0xAAAAAA) + 0x555555));
                }
            } else {
                for(int i=0;i<membercount;i++) {
                    colorList2.add(String.format("#%06X", (int) (Math.random() * 0x888888) + 0x777777));
                }
            }
        }


        for (int position = 0; position < messageList.size(); position++) {
            String s = messageList.get(position);
            String name = "";
            String time = "";
            String message = "";

            if (canigetName(s)) {
                name = getName(s);
            }
            if (canigetMessage(s)) {
                message = getMessage(s);
            }
            if (canigetTime(s)) {
                time = getTime(s);
            }

            if (getItemViewType(position) == 2) { // date;
                String note = messageList.get(position);
                try {
                    new SimpleDateFormat("dd/MM/yy").parse(note);
                    note = getReadableDate(note);
                } catch (ParseException e) {
                }
                startData += noteStart + Html.escapeHtml(note) + noteEnd;
            }

            else if (getItemViewType(position) == 0) { // name.equals(userName1)
                boolean isEdited = false;
                if (message.contains("<This message was edited>")) {
                    Pattern pattern = Pattern.compile("(?i)<This message was edited>");
                    Matcher matcher = pattern.matcher(message);
                    message = matcher.replaceAll("");
                    isEdited = true;
                }
                message = Html.escapeHtml(message);
                String tempMsg;
                String tempMsg1 = firstSenderMsgStart + showNameStartTillColor + "#ff5858" + showNameColorEndNameStart + senderName.getText() + showNameEnd + message + timeStart;
                if (position != 0 && canigetName(messageList.get(position - 1))) {
                    if (!(Objects.requireNonNull(getName(messageList.get(position - 1)))).equals(this.userName1)) {
                        /*params.setMargins(0, 8, 0, 0);
                        sentViewHolder.fullbackgroundlayout.setLayoutParams(params);
                        sentViewHolder.senderBubbleBg.setBackgroundResource(R.drawable.bg_sender);*/
                        if(showSenderNameBool) {
                            tempMsg = tempMsg1;
                        } else {
                            tempMsg = firstSenderMsgStart + message + timeStart;
                        }
                    } else {
                        tempMsg = secondSenderMsgStart + message + timeStart;
                    }
                } else {
                    if(showSenderNameBool) {
                        tempMsg = tempMsg1;
                    } else {
                        tempMsg = firstSenderMsgStart + message + timeStart;
                    }
                }
                if(isEdited) {tempMsg += editedTag;}
                tempMsg += time + senderMsgEnding;
                startData += tempMsg;
            }

            else if (getItemViewType(position) == 1) { //name.equals(userName2) || getItemViewType(position) == 1
                boolean isEdited = false;
                if (message.contains("<This message was edited>")) {
                    Pattern pattern = Pattern.compile("(?i)<This message was edited>");
                    Matcher matcher = pattern.matcher(message);
                    message = matcher.replaceAll("");
                    isEdited = true;
                }
                message = Html.escapeHtml(message);
                String recievername;
                String recieverColor = "blue";



                if (isthisagroup) {
                    recievername = getName(messageList.get(position));
                    if(isNightMode!=isChatThemeDark) {
                        recieverColor = colorList2.get(groupmemebernames.indexOf(recievername));
                    } else {
                        recieverColor = colorList.get(groupmemebernames.indexOf(recievername));
                    }
                } else {
                    recievername = recieverName.getText().toString();
                }
                String tempMsg;
                String tempMsg1 = showNameStartTillColor + recieverColor + showNameColorEndNameStart + recievername + showNameEnd + message + timeStart;
                if (position != 0 && canigetName(messageList.get(position - 1))) {
                    if (!(Objects.requireNonNull(getName(messageList.get(position - 1)))).equals(recievername)) {
                        tempMsg = firstRecieverMsgStart;
                        if(isthisagroup) {
                            tempMsg += tempMsg1;
                        } else if(showRecieverNameBool) {
                            tempMsg += showNameStartTillColor + "#5da8e5" + showNameColorEndNameStart + recievername + showNameEnd + message + timeStart;
                        } else {
                            tempMsg = firstRecieverMsgStart + message + timeStart;
                        }
                        //firstRecieverMsgStart + showNameStartTillColor + color + showNameColorEndNameStart + name + showNameEnd + message + timeStart + editedTag + time + recieverMsgEnding;
                    } else {
                        tempMsg = secondRecieverMsgStart + message + timeStart;
                    }
                } else {
                    tempMsg = firstRecieverMsgStart;
                    if(isthisagroup) {
                        tempMsg += tempMsg1;
                    } else if(showRecieverNameBool) {
                        tempMsg += showNameStartTillColor + "#5da8e5" + showNameColorEndNameStart + recievername + showNameEnd + message + timeStart;
                    }  else {
                        tempMsg = firstRecieverMsgStart + message + timeStart;
                    }
                }
                if(isEdited) { tempMsg += editedTag; }
                tempMsg += time + recieverMsgEnding;
                startData += tempMsg;
            }

            startData += "\n";
        }

        if(isChatThemeDark) {
            startData += endDataDarkMode;
        } else {
            startData += endData;
        }


        String finalStartData = startData;
        ((MainActivity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createAndShareHTMLFile(context, finalStartData,"Chat with "+usernamepreviewexportchat.getText()+" by WhatsViewer","WhatsViewer");
            }
        });
    }

    public void createAndShareHTMLFile(Context context, String htmlString, String fileName, String appName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            new CreateAndShareHTMLFileAsyncTaskAndroid10Plus(context, htmlString, fileName, appName).execute();
        } else {
            new CreateAndShareHTMLFileAsyncTaskPreAndroid10(context, htmlString, fileName, appName).execute();
        }
    }

    private static class CreateAndShareHTMLFileAsyncTaskAndroid10Plus extends AsyncTask<Void, Void, File> {
        private Context context;
        private String htmlString;
        private String fileName;
        private String appName;

        public CreateAndShareHTMLFileAsyncTaskAndroid10Plus(Context context, String htmlString, String fileName, String appName) {
            this.context = context;
            this.htmlString = htmlString;
            this.fileName = fileName;
            this.appName = appName;
        }

        @Override
        protected File doInBackground(Void... voids) {
            return createHTMLFileInPrivateDir(context, htmlString, fileName);
        }

        @Override
        protected void onPostExecute(File file) {
            if (file != null) {
                saveToPublicDirectoryAndroid10Plus(context, file, appName);
            }
        }
    }

    private static class CreateAndShareHTMLFileAsyncTaskPreAndroid10 extends AsyncTask<Void, Void, File> {
        private Context context;
        private String htmlString;
        private String fileName;
        private String appName;

        public CreateAndShareHTMLFileAsyncTaskPreAndroid10(Context context, String htmlString, String fileName, String appName) {
            this.context = context;
            this.htmlString = htmlString;
            this.fileName = fileName;
            this.appName = appName;
        }

        @Override
        protected File doInBackground(Void... voids) {
            return createHTMLFileInRootDir(context, htmlString, fileName, appName);
        }

        @Override
        protected void onPostExecute(File file) {
            if (file != null) {
                shareFile(context, Uri.fromFile(file), "text/html");
            }
        }
    }

    private static File createHTMLFileInPrivateDir(Context context, String htmlString, String fileName) {
        File file = new File(context.getFilesDir(), fileName + ".html");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(htmlString.getBytes());
            fos.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    private static File createHTMLFileInRootDir(Context context, String htmlString, String fileName, String appName) {
        File rootDir = new File(Environment.getExternalStorageDirectory(), appName);
        if (!rootDir.exists()) {
            rootDir.mkdirs();
        }

        File file = new File(rootDir, fileName + ".html");
        ((MainActivity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "File was successsfully saved in " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            }
        });

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(htmlString.getBytes());
            fos.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void saveToPublicDirectoryAndroid10Plus(Context context, File file, String appName) {
        String mimeType = "text/html";
        String displayName = file.getName();

        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName);
        values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/" + appName);

        Uri externalUri = MediaStore.Files.getContentUri("external");
        Uri insertedUri = context.getContentResolver().insert(externalUri, values);

        try {
            if (insertedUri != null) {
                OutputStream outputStream = context.getContentResolver().openOutputStream(insertedUri);
                if (outputStream != null) {
                    FileInputStream inputStream = new FileInputStream(file);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }
                    inputStream.close();
                    outputStream.close();
                    ((MainActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,"File was successsfully saved in storage/emulated/0/Download/WhastViewer/"+displayName,Toast.LENGTH_LONG).show();
                        }
                    });
                    // Share the file
                    shareFile(context, insertedUri, mimeType);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void shareFile(Context context, Uri fileUri, String mimeType) {
        exportChatCloseBtn.callOnClick();
        ((MainActivity)context).findViewById(R.id.exportingscreen).setVisibility(View.GONE);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType(mimeType);
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(shareIntent, "Share HTML File"));
    }

    private boolean notempty(String text) {
        for(int i=0;i<text.length();i++) {
            char a = text.charAt(i);
            if(a != ' ') {
                return true;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        String s = messageList.get(position);
        String name = "";
        String time = "";
        String message = "";

        if (canigetName(s)) {
            name = getName(s);
        }
        if (canigetMessage(s)) {
            message = getMessage(s);
        }
        if (canigetTime(s)) {
            time = getTime(s);
        }

        if (getItemViewType(position) == 2) {
            NoteViewHolder noteViewHolder = (NoteViewHolder) holder;

            String note = messageList.get(position);

            try {
                new SimpleDateFormat("dd/MM/yy").parse(note);
                note = getReadableDate(note);
            } catch (ParseException e) {
            }

            noteViewHolder.note.setText(note);

            if (messageList.get(position).contains("Messages and calls are end-to-end encrypted. No one outside of this chat") && position == 1 && !isNightMode) {
                noteViewHolder.note.setBackgroundResource(R.drawable.firstnotmessage_bg);
            } else {
                noteViewHolder.note.setBackgroundResource(R.drawable.date_bg);
            }
            if (isNightMode)
                noteViewHolder.note.setTextColor(((MainActivity) context).getResources().getColor(R.color.lightwhite));
            else
                noteViewHolder.note.setTextColor(((MainActivity) context).getResources().getColor(R.color.verylightblack));
            return;
        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        if (getItemViewType(position) == 0) { // name.equals(userName1)
            SentViewHolder sentViewHolder = (SentViewHolder) holder;
            sentViewHolder.sentmessage.setText(message);
            sentViewHolder.senttime.setText(time);


            if (position != 0 && canigetName(messageList.get(position - 1))) {
                if (!(Objects.requireNonNull(getName(messageList.get(position - 1)))).equals(this.userName1)) {
                    params.setMargins(0, 8, 0, 0);
                    sentViewHolder.fullbackgroundlayout.setLayoutParams(params);
                    sentViewHolder.senderBubbleBg.setBackgroundResource(R.drawable.bg_sender);
                } else {
                    params.setMargins(0, 0, 0, 0);
                    sentViewHolder.fullbackgroundlayout.setLayoutParams(params);
                    sentViewHolder.senderBubbleBg.setBackgroundResource(R.drawable.bg_sender_without_bubble);
                }
            } else {
                params.setMargins(0, 8, 0, 0);
                sentViewHolder.fullbackgroundlayout.setLayoutParams(params);
                sentViewHolder.senderBubbleBg.setBackgroundResource(R.drawable.bg_sender);
            }
            if (isNightMode) {
                sentViewHolder.sentmessage.setTextColor(((MainActivity) context).getResources().getColor(R.color.white));
                sentViewHolder.senttime.setTextColor(((MainActivity) context).getResources().getColor(R.color.lightwhite));
            } else {
                sentViewHolder.sentmessage.setTextColor(((MainActivity) context).getResources().getColor(R.color.black));
                sentViewHolder.senttime.setTextColor(((MainActivity) context).getResources().getColor(R.color.lightblack));
            }
            if (triggeredposition == position) {
                sentViewHolder.fullbackgroundlayout.setForeground(context.getResources().getDrawable(R.drawable.bg_selected_message));
                ObjectAnimator anim = ObjectAnimator.ofInt(sentViewHolder.fullbackgroundlayout.getForeground(), "alpha", 255, 0);
                anim.setDuration(1500).setStartDelay(100);
                anim.start();
            }

            if (indexes.contains(position + "") || triggeredposition == position) {
                if (!(triggeredposition == position)) {
                    sentViewHolder.fullbackgroundlayout.setForeground(context.getResources().getDrawable(R.drawable.bg_selected_message));
                }
            } else {
                sentViewHolder.fullbackgroundlayout.setForeground(null);
            }

            if (searchedIndexes.contains(String.valueOf(position))) {
                String highlightedtext = "";
                if (!isNightMode) {
                    highlightedtext = "<span style='background-color:yellow'>" + searchedword + "</span>";
                } else {
                    highlightedtext = "<span style='background-color:#ffffff; color:black'>" + searchedword + "</span>";
                }
                String text = message.replaceAll("(?i)" + Pattern.quote(searchedword), highlightedtext);
                sentViewHolder.sentmessage.setText(Html.fromHtml(text, 0));
            }

            sentViewHolder.fullbackgroundlayout.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View view) {
                    if (selectionmode) {
                        if (sentViewHolder.isselected) {
                            sentViewHolder.isselected = false;
                            count--;
                            selectedcount.setText(String.valueOf(count));
                            selectedsitems.remove(sentViewHolder);
                            indexes.remove(sentViewHolder.getAdapterPosition() + "");
                            sentViewHolder.fullbackgroundlayout.setForeground(null);
                        } else {
                            sentViewHolder.isselected = true;
                            count++;
                            selectedcount.setText(String.valueOf(count));
                            selectedsitems.add(sentViewHolder);
                            indexes.add(sentViewHolder.getAdapterPosition() + "");
                            sentViewHolder.fullbackgroundlayout.setForeground(context.getResources().getDrawable(R.drawable.bg_selected_message));
                        }
                    }

                    if (count > 1) {
                        editmode = false;
                        editmessagelayout.setVisibility(View.GONE);
                    }

                    if (count == 0) {
                        hideThisViewByAlpha(chatmenulayout);
                        selectionmode = false;
                    } else {
                        if (issearching) {
                            issearching = false;
                            triggeredposition = -1;
                            searchedIndexes.clear();
                            maininputlayout.setVisibility(View.VISIBLE);
                            searchlayout.setVisibility(View.GONE);
                        }
                        revealThisViewByAlpha(chatmenulayout);
                    }
                }
            });

            sentViewHolder.fullbackgroundlayout.setOnLongClickListener(new View.OnLongClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public boolean onLongClick(View view) {

                    if (!selectionmode) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            v.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            v.vibrate(30);
                        }
                        selectionmode = true;
                    }
                    if (sentViewHolder.isselected) {
                        sentViewHolder.isselected = false;
                        count--;
                        selectedcount.setText(String.valueOf(count));
                        selectedsitems.remove(sentViewHolder);
                        indexes.remove(sentViewHolder.getAdapterPosition() + "");
                        sentViewHolder.fullbackgroundlayout.setForeground(null);
                    } else {
                        sentViewHolder.isselected = true;
                        count++;
                        selectedcount.setText(String.valueOf(count));
                        selectedsitems.add(sentViewHolder);
                        indexes.add(sentViewHolder.getAdapterPosition() + "");
                        sentViewHolder.fullbackgroundlayout.setForeground(context.getResources().getDrawable(R.drawable.bg_selected_message));
                    }

                    if (count > 1) {
                        editmode = false;
                        editmessagelayout.setVisibility(View.GONE);
                    }

                    if (count == 0) {
                        hideThisViewByAlpha(chatmenulayout);
                        selectionmode = false;
                    } else {
                        if (issearching) {
                            issearching = false;
                            triggeredposition = -1;
                            searchedIndexes.clear();
                            maininputlayout.setVisibility(View.VISIBLE);
                            searchlayout.setVisibility(View.GONE);
                        }
                        revealThisViewByAlpha(chatmenulayout);
                        editmessagelayout.setVisibility(View.GONE);
                    }
                    return true;
                }
            });

        }
        else if (getItemViewType(position) == 1) { //name.equals(userName2) || getItemViewType(position) == 1
            RecievedViewHolder recievedViewHolder = (RecievedViewHolder) holder;
            String recievername;
            recievedViewHolder.recievedmessage.setText(message);
            recievedViewHolder.recievedtime.setText(time);
            if (isthisagroup) {
                recievedViewHolder.recievername.setVisibility(View.VISIBLE);
                recievedViewHolder.recievername.setText(getName(messageList.get(position)));
                recievername = getName(messageList.get(position));
                recievedViewHolder.recievername.setTextColor(Color.parseColor(colorList.get(groupmemebernames.indexOf(recievername))));
            } else {
                recievedViewHolder.recievername.setVisibility(View.GONE);
                recievername = userName2;
            }

            if (position != 0 && canigetName(messageList.get(position - 1))) {
                if (!(Objects.requireNonNull(getName(messageList.get(position - 1)))).equals(recievername)) {
                    params.setMargins(0, 8, 0, 0);
                    recievedViewHolder.fullbackgroundlayout.setLayoutParams(params);
                    recievedViewHolder.recieverLayout.setBackgroundResource(R.drawable.bg_reciever);
                } else {
                    recievedViewHolder.recievername.setVisibility(View.GONE);
                    params.setMargins(0, 0, 0, 0);
                    recievedViewHolder.fullbackgroundlayout.setLayoutParams(params);
                    recievedViewHolder.recieverLayout.setBackgroundResource(R.drawable.bg_reciever_without_bubble);
                }
            } else {
                params.setMargins(0, 8, 0, 0);
                recievedViewHolder.fullbackgroundlayout.setLayoutParams(params);
                recievedViewHolder.recieverLayout.setBackgroundResource(R.drawable.bg_reciever);
            }
            if (isNightMode) {
                recievedViewHolder.recievedmessage.setTextColor(((MainActivity) context).getResources().getColor(R.color.white));
                recievedViewHolder.recievedtime.setTextColor(((MainActivity) context).getResources().getColor(R.color.lightwhite));
                //recievedViewHolder.rmediarecievedtime.setTextColor(((MainActivity) context).getResources().getColor(R.color.lightwhite));
            } else {
                recievedViewHolder.recievedmessage.setTextColor(((MainActivity) context).getResources().getColor(R.color.black));
                recievedViewHolder.recievedtime.setTextColor(((MainActivity) context).getResources().getColor(R.color.lightblack));
                //recievedViewHolder.rmediarecievedtime.setTextColor(((MainActivity) context).getResources().getColor(R.color.lightblack));
            }

            if (triggeredposition == position) {
                recievedViewHolder.fullbackgroundlayout.setForeground(context.getResources().getDrawable(R.drawable.bg_selected_message));
                ObjectAnimator anim = ObjectAnimator.ofInt(recievedViewHolder.fullbackgroundlayout.getForeground(), "alpha", 255, 0);
                anim.setDuration(1500).setStartDelay(100);
                anim.start();
            }

            if (indexes.contains(position + "") || triggeredposition == position) {
                if (!(triggeredposition == position)) {
                    recievedViewHolder.fullbackgroundlayout.setForeground(context.getResources().getDrawable(R.drawable.bg_selected_message));
                }
            } else {
                recievedViewHolder.fullbackgroundlayout.setForeground(null);
            }

            if (searchedIndexes.contains(String.valueOf(position))) {
                String highlightedtext = "";
                if (!isNightMode) {
                    highlightedtext = "<span style='background-color:yellow'>" + searchedword + "</span>";
                } else {
                    highlightedtext = "<span style='background-color:#ffffff; color:black'>" + searchedword + "</span>";
                }
                String text = message.replaceAll("(?i)" + Pattern.quote(searchedword), highlightedtext);
                recievedViewHolder.recievedmessage.setText(Html.fromHtml(text, 0));
            }

            recievedViewHolder.fullbackgroundlayout.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View view) {

                    if (selectionmode) {
                        if (recievedViewHolder.isselected) {
                            recievedViewHolder.isselected = false;
                            count--;
                            selectedcount.setText(String.valueOf(count));
                            selectedritems.remove(recievedViewHolder);
                            indexes.remove(recievedViewHolder.getAdapterPosition() + "");
                            recievedViewHolder.fullbackgroundlayout.setForeground(null);
                        } else {
                            recievedViewHolder.isselected = true;
                            count++;
                            selectedcount.setText(String.valueOf(count));
                            selectedritems.add(recievedViewHolder);
                            indexes.add(recievedViewHolder.getAdapterPosition() + "");
                            recievedViewHolder.fullbackgroundlayout.setForeground(context.getResources().getDrawable(R.drawable.bg_selected_message));
                        }
                        //copy.callOnClick();
                    }

                    if (count > 1) {
                        editmode = false;
                        editmessagelayout.setVisibility(View.GONE);
                    }

                    if (count == 0) {
                        hideThisViewByAlpha(chatmenulayout);
                        selectionmode = false;
                    } else {
                        if (issearching) {
                            issearching = false;
                            triggeredposition = -1;
                            searchedIndexes.clear();
                            maininputlayout.setVisibility(View.VISIBLE);
                            searchlayout.setVisibility(View.GONE);
                        }
                        revealThisViewByAlpha(chatmenulayout);
                        editmessagelayout.setVisibility(View.GONE);
                    }
                }
            });

            recievedViewHolder.fullbackgroundlayout.setOnLongClickListener(new View.OnLongClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public boolean onLongClick(View view) {

                    if (!selectionmode) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            v.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            v.vibrate(30);
                        }
                        selectionmode = true;
                    }
                    if (recievedViewHolder.isselected) {
                        recievedViewHolder.isselected = false;
                        count--;
                        selectedcount.setText(String.valueOf(count));
                        selectedritems.remove(recievedViewHolder);
                        indexes.remove(recievedViewHolder.getAdapterPosition() + "");
                        recievedViewHolder.fullbackgroundlayout.setForeground(null);
                    } else {
                        recievedViewHolder.isselected = true;
                        count++;
                        selectedcount.setText(String.valueOf(count));
                        selectedritems.add(recievedViewHolder);
                        indexes.add(recievedViewHolder.getAdapterPosition() + "");
                        recievedViewHolder.fullbackgroundlayout.setForeground(context.getResources().getDrawable(R.drawable.bg_selected_message));
                    }

                    if (count > 1) {
                        editmode = false;
                        editmessagelayout.setVisibility(View.GONE);
                    }

                    if (count == 0) {
                        hideThisViewByAlpha(chatmenulayout);
                        selectionmode = false;
                    } else {
                        if (issearching) {
                            issearching = false;
                            triggeredposition = -1;
                            searchedIndexes.clear();
                            maininputlayout.setVisibility(View.VISIBLE);
                            searchlayout.setVisibility(View.GONE);
                        }
                        editmessagelayout.setVisibility(View.GONE);
                        revealThisViewByAlpha(chatmenulayout);
                    }
                    return true;
                }
            });

        } else if (getItemViewType(position) == 3) { // name.equals(userName1)
            SentMediaViewHolder sentMediaViewHolder = (SentMediaViewHolder) holder;
            sentMediaViewHolder.sentmessage.setText(message);
            sentMediaViewHolder.senttime.setText(time);

                File file = new File("This file does not exist");
                String fileName = "";
                //f = 4;
                //String[] paths = {"storage/emulated/0/Download/song.mp3","storage/emulated/0/Download/Testimg.jpg","storage/emulated/0/Download/Crop2.jpg","storage/emulated/0/Download/Crop1.jpg","storage/emulated/0/Vinay/Crop3.jpg","storage/emulated/0/Download/aditya.mp4","storage/emulated/0/Download/Video.mp4"};
                //sentViewHolder.mainimage.setImageURI(Uri.fromFile(new File(paths[f])));
                //sentViewHolder.sentmessage.setText((position+""));
                sentMediaViewHolder.senderaudiolayout.setVisibility(View.GONE);
                sentMediaViewHolder.documentlayout.setVisibility(View.GONE);
                sentMediaViewHolder.imagecontainer.setVisibility(View.VISIBLE);

                    message = sentMediaViewHolder.sentmessage.getText().toString();
                    String text = "";

                    String[] parts = message.split("\\(file attached\\)");
                    for (String ss : parts) {
                        if (ss.contains("(file attached)"))
                            ss = ss.replaceAll("(?=\\(file attached\\))", "");
                        ss = ss.trim();
                        if (!ss.isEmpty())
                            text += ss + "\n";
                        File newFile;
                        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q) {
                            newFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsViewer/chats/" + tablename + "/" + ss);
                        } else {
                            newFile = new File(context.getFilesDir() + "/" + tablename + "/" + ss);
                        }
                        if (newFile.exists()) {
                            fileName = ss;
                            file = newFile;
                            break;
                        }

                    }
                String path = file.getAbsolutePath();
                boolean isImage, isVideo, isAudio;
                isImage = isVideo = isAudio = false;

                if (path.contains("IMG-") || path.contains(".jpg") || path.contains(".png")) {
                    isImage = true;
                } else if (path.contains("VID-") || path.contains(".mp4") || path.contains(".mkv") || path.contains(".3gp") || path.contains(".mpeg") || path.contains(".m4a")) {
                    isVideo = true;
                } else if ((path.contains("AUD-") || path.contains(".mp3") || path.contains(".ogg") || path.contains(".opus"))) {
                    isAudio = true;
                } else {
                    isImage = isVideo = isAudio = false;
                }

                int calculatedHeight = maxheightinpx;
                int calculatedWidth = maxwidthinpx;

                    if (!file.exists()) { //!file.exists()
                        if(isImage)
                        {
                            calculatedHeight = maxheightinpx;
                            calculatedWidth = (int)(maxwidthinpx*0.7);

                            sentMediaViewHolder.svideotimelayout.setVisibility(View.GONE);
                            sentMediaViewHolder.senderaudiolayout.setVisibility(View.GONE);
                            sentMediaViewHolder.documentlayout.setVisibility(View.GONE);

                            sentMediaViewHolder.playbutton.setVisibility(View.VISIBLE);
                            sentMediaViewHolder.mainimage.setBackgroundResource(R.drawable.blur);
                            sentMediaViewHolder.playbutton.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_download_white));


                        } else if(isVideo) {
                            calculatedHeight = maxheightinpx;
                            calculatedWidth = (int)(maxwidthinpx*0.7);
                        } else if(isAudio) {
                            calculatedHeight = sentMediaViewHolder.senderaudiolayout.getHeight();
                            calculatedWidth = maxwidthinpx;
                        } else {

                        }
                    } else {
                        //String path = file.getAbsolutePath();

                        //boolean isImage, isVideo, isAudio;
                        isImage = isVideo = isAudio = false;

                        if (path.contains("IMG-") || path.contains(".jpg") || path.contains(".png")) {
                            isImage = true;
                            //sentViewHolder.sentmessage.setText(("This is an image"));

                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;
                            BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                            int imageHeight = options.outHeight;
                            int imageWidth = options.outWidth;

                            sentMediaViewHolder.svideotimelayout.setVisibility(View.GONE);
                            sentMediaViewHolder.playbutton.setVisibility(View.GONE);
                            sentMediaViewHolder.senderaudiolayout.setVisibility(View.GONE);
                            sentMediaViewHolder.documentlayout.setVisibility(View.GONE);
                            ((SentMediaViewHolder) holder).imagecontainer.setVisibility(View.VISIBLE);
                            //sentMediaViewHolder.mainimage.setBackgroundColor(context.getResources().getColor(R.color.white));
                            sentMediaViewHolder.playbutton.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_play));
                            //Toast.makeText(context, path, Toast.LENGTH_SHORT).show();
                            Glide.with(context).load(file).fitCenter().into(sentMediaViewHolder.mainimage);
                            //Toast.makeText(context, file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                Glide.with(context).load(file).fitCenter().into(sentMediaViewHolder.mainimage);
                            } else {
                                Glide.with(context).load(file).fitCenter().into(sentMediaViewHolder.mainimage);
                            }*/


                            if (imageHeight > imageWidth) {
                                double heightpercentage = (imageHeight / (imageWidth * 0.01));
                                double tempheight = (maxwidthinpx * 0.01) * (heightpercentage);
                                if (tempheight > maxheightinpx) {
                                    double tempheight2 = ((((maxwidthinpx * 0.9) * 0.01) * (100 + heightpercentage)));
                                    if (tempheight2 >= maxheightinpx) {
                                        calculatedWidth = (int) (maxwidthinpx * 0.9);
                                        calculatedHeight = maxheightinpx;
                                    } else {
                                        calculatedWidth = (int) (maxwidthinpx * 0.9);
                                        calculatedHeight = (int) tempheight2;
                                    }
                                    //calculatedHeight=tempheight;
                                } else {
                                    calculatedWidth = maxwidthinpx;
                                    calculatedHeight = (int) tempheight;
                                }
                            } else {
                                int widthpercentage = (int) (imageHeight / (imageWidth * 0.01));
                                int minheight = (int) (maxheightinpx * 0.16);
                                int tempheight = (int) (maxwidthinpx * 0.01) * (widthpercentage);
                                if (tempheight > minheight && tempheight < maxheightinpx) {
                                    calculatedHeight = tempheight;
                                } else if (tempheight < minheight) {
                                    calculatedHeight = (int) minheight;
                                } else {
                                    calculatedHeight = maxheightinpx;
                                }
                            }

                            ImFlexboxLayout.LayoutParams params1 = new ImFlexboxLayout.LayoutParams(calculatedWidth, calculatedHeight);
                            sentMediaViewHolder.imagecontainer.setLayoutParams(params1);

                            File finalFile = file;
                            String finalFileName = fileName;
                            sentMediaViewHolder.imagecontainer.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(!selectionmode) {
                                        Intent i = new Intent(((MainActivity) context), MediaViewerActivity.class);
                                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(((MainActivity) context), sentMediaViewHolder.mainimage, ViewCompat.getTransitionName(sentMediaViewHolder.mainimage));
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                            //Toast.makeText(context, finalFile.getAbsolutePath()+"", Toast.LENGTH_SHORT).show();
                                            i.putExtra("path", finalFile.getAbsolutePath());
                                        } else {
                                            i.putExtra("path", finalFile.getAbsolutePath());
                                        }


                                        context.startActivity(i, options.toBundle());
                                    } else {
                                        sentMediaViewHolder.fullbackgroundlayout.performLongClick();
                                    }
                                }
                            });
                            sentMediaViewHolder.imagecontainer.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View view) {
                                    sentMediaViewHolder.fullbackgroundlayout.performLongClick();
                                    return true;
                                }
                            });

                            //isVideo = isAudio = false;
                            //Toast.makeText(context, path, Toast.LENGTH_SHORT).show();
                            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                file = new File(Environment.DIRECTORY_DOWNLOADS);
                            } else {
                                file = new File(Environment.getExternalStorageDirectory() + "/WhatsViewer/chats/" + tablename + "/" + fileName);
                            }*/
                        } else if (path.contains("VID-") || path.contains(".mp4") || path.contains(".mkv") || path.contains(".3gp") || path.contains(".mpeg") || path.contains(".m4a")) {
                            //isImage = isAudio = false;
                            isVideo = true;
                            //sentViewHolder.sentmessage.setText(("This is a Video"));

                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;
                            BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                            int imageHeight = options.outHeight;
                            int imageWidth = options.outWidth;

                            sentMediaViewHolder.senderaudiolayout.setVisibility(View.GONE);
                            //Toast.makeText(context, fileName+" is a Video", Toast.LENGTH_SHORT).show();
                            Glide.with(context).load(file).fitCenter().into(sentMediaViewHolder.mainimage);
                            String duration = "";
                            try {
                                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                                retriever.setDataSource(file.getAbsolutePath());
                                duration = "";
                                int dur_in_mills = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                                duration = convertToMMSS(dur_in_mills + "");

                                sentMediaViewHolder.svideotimetext.setText(duration);
                                //sentViewHolder.mainimage.setImageBitmap(ThumbnailUtils.createVideoThumbnail(paths[f], MediaStore.Video.Thumbnails.MINI_KIND));
                                //imageHeight = 300;
                                //imageWidth = 400;
                                imageHeight = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                                imageWidth = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                                retriever.release();
                            } catch (Exception e) {
                            }

                            if (imageHeight > imageWidth) {
                                double heightpercentage = (imageHeight / (imageWidth * 0.01));
                                double tempheight = (maxwidthinpx * 0.01) * (heightpercentage);
                                if (tempheight > maxheightinpx) {
                                    double tempheight2 = ((((maxwidthinpx * 0.9) * 0.01) * (100 + heightpercentage)));
                                    if (tempheight2 >= maxheightinpx) {
                                        calculatedWidth = (int) (maxwidthinpx * 0.9);
                                        calculatedHeight = maxheightinpx;
                                    } else {
                                        calculatedWidth = (int) (maxwidthinpx * 0.9);
                                        calculatedHeight = (int) tempheight2;
                                    }
                                    //calculatedHeight=tempheight;
                                } else {
                                    calculatedWidth = maxwidthinpx;
                                    calculatedHeight = (int) tempheight;
                                }
                            } else {
                                int widthpercentage = (int) (imageHeight / (imageWidth * 0.01));
                                int minheight = (int) (maxheightinpx * 0.16);
                                int tempheight = (int) (maxwidthinpx * 0.01) * (widthpercentage);
                                if (tempheight > minheight && tempheight < maxheightinpx) {
                                    calculatedHeight = tempheight;
                                } else if (tempheight < minheight) {
                                    calculatedHeight = (int) minheight;
                                } else {
                                    calculatedHeight = maxheightinpx;
                                }
                            }

                            ImFlexboxLayout.LayoutParams params1 = new ImFlexboxLayout.LayoutParams(calculatedWidth, calculatedHeight);
                            sentMediaViewHolder.imagecontainer.setLayoutParams(params1);

                            File finalFile = file;
                            sentMediaViewHolder.imagecontainer.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(((MainActivity) context), MediaViewerActivity.class);
                                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(((MainActivity) context), sentMediaViewHolder.mainimage, ViewCompat.getTransitionName(sentMediaViewHolder.mainimage));
                                    i.putExtra("path", finalFile.getAbsolutePath());
                                    context.startActivity(i, options.toBundle());
                                }
                            });
                            sentMediaViewHolder.imagecontainer.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View view) {
                                    sentMediaViewHolder.fullbackgroundlayout.performLongClick();
                                    return true;
                                }
                            });

                            sentMediaViewHolder.playbutton.setVisibility(View.VISIBLE);
                            sentMediaViewHolder.svideotimelayout.setVisibility(View.VISIBLE);

                        } else if (path.contains("AUD-") || path.contains(".mp3") || path.contains(".ogg") || path.contains(".opus")) {
                            isImage = isVideo = false;
                            //sentViewHolder.sentmessage.setText(("This is an audio"));
                            isAudio = true;

                            Toast.makeText(context, "This is an Audio", Toast.LENGTH_SHORT).show();

                            calculatedHeight = sentMediaViewHolder.senderaudiolayout.getHeight();
                            calculatedWidth = maxwidthinpx;
                            sentMediaViewHolder.sentmessage.setVisibility(View.GONE);
                            sentMediaViewHolder.mainimage.setVisibility(View.GONE);
                            sentMediaViewHolder.senderaudiolayout.setVisibility(View.VISIBLE);
                            sentMediaViewHolder.svideotimelayout.setVisibility(View.GONE);
                            sentMediaViewHolder.playbutton.setVisibility(View.GONE);


                            String audioduration = "0:00";
                            try {
                                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                                mediaMetadataRetriever.setDataSource(path);
                                int dur = Integer.valueOf(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                                if (currentAudioIndex != sentMediaViewHolder.getAdapterPosition())
                                    sentMediaViewHolder.audioduration.setText(convertToMMSS(dur + ""));
                                audioduration = convertToMMSS(dur + "");
                            } catch (Exception e) {
                                Toast.makeText(context, "Unable to get the duration of this audio file" + e.toString(), Toast.LENGTH_SHORT).show();
                            }

                            String finalAudioduration = audioduration;
                            if (currentAudioIndex != sentMediaViewHolder.getAdapterPosition()) {
                                sentMediaViewHolder.saudioplaybutton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_lightwhite_play, null));
                                sentMediaViewHolder.audioduration.setText(finalAudioduration);
                                sentMediaViewHolder.seekBar.setProgress(0);
                            }
                            sentMediaViewHolder.saudioplaybutton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ((MainActivity) context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Runnable audioRunnable = new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (audioPlayer != null && audioPlayer.isPlaying()) {
                                                        if (currentAudioIndex == sentMediaViewHolder.getAdapterPosition()) {
                                                            int progress = audioPlayer.getCurrentPosition();
                                                            sentMediaViewHolder.seekBar.setProgress(progress);
                                                            sentMediaViewHolder.audioduration.setText(convertToMMSS(progress + ""));
                                                            myHandler.postDelayed(this, 500);
                                                        }
                                                    }
                                                }
                                            };
                                            if (audioPlayer == null || currentAudioIndex != sentMediaViewHolder.getAdapterPosition()) {
                                                if (audioPlayer != null) {
                                                    audioPlayer.release();
                                                    audioPlayer = null;
                                                    myHandler.removeCallbacks(audioRunnable);
                                                    sentMediaViewHolder.saudioplaybutton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_lightwhite_play, null));
                                                    notifyItemChanged(currentAudioIndex);
                                                }
                                                audioPlayer = MediaPlayer.create(context, Uri.fromFile(new File(path)));
                                                audioPlayer.start();
                                                sentMediaViewHolder.saudioplaybutton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_pause_round, null));
                                                myHandler.post(audioRunnable);
                                                sentMediaViewHolder.seekBar.setMax(audioPlayer.getDuration());
                                                currentAudioIndex = sentMediaViewHolder.getAdapterPosition();
                                                audioPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                                    @Override
                                                    public void onCompletion(MediaPlayer mediaPlayer) {
                                                        audioPlayer.release();
                                                        audioPlayer = null;
                                                        myHandler.removeCallbacks(audioRunnable);
                                                        sentMediaViewHolder.saudioplaybutton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_lightwhite_play, null));
                                                        sentMediaViewHolder.audioduration.setText(finalAudioduration);
                                                        sentMediaViewHolder.seekBar.setProgress(0);
                                                        currentAudioIndex = -1;
                                                    }
                                                });
                                            } else if (audioPlayer.isPlaying()) {
                                                audioPlayer.pause();
                                                sentMediaViewHolder.saudioplaybutton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_lightwhite_play, null));
                                                myHandler.removeCallbacks(audioRunnable);
                                            } else {
                                                audioPlayer.start();
                                                sentMediaViewHolder.saudioplaybutton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_pause_round, null));
                                                myHandler.removeCallbacks(audioRunnable);
                                                myHandler.post(audioRunnable);
                                            }
                                        }
                                    });
                                }
                            });

                            sentMediaViewHolder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                                }

                                @Override
                                public void onStartTrackingTouch(SeekBar seekBar) {

                                }

                                @Override
                                public void onStopTrackingTouch(SeekBar seekBar) {
                                    if (audioPlayer != null)
                                        audioPlayer.seekTo(seekBar.getProgress());
                                }
                            });



                        } else {
                            sentMediaViewHolder.svideotimelayout.setVisibility(View.GONE);
                            sentMediaViewHolder.playbutton.setVisibility(View.GONE);
                            sentMediaViewHolder.senderaudiolayout.setVisibility(View.GONE);
                            sentMediaViewHolder.documentlayout.setVisibility(View.VISIBLE);
                            sentMediaViewHolder.imagecontainer.setVisibility(View.GONE);
                            ImFlexboxLayout.LayoutParams params1 = new ImFlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            sentMediaViewHolder.senderLayout.setLayoutParams(params1);
                            //isImage = isVideo = isAudio = false;
                        }

                        /*if (isImage || isVideo) {

                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;
                            BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                            int imageHeight = options.outHeight;
                            int imageWidth = options.outWidth;

                            if (isImage) {
                                sentMediaViewHolder.svideotimelayout.setVisibility(View.GONE);
                                sentMediaViewHolder.playbutton.setVisibility(View.GONE);
                                sentMediaViewHolder.senderaudiolayout.setVisibility(View.GONE);
                                //Toast.makeText(context, path, Toast.LENGTH_SHORT).show();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    Glide.with(context).load(MediaImageViewerFragment.getImageContentUri(context, file)).fitCenter().into(sentMediaViewHolder.mainimage);
                                } else {
                                    Glide.with(context).load(file).fitCenter().into(sentMediaViewHolder.mainimage);
                                }
                            } else if (isVideo) {
                                sentMediaViewHolder.senderaudiolayout.setVisibility(View.GONE);
                                //Toast.makeText(context, fileName+" is a Video", Toast.LENGTH_SHORT).show();
                                Glide.with(context).load(MediaVideoViewerFragment.getImageContentUri(context, file)).fitCenter().into(sentMediaViewHolder.mainimage);
                                String duration = "";
                                try {
                                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                                    retriever.setDataSource(file.getAbsolutePath());
                                    duration = "";
                                    int dur_in_mills = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                                    duration = convertToMMSS(dur_in_mills + "");

                                    sentMediaViewHolder.svideotimetext.setText(duration);
                                    //sentViewHolder.mainimage.setImageBitmap(ThumbnailUtils.createVideoThumbnail(paths[f], MediaStore.Video.Thumbnails.MINI_KIND));
                                    //imageHeight = 300;
                                    //imageWidth = 400;
                                    imageHeight = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                                    imageWidth = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                                    retriever.release();
                                } catch (Exception e) {
                                }
                            }

                            if (imageHeight > imageWidth) {
                                double heightpercentage = (imageHeight / (imageWidth * 0.01));
                                double tempheight = (maxwidthinpx * 0.01) * (heightpercentage);
                                if (tempheight > maxheightinpx) {
                                    double tempheight2 = ((((maxwidthinpx * 0.9) * 0.01) * (100 + heightpercentage)));
                                    if (tempheight2 >= maxheightinpx) {
                                        calculatedWidth = (int) (maxwidthinpx * 0.9);
                                        calculatedHeight = maxheightinpx;
                                    } else {
                                        calculatedWidth = (int) (maxwidthinpx * 0.9);
                                        calculatedHeight = (int) tempheight2;
                                    }
                                    //calculatedHeight=tempheight;
                                } else {
                                    calculatedWidth = maxwidthinpx;
                                    calculatedHeight = (int) tempheight;
                                }
                            } else {
                                int widthpercentage = (int) (imageHeight / (imageWidth * 0.01));
                                int minheight = (int) (maxheightinpx * 0.16);
                                int tempheight = (int) (maxwidthinpx * 0.01) * (widthpercentage);
                                if (tempheight > minheight && tempheight < maxheightinpx) {
                                    calculatedHeight = tempheight;
                                } else if (tempheight < minheight) {
                                    calculatedHeight = (int) minheight;
                                } else {
                                    calculatedHeight = maxheightinpx;
                                }
                            }


                            File finalFile = file;
                            sentMediaViewHolder.imagecontainer.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(((MainActivity) context), MediaViewerActivity.class);
                                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(((MainActivity) context), sentMediaViewHolder.mainimage, ViewCompat.getTransitionName(sentMediaViewHolder.mainimage));
                                    i.putExtra("path", finalFile.getAbsolutePath());
                                    context.startActivity(i, options.toBundle());
                                }
                            });
                            sentMediaViewHolder.imagecontainer.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View view) {
                                    sentMediaViewHolder.fullbackgroundlayout.performLongClick();
                                    return true;
                                }
                            });

                        } else if (isAudio) if(isAudio) {
                            //Toast.makeText(context, fileName+" is a Audio", Toast.LENGTH_SHORT).show();
                            calculatedHeight = sentMediaViewHolder.senderaudiolayout.getHeight();
                            calculatedWidth = maxwidthinpx;
                            sentMediaViewHolder.sentmessage.setVisibility(View.GONE);
                            sentMediaViewHolder.mainimage.setVisibility(View.GONE);
                            sentMediaViewHolder.senderaudiolayout.setVisibility(View.VISIBLE);
                            sentMediaViewHolder.svideotimelayout.setVisibility(View.GONE);
                            sentMediaViewHolder.playbutton.setVisibility(View.GONE);


                            String audioduration = "0:00";
                            try {
                                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                                mediaMetadataRetriever.setDataSource(path);
                                int dur = Integer.valueOf(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                                if (currentAudioIndex != sentMediaViewHolder.getAdapterPosition())
                                    sentMediaViewHolder.audioduration.setText(convertToMMSS(dur + ""));
                                audioduration = convertToMMSS(dur + "");
                            } catch (Exception e) {
                                Toast.makeText(context, "Unable to get the duration of this audio file" + e.toString(), Toast.LENGTH_SHORT).show();
                            }

                            String finalAudioduration = audioduration;
                            if (currentAudioIndex != sentMediaViewHolder.getAdapterPosition()) {
                                sentMediaViewHolder.saudioplaybutton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_lightwhite_play, null));
                                sentMediaViewHolder.audioduration.setText(finalAudioduration);
                                sentMediaViewHolder.seekBar.setProgress(0);
                            }
                            sentMediaViewHolder.saudioplaybutton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ((MainActivity) context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Runnable audioRunnable = new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (audioPlayer != null && audioPlayer.isPlaying()) {
                                                        if (currentAudioIndex == sentMediaViewHolder.getAdapterPosition()) {
                                                            int progress = audioPlayer.getCurrentPosition();
                                                            sentMediaViewHolder.seekBar.setProgress(progress);
                                                            sentMediaViewHolder.audioduration.setText(convertToMMSS(progress + ""));
                                                            myHandler.postDelayed(this, 500);
                                                        }
                                                    }
                                                }
                                            };
                                            if (audioPlayer == null || currentAudioIndex != sentMediaViewHolder.getAdapterPosition()) {
                                                if (audioPlayer != null) {
                                                    audioPlayer.release();
                                                    audioPlayer = null;
                                                    myHandler.removeCallbacks(audioRunnable);
                                                    sentMediaViewHolder.saudioplaybutton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_lightwhite_play, null));
                                                    notifyItemChanged(currentAudioIndex);
                                                }
                                                audioPlayer = MediaPlayer.create(context, Uri.fromFile(new File(path)));
                                                audioPlayer.start();
                                                sentMediaViewHolder.saudioplaybutton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_pause_round, null));
                                                myHandler.post(audioRunnable);
                                                sentMediaViewHolder.seekBar.setMax(audioPlayer.getDuration());
                                                currentAudioIndex = sentMediaViewHolder.getAdapterPosition();
                                                audioPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                                    @Override
                                                    public void onCompletion(MediaPlayer mediaPlayer) {
                                                        audioPlayer.release();
                                                        audioPlayer = null;
                                                        myHandler.removeCallbacks(audioRunnable);
                                                        sentMediaViewHolder.saudioplaybutton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_lightwhite_play, null));
                                                        sentMediaViewHolder.audioduration.setText(finalAudioduration);
                                                        sentMediaViewHolder.seekBar.setProgress(0);
                                                        currentAudioIndex = -1;
                                                    }
                                                });
                                            } else if (audioPlayer.isPlaying()) {
                                                audioPlayer.pause();
                                                sentMediaViewHolder.saudioplaybutton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_lightwhite_play, null));
                                                myHandler.removeCallbacks(audioRunnable);
                                            } else {
                                                audioPlayer.start();
                                                sentMediaViewHolder.saudioplaybutton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_pause_round, null));
                                                myHandler.removeCallbacks(audioRunnable);
                                                myHandler.post(audioRunnable);
                                            }
                                        }
                                    });
                                }
                            });

                            sentMediaViewHolder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                                }

                                @Override
                                public void onStartTrackingTouch(SeekBar seekBar) {

                                }

                                @Override
                                public void onStopTrackingTouch(SeekBar seekBar) {
                                    if (audioPlayer != null)
                                        audioPlayer.seekTo(seekBar.getProgress());
                                }
                            });

                        } else {
                            //Toast.makeText(context, fileName+" just is a File", Toast.LENGTH_SHORT).show();
                            sentMediaViewHolder.svideotimelayout.setVisibility(View.GONE);
                            sentMediaViewHolder.playbutton.setVisibility(View.GONE);
                            sentMediaViewHolder.senderaudiolayout.setVisibility(View.GONE);
                            sentMediaViewHolder.mainimage.setVisibility(View.GONE);
                            sentMediaViewHolder.documentlayout.setVisibility(View.VISIBLE);
                            sentMediaViewHolder.filename.setText(fileName);
                            sentMediaViewHolder.fileextension.setText(fileName.substring(fileName.lastIndexOf('.') + 1));
                        } */


                        /*if (false) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                        int imageHeight = options.outHeight;
                        int imageWidth = options.outWidth;
                        if (f == 5 || f == 6) {
                            // video code
                            Glide.with(context).load(MediaVideoViewerFragment.getImageContentUri(context, file)).fitCenter().into(sentViewHolder.mainimage);
                            String duration = "";
                            try {
                                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                                retriever.setDataSource(file.getAbsolutePath());
                                duration = "";
                                int dur_in_mills = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                                duration = convertToMMSS(dur_in_mills + "");

                                sentViewHolder.svideotimetext.setText(duration);
                                //sentViewHolder.mainimage.setImageBitmap(ThumbnailUtils.createVideoThumbnail(paths[f], MediaStore.Video.Thumbnails.MINI_KIND));
                                //imageHeight = 300;
                                //imageWidth = 400;
                                imageHeight = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                                imageWidth = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                                retriever.release();
                            } catch (Exception e) {
                            }
                            sentViewHolder.playbutton.setVisibility(View.VISIBLE);
                            sentViewHolder.svideotimelayout.setVisibility(View.VISIBLE);
                            isAudio = false;
                        } else if (f == 0) {
                            isAudio = true;
                        } else {
                            isAudio = false;
                            sentViewHolder.svideotimelayout.setVisibility(View.GONE);
                            sentViewHolder.playbutton.setVisibility(View.GONE);
                        }
                        int baseHeight = 0;
                        int baseWidth = 0;
                        // for both video and image
                        *//*int calculatedHeight = maxheightinpx;
                        int calculatedWidth = maxwidthinpx;
                        if (imageHeight > imageWidth) {
                            double heightpercentage = (imageHeight / (imageWidth * 0.01));
                            double tempheight = (maxwidthinpx * 0.01) * (heightpercentage);
                            if (tempheight > maxheightinpx) {
                                double tempheight2 = ((((maxwidthinpx * 0.9) * 0.01) * (100 + heightpercentage)));
                                if (tempheight2 >= maxheightinpx) {
                                    calculatedWidth = (int) (maxwidthinpx * 0.9);
                                    calculatedHeight = maxheightinpx;
                                } else {
                                    calculatedWidth = (int) (maxwidthinpx * 0.9);
                                    calculatedHeight = (int) tempheight2;
                                }
                                //calculatedHeight=tempheight;
                            } else {
                                calculatedWidth = maxwidthinpx;
                                calculatedHeight = (int) tempheight;
                            }
                        } else {
                            int widthpercentage = (int) (imageHeight / (imageWidth * 0.01));
                            int minheight = (int) (maxheightinpx * 0.16);
                            int tempheight = (int) (maxwidthinpx * 0.01) * (widthpercentage);
                            if (tempheight > minheight && tempheight < maxheightinpx) {
                                calculatedHeight = tempheight;
                            } else if (tempheight < minheight) {
                                calculatedHeight = (int) minheight;
                            } else {
                                calculatedHeight = maxheightinpx;
                            }
                        }*//*
                        if (isAudio) {
                            calculatedHeight = sentViewHolder.senderaudiolayout.getHeight();
                            calculatedWidth = maxwidthinpx;
                            sentViewHolder.mainimage.setVisibility(View.GONE);
                            sentViewHolder.senderaudiolayout.setVisibility(View.VISIBLE);
                            sentViewHolder.svideotimelayout.setVisibility(View.GONE);
                            sentViewHolder.playbutton.setVisibility(View.GONE);
                        } else {
                            sentViewHolder.senderaudiolayout.setVisibility(View.GONE);
                            sentViewHolder.mainimage.setVisibility(View.VISIBLE);
                        }
                        sentViewHolder.sentmessage.setText(file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("/")));
                        int finalF = f;
                        sentViewHolder.imagecontainer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(((MainActivity) context), MediaViewerActivity.class);
                                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(((MainActivity) context), sentViewHolder.mainimage, ViewCompat.getTransitionName(sentViewHolder.mainimage));
                                i.putExtra("path", finalF);
                                context.startActivity(i, options.toBundle());
                            }
                        });
                        sentViewHolder.imagecontainer.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                sentViewHolder.fullbackgroundlayout.performLongClick();
                                return true;
                            }
                        });
                        //Toast.makeText(context, "Height: "+imageHeight+" \t Width: "+imageWidth+" base: "+baseHeight, Toast.LENGTH_SHORT).show();
                */
                        /*if(f==0)
                { calculatedHeight = calculatedWidth = 0; }*//*
                         *//*ImFlexboxLayout.LayoutParams params1 = new ImFlexboxLayout.LayoutParams(calculatedWidth, calculatedHeight);
                        sentViewHolder.imagecontainer.setLayoutParams(params1);*//*
                         *//*ImFlexboxLayout.LayoutParams params2 = new ImFlexboxLayout.LayoutParams(maxwidthinpx,RelativeLayout.LayoutParams.WRAP_CONTENT);
                sentViewHolder.documentlayout.setLayoutParams(params2);*//*
                         *//*ImFlexboxLayout.LayoutParams params2 = new ImFlexboxLayout.LayoutParams(ImFlexboxLayout.LayoutParams.WRAP_CONTENT, ImFlexboxLayout.LayoutParams.WRAP_CONTENT);
                sentViewHolder.sentmessage.setLayoutParams(params2);*//*
                        sentViewHolder.sentmessage.setMaxWidth(params1.width - 30);
                        if (f == 4 || true) {
                            sentViewHolder.sentmessage.setVisibility(View.GONE);
                        } else {
                            sentViewHolder.sentmessage.setVisibility(View.VISIBLE);
                        }
                    }

                    } else {
                        //sentViewHolder.sentmessage.setText(("This is just a message (FNF)"));
                        sentViewHolder.documentlayout.setVisibility(View.GONE);
                        sentViewHolder.imagecontainer.setVisibility(View.GONE);
                        sentViewHolder.senderaudiolayout.setVisibility(View.GONE);
                        sentViewHolder.playbutton.setVisibility(View.GONE);
                        sentViewHolder.svideotimelayout.setVisibility(View.GONE);
                        sentViewHolder.mainimage.setVisibility(View.GONE);

                        ImFlexboxLayout.LayoutParams params1 = new ImFlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        sentViewHolder.senderLayout.setLayoutParams(params1);
                    }





                //sentViewHolder.sentmessage.setVisibility(View.GONE);
            } else {
                    //sentViewHolder.sentmessage.setText(("This is just a message"));
                    //sentViewHolder.sentmessage.setText(("false"));
                    sentViewHolder.documentlayout.setVisibility(View.GONE);
                    sentViewHolder.imagecontainer.setVisibility(View.GONE);
                    sentViewHolder.senderaudiolayout.setVisibility(View.GONE);
                    sentViewHolder.playbutton.setVisibility(View.GONE);
                    sentViewHolder.svideotimelayout.setVisibility(View.GONE);
                    sentViewHolder.mainimage.setVisibility(View.GONE);
                    ImFlexboxLayout.LayoutParams params1 = new ImFlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    sentViewHolder.senderLayout.setLayoutParams(params1);
                    //just a normal message :-)
            }*/

                    }

                    if (position != 0 && canigetName(messageList.get(position - 1))) {
                        if (!(Objects.requireNonNull(getName(messageList.get(position - 1)))).equals(this.userName1)) {
                            params.setMargins(0, 8, 0, 0);
                            sentMediaViewHolder.fullbackgroundlayout.setLayoutParams(params);
                            sentMediaViewHolder.senderBubbleBg.setBackgroundResource(R.drawable.bg_sender);
                        } else {
                            params.setMargins(0, 0, 0, 0);
                            sentMediaViewHolder.fullbackgroundlayout.setLayoutParams(params);
                            sentMediaViewHolder.senderBubbleBg.setBackgroundResource(R.drawable.bg_sender_without_bubble);
                        }
                    } else {
                        params.setMargins(0, 8, 0, 0);
                        sentMediaViewHolder.fullbackgroundlayout.setLayoutParams(params);
                        sentMediaViewHolder.senderBubbleBg.setBackgroundResource(R.drawable.bg_sender);
                    }
                    if (isNightMode) {
                        sentMediaViewHolder.sentmessage.setTextColor(((MainActivity) context).getResources().getColor(R.color.white));
                        sentMediaViewHolder.senttime.setTextColor(((MainActivity) context).getResources().getColor(R.color.lightwhite));
                    } else {
                        sentMediaViewHolder.sentmessage.setTextColor(((MainActivity) context).getResources().getColor(R.color.black));
                        sentMediaViewHolder.senttime.setTextColor(((MainActivity) context).getResources().getColor(R.color.lightblack));
                    }
                    if (triggeredposition == position) {
                        sentMediaViewHolder.fullbackgroundlayout.setForeground(context.getResources().getDrawable(R.drawable.bg_selected_message));
                        ObjectAnimator anim = ObjectAnimator.ofInt(sentMediaViewHolder.fullbackgroundlayout.getForeground(), "alpha", 255, 0);
                        anim.setDuration(1500).setStartDelay(100);
                        anim.start();
                    }

                    if (indexes.contains(position + "") || triggeredposition == position) {
                        if (!(triggeredposition == position)) {
                            sentMediaViewHolder.fullbackgroundlayout.setForeground(context.getResources().getDrawable(R.drawable.bg_selected_message));
                        }
                    } else {
                        sentMediaViewHolder.fullbackgroundlayout.setForeground(null);
                    }

                    if (searchedIndexes.contains(String.valueOf(position))) {
                        String highlightedtext = "";
                        if (!isNightMode) {
                            highlightedtext = "<span style='background-color:yellow'>" + searchedword + "</span>";
                        } else {
                            highlightedtext = "<span style='background-color:#ffffff; color:black'>" + searchedword + "</span>";
                        }
                        String textu = message.replaceAll("(?i)" + Pattern.quote(searchedword), highlightedtext);
                        sentMediaViewHolder.sentmessage.setText(Html.fromHtml(textu, 0));
                    }

                    sentMediaViewHolder.fullbackgroundlayout.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onClick(View view) {
                            if (selectionmode) {
                                if (sentMediaViewHolder.isselected) {
                                    sentMediaViewHolder.isselected = false;
                                    count--;
                                    selectedcount.setText(String.valueOf(count));
                                    selectedmediasitems.remove(sentMediaViewHolder);
                                    indexes.remove(sentMediaViewHolder.getAdapterPosition() + "");
                                    sentMediaViewHolder.fullbackgroundlayout.setForeground(null);
                                } else {
                                    sentMediaViewHolder.isselected = true;
                                    count++;
                                    selectedcount.setText(String.valueOf(count));
                                    selectedmediasitems.add(sentMediaViewHolder);
                                    indexes.add(sentMediaViewHolder.getAdapterPosition() + "");
                                    sentMediaViewHolder.fullbackgroundlayout.setForeground(context.getResources().getDrawable(R.drawable.bg_selected_message));
                                }
                            }

                            if (count > 1) {
                                editmode = false;
                                editmessagelayout.setVisibility(View.GONE);
                            }

                            if (count == 0) {
                                hideThisViewByAlpha(chatmenulayout);
                                selectionmode = false;
                            } else {
                                if (issearching) {
                                    issearching = false;
                                    triggeredposition = -1;
                                    searchedIndexes.clear();
                                    maininputlayout.setVisibility(View.VISIBLE);
                                    searchlayout.setVisibility(View.GONE);
                                }
                                revealThisViewByAlpha(chatmenulayout);
                            }
                        }
                    });

                    sentMediaViewHolder.fullbackgroundlayout.setOnLongClickListener(new View.OnLongClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public boolean onLongClick(View view) {

                            if (!selectionmode) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    v.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE));
                                } else {
                                    v.vibrate(30);
                                }
                                selectionmode = true;
                            }
                            if (sentMediaViewHolder.isselected) {
                                sentMediaViewHolder.isselected = false;
                                count--;
                                selectedcount.setText(String.valueOf(count));
                                selectedmediasitems.remove(sentMediaViewHolder);
                                indexes.remove(sentMediaViewHolder.getAdapterPosition() + "");
                                sentMediaViewHolder.fullbackgroundlayout.setForeground(null);
                            } else {
                                sentMediaViewHolder.isselected = true;
                                count++;
                                selectedcount.setText(String.valueOf(count));
                                selectedmediasitems.add(sentMediaViewHolder);
                                indexes.add(sentMediaViewHolder.getAdapterPosition() + "");
                                sentMediaViewHolder.fullbackgroundlayout.setForeground(context.getResources().getDrawable(R.drawable.bg_selected_message));
                            }

                            if (count > 1) {
                                editmode = false;
                                editmessagelayout.setVisibility(View.GONE);
                            }

                            if (count == 0) {
                                hideThisViewByAlpha(chatmenulayout);
                                selectionmode = false;
                            } else {
                                if (issearching) {
                                    issearching = false;
                                    triggeredposition = -1;
                                    searchedIndexes.clear();
                                    maininputlayout.setVisibility(View.VISIBLE);
                                    searchlayout.setVisibility(View.GONE);
                                }
                                revealThisViewByAlpha(chatmenulayout);
                                editmessagelayout.setVisibility(View.GONE);
                            }
                            return true;
                        }
                    });


        } else if (getItemViewType(position) == 4) { //name.equals(userName2) || getItemViewType(position) == 1
            RecievedMediaViewHolder recievedMediaViewHolder = (RecievedMediaViewHolder) holder;
            String recievername;
            recievedMediaViewHolder.recievedmessage.setText(message);
            recievedMediaViewHolder.recievedtime.setText(time);
            if (isthisagroup) {
                recievedMediaViewHolder.recievername.setVisibility(View.VISIBLE);
                recievedMediaViewHolder.recievername.setText(getName(messageList.get(position)));
                recievername = getName(messageList.get(position));
                recievedMediaViewHolder.recievername.setTextColor(Color.parseColor(colorList.get(groupmemebernames.indexOf(recievername))));
            } else {
                recievedMediaViewHolder.recievername.setVisibility(View.GONE);
                recievername = userName2;
            }

            File file = new File("This file does not exist");
            String fileName = "";

            if (message.contains("(file attached)")) {

                String text = "";

                String[] parts = message.split("\\(file attached\\)");
                for (String ss : parts) {
                    if (ss.contains("(file attached)"))
                        ss = ss.replaceAll("(?=\\(file attached\\))", "");
                    ss = ss.trim();
                    if (!ss.isEmpty())
                        text += ss + "\n";
                    File newFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsViewer/chats/" + tablename + "/" + ss);
                    if (newFile.exists()) {
                        fileName = ss;
                        file = newFile;
                        break;
                    }
                }

                if (file.exists()) {
                    String path = file.getAbsolutePath();

                    boolean isImage, isVideo, isAudio;
                    isImage = isVideo = isAudio = false;

                    if (path.contains("IMG-") || path.toLowerCase().contains(".jpg") || path.toLowerCase().contains(".png") || path.toLowerCase().contains(".jpeg")) {
                        isImage = true;
                        isVideo = isAudio = false;
                        Toast.makeText(context, path, Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            file = new File(Environment.DIRECTORY_DOWNLOADS);
                        } else {
                            file = new File(Environment.getExternalStorageDirectory() + "/WhatsViewer/chats/" + tablename + "/" + fileName);
                        }

                    } else if (path.contains("VID-") || path.contains(".mp4") || path.contains(".mkv") || path.contains(".3gp") || path.contains(".mpeg") || path.contains(".m4a")) {
                        isImage = isAudio = false;
                        isVideo = true;

                        recievedMediaViewHolder.playbutton.setVisibility(View.VISIBLE);
                        recievedMediaViewHolder.rvideotimelayout.setVisibility(View.VISIBLE);
                    } else if (path.contains("AUD-") || path.contains(".mp3") || path.contains(".ogg") || path.contains(".opus")) {
                        isImage = isVideo = false;
                        isAudio = true;
                    } else {
                        isImage = isVideo = isAudio = false;
                    }

                    int calculatedHeight = maxheightinpx;
                    int calculatedWidth = maxwidthinpx;

                    if (isImage || isVideo) {

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                        int imageHeight = options.outHeight;
                        int imageWidth = options.outWidth;

                        if (isImage) {
                            recievedMediaViewHolder.rvideotimelayout.setVisibility(View.GONE);
                            recievedMediaViewHolder.playbutton.setVisibility(View.GONE);
                            recievedMediaViewHolder.recieverAudioLayout.setVisibility(View.GONE);
                            //Toast.makeText(context, path, Toast.LENGTH_SHORT).show();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                Glide.with(context).load(MediaImageViewerFragment.getImageContentUri(context, file)).fitCenter().into(recievedMediaViewHolder.mainimage);
                            } else {
                                Glide.with(context).load(file).fitCenter().into(recievedMediaViewHolder.mainimage);
                            }
                        } else if (isVideo) {
                            recievedMediaViewHolder.recieverAudioLayout.setVisibility(View.GONE);
                            //Toast.makeText(context, fileName+" is a Video", Toast.LENGTH_SHORT).show();
                            Glide.with(context).load(MediaVideoViewerFragment.getImageContentUri(context, file)).fitCenter().into(recievedMediaViewHolder.mainimage);
                            String duration = "";
                            try {
                                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                                retriever.setDataSource(file.getAbsolutePath());
                                duration = "";
                                int dur_in_mills = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                                duration = convertToMMSS(dur_in_mills + "");

                                recievedMediaViewHolder.rvideotimetext.setText(duration);
                                //sentViewHolder.mainimage.setImageBitmap(ThumbnailUtils.createVideoThumbnail(paths[f], MediaStore.Video.Thumbnails.MINI_KIND));
                                //imageHeight = 300;
                                //imageWidth = 400;
                                imageHeight = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                                imageWidth = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                                retriever.release();
                            } catch (Exception e) {
                            }
                        }

                        if (imageHeight > imageWidth) {
                            double heightpercentage = (imageHeight / (imageWidth * 0.01));
                            double tempheight = (maxwidthinpx * 0.01) * (heightpercentage);
                            if (tempheight > maxheightinpx) {
                                double tempheight2 = ((((maxwidthinpx * 0.9) * 0.01) * (100 + heightpercentage)));
                                if (tempheight2 >= maxheightinpx) {
                                    calculatedWidth = (int) (maxwidthinpx * 0.9);
                                    calculatedHeight = maxheightinpx;
                                } else {
                                    calculatedWidth = (int) (maxwidthinpx * 0.9);
                                    calculatedHeight = (int) tempheight2;
                                }
                                //calculatedHeight=tempheight;
                            } else {
                                calculatedWidth = maxwidthinpx;
                                calculatedHeight = (int) tempheight;
                            }
                        } else {
                            int widthpercentage = (int) (imageHeight / (imageWidth * 0.01));
                            int minheight = (int) (maxheightinpx * 0.16);
                            int tempheight = (int) (maxwidthinpx * 0.01) * (widthpercentage);
                            if (tempheight > minheight && tempheight < maxheightinpx) {
                                calculatedHeight = tempheight;
                            } else if (tempheight < minheight) {
                                calculatedHeight = (int) minheight;
                            } else {
                                calculatedHeight = maxheightinpx;
                            }
                        }


                        File finalFile = file;
                        recievedMediaViewHolder.imagecontainer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(((MainActivity) context), MediaViewerActivity.class);
                                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(((MainActivity) context), recievedMediaViewHolder.mainimage, ViewCompat.getTransitionName(recievedMediaViewHolder.mainimage));
                                i.putExtra("path", finalFile.getAbsolutePath());
                                context.startActivity(i, options.toBundle());
                            }
                        });
                        recievedMediaViewHolder.imagecontainer.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                recievedMediaViewHolder.fullbackgroundlayout.performLongClick();
                                return true;
                            }
                        });

                    } else if (isAudio) {
                        //Toast.makeText(context, fileName+" is a Audio", Toast.LENGTH_SHORT).show();
                        calculatedHeight = recievedMediaViewHolder.recieverAudioLayout.getHeight();
                        calculatedWidth = maxwidthinpx;
                        recievedMediaViewHolder.recievedmessage.setVisibility(View.GONE);
                        recievedMediaViewHolder.mainimage.setVisibility(View.GONE);
                        recievedMediaViewHolder.recieverAudioLayout.setVisibility(View.VISIBLE);
                        recievedMediaViewHolder.rvideotimelayout.setVisibility(View.GONE);
                        recievedMediaViewHolder.playbutton.setVisibility(View.GONE);


                        String audioduration = "0:00";
                        try {
                            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                            mediaMetadataRetriever.setDataSource(path);
                            int dur = Integer.valueOf(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                            if (currentAudioIndex != recievedMediaViewHolder.getAdapterPosition())
                                recievedMediaViewHolder.audioDuration.setText(convertToMMSS(dur + ""));
                            audioduration = convertToMMSS(dur + "");
                        } catch (Exception e) {
                            Toast.makeText(context, "Unable to get the duration of this audio file" + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                        String finalAudioduration = audioduration;
                        if (currentAudioIndex != recievedMediaViewHolder.getAdapterPosition()) {
                            recievedMediaViewHolder.audioDuration.setText(finalAudioduration);
                            recievedMediaViewHolder.seekBar.setProgress(0);
                            recievedMediaViewHolder.raudioplaybutton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_lightwhite_play, null));
                        }
                        recievedMediaViewHolder.raudioplaybutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ((MainActivity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Runnable audioRunnable = new Runnable() {
                                            @Override
                                            public void run() {
                                                if (audioPlayer != null && audioPlayer.isPlaying()) {
                                                    if (currentAudioIndex == recievedMediaViewHolder.getAdapterPosition()) {
                                                        int progress = audioPlayer.getCurrentPosition();
                                                        recievedMediaViewHolder.seekBar.setProgress(progress);
                                                        recievedMediaViewHolder.audioDuration.setText(convertToMMSS(progress + ""));
                                                        myHandler.postDelayed(this, 500);
                                                    }
                                                }
                                            }
                                        };
                                        if (audioPlayer == null || currentAudioIndex != recievedMediaViewHolder.getAdapterPosition()) {
                                            if (audioPlayer != null) {
                                                audioPlayer.release();
                                                audioPlayer = null;
                                                myHandler.removeCallbacks(audioRunnable);
                                                notifyItemChanged(currentAudioIndex);
                                            }
                                            audioPlayer = MediaPlayer.create(context, Uri.fromFile(new File(path)));
                                            audioPlayer.start();
                                            recievedMediaViewHolder.raudioplaybutton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_pause_round, null));
                                            myHandler.post(audioRunnable);
                                            recievedMediaViewHolder.seekBar.setMax(audioPlayer.getDuration());
                                            currentAudioIndex = recievedMediaViewHolder.getAdapterPosition();
                                            audioPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                                @Override
                                                public void onCompletion(MediaPlayer mediaPlayer) {
                                                    audioPlayer.release();
                                                    audioPlayer = null;
                                                    myHandler.removeCallbacks(audioRunnable);
                                                    recievedMediaViewHolder.raudioplaybutton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_lightwhite_play, null));
                                                    recievedMediaViewHolder.audioDuration.setText(finalAudioduration);
                                                    recievedMediaViewHolder.seekBar.setProgress(0);
                                                    currentAudioIndex = -1;
                                                }
                                            });
                                        } else if (audioPlayer.isPlaying()) {
                                            audioPlayer.pause();
                                            recievedMediaViewHolder.raudioplaybutton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_lightwhite_play, null));
                                            myHandler.removeCallbacks(audioRunnable);
                                        } else {
                                            audioPlayer.start();
                                            recievedMediaViewHolder.raudioplaybutton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_pause_round, null));
                                            myHandler.removeCallbacks(audioRunnable);
                                            myHandler.post(audioRunnable);
                                        }
                                    }
                                });
                            }
                        });

                        recievedMediaViewHolder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                                if (audioPlayer != null)
                                    audioPlayer.seekTo(seekBar.getProgress());
                            }
                        });

                    } else {
                        //Toast.makeText(context, fileName+" just is a File", Toast.LENGTH_SHORT).show();
                        recievedMediaViewHolder.rvideotimelayout.setVisibility(View.GONE);
                        recievedMediaViewHolder.playbutton.setVisibility(View.GONE);
                        recievedMediaViewHolder.recieverAudioLayout.setVisibility(View.GONE);
                        recievedMediaViewHolder.mainimage.setVisibility(View.GONE);
                        recievedMediaViewHolder.documentLayout.setVisibility(View.VISIBLE);
                        recievedMediaViewHolder.fileName.setText(fileName);
                        String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
                        recievedMediaViewHolder.fileExtension.setText(extension);
                        recievedMediaViewHolder.fileExtension.setText(extension);
                    }

                    ImFlexboxLayout.LayoutParams params1 = new ImFlexboxLayout.LayoutParams(calculatedWidth, calculatedHeight);
                    recievedMediaViewHolder.imagecontainer.setLayoutParams(params1);


                        /*if (false) {
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;
                            BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                            int imageHeight = options.outHeight;
                            int imageWidth = options.outWidth;
                            if (f == 5 || f == 6) {
                                // video code
                                Glide.with(context).load(MediaVideoViewerFragment.getImageContentUri(context, file)).fitCenter().into(recievedViewHolder.mainimage);
                                String duration = "";
                                try {
                                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                                    retriever.setDataSource(file.getAbsolutePath());
                                    duration = "";
                                    int dur_in_mills = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                                    duration = convertToMMSS(dur_in_mills + "");

                                    recievedMediaViewHolder.rvideotimetext.setText(duration);
                                    //sentViewHolder.mainimage.setImageBitmap(ThumbnailUtils.createVideoThumbnail(paths[f], MediaStore.Video.Thumbnails.MINI_KIND));
                                    //imageHeight = 300;
                                    //imageWidth = 400;
                                    imageHeight = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                                    imageWidth = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                                    retriever.release();
                                } catch (Exception e) {
                                }
                                recievedMediaViewHolder.playbutton.setVisibility(View.VISIBLE);
                                recievedMediaViewHolder.rvideotimelayout.setVisibility(View.VISIBLE);
                                isAudio = false;
                            } else if (f == 0) {
                                isAudio = true;
                            } else {
                                isAudio = false;
                                recievedMediaViewHolder.rvideotimelayout.setVisibility(View.GONE);
                                recievedMediaViewHolder.playbutton.setVisibility(View.GONE);
                            }
                            int baseHeight = 0;
                            int baseWidth = 0;
                            // for both video and image
                        /*int calculatedHeight = maxheightinpx;
                        int calculatedWidth = maxwidthinpx;
                        if (imageHeight > imageWidth) {
                            double heightpercentage = (imageHeight / (imageWidth * 0.01));
                            double tempheight = (maxwidthinpx * 0.01) * (heightpercentage);
                            if (tempheight > maxheightinpx) {
                                double tempheight2 = ((((maxwidthinpx * 0.9) * 0.01) * (100 + heightpercentage)));
                                if (tempheight2 >= maxheightinpx) {
                                    calculatedWidth = (int) (maxwidthinpx * 0.9);
                                    calculatedHeight = maxheightinpx;
                                } else {
                                    calculatedWidth = (int) (maxwidthinpx * 0.9);
                                    calculatedHeight = (int) tempheight2;
                                }
                                //calculatedHeight=tempheight;
                            } else {
                                calculatedWidth = maxwidthinpx;
                                calculatedHeight = (int) tempheight;
                            }
                        } else {
                            int widthpercentage = (int) (imageHeight / (imageWidth * 0.01));
                            int minheight = (int) (maxheightinpx * 0.16);
                            int tempheight = (int) (maxwidthinpx * 0.01) * (widthpercentage);
                            if (tempheight > minheight && tempheight < maxheightinpx) {
                                calculatedHeight = tempheight;
                            } else if (tempheight < minheight) {
                                calculatedHeight = (int) minheight;
                            } else {
                                calculatedHeight = maxheightinpx;
                            }
                        }*//*
                            if (isAudio) {
                                calculatedHeight = recievedViewHolder.recieverAudioLayout.getHeight();
                                calculatedWidth = maxwidthinpx;
                                recievedViewHolder.mainimage.setVisibility(View.GONE);
                                recievedViewHolder.recieverAudioLayout.setVisibility(View.VISIBLE);
                                recievedViewHolder.rvideotimelayout.setVisibility(View.GONE);
                                recievedViewHolder.playbutton.setVisibility(View.GONE);
                            } else {
                                recievedViewHolder.recieverAudioLayout.setVisibility(View.GONE);
                                recievedViewHolder.mainimage.setVisibility(View.VISIBLE);
                            }
                            recievedViewHolder.recievedmessage.setText(file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("/")));
                            int finalF = f;
                            recievedViewHolder.imagecontainer.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(((MainActivity) context), MediaViewerActivity.class);
                                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(((MainActivity) context), recievedViewHolder.mainimage, ViewCompat.getTransitionName(recievedViewHolder.mainimage));
                                    i.putExtra("path", finalF);
                                    context.startActivity(i, options.toBundle());
                                }
                            });
                            recievedViewHolder.imagecontainer.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View view) {
                                    recievedViewHolder.fullbackgroundlayout.performLongClick();
                                    return true;
                                }
                            });
                            //Toast.makeText(context, "Height: "+imageHeight+" \t Width: "+imageWidth+" base: "+baseHeight, Toast.LENGTH_SHORT).show();
                *//*if(f==0)
                { calculatedHeight = calculatedWidth = 0; }*//*
                     *//*ImFlexboxLayout.LayoutParams params1 = new ImFlexboxLayout.LayoutParams(calculatedWidth, calculatedHeight);
                        sentViewHolder.imagecontainer.setLayoutParams(params1);*//*
                     *//*ImFlexboxLayout.LayoutParams params2 = new ImFlexboxLayout.LayoutParams(maxwidthinpx,RelativeLayout.LayoutParams.WRAP_CONTENT);
                sentViewHolder.documentlayout.setLayoutParams(params2);*//*
                     *//*ImFlexboxLayout.LayoutParams params2 = new ImFlexboxLayout.LayoutParams(ImFlexboxLayout.LayoutParams.WRAP_CONTENT, ImFlexboxLayout.LayoutParams.WRAP_CONTENT);
                sentViewHolder.sentmessage.setLayoutParams(params2);*//*
                            recievedViewHolder.recievedmessage.setMaxWidth(params1.width - 30);
                            *//*if (1 == 4 || true) {
                                recievedViewHolder.recievedmessage.setVisibility(View.GONE);
                            } else {
                                recievedViewHolder.recievedmessage.setVisibility(View.VISIBLE);
                            }*//*
                        }

                    } else {
                        //Toast.makeText(context, fileName+" does not exist", Toast.LENGTH_SHORT).show();
                        recievedViewHolder.documentLayout.setVisibility(View.GONE);
                        recievedViewHolder.imagecontainer.setVisibility(View.GONE);
                        recievedViewHolder.recieverAudioLayout.setVisibility(View.GONE);
                        recievedViewHolder.playbutton.setVisibility(View.GONE);
                        recievedViewHolder.rvideotimelayout.setVisibility(View.GONE);
                        recievedViewHolder.documentLayout.setVisibility(View.GONE);
                    }



                    //sentViewHolder.sentmessage.setVisibility(View.GONE);
                } else {
                    recievedViewHolder.documentLayout.setVisibility(View.GONE);
                    recievedViewHolder.imagecontainer.setVisibility(View.GONE);
                    recievedViewHolder.recieverAudioLayout.setVisibility(View.GONE);
                    recievedViewHolder.playbutton.setVisibility(View.GONE);
                    recievedViewHolder.rvideotimelayout.setVisibility(View.GONE);
                    recievedViewHolder.documentLayout.setVisibility(View.GONE);
                    //just a normal message :-)
                }

                if(false) {
                    int f = (int) Math.floor(Math.random() * 6);
                    //f = 3;
                    String[] paths = {"storage/emulated/0/Download/song.mp3", "storage/emulated/0/Download/Testimg.jpg", "storage/emulated/0/Download/Crop2.jpg", "storage/emulated/0/Download/Crop1.jpg", "storage/emulated/0/Download/Crop3.jpg", "storage/emulated/0/Download/aditya.mp4"};
                    //recievedViewHolder.mainimage.setImageURI(Uri.fromFile(new File(paths[f])));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        Glide.with(context).load(MediaImageViewerFragment.getImageContentUri(context, new File(paths[f]))).fitCenter().into(recievedViewHolder.mainimage);
                    } else {
                        Glide.with(context).load(new File(paths[f])).fitCenter().into(recievedViewHolder.mainimage);
                    }
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(paths[f], options);
                    int imageHeight = options.outHeight;
                    int imageWidth = options.outWidth;
                    String duration = "00:00";
                    if (f == 5) {
                        Glide.with(context).load(MediaVideoViewerFragment.getImageContentUri(context, new File(paths[f]))).fitCenter().into(recievedViewHolder.mainimage);
                        try {
                            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                            retriever.setDataSource(paths[f]);
                            duration = "";
                            int dur_in_mills = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                            int dur_in_seconds = dur_in_mills / 1000;
                            if (dur_in_seconds > 60) {
                                int m = dur_in_seconds / 60;
                                if (m > 60) {
                                    int h = m / 60;
                                    duration = h + ":";
                                    int min = m - (h * 60);
                                    if (min < 10)
                                        duration += "0" + min + ":";
                                    else
                                        duration += min + ":";
                                    int sec = dur_in_seconds - m * 60;
                                    if (sec < 10)
                                        duration += "0" + sec;
                                    else
                                        duration += "" + sec;
                                } else {
                                    int sec = dur_in_seconds - m * 60;
                                    if (sec < 10)
                                        duration = m + ":0" + sec;
                                    else
                                        duration = m + ":" + sec;
                                }
                            } else if (dur_in_seconds < 10) {
                                duration = "0:0" + dur_in_seconds;
                            } else {
                                duration = "0:" + dur_in_seconds;
                            }
                            imageHeight = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                            imageWidth = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                            retriever.release();
                        } catch (Exception e) {

                        }
                        recievedViewHolder.rmediarecievedtime.setText(time);
                        recievedViewHolder.rvideotimetext.setText(duration);
                        //recievedViewHolder.mainimage.setImageBitmap(ThumbnailUtils.createVideoThumbnail(paths[f], MediaStore.Video.Thumbnails.MINI_KIND));
                        //imageHeight = 300;
                        //imageWidth = 400;
                        recievedViewHolder.rvideotimelayout.setVisibility(View.VISIBLE);
                        recievedViewHolder.playbutton.setVisibility(View.VISIBLE);
                        //recievedViewHolder.recieverAudioLayout.setVisibility(View.GONE);
                        recievedViewHolder.mainimage.setVisibility(View.VISIBLE);
                        recievedViewHolder.rmediarecievedtime.setVisibility(View.VISIBLE);
                        isAudio = false;
                    } else if (f == 0) {
                        isAudio = true;
                    } else {
                        isAudio = false;
                        recievedViewHolder.rmediarecievedtime.setVisibility(View.VISIBLE);
                        recievedViewHolder.mainimage.setVisibility(View.VISIBLE);
                        //recievedViewHolder.recieverAudioLayout.setVisibility(View.GONE);
                        recievedViewHolder.rvideotimelayout.setVisibility(View.GONE);
                        recievedViewHolder.playbutton.setVisibility(View.GONE);
                    }
                    int baseHeight = 0;
                    int baseWidth = 0;
                    int calculatedHeight = maxheightinpx;
                    int calculatedWidth = maxwidthinpx;
                    if (imageHeight > imageWidth) {
                        double heightpercentage = (imageHeight / (imageWidth * 0.01));
                        double tempheight = (maxwidthinpx * 0.01) * (heightpercentage);
                        if (tempheight > maxheightinpx) {
                            double tempheight2 = ((((maxwidthinpx * 0.9) * 0.01) * (100 + heightpercentage)));
                            if (tempheight2 >= maxheightinpx) {
                                calculatedWidth = (int) (maxwidthinpx * 0.9);
                                calculatedHeight = maxheightinpx;
                            } else {
                                calculatedWidth = (int) (maxwidthinpx * 0.9);
                                calculatedHeight = (int) tempheight2;
                            }
                            //calculatedHeight=tempheight;
                        } else {
                            calculatedWidth = maxwidthinpx;
                            calculatedHeight = (int) tempheight;
                        }
                    } else {
                        int widthpercentage = (int) (imageHeight / (imageWidth * 0.01));
                        int minheight = (int) (maxheightinpx * 0.16);
                        int tempheight = (int) (maxwidthinpx * 0.01) * (widthpercentage);
                        if (tempheight > minheight && tempheight < maxheightinpx) {
                            calculatedHeight = tempheight;
                        } else if (tempheight < minheight) {
                            calculatedHeight = (int) minheight;
                        } else {
                            calculatedHeight = maxheightinpx;
                        }
                    }
                    String audioduration = "00:00";
                    if (isAudio) {
                        calculatedHeight = recievedViewHolder.recieverAudioLayout.getHeight();
                        calculatedWidth = maxwidthinpx;
                        recievedViewHolder.recievedmessage.setVisibility(View.GONE);
                        recievedViewHolder.mainimage.setVisibility(View.GONE);
                        recievedViewHolder.recieverAudioLayout.setVisibility(View.VISIBLE);
                        recievedViewHolder.rvideotimelayout.setVisibility(View.GONE);
                        recievedViewHolder.playbutton.setVisibility(View.GONE);
                        recievedViewHolder.rmediarecievedtime.setVisibility(View.GONE);
                        try {
                            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                            mediaMetadataRetriever.setDataSource("storage/emulated/0/Download/song.mp3");
                            int dur = Integer.valueOf(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                            if (currentAudioIndex != recievedViewHolder.getAdapterPosition())
                                recievedViewHolder.audioDuration.setText(convertToMMSS(dur + ""));
                            audioduration = convertToMMSS(dur + "");
                        } catch (Exception e) {
                            Toast.makeText(context, "Unable to get the duration of this audio file" + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                        String finalAudioduration = audioduration;
                        if (currentAudioIndex != recievedViewHolder.getAdapterPosition()) {
                            recievedViewHolder.raudioplaybutton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_lightwhite_play, null));
                            recievedViewHolder.audioDuration.setText(finalAudioduration);
                            recievedViewHolder.seekBar.setProgress(0);
                        }
                        recievedViewHolder.raudioplaybutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ((MainActivity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Runnable audioRunnable = new Runnable() {
                                            @Override
                                            public void run() {
                                                if (audioPlayer != null && audioPlayer.isPlaying()) {
                                                    if (currentAudioIndex == recievedViewHolder.getAdapterPosition()) {
                                                        int progress = audioPlayer.getCurrentPosition();
                                                        recievedViewHolder.seekBar.setProgress(progress);
                                                        recievedViewHolder.audioDuration.setText(convertToMMSS(progress + ""));
                                                        myHandler.postDelayed(this, 500);
                                                    }
                                                }
                                            }
                                        };
                                        if (audioPlayer == null || currentAudioIndex != recievedViewHolder.getAdapterPosition()) {
                                            if (audioPlayer != null) {
                                                audioPlayer.release();
                                                audioPlayer = null;
                                                myHandler.removeCallbacks(audioRunnable);
                                                notifyItemChanged(currentAudioIndex);
                                            }
                                            audioPlayer = MediaPlayer.create(context, Uri.fromFile(new File("storage/emulated/0/Download/song.mp3")));
                                            audioPlayer.start();
                                            recievedViewHolder.raudioplaybutton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_pause_round, null));
                                            myHandler.post(audioRunnable);
                                            recievedViewHolder.seekBar.setMax(audioPlayer.getDuration());
                                            currentAudioIndex = recievedViewHolder.getAdapterPosition();
                                            audioPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                                @Override
                                                public void onCompletion(MediaPlayer mediaPlayer) {
                                                    audioPlayer.release();
                                                    audioPlayer = null;
                                                    myHandler.removeCallbacks(audioRunnable);
                                                    recievedViewHolder.raudioplaybutton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_lightwhite_play, null));
                                                    recievedViewHolder.audioDuration.setText(finalAudioduration);
                                                    recievedViewHolder.seekBar.setProgress(0);
                                                    currentAudioIndex = -1;
                                                }
                                            });
                                        } else if (audioPlayer.isPlaying()) {
                                            audioPlayer.pause();
                                            recievedViewHolder.raudioplaybutton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_lightwhite_play, null));
                                            myHandler.removeCallbacks(audioRunnable);
                                        } else {
                                            audioPlayer.start();
                                            recievedViewHolder.raudioplaybutton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_pause_round, null));
                                            myHandler.removeCallbacks(audioRunnable);
                                            myHandler.post(audioRunnable);
                                        }
                                    }
                                });
                            }
                        });

                        recievedViewHolder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                                if (audioPlayer != null) audioPlayer.seekTo(seekBar.getProgress());
                            }
                        });
                    } else {
                        int finalF = f;
                        recievedViewHolder.imagecontainer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(((MainActivity) context), MediaViewerActivity.class);
                                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(((MainActivity) context), recievedViewHolder.mainimage, ViewCompat.getTransitionName(recievedViewHolder.mainimage));
                                i.putExtra("path", paths[finalF]);
                                context.startActivity(i, options.toBundle());
                            }
                        });
                        recievedViewHolder.recieverAudioLayout.setVisibility(View.GONE);
                    }
                    recievedViewHolder.recievedmessage.setVisibility(View.GONE);
                    //recievedViewHolder.recievedmessage.setText(paths[f].substring(paths[f].lastIndexOf("/")));
                    //Toast.makeText(context, "Height: "+imageHeight+" \t Width: "+imageWidth+" base: "+baseHeight, Toast.LENGTH_SHORT).show();


                if(f==0)
                { calculatedHeight = calculatedWidth = 0; }
                    ImFlexboxLayout.LayoutParams params1 = new ImFlexboxLayout.LayoutParams(calculatedWidth, calculatedHeight);
                    recievedViewHolder.imagecontainer.setLayoutParams(params1);
                *//*ImFlexboxLayout.LayoutParams params2 = new ImFlexboxLayout.LayoutParams(ImFlexboxLayout.LayoutParams.WRAP_CONTENT, ImFlexboxLayout.LayoutParams.WRAP_CONTENT);
                sentViewHolder.sentmessage.setLayoutParams(params2);*//*
                    recievedViewHolder.recievedmessage.setMaxWidth(params1.width - 30);
                    if (f == 4 || f == 0)
                        recievedViewHolder.recievedmessage.setVisibility(View.GONE);
                    else
                        recievedViewHolder.recievedmessage.setVisibility(View.VISIBLE);
                    //recievedViewHolder.recievername.setVisibility(View.VISIBLE);
                    //recievedViewHolder.recievedtime.setVisibility(View.GONE);
                }*/
                }


                if (position != 0 && canigetName(messageList.get(position - 1))) {
                    if (!(Objects.requireNonNull(getName(messageList.get(position - 1)))).equals(recievername)) {
                        params.setMargins(0, 8, 0, 0);
                        recievedMediaViewHolder.fullbackgroundlayout.setLayoutParams(params);
                        recievedMediaViewHolder.recieverLayout.setBackgroundResource(R.drawable.bg_reciever);
                    } else {
                        recievedMediaViewHolder.recievername.setVisibility(View.GONE);
                        params.setMargins(0, 0, 0, 0);
                        recievedMediaViewHolder.fullbackgroundlayout.setLayoutParams(params);
                        recievedMediaViewHolder.recieverLayout.setBackgroundResource(R.drawable.bg_reciever_without_bubble);
                    }
                } else {
                    params.setMargins(0, 8, 0, 0);
                    recievedMediaViewHolder.fullbackgroundlayout.setLayoutParams(params);
                    recievedMediaViewHolder.recieverLayout.setBackgroundResource(R.drawable.bg_reciever);
                }
                if (isNightMode) {
                    recievedMediaViewHolder.recievedmessage.setTextColor(((MainActivity) context).getResources().getColor(R.color.white));
                    recievedMediaViewHolder.recievedtime.setTextColor(((MainActivity) context).getResources().getColor(R.color.lightwhite));
                    //recievedViewHolder.rmediarecievedtime.setTextColor(((MainActivity) context).getResources().getColor(R.color.lightwhite));
                } else {
                    recievedMediaViewHolder.recievedmessage.setTextColor(((MainActivity) context).getResources().getColor(R.color.black));
                    recievedMediaViewHolder.recievedtime.setTextColor(((MainActivity) context).getResources().getColor(R.color.lightblack));
                    //recievedViewHolder.rmediarecievedtime.setTextColor(((MainActivity) context).getResources().getColor(R.color.lightblack));
                }

                if (triggeredposition == position) {
                    recievedMediaViewHolder.fullbackgroundlayout.setForeground(context.getResources().getDrawable(R.drawable.bg_selected_message));
                    ObjectAnimator anim = ObjectAnimator.ofInt(recievedMediaViewHolder.fullbackgroundlayout.getForeground(), "alpha", 255, 0);
                    anim.setDuration(1500).setStartDelay(100);
                    anim.start();
                }

                if (indexes.contains(position + "") || triggeredposition == position) {
                    if (!(triggeredposition == position)) {
                        recievedMediaViewHolder.fullbackgroundlayout.setForeground(context.getResources().getDrawable(R.drawable.bg_selected_message));
                    }
                } else {
                    recievedMediaViewHolder.fullbackgroundlayout.setForeground(null);
                }

                if (searchedIndexes.contains(String.valueOf(position))) {
                    String highlightedtext = "";
                    if (!isNightMode) {
                        highlightedtext = "<span style='background-color:yellow'>" + searchedword + "</span>";
                    } else {
                        highlightedtext = "<span style='background-color:#ffffff; color:black'>" + searchedword + "</span>";
                    }
                    String textu = message.replaceAll("(?i)" + Pattern.quote(searchedword), highlightedtext);
                    recievedMediaViewHolder.recievedmessage.setText(Html.fromHtml(textu, 0));
                }

                recievedMediaViewHolder.fullbackgroundlayout.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View view) {

                        if (selectionmode) {
                            if (recievedMediaViewHolder.isselected) {
                                recievedMediaViewHolder.isselected = false;
                                count--;
                                selectedcount.setText(String.valueOf(count));
                                selectedmediaritems.remove(recievedMediaViewHolder);
                                indexes.remove(recievedMediaViewHolder.getAdapterPosition() + "");
                                recievedMediaViewHolder.fullbackgroundlayout.setForeground(null);
                            } else {
                                recievedMediaViewHolder.isselected = true;
                                count++;
                                selectedcount.setText(String.valueOf(count));
                                selectedmediaritems.add(recievedMediaViewHolder);
                                indexes.add(recievedMediaViewHolder.getAdapterPosition() + "");
                                recievedMediaViewHolder.fullbackgroundlayout.setForeground(context.getResources().getDrawable(R.drawable.bg_selected_message));
                            }
                            //copy.callOnClick();
                        }

                        if (count > 1) {
                            editmode = false;
                            editmessagelayout.setVisibility(View.GONE);
                        }

                        if (count == 0) {
                            hideThisViewByAlpha(chatmenulayout);
                            selectionmode = false;
                        } else {
                            if (issearching) {
                                issearching = false;
                                triggeredposition = -1;
                                searchedIndexes.clear();
                                maininputlayout.setVisibility(View.VISIBLE);
                                searchlayout.setVisibility(View.GONE);
                            }
                            revealThisViewByAlpha(chatmenulayout);
                            editmessagelayout.setVisibility(View.GONE);
                        }
                    }
                });

                recievedMediaViewHolder.fullbackgroundlayout.setOnLongClickListener(new View.OnLongClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public boolean onLongClick(View view) {

                        if (!selectionmode) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                v.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE));
                            } else {
                                v.vibrate(30);
                            }
                            selectionmode = true;
                        }
                        if (recievedMediaViewHolder.isselected) {
                            recievedMediaViewHolder.isselected = false;
                            count--;
                            selectedcount.setText(String.valueOf(count));
                            selectedmediaritems.remove(recievedMediaViewHolder);
                            indexes.remove(recievedMediaViewHolder.getAdapterPosition() + "");
                            recievedMediaViewHolder.fullbackgroundlayout.setForeground(null);
                        } else {
                            recievedMediaViewHolder.isselected = true;
                            count++;
                            selectedcount.setText(String.valueOf(count));
                            selectedmediaritems.add(recievedMediaViewHolder);
                            indexes.add(recievedMediaViewHolder.getAdapterPosition() + "");
                            recievedMediaViewHolder.fullbackgroundlayout.setForeground(context.getResources().getDrawable(R.drawable.bg_selected_message));
                        }

                        if (count > 1) {
                            editmode = false;
                            editmessagelayout.setVisibility(View.GONE);
                        }

                        if (count == 0) {
                            hideThisViewByAlpha(chatmenulayout);
                            selectionmode = false;
                        } else {
                            if (issearching) {
                                issearching = false;
                                triggeredposition = -1;
                                searchedIndexes.clear();
                                maininputlayout.setVisibility(View.VISIBLE);
                                searchlayout.setVisibility(View.GONE);
                            }
                            editmessagelayout.setVisibility(View.GONE);
                            revealThisViewByAlpha(chatmenulayout);
                        }
                        return true;
                    }
                });
            }
        } else {

                NoteViewHolder noteViewHolder = (NoteViewHolder) holder;
                noteViewHolder.note.setText(messageList.get(position));
                if (messageList.get(position).contains("Messages and calls are end-to-end encrypted. No one outside of this chat") && position == 1) {
                    noteViewHolder.note.setBackgroundResource(R.drawable.firstnotmessage_bg);
                } else {
                    noteViewHolder.note.setBackgroundResource(R.drawable.date_bg);
                }
            }
        /*} else if(getItemViewType(position) == 2) {
                NoteViewHolder noteViewHolder = (NoteViewHolder) holder;
                noteViewHolder.note.setText(messageList.get(position));
        }*/

    }

    private String getReadableDate(String s) {

        String f = selectedDateFormat;
        String tempS = s;
        if(f!=null) {
            try {
                int monthIndex;
                int yearTemp;
                String day, month, year;
                List<String> dateValues = new ArrayList<>();
                dateValues.add(s.substring(0, s.indexOf("/")));
                s = s.substring(s.indexOf("/") + 1);
                dateValues.add(s.substring(0, s.indexOf("/")));
                dateValues.add(s.substring(s.indexOf("/") + 1));

                List<String> formatValues = new ArrayList<>();
                formatValues.add(f.substring(0, f.indexOf("/")));
                f = f.substring(f.indexOf("/") + 1);
                formatValues.add(f.substring(0, f.indexOf("/")));
                formatValues.add(f.substring(f.indexOf("/") + 1));

                day = dateValues.get(formatValues.indexOf("day"));
                monthIndex = Integer.parseInt((dateValues.get(formatValues.indexOf("month"))).trim());
                month = months[monthIndex - 1];
                yearTemp = Integer.parseInt((dateValues.get(formatValues.indexOf("year"))).trim());
                if (yearTemp < 100)
                    yearTemp += 2000;
                year = yearTemp + "";

                return day + " " + month + " " + year;
            } catch (Exception e) {
                return tempS;
            }
        } else {
            try {
                return getReadableDateOldChats(s);
            } catch (Exception e) {
                return s;
            }
        }
    }

    private String getReadableDateOldChats(String s) {

        int day = Integer.parseInt(s.substring(0,s.indexOf("/")));
        s = s.substring(s.indexOf("/")+1);
        int month = Integer.parseInt(s.substring(0,s.indexOf("/")));
        if(month > 12 && day <= 12) {
            /*useseconddateformat = true;
            changedatestillnow = true;*/
            int temp = month;
            month = day;
            day = temp;
        }/* else if (useseconddateformat && day <= 12) {
            int temp = month;
            month = day;
            day = temp;
        }*/
        s = s.substring(s.indexOf("/")+1);
        int year = Integer.parseInt(s);
        if(year<100) {
            /*if(!changedatestillnow) {
                readabledates.add(day + " " + months[month - 1] + " 20" + year);
            }*/
            return day + " " + months[month - 1] + " 20" + year;
        }
        else {
            /*if(!changedatestillnow) {
                readabledates.add(day + " " + months[month - 1] + " " + year);
            }*/
            return day + " " + months[month - 1] + " " + year;
        }
    }

    private void revealThisViewByAlpha(ConstraintLayout layout) {
        layout.setVisibility(View.VISIBLE);
        layout.animate().alpha(1.0f).setDuration(200).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                layout.animate().setListener(null);
            }
        });
    }

    private void hideThisViewByAlpha(ConstraintLayout layout) {
        layout.animate().alpha(0.0f).setDuration(200).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                layout.animate().setListener(null);
                layout.setVisibility(View.GONE);
            }
        });
    }

    private void addThumb() {
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public void deletefromtable(List<String> messages) {
        MySqllite mySqllite = new MySqllite(context);
        mySqllite.deletemessagefromtable(tablename,messages);
        messages.clear();
    }

    public void updatemessage(String oldmessage,String newmessage) {
        MySqllite mySqllite = new MySqllite(context);
        mySqllite.updatemessage(tablename,oldmessage,newmessage);
    }

    public boolean getNightMode()
    {
        SharedPreferences sharedPreferences = ((MainActivity) context).getSharedPreferences("SP",MODE_PRIVATE);
        return sharedPreferences.getBoolean("darkMode",false);
    }

    public String convertToMMSS(String duration)
    {
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis)%TimeUnit.MINUTES.toSeconds(1));
    }

}

class SentViewHolder extends RecyclerView.ViewHolder {
    ImFlexboxLayout senderLayout;
    ConstraintLayout senderBubbleBg;
    LinearLayout fullbackgroundlayout;
    TextView sentmessage,senttime;
    boolean isselected = false;
    ImageView ticks;

    public SentViewHolder(@NonNull View itemView) {
        super(itemView);
        senderLayout = itemView.findViewById(R.id.sender_bg_layout);
        fullbackgroundlayout = itemView.findViewById(R.id.senderlayoutfull);
        sentmessage = itemView.findViewById(R.id.recievedmessage);
        senttime = itemView.findViewById(R.id.recievedtime);
        ticks = itemView.findViewById(R.id.ticks);
        senderBubbleBg = itemView.findViewById(R.id.senderbubblebg);
    }
}

    /*class SentViewHolder extends RecyclerView.ViewHolder {
        ImFlexboxLayout senderLayout;
        LinearLayout fullbackgroundlayout,svideotimelayout;
        TextView sentmessage,senttime,svideotimetext,audioduration,filename,fileextension;
        boolean isselected = false;
        ImageView mainimage,ticks,playbutton,saudioplaybutton;
        CardView imagecontainer;
        ConstraintLayout senderBubbleBg,senderaudiolayout,documentlayout;
        SeekBar seekBar;

        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            senderLayout = itemView.findViewById(R.id.sender_bg_layout);
            fullbackgroundlayout = itemView.findViewById(R.id.senderlayoutfull);
            svideotimelayout = itemView.findViewById(R.id.sendervideodurationtimelayout);
            senderBubbleBg = itemView.findViewById(R.id.senderbubblebg);
            sentmessage = itemView.findViewById(R.id.recievedmessage);
            senttime = itemView.findViewById(R.id.recievedtime);
            svideotimetext = itemView.findViewById(R.id.svideotimetext);
            audioduration = itemView.findViewById(R.id.saudioduration);
            ticks = itemView.findViewById(R.id.ticks);
            playbutton = itemView.findViewById(R.id.playbuttonimg);
            mainimage = itemView.findViewById(R.id.mainimagesender);
            imagecontainer = itemView.findViewById(R.id.imageView3);
            senderaudiolayout = itemView.findViewById(R.id.senderaudiolayout);
            documentlayout = itemView.findViewById(R.id.document_layout);
            seekBar = itemView.findViewById(R.id.seekBar2);
            saudioplaybutton = itemView.findViewById(R.id.saudioplaybutton);
            filename = itemView.findViewById(R.id.textView19);
            fileextension = itemView.findViewById(R.id.textView21);
        }
    }*/
    class SentMediaViewHolder extends RecyclerView.ViewHolder {
       ImFlexboxLayoutForMedia senderLayout;
       LinearLayout fullbackgroundlayout,svideotimelayout;
       TextView sentmessage,senttime,svideotimetext,audioduration,filename,fileextension;
       boolean isselected = false;
       ImageView mainimage,ticks,playbutton,saudioplaybutton;
       CardView imagecontainer;
       ConstraintLayout senderBubbleBg,senderaudiolayout,documentlayout;
       SeekBar seekBar;

       public SentMediaViewHolder(@NonNull View itemView) {
        super(itemView);
        senderLayout = itemView.findViewById(R.id.sender_media_bg_layout);
        fullbackgroundlayout = itemView.findViewById(R.id.senderlayoutfull);
        svideotimelayout = itemView.findViewById(R.id.sendervideodurationtimelayout);
        senderBubbleBg = itemView.findViewById(R.id.sendermediabubblebg);
        sentmessage = itemView.findViewById(R.id.recievedmessage);
        senttime = itemView.findViewById(R.id.recievedtime);
        svideotimetext = itemView.findViewById(R.id.svideotimetext);
        audioduration = itemView.findViewById(R.id.saudioduration);
        ticks = itemView.findViewById(R.id.ticks);
        playbutton = itemView.findViewById(R.id.playbuttonimg);
        mainimage = itemView.findViewById(R.id.mainimagesender);
        imagecontainer = itemView.findViewById(R.id.imageView3);
        senderaudiolayout = itemView.findViewById(R.id.senderaudiolayout);
        documentlayout = itemView.findViewById(R.id.document_layout);
        seekBar = itemView.findViewById(R.id.seekBar2);
        saudioplaybutton = itemView.findViewById(R.id.saudioplaybutton);
        filename = itemView.findViewById(R.id.textView19);
        fileextension = itemView.findViewById(R.id.textView21);
    }
}

class RecievedViewHolder extends RecyclerView.ViewHolder {
    ConstraintLayout recieverLayout;
    LinearLayout fullbackgroundlayout;
    TextView recievedmessage;
    TextView recievedtime;
    TextView recievername;
    boolean isselected = false;


    public RecievedViewHolder(@NonNull View itemView) {
        super(itemView);
        recieverLayout = itemView.findViewById(R.id.reciever_bg_layout);
        fullbackgroundlayout = itemView.findViewById(R.id.recieverlayoutfull);
        recievername = itemView.findViewById(R.id.textView17);
        recievedmessage = itemView.findViewById(R.id.recievedmessage);
        recievedtime = itemView.findViewById(R.id.recievedtime);
    }
}

    /*class RecievedViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout recieverLayout,recieverAudioLayout,documentLayout;
        LinearLayout fullbackgroundlayout,rvideotimelayout;
        TextView recievedmessage;
        TextView recievedtime,rvideotimetext,rmediarecievedtime,audioDuration,fileName,fileExtension,fileExtension2,fileSize;
        TextView recievername;
        ImageView mainimage,playbutton,raudioplaybutton;
        CardView imagecontainer;
        SeekBar seekBar;
        boolean isselected = false;


        public RecievedViewHolder(@NonNull View itemView) {
            super(itemView);
            recieverLayout = itemView.findViewById(R.id.reciever_bg_layout);
            recieverAudioLayout = itemView.findViewById(R.id.recieveraudiolayout);
            documentLayout = itemView.findViewById(R.id.reciever_document_layout);
            fullbackgroundlayout = itemView.findViewById(R.id.recieverlayoutfull);
            rvideotimelayout = itemView.findViewById(R.id.recievervideodurationtimelayout);
            recievername = itemView.findViewById(R.id.textView17);
            mainimage = itemView.findViewById(R.id.mainimagereciever);
            playbutton = itemView.findViewById(R.id.playbuttonimg2);
            raudioplaybutton = itemView.findViewById(R.id.raudioplaybutton);
            imagecontainer = itemView.findViewById(R.id.imageView19);
            recievedmessage = itemView.findViewById(R.id.recievedmessage);
            recievedtime = itemView.findViewById(R.id.recievedtime);
            rvideotimetext = itemView.findViewById(R.id.rvideotimetext);
            rmediarecievedtime = itemView.findViewById(R.id.imagerecievedtime);
            audioDuration = itemView.findViewById(R.id.textView4);
            seekBar = itemView.findViewById(R.id.seekBar);
            fileExtension = itemView.findViewById(R.id.textView24);
            fileName = itemView.findViewById(R.id.textView19);
            fileExtension2 = itemView.findViewById(R.id.textView21);
            fileSize = itemView.findViewById(R.id.textView22);
        }
    }*/

class RecievedMediaViewHolder extends RecyclerView.ViewHolder {
    ConstraintLayout recieverLayout,recieverAudioLayout,documentLayout;
    LinearLayout fullbackgroundlayout,rvideotimelayout;
    TextView recievedmessage;
    TextView recievedtime,rvideotimetext,rmediarecievedtime,audioDuration,fileName,fileExtension,fileExtension2,fileSize;
    TextView recievername;
    ImageView mainimage,playbutton,raudioplaybutton;
    CardView imagecontainer;
    SeekBar seekBar;
    boolean isselected = false;


    public RecievedMediaViewHolder(@NonNull View itemView) {
        super(itemView);
        recieverLayout = itemView.findViewById(R.id.reciever_bg_layout);
        recieverAudioLayout = itemView.findViewById(R.id.recieveraudiolayout);
        documentLayout = itemView.findViewById(R.id.reciever_document_layout);
        fullbackgroundlayout = itemView.findViewById(R.id.recieverlayoutfull);
        rvideotimelayout = itemView.findViewById(R.id.recievervideodurationtimelayout);
        recievername = itemView.findViewById(R.id.textView17);
        mainimage = itemView.findViewById(R.id.mainimagereciever);
        playbutton = itemView.findViewById(R.id.playbuttonimg2);
        raudioplaybutton = itemView.findViewById(R.id.raudioplaybutton);
        imagecontainer = itemView.findViewById(R.id.imageView19);
        recievedmessage = itemView.findViewById(R.id.recievedmessage);
        recievedtime = itemView.findViewById(R.id.recievedtime);
        rvideotimetext = itemView.findViewById(R.id.rvideotimetext);
        rmediarecievedtime = itemView.findViewById(R.id.imagerecievedtime);
        audioDuration = itemView.findViewById(R.id.textView4);
        seekBar = itemView.findViewById(R.id.seekBar);
        fileExtension = itemView.findViewById(R.id.textView24);
        fileName = itemView.findViewById(R.id.textView19);
        fileExtension2 = itemView.findViewById(R.id.textView21);
        fileSize = itemView.findViewById(R.id.textView22);
    }
}

    class NoteViewHolder extends  RecyclerView.ViewHolder {

        TextView note;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            note = itemView.findViewById(R.id.note);
        }

    }

