package com.agkminds.dude.Activities;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.agkminds.dude.Models.User;
import com.agkminds.dude.databinding.ActivityProfileSetupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class ProfileSetupActivity extends AppCompatActivity {
    ActivityProfileSetupBinding binding;
    ActivityResultLauncher<String> getImage;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseStorage storage =  FirebaseStorage.getInstance();
    Uri selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProfileSetupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide();

        /* Select Profile Picture Method */
        getImage=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                binding.profileImage.setImageURI(result);
                selectedImage=result;
            }
        });

        String name= getIntent().getStringExtra("name");
        binding.nameBox.setText(name);

        binding.setupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName= binding.nameBox.getText().toString();

                if (userName.isEmpty()){
                    binding.nameBox.setError("Please type your name first.");
                }

                if (selectedImage != null){
                    StorageReference reference=storage.getReference().child("Profiles").child(Objects.requireNonNull(mAuth.getUid()));

                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()){
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();
                                        String uId = mAuth.getUid();

                                        String randomKey = database.getReference().push().getKey();
                                        User user=new User(uId, userName, imageUrl);

                                        database.getReference()
                                                .child("Users")
                                                .child(randomKey)
                                                .setValue(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(ProfileSetupActivity.this, "Profile Created Successfully.", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(ProfileSetupActivity.this, HomeActivity.class));
                                                        finish();
                                                    }
                                                });
                                    }
                                });
                            }
                        }
                    });
                } else {
                    String uId = mAuth.getUid();

                    User user=new User(uId, userName, "No Image");

                    database.getReference()
                            .child("Users")
                            .push()
                            .setValue(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    startActivity(new Intent(ProfileSetupActivity.this, HomeActivity.class));
                                    finish();
                                }
                            });
                }

            }
        });

        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImage.launch("image/*");
            }
        });

    }
}