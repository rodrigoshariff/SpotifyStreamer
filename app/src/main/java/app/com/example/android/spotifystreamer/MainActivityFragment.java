package app.com.example.android.spotifystreamer;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Image;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    ArrayAdapter<String> mSearchArtistAdapter;
    List<String> artistsName = new ArrayList<String>();
    List<String> spotifyId = new ArrayList<String>();
    List<String> artistImageURL = new ArrayList<String>();
    List<android.media.Image> artistImages = new ArrayList<android.media.Image>();


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

                String[] searchArtistArray = {
                        "https://i.scdn.co/image/8345ff2772773ece533a4dbfc29e084a8c292d24",
                        "https://i.scdn.co/image/fa5f0fae8533c41836b9e350a3a67901128a8bba",
                        "https://i.scdn.co/image/7d1d865fb1829effa9c7f648f57790194944ee2a"
                };

        List<String> artistSearch = new ArrayList<String>(Arrays.asList(searchArtistArray));

        mSearchArtistAdapter = new ArrayAdapter<String>(
                                        getActivity(), // The current context (this activity)
                                        R.layout.list_item_search_artist, // The name of the layout ID.
                                        R.id.list_item_search_artist_name, // The ID of the textview to populate.
                                        artistSearch);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_artists);
        listView.setAdapter(mSearchArtistAdapter);


        EditText editText = (EditText) rootView.findViewById(R.id.searchText);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView editText, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //populate listview using spotify API
                    Context context = getActivity();
                    CharSequence text = "This button will populate listview" + " " + editText.getText();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    QueryArtistsFromSpotify artistSearch = new QueryArtistsFromSpotify();
                    artistSearch.execute(editText.getText().toString());

                    handled = true;
                }
                return handled;
            }
        });


        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();

        spotify.getAlbum("2dIGnmEIy1WZIcZCFSj6i8", new Callback<Album>() {
            @Override
            public void success(Album album, Response response) {
                Log.d("Album success", album.name);
            }
            @Override
            public void failure(RetrofitError error) {
                Log.d("Album failure", error.toString());
            }
        });

        return rootView;

    }




    private class QueryArtistsFromSpotify extends AsyncTask<String, Void, ArtistsPager> {

        private final String LOG_TAG = QueryArtistsFromSpotify.class.getSimpleName();

        @Override
        protected ArtistsPager doInBackground(String... params) {

            SpotifyApi spotifyApi = new SpotifyApi();
            SpotifyService spotifyService = spotifyApi.getService();
            ArtistsPager artistsPager = spotifyService.searchArtists(params[0]);

            return artistsPager;

        }

        @Override
        protected void onPostExecute(ArtistsPager artistsPager) {
            //super.onPostExecute(artistsPager);

            List<Artist> artistsList = artistsPager.artists.items;

            for(Artist a : artistsPager.artists.items) {
                Log.d("NAME", "-----------> " + a.name + " : ");

                for (Image imgUrl : a.images ){
                    Log.d("IMAGES", "-----------> " + imgUrl.url + " : ");
                    if(imgUrl.width <=100){

                    }

                }


            }

            Log.d("Artist Search results", artistsList.toString());
            //Iterator<Artist> iterator = artistsList.iterator();

        }
    }






}
