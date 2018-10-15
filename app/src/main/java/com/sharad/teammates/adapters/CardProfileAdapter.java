package com.sharad.teammates.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sharad.teammates.R;
import com.sharad.teammates.activities.ProfileActivity;
import com.sharad.teammates.activities.ViewProfileAcitivity;
import com.sharad.teammates.models.Student;
import com.sharad.teammates.models.Subject;

import java.util.ArrayList;
import java.util.List;

public class CardProfileAdapter extends RecyclerView.Adapter<CardProfileAdapter.ViewHolder> {

    private final List<Student> student;
    private Context mContext;
    private boolean flag = true;
    private DatabaseReference studentsDB;
    private List<Student> addedStudent = new ArrayList<>();

    public CardProfileAdapter(Context mContext, List<Student> student) {
        this.mContext = mContext;
        this.student = student;
        studentsDB = FirebaseDatabase.getInstance().getReference().child(mContext.getString(R.string.dbnode_students));
    }

    /* This Method is Responsible for inflating the view  */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_profile_view, viewGroup, false);
        return new ViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        Student myStudent =  student.get(i);
//        Glide.with(mContext).asBitmap().load(mImages.get(i)).into(viewHolder.image);
//        viewHolder.nameTV.setText(mName.get(i));

        switch (myStudent.getProfile_image()){
            case "https://www.freewebmentor.com/default-avatar.png" :
                Glide.with(mContext).asBitmap().load(mContext.getString(R.string.default_image)).into(viewHolder.image);
                break;

            default:
                Glide.with(mContext).asBitmap().load(myStudent.getProfile_image()).into(viewHolder.image);
                break;
        }

        viewHolder.nameTV.setText(myStudent.getName());
        viewHolder.matchIDTV.setText(myStudent.getUser_id());
        viewHolder.relativeLayoutCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("clickedStudentID",viewHolder.matchIDTV.getText().toString());
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        viewHolder.addBtn.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if(flag){
                    Student addStudent = student.get(i);
                    addedStudent.add(addStudent);
                    viewHolder.addBtn.setBackgroundColor(Color.parseColor("#D3D3D3"));
                    viewHolder.addBtn.setText("Remove");
                    Toast.makeText(mContext, "Added", Toast.LENGTH_SHORT).show();
                    addStudentToAddedList(addStudent);
                    flag = false;
                }
            }
        });

    }


    //Adds student to the list of added in database
    private void addStudentToAddedList(Student addStudent) {
        studentsDB.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Connections").child("Added")
                .child(addStudent.getUser_id())
                .setValue(true);


        isTeamMatch(addStudent.getUser_id());
    }

    //Conforming a match
    private void isTeamMatch(final String user_id) {
        DatabaseReference loggedInUserAddedDB = studentsDB.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Connections").child("Added").child(user_id);
        loggedInUserAddedDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Toast.makeText(mContext, "Connection Made!!!!!", Toast.LENGTH_SHORT).show();


                    //This will be key for the chat
                    String key = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();

                    //Added Matched to both Users
                    studentsDB.child(dataSnapshot.getKey())
                            .child("Connections")
                            .child("Matched")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ChatID")
                            .setValue(key);

                    studentsDB.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("Connections")
                            .child("Matched")
                            .child(dataSnapshot.getKey()).child("ChatID")
                            .setValue(key);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return student.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView image;
        TextView nameTV, matchIDTV;
        RelativeLayout relativeLayoutCardView;
        Button addBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            nameTV = itemView.findViewById(R.id.nameOfMate);
            relativeLayoutCardView = itemView.findViewById(R.id.relativeLayoutCardView);
            addBtn = itemView.findViewById(R.id.addButtonMatches);
            matchIDTV = itemView.findViewById(R.id.matchIDTV);
        }
    }

}
