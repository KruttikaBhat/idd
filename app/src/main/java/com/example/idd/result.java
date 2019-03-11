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

    private double[] features= new double[10];

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


        for(int i=0;i<10;i++){
            if(answer[i].equals("yes"))
                features[i]=0;
            else
                features[i]=1;
        }

        if(answer[2].equals("2 or 3"))
            features[2]=0;
        else
            features[2]=1;
        if(answer[5].equals("0 to 2"))
            features[5]=0;
        else
            features[5]=1;

        if(answer[8].equals("found"))
            features[8]=0;
        else
            features[8]=1;

        answersTextView.setText(toString(features));
        //Toast.makeText(this, toString(features), Toast.LENGTH_SHORT).show();

        predictLD();



    }

    private String toString(double[] features) {
        String string="";
        for(int i=0;i<10;i++)
            string+=features[i]+" ";
        return string;
    }


    public void predictLD(){
        AssetManager assetManager = getAssets();
        try {
            mClassifier = (Classifier) weka.core.SerializationHelper.read(assetManager.open("data.model"));
            Toast.makeText(this, "Model loaded.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } catch (Exception e) {
            // Weka "catch'em all!"
            e.printStackTrace();
            return;
        }
        //Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();


        final Attribute attribute1 = new Attribute("identification");
        final Attribute attribute2 = new Attribute("imitation");
        final Attribute attribute3 = new Attribute("tasks");
        final Attribute attribute4 = new Attribute("memory");
        final Attribute attribute5 = new Attribute("responsive");
        final Attribute attribute6 = new Attribute("patience");
        final Attribute attribute7 = new Attribute("energy");
        final Attribute attribute8 = new Attribute("social");
        final Attribute attribute9 = new Attribute("attention");
        final Attribute attribute10 = new Attribute("lose");
        final List<String> classes = new ArrayList<String>() {
            {
                add("no-LD"); // cls nr 1
                add("mild"); // cls nr 2
                add("has-LD"); // cls nr 3
                add("Hyperactivity"); //cls nr 4
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
