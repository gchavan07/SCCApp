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
import com.project.roomeet.models.Listing;
import com.project.roomeet.models.SaveListings;

import java.util.List;

public class SaveListingAdapter extends RecyclerView.Adapter<SaveListingAdapter.ViewHolder> {

    private SaveListings listing;
    private Context context;
    private List<SaveListings> listingList;


    public class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView tvTitle, tvBed, tvRent, tvAddress, tvLandmark, tvName, tvPhone;
        AppCompatImageView ivImage;

        AppCompatButton btnDelete;

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
            btnDelete = view.findViewById(R.id.btnDelete);

            btnDelete.setOnClickListener(view1 -> {

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query applesQuery = ref.child("SaveListings").orderByChild("id").equalTo(listingList.get(getAdapterPosition()).id);

                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                            listingList.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });
            });


        }
    }

    public SaveListingAdapter(Context mContext, List<SaveListings> listingList) {
        this.context = mContext;
        this.listingList = listingList;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listing, parent, false);
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