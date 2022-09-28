package self.safayet.e_medical_chamber.domain.model

import androidx.annotation.Keep

@Keep
data class Patient (
    var id: String = "",
    var email: String = "",
    var fullName: String = "",
    var age: Long = 0,
    var dob: String = "",
    var password: String = "",
    var gender: String = "",
    var appointment: List<Appointment> = listOf()
) {
}