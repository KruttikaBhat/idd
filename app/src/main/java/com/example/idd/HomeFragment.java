package com.example.idd;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {


    public HomeFragment(){

    }

    EditText childName,childAge,childClass;
    Button start;


    private static final String root="users";
    private static final String childcollection="children";
    private static final String key_name="Name";
    private static final String key_age="Age";
    private static final String key_class="Class";
    private static final String key_numOfAssess="numberOfAssessments";
    private static final String key_numofStudents="numberOfStudents";
    private static final String key_index="Index";


    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_home,container,false);

        firebaseAuth=FirebaseAuth.getInstance();

        childName=(EditText)view.findViewById(R.id.editTextChildName);
        childAge=(EditText)view.findViewById(R.id.editTextChildAge);
        childClass=(EditText)view.findViewById(R.id.editTextChildClass);



        start=(Button)view.findViewById(R.id.registerChildButton);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmpty(childName) && isEmpty(childAge) && isEmpty(childClass)){
                    Toast.makeText(getActivity(), "Enter all fields", Toast.LENGTH_SHORT).show();

                }else{
                    String cName  = childName.getText().toString().trim();
                    String cAge = childAge.getText().toString().trim();
                    String cClass = childClass.getText().toString().trim();
                    String numOfAssess="0";

                    final Map<String, Object> child=new HashMap<>();
                    child.put(key_name,cName);
                    child.put(key_age,cAge);
                    child.put(key_class,cClass);
                    child.put(key_numOfAssess,numOfAssess);

                    FirebaseUser user=firebaseAuth.getCurrentUser();
                    if(user!=null){
                        final String email=user.getEmail();
                        if(email!=null) {
                            //Toast.makeText(home.this, email, Toast.LENGTH_SHORT).show();
                            db.collection(root).document(email).get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            String number=documentSnapshot.getString(key_numofStudents);
                                            Integer newnumInt = (Integer.valueOf(number))+1;
                                            String newnumString=String.valueOf(newnumInt);
                                            child.put(key_index,newnumString);

                                            db.collection(root).document(email).update(key_numofStudents,newnumString);
                                            db.collection(root).document(email).collection(childcollection).document(newnumString).set(child)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            //Toast.makeText(Signup.this,"New user created",Toast.LENGTH_LONG).show();
                                                            Toast.makeText(getActivity(),"Successfully registered child",Toast.LENGTH_LONG).show();
                                                            //Intent intent=new Intent(getActivity(),Quiz.class);
                                                            //startActivity(intent);

                                                            FragmentTransaction transaction=getFragmentManager().beginTransaction();
                                                            transaction.replace(R.id.fragment_container,new StudentsFragment());
                                                            transaction.addToBackStack(null);
                                                            transaction.commit();


                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(getActivity(),"Error in registration",Toast.LENGTH_LONG).show();
                                                            Log.d("HomeFragment",e.toString());

                                                        }
                                                    });





                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getActivity(), "couldn't get user email", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }


                    }

                }
                
            }
        });





        return view;
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0; //false->EditText is not empty, true->EditText is empty
    }
}
