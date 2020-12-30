 package com.example.twomintimer;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Timer;
import java.util.TimerTask;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    MyCountDownTimer myCDT;
    MediaPlayer player;
    //DEFAULT_RINGTONE_URI with DEFAULT_NOTIFICATION_URI or DEFAULT_ALARM_ALERT_UR
    TextView display;
    TextView intervalProgress;
    Button btn_start;
    private boolean timerTicking = false, timerOn = false;

    NumberPicker picker;
    //values for scroll wheel
    private static final String[] numValues = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    int pickerVal;
    int currentCycle = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_start = (Button)findViewById(R.id.startButt);
        display = (TextView)findViewById(R.id.timeDisplay);
        intervalProgress = (TextView)findViewById(R.id.progessNum);
        if (Settings.System.DEFAULT_RINGTONE_URI != null){
            player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI); // loads default sound to play
        }
        else{
            player = MediaPlayer.create(this, Settings.System.DEFAULT_NOTIFICATION_URI);
        }
        player = MediaPlayer.create(this, Settings.System.DEFAULT_NOTIFICATION_URI);



        //number picker initialization of values
        picker = (NumberPicker)findViewById(R.id.numPicker);

        picker.setMinValue(0);
        picker.setMaxValue(numValues.length-1);
        picker.setDisplayedValues(numValues);
        picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

    }
    //Queries
    private int getPickerVal(){
        int pickerVal = Integer.parseInt(numValues[picker.getValue()]);
        return pickerVal;
    }

    private Boolean isInLoop(){
        return this.currentCycle < this.getPickerVal();
    }

    //Commands
    private void updateProgress(){
        String progText = currentCycle +"/"+ this.getPickerVal();
        intervalProgress.setText(progText);
    }
    public void buttonStart(View v) {
            startTimer();

    }
    private void startTimer(){//timer behaviour
        if (timerOn == false) {
            myCDT = new MyCountDownTimer(120000, 100);
            myCDT.start();
            timerOn = true;
            timerTicking = true;
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

            timeText = minUntilFin + ":" + modSecRemaining;
            display.setText(timeText);
        }
        @Override
        public void onFinish(){
            finishingSound();
            display.setText("0:00");
            timerOn = false;
            timerTicking = false;
            updateProgress();
            if (isInLoop()){
                startTimer();
            }
            else{
                currentCycle = 0;
                btn_start.setText("Start Timer");
            }
        }
        public void finishingSound(){
            int endAt = 7000; // time interval to play audio
            Runnable stopPlayerTask = new Runnable() {
                // defines task to run by delayed handler
                @Override
                public void run() {
                    player.pause();
                }
            };
            player.start();
            Handler myHandler = new Handler();
            myHandler.postDelayed(stopPlayerTask, endAt);
        }
        public void finishingNotifSound(){
            for (int i = 0; i < 3; i++){
                player.start();
            }
        }

    }

}