package anxa.com.smvideo.activities.account;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import anxa.com.smvideo.ApplicationData;
import anxa.com.smvideo.R;
import anxa.com.smvideo.activities.RecipeActivity;
import anxa.com.smvideo.activities.RecipesActivity;
import anxa.com.smvideo.ui.CoachingArchiveListAdapter;
import anxa.com.smvideo.ui.CustomListView;

/**
 * Created by aprilanxa on 17/07/2017.
 */

public class CoachingArchiveAccountActivity extends Activity implements View.OnClickListener{

    CustomListView weekListView;
    CoachingArchiveListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.coaching_archive_account);
        ((TextView)findViewById(R.id.header_title_tv)).setText(getString(R.string.coaching_header_right));
        ((TextView)findViewById(R.id.header_right_tv)).setVisibility(View.GONE);

        weekListView = (CustomListView)findViewById(R.id.archiveListView);

        if (adapter == null) {
            adapter = new CoachingArchiveListAdapter(this, ApplicationData.getInstance().coachingVideoList, this);
        }

        weekListView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

        int weekId = (Integer) v.getTag(R.id.week_id);
        ApplicationData.getInstance().selectedWeekNumber = weekId;
        ApplicationData.getInstance().fromArchive = true;

        System.out.println("weekId: " + weekId);
//
        Intent broadint = new Intent();
        broadint.setAction(this.getResources().getString(R.string.coaching_broadcast_string));
        this.sendBroadcast(broadint);
//        Intent intent = new Intent();
//
//        setResult(RESULT_OK, intent);

        finish();
    }
}
