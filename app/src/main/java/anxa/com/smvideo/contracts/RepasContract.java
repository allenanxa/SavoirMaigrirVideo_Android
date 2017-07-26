package anxa.com.smvideo.contracts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aprilanxa on 13/07/2017.
 */

public class RepasContract {
    @SerializedName("progId")
    public int progId;
    @SerializedName("dnum")
    public int dnum;
    @SerializedName("wnum")
    public int wnum;
    @SerializedName("mealType")
    public int mealType;
    @SerializedName("ctid")
    public int ctid;
    @SerializedName("itemName")
    public String itemName;
    @SerializedName("linkCtid")
    public int linkCtid;
    @SerializedName("originalCtid")
    public int originalCtid;
    @SerializedName("bootcampId")
    public int bootcampId;
}
