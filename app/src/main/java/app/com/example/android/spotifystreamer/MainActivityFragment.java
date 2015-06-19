package app.com.example.android.spotifystreamer;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    ArrayAdapter<String> mSearchArtistAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

                String[] searchArtistArray = {
                        "Morgan James",
                        "Rush",
                        "Iron Maiden",
                        "Fleetwood Mac",
                        "Boston",
                        "Pink Floyd",
                        "Styx"
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
                    
                    handled = true;
                }
                return handled;
            }
        });


        SpotifyApi api = new SpotifyApi();

// Most (but not all) of the Spotify Web API endpoints require authorisation.
// If you know you'll only use the ones that don't require authorisation you can skip this step
        //api.setAccessToken("myAccessToken");

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

        spotify.searchArtists("morgan james", new Callback<ArtistsPager>() {
            @Override
            public void success(ArtistsPager artist, Response response) {
                Log.d("Artist success", artist.artists.toString());

                ListView listView = (ListView) rootView.findViewById(R.id.listview_artists);
                listView.setAdapter(mSearchArtistAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Artist failure", error.toString());
            }
        });




        return rootView;


    }




}
