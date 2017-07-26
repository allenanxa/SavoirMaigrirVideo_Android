package anxa.com.smvideo.activities.account;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import anxa.com.smvideo.ApplicationData;
import anxa.com.smvideo.R;
import anxa.com.smvideo.common.SavoirMaigrirVideoConstants;
import anxa.com.smvideo.connection.ApiCaller;
import anxa.com.smvideo.connection.http.AsyncResponse;
import anxa.com.smvideo.contracts.CoachingVideosContract;
import anxa.com.smvideo.contracts.CoachingVideosResponseContract;
import anxa.com.smvideo.contracts.VideoContract;
import anxa.com.smvideo.ui.CoachingVideoListAdapter;
import anxa.com.smvideo.ui.CustomListView;
import anxa.com.smvideo.util.AppUtil;
import anxa.com.smvideo.util.VideoHelper;

/**
 * Created by aprilanxa on 14/06/2017.
 */

public class CoachingAccountFragment extends Fragment implements View.OnClickListener {

    private Context context;
    protected ApiCaller caller;

    private CoachingVideoListAdapter adapter;
    private List<CoachingVideosContract> videosList_all;
    private List<CoachingVideosContract> videosList;
    private CustomListView coachingListView;
    private TextView header_right;

    private YouTubePlayerFragment playerFragment;

    private int currentCoachingWeekNumber;
    private int currentCoachingDayNumber;
    private int selectedCoachingWeekNumber;

    private boolean fromArchive;
    String headerTitle;

    View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.context = getActivity();
        mView = inflater.inflate(R.layout.coaching_account, null);

        caller = new ApiCaller();

        IntentFilter filter = new IntentFilter();
        filter.addAction(this.getResources().getString(R.string.coaching_broadcast_string));
        context.registerReceiver(the_receiver, filter);

        currentCoachingWeekNumber = AppUtil.getCurrentWeekNumber(Long.parseLong(ApplicationData.getInstance().dietProfilesDataContract.CoachingStartDate), new Date());
        currentCoachingDayNumber = AppUtil.getCurrentDayNumber();
        ApplicationData.getInstance().currentWeekNumber = currentCoachingWeekNumber;

        //header change
        headerTitle = getString(R.string.coaching_header);
        headerTitle.replace("%d", Integer.toString(currentCoachingWeekNumber));
        ((TextView) (mView.findViewById(R.id.header_title_tv))).setText(headerTitle.replace("%d", Integer.toString(currentCoachingWeekNumber)));
        header_right = (TextView) (mView.findViewById(R.id.header_right_tv));
        header_right.setText(getString(R.string.coaching_header_right));
        header_right.setOnClickListener(this);

        coachingListView = (CustomListView) mView.findViewById(R.id.coachingListView);

        videosList = new ArrayList<CoachingVideosContract>();
        videosList_all = new ArrayList<CoachingVideosContract>();

        if (adapter == null) {
            adapter = new CoachingVideoListAdapter(context, videosList, this);
        }

        coachingListView.setAdapter(adapter);

        //Initializing and adding YouTubePlayerFragment
        FragmentManager fm = getFragmentManager();
        String tag = YouTubePlayerFragment.class.getSimpleName();
        playerFragment = (YouTubePlayerFragment) fm.findFragmentByTag(tag);

        FragmentTransaction ft = fm.beginTransaction();
        playerFragment = YouTubePlayerFragment.newInstance();
        ft.replace(R.id.youtube_layout, playerFragment, tag);
        ft.commit();

        selectedCoachingWeekNumber = ApplicationData.getInstance().selectedWeekNumber;
        System.out.println("onCreate selectedweek: " + selectedCoachingWeekNumber);

        if (ApplicationData.getInstance().fromArchive) {
            updateVideosList();
        }else{
            getCoachingVideosFromAPI();
        }
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(final View v) {
        if (v == header_right) {
            proceedToArchivePage();
        } else {
            FragmentManager fm = getFragmentManager();
            String tag = YouTubePlayerFragment.class.getSimpleName();
            playerFragment = (YouTubePlayerFragment) fm.findFragmentByTag(tag);
            if (playerFragment != null) {
                FragmentTransaction ft = fm.beginTransaction();
                playerFragment = YouTubePlayerFragment.newInstance();
                ft.replace(R.id.youtube_layout, playerFragment, tag);
                ft.commit();
            }

            final String videoId = (String) v.getTag(R.id.video_id);

            for (int i = 0; i < videosList.size(); i++) {
                VideoContract temp = new VideoContract();
                if (videosList.get(i).VideoUrl == videoId) {
                    RefreshPlayer(v, videosList.get(i));
                    videosList.get(i).IsSelected = true;
                } else {
                    videosList.get(i).IsSelected = false;
                }
            }
            adapter.updateItems(videosList);
        }
    }

