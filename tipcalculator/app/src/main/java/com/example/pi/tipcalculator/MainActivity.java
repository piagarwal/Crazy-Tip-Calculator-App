package com.example.pi.tipcalculator;

import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private static final String TOTAL_BILL = "TOTAL_BILL";
    private static final String CURRENT_TIP = "CURRENT_TIP";
    private static final String BILL_WITHOUT_TIP = "BILL_WITHOUT_TIP";

    EditText billBeforeTipET;
    EditText tipAmountET;
    EditText finalBillET;

    SeekBar tipSeekBar;

    private int[] checklistValues = new int[12];

    CheckBox friendlyCheckBox;
    CheckBox specialCheckBox;
    CheckBox opinionCheckBox;

    RadioGroup availableradiogroup;
    RadioButton availableGoodRadio;
    RadioButton availableOkRadio;
    RadioButton availableBadRadio;

    Spinner problemSpinner;

    Button startChronometerButton;
    Button pauseChronometerButton;
    Button resetChronometerButton;

    Chronometer timeWaitingChronometer;

    long secondsYouWaited = 0;

    TextView timeWaitingTextView;


    private double billbeforetip;
    private double tipamount;
    private double finalbill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      /*  if(savedInstanceState == null){
            billbeforetip = 0.0;
            tipamount = 0.0;
            finalbill = 0.0;
        }else{

            billbeforetip = savedInstanceState.getDouble(TOTAL_BILL);
            tipamount = savedInstanceState.getDouble(CURRENT_TIP);
            finalbill = savedInstanceState.getDouble(BILL_WITHOUT_TIP);

        }*/

        billBeforeTipET = (EditText)findViewById(R.id.billeditText);
        tipAmountET = (EditText)findViewById(R.id.tipEditText);
        finalBillET = (EditText)findViewById(R.id.finalEditText);
        tipSeekBar = (SeekBar)findViewById(R.id.seekBar);

        billBeforeTipET.addTextChangedListener(billBeforeTipListner);

        tipSeekBar.setOnSeekBarChangeListener(tipSeekBarListner);

        friendlyCheckBox = (CheckBox)findViewById(R.id.friendlyCheckBox);
        specialCheckBox = (CheckBox)findViewById(R.id.specialCheckBox);
        opinionCheckBox = (CheckBox)findViewById(R.id.opinionCheckBox);

        setUpIntroCheckboxes();

        availableradiogroup = (RadioGroup)findViewById(R.id.availableRadioGroup);
        availableGoodRadio = (RadioButton)findViewById(R.id.goodRadioButton);
        availableOkRadio = (RadioButton)findViewById(R.id.okRadioButton);
        availableBadRadio =(RadioButton)findViewById(R.id.badRadioButton);

        addChangeListenerToRadios();

        problemSpinner = (Spinner)findViewById(R.id.problemSpinner);

        addItemSelectedListenerToSpinner();

        startChronometerButton = (Button)findViewById(R.id.startChronometerbutton);
        pauseChronometerButton = (Button)findViewById(R.id.pauseChronometerbutton);
        resetChronometerButton = (Button)findViewById(R.id.resetChronometerbutton);

        setButtonOnClickListeners();

        timeWaitingChronometer = (Chronometer)findViewById(R.id.timeWaitingChronometer);

        timeWaitingTextView = (TextView)findViewById(R.id.timeWaitingTextView);

    }

    private TextWatcher billBeforeTipListner = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
           try {
               billbeforetip = Double.parseDouble(s.toString());
           }catch(NumberFormatException e){
               billbeforetip = 0.0;

            }
            updateTipAndFinalBill();

        }



        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    private void updateTipAndFinalBill(){
         tipamount = Double.parseDouble(tipAmountET.getText().toString());
         finalbill = billbeforetip + (billbeforetip*tipamount);
        finalBillET.setText(String.format("%.02f",finalbill));

    }

    private SeekBar.OnSeekBarChangeListener tipSeekBarListner = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            tipamount = (tipSeekBar.getProgress()) * .01;
            tipAmountET.setText(String.format("%.02f",tipamount));
            friendlyCheckBox.setChecked(false);
            specialCheckBox.setChecked(false);
            opinionCheckBox.setChecked(false);
            availableGoodRadio.setChecked(false);
            availableBadRadio.setChecked(false);
            availableOkRadio.setChecked(false);
            updateTipAndFinalBill();
        }


        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

  /*  protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putDouble(TOTAL_BILL,finalbill);
        outState.putDouble(BILL_WITHOUT_TIP,billbeforetip);
        outState.putDouble(CURRENT_TIP,tipamount);
    }*/

    private void setUpIntroCheckboxes(){
        friendlyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checklistValues[0] = (friendlyCheckBox.isChecked())?4:0;
                setTipFromWaitressChecklist();
                updateTipAndFinalBill();
            }
        });

        specialCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checklistValues[1] = (specialCheckBox.isChecked())?1:0;
                setTipFromWaitressChecklist();
                updateTipAndFinalBill();
            }
        });

        opinionCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checklistValues[2] = (opinionCheckBox.isChecked())?2:0;
                setTipFromWaitressChecklist();
                updateTipAndFinalBill();
            }
        });


    }

    private void setTipFromWaitressChecklist(){

        int checkListTotal = 0;
        for(int item : checklistValues){
            checkListTotal += item;
        }

        tipAmountET.setText(String.format("%.02f",checkListTotal*.01));
    }

    private void addChangeListenerToRadios(){
        availableradiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checklistValues[3] = (availableGoodRadio.isChecked())?4:0;
                checklistValues[4] = (availableOkRadio.isChecked())?2:0;
                checklistValues[5] = (availableBadRadio.isChecked())?-1:0;
                setTipFromWaitressChecklist();
                updateTipAndFinalBill();
            }
        });
    }

    private void addItemSelectedListenerToSpinner(){
        problemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checklistValues[6] = (problemSpinner.getSelectedItem().equals("Bad"))?-1:0;
                checklistValues[7] = (problemSpinner.getSelectedItem().equals("Ok"))?3:0;
                checklistValues[8] = (problemSpinner.getSelectedItem().equals("Good"))?6:0;
                setTipFromWaitressChecklist();
                updateTipAndFinalBill();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setButtonOnClickListeners(){
        startChronometerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int stoppedMilliseconds = 0;
                String chronoText = timeWaitingChronometer.getText().toString();
                String array[] = chronoText.split(":");
                if(array.length == 2){
                    stoppedMilliseconds = Integer.parseInt(array[0])* 60 * 1000+  Integer.parseInt(array[1])*1000;
                }else if(array.length == 3){
                    stoppedMilliseconds = Integer.parseInt(array[0])*60 * 60 * 1000+  Integer.parseInt(array[1])*60*1000 + Integer.parseInt(array[1])*1000 ;
                }

                 timeWaitingChronometer.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
                //timeWaitingChronometer.setBase(stoppedMilliseconds);
                secondsYouWaited = Long.parseLong(array[1]);
                updateTipBasedOnTimeYouWaited(secondsYouWaited);
                timeWaitingChronometer.start();
                }
        });

        pauseChronometerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeWaitingChronometer.stop();


            }
        });


        resetChronometerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeWaitingChronometer.setBase(SystemClock.elapsedRealtime());
                secondsYouWaited = 0;
            }
        });
    }

    private void updateTipBasedOnTimeYouWaited(long secondsYouWaited){
        checklistValues[9] = (secondsYouWaited > 10)?-2:2;
        setTipFromWaitressChecklist();
        updateTipAndFinalBill();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("TAG", "Restoring Instance state");

    }
}
