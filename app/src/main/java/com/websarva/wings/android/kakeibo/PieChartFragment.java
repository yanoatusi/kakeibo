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
import android.os.AsyncTask;
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
    SQLiteDatabase d;
    SQLiteDatabase e;
    private DatabaseHelper databaseHelperMonth;
    Cursor dateSumMonth = null;
    Cursor cursor = null;
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

        setnowMonth(_nowMonth);
        View.OnClickListener event = new View.OnClickListener() {
            // クリックしたら前月か次月
            public void onClick(View backNext) {
                switch (backNext.getId()) {
                    case R.id.backMonth:
                        cl.add(Calendar.MONTH, -1);
                        setnowMonth(_nowMonth);
                        setupPieChart(_piechart);
                        textViewDateSumPieChart();
                        break;
                    case R.id.nextMonth:
                        cl.add(Calendar.MONTH, +1);
                        setnowMonth(_nowMonth);
                        setupPieChart(_piechart);
                        textViewDateSumPieChart();
                        break;
                }
            }
        };
        _backMonth.setOnClickListener(event);
        _nextMonth.setOnClickListener(event);

        textViewDateSumPieChart();
        _nowMonth.setFocusable(false);
//        _piechart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SampleAsyncTask task = new SampleAsyncTask();
//                task.execute();
//            }
//        });
        return view2;

    }
    @Override
    public void onResume(){
        super.onResume();
        setupPieChart(_piechart);
    }
    private class SampleAsyncTask extends AsyncTask<Void, Long, Long> {

        public SampleAsyncTask() {

        }

        // バックグラウンド処理の前に実行される処理
        @Override
        protected void onPreExecute() {

        }

        // バックグラウンド処理完了後に実行される処理
        @Override
        protected void onPostExecute(Long result) {
            setupPieChart(_piechart);
        }

        // バックグラウンド処理
        @Override
        protected Long doInBackground(Void... params) {
long a=0;
            return a;
        }
    }


    public void onSelectedDayChange(CalendarView view, int year, int month,
                                    int dayOfMonth) {

    }

    //日付ごとの合計をTextViewに表示
    private String textViewDateSumPieChart() {
        MainActivity mainActivity = (MainActivity) getActivity();
        databaseHelperMonth = new DatabaseHelper(getActivity());

        // データ取り出し
        dateSumDbMonth = databaseHelperMonth.getReadableDatabase();

        // SQLの実行。
        String sql2 = "SELECT SUM(Price) FROM DatePrice WHERE Date LIKE" + "'%" + getSqlDate() + "%'";
        dateSumMonth = dateSumDbMonth.rawQuery(sql2, null);

        // データベース内のCursorを移動
        dateSumMonth.move(1);

        // TextViewを取得しデータベースの値を反映。
        return dateSumMonth.getString(0);
    }

    private void setupPieChart(View view) {
        //PieEntriesのリストを作成する:

        List<PieEntry> pieEntries = new ArrayList<>();
        for (int i = 0; i < floatContacts().length; i++) {
            pieEntries.add(new PieEntry(Float()[i], getContacts()[i]));
        }
        PieDataSet dataSet = new PieDataSet(pieEntries, getSqlDate());
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        PieData data = new PieData(dataSet);

        //PieChartを取得する:
        _piechart.setData(data);
        Log.d("dsfr",textViewDateSumPieChart()+"");
        _piechart.setCenterText("" + textViewDateSumPieChart() + "");
        _piechart.setCenterTextSize(18f);
        _piechart.setEntryLabelColor(Color.BLACK);
        _piechart.setEntryLabelTextSize(16f);
        if (textViewDateSumPieChart() == null) {
            _piechart.setVisibility(View.GONE);
        } else {
            _piechart.setVisibility(View.VISIBLE);
        }
    }

    public String[] getContacts() {
        FrgDbHelper = new DatabaseHelper(getActivity());
        d = FrgDbHelper.getReadableDatabase();
        String sql = "SELECT _Id,Date,Large_category,Price,Memo FROM DatePrice " +
                "INNER JOIN Category ON DatePrice.Category_Id = Category.Category_Id " +
                "WHERE Date LIKE" + "'%" + getSqlDate() + "%'" +
                "GROUP BY Large_category";

        cursor = d.rawQuery(sql, null);
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
        FrgDbHelper = new DatabaseHelper(getActivity());
        d = FrgDbHelper.getReadableDatabase();
        String sql = "SELECT _Id,Date,Large_category,Price,Memo FROM DatePrice " +
                "INNER JOIN Category ON DatePrice.Category_Id = Category.Category_Id " +
                "WHERE Date LIKE" + "'%" + getSqlDate() + "%'" +
                "GROUP BY Large_category ";
        Log.d("etyu3",_nowMonth.getText().toString()+"");
        cursor = d.rawQuery(sql, null);
        cursor.moveToFirst();
        ArrayList<String> names = new ArrayList<String>();
        while (!cursor.isAfterLast()) {
            names.add(cursor.getString(cursor.getColumnIndex("Price")));
            cursor.moveToNext();
        }
        cursor.close();
        return names.toArray(new String[names.size()]);
    }
    public float[] Float() {
        float[] floatArray = new float[floatContacts().length];
        for (int i = 0; i < floatContacts().length; i++) {
            float x = Float.parseFloat(floatContacts()[i]);
            floatArray[i] = x;
        }
        return floatArray;
    }
    public String getSqlDate(){
        return _nowMonth.getText().toString();
    }
    public void setnowMonth(EditText dialogBtn) {
        try {

            int year = cl.get(Calendar.YEAR);
            int month = cl.get(Calendar.MONTH);
            int day = cl.get(Calendar.DAY_OF_MONTH);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
            String date = year + "年" + (month + 1) + "月";
            Date selectDate = sdf.parse(date);
            dialogBtn.setText(sdf.format(selectDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}













