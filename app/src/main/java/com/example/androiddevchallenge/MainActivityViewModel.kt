package com.example.androiddevchallenge

import android.os.CountDownTimer
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {

    val isDarkTheme = mutableStateOf(false)

    val hour = mutableStateOf(0L)
    val minute = mutableStateOf(1L)
    val seconds = mutableStateOf(0L)

    val timerState = mutableStateOf(TimerState.Pause)
    val editState = mutableStateOf(true)

    private var timer: CountDownTimer? = null

    fun toggleTheme() {
        isDarkTheme.value = !isDarkTheme.value
    }

    fun toggleTimeState() {
        if (timerState.value == TimerState.Play) {
            pauseTimer()
        } else {
            startTimer()
        }
    }

    private fun startTimer() {

        if (hour.value == 0L && minute.value == 0L && seconds.value == 0L) {
            //go to reset state
            timerState.value = TimerState.Reset
            editState.value = true
            return
        }


        //count total number also
        // if in restart , means start a new timer,
        //if in pause state, play it
        //if playing, pause it
        when (timerState.value) {
            TimerState.Play -> {
                pauseTimer()
            }
            TimerState.Pause -> {
                startTimerWith(hour.value, minute.value, seconds.value)
                timerState.value = TimerState.Play
                editState.value = false
            }
            TimerState.Reset -> {
                startTimerWith(hour.value, minute.value, seconds.value)
                timerState.value = TimerState.Play
                editState.value = false
            }
        }
    }

    private fun pauseTimer() {
        timerState.value = TimerState.Pause
        timer?.cancel()
        timer = null
    }

    fun resetTimer() {
        timer?.cancel()
        timer = null

        hour.value = 0
        minute.value = 1
        seconds.value = 0

        timerState.value = TimerState.Reset
        editState.value = true
    }

    var tempMinute = 0L
    var tempSeconds = 0L
    private fun startTimerWith(h: Long, m: Long, s: Long) {
        timer?.cancel()
        timer = object : CountDownTimer(s * 1000 + m * 60 * 1000 + h * 60 * 60 * 1000, 1) {
            override fun onTick(millisUntilFinished: Long) {
                //set in mutable state
                tempSeconds = millisUntilFinished / 1000 //seconds + minute + hour
                tempMinute = tempSeconds / 60 //minutes+hour
                hour.value = tempMinute / 60 //hour
                minute.value = tempMinute % 60
                seconds.value = tempSeconds % 60
            }

            override fun onFinish() {
                resetTimer()
            }
        }
        timer?.start()
    }

    fun destroyTimer(){
        timer?.cancel()
        timer = null
    }

}