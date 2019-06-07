package com.websarva.wings.android.kakeibo;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    /**
     * データベースファイル名の定数フィールド。
     */
    private static final String DATABASE_NAME = "kakeibo.db";
    /**
     * バージョン情報の定数フィールド。
     */
    private static final int DATABASE_VERSION = 1;


    /**
     * コンストラクタ。
     */
    public DatabaseHelper(Context context) {
        // 親クラスのコンストラクタの呼び出し。
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE DatePrice (_id INTEGER PRIMARY KEY AUTOINCREMENT ,Date TEXT, Price INTERGER," +
                "Category_Id INTEGER,Memo TEXT);");

        db.execSQL("CREATE TABLE Category (Category_Id INTEGER," +
                   "Attributable_Type TEXT, Large_category TEXT, Small_category TEXT);");

            db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 1,'支出','交通費','電車');");
            db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 2,'支出','交通費','バス');");
            db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 3,'支出','交通費','ガソリン給油');");
            db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 4,'支出','交通費','タクシー');");
            db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 5,'支出','交通費','飛行機');");
            db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 6,'支出','食費','外食');");
            db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 7,'支出','食費','スーパー');");
            db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 8,'支出','食費','デリバリー');");

            db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 9,'収入','中古品販売','メルカリ');");
            db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 10,'収入','中古品販売','ヤフオク');");
            db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 11,'収入','中古品販売','ラクマ');");
            db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 12,'収入','給料','固定給');");
            db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 13,'収入','給料','ボーナス');");
            db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 14,'収入','給料','パート・アルバイト');");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    }
