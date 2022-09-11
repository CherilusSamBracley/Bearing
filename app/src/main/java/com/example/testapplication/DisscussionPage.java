package com.example.testapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DisscussionPage extends AppCompatActivity {


    String UserName, Topics, user_msg_key;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disscussion_page);


        Button btn_send_message;
        EditText message_field;
        ListView message_list;
        ArrayList<String> listConversation = new ArrayList<String>();

         DatabaseReference databaseReference;




        btn_send_message = findViewById(R.id.button_send_message);
        message_field = findViewById(R.id.edittext_TypeMessage);
        message_list = (ListView) findViewById(R.id.messagesList);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listConversation);
        message_list.setAdapter(arrayAdapter);


        UserName = getIntent().getExtras().get("user_name").toString();
        Topics = getIntent().getExtras().get("selected_topic").toString();

        setTitle("Topic:"+ Topics);

        databaseReference = FirebaseDatabase.getInstance().getReference().child(Topics);

        btn_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, Object> map = new HashMap<String, Object>();
                user_msg_key = databaseReference.push().getKey();
                databaseReference.updateChildren(map);


                DatabaseReference databaseReference1 = databaseReference.child(user_msg_key);

                Map<String, Object> map1 = new HashMap<String, Object>();
                map1.put("msg", message_field.getText().toString());
                map1.put("user", UserName);
                databaseReference1.updateChildren(map1);
            }
        });


        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                updateConversation(snapshot);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                updateConversation(snapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }





    public  void updateConversation(DataSnapshot snapshot){
        String msg, user, conversation;
        Iterator I = snapshot.getChildren().iterator();
        while(I.hasNext()){
            msg = (String)((DataSnapshot)I.next()).getValue();
            user = (String)((DataSnapshot)I.next()).getValue();

            conversation = user+ ":" +msg;

            arrayAdapter.insert(conversation, 0);
            arrayAdapter.notifyDataSetChanged();

        }
    }
}