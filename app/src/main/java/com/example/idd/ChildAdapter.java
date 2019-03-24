package com.example.idd;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ChildViewHolder> {

    private ArrayList<ChildItem> mChildList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemCLickListener(OnItemClickListener listener){
        mListener=listener;
    }

    public static class ChildViewHolder extends RecyclerView.ViewHolder{

        public TextView childName,childClass,childAge;

        public ChildViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            childName=itemView.findViewById(R.id.textViewChildName);
            childClass=itemView.findViewById(R.id.textViewChildAge);
            childAge=itemView.findViewById(R.id.textViewChildClass);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }

                }
            });
        }
    }

    public ChildAdapter(ArrayList<ChildItem> childList) {
        mChildList=childList;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.child_item, viewGroup, false);
        ChildViewHolder cvh= new ChildViewHolder(v, mListener);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder childViewHolder, int i) {
        ChildItem currentItem=mChildList.get(i);
        childViewHolder.childName.setText(currentItem.getChildName());
        childViewHolder.childAge.setText(currentItem.getChildAge());
        childViewHolder.childClass.setText(currentItem.getChildClass());

    }

    @Override
    public int getItemCount() {
        return mChildList.size();
    }
}
