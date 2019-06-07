package com.websarva.wings.android.kakeibo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CustomDialogFlagment extends DialogFragment {

    // ダイアログが生成された時に呼ばれるメソッド ※必須
    public Dialog onCreateDialog(Bundle savedInstanceState){
        // 今日の日付のカレンダーインスタンスを取得
        final Calendar calendar = Calendar.getInstance();

        //日付更新取得
        MainActivity mainActivity = (MainActivity) getActivity();
            calendar.set(mainActivity.intYear(), mainActivity.intMonth(), mainActivity.intDay());

        // ダイアログ生成  DatePickerDialogのBuilderクラスを指定してインスタンス化します
        DatePickerDialog dateBuilder = new DatePickerDialog(
                getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // 選択された年・月・日を整形 ※月は0-11なので+1している
                        try {
                            MainActivity mainActivity = (MainActivity) getActivity();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                            String dateStr = year + "年" + (month + 1) + "月" + dayOfMonth + "日";
                            Date selectDate = sdf.parse(dateStr);
                            mainActivity.setTextView(sdf.format(selectDate));
                            //calendarFragmentに日付セット
                            calendarFragment calendarFrg = new calendarFragment() ;
                            mainActivity.setCalendarView(calendarFrg.convertDateStringToLong(dateStr));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },
                // 初期選択年月日
                calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)

        );
        // dateBulderを返す
        return dateBuilder;
    }

}
