package com.example.jessicamcavazoserhard.heatstress.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.jessicamcavazoserhard.heatstress.R;
import com.example.jessicamcavazoserhard.heatstress.model.GifImageView;
import com.example.jessicamcavazoserhard.heatstress.model.Subject;

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

        GifImageView gifImageView = (GifImageView) findViewById(R.id.GifImageView);
        gifImageView.setGifImageResource(R.drawable.cpr);
        //gifImageView.setGifImageUri(Uri);

        if (getIntent().getExtras() != null) {
            s = (Subject) getIntent().getSerializableExtra("subject");

            tvtitle.setText(s.getTitle());
            tvdescription.setText(s.getsDescription());
        }
    }

}
