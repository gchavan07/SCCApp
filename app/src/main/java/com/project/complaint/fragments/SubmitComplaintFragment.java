package com.project.complaint.fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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
import com.project.complaint.GPSTracker;
import com.project.complaint.MapsActivity;
import com.project.complaint.PrefHelper;
import com.project.complaint.databinding.FragmentSubmitComplaintBinding;
import com.project.complaint.models.Complaint;

import java.util.Objects;
import java.util.UUID;

public class SubmitComplaintFragment extends Fragment {

    private FragmentSubmitComplaintBinding binding;

    int SELECT_PICTURE = 200;
    int MY_PERMISSIONS_REQUEST_LOCATION = 100;
    String imageUrl;

    FirebaseStorage storage;
    StorageReference storageReference;

    String latitude, longitude;

    String name, userId;

    PrefHelper prefHelper;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentSubmitComplaintBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        prefHelper = new PrefHelper(getContext());
        name = prefHelper.getString(PrefHelper.NAME);
        userId = prefHelper.getString(PrefHelper.USERID);

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

        binding.etLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), MapsActivity.class);
                startActivityForResult(i, 2);
            }
        });
        binding.btnSave.setOnClickListener(view -> {

            String title = binding.etTitle.getText().toString();
            String address = binding.etAddress.getText().toString();
            String landMark = binding.etLandmark.getText().toString();
            String location = binding.etLocation.getText().toString();
            String desc = binding.etDesc.getText().toString();


            if (TextUtils.isEmpty(title)) {
                binding.etTitle.setError("Please enter the title");
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

            if (TextUtils.isEmpty(desc)) {
                binding.etDesc.setError("Please enter the description");
                return;
            }


            if (TextUtils.isEmpty(imageUrl)) {
                Toast.makeText(getContext(), "Please upload the image", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(location)) {
                Toast.makeText(getContext(), "Please select the location", Toast.LENGTH_SHORT).show();
                return;
            }
            DatabaseReference myRef;
            FirebaseDatabase firebaseDatabase;
            DatabaseReference rootReference;
            firebaseDatabase = FirebaseDatabase.getInstance();
            rootReference = firebaseDatabase.getReference();

            myRef = rootReference.child("Complaints");

                   Complaint complaint = new Complaint();

            String id = myRef.push().getKey();
            complaint.setId(id);
            complaint.setName(name);
            complaint.setUserId(userId);
            complaint.setImage(imageUrl);
            complaint.setTitle(title);
            complaint.setAddress(address);
            complaint.setLandmark(landMark);
            complaint.setLocation(location);
            complaint.setDesc(desc);
            myRef.child(Objects.requireNonNull(id)).setValue(complaint);

            Toast.makeText(getContext(), "Successfully Saved", Toast.LENGTH_SHORT).show();

        });


        return root;

    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == SELECT_PICTURE) {

            Uri selectedImageUri = data.getData();

            binding.ivImage.setImageURI(selectedImageUri);
            uploadImage(selectedImageUri);


        } else if (requestCode == 2) {

            latitude = data.getStringExtra("latitude");
            longitude = data.getStringExtra("longitude");
            binding.etLocation.setText(latitude + "," + longitude);

        }


    }

    private void uploadImage(Uri filePath) {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(getContext());
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
                            Toast.makeText(getContext(), "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (checkLocationPermission()) {

            getActivity().startService(new Intent(getContext(), GPSTracker.class));
        }
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

}
