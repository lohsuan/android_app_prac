package com.example.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class SQLiteDataBaseHelper extends SQLiteOpenHelper {
   String TableName;

    public SQLiteDataBaseHelper(@Nullable Context context
            , @Nullable String  dataBaseName
            , @Nullable SQLiteDatabase.CursorFactory factory, int version, String TableName) {
        super(context, dataBaseName, factory, version);
        this.TableName = TableName;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQLTable = "CREATE TABLE IF NOT EXISTS " + TableName + "( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "CommodityName TEXT, " +
                "StoreName TEXT, " +
                "Price INTEGER," +
                "CreateDate TEXT" +
                ");";
        sqLiteDatabase.execSQL(SQLTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        final String SQL = "DROP TABLE " + TableName;
        sqLiteDatabase.execSQL(SQL);
    }

    //檢查資料表狀態，若無指定資料表則新增
    public void checkTable(){
        Cursor cursor = getWritableDatabase().rawQuery(
                "select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() == 0)
                getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS " + TableName + "( " +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "CommodityName TEXT, " +
                        "StoreName TEXT, " +
                        "Price INTEGER," +
                        "CreateDate TEXT" +
                        ");");
            cursor.close();
        }
    }
    //取得有多少資料表,並以陣列回傳
    public ArrayList<String> getTables(){
        Cursor cursor = getWritableDatabase().rawQuery(
                "select DISTINCT tbl_name from sqlite_master", null);
        ArrayList<String> tables = new ArrayList<>();
        while (cursor.moveToNext()){
            String getTab = new String (cursor.getBlob(0));
            if (getTab.contains("android_metadata")){}
            else if (getTab.contains("sqlite_sequence")){}
            else tables.add(getTab.substring(0,getTab.length()-1));
        }
        return tables;
    }

    //新增資料
    public void addData(String CommodityName, String StoreName, Integer Price) {
        // 取得目前時間加入
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
        // 獲得當前時間
        Date date = new Date(System.currentTimeMillis());
        String CreateDate = simpleDateFormat.format(date);
        Log.i("dateTime",CreateDate);
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("CommodityName", CommodityName);
        values.put("StoreName", StoreName);
        values.put("Price", Price);
        values.put("CreateDate", CreateDate);
        db.insert(TableName, null, values);
    }

    // 修改資料
    public void modify(Integer id, String CommodityName, String StoreName, Integer Price) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(" UPDATE " + TableName
                + " SET CommodityName=" + "'" + CommodityName + "',"
                + "StoreName=" + "'" + StoreName + "',"
                + "Price=" + "" + Price
                + " WHERE _id=" + id );
    }

    //顯示所有資料
    public ArrayList<HashMap<String, String>> showAll() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT * FROM " + TableName + " ORDER BY CreateDate DESC", null);
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        while (c.moveToNext()) {
            HashMap<String, String> hashMap = new HashMap<>();
            String id = c.getString(0);
            String CommodityName = c.getString(1);
            String StoreName = c.getString(2);
            String Price = c.getString(3);
            String CreateDate = c.getString(4);
            hashMap.put("id", id);
            hashMap.put("CommodityName", CommodityName);
            hashMap.put("StoreName", StoreName);
            hashMap.put("Price", Price);
            hashMap.put("CreateDate", CreateDate);
            arrayList.add(hashMap);
        }
        return arrayList;
    }

    //以id搜尋特定資料
    public ArrayList<HashMap<String,String>> searchById(String getId){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT * FROM " + TableName
                + " WHERE _id =" + "'" + getId + "'", null);
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        while (c.moveToNext()) {
            HashMap<String, String> hashMap = new HashMap<>();
            String id = c.getString(0);
            String CommodityName = c.getString(1);
            String StoreName = c.getString(2);
            String Price = c.getString(3);
            String elseInfo = c.getString(4);
            hashMap.put("id", id);
            hashMap.put("CommodityName", CommodityName);
            hashMap.put("StoreName", StoreName);
            hashMap.put("Price", Price);
            arrayList.add(hashMap);
        }
        return arrayList;
    }

    public void deleteByIdEZ(String id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TableName,"_id = " + id,null);
    }

}
