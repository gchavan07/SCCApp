package com.project.roomeet.activities;


import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.roomeet.databinding.ActivityRegisterBinding;
import com.project.roomeet.models.User;

import java.util.Objects;


public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    private FirebaseAuth registerAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
       /* ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
        registerAuth = FirebaseAuth.getInstance();
        binding.btnRegister.setOnClickListener(view -> {

            String name = binding.etName.getText().toString();
            String email = binding.etEmail.getText().toString();
            String mobile = binding.etMobile.getText().toString();
            String password = binding.etPassword.getText().toString();


            if (TextUtils.isEmpty(name)) {
                binding.etName.setError("Please enter the name");
                return;
            }

            if (TextUtils.isEmpty(email)) {
                binding.etEmail.setError("Please enter the email");
                return;
            }

            if (mobile.length() != 10) {
                binding.etMobile.setError("Please enter the mobile");
                return;
            }

            if (password.length() < 6) {
                binding.etPassword.setError("Please enter minimum 6 characters");
                return;
            }


            registerAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, task -> {

                if (task.isSuccessful()) {
                    DatabaseReference myRef;
                    FirebaseDatabase firebaseDatabase;
                    DatabaseReference rootReference;
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    rootReference = firebaseDatabase.getReference();

                    myRef = rootReference.child("Users");

                    User user = new User();
                    user.setName(name);
                    user.setEmail(email);
                    user.setMobile(mobile);
                    String id = myRef.push().getKey();
                    user.setId(id);
                    myRef.child(Objects.requireNonNull(id)).setValue(user);

                    Toast.makeText(RegisterActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                    finish();


                }
                if (!task.isSuccessful()) {

                    Toast.makeText(RegisterActivity.this, "Failed!! Email id is already used", Toast.LENGTH_SHORT).show();

                }
            });


        });


    }
}