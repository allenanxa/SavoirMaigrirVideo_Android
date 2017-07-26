package anxa.com.smvideo.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import anxa.com.smvideo.R;
import anxa.com.smvideo.contracts.RepasContract;

/**
 * Created by aprilanxa on 14/07/2017.
 */

public class RepasRelatedListAdapter extends ArrayAdapter<RepasContract> implements View.OnClickListener {

    private final Context context;
    private List<RepasContract> items = new ArrayList<RepasContract>();

    LayoutInflater layoutInflater;
    String inflater = Context.LAYOUT_INFLATER_SERVICE;
    View.OnClickListener listener;

    public RepasRelatedListAdapter(Context context, List<RepasContract> items, View.OnClickListener listener) {
        super(context, R.layout.listitem_repas_related_content, items);

        layoutInflater = (LayoutInflater) context.getSystemService(inflater);
        this.context = context;
        this.items = items;
        this.listener = listener;

    }

    public void updateItems(List<RepasContract> items) {
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

            row = layoutInflator.inflate(R.layout.listitem_repas_related_content, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.recipeTitle = ((TextView) row.findViewById(R.id.recipeTitle));
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }

        RepasContract repas = (RepasContract) items.get(position);
        row.setTag(R.id.recipe_id, repas.linkCtid);
        row.setOnClickListener(this);

        viewHolder.recipeTitle.setText(repas.itemName);
        return row;
    }


    private void refreshUI() {
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v != null) {

            if (items != null && items.size() > 0) {
                if (listener != null) {
                    listener.onClick(v);
                }
            }
        }

    }

    private static class ViewHolder {
        TextView recipeTitle;
    }
}