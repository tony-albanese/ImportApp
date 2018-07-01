package com.example.tony.inventoryapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.tony.inventoryapp.ProductContract.ProductEntry;

/**
 * Created by tony on 25.11.17.
 */

public class ProductDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = ProductDbHelper.class.getSimpleName();

    public static final String DATABASE_NAME = "inventory";
    public static final int DATABASE_VERSION = 1;

    public ProductDbHelper(Context context){
        super(context, DATABASE_NAME, null,DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase database){
        String SQL_CREATE_PRODUCTS_TABLE =
                "CREATE TABLE " + ProductEntry.TABLE_NAME + " ("
                        + ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + ProductEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                        + ProductEntry.COLUMN_PRODUCT_DESCRIPTION + " TEXT NOT NULL, "
                        + ProductEntry.COLUMN_PRODUCT_PRICE + " INTEGER NOT NULL, "
                        + ProductEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER, "
                        + ProductEntry.COLUMN_PRODUCT_EMAIL + " TEXT NOT NULL, "
                        + ProductEntry.COLUMN_IMAGE + " TEXT, "
                        + ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE + " TEXT);";

        database.execSQL(SQL_CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }

}
