package com.example.idd;

public class assessData {
    private String date,result,description;
    public assessData(String d, String r,String des){
        date=d;
        result=r;
        description=des;
    }

    public String getDate(){
        return date;
    }

    public String getResult(){
        return result;
    }

    public String getDescription(){
        return description;
    }
}

