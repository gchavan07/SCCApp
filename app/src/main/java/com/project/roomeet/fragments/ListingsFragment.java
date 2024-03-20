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
import com.project.roomeet.adapters.UserListingAdapter;
import com.project.roomeet.databinding.FragmentListingsBinding;
import com.project.roomeet.models.Listing;

import java.util.ArrayList;
import java.util.List;


public class ListingsFragment extends Fragment {

    private FragmentListingsBinding binding;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference rootReference;

    DatabaseReference myRef;
    List<Listing> listingList;

    UserListingAdapter leaveAdapter;

    PrefHelper prefHelper;
    String email;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentListingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        prefHelper = new PrefHelper(getContext());
        listingList = new ArrayList<>();
        email = prefHelper.getString(PrefHelper.EMAIL);
        leaveAdapter = new UserListingAdapter(getContext(), listingList, email);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
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


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}