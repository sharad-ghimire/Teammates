package com.sharad.teammates.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sharad.teammates.R;
import com.sharad.teammates.models.Student;

import java.util.Map;
import java.util.Objects;

public class AddUniversityDetails extends AppCompatActivity {

    private Button mAddUniDetainsBtn;
    private EditText mUniversityET, mMajorET, mSubjectET;

    private static final int REQ_CODE = 1234 ;
    private static final String UNIVERSITY = "UNIVERSITY" ;
    private static final String SUBJECT = "SUBJECT" ;
    private static final String MAJOR = "MAJOR" ;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_university_details);

        //Initialize
        mAddUniDetainsBtn = findViewById(R.id.addUniDetailsBtn);
        mUniversityET = findViewById(R.id.universityName);
        mMajorET = findViewById(R.id.majorName);
        mSubjectET = findViewById(R.id.subjectName);

        setupFirebaseAuth();
        getUserAccountData();

        mAddUniDetainsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                //Update University of the User
                if(!mUniversityET.getText().toString().equals("")){
                    reference.child(getString(R.string.dbnode_students))
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("uni_id").setValue(mUniversityET.getText().toString());
                }
                //Update University of the User
                if(!mMajorET.getText().toString().equals("")){
                    reference.child(getString(R.string.dbnode_students))
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("major_id").setValue(mMajorET.getText().toString());
                }
                if(!mSubjectET.getText().toString().equals("")){
                    reference.child(getString(R.string.dbnode_students))
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("subjects").setValue(mSubjectET.getText().toString());
                }
                startHomeActivity();
            }
        });

    }

    private void startHomeActivity() {
        Intent intent = new Intent(AddUniversityDetails.this, MainPageAfterLoginActivity.class);
        intent.putExtra(UNIVERSITY,"" + mUniversityET.getText().toString());
        intent.putExtra(MAJOR,"" + mMajorET.getText().toString());
        intent.putExtra(SUBJECT,"" + mSubjectET.getText().toString());
        startActivityForResult(intent, REQ_CODE);
    }

    private void setupFirebaseAuth(){
        mAuth = FirebaseAuth.getInstance();
    }

    private void getUserAccountData(){
        final FirebaseUser loggedUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(getString(R.string.dbnode_students));

        //Querying the database
        Query query = reference.orderByKey().equalTo(loggedUser.getUid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //get children method will return all of the children of the snapshot
                //But in this case it should only return a single child
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Student student = ds.getValue(Student.class);

                    //Set the values
                    assert student != null;
                    mUniversityET.setText(student.getUni_id());
                    mMajorET.setText(student.getMajor_id());
                    mSubjectET.setText(student.getSubjects());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

}
