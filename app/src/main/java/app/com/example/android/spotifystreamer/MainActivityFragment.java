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
    List<String> spotifyId = new ArrayList<String>();
    List<RowItem> artistNameAndImageURL = new ArrayList<>();

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final ListView listView = (ListView) rootView.findViewById(R.id.listview_artists);
        EditText editText = (EditText) rootView.findViewById(R.id.searchText);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView editText, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (editText.getText() != "") {

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
                        listView.invalidateViews();
                    }
                }
                return false;
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                RowItem artistItem = mSearchArtistAdapter.getItem(position);
                String artistID = spotifyId.get(position);
                Toast.makeText(getActivity(), artistItem.getTextViewText() + " " + artistID, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ArtistTopTenActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, artistID);
                startActivity(intent);
            }
        });


        return rootView;

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
            String aImageUrl = "http://www.gstatic.com/webp/gallery/1.jpg";

            for(Artist a : artistsPager.artists.items) {
                Log.d("NAME", "-----------> " + a.name.toString() + " : ");
                Log.d("ID", "-----------> " + a.id.toString() + " : ");

                aName = a.name.toString();
                spotifyId.add(a.id);

                for (Image imgUrl : a.images ){
                    Log.d("IMAGES", "-----------> " + imgUrl.url + " : ");
                    if(imgUrl.width <=300){
                        aImageUrl = imgUrl.url.toString();
                    }
                }
                RowItem artistItem = new RowItem(aName, aImageUrl);
                artistNameAndImageURL.add(artistItem);
            }
            mSearchArtistAdapter.notifyDataSetChanged();
        }
    }

}
