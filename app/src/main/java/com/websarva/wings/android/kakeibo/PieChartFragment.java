package com.websarva.wings.android.kakeibo;


import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PieChartFragment extends Fragment implements CalendarView.OnDateChangeListener {

    SQLiteDatabase dateSumDbMonth;
    private DatabaseHelper databaseHelperMonth;
    Cursor dateSumMonth = null;
    DatabaseHelper FrgDbHelper;
    com.github.mikephil.charting.charts.PieChart _piechart;
    Button _backMonth;
    EditText _nowMonth;
    Button _nextMonth;
    Calendar cl = Calendar.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view2 = inflater.inflate(R.layout.pei_chart, container, false);

        _backMonth = view2.findViewById(R.id.backMonth);
        _nowMonth = view2.findViewById(R.id.nowMonth);
        _nextMonth = view2.findViewById(R.id.nextMonth);
        _piechart = view2.findViewById(R.id.peichart);

        MainActivity mainActivity = (MainActivity) getActivity();
        Log.d("sddd2", mainActivity.getSqlDate());
        _nowMonth.setText(cl.get(Calendar.YEAR) + "年" + (cl.get(Calendar.MONTH)+1) +"月", TextView.BufferType.NORMAL);
        View.OnClickListener event = new View.OnClickListener() {
            // クリックしたら前月か次月
            public void onClick(View backNext) {
                // MainActivityのインスタンスを取得

                MainActivity mainActivity = (MainActivity) getActivity();
                switch (backNext.getId()) {
                    case R.id.backMonth:
                        cl.add(Calendar.MONTH, -1);
                        _nowMonth.setText(cl.get(Calendar.YEAR) + "年" + (cl.get(Calendar.MONTH)+1) +"月", TextView.BufferType.NORMAL);
                        setupPieChart(_piechart);
                        textViewDateSumPieChart();
                        break;
                    case R.id.nextMonth:
                        cl.add(Calendar.MONTH, +1);
                        _nowMonth.setText(cl.get(Calendar.YEAR) + "年" + (cl.get(Calendar.MONTH)+1) +"月", TextView.BufferType.NORMAL);
                        setupPieChart(_piechart);
                        textViewDateSumPieChart();
                        break;
                }
            }
        };
        _backMonth.setOnClickListener(event);
        _nextMonth.setOnClickListener(event);
        setupPieChart(_piechart);
        textViewDateSumPieChart();
        _nowMonth.setFocusable(false);
        return view2;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            MainActivity mainActivity = (MainActivity) getActivity();

//            setupPieChart(_piechart);
//            _piechart.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
        }

    }

    String ymonth;

    public void onSelectedDayChange(CalendarView view, int year, int month,
                                    int dayOfMonth) {

        String _date;
        MainActivity mainActivity = (MainActivity) getActivity();
        try {
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy年MM月dd日");
            _date = year + "年" + (month + 1) + "月" + dayOfMonth + "日";
            Log.d("rrrrr", _date + "");
            Date selectDate = sdFormat.parse(_date);
            mainActivity.setTextView(sdFormat.format(selectDate));
            ymonth = sdFormat.format(selectDate).substring(0, 8);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 日付文字列をlong値に変換する
     *
     * @param value
     * @return
     */
    public long convertDateStringToLong(String value) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Date parse = null;
        try {
            parse = simpleDateFormat.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time = parse.getTime();
        return time;
    }

    //日付ごとの合計をTextViewに表示
    private String textViewDateSumPieChart() {
        MainActivity mainActivity = (MainActivity) getActivity();
        databaseHelperMonth = new DatabaseHelper(getActivity());

        // データ取り出し
        dateSumDbMonth = databaseHelperMonth.getReadableDatabase();

        // SQLの実行。
        String sql2 = "SELECT SUM(Price) FROM DatePrice WHERE Date LIKE" + "'%" + _nowMonth.getText().toString() + "%'";
        dateSumMonth = dateSumDbMonth.rawQuery(sql2, null);

        // データベース内のCursorを移動
        dateSumMonth.move(1);

        // TextViewを取得しデータベースの値を反映。
        return dateSumMonth.getString(0);
    }

    private void setupPieChart(View view) {
        //PieEntriesのリストを作成する:

        float[] floatArray = new float[floatContacts().length];
        for (int i = 0; i < floatContacts().length; i++) {
            float x = Float.parseFloat(floatContacts()[i]);
            floatArray[i] = x;
        }
        List<PieEntry> pieEntries = new ArrayList<>();
        for (int i = 0; i < floatContacts().length; i++) {
            pieEntries.add(new PieEntry(floatArray[i], getContacts()[i]));
        }
        MainActivity mainActivity = (MainActivity) getActivity();
        PieDataSet dataSet = new PieDataSet(pieEntries, _nowMonth.getText().toString());
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        PieData data = new PieData(dataSet);

        //PieChartを取得する:
        com.github.mikephil.charting.charts.PieChart piechart = (com.github.mikephil.charting.charts.PieChart) view;
        piechart.setCenterText("" + textViewDateSumPieChart() + "");
        piechart.setCenterTextSize(18f);

        piechart.setEntryLabelColor(Color.BLACK);
        piechart.setEntryLabelTextSize(16f);
        piechart.setData(data);
    }

    public String[] getContacts() {
        MainActivity mainActivity = (MainActivity) getActivity();
        FrgDbHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase d = FrgDbHelper.getReadableDatabase();
        String sql = "SELECT _Id,Date,Large_category,Price,Memo FROM DatePrice " +
                "INNER JOIN Category ON DatePrice.Category_Id = Category.Category_Id " +
                "WHERE Date LIKE" + "'%" + _nowMonth.getText().toString() + "%'" +
                "GROUP BY Large_category";
        Cursor cursor = d.rawQuery(sql, null);
        cursor.moveToFirst();
        ArrayList<String> names = new ArrayList<String>();
        while (!cursor.isAfterLast()) {
            names.add(cursor.getString(cursor.getColumnIndex("Large_category")));
            cursor.moveToNext();
        }
        cursor.close();
        return names.toArray(new String[names.size()]);
    }

    public String[] floatContacts() {
        MainActivity mainActivity = (MainActivity) getActivity();
        FrgDbHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase d = FrgDbHelper.getReadableDatabase();
        String sql = "SELECT _Id,Date,Large_category,Price,Memo FROM DatePrice " +
                "INNER JOIN Category ON DatePrice.Category_Id = Category.Category_Id " +
                "WHERE Date LIKE" + "'%" + _nowMonth.getText().toString() + "%'" +
                "GROUP BY Large_category ";
        Cursor cursor = d.rawQuery(sql, null);
        cursor.moveToFirst();
        ArrayList<String> names = new ArrayList<String>();
        while (!cursor.isAfterLast()) {
            names.add(cursor.getString(cursor.getColumnIndex("Price")));
            cursor.moveToNext();
        }
        cursor.close();
        return names.toArray(new String[names.size()]);
    }


    public static String getNowMonth() {
        final DateFormat df = new SimpleDateFormat("yyyy年MM月");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }

}













