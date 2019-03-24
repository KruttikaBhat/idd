package com.example.idd;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class assessmentsAdapter extends ArrayAdapter<assessments> {
    public assessmentsAdapter(OnCompleteListener<QuerySnapshot> context, List<assessments> object){
        super((Context) context,0, object);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView =  ((Activity)getContext()).getLayoutInflater().inflate(R.layout.assessitem,parent,false);
        }

        TextView resultTextView = (TextView) convertView.findViewById(R.id.assess_result);
        TextView dateTextView = (TextView) convertView.findViewById(R.id.assess_date);

        assessments assess = getItem(position);

        resultTextView.setText(assess.getResult());
        dateTextView.setText(assess.getDate());


        return convertView;
    }

}


