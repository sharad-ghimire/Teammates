package com.sharad.teammates.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sharad.teammates.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.os.Build.VERSION_CODES.JELLY_BEAN;
import static android.text.TextUtils.isEmpty;

public class EditProfileActivity extends AppCompatActivity  {

    private static final int REQUEST_CODE = 1234;
    EditText mNameET, mBioET, mSkillsET, mEmailET, mInterestET, mAgeET;
    Button mUpdateButton;
    Button mBackBtn;

    private String loggedInStudentID;

    ProgressBar mProgressBar;

    CircleImageView mProfileImage;

    private FirebaseAuth mAuth;
    private DatabaseReference studentDB;
    private FirebaseStorage storage;
    private StorageReference storageReference;


    private Uri mImageUri;
    private boolean mStoragePermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        mNameET = findViewById(R.id.updateName);
        mEmailET = findViewById(R.id.updateEmail);
        mBioET = findViewById(R.id.updateBio);
        mSkillsET = findViewById(R.id.updateSkills);
        mInterestET = findViewById(R.id.updateInterest);
        mAgeET = findViewById(R.id.updateAge);

        mProgressBar = findViewById(R.id.pBarImage);

        //Image View of Profile Edit
        mProfileImage = findViewById(R.id.profileImage);

        mUpdateButton = findViewById(R.id.updateBtn);
        mBackBtn = findViewById(R.id.backBtn);

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();




        loggedInStudentID = mAuth.getCurrentUser().getUid();
        studentDB = FirebaseDatabase.getInstance().getReference().child(getString(R.string.dbnode_students)).child(loggedInStudentID);

        getUserAccountData();

        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEmailET.getText().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                    //Make sure Email is required while updating
                    if(!isEmpty(mEmailET.getText().toString())){
                        editUserProfile();
                    }
                } else {
                    Toast.makeText(EditProfileActivity.this, "Email field must be filled to update", Toast.LENGTH_SHORT).show();
                }
                saveInformation();
            }
        });

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mProfileImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    private void saveInformation() {
        Map studentInformation = new HashMap();
        studentInformation.put("name", mNameET.getText().toString());
        studentInformation.put("bio", mBioET.getText().toString());
        studentInformation.put("interest", mInterestET.getText().toString());
        studentInformation.put("skills", mSkillsET.getText().toString());
        studentInformation.put("age", mAgeET.getText().toString());

        studentDB.updateChildren(studentInformation);

        if(mImageUri != null){
            StorageReference filePath = FirebaseStorage.getInstance().getReference().child("ProfileImage").child(loggedInStudentID);
            Bitmap bitmap = null;
            try {
                //Convert that Uri into bitmap for compression
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), mImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            showDialog();

            //Compress the Image
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArray);
            //Pass it to byte array
            byte[] imageData = byteArray.toByteArray();


            //Upload Task tries to upload the file
            UploadTask uploadTask = filePath.putBytes(imageData);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                }
            });

            final StorageReference ref  = storageReference.child(loggedInStudentID);
            ref.putFile(mImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        //Upload the file
                        Uri downloadUri = task.getResult();
                        Map studentInformation = new HashMap();
                        studentInformation.put("profile_image", downloadUri.toString());
                        studentDB.updateChildren(studentInformation);
                        hideDialog();
                        finish();
                    } else {
                        Toast.makeText(EditProfileActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            finish();
        }

        Toast.makeText(EditProfileActivity.this, "Details Updated Successfully", Toast.LENGTH_SHORT).show();
        finish();
        return;

    }

    private void getUserAccountData(){
        studentDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>)dataSnapshot.getValue();
                    if(map.get("name") != null){
                        mNameET.setText(map.get("name").toString());
                    }
                    if(map.get("bio") != null){
                        mBioET.setText(map.get("bio").toString());
                    }
                    if(map.get("skills") != null){
                        mSkillsET.setText(map.get("skills").toString());
                    }
                    if(map.get("email") != null){
                        mEmailET.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    }
                    if(map.get("interest") != null){
                        mInterestET.setText(map.get("interest").toString());
                    }
                    if(map.get("age") != null){
                        mAgeET.setText(map.get("age").toString());
                    }
                    //Loading The uploaded image with Glide
                    if(map.get("profile_image") != null){
//                        Glide.with(getApplication()).load(map.get("profile_image").toString()).into(mProfileImage);

                        switch (map.get("profile_image").toString()) {
                            case "https://www.freewebmentor.com/default-avatar.png" :
                                Glide.with(getApplication()).load(getString(R.string.default_image)).into(mProfileImage);
                                break;

                            default:

                                Glide.with(getApplication()).load(map.get("profile_image").toString()).into(mProfileImage);
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void editUserProfile() {
        //Get auth credentials from the user for re-auth

    }

    //Checks to see if permission is granted or not
     public void storagePermission() {
        String[] permissions = new String[0];
        if (android.os.Build.VERSION.SDK_INT >= JELLY_BEAN) {
            permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA };
        }


        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),permissions[1]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),permissions[2]) == PackageManager.PERMISSION_GRANTED) {
            mStoragePermission = true;
        } else {
            ActivityCompat.requestPermissions(EditProfileActivity.this, permissions, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            final Uri selectedUri = data.getData();
            //Uri is simply the location of image within the phone
            mImageUri = selectedUri;
            mProfileImage.setImageURI(mImageUri);
        }
    }

    private void showDialog(){
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideDialog(){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }



}
