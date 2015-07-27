package app.com.example.android.spotifystreamer;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class SimplePlayerActivityFragment extends Fragment {

    List<RowItemFiveStrings> TopTracksData = new ArrayList<>();
    int NowPlayingSong = 0;
    String artistName = "";
    String pausedOrReset = "";
    MediaPlayer myMediaPlayer = new MediaPlayer();

    public SimplePlayerActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_simple_player, container, false);

        final View rootView = inflater.inflate(R.layout.fragment_simple_player, container, false);

        TextView artistNameTextView = (TextView) rootView.findViewById(R.id.artist_name);
        TextView albumNameTextView = (TextView) rootView.findViewById(R.id.album_name);
        ImageView albumArt = (ImageView) rootView.findViewById(R.id.now_playing_thumbnail);
        TextView songNameTextView = (TextView) rootView.findViewById(R.id.track_name);
        final SeekBar trackSeekBar = (SeekBar) rootView.findViewById(R.id.track_seekBar);
        final Chronometer timer = (Chronometer) rootView.findViewById(R.id.chronometer);

        Intent intent = getActivity().getIntent();

        if (intent != null) //&& intent.hasExtra(Intent.EXTRA_TEXT)) {
        {
            TopTracksData = intent.getParcelableArrayListExtra("TopTracksData");
            NowPlayingSong = intent.getIntExtra("SelectedSong", 0);
            artistName = intent.getStringExtra("ArtistName");

            artistNameTextView.setText(artistName);
            albumNameTextView.setText(TopTracksData.get(NowPlayingSong).gettextColumn1());
            songNameTextView.setText(TopTracksData.get(NowPlayingSong).gettextColumn0());
            Picasso.with(getActivity()).load(TopTracksData.get(NowPlayingSong).gettextColumn3()).into(albumArt);
            Log.d("Top track data", "-----------> " + TopTracksData);

            //((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(artistName + "  (" +countryPref+")");
            startPlayer(TopTracksData.get(NowPlayingSong).gettextColumn4(), trackSeekBar);
            timer.start();

        }

        final ImageButton play_pause = (ImageButton) rootView.findViewById(R.id.play_pause);
        play_pause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (myMediaPlayer.isPlaying()) {
                    myMediaPlayer.pause();
                    timer.stop();
                    //timer.setBase(myMediaPlayer.getCurrentPosition());
                    pausedOrReset = "";
                    play_pause.setImageResource(android.R.drawable.ic_media_play);
                } else {
                    if (pausedOrReset != "reset")  // play button clicked
                    {
                        myMediaPlayer.start();
                        timer.setBase((SystemClock.elapsedRealtime() - timer.getBase()));
                        timer.start();
                    } else {                          // next or previous track button was clicked
                        myMediaPlayer.reset();
                        startPlayer(TopTracksData.get(NowPlayingSong).gettextColumn4(), trackSeekBar);
                        timer.setBase(SystemClock.elapsedRealtime());
                    }
                    play_pause.setImageResource(android.R.drawable.ic_media_pause);
                }
            }
        });

        final ImageButton next_track = (ImageButton) rootView.findViewById(R.id.next_track);
        next_track.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myMediaPlayer.stop();
                myMediaPlayer.reset();
                pausedOrReset = "reset";
                if (NowPlayingSong == 9) {
                    NowPlayingSong = 0;
                } else {
                    NowPlayingSong = NowPlayingSong + 1;
                }
                refreshViewsAndStartPlayer(rootView);
                play_pause.setImageResource(android.R.drawable.ic_media_pause);
            }
        });

        final ImageButton previous_track = (ImageButton) rootView.findViewById(R.id.previous_track);
        previous_track.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (NowPlayingSong > 0) {
                    myMediaPlayer.stop();
                    myMediaPlayer.reset();
                    pausedOrReset = "reset";
                    NowPlayingSong = NowPlayingSong - 1;
                    refreshViewsAndStartPlayer(rootView);
                    play_pause.setImageResource(android.R.drawable.ic_media_pause);
                }
            }
        });

