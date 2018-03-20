package com.example.libin.fyp;

/**
 * Created by Li Bin on 3/24/2017.
 */

public class Session {

    private String day;
    private String name ;

    private String place ;
    private String startTime;
    private String endTime;



    public Session(){

    }

    public Session(String day) {
        this.day = day;
    }

    public Session(String day, String name, String place, String startTime, String endTime){
        this.day=day;
        this.name=name;
        this.place=place;
        this.startTime=startTime;
        this.endTime=endTime;
    }
    public String getDay() {
        return day;
    }

    public String getName() {
        return name;
    }

    public String getPlace() {
        return place;
    }


    public String view(){
        return day+"\n"+name+"\n"+place+"\n"+startTime+" - "+endTime;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlace(String place) {
        this.place = place;
    }



    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
