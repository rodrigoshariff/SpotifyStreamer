package app.com.example.android.spotifystreamer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rodrigoshariff on 6/20/2015.
 */

public class ImageAndTextArrayAdapter extends ArrayAdapter<RowItem> {

    private final Activity context;

    public ImageAndTextArrayAdapter(Activity context, int resourceId, List<RowItem> items) {
        super(context, resourceId, items);
        this.context= context;

    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RowItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_search_artist, null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.list_item_search_artist_name);
            holder.imageView = (ImageView) convertView.findViewById(R.id.list_item_search_artist_image);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtTitle.setText(rowItem.getTextViewText());
        Picasso.with(context).load(rowItem.getImgURL()).into(holder.imageView);

        return convertView;
    }
}