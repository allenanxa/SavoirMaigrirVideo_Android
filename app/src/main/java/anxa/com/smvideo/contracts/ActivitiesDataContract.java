package anxa.com.smvideo.contracts;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by aprilanxa on 29/06/2017.
 */

public class ActivitiesDataContract {

    @SerializedName("id")
    public int Id;

    @SerializedName("activityType")
    public int ActivityType;

    @SerializedName("userId")
    public int UserId;

    @SerializedName("startDate")
    public String StartDate;

    @SerializedName("endDate")
    public String EndDate;

    @SerializedName("confirmedData")
    public boolean confirmedData;

    @SerializedName("activityValues")
    public List<ActivityValuesContract> ActivityValues;

    @SerializedName("deviceType")
    public int DeviceType;

    @SerializedName("applicationId")
    public int applicationId;

    @SerializedName("application")
    public ApplicationDataContract Application;

    @SerializedName("deleted")
    public boolean Deleted;

    @SerializedName("modifiedDate")
    public String ModifiedDate;

    @SerializedName("createdDate")
    public String CreatedDate;
}
