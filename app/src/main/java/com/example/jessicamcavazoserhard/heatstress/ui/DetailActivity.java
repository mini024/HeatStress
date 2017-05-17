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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.jessicamcavazoserhard.heatstress.BlurBuilder;
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

        View FilterView = findViewById(R.id.activity_detail);

        //Blurry background
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.background);

        Bitmap blurredBitmap = BlurBuilder.blur( DetailActivity.this, icon);

        FilterView.setBackgroundDrawable( new BitmapDrawable( getResources(), blurredBitmap ) );
    }

}
