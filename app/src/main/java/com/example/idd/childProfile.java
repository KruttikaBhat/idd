package com.example.idd;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class childProfile extends AppCompatActivity {


    private static final String TAG = "Firelog";
    private RecyclerView recyclerView;
    private assessAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView name, age, cclass, assessmentsDone;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;

    private Button quiz;

    private static final String root = "users";
    private static final String childcollection = "children";
    private static final String key_assess = "assessments";
    private static final String key_name = "Name";
    private static final String key_age = "Age";
    private static final String key_class = "Class";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_profile);

        final ArrayList<assessData> List= new ArrayList<>();

        //List.add(new assessData("name1","age1","class1"));
        //List.add(new assessData("name2","age2","class2"));
        //List.add(new assessData("name3","age3","class3"));
        recyclerView=findViewById(R.id.childProfileRecyclerView);
        layoutManager=new LinearLayoutManager(this);
        adapter=new assessAdapter(List);

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);




        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //assessmentsDone = (TextView) findViewById(R.id.childAssessments);
        name = (TextView) findViewById(R.id.childNameTextView);
        age = (TextView) findViewById(R.id.childAgeTextView);
        cclass = (TextView) findViewById(R.id.childClassTextView);


        Bundle bundle = getIntent().getExtras();
        final String index = bundle.getString("index");
        Toast.makeText(this, index, Toast.LENGTH_SHORT).show();

        quiz = (Button) findViewById(R.id.assessmentButton);
        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(childProfile.this, Quiz.class);
                Bundle bundle = new Bundle();
                bundle.putString("index", index);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });




        if (user != null)

            {
                final String email = user.getEmail();
                if (email != null) {
                    //Toast.makeText(home.this, email, Toast.LENGTH_SHORT).show();
                    db.collection(root).document(email).collection(childcollection).document(index).get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    String cname = documentSnapshot.getString(key_name);
                                    String cage = documentSnapshot.getString(key_age);
                                    String ccls = documentSnapshot.getString(key_class);
                                    name.setText(cname);
                                    age.setText(cage);
                                    cclass.setText(ccls);

                                    db.collection(root).document(email).collection(childcollection).document(index).collection(key_assess)
                                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                @Override
                                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                                    if (e != null) {
                                                        Log.d(TAG, "Error: " + e.getMessage());
                                                    }
                                                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                                                        if(doc.getType()==DocumentChange.Type.ADDED){
                                                            assessData data=doc.getDocument().toObject(assessData.class);
                                                            List.add(data);
                                                            adapter.notifyDataSetChanged();
                                                        }
                                                        /*

                                                        String date = doc.getString(key_date);
                                                        String result = doc.getString(key_result);
                                                        String des = doc.getString(key_des);
                                                        Log.d(TAG, "Date:" + date + " Result:" + result + " Description:" + des);
                                                        */
                                                    }

                                                }
                                            });
                                }

                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(childProfile.this, "Couldn't retrieve child info", Toast.LENGTH_SHORT).show();
                                }
                            });


                }



                /*db.collection(root).document(email).collection(childcollection).document(index).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String number = documentSnapshot.getString(key_numassess);
                                String nameString = documentSnapshot.getString(key_name);
                                String ageString = documentSnapshot.getString(key_age);
                                String cclassString = documentSnapshot.getString(key_class);
                                name.setText(nameString);
                                age.setText(ageString);
                                cclass.setText(cclassString);

                                Integer num = (Integer.valueOf(number));

                                if (Integer.valueOf(number) == 0) {
                                    assessmentsDone.setText("No assessments done so far");
                                }/*else{

                                    db.collection(root).document(email).collection(childcollection).document(index).collection(key_assess).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            List<assessments> mAssessList = new ArrayList<>();
                                            if(task.isSuccessful()){
                                                for(QueryDocumentSnapshot document : task.getResult()) {
                                                    assessments assess = document.toObject(assessments.class);
                                                    mAssessList.add(assess);
                                                }
                                                mAssessmentsListView = (ListView) findViewById(R.id.assessmentsListView);
                                                mAssessmentsAdapter = new assessmentsAdapter(this, mAssessmentsList);
                                                mAssessmentsListView.setAdapter(mAssessmentsAdapter);



                                            } else {
                                                Log.d("childProfile", "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });


                                }

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(childProfile.this, "Child document not found", Toast.LENGTH_SHORT).show();
                            }
                        });

            }*/
                else {
                    Toast.makeText(childProfile.this, "Email can't be found", Toast.LENGTH_SHORT).show();

                }


            }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private List<assessData> getData()
    {
        List<assessData> list = new ArrayList<>();
        list.add(new assessData("First Exam", "May 23, 2015", "Best Of Luck"));
        list.add(new assessData("Second Exam", "June 09, 2015", "b of l"));
        list.add(new assessData("My Test Exam", "April 27, 2017", "This is testing exam .."));
        return list;
    }



}
