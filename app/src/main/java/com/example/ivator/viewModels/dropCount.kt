package com.example.ivator.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DropRateViewModel : ViewModel() {
    private val _dropRate = MutableStateFlow("0")             // drops/min
    val dropRate: StateFlow<String> = _dropRate

    private val _flowRate = MutableStateFlow("0.0")            // ml/min
    val flowRate: StateFlow<String> = _flowRate

    private val _liquidVolume = MutableStateFlow("0")          // consumed volume in ml
    val liquidVolume: StateFlow<String> = _liquidVolume

    private val _totalVolume = MutableStateFlow("500")         // total volume from Firebase
    val totalVolume: StateFlow<String> = _totalVolume

    private val _remainingVolume = MutableStateFlow("500")     // remaining volume
    val remainingVolume: StateFlow<String> = _remainingVolume

    // Firebase References
    private val dropCountRef = FirebaseDatabase.getInstance().getReference("ir_counter/count")
    private val dropsPerMlRef = FirebaseDatabase.getInstance().getReference("liquid_settings/drops_per_ml")
    private val totalVolumeRef = FirebaseDatabase.getInstance().getReference("liquid_settings/total_volume_ml")

    private var dropsPerMl: Double = 20.0 // Default value
    private var totalVolumeMl: Double = 500.0 // Default value

    // Simple tracking variables
    private var previousDropCount = 0
    private var previousTimestamp = 0L
    private var totalDrops = 0

    // For rate calculation
    private val dropHistory = mutableListOf<Pair<Long, Int>>() // timestamp, drop_count
    private val maxHistorySize = 10 // Keep last 10 readings

    private var updateJob: Job? = null

    init {
        Log.d("DropRateViewModel", "Initializing DropRateViewModel")
        listenToSettings()
        listenToDropCount()
        startPeriodicUpdate()
    }

    private fun startPeriodicUpdate() {
        updateJob = viewModelScope.launch {
            while (true) {
                delay(2000) // Update every 2 seconds
                calculateRates()
            }
        }
    }

    private fun listenToSettings() {
        Log.d("DropRateViewModel", "Setting up settings listeners")

        dropsPerMlRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(Double::class.java) ?: snapshot.getValue(Long::class.java)?.toDouble()
                dropsPerMl = value ?: 20.0
                Log.d("DropRateViewModel", "Drops per ml updated: $dropsPerMl")
                updateVolumes()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DropRateViewModel", "Failed to read drops per ml: ${error.message}")
            }
        })

        totalVolumeRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(Double::class.java) ?: snapshot.getValue(Long::class.java)?.toDouble()
                totalVolumeMl = value ?: 500.0
                _totalVolume.value = totalVolumeMl.toInt().toString()
                Log.d("DropRateViewModel", "Total volume updated: $totalVolumeMl ml")
                updateVolumes()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DropRateViewModel", "Failed to read total volume: ${error.message}")
            }
        })
    }

    private fun listenToDropCount() {
        Log.d("DropRateViewModel", "Setting up drop count listener")

        dropCountRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("DropRateViewModel", "Drop count snapshot received: ${snapshot.value}")

                // Handle different data types
                val currentCount = when (val value = snapshot.value) {
                    is Long -> value.toInt()
                    is Int -> value
                    is Double -> value.toInt()
                    is String -> value.toIntOrNull() ?: 0
                    else -> {
                        Log.e("DropRateViewModel", "Unexpected data type: ${value?.javaClass}")
                        0
                    }
                }

                val currentTime = System.currentTimeMillis()

                Log.d("DropRateViewModel", "Current count: $currentCount, Previous: $previousDropCount")

                // Handle first reading or count reset
                if (previousTimestamp == 0L || currentCount < previousDropCount) {
                    Log.d("DropRateViewModel", "First reading or reset detected")
                    previousDropCount = currentCount
                    previousTimestamp = currentTime
                    totalDrops = currentCount
                } else {
                    // Add new drops to total
                    val newDrops = currentCount - previousDropCount
                    totalDrops += newDrops
                    Log.d("DropRateViewModel", "New drops: $newDrops, Total drops: $totalDrops")
                }

                // Store in history
                dropHistory.add(Pair(currentTime, currentCount))

                // Keep only recent history
                if (dropHistory.size > maxHistorySize) {
                    dropHistory.removeFirst()
                }

                // Update previous values
                previousDropCount = currentCount
                previousTimestamp = currentTime

                // Update volumes and rates
                updateVolumes()
                calculateRates()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DropRateViewModel", "Failed to read drop count: ${error.message}")
            }
        })
    }

    private fun updateVolumes() {
        val consumedMl = totalDrops / dropsPerMl
        val remainingMl = maxOf(0.0, totalVolumeMl - consumedMl)

        _liquidVolume.value = consumedMl.toInt().toString()
        _remainingVolume.value = remainingMl.toInt().toString()

        Log.d("DropRateViewModel", "Volumes updated - Consumed: ${consumedMl}ml, Remaining: ${remainingMl}ml")
    }

    private fun calculateRates() {
        if (dropHistory.size < 2) {
            Log.d("DropRateViewModel", "Not enough data for rate calculation")
            return
        }

        val currentTime = System.currentTimeMillis()

        // Use data from last 30 seconds for calculation
        val recentData = dropHistory.filter { currentTime - it.first <= 30000 }

        if (recentData.size < 2) {
            Log.d("DropRateViewModel", "Not enough recent data")
            return
        }

        val oldestPoint = recentData.first()
        val newestPoint = recentData.last()

        val timeDiffMs = newestPoint.first - oldestPoint.first
        val dropDiff = newestPoint.second - oldestPoint.second

        Log.d("DropRateViewModel", "Rate calculation - Time diff: ${timeDiffMs}ms, Drop diff: $dropDiff")

        if (timeDiffMs > 0 && dropDiff >= 0) {
            // Calculate drops per minute
            val dropsPerMinute = (dropDiff.toDouble() / timeDiffMs) * 60000.0

            // Calculate flow rate in ml per minute
            val flowRatePerMinute = dropsPerMinute / dropsPerMl

            _dropRate.value = if (dropsPerMinute < 0.1) "0" else String.format("%.0f", dropsPerMinute)
            _flowRate.value = if (flowRatePerMinute < 0.01) "0.0" else String.format("%.2f", flowRatePerMinute)

            Log.d("DropRateViewModel", "Rates calculated - ${dropsPerMinute} drops/min, ${flowRatePerMinute} ml/min")
        } else {
            Log.d("DropRateViewModel", "Invalid time or drop difference")
        }
    }

    // Debug function to check current state
    fun getDebugInfo(): String {
        val info = """
            Total Drops: $totalDrops
            Previous Count: $previousDropCount
            History Size: ${dropHistory.size}
            Drops per ML: $dropsPerMl
            Current Drop Rate: ${_dropRate.value} drops/min
            Current Flow Rate: ${_flowRate.value} ml/min
            Consumed Volume: ${_liquidVolume.value} ml
            Remaining Volume: ${_remainingVolume.value} ml
        """.trimIndent()

        Log.d("DropRateViewModel", info)
        return info
    }

    // Reset function for testing
    fun resetCounters() {
        totalDrops = 0
        previousDropCount = 0
        previousTimestamp = 0L
        dropHistory.clear()
        _dropRate.value = "0"
        _flowRate.value = "0.0"
        updateVolumes()
        Log.d("DropRateViewModel", "Counters reset")
    }

    override fun onCleared() {
        super.onCleared()
        updateJob?.cancel()
        Log.d("DropRateViewModel", "ViewModel cleared")
    }
}
class ServoControlViewModel : ViewModel() {
    private val _servoAngle = MutableStateFlow(90) // Default is 90 (stopped)
    val servoAngle: StateFlow<Int> = _servoAngle

