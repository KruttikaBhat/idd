package com.example.idd;

public class assessData {
    private String date,result,description;

    public assessData(){

    }

    public assessData(String d, String r,String des){
        this.date=d;
        this.result=r;
        this.description=des;
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

