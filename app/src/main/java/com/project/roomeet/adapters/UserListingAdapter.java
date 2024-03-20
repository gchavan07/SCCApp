package com.project.roomeet.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.roomeet.R;
import com.project.roomeet.activities.AddListingActivity;
import com.project.roomeet.models.Listing;
import com.project.roomeet.models.SaveListings;

import java.util.List;
import java.util.Objects;

public class UserListingAdapter extends RecyclerView.Adapter<UserListingAdapter.ViewHolder> {

    private Listing listing;
    private Context context;
    private List<Listing> listingList;

    String email;


    public class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView tvTitle, tvBed, tvRent, tvAddress, tvLandmark, tvName, tvPhone;
        AppCompatImageView ivImage;

        AppCompatButton btnSave;

        ViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvBed = view.findViewById(R.id.tvBed);
            tvRent = view.findViewById(R.id.tvRent);
            tvAddress = view.findViewById(R.id.tvAddress);
            tvLandmark = view.findViewById(R.id.tvLandmark);
            tvName = view.findViewById(R.id.tvName);
            tvPhone = view.findViewById(R.id.tvPhone);
            ivImage = view.findViewById(R.id.ivImage);
            btnSave = view.findViewById(R.id.btnSave);

            btnSave.setOnClickListener(view1 -> {


                DatabaseReference myRef;
                FirebaseDatabase firebaseDatabase;
                DatabaseReference rootReference;
                firebaseDatabase = FirebaseDatabase.getInstance();
                rootReference = firebaseDatabase.getReference();

                myRef = rootReference.child("SaveListings");

                SaveListings listing = new SaveListings();
                listing.setTitle(listingList.get(getAdapterPosition()).title);
                listing.setBeds(listingList.get(getAdapterPosition()).beds);
                listing.setName(listingList.get(getAdapterPosition()).name);
                listing.setPhone(listingList.get(getAdapterPosition()).phone);
                listing.setAddress(listingList.get(getAdapterPosition()).address);
                listing.setLandmark(listingList.get(getAdapterPosition()).landmark);
                listing.setRent(listingList.get(getAdapterPosition()).rent);
                listing.setImage(listingList.get(getAdapterPosition()).image);
                listing.setEmail(email);
                String id = myRef.push().getKey();
                listing.setId(id);
                myRef.child(Objects.requireNonNull(id)).setValue(listing);

                Toast.makeText(context, "Successfully Saved", Toast.LENGTH_SHORT).show();


            });


        }
    }

    public UserListingAdapter(Context mContext, List<Listing> listingList, String email) {
        this.context = mContext;
        this.listingList = listingList;
        this.email = email;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_listiings, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        listing = listingList.get(position);

        holder.tvTitle.setText(listing.title);
        holder.tvBed.setText("No of Beds: " + listing.beds);
        holder.tvRent.setText("Rent Rs.: " + listing.rent);
        holder.tvAddress.setText("Address: " + listing.address);
        holder.tvLandmark.setText("Landmark: " + listing.landmark);
        holder.tvName.setText("Contact Person: " + listing.name);
        holder.tvPhone.setText("Contact: " + listing.phone);

        Glide.with(context).load(listing.image).into(holder.ivImage);


    }

    @Override
    public int getItemCount() {
        return listingList.size();
    }


}