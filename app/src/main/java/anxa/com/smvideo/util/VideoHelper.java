package anxa.com.smvideo.util;

import android.graphics.Bitmap;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import anxa.com.smvideo.ApplicationData;
import anxa.com.smvideo.contracts.CoachingVideosContract;
import anxa.com.smvideo.contracts.VideoContract;

/**
 * Created by angelaanxa on 5/31/2017.
 */

public class VideoHelper {
    static Comparator<VideoContract> indexComparator = new Comparator<VideoContract>() {
        @Override
        public int compare(VideoContract o1, VideoContract o2) {
            return Integer.valueOf(o1.Index).compareTo(o2.Index);
        }
    };

    static Comparator<CoachingVideosContract> indexComparatorCoaching = new Comparator<CoachingVideosContract>() {
        @Override
        public int compare(CoachingVideosContract o1, CoachingVideosContract o2) {
            return Integer.valueOf(o1.Index).compareTo(o2.Index);
        }
    };

    public static void sort(final String field, List<VideoContract> itemLocationList) {

        final Comparator<VideoContract> comparator;

        if(field.toLowerCase().equals("index")) {
            comparator = indexComparator;
        }  else {
            throw new IllegalArgumentException("Comparator not found for " + field);
        }

        Collections.sort(itemLocationList, comparator);
    }

    public static void sortCoachingVideos(final String field, List<CoachingVideosContract> itemLocationList) {

        final Comparator<CoachingVideosContract> comparator;

        if(field.toLowerCase().equals("index")) {
            comparator = indexComparatorCoaching;
        }  else {
            throw new IllegalArgumentException("Comparator not found for " + field);
        }

        Collections.sort(itemLocationList, comparator);
    }

    public static Bitmap GetVideoImage(String videoId) {
        Bitmap avatarBMP = null;
        if (videoId != null) {
            avatarBMP = ApplicationData.getInstance().videoPhotoList.get(String.valueOf(videoId));

            return avatarBMP;

        }

        return avatarBMP;

    }
}
