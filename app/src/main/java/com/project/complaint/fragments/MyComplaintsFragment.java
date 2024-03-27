package com.project.complaint.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.complaint.PrefHelper;
import com.project.complaint.adapters.MyComplaintAdapter;
import com.project.complaint.databinding.FragmentMyComplaintsBinding;
import com.project.complaint.models.Complaint;

import java.util.ArrayList;
import java.util.List;

public class MyComplaintsFragment extends Fragment {

    private FragmentMyComplaintsBinding binding;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference rootReference;

    DatabaseReference myRef;
    List<Complaint> complaintList;

    MyComplaintAdapter myComplaintAdapter;

    PrefHelper prefHelper;
    String email, userId;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentMyComplaintsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        prefHelper = new PrefHelper(getContext());
        complaintList = new ArrayList<>();
        email = prefHelper.getString(PrefHelper.EMAIL);
        userId = prefHelper.getString(PrefHelper.USERID);
        myComplaintAdapter = new MyComplaintAdapter(getContext(), complaintList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(myComplaintAdapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        rootReference = firebaseDatabase.getReference();


        myRef = rootReference.child("Complaints");

        myRef.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {
                    complaintList.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        Complaint complaint = snapshot.getValue(Complaint.class);

                        if (complaint != null) {
                            if (userId.equals(complaint.userId))
                                complaintList.add(complaint);


                        }


                    }
                    myComplaintAdapter.notifyDataSetChanged();


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}