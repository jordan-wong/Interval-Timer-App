package com.example.twomintimer;

import android.os.CountDownTimer;

public class Timer extends CountDownTimer {
    public Timer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {

        int progress = (int) (millisUntilFinished/1000);

    }

    @Override
    public void onFinish() {
        //finish();
        System.out.println("hello");
    }
}

