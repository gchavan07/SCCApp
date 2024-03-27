package com.project.complaint.activities;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.complaint.MainActivity;
import com.project.complaint.PrefHelper;
import com.project.complaint.databinding.ActivityLoginBinding;
import com.project.complaint.models.User;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    PrefHelper prefHelper;
    private FirebaseAuth registerAuth;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference rootReference;

    DatabaseReference myRef;


    @Override
    protected void onResume() {
        super.onResume();
        prefHelper = new PrefHelper(LoginActivity.this);
        if (prefHelper.getString(PrefHelper.EMAIL).equals("admin@admin.com")) {
            startActivity(new Intent(LoginActivity.this, AdminActivity.class));
            finish();
        } else {
            if (!TextUtils.isEmpty(prefHelper.getString(PrefHelper.EMAIL))) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();

            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
        registerAuth = FirebaseAuth.getInstance();


        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        binding.btnLogin.setOnClickListener(view -> {


            String email = binding.etEmail.getText().toString();
            String password = binding.etPassword.getText().toString();


            if (TextUtils.isEmpty(email)) {
                binding.etEmail.setError("Please enter the email");
                return;
            }

            if (password.length() < 6) {
                binding.etPassword.setError("Please enter minimum 6 characters");
                return;
            }

            if (email.equals("admin@admin.com") && password.equals("admin@123")) {

                prefHelper.saveString(PrefHelper.EMAIL, email);
                startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                finish();
            } else {
                registerAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, task -> {

                    if (task.isSuccessful()) {

                        prefHelper.saveString(PrefHelper.EMAIL, Objects.requireNonNull(registerAuth.getCurrentUser()).getEmail());
                        getUser(email);

                    }
                    if (!task.isSuccessful()) {

                        Toast.makeText(LoginActivity.this, "Failed!! Incorrect email or password", Toast.LENGTH_SHORT).show();


                    }
                });

            }


        });

    }

    void getUser(String email) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        rootReference = firebaseDatabase.getReference();


        myRef = rootReference.child("Users");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {


                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        User user = snapshot.getValue(User.class);

                        if (user != null) {
                            if (user.email.equals(email)) {
                                prefHelper.saveString(PrefHelper.EMAIL, user.email);
                                prefHelper.saveString(PrefHelper.USERID, user.id);
                                prefHelper.saveString(PrefHelper.NAME, user.name);
                             
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }

                        }
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    }


}