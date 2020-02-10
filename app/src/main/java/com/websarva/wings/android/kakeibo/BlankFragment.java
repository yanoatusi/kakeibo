package com.websarva.wings.android.kakeibo;


import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressWarnings("NullableProblems")
public class BlankFragment extends Fragment {
    /**
     * 選択されたカクテル名を表すフィールド。
     */
    String _cocktailName = "";
    /**
     * カクテル名を表示するTextViewフィールド。
     */
    TextView _tvCocktailName;
    /**
     * ［保存］ボタンフィールド。
     */
    TextView _btnSave;

    EditText _typePrice;

    EditText _memoNote;

    TextView _value;

    ListView _largeCotegoryList;
    ListView _smallCategoryList;
//    Button _plusButton;
    Button _minusButton;
//    Button _memoButton;
    Button _backDate;
    EditText _nowDate;
    Button _nextDate;

    ListView _calendarFrgList;

    GridView _gridView;

    //カテゴリーリスト
    private DatabaseHelper dbHelper;
    private DatabaseHelper Helper;
    Cursor gridcursor=null;
    Cursor cursor=null;
    Cursor cursor2=null;
    Cursor categoryCursol=null;
    SQLiteDatabase categoryDb;
    int categoryIdSave = 0;
    int memoIdSave = 0;
    private SimpleCursorAdapter gridAdapter;
    private SimpleCursorAdapter CategoryAdapter2;
    private SimpleCursorAdapter _saveAdapter;
    Calendar calendar = Calendar.getInstance();
    //+-切り替え変数
    private int _priceType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_blank, container, false);
        super.onCreate(savedInstanceState);

        //
        _calendarFrgList = (ListView) getActivity().findViewById(R.id.list);
        _gridView = (GridView) view.findViewById(R.id.gridView);
        _tvCocktailName = view.findViewById(R.id.tvCocktailName);
        _typePrice = view.findViewById(R.id.typePrice);
        _memoNote = view.findViewById(R.id.memoNote);
        _btnSave = (TextView) view.findViewById(R.id.btnSave);
        _largeCotegoryList = (ListView) view.findViewById(R.id.LargeCotegory);
        _smallCategoryList = (ListView) view.findViewById(R.id.SmallCategory);
//        _plusButton = (Button) view.findViewById(R.id.plusButton);
//        _minusButton = (Button) view.findViewById(R.id.minusButton);
//        _memoButton = (Button) view.findViewById(R.id.memoButton);
        _backDate = (Button) view.findViewById(R.id.backDate);
        _nowDate= (EditText) view.findViewById(R.id.nowDate);
        _nextDate = (Button) view.findViewById(R.id.nextDate);


         MainActivity mainActivity = (MainActivity) getActivity();
        // idが_nowDatenのButtonを取得
        mainActivity.setNowDate(_nowDate);
        mainActivity.setSqlDate(_nowDate.getText().toString());
        Log.d("BlankFrg_getSqlDate1",_nowDate.getText().toString()+"");

        // clickイベント追加
        _nowDate.setOnClickListener(new View.OnClickListener() {
            @Override
            // クリックしたらダイアログを表示する処理
            public void onClick(View v) {
                // ダイアログクラスをインスタンス化
                CustomDialogFlagment dialog1 = new CustomDialogFlagment();
                // 表示  getFagmentManager()は固定、sampleは識別タグ
                dialog1.show(getFragmentManager(), "sample");
            }

        });

        View.OnClickListener event = new View.OnClickListener() {
            // クリックしたら前の日か次の日
            public void onClick(View backNext) {
                // MainActivityのインスタンスを取得
                MainActivity mainActivity = (MainActivity) getActivity();
                switch (backNext.getId()) {
                    case R.id.backDate:
                        calendar.add(Calendar.DAY_OF_MONTH, -1);
                        replayDatabase();
                        break;
                    case R.id.nextDate:
                        calendar.add(Calendar.DAY_OF_MONTH, +1);
                        replayDatabase();
                        break;
                }

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                    String dateStr = calendar.get(Calendar.YEAR) + "年"
                            + (calendar.get(Calendar.MONTH) + 1) + "月"
                            + calendar.get(Calendar.DAY_OF_MONTH) + "日";
                    Date selectDate = sdf.parse(dateStr);
                    mainActivity.setTextView(sdf.format(selectDate));
                    //calendarFragmentに日付セット
                    calendarFragment calendarFrg = new calendarFragment() ;
                    mainActivity.setCalendarView(calendarFrg.convertDateStringToLong(dateStr));

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        _backDate.setOnClickListener(event);
        _nextDate.setOnClickListener(event);

        replayDatabase();

        _gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.d("ABCD", "Position Single Click is " + position);

                    new AlertDialog.Builder(getActivity())
                        .setTitle("削除しますか？")
                        //.setMessage("削除しますか？")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // OK button pressed
                                MainActivity mainActivity = (MainActivity) getActivity();
                                //DBHelpderを作成する。この時にDBが作成される。
                                DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
                                //DBを読み込み可能状態で開く。
                                //※getWritableDatabase（書き込み可能状態でも読み込みはできる）
                                SQLiteDatabase d = dbHelper.getReadableDatabase();
                                d.delete("DatePrice","Date =" +  "'" + mainActivity.getSqlDate() + "'"+
                                        "AND _id = (SELECT _id FROM DatePrice WHERE Date ="+  "'" + mainActivity.getSqlDate() + "'" +
                                    " LIMIT 1 OFFSET" +  "'" + position + "'"+")",null);
                                replayDatabase();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                return true;
            }
        });

