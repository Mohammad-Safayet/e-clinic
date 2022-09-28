package self.safayet.e_medical_chamber.domain.repository

import androidx.lifecycle.LiveData
import self.safayet.e_medical_chamber.database.AlarmDao
import self.safayet.e_medical_chamber.domain.model.Alarm

interface AlarmRepository {
    val alarms: LiveData<List<Alarm>>

    suspend fun addAlarm(alarm: Alarm)

    suspend fun deleteAlarm(alarm: Alarm)
}