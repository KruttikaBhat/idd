package com.example.idd;

public class ChildItem {
    private String childName,childAge,childClass;

    public ChildItem(String cName,String cAge,String cClass){
        childName=cName;
        childAge=cAge;
        childClass=cClass;
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
}
