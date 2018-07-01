package com.example.tony.inventoryapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tony.inventoryapp.ProductContract.ProductEntry;

public class AddProduct extends AppCompatActivity {

    public boolean allDataValid = true;
    public Uri imageUri = null;
    public ImageView imageView;

    private static final int SELECT_IMAGE_REQUEST_ID = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        //Get a reference to the save button and set the onClickListener.
        Button saveButton = (Button) findViewById(R.id.btn_save_record);
        Button imageButton = (Button) findViewById(R.id.btn_add_choose_image);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertValues();
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getProductImage();
            }
        });

        imageView = (ImageView) findViewById(R.id.add_image_view);
    }

    //This method will insert the values into the database.
    private void insertValues(){
        EditText name = (EditText) findViewById(R.id.et_product_name);
        EditText description = (EditText) findViewById(R.id.et_product_descriprion);
        EditText price = (EditText) findViewById(R.id.et_product_price);
        EditText phone = (EditText) findViewById(R.id.et_supplier_phone_number);
        EditText email = (EditText) findViewById(R.id.et_supplier_email);
        EditText quantity = (EditText) findViewById(R.id.et_quantity);

        String nameString = name.getText().toString().trim();
        String descriptionString = description.getText().toString();
        String phoneString = phone.getText().toString().trim();
        String emailString = email.getText().toString().trim();
        String priceString = price.getText().toString();
        String quantityString = quantity.getText().toString();


        //Need a ContentValues object to store the key-value pairs.
        ContentValues values = new ContentValues();

        //Check the value of the imageUri. If it is null, the data is invalid. Otherwise, add it to
        //the ContentValues object as a string.

        if(imageUri != null){
            String imageUriString = imageUri.toString();
            values.put(ProductEntry.COLUMN_IMAGE, imageUriString);
        } else{
            allDataValid = false;
            imageToast();
        }

        //Add values to the ContentValues object after validation.
        if(ProductUtilities.validateName(nameString)){
            values.put(ProductEntry.COLUMN_PRODUCT_NAME, nameString);
        } else{
            allDataValid = false;
        }

        if(descriptionString != null && descriptionString.length() > 0){
            values.put(ProductEntry.COLUMN_PRODUCT_DESCRIPTION, descriptionString);
        }else{
            values.put(ProductEntry.COLUMN_PRODUCT_DESCRIPTION, getString(R.string.default_description));
        }


        if(ProductUtilities.validatePhone(phoneString)){
            values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE, phoneString);
        }else{
            allDataValid = false;
        }

        if(ProductUtilities.validateEmail(emailString)){
            values.put(ProductEntry.COLUMN_PRODUCT_EMAIL, emailString);
        }else{
            allDataValid = false;
        }


        //Check that the quanity string is not null, is not empty, and is free from non-numeric characters
        //before adding it the content values.
        if(quantityString == null || quantityString.length() == 0 || quantityString.contains("[a-zA-Z]")) {
            allDataValid = false;
        } else{
            int quantityInt = Integer.parseInt(quantity.getText().toString().trim());
            values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantityInt);
        }



        if(priceString == null || priceString.length() == 0 || priceString.contains("[^0-9]+")){
            allDataValid = false;

        }else{
            int priceInt = ProductUtilities.priceToCents(Float.parseFloat(price.getText().toString()));
            values.put(ProductEntry.COLUMN_PRODUCT_PRICE, priceInt);
        }

        if(allDataValid){
                ProductDbHelper helper = new ProductDbHelper(this);
                SQLiteDatabase db = helper.getWritableDatabase();
                db.insert(ProductEntry.TABLE_NAME, null, values);
                goToSummaryActivity();
        } else{
            //Reset the value of variable that says all the data is valid.
            allDataValid = true;
            Toast toast = Toast.makeText(this, getString(R.string.invalid_data), Toast.LENGTH_SHORT);
            toast.show();
        }


    }

    private void goToSummaryActivity() {Intent intent = new Intent(this, ProductSummaryActivity.class);
        startActivity(intent);
    }


    public void getProductImage(){
        Intent imageSelectorIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        imageSelectorIntent.setType("image/*");
        startActivityForResult(imageSelectorIntent, SELECT_IMAGE_REQUEST_ID);
    }

    //I learned about this method from studying Lara Martin's project: https://github.com/laramartin/android_inventory
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_IMAGE_REQUEST_ID) {
                imageUri = data.getData();
                imageView.setImageURI(imageUri);
                imageView.invalidate();
            }
        }
    }


    private void imageToast(){
        String message = getString(R.string.image_error);
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
