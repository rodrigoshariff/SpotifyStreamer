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
 * Created by rodrigoshariff on 6/22/2015.
 */
public class ImageAndTwoTextsArrayAdapter extends ArrayAdapter<RowItemFiveStrings> {


    private final Activity context;

    public ImageAndTwoTextsArrayAdapter(Activity context, int resourceId, List<RowItemFiveStrings> items) {
        super(context, resourceId, items);
        this.context= context;

    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtSong;
        TextView txtAlbum;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RowItemFiveStrings rowItemSong = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_top10, null);
            holder = new ViewHolder();
            holder.txtSong = (TextView) convertView.findViewById(R.id.list_item_top10_song);
            holder.txtAlbum = (TextView) convertView.findViewById(R.id.list_item_album);
            holder.imageView = (ImageView) convertView.findViewById(R.id.list_item_top10_image);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtSong.setText(rowItemSong.gettextColumn0());
        holder.txtAlbum.setText(rowItemSong.gettextColumn1());
        Picasso.with(context).load(rowItemSong.gettextColumn2()).into(holder.imageView);

        return convertView;
    }





}
