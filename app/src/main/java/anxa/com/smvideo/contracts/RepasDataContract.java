package anxa.com.smvideo.contracts;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by aprilanxa on 13/07/2017.
 */

public class RepasDataContract {
    @SerializedName("mealPlans")
    public List<RepasContract> Repas;
}
