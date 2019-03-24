package com.example.idd;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

//import com.firebase.client.DataSnapshot;
//import com.firebase.client.Firebase;
//import com.firebase.client.FirebaseError;
//import com.firebase.client.ValueEventListener;

public class Quiz extends AppCompatActivity {

    private TextView questionTextView,quesnumTextView;
    private RadioButton option1RadioButton, option2RadioButton;
    private ImageButton nextButton;

    public int quesNum,categoryNum=1,numOfQues,count1=0,count2=0,total=0,storequesNum;
    public String categoryName;

    private FirebaseFirestore db=FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        final String[] answer = new String[14];
        quesnumTextView=(TextView)findViewById(R.id.quesnumTextView);
        questionTextView=(TextView)findViewById(R.id.question);
        option1RadioButton=(RadioButton)findViewById(R.id.option1);
        option2RadioButton=(RadioButton)findViewById(R.id.option2);
        nextButton=(ImageButton) findViewById(R.id.nextButton);

        Bundle bundle=getIntent().getExtras();
        final String index=bundle.getString("index");
        Toast.makeText(this, index, Toast.LENGTH_SHORT).show();


        updateQuestion();

        option1RadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer[total-1] = (String) option1RadioButton.getText();
                //Toast.makeText(getApplicationContext(),"Answered q"+String.valueOf(categoryNum-1) +
                //        ": "+answer[total-1]+",subques"+String.valueOf(quesNum)+",count"+String.valueOf(count)
                //        +"Categoryname:"+categoryName+",total:"+String.valueOf(total),Toast.LENGTH_LONG).show();

            }
        });

        option2RadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer[total-1] = (String) option2RadioButton.getText();
                //Toast.makeText(getApplicationContext(),"Answered q"+String.valueOf(categoryNum-1) +
                //        ": "+answer[total-1]+",subques"+String.valueOf(quesNum)+",count"+String.valueOf(count)
                //        +"Categoryname:"+categoryName+",total:"+String.valueOf(total),Toast.LENGTH_LONG).show();

            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(total==14){
                    Toast.makeText(getApplicationContext(),"Finished assessment",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(Quiz.this,result.class);
                    Bundle bundle =new Bundle();
                    bundle.putStringArray("key",answer);
                    bundle.putString("index",index);
                    intent.putExtras(bundle);
                    Quiz.this.finish();
                    startActivity(intent);
                }else{
                    quesNum=0;
                    updateQuestion();

                }
            }
        });
    }

    public void updateQuestion(){


        db.document("questions/"+categoryNum).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            numOfQues =documentSnapshot.getLong("numberOfQuestions").intValue();
                            categoryName=documentSnapshot.getString("categoryName");
                            int min=1;
                            int max= numOfQues +1;
                            if(count1==0 || count2==0){
                                if(categoryName.equals("reading/spelling"))
                                    count1++;
                                if(categoryName.equals("dyslexia(other)"))
                                    count2++;
                                Random r = new Random();
                                quesNum = r.nextInt(max - min) + min;
                                storequesNum=quesNum;
                            }
                            else if(count1==1 || count2==1){
                                quesNum=max-storequesNum-1;
                            }
                            if(quesNum!=0){
                                Toast.makeText(Quiz.this, "subquesNum:"+String.valueOf(quesNum), Toast.LENGTH_SHORT).show();
                                db.document("questions/"+categoryNum+"/subquestions/"+quesNum).get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if(documentSnapshot.exists()){
                                                    String question=documentSnapshot.getString("question");
                                                    String option1=documentSnapshot.getString("option1");
                                                    String option2=documentSnapshot.getString("option2");
                                                    quesnumTextView.setText("Question: "+String.valueOf(total+1)+"/14");
                                                    questionTextView.setText(question);
                                                    option1RadioButton.setText(option1);
                                                    option2RadioButton.setText(option2);
                                                    option1RadioButton.setChecked(false);
                                                    option2RadioButton.setChecked(false);
                                                    if(!((count1==1 && categoryName.equals("reading/spelling"))
                                                            || (count2==1 && categoryName.equals("dyslexia(other)")))){
                                                        categoryNum++;
                                                    }
                                                    total++;

                                                } else{
                                                    Toast.makeText(Quiz.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Quiz.this, "Error!", Toast.LENGTH_SHORT).show();
                                                Log.d("Quiz",e.toString());
                                            }
                                        });
                            }

                        } else{
                            Toast.makeText(Quiz.this, "Category document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Quiz.this, "Category Error", Toast.LENGTH_SHORT).show();
                        Log.d("Quiz",e.toString());
                    }
                });



    }

/*
    public void updateQuestion(){
        questionRef=new Firebase("https://iddapp-17fcc.firebaseio.com/"+ quesNum +"/question");
        questionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String question=dataSnapshot.getValue(String.class);
                questionTextView.setText(question);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        option1Ref=new Firebase("https://iddapp-17fcc.firebaseio.com/"+ quesNum +"/option1");
        option1Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String choice=dataSnapshot.getValue(String.class);
                option1RadioButton.setText(choice);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        option2Ref=new Firebase("https://iddapp-17fcc.firebaseio.com/"+ quesNum +"/option2");
        option2Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String choice=dataSnapshot.getValue(String.class);
                option2RadioButton.setText(choice);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        quesNum++;

    }
*/

}
