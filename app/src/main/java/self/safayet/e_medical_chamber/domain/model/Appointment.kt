package self.safayet.e_medical_chamber.domain.model

import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
data class Appointment (
    var patientId: String = "",
    var doctorId: String = "",
    var name: String = "",
    var contact: String = "",
    var symptoms: List<String> = listOf(),
    var time: String = "",
    var prescription: Prescription = Prescription(""),
    var isActive: Boolean = true,
)