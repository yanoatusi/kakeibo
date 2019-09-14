package com.websarva.wings.android.kakeibo;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

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
    SimpleCursorAdapter dateSumAdapter;

    ListView _calendarFrgList;
    TextView _dateSum;
    CalendarView _cv;
    DatabaseHelper FrgDbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        View view2 = inflater.inflate(R.layout.fragment_calendar, container, false);

        _calendarFrgList = (ListView) view2.findViewById(R.id.list);
        _dateSum = view2.findViewById(R.id.subDp);
        _cv = (CalendarView) view2.findViewById(R.id.calendarView1);

        calendarFrgListDisp();

        _cv.setOnDateChangeListener(this);

        setupPieChart(view2.findViewById(R.id.y));

        return view2;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            calendarFrgListDisp();

            textViewDateSum();
        }

    }

    public void onSelectedDayChange(CalendarView view, int year, int month,
                                    int dayOfMonth) {
//        Toast.makeText(getContext(), year+"/"+(month + 1)+"/"+dayOfMonth, Toast.LENGTH_LONG ).show();// TODO Auto-generated method stub
        String _date;
        MainActivity mainActivity = (MainActivity) getActivity();
        try {
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy年MM月dd日");
                _date = year + "年" + (month + 1) + "月" + dayOfMonth + "日";
            Log.d("rrrrr", _date + "");
            Date selectDate = sdFormat.parse(_date);
            mainActivity.setTextView(sdFormat.format(selectDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        calendarFrgListDisp();

        textViewDateSum();

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
        String sql2 = "SELECT SUM(Price) FROM DatePrice WHERE Date =" + "'" + mainActivity.getSqlDate() + "'";
        dateSumCursor = dateSumDb.rawQuery(sql2, null);

        // データベース内のCursorを移動
        dateSumCursor.move(1);
        // TextViewを取得しデータベースの値を反映。
        _dateSum.setText(dateSumCursor.getString(0));

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
                (this.getContext(), db_lay, calendarFrgCursor, head, lay, 0);

        _calendarFrgList.setAdapter(calendarFrgAdapter);
        calendarFrgAdapter.notifyDataSetChanged();
    }
    float rainfall[] = {98.8f, 123.8f, 34.6f, 43.9f, 69.4f, 12.5f, 52.8f, 158.6f,
            203.6f, 30.7f, 160.7f, 159.7f };
    String monthNames[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
            "Sep", "Oct", "Nov", "Dec"};
    String[] columnName, type, nullable;
    private void setupPieChart(View view) {
        //PieEntriesのリストを作成する:
        FrgDbHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase d = FrgDbHelper.getReadableDatabase();
        String sql = "SELECT _Id,Date,Large_category,Price,Memo FROM DatePrice " +
                "INNER JOIN Category ON DatePrice.Category_Id = Category.Category_Id " +
                "WHERE Date LIKE" + "'%" + "月" + "%'"+
                "GROUP BY Large_category ";
        Cursor cursor = d.rawQuery(sql, null);

//Ge
        List<PieEntry> pieEntries = new ArrayList<>();
        for (int i = 0; i < rainfall.length; i++) {
            pieEntries.add(new PieEntry(rainfall[i], monthNames[i]));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "Rainfall for Vancouver");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);

        //PieChartを取得する:
        PieChart piechart = (PieChart) view;
        piechart.setData(data);
        piechart.invalidate();
    }

}














