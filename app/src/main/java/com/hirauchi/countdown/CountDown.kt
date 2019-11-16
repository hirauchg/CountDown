package com.hirauchi.countdown

import android.os.CountDownTimer

interface OnCountDownListener {
    fun onFinish()
    fun onTick(millisUntilFinished: Long)
}

class CountDown(millisInFuture: Long, countDownInterval: Long, val mListener: OnCountDownListener) : CountDownTimer(millisInFuture, countDownInterval) {

    override fun onFinish() {
        mListener.onFinish()
    }

    override fun onTick(millisUntilFinished: Long) {
        mListener.onTick(millisUntilFinished)
    }
}
