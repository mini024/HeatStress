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
        String[] sTitles = new String[3];
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
        Intent i = new Intent(this, DetailActivity.class);
        i.putExtra("subject", s);
        startActivity(i);
    }
}
