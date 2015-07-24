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
    int NowPlayingSong= 0;
    String artistName = "";

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

            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mediaPlayer.setDataSource(TopTracksData.get(NowPlayingSong).gettextColumn4());
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mediaPlayer.start();


        }

        return rootView;

    }
}
