package com.project.roomeet.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.roomeet.PrefHelper;
import com.project.roomeet.R;
import com.project.roomeet.adapters.ChatAdapter;
import com.project.roomeet.databinding.ActivityChatBinding;
import com.project.roomeet.models.Chat;
import com.project.roomeet.models.ChatTitle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    String toUserId, toUserName, fromUserId, fromUserName;

    PrefHelper prefHelper;
    private List<Chat> chatList;
    private ChatAdapter adapter;

    String refSender, refReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        prefHelper = new PrefHelper(this);

        toUserId = getIntent().getStringExtra("touserid");
        toUserName = getIntent().getStringExtra("tousername");
        fromUserId = prefHelper.getString(PrefHelper.USERID);
        fromUserName = prefHelper.getString(PrefHelper.NAME);

        refSender = fromUserId + toUserId;
        refReceiver = toUserId + fromUserId;
        chatList = new ArrayList<>();

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        // recyclerView.addItemDecoration(new EmployeeActivity.GridSpacingItemDecoration(4, dpToPx(3), true));
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ChatAdapter(getApplicationContext(), chatList, fromUserId);

        binding.recyclerView.setAdapter(adapter);


        DatabaseReference databaseReferenceUser = FirebaseDatabase.getInstance().getReference("chats");

        databaseReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                int count = 0;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    ChatTitle chatTitle = dataSnapshot.getValue(ChatTitle.class);
                    if ((chatTitle.getFromUserId().equals(fromUserId) && chatTitle.getToUserId().equals(toUserId)) || (chatTitle.getFromUserId().equals(toUserId) && chatTitle.getToUserId().equals(fromUserId))) {
                        count++;
                    } else {


                    }
                }
                if (count == 0) {
                    Log.e("Chat0", "Already Exist0");
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("chats");
                    String chatId = mDatabase.push().getKey();
                    //  SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a", Locale.getDefault());
                    //String currentDateandTime = sdf.format(new Date());
                    ChatTitle chat = new ChatTitle(chatId, fromUserId, fromUserName, toUserId, toUserName);
                    mDatabase.child(Objects.requireNonNull(chatId)).setValue(chat);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(refSender);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try {

                        Chat model = dataSnapshot.getValue(Chat.class);

                        chatList.add(model);
                        binding.recyclerView.scrollToPosition(chatList.size() - 1);
                        adapter.notifyItemInserted(chatList.size() - 1);


                    } catch (Exception ex) {
                        Log.e("ChatActivity", ex.getMessage());
                    }
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(Objects.requireNonNull(binding.etMessage.getText()).toString())) {

                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(refSender);
                    String messageId = mDatabase.push().getKey();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a", Locale.getDefault());
                    String currentDateandTime = sdf.format(new Date());
                    Chat chat = new Chat(messageId, binding.etMessage.getText().toString(), toUserId, toUserName, fromUserId, fromUserName, currentDateandTime, 0);
                    mDatabase.child(Objects.requireNonNull(messageId)).setValue(chat);


                    DatabaseReference mDatabaseReceiver = FirebaseDatabase.getInstance().getReference(refReceiver);
                    String messageIdReceiver = mDatabaseReceiver.push().getKey();

                    Chat chatMsg = new Chat(messageId, binding.etMessage.getText().toString(), toUserId, toUserName, fromUserId, fromUserName, currentDateandTime, 0);
                    mDatabaseReceiver.child(Objects.requireNonNull(messageIdReceiver)).setValue(chatMsg);


                }


                //   sendNotification(notification);
                binding.etMessage.setText("");
            }
        });


    }


}