package com.example.testapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInPage extends AppCompatActivity {



    EditText editTextEmail_I;
    EditText editTextPassword_I;

    Button buttonLogin;

    private FirebaseAuth mAuth;

    ProgressDialog progressDialog;
    TextView Text_to_Register;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_page);

        editTextEmail_I = findViewById(R.id.editTextEmail_I);
        editTextPassword_I = findViewById(R.id.editTextPassword_I);

        buttonLogin = findViewById(R.id.button_Login);

        mAuth = FirebaseAuth.getInstance();




        progressDialog = new ProgressDialog(SignInPage.this);
        progressDialog.setTitle("SignIn Process");
        progressDialog.setMessage("Loading...");

        Text_to_Register= findViewById(R.id.Text_to_Register);
        Text_to_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (getApplicationContext(), SignUpPage.class);
                startActivity(intent);
            }
        });


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email_I = editTextEmail_I.getText().toString();
                String password_I = editTextPassword_I.getText().toString();

                if(email_I.isEmpty()){
                    Toast.makeText(SignInPage.this, "Please, insert your email", Toast.LENGTH_SHORT).show();
                }

                if(password_I.isEmpty()){
                    Toast.makeText(SignInPage.this, "Please you need to insert your password", Toast.LENGTH_SHORT).show();
                }

                else{
                    SignInProcess(email_I, password_I);
                    progressDialog.show();
                }
            }
        });
    }

    private void SignInProcess(String email_i, String password_i) {
        mAuth.signInWithEmailAndPassword(email_i, password_i).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    // User can go to the Main Activity
                    FirebaseUser user = mAuth.getCurrentUser();

                    Intent intent = new Intent(getApplication(), MainActivity.class);
                    startActivity(intent);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(SignInPage.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}