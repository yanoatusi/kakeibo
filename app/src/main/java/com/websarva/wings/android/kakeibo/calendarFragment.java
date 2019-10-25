package com.websarva.wings.android.kakeibo;


import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class calendarFragment extends Fragment implements CalendarView.OnDateChangeListener {

    SQLiteDatabase calendarFrgDb;
    private DatabaseHelper calendarFrgDbHelper;
    Cursor calendarFrgCursor=null;
    SimpleCursorAdapter calendarFrgAdapter;

    SQLiteDatabase dateSumDb;
    private DatabaseHelper dateSumDbHelper;
    Cursor dateSumCursor=null;
    SQLiteDatabase dateSumDbMonth;
    private DatabaseHelper databaseHelperMonth;
    Cursor dateSumMonth=null;
    SimpleCursorAdapter dateSumAdapter;

    ListView _calendarFrgList;
    TextView _dateSum;
    CalendarView _cv;
    DatabaseHelper FrgDbHelper;
PieChart _piechart;
GridView _blankgrid;
TextView _sumdp;
TextView _sumdppiechart;
    AlertDialog alertDlg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        View view2 = inflater.inflate(R.layout.fragment_calendar, container, false);

        _calendarFrgList = (ListView) view2.findViewById(R.id.list);
        _dateSum = view2.findViewById(R.id.subDp);
        _cv = (CalendarView) view2.findViewById(R.id.calendarView1);
_piechart =view2.findViewById(R.id.y);
_blankgrid = view.findViewById(R.id.gridView);
_sumdp= view2.findViewById(R.id.subDp);


        _cv.setOnDateChangeListener(this);

        textViewDateSum();

        calendarFrgListDisp();
        setupPieChart(_piechart);
        _sumdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // ダイアログクラスをインスタンス化
                ChartDialog dialog1 = new ChartDialog();
                // 表示  getFagmentManager()は固定、sampleは識別タグ
                dialog1.show(getFragmentManager(), "sample");
                Log.d("wwww","rrr");
            }
        });
        float scalingFactor = 1.0f; // scale down to half the size
        _piechart.setScaleX(scalingFactor);
        _piechart.setScaleY(scalingFactor);
