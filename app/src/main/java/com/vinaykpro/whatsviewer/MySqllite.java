package com.vinaykpro.whatsviewer;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MySqllite extends SQLiteOpenHelper {

    private static final String DB_NAME = "vkwhatsviewerdb";
    //private static final String TABLE_NAME = "ExTable";
    public MySqllite(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String hometable = "create table "+"hometable"+"(id INTEGER PRIMARY KEY, name text, tablename text, firstname text, secondname text, sendername text, usercount integer, lastseen text,lastmessage text, lastmessagetime text, uri text, messageindex integer, blueticks integer)";
        sqLiteDatabase.execSQL(hometable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ "hometable");
        onCreate(sqLiteDatabase);
    }

    public boolean addText(String name, String tablename, String firstname, String lastname, String sendername, int usercount, String lastseen, String lastmessage, String lastmessagetime, String uri, int messageindex, int blueticks) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("tablename",tablename);
        contentValues.put("firstname",firstname);
        contentValues.put("secondname",lastname);
        contentValues.put("sendername",sendername);
        contentValues.put("usercount",usercount);
        contentValues.put("lastseen",lastseen);
        contentValues.put("lastmessage",lastmessage);
        contentValues.put("lastmessagetime",lastmessagetime);
        contentValues.put("uri",uri);
        contentValues.put("messageindex",messageindex);
        contentValues.put("blueticks",blueticks);
        long res = sqLiteDatabase.insertOrThrow("hometable",null,contentValues);
        return res != -1;
    }

    @SuppressLint("Range")
    public List<Contact> getAllText() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        List<Contact> arrayList = new ArrayList<>();
        String name,tablename,firstname,lastname,sendername,lastseen,lastmessage,lastmessagetime,uri;
        int messageindex,usercount,blueticks;

        Cursor cursor = sqLiteDatabase.rawQuery("select * from "+"hometable",null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            name = cursor.getString(cursor.getColumnIndex("name"));
            tablename = cursor.getString(cursor.getColumnIndex("tablename"));
            firstname = cursor.getString(cursor.getColumnIndex("firstname"));
            lastname = cursor.getString(cursor.getColumnIndex("secondname"));
            sendername = cursor.getString(cursor.getColumnIndex("sendername"));
            lastseen = cursor.getString(cursor.getColumnIndex("lastseen"));
            usercount = Integer.parseInt(cursor.getString(cursor.getColumnIndex("usercount")));
            lastmessage = cursor.getString(cursor.getColumnIndex("lastmessage"));
            lastmessagetime = cursor.getString(cursor.getColumnIndex("lastmessagetime"));
            blueticks = Integer.parseInt(cursor.getString(cursor.getColumnIndex("blueticks")));
            messageindex = Integer.parseInt(cursor.getString(cursor.getColumnIndex("messageindex")));
            messageindex = 0;
            arrayList.add(new Contact(name,tablename,firstname,lastname,sendername,usercount,lastseen,lastmessage,lastmessagetime,null,messageindex,blueticks));
            cursor.moveToNext();
        }
        cursor.close();
        //arrayList.add(new Contact("Test","","","","","","",null,0));
        return arrayList;
    }

    public void addChatToContact(String tablename,List<String> s) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        String table = "create table "+tablename+"(id INTEGER PRIMARY KEY, str text)";
        sqLiteDatabase.execSQL(table);

        //SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        for (String str : s) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("str", str);
            sqLiteDatabase.insert(tablename, null, contentValues);
        }
    }

    public void addDatesToChat(String tablename,List<String> s) {
        tablename = tablename+"dates";
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        String table = "create table "+tablename+"(id INTEGER PRIMARY KEY, str text)";
        sqLiteDatabase.execSQL(table);

        //SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        for (String str : s) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("str", str);
            sqLiteDatabase.insert(tablename, null, contentValues);
        }
    }

    public List<String> getChatData(String tablename) {
        List<String> messageList = new ArrayList<>();
        if(isTableExists(tablename)) {
            SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("select * from " + tablename, null);
            cursor.moveToFirst();
            String text = "";
            while (!cursor.isAfterLast()) {
                text = cursor.getString((cursor.getColumnIndex("str")));
                messageList.add(text);
                cursor.moveToNext();
            }
            cursor.close();
        }
            return messageList;
    }

    public List<String> getChatDates(String tablename) {
        tablename = tablename+"dates";
        List<String> datesList = new ArrayList<>();
        if(isTableExists(tablename)) {
            SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("select * from " + tablename, null);
            cursor.moveToFirst();
            String text = "";
            while (!cursor.isAfterLast()) {
                text = cursor.getString((cursor.getColumnIndex("str")));
                datesList.add(text);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return datesList;
    }

    public boolean isTableExists(String tableName) {
        SQLiteDatabase sqllitedatabse = this.getReadableDatabase();
        String query = "select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'";
        try (Cursor cursor = sqllitedatabse.rawQuery(query, null)) {
            if(cursor!=null) {
                if(cursor.getCount()>0) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean addExtraFieldsToChats(int exportUnlockStatus, String chatid, String extra2, String extra3, String extra4, int extra5, int extra6, int extra7) {
        String tablename = "chatextras";
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        if(!isTableExists(tablename)) {
            String table = "create table "+tablename+"(id INTEGER PRIMARY KEY, exportunlock integer, chatid text, extra2 text, extra3 text, extra4 text, extra5 integer, extra6 integer, extra7 integer)";
            sqLiteDatabase.execSQL(table);
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("exportunlock",exportUnlockStatus);
        contentValues.put("chatid",chatid);
        contentValues.put("extra2",extra2);
        contentValues.put("extra3",extra3);
        contentValues.put("extra4",extra4);
        contentValues.put("extra5",extra5);
        contentValues.put("extra6",extra6);
        contentValues.put("extra7",extra7);
        long res = sqLiteDatabase.insertOrThrow("hometable",null,contentValues);
        return res != -1;
    }

    public void deletemessagefromtable(String tablename,List<String> messages) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        for(String s: messages) {
            sqLiteDatabase.execSQL("DELETE from "+tablename+" where str="+"'"+s+"'");
        }
    }

    public void updatemessage(String tablename,String oldmessage,String newmessage) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        if(newmessage.contains("'"))
            newmessage = newmessage.replaceAll("'","''");
        sqLiteDatabase.execSQL("UPDATE "+tablename+" SET str="+"'"+newmessage+"'"+" WHERE str="+"'"+oldmessage+"'");
    }

    public void updatechatname(String tablename,String name) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        if(name.contains("'"))
         name = name.replaceAll("'","''");
        sqLiteDatabase.execSQL("UPDATE "+"hometable"+" SET name="+"'"+name+"'"+" WHERE tablename="+"'"+tablename+"'");
    }

    public void updatefirstname(String tablename,String name) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("UPDATE "+"hometable"+" SET firstname="+"'"+name+"'"+" WHERE tablename="+"'"+tablename+"'");
    }

    public void updatesecondname(String tablename,String name) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("UPDATE "+"hometable"+" SET secondname="+"'"+name+"'"+" WHERE tablename="+"'"+tablename+"'");
    }

    //name,tablename,firstname,lastname,lastseen,lastmessage,lastmessagetime,null,messageindex)

    public void updatelastseen(String tablename,String text) {
        if(text.contains("'"))
            text = text.replaceAll("'","''");
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("UPDATE "+"hometable"+" SET lastseen="+"'"+text+"'"+" WHERE tablename="+"'"+tablename+"'");
    }

    public void updatelastmessage(String tablename,String text) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("UPDATE "+"hometable"+" SET lastmessage="+"'"+text+"'"+" WHERE tablename="+"'"+tablename+"'");
    }

    public void updatelastmessagetime(String tablename,String text) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("UPDATE "+"hometable"+" SET lastmessagetime="+"'"+text+"'"+" WHERE tablename="+"'"+tablename+"'");
    }

    public void updatemessageindex(String tablename,int index) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("UPDATE "+"hometable"+" SET messageindex="+index+" WHERE tablename="+"'"+tablename+"'");
    }

    public int getusercount(String tablename) {
        int count = 2;
            SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT usercount FROM hometable WHERE tablename = '"+tablename+"'", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                count = Integer.parseInt(cursor.getString((cursor.getColumnIndex("usercount"))));
                cursor.moveToNext();
            }
        cursor.close();
        return count;
    }

    public List<String> getUserNames(String tablename) {
        List<String> names = new ArrayList<>();
        String namestring;
        String[] namesarray;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select secondname from hometable WHERE tablename = '"+tablename+"'", null);
        cursor.moveToFirst();
        namestring = cursor.getString((cursor.getColumnIndex("secondname")));
        namesarray = namestring.split("\\R");
        for(String n:namesarray) {
            names.add(n);
        }
        return  names;
    }

    public void deleteChat(String tablename) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DROP TABLE "+tablename+"");
        sqLiteDatabase.execSQL("DELETE FROM hometable"+" WHERE tablename="+"'"+tablename+"'");
    }

    public void updateProfilePicture(String tablename,String s) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("UPDATE hometable SET uri='"+s+"' WHERE tablename='"+tablename+"'");
    }

    public String getProfilePicture(String tablename) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select uri from hometable WHERE tablename = '"+tablename+"'", null);
        cursor.moveToFirst();
        String s = cursor.getString((cursor.getColumnIndex("uri")));
        cursor.close();
        return s;
    }

    public String getlastseen(String tablename) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select lastseen from hometable WHERE tablename = '"+tablename+"'", null);
        cursor.moveToFirst();
        String s = cursor.getString((cursor.getColumnIndex("lastseen")));
        cursor.close();
        return s;
    }

    public String getname(String tablename) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select name from hometable WHERE tablename = '"+tablename+"'", null);
        cursor.moveToFirst();
        String s = cursor.getString((cursor.getColumnIndex("name")));
        cursor.close();
        return s;
    }

    public void updatelastleftmessageindex(String tablename,int messageindex) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("UPDATE hometable SET messageindex="+messageindex+" WHERE tablename='"+tablename+"'");
    }

    public int getlastleftmessageindex(String tablename) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select messageindex from hometable WHERE tablename = '"+tablename+"'", null);
        cursor.moveToFirst();
        int i = cursor.getInt((cursor.getColumnIndex("messageindex")));
        cursor.close();
        return i;
    }

    public void updateblueticks(String tablename,int status) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("UPDATE hometable SET blueticks="+status+" WHERE tablename='"+tablename+"'");
    }

    public int getblueticks(String tablename) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select blueticks from hometable WHERE tablename = '"+tablename+"'", null);
        cursor.moveToFirst();
        int i = cursor.getInt((cursor.getColumnIndex("blueticks")));
        cursor.close();
        return i;
    }

}
/*
ContentValues contentValues = new ContentValues();
contentValues.put("str",newmessage);
sqLiteDatabase.update(tablename,contentValues,"str = "+"'"+oldmessage+"'",new String[] {"str"});
 */