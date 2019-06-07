package com.websarva.wings.android.kakeibo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.websarva.wings.android.kakeibo.UserInfoViewPagerAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity   {
//
    private String sqlDate;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Cursor cursor=null;
    private SimpleCursorAdapter simpleCursorAdapter;

    private NonSwipeableViewPager pager;

    private FragmentPagerAdapter adapter;

    private int currentPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pager = (NonSwipeableViewPager)findViewById(R.id.pager);

        adapter = new UserInfoViewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        currentPage = 0;
//        NonSwipeableViewPager a = new NonSwipeableViewPager();
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(pager);
        tabLayout.getTabAt(0).setText("ホーム");
        tabLayout.getTabAt(1).setText("カレンダー");

    }

    public void onClickNext(View view) {
        currentPage ++;
        pager.setCurrentItem(currentPage);
    }
    public EditText set_nowDate(){
        EditText editText = findViewById(R.id.nowDate);
        return editText;
    }
    public void onClickGoToTop(View view) {
        currentPage = 0;
        pager.setCurrentItem(currentPage);
    }
    // ダイアログで入力した値をtextViewに入れる - ダイアログから呼び出される
    public void setTextView(String value){
        set_nowDate().setText(value);

    }
    //CalendarViewに日付セット
    public void setCalendarView (long date){
        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView1);
        calendarView.setDate(date);
    }

    //入力した文字列型の日付から数値型の年取得
    public int intYear() {
        String str = set_nowDate().getText().toString();
        int i = Integer.parseInt(str.substring(0,4));
        return i;
    }

    //sql用の日付取得
    public String getSqlDate(){
        Log.d("etyu",sqlDate+"");
        return sqlDate;
    }
    public void setSqlDate(String str){
        sqlDate = str;
        Log.d("etyu2",str+"");
    }

    public String buttonStr(View plusMinus){
        Button button = (Button) findViewById(plusMinus.getId());
        String buttonStr = button.getText().toString();
        return buttonStr;
    }

    //入力した文字列型の日付から数値型の月取得
    public int intMonth() {
        String str = set_nowDate().getText().toString();
        int i = Integer.parseInt(str.substring(5,7))-1;
        return i;
    }

    //入力した文字列型の日付から数値型の日取得
    public int intDay() {
        String str = set_nowDate().getText().toString();
        int i = Integer.parseInt(str.substring(8,10));
        return i;
    }

    //textviewの文字列取得
    public String strStart() {
        String str = set_nowDate().getText().toString();
        return str;
    }
    //現在の日付セット
    public void setNowDate(TextView dialogBtn) {
        try {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
            String date = year + "年" + (month + 1) + "月" + day + "日";
            Date selectDate = sdf.parse(date);
            dialogBtn.setText(sdf.format(selectDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void getDialogSelectDate (TextView dialogBtn) {
        try {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
            String date = year + "年" + (month + 1) + "月" + day + "日";
            Date selectDate = sdf.parse(date);
            dialogBtn.setText(sdf.format(selectDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public void mainListcreatAdapter(View view, String sql, String[] headers, int[] layouts,int db_layouts) {
        ListView listView = (ListView) view;
        //DBHelpderを作成する。この時にDBが作成される。
        dbHelper = new DatabaseHelper(this);
        //DBを読み込み可能状態で開く。
        //※getWritableDatabase（書き込み可能状態でも読み込みはできる）
        SQLiteDatabase d = dbHelper.getReadableDatabase();
        //DBへクエリーを発行し、カーソルを取得する。
        cursor = d.rawQuery(sql,null);
        simpleCursorAdapter = new SimpleCursorAdapter
                (this,db_layouts,cursor, headers, layouts,0);
        listView.setAdapter(simpleCursorAdapter);
        adapter.notifyDataSetChanged();
    }
}

