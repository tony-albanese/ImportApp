package com.example.tony.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.tony.inventoryapp.ProductContract.ProductEntry;

/**
 * Created by tony on 25.11.17.
 */

public class ProductCursorAdapter extends CursorAdapter {
    private Button saleButton;
    private TextView productQuantityView;

    public ProductCursorAdapter(Context context, Cursor cursor){
        super(context, cursor, 0);
    }



    @Override
  public View newView(Context context, Cursor cursor, ViewGroup parent){
        return LayoutInflater.from(context).inflate(R.layout.product_list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor){
        TextView  productNameView = (TextView) view.findViewById(R.id.tv_list_product_name);
        productQuantityView = (TextView) view.findViewById(R.id.tv_list_quantity);
        TextView productPriceView = (TextView) view.findViewById(R.id.tv_list_price);
        saleButton = (Button) view.findViewById(R.id.sale_button);

        String name = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_NAME));
        final int initialQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_QUANTITY));
        int price = cursor.getInt(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_PRICE));
        final long id = cursor.getInt(cursor.getColumnIndexOrThrow(ProductEntry._ID));

        productNameView.setText(name);
        productQuantityView.setText(Integer.toString(initialQuantity));
        productPriceView.setText(ProductUtilities.convertPriceToDollars(price));



        //Set the listener for the sale button.
        saleButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(initialQuantity > 0){
                    Uri uri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id);
                    ContentValues values = new ContentValues();
                    values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, initialQuantity - 1 );
                    int rowsAffected = context.getContentResolver().update(uri, values, null, null);
                }
            }
        });
    }
}
