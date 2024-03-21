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
import com.project.roomeet.adapters.RoomMateAdapter;
import com.project.roomeet.adapters.UserAdapter;
import com.project.roomeet.adapters.UserListingAdapter;
import com.project.roomeet.databinding.FragmentListingsBinding;
import com.project.roomeet.databinding.FragmentUsersBinding;
import com.project.roomeet.models.Listing;
import com.project.roomeet.models.User;

import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment {

    private FragmentUsersBinding binding;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference rootReference;

    DatabaseReference myRef;
    List<User> userList;

    RoomMateAdapter userAdapter;

    String email;
    PrefHelper prefHelper;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentUsersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        prefHelper = new PrefHelper(getContext());
        email = prefHelper.getString(PrefHelper.EMAIL);
        userList = new ArrayList<>();

        userAdapter = new RoomMateAdapter(getContext(), userList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(userAdapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        rootReference = firebaseDatabase.getReference();


        myRef = rootReference.child("Users");

        myRef.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {
                    userList.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        User user = snapshot.getValue(User.class);

                        if (user != null) {
                            if (!email.equals(user.email)) {
                                userList.add(user);
                            } else {
                                prefHelper.saveString(PrefHelper.USERID, user.id);
                                prefHelper.saveString(PrefHelper.NAME, user.name);
                            }
                        }
                    }
                    userAdapter.notifyDataSetChanged();
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