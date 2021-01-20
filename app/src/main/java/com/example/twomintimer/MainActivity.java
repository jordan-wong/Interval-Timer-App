 package com.example.twomintimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    MyCountDownTimer myCDT;
    MediaPlayer player;
    //DEFAULT_RINGTONE_URI with DEFAULT_NOTIFICATION_URI or DEFAULT_ALARM_ALERT_UR

    //UI when setting up
    TextView infoSetup;
    TextView infoMin;
    TextView infoSec;
    TextView infoColon;
    ConstraintLayout timePickLay;

    //UI when timer playing
    TextView infoProgress;
    TextView display;
    TextView intervalProgress;

    Button btn_start;
    private boolean timerTicking = false, timerOn = false;

    NumberPicker interv_picker;
    //values for scroll wheel
    private static final String[] numValues = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    int pickerVal;
    int currentCycle = 0;

    NumberPicker length_pick_min;
    private static final String[] minuteValues = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    int minVal;

    NumberPicker length_pick_sec;
    private static final String[] secValues = new String[60];
    int secVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_start = (Button)findViewById(R.id.startButt);
        btn_start.setAlpha(.5f);
        btn_start.setClickable(false);


        //UI when setting up
        infoSetup  = (TextView)findViewById(R.id.infoSettingsText);
        infoMin = (TextView)findViewById(R.id.infoMin);
        infoSec = (TextView)findViewById(R.id.infoSec);
        infoColon = (TextView)findViewById(R.id.colon);
        timePickLay = (ConstraintLayout)findViewById(R.id.timePickConstraint);

        //UI when timer playing
        infoProgress = (TextView)findViewById(R.id.progressText);
        intervalProgress = (TextView)findViewById(R.id.progessNum);
        display = (TextView)findViewById(R.id.timeDisplay);


        player = MediaPlayer.create(this, R.raw.boxing_ring); // loads default sound to play

        //secvals intialization
        for (int i = 0; i < 60; i++){
            //secValues[i] = String.format("%02d", String.valueOf(i));

            secValues[i] = String.format("%02d", i);

            //secValues[i] = String.valueOf(i);
        }
        length_pick_sec = (NumberPicker)findViewById(R.id.numPickerTime_sec);
        length_pick_sec.setMinValue(0);
        length_pick_sec.setMaxValue(secValues.length-1);
        length_pick_sec.setDisplayedValues(secValues);
        length_pick_sec.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        //minuteVals
        length_pick_min = (NumberPicker)findViewById(R.id.numPickerTime_min);
        length_pick_min.setMinValue(0);
        length_pick_min.setMaxValue(minuteValues.length-1);
        length_pick_min.setDisplayedValues(minuteValues);
        length_pick_min.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        length_pick_min.setOnScrollListener(new NumberPicker.OnScrollListener() {

            @Override
            public void onScrollStateChange(NumberPicker numberPicker, int scrollState) {
                if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
                    int value = numberPicker.getValue();
                    switchEnabled();
                }
            }
        });
        length_pick_sec.setOnScrollListener(new NumberPicker.OnScrollListener() {

            @Override
            public void onScrollStateChange(NumberPicker numberPicker, int scrollState) {
                if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
                    int value = numberPicker.getValue();
                    switchEnabled();
                }
            }
        });


        //number picker initialization of values
        interv_picker = (NumberPicker)findViewById(R.id.numPicker);
        interv_picker.setMinValue(0);
        interv_picker.setMaxValue(numValues.length-1);
        interv_picker.setDisplayedValues(numValues);
        interv_picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

