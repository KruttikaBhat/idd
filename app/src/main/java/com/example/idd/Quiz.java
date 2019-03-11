package com.example.idd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class Quiz extends AppCompatActivity {

    private TextView questionTextView;
    private RadioButton option1RadioButton, option2RadioButton;
    private Button nextButton;

    private int quesNum=0;

    String answer;

    private Firebase questionRef,option1Ref,option2Ref,answerRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        final String[] answer = new String[10];
        questionTextView=(TextView)findViewById(R.id.question);
        option1RadioButton=(RadioButton)findViewById(R.id.option1);
        option2RadioButton=(RadioButton)findViewById(R.id.option2);
        nextButton=(Button)findViewById(R.id.nextButton);

        updateQuestion();

        option1RadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer[quesNum-1] = (String) option1RadioButton.getText();
                Toast.makeText(getApplicationContext(),"Answered q"+quesNum+": "+answer[quesNum-1],Toast.LENGTH_LONG).show();

            }
        });

        option2RadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer[quesNum-1] = (String) option2RadioButton.getText();
                Toast.makeText(getApplicationContext(),"Answered q"+quesNum+": "+answer[quesNum-1],Toast.LENGTH_LONG).show();

            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quesNum==10){
                    //Toast.makeText(getApplicationContext(),"Finished assessment",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(Quiz.this,result.class);
                    Bundle bundle =new Bundle();
                    bundle.putStringArray("key",answer);
                    intent.putExtras(bundle);
                    Quiz.this.finish();
                    startActivity(intent);
                }else{
                    option1RadioButton.setChecked(false);
                    option2RadioButton.setChecked(false);
                    updateQuestion();
                }
            }
        });
    }

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


}
