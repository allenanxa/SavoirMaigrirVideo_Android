package anxa.com.smvideo.contracts;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by aprilanxa on 29/06/2017.
 */

public class UserDataContract {
    @SerializedName("id")
    public int Id;

    @SerializedName("email")
    public String Email;

    @SerializedName("emailConfirmed")
    public boolean EmailConfirmed;

    @SerializedName("passwordHash")
    public String PasswordHash;

    @SerializedName("phone")
    public String Phone;

    @SerializedName("phoneConfirmed")
    public boolean PhoneConfirmed;

    @SerializedName("userName")
    public String userName;

    @SerializedName("firstName")
    public String FirstName;

    @SerializedName("middleName")
    public String MiddleName;

    @SerializedName("lastName")
    public String LastName;

    @SerializedName("pictureUrl")
    public String PictureUrl;

    @SerializedName("membershipType")
    public String MembershipType;

    @SerializedName("alertType")
    public String AlertType;

    @SerializedName("alertContact")
    public String AlertContact;

    @SerializedName("activities")
    public List<ActivitiesDataContract> Activities;

    @SerializedName("dietProfiles")
    public List<DietProfilesDataContract> DietProfiles;

    @SerializedName("deleted")
    public Boolean deleted;

    @SerializedName("modifiedDate")
    public String modifiedDate;

    @SerializedName("createdDate")
    public String createdDate;

}
