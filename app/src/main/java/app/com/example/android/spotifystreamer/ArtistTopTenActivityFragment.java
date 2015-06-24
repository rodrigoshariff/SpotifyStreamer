package app.com.example.android.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;



/**
 * A placeholder fragment containing a simple view.
 */
public class ArtistTopTenActivityFragment extends Fragment {

    ImageAndTwoTextsArrayAdapter mTop10SongsAdapter;
    List<RowItemSong> songNameAndImageURL = new ArrayList<>();
    String artistName = "";

    public ArtistTopTenActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_artist_top_ten, container, false);

        // The detail Activity called via intent.  Inspect the intent for forecast data.
        Intent intent = getActivity().getIntent();

        if (intent != null) //&& intent.hasExtra(Intent.EXTRA_TEXT)) {
        {
            //String artistID = intent.getStringExtra(Intent.EXTRA_TEXT);

            String[] IdAndNameArray = intent.getStringArrayExtra("IdAndNameArray");
            String artistID = IdAndNameArray[0];
            String artistName = IdAndNameArray[1];

            ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(artistName);

          QueryArtistsTop10FromSpotify artistTop10Search = new QueryArtistsTop10FromSpotify();
            artistTop10Search.execute(artistID);

           ListView listView = (ListView) rootView.findViewById(R.id.listview_artistsTop10);
            mTop10SongsAdapter = new ImageAndTwoTextsArrayAdapter(getActivity(),
                    R.id.list_item_top10, songNameAndImageURL );

            listView.setAdapter(mTop10SongsAdapter);

        }

        return rootView;
    }


    private class QueryArtistsTop10FromSpotify extends AsyncTask<String, Void, Tracks> {

        private final String LOG_TAG = QueryArtistsTop10FromSpotify.class.getSimpleName();

        @Override
        protected Tracks doInBackground(String... params) {

            SpotifyApi spotifyApi = new SpotifyApi();
            SpotifyService spotifyService = spotifyApi.getService();
            Map<String, Object> map = new HashMap<>();
            map.put("country", "US");
            Tracks topTracks = spotifyService.getArtistTopTrack(params[0],map);

            return topTracks;
        }

        @Override
        protected void onPostExecute(Tracks topTracks) {
            //super.onPostExecute(artistsPager);
            //songNameAndImageURL.clear();
            String songName = "No Name";
            String albumName = "No Name";
            String aImageUrl = "http://www.gstatic.com/webp/gallery/1.jpg";

            for(Track t : topTracks.tracks) {
                Log.d("NAME", "-----------> " + t.name.toString() + " : ");
                Log.d("NAME", "-----------> " + t.album + " : ");
                songName = t.name.toString();
                albumName = t.album.name;

                for (Image imgUrl : t.album.images){
                    Log.d("IMAGES", "-----------> " + imgUrl.url + " : ");
                    if(imgUrl.width <=300){
                        aImageUrl = imgUrl.url.toString();
                    }
                }
                RowItemSong songItem = new RowItemSong(songName, albumName, aImageUrl);
                songNameAndImageURL.add(songItem);
            }
            mTop10SongsAdapter.notifyDataSetChanged();
        }
    }


}