//        Runnable _progressUpdater = new Runnable() {
//            @Override
//            public void run() {
//                int currentPosition = 0;
//                int total = myMediaPlayer.getDuration();
//                trackSeekBar.setMax(total);
//                while (myMediaPlayer != null && currentPosition < total) {
//                    try {
//                        Thread.sleep(1000);
//                        currentPosition = myMediaPlayer.getCurrentPosition();
//                    } catch (InterruptedException e) {
//                        return;
//                    } catch (Exception e) {
//                        return;
//                    }
//                    trackSeekBar.setProgress(currentPosition);
//                }
//            }
//        };
//
//        trackSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onStopTrackingTouch(SeekBar arg0) {
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar arg0) {
//            }
//
//            @Override
//            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
//                if (arg2) {
//                    myMediaPlayer.seekTo(arg1 * 1000);
//                }
//            }
//        });


        return rootView;

    }

    private void startPlayer(String previewUrl, final SeekBar mySeekBar) {
        //Implement Service at later time
//        Intent toServiceIntent = new Intent(getActivity(), StreamerService.class);
//        toServiceIntent.putExtra(StreamerService.NOW_PLAYING_URL,previewUrl);
//        getActivity().startService(toServiceIntent);

        final Handler mHandler = new Handler();

        myMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            myMediaPlayer.setDataSource(previewUrl);
            myMediaPlayer.prepare();
            //myMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        myMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mPlayer)
            {
                mPlayer.start();
                mySeekBar.setMax(mPlayer.getDuration());
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        while(mPlayer!=null && mPlayer.getCurrentPosition() < mPlayer.getDuration())
                        {
                            mySeekBar.setProgress(mPlayer.getCurrentPosition());
                            Message msg=new Message();
                            int millis = mPlayer.getCurrentPosition();

                            msg.obj=millis/1000;
                            mHandler.sendMessage(msg);
                            try {
                                Thread.sleep(200);
                            }
                            catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();

            }
        });
    }

    private void refreshViewsAndStartPlayer(View rootView) {

        TextView artistNameTextView = (TextView) rootView.findViewById(R.id.artist_name);
        TextView albumNameTextView = (TextView) rootView.findViewById(R.id.album_name);
        ImageView albumArt = (ImageView) rootView.findViewById(R.id.now_playing_thumbnail);
        TextView songNameTextView = (TextView) rootView.findViewById(R.id.track_name);
        Chronometer timer = (Chronometer) rootView.findViewById(R.id.chronometer);
        final SeekBar trackSeekBar = (SeekBar) rootView.findViewById(R.id.track_seekBar);

        artistNameTextView.setText(artistName);
        albumNameTextView.setText(TopTracksData.get(NowPlayingSong).gettextColumn1());
        songNameTextView.setText(TopTracksData.get(NowPlayingSong).gettextColumn0());
        Picasso.with(getActivity()).load(TopTracksData.get(NowPlayingSong).gettextColumn3()).into(albumArt);

        startPlayer(TopTracksData.get(NowPlayingSong).gettextColumn4(), trackSeekBar);
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
    }

    private void refreshViews(View rootView) {

        TextView artistNameTextView = (TextView) rootView.findViewById(R.id.artist_name);
        TextView albumNameTextView = (TextView) rootView.findViewById(R.id.album_name);
        ImageView albumArt = (ImageView) rootView.findViewById(R.id.now_playing_thumbnail);
        TextView songNameTextView = (TextView) rootView.findViewById(R.id.track_name);
        Chronometer timer = (Chronometer) rootView.findViewById(R.id.chronometer);

        artistNameTextView.setText(artistName);
        albumNameTextView.setText(TopTracksData.get(NowPlayingSong).gettextColumn1());
        songNameTextView.setText(TopTracksData.get(NowPlayingSong).gettextColumn0());
        Picasso.with(getActivity()).load(TopTracksData.get(NowPlayingSong).gettextColumn3()).into(albumArt);

        timer.setBase(SystemClock.elapsedRealtime());

    }

    @Override
    public void onStop() {
        super.onStop();
        myMediaPlayer.release();
        myMediaPlayer = null;
    }

}
