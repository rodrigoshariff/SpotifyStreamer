package app.com.example.android.spotifystreamer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;


public class ArtistTopTenActivityFragment extends Fragment {

    ImageAndTwoTextsArrayAdapter mTop10SongsAdapter;
    List<RowItemFiveStrings> songNameAndImageURL = new ArrayList<>();
    String countryPrefMaster = "US";
    String countryPrefLocal = "US";
    String artistName = "";
    String artistID = "";
    private ListView listViewTop10 = null;
    private boolean mTwoPane = false;

    public ArtistTopTenActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_artist_top_ten, container, false);
        //ListView listView = (ListView) rootView.findViewById(R.id.listview_artistsTop10);
        listViewTop10 = (ListView) rootView.findViewById(R.id.listview_artistsTop10);

        //receive intent data from activity

        Bundle arguments = getArguments();
        if (arguments != null) {
            String[] IdAndNameArray = arguments.getStringArray("IdAndNameArray");
            mTwoPane = arguments.getBoolean("mTwoPane");

            artistID = IdAndNameArray[0];
            artistName = IdAndNameArray[1];
            countryPrefMaster = IdAndNameArray[2];

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            countryPrefLocal = sharedPrefs.getString(getString(R.string.pref_country_key), getString(R.string.pref_country_US));

            //Toast.makeText(getActivity(), "On Create Country " + countryPrefMaster +" "+countryPrefLocal, Toast.LENGTH_SHORT).show();
            //Log.d("COUNTRY_PREF_Child", "-----------> " + countryPrefMaster);

            ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(artistName + "  (" + countryPrefMaster + ")");

            //only query Spotify if entering this view for the first time, otherwise restore from saved instance state
            if (songNameAndImageURL.isEmpty() || (songNameAndImageURL.size() == 0)) {

                QueryArtistsTop10FromSpotify artistTop10Search = new QueryArtistsTop10FromSpotify();
                artistTop10Search.execute(artistID);
            }

            mTop10SongsAdapter = new ImageAndTwoTextsArrayAdapter(getActivity(),
                    R.id.list_item_top10, songNameAndImageURL);
            listViewTop10.setAdapter(mTop10SongsAdapter);

        }


/*        Intent intent = getActivity().getIntent();

        if (!(intent == null || intent.getData() == null))//&& intent.hasExtra(Intent.EXTRA_TEXT)) {
        {
            String[] IdAndNameArray = intent.getStringArrayExtra("IdAndNameArray");
            artistID = IdAndNameArray[0];
            artistName = IdAndNameArray[1];
            countryPrefMaster = IdAndNameArray[2];

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            countryPrefLocal = sharedPrefs.getString(getString(R.string.pref_country_key), getString(R.string.pref_country_US));

            //Toast.makeText(getActivity(), "On Create Country " + countryPrefMaster +" "+countryPrefLocal, Toast.LENGTH_SHORT).show();
            //Log.d("COUNTRY_PREF_Child", "-----------> " + countryPrefMaster);

            ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(artistName + "  (" + countryPrefMaster + ")");

            //only query Spotify if entering this view for the first time, otherwise restore from saved instance state
            if (songNameAndImageURL.isEmpty() || (songNameAndImageURL.size() == 0)) {

                QueryArtistsTop10FromSpotify artistTop10Search = new QueryArtistsTop10FromSpotify();
                artistTop10Search.execute(artistID);
            }

            mTop10SongsAdapter = new ImageAndTwoTextsArrayAdapter(getActivity(),
                    R.id.list_item_top10, songNameAndImageURL);
            listViewTop10.setAdapter(mTop10SongsAdapter);

        }*/

        listViewTop10.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                if (mTwoPane) {
                    // In two-pane mode, show the detail view in this activity by
                    // adding or replacing the detail fragment using a
                    // fragment transaction.
                    Bundle args = new Bundle();
                    args.putParcelableArrayList("TopTracksData", (ArrayList<? extends Parcelable>) songNameAndImageURL);
                    args.putInt("SelectedSong", position);
                    args.putString("ArtistName", artistName);

                    FragmentManager fragmentManager = getFragmentManager();
                    SimplePlayerDialogFragment playerDialogFragment = new SimplePlayerDialogFragment();
                    playerDialogFragment.setArguments(args);
                    playerDialogFragment.show(fragmentManager, "dialog");


                } else {
                    Intent intent = new Intent(getActivity(), SimplePlayerActivity.class);
                    //.putExtra(Intent.EXTRA_TEXT, artistID);
                    intent.putParcelableArrayListExtra("TopTracksData", (ArrayList<? extends Parcelable>) songNameAndImageURL);
                    intent.putExtra("SelectedSong", position);
                    intent.putExtra("ArtistName", artistName);
                    startActivity(intent);
                }



