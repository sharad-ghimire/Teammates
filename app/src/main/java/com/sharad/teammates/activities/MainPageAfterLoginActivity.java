package com.sharad.teammates.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sharad.teammates.R;
import com.sharad.teammates.adapters.CardProfileAdapter;
import com.sharad.teammates.models.Student;

import java.util.ArrayList;
import java.util.List;

public class MainPageAfterLoginActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String UNIVERSITY = "UNIVERSITY" ;
    private static final String SUBJECT = "SUBJECT" ;
    private static final String MAJOR = "MAJOR" ;

    private DatabaseReference studentsDB;

    String universityName, majorName, subjectName;

//    Student loggedStudent = new Student();
    List<Student> listOfStudent = new ArrayList<>();


    private RecyclerView mCardRecyclerView;
    private CardProfileAdapter mCardRecyclerViewAdapter;

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();


    private TextView loggedUserSubjectName;
    private FirebaseAuth mAuth;


    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page_after_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        imageView = findViewById(R.id.imageViewForNavigation);


        mAuth = FirebaseAuth.getInstance();
        studentsDB = FirebaseDatabase.getInstance().getReference().child(getString(R.string.dbnode_students));
//        loggedUserSubjectName = findViewById(R.id.thisSubject);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        getValueFromUniversityIntent();
        //Init images and data from database and show it in recycler view
        initImages();
        //Init database and checks for potential matches
        initDatabase();
    }

    /**
     *  Get the passed values from the intent
     */
    private void getValueFromUniversityIntent() {
        Intent anotherIntent = getIntent();
        universityName = anotherIntent.getStringExtra(UNIVERSITY);
        majorName = anotherIntent.getStringExtra(MAJOR);
        subjectName = anotherIntent.getStringExtra(SUBJECT);
    }

    private void initDatabase() {

        final FirebaseUser loggedUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        //Querying the database
        Query query = reference.child(getString(R.string.dbnode_students)).orderByChild("subjects").equalTo(subjectName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Student matchedStudent = ds.getValue(Student.class);
                        listOfStudent.add(matchedStudent);
                        mNames.add(ds.child("name").getValue().toString());
                        String profileImage = getString(R.string.default_image);
                        if(!ds.child("profile_image").getValue().equals(getString(R.string.default_image))){
                            profileImage = ds.child("profile_image").getValue().toString();
//                            Glide.with(getApplication()).load(ds.child("profile_image").getValue().toString()).into(imageView);
                        } else {
//                            Glide.with(getApplication()).asBitmap().load(getString(R.string.default_image)).into(imageView);
                        }

                        mImages.add(profileImage);
//                        imageView.setImageURI(Uri.parse(profileImage));
                    }
                    mCardRecyclerViewAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void initImages() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        mCardRecyclerView = findViewById(R.id.cardRecyclerView);
        mCardRecyclerViewAdapter = new CardProfileAdapter( this, listOfStudent);
        mCardRecyclerView.setAdapter(mCardRecyclerViewAdapter);
        mCardRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_page_after_login, menu);
        return true;
    }


    //Side Navigation Item handler
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile) {
            // Handle the profile selected
            startActivity(new Intent(MainPageAfterLoginActivity.this, ProfileActivity.class));


        } else if (id == R.id.chat) {
            Toast.makeText(this, "Chat Selected", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.editProfile) {
            startActivity(new Intent(MainPageAfterLoginActivity.this, EditProfileActivity.class));

        } else if (id == R.id.uni) {
            startActivity(new Intent(MainPageAfterLoginActivity.this, AddUniversityDetails.class));

        } else if (id == R.id.teams) {
            startActivity(new Intent(MainPageAfterLoginActivity.this, TeamMatchesActivity.class));

        } else if (id == R.id.signOut) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainPageAfterLoginActivity.this, SignInActivity.class));
            Toast.makeText(this, "You are now Signed Out!", Toast.LENGTH_SHORT).show();
            finish();
        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
