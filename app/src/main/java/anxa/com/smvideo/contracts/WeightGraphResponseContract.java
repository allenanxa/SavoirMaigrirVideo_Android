package anxa.com.smvideo.contracts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aprilanxa on 29/06/2017.
 */

public class WeightGraphResponseContract extends BaseContract {
    @SerializedName("data")
    public WeightGraphDataContract Data;
}
