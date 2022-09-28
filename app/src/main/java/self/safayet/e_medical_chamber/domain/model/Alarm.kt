package self.safayet.e_medical_chamber.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import self.safayet.e_medical_chamber.utils.Constants.ALARM_ENTITY_NAME

@Entity(tableName = ALARM_ENTITY_NAME)
data class Alarm(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val medicineName: String,
    val time: String,
    val days: Int,
    val isActive: Boolean
)
