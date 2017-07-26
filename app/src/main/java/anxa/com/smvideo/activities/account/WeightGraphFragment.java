package anxa.com.smvideo.activities.account;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import anxa.com.smvideo.ApplicationData;
import anxa.com.smvideo.R;
import anxa.com.smvideo.connection.ApiCaller;
import anxa.com.smvideo.connection.http.AsyncResponse;
import anxa.com.smvideo.contracts.VideoResponseContract;
import anxa.com.smvideo.contracts.WeightGraphContract;
import anxa.com.smvideo.contracts.WeightGraphResponseContract;
import anxa.com.smvideo.contracts.WeightHistoryContract;
import anxa.com.smvideo.contracts.WeightHistoryDataContract;
import anxa.com.smvideo.contracts.WeightHistoryResponseContract;
import anxa.com.smvideo.ui.WeightLogsListAdapter;
import anxa.com.smvideo.util.AppUtil;


/**
 * Created by aprilanxa on 28/06/2017.
 */

public class WeightGraphFragment extends Fragment implements View.OnClickListener, View.OnKeyListener, OnChartGestureListener, OnChartValueSelectedListener {

    private Context context;
    protected ApiCaller caller;

    private View mView;

    private LineChart weightLineChart;
    private TextView dateRange_tv;
    private Button date_left_btn;
    private Button date_right_btn;
    private Button weight_1m;
    private Button weight_3m;
    private Button weight_1y;
    private Button weight_all;
    private Button weight_submitBtn;
    private EditText weight_enter_et;
    private WeightGraphContract latestWeight;
    private TextView currentWeightTitle;

    private TextView targetWeightValue_tv, lostWeightValue_tv, bmiValue_tv;

    private int selectedDateRange;

    private final int DATE_RANGE_1M = 0;
    private final int DATE_RANGE_3M = 1;
    private final int DATE_RANGE_1Y = 2;
    private final int DATE_RANGE_ALL = 3;
    private final int DATE_RANGE_1W = 4;
    private int dateRangeIndex = 0;

    private double targetWeight = 0.0;
    private double initialWeight = 0.0;
    private double lowestWeight = 0.0;
    private double heighestWeight = 0.0;
    private double bmi = 0.0;

    private boolean fromSubmitWeight = false;

    private ListView weightLogsListView;
    private List<WeightGraphContract> weightGraphDataArrayList = new ArrayList<>();
    private List<WeightHistoryContract> weightGraphHistoryDataList = new ArrayList<>();
    private List<WeightHistoryContract> weightLogsListCurrentDisplay = new ArrayList<>();
    private WeightLogsListAdapter weightLogsListAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.context = getActivity();
        mView = inflater.inflate(R.layout.weight_graph, null);

        caller = new ApiCaller();

        IntentFilter filter = new IntentFilter();
        filter.addAction(this.getResources().getString(R.string.WEIGHT_GRAPH_BROADCAST_DELETE));
        filter.addAction(this.getResources().getString(R.string.WEIGHT_GRAPH_BROADCAST_EDIT));

        context.getApplicationContext().registerReceiver(the_receiver, filter);


        //header change
        ((TextView) (mView.findViewById(R.id.header_title_tv))).setText(getString(R.string.menu_account_poids));
        ((TextView) (mView.findViewById(R.id.header_right_tv))).setVisibility(View.INVISIBLE);

        weightLineChart = (LineChart) mView.findViewById(R.id.viewcontentGraph);
        weightLineChart.setVisibility(View.VISIBLE);

        weight_1m = (Button) mView.findViewById(R.id.weight_1m_btn);
        weight_3m = (Button) mView.findViewById(R.id.weight_3m_btn);
        weight_1y = (Button) mView.findViewById(R.id.weight_1y_btn);
        weight_all = (Button) mView.findViewById(R.id.weight_all_btn);
        weight_submitBtn = (Button) mView.findViewById(R.id.weight_submitButton);

        weightLogsListView = (ListView) mView.findViewById(R.id.weight_graph_listView);

        dateRange_tv = (TextView) mView.findViewById(R.id.weight_date_range);
        date_left_btn = (Button) mView.findViewById(R.id.date_left_btn);
        date_right_btn = (Button) mView.findViewById(R.id.date_right_btn);

        targetWeightValue_tv = (TextView) mView.findViewById(R.id.targetWeightValue_tv);
        lostWeightValue_tv = (TextView) mView.findViewById(R.id.lostWeightValue_tv);
        bmiValue_tv = (TextView) mView.findViewById(R.id.bmiValue_tv);

