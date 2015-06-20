package app.com.example.android.spotifystreamer;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    ArrayAdapter<String> mSearchArtistAdapter;
    List<String> artistsName = new ArrayList<String>();
    List<String> spotifyId = new ArrayList<String>();
    List<String> artistImageURL = new ArrayList<String>();


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        //mSearchArtistAdapter.clear();
        //mSearchArtistAdapter.notifyDataSetChanged();

        EditText editText = (EditText) rootView.findViewById(R.id.searchText);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView editText, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //populate listview using spotify API
                    //Context context = getActivity();

                    ListView listView = (ListView) rootView.findViewById(R.id.listview_artists);

                    QueryArtistsFromSpotify artistSearch = new QueryArtistsFromSpotify();
                    artistSearch.execute(editText.getText().toString());
                    refreshListView(rootView);
                    editText.clearFocus();


                    InputMethodManager in = (InputMethodManager)getActivity().
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(editText.getWindowToken(), 0);


                    return true;
                }
                return false;
            }
        });

        return rootView;

    }

    private void refreshListView(View rootView){

        ListView listView = (ListView) rootView.findViewById(R.id.listview_artists);
        mSearchArtistAdapter = new ArrayAdapter<String>(
                getActivity(), // The current context (this activity)
                R.layout.list_item_search_artist, // The name of the layout ID.
                R.id.list_item_search_artist_name, // The ID of the textview to populate.
                artistsName);
        mSearchArtistAdapter.notifyDataSetChanged();

        listView.setAdapter(mSearchArtistAdapter);



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
            artistsName.clear();
            //List<Artist> artistsList = artistsPager.artists.items;

            for(Artist a : artistsPager.artists.items) {
                Log.d("NAME", "-----------> " + a.name.toString() + " : ");
                artistsName.add(a.name.toString());
                //spotifyId.add(a.id);

/*                for (Image imgUrl : a.images ){
                    Log.d("IMAGES", "-----------> " + imgUrl.url + " : ");
                    if(imgUrl.width <=100){
                        artistImageURL.add(imgUrl.url);
                    }
                }*/

            }




        }
    }






}
