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

    private Button qButton[]=new Button[14];

    private long idList[] = {R.id.question1Button, R.id.question2Button, R.id.question3Button,R.id.question4Button,
            R.id.question5Button,R.id.question6Button,R.id.question7Button,R.id.question8Button,R.id.question9Button,R.id.question10Button,
            R.id.question11Button,R.id.question12Button,R.id.question13Button,R.id.question14Button};


    public int quesNum,categoryNum=1,numOfQues,count1=0,count2=0,total=0,
            storequesNum,currentQuestion=0,pre=0,i;
    public String categoryName;

    private String[] questions=new String[14];
    private String[] answer = new String[14];

    private FirebaseFirestore db=FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);


        quesnumTextView=(TextView)findViewById(R.id.quesnumTextView);
        questionTextView=(TextView)findViewById(R.id.question);
        option1RadioButton=(RadioButton)findViewById(R.id.option1);
        option2RadioButton=(RadioButton)findViewById(R.id.option2);
        nextButton=(ImageButton) findViewById(R.id.nextButton);
        qButton[0]=(Button)findViewById(R.id.question1Button);
        qButton[1]=(Button)findViewById(R.id.question2Button);
        qButton[2]=(Button)findViewById(R.id.question3Button);
        qButton[3]=(Button)findViewById(R.id.question4Button);
        qButton[4]=(Button)findViewById(R.id.question5Button);
        qButton[5]=(Button)findViewById(R.id.question6Button);
        qButton[6]=(Button)findViewById(R.id.question7Button);
        qButton[7]=(Button)findViewById(R.id.question8Button);
        qButton[8]=(Button)findViewById(R.id.question9Button);
        qButton[9]=(Button)findViewById(R.id.question10Button);
        qButton[10]=(Button)findViewById(R.id.question11Button);
        qButton[11]=(Button)findViewById(R.id.question12Button);
        qButton[12]=(Button)findViewById(R.id.question13Button);
        qButton[13]=(Button)findViewById(R.id.question14Button);

        Bundle bundle=getIntent().getExtras();
        final String index=bundle.getString("index");
        Toast.makeText(this, index, Toast.LENGTH_SHORT).show();


        updateQuestion();



        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(total==14){
                    Toast.makeText(getApplicationContext(),"Finished assessment",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(Quiz.this,result.class);
                    Bundle bundle =new Bundle();
                    bundle.putStringArray("key",answer);
                    bundle.putStringArray("ques",questions);
                    bundle.putString("index",index);
                    intent.putExtras(bundle);
                    Quiz.this.finish();
                    startActivity(intent);
                }else if(currentQuestion==total && answer[currentQuestion-1]!=null){
                    quesNum=0;
                    updateQuestion();
                }else if(currentQuestion==total && answer[currentQuestion-1]==null){
                    Toast.makeText(Quiz.this, "Provide answer for this question", Toast.LENGTH_SHORT).show();

                }else if(currentQuestion!=total){
                    currentQuestion++;
                    questionTextView.setText(questions[currentQuestion-1]);
                    option1RadioButton.setText("yes");
                    option2RadioButton.setText("no");
                    qButton[currentQuestion-1].setBackgroundResource(R.drawable.round_shape_selected);
                    if(answer[currentQuestion-1].equals("yes")){
                        option1RadioButton.setChecked(true);
                    }else{
                        option2RadioButton.setChecked(true);
                    }


                }
            }
        });





        View.OnClickListener btnListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0 ; i < qButton.length ; i++){
                    if(v.getId() == idList[i]) {
                        if(questions[i]!=null && answer[i]!=null){
                            option1RadioButton.setChecked(false);
                            option2RadioButton.setChecked(false);

                            questionTextView.setText(questions[i]);
                            option1RadioButton.setText("yes");
                            option2RadioButton.setText("no");
                            qButton[pre].setBackgroundResource(R.drawable.round_shape);
                            qButton[i].setBackgroundResource(R.drawable.round_shape_selected);
                            pre=i;
                            currentQuestion=i+1;
                            quesnumTextView.setText("Question: "+String.valueOf(currentQuestion)+"/14");
                            if(answer[i].equals("yes")){
                                option1RadioButton.setChecked(true);
                            }else{
                                option2RadioButton.setChecked(true);
                            }
                            //Toast.makeText(Quiz.this, "Pre:"+pre+" current:"+currentQuestion, Toast.LENGTH_SHORT).show();

                        }else if(questions[i]!=null && answer[i]==null){
                            option1RadioButton.setChecked(false);
                            option2RadioButton.setChecked(false);
                            questionTextView.setText(questions[i]);
                            option1RadioButton.setText("yes");
                            option2RadioButton.setText("no");
                            qButton[pre].setBackgroundResource(R.drawable.round_shape);
                            qButton[i].setBackgroundResource(R.drawable.round_shape_selected);
                            pre=i;
                            currentQuestion=i+1;
                            quesnumTextView.setText("Question: "+String.valueOf(currentQuestion)+"/14");

                            //Toast.makeText(Quiz.this, "Pre:"+pre+" current:"+currentQuestion, Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(Quiz.this, "Not yet answered", Toast.LENGTH_SHORT).show();
                        }
                    }


                }

            }
        };



        for(Button btn:qButton)
            btn.setOnClickListener(btnListener);

    }

    public void option1Clicked(View view){
        option2RadioButton.setChecked(false);
        answer[currentQuestion-1]=(String)option1RadioButton.getText();
        //Toast.makeText(this, "Answer to q"+currentQuestion+": "+answer[currentQuestion-1], Toast.LENGTH_SHORT).show();

    }

    public void option2Clicked(View view){
        option1RadioButton.setChecked(false);
        answer[currentQuestion-1]=(String)option2RadioButton.getText();
        //Toast.makeText(this, "Answer to q"+currentQuestion+": "+answer[currentQuestion-1], Toast.LENGTH_SHORT).show();

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
                            Random r = new Random();
                            quesNum = r.nextInt(max - min) + min;
                            storequesNum=quesNum;
                            if(categoryName.equals("reading/spelling"))
                                count1++;
                            else if(categoryName.equals("dyslexia(other)"))
                                count2++;

                            else if((count1==1 && count2==0) || (count1==1 && count2==1)){
                                quesNum=max-storequesNum-1;
                            }
                            if(quesNum!=0){
                                //Toast.makeText(Quiz.this, "subquesNum:"+String.valueOf(quesNum), Toast.LENGTH_SHORT).show();
                                db.document("questions/"+categoryNum+"/subquestions/"+quesNum).get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if(documentSnapshot.exists()){
                                                    String question=documentSnapshot.getString("question");
                                                    String option1=documentSnapshot.getString("option1");
                                                    String option2=documentSnapshot.getString("option2");
                                                    quesnumTextView.setText("Question: "+String.valueOf(currentQuestion+1)+"/14");
                                                    questionTextView.setText(question);
                                                    option1RadioButton.setText(option1);
                                                    option2RadioButton.setText(option2);
                                                    option1RadioButton.setChecked(false);
                                                    option2RadioButton.setChecked(false);
                                                    qButton[pre].setBackgroundResource(R.drawable.round_shape);
                                                    qButton[total].setBackgroundResource(R.drawable.round_shape_selected);
                                                    pre=total;
                                                    currentQuestion=total+1;
                                                    if(!((categoryName.equals("reading/spelling") && count1==1)
                                                            ||(categoryName.equals("dyslexia(other)") && count2==1))){
                                                        categoryNum++;

                                                    }
                                                    questions[total]=question;

                                                    total++;
                                                    //Toast.makeText(Quiz.this, "count1:"+String.valueOf(count1)+" count2:"+String.valueOf(count2)+" categoryName:"+categoryName+" categoryNum:"+String.valueOf(categoryNum)+" currentQues:"+currentQuestion+" total:"+String.valueOf(total), Toast.LENGTH_LONG).show();



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





}
