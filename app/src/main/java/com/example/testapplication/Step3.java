package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Step3 extends AppCompatActivity {

    ConstraintLayout constraintLayout;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step3);

        constraintLayout = findViewById(R.id. step_three);

        mAuth = FirebaseAuth.getInstance();

        constraintLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                CheckUserStatus();

                return false;
            }
        });
    }

    private void CheckUserStatus() {
        FirebaseUser user = mAuth.getCurrentUser();

        if(user!=null){
            // already been registered
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent (getApplicationContext(), SignUpPage.class);
            startActivity(intent);
        }
    }
}