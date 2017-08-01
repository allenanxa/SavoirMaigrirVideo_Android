package anxa.com.smvideo.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import anxa.com.smvideo.ApplicationData;
import anxa.com.smvideo.R;
import anxa.com.smvideo.contracts.RecipeContract;
import anxa.com.smvideo.util.RecipeHelper;

/**
 * Created by angelaanxa on 5/25/2017.
 */

public class RecipesListAdapter extends ArrayAdapter<RecipeContract> implements View.OnClickListener {

    private final Context context;
    private List<RecipeContract> items = new ArrayList<RecipeContract>();

    LayoutInflater layoutInflater;
    String inflater = Context.LAYOUT_INFLATER_SERVICE;
    View.OnClickListener listener;

    public RecipesListAdapter(Context context, List<RecipeContract> items, View.OnClickListener listener) {
        super(context, R.layout.listitem_recipe, items);

        layoutInflater = (LayoutInflater) context.getSystemService(inflater);
        this.context = context;
        this.items = items;
        this.listener = listener;

    }

    public void updateItems(List<RecipeContract> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public void clear() {
        this.items = new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        View row = convertView;
        if (row == null) {
            LayoutInflater layoutInflator = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            row = layoutInflator.inflate(R.layout.listitem_recipe, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.recipeImage = (ImageView) row.findViewById(R.id.recipeImage);
            viewHolder.recipeTitle = ((TextView) row.findViewById(R.id.recipeTitle));
            viewHolder.recipeImageProgress = ((ProgressBar) row.findViewById(R.id.recipeImageProgress));
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }

        RecipeContract recipe = (RecipeContract) items.get(position);
        row.setTag(R.id.recipe_id, recipe.Id);
        row.setOnClickListener(this);

        Bitmap avatar = null;
        avatar = RecipeHelper.GetRecipeImage(recipe.Id);

        //viewHolder.recipeImage.setTag(recipe.Id);
        //display message
        viewHolder.recipeTitle.setText(recipe.Title);

        if (avatar == null) {
           /* if( !ApplicationData.getInstance().RecipeOngoigImageDownload.contains(recipe.Id)) {
                ApplicationData.getInstance().RecipeOngoigImageDownload.add(recipe.Id);*/
            //new RecipeDownloadImageAsync(viewHolder.recipeImage, viewHolder.recipeImageProgress, recipe.Id).execute(recipe.ImageUrl);
               //new RecipeDownloadImageAsync(viewHolder.recipeImage, viewHolder.recipeImageProgress, recipe.Id).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, recipe.ImageUrl);
            //}

           // Picasso.with(context).setDebugging(true);
            //Picasso.with(context).setIndicatorsEnabled(true);
            Glide.with(context).load(recipe.ImageUrl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(viewHolder.recipeImage);
            try {
                if (!ApplicationData.getInstance().recipePhotoList.containsKey(String.valueOf(recipe.Id)) && viewHolder.recipeImage.getDrawable() != null) {

                    ApplicationData.getInstance().recipePhotoList.put(String.valueOf(recipe.Id), ((GlideBitmapDrawable)viewHolder.recipeImage.getDrawable()).getBitmap());
                }

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            viewHolder.recipeImageProgress.setVisibility(View.GONE);
        } else {
            //Picasso.with(context).load(avatar).fit().into(viewHolder.recipeImage);
            viewHolder.recipeImage.setImageBitmap(avatar);
            viewHolder.recipeImageProgress.setVisibility(View.GONE);
        }

        return row;
    }



    private void refreshUI() {
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v != null) {

            if (items != null && items.size() > 0) {
                int pos = (Integer) v.getTag(R.id.recipe_id);
//                items.get(pos).is_read = true;

                if (listener != null) {
                    listener.onClick(v);
                }
            }
        }

    }


    private static class ViewHolder {
        ImageView recipeImage;
        TextView recipeTitle;
        ProgressBar recipeImageProgress;
    }
}
