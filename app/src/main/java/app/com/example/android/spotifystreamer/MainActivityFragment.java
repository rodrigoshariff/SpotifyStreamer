package app.com.example.android.spotifystreamer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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
        return rootView;
    }
}
