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
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ModeNight
import androidx.compose.material.icons.sharp.NotificationsNone
import androidx.compose.material.icons.sharp.Pause
import androidx.compose.material.icons.sharp.PlayArrow
import androidx.compose.material.icons.sharp.Refresh
import androidx.compose.material.icons.sharp.WbSunny
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.TextStyle
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
    @Composable
    fun MyApp(backgroundColor: Color = Color.White) {
        ConstraintLayout(
            constraintSet = ConstraintSet {
                val bottomRowSet = createRefFor("button_row")
                val timerSet = createRefFor("timer_wrapper")
                val pickerSet = createRefFor("picker_wrapper")
                val themeSet = createRefFor("theme_button")
                constrain(bottomRowSet) {
                    bottom.linkTo(parent.bottom, 24.dp)
                    start.linkTo(parent.start, 8.dp)
                    end.linkTo(parent.end, 8.dp)
                }
                constrain(timerSet) {
                    top.linkTo(parent.top, 80.dp)
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
                imageVector = if (viewModel.isDarkTheme.value) Icons.Sharp.WbSunny else Icons.Sharp.ModeNight,
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
                ImageButton(
                    imageVector = Icons.Sharp.NotificationsNone,
                    contentDescription = "reset",
                    size = 20.dp
                ) {
                    Toast.makeText(baseContext, "Coming Soon..", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @Composable
    fun TimerCounter(modifier: Modifier = Modifier, size: Dp = 150.dp) {
        Box(contentAlignment = Alignment.Center, modifier = modifier) {
            Surface(
                modifier = Modifier.size(size),
                shape = CircleShape,
                elevation = 0.dp,
                color = MaterialTheme.colors.primaryVariant
            ) {
                ColouredOutline(viewModel.undonePercent.value)
            }
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

    @Composable
    fun ColouredOutline(undonePercent: Float = 0f) {
        Canvas(
            modifier = Modifier,
            onDraw = {
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Green, Color.Yellow, Color.Red
                        ),
                        startY = 0f,
                        endY = size.height
                    ),
                    topLeft = Offset(0f, (1 - undonePercent) * size.height),
                    size = Size(size.width, undonePercent * size.height)
                )
            }
        )
    }

    override fun onDestroy() {
        viewModel.destroyTimer()
        super.onDestroy()
    }
}