textViewDateSumPieChart();
        return view2;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            MainActivity mainActivity = (MainActivity) getActivity();

            _cv.setOnDateChangeListener(this);

            textViewDateSum();
            calendarFrgListDisp();
            setupPieChart(_piechart);
            _piechart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
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
            ymonth = sdFormat.format(selectDate).substring(0,8);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        _cv.setOnDateChangeListener(this);
        _piechart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        textViewDateSum();
        calendarFrgListDisp();
        setupPieChart(_piechart);
    }

    /**
     * 日付文字列をlong値に変換する
     * @param value
     * @return
     */
    public long convertDateStringToLong(String value) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Date parse = null ;
        try {
            parse = simpleDateFormat.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time = parse.getTime();
        return time;
    }

    //日付ごとの合計をTextViewに表示
    private void textViewDateSum(){
        MainActivity mainActivity = (MainActivity) getActivity();
        dateSumDbHelper = new DatabaseHelper(getActivity());

        // データ取り出し
        dateSumDb = dateSumDbHelper.getReadableDatabase();

        // SQLの実行。
        String sql2 = "SELECT SUM(Price) FROM DatePrice WHERE Date LIKE" + "'%" + mainActivity.getSqlDate() + "%'";
        dateSumCursor = dateSumDb.rawQuery(sql2, null);

        // データベース内のCursorを移動
        dateSumCursor.move(1);

        // TextViewを取得しデータベースの値を反映。
        _dateSum.setText(dateSumCursor.getString(0));

    }
    //日付ごとの合計をTextViewに表示
    private String textViewDateSumPieChart(){
        MainActivity mainActivity = (MainActivity) getActivity();
        databaseHelperMonth = new DatabaseHelper(getActivity());

        // データ取り出し
        dateSumDbMonth = databaseHelperMonth.getReadableDatabase();

        // SQLの実行。
        String sql2 = "SELECT SUM(Price) FROM DatePrice WHERE Date LIKE" + "'%" + mainActivity.getSqlDate().substring(0,8) + "%'";
        dateSumMonth = dateSumDbMonth.rawQuery(sql2, null);

        // データベース内のCursorを移動
        dateSumMonth.move(1);

        // TextViewを取得しデータベースの値を反映。
        return dateSumMonth.getString(0);
    }
    private void calendarFrgListDisp(){
        MainActivity mainActivity = (MainActivity) getActivity();
        //DBHelpderを作成する。この時にDBが作成される。
        calendarFrgDbHelper = new DatabaseHelper(getActivity());
        //DBを読み込み可能状態で開く。
        //※getWritableDatabase（書き込み可能状態でも読み込みはできる）
        SQLiteDatabase d = calendarFrgDbHelper.getReadableDatabase();
        //DBへクエリーを発行し、カーソルを取得する。
        String sql = "SELECT _Id,Date,Small_category,Price,Memo FROM DatePrice " +
                "INNER JOIN Category ON DatePrice.Category_Id = Category.Category_Id " +
                "WHERE Date =" + "'" + mainActivity.getSqlDate() + "'";
        calendarFrgCursor = d.rawQuery(sql, null);
        //取得したカーソルをカーソル用のアダプターに設定する。
        String[] head = {"Small_category", "Price", "Memo"};
        int[] lay = {R.id.name, R.id.price, R.id.memo};
        int db_lay = R.layout.listrow;
        calendarFrgAdapter = new SimpleCursorAdapter
                (getContext(), db_lay, calendarFrgCursor, head, lay, 0);

        _calendarFrgList.setAdapter(calendarFrgAdapter);
        calendarFrgAdapter.notifyDataSetChanged();
    }

    private void setupPieChart(View view) {
        //PieEntriesのリストを作成する:

        float[] floatArray = new float[floatContacts().length];
        for (int i = 0; i < floatContacts().length; i++){
            float x = Float.parseFloat(floatContacts()[i]);
            floatArray[i] = x;
        }
        List<PieEntry> pieEntries = new ArrayList<>();
        for (int i = 0; i < floatContacts().length; i++) {
            pieEntries.add(new PieEntry(floatArray[i], getContacts()[i]));
        }
        MainActivity mainActivity = (MainActivity) getActivity();
        PieDataSet dataSet = new PieDataSet(pieEntries, mainActivity.getSqlDate().substring(0,8));
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        PieData data = new PieData(dataSet);

        //PieChartを取得する:
        PieChart piechart = (PieChart) view;
        piechart.setCenterText("" +textViewDateSumPieChart()+"");
        piechart.setCenterTextSize(18f);

        piechart.setEntryLabelColor(Color.BLACK);
        piechart.setEntryLabelTextSize(16f);
        piechart.setData(data);
    }
    public String[] getContacts(){
        MainActivity mainActivity = (MainActivity) getActivity();
        FrgDbHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase d = FrgDbHelper.getReadableDatabase();
        String sql = "SELECT _Id,Date,Large_category,Price,Memo FROM DatePrice " +
                "INNER JOIN Category ON DatePrice.Category_Id = Category.Category_Id " +
                "WHERE Date LIKE" + "'%" + mainActivity.getSqlDate().substring(0,8) + "%'"+
                "GROUP BY Large_category";
        Cursor cursor = d.rawQuery(sql, null);
        cursor.moveToFirst();
        ArrayList<String> names = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            names.add(cursor.getString(cursor.getColumnIndex("Large_category")));
            cursor.moveToNext();
        }
        cursor.close();
        return names.toArray(new String[names.size()]);
    }

    public String[] floatContacts(){
        MainActivity mainActivity = (MainActivity) getActivity();
        FrgDbHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase d = FrgDbHelper.getReadableDatabase();
        String sql = "SELECT _Id,Date,Large_category,Price,Memo FROM DatePrice " +
                "INNER JOIN Category ON DatePrice.Category_Id = Category.Category_Id " +
                "WHERE Date LIKE" + "'%" + mainActivity.getSqlDate().substring(0,8) + "%'"+
                "GROUP BY Large_category ";
        Cursor cursor = d.rawQuery(sql, null);
        cursor.moveToFirst();
        ArrayList<String> names = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            names.add(cursor.getString(cursor.getColumnIndex("Price")));
            cursor.moveToNext();
        }
        cursor.close();
        return names.toArray(new String[names.size()]);
    }
    private class chartClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            float scalingFactor = 1.0f; // scale down to half the size
            view.setScaleX(scalingFactor);
            view.setScaleY(scalingFactor);
        }
    }
}














