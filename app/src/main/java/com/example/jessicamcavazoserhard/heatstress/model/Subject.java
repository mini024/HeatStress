/**
 Heat Stress is an Android health app.
 Copyright (C) 2017  Heat Stress Team

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.example.jessicamcavazoserhard.heatstress.model;

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
