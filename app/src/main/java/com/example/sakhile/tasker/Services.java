package com.example.sakhile.tasker;

import java.io.Serializable;

public class Services implements Serializable{

    public String name;
    public String location;
    public String contact;

    public Services(String name, String location, String contact){

        this.name= name;
        this.location= location;
        this.contact= contact;

    }




}
