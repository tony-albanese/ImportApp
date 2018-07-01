package com.example.tony.inventoryapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_PRODUCT_LOADER = 0;

    private Uri currentProductUri;

    //Declare variables for all the views we will need to manipulate in the app.

    private TextView idView;
    private TextView nameView;
    private TextView descriptionView;
    private TextView priceView;
    private TextView phoneView;
    private TextView mailView;
    private TextView supplyView;

    private Button plusButton;
    private Button minusButton;
    private Button orderButton;

    private ImageView detailImageView;

    private int productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        currentProductUri = intent.getData();

        idView = (TextView) findViewById(R.id.tv_details_id);
        nameView = (TextView) findViewById(R.id.tv_details_product_name);
        descriptionView = (TextView) findViewById(R.id.tv_details_description);
        priceView = (TextView) findViewById(R.id.tv_details_price);
        phoneView = (TextView) findViewById(R.id.tv_details_supplier_phone);
        supplyView = (TextView) findViewById(R.id.tv_details_supply);
        mailView = (TextView) findViewById(R.id.tv_details_supplier_mail);
        plusButton = (Button) findViewById(R.id.btn_increase);
        minusButton = (Button) findViewById(R.id.btn_decrease);
        orderButton = (Button) findViewById(R.id.btn_contact_supplier);
        detailImageView = (ImageView) findViewById(R.id.detail_product_image);

        //Set the listeners for the buttons.
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incrementSupply();
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decrementSupply();
            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneView = (TextView) findViewById(R.id.tv_details_supplier_phone);
                String number = phoneView.getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null));
                startActivity(intent);
            }
        });

        if(currentProductUri != null){
            getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle){
        String[] projection = {
                ProductContract.ProductEntry._ID,
                ProductContract.ProductEntry.COLUMN_PRODUCT_NAME,
                ProductContract.ProductEntry.COLUMN_PRODUCT_DESCRIPTION,
                ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE,
                ProductContract.ProductEntry.COLUMN_PRODUCT_EMAIL,
                ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductContract.ProductEntry.COLUMN_IMAGE
        };

        return new CursorLoader(this, currentProductUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor){
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        //Read the data from the cursor if it is valid.
        if(cursor.moveToFirst()){
            int nameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
            int descriptionColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_DESCRIPTION);
            int priceColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);
            int phoneColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE);
            int quantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int emailColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_EMAIL);
            int imageColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_IMAGE);
            int idColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry._ID);

            int productId = cursor.getInt(idColumnIndex);
            String name = cursor.getString(nameColumnIndex);
            String description = cursor.getString(descriptionColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            String phoneNumber = cursor.getString(phoneColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            String emailAddress = cursor.getString(emailColumnIndex);
            String imageUriString = cursor.getString(imageColumnIndex);
            Uri imageUri = Uri.parse(imageUriString);

            idView.setText(Integer.toString(productId));
            nameView.setText(name);
            descriptionView.setText(description);
            priceView.setText(ProductUtilities.convertPriceToDollars(price));
            phoneView.setText(phoneNumber);
            supplyView.setText(Integer.toString(quantity));
            mailView.setText(emailAddress);
            detailImageView.setImageURI(imageUri);
            detailImageView.invalidate();

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){

    }

    public void incrementSupply(){
        supplyView = (TextView) findViewById(R.id.tv_details_supply);
        int currentSupply = Integer.parseInt(supplyView.getText().toString());
        currentSupply = currentSupply + 1;

        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, currentSupply);

        int rowsAffected = getContentResolver().update(currentProductUri, values, null, null);
    }

    public void decrementSupply(){
        supplyView = (TextView) findViewById(R.id.tv_details_supply);
        int currentSupply = Integer.parseInt(supplyView.getText().toString());

        if(currentSupply > 0 ){
            currentSupply = currentSupply - 1;
            ContentValues values = new ContentValues();
            values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, currentSupply);
            int rowsAffected = getContentResolver().update(currentProductUri, values, null, null);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_item_confirm:
                finish();
                return true;
            case R.id.menu_item_delete_product:
              confirmDeleteProductDialog();
              return true;
        }
        return true;
    }

    public void confirmDeleteProductDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_message_delete_product);
        builder.setPositiveButton(R.string.dialog_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteProduct();
            }
        });

        builder.setNegativeButton(R.string.dialog_negativ, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(dialogInterface != null){
                    dialogInterface.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteProduct(){
        int numberRowsDeleted = 0;
        if(currentProductUri != null){
            numberRowsDeleted = getContentResolver().delete(currentProductUri, null,null);
        }
        if(numberRowsDeleted == 0){
            Toast toast = Toast.makeText(this, getString(R.string.delete_failed), Toast.LENGTH_SHORT);
            toast.show();
        }
        finish();
    }
}
