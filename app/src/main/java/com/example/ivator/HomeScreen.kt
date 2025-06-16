package com.example.ivator

import android.app.TimePickerDialog
import android.content.Context
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ivator.Components.RateCard
import com.example.ivator.ui.theme.dongleFontFamily
import com.example.ivator.ui.theme.urbanistFontFamily
import com.example.ivator.ui.theme.white
import com.example.ivator.viewModels.DropRateViewModel
import com.example.ivator.viewModels.ServoControlViewModel
import java.time.LocalDateTime


@Composable
fun RateAlertDialog(
    isVisible: Boolean,
    alertMessage: String,
    onDismiss: () -> Unit,
    onStop: () -> Unit,
    onStopSound: () -> Unit
) {
    if (isVisible) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_dialog_alert),
                        contentDescription = "Alert",
                        tint = Color.Red,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Rate Alert!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = alertMessage,
                        fontSize = 16.sp,
                        fontFamily = urbanistFontFamily
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Action buttons in the text area
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Stop IV button (primary action)
                        Button(
                            onClick = onStop,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "STOP IV DRIP",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Stop sound button
                        Button(
                            onClick = onStopSound,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFF9800)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "STOP SOUND ONLY",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Dismiss button
                        Button(
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Gray
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "DISMISS ALERT",
                                color = Color.White
                            )
                        }
                    }
                }
            },
            confirmButton = {
                // Empty since we moved buttons to text area for better layout
            },
            dismissButton = {
                // Empty since we moved buttons to text area for better layout
            },
            containerColor = Color.White
        )
    }
}

// Add these functions to your HomeScreen file or create a separate utility file

