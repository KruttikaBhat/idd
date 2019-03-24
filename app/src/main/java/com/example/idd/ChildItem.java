package com.example.idd;

public class ChildItem {
    private String childName,childAge,childClass,childIndex;

    public ChildItem(String cName,String cAge,String cClass,String cIndex){
        childName=cName;
        childAge=cAge;
        childClass=cClass;
        childIndex=cIndex;
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
