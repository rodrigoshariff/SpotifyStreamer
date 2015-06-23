package app.com.example.android.spotifystreamer;

/**
 * Created by rodrigoshariff on 6/20/2015.
 */
public class RowItemArtist {
    private String textViewText;
    private String ImgURL;

    public RowItemArtist() {
    }

    public RowItemArtist(String textViewText, String ImgURL) {
        this.ImgURL = ImgURL;
        this.textViewText = textViewText;
    }
    public String getImgURL() {
        return ImgURL;
    }
    public void setImgURL(String imgURL) {
        this.ImgURL = imgURL;
    }
    public String getTextViewText() {
        return textViewText;
    }
    public void setTextViewText(String title) {
        this.textViewText = title;
    }
    @Override
    public String toString() {
        return textViewText;
    }
}