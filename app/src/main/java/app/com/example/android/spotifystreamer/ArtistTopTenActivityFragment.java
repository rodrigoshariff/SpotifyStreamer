package app.com.example.android.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    String countryPref = "US";

    public ArtistTopTenActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_artist_top_ten, container, false);

        //receive intent data from main activity
        Intent intent = getActivity().getIntent();

        if (intent != null) //&& intent.hasExtra(Intent.EXTRA_TEXT)) {
        {
            String[] IdAndNameArray = intent.getStringArrayExtra("IdAndNameArray");
            String artistID = IdAndNameArray[0];
            String artistName = IdAndNameArray[1];
            countryPref = IdAndNameArray[2];
            Log.d("COUNTRY_PREF_Child", "-----------> " + countryPref);

            ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(artistName + "  (" +countryPref+")");

            //only query Spotify if entering this view for the first time, otherwise restore from saved instance state
            if(songNameAndImageURL.isEmpty() || (songNameAndImageURL.size() == 0)) {

                QueryArtistsTop10FromSpotify artistTop10Search = new QueryArtistsTop10FromSpotify();
                artistTop10Search.execute(artistID);
            }

                ListView listView = (ListView) rootView.findViewById(R.id.listview_artistsTop10);
                mTop10SongsAdapter = new ImageAndTwoTextsArrayAdapter(getActivity(),
                        R.id.list_item_top10, songNameAndImageURL);
                listView.setAdapter(mTop10SongsAdapter);

        }
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
            Log.d("SavedInstanceArray", "-----------> " + songNameAndImageURL.get(0).gettextColumn0());
        }
    }

    private class QueryArtistsTop10FromSpotify extends AsyncTask<String, Void, Tracks> {

        @Override
        protected Tracks doInBackground(String... params) {

            SpotifyApi spotifyApi = new SpotifyApi();
            SpotifyService spotifyService = spotifyApi.getService();
            Map<String, Object> map = new HashMap<>();
            map.put("country", countryPref);
            Tracks topTracks = spotifyService.getArtistTopTrack(params[0],map);

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

            if (topTracks.tracks.size() == 0)
            {
                Toast.makeText(getActivity(),"There are no tracks for this artist", Toast.LENGTH_SHORT).show();
            }

            else {
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
                        } else if (imgUrl.width >500 && imgUrl.width <= 700) {
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
