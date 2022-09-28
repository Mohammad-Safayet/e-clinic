package self.safayet.e_medical_chamber.domain.model

import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
data class Medicine (
    var name: String = "",
    var notes: String = "",
    var morning: Boolean = false,
    var noon: Boolean = false,
    var night: Boolean = false,
)