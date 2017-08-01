package anxa.com.smvideo.contracts;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by aprilanxa on 27/07/2017.
 */

public class LoginContract {
    @SerializedName("email")
    public String Email;

    @SerializedName("password")
    public String Password;

    @SerializedName("check_npna")
    public boolean Check_npna;
}
