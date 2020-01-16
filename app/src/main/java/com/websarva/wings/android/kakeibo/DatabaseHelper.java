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

        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 1,'支出','固定費','住居');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 2,'支出','固定費','電気');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 3,'支出','固定費','ガス');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 4,'支出','固定費','水道');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 5,'支出','固定費','インターネット');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 6,'支出','固定費','電話');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 7,'支出','固定費','保険');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 8,'支出','固定費','税金');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 9,'支出','固定費','新聞');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 10,'支出','固定費','教育');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 11,'支出','交通費','電車');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 12,'支出','交通費','バス');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 13,'支出','交通費','自転車');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 14,'支出','交通費','車');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 15,'支出','交通費','タクシー');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 16,'支出','交通費','飛行機');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 17,'支出','食費','食料品');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 18,'支出','食費','飲料');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 19,'支出','食費','酒');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 20,'支出','食費','外食(朝)');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 21,'支出','食費','外食(昼)');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 22,'支出','食費','外食(夜)');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 23,'支出','食費','お菓子');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 24,'支出','生活雑貨・日用品','日用品');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 25,'支出','生活雑貨・日用品','雑貨');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 26,'支出','生活雑貨・日用品','文房具');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 27,'支出','生活雑貨・日用品','家具');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 28,'支出','生活雑貨・日用品','家電');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 29,'支出','遊興費','おもちゃ');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 30,'支出','遊興費','動画配信');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 31,'支出','遊興費','アプリ');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 32,'支出','遊興費','ゲーム');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 33,'支出','遊興費','遊園地');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 34,'支出','遊興費','ギャンブル');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 35,'支出','美容費','理容院・美容院');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 36,'支出','美容費','エステ');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 37,'支出','美容費','化粧品・整髪料');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 38,'支出','被服費','服・靴');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 39,'支出','被服費','バッグ・アクセサリ');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 40,'支出','被服費','クリーニング');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 41,'支出','医療費','病院');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 42,'支出','医療費','医薬品');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 43,'支出','医療費','市販薬');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 44,'支出','特別支出','冠婚葬祭');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 45,'支出','その他支出','娯楽');");
        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 46,'支出','その他支出','その他');");


//        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 42,'収入','中古品販売','フリマ');");
//        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 43,'収入','中古品販売','オークション');");
//        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 44,'収入','給料・年金','固定給');");
//        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 45,'収入','給料・年金','ボーナス');");
//        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 46,'収入','給料・年金','パート・アルバイト');");
//        db.execSQL("INSERT INTO Category (Category_Id,Attributable_Type,Large_category,Small_category) VALUES( 47,'収入','給料・年金','年金');");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    }
