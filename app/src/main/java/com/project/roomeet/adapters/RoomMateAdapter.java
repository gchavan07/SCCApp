package com.project.roomeet.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.project.roomeet.R;
import com.project.roomeet.activities.ChatActivity;
import com.project.roomeet.models.User;

import java.util.List;

public class RoomMateAdapter extends RecyclerView.Adapter<RoomMateAdapter.ViewHolder> {

    private User user;
    private Context context;
    private List<User> userList;


    public class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView tvEmail, tvName, tvPhone;
        AppCompatButton btnChat;

        ViewHolder(View view) {
            super(view);

            tvName = view.findViewById(R.id.tvName);
            tvPhone = view.findViewById(R.id.tvPhone);
            tvEmail = view.findViewById(R.id.tvEmail);
            btnChat = view.findViewById(R.id.btnChat);
            btnChat.setOnClickListener(view1 -> {
                Intent i = new Intent(context, ChatActivity.class);
                i.putExtra("touserid", userList.get(getAdapterPosition()).id);
                i.putExtra("tousername", userList.get(getAdapterPosition()).name);
                context.startActivity(i);
            });


        }
    }

    public RoomMateAdapter(Context mContext, List<User> userList) {
        this.context = mContext;
        this.userList = userList;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room_mate, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        user = userList.get(position);

        holder.tvEmail.setText("Email: " + user.email);
        holder.tvName.setText("Name: " + user.name);
        holder.tvPhone.setText("Contact: " + user.mobile);


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


}