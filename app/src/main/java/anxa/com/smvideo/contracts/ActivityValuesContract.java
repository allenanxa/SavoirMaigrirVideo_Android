package anxa.com.smvideo.contracts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aprilanxa on 29/06/2017.
 */

public class ActivityValuesContract {

    @SerializedName("id")
    public int Id;

    @SerializedName("activityId")
    public int ActivityId;

    @SerializedName("activityValueType")
    public int ActivityValueType;

    @SerializedName("value")
    public float value;

    @SerializedName("modifiedDate")
    public String ModifiedDate;

    @SerializedName("deleted")
    public boolean Deleted;

    @SerializedName("createdDate")
    public String createdDate;
}
