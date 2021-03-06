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

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.views.CircularImageButton
import com.example.androiddevchallenge.views.ImageButton
import com.example.androiddevchallenge.views.Picker

class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme(darkTheme = viewModel.isDarkTheme.value) {
                MyApp(backgroundColor = MaterialTheme.colors.background)
            }
        }
    }

    @ExperimentalAnimationApi
    @Preview("Light Theme", widthDp = 360, heightDp = 640)
    @Composable
    fun LightPreview() {
        MyTheme {
            MyApp()
        }
    }

    @ExperimentalAnimationApi
    @Preview("Dark Theme", widthDp = 360, heightDp = 640)
    @Composable
    fun DarkPreview() {
        MyTheme(darkTheme = true) {
            MyApp(backgroundColor = MaterialTheme.colors.background)
        }
    }

    // Start building your app here!
    @ExperimentalAnimationApi
    @Composable
    fun MyApp(backgroundColor: Color = Color.White) {
        ConstraintLayout(
            constraintSet = ConstraintSet {
                val bottomRowSet = createRefFor("button_row")
                val timerSet = createRefFor("timer_wrapper")
                val pickerSet = createRefFor("picker_wrapper")
                val themeSet = createRefFor("theme_button")
                constrain(bottomRowSet) {
                    bottom.linkTo(parent.bottom, 16.dp)
                    start.linkTo(parent.start, 8.dp)
                    end.linkTo(parent.end, 8.dp)
                }
                constrain(timerSet) {
                    top.linkTo(parent.top)
                    bottom.linkTo(pickerSet.top)
                    start.linkTo(parent.start, 8.dp)
                    end.linkTo(parent.end, 8.dp)
                }
                constrain(pickerSet) {
                    start.linkTo(parent.start, 8.dp)
                    end.linkTo(parent.end, 8.dp)
                    top.linkTo(timerSet.bottom)
                    bottom.linkTo(bottomRowSet.top)
                }
                constrain(themeSet) {
                    top.linkTo(parent.top, 8.dp)
                    end.linkTo(parent.end, 8.dp)
                }
            },
            modifier = Modifier
                .background(color = backgroundColor)
                .fillMaxSize()
        ) {
            ImageButton(
                imageVector = if(viewModel.isDarkTheme.value) Icons.Sharp.WbSunny else Icons.Sharp.ModeNight,
                contentDescription = "theme",
                size = 20.dp,
                modifier = Modifier.layoutId("theme_button")
            ) {
                viewModel.toggleTheme()
            }

            Text(
                "Timer",
                style = TextStyle(color = MaterialTheme.colors.onPrimary, fontSize = 20.sp),
                modifier = Modifier.padding(8.dp)
            )

            TimerCounter(modifier = Modifier.layoutId("timer_wrapper"), size = 250.dp)

            RowScope.AnimatedVisibility(
                visible = viewModel.editState.value,
                modifier = Modifier.layoutId("picker_wrapper")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Picker(range = 0..99, indicator = "h", valueState = viewModel.hour)
                    Text(":", style = TextStyle(color = MaterialTheme.colors.onPrimary))
                    Picker(range = 0..99, indicator = "m", valueState = viewModel.minute)
                    Text(":", style = TextStyle(color = MaterialTheme.colors.onPrimary))
                    Picker(range = 0..99, indicator = "s", valueState = viewModel.seconds)
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .layoutId("button_row")
                    .fillMaxWidth()
            ) {
                ImageButton(
                    imageVector = Icons.Sharp.Refresh,
                    contentDescription = "reset",
                    size = 20.dp,
                    enabled = viewModel.timerState.value != TimerState.Reset
                ) {
                    viewModel.resetTimer()
                }
                CircularImageButton(
                    imageVector = if (viewModel.timerState.value == TimerState.Play) Icons.Sharp.Pause else Icons.Sharp.PlayArrow,
                    size = 75.dp,
                    onClick = {
                        viewModel.toggleTimeState()
                    }
                )
                Spacer(modifier = Modifier.width(20.dp))
            }
        }
    }

    @Preview
    @Composable
    fun TimerCounter(modifier: Modifier = Modifier, size: Dp = 150.dp) {
        Box(contentAlignment = Alignment.Center, modifier = modifier) {
            Surface(
                modifier = Modifier.size(size),
                shape = CircleShape,
                elevation = 0.dp,
                color = MaterialTheme.colors.primaryVariant
            ) {}
            Card(
                modifier = Modifier.size(size.times(0.75F)),
                shape = CircleShape,
                elevation = 5.dp
            ) {
                Text(
                    String.format("%02d", viewModel.hour.value) + ":" +
                            String.format("%02d", viewModel.minute.value) + ":" +
                            String.format("%02d", viewModel.seconds.value),
                    modifier = Modifier.wrapContentSize(align = Alignment.Center),
                    style = TextStyle(fontSize = 20.sp)
                )
            }
        }
    }

    override fun onDestroy() {
        viewModel.destroyTimer()
        super.onDestroy()
    }
}