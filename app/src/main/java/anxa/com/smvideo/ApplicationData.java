package anxa.com.smvideo;

import android.app.Application;
import android.graphics.Bitmap;

import com.crashlytics.android.Crashlytics;

import anxa.com.smvideo.contracts.CoachingVideosContract;
import anxa.com.smvideo.contracts.DietProfilesDataContract;
import anxa.com.smvideo.contracts.QuestionsContract;
import anxa.com.smvideo.contracts.RepasContract;
import anxa.com.smvideo.contracts.ResultsResponseDataContract;
import anxa.com.smvideo.contracts.UserDataContract;
import anxa.com.smvideo.contracts.VideoContract;
import anxa.com.smvideo.contracts.WeightGraphContract;
import anxa.com.smvideo.contracts.WeightHistoryContract;
import anxa.com.smvideo.util.AppUtil;
import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import anxa.com.smvideo.contracts.RecipeContract;

/**
 * Created by aprilanxa on 19/05/2017.
 */

public class ApplicationData extends Application {

    public enum SelectedFragment{
        Decouvir(0),
        Bilan(1),
        Temoignages(2),
        Recettes(3),
        MonCompte(4),
        Account_Coaching(5),
        Account_Repas(6),
        Account_Recettes(7),
        Account_Conseil(8),
        Account_Exercices(9),
        Account_Suivi(10),
        Account_MonCompte(11);
        private int numVal;

        SelectedFragment(int numVal) {
            this.numVal = numVal;
        }

        public int getNumVal() {
            return numVal;
        }
    }

    private static ApplicationData instance = null;
    public boolean showLandingPage = true;

    public int regId = 27;
    public int currentWeekNumber = 0;
    public int selectedWeekNumber = 0;
    public int applicationId = 1;
    public String accountType = "account";
    public String userName = "User";

    public SelectedFragment selectedFragment = SelectedFragment.Decouvir;

    public Hashtable<String, Bitmap> recipePhotoList = new Hashtable<String, Bitmap>();
    public Hashtable<String, Bitmap> videoPhotoList = new Hashtable<String, Bitmap>();
    public List<RecipeContract> recipeList = new ArrayList<>();
    public List<RecipeContract> recipeAccountList = new ArrayList<>();
    public List<QuestionsContract> questionsList = new ArrayList<>();
    public List<VideoContract> discoverVideoList = new ArrayList<>();
    public List<VideoContract> testimonialVideoList = new ArrayList<>();

    /** account **/
    public UserDataContract userDataContract = null;
    public DietProfilesDataContract dietProfilesDataContract = null;

    public List<CoachingVideosContract> coachingVideoList = new ArrayList<>();
    public List<VideoContract> conseilsVideoList = new ArrayList<>();
    public List<VideoContract> exerciseVideoList = new ArrayList<>();
    public List<WeightGraphContract> weightGraphContractList = new ArrayList<>();
    public List<RepasContract> repasContractArrayList = new ArrayList<>();
    public ResultsResponseDataContract bilanminceurResults = new ResultsResponseDataContract();
    public RecipeContract selectedRelatedRecipe = new RecipeContract();

    public WeightHistoryContract currentWeight;
    public WeightHistoryContract initialWeightContract;

    public String currentDateRangeDisplay;
    public Date currentDateRangeDisplay_date;
    public Date currentDateRangeDisplay_date2;

    public boolean fromArchive = false;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        instance = this;
    }


    public ApplicationData() {
        super();
    }


    public static ApplicationData getInstance() {
        return instance;
    }

}