//        interv_picker = (NumberPicker)findViewById(R.id.numPicker);
//        interv_picker.setMinValue(0);
//        interv_picker.setMaxValue(numValues.length-1);
//        interv_picker.setDisplayedValues(numValues);
//        interv_picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    }


    //Queries
    private int getPickerVal(){
        int pickerVal = Integer.parseInt(numValues[interv_picker.getValue()]);
        return pickerVal;
    }

    private long getLengthVal(){ //returns value of interval picker in milliseconds
        int mins = Integer.parseInt(minuteValues[length_pick_min.getValue()]);
        int sec = Integer.parseInt(secValues[length_pick_sec.getValue()]);
        int timeLength = (mins * 60 + sec) * 1000;
        return timeLength;
    }

    private Boolean isInLoop(){
        return this.currentCycle < this.getPickerVal();
    }

    //Commands
    private void updateProgress(){
        String progText = currentCycle +"/"+ this.getPickerVal();
        intervalProgress.setText(progText);
    }
    private void switchEnabled(){
        if (length_pick_min.getValue() == 0 && length_pick_sec.getValue() == 0){
            btn_start.setAlpha(.5f);
            btn_start.setClickable(false);
        }
        else{
            btn_start.setAlpha(1f);
            btn_start.setClickable(true);
        }

    }

    public void buttonStart(View v) {
            startTimer();
    }
    private void startTimer(){//timer behaviour
        if (timerOn == false) {
            long totalInterval = this.getLengthVal();
            myCDT = new MyCountDownTimer(totalInterval, 100);
            //myCDT = new MyCountDownTimer(12000, 100);
            myCDT.start();
            timerOn = true;
            timerTicking = true;

            changeVis(timerOn);
            currentCycle += 1;
            btn_start.setText("Stop Timer");

        }
        else{
            if (timerTicking){
                myCDT.cancel();
                timerTicking = false;
                btn_start.setText("Start Timer");
            }
            else{
                myCDT.start();
                timerTicking = true;
                btn_start.setText("Stop Timer");
            }
        }
        updateProgress();
    }

    private void changeVis(Boolean inTimer){

        if (inTimer){
            //UI when setting up
            infoSetup.setVisibility(View.GONE);
            infoMin.setVisibility(View.GONE);
            infoSec.setVisibility(View.GONE);
            infoColon.setVisibility(View.GONE);
            timePickLay.setVisibility(View.GONE);

            //UI when timer playing
            infoProgress.setVisibility(View.VISIBLE);
            intervalProgress.setVisibility(View.VISIBLE);
            display.setVisibility(View.VISIBLE);
        }
        else{
            //UI when setting up
            infoSetup.setVisibility(View.VISIBLE);
            infoMin.setVisibility(View.VISIBLE);
            infoSec.setVisibility(View.VISIBLE);
            infoColon.setVisibility(View.VISIBLE);
            timePickLay.setVisibility(View.VISIBLE);

            //UI when timer playing
            infoProgress.setVisibility(View.GONE);
            intervalProgress.setVisibility(View.GONE);
            display.setVisibility(View.GONE);
        }
    }
    private void finishSound(Boolean full_set){
        Runnable stopPlayerTask = new Runnable() {
            // defines task to run by delayed handler
            @Override
            public void run() {
                player.pause();
            }
        };
        int endAt; // time interval to play audio
        if (full_set == false){
            endAt = 7000;
            player = MediaPlayer.create(this, R.raw.boxing_ring);
        }
        else{
            endAt = 9000; // time interval to play audio
            player = MediaPlayer.create(this, R.raw.bell_ring_long);
        }
        player.start();
        Handler myHandler = new Handler();
        myHandler.postDelayed(stopPlayerTask, endAt);
    }

    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval){
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onTick(long millisUntilFinished){
            long totalSecUntilFin = millisUntilFinished / 1000;

            long minUntilFin = totalSecUntilFin / 60;
            String timeText;
            String modSecRemaining = String.format("%02d", totalSecUntilFin%60);
            //String modSecRemaining = String.valueOf(totalSecUntilFin%60);
            timeText = minUntilFin + ":" + modSecRemaining;
            display.setText(timeText);
        }
        @Override
        public void onFinish(){
            //finishIntervalSound();

            display.setText("0:00");
            timerTicking = false;
            timerOn = false;
            updateProgress();
            if (isInLoop()){
                finishSound(false);
                startTimer();
            }
            else{
                timerOn = false;
                currentCycle = 0;
                btn_start.setText("Start Timer");
                finishSound(true);
                changeVis(timerOn);
            }
        }
        public String notifyFinInterval(String finType){
            return "";
        }

        /*public void finishSetSound(){
            //player = MediaPlayer.create(this, R.raw.bell_ring_long);
            player.start();
        }*/
        
    }

}