package com.sharad.teammates.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.sharad.teammates.R;
import com.sharad.teammates.adapters.MatchAdapeter;
import com.sharad.teammates.models.Match;

import java.util.ArrayList;
import java.util.List;

public class TeamMatchesActivity extends AppCompatActivity {

    private RecyclerView mRecyclerViewMatches;
    private RecyclerView.Adapter mRecyclerViewAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Match> resultMatches = new ArrayList<>();
    private String loggedStudentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_matches);

        loggedStudentID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        getStudentMatched();
        mRecyclerViewMatches = findViewById(R.id.recyclerMatch);
        mRecyclerViewAdapter = new MatchAdapeter( TeamMatchesActivity.this, getMatchesData());
        mRecyclerViewMatches.setAdapter(mRecyclerViewAdapter);
        mRecyclerViewMatches.setLayoutManager(new LinearLayoutManager(this));


    }


    //Loop through matches of current User Matches in Database
    private void getStudentMatched() {
        DatabaseReference matchedDB = FirebaseDatabase.getInstance().getReference().child(getString(R.string.dbnode_students))
                .child(loggedStudentID).child("Connections").child("Matched");
        matchedDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        getMatchInformation(ds.getKey());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getMatchInformation(String key) {
        DatabaseReference studentDB = FirebaseDatabase.getInstance().getReference().child(getString(R.string.dbnode_students))
                .child(key);
        studentDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String studentUId = dataSnapshot.getKey();
                    String studentName = "";
                    String profileImage = "";
                    if(dataSnapshot.child("name").getValue() != null){
                        studentName = dataSnapshot.child("name").getValue().toString();
                    }
                    if(dataSnapshot.child("profile_image").getValue() != null){
                        profileImage = dataSnapshot.child("profile_image").getValue().toString();
                    }

                    Match newMatch = new Match(studentUId, studentName, profileImage);
                    resultMatches.add(newMatch);
                    mRecyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private List<Match> getMatchesData() {
        return resultMatches;
    }
}
