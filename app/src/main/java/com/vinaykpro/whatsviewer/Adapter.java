package com.vinaykpro.whatsviewer;

import static android.content.Context.MODE_PRIVATE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Telephony;
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
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class Adapter extends RecyclerView.Adapter {

    List<String> messageList;
    List<String> colorList;
    String userName1;
    String userName2;
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

    public Adapter(List<String> messageList,String tablename,String fname,String sname,Context context,ConstraintLayout chatmenulayout, ImageView backbtn, ImageView edit, ImageView copy, ImageView delete, ImageView info,TextView selectedcount,ConstraintLayout editmessagelayout,ImageView editmsgbackbtn,EditText editmsgedittext,TextView editmsgupdate,ConstraintLayout searchlayout,ImageView searchlayoutbackbtn,EditText searchlayoutedittext,ImageView searchlayoutupbutton,ImageView searchlayoutdownbutton,LinearLayoutManager linearLayoutManager) {
        this.messageList = messageList;
        this.userName1 = fname;
        this.userName2= sname;
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
                int nextInt = random.nextInt(0xffffff + 1);
                String colorCode = String.format("#%06x",nextInt);
                colorList.add(colorCode);
            }
            //membersradiogroup.add
            //Toast.makeText(context, "count : "+database.getusercount(tablename)+" listsize : "+groupmemebernames.get(0), Toast.LENGTH_SHORT).show();
        }

        isNightMode = getNightMode();

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
                    case R.id.gotofirstmessage:
                        linearLayoutManager.scrollToPosition(0);
                        //Toast.makeText(context, "Go to first message", Toast.LENGTH_SHORT).show();
                        return true;
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
                    int x = (int)Math.floor(Math.random()*100);
                    if(x<=75) {
                        ((MainActivity) context).showInterstitialAd();
                    }
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
                    int x = (int)Math.floor(Math.random()*100);
                    if(x<=75) {
                        ((MainActivity) context).showInterstitialAd();
                    }
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
            noteViewHolder.note.setText(messageList.get(position));
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

