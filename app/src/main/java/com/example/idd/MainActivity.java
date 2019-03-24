package com.example.idd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;



public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private Button signupButton,loginButton;
    private EditText emailEditText,passwordEditText;
    private ProgressDialog progressDialog;

    private CheckBox showPassword;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth=FirebaseAuth.getInstance();

        //signupButton = (Button) findViewById(R.id.signup);
        loginButton = (Button) findViewById(R.id.login);
        emailEditText=(EditText)findViewById(R.id.editTextEmail);
        passwordEditText=(EditText)findViewById(R.id.editTextPassword);
        progressDialog=new ProgressDialog(this);



        /*signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(view.getContext(), Signup.class);
                view.getContext().startActivity(Intent);}
        });
*/
        showPassword = (CheckBox) findViewById(R.id.showPasswordCheckbox);
        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            // @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=emailEditText.getText().toString();
                final String password=passwordEditText.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.setMessage("Logging in. Please Wait...");
                progressDialog.show();

                //authenticate user
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.

                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        passwordEditText.setError("Password too short, enter minimum 6 characters!");
                                    } else {
                                        Toast.makeText(MainActivity.this, "Authentication failed, check your email and password or sign up", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    finish();
                                    Intent intent = new Intent(MainActivity.this, home.class);
                                    startActivity(intent);
                                }
                                progressDialog.dismiss();
                            }
                        });

            }
        });

    }

    public void signUpClicked(View view){
        Intent Intent = new Intent(MainActivity.this, Signup.class);
        startActivity(Intent);
    }
/*
    showPassword = (CheckBox) findViewById(R.id.showPassword);
		showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
       // @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked) {
                mPasswordView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                mPasswordView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        }
    });*/

    @Override
    protected void onStart() {

        super.onStart();

        if(firebaseAuth.getCurrentUser()!=null){
            finish();
            Intent intent = new Intent(MainActivity.this, home.class);
            startActivity(intent);
        }
    }

}
