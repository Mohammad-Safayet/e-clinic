package self.safayet.e_medical_chamber.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import self.safayet.e_medical_chamber.domain.model.Alarm
import self.safayet.e_medical_chamber.utils.Constants.ALARM_ENTITY_NAME

@Database(entities = [Alarm::class], version = 1, exportSchema = false)
abstract class AlarmDatabase : RoomDatabase() {

    abstract fun getAlarmDao(): AlarmDao

    companion object {
        @Volatile
        private var INSTANCE: AlarmDatabase? = null

        fun getDatabase(context: Context): AlarmDatabase {
            val temp = INSTANCE
            if (temp != null) {
                return temp
            }
            Log.d("Alarm", "getDatabase: $INSTANCE")

            try {
                synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AlarmDatabase::class.java,
                        ALARM_ENTITY_NAME,
                    ).build()

                    INSTANCE = instance
                }
            } catch (e: Exception) {
                Log.d("Alarm", "getDatabase: ${e.toString()}")
            }

            return INSTANCE!!
        }
    }
}