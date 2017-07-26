package anxa.com.smvideo.contracts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aprilanxa on 14/07/2017.
 */

public class RecipeCtidContract extends BaseContract {
    @SerializedName("data")
    public RecipeDataContract Data;
}
