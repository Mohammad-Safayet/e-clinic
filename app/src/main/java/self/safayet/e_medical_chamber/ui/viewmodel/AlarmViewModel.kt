package self.safayet.e_medical_chamber.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import self.safayet.e_medical_chamber.data.repository.AlarmRepositoryImpl
import self.safayet.e_medical_chamber.database.AlarmDatabase
import self.safayet.e_medical_chamber.domain.model.Alarm
import self.safayet.e_medical_chamber.domain.repository.AlarmRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmViewModel(application: Application): AndroidViewModel(application) {
    private val TAG = AlarmViewModel::class.java.toString()

    private lateinit var repository: AlarmRepository
    lateinit var alarms: LiveData<List<Alarm>>

    init {
        try {
            val alarmDao = AlarmDatabase.getDatabase(application).getAlarmDao()
            Log.d(TAG, "init: $alarmDao")
            repository = AlarmRepositoryImpl(alarmDao)

            alarms = repository.alarms
        } catch (e: Exception) {
            Log.e(TAG, "error: ${e.message}")
        }
    }

    fun addAlarm(alarm: Alarm) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addAlarm(alarm)
        }
    }

    fun deleteAlarm(alarm: Alarm) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAlarm(alarm)
        }
    }
}