import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import app.com.example.android.spotifystreamer.R;

/**
 * Created by rmendoza on 6/19/2015.
 */
public class MultiTargetArrayAdapter extends ArrayAdapter<String>{
    private final Context context;

    public MultiTargetArrayAdapter(Context context, String[] artistName, String[] imageUrls) {
        super(context, -1, imageUrls);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView textView = (TextView) convertView.findViewById(R.id.list_item_search_artist_name);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.icon);
        return convertView;
    }
}
