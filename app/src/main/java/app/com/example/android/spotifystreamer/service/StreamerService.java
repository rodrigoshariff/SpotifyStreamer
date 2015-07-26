package app.com.example.android.spotifystreamer.service;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by rodrigoshariff on 7/25/2015.
 */
public class StreamerService extends IntentService {

    public static final String NOW_PLAYING_URL = "someUrl";

    public StreamerService() {
        super("StreamerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String previewUrl = intent.getStringExtra(NOW_PLAYING_URL);

//        MediaPlayer myMediaPlayer = new MediaPlayer();
//        myMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        try {
//            myMediaPlayer.setDataSource(previewUrl);
//            myMediaPlayer.prepare();//Async();
//            myMediaPlayer.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        myMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
//            @Override
//        public void onPrepared(MediaPlayer player) {
//            player.start();
//        }
//    });

    }
}