class SoundAlertManager(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false

    fun startAlert() {
        if (!isPlaying) {
            try {
                mediaPlayer = MediaPlayer.create(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                mediaPlayer?.isLooping = true
                mediaPlayer?.start()
                isPlaying = true
            } catch (e: Exception) {
                Log.e("SoundAlert", "Error starting alert sound: ${e.message}")
                // Fallback to notification sound
                try {
                    mediaPlayer = MediaPlayer.create(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    mediaPlayer?.isLooping = true
                    mediaPlayer?.start()
                    isPlaying = true
                } catch (ex: Exception) {
                    Log.e("SoundAlert", "Error with fallback sound: ${ex.message}")
                }
            }
        }
    }

    fun stopAlert() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
        }
        mediaPlayer = null
        isPlaying = false
    }

    fun isAlertPlaying(): Boolean = isPlaying
}
// Updated HomeScreen function with alert system
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun HomeScreen(
    servoViewModel: ServoControlViewModel = viewModel(),
    name: String?,
    age: String?,
    gender: String?,
    liquid: String?,
    driprate: String?,
    flowrate: String?,
) {
    val context = LocalContext.current
    var displayedDripRate by rememberSaveable { mutableStateOf(driprate ?: "") }
    var displayedFlowRate by rememberSaveable { mutableStateOf(flowrate ?: "") }
    val showDialog = remember { mutableStateOf(false) }
    val newAlarmTime = remember { mutableStateOf("") }
    val newAlarmReason = remember { mutableStateOf(TextFieldValue("")) }

    // Alert system state
    val showRateAlert = remember { mutableStateOf(false) }
    val alertMessage = remember { mutableStateOf("") }
    val soundAlertManager = remember { SoundAlertManager(context) }

    // Get ViewModel data
    val dropRateViewModel: DropRateViewModel = viewModel()
    val liveDropRate by dropRateViewModel.dropRate.collectAsState()
    val liveFlowRate by dropRateViewModel.flowRate.collectAsState()
    val liquidVolume by dropRateViewModel.liquidVolume.collectAsState()
    val totalVolume by dropRateViewModel.totalVolume.collectAsState()

    // Servo control state
    val servoAngle by servoViewModel.servoAngle.collectAsState()
    val isIVRunning = servoAngle != 90

    // Add the missing alarms state
    val alarms = remember { mutableStateOf<List<Alarm>>(emptyList()) }

    // Alert checking effect
    LaunchedEffect(liveDropRate, liveFlowRate, displayedDripRate, displayedFlowRate, isIVRunning) {
        if (isIVRunning) {
            val currentDropRate = liveDropRate.toDoubleOrNull() ?: 0.0
            val targetDropRate = displayedDripRate.toDoubleOrNull() ?: 60.0
            val currentFlowRate = (liveFlowRate.toDoubleOrNull() ?: 0.0) * 60 // Convert to ml/hr
            val targetFlowRate = (displayedFlowRate.toDoubleOrNull() ?: 1.67) * 60 // Convert to ml/hr

            // Check for rate exceedance (allow 20% tolerance)
            val dropRateExceeded = currentDropRate > targetDropRate * 1.2
            val flowRateExceeded = currentFlowRate > targetFlowRate * 1.2

            if (dropRateExceeded || flowRateExceeded) {
                var message = "Alert: Rate exceeded safe limits!\n\n"

                if (dropRateExceeded) {
                    message += "• Drip Rate: ${String.format("%.0f", currentDropRate)} drops/min\n"
                    message += "  Target: ${String.format("%.0f", targetDropRate)} drops/min\n\n"
                }

                if (flowRateExceeded) {
                    message += "• Flow Rate: ${String.format("%.1f", currentFlowRate)} ml/hr\n"
                    message += "  Target: ${String.format("%.1f", targetFlowRate)} ml/hr\n\n"
                }

                message += "Consider stopping IV drip immediately!"

                alertMessage.value = message
                showRateAlert.value = true
                soundAlertManager.startAlert()
            } else if (showRateAlert.value && !dropRateExceeded && !flowRateExceeded) {
                // Rates are back to normal
                showRateAlert.value = false
                soundAlertManager.stopAlert()
            }
        } else {
            // IV is not running, clear any alerts
            if (showRateAlert.value) {
                showRateAlert.value = false
                soundAlertManager.stopAlert()
            }
        }
    }

    // Cleanup sound when composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            soundAlertManager.stopAlert()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            contentAlignment = Alignment.TopStart
        ) {
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(id = R.drawable.group1),
                contentDescription = null
            )

            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "IVATOR",
                    fontSize = 60.sp,
                    color = white,
                    fontFamily = dongleFontFamily,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 20.dp, end = 0.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_account_circle_24),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(10.dp)
                            .size(60.dp)
                            .clip(CircleShape)
                    )
                    Column {
                        Text(
                            text = "$name",
                            fontSize = 25.sp,
                            color = white,
                            fontFamily = urbanistFontFamily,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 2.dp)
                        )
                        Text(
                            text = "$age | $gender | $liquid",
                            fontSize = 15.sp,
                            fontFamily = urbanistFontFamily,
                            fontWeight = FontWeight(400),
                            modifier = Modifier.padding(top = 1.dp)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        // IV Monitoring Cards
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            RateCard(
                cardHeading = "Drip Rate",
                cardUnit = "drops/min",
                liveDropRate = liveDropRate, // Already in drops/min from ViewModel
                displayedDripRate = displayedDripRate.ifEmpty { "60" }
            )

            RateCard(
                cardHeading = "Flow Rate",
                cardUnit = "ml/hr",
                liveDropRate = String.format("%.1f", (liveFlowRate.toDoubleOrNull() ?: 0.0) * 60), // Convert ml/min to ml/hr
                displayedDripRate = String.format("%.1f", (displayedFlowRate.toDoubleOrNull() ?: 1.67) * 60) // Convert target to ml/hr
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // IV Liquid Status
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 25.dp)
                .shadow(elevation = 5.dp, shape = RoundedCornerShape(10.dp))
                .background(
                    color = Color(0xFfffffff),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 16.dp)
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = "IV Fluid Status",
                    fontSize = 25.sp,
                    fontFamily = urbanistFontFamily,
                    fontWeight = FontWeight(1000),
                    color = Color(0xFF00218E),
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Remaining",
                        fontFamily = urbanistFontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight(600),
                        color = Color(0xFF00218E),
                    )
                    Text(
                        text = "${String.format("%.0f", (totalVolume.toDoubleOrNull() ?: 500.0) - (liquidVolume.toDoubleOrNull() ?: 0.0))}ml / ${totalVolume}ml",
                        fontFamily = urbanistFontFamily,
                        fontSize = 25.sp,
                        color = Color(0xFF1575FB),
                        fontWeight = FontWeight(800),
                        modifier = Modifier.padding(5.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Urine Monitoring (Placeholder for future load cell integration)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 25.dp)
                .shadow(elevation = 5.dp, shape = RoundedCornerShape(10.dp))
                .background(
                    color = Color(0xFFFFF3E0),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 16.dp)
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = "Urine Collection",
                    fontSize = 25.sp,
                    fontFamily = urbanistFontFamily,
                    fontWeight = FontWeight(1000),
                    color = Color(0xFFE65100),
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Collected Volume",
                        fontFamily = urbanistFontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight(600),
                        color = Color(0xFFE65100),
                    )
                    Text(
                        text = "-- ml", // This will be connected to load cell data
                        fontFamily = urbanistFontFamily,
                        fontSize = 25.sp,
                        color = Color(0xFFFF9800),
                        fontWeight = FontWeight(800),
                        modifier = Modifier.padding(5.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        AlarmRow(
            alarms = alarms.value,
            onAddAlarm = { showDialog.value = true }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // IV Control Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    servoViewModel.updateServoAngle(0) // Start IV drip
                },
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp)
                    .padding(end = 8.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "START IV",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Button(
                onClick = {
                    servoViewModel.updateServoAngle(180) // Stop IV drip
                },
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF44336)
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp)
                    .padding(start = 8.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "STOP IV",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        RateAlertDialog(
            isVisible = showRateAlert.value,
            alertMessage = alertMessage.value,
            onDismiss = {
                showRateAlert.value = false
                soundAlertManager.stopAlert()
            },
            onStop = {
                servoViewModel.updateServoAngle(180) // Stop IV drip
                showRateAlert.value = false
                soundAlertManager.stopAlert()
            },
            onStopSound = {
                soundAlertManager.stopAlert() // Stop sound but keep dialog open
            }
        )

        // IV Status Indicator
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "IV Status: ${if (isIVRunning) "RUNNING" else "STOPPED"}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (isIVRunning) Color(0xFF4CAF50) else Color(0xFFF44336),
                fontFamily = urbanistFontFamily
            )
        }

        // Move the dialog outside the main column
        if (showDialog.value) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                AddAlarmDialog(
                    onDismiss = { showDialog.value = false },
                    onAddAlarm = { time, reason ->
                        alarms.value = alarms.value + Alarm(time, reason)
                        showDialog.value = false
                    },
                    newAlarmTime = newAlarmTime,
                    newAlarmReason = newAlarmReason
                )
            }
        }
    }
}

