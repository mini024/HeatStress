package com.example.jessicamcavazoserhard.heatstress;

import java.io.Serializable;

/**
 * Created by jessicamcavazoserhard on 3/8/17.
 */

public class Subject implements Serializable {

    private String sTitle;
    private String sDescription;

    //Add Video or Images

    public Subject(String sTitle, String sDescription){
        this.sTitle = sTitle;
        this.sDescription = sDescription;
    }

    public String getTitle(){
        return sTitle;
    }

    public void setTitle(String sTitle){
        this.sTitle = sTitle;
    }

    public String getsDescription(){
        return  sDescription;
    }

    public void setsDescription(String sDesc){
        sDescription = sDesc;
    }
}
