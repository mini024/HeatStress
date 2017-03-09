package com.example.jessicamcavazoserhard.heatstress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    TextView tvtitle;
    TextView tvdescription;
    Subject s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvtitle = (TextView) findViewById(R.id.textView_title);
        tvdescription = (TextView) findViewById(R.id.textView_description);

        if (getIntent().getExtras() != null) {
            s = (Subject) getIntent().getSerializableExtra("subject");

            tvtitle.setText(s.getTitle());
            tvdescription.setText(s.getsDescription());
        }
    }

}
