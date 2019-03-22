package com.example.idd;

import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

public class result extends AppCompatActivity {

    private TextView answersTextView,resultTextView;
    private Button homeButton;
    private String print;

    private double[] features= new double[14];

    private Classifier mClassifier=null;

    private Random mRandom = new Random();

    /*private Sample[] mSamples = new Sample[]{
            new Sample(1, 0, new double[]{0, 0, 0, 0, 1, 0, 0, 0, 0, 0}), // should be in the setosa domain
            //new Sample(2, 1, new double[]{0, 0, 0, 0, 1, 0, 1, 1, 1, 0}), // should be in the versicolor domain
            //new Sample(3, 2, new double[]{7, 3, 6.8, 2.1}) // should be in the virginica domain
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        answersTextView=(TextView)findViewById(R.id.answers);
        resultTextView=(TextView)findViewById(R.id.resultTextView);
        homeButton=(Button)findViewById(R.id.homeButton);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),home.class);
                v.getContext().startActivity(intent);
            }
        });

        Bundle bundle=getIntent().getExtras();
        String answer[]=bundle.getStringArray("key");


        for(int i=0;i<14;i++){
            if(answer[i].equals("yes"))
                features[i]=0;
            else
                features[i]=1;
        }


        //answersTextView.setText(toString(features));

        predictLD();



    }

    private String toString(double[] features) {
        String string="";
        for(int i=0;i<14;i++)
            string+=features[i]+" ";
        return string;
    }


    public void predictLD(){
        AssetManager assetManager = getAssets();
        try {
            mClassifier = (Classifier) weka.core.SerializationHelper.read(assetManager.open("temp.model"));
            //Toast.makeText(this, "Model loaded.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } catch (Exception e) {
            // Weka "catch'em all!"
            e.printStackTrace();
            return;
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
                add("normal"); // cls nr 2
                add("dyslexia"); // cls nr 3
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

        Instances dataUnpredicted = new Instances("TestInstances",attributeList, 1);
        // last feature is target variable
        dataUnpredicted.setClassIndex(dataUnpredicted.numAttributes() - 1);

        // create new instance: this one should fall into the setosa domain

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
            double result = mClassifier.classifyInstance(newInstance);
            String className = classes.get(new Double(result).intValue());
            //String msg = "Nr: " + s.nr + ", predicted: " + className + ", actual: " + classes.get(s.label);
            //Log.d(WEKA_TEST, msg);
            //Toast.makeText(this, className, Toast.LENGTH_SHORT).show();
            resultTextView.setText(className);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }





}
