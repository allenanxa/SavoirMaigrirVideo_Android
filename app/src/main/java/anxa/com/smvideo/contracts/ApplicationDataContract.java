package anxa.com.smvideo.contracts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aprilanxa on 29/06/2017.
 */

public class ApplicationDataContract {

    @SerializedName("id")
    public int Id;

    @SerializedName("name")
    public String Name;

    @SerializedName("modifiedDate")
    public String ModifiedDate;

    @SerializedName("createdDate")
    public String CreatedDate;

    @SerializedName("deleted")
    public boolean Deleted;
}
