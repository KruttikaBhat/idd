package com.example.idd;

public class assessments {
    private String result;
    private String date;


    public assessments() {

    }

    public assessments(String result, String date) {
        this.result=result;
        this.date = date;

    }

    public String getResult(){
        return result;
    }
    public String getDate(){
        return date;
    }


}