    private val _isIVRunning = MutableStateFlow(false)
    val isIVRunning: StateFlow<Boolean> = _isIVRunning

    // Reference to the servo angle in Firebase
    private val servoAngleRef = FirebaseDatabase.getInstance().getReference("servo/angle")

    init {
        listenToServoAngle()
    }

    private fun listenToServoAngle() {
        servoAngleRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(Int::class.java)
                val angle = value ?: 90 // Default to 90 if null
                _servoAngle.value = angle

                // Update running state based on servo position
                // Assuming: 0 = fully open (running), 90 = partially open, 180 = closed (stopped)
                _isIVRunning.value = angle < 90
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ServoControlViewModel", "Error reading servo angle: ${error.message}")
            }
        })
    }

    // Start IV drip - set servo to open position
    fun startIV() {
        updateServoAngle(0) // 0 degrees = fully open
    }

    // Stop IV drip - set servo to closed position
    fun stopIV() {
        updateServoAngle(180) // 180 degrees = fully closed
    }

    // Update the servo angle in Firebase
    fun updateServoAngle(angle: Int) {
        // Ensure angle is within valid range
        val validAngle = angle.coerceIn(0, 180)

        servoAngleRef.setValue(validAngle)
            .addOnSuccessListener {
                _servoAngle.value = validAngle
                _isIVRunning.value = validAngle < 90
                Log.d("ServoControlViewModel", "Servo angle updated to: $validAngle")
            }
            .addOnFailureListener { e ->
                Log.e("ServoControlViewModel", "Error updating servo angle: ${e.message}")
            }
    }

    // Get current IV flow status
    fun getIVStatus(): String {
        return when (_servoAngle.value) {
            in 0..30 -> "RUNNING - High Flow"
            in 31..60 -> "RUNNING - Medium Flow"
            in 61..89 -> "RUNNING - Low Flow"
            90 -> "PAUSED"
            in 91..180 -> "STOPPED"
            else -> "UNKNOWN"
        }
    }
}