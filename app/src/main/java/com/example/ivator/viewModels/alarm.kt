package com.example.ivator.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.ivator.Alarm

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