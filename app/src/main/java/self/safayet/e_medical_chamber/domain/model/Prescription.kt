package self.safayet.e_medical_chamber.domain.model

import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
data class Prescription(
    var time: String = "",
    var medicine: List<Medicine> = listOf()
)