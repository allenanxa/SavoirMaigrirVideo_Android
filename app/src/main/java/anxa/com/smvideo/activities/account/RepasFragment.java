package anxa.com.smvideo.activities.account;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import anxa.com.smvideo.ApplicationData;
import anxa.com.smvideo.R;
import anxa.com.smvideo.activities.free.RecipeActivity;
import anxa.com.smvideo.connection.ApiCaller;
import anxa.com.smvideo.connection.http.AsyncResponse;
import anxa.com.smvideo.contracts.RecipeContract;
import anxa.com.smvideo.contracts.RecipeResponseContract;
import anxa.com.smvideo.contracts.RepasContract;
import anxa.com.smvideo.contracts.RepasResponseContract;
import anxa.com.smvideo.ui.CustomListView;
import anxa.com.smvideo.ui.RepasListAdapter;
import anxa.com.smvideo.ui.RepasRelatedListAdapter;
import anxa.com.smvideo.util.AppUtil;

/**
 * Created by aprilanxa on 22/06/2017.
 */

public class RepasFragment extends Fragment implements View.OnClickListener {

    private Context context;
    protected ApiCaller caller;

    View mView;

    private TextView repasProgram_tv;
    private EditText repasDay_et;
    private ImageButton nextDay_btn;
    private ImageButton previousDay_btn;

    private CustomListView bfastListView;
    private CustomListView lunchListView;
    private CustomListView dinnerListView;
    private CustomListView relatedListView;

    private ArrayList<RepasContract> bfastList = new ArrayList<>();
    private ArrayList<RepasContract> lunchList = new ArrayList<>();
    private ArrayList<RepasContract> dinnerList = new ArrayList<>();
    private ArrayList<RepasContract> repasRelatedContent = new ArrayList<>();

    private RepasListAdapter repasListAdapter_bfast;
    private RepasListAdapter repasListAdapter_lunch;
    private RepasListAdapter repasListAdapter_dinner;
    private RepasRelatedListAdapter repasListAdapter_related;

    String[] plansArray = new String[]{};
    String[] caloriesArray = new String[]{};

    private int dayOffset = 0;
    private int weekNumber = 0;
    private int dayNumber = 0;
    private int totalDaysArchive = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.context = getActivity();
        mView = inflater.inflate(R.layout.repas_account, null);

        caller = new ApiCaller();

        //header change
        ((TextView) (mView.findViewById(R.id.header_title_tv))).setText(getString(R.string.menu_account_repas));
        ((mView.findViewById(R.id.header_right_tv))).setVisibility(View.INVISIBLE);

        repasProgram_tv = (TextView) (mView.findViewById(R.id.repasHeader_tv));
        repasDay_et = (EditText) (mView.findViewById(R.id.repasDay_et));

        nextDay_btn = (ImageButton) (mView.findViewById(R.id.nextDayButton));
        previousDay_btn = (ImageButton) (mView.findViewById(R.id.previousDayButton));

        nextDay_btn.setOnClickListener(this);
        previousDay_btn.setOnClickListener(this);

        bfastListView = (CustomListView) mView.findViewById(R.id.repas_bfast_list);
        lunchListView = (CustomListView) mView.findViewById(R.id.repas_lunch_list);
        dinnerListView = (CustomListView) mView.findViewById(R.id.repas_dinner_list);
        relatedListView = (CustomListView) mView.findViewById(R.id.repas_related_list);

        plansArray = new String[]{getString(R.string.mon_compte_plans_brunch), getString(R.string.mon_compte_plans_classique), getString(R.string.mon_compte_plans_pour),
                getString(R.string.mon_compte_plans_dejeuner), getString(R.string.mon_compte_plans_diner), getString(R.string.mon_compte_plans_gluten), getString(R.string.mon_compte_plans_vache),
                getString(R.string.mon_compte_plans_laitages_avec), getString(R.string.mon_compte_plans_laitages_sans), getString(R.string.mon_compte_plans_petit_dejeuner), getString(R.string.mon_compte_plans_special), getString(R.string.mon_compte_plans_vegetarien)};
        caloriesArray = new String[]{getString(R.string.mon_compte_niveau_calorique_900), getString(R.string.mon_compte_niveau_calorique_1200), getString(R.string.mon_compte_niveau_calorique_1400),
                getString(R.string.mon_compte_niveau_calorique_1600), getString(R.string.mon_compte_niveau_calorique_1800)};

        String programHeader = getString(R.string.repas_header_meal_plan);

        programHeader = programHeader.replace("#program", plansArray[ApplicationData.getInstance().dietProfilesDataContract.MealPlanType]);
        programHeader = programHeader.replace("#calorie", caloriesArray[ApplicationData.getInstance().dietProfilesDataContract.MealPlanType]);

        repasProgram_tv.setText(programHeader);

        repasDay_et.setText(AppUtil.getRepasDateHeader(new Date(), true));

