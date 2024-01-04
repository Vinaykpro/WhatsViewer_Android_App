package com.vinaykpro.whatsviewer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsFragmentAdapter extends RecyclerView.Adapter {

    List<Contact> messageList;
    Context context;
    public static boolean isopenedfromthisfragment = false;
    public static List<String> names;

    public ChatsFragmentAdapter(List<Contact> messageList, Context context, TextView hint) {
        this.messageList = messageList;
        this.context = context;
        try { if(messageList.size() == 0) {hint.setVisibility(View.VISIBLE);} else {hint.setVisibility(View.GONE);} } catch (Exception e ) {}
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.user_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ChatViewHolder chatViewHolder = (ChatViewHolder) holder;
        chatViewHolder.userChatNumber.setText(messageList.get(position).getName());
        chatViewHolder.userChatLastMessage.setText(messageList.get(position).getLastmessage());
        chatViewHolder.userChatLastTime.setText(messageList.get(position).getLastmessagetime());
        chatViewHolder.tablename = messageList.get(position).getTablename();
        chatViewHolder.firstname = messageList.get(position).getFirstname();
        chatViewHolder.lastname = messageList.get(position).getLastname();
        chatViewHolder.name = messageList.get(position).getName();

        if(messageList.get(position).getBlueticks()==1) {
            chatViewHolder.blueticks.setVisibility(View.VISIBLE);
        } else {
            chatViewHolder.blueticks.setVisibility(View.GONE);
        }

            File f = new File(context.getApplicationContext().getCacheDir()+"/"+chatViewHolder.tablename + "dp.png");
            if (f.exists()) {
                chatViewHolder.profilepic.setImageURI(Uri.fromFile(f));
            } else {
                if(messageList.get(position).getUsercount() > 2) {
                    chatViewHolder.profilepic.setImageResource(R.drawable.groupdefaultdp);
                } else {
                    chatViewHolder.profilepic.setImageResource(R.drawable.userdefaultdp);
                }
            }


        chatViewHolder.userChatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,MainActivity.class);
                intent.putExtra("tablename",chatViewHolder.tablename);
                intent.putExtra("fname",chatViewHolder.firstname);
                intent.putExtra("sname",chatViewHolder.lastname);
                intent.putExtra("zname",chatViewHolder.name);
                HomeActivity.isfilevalid = true;
                // ((HomeActivity) context).loadinglayout.setVisibility(View.VISIBLE);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView userChatNumber;
        TextView userChatLastMessage;
        TextView userChatLastTime;
        ConstraintLayout userChatLayout;
        CircleImageView profilepic;
        String tablename,firstname,lastname,name;
        ImageView blueticks;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            userChatLayout = itemView.findViewById(R.id.user_chat_bg);
            profilepic = itemView.findViewById(R.id.userChatcircleImageView);
            userChatNumber = itemView.findViewById(R.id.userChatNumber);
            userChatLastMessage = itemView.findViewById(R.id.userChatLastMessage);
            userChatLastTime = itemView.findViewById(R.id.userChatLastTime);
            blueticks = itemView.findViewById(R.id.imageView13);
        }
    }
}
