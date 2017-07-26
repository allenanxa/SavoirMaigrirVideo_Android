package anxa.com.smvideo.contracts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aprilanxa on 29/06/2017.
 */

public class WeightGraphContract {

    @SerializedName("activityId")
    public int ActivityId;

    @SerializedName("userId")
    public int UserId;

    @SerializedName("weight")
    public float WeightKg;

    @SerializedName("bmi")
    public float Bmi;

    @SerializedName("type")
    public String DeviceType;

    @SerializedName("date")
    public long Date;

    @SerializedName("deleted")
    public String Deleted;

    @SerializedName("confirmedData")
    public String ConfirmedData;
}
