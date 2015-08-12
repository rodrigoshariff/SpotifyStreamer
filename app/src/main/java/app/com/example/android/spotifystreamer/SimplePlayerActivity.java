package app.com.example.android.spotifystreamer;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;


public class SimplePlayerActivity extends ActionBarActivity {

    List<RowItemFiveStrings> songNameAndImageURL = new ArrayList<>();
    int position = 0;
    String artistName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_player);


        if (savedInstanceState == null) {

            Bundle args = new Bundle();
            args.putParcelableArrayList("TopTracksData", getIntent().getParcelableArrayListExtra("TopTracksData"));
            args.putInt("SelectedSong", getIntent().getIntExtra("SelectedSong",0));
            args.putString("ArtistName", getIntent().getStringExtra("ArtistName"));

            SimplePlayerDialogFragment fragment = new SimplePlayerDialogFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.player_container, fragment)
                    .commit();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_simple_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
