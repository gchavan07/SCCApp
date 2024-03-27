package com.project.complaint.adapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.complaint.R;
import com.project.complaint.models.Complaint;

import java.util.List;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ViewHolder> {

    private Complaint complaint;
    private Context context;
    private List<Complaint> complaintList;

    String email;


    public class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView tvTitle, tvDesc, tvAddress, tvLandmark, tvName;
        AppCompatImageView ivImage, ivLocation;

        AppCompatButton btnSave;

        ViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvDesc = view.findViewById(R.id.tvDesc);
            tvName = view.findViewById(R.id.tvName);
            tvAddress = view.findViewById(R.id.tvAddress);
            tvLandmark = view.findViewById(R.id.tvLandmark);

            ivImage = view.findViewById(R.id.ivImage);
            btnSave = view.findViewById(R.id.btnSave);
            ivLocation = view.findViewById(R.id.ivLocation);


            ivLocation.setOnClickListener(view12 -> {
                String strUri = "http://maps.google.com/maps?q=loc:" + complaintList.get(getAdapterPosition()).location;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(strUri));

                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

                context.startActivity(intent);
            });

        }
    }

    public ComplaintAdapter(Context mContext, List<Complaint> complaintList) {
        this.context = mContext;
        this.complaintList = complaintList;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_complaint, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        complaint = complaintList.get(position);

        holder.tvTitle.setText(complaint.title);
        holder.tvName.setText("Complaint By: " + complaint.name);
        holder.tvAddress.setText("Address: " + complaint.address);
        holder.tvLandmark.setText("Landmark: " + complaint.landmark);
        holder.tvDesc.setText(complaint.desc);

        Glide.with(context).load(complaint.image).into(holder.ivImage);


    }

    @Override
    public int getItemCount() {
        return complaintList.size();
    }


}