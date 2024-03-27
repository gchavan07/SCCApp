package com.project.complaint.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.project.complaint.R;
import com.project.complaint.models.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private User user;
    private Context context;
    private List<User> userList;


    public class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView tvEmail, tvName, tvPhone;
        AppCompatImageView ivImage;

        ViewHolder(View view) {
            super(view);

            tvName = view.findViewById(R.id.tvName);
            tvPhone = view.findViewById(R.id.tvPhone);
            tvEmail = view.findViewById(R.id.tvEmail);


        }
    }

    public UserAdapter(Context mContext, List<User> userList) {
        this.context = mContext;
        this.userList = userList;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
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