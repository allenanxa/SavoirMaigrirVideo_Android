package anxa.com.smvideo.activities.free;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.squareup.picasso.Picasso;

import java.util.List;

import anxa.com.smvideo.ApplicationData;
import anxa.com.smvideo.R;
import anxa.com.smvideo.connection.http.RecipeDownloadImageAsync;
import anxa.com.smvideo.contracts.RecipeContract;
import anxa.com.smvideo.util.RecipeHelper;
import anxa.com.smvideo.util.UITagHandler;

/**
 * Created by angelaanxa on 5/29/2017.
 */

public class RecipeActivity extends Fragment {
    private List<RecipeContract> recipesList;
    View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.recipe, null);
        ((TextView) ((RelativeLayout) mView.findViewById(R.id.headermenu)).findViewById(R.id.header_title_tv)).setText(getString(R.string.menu_recette));
        ((ImageView) ((RelativeLayout) mView.findViewById(R.id.headermenu)).findViewById(R.id.header_menu_back)).setVisibility(View.VISIBLE);
        ((ImageView) ((RelativeLayout) mView.findViewById(R.id.headermenu)).findViewById(R.id.header_menu_iv)).setVisibility(View.GONE);

        String myValue = this.getArguments().getString("message");
        String source = this.getArguments().getString("SOURCE");

        if (source.equalsIgnoreCase("fromRepas")) {
            updateUI(ApplicationData.getInstance().selectedRelatedRecipe);

        } else {
            int recipeId = Integer.parseInt(this.getArguments().getString("RECIPE_ID"));
            if (source.equalsIgnoreCase("fromRecettesAccount")) {
                recipesList = ApplicationData.getInstance().recipeAccountList;
            }else{
                recipesList = ApplicationData.getInstance().recipeList;
            }
            if (recipeId > 0) {
                for (RecipeContract r : recipesList) {
                    if (r.Id == recipeId) {
                        updateUI(r);
                    }
                }
            }
        }
        return mView;
    }

    private void updateUI(RecipeContract recipeContract) {
        ((TextView) mView.findViewById(R.id.recipeTitle)).setText(recipeContract.Title);
        Bitmap avatar = null;
        avatar = RecipeHelper.GetRecipeImage(recipeContract.Id);
        ImageView img = (ImageView) mView.findViewById(R.id.recipeImage);
        img.setTag(recipeContract.Id);
        if (avatar == null) {

            Glide.with(this).load(recipeContract.ImageUrl).into(img);
            try {
                if (!ApplicationData.getInstance().recipePhotoList.containsKey(String.valueOf(recipeContract.Id)) && img.getDrawable() != null) {

                    ApplicationData.getInstance().recipePhotoList.put(String.valueOf(recipeContract.Id), ((GlideBitmapDrawable)img.getDrawable()).getBitmap());
                }

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
          mView.findViewById(R.id.recipeImageProgress).setVisibility(View.GONE);
            //new RecipeDownloadImageAsync(img, (ProgressBar) mView.findViewById(R.id.recipeImageProgress), recipeContract.Id).execute(recipeContract.ImageUrl);
        } else {
            ((ImageView) mView.findViewById(R.id.recipeImage)).setImageBitmap(avatar);
            ((ProgressBar) mView.findViewById(R.id.recipeImageProgress)).setVisibility(View.GONE);
        }
        ((TextView) mView.findViewById(R.id.recipeIngredientsTitle)).setText((recipeContract.IngredientsTitle));
        ((TextView) mView.findViewById(R.id.recipeIngredients)).setText(Html.fromHtml(recipeContract.IngredientsHtml, null, new UITagHandler()));
        ((TextView) mView.findViewById(R.id.recipePreparation)).setText(Html.fromHtml(recipeContract.PreparationHtml, null, new UITagHandler()));
    }


    private Bitmap getAvatar(int recipeId) {
        Bitmap avatarBMP = null;
        if (recipeId > 0) {
            avatarBMP = ApplicationData.getInstance().recipePhotoList.get(String.valueOf(recipeId));

            return avatarBMP;
        }
        return avatarBMP;
    }

}