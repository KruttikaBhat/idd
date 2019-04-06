package com.example.idd;

import android.content.Intent;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

public class result extends AppCompatActivity {

    private TextView answersTextView,resultTextView,graphTextView,desTextView,sourceTextView;
    private Button homeButton,contactButton,moreButton;
    private String print;

    private static final String root="users";
    private static final String key_result="result";
    private static final String key_date="date";
    private static final String childcollection="children";
    private static final String key_assess="assessments";
    private static final String key_numassess="numberOfAssessments";
    private static final String key_des="description";
    private static final String key_input="input";


    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;

    private String index,describe=null,className=null;
    private String[] answer=new String[14];
    private String[] questions=new String[14];

    private double[] features= new double[14];

    private Classifier mClassifier=null;

    private J48 clsfr=null;

    private Random mRandom = new Random();

    private TextView q[]=new TextView[14];
    private TextView a[]=new TextView[14];

    /*private int qid[] = {R.id.ques1, R.id.ques2,R.id.ques3,R.id.ques4,R.id.ques5,R.id.ques6,R.id.ques7,
            R.id.ques8,R.id.ques9,R.id.ques10,R.id.ques11,R.id.ques12,R.id.ques13,R.id.ques14};
    private int aid[] = {R.id.ans1, R.id.ans2,R.id.ans3,R.id.ans4,R.id.ans5,R.id.ans6,R.id.ans7,
            R.id.ans8,R.id.ans9,R.id.ans10,R.id.ans11,R.id.ans12,R.id.ans13,R.id.ans14};
*/


    private Sample[] mSamples = new Sample[]{
            //new Sample(1, 0, new double[]{5, 3.5, 2, 0.4}), // should be in the setosa domain
            new Sample(2, 1, new double[]{0,1,1,1,0,0,0,0,0,0,1,0,1,1}), // should be in the versicolor domain
            //new Sample(3, 2, new double[]{7, 3, 6.8, 2.1}) // should be in the virginica domain
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);



