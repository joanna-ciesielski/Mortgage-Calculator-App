package com.zybooks.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    // constants used when saving/restoring state
    private static final String PURCHASE_PRICE = "PURCHASE_PRICE";
    private static final String DOWN_PAYMENT = "DOWN_PAYMENT";
    private static final String INTEREST_RATE = "INTEREST_RATE";
    private static final String CUSTOM_DURATION = "CUSTOM_DURATION";

    private double purchasePriceTotal; // purchase price entered by the user
    private double downPaymentTotal; // down payment entered by the user
    private double interestRateTotal; // interest rate entered by the user
    private int customLoanDuration; // custom loan duration set with the SeekBar

    private EditText tenYearEditText; // displays monthly payment for 10 year loan
    private EditText twentyYearEditText; // displays monthly payment for 20 year loan
    private EditText thirtyYearEditText; // displays monthly payment for 30 year loan
    private EditText customYearEditText; // displays monthly payment for custom loan duration
    private EditText loanAmountEditText; // displays loan amount

    private EditText purchasePriceEditText; // accepts user input for purchase price
    private EditText downPaymentEditText; // accepts user input for down payment
    private EditText interestEditText; // accepts user input for interest rate

    private TextView customDurationTextView; // displays custom loan duration in years

    // Called when the activity is first started

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // inflate the GUI
        // check if app just started or is being restored from memory
        if (savedInstanceState == null) // the app just started running
        {
            purchasePriceTotal = 0.0; // initialize purchase price to zero
            downPaymentTotal = 0.0; // initialize down payment to zero
            interestRateTotal = 0.0; // initialize interest rate to zero
            customLoanDuration = 0; // initialize custom loan duration to zero
        } // end if
        else // app is being restored form memory, not executed from scratch
        {
            // initialize variable amounts to saved amounts
            purchasePriceTotal = savedInstanceState.getDouble(PURCHASE_PRICE);
            downPaymentTotal = savedInstanceState.getDouble(DOWN_PAYMENT);
            interestRateTotal = savedInstanceState.getDouble(INTEREST_RATE);
            customLoanDuration = savedInstanceState.getInt(CUSTOM_DURATION);

        }
        // get references to the 10, 20, and 30 year monthly payment, and loan amount text edits
        tenYearEditText = (EditText) findViewById(R.id.tenYearEditText);
        twentyYearEditText = (EditText) findViewById(R.id.twentyYearEditText);
        thirtyYearEditText = (EditText) findViewById(R.id.thirtyYearEditText);
        loanAmountEditText = (EditText) findViewById(R.id.loanAmountEditText);

        // get the TextView displaying thw custom loan duration
        customDurationTextView = (TextView) findViewById(R.id.customDurationTextView);

        // get the custom loan duration monthly payment EditText
        customYearEditText = (EditText) findViewById(R.id.customYearEditText);

        // get the purchasePriceEditText, downPaymentEditText, and interestEditText
        purchasePriceEditText = (EditText) findViewById(R.id.purchasePriceEditText);
        downPaymentEditText = (EditText) findViewById(R.id.downPaymentEditText);
        interestEditText = (EditText) findViewById(R.id.interestEditText);

        // purchasePriceEditTextWatcher, downPaymentEditTextWatcher, and interestEditTextWatcher handle onTextChanged events
       // TextWatcher purchasePriceEditTextWatcher = null;
        purchasePriceEditText.addTextChangedListener(purchasePriceEditTextWatcher);
       // TextWatcher downPaymentEditTextWatcher = null;
        downPaymentEditText.addTextChangedListener(downPaymentEditTextWatcher);
        //TextWatcher interestEditTextWatcher = null;
        interestEditText.addTextChangedListener(interestEditTextWatcher);

        // get the seekBar used to set the custom loan duration
        SeekBar customSeekBar = (SeekBar) findViewById(R.id.customSeekBar);
       // OnSeekBarChangeListener customSeekBarListener = null;
        customSeekBar.setOnSeekBarChangeListener(customSeekBarListener);
    } // end method onCreate

    // updates 10, 20, and 30 year payment, and loan amount text edits
    private void updateStandard()
    {
        // calculate loan amount
        double loanAmount = purchasePriceTotal - downPaymentTotal;

        // calculate monthly payments
        double interest = interestRateTotal/100/12; // interest as monthly percentage
        double monthlyPaymentTen = loanAmount * (interest * Math.pow((1+interest),120)/(Math.pow((1+interest), 120)-1));
        double monthlyPaymentTwenty = loanAmount * (interest * Math.pow((1+interest),240)/(Math.pow((1+interest), 240)-1));
        double monthlyPaymentThirty = loanAmount * (interest * Math.pow((1+interest),360)/(Math.pow((1+interest), 360)-1));

        // set loanAmountEditText to loan amount

        loanAmountEditText.setText(String.format("%.02f", loanAmount));

        // set monthly payment editTexts
        tenYearEditText.setText(String.format("%.02f", monthlyPaymentTen));
        twentyYearEditText.setText(String.format("%.02f", monthlyPaymentTwenty));
        thirtyYearEditText.setText(String.format("%.02f", monthlyPaymentThirty));

    } // end method update standard

    // updates the custom loan duration monthly payment

    private void updateCustom()
    {
        // Set customDurationTextView to match the position of the SeekBar
        customDurationTextView.setText(customLoanDuration + "Years");
        // calculate monthly payments
        double loanAmount = purchasePriceTotal - downPaymentTotal;
        double interest = interestRateTotal/100/12; // interest as monthly percentage
        int customMonths = customLoanDuration * 12;
        double monthlyPaymentCustom = loanAmount * (interest * Math.pow((1+interest),customMonths)/(Math.pow((1+interest), customMonths)-1));

        // display the custom monthly payment amount
        customYearEditText.setText(String.format("%.02f", monthlyPaymentCustom));

    } // end method updateCustom

    // save values of purchasePriceEditText, downPaymentEditText, interestEditText and customSeekBar
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putDouble(PURCHASE_PRICE, purchasePriceTotal);
        outState.putDouble(DOWN_PAYMENT, downPaymentTotal);
        outState.putDouble(INTEREST_RATE, interestRateTotal);
        outState.putInt(CUSTOM_DURATION, customLoanDuration);
    } // end method onSaveInstanceState

    // called when the user changes the position of SeekBar
    public OnSeekBarChangeListener customSeekBarListener =
            new OnSeekBarChangeListener()
            {
                // update currentCustomDuration, then call updateCustom
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser)
                {
                    // sets currentCustomDuration to position of the SeekBar's thumb
                    customLoanDuration = seekBar.getProgress();
                    updateCustom(); // update EditTexts for custom loan duration and monthly payment
                } // end method onProgressChanged

                @Override
                public void onStartTrackingTouch(SeekBar seekBar)
                {
                } // end method onStartTrackingTouch

                @Override
                public void onStopTrackingTouch(SeekBar seekBar)
                {
                } // end method onStopTrackingTouch
            }; // end OnSeekBarChangeListener

    // event-handling object that responds to purchasePriceEditText's events
    public TextWatcher purchasePriceEditTextWatcher = new TextWatcher()
    {
        // called when the user enters a number
        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count)
        {
            // convert purchasePriceEditText's text to a double
            try
            {
                purchasePriceTotal = Double.parseDouble(s.toString());
            } // end try
            catch (NumberFormatException e)
            {
                purchasePriceTotal = 0.0; // default if an exception occurs
            } // end catch

            // update the standard and custom monthly payment EditTexts
            updateStandard(); // update the 10, 20, and 30 years EditTexts
            updateCustom(); // update the custom duration EditTexts
        } // end method onTextChanged

        @Override
        public void afterTextChanged(Editable s)
        {
        } // end method afterTextChanged

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after)
        {
        } // end method beforeTextChanged
    }; // end purchasePriceEditTextWatcher

    // event-handling object that responds to downPaymentEditText's events
    public TextWatcher downPaymentEditTextWatcher = new TextWatcher()
    {
        // called when the user enters a number
        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count)
        {
            // convert downPaymentEditText's text to a double
            try
            {
                downPaymentTotal = Double.parseDouble(s.toString());
            } // end try
            catch (NumberFormatException e)
            {
                downPaymentTotal = 0.0; // default if an exception occurs
            } // end catch

            // update the standard and custom monthly payment EditTexts
            updateStandard(); // update the 10, 20, and 30 years EditTexts
            updateCustom(); // update the custom duration EditTexts
        } // end method onTextChanged

        @Override
        public void afterTextChanged(Editable s)
        {
        } // end method afterTextChanged

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after)
        {
        } // end method beforeTextChanged
    }; // end downPaymentEditTextWatcher

    // event-handling object that responds to interestRateEditText's events
    public TextWatcher interestEditTextWatcher = new TextWatcher()
    {
        // called when the user enters a number
        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count)
        {
            // convert purchasePriceEditText's text to a double
            try
            {
                interestRateTotal = Double.parseDouble(s.toString());
            } // end try
            catch (NumberFormatException e)
            {
                interestRateTotal = 0.0; // default if an exception occurs
            } // end catch

            // update the standard and custom monthly payment EditTexts
            updateStandard(); // update the 10, 20, and 30 years EditTexts
            updateCustom(); // update the custom duration EditTexts
        } // end method onTextChanged

        @Override
        public void afterTextChanged(Editable s)
        {
        } // end method afterTextChanged

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after)
        {
        } // end method beforeTextChanged
    }; // end interestEditTextWatcher



} // end class MainActivity