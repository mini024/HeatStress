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

package com.example.jessicamcavazoserhard.heatstress.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.jessicamcavazoserhard.heatstress.R;
import com.example.jessicamcavazoserhard.heatstress.model.Subject;

import java.util.ArrayList;

public class MoreInfoActivity extends ListActivity implements AdapterView.OnItemClickListener {

    ArrayList<Subject> sSubjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

        sSubjects = getDataForListView();

        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, getTitles());

        setListAdapter(subjectAdapter);
        getListView().setOnItemClickListener(this);
    }

    //Getting information to populate list
    public ArrayList<Subject> getDataForListView(){
        Subject s;

        ArrayList<Subject> listSubjects = new ArrayList<>();
        s = new Subject("Heatstroke symptoms and treatment", "Look for the following symptoms: \n" +
                "Confusion\n" +
                "Fainting\n" +
                "Seizures\n" +
                "Excessive sweating or red, hot, dry skin\n" +
                "Very high body temperature \n" +"\n" +

                "First Aid \n" +
                "Call 911 \n" + "\n" +

                "While waiting for help: \n" +

                "Place worker in shady, cool area \n"+
                "Loosen clothing, remove outer clothing \n"+
                "Fan air on worker; cold packs in armpits \n"+
                "Wet worker with cool water; apply ice packs, cool compresses, or ice if available\n"+
                "Provide fluids (preferably water) as soon as possible \n"+
                "Stay with worker until help arrives");
        listSubjects.add(s);
        s = new Subject(  "CPR",
                "When to use\n  CPR can help a person who has stopped breathing, and whose heart may have stopped beating." + "Someone can suffer that from:\n" +
                        "- heart attacks\n" +
                        "- strokes (when the blood flow to a part of the brain suddenly stops)\n" +
                        "- choking on something that blocks the entire airway\n" +
                        "- a bad neck, head, or back injury\n" +
                        "- severe electrical shocks (like from touching a power line)\n" +
                        "- serious infection\n" +
                        "- too much bleeding\n" +
                        "- allergic reactions\n" +
                        "- swallowing a drug or chemical\n\n " +
                        "What to do?\n The person giving CPR — called a rescuer — follows 3 main steps, which are known as C-A-B:\n" +
                        "\n" +
                        "C: do chest compressions\n" +
                        "A: check the airway\n" +
                        "B: do rescue breathing");
        listSubjects.add(s);
        s = new Subject(  "Hemilich Tecnique",
                "Hemlich Description goes here");
        listSubjects.add(s);
        s = new Subject(  "Dehidration",
                "Dehidration Description goes here");
        listSubjects.add(s);

        return listSubjects;
    }

    public String[] getTitles(){
        String[] sTitles = new String[4];
        for (int i = 0; i<sSubjects.size(); i++){
            String title =  sSubjects.get(i).getTitle();
            sTitles[i] = title;
        }
        return sTitles;
    }


    //On item Click - Sending information to DetailView
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Subject s = (Subject) sSubjects.get(position);

        if (position == 0){
            Intent i = new Intent(this, VideoDetailActivity.class);
            i.putExtra("subject", s);
            startActivity(i);
        }
        else {
            Intent i = new Intent(this, DetailActivity.class);
            i.putExtra("subject", s);
            startActivity(i);
        }
    }
}
