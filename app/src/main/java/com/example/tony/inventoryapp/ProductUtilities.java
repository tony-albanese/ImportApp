package com.example.tony.inventoryapp;

/**
 * Created by tony on 10.12.17.
 */

public abstract class ProductUtilities {
    //This function will take an integer price (in cents) and turn it into a String for dollars.
    //This is to display to the user.
    public static String convertPriceToDollars(int price){
        float decimalPrice = (float) price;
        decimalPrice = decimalPrice/100;
        String priceAsString = String.format("%.2f", decimalPrice);

        return "$" + priceAsString;
    }

    //This converts the price to cents so it can be stored in the database as an INT.
    public static int priceToCents(float price){
        float cents = price * 100;
        return (int) cents;
    }

    //A series of functions for data validation.
    //This function checks for non numerical characters in the phone number and
    //makes sure the phone number is between 6 and 12 characters.
    public static boolean validatePhone(String number){
        if(number == null || number.length() == 0){
            return false;
        } else{
            number = number.replaceAll("[^0-9.]", "");
        }
         if((number.length() >=6) && (number.length() < 12)){
            return true;
         } else {
             return false;
         }
    }

    //Check the e-mail address has at least the @ symbol and a . . Not foolproof, but at least
    //somewhat prevents users from entering in nonsense.
    public static boolean validateEmail(String email){
        if(!email.contains("@") || !email.contains(".") || email.contains(" ")){
            return false;
        } else {
            return true;
        }

    }

    public static boolean validateName(String name){
        if (name.length() == 0 || name == null) {
            return false;
        }else{
            return true;
        }

    }

}
