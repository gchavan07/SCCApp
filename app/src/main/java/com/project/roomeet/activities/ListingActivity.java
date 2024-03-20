package com.project.roomeet.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.roomeet.adapters.ListingAdapter;
import com.project.roomeet.databinding.ActivityListingsBinding;
import com.project.roomeet.models.Listing;

import java.util.ArrayList;
import java.util.List;

public class ListingActivity extends AppCompatActivity {

    private ActivityListingsBinding binding;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference rootReference;

    DatabaseReference myRef;
    List<Listing> listingList;

    ListingAdapter leaveAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityListingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ListingActivity.this, AddListingActivity.class));

            }
        });

        listingList = new ArrayList<>();

        leaveAdapter = new ListingAdapter(this, listingList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ListingActivity.this);
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(leaveAdapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        rootReference = firebaseDatabase.getReference();


        myRef = rootReference.child("Listings");

        myRef.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {
                    listingList.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        Listing listing = snapshot.getValue(Listing.class);

                        if (listing != null) {

                            listingList.add(listing);


                        }


                    }
                    leaveAdapter.notifyDataSetChanged();


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    }


}