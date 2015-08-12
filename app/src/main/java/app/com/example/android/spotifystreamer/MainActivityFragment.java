package app.com.example.android.spotifystreamer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Image;


public class MainActivityFragment extends Fragment {

    OnArtistSelectedListener mCallback;

    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnArtistSelectedListener {
        /**
         * Called by MainFragment when a list item is selected
         */
        public void onArtistSelected(String[] idAndName);
    }

    ImageAndTextArrayAdapter mSearchArtistAdapter;
    List<RowItemFiveStrings> artistNameAndImageURL = new ArrayList<>();
    String searchText = "";
    String countryPref = "US";
    private boolean mTwoPane;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final ListView listView = (ListView) rootView.findViewById(R.id.listview_artists);
        EditText editText = (EditText) rootView.findViewById(R.id.searchText);
        if (searchText.length() > 0) {
            editText.setText(searchText);
        }
        ;

        if (getActivity().findViewById(R.id.artist_top_ten_container) != null) {
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }
        //search click
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView editText, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    artistNameAndImageURL.clear();
                    if (editText.getText() != "" && editText.getText() != null && editText.length() > 0) {

                        searchText = editText.getText().toString();
                        editText.clearFocus();

                        //populate listwiew with spotify query results
                        QueryArtistsFromSpotify artistSearch = new QueryArtistsFromSpotify();
                        artistSearch.execute(editText.getText().toString());
                        refreshListView(rootView);


                        //hide keyboard
                        InputMethodManager in = (InputMethodManager) getActivity().
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(editText.getWindowToken(), 0);

                        return true;
                    } else {
                        //hide keyboard
                        InputMethodManager in = (InputMethodManager) getActivity().
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(editText.getWindowToken(), 0);

                        Toast.makeText(getActivity(), "Please enter the name of the artist", Toast.LENGTH_SHORT).show();
                        listView.invalidateViews();
                    }
                }
                return false;
            }
        });

        //artist item click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                //get selected country from settings or use default
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                countryPref = sharedPrefs.getString(getString(R.string.pref_country_key), getString(R.string.pref_country_US));
                Log.d("COUNTRY_PREF", "-----------> " + countryPref);

                //prepare parameters to send to next activity/fragment
                //RowItemFiveStrings artistItem = mSearchArtistAdapter.getItem(position);
                String artistID = artistNameAndImageURL.get(position).gettextColumn2();
                String idAndName[] = {artistID, artistNameAndImageURL.get(position).gettextColumn0(), countryPref};

                //send intent
                //Toast.makeText(getActivity(), artistItem.getTextViewText() + " " + artistID, Toast.LENGTH_SHORT).show();

/*              send intent.  original code for phone only
                Intent intent = new Intent(getActivity(), ArtistTopTenActivity.class);
                intent.putExtra("IdAndNameArray", idAndName);
                startActivity(intent);*/

                //send parameter to container activity MainActivity to start top ten activity or start top ten fragment on tablet layout
                mCallback.onArtistSelected(idAndName);

            }

            //}
        });

        return rootView;

    }

    //save artist data once back from Spotify API
    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putString("searchText", searchText);
    }


    //populate artist listview
    private void refreshListView(View rootView) {

        ListView listView = (ListView) rootView.findViewById(R.id.listview_artists);
        mSearchArtistAdapter = new ImageAndTextArrayAdapter(getActivity(),
                R.id.list_item_search_artist, artistNameAndImageURL);

        listView.setAdapter(mSearchArtistAdapter);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnArtistSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnArtistSelectedListener");
        }
    }


    //query Spotify API
    private class QueryArtistsFromSpotify extends AsyncTask<String, Void, ArtistsPager> {

        private final String LOG_TAG = QueryArtistsFromSpotify.class.getSimpleName();

        @Override
        protected ArtistsPager doInBackground(String... params) {

            SpotifyApi spotifyApi = new SpotifyApi();
            SpotifyService spotifyService = spotifyApi.getService();
            try {
                ArtistsPager artistsPager = spotifyService.searchArtists(params[0]);
                return artistsPager;
            } catch (Exception e) {

                Log.e(LOG_TAG, "Error ==========>: " + e.getMessage(), e);
                //Toast.makeText(getActivity(), "Unable to connect", Toast.LENGTH_SHORT).show();
                return null;
            }
        }


        @Override
        protected void onPostExecute(ArtistsPager artistsPager) {
            //super.onPostExecute(artistsPager);

            artistNameAndImageURL.clear();
            String aName = "No Name";
            String aImageUrl = "http://vignette2.wikia.nocookie.net/legendmarielu/images/b/b4/No_image_available.jpg/revision/latest?cb=20130511180903";
            String aID = "No ID";

            if (artistsPager == null || artistsPager.artists.items.size() == 0 ) {
                if(artistsPager == null) {
                    Toast.makeText(getActivity(), "Check internet connection", Toast.LENGTH_SHORT).show();}
                else {
                    Toast.makeText(getActivity(), "Your search did not return any artist", Toast.LENGTH_SHORT).show();
                }
            } else {
                for (Artist a : artistsPager.artists.items) {

                    aName = a.name.toString();
                    aID = a.id.toString();

                    for (Image imgUrl : a.images) {
                        //Log.d("IMAGES", "-----------> " + imgUrl.url + " : ");
                        if (imgUrl.width <= 200) {
                            aImageUrl = imgUrl.url.toString();
                        }
                    }
                    //populate list to use in custom adapter to populate listview
                    RowItemFiveStrings artistItem = new RowItemFiveStrings(aName, aImageUrl, aID, "", "");
                    artistNameAndImageURL.add(artistItem);
                }
                mSearchArtistAdapter.notifyDataSetChanged();
            }
        }
    }

}
