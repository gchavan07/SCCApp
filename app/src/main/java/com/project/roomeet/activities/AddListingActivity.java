package com.project.roomeet.activities;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.roomeet.databinding.ActivityAddListingBinding;
import com.project.roomeet.models.Listing;

import java.util.Objects;
import java.util.UUID;

public class AddListingActivity extends AppCompatActivity {

    private ActivityAddListingBinding binding;
    int SELECT_PICTURE = 200;
    String imageUrl;

    FirebaseStorage storage;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAddListingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        binding.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);

            }
        });

        binding.btnSave.setOnClickListener(view -> {

            String title = binding.etRoomType.getText().toString();
            String beds = binding.etBed.getText().toString();
            String name = binding.etName.getText().toString();
            String landMark = binding.etLandmark.getText().toString();
            String mobile = binding.etPhone.getText().toString();
            String address = binding.etAddress.getText().toString();
            String rent = binding.etRent.getText().toString();


            if (TextUtils.isEmpty(title)) {
                binding.etRoomType.setError("Please enter the title");
                return;
            }

            if (TextUtils.isEmpty(beds)) {
                binding.etBed.setError("Please enter the number of beds");
                return;
            }

            if (TextUtils.isEmpty(name)) {
                binding.etName.setError("Please enter the contact name");
                return;
            }

            if (mobile.length() != 10) {
                binding.etPhone.setError("Please enter the mobile");
                return;
            }
            if (TextUtils.isEmpty(address)) {
                binding.etAddress.setError("Please enter the address");
                return;
            }

            if (TextUtils.isEmpty(landMark)) {
                binding.etLandmark.setError("Please enter the landmark");
                return;
            }

            if (TextUtils.isEmpty(rent)) {
                binding.etRent.setError("Please enter the rent");
                return;
            }
            if (TextUtils.isEmpty(imageUrl)) {
                Toast.makeText(AddListingActivity.this, "Please upload the image", Toast.LENGTH_SHORT).show();
                return;
            }
            DatabaseReference myRef;
            FirebaseDatabase firebaseDatabase;
            DatabaseReference rootReference;
            firebaseDatabase = FirebaseDatabase.getInstance();
            rootReference = firebaseDatabase.getReference();

            myRef = rootReference.child("Listings");

            Listing listing = new Listing();
            listing.setTitle(title);
            listing.setBeds(beds);
            listing.setName(name);
            listing.setPhone(mobile);
            listing.setAddress(address);
            listing.setLandmark(landMark);
            listing.setRent(rent);
            listing.setImage(imageUrl);
            String id = myRef.push().getKey();
            listing.setId(id);
            myRef.child(Objects.requireNonNull(id)).setValue(listing);

            Toast.makeText(AddListingActivity.this, "Successfully Saved", Toast.LENGTH_SHORT).show();
            finish();

        });


    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_PICTURE) {

                Uri selectedImageUri = data.getData();

                binding.ivImage.setImageURI(selectedImageUri);
                uploadImage(selectedImageUri);


            }
        }

    }

    private void uploadImage(Uri filePath) {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();


            // Defining the child of storageReference
            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    imageUrl = task.getResult().toString();
                                }
                            });
                            // Image uploaded successfully
                            // Dismiss dialog
                            progressDialog.dismiss();
                            Toast.makeText(AddListingActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(AddListingActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                        // Progress Listener for loading
                        // percentage on the dialog box
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }
}
