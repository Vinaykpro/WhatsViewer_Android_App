package com.vinaykpro.whatsviewer;

import android.net.Uri;

public class Contact {
    String name,tablename,sendername,lastseen,lastmessage,lastmessagetime,firstname,lastname;
    int messageindex,usercount,blueticks;
    Uri imageuri = null;

    public Contact(String name, String tablename, String firstname, String lastname, String sendername, int usercount, String lastseen, String lastmessage, String lastmessagetime, Uri imageuri,int messageindex,int blueticks) {
        this.name = name;
        this.tablename = tablename;
        this.lastseen = lastseen;
        this.sendername = sendername;
        this.usercount = usercount;
        this.lastmessage = lastmessage;
        this.lastmessagetime = lastmessagetime;
        this.imageuri = imageuri;
        this.messageindex = messageindex;
        this.firstname = firstname;
        this.lastname = lastname;
        this.blueticks = blueticks;
    }

    public String getName() {
        return name;
    }

    public String getTablename() {
        return tablename;
    }

    public String getFirstname() { return firstname; }

    public String getLastname() { return lastname; }

    public String getSendername() { return sendername; }

    public int getUsercount() { return usercount; }

    public String getLastseen() {
        return lastseen;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public String getLastmessagetime() {
        return lastmessagetime;
    }

    public Uri getImageuri() {
        return imageuri;
    }

    public int getMessageindex() {
        return messageindex;
    }

    public int getBlueticks() { return blueticks; }
}
