package anxa.com.smvideo.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import anxa.com.smvideo.ApplicationData;
import anxa.com.smvideo.contracts.WeightGraphContract;

/**
 * Created by angelaanxa on 5/23/2017.
 */

public class AppUtil {
    private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

    public static String SHA1(String text) throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        byte[] sha1hash = md.digest();
        return asHex(sha1hash);
    }

    public static String asHex(byte[] buf) {
        char[] chars = new char[2 * buf.length];
        for (int i = 0; i < buf.length; ++i) {
            chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
            chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
        }
        return new String(chars);
    }

    public static int getCurrentWeek(){

        return 0;
//       return new GregorianCalendar().get(Calendar.WEEK_OF_YEAR);
    }

    public static String get1MDateRangeDisplay(boolean initDate, boolean previous, int index) {
        String stringDisplay = "";

        Calendar cal = Calendar.getInstance();

        if (ApplicationData.getInstance().currentDateRangeDisplay_date == null || initDate) {
            cal.setTime(new Date());
        } else {
            cal.setTime(ApplicationData.getInstance().currentDateRangeDisplay_date);
        }

        if (previous) {
            cal.add(Calendar.MONTH, -1);
        } else {
            cal.add(Calendar.MONTH, 1);
        }

        if (initDate) {
            stringDisplay = new SimpleDateFormat("MMM dd").format(new Date());
            stringDisplay = new SimpleDateFormat("MMM dd").format(cal.getTime()) + " - " + stringDisplay;
            ApplicationData.getInstance().currentDateRangeDisplay_date = new Date();

        } else {
            stringDisplay = new SimpleDateFormat("MMM yyyy").format(cal.getTime());
            ApplicationData.getInstance().currentDateRangeDisplay_date = cal.getTime();
        }

        ApplicationData.getInstance().currentDateRangeDisplay = stringDisplay;

        return stringDisplay;
    }

    public static String get3MDateRangeDisplay(boolean initDate, boolean previous, int index) {
        String stringDisplay = "";
        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        if (ApplicationData.getInstance().currentDateRangeDisplay_date == null || initDate) {
            cal.setTime(new Date());
            cal.add(Calendar.MONTH, -2);

            stringDisplay = new SimpleDateFormat("MMM yyyy").format(cal.getTime()) + " - " + new SimpleDateFormat("MMM yyyy").format(new Date());
            //store previous month
            ApplicationData.getInstance().currentDateRangeDisplay_date = cal.getTime();
            ApplicationData.getInstance().currentDateRangeDisplay_date2 = new Date();

        } else {
            cal.setTime(ApplicationData.getInstance().currentDateRangeDisplay_date);
            cal2.setTime(ApplicationData.getInstance().currentDateRangeDisplay_date2);
            if (previous) {
                cal.add(Calendar.MONTH, -3);
                cal2.add(Calendar.MONTH, -3);
                stringDisplay = new SimpleDateFormat("MMM yyyy").format(cal2.getTime());
                stringDisplay = new SimpleDateFormat("MMM yyyy").format(cal.getTime()) + " - " + stringDisplay;

                //store previous month
                ApplicationData.getInstance().currentDateRangeDisplay_date = cal.getTime();
                ApplicationData.getInstance().currentDateRangeDisplay_date2 = cal2.getTime();

            } else {
                cal.add(Calendar.MONTH, +3);
                cal2.add(Calendar.MONTH, +3);
                stringDisplay = new SimpleDateFormat("MMM yyyy").format(cal2.getTime());
                stringDisplay = new SimpleDateFormat("MMM yyyy").format(cal.getTime()) + " - " + stringDisplay;

                //store previous month
                ApplicationData.getInstance().currentDateRangeDisplay_date = cal.getTime();
                ApplicationData.getInstance().currentDateRangeDisplay_date2 = cal2.getTime();
            }
        }

        ApplicationData.getInstance().currentDateRangeDisplay = stringDisplay;

        return stringDisplay;
    }

    public static String get1YDateRangeDisplay(boolean initDate, boolean previous) {
        String stringDisplay = "";

        Calendar cal = Calendar.getInstance();

        if (ApplicationData.getInstance().currentDateRangeDisplay_date == null || initDate) {
            cal.setTime(new Date());
        } else {
            cal.setTime(ApplicationData.getInstance().currentDateRangeDisplay_date);

            if (previous) {
                cal.add(Calendar.YEAR, -1);
            } else {
                cal.add(Calendar.YEAR, 1);
            }
        }

        if (initDate) {
            cal.add(Calendar.MONTH, -11);
            stringDisplay = new SimpleDateFormat("MMM yyyy").format(new Date());
            stringDisplay = new SimpleDateFormat("MMM yyyy").format(cal.getTime()) + " - " + stringDisplay;
            ApplicationData.getInstance().currentDateRangeDisplay_date = new Date();

        } else {
            stringDisplay = new SimpleDateFormat("yyyy").format(cal.getTime());
            ApplicationData.getInstance().currentDateRangeDisplay_date = cal.getTime();
        }

        ApplicationData.getInstance().currentDateRangeDisplay = stringDisplay;

        return stringDisplay;
    }

    /**
     * @return list of Weight data in a year
     * must be 12 items or less
     * return the latest weight recorderd in the month
     **/
    public static List<WeightGraphContract> get1YWeightList(boolean initDate, int dateRangeIndex) {
        List<WeightGraphContract> weightGraphDataArrayList = ApplicationData.getInstance().weightGraphContractList;
        Hashtable<Date, WeightGraphContract> weightDate = new Hashtable<Date, WeightGraphContract>();
        ArrayList<Date> dateList = dateList = new ArrayList<>(12);

        Calendar cal = Calendar.getInstance();
        Calendar calValid = Calendar.getInstance();

        if (initDate) {
            cal.setTime(new Date());

        } else {
            cal.setTime(ApplicationData.getInstance().currentDateRangeDisplay_date);
            cal.set(Calendar.MONTH, Calendar.DECEMBER);
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR));
            cal.set(Calendar.DAY_OF_YEAR, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_YEAR));

            calValid.setTime(ApplicationData.getInstance().currentDateRangeDisplay_date);
            calValid.set(Calendar.YEAR, calValid.get(Calendar.YEAR));
            calValid.set(Calendar.MONTH, Calendar.DECEMBER);
        }

        dateList.add(cal.getTime());

        //include current date;

        WeightGraphContract dummyWeight = new WeightGraphContract();
        dummyWeight.Date = cal.getTime().toString();
        dummyWeight.WeightKg = 0;
        weightDate.put(cal.getTime(), dummyWeight);

        for (int i = 1; i < 12; i++) {
            calValid.add(Calendar.MONTH, -1);
            dateList.add(calValid.getTime());

            dummyWeight = new WeightGraphContract();
            dummyWeight.Date = calValid.getTime().toString();
            dummyWeight.WeightKg = 0;
            weightDate.put(calValid.getTime(), dummyWeight);
        }

        Collections.sort(dateList, new Comparator<Date>() {
            public int compare(Date o1, Date o2) {
                return o1.compareTo(o2);
            }
        });

        for (WeightGraphContract weight : weightGraphDataArrayList) {
            Calendar calIndex = Calendar.getInstance();
            calIndex.setTime(convertStringToDate(weight.Date));
            int monthIndex = calIndex.get(Calendar.MONTH);

            if (convertStringToDate(weight.Date).before(cal.getTime()) && convertStringToDate(weight.Date).after((Date) dateList.get(0))) {
                for (Date date_list : dateList) {
                    Calendar calIndex_date = Calendar.getInstance();
                    calIndex_date.setTime(date_list);
                    if (monthIndex == calIndex_date.get(Calendar.MONTH)) {
                        weightDate.put(date_list, weight);
                    }
                }
            }
        }

        ArrayList<WeightGraphContract> weightGraphDataArrayList_1y = new ArrayList<WeightGraphContract>();
        for (Date date_list : dateList) {
            weightGraphDataArrayList_1y.add(weightDate.get(date_list));
        }

        return weightGraphDataArrayList_1y;
    }

    public static List<WeightGraphContract> getAllWeightList() {
        List<WeightGraphContract> weightGraphDataArrayList = ApplicationData.getInstance().weightGraphContractList;
        WeightGraphContract oldestWeight = AppUtil.getOldestWeight();

        Collections.sort(weightGraphDataArrayList, new Comparator<WeightGraphContract>() {
            public int compare(WeightGraphContract o1, WeightGraphContract o2) {
                return convertStringToDate(o1.Date).compareTo(convertStringToDate(o2.Date));
            }
        });

        ArrayList<WeightGraphContract> weightGraphDataArrayList_all = new ArrayList<WeightGraphContract>();

        Hashtable<Date, WeightGraphContract> weightDate = new Hashtable<Date, WeightGraphContract>();
        ArrayList<Date> dateList = new ArrayList<>();

        //newest date
        Calendar calLatest = Calendar.getInstance();
        calLatest.setTime(new Date());

        //oldest weight
        Calendar calValid = Calendar.getInstance();
        if (oldestWeight.Date != null) {
            calValid.setTime(convertStringToDate(oldestWeight.Date));
        } else {
            calValid.setTime(new Date());
        }

        //present year only
        if (calValid.get(Calendar.YEAR) == calLatest.get(Calendar.YEAR)) {
            return get1YWeightList(true, 0);
        }


        dateList.add(calLatest.getTime());

        WeightGraphContract dummyWeight = new WeightGraphContract();
        dummyWeight.Date = calLatest.getTime().toString();
        dummyWeight.WeightKg = 0;
        weightDate.put(calLatest.getTime(), dummyWeight);

        int monthsDiff = getMonthsDifference(calValid.getTime(), calLatest.getTime());

        for (int i = 1; i < monthsDiff; i++) {
            calLatest.add(Calendar.MONTH, -1);
            dateList.add(calLatest.getTime());

            dummyWeight = new WeightGraphContract();
            dummyWeight.Date = calLatest.getTime().toString();
            dummyWeight.WeightKg = 0;
            weightDate.put(calLatest.getTime(), dummyWeight);
        }

        Collections.sort(dateList, new Comparator<Date>() {
            public int compare(Date o1, Date o2) {
                return o1.compareTo(o2);
            }
        });

        for (WeightGraphContract weight : weightGraphDataArrayList) {
            Calendar calIndex = Calendar.getInstance();
            calIndex.setTime(convertStringToDate(weight.Date));
            int monthIndex = calIndex.get(Calendar.MONTH);
            int yearIndex = calIndex.get(Calendar.YEAR);

            if (convertStringToDate(weight.Date).before(new Date()) && convertStringToDate(weight.Date).after((Date) dateList.get(0))) {
                for (Date date_list : dateList) {
                    Calendar calIndex_date = Calendar.getInstance();
                    calIndex_date.setTime(date_list);
                    if (monthIndex == calIndex_date.get(Calendar.MONTH) && yearIndex == calIndex_date.get(Calendar.YEAR)) {
                        weightDate.put(date_list, weight);
                    }
                }
            }
        }

        for (Date date_list : dateList) {
            weightGraphDataArrayList_all.add(weightDate.get(date_list));
        }
        return weightGraphDataArrayList_all;
    }

    public static WeightGraphContract getOldestWeight() {
        List<WeightGraphContract> weightGraphDataArrayList = ApplicationData.getInstance().weightGraphContractList;
        WeightGraphContract oldestWeight = new WeightGraphContract();

        if (weightGraphDataArrayList.size() > 0) {
            oldestWeight = weightGraphDataArrayList.get(0);
        }

        for (WeightGraphContract weight : weightGraphDataArrayList) {
            Calendar calIndex = Calendar.getInstance();
            calIndex.setTime(convertStringToDate(weight.Date));
            int monthIndex = calIndex.get(Calendar.MONTH);

            if (convertStringToDate(weight.Date).before(convertStringToDate(oldestWeight.Date))) {
                oldestWeight = weight;
            }
        }

        return oldestWeight;
    }

    public static Date convertStringToDate(String dateToConvert){
    //sample date: 2017-06-26T19:32:57.247
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss.SSS", Locale.ENGLISH);

        try {
            cal.setTime(sdf.parse(dateToConvert));
            return cal.getTime();

        }catch (ParseException e){
            e.printStackTrace();
        }

        return cal.getTime();
    }

    public static final int getMonthsDifference(Date date1, Date date2) {
        Calendar startCalendar = new GregorianCalendar();
        startCalendar.setTime(date1);
        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(date2);

        int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);

        return diffMonth;
    }

    public static List<WeightGraphContract> get3MWeightList(boolean initDate) {
        List<WeightGraphContract> weightGraphDataArrayList = ApplicationData.getInstance().weightGraphContractList;
        ArrayList<WeightGraphContract> weightGraphDataArrayList_3m = new ArrayList<WeightGraphContract>();

        Hashtable<Date, WeightGraphContract> weightDate = new Hashtable<Date, WeightGraphContract>();

        Collections.sort(weightGraphDataArrayList, new Comparator<WeightGraphContract>() {
            public int compare(WeightGraphContract o1, WeightGraphContract o2) {
                return o1.Date.compareTo(o2.Date);
            }
        });

        Calendar cal = Calendar.getInstance();
        Calendar calValid = Calendar.getInstance();

        ArrayList<Date> dateList = new ArrayList(3);

        if (initDate) {
            cal.setTime(new Date());
            calValid.set(Calendar.DAY_OF_MONTH, 1);
        } else {
            cal.setTime(ApplicationData.getInstance().currentDateRangeDisplay_date2);
            cal.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
            calValid.setTime(ApplicationData.getInstance().currentDateRangeDisplay_date2);
            calValid.set(Calendar.DAY_OF_MONTH, 1);
        }

        //include current date;
        dateList.add(cal.getTime());
        WeightGraphContract dummyWeight = new WeightGraphContract();
        dummyWeight.Date = cal.getTime().toString();
        dummyWeight.WeightKg = 0;
        weightDate.put(cal.getTime(), dummyWeight);

        for (int i = 1; i < 3; i++) {
            calValid.add(Calendar.MONTH, -1);
            dateList.add(calValid.getTime());

            dummyWeight = new WeightGraphContract();
            dummyWeight.Date = calValid.getTime().toString();
            dummyWeight.WeightKg = 0;
            weightDate.put(calValid.getTime(), dummyWeight);
        }

        Collections.sort(dateList, new Comparator<Date>() {
            public int compare(Date o1, Date o2) {
                return o1.compareTo(o2);
            }
        });

        for (WeightGraphContract weight : weightGraphDataArrayList) {
            Calendar calIndex = Calendar.getInstance();
            calIndex.setTime(convertStringToDate(weight.Date));
            int monthIndex = calIndex.get(Calendar.MONTH);

            if (convertStringToDate(weight.Date).before(cal.getTime()) && convertStringToDate(weight.Date).after((Date) dateList.get(0))) {
                for (Date date_list : dateList) {
                    Calendar calIndex_date = Calendar.getInstance();
                    calIndex_date.setTime(date_list);
                    if (monthIndex == calIndex_date.get(Calendar.MONTH)) {
                        weightDate.put(date_list, weight);
                    }
                }
            }
        }

        for (Date date_list : dateList) {
            weightGraphDataArrayList_3m.add(weightDate.get(date_list));
        }

        return weightGraphDataArrayList_3m;
    }

    public static List<WeightGraphContract> get1MWeightList(boolean initDate, int dateIndex) {

        ArrayList<WeightGraphContract> weightGraphDataArrayList_1m = new ArrayList<WeightGraphContract>();
        List<WeightGraphContract> weightGraphDataArrayList = ApplicationData.getInstance().weightGraphContractList;
        Hashtable<Date, WeightGraphContract> weightDate = new Hashtable<Date, WeightGraphContract>();
        ArrayList<Date> dateList;

        Calendar cal = Calendar.getInstance();
        Calendar calValid = Calendar.getInstance();

        if (initDate) {
            cal.setTime(new Date());
            dateList = new ArrayList<>(31);
        } else {
            cal.setTime(ApplicationData.getInstance().currentDateRangeDisplay_date);
            calValid.setTime(ApplicationData.getInstance().currentDateRangeDisplay_date);

            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

            calValid.set(Calendar.MONTH, calValid.get(Calendar.MONTH));
            calValid.set(Calendar.DAY_OF_MONTH, calValid.getActualMaximum(Calendar.DAY_OF_MONTH));
            dateList = new ArrayList<>(cal.getActualMaximum(Calendar.DAY_OF_MONTH));

        }
        //include current date;
        dateList.add(cal.getTime());

        WeightGraphContract dummyWeight = new WeightGraphContract();
        dummyWeight.Date = cal.getTime().toString();
        dummyWeight.WeightKg = 0;

        weightDate.put(cal.getTime(), dummyWeight);

        for (int i = 1; i < 31; i++) {
            calValid.add(Calendar.DAY_OF_MONTH, -1);
            dateList.add(calValid.getTime());

            dummyWeight = new WeightGraphContract();
            dummyWeight.Date = calValid.getTime().toString();
            dummyWeight.WeightKg = 0;

            weightDate.put(calValid.getTime(), dummyWeight);
        }

        Collections.sort(dateList, new Comparator<Date>() {
            public int compare(Date o1, Date o2) {
                return o1.compareTo(o2);
            }
        });

        for (WeightGraphContract weight : weightGraphDataArrayList) {
            Calendar calIndex = Calendar.getInstance();
            calIndex.setTime(convertStringToDate(weight.Date));
            int dayIndex = calIndex.get(Calendar.DAY_OF_MONTH);

            if (convertStringToDate(weight.Date).before(cal.getTime()) && convertStringToDate(weight.Date).after((Date) dateList.get(0))) {
                for (Date date_list : dateList) {
                    Calendar calIndex_date = Calendar.getInstance();
                    calIndex_date.setTime(date_list);
                    if (dayIndex == calIndex_date.get(Calendar.DAY_OF_MONTH)) {
                        weightDate.put(date_list, weight);
                    }
                }
            }
        }

        for (Date date_list : dateList) {
            weightGraphDataArrayList_1m.add(weightDate.get(date_list));
        }

        Collections.sort(weightGraphDataArrayList_1m, new Comparator<WeightGraphContract>() {
            public int compare(WeightGraphContract o1, WeightGraphContract o2) {
                return o1.Date.compareTo(o2.Date);
            }
        });

        return weightGraphDataArrayList_1m;
    }

    public static double getHeighestWeight(List<WeightGraphContract> weightList) {
        double heighestWeight = 0.0;

        for (WeightGraphContract weight : weightList) {
            heighestWeight = weight.WeightKg > heighestWeight ? weight.WeightKg : heighestWeight;
        }
        return heighestWeight;
    }

    public static double getLowestWeight(List<WeightGraphContract> weightList) {
        double lowestWeight = 0.0;

        //do not include zero
        if (weightList.size() > 0) {
            //get lowest with value except 0
            for (WeightGraphContract weight : weightList) {
                if (weight.WeightKg > 0) {
                    lowestWeight = weight.WeightKg;
                    break;
                }
            }

            for (WeightGraphContract weight : weightList) {
                if (weight.WeightKg > 0) {
                    lowestWeight = weight.WeightKg < lowestWeight ? weight.WeightKg : lowestWeight;
                }
            }
        }
        return lowestWeight;
    }

    public static boolean isWeightDataHistory1Year() {

        boolean isTrue = true;

        //get date same year from present
        java.util.Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);

        List<WeightGraphContract> weightGraphDataArrayList = new ArrayList<WeightGraphContract>();
        weightGraphDataArrayList = ApplicationData.getInstance().weightGraphContractList;

        for (int i = 0; i < weightGraphDataArrayList.size(); i++) {
            Date dateIndex = convertStringToDate(weightGraphDataArrayList.get(i).Date);

            cal = Calendar.getInstance();
            cal.setTime(dateIndex);
            int yearIndex = cal.get(Calendar.YEAR);
            int monthIndex = cal.get(Calendar.MONTH);

            if (yearIndex == year - 1) {
                if (monthIndex <= month) {
                    return true;
                } else {
                    isTrue = false;
                }
            } else {
                if (yearIndex == year) {
                    if (monthIndex == 1 && month == 12) {
                        return true;
                    }
                }
                isTrue = false;
            }
        }
        return isTrue;
    }

    public static boolean isWeightDataHistory3MonthsMore() {

        boolean isTrue = true;

        //get date same year from present
        java.util.Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);

        int month3less = (month - 2) % 12;

        List<WeightGraphContract> weightGraphDataArrayList = new ArrayList<WeightGraphContract>();
        weightGraphDataArrayList = ApplicationData.getInstance().weightGraphContractList;

        for (int i = 0; i < weightGraphDataArrayList.size(); i++) {
            Date dateIndex = convertStringToDate(weightGraphDataArrayList.get(i).Date);

            cal = Calendar.getInstance();
            cal.setTime(dateIndex);
            int yearIndex = cal.get(Calendar.YEAR);
            int monthIndex = cal.get(Calendar.YEAR);

            //01 2016
            //11 2015
            //9 2015

            if (yearIndex == year - 1) {
                if (monthIndex >= month3less) {
                    return true;
                }
            } else {
                if (yearIndex == year) {
                    if (monthIndex >= month3less) {
                        return true;
                    }
                }
                isTrue = false;
            }
        }
        return isTrue;
    }

    public static boolean isWeightDataHistory3MonthsLess() {

        boolean isTrue = true;

        //get date same year from present
        java.util.Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);

        int month3less = (month - 2) % 12;

        List<WeightGraphContract> weightGraphDataArrayList = new ArrayList<WeightGraphContract>();
        weightGraphDataArrayList = ApplicationData.getInstance().weightGraphContractList;

        for (int i = 0; i < weightGraphDataArrayList.size(); i++) {
            Date dateIndex = convertStringToDate(weightGraphDataArrayList.get(i).Date);

            cal = Calendar.getInstance();
            cal.setTime(dateIndex);
            int yearIndex = cal.get(Calendar.YEAR);
            int monthIndex = cal.get(Calendar.YEAR);
            if (yearIndex == year - 1) {
                if (monthIndex < month3less) {
                    return true;
                }
            } else {
                if (yearIndex == year) {
                    if (monthIndex < month3less) {
                        return true;
                    }
                }
                isTrue = false;
            }
        }
        return isTrue;
    }

    public static String getWeightDateFormat(Date date) {

        String localTime = "";
        try {

            Calendar cal = Calendar.getInstance();
            TimeZone tz = cal.getTimeZone();

        /* date formatter in local timezone */
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

            sdf.setTimeZone(tz);
            localTime = sdf.format(date);

        } catch (NullPointerException e) {
            return "";
        }

        return localTime;
    }

}
