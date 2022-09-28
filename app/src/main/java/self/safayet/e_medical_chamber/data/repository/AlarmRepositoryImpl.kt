package self.safayet.e_medical_chamber.data.repository

import androidx.lifecycle.LiveData
import self.safayet.e_medical_chamber.database.AlarmDao
import self.safayet.e_medical_chamber.domain.model.Alarm
import self.safayet.e_medical_chamber.domain.repository.AlarmRepository

class AlarmRepositoryImpl(private val alarmDao: AlarmDao): AlarmRepository {
    override val alarms: LiveData<List<Alarm>> = alarmDao.getAllAlarms()

    override suspend fun addAlarm(alarm: Alarm) {
        alarmDao.addAlarm(alarm)
    }

    override suspend fun deleteAlarm(alarm: Alarm) {
        alarmDao.deleteAlarm(alarm)
    }
}