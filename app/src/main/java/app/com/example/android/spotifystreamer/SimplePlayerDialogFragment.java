package app.com.example.android.spotifystreamer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class SimplePlayerDialogFragment extends DialogFragment {

    List<RowItemFiveStrings> TopTracksData = new ArrayList<>();
    int NowPlayingSong = 0;
    String artistName = "";
    String pausedOrReset = "";
    MediaPlayer myMediaPlayer = new MediaPlayer();
    Handler durationHandler = new Handler();
    double timeElapsed = 0, finalTime =0;
    private OnFragmentInteractionListener mListener;

    public SimplePlayerDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_simple_player, container, false);

        TextView artistNameTextView = (TextView) rootView.findViewById(R.id.artist_name);
        TextView albumNameTextView = (TextView) rootView.findViewById(R.id.album_name);
        ImageView albumArt = (ImageView) rootView.findViewById(R.id.now_playing_thumbnail);
        TextView songNameTextView = (TextView) rootView.findViewById(R.id.track_name);
        final TextView trackTimer = (TextView) rootView.findViewById(R.id.track_start_time);
        final TextView trackDuration = (TextView) rootView.findViewById(R.id.track_end_time);
        final SeekBar trackSeekBar = (SeekBar) rootView.findViewById(R.id.track_seekBar);
        final ImageButton play_pause = (ImageButton) rootView.findViewById(R.id.play_pause);
        final ImageButton next_track = (ImageButton) rootView.findViewById(R.id.next_track);
        final ImageButton previous_track = (ImageButton) rootView.findViewById(R.id.previous_track);

        //Intent intent = getActivity().getIntent();

        //original code
