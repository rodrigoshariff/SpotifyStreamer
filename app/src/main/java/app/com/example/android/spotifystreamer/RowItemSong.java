package app.com.example.android.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rodrigoshariff on 6/22/2015.
 */
public class RowItemSong implements Parcelable{

    private String textViewSong;
    private String textViewAlbum;
    private String ImgURL;

    public RowItemSong() {
    }

    public RowItemSong(String textViewSong, String textViewAlbum, String ImgURL) {
        this.ImgURL = ImgURL;
        this.textViewSong = textViewSong;
        this.textViewAlbum = textViewAlbum;
    }
    public String getImgURL() {
        return ImgURL;
    }
    public void setImgURL(String imgURL) {
        this.ImgURL = imgURL;
    }
    public String gettextViewSong() {
        return textViewSong;
    }
    public void settextViewSong(String title) {
        this.textViewSong = title;
    }
    public String gettextViewAlbum() {
        return textViewAlbum;
    }
    public void settextViewAlbum(String title) {
        this.textViewAlbum = title;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public RowItemSong (Parcel parcel) {
        this.textViewSong = parcel.readString();
        this.textViewAlbum = parcel.readString();
        this.ImgURL = parcel.readString();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(textViewSong);
        dest.writeString(textViewAlbum);
        dest.writeString(ImgURL);
    }

    public static Creator<RowItemSong> CREATOR = new Creator<RowItemSong>() {

        @Override
        public RowItemSong createFromParcel(Parcel source) {
            return new RowItemSong(source);
        }

        @Override
        public RowItemSong[] newArray(int size) {
            return new RowItemSong[size];
        }

    };


}
