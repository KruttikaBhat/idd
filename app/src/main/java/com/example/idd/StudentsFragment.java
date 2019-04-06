package com.example.idd;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class StudentsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ChildAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private static final String root="users";
    private static final String key_numofStudents="numberOfStudents";
    private static final String childcollection="children";

    public int i,i1;


    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_students,container,false);
        inflater.inflate(R.layout.fragment_home,container,false);

        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();

        final ArrayList<ChildItem> childList= new ArrayList<>();



        //childList.add(new ChildItem("name1","age1","class1"));
        //childList.add(new ChildItem("name2","age2","class2"));
        //childList.add(new ChildItem("name3","age3","class3"));
        recyclerView=view.findViewById(R.id.studentRecyclerView);
        layoutManager=new LinearLayoutManager(getActivity());
        adapter=new ChildAdapter(childList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnChildCLickListener(new ChildAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(int position) {
                childList.get(position).getChildName();

            }
        });

        recyclerView=view.findViewById(R.id.studentRecyclerView);
        layoutManager=new LinearLayoutManager(getActivity());

        if(user!=null){
            final String email=user.getEmail();
            if(email!=null) {

                /*db.collection(root).document(email).collection(childcollection)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                                if (e != null) {
                                    Log.d(TAG, "Error: " + e.getMessage());
                                }
                                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                                    if(doc.getType()==DocumentChange.Type.ADDED){
                                        ChildItem data=doc.getDocument().toObject(ChildItem.class);
                                        childList.add(data);
                                        adapter.notifyDataSetChanged();
                                    }

                                }
                            }
                        });*/


                //Toast.makeText(home.this, email, Toast.LENGTH_SHORT).show();
                db.collection(root).document(email).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String number=documentSnapshot.getString(key_numofStudents);
                                final Integer num = (Integer.valueOf(number));



                                //final String[] cName=new String[num];
                                //final String[] cAge=new String[num];
                                //final String[] cClass=new String[num];



                                for(i=1; i <=num; i++){
                                    db.collection(root).document(email).collection(childcollection).document(String.valueOf(i)).get()
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    String name=documentSnapshot.getString("Name");
                                                    //cName[i-1]=name;
                                                    String age=documentSnapshot.getString("Age");
                                                    //cAge[i -1]=age;
                                                    String cclass=documentSnapshot.getString("Class");
                                                    String cindex=documentSnapshot.getString("Index");
                                                    //cClass[i -1]=cclass;
                                                    //Toast.makeText(getActivity(), cName[i-1]+" age:"+cAge[i-1]+" class:"+cClass[i-1], Toast.LENGTH_SHORT).show();
                                                    if(name!=null && age!=null && cclass!=null){
                                                        //Toast.makeText(getActivity(), name+" age1: "+age+" class1: "+cclass, Toast.LENGTH_SHORT).show();
                                                        childList.add(new ChildItem(name,age,cclass,cindex));
                                                        //Toast.makeText(getActivity(), String.valueOf(childList.size()), Toast.LENGTH_SHORT).show();

                                                    }
                                                    if(childList.size()==num){
                                                        //Toast.makeText(getActivity(), "done", Toast.LENGTH_SHORT).show();

                                                        adapter=new ChildAdapter(childList);
                                                        recyclerView.setLayoutManager(layoutManager);
                                                        recyclerView.setAdapter(adapter);
                                                        //Toast.makeText(getActivity(), "done1", Toast.LENGTH_SHORT).show();

                                                    }



                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getActivity(), "Couldn't get child data", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                                //childList.add(new ChildItem("name1","age1","class1"));

                                //childList.add(new ChildItem("name2","age2","class2"));
                                //childList.add(new ChildItem("name3","age3","class3"));
                                //Toast.makeText(getActivity(), String.valueOf(childList.size()), Toast.LENGTH_SHORT).show();


                               /* while(i==num+1){
                                    for(i1 =0; i1<num; i1++){
                                        childList.add(new ChildItem( cName[i1],cAge[i1],cClass[i1]));
                                        Toast.makeText(getActivity(), cName[i1]+" age2:"+cAge[i1]+" class2:"+cClass[i1], Toast.LENGTH_SHORT).show();

                                    }
                                    while(i1==num){

                                        //childList.add(new ChildItem("name1","age1","class1"));

                                        //childList.add(new ChildItem("name2","age2","class2"));
                                        //childList.add(new ChildItem("name3","age3","class3"));
                                        recyclerView=view.findViewById(R.id.studentRecyclerView);
                                        layoutManager=new LinearLayoutManager(getActivity());
                                        adapter=new ChildAdapter(childList);

                                        recyclerView.setLayoutManager(layoutManager);
                                        recyclerView.setAdapter(adapter);

                                        break;

                                    }
                                    break;

                                }*/



                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Couldn't get user email", Toast.LENGTH_SHORT).show();
                            }
                        });



            }
            else{
                Toast.makeText(getActivity(), "Email can't be found", Toast.LENGTH_SHORT).show();

            }


        }





        return view;
    }
}