/* Original code
                Intent intent = new Intent(getActivity(), SimplePlayerActivity.class);
                //.putExtra(Intent.EXTRA_TEXT, artistID);
                intent.putParcelableArrayListExtra("TopTracksData", (ArrayList<? extends Parcelable>) songNameAndImageURL);
                intent.putExtra("SelectedSong", position);
                intent.putExtra("ArtistName", artistName);
                startActivity(intent);*/

            }
        });

        return rootView;
    }


    //save song data once back from Spotify API
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        //savedInstanceState.putString("artistName", artistName);
        savedInstanceState.putParcelableArrayList("SongsArray", (ArrayList<? extends Parcelable>) songNameAndImageURL);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            //List<RowItemFiveStrings> savedSongNameAndImageURL = new ArrayList<>();
            songNameAndImageURL = savedInstanceState.getParcelableArrayList("SongsArray");
            //Log.d("SavedInstanceArray", "-----------> " + songNameAndImageURL.get(0).gettextColumn0());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        countryPrefLocal = sharedPrefs.getString(getString(R.string.pref_country_key), getString(R.string.pref_country_US));

        if (!countryPrefLocal.equals(countryPrefMaster) )
        {
            //Toast.makeText(getActivity(), "On Resume Country " + countryPrefMaster +" "+countryPrefLocal, Toast.LENGTH_SHORT).show();
            countryPrefMaster = countryPrefLocal;
            songNameAndImageURL.clear();
            QueryArtistsTop10FromSpotify artistTop10Search = new QueryArtistsTop10FromSpotify();
            artistTop10Search.execute(artistID);

            mTop10SongsAdapter = new ImageAndTwoTextsArrayAdapter(getActivity(),
                    R.id.list_item_top10, songNameAndImageURL);
            listViewTop10.setAdapter(mTop10SongsAdapter);

            ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(artistName + "  (" + countryPrefMaster + ")");

        }

    }

    private class QueryArtistsTop10FromSpotify extends AsyncTask<String, Void, Tracks> {

        @Override
        protected Tracks doInBackground(String... params) {

            SpotifyApi spotifyApi = new SpotifyApi();
            SpotifyService spotifyService = spotifyApi.getService();
            Map<String, Object> map = new HashMap<>();
            map.put("country", countryPrefMaster);
            Tracks topTracks = spotifyService.getArtistTopTrack(params[0], map);

            return topTracks;
        }

        @Override
        protected void onPostExecute(Tracks topTracks) {
            //super.onPostExecute(artistsPager);
            //songNameAndImageURL.clear();
            String songName = "No Name";
            String albumName = "No Name";
            String aImageUrlsmall = "http://vignette2.wikia.nocookie.net/legendmarielu/images/b/b4/No_image_available.jpg/revision/latest?cb=20130511180903";
            String aImageUrllarge = "http://vignette2.wikia.nocookie.net/legendmarielu/images/b/b4/No_image_available.jpg/revision/latest?cb=20130511180903";
            String previewUrl = "No Preview";

            if (topTracks.tracks.size() == 0) {
                Toast.makeText(getActivity(), "There are no tracks for this artist", Toast.LENGTH_SHORT).show();
            } else {
                for (Track t : topTracks.tracks) {
                    //Log.d("NAME", "-----------> " + t.name.toString() + " : ");
                    //Log.d("NAME", "-----------> " + t.album + " : ");
                    songName = t.name.toString();
                    albumName = t.album.name;
                    previewUrl = t.preview_url.toString();

                    for (Image imgUrl : t.album.images) {
                        //Log.d("IMAGES", "-----------> " + imgUrl.url + " : ");
                        if (imgUrl.width <= 200) {
                            aImageUrlsmall = imgUrl.url.toString();
                        } else if (imgUrl.width > 500 && imgUrl.width <= 700) {
                            aImageUrllarge = imgUrl.url.toString();
                        }
                    }
                    //populate list with song data to use in custom adapter to populate song listview.
                    //also add now playing image and preview URL for future implementation
                    RowItemFiveStrings songItem = new RowItemFiveStrings(songName, albumName, aImageUrlsmall, aImageUrllarge, previewUrl);
                    songNameAndImageURL.add(songItem);
                }

                mTop10SongsAdapter.notifyDataSetChanged();
            }
        }
    }


}
