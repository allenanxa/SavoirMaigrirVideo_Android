package anxa.com.smvideo.contracts;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by aprilanxa on 29/06/2017.
 */

public class WeightHistoryDataContract {

    @SerializedName("initialWeightModel")
    public WeightHistoryContract InitialWeightModel;

    @SerializedName("historyModel")
    public List<WeightHistoryContract> HistoryModel;

    @SerializedName("page")
    public int Page;

    @SerializedName("totalRecords")
    public int TotalRecords;

}
