package anxa.com.smvideo.contracts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aprilanxa on 13/07/2017.
 */

public class RepasResponseContract extends BaseContract {
    @SerializedName("data")
    public RepasDataContract Data;
}
