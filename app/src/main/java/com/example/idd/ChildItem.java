package com.example.idd;

public class ChildItem {
    private String childName,childAge,childClass,childIndex;

    public ChildItem(){

    }

    public ChildItem(String cName,String cAge,String cClass,String cIndex){
        this.childName=cName;
        this.childAge=cAge;
        this.childClass=cClass;
        this.childIndex=cIndex;
    }

    public String getChildName(){
        return childName;
    }
    public String getChildAge(){
        return childAge;
    }
    public String getChildClass(){
        return childClass;
    }
    public String getChildIndex(){
        return childIndex;
    }
}
