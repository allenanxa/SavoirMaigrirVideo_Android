package anxa.com.smvideo.contracts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aprilanxa on 29/06/2017.
 */

public class UserDataResponseContract extends BaseContract {
    @SerializedName("data")
    public UserDataContract Data;
}
