package com.example.libin.fyp;

import java.util.ArrayList;

/**
 * Created by Li Bin on 3/24/2017.
 */

public class Course {
    private ArrayList<Session> sessions;

    public Course(){
        sessions=new ArrayList<>();
    }
    public Course(ArrayList<Session> sessions){
        this.sessions=sessions;
    }
    public Session getSession(int i){
        return sessions.get(i);
    }
    public void addSession(Session session){
        sessions.add(session);
    }
    public void removeSession(int index){
        sessions.remove(index);
    }
    public String view(){
        String s="";
        for (int i=0;i<sessions.size();i++){
            s+=sessions.get(i).view()+"\n";
        }
        return s;
    }
    public void clear(){
        sessions.clear();
    }
    public int getSize(){
        return sessions.size();
    }

}
