package app.com.example.android.spotifystreamer;

import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Image;


public class MainActivityFragment extends Fragment {

    ImageAndTextArrayAdapter mSearchArtistAdapter;
    List<RowItemFiveStrings> artistNameAndImageURL = new ArrayList<>();
    String searchText ="";

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final ListView listView = (ListView) rootView.findViewById(R.id.listview_artists);
        EditText editText = (EditText) rootView.findViewById(R.id.searchText);
        if(searchText.length()>0) {editText.setText(searchText);};

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
                        Toast.makeText(getActivity(), "Please enter the name of the artist", Toast.LENGTH_SHORT).show();
                        listView.invalidateViews();
                    }
                }
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                RowItemFiveStrings artistItem = mSearchArtistAdapter.getItem(position);
                //String artistID = spotifyId.get(position);
                String artistID = artistNameAndImageURL.get(position).gettextColumn2();
                String idAndName[] = {artistID, artistNameAndImageURL.get(position).gettextColumn0()};

                //Toast.makeText(getActivity(), artistItem.getTextViewText() + " " + artistID, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ArtistTopTenActivity.class);
                        //.putExtra(Intent.EXTRA_TEXT, artistID);
                intent.putExtra("IdAndNameArray", idAndName);
                startActivity(intent);

            }
        });

        return rootView;

    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putString("searchText", searchText);
    }


    private void refreshListView(View rootView){

        ListView listView = (ListView) rootView.findViewById(R.id.listview_artists);
        mSearchArtistAdapter = new ImageAndTextArrayAdapter(getActivity(),
                R.id.list_item_search_artist, artistNameAndImageURL );

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
            artistNameAndImageURL.clear();
            String aName = "No Name";
            String aImageUrl = "http://vignette2.wikia.nocookie.net/legendmarielu/images/b/b4/No_image_available.jpg/revision/latest?cb=20130511180903";
            String aID = "No ID";

            if (artistsPager.artists.items.size() == 0)
            {
                Toast.makeText(getActivity(),"Your search did not return any artist", Toast.LENGTH_SHORT).show();
            }
            else {
                for (Artist a : artistsPager.artists.items) {
                    //Log.d("NAME", "-----------> " + a.name.toString() + " : ");
                    //Log.d("ID", "-----------> " + a.id.toString() + " : ");

                    aName = a.name.toString();
                    aID = a.id.toString();
                    //spotifyId.add(a.id);

                    for (Image imgUrl : a.images) {
                        //Log.d("IMAGES", "-----------> " + imgUrl.url + " : ");
                        if (imgUrl.width <= 200) {
                            aImageUrl = imgUrl.url.toString();
                        }
                    }
                    RowItemFiveStrings artistItem = new RowItemFiveStrings(aName, aImageUrl, aID, "", "");
                    artistNameAndImageURL.add(artistItem);
                }
                mSearchArtistAdapter.notifyDataSetChanged();
            }
        }
    }

}
