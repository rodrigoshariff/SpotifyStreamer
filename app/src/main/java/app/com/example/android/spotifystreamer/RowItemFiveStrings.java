package app.com.example.android.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rmendoza on 6/24/2015.
 */
public class RowItemFiveStrings implements Parcelable {

    private String textColumn0;
    private String textColumn1;
    private String textColumn2;
    private String textColumn3;
    private String textColumn4;

    public RowItemFiveStrings() {
    }

    public RowItemFiveStrings(String textColumn0, String textColumn1, String textColumn2, String textColumn3, String textColumn4) {
        this.textColumn0 = textColumn0;
        this.textColumn1 = textColumn1;
        this.textColumn2 = textColumn2;
        this.textColumn3 = textColumn3;
        this.textColumn4 = textColumn4;
    }

    public String gettextColumn0() {return textColumn0;    }
    public void settextColumn0(String title) {this.textColumn0 = title;    }
    public String gettextColumn1() {        return textColumn1;    }
    public void settextColumn1(String title) {        this.textColumn1 = title;    }
    public String gettextColumn2() {return textColumn2;    }
    public void settextColumn2(String title) {this.textColumn2 = title;    }
    public String gettextColumn3() {return textColumn3;    }
    public void settextColumn3(String title) {this.textColumn3 = title;    }
    public String gettextColumn4() {return textColumn4;    }
    public void settextColumn4(String title) {this.textColumn4 = title;    }

    @Override
    public int describeContents() {
        return 0;
    }

    public RowItemFiveStrings(Parcel parcel) {
        this.textColumn0 = parcel.readString();
        this.textColumn1 = parcel.readString();
        this.textColumn2 = parcel.readString();
        this.textColumn3 = parcel.readString();
        this.textColumn4 = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(textColumn0);
        dest.writeString(textColumn1);
        dest.writeString(textColumn2);
        dest.writeString(textColumn3);
        dest.writeString(textColumn4);
    }

    public static Creator<RowItemFiveStrings> CREATOR = new Creator<RowItemFiveStrings>() {

        @Override
        public RowItemFiveStrings createFromParcel(Parcel source) {
            return new RowItemFiveStrings(source);
        }

        @Override
        public RowItemFiveStrings[] newArray(int size) {
            return new RowItemFiveStrings[size];
        }

    };

}
