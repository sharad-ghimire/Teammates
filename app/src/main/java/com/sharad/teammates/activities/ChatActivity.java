package com.sharad.teammates.activities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sharad.teammates.R;
import com.sharad.teammates.adapters.ChatAdapter;
import com.sharad.teammates.adapters.MatchAdapeter;
import com.sharad.teammates.models.Chat;
import com.sharad.teammates.models.Match;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private String currentLoggedStudentUID, matchID;
    private RecyclerView mRecyclerViewChat;
    private RecyclerView.Adapter mRecyclerViewAdapter;

    private EditText mSendMsgET;
    private Button mSendMsgBtb;

    DatabaseReference studentDBRef;
    DatabaseReference chatDBRef;
    private String chatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        currentLoggedStudentUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        matchID = getIntent().getExtras().getString("matchID");
        mSendMsgET = findViewById(R.id.sendMsgId);
        mSendMsgBtb = findViewById(R.id.sendMsg);
        studentDBRef = FirebaseDatabase.getInstance().getReference().child(getString(R.string.dbnode_students)).child(currentLoggedStudentUID)
                .child("Connections").child("Matched").child(matchID).child("ChatID");
        chatDBRef = FirebaseDatabase.getInstance().getReference().child("Chat");

        getChatID();

        mRecyclerViewChat = findViewById(R.id.recyclerViewChat);
        mRecyclerViewAdapter = new ChatAdapter(ChatActivity.this, getChatData());
        mRecyclerViewChat.setAdapter(mRecyclerViewAdapter);
        mRecyclerViewChat.setLayoutManager(new LinearLayoutManager(this));


        mSendMsgBtb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendThatMsg();
            }
        });
    }

    private void sendThatMsg() {
        String msg = mSendMsgET.getText().toString();
        if(!msg.isEmpty()){
            //Create new Data entree for Message
            DatabaseReference nayaMsgDB = chatDBRef.push();

            Map newMsg = new HashMap();
            newMsg.put("createdByUser", currentLoggedStudentUID);
            newMsg.put("textMessage", msg);
            nayaMsgDB.setValue(newMsg);

        }
        mSendMsgET.setText(null);

    }
    private void getChatID() {
        studentDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    chatId = dataSnapshot.getValue().toString();
                    chatDBRef = chatDBRef.child(chatId);
                    getChatMsg();

                    
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getChatMsg() {
        chatDBRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    String textMessage = null;
                    String createdByUser = null;
                    if(dataSnapshot.child("textMessage").getValue() != null){
                        textMessage = dataSnapshot.child("textMessage").getValue().toString();

                    }
                    if(dataSnapshot.child("createdByUser").getValue() != null){
                        createdByUser = dataSnapshot.child("createdByUser").getValue().toString();
                    }
                    if(textMessage != null && createdByUser != null){
                        Boolean whichUser = false;
                        if(createdByUser.equals(currentLoggedStudentUID)){
                            whichUser = true;
                        }
                        Chat newChat = new Chat(textMessage, whichUser);

                        //Add this chat to array list of chats which is chat
                        chat.add(newChat);
                        mRecyclerViewAdapter.notifyDataSetChanged();
                    }

                }


            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    List<Chat> chat = new ArrayList<Chat>();
    private List<Chat> getChatData() {
        return chat;
    }
}
