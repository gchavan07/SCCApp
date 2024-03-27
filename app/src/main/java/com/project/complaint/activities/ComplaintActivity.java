package com.project.complaint.activities;


import android.os.Bundle;

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
import com.project.complaint.adapters.ComplaintAdapter;
import com.project.complaint.databinding.ActivityComplaintsBinding;
import com.project.complaint.models.Complaint;

import java.util.ArrayList;
import java.util.List;

public class ComplaintActivity extends AppCompatActivity {

    private ActivityComplaintsBinding binding;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference rootReference;

    DatabaseReference myRef;
    List<Complaint> complaintList;

    ComplaintAdapter complaintAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityComplaintsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        complaintList = new ArrayList<>();

        complaintAdapter = new ComplaintAdapter(this, complaintList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(complaintAdapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        rootReference = firebaseDatabase.getReference();


        myRef = rootReference.child("Complaints");

        myRef.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {
                    complaintList.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        Complaint listing = snapshot.getValue(Complaint.class);

                        if (listing != null) {

                            complaintList.add(listing);


                        }


                    }
                    complaintAdapter.notifyDataSetChanged();


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    }


}