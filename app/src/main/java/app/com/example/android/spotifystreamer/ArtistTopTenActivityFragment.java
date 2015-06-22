package app.com.example.android.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;


/**
 * A placeholder fragment containing a simple view.
 */
public class ArtistTopTenActivityFragment extends Fragment {

    ImageAndTextArrayAdapter mTop10SongsAdapter;
    List<RowItem> songNameAndImageURL = new ArrayList<>();

    public ArtistTopTenActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_artist_top_ten, container, false);
        // The detail Activity called via intent.  Inspect the intent for forecast data.
        Intent intent = getActivity().getIntent();

        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            String artistID = intent.getStringExtra(Intent.EXTRA_TEXT);

           QueryArtistsTop10FromSpotify artistTop10Search = new QueryArtistsTop10FromSpotify();
            artistTop10Search.execute(artistID);

           ListView listView = (ListView) rootView.findViewById(R.id.listview_artists);
            mTop10SongsAdapter = new ImageAndTextArrayAdapter(getActivity(),
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
            Tracks topTracks = spotifyService.getArtistTopTrack(params[0]);

            return topTracks;
        }

        @Override
        protected void onPostExecute(Tracks topTracks) {
            //super.onPostExecute(artistsPager);
            songNameAndImageURL.clear();
            String aName = "No Name";
            String aImageUrl = "http://www.gstatic.com/webp/gallery/1.jpg";

            for(Track t : topTracks.tracks) {
                Log.d("NAME", "-----------> " + t.name.toString() + " : ");
                Log.d("NAME", "-----------> " + t.album + " : ");
                aName = t.name.toString();

                for (Image imgUrl : t.album.images){
                    Log.d("IMAGES", "-----------> " + imgUrl.url + " : ");
                    if(imgUrl.width <=300){
                        aImageUrl = imgUrl.url.toString();
                    }
                }
                RowItem artistItem = new RowItem(aName, aImageUrl);
                songNameAndImageURL.add(artistItem);
            }
            mTop10SongsAdapter.notifyDataSetChanged();
        }
    }


}
