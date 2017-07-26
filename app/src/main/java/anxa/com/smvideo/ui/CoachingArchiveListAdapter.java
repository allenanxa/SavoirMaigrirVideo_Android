package anxa.com.smvideo.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import anxa.com.smvideo.ApplicationData;
import anxa.com.smvideo.R;
import anxa.com.smvideo.contracts.CoachingVideosContract;
import anxa.com.smvideo.contracts.RepasContract;

/**
 * Created by aprilanxa on 17/07/2017.
 */

public class CoachingArchiveListAdapter extends ArrayAdapter<CoachingVideosContract> implements View.OnClickListener {

    private final Context context;
    private List<CoachingVideosContract> items = new ArrayList<CoachingVideosContract>();
    private int currentWeek;

    LayoutInflater layoutInflater;
    String inflater = Context.LAYOUT_INFLATER_SERVICE;
    View.OnClickListener listener;

    public CoachingArchiveListAdapter(Context context, List<CoachingVideosContract> items, View.OnClickListener listener) {
        super(context, R.layout.listitem_archive_content, items);

        layoutInflater = (LayoutInflater) context.getSystemService(inflater);
        this.context = context;
        this.items = items;
        this.listener = listener;

        currentWeek = ApplicationData.getInstance().currentWeekNumber;

    }

    public void updateItems(List<CoachingVideosContract> items) {
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
        return ApplicationData.getInstance().currentWeekNumber;
//        return items.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        View row = convertView;
        if (row == null) {
            LayoutInflater layoutInflator = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            row = layoutInflator.inflate(R.layout.listitem_archive_content, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.weekNumber = ((TextView) row.findViewById(R.id.weekNumber));
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }

        row.setTag(R.id.week_id, currentWeek - position);
        row.setOnClickListener(this);

        String weekString = (context.getString(R.string.coaching_archive_week)).replace("%d", Integer.toString(currentWeek - position));
        viewHolder.weekNumber.setText(weekString);
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
        TextView weekNumber;
    }
}
