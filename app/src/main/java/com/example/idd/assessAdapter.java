package com.example.idd;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class assessAdapter extends RecyclerView.Adapter<assessAdapter.AssessViewHolder> {

    private ArrayList<assessData> mAssessList;

    public static class AssessViewHolder extends RecyclerView.ViewHolder{



        public TextView assessDate;
        public TextView assessResult;
        public TextView assessDescription;

        public AssessViewHolder(@NonNull final View itemView) {
            super(itemView);
            assessDate=(TextView)itemView.findViewById(R.id.textViewDate);
            assessResult=(TextView)itemView.findViewById(R.id.textViewResult);
            assessDescription=(TextView)itemView.findViewById(R.id.textViewDescription);

        }

    }

    public assessAdapter(ArrayList<assessData> assessList) {
        this.mAssessList=assessList;
    }

    @NonNull
    @Override
    public AssessViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.assessitem, viewGroup, false);
        AssessViewHolder holder= new AssessViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AssessViewHolder holder, int i) {
        assessData currentItem=mAssessList.get(i);
        holder.assessDate.setText(currentItem.getDate());
        holder.assessResult.setText(currentItem.getResult());
        holder.assessDescription.setText(currentItem.getDescription());


    }

    @Override
    public int getItemCount() {
        return mAssessList.size();
    }


}