    private void RefreshPlayer(final View v, final CoachingVideosContract video) {
        playerFragment.initialize(SavoirMaigrirVideoConstants.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
                if (video.VideoUrl != null) {

                    youTubePlayer.cueVideo(video.VideoUrl);

                    ((TextView) (mView.findViewById(R.id.videoTitle))).setText(video.Title);
                    ((TextView) (mView.findViewById(R.id.videoDesc))).setText(video.Description);
                    ((TextView) (mView.findViewById(R.id.videoDuration))).setText(video.Duration);
                    youTubePlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
                        @Override
                        public void onBuffering(boolean arg0) {
                        }

                        @Override
                        public void onPaused() {
                        }

                        @Override
                        public void onPlaying() {
                            //youTubePlayer.setFullscreen(true);
                            youTubePlayer.play();
                        }

                        @Override
                        public void onSeekTo(int arg0) {
                        }

                        @Override
                        public void onStopped() {
                            //youTubePlayer.setFullscreen(false);
                        }
                    });

                    youTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                        @Override
                        public void onFullscreen(boolean b) {
                            if (!b) {
                                getActivity().setRequestedOrientation(
                                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                                if (youTubePlayer.isPlaying()) {
                                    System.out.println("notfullscreen youtubeplay playing");
                                    youTubePlayer.play();
                                } else {
                                    System.out.println("notfullscreen youtubeplay not playing");
                                }
                            } else {
                                if (youTubePlayer.isPlaying()) {
                                    System.out.println("fullscreen youtubeplay playing");
                                    youTubePlayer.play();
                                } else {
                                    System.out.println("fullscreen youtubeplay not playing");
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                //Toast.makeText(YouTubePlayerFragmentActivity.this, "Error while initializing YouTubePlayer.", Toast.LENGTH_SHORT).show();
            }


        });
    }

    private void proceedToArchivePage() {
        Intent mainIntent = new Intent(this.getActivity(), CoachingArchiveAccountActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(mainIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 12345 && resultCode == Activity.RESULT_OK) {
            System.out.println("onActivityResult");
            selectedCoachingWeekNumber = ApplicationData.getInstance().selectedWeekNumber;

            updateVideosList();
            }
    }

    private void updateVideosList() {
        System.out.println("updateVideosList selectedweek: " + selectedCoachingWeekNumber);

        videosList = new ArrayList<>();

        for (CoachingVideosContract v : ApplicationData.getInstance().coachingVideoList) {
            if (v.WeekNumber == selectedCoachingWeekNumber) {
                if (v.WeekNumber == currentCoachingWeekNumber) {
                    if (v.DayNumber <= currentCoachingDayNumber) {
                        videosList.add(v);
                    }
                }else {
                    videosList.add(v);
                }
            }
        }

        if (videosList.size() > 0) {
            VideoHelper.sortCoachingVideos("index", videosList);
            videosList.get(0).IsSelected = true;
            adapter.updateItems(videosList);
            RefreshPlayer(mView, videosList.get(0));
        }

        ((TextView) (mView.findViewById(R.id.header_title_tv))).setText(headerTitle.replace("%d", Integer.toString(selectedCoachingWeekNumber)));
    }

    private void getCoachingVideosFromAPI() {
        System.out.println("getCoachingVideosFromAPI selectedweek: " + selectedCoachingWeekNumber);

        caller.GetAccountCoaching(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                //INITIALIZE ALL ONCLICK AND API RELATED PROCESS HERE TO AVOID CRASHES
                if (output != null) {
                    CoachingVideosResponseContract c = (CoachingVideosResponseContract) output;

                    if (c != null && c.Data != null && c.Data.Videos != null) {
                        for (CoachingVideosContract v : c.Data.Videos) {
//                            if (v.VideoSource != null && v.VideoSource.equalsIgnoreCase("youtube")) {
                            videosList_all.add(v);
//                            }

                            if (v.WeekNumber == currentCoachingWeekNumber) {
                                if (v.DayNumber <= currentCoachingDayNumber) {
                                    videosList.add(v);
                                }
                            }
                        }

                        ApplicationData.getInstance().coachingVideoList = videosList_all;

                        VideoHelper.sortCoachingVideos("index", videosList);
                        videosList.get(0).IsSelected = true;
                        adapter.updateItems(videosList);

                        RefreshPlayer(mView, videosList.get(0));
                    }
                }
            }
        }, currentCoachingWeekNumber);
    }

    private BroadcastReceiver the_receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction() == context.getResources().getString(R.string.coaching_broadcast_string)) {
                fromArchive = true;
                selectedCoachingWeekNumber = ApplicationData.getInstance().selectedWeekNumber;
                updateVideosList();

                System.out.println("broadcast receiver");
            }
        }
    };
}