        currentWeightTitle = (TextView) mView.findViewById(R.id.currentWeight_tv);

        targetWeight = ApplicationData.getInstance().dietProfilesDataContract.TargetWeightInKg;

        date_left_btn.setOnClickListener(this);
        date_right_btn.setOnClickListener(this);
        weight_1m.setOnClickListener(this);
        weight_3m.setOnClickListener(this);
        weight_1y.setOnClickListener(this);
        weight_all.setOnClickListener(this);
        weight_submitBtn.setOnClickListener(this);

        mView.findViewById(R.id.weight_data_rl).setOnClickListener(this);

        weight_enter_et = (EditText) mView.findViewById(R.id.weight_enter_et);
        weight_enter_et.setOnKeyListener(this);

        getWeightHistoryAPI();

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();

        dismissKeyboard();
    }

    @Override
    public void onClick(final View v) {
        fromSubmitWeight = false;

        if (v.getId() == R.id.weight_1m_btn) {
            selectedDateRange = DATE_RANGE_1M;
            weight_1m.setSelected(true);
            weight_3m.setSelected(false);
            weight_1y.setSelected(false);
            weight_all.setSelected(false);
            dateRange_tv.setText(AppUtil.get1MDateRangeDisplay(true, true, dateRangeIndex));
            dateRangeIndex = 0;
            date_right_btn.setTextColor(ContextCompat.getColor(context, R.color.text_darkgray));
            updateWeightGraph(DATE_RANGE_1M);
        } else if (v.getId() == R.id.weight_3m_btn) {
            selectedDateRange = DATE_RANGE_3M;
            weight_3m.setSelected(true);
            weight_1m.setSelected(false);
            weight_1y.setSelected(false);
            weight_all.setSelected(false);
            dateRange_tv.setText(AppUtil.get3MDateRangeDisplay(true, true, dateRangeIndex));
            date_right_btn.setTextColor(ContextCompat.getColor(context, R.color.text_darkgray));

            dateRangeIndex = 0;
            updateWeightGraph(DATE_RANGE_3M);
        } else if (v.getId() == R.id.weight_1y_btn) {
            selectedDateRange = DATE_RANGE_1Y;
            weight_1y.setSelected(true);
            weight_1m.setSelected(false);
            weight_all.setSelected(false);
            weight_3m.setSelected(false);
            dateRange_tv.setText(AppUtil.get1YDateRangeDisplay(true, false));
            dateRangeIndex = 0;
            date_right_btn.setTextColor(ContextCompat.getColor(context, R.color.text_darkgray));
            updateWeightGraph(DATE_RANGE_1Y);
        } else if (v.getId() == R.id.weight_all_btn) {
            selectedDateRange = DATE_RANGE_ALL;
            weight_all.setSelected(true);
            weight_1m.setSelected(false);
            weight_3m.setSelected(false);
            weight_1y.setSelected(false);
            date_right_btn.setTextColor(ContextCompat.getColor(context, R.color.text_darkgray));
            dateRange_tv.setText(AppUtil.get1YDateRangeDisplay(true, false));
            dateRangeIndex = 0;
            updateWeightGraph(DATE_RANGE_ALL);
        } else if (v.getId() == R.id.weight_submitButton) {
            postWeightData();
        } else if (v == date_left_btn) {
            if (selectedDateRange == DATE_RANGE_1M) {
                dateRangeIndex++;
                dateRange_tv.setText(AppUtil.get1MDateRangeDisplay(false, true, dateRangeIndex));

                weightGraphDataArrayList = AppUtil.get1MWeightList(false, dateRangeIndex);
                populateData();

            } else if (selectedDateRange == DATE_RANGE_3M) {
                dateRangeIndex++;
                dateRange_tv.setText(AppUtil.get3MDateRangeDisplay(false, true, dateRangeIndex));
                weightGraphDataArrayList = AppUtil.get3MWeightList(false);
                populateData();
            } else if (selectedDateRange == DATE_RANGE_1Y || selectedDateRange == DATE_RANGE_ALL) {
                dateRangeIndex++;
                dateRange_tv.setText(AppUtil.get1YDateRangeDisplay(false, true));

                weightGraphDataArrayList = AppUtil.get1YWeightList(false, dateRangeIndex);
                populateData();
            }
            date_right_btn.setTextColor(ContextCompat.getColor(context, R.color.text_orangedark));

        } else if (v == date_right_btn) {
            if (selectedDateRange == DATE_RANGE_1M) {
                if (dateRangeIndex > 0) {
                    dateRangeIndex--;
                    dateRange_tv.setText(AppUtil.get1MDateRangeDisplay(false, false, dateRangeIndex));
                    weightGraphDataArrayList = AppUtil.get1MWeightList(false, dateRangeIndex);
                    populateData();
                    if (dateRangeIndex <= 0) {
                        date_right_btn.setTextColor(ContextCompat.getColor(context, R.color.text_darkgray));
                    }
                } else {
                    date_right_btn.setTextColor(ContextCompat.getColor(context, R.color.text_darkgray));
                }
            } else if (selectedDateRange == DATE_RANGE_3M) {
                if (dateRangeIndex > 0) {
                    dateRangeIndex--;
                    dateRange_tv.setText(AppUtil.get3MDateRangeDisplay(false, false, dateRangeIndex));
                    weightGraphDataArrayList = AppUtil.get3MWeightList(false);
                    populateData();

                    if (dateRangeIndex <= 0) {
                        date_right_btn.setTextColor(ContextCompat.getColor(context, R.color.text_darkgray));
                    }
                } else {
                    date_right_btn.setTextColor(ContextCompat.getColor(context, R.color.text_darkgray));
                }
            } else if (selectedDateRange == DATE_RANGE_1Y || selectedDateRange == DATE_RANGE_ALL) {
                if (dateRangeIndex > 0) {
                    dateRangeIndex--;
                    dateRange_tv.setText(AppUtil.get1YDateRangeDisplay(false, false));
                    weightGraphDataArrayList = AppUtil.get1YWeightList(false, dateRangeIndex);
                    populateData();
                    if (dateRangeIndex <= 0) {
                        date_right_btn.setTextColor(ContextCompat.getColor(context, R.color.text_darkgray));
                    }
                } else {
                    date_right_btn.setTextColor(ContextCompat.getColor(context, R.color.text_darkgray));
                }
            }
        } else if (v.getId() == R.id.weight_data_rl) {
            dismissKeyboard();
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        // If the event is a key-down event on the "enter" button
        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
            postWeightData();
            // Perform action on key press
            return true;
        }
        return false;
    }


    private void updateWeightGraphUI() {

        dateRange_tv.setText(AppUtil.get1MDateRangeDisplay(true, true, 1));

        Collections.sort(ApplicationData.getInstance().weightGraphContractList, new Comparator<WeightGraphContract>() {
            public int compare(WeightGraphContract o1, WeightGraphContract o2) {
                return AppUtil.toDate(o2.Date).compareTo(AppUtil.toDate(o1.Date));
            }
        });

        latestWeight = ApplicationData.getInstance().weightGraphContractList.get(0);

        System.out.println("GetAccountGraphData latest weight: " + latestWeight.Date);
        System.out.println("GetAccountGraphData latest weight: " + AppUtil.getWeightDateFormat(AppUtil.toDate(latestWeight.Date)));
        System.out.println("GetAccountGraphData latest weight: " + latestWeight.WeightKg);

        String currentWeightString = getResources().getString(R.string.WEIGHT_GRAPH_CURRENT_WEIGHT_ASOF) + " " + AppUtil.getWeightDateFormat(AppUtil.toDate(latestWeight.Date));
        String currentWeight = String.valueOf(latestWeight.WeightKg);
        final String stringToDisplay = currentWeightString.replace("%@", currentWeight);

        if (ApplicationData.getInstance().initialWeightContract != null){
            initialWeight = ApplicationData.getInstance().initialWeightContract.WeightKg;
        }

        final double weightLost = initialWeight - latestWeight.WeightKg;

        currentWeightTitle.setText(stringToDisplay);
        dateRange_tv.setText(AppUtil.get1MDateRangeDisplay(true, true, 1));

        targetWeightValue_tv.setText(String.valueOf(targetWeight) + " kg");
        lostWeightValue_tv.setText(String.format("%.1f", weightLost) + " kg");
        bmiValue_tv.setText(String.format("%.1f", latestWeight.Bmi));

        if (weightGraphDataArrayList.size() < 10) {
            mView.findViewById(R.id.weight_graph_date_ll).setVisibility(View.GONE);
            updateWeightGraph(DATE_RANGE_ALL);
        } else {
            mView.findViewById(R.id.weight_graph_date_ll).setVisibility(View.VISIBLE);
            weight_1m.setSelected(false);
            weight_3m.setSelected(false);
            weight_1y.setSelected(false);
            weight_all.setSelected(false);

            if (fromSubmitWeight) {
                selectedDateRange = DATE_RANGE_1M;
                weight_1m.setSelected(true);
                dateRange_tv.setText(AppUtil.get1MDateRangeDisplay(true, true, dateRangeIndex));
            } else {
                if (AppUtil.isWeightDataHistory1Year()) {
                    selectedDateRange = DATE_RANGE_1Y;
                    weight_1y.setSelected(true);
                    weight_all.setVisibility(View.VISIBLE);
                    dateRange_tv.setText(AppUtil.get1YDateRangeDisplay(true, true));
                } else if (AppUtil.isWeightDataHistory3MonthsMore()) {
                    selectedDateRange = DATE_RANGE_3M;
                    weight_3m.setSelected(true);
                    weight_all.setVisibility(View.GONE);
                    dateRange_tv.setText(AppUtil.get3MDateRangeDisplay(true, true, dateRangeIndex));
                } else if (AppUtil.isWeightDataHistory3MonthsLess()) {
                    selectedDateRange = DATE_RANGE_1M;
                    weight_1m.setSelected(true);
                    dateRange_tv.setText(AppUtil.get1MDateRangeDisplay(true, true, dateRangeIndex));
                } else {
                    selectedDateRange = DATE_RANGE_ALL;
                    weight_all.setSelected(true);
                }
            }
            updateWeightGraph(selectedDateRange);
        }

    }

    private void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(weight_enter_et.getWindowToken(), 0);
    }

    private void updateWeightGraph(int range) {
        switch (range) {
            case DATE_RANGE_1M:
                weightGraphDataArrayList = AppUtil.get1MWeightList(true, 0);
                break;
            case DATE_RANGE_3M:
                weightGraphDataArrayList = AppUtil.get3MWeightList(true);
                break;
            case DATE_RANGE_1Y:
                weightGraphDataArrayList = AppUtil.get1YWeightList(true, 0);
                break;
            case DATE_RANGE_ALL:
                weightGraphDataArrayList = AppUtil.getAllWeightList();
                break;
            default:
                break;
        }
        populateData();
    }

    /**
     * Weightloss Graph
     **/
    private void populateData() {
        lowestWeight = AppUtil.getLowestWeight(weightGraphDataArrayList);
        heighestWeight = AppUtil.getHeighestWeight(weightGraphDataArrayList);

        System.out.println("lowestWeight: " + lowestWeight);
        System.out.println("heighestWeight: " + heighestWeight);

        createGraph(weightGraphDataArrayList);
    }

    private void createGraph(List<WeightGraphContract> graphMealList) {

        weightLineChart.clear();

        weightLineChart.setOnChartGestureListener(this);
        weightLineChart.setOnChartValueSelectedListener(this);
        weightLineChart.setDrawGridBackground(false);

        // no description text
        weightLineChart.setDescription("");
        weightLineChart.setNoDataTextDescription(getResources().getString(R.string.WEIGHT_GRAPH_NO_DATA));

        // enable value highlighting
        weightLineChart.setHighlightEnabled(true);

        // enable touch gestures
        weightLineChart.setTouchEnabled(false);

        // enable scaling and dragging
        weightLineChart.setDragEnabled(true);
        weightLineChart.setScaleEnabled(true);
        weightLineChart.setScaleXEnabled(false);
        weightLineChart.setScaleYEnabled(true);
        weightLineChart.getLegend().setEnabled(false);
        weightLineChart.setDrawBorders(true);
        weightLineChart.setBorderWidth(0.5f);
        weightLineChart.setBorderColor(Color.LTGRAY);

        // if disabled, scaling can be done on x- and y-axis separately
        weightLineChart.setPinchZoom(true);

        // x-axis limit line
        LimitLine llXAxis = new LimitLine((float) targetWeight, "Objective: " + (int) targetWeight + "kg");
        llXAxis.setLineWidth(2f);
        llXAxis.setLineColor(Color.parseColor("#ffa135"));
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
        llXAxis.setTextSize(8f);
        llXAxis.setTextColor(Color.GRAY);

        XAxis xAxis = weightLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.GRAY);
        xAxis.setGridColor(Color.LTGRAY);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawAxisLine(true);
        xAxis.setGridLineWidth(0.5f);
        xAxis.setAxisLineColor(Color.LTGRAY);
        xAxis.setSpaceBetweenLabels(1);

        YAxis rightAxis = weightLineChart.getAxisRight();
        rightAxis.setEnabled(false);

        YAxis leftAxis = weightLineChart.getAxisLeft();
        leftAxis.setEnabled(true);
        leftAxis.setDrawAxisLine(true);
        //set grid color to light gray - 10/20/15
        leftAxis.setGridColor(Color.LTGRAY);
        leftAxis.setGridLineWidth(0.5f);
        leftAxis.addLimitLine(llXAxis);

        if (heighestWeight > initialWeight) {
            leftAxis.setAxisMaxValue((float) heighestWeight + 3);
        } else {
            leftAxis.setAxisMaxValue((float) initialWeight + 3);
        }

        if (lowestWeight < targetWeight) {
            leftAxis.setAxisMinValue((float) lowestWeight - 3);
        } else {
            leftAxis.setAxisMinValue((float) targetWeight - 3);
        }

        leftAxis.setTextColor(Color.GRAY);

        dataToGraphArray(graphMealList);

        weightLineChart.setBackgroundColor(Color.WHITE);

        leftAxis.setStartAtZero(false);

        weightLineChart.getAxisLeft().setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, YAxis yAxis) {
                return String.valueOf((int) Math.floor(v));
            }
        });
        weightLineChart.invalidate();
    }

    /**
     * @param graphMealList - list of GraphMeals to be plotted in line graph
     **/
    private void dataToGraphArray(List<WeightGraphContract> graphMealList) {

        List<String> dayArray = new ArrayList<>();
        ArrayList<Entry> valsList = new ArrayList<>();

        for (int i = 0; i < graphMealList.size(); i++) {
            if (graphMealList.get(i).WeightKg > 0) {
                Entry c1e1 = new Entry((float) graphMealList.get(i).WeightKg, i);
                valsList.add(c1e1);
            }
            String dayData = "";

            if (selectedDateRange == DATE_RANGE_1M) {
                dayData = new SimpleDateFormat("MM/dd").format(AppUtil.toDate(graphMealList.get(i).Date));
            } else {
                dayData = new SimpleDateFormat("MMM").format(AppUtil.toDate(graphMealList.get(i).Date));
            }
            dayArray.add(dayData);
        }

        LineDataSet dataSet1 = new LineDataSet(valsList, "Data 1");
        dataSet1.setDrawValues(false);
        dataSet1.setLineWidth(2.0f);
        dataSet1.setColor(Color.parseColor("#4CB6EB"));
        dataSet1.setCircleColor(Color.parseColor("#4CB6EB"));
        dataSet1.setCircleSize(3.0f);
        dataSet1.setCircleColorHole(Color.parseColor("#4CB6EB"));
        dataSet1.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet1.setDrawCubic(true);

        LineData data = new LineData(dayArray, dataSet1);

        weightLineChart.setData(data);
    }

    private void getWeightGraphData() {
        caller.GetAccountGraphData(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {

                //INITIALIZE ALL ONCLICK AND API RELATED PROCESS HERE TO AVOID CRASHES
                if (output != null) {
                    WeightGraphResponseContract c = (WeightGraphResponseContract) output;

                    if (c != null && c.Data != null && c.Data.Weights != null) {

                            for (WeightGraphContract weightGraphContract: c.Data.Weights){
                                if (weightGraphContract.ConfirmedData.equalsIgnoreCase("true")){
                                    ApplicationData.getInstance().weightGraphContractList.add(weightGraphContract);
                                }

                        }
                        updateWeightGraphUI();

                    }
                }
            }
        });
    }
    private void getWeightHistoryAPI() {
        caller.GetAccountGraphHistory(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {

                //INITIALIZE ALL ONCLICK AND API RELATED PROCESS HERE TO AVOID CRASHES
                if (output != null) {
                    WeightHistoryResponseContract c = (WeightHistoryResponseContract) output;

                    if (c != null && c.Data != null) {
                        initialWeight = c.Data.InitialWeightModel.WeightKg;

                        if (c.Data.HistoryModel!=null){
                            for (WeightHistoryContract weightHistoryContract: c.Data.HistoryModel){
                                if (weightHistoryContract.ConfirmedData.equalsIgnoreCase("true")){
                                    weightGraphHistoryDataList.add(weightHistoryContract);
                                }
                            }
                        }
//                        weightGraphHistoryDataList = c.Data.HistoryModel;
                        ApplicationData.getInstance().initialWeightContract = c.Data.InitialWeightModel;

                        updateWeightHistoryList();

                    }

                    getWeightGraphData();
                }
            }
        });
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onNothingSelected() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onChartGestureStart(MotionEvent me,
                                    ChartTouchListener.ChartGesture lastPerformedGesture) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onChartGestureEnd(MotionEvent me,
                                  ChartTouchListener.ChartGesture lastPerformedGesture) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2,
                             float velocityX, float velocityY) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        // TODO Auto-generated method stub

    }

    private void updateWeightHistoryList() {
        Collections.sort(weightGraphHistoryDataList, new Comparator<WeightHistoryContract>() {
            public int compare(WeightHistoryContract o1, WeightHistoryContract o2) {
                return AppUtil.toDate(o2.Date).compareTo(AppUtil.toDate(o1.Date));
            }
        });

        if (fromSubmitWeight) {
            weightLogsListCurrentDisplay.clear();
            selectedDateRange = DATE_RANGE_1M;
            weight_1m.setSelected(true);
            weight_3m.setSelected(false);
            weight_1y.setSelected(false);
            weight_all.setSelected(false);

            dateRange_tv.setText(AppUtil.get1MDateRangeDisplay(true, true, dateRangeIndex));
            dateRangeIndex = 0;
            date_right_btn.setTextColor(getResources().getColor(R.color.text_darkgray));

            updateWeightGraph(DATE_RANGE_1M);
        }

        if (weightGraphHistoryDataList.size() > 7) {

            for (int i = 0; i < 7; i++) {
                weightLogsListCurrentDisplay.add(weightGraphHistoryDataList.get(i));
            }

        } else {
//            hideSeeMoreButton(weightLogsListView);
            weightLogsListCurrentDisplay = weightGraphHistoryDataList;
        }

        if (weightLogsListView.getAdapter() == null) {
            weightLogsListAdapter = new WeightLogsListAdapter(context, weightLogsListCurrentDisplay);
            weightLogsListAdapter.notifyDataSetChanged();
            weightLogsListView.invalidateViews();
            weightLogsListView.refreshDrawableState();
            weightLogsListView.setAdapter(weightLogsListAdapter);
        } else {
            weightLogsListAdapter.updateItems(weightLogsListCurrentDisplay);
        }
        AppUtil.setListViewHeightBasedOnChildren(weightLogsListView);
    }

    private final BroadcastReceiver the_receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(context.getString(R.string.WEIGHT_GRAPH_BROADCAST_DELETE))) {
                deleteWeightData(ApplicationData.getInstance().currentWeight);
            } else if (intent.getAction().equalsIgnoreCase(context.getString(R.string.WEIGHT_GRAPH_BROADCAST_EDIT))) {
                updateWeightData(ApplicationData.getInstance().currentWeight);
            }
        }
    };


    private void deleteWeightData(final WeightHistoryContract weightToDelete) {
        dismissKeyboard();

        weightToDelete.Deleted = true;

        caller.EditWeight(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {

                //INITIALIZE ALL ONCLICK AND API RELATED PROCESS HERE TO AVOID CRASHES
                if (output != null) {
                        getWeightHistoryAPI();
                }
            }
        }, weightToDelete);
    }


    private void updateWeightData(WeightHistoryContract weightToUpdate) {

        dismissKeyboard();
        caller.EditWeight(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {

                //INITIALIZE ALL ONCLICK AND API RELATED PROCESS HERE TO AVOID CRASHES
                if (output != null) {
                    System.out.println("GetAccountGraphHistory: " + output);
                    getWeightHistoryAPI();
                }
            }
        }, weightToUpdate);
    }


    private void postWeightData() {

        dismissKeyboard();

        String weightInput_tv = weight_enter_et.getText().toString();

        if (weightInput_tv.length() > 0) {
            if (weightInput_tv.contains(",")) {
                weightInput_tv.replace(",", ".");
            }
        } else {
            return;
        }

        Float weightInput = Float.parseFloat(weightInput_tv);

        long unixTime = System.currentTimeMillis() / 1000L;
        final String toDate = String.valueOf(unixTime);

        WeightHistoryContract weightToPost = new WeightHistoryContract();
        weightToPost.Date = unixTime;
        weightToPost.WeightKg = weightInput;
        weightToPost.Deleted = false;
        weightToPost.UserId = ApplicationData.getInstance().regId;

        caller.PostWeight(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {

                //INITIALIZE ALL ONCLICK AND API RELATED PROCESS HERE TO AVOID CRASHES
                if (output != null) {

                    System.out.println("PostWeight: " + output);

                    getWeightHistoryAPI();

                }
            }
        }, weightToPost);

    }

}
