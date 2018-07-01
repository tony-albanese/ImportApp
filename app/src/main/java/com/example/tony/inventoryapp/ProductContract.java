package com.example.tony.inventoryapp;

import android.net.Uri;
import android.provider.BaseColumns;



public final class ProductContract {

    public static final String CONTENT_AUTHORITY = "com.example.tony.inventoryapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_PRODUCTS = "products";

    //The required constructor.
    private ProductContract(){}

    public static final class ProductEntry implements BaseColumns{
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTS);
        //Let's define the basic parameters of the database.
        public static final String TABLE_NAME = "products";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PRODUCT_NAME = "name";
        public static final String COLUMN_PRODUCT_DESCRIPTION = "description";
        public static final String COLUMN_PRODUCT_PRICE = "price";
        public static final String COLUMN_PRODUCT_QUANTITY = "quantity";
        public static final String COLUMN_PRODUCT_SUPPLIER_PHONE = "phone";
        public static final String COLUMN_PRODUCT_EMAIL = "email";
        public static final String COLUMN_IMAGE = "image";
    }
}
