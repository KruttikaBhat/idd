package com.example.idd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signup extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Button buttonSignUp;
    private EditText editTextEmail, editTextPassword;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth=FirebaseAuth.getInstance();

        editTextEmail=(EditText)findViewById(R.id.editTextEmail);
        editTextPassword=(EditText)findViewById(R.id.editTextPassword);

        buttonSignUp=(Button)findViewById(R.id.buttonSignUp);

        progressDialog=new ProgressDialog(this);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getting email and password from edit texts
                String email = editTextEmail.getText().toString().trim();
                String password  = editTextPassword.getText().toString().trim();

                //checking if email and passwords are empty
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(),"Please enter email",Toast.LENGTH_LONG).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(),"Please enter password",Toast.LENGTH_LONG).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //if the email and password are not empty
                //displaying a progress dialog

                progressDialog.setMessage("Registering Please Wait...");
                progressDialog.show();

                //creating a new user
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //checking if success
                                if(task.isSuccessful()){
                                    //display some message here
                                    Toast.makeText(Signup.this,"Successfully registered",Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(Signup.this,home.class));
                                }else{
                                    //display some message here
                                    Toast.makeText(Signup.this,"Registration Error",Toast.LENGTH_LONG).show();
                                }
                                progressDialog.dismiss();
                            }
                        });
            }
        });



    }




}
