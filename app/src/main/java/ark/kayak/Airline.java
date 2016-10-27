package ark.kayak;

/**
 * Created by avinash on 10/21/16.
 */
import com.google.gson.annotations.SerializedName;

/**
 * Created by avinash on 4/8/16.
 */
public class Airline {
    @SerializedName("name")
    private String mName;

    @SerializedName("logoURL")
    private String mLogoUrl;

    @SerializedName("phone")
    private String mPhone;

    @SerializedName("site")
    private String mSite;

    private boolean isFavourite = false;


    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean isFavourite) {
        this.isFavourite = isFavourite;
    }



    public String getName() {
        return mName;
    }

    public String getLogoUrl() {
        return mLogoUrl;
    }

    public String getPhone() {
        return mPhone;
    }

    public String getSite() {
        return mSite;
    }
}

