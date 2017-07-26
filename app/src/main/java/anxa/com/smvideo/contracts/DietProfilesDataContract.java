package anxa.com.smvideo.contracts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aprilanxa on 29/06/2017.
 */

public class DietProfilesDataContract {

    @SerializedName("id")
    public int Id;

    @SerializedName("userId")
    public int UserId;

    @SerializedName("gender")
    public int Gender;

    @SerializedName("startWeightInKg")
    public float StartWeightInKg;

    @SerializedName("currentWeightInKg")
    public float CurrentWeightInKg;

    @SerializedName("targetWeightInKg")
    public float TargetWeightInKg;

    @SerializedName("heightInMeter")
    public float HeightInMeter;

    @SerializedName("dietProfileAnswers")
    public String DietProfileAnswers;

    @SerializedName("version")
    public int Version;

    @SerializedName("mealPlanType")
    public int MealPlanType;

    @SerializedName("calorieType")
    public int CalorieType;

    @SerializedName("coachProgram")
    public int CoachProgram;

    @SerializedName("coachingStartDate")
    public String CoachingStartDate;

    @SerializedName("coachingEndDate")
    public String CoachingEndDate;

    @SerializedName("applicationId")
    public int ApplicationId;

    @SerializedName("application")
    public ApplicationDataContract Application;

    @SerializedName("deleted")
    public Boolean deleted;

    @SerializedName("modifiedDate")
    public String modifiedDate;

    @SerializedName("createdDate")
    public String createdDate;

}