@Composable
fun AlarmRow(
    alarms: List<Alarm>,
    onAddAlarm: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(alarms) { alarm ->
            AlarmCard(alarm)
        }
        item {
            AddAlarmButton(onAddAlarm)
        }
    }
}

@Composable
fun AlarmCard(alarm: Alarm) {
    Box(
        modifier = Modifier
            .size(120.dp, 80.dp)
            .background(Color.LightGray, RoundedCornerShape(8.dp))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = alarm.time,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                text = alarm.reason,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun AddAlarmButton(onAddAlarm: () -> Unit) {
    Box(
        modifier = Modifier
            .size(120.dp, 80.dp)
            .background(Color(0xFF1575FB), RoundedCornerShape(8.dp))
            .clickable { onAddAlarm() }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Add Alarm",
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddAlarmDialog(
    onDismiss: () -> Unit,
    onAddAlarm: (String, String) -> Unit,
    newAlarmTime: MutableState<String>,
    newAlarmReason: MutableState<TextFieldValue>
) {
    val context = LocalContext.current
    val localDateTime = remember { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        mutableStateOf(LocalDateTime.now())
    } else {
        TODO("VERSION.SDK_INT < O")
    }
    }

    val timePickerDialog = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        TimePickerDialog(
            context,
            { _, hourOfDay: Int, minute: Int ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    localDateTime.value = localDateTime.value.withHour(hourOfDay).withMinute(minute)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    newAlarmTime.value = localDateTime.value.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))
                }
            },
            localDateTime.value.hour,
            localDateTime.value.minute,
            true
        )
    } else {
        TODO("VERSION.SDK_INT < O")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Add Alarm") },
        text = {
            Column {
                OutlinedButton(onClick = { timePickerDialog.show() }) {
                    Text(text = if (newAlarmTime.value.isEmpty()) "Pick Time" else newAlarmTime.value)
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = newAlarmReason.value,
                    onValueChange = { newAlarmReason.value = it },
                    label = { Text("Reason") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (newAlarmTime.value.isNotEmpty() && newAlarmReason.value.text.isNotEmpty()) {
                        onAddAlarm(newAlarmTime.value, newAlarmReason.value.text)
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

data class Alarm(val time: String, val reason: String)

class AlarmViewModel : ViewModel() {
    private val _alarms = mutableStateOf<List<Alarm>>(emptyList())
    val alarms: State<List<Alarm>> = _alarms

    private val _isDialogVisible = mutableStateOf(false)
    val isDialogVisible: Boolean get() = _isDialogVisible.value

    fun addAlarm(time: String, reason: String) {
        _alarms.value = _alarms.value + Alarm(time, reason)
    }

    fun showAlarmDialog() {
        _isDialogVisible.value = true
    }

    fun hideAlarmDialog() {
        _isDialogVisible.value = false
    }
}