//        Category表示
        listcreatAdapter(_largeCotegoryList,"SELECT Category_Id as _id,Attributable_Type, Large_category FROM Category WHERE Attributable_Type = '支出' GROUP BY Large_category ORDER BY Category_Id",
                new String[]{"Large_category"},new int[]{R.id.lcategory1},R.layout.category_list);


        _saveAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view1, Cursor cursor, int columnIndex) {

                switch (columnIndex) {
                    case 0:
                        TextView number = (TextView) view1;
                        number.setText(cursor.getString(cursor.getColumnIndex("Large_category")));
                        return true;
                     default:
                        break;
                }
                return false;
            }
        });
        _largeCotegoryList.setAdapter(_saveAdapter);

      //LargeCotegory文字列からSmallCategory文字列の表示
        _largeCotegoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View viewnull, int position, long id) {

                //ここで内容取得
                String currentId = cursor.getString(cursor.getColumnIndex("Large_category"));

                //DBHelpderを作成する。この時にDBが作成される。
                DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
                //DBを読み込み可能状態で開く。
                //※getWritableDatabase（書き込み可能状態でも読み込みはできる）
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                //DBへクエリーを発行し、カーソルを取得する。
                cursor2 = db.query("Category", new String[]{"Category.Category_Id AS _id", "Attributable_Type", "Large_category", "Small_category"}
                                  ,"Large_category=?", new String[]{currentId}, null, null, null);
                //取得したカーソルをカーソル用のアダプターに設定する。
                String[] headers2 = {"_id","Small_category"};
                int[] layouts2 = {R.id.SmallCategorylist2};
                int db_layout2 = R.layout.small_category_list;
                CategoryAdapter2 = new SimpleCursorAdapter
                        (view.getContext(),db_layout2,cursor2, headers2, layouts2,0);
                Log.d("ssff","ssff");
                CategoryAdapter2.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                    @Override
                    public boolean setViewValue(View view1, Cursor cursor, int columnIndex) {
                        switch (columnIndex) {
                            case 0:
                                    TextView number = (TextView) view1;
                                    //タップしたSmall_categoryの行番号と文字列の取得
                                    number.setText(cursor.getString(cursor.getColumnIndex("Small_category")));

                                    return true;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                _smallCategoryList.setAdapter(CategoryAdapter2);
            }
        });

        //収入と支出の切り替え
        View.OnClickListener plusMinusEvent = new View.OnClickListener() {
            // クリックしたら収入
            public void onClick(View plusMinus) {
                Button button = (Button) plusMinus.findViewById(plusMinus.getId());
                String buttonStr = button.getText().toString();
                listcreatAdapter(_largeCotegoryList,"SELECT rowid as _id,Attributable_Type, Large_category FROM Category WHERE Attributable_Type =" +  "'" + buttonStr + "'" +
                                "GROUP BY Large_category ORDER BY _id ASC",
                        new String[]{"Large_category"},new int[]{R.id.lcategory1},R.layout.category_list);
            }
        };