/*        if (intent != null) //&& intent.hasExtra(Intent.EXTRA_TEXT)) {
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
            startPlayer(TopTracksData.get(NowPlayingSong).gettextColumn4(), trackSeekBar, trackTimer, trackDuration);
        }*/

        Bundle arguments = getArguments();
        if (arguments != null) {

            TopTracksData = arguments.getParcelableArrayList("TopTracksData");
            NowPlayingSong = arguments.getInt("SelectedSong", 0);
            artistName = arguments.getString("ArtistName");

            artistNameTextView.setText(artistName);
            albumNameTextView.setText(TopTracksData.get(NowPlayingSong).gettextColumn1());
            songNameTextView.setText(TopTracksData.get(NowPlayingSong).gettextColumn0());
            Picasso.with(getActivity()).load(TopTracksData.get(NowPlayingSong).gettextColumn3()).into(albumArt);
            Log.d("Top track data", "-----------> " + TopTracksData);

            //((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(artistName + "  (" +countryPref+")");
            startPlayer(TopTracksData.get(NowPlayingSong).gettextColumn4(), trackSeekBar, trackTimer, trackDuration);


        }



        play_pause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (myMediaPlayer.isPlaying()) {  // pause button clicked
                    myMediaPlayer.pause();
                    //timer.setBase(myMediaPlayer.getCurrentPosition());
                    pausedOrReset = "";
                    play_pause.setImageResource(android.R.drawable.ic_media_play);
                } else {
                    if (pausedOrReset != "reset")  // play button clicked from pause
                    {
                        myMediaPlayer.start();

                    } else {                          // next or previous track button was clicked
                        myMediaPlayer.reset();
                        durationHandler.removeCallbacksAndMessages(null);
                        startPlayer(TopTracksData.get(NowPlayingSong).gettextColumn4(), trackSeekBar, trackTimer, trackDuration);

                    }
                    play_pause.setImageResource(android.R.drawable.ic_media_pause);
                }
            }
        });


        next_track.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myMediaPlayer.stop();
                myMediaPlayer.reset();
                durationHandler.removeCallbacksAndMessages(null);
                pausedOrReset = "reset";
                if (NowPlayingSong == (TopTracksData.size()-1)) {
                    NowPlayingSong = 0;
                } else {
                    NowPlayingSong = NowPlayingSong + 1;
                }
                refreshViewsAndStartPlayer(rootView);
                play_pause.setImageResource(android.R.drawable.ic_media_pause);
            }
        });


        previous_track.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (NowPlayingSong > 0) {
                    if (!myMediaPlayer.isPlaying()){
                        NowPlayingSong = NowPlayingSong - 1;
                    }
                    myMediaPlayer.stop();
                    myMediaPlayer.reset();
                    durationHandler.removeCallbacksAndMessages(null);
                    pausedOrReset = "reset";
                    refreshViewsAndStartPlayer(rootView);
                    play_pause.setImageResource(android.R.drawable.ic_media_pause);
                }
                else {
                    myMediaPlayer.stop();
                    myMediaPlayer.reset();
                    durationHandler.removeCallbacksAndMessages(null);
                    pausedOrReset = "reset";
                    refreshViewsAndStartPlayer(rootView);
                    play_pause.setImageResource(android.R.drawable.ic_media_pause);
                }
            }
        });

        myMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {

                myMediaPlayer.stop();
                myMediaPlayer.reset();
                durationHandler.removeCallbacksAndMessages(null);
                pausedOrReset = "reset";
                if (NowPlayingSong == (TopTracksData.size()-1)) {
                    NowPlayingSong = 0;
                } else {
                    NowPlayingSong = NowPlayingSong + 1;
                }
                refreshViewsAndStartPlayer(rootView);
                play_pause.setImageResource(android.R.drawable.ic_media_pause);

            }
        });

        return rootView;
    }




    private void startPlayer(String previewUrl, final SeekBar mySeekBar, final TextView trackTimer, final TextView trackDuration) {
        //Implement Service at later time
//        Intent toServiceIntent = new Intent(getActivity(), StreamerService.class);
//        toServiceIntent.putExtra(StreamerService.NOW_PLAYING_URL,previewUrl);
//        getActivity().startService(toServiceIntent);

        myMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            myMediaPlayer.setDataSource(previewUrl);
            myMediaPlayer.prepareAsync();
            //myMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        myMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mPlayer) {
                mPlayer.start();

                finalTime = myMediaPlayer.getDuration();
                trackDuration.setText(String.format("%d,%02d", TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime)));
                mySeekBar.setMax((int) finalTime);
                timeElapsed = myMediaPlayer.getCurrentPosition();
                mySeekBar.setProgress((int) timeElapsed);

                try {
                    Runnable updateSeekBarTime = new Runnable() {
                        public void run() {
                            //get current position
                            timeElapsed = myMediaPlayer.getCurrentPosition();
                            //set seekbar progress
                            mySeekBar.setProgress((int) timeElapsed);
                            trackTimer.setText(String.format("%d,%02d", TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed),
                                    TimeUnit.MILLISECONDS.toSeconds((long) timeElapsed)));
                            //repeat in 100 miliseconds
                            durationHandler.postDelayed(this, 100);
                        }
                    };
                    durationHandler.postDelayed(updateSeekBarTime, 100);
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                    return;
                }

            }
        });

        mySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    myMediaPlayer.seekTo(progress);
                }
            }
        });

    }



    private void refreshViewsAndStartPlayer(View rootView) {

        TextView artistNameTextView = (TextView) rootView.findViewById(R.id.artist_name);
        TextView albumNameTextView = (TextView) rootView.findViewById(R.id.album_name);
        ImageView albumArt = (ImageView) rootView.findViewById(R.id.now_playing_thumbnail);
        TextView songNameTextView = (TextView) rootView.findViewById(R.id.track_name);
        SeekBar trackSeekBar = (SeekBar) rootView.findViewById(R.id.track_seekBar);
        TextView trackTimer = (TextView) rootView.findViewById(R.id.track_start_time);
        TextView trackDuration = (TextView) rootView.findViewById(R.id.track_end_time);

        artistNameTextView.setText(artistName);
        albumNameTextView.setText(TopTracksData.get(NowPlayingSong).gettextColumn1());
        songNameTextView.setText(TopTracksData.get(NowPlayingSong).gettextColumn0());
        Picasso.with(getActivity()).load(TopTracksData.get(NowPlayingSong).gettextColumn3()).into(albumArt);

        trackSeekBar.setProgress(0);
        startPlayer(TopTracksData.get(NowPlayingSong).gettextColumn4(), trackSeekBar, trackTimer, trackDuration);
    }

    @Override
    public void onStop() {
        durationHandler.removeCallbacksAndMessages(null);
        if (myMediaPlayer != null) {
            myMediaPlayer.release();
            myMediaPlayer = null;
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        durationHandler.removeCallbacksAndMessages(null);
        if (myMediaPlayer != null) {
            myMediaPlayer.release();
            myMediaPlayer = null;
        }
        super.onDestroy();
    }

    @Override
    public void onPause() {
        durationHandler.removeCallbacksAndMessages(null);
        if (myMediaPlayer != null) {
            myMediaPlayer.release();
            myMediaPlayer = null;
        }
        super.onPause();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
