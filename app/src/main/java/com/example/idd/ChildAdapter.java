package com.example.idd;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ChildViewHolder> {

    private ArrayList<ChildItem> mChildList;
    private OnChildClickListener mListener;

    public interface OnChildClickListener{
        void onChildClick(int position);
    }

    public void setOnChildCLickListener(OnChildClickListener listener){
        mListener=listener;
    }

    public static class ChildViewHolder extends RecyclerView.ViewHolder{

        public TextView childName,childClass,childAge,childIndex;

        public ChildViewHolder(@NonNull final View itemView, final OnChildClickListener listener) {
            super(itemView);
            childName=itemView.findViewById(R.id.textViewChildName);
            childClass=itemView.findViewById(R.id.textViewChildClass);
            childAge=itemView.findViewById(R.id.textViewChildAge);
            childIndex=itemView.findViewById(R.id.textViewChildIndex);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onChildClick(position);
                        }
                        //Snackbar snackbar=Snackbar.make(itemView,"you clicked "+String.valueOf(position),Snackbar.LENGTH_SHORT);
                        //snackbar.show();


                    }
                    String index=childIndex.getText().toString();

                    Snackbar snackbar=Snackbar.make(itemView,"you clicked "+index,Snackbar.LENGTH_SHORT);
                    snackbar.show();

                    //Toast.makeText(getApplicationContext(),"Finished assessment",Toast.LENGTH_LONG).show();
                    Bundle bundle =new Bundle();
                    bundle.putString("index",index);
                    Intent intent=new Intent(v.getContext(), childProfile.class);
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);

                }
            });
        }
    }

    public ChildAdapter(ArrayList<ChildItem> childList) {
        this.mChildList=childList;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.child_item, viewGroup, false);
        ChildViewHolder holder= new ChildViewHolder(v, mListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder childViewHolder, int i) {
        ChildItem currentItem=mChildList.get(i);
        childViewHolder.childName.setText(currentItem.getChildName());
        childViewHolder.childAge.setText(currentItem.getChildAge());
        childViewHolder.childClass.setText(currentItem.getChildClass());
        childViewHolder.childIndex.setText(currentItem.getChildIndex());

    }

    @Override
    public int getItemCount() {
        return mChildList.size();
    }
}