//        _plusButton.setOnClickListener(plusMinusEvent);
//        _minusButton.setOnClickListener(plusMinusEvent);
        // _smallCategoryListにリスナを登録。
        _smallCategoryList.setOnItemClickListener(new ListItemClickListener());


        _btnSave.setOnClickListener(new View.OnClickListener() {
            /**
             * 保存ボタンがタップされた時の処理メソッド。
             */
            @Override
            public void onClick(View view1) {

                //入力された金額を取得。
                MainActivity mainActivity = (MainActivity) getActivity();
                int priceNote = Integer.parseInt(_typePrice.getText().toString());
                mainActivity.setSqlDate(_nowDate.getText().toString());
                String a = mainActivity.getSqlDate();
                Log.d("etyu3",_nowDate.getText().toString()+"");
                String memoNote =  _memoNote.getText().toString();
                //データベースヘルパーオブジェクトを作成。
                DatabaseHelper helper = new DatabaseHelper(getContext());
                //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得。
                SQLiteDatabase db = helper.getWritableDatabase();

                try {
                    //まず、リストで選択されたカクテルのメモデータを削除。その後インサートを行う。
                    //削除用SQL文字列を用意。
                    String sqlDeleteDatePrice = "DELETE FROM DatePrice WHERE _Id = ?";
                    SQLiteStatement DatePriceStmt = db.compileStatement(sqlDeleteDatePrice);
                    DatePriceStmt.executeUpdateDelete();
                    //インサート用SQL文字列の用意。
                    String DatePriceInsert = "INSERT INTO DatePrice (_id, Date, Price, Category_Id, Memo)" +
                            " VALUES (?, ?, ?, ?, ?)";
                    //SQL文字列を元にプリペアドステートメントを取得。
                    DatePriceStmt = db.compileStatement(DatePriceInsert);
                    //変数のバイド。

                    Log.d("price",priceNote + "");
                    Log.d("categoryid",categoryIdSave + "");
                    Log.d("mainq",mainActivity.getSqlDate());
//                    DatePriceStmt.bindLong(1, _id);

                    DatePriceStmt.bindString(2, a);

                    DatePriceStmt.bindLong(3, priceNote * _priceType);
                    DatePriceStmt.bindLong(4, categoryIdSave);
                    DatePriceStmt.bindString(5, memoNote);

                    //インサートSQLの実行。
                    DatePriceStmt.executeInsert();

                } finally {
                    //データベース接続オブジェクトの解放。
                    db.close();
                }

                _typePrice.setText("");
                Log.d("qwas",priceNote+"");
                //+-切替変数の初期化
                //データベースヘルパーオブジェクトを作成。
                helper = new DatabaseHelper(getActivity());
                //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得。
                categoryDb = helper.getWritableDatabase();
                String sql= "SELECT Category_id as _id,Attributable_Type, Large_category, Small_category FROM Category " +
                        "WHERE Small_category = " +  "'" + _cocktailName + "'";
                categoryCursol = categoryDb.rawQuery(sql,null);
                String attributableName = (String)  cursor.getString(categoryCursol.getColumnIndex("Attributable_Type"));
                Log.d("ppppp",attributableName+"");
                switch (attributableName) {
                    case "支出":
                        _priceType = 1;
                        break;
                    case "収入":
                        _priceType = 1;
                        break;
                }

                _memoNote.setText("");
                _btnSave.setEnabled(false);
                replayDatabase();



            }



        });


        // リスナーを登録
        _nowDate.addTextChangedListener(new GenericTextWatcher(_nowDate));
        _typePrice.addTextChangedListener(new GenericTextWatcher(_typePrice));
        _tvCocktailName.addTextChangedListener(new GenericTextWatcher(_tvCocktailName));
        _nowDate.setFocusable(false);
        return view;
    }

    //リストがタップされたときの処理が記述されたメンバクラス。
    private class ListItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //タップされた行のデータを取得。これがカクテル名となるので、フィールドに代入。
                _cocktailName = (String) cursor2.getString(cursor2.getColumnIndex("Small_category"));
                    Log.d("qqqw",_cocktailName+"");
                //カクテル名を表示するTextViewに表示カクテル名を設定。
                _tvCocktailName.setText(_cocktailName);

            categoryIdSave = Integer.parseInt(cursor2.getString(cursor2.getColumnIndex("_id")));
            Log.d("categoryid",categoryIdSave + "");



            //データベースヘルパーオブジェクトを作成。
            DatabaseHelper helper = new DatabaseHelper(getActivity());
            //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得。
            categoryDb = helper.getWritableDatabase();
            String sql= "SELECT Category_id as _id,Attributable_Type, Large_category, Small_category FROM Category " +
                    "WHERE Small_category = " +  "'" + _cocktailName + "'";
            categoryCursol = categoryDb.rawQuery(sql,null);
            String attributableName = (String)  cursor.getString(categoryCursol.getColumnIndex("Attributable_Type"));
            Log.d("ppppp",attributableName+"");
            switch (attributableName) {
                case "支出":
                    _priceType = 1;
                    break;
                case "収入":
                    _priceType = 1;
                    break;
            }
        }

    }

    //Declaration
    private class GenericTextWatcher implements TextWatcher{

        /**
         * View
         *
         * このViewが引数として渡されたEditTextそれぞれを意味する
         */
        private View view;
        /**
         * コンストラクタが、GenericTextWatcherのインスタンスが作成されるとき上の
         * privateフィールドにあるviewに、引数として渡されたEditTextの実体参照を渡す
         *
         * @param {[type]} View view [description]
         */
        private GenericTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {    }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {    }

        public void afterTextChanged(Editable editable) {
            String  inputStr = editable.toString();
            // ここで条件分岐をする
            switch(view.getId()){
                case R.id.nowDate:
                    //日付変更を検知する度更新
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.setSqlDate(inputStr);
                    Log.d("setaaa",inputStr+"");
                    replayDatabase();
                    break;
                case R.id.typePrice:
                    if (! inputStr.equals("")) {
                        if (! _tvCocktailName.getText().toString().equals("選択項目")){
                            Log.d("sddd",_tvCocktailName.getText().toString());
                            _btnSave.setEnabled(true);
                        }
                    } else {
                        _btnSave.setEnabled(false);
                    }
                    break;
                case R.id.tvCocktailName:
                    if (! inputStr.equals("")) {
                        if (! _typePrice.getText().toString().equals("")){
                            Log.d("sddd",_typePrice.getText().toString());
                            _btnSave.setEnabled(true);
                        }
                    } else {
                        _btnSave.setEnabled(false);
                    }
                    break;
            }
        }
    }
    public void replayDatabase() {
        MainActivity mainActivity = (MainActivity) getActivity();
        //DBHelpderを作成する。この時にDBが作成される。
        Helper = new DatabaseHelper(getActivity());
        //※getWritableDatabase（書き込み可能状態でも読み込みはできる）
        SQLiteDatabase d = Helper.getReadableDatabase();
        //DBへクエリーを発行し、カーソルを取得する。
        String sql = "SELECT _Id,Date,Small_category,Price,Memo FROM DatePrice "  +
                "INNER JOIN Category ON DatePrice.Category_Id = Category.Category_Id " +
                "WHERE Date =" +  "'" + mainActivity.getSqlDate() + "'";
        gridcursor = d.rawQuery(sql,null);
        //取得したカーソルをカーソル用のアダプターに設定する。
        String[] head = {"Small_category","Price","Memo"};
        int[] lay = {R.id.name,R.id.price,R.id.memo};
        int db_lay = R.layout.listrow;
        gridAdapter = new SimpleCursorAdapter
                (getContext(),db_lay,gridcursor, head, lay,0);

        _gridView.setAdapter(gridAdapter);
        gridAdapter.notifyDataSetChanged();
    }

    private void listcreatAdapter(View view, String sql, String[] headers, int[] layouts,int db_layouts) {
        ListView listView = (ListView) view;
        //DBHelpderを作成する。この時にDBが作成される。
        dbHelper = new DatabaseHelper(getActivity());
        //DBを読み込み可能状態で開く。
        //※getWritableDatabase（書き込み可能状態でも読み込みはできる）
        SQLiteDatabase d = dbHelper.getReadableDatabase();
        //DBへクエリーを発行し、カーソルを取得する。
        cursor = d.rawQuery(sql,null);
        _saveAdapter = new SimpleCursorAdapter
                (getContext(),db_layouts,cursor, headers, layouts,0);
        listView.setAdapter(_saveAdapter);
        _saveAdapter.notifyDataSetChanged();
    }
}
