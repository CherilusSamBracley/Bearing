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

public class SignUpPage extends AppCompatActivity {


    private FirebaseAuth mAuth;

    EditText editTextUsername;
    EditText editTextEmail;
    EditText editTextPassword;

    Button buttonRegister;

    ProgressDialog progressDialog;

    TextView Text_to_Login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);

        buttonRegister = findViewById(R.id.button_Register);

        progressDialog = new ProgressDialog(SignUpPage.this);

        progressDialog.setTitle("Sign Up process");
        progressDialog.setMessage("Loading...");

        Text_to_Login = findViewById(R.id.Text_to_Login);

        Text_to_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignInPage.class);
                startActivity(intent);
            }
        });




        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String email_user_R = editTextEmail.getText().toString();
                String password_user_R = editTextPassword.getText().toString();
                String username_user_R = editTextUsername.getText().toString();





                if(username_user_R.isEmpty()){
                    Toast.makeText(SignUpPage.this, "Please, you need to pick a username", Toast.LENGTH_SHORT).show();
                }

                if(email_user_R.isEmpty()){
                    Toast.makeText(SignUpPage.this, "Please, enter your email address", Toast.LENGTH_SHORT).show();
                }


                if(password_user_R.length() <=6){
                    Toast.makeText(SignUpPage.this, "Please use a stronger password...", Toast.LENGTH_SHORT).show();

                }

                if(password_user_R.isEmpty()){
                    Toast.makeText(SignUpPage.this, "Please add a password", Toast.LENGTH_SHORT).show();
                }

                else{

                    SignUpProcess(email_user_R,password_user_R);
                    progressDialog.show();
                }








            }
        });
    }

    private void SignUpProcess(String email_user_r, String password_user_r) {
        mAuth.createUserWithEmailAndPassword(email_user_r, password_user_r).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                FirebaseUser user = mAuth.getCurrentUser();

               if(task.isSuccessful()){
                   progressDialog.dismiss();
                   Toast.makeText(SignUpPage.this, "Welcome"+user.getEmail(), Toast.LENGTH_SHORT).show();
                   Intent intent = new Intent (getApplicationContext(), MainActivity.class);
                   startActivity(intent);

               }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(SignUpPage.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }


}