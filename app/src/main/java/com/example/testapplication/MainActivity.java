package com.example.testapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.pdf.PdfPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    DrawerLayout drawerLayout;

    ImageView MenuIconButton;

    BottomNavigationView bottomNavigationItems;

    NavigationView navigationView;

    ProgressDialog progressDialog;

    ListView lvDisscussionTopics;
    ArrayList<String> listOfDiscussion = new ArrayList<String>();
    ArrayAdapter arrayAdapter;

    String UserName;


    DatabaseReference databaseReference;

    DatabaseReference goals;
    TextView goal_1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goal_1 = findViewById(R.id.goal_1);


        mAuth = FirebaseAuth.getInstance();

        drawerLayout = findViewById(R.id.drawerLayout);

        MenuIconButton = findViewById(R.id.menu_icon);

        bottomNavigationItems = findViewById(R.id.bottomNavigationItems);

        navigationView = findViewById(R.id.navigationViewMain);
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot();

        goals = FirebaseDatabase.getInstance().getReference("GOALS");




        lvDisscussionTopics = (ListView) findViewById(R.id.lvDiscussionTopics);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1,listOfDiscussion);
        lvDisscussionTopics.setAdapter(arrayAdapter);

        getUsername();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Set <String> set = new HashSet<>();
                Iterator i = snapshot.getChildren().iterator();

                while(i.hasNext()){
                    set.add(((DataSnapshot)i.next()).getKey());
                }

                arrayAdapter.clear();
                arrayAdapter.addAll(set);
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        lvDisscussionTopics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent I = new Intent (getApplicationContext(), DisscussionPage.class);
                I.putExtra("selected_topic", ((TextView)view).getText().toString());
                I.putExtra("user_name", UserName);
                startActivity(I);

            }
        });



        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Sign Out");
        progressDialog.setMessage("Loading...");

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.item_3:
                        Toast.makeText(MainActivity.this, "Goals", Toast.LENGTH_SHORT).show();
                        goalsBuilder();
                        break;


                    case R.id.item_6:
                        Toast.makeText(MainActivity.this, "Sign Out", Toast.LENGTH_SHORT).show();
                        progressDialog.show();
                        SignOutFunction();
                        break;

                    case R.id.item_7:
                        Toast.makeText(MainActivity.this, "About developers...", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent (getApplicationContext(), AboutDeveloper.class);
                        startActivity(intent);
                        break;


                }
                return false;
            }
        });





        MenuIconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        bottomNavigationItems.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.Item_1:
                        goalsBuilder();
                        break;

                    case R.id.Item_2:
                       Intent intent = new Intent (getApplicationContext(), MainActivity.class);
                       startActivity(intent);
                        break;

                    case R.id.Item_3:
                        progressDialog.show();
                        SignOutFunction();
                        break;
                }
                return false;
            }
        });





    }

    private void SignOutFunction() {
        mAuth.signOut();
        progressDialog.dismiss();
        CheckUserStatus();
    }




    private void CheckUserStatus(){
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){
            //Already been registered stay on the main page in this case.
        }else{
            //haven't been registered yet, go back to the main Page
            Intent intent = new Intent(getApplicationContext(), Step1.class);
            startActivity(intent);

        }
    }

    private void getUsername(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an Username...");
        EditText userName = new EditText(this);

        builder.setView(userName);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                UserName= userName.getText().toString();


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getUsername();
            }
        });

        builder.show();
    }

    @Override
    protected void onStart() {
        CheckUserStatus();
        super.onStart();

    }

    private void goalsBuilder (){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set your daily goal");


        EditText goal_one = new EditText(this);
        builder.setView(goal_one);


        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String goal_one_1 = goal_one.getText().toString();
                goals.setValue(goal_one_1);

                Toast.makeText(MainActivity.this, ""+goal_one_1.toString(), Toast.LENGTH_SHORT).show();

                goals.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String data = snapshot.getValue(String.class);
                        goal_1.setText(data);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               //

            }
        });

        builder.show();
    }
}