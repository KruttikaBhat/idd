package com.example.idd;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Button startButton;
    EditText editTextAge,editTextName;
    TextView userEmailNavHeader,userNameNavHeader;

    View headerView;
    NavigationView navigationView;

    private DrawerLayout drawer;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        firebaseAuth=FirebaseAuth.getInstance();

        android.support.v7.widget.Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        headerView=navigationView.getHeaderView(0);

        userEmailNavHeader=(TextView)headerView.findViewById(R.id.userEmailNavHeader);
        userNameNavHeader=(TextView)headerView.findViewById(R.id.userNameNavHeader);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigationdrawer_open,R.string.navigationdrawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null){
            String email=user.getEmail();
            if(email!=null) {
                //Toast.makeText(home.this, email, Toast.LENGTH_SHORT).show();
                userEmailNavHeader.setText(email);
                db.collection("users").document(email).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String name=documentSnapshot.getString("Name");
                                userNameNavHeader.setText(name);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(home.this, "couldn't get user info", Toast.LENGTH_SHORT).show();
                            }
                        });

            }


        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
                break;
            case R.id.nav_mystudents:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new StudentsFragment()).commit();
                break;

            case R.id.nav_signout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this,MainActivity.class));
                break;


        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    protected void onStart() {

        super.onStart();

        if(firebaseAuth.getCurrentUser()==null){
            finish();
            Intent intent = new Intent(home.this, MainActivity.class);
            startActivity(intent);
        }
    }


}
