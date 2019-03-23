package com.example.idd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Button buttonSignUp;
    private EditText editTextEmail, editTextPassword,editTextAge,editTextName,editTextProfession,editTextSchool;
    private ProgressDialog progressDialog;

    private static final String root="users";
    private static final String key_name="Name";
    private static final String key_age="Age";
    private static final String key_profession="Profession";
    private static final String key_school="School";
    private static final String key_num="numberOfStudents";

    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth=FirebaseAuth.getInstance();

        editTextEmail=(EditText)findViewById(R.id.editTextEmail);
        editTextPassword=(EditText)findViewById(R.id.editTextPassword);
        editTextName=(EditText)findViewById(R.id.editTextName);
        editTextAge=(EditText)findViewById(R.id.editTextAge);
        editTextProfession=(EditText)findViewById(R.id.editTextProfession);
        editTextSchool=(EditText)findViewById(R.id.editTextSchool);

        buttonSignUp=(Button)findViewById(R.id.buttonSignUp);

        progressDialog=new ProgressDialog(this);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getting email and password from edit texts
                final String email = editTextEmail.getText().toString().trim();
                String password  = editTextPassword.getText().toString().trim();
                String name = editTextName.getText().toString().trim();
                String age = editTextAge.getText().toString().trim();
                String profession = editTextProfession.getText().toString().trim();
                String school = editTextSchool.getText().toString().trim();
                String num="0";

                final Map<String, Object> user=new HashMap<>();
                user.put(key_name,name);
                user.put(key_age,age);
                user.put(key_profession,profession);
                user.put(key_school,school);
                user.put(key_num,num);
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
                                    db.collection(root).document(email).set(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    //Toast.makeText(Signup.this,"New user created",Toast.LENGTH_LONG).show();
                                                    Toast.makeText(Signup.this,"Successfully registered",Toast.LENGTH_LONG).show();
                                                    finish();
                                                    startActivity(new Intent(Signup.this,home.class));
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(Signup.this,"Error",Toast.LENGTH_LONG).show();
                                                    Log.d("Signup",e.toString());

                                                }
                                            });

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