        weekNumber = AppUtil.getCurrentWeekNumber(Long.parseLong(ApplicationData.getInstance().dietProfilesDataContract.CoachingStartDate), new Date());
        dayNumber = AppUtil.getCurrentDayNumber();
        totalDaysArchive = AppUtil.getDaysDiffToCurrent(Long.parseLong(ApplicationData.getInstance().dietProfilesDataContract.CoachingStartDate));

        getMealOfTheDay();

        return mView;
    }

    private void updateRepasList(){
        if (repasListAdapter_bfast == null) {
            repasListAdapter_bfast = new RepasListAdapter(getActivity(), bfastList, this);
        }
        if (repasListAdapter_lunch == null) {
            repasListAdapter_lunch = new RepasListAdapter(getActivity(), lunchList, this);
        }
        if (repasListAdapter_dinner == null) {
            repasListAdapter_dinner = new RepasListAdapter(getActivity(), dinnerList, this);
        }
        if (repasListAdapter_related == null) {
            repasListAdapter_related = new RepasRelatedListAdapter(getActivity(), repasRelatedContent, this);
        }

        if (ApplicationData.getInstance().repasContractArrayList!=null){
            repasRelatedContent = new ArrayList<>();
            bfastList = new ArrayList<>();
            lunchList = new ArrayList<>();
            dinnerList = new ArrayList<>();

            for (RepasContract repasContract: ApplicationData.getInstance().repasContractArrayList){
                switch (repasContract.mealType){
                    case 1:
                        bfastList.add(repasContract);
                        break;
                    case 2:
                        lunchList.add(repasContract);
                        break;
                    case 4:
                        dinnerList.add(repasContract);
                        break;
                    default:
                        break;
                }
                if (repasContract.linkCtid > 0){
                    if (!repasRelatedContent.contains(repasContract)) {
                        repasRelatedContent.add(repasContract);
                    }
                }
            }
            bfastListView.setAdapter(repasListAdapter_bfast);
            lunchListView.setAdapter(repasListAdapter_lunch);
            dinnerListView.setAdapter(repasListAdapter_dinner);
            relatedListView.setAdapter(repasListAdapter_related);

            repasListAdapter_bfast.updateItems(bfastList);
            repasListAdapter_lunch.updateItems(lunchList);
            repasListAdapter_dinner.updateItems(dinnerList);
            repasListAdapter_related.updateItems(repasRelatedContent);
        }
    }

    private void getMealOfTheDay(){
        //dayOffset = 0, today
        caller.GetAccountRepas(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                //INITIALIZE ALL ONCLICK AND API RELATED PROCESS HERE TO AVOID CRASHES
                if (output != null) {
                    RepasResponseContract c = (RepasResponseContract)output;
                    if (c!=null && c.Data!=null){
                        ApplicationData.getInstance().repasContractArrayList = c.Data.Repas;
                        updateRepasList();
                    }
                }
            }
        }, weekNumber, dayNumber);

    }

    @Override
    public void onClick(final View v) {
        System.out.println("Repas onClick: " + v.getTag());

        if (v==nextDay_btn){
            if (dayOffset<0){
                dayOffset++;
                getDateString();
                getMealOfTheDay();
            }
        }else if (v==previousDay_btn){
            if (totalDaysArchive+dayOffset > 0){
                dayOffset--;

                getDateString();
                getMealOfTheDay();
            }
        }else{
            int recipeId = (Integer) v.getTag(R.id.recipe_id);
            getSpecificRecipe(recipeId);
        }
    }

    private void getSpecificRecipe(int recipeId){

        caller.GetAccountRecipeCtid(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {

                System.out.println("getSpecificRecipe: " + output);
                RecipeResponseContract c = (RecipeResponseContract)output;
                if (c!=null && c.Data!=null && c.Data.Recipes!=null){
                    System.out.println("getSpecificRecipe: " + c.Data.Recipes.get(0).Title);
                    proceedToRecipePage(c.Data.Recipes.get(0));
                }
            }
        }, recipeId, dayNumber);
    }

    private void proceedToRecipePage(RecipeContract recipeContract){
        ApplicationData.getInstance().selectedRelatedRecipe = recipeContract;

        Fragment fragment = new RecipeActivity();
        FragmentManager fragmentManager = getFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("SOURCE", "fromRepas");
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().add(R.id.mainContent, fragment, "RECIPE_FRAGMENT").addToBackStack(null)
                .commit();
    }


    private void getDateString(){
        Calendar cal = GregorianCalendar.getInstance();
        cal.add( Calendar.DAY_OF_YEAR, dayOffset);
        Date displayDate = cal.getTime();

        weekNumber = AppUtil.getCurrentWeekNumber(Long.parseLong(ApplicationData.getInstance().dietProfilesDataContract.CoachingStartDate), displayDate);
        if (weekNumber==0)
            weekNumber = 1;
        dayNumber = AppUtil.getDayNumber(displayDate);

        repasDay_et.setText(AppUtil.getRepasDateHeader(displayDate, false));
    }
}
