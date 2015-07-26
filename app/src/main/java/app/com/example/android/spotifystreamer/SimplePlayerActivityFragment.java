package app.com.example.android.spotifystreamer;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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
            startPlayer(TopTracksData.get(NowPlayingSong).gettextColumn4());

        }

        final ImageButton play_pause = (ImageButton) rootView.findViewById(R.id.play_pause);
        play_pause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (myMediaPlayer.isPlaying()) {
                    myMediaPlayer.pause();
                    pausedOrReset = "";
                    play_pause.setImageResource(android.R.drawable.ic_media_play);
                } else {
                    if (pausedOrReset != "reset")
                    {
                        myMediaPlayer.start();
                    }
                    else {
                        myMediaPlayer.reset();
                        startPlayer(TopTracksData.get(NowPlayingSong).gettextColumn4());
                    }
                    play_pause.setImageResource(android.R.drawable.ic_media_pause);
                }
            }
        });

        final ImageButton next_track = (ImageButton) rootView.findViewById(R.id.next_track);
        next_track.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (myMediaPlayer.isPlaying()) {
                    myMediaPlayer.stop();
                    myMediaPlayer.reset();
                    pausedOrReset = "reset";
                    if (NowPlayingSong == 9) {
                        NowPlayingSong = 0;
                    } else {
                        NowPlayingSong = NowPlayingSong + 1;
                    }
                    refreshViewsAndStartPlayer(rootView);
                } else {
                    myMediaPlayer.stop();
                    myMediaPlayer.reset();
                    pausedOrReset = "reset";
                    if (NowPlayingSong == 9) {
                        NowPlayingSong = 0;
                    } else {
                        NowPlayingSong = NowPlayingSong + 1;
                    }
                    refreshViews(rootView);
                }
            }
        });

        final ImageButton previous_track = (ImageButton) rootView.findViewById(R.id.previous_track);
        previous_track.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (NowPlayingSong > 0) {
                    if (myMediaPlayer.isPlaying()) {
                        myMediaPlayer.stop();
                        myMediaPlayer.reset();
                        String pausedOrReset = "reset";
                        NowPlayingSong = NowPlayingSong - 1;
                        refreshViewsAndStartPlayer(rootView);
                    } else {
                        myMediaPlayer.stop();
                        myMediaPlayer.reset();
                        String pausedOrReset = "reset";
                        NowPlayingSong = NowPlayingSong - 1;
                        refreshViews(rootView);
                    }
                }
            }
        });

        return rootView;

    }

    private void startPlayer(String previewUrl) {
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
            public void onPrepared(MediaPlayer myMediaPlayer) {
                myMediaPlayer.start();
            }
        });
    }

    private void refreshViewsAndStartPlayer(View rootView) {

        TextView artistNameTextView = (TextView) rootView.findViewById(R.id.artist_name);
        TextView albumNameTextView = (TextView) rootView.findViewById(R.id.album_name);
        ImageView albumArt = (ImageView) rootView.findViewById(R.id.now_playing_thumbnail);
        TextView songNameTextView = (TextView) rootView.findViewById(R.id.track_name);

        artistNameTextView.setText(artistName);
        albumNameTextView.setText(TopTracksData.get(NowPlayingSong).gettextColumn1());
        songNameTextView.setText(TopTracksData.get(NowPlayingSong).gettextColumn0());
        Picasso.with(getActivity()).load(TopTracksData.get(NowPlayingSong).gettextColumn3()).into(albumArt);

        startPlayer(TopTracksData.get(NowPlayingSong).gettextColumn4());
    }

    private void refreshViews(View rootView) {

        TextView artistNameTextView = (TextView) rootView.findViewById(R.id.artist_name);
        TextView albumNameTextView = (TextView) rootView.findViewById(R.id.album_name);
        ImageView albumArt = (ImageView) rootView.findViewById(R.id.now_playing_thumbnail);
        TextView songNameTextView = (TextView) rootView.findViewById(R.id.track_name);

        artistNameTextView.setText(artistName);
        albumNameTextView.setText(TopTracksData.get(NowPlayingSong).gettextColumn1());
        songNameTextView.setText(TopTracksData.get(NowPlayingSong).gettextColumn0());
        Picasso.with(getActivity()).load(TopTracksData.get(NowPlayingSong).gettextColumn3()).into(albumArt);

    }

    @Override
    public void onStop() {
        super.onStop();
        myMediaPlayer.release();
        myMediaPlayer = null;
    }

}
