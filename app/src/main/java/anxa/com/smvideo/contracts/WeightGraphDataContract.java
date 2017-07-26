package anxa.com.smvideo.contracts;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by aprilanxa on 29/06/2017.
 */

public class WeightGraphDataContract {
    @SerializedName("weights")
    public List<WeightGraphContract> Weights;
}
