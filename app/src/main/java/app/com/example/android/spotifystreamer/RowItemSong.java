package app.com.example.android.spotifystreamer;

/**
 * Created by rodrigoshariff on 6/22/2015.
 */
public class RowItemSong {

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


}
