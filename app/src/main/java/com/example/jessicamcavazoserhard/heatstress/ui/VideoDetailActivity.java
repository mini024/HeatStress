package com.example.jessicamcavazoserhard.heatstress.ui;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


import com.example.jessicamcavazoserhard.heatstress.R;
import com.example.jessicamcavazoserhard.heatstress.model.Subject;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.ErrorReason;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;


public class VideoDetailActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    public static final String API_KEY ="AIzaSyAucJPM1P00vwl0jDK6f_sjo1izJ2dpfmI";
    public String Video_ID; //hdeFZ6nt9tc LUAWtmQO31k krkJfHO6vs8

    TextView tvtitle;
    TextView tvdescription;
    Subject s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);

        tvtitle = (TextView) findViewById(R.id.textView_title);
        tvdescription = (TextView) findViewById(R.id.textView_description);

        if (getIntent().getExtras() != null) {

            Bundle b = getIntent().getExtras();
            s = (Subject) getIntent().getSerializableExtra("subject");
            Video_ID = b.getString("Video");
            tvtitle.setText(s.getTitle());
            tvdescription.setText(s.getsDescription());
        }

        YouTubePlayerView youtubeplayer = (YouTubePlayerView) findViewById(R.id.youtube_player);
        youtubeplayer.initialize(API_KEY,this);

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult result){

        Toast.makeText(this.getApplicationContext(), "Failure to initialize!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored){

        player.setPlayerStateChangeListener(playerStateChangeListener);
        player.setPlaybackEventListener(playbackEventListener);

        if (!wasRestored){
            player.cueVideo(Video_ID);
        }

    }


   private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {
       @Override
       public void onPlaying() {

       }

       @Override
       public void onPaused() {

       }

       @Override
       public void onStopped() {

       }

       @Override
       public void onBuffering(boolean b) {

       }

       @Override
       public void onSeekTo(int i) {

       }
   };

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onLoading() {

        }

        @Override
        public void onLoaded(String s) {

        }

        @Override
        public void onAdStarted() {

        }

        @Override
        public void onVideoStarted() {

        }

        @Override
        public void onVideoEnded() {

        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {

        }
    };

}
