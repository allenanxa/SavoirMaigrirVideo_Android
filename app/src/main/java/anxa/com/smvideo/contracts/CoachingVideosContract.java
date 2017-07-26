package anxa.com.smvideo.contracts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aprilanxa on 29/06/2017.
 */

public class CoachingVideosContract {

    @SerializedName("id")
    public int Id;

    @SerializedName("index")
    public int Index;

    @SerializedName("weekNumber")
    public int WeekNumber;

    @SerializedName("dayNumber")
    public int DayNumber;

    @SerializedName("title")
    public String Title;

    @SerializedName("duration")
    public String Duration;

    @SerializedName("description")
    public String Description;

//    @SerializedName("videoId")
//    public String VideoId;
//
//    @SerializedName("videoSource")
//    public String VideoSource;

    @SerializedName("videoUrl")
    public String VideoUrl;

    @SerializedName("thumbnailUrl")
    public String ThumbnailUrl;

    public boolean IsSelected;

}