        answersTextView=(TextView)findViewById(R.id.answers);
        //desTextView=(TextView)findViewById(R.id.des);
        //graphTextView=(TextView)findViewById(R.id.graph);
        //sourceTextView=(TextView)findViewById(R.id.source);
        resultTextView=(TextView)findViewById(R.id.resultTextView);
        homeButton=(Button)findViewById(R.id.homeButton);
        contactButton=(Button)findViewById(R.id.contactSpecialist);
        moreButton=(Button)findViewById(R.id.MoreOnLD);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),home.class);
                v.getContext().startActivity(intent);
            }
        });



        Bundle bundle=getIntent().getExtras();
        answer=bundle.getStringArray("key");
        questions=bundle.getStringArray("ques");
        index=bundle.getString("index");


        for(int i=0;i<14;i++){
            //q[i]=(TextView)findViewById(qid[i]);
            //a[i]=(TextView)findViewById(aid[i]);
            //q[i].setText("Q"+String.valueOf(i+1)+": "+questions[i]);
            //a[i].setText("A:"+answer[i]);

            if(answer[i].equals("yes"))
                features[i]=0;
            else
                features[i]=1;


        }

        predictLD();


        switch(answer[6]){
            case "yes":
                switch(answer[3]){
                    case "yes":
                        switch(answer[9]){
                            case "yes":
                                switch(answer[2]){
                                    case "yes":
                                        describe="The result is dyslexia because you answered yes to \""
                                                +questions[2]+"\" (indicates deficits in social-emotional reciprocity)\n\""
                                                +questions[3]+"\" (indicates deficits in developing and maintaining relationships)\n\""
                                                +questions[6]+"\" (indicates highly restricted, fixated interest which are abnormal in intensity)\nand \""
                                                +questions[9]+"\" (has trouble in reading/spelling)";
                                        break;
                                    case "no":
                                        describe="The result is autistic features because you answered yes to \""
                                                +questions[3]+"\" (indicates deficits in developing and maintaining relationships)\n\""
                                                +questions[6]+"\" (indicates highly restricted, fixated interest which are abnormal in intensity)\n\""
                                                +questions[9]+"\" (has trouble in reading/spelling)\nand no to \""
                                                +questions[2]+"\" (indicates deficits in social-emotional reciprocity)";

                                        break;
                                }

                                break;
                            case "no":
                                describe="The result is autistic features because you answered yes to \""
                                        +questions[3]+"\" (indicates deficits in developing and maintaining relationships)\n\""
                                        +questions[6]+"\" (indicates highly restricted, fixated interest which are abnormal in intensity)\nand no to \""
                                        +questions[9]+"\" (has trouble in reading/spelling)";
                                break;
                        }
                        break;
                    case "no":
                        switch(answer[2]){
                            case "yes":
                                describe="The result is autistic features because you answered yes to \""
                                        +questions[6]+"\" (indicates highly restricted, fixated interest which are abnormal in intensity)\n\""
                                        +questions[2]+"\" (indicates deficits in social-emotional reciprocity)\"\n and no to \""
                                        +questions[3]+"\" (indicates deficits in developing and maintaining relationships)\n\"";
                                break;
                            case "no":
                                switch(answer[7]){
                                    case "yes":
                                        switch(answer[1]){
                                            case "yes":
                                                describe="The result is autistic features because you answered yes to \""
                                                        +questions[6]+"\" (indicates highly restricted, fixated interest which are abnormal in intensity)\n\""
                                                        +questions[7]+"\" (indicates hyper/hypo reactivity to sensory input)\n\""
                                                        +questions[1]+"\" (indicates deficits in non verbal communicative behaviors) \nand no to \""
                                                        +questions[3]+"\" (indicates deficits in developing and maintaining relationships)\n\""
                                                        +questions[2]+"\" (indicates deficits in social-emotional reciprocity)\"";

                                                break;
                                            case "no":
                                                describe="The result is autism because you answered yes to \""
                                                        +questions[6]+"\" (indicates highly restricted, fixated interest which are abnormal in intensity)\n\""
                                                        +questions[7]+"\" (indicates hyper/hypo reactivity to sensory input)\n and no to \""
                                                        +questions[1]+"\" (indicates deficits in non verbal communicative behaviors) \n\""
                                                        +questions[2]+"\" (indicates deficits in social-emotional reciprocity)\""
                                                        +questions[3]+"\" (indicates deficits in developing and maintaining relationships)\n\"";

                                                break;
                                        }
                                        break;
                                    case "no":
                                        describe="The result is autistic features because you answered yes to \""
                                                +questions[6]+"\" (indicates highly restricted, fixated interest which are abnormal in intensity)\n and no to\""
                                                +questions[7]+"\" (indicates hyper/hypo reactivity to sensory input)\n and no to \""
                                                +questions[2]+"\" (indicates deficits in social-emotional reciprocity)\""
                                                +questions[3]+"\" (indicates deficits in developing and maintaining relationships)\n\"";

                                        break;
                                }
                                break;
                        }
                        break;
                }

                break;
            case "no":
                switch (answer[13]){
                    case "yes":
                        switch(answer[9]){
                            case "yes":
                                switch(answer[1]){
                                    case "yes":
                                        describe="The result is normal because you answered yes to \""
                                                +questions[9]+"\" (has trouble in reading/spelling)"
                                                +questions[13]+"\" (creativity)\n\""
                                                +questions[1]+"\" (deficits in social-emotional reciprocity)\n and no to\""
                                                +questions[6]+"\" (indicates highly restricted, fixated interest which are abnormal in intensity)\nand \"";
                                        break;
                                    case "no":
                                        describe="The result is autistic-features because you answered yes to \""
                                                +questions[9]+"\" (has trouble in reading/spelling)"
                                                +questions[13]+"\" (creativity)\nand no to\""
                                                +questions[1]+"\" (deficits in social-emotional reciprocity)\n\""
                                                +questions[6]+"\" (indicates highly restricted, fixated interest which are abnormal in intensity)\nand \"";
                                        break;

                                }
                                break;
                            case "no":
                                describe="The result is normal because you answered yes to \""
                                        +questions[13]+"\" (creativity)\n and no to \""
                                        +questions[9]+"\" (has trouble in reading/spelling)\n\""
                                        +questions[6]+"\" (indicates highly restricted, fixated interest which are abnormal in intensity)";
                                break;
                        }
                        break;
                    case "no":
                        describe="The result is autistic-features because you answered no to \""
                                +questions[13]+"\" (creativity)\n and no to \""
                                +questions[6]+"\" (indicates highly restricted, fixated interest which are abnormal in intensity)";
                        break;
                }
                break;
        }



        answersTextView.setText(describe);




    }

    private String toString(double[] features) {
        String string="";
        for(int i=0;i<14;i++)
            string+=features[i]+" ";
        return string;
    }


    public void predictLD(){

        //String className = null;
        AssetManager assetManager = getAssets();
        try {
            //mClassifier = (Classifier) weka.core.SerializationHelper.read(assetManager.open("dataset.model"));
            clsfr= (J48) weka.core.SerializationHelper.read(assetManager.open("dataset1.model"));
            //Toast.makeText(this, "Model loaded.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            return ;
        } catch (Exception e) {
            // Weka "catch'em all!"
            e.printStackTrace();
            return ;
        }
        //Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();




        final Attribute attribute1 = new Attribute("social");
        final Attribute attribute2 = new Attribute("nonverbal");
        final Attribute attribute3 = new Attribute("social_emotional");
        final Attribute attribute4 = new Attribute("relationships");
        final Attribute attribute5 = new Attribute("speech_motor");
        final Attribute attribute6 = new Attribute("routine");
        final Attribute attribute7 = new Attribute("interests");
        final Attribute attribute8 = new Attribute("sensory_input");
        final Attribute attribute9 = new Attribute("reading_1");
        final Attribute attribute10 = new Attribute("reading_2");
        final Attribute attribute11 = new Attribute("writing");
        final Attribute attribute12 = new Attribute("other_1");
        final Attribute attribute13 = new Attribute("other_2");
        final Attribute attribute14 = new Attribute("strengths");
        final List<String> classes = new ArrayList<String>() {
            {
                add("autism"); // cls nr 1
                add("autistic-features"); // cls nr 2
                add("dyslexia"); // cls nr 3
                add("normal");
            }
        };

        ArrayList<Attribute> attributeList = new ArrayList<Attribute>(2) {
            {
                add(attribute1);
                add(attribute2);
                add(attribute3);
                add(attribute4);
                add(attribute5);
                add(attribute6);
                add(attribute7);
                add(attribute8);
                add(attribute9);
                add(attribute10);
                add(attribute11);
                add(attribute12);
                add(attribute13);
                add(attribute14);
                Attribute attributeClass = new Attribute("@@class@@", classes);
                add(attributeClass);
            }
        };





        // unpredicted data sets (reference to sample structure for new instances)
        Instances dataUnpredicted = new Instances("TestInstances",
                attributeList, 1);
        // last feature is target variable
        dataUnpredicted.setClassIndex(dataUnpredicted.numAttributes() - 1);

        // create new instance: this one should fall into the setosa domain
        final Sample s = mSamples[0];
        DenseInstance newInstance = new DenseInstance(dataUnpredicted.numAttributes()) {
            {
                setValue(attribute1, features[0]);
                setValue(attribute2, features[1]);
                setValue(attribute3, features[2]);
                setValue(attribute4, features[3]);
                setValue(attribute5, features[4]);
                setValue(attribute6, features[5]);
                setValue(attribute7, features[6]);
                setValue(attribute8, features[7]);
                setValue(attribute9, features[8]);
                setValue(attribute10, features[9]);
                setValue(attribute11, features[10]);
                setValue(attribute12, features[11]);
                setValue(attribute13, features[12]);
                setValue(attribute14, features[13]);
            }
        };
        // reference to dataset
        newInstance.setDataset(dataUnpredicted);

        try {
            //double result = mClassifier.classifyInstance(newInstance);
            double result = clsfr.classifyInstance(newInstance);
            className = classes.get(new Double(result).intValue());
            //String msg = "Nr: " + s.nr + ", predicted: " + className + ", actual: " + classes.get(s.label);
            String source=clsfr.toSource(className);
            String des=clsfr.toString();
            String graph=clsfr.graph();
            //Log.d(WEKA_TEST, msg);
            //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            resultTextView.setText(className);
            //sourceTextView.setText(source);
            //graphTextView.setText(graph);
            //desTextView.setText(des);

            //store in database

            firebaseAuth= FirebaseAuth.getInstance();
            final FirebaseUser user=firebaseAuth.getCurrentUser();

            if(user!=null && className!=null){
                final String email=user.getEmail();
                if(email!=null) {
                    //Toast.makeText(home.this, email, Toast.LENGTH_SHORT).show();
                    db.collection(root).document(email).collection(childcollection).document(index).get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    String number=documentSnapshot.getString(key_numassess);
                                    Integer newnumInt = (Integer.valueOf(number))+1;
                                    String newnumString=String.valueOf(newnumInt);
                                    Calendar calendar=Calendar.getInstance();
                                    String currentDate= DateFormat.getDateInstance(DateFormat.LONG).format(calendar.getTime());
                                    final Map<String, Object> child=new HashMap<>();
                                    child.put("index",newnumString);
                                    child.put(key_date,currentDate);
                                    child.put(key_result,className);
                                    child.put(key_input,getInput());
                                    child.put(key_des,describe);
                                    db.collection(root).document(email).collection(childcollection).document(index).update(key_numassess,newnumString);
                                    db.collection(root).document(email).collection(childcollection).document(index).collection(key_assess).document(newnumString).set(child)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    //Toast.makeText(Signup.this,"New user created",Toast.LENGTH_LONG).show();
                                                    Toast.makeText(result.this,"Updated result in database",Toast.LENGTH_LONG).show();
                                                    //Intent intent=new Intent(getActivity(),Quiz.class);
                                                    //startActivity(intent);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(result.this,"Error in registration",Toast.LENGTH_LONG).show();
                                                    Log.d("result",e.toString());
                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(result.this, "Child document not found", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else{
                    Toast.makeText(result.this, "Email can't be found", Toast.LENGTH_SHORT).show();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }




    }

    public String getInput(){
        String input="";
        for(int i=0;i<14;i++){
            input=input+"Q"+String.valueOf(i+1)+": "+questions[i]+"(Answered:"+answer[i]+")\n";
        }
        return input;
    }
    public class Sample {
        public int nr;
        public int label;
        public double [] features;

        public Sample(int _nr, int _label, double[] _features) {
            this.nr = _nr;
            this.label = _label;
            this.features = _features;
        }

        @Override
        public String toString() {
            return "Nr " +
                    nr +
                    ", cls " + label +
                    ", feat: " + Arrays.toString(features);
        }
    }


}