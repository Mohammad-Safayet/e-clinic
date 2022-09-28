package self.safayet.e_medical_chamber.domain.model

import androidx.annotation.Keep

@Keep
data class Doctor (
    var id: String = "",
    var email: String = "",
    var fullName: String = "",
    var type: String = "",
    var registrationId: String = "",
    var password: String = "",
    var nid: String = "",
    var specialist: String = "",
    var title: String = "",
    var mobileNo: String = "",
    var gender: String = "",
    var desc: String = "",
    var isActive: Boolean = true,
    var appointments: List<Appointment> = listOf()
)