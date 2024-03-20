package com.project.roomeet.fragments;

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
import com.project.roomeet.PrefHelper;
import com.project.roomeet.adapters.SaveListingAdapter;
import com.project.roomeet.adapters.UserListingAdapter;
import com.project.roomeet.databinding.FragmentSavedListingsBinding;
import com.project.roomeet.models.Listing;
import com.project.roomeet.models.SaveListings;

import java.util.ArrayList;
import java.util.List;


public class SavedListingFragment extends Fragment {

    private FragmentSavedListingsBinding binding;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference rootReference;

    DatabaseReference myRef;
    List<SaveListings> listingList;

    SaveListingAdapter userListingAdapter;

    PrefHelper prefHelper;
    String email;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentSavedListingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        prefHelper = new PrefHelper(getContext());
        listingList = new ArrayList<>();
        email = prefHelper.getString(PrefHelper.EMAIL);
        userListingAdapter = new SaveListingAdapter(getContext(), listingList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(userListingAdapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        rootReference = firebaseDatabase.getReference();


        myRef = rootReference.child("SaveListings");

        myRef.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {
                    listingList.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        SaveListings listing = snapshot.getValue(SaveListings.class);

                        if (listing != null) {
                            if (listing.email.equals(email))

                                listingList.add(listing);


                        }


                    }
                    userListingAdapter.notifyDataSetChanged();


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