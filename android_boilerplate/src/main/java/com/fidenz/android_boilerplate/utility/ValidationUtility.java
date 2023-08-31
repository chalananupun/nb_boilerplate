package com.fidenz.android_boilerplate.utility;

import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chalana
 * @contact Chalana.n@fidenz.com | 071 6 359 376
 */

public class ValidationUtility {
    private static final String TAG = "ValidationUtility";

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private static Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private static Matcher matcher;

    private static final int CARD_NUMBER_TOTAL_SYMBOLS = 19; // size of pattern 0000-0000-0000-0000
    private static final int CARD_NUMBER_TOTAL_DIGITS = 16; // max numbers of digits in pattern: 0000 x 4
    private static final int CARD_NUMBER_DIVIDER_MODULO = 5; // means divider position is every 5th symbol beginning with 1
    private static final int CARD_NUMBER_DIVIDER_POSITION = CARD_NUMBER_DIVIDER_MODULO - 1; // means divider position is every 4th symbol beginning with 0
    private static final char CARD_NUMBER_DIVIDER = '-';

    private static final int CARD_DATE_TOTAL_SYMBOLS = 5; // size of pattern MM/YY
    private static final int CARD_DATE_TOTAL_DIGITS = 4; // max numbers of digits in pattern: MM + YY
    private static final int CARD_DATE_DIVIDER_MODULO = 3; // means divider position is every 3rd symbol beginning with 1
    private static final int CARD_DATE_DIVIDER_POSITION = CARD_DATE_DIVIDER_MODULO - 1; // means divider position is every 2nd symbol beginning with 0
    private static final char CARD_DATE_DIVIDER = '/';

    public static String ptVisa = "^4[0-9]{6,}$";
    public static String ptMasterCard = "^5[1-5][0-9]{5,}$";
    public static String ptAmeExp = "^3[47][0-9]{5,}$";

    private static ArrayList<String> listOfPattern=new ArrayList<String>();

    public static void addCreditCardPattern(){
        listOfPattern=new ArrayList<String>();

        listOfPattern.add(ptVisa);
        listOfPattern.add(ptMasterCard);
        listOfPattern.add(ptAmeExp);
//        String ptDinClb = "^3(?:0[0-5]|[68][0-9])[0-9]{4,}$";
//        listOfPattern.add(ptDinClb);
//        String ptDiscover = "^6(?:011|5[0-9]{2})[0-9]{3,}$";
//        listOfPattern.add(ptDiscover);
//        String ptJcb = "^(?:2131|1800|35[0-9]{3})[0-9]{3,}$";
//        listOfPattern.add(ptJcb);
    }

    public static boolean validateCreditCard(String cardNumber, EditText editText,String msg){
        addCreditCardPattern();
        String card = cardNumber.replace("-","");
        for(String p:listOfPattern){
            if(card.matches(p)){
                return true;
            }
        }
        editText.setError(msg);
        return false;
    }

    //Method to validate EditText
    public static boolean validateField(EditText editText, String msg) {
        if (editText.getText().toString().length() == 0) {
            editText.setError(msg);
            editText.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean validateField(TextView textView, String msg) {
        if (textView.getText().toString().length() == 0) {
            textView.setError(msg);
            textView.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean validatePasswords(EditText editText, String msg) {
        if (editText.getText().toString().length() == 0) {
            editText.setError(msg);
            editText.requestFocus();
            return false;
        }
        return true;
    }

    public static void formatCreditCard(Editable e){
        if (!isInputCorrect(e, CARD_NUMBER_TOTAL_SYMBOLS, CARD_NUMBER_DIVIDER_MODULO, CARD_NUMBER_DIVIDER)) {
            e.replace(0, e.length(), concatString(getDigitArray(e, CARD_NUMBER_TOTAL_DIGITS), CARD_NUMBER_DIVIDER_POSITION, CARD_NUMBER_DIVIDER));
        }
    }

    public static void formatDate(Editable e){
        if (!isInputCorrect(e, CARD_DATE_TOTAL_SYMBOLS, CARD_DATE_DIVIDER_MODULO, CARD_DATE_DIVIDER)) {
            e.replace(0, e.length(), concatString(getDigitArray(e, CARD_DATE_TOTAL_DIGITS), CARD_DATE_DIVIDER_POSITION, CARD_DATE_DIVIDER));
        }
    }

    private static boolean isInputCorrect(Editable s, int size, int dividerPosition, char divider) {
        boolean isCorrect = s.length() <= size;
        for (int i = 0; i < s.length(); i++) {
            if (i > 0 && (i + 1) % dividerPosition == 0) {
                isCorrect &= divider == s.charAt(i);
            } else {
                isCorrect &= Character.isDigit(s.charAt(i));
            }
        }
        return isCorrect;
    }

    private static String concatString(char[] digits, int dividerPosition, char divider) {
        final StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < digits.length; i++) {
            if (digits[i] != 0) {
                formatted.append(digits[i]);
                if ((i > 0) && (i < (digits.length - 1)) && (((i + 1) % dividerPosition) == 0)) {
                    formatted.append(divider);
                }
            }
        }

        return formatted.toString();
    }

    private static char[] getDigitArray(final Editable s, final int size) {
        char[] digits = new char[size];
        int index = 0;
        for (int i = 0; i < s.length() && index < size; i++) {
            char current = s.charAt(i);
            if (Character.isDigit(current)) {
                digits[index] = current;
                index++;
            }
        }
        return digits;
    }

    public static boolean validateField(EditText editText, TextView textView, String msg) {
        if (editText.getText().toString().length() == 0) {
            textView.setText(msg);
            textView.setVisibility(View.VISIBLE);
            editText.requestFocus();
            return false;
        }
        return true;
    }

    //Method to Validate Email
    public static boolean validateEmail(EditText editText, String msg) {
        if (editText.getText().toString().trim().isEmpty()) {
            editText.setError(msg);
            return false;
        } else if (!validateEmailPattern(editText.getText().toString())) {
            editText.setError("Invalid email address");
            return false;
        }
        return true;
    }

    public static boolean validateContactNo(EditText editText, int validLength) {
        if (editText.getText().toString().trim().isEmpty()) {
            return false;
        } else if (editText.getText().toString().length() != validLength) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean validateContactNo(EditText editText) {
       return validateContactNo(editText,10);
    }

    public static boolean validateSLNICNo(EditText editText) {
        if (editText.getText().toString().trim().isEmpty()) {
            return false;
        } else if (editText.getText().toString().length() == 10) {
            if (editText.getText().toString().contains("V") || editText.getText().toString().contains("v")) {
                if (editText.getText().toString().toUpperCase().lastIndexOf("V") == 9) {
                    return true;
                } else {
                    return false;
                }


            } else if (editText.getText().toString().contains("X") || editText.getText().toString().contains("x")) {
                if (editText.getText().toString().toUpperCase().lastIndexOf("X") == 9) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }

        } else if (editText.getText().toString().length() == 12) {
            try {
                Double.parseDouble(editText.getText().toString());
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    //Method that checks email pattern
    private static boolean validateEmailPattern(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
