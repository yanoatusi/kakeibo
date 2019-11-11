package com.websarva.wings.android.kakeibo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity   {
//
    private String sqlDate;
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
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(pager);
        tabLayout.getTabAt(0).setText("ホーム");
        tabLayout.getTabAt(1).setText("カレンダー");
        tabLayout.getTabAt(2).setText("グラフ");
    }

    public void onClickNext(View view) {
        currentPage ++;
        pager.setCurrentItem(currentPage);
    }
    public EditText set_nowDate(){
        EditText editText = findViewById(R.id.nowDate);
        return editText;
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


}

