package self.safayet.e_medical_chamber.database

import androidx.lifecycle.LiveData
import androidx.room.*
import self.safayet.e_medical_chamber.domain.model.Alarm
import self.safayet.e_medical_chamber.utils.Constants.ALARM_ENTITY_NAME

@Dao
interface AlarmDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addAlarm(alarm: Alarm)

    @Delete
    fun deleteAlarm(alarm: Alarm)

    @Query("SELECT * FROM $ALARM_ENTITY_NAME ORDER BY id ASC")
    fun getAllAlarms(): LiveData<List<Alarm>>
}