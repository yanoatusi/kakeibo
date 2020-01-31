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
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class calendarFragment extends Fragment implements CalendarView.OnDateChangeListener {

    private DatabaseHelper calendarFrgDbHelper;
    Cursor calendarFrgCursor=null;
    SimpleCursorAdapter calendarFrgAdapter;

    SQLiteDatabase dateSumDb;
    private DatabaseHelper dateSumDbHelper;
    Cursor dateSumCursor=null;

    ListView _calendarFrgList;
    TextView _dateSum;
    CalendarView _cv;
    GridView _blankgrid;
    TextView _sumdp;
    EditText _nowDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view2 = inflater.inflate(R.layout.fragment_calendar, container, false);
        View view = inflater.inflate(R.layout.fragment_blank, container, false);

        _calendarFrgList = (ListView) view2.findViewById(R.id.list);
        _dateSum = view2.findViewById(R.id.subDp);
        _cv = (CalendarView) view2.findViewById(R.id.calendarView1);
        _blankgrid = getActivity().findViewById(R.id.gridView);
        _sumdp= view2.findViewById(R.id.subDp);
        _nowDate = view.findViewById(R.id.nowDate);

        _cv.setOnDateChangeListener(this);

            textViewDateSum();
            calendarFrgListDisp();

        Log.d("sddd",  "aaa");

        return view2;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            _cv.setOnDateChangeListener(this);

            textViewDateSum();
            calendarFrgListDisp();

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
Log.d("afa",_cv.getDate()+"");
        textViewDateSum();
        calendarFrgListDisp();

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
}