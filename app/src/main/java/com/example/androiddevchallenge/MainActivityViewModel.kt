/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.CountDownTimer
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {

    val isDarkTheme = mutableStateOf(false)

    val hour = mutableStateOf(0L)
    val minute = mutableStateOf(1L)
    val seconds = mutableStateOf(0L)
    val milliSeconds = mutableStateOf(0L)

    val timerState = mutableStateOf(TimerState.Reset)
    val editState = mutableStateOf(true)
    val undonePercent = mutableStateOf(0F)

    private var totalMillis = 0L

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

        if (hour.value == 0L && minute.value == 0L && seconds.value == 0L && milliSeconds.value == 0L) {
            // go to reset state
            timerState.value = TimerState.Reset
            editState.value = true
            return
        }

        // count total number also
        // if in restart , means start a new timer,
        // if in pause state, play it
        // if playing, pause it
        when (timerState.value) {
            TimerState.Play -> {
                pauseTimer()
            }
            TimerState.Pause -> {
                startTimerWith(60 * 60 * 1000 * hour.value + 60 * 1000 * minute.value + 1000 * seconds.value + milliSeconds.value)
                timerState.value = TimerState.Play
                editState.value = false
            }
            TimerState.Reset -> {
                totalMillis = 60 * 60 * 1000 * hour.value + 60 * 1000 * minute.value + 1000 * seconds.value + milliSeconds.value
                startTimerWith(totalMillis)
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

        totalMillis = 0

        timerState.value = TimerState.Reset
        editState.value = true
        undonePercent.value = 0f
    }

    var tempMinute = 0L
    var tempSeconds = 0L
    private fun startTimerWith(millis: Long) {
        timer?.cancel()
        timer = object : CountDownTimer(millis, 1) {
            override fun onTick(millisUntilFinished: Long) {
                // set in mutable state
                milliSeconds.value = millisUntilFinished % 1000 // milliseconds
                tempSeconds = millisUntilFinished / 1000 // seconds + minute + hour
                tempMinute = tempSeconds / 60 // minutes+hour
                hour.value = tempMinute / 60 // hour
                minute.value = tempMinute % 60
                seconds.value = tempSeconds % 60

                // set sweep angle
                undonePercent.value = millisUntilFinished / totalMillis.toFloat()
            }

            override fun onFinish() {
                resetTimer()
            }
        }
        timer?.start()
    }

    fun destroyTimer() {
        timer?.cancel()
        timer = null
    }
}
